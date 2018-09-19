/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import com.chelseasystems.cr.business.*;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import java.util.*;
import com.chelseasystems.cr.pos.Reduction;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cs.pos.CMSReduction;

import com.chelseasystems.cs.pos.CMSReturnLineItem;

import com.chelseasystems.cr.pos.ReturnLineItem;

import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.pos.CMSReturnLineItemDetail;
import com.chelseasystems.cr.pos.SaleLineItemDetail;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.config.ConfigMgr;


/**
 *
 * <p>Title: LineItemPOSUtil</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class LineItemPOSUtil extends BusinessObject {
  private POSLineItem posLineItem;
  private POSLineItemDetail posLineItemDetail;
  private double dPromotionalMarkdown;
  private double dTotalReduction;
  private Vector vecDiscountPercents;
  private Vector vecDiscountAmounts;
  private Vector vecDiscountReasons;
  private Vector vecDiscounts;
  private Vector vecTxnDiscounts;
  private Vector vecReductionType;
  private final String REASON_MARKDOWN = "MARKDOWN";
  private final String REASON_DISCOUNT = "DISCOUNT";
  private IApplicationManager theAppMgr;
  private static List notOnFileIDs;

  /**
   * Construct LineItemPOSUtil
   * @param posLineItem POSLineItem[]
   */
  public LineItemPOSUtil(POSLineItem posLineItem) {
    this.theAppMgr = theAppMgr;
    vecReductionType = new Vector();
    vecDiscountAmounts = new Vector();
    vecDiscountPercents = new Vector();
    vecDiscountReasons = new Vector();
    vecDiscounts = new Vector();
    vecTxnDiscounts = new Vector();
    this.posLineItem = posLineItem;
    dTotalReduction = 0.0;
    posLineItemDetail = null;
    posLineItemDetail = (posLineItem.getLineItemDetailsArray())[0];
    createDiscounts();
    createTxnDiscounts();
    createReductions();
  }

  /**
   * This method is used to create discount for pos line item
   */
  private void createDiscounts() {
    Discount[] arrayDiscounts = null;
    if (posLineItem instanceof CMSReturnLineItem) {
      SaleLineItem saleLineItem = ((CMSReturnLineItem)posLineItem).getSaleLineItem();
      if (saleLineItem != null)
        arrayDiscounts = saleLineItem.getDiscountsArray();
      else {
        SaleLineItemDetail saleLineItemDetail = ((CMSReturnLineItemDetail)posLineItem.
            getLineItemDetailsArray()[0]).getSaleLineItemDetail();
        if (saleLineItemDetail != null) {
          saleLineItem = (CMSSaleLineItem)saleLineItemDetail.getLineItem();
          if (saleLineItem != null)
            arrayDiscounts = saleLineItem.getDiscountsArray();
        }
      }
    }
    if (arrayDiscounts == null) {
      arrayDiscounts = posLineItem.getDiscountsArray();
    }
    for (int iCtr = 0; iCtr < arrayDiscounts.length; iCtr++) {
      vecDiscounts.addElement(arrayDiscounts[iCtr]);
    }
  }

  /**
   * This method is used to create discount for pos line item
   */
  private void createTxnDiscounts() {
    Discount[] arrayDiscounts = null;
    if (posLineItem instanceof CMSReturnLineItem) {
      SaleLineItem saleLineItem = ((CMSReturnLineItem)posLineItem).getSaleLineItem();
      if (saleLineItem != null)
        arrayDiscounts = saleLineItem.getTransaction().getCompositeTransaction().
            getSettlementDiscountsArray();
      else {
        SaleLineItemDetail saleLineItemDetail = ((CMSReturnLineItemDetail)posLineItem.
            getLineItemDetailsArray()[0]).getSaleLineItemDetail();
        if (saleLineItemDetail != null) {
          saleLineItem = (CMSSaleLineItem)saleLineItemDetail.getLineItem();
          arrayDiscounts = saleLineItem.getTransaction().getCompositeTransaction().
              getSettlementDiscountsArray();
        }
      }
    }
    if (arrayDiscounts == null) {
      arrayDiscounts = posLineItem.getTransaction().getCompositeTransaction().
          getSettlementDiscountsArray();
    }
    for (int i = 0; i < arrayDiscounts.length; i++) {
      if (!vecTxnDiscounts.contains(arrayDiscounts[i])) {
        vecTxnDiscounts.add(arrayDiscounts[i]);
      }
    }
  }

  /**
   * This method is used to create reduction
   */
  private void createReductions() {
    Reduction arrayReductions[];
    ArmCurrency currAmount;
    int iReductionCtr;
    String sReason;
    arrayReductions = posLineItemDetail.getReductionsArray();
    System.out.println("Ruchi 111createReductions :  ");
    for (iReductionCtr = 0; iReductionCtr < arrayReductions.length; iReductionCtr++) {
      sReason = arrayReductions[iReductionCtr].getReason();
      System.out.println("Ruchi 2222createReductions :  "+sReason);
      currAmount = arrayReductions[iReductionCtr].getAmount();
      //Fix for 1303: Items that were marked down by the buyers and loaded to the stores 
      //read "Deal Markdown."  We need to change this wording to read "Promotion ??%".
      if (sReason.indexOf("Markdown") != -1 || sReason.indexOf("MARKDOWN") != -1) {
        sReason = REASON_MARKDOWN;
        dPromotionalMarkdown += currAmount.doubleValue();
      } else {
        Discount lineDiscount = null;
        if (sReason == null || sReason.trim().length() < 1){
          sReason = "Discount";
         }
        vecDiscountReasons.addElement(sReason);
        lineDiscount = findDiscount(sReason);
        if (lineDiscount == null && arrayReductions[iReductionCtr] instanceof CMSReduction) {
          Discount tmpDiscount = ((CMSReduction)arrayReductions[iReductionCtr]).getDiscount();
          if (tmpDiscount != null && tmpDiscount.isDiscountPercent())
            lineDiscount = tmpDiscount;
        }
        if (lineDiscount != null) {
          if (lineDiscount.isDiscountPercent()) {
            vecDiscountPercents.addElement((lineDiscount.getPercent() * 100) + "%");
          } else {
            currAmount = lineDiscount.getAmount();
            vecDiscountPercents.addElement("");
          }
        } else {
          vecDiscountPercents.addElement("");
        }
        vecDiscountAmounts.addElement(currAmount);
        sReason = REASON_DISCOUNT;
      }
      dTotalReduction += currAmount.doubleValue();
      vecReductionType.addElement(sReason);
    }
  }

  /**
   *
   */
  public void findTxnDiscount() {
    // theAppMgr.
  }

  /**
   * This method is used to find the discount on the basis of discount reason
   * @param sDiscountReason discount reason
   * @return disc Discount
   */
  public Discount findDiscount(String sDiscountReason) {
    Discount disc = null;
    for (int iCtr = 0; iCtr < vecDiscounts.size(); iCtr++) {
      disc = (Discount)vecDiscounts.elementAt(iCtr);
      if (disc != null) {
        if (disc.getReason() == null || disc.getReason().length() == 0
            || sDiscountReason.indexOf(disc.getReason()) < 0) {
          disc = null;
        } else {
          vecDiscounts.remove(iCtr);
          vecTxnDiscounts.remove(disc);
          return disc;
        }
      }
    }
    for (int iCtr = 0; iCtr < vecTxnDiscounts.size(); iCtr++) {
      disc = (Discount)vecTxnDiscounts.elementAt(iCtr);
      if (disc != null) {
        vecTxnDiscounts.remove(iCtr);
        if (disc.getReason() == null || disc.getReason().length() == 0
            || sDiscountReason.indexOf(disc.getReason()) < 0 || !disc.isDiscountPercent()) {
          return null;
        }
        //else
        return disc;
      }
    }
    return null;
  }

  /**
   * This method is used to get the discount reason from a perticular index of list
   * @param iDiscCtr int
   * @return String
   */
  public String getDiscountReason(int iDiscCtr) {
    return (String)vecDiscountReasons.elementAt(iDiscCtr);
  }

  /**
   * This method is used to get the discount percent from a perticular index of list
   * @param iDiscCtr int
   * @return String
   */
  public String getDiscountPercent(int iCtr) {
    return (String)vecDiscountPercents.elementAt(iCtr);
  }

  /**
   * This method is used to get Promotional mark discount
   * @return Currency
   */
  public ArmCurrency getPromotionalMarkdown() {
    if (vecReductionType.contains(REASON_MARKDOWN)) {
      return new ArmCurrency(dPromotionalMarkdown);
    }
    return null;
  }

  /**
   * This method is used to get all available discounts
   * @return ArmCurrency[]
   */
  public ArmCurrency[] getAvailableDiscounts() {
    return (ArmCurrency[])vecDiscountAmounts.toArray(new ArmCurrency[vecDiscountAmounts.size()]);
    //    ArmCurrency curDiscounts[];
    //    curDiscounts = new ArmCurrency[vecDiscountAmounts.size()];
    //    for(int iCtr =0; iCtr < curDiscounts.length;iCtr++)
    //    {
    //      curDiscounts[iCtr] = (ArmCurrency)vecDiscountAmounts.elementAt(iCtr);
    //    }
    //    return curDiscounts;
  }

  /**
   * This method is used to get total reduction
   * @return Currency
   */
  public ArmCurrency getTotalReduction() {
    return new ArmCurrency(dTotalReduction);
  }

  /**
   * This method is used to get retail price
   * @return double
   */
  public double getRetailPrice() {
    return 0.0;
  }

  /**
   * This method is used to get all discount reasons
   * @return double
   */
  public String[] getDiscountReasons() {
    return (String[])vecDiscountReasons.toArray(new String[vecDiscountReasons.size()]);
  }

  /**
   * This method is used to check whether list contain markdown reason or not
   * @return boolean
   */
  public boolean hasMarkDown() {
    return vecReductionType.contains(REASON_MARKDOWN);
  }

  /**
   * This method is used to check whether list contain discount reason or not
   * @return boolean
   */
  public boolean hasDiscount() {
    return vecReductionType.contains(REASON_DISCOUNT);
  }

  /**
   *
   * @return Map
   */
  public static Map getReductionAmountByReason(POSLineItem lineItem) {
    Map reductionAmtMap = new HashMap();
    Reduction reduction = null;
    String reason = "";
    POSLineItemDetail[] lnItmDtlArr = lineItem.getLineItemDetailsArray();
    if (lnItmDtlArr != null && lnItmDtlArr.length > 0) {
      for (int i = 0; i < lnItmDtlArr.length; i++) {
        for (Enumeration enm = lnItmDtlArr[i].getReductions(); enm.hasMoreElements(); ) {
          reduction = (Reduction)enm.nextElement();
          reason = reduction.getReason();
          System.out.println("Ruchi inside getReductionAmountByReason   :"+reason);
          if (reductionAmtMap.containsKey(reason)) {
            try {
              reductionAmtMap.put(reason
                  , ((ArmCurrency)reductionAmtMap.get(reason)).add(reduction.getAmount()));
              System.out.println("Printing reason :"+reason);
            } catch (Exception e) {}
          } else {
            reductionAmtMap.put(reason, reduction.getAmount());
            System.out.println("Printing reason inside else:"+reason);
          }
        }
      }
    }
    return reductionAmtMap;
  }

  /**
   * put your documentation comment here
   * @param lineItem
   * @return
   */
  public static String getNotOnFileItemId(POSLineItem lineItem) {
    if (lineItem.isMiscItem() && lineItem.getMiscItemComment() != null
        && lineItem.getMiscItemComment().indexOf("|") > 0) {
      StringTokenizer strTokenizer = new StringTokenizer(lineItem.getMiscItemComment(), "|");
      if (strTokenizer.hasMoreTokens())
        return strTokenizer.nextToken();
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param lineItem
   * @return
   */
  public static String getNotOnFileItemClass(POSLineItem lineItem) {
    if (lineItem.isMiscItem() && lineItem.getMiscItemComment() != null
        && lineItem.getMiscItemComment().indexOf("|") > 0) {
      StringTokenizer strTokenizer = new StringTokenizer(lineItem.getMiscItemComment(), "|");
      if (!strTokenizer.hasMoreTokens())
        return null;
      strTokenizer.nextToken();
      if (strTokenizer.hasMoreTokens())
        return strTokenizer.nextToken();
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param lineItem
   * @return
   */
  public static String getNotOnFileItemDept(POSLineItem lineItem) {
    if (lineItem.isMiscItem() && lineItem.getMiscItemComment() != null
        && lineItem.getMiscItemComment().indexOf("|") > 0) {
      StringTokenizer strTokenizer = new StringTokenizer(lineItem.getMiscItemComment(), "|");
      for (int i = 0; i < 2; i++) {
        if (!strTokenizer.hasMoreTokens())
          return null;
        strTokenizer.nextToken();
      }
      if (strTokenizer.hasMoreTokens())
        return strTokenizer.nextToken();
    }
    return null;
  }
  public static boolean isNotOnFileItem(String itemID) {
	    if(notOnFileIDs == null) {
	      notOnFileIDs = new Vector();
	      ConfigMgr itmCfg = new ConfigMgr("item.cfg");
	      String notOnFileTypes = "NOTONFILE_TYPES";
	      String notOnFileKeys = itmCfg.getString(notOnFileTypes);
	      if (notOnFileKeys != null && notOnFileKeys.trim().length() > 0) {
	        StringTokenizer sTokens = new StringTokenizer(notOnFileKeys, ",");
	        while (sTokens.hasMoreTokens()) {
	          String id = itmCfg.getString(sTokens.nextToken()+".BASE_ITEM");
	          if(id.trim().length()>0)
	            notOnFileIDs.add(id.trim());
	        }
	      }
	    }
	    return notOnFileIDs.contains(itemID.trim());
	  }

  /**
   * put your documentation comment here
   * @param lineItem
   * @return
   * @exception CurrencyException
   */
  public static ArmCurrency getSellingPrice(POSLineItem lineItem)
      throws CurrencyException {
    ArmCurrency currSellingPrice = lineItem.getItemSellingPrice(); //lineItem.getItem().getSellingPrice();
    currSellingPrice = currSellingPrice.subtract(LineItemPOSUtil.getDealMarkdownAmount(lineItem));
    return currSellingPrice;
  }

  /**
   * put your documentation comment here
   * @param lineItem
   * @return
   * @exception CurrencyException
   */
  public static ArmCurrency getDealMarkdownAmount(POSLineItem lineItem)
      throws CurrencyException {
    POSLineItemDetail[] lnItmDtlArr = lineItem.getLineItemDetailsArray();
    ArmCurrency currTotalDealMarkdownAmt = new ArmCurrency(0.0d);
    if (lnItmDtlArr != null && lnItmDtlArr.length > 0) {
      currTotalDealMarkdownAmt = currTotalDealMarkdownAmt.add(lnItmDtlArr[0].getDealMarkdownAmount());
    }
    return currTotalDealMarkdownAmt;
  }

  /**
   *
   * @param type String
   * @return Discount
   */
  public Discount getDiscountByType(String type) {
    Reduction reduction = null;
    CMSReduction cmsReduction = null;
    String reason = "";
    POSLineItemDetail[] lnItmDtlArr = this.posLineItem.getLineItemDetailsArray();
    if (lnItmDtlArr != null && lnItmDtlArr.length > 0) {
      for (int i = 0; i < lnItmDtlArr.length; i++) {
        for (Enumeration enm = lnItmDtlArr[i].getReductions(); enm.hasMoreElements(); ) {
          reduction = (Reduction)enm.nextElement();
          reason = reduction.getReason().toLowerCase();
          if (reason.indexOf(" discount") > 0) {
            if (reduction instanceof CMSReduction) {
              cmsReduction = (CMSReduction)reduction;
           //Anjana added 05-22-2017 to save discount code in staging table based on reason selected from pop up
              System.out.println("Ruchi printing discount type in LineItemPOSUtil  :"+type);
              System.out.println("Ruchi printing discount type in LineItemPOSUtilcmsReduction.getDiscount().getType()  :"+cmsReduction.getDiscount().getType());
              if (type.toLowerCase().equalsIgnoreCase(cmsReduction.getDiscount().getType().toLowerCase()) || (cmsReduction.getDiscount().getType().toLowerCase()).contains(type.toLowerCase())) {
                return cmsReduction.getDiscount();
              }
            }
          }
        }
      }
    }
    return null;
  }

  /**
   *
   * @return Map
   */
  public Map getReductionAmountByReason() {
	  //changed the map type to LinkedHashMap from HashMap Vishal Yevale : markdown CR 3/1/2017
    Map reductionAmtMap = new LinkedHashMap();
    Reduction reduction = null;
    String reason = "";
    POSLineItemDetail[] lnItmDtlArr = this.posLineItem.getLineItemDetailsArray();
    if (lnItmDtlArr != null && lnItmDtlArr.length > 0) {
      for (int i = 0; i < lnItmDtlArr.length; i++) {
        for (Enumeration enm = lnItmDtlArr[i].getReductions(); enm.hasMoreElements(); ) {
          reduction = (Reduction)enm.nextElement();
          reason = reduction.getReason();
          if (reductionAmtMap.containsKey(reason)) {
            try {
              reductionAmtMap.put(reason
                  , ((ArmCurrency)reductionAmtMap.get(reason)).add(reduction.getAmount()));
            } catch (Exception e) {}
          } else {
            reductionAmtMap.put(reason, reduction.getAmount());
          }
        }
      }
    }
    return reductionAmtMap;
  }
  static{
	  initDepositTypes();
  }
  static Vector openDepositTypes;
  static Vector closeDepositTypes;
  private static void initDepositTypes() {
	    openDepositTypes = new Vector();
	    closeDepositTypes = new Vector();
	    ConfigMgr mgr = new ConfigMgr("item.cfg");
	    String depositeTypesStr = mgr.getString("OPEN_DEPOSIT_TYPES");
	    if (depositeTypesStr == null || depositeTypesStr.trim().length() == 0)
	      return;
	    StringTokenizer strTok = new StringTokenizer(depositeTypesStr, ",");
	    while (strTok.hasMoreTokens()) {
	      openDepositTypes.addElement(strTok.nextToken());
	    }
	    depositeTypesStr = mgr.getString("CLOSE_DEPOSIT_TYPES");
	    strTok = new StringTokenizer(depositeTypesStr, ",");
	    while (strTok.hasMoreTokens()) {
	      closeDepositTypes.addElement(strTok.nextToken());
	    }
	  }
  public static boolean isDeposit(String lineItemId){
	  if (openDepositTypes.contains(lineItemId)
	          || closeDepositTypes.contains(lineItemId)) {
		  return true;
	  }
	  	return false;
  }
}

