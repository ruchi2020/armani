/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 07-08-2005 | Vikram    | 406       | Display Barcode instead of SKU               |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 07-06-2005 | Vikram    | 346       |Transaction history discount display -        |
 |      |            |           |           |Txn Discount details to be TOTAL DISCOUNTS    |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 05-10-2005 | Anand     | N/A       |2. Modifications as per specifications for    |
 |      |            |           |           |   Txn History                                |
 -------------------------------------------------------------------------------------------|
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.ScrollProcessor;
import com.chelseasystems.cs.swing.model.TxnDetailModel;
import com.chelseasystems.cs.swing.model.DetailContainer;
import com.chelseasystems.cs.swing.transaction.TxnDetailApplet;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.pos.ViewLineItemAppModel;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.TransactionHeader;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cr.pos.VoidTransaction;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.payment.CMSPaymentCode;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSPresaleLineItem;
/**
 *
 * <p>Title:TxnDetailPanel </p>
 *
 * <p>Description: Panel for rendering the Txn Detail Information </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TxnDetailPanel extends JPanel implements ScrollProcessor, PositionableDisplayer {
  ResourceBundle res;
  JTextArea txtAudit;
  TxnDetailModel model;
  JCMSTable tblTxn;
  IApplicationManager theAppMgr;
  VariableRenderer taRenderer;
  private static ConfigMgr configMgr = new ConfigMgr("pos.cfg");
  private static String txnDetailPanelString = configMgr.getString("TRANSACTION.TXN_DETAIL_PANEL");

  /**
   */
  public TxnDetailPanel() {
    try {
      res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
      txtAudit = new JTextArea();
      model = new TxnDetailModel();
      tblTxn = new JCMSTable(model, JCMSTable.VIEW_ROW);
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    this.setLayout(new BorderLayout());
    this.add(txtAudit, BorderLayout.SOUTH);
    this.add(tblTxn, BorderLayout.CENTER);
    this.add(tblTxn.getTableHeader(), BorderLayout.NORTH);
    this.setPreferredSize(new Dimension(844, 405));
    txtAudit.setForeground(tblTxn.getForeground());
    txtAudit.setEditable(false);
    txtAudit.setMargin(new Insets(5, 5, 5, 5));
    txtAudit.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
    txtAudit.setLineWrap(true);
    txtAudit.setWrapStyleWord(true);
    txtAudit.setFont(new Font("SansSerif", 1, 12));
    //FontMetrics fm = txtAudit.getFontMetrics(txtAudit.getFont());
    //txtAudit.setPreferredSize(new Dimension(100, fm.getHeight()*10));
    txtAudit.setPreferredSize(new Dimension(10, 69));
    //txtAudit.setBorder(BorderFactory.createLineBorder(Color.black,1));
    //txtAudit.setText("Audit History:");
    taRenderer = new VariableRenderer();
    tblTxn.getColumnModel().getColumn(1).setCellRenderer(taRenderer);
    DefaultTableCellRenderer rndrMiddle = new DefaultTableCellRenderer();
    rndrMiddle.setHorizontalAlignment(SwingConstants.CENTER);
    //
    DefaultTableCellRenderer rndrRight = new DefaultTableCellRenderer();
    rndrRight.setHorizontalAlignment(SwingConstants.RIGHT);
    tblTxn.getColumnModel().getColumn(2).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(3).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(4).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(5).setCellRenderer(rndrRight);
    tblTxn.getColumnModel().getColumn(6).setCellRenderer(rndrRight);
    tblTxn.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        resetColumnWidths();
      }
    });
  }

  /**
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    tblTxn.setAppMgr(theAppMgr);
    txtAudit.setFont(theAppMgr.getTheme().getTextFieldFont());
    taRenderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    this.theAppMgr = theAppMgr;
  }

  /**
   * @param l mouse listener added to table
   */
  public void addMouseListener(java.awt.event.MouseListener l) {
    tblTxn.addMouseListener(l);
  }

  /**
   * Clears the model data
   */
  public void clear() {
    model.clear();
    txtAudit.setText("");
    repaint();
  }

  /**
   * This method populates return item list into this applet screen.
   */
  public void showTransaction(PaymentTransactionAppModel aTxn) {
    PaymentTransaction paymentTxn = aTxn.getPaymentTransaction();
    CMSStore store = (CMSStore)aTxn.getPaymentTransaction().getStore();
    model.clear();
    ViewLineItemAppModel[] lineItems = aTxn.getLineItemsAppModelArray();
    POSLineItem[] lineItem = aTxn.getLineItemsArray();
    //added by shushma for promotion
    if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
    CMSCompositePOSTransaction cmsCompositePOSTransaction=(CMSCompositePOSTransaction)paymentTxn;
    String promCode[]=cmsCompositePOSTransaction.getPromCode();
    if (lineItem != null && lineItem.length != 0) {
      for (int i = 0; i < lineItems.length; i++) {
    	  //Modified by deepika to handle NullPointerException for items without promotion code
    	  try {
    		  if (promCode[i]!= null)
			 {
			  lineItem[i].setPromotionCode(promCode[i]);
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    	  model.addContainer(getContainer(lineItems[i], lineItem[i]));
      }//promotion close
    }
    }
    else{
    	 if (lineItem != null && lineItem.length != 0) {
    	      for (int i = 0; i < lineItems.length; i++) {
    	    	  model.addContainer(getContainer(lineItems[i], lineItem[i]));
    	      }
    	    }
    }
    //discount
    model.addContainer(getContainer()); //blank line
    try {
      if (aTxn.getSaleReductionAmount().greaterThan(new ArmCurrency(0))) {
        //                if(isThereANamedDiscount(aTxn))
        //                    model.addContainer(getDiscountHeading());
        model.addContainer(getDiscountContainer(aTxn));
      }
    } catch (CurrencyException ce) {}
    //subtotal
    model.addContainer(getSubtotalContainer(aTxn));
    ArmCurrency tAmt = null;
    tAmt = aTxn.getCompositeTaxAmount();
    if (!(store.isVATEnabled()
        && (tAmt == null || tAmt.doubleValue() == 0))) {
      model.addContainer(getTaxContainer(aTxn));
    }
    tAmt = aTxn.getCompositeRegionalTaxAmount();
    if (!(store.isVATEnabled()
        && (tAmt == null || tAmt.doubleValue() == 0))
        && store.usesRegionalTaxCalculations()) {
      model.addContainer(getRegionalTaxContainer(aTxn));
    }
    model.addContainer(getTotalContainer(aTxn));
    model.addContainer(getContainer());
    //populate payments
    //if (aTxn instanceof PaymentTransaction) {
    // PaymentTransaction payTxn = (PaymentTransaction)aTxn;
    for (Enumeration e = aTxn.getPayments(); e.hasMoreElements(); ) {
      Payment pay = (Payment)e.nextElement();
      model.addContainer(getContainer(pay));
    }
    //}
    //add audit trail
    String text = aTxn.getAuditString();
    if (text == null || text.trim().length() == 0)
      txtAudit.setText("--- " + res.getString("No Audit History") + " ---");
    else
      txtAudit.setText(res.getString("Audit History") + ": " + text);
    model.firstPage();
    try {
      if (!aTxn.getTotalPaymentAmount().equals(aTxn.getCompositeTotalAmountDue())
          && !(aTxn.getPaymentTransaction() instanceof VoidTransaction)) {
        theAppMgr.showErrorDlg(res.getString("Prior system transaction: Inconsistent total transaction amount, use Manual Tax to adjust tax amount."));
      }
    } catch (CurrencyException ex1) {
      ex1.printStackTrace();
    }
  }

  /**
   */
  /*  this comes from the appmodel now     djr
   private String getAuditString (PaymentTransactionAppModel txn) {
   //temporary fix until the backend supports the logic of populating the auditHistory.
   if (txn.getPaymentTransaction() instanceof VoidTransaction){
   VoidTransaction voidTxn = (VoidTransaction)txn.getPaymentTransaction();
   String transVoidedString = "Void Of: " + voidTxn.getOriginalTransaction().getId() + " Reason: " + voidTxn.getReason();
   return transVoidedString;
   }
   String auditTrail = "";//txn.getAuditFlag();
   return  auditTrail;
   }
   */
  /**
   * @param txn
   * @return
   */
  private DetailContainer getSubtotalContainer(PaymentTransactionAppModel txn) {
    String[] row = new String[7];
    row[0] = "";
    row[1] = "";
    row[2] = "";
    row[3] = "";
    row[4] = "";
    row[5] = res.getString("Subtotal") + ":";
    //1217
    POSLineItem[] posLineItem = txn.getLineItemsArray();
    ArmCurrency totalReductiontotal = new ArmCurrency(0.0);
    ArmCurrency totalRetailValue = new ArmCurrency(0.0);
    if( (posLineItem != null) && posLineItem.length > 0 && (posLineItem[0] instanceof CMSPresaleLineItem)){
      for (int i = 0; i < posLineItem.length; i++){
        try {
          totalReductiontotal = totalReductiontotal.add(posLineItem[i].getExtendedReductionAmount());
          totalRetailValue = totalRetailValue.add(posLineItem[i].getExtendedRetailAmount());
          }catch (Exception ex){ex.printStackTrace();}
      }
      try {
        ArmCurrency temp = totalRetailValue.subtract(totalReductiontotal);
        row[6] = temp.formattedStringValue();
      }catch (Exception ex){}
    }else {
    row[6] = txn.getCompositeNetAmount().formattedStringValue();
    }
    DetailContainer container = new DetailContainer("SUBTOTAL", row);
    return (container);
  }

  /**
   * @param txn
   * @return
   */
  private DetailContainer getTaxContainer(PaymentTransactionAppModel txn) {
    String[] row = new String[7];
    if (txn.getTaxExemptId() != null && txn.getTaxExemptId().length() != 0) {
      row[0] = res.getString(txn.getTaxLabel().getKey()) + " " + res.getString("Exempt ID") + ":";
      row[1] = txn.getTaxExemptId();
    } else {
      row[0] = "";
      row[1] = "";
    }
    row[2] = "";
    row[3] = "";
    row[4] = "";
    row[5] = res.getString("Tax Amt") + ":";
    row[6] = (txn.getCompositeTaxAmount() == null ? ""
        : txn.getCompositeTaxAmount().formattedStringValue());
    DetailContainer container = new DetailContainer("TAX", row);
    return (container);
  }

  /**
   * @param txn
   * @return
   */
  private DetailContainer getRegionalTaxContainer(PaymentTransactionAppModel txn) {
    String[] row = new String[7];
    if (txn.getTaxExemptId() != null && txn.getTaxExemptId().length() != 0) {
      row[0] = res.getString(txn.getRegionalTaxLabel().getKey()) + " " + res.getString("Exempt ID")
          + ":";
      row[1] = txn.getRegionalTaxExemptId();
    } else {
      row[0] = "";
      row[1] = "";
    }
    row[2] = "";
    row[3] = "";
    row[4] = "";
    row[5] = res.getString(txn.getRegionalTaxLabel().getKey()) + ":";
    row[6] = txn.getCompositeRegionalTaxAmount().formattedStringValue();
    DetailContainer container = new DetailContainer("TAX", row);
    return (container);
  }

  /**
   * @param txn
   * @return
   */
  private DetailContainer getTotalContainer(PaymentTransactionAppModel txn) {
    String[] row = new String[7];
    row[0] = "";
    row[1] = "";
    row[2] = "";
    row[3] = "";
    row[4] = "";
    row[5] = res.getString("Total Amt") + ":";
    row[6] = txn.getTotalPaymentAmount().formattedStringValue();
    DetailContainer container = new DetailContainer("TOTAL", row);
    return (container);
  }

  /**
   * @param line
   * @return
   */
  private DetailContainer getContainer(ViewLineItemAppModel line, POSLineItem lineItem) {
    String[] row = new String[7];  
 	row[0] = ((CMSItem)line.getItem()).getBarCode() == null ? ""
        : ((CMSItem)line.getItem()).getBarCode();
 	//Added for dolci
  	if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
	String itemId = lineItem.getItem().getId();	
	//this will check whether item is dolci or not
	boolean flag = isDolciCandy(itemId);
	if(flag){
		try {
		HashMap dolciSkuValue =(HashMap)theAppMgr.getGlobalObject("DOLCI_ITEM_PRESENT");
		String key = getKeyFromValue(dolciSkuValue,itemId);
		int index = key.indexOf(".");
		String keyIndex = key.substring(index+1);
		String itemDesc = key.replace('.', '-');	
		lineItem.getItem().setDescription(itemDesc);
		} catch (BusinessRuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	  }
	}
	//Ended for dolci 
    row[1] = line.getItemDescription(); 	
    if (lineItem.getAdditionalConsultant() != null)
    	//PCR1326 View Transaction Details fix for Armani Japan
    	if (txnDetailPanelString == null) {
    		row[2] = lineItem.getAdditionalConsultant().getExternalID();
    	} else {
    		row[2] = lineItem.getAdditionalConsultant().getLastName() + " " +lineItem.getAdditionalConsultant().getFirstName();
    	}
    row[3] = line.getQuantity() + "";
    row[4] = line.getItemRetailPrice();
    //row[5] = line.getExtendedReductionAmount();
    //Fix for defect# 1252
    row[5] = TxnDetailApplet.getDiscountPercentString(lineItem);   
    row[6] = line.getExtendedNetAmount();
    String type = line.getType();
    DetailContainer container = new DetailContainer(type, row);
    return (container);
  }

  /**
   * @param payment
   * @return
   */
  private DetailContainer getContainer(Payment payment) {
    String[] row = new String[7];
    row[0] = payment.getGUIPaymentName();
    if (payment instanceof CreditCard) {
      CreditCard cc = (CreditCard)payment;
      if(cc.getAccountNumber() !=null
         &&
         //Fix for issue#1894 added trim
         // cc.getAccountNumber().length()>0)
         cc.getAccountNumber().trim().length()>0)
      {
      	row[1] = res.getString("Account No: x");
      	//Fix for issue#1894 added check when credit card number is less than 4 digit.
      	//added trim wherever taking the length of credit card because credit card number should not have spaces and if it has we have to trim them
      	if(cc.getAccountNumber().trim().length()<4){
      		//System.out.println("*********Short Credit Card ="+cc.getAccountNumber()+" Length ="+cc.getAccountNumber().length());
      		row[1] = row[1] +	 cc.getAccountNumber();
      	}else 
      		row[1] = row[1] +	cc.getAccountNumber().substring(cc.getAccountNumber().trim().length() - 4);
      }
      else
      {
          row[1] = getCardDetails(cc);
      }
      // Expiration date not needed.   alt 3-13-2000
      //if (cc.getExpirationDate() != null)
      //   row[2] = cc.getExpirationDate().toString();
      //else
      //   row[2] = "Unknown";
      row[2] = "";
      row[3] = "";
      row[4] = "";
      row[5] = "";
    } else if (payment instanceof Redeemable) {
      Redeemable r = (Redeemable)payment;
      row[1] = res.getString("Control ID: ") + r.getId();
      // Amount not needed.   alt 3-13-2000
      //row[2] = r.getIssueAmount().formattedStringValue();
      row[2] = "";
      row[3] = "";
      row[4] = "";
      row[5] = "";
    } else if (payment instanceof BankCheck) {
      BankCheck c = (BankCheck)payment;
      row[1] = res.getString("CHK #: ") + c.getCheckNumber();
      row[2] = "";
      row[3] = "";
      row[4] = "";
      row[5] = "";
    } else {
      row[1] = "";
      row[2] = "";
      row[3] = "";
      row[4] = "";
      row[5] = "";
    }
    row[6] = payment.getAmount().formattedStringValue();
    DetailContainer container = new DetailContainer("PAYMENT", row);
    return (container);
  }

  private String getCardDetails(CreditCard cc)
  {
    String sTmp="";
    String sCardPlan = cc.getCardPlanCode();
    String sCardType = cc.getPaymentCode();
    if(sCardType!=null && sCardType.length()>0)
    {
      java.util.List paymentCCodes = CMSPaymentMgr.getPaymentCode("CREDIT_CARD");
      java.util.List paymentDCodes = CMSPaymentMgr.getPaymentCode("DEBIT_CARD");
      int numPaymentCCodes = paymentCCodes.size();
      int numPaymentDCodes = paymentDCodes.size();
      int numPaymentCodes = numPaymentCCodes + numPaymentDCodes;

      ArrayList aPaymentCodes = new ArrayList(numPaymentCodes);
      aPaymentCodes.addAll(paymentCCodes);
      aPaymentCodes.addAll(paymentDCodes);

      CMSPaymentCode[] paymentCodes = (CMSPaymentCode[]) aPaymentCodes.toArray(new
          CMSPaymentCode[numPaymentCodes]);
      for (int iCtr = 0; iCtr < numPaymentCodes; iCtr++) {
        if (sCardType.equals(paymentCodes[iCtr].getPaymentCode())) {
          sTmp = paymentCodes[iCtr].getPaymentDesc();
          break;
        }
      }

      if (sCardType != null && sCardType.length() > 0
          &&
          sCardPlan != null && sCardPlan.length() > 0
          ) {
        ConfigMgr config = new ConfigMgr("ArmCreditCardPlans.cfg");
        if (config == null)return sTmp;
        String sCreditCardPlans = "CREDIT_CARD_" + sCardType +
            "_CARD_PLAN_"+sCardPlan+".LABEL";
        sCreditCardPlans = config.getString(sCreditCardPlans);
        if (sCreditCardPlans != null) {
//          StringTokenizer sTokens = new StringTokenizer(sCreditCardPlans, ",");
//          if (sTokens != null) {
//            while (sTokens.hasMoreTokens()) {
//              String sTmp1 = sTokens.nextToken();
//              String sCode = config.getString(sTmp1 + ".CODE");
//
//              if (sCardPlan.equals(sCode)) {
//                if (sTmp.length() > 1) sTmp += ", ";
//                sTmp += res.getString("Plan: ") +
//                    config.getString(sTmp1 + ".LABEL");
//                break;
//              }
//            }
//          }
          sTmp += " : "+ sCreditCardPlans;
        }

      }
    }

    return sTmp;
  }

  /**
   * put your documentation comment here
   * @return
   */
  private DetailContainer getDiscountHeading() {
    String[] row = new String[7];
    row[0] = res.getString("Discount Type");
    row[1] = res.getString("Discount Description");
    row[2] = "";
    row[3] = "";
    row[4] = "";
    row[5] = "";
    row[6] = "";
    return (new DetailContainer("", row));
  }

  /**
   * put your documentation comment here
   * @param theTxn
   * @return
   */
  private boolean isThereANamedDiscount(PaymentTransactionAppModel theTxn) {
    Discount[] discounts = theTxn.getDiscountsArray();
    if (discounts.length > 0)
      if (discounts[discounts.length - 1] != null)
        if (discounts[discounts.length - 1].getGuiLabel().length() > 0)
          return (true);
    return (false);
  }

  /**
   * put your documentation comment here
   * @param theTxn
   * @return
   */
  private DetailContainer getDiscountContainer(PaymentTransactionAppModel theTxn) {
    String[] row = new String[7];
    Arrays.fill(row, "");
    /*
     Discount[] discounts = theTxn.getDiscountsArray();
     Discount discount = null;
     if(discounts.length > 0)
     discount = discounts[discounts.length - 1];
     if(discount != null)
     {
     row[0] = discount.getGuiLabel();
     if(discount.getEmployee() != null)
     {
     Employee emp = discount.getEmployee();              // Bogus employee comes back from database
     StringBuffer nameBuf = new StringBuffer(30);
     if(null != emp.getFirstName())
     {
     nameBuf.append(emp.getFirstName());
     }
     if(nameBuf.length() > 0)
     {
     nameBuf.append(" ");
     }
     if(null != emp.getLastName())
     {
     nameBuf.append(emp.getLastName());
     }
     row[1] = nameBuf.toString();
     }
     else if(discount.getReason() !=null && discount.getReason().length() > 0)
     {
     row[1] = discount.getReason();
     }
     else if(discount.getCorporateId()!=null && discount.getCorporateId().length() > 0)
     {
     row[1] = discount.getCorporateId();
     }
     }
     */
    row[1] = res.getString("TOTAL DISCOUNTS");
    row[4] = res.getString("Disc Amount") + ":";
    row[5] = theTxn.getCompositeReductionAmount().formattedStringValue();
    DetailContainer container = new DetailContainer("DISCOUNT", row);
    return (container);
  }

  /**
   * @return
   */
  private DetailContainer getContainer() {
    String[] row = new String[7];
    row[0] = "";
    row[1] = "";
    row[2] = "";
    row[3] = "";
    row[4] = "";
    row[5] = "";
    row[6] = "";
    return (new DetailContainer("", row));
  }

  /**
   * @param header
   * @return
   */
  private boolean isTxnShowable(TransactionHeader header) {
    String txnType = header.getTransactionType();
    /*if ((txnType.equalsIgnoreCase("EOD")) || (txnType.equalsIgnoreCase("PHINV"))
     || (txnType.equalsIgnoreCase("EOS")) || (txnType.equalsIgnoreCase("RCPT"))
     || (txnType.equalsIgnoreCase("SOD")) || (txnType.equalsIgnoreCase("FCHG"))
     || (txnType.equalsIgnoreCase("FDEL")))
     return  false;
     else*/
    return (true);
  }

  /**
   * Resets the column width
   */
  public void resetColumnWidths() {
    model.setRowsShown(tblTxn.getHeight() / tblTxn.getRowHeight());
    model.setColumnWidth(tblTxn);
  }

  /**
   * Next page
   */
  public void nextPage() {
    model.nextPage();
  }

  /**
   * Prev page
   */
  public void prevPage() {
    model.prevPage();
  }

  /**
   * Gets the PageNumber (For Up/Down Functionality)
   * @return
   */
  public int getCurrentPageNumber() {
    return (model.getCurrentPageNumber());
  }

  /**
   * Gets the pageCount( For Up/Down functionality)
   * @return
   */
  public int getPageCount() {
    return (model.getPageCount());
  }

  /**
   * Gets the Audit Area
   * @return
   */
  public JTextArea getAuditArea() {
    return (txtAudit);
  }

  /**
   * put your documentation comment here
   * @param key
   */
  public void positionToKey(int key) {
    model.pageContainingRow(key);
  }

  /**
   * Gets the chosen row.
   * @return int
   */
  public int getPositionKey() {
    int positionKey = 0;
    if (model.getRowCount() > 0)
      positionKey = model.getRowsShown() * model.getCurrentPageNumber();
    return (positionKey);
  }

  /**
   *
   * <p>Title:VariableRenderer </p>
   *
   * <p>Description:Sets the panel look & feel </p>
   *
   * <p>Copyright: Copyright (c) 2005</p>
   *
   * <p>Company: </p>
   *
   * @author not attributable
   * @version 1.0
   */
  class VariableRenderer implements TableCellRenderer {
    JTextArea textAreaRenderer = new JTextArea();
    JLabel labelRenderer = new JLabel();

    /**
     * put your documentation comment here
     */
    public VariableRenderer() {
      textAreaRenderer.setLineWrap(false);
      //setWrapStyleWord(true);
      textAreaRenderer.setOpaque(true);
      textAreaRenderer.setForeground(new Color(0, 0, 175));
      labelRenderer.setForeground(new Color(0, 0, 175));
    }

    /**
     * put your documentation comment here
     * @param font
     */
    public void setFont(Font font) {
      labelRenderer.setFont(font);
      textAreaRenderer.setFont(font);
    }

    /**
     * put your documentation comment here
     * @param table
     * @param theValue
     * @param isSelected
     * @param cellHasFocus
     * @param row
     * @param col
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object theValue
        , boolean isSelected, boolean cellHasFocus, int row, int col) {
      if (theValue == null) {
        labelRenderer.setText("");
        return (labelRenderer);
      } else if (theValue.toString().indexOf("\n") > 0) {
        textAreaRenderer.setText(theValue.toString());
        return (textAreaRenderer);
      } else {
        labelRenderer.setText(theValue.toString());
        return (labelRenderer);
      }
    }
  }
  
  /**
   * This is method is add the skues of dolci in HashMap and also set in Golbalobject
   * @return boolean
   * @param ItemID
   * 
   * */
  private boolean isDolciCandy(String itemID){
       java.util.Map dolciSkuValue =(java.util.Map)theAppMgr.getGlobalObject("DOLCI_ITEM_PRESENT");
  	  java.util.List dolciSkuList = null;
  	  String  dolciValue = null;
  	  if(dolciSkuValue == null) {
            dolciSkuList = new ArrayList();
            dolciSkuValue = new HashMap();
            ConfigMgr mgr = new ConfigMgr("item.cfg");
        	  String dolciKeys = mgr.getString("DOLCI_CANDY_KEYS");
            StringTokenizer strTok = new StringTokenizer(dolciKeys, ",");
  	          while(strTok.hasMoreTokens()) {
  	                dolciSkuList.add(strTok.nextToken());
  	          }
            int length = dolciSkuList.size();
            if(length!=0){
            	for(int i=0;i<length;i++){
            		String key = dolciSkuList.get(i).toString() ;
            		String boxesKeys = mgr.getString(key+"_BOXES_KEYS");
            		if(boxesKeys!=null){
            		     StringTokenizer strToken = new StringTokenizer(boxesKeys, ",");
  	              	   while(strToken.hasMoreTokens()) {
  	              		String token = strToken.nextToken();
  	              		String newKey = key+"."+token;
  	              		dolciValue = mgr.getString(newKey);
  	              		dolciValue = dolciValue.trim();
  	              		//this is added all skues in hashmap.  	              		
  	              		dolciSkuValue.put(newKey,dolciValue);    
  	                }
            		}
            	 }
            }
            theAppMgr.addGlobalObject("DOLCI_ITEM_PRESENT",dolciSkuValue );       
           }
  	  return dolciSkuValue.containsValue(itemID);
  }
  /**
   * This is method which retrived the key from value in Dolci Candy.
   * HashMap is set in Application for Dolci candy 
   * Value is ItemId from EditArea Event.
   * @author vivek sawant
   * */
  	 public String getKeyFromValue(HashMap hashmap,String value){
  		Set ref = hashmap.keySet();
  		Iterator it = ref.iterator();
  		
  		while (it.hasNext()) {
  			 String key = (String)it.next();
  			 String values =(String)hashmap.get(key);
  			  if(values.equals(value)) {
  		        return key;
  		    }
  		  }
  		 return null;
  	 }


}
