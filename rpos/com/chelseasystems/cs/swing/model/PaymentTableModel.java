/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import java.util.Vector;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.Calendar;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.store.CMSStore;


/**
 */
public class PaymentTableModel extends ScrollableTableModel {
  public static final int TYPE = 0;
  public static final int DETAIL = 1;
  public static final int EXPIRE = 2;
  public static final int APPROVAL = 3;
  public static final int AMOUNT = 4;
// Mayuri Edhara : fix for Change due name
  public boolean noShowChangeDueName = false;
  ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
	private String fipayGiftcardFlag=null;

  /**
   */
  public PaymentTableModel() {
	  // added by vishal for Fipay gc Integration 
	  String fileName = "store_custom.cfg";
	  ConfigMgr fipayConfig = new ConfigMgr(fileName);
	  fipayGiftcardFlag = fipayConfig.getString("FIPAY_GIFTCARD_INTEGRATION");
		 //Default value of the flag is N if its not present in store_custom.cfg
		if (fipayGiftcardFlag == null) {
			fipayGiftcardFlag = "N";
		}
		//end Vishal 4/oct/2016
    this.setColumnIdentifiers(new String[] {res.getString("Payment Type")
        , res.getString("Payment Detail"), res.getString("Expiration Date")
        , res.getString("Approval"), res.getString("Amount Tendered")
    });
  }

  /**
   * @param pay
   */
  public void addPayment(Payment pay) {
    addRow(pay);
    if (getTotalRowCount() > getRowsShown()) { // scroll down with rows
      nextPage();
    }
  }

  /**
   * @param row
   */
  public void removeRow(int row) {
    removeRowInPage(row);
  }

  /**
   * @param row
   * @return
   */
  public Payment getPayment(int row) {
    return (Payment)getRowInPage(row);
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return 5;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return false;
  }
  // Mayuri Edhara : fix for Change due name
  public void setNoShowChangeDueName(boolean value){
	  noShowChangeDueName = value;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = getCurrentPage();
    Payment pay = (Payment)vTemp.elementAt(row);
    Object obj = null;
    switch (column) {
      case TYPE:
        try {
        	//added below to show the card type based on the value in Fipay response as it is getting set in cc object in AJbVAlidation
        	  if(pay instanceof CreditCard ){
        		    CreditCard ccard =  (CreditCard)pay;
        	 return ccard.getGUIPaymentName();
        		 
        	  }
          if (pay instanceof Cash){
        	 // System.out.println("changeDue ===== " + noShowChangeDueName);
//PM: Issue #1474
           // return (pay.getAmount().lessThan(new ArmCurrency(0.0d))) ? res.getString("AddTender_Amount_Due_Change")
           // Lorenzo
          // fix for issue#  1882 Amount Due on the "Tender" screen
          //return (pay.getAmount().lessThan(new ArmCurrency(0.0d))) ? res.getString("Change_Due")
           //     : pay.getGUIPaymentName();
        	 //Mayuri Edhara : 04/19/2016
        	  // Fix for Change Due name to not show when the payment is loaded on payment applet as 
        	 // original (-ve)payment of the  return transaction.
        	  if(noShowChangeDueName && (pay.getAmount().lessThan(new ArmCurrency(0.0d)))){
        		return pay.getGUIPaymentName();
        	  }else if(pay.getAmount().lessThan(new ArmCurrency(0.0d))){
        		return res.getString("Change_Due");
        	  }else{
        		return pay.getGUIPaymentName();
        	  }
          }
          
        } catch(CurrencyException ce) {

        }

        //return  (pay instanceof IForeignPayment)?
        //         ((IForeignPayment)pay).getCurrencyTypeConvertedFrom().getDescription()
        //         : pay.getGUIPaymentName();
        return res.getString(pay.getGUIPaymentName());
      case DETAIL:
        return pay.getGUIPaymentDetail(); /*
                 if (pay instanceof CreditCard)
                 return  ((CreditCard)pay).getAccountNumber();
                 else if (pay instanceof BankCheck) {
                 StringBuffer buf = new StringBuffer();
                 // getDriversLicenseNumber() returns null on a business check
                 if (null != ((BankCheck)pay).getDriversLicenseNumber()) {
                 buf.append("ID#");
                 buf.append(((BankCheck)pay).getDriversLicenseNumber());
                 }
                 // print foreign currency information if necessary
                 ArmCurrency payAmt = pay.getAmount();
                 if (payAmt.isConverted()) {
                 if (buf.length() > 0)
                 buf.append(",  ");
                 buf.append(formatForeignCurrency(payAmt));
                 }
                 return  buf.toString();
                 }
                 else if (pay instanceof Redeemable)
                 return  ((Redeemable)pay).getId();
                 else if (pay instanceof Cash) {
                 ArmCurrency payAmt = pay.getAmount();
                 if (payAmt.isConverted()) {
                 return  formatForeignCurrency(payAmt);
                 }
                 }
                 break;
        */

      case EXPIRE:
        if (pay instanceof CreditCard) {
          Date dt = ((CreditCard)pay).getExpirationDate();
          if (dt!=null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            String year = cal.get(cal.YEAR) + "";
            if (year.length() == 1)
              year = "0" + year;
            if (year.length() == 4) {
              year = year.substring(2);
            }
            String month = (cal.get(cal.MONTH) + 1) + "";
            if (month.length() == 1)
              month = "0" + month;
            return month + "/" + year;
          }else
            return "";
        }
        break;
      case APPROVAL:
        if (pay instanceof CreditCard) {
          CreditCard cc = (CreditCard)pay;
          //Anjana added as  no authorization code was shown while returning
          if (pay.getAmount().doubleValue() < 0)
            return cc.getRespStatusCodeDesc();
          if (cc.getRespStatusCode() != null)
            return cc.getRespStatusCodeDesc();
          else
            return "";
        }
        if (pay instanceof BankCheck) {
          BankCheck chk = (BankCheck)pay;
          if (chk.getRespStatusCode() != null)
            return chk.getRespStatusCodeDesc();
          else
            return "";
        }
        //#bug 341 : For Reedemable
        if (pay instanceof CMSRedeemable) {
          CMSRedeemable storeValCard = (CMSRedeemable)pay;
          return storeValCard.getManualAuthCode();
        }
        //vishal added
        if(fipayGiftcardFlag!=null && fipayGiftcardFlag.equalsIgnoreCase("Y")){
        if(pay instanceof CMSStoreValueCard){
        	CMSStoreValueCard cmsstorevaluecard=(CMSStoreValueCard)pay;
        	 
        	 if (pay.getAmount().doubleValue() < 0)
                 return cmsstorevaluecard.getRespStatusCodeDesc();
               if (cmsstorevaluecard.getRespStatusCode() != null)
                 return cmsstorevaluecard.getRespStatusCodeDesc();
               else
                 return "";
        }
        if(pay instanceof CMSDueBill){
        	CMSDueBill cmsDueBill =(CMSDueBill) pay;
        	 if (pay.getAmount().doubleValue() < 0)
                 return cmsDueBill.getRespStatusCodeDesc();
               if (cmsDueBill.getRespStatusCode() != null)
                 return cmsDueBill.getRespStatusCodeDesc();
               else
                 return "";
        }
        if(pay instanceof CMSDueBillIssue){
        	CMSDueBillIssue cmsDueBillIssue =(CMSDueBillIssue) pay;
        	 if (pay.getAmount().doubleValue() < 0)
                 return cmsDueBillIssue.getRespStatusCodeDesc();
               if (cmsDueBillIssue.getRespStatusCode() != null)
                 return cmsDueBillIssue.getRespStatusCodeDesc();
               else
                 return "";
        }
        }
        //end 28 sept 2016
        return " ";
      case AMOUNT:
        return pay.getAmount().formattedStringValue();
      default:
        return " ";
    }
    return " ";
  }
}

