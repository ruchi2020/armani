/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

/**
 * <p>Title: TransactionUtil</p>
 * <p>Description: Class having utility method related to Transaction </p>
 * @author Rajesh Pradhan
 * @version 1.0
 */
import com.chelseasystems.cr.transaction.CommonTransaction;
import com.chelseasystems.cr.eod.TransactionEOD;
import com.chelseasystems.cr.sos.TransactionSOS;
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.paidout.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;;


/**
 * put your documentation comment here
 */
public class TransactionUtil implements ArtsConstants {

  /**
   * put your documentation comment here
   */
  public TransactionUtil() {
  }

  /**
   * Returns Transaction Type example :- TRNPOS, TRNPSO, TRNSOS etc.
   * @param object CommonTransaction
   * @return String
   */
  static String countryId = null;
  public static String getTransactionType(CommonTransaction object) {
	  //storing country id for CAN store tender types support
	  // this will execute first then the getTenderType()
	  countryId = object.getStore().getCountry();
	 if (object instanceof TransactionSOS)
      return TRANSACTION_TYPE_SOS;
    if (object instanceof TransactionEOD)
      return TRANSACTION_TYPE_EOD;
    if (object instanceof CompositePOSTransaction) {
      CMSCompositePOSTransaction obj = (CMSCompositePOSTransaction)object;
      if (obj.getPresaleLineItemsArray() != null && obj.getPresaleLineItemsArray().length > 0)
        return TRANSACTION_TYPE_COMPOSITE_PRS;
      if (obj.getConsignmentLineItemsArray() != null
          && obj.getConsignmentLineItemsArray().length > 0)
        return TRANSACTION_TYPE_COMPOSITE_CSG;
      if (obj.getReservationLineItemsArray() != null
          && obj.getReservationLineItemsArray().length > 0)
        return TRANSACTION_TYPE_COMPOSITE_RSV;
      if ((obj.getNonFiscalNoSaleLineItemsArray() != null
          && obj.getNonFiscalNoSaleLineItemsArray().length > 0) 
          || (obj.getNonFiscalNoReturnLineItemsArray() != null
          && obj.getNonFiscalNoReturnLineItemsArray().length > 0))
        return TRANSACTION_TYPE_COMPOSITE_NFS;
      return TRANSACTION_TYPE_COMPOSITE_POS;
    } else if (object instanceof CollectionTransaction)
      return TRANSACTION_TYPE_COLLECTION;
    else if (object instanceof PaidOutTransaction)
      return TRANSACTION_TYPE_PAID_OUT;
    else if (object instanceof VoidTransaction)
      return TRANSACTION_TYPE_VOID;
    else if (object instanceof NoSaleTransaction)
      return TRANSACTION_TYPE_NO_SALE;
    else if (object instanceof RedeemableBuyBackTransaction)
      return TRANSACTION_TYPE_REDEEMABLE_BUYBACK;
    if (object instanceof PaymentTransaction)
      return TRANSACTION_TYPE_PAYMENT;
    return TRANSACTION_TYPE_UNKNOWN;
  }

  /**
   * Returns payment type identifier
   * @param object Payment
   * @return String
   */
  //Vivek Mishra : Added countryid parameter to restrict use of global variable countryId in order to resolve CAD_CASH issue : 14-JUL-2016
  public static String getPaymentTypeId(Payment object, String countryId) {
    String type = PAYMENT_TYPE_UNKNOWN;
    if (object instanceof Cash) {
      type = PAYMENT_TYPE_CASH;
    //Added by Anjana to support CAD cash
      //Anjana Commented as CANADA cash needs to be saved as CASH itself
	 	// if(countryId.equalsIgnoreCase("CAN")){
	 		//type = PAYMENT_TYPE_CAD_CASH;
	 	// }//End Anjana's changes to support CAD cash
    } else if (object instanceof MallCert) {
      type = PAYMENT_TYPE_MALLCERT;
    } else if (object instanceof Check) {
    	 //Added by Anjana to support CAD checks
        if(countryId.equalsIgnoreCase("CAN")){
      	  if(object instanceof BusinessCheck){
      		  type = PAYMENT_TYPE_CAD_BUSINESS_CHECK;
      	  }
      	  else{
      		  type = PAYMENT_TYPE_CAD_BANK_CHECK;
      	  }
        }//End Anjana's changes to support CAD checks
      if (object instanceof EmployeeCheck)
        type = PAYMENT_TYPE_EMPLOYEE_CHECK;
      else if (object instanceof BankCheck) {
        if (object instanceof BusinessCheck) {
          type = PAYMENT_TYPE_BUSINESS_CHECK;
        } else
          type = PAYMENT_TYPE_BANK_CHECK;
      }
      //ks: Local and out of area checks for europe
      else if (object instanceof LocalCheck) {
        type = PAYMENT_TYPE_LOCAL_CHECK;
      } else if (object instanceof OutOfAreaCheck) {
        type = PAYMENT_TYPE_OUT_OF_AREA_CHECK;
      } else if (object instanceof MoneyOrder)
        type = PAYMENT_TYPE_MONEY_ORDER;
      else if (object instanceof TravellersCheck)
        type = PAYMENT_TYPE_TRAVELLERS_CHECK;
      else
        type = PAYMENT_TYPE_CHECK;
    } else if (object instanceof CreditCard) {
      if (object instanceof Amex)
        type = PAYMENT_TYPE_AMEX;
      else if (object instanceof Discover)
        type = PAYMENT_TYPE_DISCOVER;
      else if (object instanceof MasterCard)
        type = PAYMENT_TYPE_MASTER_CARD;
      // Added for CMSVisa
      else if (object instanceof Visa)
        type = PAYMENT_TYPE_VISA;
      else if (object instanceof DebitCard) 
    	   type = PAYMENT_TYPE_DEBIT_CARD;
      
      else if ((object instanceof CreditCard)&& (object.getGUIPaymentName().equalsIgnoreCase("Debit"))){
    	  type = PAYMENT_TYPE_DEBIT_CARD;
      }
    //ruchi for Canada
      else if ((object instanceof CreditCard)&& (object.getGUIPaymentNameForMobileTerminal().equalsIgnoreCase("DEBIT"))){
    	  System.out.println("Ruchi inside Canada payment  for mobile terminal :"+object.getGUIPaymentNameForMobileTerminal());
    	  type = PAYMENT_TYPE_DEBIT_CARD;
 }
      
 
      // Added for JCB & Diners.
      else if (object instanceof JCBCreditCard)
        type = PAYMENT_TYPE_JCB;
      //Changed PAYMENT_TYPE_diners to PAYMENT_TYPE_DINERS
      else if (object instanceof Diners)
        type = PAYMENT_TYPE_DINERS;
    
    	//sonali:Added for saving payment type in arm_stg_txn_dtl
          
         
      else if(object.getGUIPaymentName()!=null && (!object.getGUIPaymentName().equals(""))){
        	  type=object.getGUIPaymentName();
        	 }
          else{
            type = PAYMENT_TYPE_CREDIT_CARD;
          }
    	
        	  
       // type = PAYMENT_TYPE_CREDIT_CARD;
      } else if (object instanceof Redeemable) {
      if (object instanceof GiftCert)
        type = PAYMENT_TYPE_GIFT_CERTIFICATE;
      else if (object instanceof StoreValueCard || object instanceof CMSStoreValueCard) {
        type = PAYMENT_TYPE_STORE_VALUE_CARD;
      } else if (object instanceof DueBill || object instanceof CMSDueBill) {
        if (object instanceof DueBillIssue)
          type = PAYMENT_TYPE_DUE_BILL_ISSUE;
        else
          type = PAYMENT_TYPE_DUE_BILL;
      } else if (object instanceof HouseAccount) {
        type = PAYMENT_TYPE_HOUSE_ACCOUNT;
      } else {
        type = object.getTransactionPaymentName();
      }
    	  // Added By Anjana to support REDEEMABLE typecode of arm_grp_cls_tnd table
      if(type.equals(null)){
          if(countryId.equalsIgnoreCase("CAN")){
    	  type = PAYMENT_TYPE_REDEEMABLE;
      }//End Anjana's changes to support REDEEMABLE
      }
    } else if (object instanceof Coupon) {
      type = PAYMENT_TYPE_COUPON;
    } else if (object instanceof ManufacturerCoupon) {
      type = PAYMENT_TYPE_MANUFACTURE_COUPON;
    }
    else if (object instanceof MailCheck) {
      type = PAYMENT_TYPE_MAIL_CHECK;
    }
    //ks: Round Payment
    else if (object instanceof RoundPayment) {
      type = PAYMENT_TYPE_ROUND_PAYMENT;
    } else if (object instanceof Credit) {
      type = PAYMENT_TYPE_CREDIT;
    } else {
      type = PAYMENT_TYPE_UNKNOWN;
    }
    return type;
  }

  /**
   * @param amt
   * @return
   */
  public static boolean validateChangeAmount(IApplicationManager theAppMgr, String paymentType
      , String paymentTypeView, ArmCurrency amt) {
    try {
      java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
      ConfigMgr cfgMgr = new ConfigMgr("payment.cfg");
      String strMaxChange = cfgMgr.getString(paymentType.toUpperCase() + ".MAX_CHANGE_ALLOWED");
      if (strMaxChange == null)
        strMaxChange = "0.00";
      ArmCurrency maxChange = new ArmCurrency(strMaxChange);
      PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
      CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
          CMSAppModelFactory.getInstance(), theAppMgr);
      CMSPaymentTransactionAppModel txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
      ArmCurrency amtLeft = new ArmCurrency(0.0d);
    //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
		if ((txn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(txn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
				&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
			amtLeft = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
		}
		//Ends
		else
			amtLeft = appModel.getCompositeTotalAmountDue().subtract(appModel.
          getTotalPaymentAmount());
      ArmCurrency total = new ArmCurrency(0.00);
      total = amt.subtract(amtLeft);
      if (amtLeft.lessThan(new ArmCurrency(0.0))) {
        if (amt.greaterThan(amtLeft.absoluteValue())) {
          theAppMgr.showErrorDlg(res.getString("Change cannot be more than") + " "
              + amtLeft.formattedStringValue() + ".");
          return false;
        }
        return true;
      } else if (total.truncate( -1).greaterThanOrEqualTo(new ArmCurrency(0.0))
          && total.truncate( -1).greaterThan(maxChange)) {
        if (maxChange.equals(new ArmCurrency(0.00))) {
          theAppMgr.showErrorDlg(res.getString("No over payment allowed."));
          return false;
        } else {
          //theAppMgr.showErrorDlg(res.getString("Maximum change allowed for") + " "+paymentTypeView +" is " + strMaxChange);
          theAppMgr.showErrorDlg(res.getString(
              "Payment amount exceeds maximum change due. Select change type."));
          return true;
        }
      } else
        return (true);
    } catch (Exception ex) {
      ex.printStackTrace();
      return (false);
    }
  }
  
  public static String getTransactionFiscalSearchString(TransactionSearchString tss) {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    String sSearchedCriteria = res.getString("All transactions");
    if (tss.getTransId() != null && tss.getTransId().length() > 0)
      sSearchedCriteria += res.getString(" Transaction number is : ") + tss.getTransId();
    else {
      if (tss.getRegisterId() != null && tss.getRegisterId().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Register is : ") + tss.getRegisterId();
      }
      if (tss.getStoreId() != null && tss.getStoreId().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Store is : ") + tss.getStoreId();
      } else {
        if (tss.getCompanyCode() != null && tss.getCompanyCode().length() > 0) {
          sSearchedCriteria += ", "+res.getString("Company Code is : ") + tss.getStoreId();
        }
        if (tss.getStoreBrand() != null && tss.getStoreBrand().length() > 0) {
          sSearchedCriteria += ", "+res.getString("Store type is : ") + tss.getStoreBrand();
        }
      }
      if (tss.getFiscalRecieptNum() != null && tss.getFiscalRecieptNum().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Fiscal Reciept number is : ") + tss.getFiscalRecieptNum();
      }
      if (tss.getFiscalDocType() != null && tss.getFiscalDocType().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Fiscal document type is : ") + tss.getFiscalDocType();
      }
      if (tss.getFiscalDocNum() != null && tss.getFiscalDocNum().length() > 0) {
        sSearchedCriteria += ", " +res.getString("Fiscal document number is : ") + tss.getFiscalDocNum();
      }
    }
    return sSearchedCriteria;
  }
  
  public static String getTransactionSearchString(TransactionSearchString tss){
  	if (tss.isFiscalSearch()){
  		return TransactionUtil.getTransactionFiscalSearchString(tss);
  	}
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    String sSearchedCriteria = res.getString("All transactions");
    if (tss.getCustomerId() != null && tss.getCustomerId().length() > 0) {
      sSearchedCriteria = res.getString(" Customer is: ") 
      	+ tss.getCustomerFirstName() + " " + tss.getCustomerLastName();
    }
    if (tss.getStoreId() != null && tss.getStoreId().length() > 0) {
      sSearchedCriteria += ", "+res.getString("Store is : ") + tss.getStoreId();
    } else {
      if (tss.getCompanyCode() != null && tss.getCompanyCode().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Company Code is : ") + tss.getStoreId();
      }
      if (tss.getStoreBrand() != null && tss.getStoreBrand().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Store type is : ") + tss.getStoreBrand();
      }
    }
    if (tss.getAssociateId() != null && tss.getAssociateId().length() > 0) {
      sSearchedCriteria += ", "+res.getString("Associate is : ") + tss.getAssociateId();
    }
    if (tss.getCashierId() != null && tss.getCashierId().length() > 0) {
      sSearchedCriteria += ", "+res.getString("Cashier is : ") + tss.getCashierId();
    }
    if (tss.getTransactionType() != null && tss.getTransactionType().length() > 0) {
      sSearchedCriteria += ", "+res.getString("Transaction Type is : ") + tss.getTransactionType();
    }

    if (tss.getPayType() != null && tss.getPayType().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Pay type is : ") + tss.getPayType();
    }

    if (tss.getCurrencyCode() != null && tss.getCurrencyCode().length() > 0) {
      sSearchedCriteria += ", "+res.getString("Currency Code is : ")
      	+ tss.getCurrencyCode();
    }
    if (tss.getCurTransStartAmount() != null && tss.getCurTransEndAmount() != null) {
      sSearchedCriteria += ", "+res.getString("Amount is between : ") 
      		+ tss.getCurTransStartAmount().formattedStringValue()
          + res.getString(" and ") + tss.getCurTransEndAmount().formattedStringValue();
    }
    if (tss.getStartDate() != null && tss.getEndDate() != null) {
      sSearchedCriteria += ", "+res.getString("Date is between : ") 
      		+ tss.getDateFormat().format(tss.getStartDate())
      		+ res.getString(" AND ") + tss.getDateFormat().format(tss.getEndDate());
    }
    if (tss.getSku() != null && tss.getSku().length() > 0) {
      sSearchedCriteria += ", "+res.getString("Item SKU is: ") + tss.getSku();
      return sSearchedCriteria;
    } else {
      if (tss.getModel() != null && tss.getModel().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Item Model is: ") + tss.getModel();
      }
      if (tss.getStyle() != null && tss.getStyle().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Item Style is: ") + tss.getStyle();
      }
      if (tss.getSupplier() != null && tss.getSupplier().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Item Supplier is: ") + tss.getSupplier();
      }
      if (tss.getFabric() != null && tss.getFabric().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Item Fabric is: ") + tss.getFabric();
      }
      if (tss.getColor() != null && tss.getColor().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Item Color is: ") + tss.getColor();
      }
      if (tss.getYear() != null && tss.getYear().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Item Year is: ") + tss.getYear();
      }
      if (tss.getSeason() != null && tss.getSeason().length() > 0) {
        sSearchedCriteria += ", "+res.getString("Item Season is: ") + tss.getSeason();
      }
    }
    return sSearchedCriteria;
  }
}

