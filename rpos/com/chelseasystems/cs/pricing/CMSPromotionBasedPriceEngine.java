/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.Map.Entry;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.pos.CMSReduction;
import com.chelseasystems.cr.item.MiscItemManager;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.currency.CurrencyException;


/**
 * put your documentation comment here
 */
public class CMSPromotionBasedPriceEngine extends PromotionBasedPriceEngine {
  private static final long serialVersionUID = -4457008504271082751L;
  private static Hashtable promotions = null;
  private CMSPresaleLineItemDetailPriceEngine presaleLineItemDetailPriceEngine = null;
  private CMSReservationLineItemDetailPriceEngine rsvLineItemDetailPriceEngine = null;
  private CMSConsignmentLineItemDetailPriceEngine csgLineItemDetailPriceEngine = null;
  private CMSNoSaleLineItemDetailPriceEngine noSaleLineItemDetailPriceEngine = null;
  private CMSNoReturnLineItemDetailPriceEngine noReturnLineItemDetailPriceEngine = null;
  private static ConfigMgr itemCfg = new ConfigMgr("item.cfg");
  CMSStore currStore = (CMSStore) AppManager.getCurrent().getGlobalObject("STORE");
  public static List lineItem = null; // array to send lineitems to promotion engine - Himani
  public static Double finalAmount=null; // Himani
  public static POSLineItemDetail[] lineItemDetailArray=null;//Himani
  /**
   * put your documentation comment here
   * @param   CompositePOSTransaction aCompositeTransaction
   */
  public CMSPromotionBasedPriceEngine(CompositePOSTransaction aCompositeTransaction) {
    super(aCompositeTransaction);
    this.setPresaleLineItemDetailPriceEngine(new CMSPresaleLineItemDetailPriceEngine());
    this.setNoSaleLineItemDetailPriceEngine(new CMSNoSaleLineItemDetailPriceEngine());
    this.setNoReturnLineItemDetailPriceEngine(new CMSNoReturnLineItemDetailPriceEngine());
  }
  
  
//setters and getters to send lineitems to promotion engine - Himani
  public static void setLineItem(List<String> s)
  {
	  lineItem=s;
  }
  
  public static List<String> getLineItem()
  {
	  return lineItem;
  }
  
  public static void setLineItemArray(POSLineItemDetail[] s)
  {
	  lineItemDetailArray=s;
  }
  
  public static POSLineItemDetail[] getLineItemArray()
  {
	  return lineItemDetailArray;
  }

  /**
   * put your documentation comment here
   */
  protected void assignLineItemDetailPriceEngines() {
    this.setSaleLineItemDetailPriceEngine(new CMSSaleLineItemDetailPriceEngine());
    this.setReturnLineItemDetailPriceEngine(new CMSReturnLineItemDetailPriceEngine());
    this.setLayawayLineItemDetailPriceEngine(new CMSLayawayLineItemDetailPriceEngine());
    this.setPresaleLineItemDetailPriceEngine(new CMSPresaleLineItemDetailPriceEngine());
    this.setNoSaleLineItemDetailPriceEngine(new CMSNoSaleLineItemDetailPriceEngine());
    this.setNoReturnLineItemDetailPriceEngine(new CMSNoReturnLineItemDetailPriceEngine());
  }

  /**
   * put your documentation comment here
   * @param anId
   * @return
   */
 //Modified by deepika to handle NullPointerException for promotion code
  public IPromotion getPromotionById(String anId) {
	  IPromotion promotion = null;
	try {
		 promotion=(IPromotion)promotions.get(anId);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	}
	return promotion;
  }

  /**
   * put your documentation comment here
   * @param aPromotion
   * @return
   */
  protected IActivatedPromotion createActivationPromotion(IPromotion aPromotion) {
    return new ActivatedPromotion(aPromotion);
  }

  /**
   * put your documentation comment here
   * @param aPromotions
   */
  public static void setPromotions(Hashtable aPromotions) {
    promotions = aPromotions;
  }

  /**
   * put your documentation comment here
   * @param anItem
   */
  protected void removedItem(Item anItem) {
    String itemId = anItem.getId();
    for (Iterator promotionIds = anItem.getPromotionIds(); promotionIds.hasNext(); ) {
      String promotionId = (String)promotionIds.next();
      if (cachedActivatedPromotions.containsKey(promotionId)) {
        IActivatedPromotion activatedPromotion = (IActivatedPromotion)cachedActivatedPromotions.get(
            promotionId);
        if (activatedPromotion != null) {
          POSLineItemGrouping[] groupings = ((CMSCompositePOSTransaction)super.compositeTransaction).
              getLineItemGroupingForItem(anItem);
          if (groupings.length > 1)
            continue;
          if (groupings[0].getLineItemsArray().length > 1)
            continue;
          activatedPromotion.removeItemId(itemId);
          if (activatedPromotion.getItemIdsArray().length == 0)
            cachedActivatedPromotions.remove(promotionId);
        }
      }
    }
  }

  /**
   * put your documentation comment here
   */
  protected void applySettlementDiscount() {
    Discount[] theDiscounts = super.compositeTransaction.getSettlementDiscountsArray();
    if (theDiscounts == null || theDiscounts.length == 0) {
      return;
    }
    for (int i = 0; i < theDiscounts.length; i++) {
      ArmCurrency compositeTotalThresholdAmount = super.compositeTransaction.
          getCompositeTotalThresholdAmount();
      try {
        compositeTotalThresholdAmount = compositeTotalThresholdAmount.subtract(
            getCompositeMiscNetAmount());
      } catch (CurrencyException ce) {}
      ArmCurrency totalAmountOff;
      double netAmount;
      ConfigMgr cfgMgr = new ConfigMgr("discount.cfg");
  	String Rounding_Mode=cfgMgr.getString("ROUNDING_SELLING_PRICE");
      
  	
  	if (Rounding_Mode == null) {
		Rounding_Mode = "false";
	}
      double percentToReduce;
      if (theDiscounts[i].isDiscountPercent()) {
        percentToReduce = Math.min(theDiscounts[i].getPercent(), 1.0D);
        if(compositeTotalThresholdAmount.isApplyTruncation()){
        	if(Rounding_Mode.equalsIgnoreCase("true"))
       	 {
        	totalAmountOff = (new ArmCurrency(compositeTotalThresholdAmount.doubleValue() * percentToReduce)).roundAndTruncate(1);
            	netAmount=roundMode(totalAmountOff.doubleValue(),0);
            	totalAmountOff=new ArmCurrency(netAmount);
       	 }
        	else
        	{
        		totalAmountOff = (new ArmCurrency(compositeTotalThresholdAmount.doubleValue() * percentToReduce)).roundAndTruncate(1);
        	}
        }else{
        	 if(Rounding_Mode.equalsIgnoreCase("true"))
        	 {
        		 totalAmountOff = (new ArmCurrency(compositeTotalThresholdAmount.doubleValue() * percentToReduce));
             	netAmount=roundMode(totalAmountOff.doubleValue(),0);
             	totalAmountOff=new ArmCurrency(netAmount);
        	 }
        	 else
        	 {
        		 totalAmountOff = (new ArmCurrency(compositeTotalThresholdAmount.doubleValue() * percentToReduce));
        	 }
        }
      } else {
    	  if(Rounding_Mode.equalsIgnoreCase("true"))
     	 {
    		  totalAmountOff = new ArmCurrency(Math.min(theDiscounts[i].getAmount().getDoubleValue()
    		            , compositeTotalThresholdAmount.doubleValue()));
          	netAmount=roundMode(totalAmountOff.doubleValue(),0);
          	totalAmountOff=new ArmCurrency(netAmount);
          	percentToReduce = totalAmountOff.doubleValue() / compositeTotalThresholdAmount.doubleValue();
     	 }
    	  else
    	  {
    	  
        totalAmountOff = new ArmCurrency(Math.min(theDiscounts[i].getAmount().getDoubleValue()
            , compositeTotalThresholdAmount.doubleValue()));
        percentToReduce = totalAmountOff.doubleValue() / compositeTotalThresholdAmount.doubleValue();
      }
      }
      applyPromotionAcrossCompositeTransaction(theDiscounts[i], percentToReduce, totalAmountOff);
    }
  }

//Mahesh Nandure : 7 JUNE 2017 : Rounding selling price for discount
  public static double roundMode(double value, int places)
	{
		if (places < 0) throw new IllegalArgumentException();
		
		
		BigDecimal bd = new BigDecimal(value);
		ConfigMgr cfgMgr = new ConfigMgr("discount.cfg");
		String Rounding_Mode_Flag=cfgMgr.getString("ROUNDING_MODE");
		if(Rounding_Mode_Flag !=null){
		if(Rounding_Mode_Flag.equalsIgnoreCase("UP"))
		{
		bd =bd.setScale(places,RoundingMode.UP);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("DOWN"))
		{
			bd =bd.setScale(places,RoundingMode.DOWN);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("FLOOR"))
		{
			bd =bd.setScale(places,RoundingMode.FLOOR);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("CEILING"))
		{
			bd =bd.setScale(places,RoundingMode.CEILING);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("HALF_UP"))
		{
			bd =bd.setScale(places,RoundingMode.HALF_UP);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("HALF_DOWN"))
		{
			bd =bd.setScale(places,RoundingMode.HALF_DOWN);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("HALF_EVEN"))
		{
			bd =bd.setScale(places,RoundingMode.HALF_EVEN);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("UNNECESSARY"))
		{
			bd =bd.setScale(places,RoundingMode.UNNECESSARY);
		}
		}
		
		return bd.doubleValue();
		
	}
//Mahesh Nandure : 7 JUNE 2017 : Rounding selling price for discount
  /**
   * put your documentation comment here
   * @param dis
   * @param aPercentOff
   * @param aTotalAmountOff
   */
  protected void applyPromotionAcrossCompositeTransaction(Discount dis, double aPercentOff
      , ArmCurrency aTotalAmountOff) {
	//Mahesh Nandure : 7 JUNE 2017 : Rounding selling price for discount
	  ConfigMgr cfgMgr = new ConfigMgr("discount.cfg");
	  	String Rounding_Mode=cfgMgr.getString("ROUNDING_SELLING_PRICE");
	  	if(Rounding_Mode==null){
	  		Rounding_Mode="false";
	  	}
	  	double netAmount;
	  	ArmCurrency amountToTakeOff=null;
	  //end Mahesh Nandure : 7 JUNE 2017 : Rounding selling price for discount
    try {
      ArrayList saleAndLayawaylineItemDetails = super.compositeTransaction.
          getSaleAndLayawayLineItemDetailsArrayList();
      if (saleAndLayawaylineItemDetails.size() == 0) {
        return;
      }
      ArmCurrency amountRemaining = aTotalAmountOff;
      for (Iterator iterator = saleAndLayawaylineItemDetails.iterator(); iterator.hasNext(); ) {
        POSLineItemDetail lineItemDetail = (POSLineItemDetail)iterator.next();
        Item item = lineItemDetail.getLineItem().getItem();
        if ((item.isRedeemable())
            || ((lineItemDetail.getLineItem().isMiscItem())
            && !LineItemPOSUtil.isNotOnFileItem(item.getId()))) {
          continue;
        }
        if (iterator.hasNext()) {
        	//Mahesh Nandure : 7 JUNE 2017 : Rounding selling price for discount
        	if(Rounding_Mode.equalsIgnoreCase("true"))
          	 {
           		 amountToTakeOff = lineItemDetail.getNetAmount().multiply(aPercentOff);
        		 netAmount=roundMode(amountToTakeOff.doubleValue(), 0);
        		 amountToTakeOff=new ArmCurrency(netAmount);
          	 }
        	else
        	{
        		 amountToTakeOff = lineItemDetail.getNetAmount().multiply(aPercentOff);
        	}
        	//end Mahesh Nandure : 7 JUNE 2017 : Rounding selling price for discount
          lineItemDetail.doSetNetAmount(lineItemDetail.getNetAmount().subtract(amountToTakeOff));
          CMSReduction reduction = new CMSReduction(amountToTakeOff, dis.getType() + " Discount");
          reduction.doSetDiscount(dis);
          lineItemDetail.doAddReduction(reduction);
          amountRemaining = amountRemaining.subtract(amountToTakeOff);
        } else {
          lineItemDetail.doSetNetAmount(lineItemDetail.getNetAmount().subtract(amountRemaining));
          CMSReduction reduction = new CMSReduction(amountRemaining, dis.getType() + " Discount");
          reduction.doSetDiscount(dis);
          lineItemDetail.doAddReduction(reduction);
        }
      }
    } catch (CurrencyException anException) {
      logCurrencyException("applyPromotionAcrossCompositeTransaction", anException);
    }
  }

  /**
   * put your documentation comment here
   * @param aPresaleLineItemDetailPriceEngine
   */
  protected void setPresaleLineItemDetailPriceEngine(CMSPresaleLineItemDetailPriceEngine
      aPresaleLineItemDetailPriceEngine) {
    presaleLineItemDetailPriceEngine = aPresaleLineItemDetailPriceEngine;
  }

  /**
   * put your documentation comment here
   * @param aCsgLineItemDetailPriceEngine
   */
  protected void setConsignmentLineItemDetailPriceEngine(CMSConsignmentLineItemDetailPriceEngine
      aCsgLineItemDetailPriceEngine) {
    csgLineItemDetailPriceEngine = aCsgLineItemDetailPriceEngine;
  }

  /**
   * put your documentation comment here
   * @param aRsvLineItemDetailPriceEngine
   */
  protected void setReservationLineItemDetailPriceEngine(CMSReservationLineItemDetailPriceEngine
      aRsvLineItemDetailPriceEngine) {
    rsvLineItemDetailPriceEngine = aRsvLineItemDetailPriceEngine;
  }

  /**
   * put your documentation comment here
   * @param anoSaleLineItemDetailPriceEngine
   */
  protected void setNoSaleLineItemDetailPriceEngine(CMSNoSaleLineItemDetailPriceEngine
      anoSaleLineItemDetailPriceEngine) {
    this.noSaleLineItemDetailPriceEngine = anoSaleLineItemDetailPriceEngine;
  }
  
  /**
   * put your documentation comment here
   * @param anoSaleLineItemDetailPriceEngine
   */
  protected void setNoReturnLineItemDetailPriceEngine(CMSNoReturnLineItemDetailPriceEngine
      anoReturnLineItemDetailPriceEngine) {
    this.noReturnLineItemDetailPriceEngine = anoReturnLineItemDetailPriceEngine;
  }

  public ArmCurrency getCompositeMiscNetAmount() throws CurrencyException {
    ConfigMgr itemCfg = new ConfigMgr("item.cfg");    
    ArrayList saleAndLayawaylineItemDetails = super.compositeTransaction.getSaleAndLayawayLineItemDetailsArrayList();
    ArmCurrency currMiscNetAmount = new ArmCurrency(0.0d);
    for (Iterator iterator = saleAndLayawaylineItemDetails.iterator(); iterator.hasNext(); ) {
      POSLineItemDetail lineItemDetail = (POSLineItemDetail)iterator.next();
      Item item = lineItemDetail.getLineItem().getItem();
      if ((item.isRedeemable())
          || ((lineItemDetail.getLineItem().isMiscItem())
          && !LineItemPOSUtil.isNotOnFileItem(item.getId()))) {
        currMiscNetAmount = currMiscNetAmount.add(lineItemDetail.getNetAmount());
      }

    }
    return currMiscNetAmount;
  }  

  protected void clearAllLineItemDetailAmounts() {
      super.clearAllLineItemDetailAmounts();
      clearAllLineItemDetailAmountsForTransaction(((CMSCompositePOSTransaction)super.compositeTransaction).getPresaleTransaction());
      clearAllLineItemDetailAmountsForTransaction(((CMSCompositePOSTransaction)super.compositeTransaction).getNonFiscalNoSaleTransaction());
      clearAllLineItemDetailAmountsForTransaction(((CMSCompositePOSTransaction)super.compositeTransaction).getNonFiscalNoReturnTransaction());
  }

  protected void assignPricing() {
      if(shouldRunPromotionEngine()) {
    	  IActivatedPromotion activatedPromotion;
          IPromotionEngine promotionEngine;
          promotionEngine = null;
          // Commented for best deal calculations - Himani
          /*for(Iterator rankedActivatedPromotions = rankActivatedPromotions();rankedActivatedPromotions.hasNext();promotionEngine.assignPromotionMarkdown(super.compositeTransaction, activatedPromotion)){
            	activatedPromotion = (IActivatedPromotion)rankedActivatedPromotions.next();
                promotionEngine = activatedPromotion.getPromotionEngine();
              }*/
                 
          //saptarshi changes
          CompositePOSTransaction transaction=super.compositeTransaction;
          POSLineItemDetail lineItemDetail;
          ItemThresholdPromotionEngine itemThresholdPromotionEngine = null;
          MultiunitPromotionEngine multiunitPromotionEngine = null;
          String itemId = null;       
          POSLineItemDetail[] lineItemArray;
          // Map to store promotion id and quantity for calculating multiunit - Himani
          Map<String,Integer> PromQty = new HashMap<String,Integer>();
          lineItemArray=transaction.getLineItemDetailsArray(); 
          int curQty=0;
          lineItem = new ArrayList<String>();
       // List to store promotion id and amount for calculating final promotion id to apply - Himani         
          ArrayList<Map<String,Double>> promotionDetailsList= new ArrayList<Map<String,Double>>();
          boolean quitLoop=false;
          for(int k=0;k<lineItemArray.length;k++)
          {
        	  List countPromtion=new ArrayList();
        	  Iterator promotionIdForCount=lineItemArray[k].getLineItem().getItem().getPromotionIds();
        	  while(promotionIdForCount.hasNext()){
				  countPromtion.add(promotionIdForCount.next());
			  }
        	  if(countPromtion.size()>1)
        	  {
        		  quitLoop=true;
        		  break;
        	  }
          }
          String txnType=transaction.getTransactionType();
          POSTransaction transactionType = null;
          if(txnType.contains("SALE"))
        	  transactionType=transaction.getSaleTransaction();
          else if(txnType.contains("RETN"))
        	  transactionType=transaction.getReturnTransaction();
          else if(txnType.contains("LAWI"))
        	  transactionType=transaction.getLayawayTransaction();
          else if(txnType.contains("PRSO"))
        	  transactionType=((CMSCompositePOSTransaction) transaction).getPresaleTransaction();
          else
        	  quitLoop=false;
          
          if(quitLoop==false)
          {
        	  for(Iterator rankedActivatedPromotion = rankActivatedPromotions();rankedActivatedPromotion.hasNext();promotionEngine.assignPromotionMarkdown(super.compositeTransaction, activatedPromotion)){
			      	activatedPromotion = (IActivatedPromotion)rankedActivatedPromotion.next();
			          promotionEngine = activatedPromotion.getPromotionEngine();
			        }
          }
          else{
          
			for (int i = 0; i < lineItemArray.length; i++){
				try {  
				  Map<String, Double> currItemPromoMap= new HashMap<String, Double>();
				  Map<String, Double> promotionDetailsMap= new HashMap<String, Double>();
				  Map<String, Double> bestDealItemMap = new HashMap<String, Double>();
				  //List to identify promotion types of current item - Himani
				  List<String> currPromTypes = new ArrayList<String>(); 
				  lineItemDetail=lineItemArray[i];
				  itemId=lineItemDetail.getLineItem().getItem().getId();
				  int qty=lineItemDetail.getLineItem().getItem().getDefaultQuantity();
				  Iterator promotionId=lineItemDetail.getLineItem().getItem().getPromotionIds();
				  Iterator promotionIdForCount=lineItemDetail.getLineItem().getItem().getPromotionIds();
				  Iterator rankedActivatedPromotions;
				  Iterator rankedActivatedPromotionsFinal;
				//start-default code for where item does not have any promotion
				  List countPromtion=new ArrayList();
				  while(promotionIdForCount.hasNext()){
					  countPromtion.add(promotionIdForCount.next());
				  }
				  if(countPromtion.size() > 0){
			  		lineItem = new ArrayList<String>();
			  		boolean isITTriggerAmount = false;
			  		boolean isITTotalPriceOff = false;
			  		boolean findOnlyITBestDeal = false;
			  		  itemThresholdPromotionEngine = new ItemThresholdPromotionEngine();
			  		  multiunitPromotionEngine = new MultiunitPromotionEngine();
			  		  
				      while(promotionId.hasNext()){
				    	  String promoidOfitem= (String) promotionId.next();    
				    	  CMSPromotion promoItem = (CMSPromotion)getPromotionById(promoidOfitem);
				    	  try{
				    	 if (this.isOnPromotionTimeRange(promoItem)==true){
				    	  rankedActivatedPromotions = rankActivatedPromotions();
				    	  ArmCurrency currAppliedReductionAmt=new ArmCurrency(0.0d);
				    	  
				    	  if(PromQty.get(promoidOfitem)==null)
				    	  {
				    		  curQty=qty;
				    	  }
				    	  else{
				    		  curQty=PromQty.get(promoidOfitem)+qty;
				    	  }
				    	  PromQty.put(promoidOfitem, curQty);
							     
				    	  while(rankedActivatedPromotions.hasNext()) {
				    		  activatedPromotion = (IActivatedPromotion)rankedActivatedPromotions.next();
			               	  promotionEngine = activatedPromotion.getPromotionEngine();                    	 
			               	  String promoid=activatedPromotion.getId();
			               	  Double markdown=0.0;
			               	  
			               	  if((promoItem.getId()).equalsIgnoreCase(promoid)){ // matching the promotionid of item and active promo
			               		 //checking  if id is IT or MU. We can add "else if" if any other promotion type is introduced.
			               		  if((promoItem.getPromotionEngine().getClass()).equals(itemThresholdPromotionEngine.getClass())){
			               			// code to calculate markdown for IT		  
			               			  try{
			               				  ItemThresholdPromotion promotion = (ItemThresholdPromotion) activatedPromotion.getPromotion();
			               				  ArmCurrency totalOnTransaction = new ArmCurrency(0.0d);
			               				  ArmCurrency checkZero = new ArmCurrency(0.0d);
			               				  double percentAmountOff = 0.0d;
			               				  ArmCurrency currRetail = lineItemDetail.getLineItem().getItemRetailPrice();
			               				  if(currRetail.lessThan(promotion.getTriggerAmount()))
			               				  {
			               					  isITTriggerAmount=true;
			               				  }
			               				  
			               				List detailList = new ArrayList();
			               				detailList=createGrouping(transactionType,activatedPromotion);
			               				
			               				if(detailList.size() > 0)
			               				{
			               					POSLineItemDetail[] lineItemDetailArray = (POSLineItemDetail[]) detailList.toArray(new POSLineItemDetail[0]);
			               					for (int k = 0; k < lineItemDetailArray.length; k++) {
			               						ArmCurrency retail = lineItemDetailArray[k].getLineItem().getItemRetailPrice();
			               						totalOnTransaction = totalOnTransaction.add(retail);
			               					}
			               				}
			               				  if (totalOnTransaction.greaterThanOrEqualTo(promotion.getTriggerAmount())) {
			               					  if (promotion.isReductionByTotalPriceOff()) {
			               						  int promotionCounts = calculateNumberOfTriggers(transactionType, lineItemArray, promotion, totalOnTransaction);
			               						  ArmCurrency totalReductionAmt = promotion.getReductionAmount().multiply(promotionCounts);
			               						  percentAmountOff = totalReductionAmt.doubleValue() / totalOnTransaction.doubleValue();
			               				
			               						  currAppliedReductionAmt = calculateItemThresholdMarkdown(lineItemDetail, promotion, percentAmountOff);
			               						  isITTotalPriceOff=true;
			               					                       					
			               					  }else {
			               						  //issue resolved for Europe- Promotion discount rounding for items 
			               						  if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
			               							  currAppliedReductionAmt = calculateItemThresholdMarkdownforEUR(lineItemDetail, promotion, percentAmountOff);                       						
			               						  }else{
			               							  currAppliedReductionAmt = calculateItemThresholdMarkdown(lineItemDetail, promotion, percentAmountOff);
			               						  }
			               					  }//End Else
			               				  }//End if total on transaction
			               			  } // End Try
			               			  catch(CurrencyException e)
			               			  {
			               				  System.out.println("Currency Exception");
			               			  }
			               			  catch(Exception e)
			               			  {
			               				  e.printStackTrace();
			               			  } 
			               			  markdown= currAppliedReductionAmt.doubleValue();
			               			  currPromTypes.add("IT");
			          				  
			               		  }//End of IT if condition
			               		  else if((promoItem.getPromotionEngine().getClass()).equals(multiunitPromotionEngine.getClass())){
			                    	//code to calculate markdown for Multiunit 
			               			  MultiunitPromotion promotion = (MultiunitPromotion)activatedPromotion.getPromotion();
			               			  int quanityBreak = promotion.getQuantityBreak();                        			   
			               			  int currQty=PromQty.get(promoidOfitem);
			               			  if(currQty>1){                                                                                        
			               				  if((currQty==quanityBreak || (currQty % quanityBreak==0))){    
			               					String[] itemIds = activatedPromotion.getItemIdsArray();
			                   				
			               					List detailList = new ArrayList();
			              			        detailList=createGrouping(transactionType,activatedPromotion);
			              			        int totalItems=transaction.getCompositeTotalQuantityOfItems();
				               				if(detailList.size() > 0)
				               				{
				               					if(currQty!=detailList.size() && totalItems!=detailList.size())
				               					{
				               						currQty=detailList.size();
				               					}
				               					POSLineItemDetail[] lineItemDetailArray = (POSLineItemDetail[]) detailList.toArray(new POSLineItemDetail[0]);
				               					if((currQty==quanityBreak || (currQty % quanityBreak==0))){
				               						for(int beginIndex = 0; beginIndex + quanityBreak <= currQty;beginIndex += quanityBreak){
				               							try
				               							{
				               								currAppliedReductionAmt=calculateMultiUnitMarkdown(lineItemDetailArray, beginIndex, promotion);
				               							}
				               							catch(CurrencyException ce)
				               							{
				               								System.out.println ((new StringBuilder()).append("CurrencyException: ").append(ce).toString());
				               								ce.printStackTrace();
				               								beginIndex += quanityBreak;
				               							}                       						
				               						}  //End For  
				               					}// end if Qntybrk again
				               				}//end if detailList
			               				  }//End if QntyBreak
			               			  }// End if CurQty                       			                        				                      			
			               			  markdown = currAppliedReductionAmt.doubleValue();
			               			currPromTypes.add("MU");
			               		  }//End of if MU condition
			               		  //entry at Map for one item-promotion details          		  
			               			  currItemPromoMap.put(promoid, markdown);
			               	  }// End if promotion id matches with activated Promotions  
				    	  	}//End of While activated Promotions.next
				    	  }// End if expired
				    	  }//End Try
				    	  catch(Exception e){e.printStackTrace();}
				      }//End of While Promotions Id.next                	 		      		 
			  	       
			     //we need a sorted map with markdown details 
			  		// Start: Find out whether there is any Multiunit promotion - Himani  		
			  		
			  		boolean isAllMU = checkIsAllMU(currPromTypes);
			  		
			  		if(isAllMU==false)
			  		{
			  			for(String key: currItemPromoMap.keySet())
			  			{
			  				if((currItemPromoMap.get(key)).equals(0.0))
			  				{
			  					currItemPromoMap.remove(key);
			  					currPromTypes.remove("MU");
			  					break;
			  				}
			  			}
			  		}
			  		
			  		boolean isAllIT = checkIsAllIT(currPromTypes);
			  		
			  		if(isAllIT==true)
			  		{
			  			if(isITTotalPriceOff==false && isITTriggerAmount==false)
			  			{
			  				findOnlyITBestDeal=true;
			  			}
			  		}
			  		
			  		String finalPromoId = calculateBestDeal(promotionDetailsList,promotionDetailsMap,currItemPromoMap,findOnlyITBestDeal);
			  	  	promotionDetailsList.add(promotionDetailsMap);
			  	  	promotionEngine=applyBestDeal(lineItemArray,i,findOnlyITBestDeal,currItemPromoMap,finalPromoId,promotionEngine);
			  	  	
			  		/*if(lineItemDetail.getDealId()!=null && lineItemDetail.getDealMarkdownAmount().doubleValue()==0.0)
			  		{
			  			if(promotionEngine instanceof MultiunitPromotionEngine)
			  			{
			  				for(String key:currItemPromoMap.keySet())
			  				{
			  					if(key.equals(lineItemDetail.getDealId()))
			  							continue;
			  					else
			  					{
			  						
			  					}
			  				}
			  			}
			  		}*/
				  }//End count > 0
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						throw new Exception();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			  }//End for loop for Line Item Array
       }// end if quitLoop
      }//End if(shouldRunPromotionEngine)
      
      
      assignPricingForTransaction(super.compositeTransaction.getSaleTransaction(), this.getSaleLineItemDetailPriceEngine());
      assignPricingForTransaction(super.compositeTransaction.getReturnTransaction(), this.getReturnLineItemDetailPriceEngine());
      assignPricingForTransaction(super.compositeTransaction.getLayawayTransaction(), this.getLayawayLineItemDetailPriceEngine());
      assignPricingForTransaction(((CMSCompositePOSTransaction)super.compositeTransaction).getPresaleTransaction(), this.presaleLineItemDetailPriceEngine);
      assignPricingForTransaction(((CMSCompositePOSTransaction)super.compositeTransaction).getNonFiscalNoSaleTransaction(), this.noSaleLineItemDetailPriceEngine);
      assignPricingForTransaction(((CMSCompositePOSTransaction)super.compositeTransaction).getNonFiscalNoReturnTransaction(), this.noReturnLineItemDetailPriceEngine);
      applySettlementDiscount();
      applyThresholdPromotions();
      
  }// End assignPricing()
  
// Start: Added for Best Deal Promotions CR  -Himani
  
  private int calculateNumberOfTriggers(POSTransaction posTransaction, POSLineItemDetail[] lineItemDetailArray, ItemThresholdPromotion promotion, ArmCurrency totalOnTransaction)
			throws CurrencyException {
		int i = calculateNumberOfTriggersByQunatity(posTransaction, lineItemDetailArray, promotion);
		int j = calculateNumberOfTriggersByAmount(posTransaction, lineItemDetailArray, promotion, totalOnTransaction);
		return Math.max(i, j);
	}
  
  private int calculateNumberOfTriggersByQunatity(POSTransaction posTransaction, POSLineItemDetail[] lineItemDetailArray, ItemThresholdPromotion promotion) {
		if (promotion.getTriggerQuantity() <= 0 || lineItemDetailArray.length == 0)
			return 0;
		int count = 0;
		int quantityOnTransaction = posTransaction.getTotalQuantityOfItems();
		for (int i = 0; i < lineItemDetailArray.length; i++) {
			quantityOnTransaction++;
			// quantityOnTransaction -= promotion.getTriggerQuantity();
			if (quantityOnTransaction < 0)
				break;
			count++;
		}
		count = (int) (quantityOnTransaction / promotion.getTriggerQuantity());
		return count;
	}

	private int calculateNumberOfTriggersByAmount(POSTransaction posTransaction, POSLineItemDetail[] lineItemDetailArray, ItemThresholdPromotion promotion, ArmCurrency totalOnTransaction)
			throws CurrencyException {
		if (promotion.getTriggerAmount().doubleValue() <= 0.0 || lineItemDetailArray.length == 0)
			return 0;
		int count = 0;
		count = (int) (totalOnTransaction.doubleValue() / promotion.getTriggerAmount().doubleValue());
		return count;
	}
  
  public ArmCurrency calculateItemThresholdMarkdown(POSLineItemDetail lineItemDetail, ItemThresholdPromotion promotion, double percentToReduce) throws CurrencyException{

		ArmCurrency markDown = null;
		if (promotion.isReductionByFixedUnitPrice()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			if (retail.lessThan(promotion.getReductionAmount()))
				markDown = new ArmCurrency(0.0);
			else
				markDown = retail.subtract(promotion.getReductionAmount());
		} else if (promotion.isReductionByTotalPriceOff()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			markDown = retail.multiply(percentToReduce);
		} else if (promotion.isReductionByUnitPriceOff()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			if (retail.lessThan(promotion.getReductionAmount())) {
				markDown = retail;
			} else {
				markDown = promotion.getReductionAmount();
			}
		} else if (promotion.isReductionByPercentageOff()) {
			ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			ArmCurrency tempMarkdown = lineItemDetail.getLineItem().getItemRetailPrice().multiply(promotion.getReductionPercent());
			if (retail.lessThan(tempMarkdown)) {
				markDown = retail;
			} else {
					markDown = lineItemDetail.getLineItem().getItemRetailPrice().multiply(promotion.getReductionPercent());
			}
		} else {
			System.out.println("Invalid promotion reduction method on " + promotion);
			return new ArmCurrency(0.0d);
		}
		
		return markDown.round();  
  }
  
  public ArmCurrency calculateItemThresholdMarkdownforEUR(POSLineItemDetail lineItemDetail, ItemThresholdPromotion promotion, double percentToReduce) throws CurrencyException{
	  ItemThresholdPromotionEngine itemThresholdPromotionEngine = new ItemThresholdPromotionEngine();
	  ArmCurrency markDown = null;
		//Mahesh Nandure : 02 FEB 2017 : Rounding selling price By promotion for Europe
		
				ConfigMgr cfgMgr = new ConfigMgr("promotion.cfg");
				String Rounding_Mode=cfgMgr.getString("ROUNDING_SELLING_PRICE");
				
				if (Rounding_Mode == null) {
					Rounding_Mode = "false";
				} //end Mahesh Nandure code 02 FEB 2017
				
		if (promotion.isReductionByFixedUnitPrice()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			if (retail.lessThan(promotion.getReductionAmount()))
				markDown = new ArmCurrency(0.0);
			else
				markDown = retail.subtract(promotion.getReductionAmount());
		} else if (promotion.isReductionByTotalPriceOff()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			markDown = retail.multiply(percentToReduce);
		} else if (promotion.isReductionByUnitPriceOff()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			if (retail.lessThan(promotion.getReductionAmount())) {
				markDown = retail;
			} else {
				markDown = promotion.getReductionAmount();
			}
		} else if (promotion.isReductionByPercentageOff()) {
			ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			ArmCurrency tempMarkdown = lineItemDetail.getLineItem().getItemRetailPrice().multiply(promotion.getReductionPercent());
			if (retail.lessThan(tempMarkdown)) {
				markDown = retail;
			} else {
				markDown = lineItemDetail.getLineItem().getItemRetailPrice().multiply(promotion.getReductionPercent());
				//Mahesh Nandure : 02 FEB 2017 : Rounding selling price By promotion for Europe
				if(Rounding_Mode.equalsIgnoreCase("true"))
				{
              double sellingPrice = ItemThresholdPromotionEngine.roundMode(lineItemDetail.getLineItem().getItemRetailPrice().subtract(markDown).doubleValue(), 0);
				//double sellingPrice = Math.floor(lineItemDetail.getLineItem().getItemRetailPrice().subtract(markDown).doubleValue());
				markDown = lineItemDetail.getLineItem().getItemRetailPrice().subtract(new ArmCurrency(sellingPrice));
				}
			}
		} else {
			System.out.println("Invalid promotion reduction method on " + promotion);
			return new ArmCurrency(0.0d);
		}
		
		return markDown.round();
	}
  
public ArmCurrency calculateMultiUnitMarkdown(POSLineItemDetail lineItemDetailArray[], int beginIndex, MultiunitPromotion promotion) throws CurrencyException{
	  
	  

      int quanityBreak = promotion.getQuantityBreak();
      ArmCurrency totalRetail = new ArmCurrency(0.0D);
      for(int i = beginIndex; i < beginIndex + quanityBreak; i++)
          totalRetail = totalRetail.add(lineItemDetailArray[i].getLineItem().getItem().getRetailPrice());

      ArmCurrency totalDiscountPrice = calculateTotalPromotionPrice(promotion, totalRetail);
      ArmCurrency actualTotalMarkDown = totalRetail.subtract(totalDiscountPrice);
      ArmCurrency currentTotalMarkDown = new ArmCurrency(0.0D);
      for(int i = beginIndex; i < beginIndex + quanityBreak; i++)
      {
          ArmCurrency retailPrice = lineItemDetailArray[i].getLineItem().getItem().getRetailPrice();
          double retailDoubleValue = retailPrice.doubleValue();
          double totalRetailDoubleValue = totalRetail.doubleValue();
          double ratio = retailDoubleValue / totalRetailDoubleValue;
          ArmCurrency discount = totalDiscountPrice.multiply(ratio).round();
          ArmCurrency markDown = retailPrice.subtract(discount);
          if(i != beginIndex)
          {
              currentTotalMarkDown = currentTotalMarkDown.add(markDown);
          }
      }
      currentTotalMarkDown=actualTotalMarkDown.subtract(currentTotalMarkDown).round();
     return actualTotalMarkDown;
  
	  
  }

public ArmCurrency calculateTotalPromotionPrice(MultiunitPromotion promotion, ArmCurrency totalRetailPrice)
        throws CurrencyException
    {
        if(promotion.isReductionByFixedTotalPrice())
            return promotion.getReductionAmount();
        if(promotion.isReductionByFixedUnitPrice())
            return promotion.getReductionAmount().multiply(promotion.getQuantityBreak());
        if(promotion.isReductionByTotalPriceOff())
        {
            ArmCurrency totalAmountOff = promotion.getReductionAmount();
            if(totalRetailPrice.greaterThan(totalAmountOff))
                return totalRetailPrice.subtract(promotion.getReductionAmount());
            else
                return new ArmCurrency(0.0D);
        }
        if(promotion.isReductionByUnitPriceOff())
        {
            ArmCurrency totalAmountOff = promotion.getReductionAmount().multiply(promotion.getQuantityBreak());
            if(totalRetailPrice.greaterThan(promotion.getReductionAmount()))
                return totalRetailPrice.subtract(promotion.getReductionAmount());
            else
                return new ArmCurrency(0.0D);
        }
        if(promotion.isReductionByPercentageOff())
        {
            double percentOff = promotion.getReductionPercent();
            if(percentOff > 0.0D && percentOff < 1.0D)
                return totalRetailPrice.multiply(1.0D - percentOff);
            else
                return new ArmCurrency(0.0D);
        } else
        {
            return new ArmCurrency(0.0D);
        }
    }
 

  public String calculateBestDeal(ArrayList<Map<String,Double>> promotionDetailsList,Map<String,Double> promotionDetailsMap,Map<String,Double> currItemPromoMap,boolean isAllIT)
  {
	  String finalPromoId=null;
	  finalAmount=0.0;
	  if(promotionDetailsList.size()==0 || isAllIT==true)
  	  {
  		promotionDetailsMap.putAll(currItemPromoMap);
  		Set<Entry<String, Double>> set = currItemPromoMap.entrySet();
  		List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
  		Collections.sort( list, new Comparator<Map.Entry<String, Double>>()
  		{
       		public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
       		{
       			return ((Comparable<Double>) o2.getValue()).compareTo( o1.getValue() );
       		}
  		} );
  		finalPromoId=list.get(0).getKey();
  		finalAmount=list.get(0).getValue();
  	  }
  	  else
  	  {
  		Double amount=0.0;
		String promId=null;
		boolean match = false;
		Map<String,Double> finalMap = new HashMap<String,Double>();
		for (String key : currItemPromoMap.keySet()) {
			promId=key;
			amount=currItemPromoMap.get(key);
			for(int b=0; b<promotionDetailsList.size();b++){
				for(String key1 : promotionDetailsList.get(b).keySet()){
        			if(key.equals(key1)){
        				amount=amount + promotionDetailsList.get(b).get(key1);
        				match=true;
        				break;
        			}
        		}  
        		//if(match==true)
            	//{
            		//finalMap.put(promId, amount);
            		//match=false;
            	//}
			}
			
			finalMap.put(promId, amount);
  	  	}
		promotionDetailsMap.putAll(currItemPromoMap);
		if(finalMap.size()==0)
		{
			finalMap.putAll(currItemPromoMap);
		}
  		Set<Entry<String, Double>> set = finalMap.entrySet();
  		List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
  		Collections.sort( list, new Comparator<Map.Entry<String, Double>>()
  		{
       		public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
       		{
       			return ((Comparable<Double>) o2.getValue()).compareTo( o1.getValue() );
       		}
  		} );
  		finalPromoId=list.get(0).getKey();
  		finalAmount=list.get(0).getValue();
  		/*for(int b=0; b<promotionDetailsList.size();b++){
			for(String key : promotionDetailsList.get(b).keySet()){
    			if(key.equals(finalPromoId)){
    				continue;
    			}
    			else
    			{
    				if((promotionDetailsList.get(b).get(key)) > finalAmount)
    				{
    					index.add(b);
    				}
    				
    			}
    		} 
		}*/
  	  }
	  return finalPromoId;
  }
  
  public IPromotionEngine applyBestDeal(POSLineItemDetail[] lineItemArray,int i,boolean findOnlyITBestDeal,Map<String,Double> currItemPromoMap, String finalPromoId,IPromotionEngine promotionEngine)
  {
	  	Iterator rankedActivatedPromotionsFinal;
	  	IActivatedPromotion activatedPromotion;
	  	rankedActivatedPromotionsFinal = rankActivatedPromotions();
		while(rankedActivatedPromotionsFinal.hasNext()) {
			activatedPromotion = (IActivatedPromotion)rankedActivatedPromotionsFinal.next();
			if(activatedPromotion.getId().equals(finalPromoId))
			{
				promotionEngine = activatedPromotion.getPromotionEngine(); 
				
				
				if(findOnlyITBestDeal==false)
				{	
				Double totalLineAmt=0.0;
				for(int j=0;j<i;j++)
				{
					Iterator promoId=lineItemArray[j].getLineItem().getItem().getPromotionIds();
					while(promoId.hasNext())
					{
						 String promoidOfitem1= (String) promoId.next();    
	     		    	 CMSPromotion promoItem1 = (CMSPromotion)getPromotionById(promoidOfitem1);
						if(promoItem1!=null && finalPromoId.equals(promoItem1.getId()))
						{
							totalLineAmt+=lineItemArray[j].getDealMarkdownAmount().doubleValue();
							break;
						}
					}
				}
				for(int j=0;j<=i;j++)
				{
					Iterator promoId=lineItemArray[j].getLineItem().getItem().getPromotionIds();
					while(promoId.hasNext())
					{
						 String promoidOfitem1= (String) promoId.next();    
	     		    	 CMSPromotion promoItem1 = (CMSPromotion)getPromotionById(promoidOfitem1);
						if(promoItem1!=null && finalPromoId.equals(promoItem1.getId()))
						{
							Double lineAmt=lineItemArray[j].getDealMarkdownAmount().doubleValue();	
							if(lineAmt!=null && lineAmt!=0.0)
							{
								if(totalLineAmt < finalAmount)
								{
									//lineItem.add(lineItemArray[j].getLineItem().getItem().getId());
									lineItem.add(lineItemArray[j]);
								//k++;
								}
								for(String key: currItemPromoMap.keySet())
								{
									if(key.equals(finalPromoId))
									{
										if(promotionEngine instanceof ItemThresholdPromotionEngine && (lineAmt.equals(currItemPromoMap.get(key).doubleValue())))
										{
											lineItem.add(lineItemArray[j]);
											break;
										}
									}
								}
							}
							else
							{
								//lineItem.add(lineItemArray[j].getLineItem().getItem().getId());
								
								lineItem.add(lineItemArray[j]);
							}
						}
					}
						
				}//end for
				}//end if isallIt
				else
				{
					lineItem.add(lineItemArray[i]);
				}	
					//setLineItem(lineItem);
				setLineItemArray((POSLineItemDetail[])(POSLineItemDetail[])lineItem.toArray(new POSLineItemDetail[0]));
				
				
				//}
				promotionEngine.assignPromotionMarkdown(super.compositeTransaction, activatedPromotion);
				break;
			}
		}// End while promotion engine
		return promotionEngine;
  }
//End: Added for Best Deal Promotions CR  -Himani
  public boolean isOnPromotionTimeRange(CMSPromotion promotion) {
	    Calendar currentTime = Calendar.getInstance();
	    Calendar beginTime = promotion.getBeginTime();
	    Calendar endTime = promotion.getEndTime();
	    if (beginTime.before(currentTime) && endTime.after(currentTime))
	      return true;
	    else
	      return false;
}
 public boolean checkIsAllIT(List<String> currPromTypes)
 {
	 boolean isAllIT=true;
	 for(int k=0;k<currPromTypes.size();k++)
	 {
		if(currPromTypes.get(k).equals("MU"))
		{
			isAllIT = false;
			break;
		}
	}
	 return isAllIT;
 }
	
 public boolean checkIsAllMU(List<String> currPromTypes)
 {
	 boolean isAllMU=true;
	for(int k=0;k<currPromTypes.size();k++)
	{
		if(currPromTypes.get(k).equals("IT"))
		{
			isAllMU = false;
			break;
		}
	}
	return isAllMU;
 }
 
 public List createGrouping(POSTransaction posTransaction,IActivatedPromotion activatedPromotion)
 {
	 List detailList = new ArrayList();
	 String[] itemIds = activatedPromotion.getItemIdsArray();
		for (int k = 0; k < itemIds.length; k++) {
			POSLineItemGrouping grouping = posTransaction.getLineItemGroupingForItemId(itemIds[k]);
			if (grouping != null)
				detailList.addAll(grouping.getLineItemDetailsArrayList());
		}
	 return detailList;
 }
}

