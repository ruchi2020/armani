/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 10   | 05-11-2005 | Rajesh    | N/A       |modified getInsertSQL method                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 9    | 04-13-2005 | Rajesh    | N/A       |Specs Consignment impl                              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 8    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  java.sql.SQLException;
import  java.util.ArrayList;
import  java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import  java.util.HashMap;
import  java.util.List;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.employee.Employee;
import  com.chelseasystems.cr.pos.CompositePOSTransaction;
import  com.chelseasystems.cr.pos.POSLineItem;
import  com.chelseasystems.cr.pos.ShippingRequest;
import  com.chelseasystems.cr.store.Store;
// Added by Satin for digital signature
import com.chelseasystems.cs.dataaccess.CompositeDAO;
import com.chelseasystems.cs.dataaccess.TransactionDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.TrRtlOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmPosPrsOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmPosCsgOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmPosRsvOracleBean;
import com.chelseasystems.cs.database.CMSParametricStatement;
import com.chelseasystems.cs.payment.CMSPremioDiscount;
import  com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.sos.CMSTransactionSOS;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.CaseSensitiveString;
import com.chelseasystems.cs.v12basket.CMSV12Basket;
import  com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.customer.CMSCustomer;
import  com.chelseasystems.cs.customer.CustomerSaleSummary;
import  com.chelseasystems.cs.loyalty.*;
import  com.chelseasystems.cs.item.CMSItem;
import  com.chelseasystems.cs.fiscaldocument.FiscalDocument;


/**
 *
 *  CompositePOSTransaction Data Access Object.<br>
 *  This object encapsulates all database access for CompositePOSTransaction.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Consultant</td><td>TR_RTL</td><td>CONSULTANT_ID</td></tr>
 *    <tr><td>Customer</td><td>TR_RTL</td><td>IdCt</td></tr>
 *    <tr><td>Id</td><td>TR_RTL</td><td>AI_TRN</td></tr>
 *    <tr><td>RegionalTaxExemptId</td><td>TR_RTL</td><td>REGIONAL_TAX_EXMP_ID</td></tr>
 *    <tr><td>TaxExemptId</td><td>TR_RTL</td><td>TAX_EXEMPT_ID</td></tr>
 *  </table>
 *
 *  @see CompositePOSTransaction
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.TrRtlOracleBean
 *
 */
public class CompositeOracleDAO extends BaseOracleDAO implements CompositeDAO{
  private static TransactionOracleDAO transactionDAO = new TransactionOracleDAO();
  private static PaymentTransactionOracleDAO paymentTransactionDAO = new PaymentTransactionOracleDAO();
  private static EmployeeOracleDAO employeeDAO = new EmployeeOracleDAO();
  private static CustomerOracleDAO customerDAO = new CustomerOracleDAO();
  private static ShippingRequestOracleDAO shippingRequestDAO = new ShippingRequestOracleDAO();
  private static ArmFiscalDocumentOracleDAO fiscalDocumentDAO = new ArmFiscalDocumentOracleDAO();
  private static PosLayawayOracleDAO posLayawayDAO = new PosLayawayOracleDAO();
  private static PosLineItemOracleDAO posLineItemDAO = new PosLineItemOracleDAO();
  private static DiscountOracleDAO discountDAO = new DiscountOracleDAO();
  private static ArmCustSaleSummaryOracleDAO custSaleSummaryDAO = new ArmCustSaleSummaryOracleDAO();
  private static ArmPosPrsOracleDAO armPosPrsDAO = new ArmPosPrsOracleDAO();
  private static ArmPosCsgOracleDAO armPosCsgDAO = new ArmPosCsgOracleDAO();
  private static ArmPosRsvOracleDAO armPosRsvDAO = new ArmPosRsvOracleDAO();
  private static LoyaltyOracleDAO loyaltyDAO = new LoyaltyOracleDAO();
  private static String selectSql = TrRtlOracleBean.selectSql;
  private static String insertSql = TrRtlOracleBean.insertSql;
  private static String updateSql = TrRtlOracleBean.updateSql + where(TrRtlOracleBean.COL_AI_TRN);
  private static String deleteSql = TrRtlOracleBean.deleteSql + where(TrRtlOracleBean.COL_AI_TRN);

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement[] getInsertSQL (CompositePOSTransaction object) throws SQLException {
    try {
       ArrayList statements = new ArrayList();
	   //Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
	  //statements.add(new ParametricStatement(insertSql, fromObjectToBean(object).toList())); 
      statements.add(new CMSParametricStatement(insertSql, fromObjectToBean(object).toList(),true));
      //Ends here
	  ShippingRequest[] shippingRequestArray = object.getShippingRequestsArray();
      for (int i = 0; i < shippingRequestArray.length; i++)
        statements.add(shippingRequestDAO.getInsertSQL(shippingRequestArray[i], i));
      if (object.getLayawayLineItemsArray().length > 0)
        statements.add(posLayawayDAO.getInsertSQL(object.getLayawayTransaction()));
      Discount[] discounts = object.getDiscountsArray();
      for (int i = 0; i < discounts.length; i++)
        statements.addAll(Arrays.asList(discountDAO.getInsertSQL(object.getId(), 
          ((CMSDiscount)discounts[i]).getSequenceNumber(), (CMSDiscount)discounts[i])));
      // Save the settlement discount array
      Discount[] settlementdiscounts = object.getSettlementDiscountsArray();
      for (int i = 0; i < settlementdiscounts.length; i++)
        statements.addAll(Arrays.asList(discountDAO.getInsertSQL(object.getId(), 
          ((CMSDiscount)settlementdiscounts[i]).getSequenceNumber(), 
          (CMSDiscount)settlementdiscounts[i])));
      POSLineItem[] posLineItems = object.getLineItemsArray();
      for (int i = 0; i < posLineItems.length; i++)
        statements.addAll(Arrays.asList(posLineItemDAO.getInsertSQL(posLineItems[i])));
      if (object.getCustomer() != null) {
        // persist customer sales summary data
        statements.addAll(Arrays.asList(custSaleSummaryDAO.getInsertSQL(object)));
        
        //If required, update Customer in CRM Staging and set
        //status as processed.
        //This will work for only customers that are added to transaction using
        //REFRESH button on Customer Lookup applet.
        CMSCustomer customer = (CMSCustomer) object.getCustomer();
        ConfigMgr customerCFG = new ConfigMgr("customer.cfg");
        String customerPrefix = "S";
        if(customerCFG.getString("SAP_CUSTOMER_ID_PREFIX")!=null){
        	customerPrefix  = customerCFG.getString("SAP_CUSTOMER_ID_PREFIX");
        }
        if(customer.getId().startsWith(customerPrefix) && customer.getStoreId()!=null){
        	statements.addAll(Arrays.asList(customerDAO.getUpdateSQLForCRMStagingCustomer(customer)));
        }      
        
      }
      CMSCompositePOSTransaction cmsCompositePosTxn = (CMSCompositePOSTransaction)object;
      // Insert loyalty points
      if(cmsCompositePosTxn.getCmsV12Basket()!=null) {
    	  CMSV12Basket cmsV12Basket = cmsCompositePosTxn.getCmsV12Basket();
    	  cmsV12Basket.setTrnStatus(CMSV12Basket.inProcess);
    	  ArmStgVirtualSaleDAO v12BasketDAO = new ArmStgVirtualSaleDAO();
    	  statements.add(v12BasketDAO.getUpdateBasketSQL(cmsV12Basket, CMSV12Basket.close));
      }
      if (cmsCompositePosTxn.getLoyaltyCard() != null /*&& cmsCompositePosTxn.getLoyaltyPoints() != 0.0*/) {
        // insert into Loyalty History table and update the Loyalty card
        Loyalty loyalty = cmsCompositePosTxn.getLoyaltyCard();
        double loyaltyUsed = cmsCompositePosTxn.getUsedLoyaltyPoints();
        double points = cmsCompositePosTxn.getLoyaltyPoints();
        
        if(cmsCompositePosTxn.isEvenExchange()){
        	loyaltyUsed = 0;
        	points = 0;
        }
        
//      removing the points for the amount paid by PremioDiscount .        
        
//        if(cmsCompositePosTxn.getUsedLoyaltyPoints()>0){
//        	points = points - (loyalty.getPremioRewardRatioMultiplier()*cmsCompositePosTxn.getUsedLoyaltyPoints());
//        }
        
        double currBal = loyalty.getCurrBalance();
        double currYearBal = loyalty.getCurrYearBalance();
        double lastYearBal = loyalty.getLastYearBalance();
        if(loyaltyUsed>0){
        	if(loyalty.getLastYearBalance()<loyaltyUsed){
        		currYearBal=currYearBal-(loyaltyUsed-lastYearBal);
        		lastYearBal =0.0;
        	}else{
        		lastYearBal -=loyaltyUsed;
        	}
        	currBal-=loyaltyUsed;
        }else{
        	currBal-=loyaltyUsed;
//        	currYearBal -=loyaltyUsed;
        	//___Tim: BUG 1705: lastYearBal to be credited if it is != 0.
        	if(lastYearBal > 0)
        		lastYearBal -= loyaltyUsed;
        	else
        	currYearBal -=loyaltyUsed;
        }
        
        if(cmsCompositePosTxn.isEvenExchange()){        	
        	CMSPremioDiscount premio = null;
        	Payment[] payments  = cmsCompositePosTxn.getPaymentsArray();
        	for(int i=0;i<payments.length;i++){
        		if(payments[i] instanceof CMSPremioDiscount){
        			premio  = (CMSPremioDiscount)payments[i];
        			break;
        		}
        	}
        	if(premio !=null){
        			double premioValue = Math.abs(premio.getAmount().doubleValue())
                * premio.getLoyaltyRewardRatio();
						// premioValue = premioValue-Math.abs(cmsCompositePosTxn.getReturnLoyaltyPoints());
        			LoyaltyHistory hist = null;
                	hist = new LoyaltyHistory();
        	        hist.setLoyaltyNumber(loyalty.getLoyaltyNumber());
        	        hist.setTransactionDate(cmsCompositePosTxn.getSubmitDate());
        	        hist.setStoreID(cmsCompositePosTxn.getStore().getId());
        	        hist.setTransactionId(cmsCompositePosTxn.getId());
        	        hist.setPointEarned(premioValue);
        	        hist.setTransactionType(cmsCompositePosTxn.getTransactionType());
        	        hist.setReasonCode(premioValue + " " + "POINTS ADDED FOR LOYALTY CARD" + " : "
                     + loyalty.getLoyaltyNumber());
        	        statements.add(loyaltyDAO.getInsertHistorySQL(hist));
        	        
        	        hist = new LoyaltyHistory();
        	        hist.setLoyaltyNumber(loyalty.getLoyaltyNumber());
        	        hist.setTransactionDate(cmsCompositePosTxn.getSubmitDate());
        	        hist.setStoreID(cmsCompositePosTxn.getStore().getId());
        	        hist.setTransactionId(cmsCompositePosTxn.getId());
        	        hist.setPointEarned((-1)*premioValue);
        	        hist.setTransactionType(cmsCompositePosTxn.getTransactionType());
        	        hist.setReasonCode(premioValue + " " + "POINTS SUBTRACTED FOR LOYALTY CARD" + " : "
                     + loyalty.getLoyaltyNumber());
        	        
 
        	        statements.add(loyaltyDAO.getInsertHistorySQL(hist));
        		}
        }else{
        	if(loyalty.isYearlyComputed()){
						if(points>0){
							statements.add(loyaltyDAO.getUpdatePointsSQL(
							loyalty.getLoyaltyNumber(), currBal + points, loyalty.getLifeTimeBalance() + points,
    	        		currYearBal + points,lastYearBal ));
            }else{
							//___Tim: BUG 1712: change loyalty computation for each year in case of return txn with points < 0
							// the 3 scenarios are noted below
							if(currYearBal > 0){
								if(currYearBal >= Math.abs(points)){
									// case 1: points = -5, currYear = 5, lastYear = 5, total = 10 
									statements.add(loyaltyDAO.getUpdatePointsSQL(
										loyalty.getLoyaltyNumber(), currBal + points, loyalty.getLifeTimeBalance() + points,
										currYearBal + points, lastYearBal));
		        		} else {
		        			double tempLoyalty = currYearBal + points;
									// case 2: points = -5, currYear = 2, lastYear = 8, total = 10
									statements.add(loyaltyDAO.getUpdatePointsSQL(
										loyalty.getLoyaltyNumber(), currBal + points, loyalty.getLifeTimeBalance() + points,
										0, lastYearBal + tempLoyalty));
								}
							}else{
								// case 3: points = -5, currYear = 0, lastYear = 10, total = 10
								statements.add(loyaltyDAO.getUpdatePointsSQL(
									loyalty.getLoyaltyNumber(), currBal + points, loyalty.getLifeTimeBalance() + points,
									currYearBal, lastYearBal + points));
							}
						}
					} else {
						statements.add(loyaltyDAO.getUpdatePointsSQL(
							loyalty.getLoyaltyNumber(), currBal + points, loyalty.getLifeTimeBalance() + points,
    	        		0,0 ));
            }

            LoyaltyHistory hist = null;
            if(cmsCompositePosTxn.getLoyaltyPoints()!=0.0){
                hist = new LoyaltyHistory();
        hist.setLoyaltyNumber(loyalty.getLoyaltyNumber());
        hist.setTransactionDate(cmsCompositePosTxn.getSubmitDate());
        hist.setStoreID(cmsCompositePosTxn.getStore().getId());
        hist.setTransactionId(cmsCompositePosTxn.getId());
        hist.setPointEarned(cmsCompositePosTxn.getLoyaltyPoints());
        hist.setTransactionType(cmsCompositePosTxn.getTransactionType());
    	  hist.setReasonCode((int)cmsCompositePosTxn.getLoyaltyPoints() + " "
          + "POINTS ADDED FOR LOYALTY CARD"+" : " + loyalty.getLoyaltyNumber());
        statements.add(loyaltyDAO.getInsertHistorySQL(hist));
      }
            if(loyaltyUsed!=0.0){
    	        hist = new LoyaltyHistory();
    	        hist.setLoyaltyNumber(loyalty.getLoyaltyNumber());
    	        hist.setTransactionDate(cmsCompositePosTxn.getSubmitDate());
    	        hist.setStoreID(cmsCompositePosTxn.getStore().getId());
    	        hist.setTransactionId(cmsCompositePosTxn.getId());
    	        hist.setPointEarned((-1)*loyaltyUsed);
    	        hist.setTransactionType(cmsCompositePosTxn.getTransactionType());
						hist.setReasonCode((int) loyaltyUsed + " " + "POINTS SUBTRACTED FOR LOYALTY CARD"
							+ " : " + loyalty.getLoyaltyNumber());
    	        statements.add(loyaltyDAO.getInsertHistorySQL(hist));
            }
        }
        
      }
      // Look for any returns on a different loyalty card to return the points, if applicable
      HashMap pointsMap = new HashMap();
      Double pointsToReturn = new Double(0.0);
      POSLineItem[] retItems = cmsCompositePosTxn.getReturnLineItemsArray();
      if (retItems != null) {
    	  Date txnDate = null;
        for (int i = 0; i < retItems.length; i++) {
          CMSReturnLineItem retItem = (CMSReturnLineItem)retItems[i];
          if (((CMSReturnLineItemDetail)retItem.getLineItemDetailsArray()[0]).getSaleLineItemDetail() != null) {
            CMSCompositePOSTransaction origTxn = (CMSCompositePOSTransaction)(
            	(CMSReturnLineItemDetail)retItem.getLineItemDetailsArray()[0]).getSaleLineItemDetail()
            	.getLineItem().getTransaction().getCompositeTransaction();
            if (origTxn.getLoyaltyCard() != null && !origTxn.getLoyaltyCard().getLoyaltyNumber()
            	.equals(cmsCompositePosTxn.getLoyaltyCard().getLoyaltyNumber())) {
              if (pointsMap.get(origTxn.getLoyaltyCard()) != null) {
                Double points = (Double)pointsMap.get(origTxn.getLoyaltyCard());
                points = new Double(points.doubleValue() + retItem.getLoyaltyPointsToReturn());
                if (origTxn.isTruncatedPoints())
                  points = new Double(java.lang.Math.floor(points.doubleValue()));
                pointsMap.put(origTxn.getLoyaltyCard(), points);
              }
              else {
                Double points = new Double(retItem.getLoyaltyPointsToReturn());
                if (origTxn.isTruncatedPoints())
                  points = new Double(java.lang.Math.floor(points.doubleValue()));
                pointsMap.put(origTxn.getLoyaltyCard(), points);
              }
            }
            if(txnDate==null){
            	txnDate = origTxn.getSubmitDate();
            }
          }
        }
        // remove points from the Loyalty cards
        java.util.Iterator iterator = pointsMap.keySet().iterator();
        while (iterator.hasNext()) {
          Loyalty loyalty = (Loyalty)iterator.next();
          Double points = (Double)pointsMap.get(loyalty);
          double loyaltyUsed = cmsCompositePosTxn.getUsedLoyaltyPoints();
          if(!cmsCompositePosTxn.isEvenExchange() && loyalty.isYearlyComputed()){
        	  int thisyear = Calendar.getInstance().get(Calendar.YEAR);
              if(txnDate!=null ){
		//TD
		//int txnYear = txnDate.getYear();
		Calendar txnCal = Calendar.getInstance();
		txnCal.setTime(txnDate);
		int txnYear = txnCal.get(Calendar.YEAR);
            	  if((thisyear-txnYear)>1){
            		  continue;
            	  }
            	  if((thisyear-txnYear)==1){
            		  statements.add(loyaltyDAO.getUpdatePointsSQL(loyalty.getLoyaltyNumber(), 
        	        		  loyalty.getCurrBalance() - points.doubleValue() + loyaltyUsed, 
        	        		  loyalty.getLifeTimeBalance() - points.doubleValue()+ loyaltyUsed,
        	        		  loyalty.getCurrYearBalance() ,
        	        		  loyalty.getLastYearBalance()- points.doubleValue()+ loyaltyUsed));
            	  }
              }else{
    	          statements.add(loyaltyDAO.getUpdatePointsSQL(loyalty.getLoyaltyNumber(), 
    	        		  loyalty.getCurrBalance() - points.doubleValue()+ loyaltyUsed, 
    	        		  loyalty.getLifeTimeBalance() - points.doubleValue()+ loyaltyUsed,
    	        		  loyalty.getCurrYearBalance() - points.doubleValue()+ loyaltyUsed,
    	        		  loyalty.getLastYearBalance()));
              } 
          }else{
        	  statements.add(loyaltyDAO.getUpdatePointsSQL(loyalty.getLoyaltyNumber(), 
	        		  loyalty.getCurrBalance() - points.doubleValue()+ loyaltyUsed, 
	        		  loyalty.getLifeTimeBalance() - points.doubleValue()+ loyaltyUsed,
	        		  0, 0));
          }
          
          LoyaltyHistory hist = new LoyaltyHistory();
          hist.setLoyaltyNumber(loyalty.getLoyaltyNumber());
          hist.setTransactionDate(cmsCompositePosTxn.getSubmitDate());
          hist.setStoreID(cmsCompositePosTxn.getStore().getId());
          hist.setTransactionId(cmsCompositePosTxn.getId());
          hist.setPointEarned(points.doubleValue()*(-1) + loyaltyUsed);
          hist.setTransactionType(cmsCompositePosTxn.getTransactionType());
					hist.setReasonCode(points.intValue() + " " + "POINTS SUBTRACTED FOR LOYALTY CARD" + " : "
						+ loyalty.getLoyaltyNumber());
          statements.add(loyaltyDAO.getInsertHistorySQL(hist));
        }
      }
      if (cmsCompositePosTxn.getPresaleLineItemsArray() != null
				&& cmsCompositePosTxn.getPresaleLineItemsArray().length > 0) {
				statements.addAll(Arrays.asList(armPosPrsDAO.getInsertSQL(cmsCompositePosTxn
					.getPresaleTransaction())));
      }
			if (cmsCompositePosTxn.getConsignmentLineItemsArray() != null
				&& cmsCompositePosTxn.getConsignmentLineItemsArray().length > 0) {
				statements.addAll(Arrays.asList(armPosCsgDAO.getInsertSQL(cmsCompositePosTxn
					.getConsignmentTransaction())));
      }
			if (cmsCompositePosTxn.getReservationLineItemsArray() != null
				&& cmsCompositePosTxn.getReservationLineItemsArray().length > 0) {
				statements.addAll(Arrays.asList(armPosRsvDAO.getInsertSQL(cmsCompositePosTxn
					.getReservationTransaction())));
      }
      return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw  new SQLException();
    }
  }

  /**
   * put your documentation comment here
   * @param customerId
   * @return
   * @exception SQLException
   */
  public CompositePOSTransaction[] selectByCustomerId (String customerId) throws SQLException {
    String whereSql = where(TrRtlOracleBean.COL_AI_TRN);
    BaseOracleBean[] beans = query(new TrRtlOracleBean(), whereSql, customerId);
    CompositePOSTransaction[] composites = new CompositePOSTransaction[beans.length];
    for (int i = 0; i < beans.length; i++)
      composites[i] = (CompositePOSTransaction)transactionDAO.selectById(((TrRtlOracleBean)beans[i]).getAiTrn());
    return  composites;
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param store
   * @return
   * @exception SQLException
   */
  CompositePOSTransaction getById (String transactionId, Store store) throws SQLException {
	String whereSql = where(TrRtlOracleBean.COL_AI_TRN);
    CompositePOSTransaction[] compositePOSTransactions = fromBeansToObjects(query(new TrRtlOracleBean(), whereSql, transactionId), store);
    if (compositePOSTransactions == null || compositePOSTransactions.length == 0)
      return  null;
    return  compositePOSTransactions[0];
  }

 
  
  /**
	 * Added by Satin to search digital signature based on transaction Id.
	 * @param txnId String
	 * @throws SQLException
	 * @return String
	 */
  public String selectDigitalSignature(String txnId) throws SQLException {
	  String digitalSignature = null;
	  String whereSql = "where AI_TRN = ?";
	  BaseOracleBean[] beans = this.query(new TrRtlOracleBean(), TrRtlOracleBean.selectSql + whereSql, txnId);
		  if (beans != null && beans.length > 0) 
		     {digitalSignature =  this.fromBeanToObject1(beans[0]);}
		  return digitalSignature;
		
	}
  
  
//Added by Satin for digital signature
 private String fromBeanToObject1 (BaseOracleBean baseBean) {
	 TrRtlOracleBean bean = (TrRtlOracleBean)baseBean;
	    String object = new String(bean.getDigitalSignature());
	    return  object;
	  }
  
  
  /**
   * This is used to update FiscalDocumentNumbers for transaction.
   * All FiscalDocumentNumbers are concatinated using '*' for each line item
   * in the transaction.
   * @param sTxnId String
   * @param sDocumentNums String
   * @throws SQLException
   * @return ParametricStatement
   */
  ParametricStatement getUpdateFiscalDocumentSQL (CompositePOSTransaction object) throws SQLException {
    List params = new ArrayList();
    TrRtlOracleBean bean = this.fromObjectToBean(object);
    String sUpdateSQL = "UPDATE " + TrRtlOracleBean.TABLE_NAME + " SET ";
    if (object.getCustomer() != null) {
      sUpdateSQL += TrRtlOracleBean.COL_ID_CT + " = ?, ";
      params.add(object.getCustomer().getId());
    }
    sUpdateSQL += TrRtlOracleBean.COL_FISCAL_DOC_NUMBERS + " =  ? " + where(TrRtlOracleBean.COL_AI_TRN);
    params.add(bean.getFiscalDocNumbers());
    params.add(object.getId());
    return  new ParametricStatement(sUpdateSQL, params);
  }

  /**
   * put your documentation comment here
   * @param beans
   * @param store
   * @return
   * @exception SQLException
   */
  private CompositePOSTransaction[] fromBeansToObjects (BaseOracleBean[] beans, Store store) throws SQLException {
    CompositePOSTransaction[] array = new CMSCompositePOSTransaction[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i], store);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @param store
   * @return
   * @exception SQLException
   */
  private CompositePOSTransaction fromBeanToObject (BaseOracleBean baseBean, Store store) throws SQLException {
	TrRtlOracleBean bean = (TrRtlOracleBean)baseBean;
    CMSCompositePOSTransaction object = new CMSCompositePOSTransaction(bean.getAiTrn(), store);
    if (bean.getIdCt() != null)
      object.doSetCustomer(customerDAO.selectById(bean.getIdCt()));
    if (bean.getConsultantId() != null)
      object.doSetConsultant(employeeDAO.selectById(bean.getConsultantId()));
    object.doSetTaxExemptId(bean.getTaxExemptId());
    object.doSetRegionalTaxExemptId(bean.getRegTaxExmpId());
 // Added by Satin for digital signature
    if(bean.getDigitalSignature() != null){
    	object.doSetDigitalSignature(bean.getDigitalSignature());
    }
    if(bean.getMobileRegisterId() != null) {
    	CMSV12Basket cmsV12Basket = new CMSV12Basket();
    	cmsV12Basket.setMobileRegisterId(bean.getMobileRegisterId());
    	cmsV12Basket.setTrnStatus(CMSV12Basket.inProcess);
    	object.setCmsV12Basket(cmsV12Basket);
    }
    
    shippingRequestDAO.addShippingRequestIntoCompositePOSTransaction(object);
    fiscalDocumentDAO.addFiscalDocumentIntoCompositePOSTransaction(object);

    Discount[] discounts = discountDAO.selectByCompositeId(object.getId());
    for (int i = 0; i < discounts.length; i++)
      object.doAddDiscount(discounts[i]);
    posLineItemDAO.addLineItemsToComposite(object);
    posLayawayDAO.getLayawayTransaction(object);
    // Presale
    ArmPosPrsOracleBean armPosPrsBean = armPosPrsDAO.getPresaleTransaction(bean.getAiTrn());
    if (armPosPrsBean != null) {
      PresaleTransaction prsTxn = object.getPresaleTransaction();
      prsTxn.doSetPresaleId(armPosPrsBean.getIdPresale());
      prsTxn.doSetExpirationDate(armPosPrsBean.getExpDt());
      prsTxn.doSetCardExpirationDate(armPosPrsBean.getCreditCardExpirationDate());
      prsTxn.doSetCardType(armPosPrsBean.getCreditCardType());
      prsTxn.doSetCardZipcode(armPosPrsBean.getBillingZipCode());
      prsTxn.doSetCreidtCardNumber(armPosPrsBean.getCreditCardNumber());
      //Commented because of testing issue in US
      //System.out.println("ARMPOSPRSN " + armPosPrsBean.getCreditCardNumber());
	 // added by vivek for ISD 
//      String encryptedCardData = armPosPrsBean.getEncryptedCCData();
//      String keyId = armPosPrsBean.getUsedKeyId();
//      //prsTxn.setUsedKeyId(keyId);
//      if(encryptedCardData != null && encryptedCardData.trim().length()>0){
//      //prsTxn.setEncryptedCCData(armPosPrsBean.getEncryptedCCData());
//      //prsTxn.set
//      }
      //Commented because of testing issue in US
    }
    // Consignment
    ArmPosCsgOracleBean armPosCsgBean = armPosCsgDAO.getConsignmentTransaction(bean.getAiTrn());
    if (armPosCsgBean != null) {
      ConsignmentTransaction csgTxn = object.getConsignmentTransaction();
      csgTxn.doSetConsignmentId(armPosCsgBean.getIdConsignment());
      csgTxn.doSetExpirationDate(armPosCsgBean.getExpDt());
    }
    // Reservation
    ArmPosRsvOracleBean armPosRsvOracleBean = armPosRsvDAO.getReservationTransaction(bean.getAiTrn());
    if (armPosRsvOracleBean != null) {
      ReservationTransaction rsvTxn = object.getReservationTransaction();
      rsvTxn.doSetDepositAmount(armPosRsvOracleBean.getDepositAmt());
      rsvTxn.doSetReservationReason(armPosRsvOracleBean.getReasonCode());
      rsvTxn.doSetReservationId(armPosRsvOracleBean.getIdReservation());
      rsvTxn.doSetExpirationDate(armPosRsvOracleBean.getExpDt());
      if(armPosRsvOracleBean.getOrigRsvId() != null && armPosRsvOracleBean.getOrigRsvId().length()> 0){
        ReservationTransaction origRsvTxn = new ReservationTransaction(object);
        origRsvTxn.doSetReservationId(armPosRsvOracleBean.getOrigRsvId());
        rsvTxn.setOriginalRSVOTransaction(origRsvTxn);
      }
    }
    if (bean.getLoyaltyTruncated() != null)
      object.setIsTruncatedPoints(bean.getLoyaltyTruncated().booleanValue());
    if (bean.getLoyaltyCardNum() != null) {
      // Fetch the loyalty card
      Loyalty loyalty = loyaltyDAO.selectById(bean.getLoyaltyCardNum());
 //     object.setLoyaltyCard(loyalty);
      object.doSetLoyaltyCard(loyalty);
    }
    if(bean.getLoyaltyPointsEarned() != null)
    	object.doSetLoyaltyPoints(bean.getLoyaltyPointsEarned().doubleValue());
    return  object;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private TrRtlOracleBean fromObjectToBean (CompositePOSTransaction object) {
    TrRtlOracleBean bean = new TrRtlOracleBean();
    
    bean.setAiTrn(object.getId());
    if (object.getCustomer() != null)
      bean.setIdCt(object.getCustomer().getId());
    if (object.getConsultant() != null)
      bean.setConsultantId(object.getConsultant().getId());
    bean.setTaxExemptId(object.getTaxExemptId());
    bean.setReductionAmount(object.getCompositeReductionAmount());
    bean.setNetAmount(object.getCompositeNetAmount());
    bean.setRegTaxExmpId(object.getRegionalTaxExemptId());
    
    //Added by Rachana for apporval of return transaction
    if(object instanceof CompositePOSTransaction){
    	CMSCompositePOSTransaction cmpobj = (CMSCompositePOSTransaction)object;
    	if(cmpobj.getTotalPaymentAmount().stringValue().startsWith("-")){
    	 	bean.setApproverId(cmpobj.getApprover());
    	}
   }
   String discountTypes = "*";
   Discount[] discounts = object.getDiscountsArray();
   for (int i = 0; i < discounts.length; i++)
      discountTypes += (object.getDiscountsArray()[i].getGuiLabel() + "*");
   bean.setDiscountTypes(discountTypes);
   bean.setRegisterId(object.getRegisterId());
   String itemIds = "*";
   POSLineItem[] lineItems = object.getLineItemsArray();
   for (int i = 0; i < lineItems.length; i++) {
        //--MSB 02/09/2006
        // To make sure there are no
        // duplicate enteries.
        if (itemIds.indexOf(lineItems[i].getItem().getId()) == -1)
            itemIds += (lineItems[i].getItem().getId() + "*");
    }
    for (int i = 0; i < lineItems.length; i++) {
        //--MSB 02/09/2006
        // To make sure there are no
        // duplicate enteries.
        if (itemIds.indexOf(((CMSItem) lineItems[i].getItem()).getBarCode()) ==
            -1)
            itemIds += (((CMSItem) lineItems[i].getItem()).getBarCode() + "*");
    }
    bean.setItemsIds(itemIds);
    FiscalDocument[] fiscalDocuments = ((CMSCompositePOSTransaction)object).getFiscalDocumentArray();
    String fiscalDocNums = "*";
    for (int i = 0; i < fiscalDocuments.length; i++)
      fiscalDocNums += fiscalDocuments[i].getDocumentType() + "|" + fiscalDocuments[i].getDocumentNumber()+"*";
    bean.setFiscalDocNumbers(fiscalDocNums);
    bean.setLoyaltyTruncated(((CMSCompositePOSTransaction)object).isTruncatedPoints());
 
    // Added by Satin for digital signature
    bean.setDigitalSignature(((CMSCompositePOSTransaction)object).getDigitalSignature());
    if ((((CMSCompositePOSTransaction)object).getDigitalSignature()) != null){
		////Vivek Mishra : Merged missing code from source provided by Sergio 18-MAY-16
		System.out.println("Printing digital signature in CompositeOracleDAO   :"+((CMSCompositePOSTransaction)object).getDigitalSignature());
		LoggingServices.getCurrent().logMsg("Digital"+((CMSCompositePOSTransaction)object).getDigitalSignature());
		//Ends here
		bean.setDigitalSignature(((CMSCompositePOSTransaction)object).getDigitalSignature());
    }
    
    
    if (((CMSCompositePOSTransaction)object).getLoyaltyCard() != null){
      bean.setLoyaltyCardNum(((CMSCompositePOSTransaction)object).getLoyaltyCard().getLoyaltyNumber());
      bean.setLoyaltyPointsEarned(new Double(((CMSCompositePOSTransaction)object).getLoyaltyPoints()).longValue());
      
    }
  //Mahesh Nandure: Added for V12 basket button
    if (((CMSCompositePOSTransaction)object).getCmsV12Basket()!=null) {
    	CMSV12Basket cmsV12Basket = ((CMSCompositePOSTransaction)object).getCmsV12Basket();
    	bean.setMobileRegisterId(cmsV12Basket.getMobileRegisterId());
    	bean.setTrnComment(cmsV12Basket.getTrnComment());
    }
    
    return  bean;
  }
  
  /**
   * This is used to update Customer for transaction.
   
   * @throws SQLException
   * @return ParametricStatement
   */
  public ParametricStatement getUpdateCustomerSQL (CompositePOSTransaction object) throws SQLException {
    List params = new ArrayList();
    TrRtlOracleBean bean = this.fromObjectToBean(object);
    Customer customer = object.getCustomer();
    String sUpdateSQL = "UPDATE " + TrRtlOracleBean.TABLE_NAME + " SET ";
    if (customer != null) {
      sUpdateSQL += TrRtlOracleBean.COL_ID_CT + " = ? ";
      params.add(customer.getId());
    }
    sUpdateSQL += where(TrRtlOracleBean.COL_AI_TRN);
    params.add(object.getId());
    return  new ParametricStatement(sUpdateSQL, params);
  }
  
  /**
   * This is used to update Customer for transaction.
   
   * @throws SQLException
   * @return ParametricStatement
   */
  public ParametricStatement getUpdateConsultantSQL (CompositePOSTransaction object) throws SQLException {
    List params = new ArrayList();
    TrRtlOracleBean bean = this.fromObjectToBean(object);
    Employee consultant = object.getConsultant();
    String sUpdateSQL = "UPDATE " + TrRtlOracleBean.TABLE_NAME + " SET ";
    if (consultant != null) {
      sUpdateSQL += TrRtlOracleBean.COL_CONSULTANT_ID+ " = ? ";
      params.add(consultant.getId());
    }
    sUpdateSQL += where(TrRtlOracleBean.COL_AI_TRN);
    params.add(object.getId());
    return  new ParametricStatement(sUpdateSQL, params);
  }
  
}



