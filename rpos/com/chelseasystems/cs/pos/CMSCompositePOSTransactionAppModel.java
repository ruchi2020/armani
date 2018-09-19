/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import com.chelseasystems.cr.appmgr.BrowserManager;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.customer.CustomerAppModel;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.employee.EmployeeAppModel;
import com.chelseasystems.cr.layaway.LayawayPaymentInfo;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.LayawayTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.POSLineItemGrouping;
import com.chelseasystems.cr.pos.POSTransaction;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.Reduction;
import com.chelseasystems.cr.pos.ReturnLineItem;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cr.pos.SaleLineItemDetail;
import com.chelseasystems.cr.pos.ShippingRequest;
import com.chelseasystems.cr.receipt.ReceiptLocaleSetter;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.tax.ValueAddedTax;
import com.chelseasystems.cr.tax.ValueAddedTaxDetail;
import com.chelseasystems.cr.util.ResourceBundleKey;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerAppModel;
import com.chelseasystems.cs.discount.CMSEmployeeDiscount;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeAppModel;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.returns.ReturnReasonCfgMgr;
import com.chelseasystems.cs.swing.returns.ReturnReasonStruct;
import com.chelseasystems.cs.tax.CMSSaleTaxDetail;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.rb.ReceiptFactory;
import com.chelseasystems.cs.payment.HouseAccount;


/**
 */
public class CMSCompositePOSTransactionAppModel extends CMSPaymentTransactionAppModel implements
    java.io.Serializable {
  static final long serialVersionUID = -276089377054122104L;
  private CMSCompositePOSTransaction compositePOSTransaction;
  private boolean printingMerchandiseReceipt;
  private boolean isPrintingVatInvoice;
  private boolean isPrintingRetailExportReceipt;
  static ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  private SimpleDateFormat dateFormat = null;
  private ShippingRequest currentShippingRequest;
  private ArmCurrency originalVatAmount;
  private boolean isSourceFromTxnDetailApplet = false;
  private static final int totalLength = 42;

  
  // Optimization for issue #8 while in GA_JP
  private static GroupedReceiptLine[] itemsArray = null;
  private static GroupedReceiptLine[] itemsArrayForAlterations = null;
  private static GroupedReceiptLine[] itemsArrayForOpenDeposit = null;

  //Vivek Mishra : Added for signature display
  private String signLocation=null;
  //Ends
  
  /**
   */
  public CMSCompositePOSTransactionAppModel() {
    dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
 }

  /**
   * @param    CompositePOSTransaction compositePOSTransaction
   */
  public CMSCompositePOSTransactionAppModel(CompositePOSTransaction compositePOSTransaction) {
    dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
    this.compositePOSTransaction = (CMSCompositePOSTransaction)compositePOSTransaction;
  }

  /**
   * @return
   */
  public PaymentTransaction getPaymentTransaction() {	  
   return (compositePOSTransaction);
  }

   
  
  /**
   * put your documentation comment here
   * @return
   */
  public boolean getContainsRedeemablePayment() {
    Payment[] payments = getPaymentTransaction().getPaymentsArray();
    for (int index = 0; index < payments.length; index++) {
      if (payments[index] instanceof Redeemable && !(payments[index] instanceof HouseAccount)) {
        return true;
      }
    }
    return false;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSRedeemableBalanceAppModel[] getRedeemablesArray() {
    PaymentTransaction txn = getPaymentTransaction();
    Vector payments = new Vector();
    Enumeration e = txn.getPayments();
    while (e.hasMoreElements()) {
      Payment payment = (Payment)e.nextElement();
      if (payment instanceof Redeemable && !(payment instanceof HouseAccount)) {
        payments.addElement(new CMSRedeemableBalanceAppModel((Redeemable)payment));
      }
    }
    return (CMSRedeemableBalanceAppModel[])payments.toArray(new CMSRedeemableBalanceAppModel[0]);
  }

  /**
   *
   * @return
   */
  public String getAuditString() {
    StringBuffer auditSB = new StringBuffer();
    // if there are return line items, attempt to get the original tran id for audit
    // history.  David doesn't support this yet, so the audit history is returned
    // as blank (it breaks down when it trys to get the returnlineitemdetails; they
    // aren't restored from the db).  David will fix this in 2.5 and then this will
    // start working.    djr
    Enumeration returnLines = compositePOSTransaction.getReturnLineItems();
    if (returnLines != null && returnLines.hasMoreElements()) {
      CMSReturnLineItem firstReturnLine = (CMSReturnLineItem)returnLines.nextElement();
      Enumeration returnLineDetails = firstReturnLine.getLineItemDetails();
      if (returnLineDetails != null && returnLineDetails.hasMoreElements()) {
        CMSReturnLineItemDetail firstReturnLineDetail = (CMSReturnLineItemDetail)returnLineDetails.
            nextElement();
        SaleLineItemDetail firstSaleLineItemDetail = firstReturnLineDetail.getSaleLineItemDetail();
        if (firstSaleLineItemDetail != null) {
          POSLineItem firstOriginalLine = firstSaleLineItemDetail.getLineItem();
          if (firstOriginalLine != null) {
            POSTransaction originalTxn = firstOriginalLine.getTransaction();
            if (originalTxn != null) {
              if (originalTxn.getCompositeTransaction() != null) {
                auditSB.append(res.getString("Return of transaction "));
                auditSB.append(originalTxn.getCompositeTransaction().getId());
                auditSB.append("\n");
              } else
                System.out.println("composite txn is null");
            } else
              System.out.println("original txn is null");
          } else
            System.out.println("first original line is null");
        } else
          System.out.println("no return sale line detail");
      } else
        System.out.println("no return line details");
      auditSB.append(res.getString("Reason"));
      auditSB.append(": ");
      try {
        //auditSB.append(ReturnReasonCfgMgr.getReturnReason(firstReturnLine.getReasonId()));
    	//Defect# 1430: View transaction: Return Reason code (e.g. returns.cfg.Damaged) message bundle key displayed
        auditSB.append(res.getString(ReturnReasonCfgMgr.getReturnReason(firstReturnLine.getReasonId()).toString()));
      } catch (Exception e) {
        auditSB.append(" ");
      }
      if (firstReturnLine.getComments() != null && firstReturnLine.getComments().length() > 0) {
        auditSB.append("\n");
        auditSB.append(res.getString("Comments"));
        auditSB.append(": ");
        auditSB.append(firstReturnLine.getComments());
      }
      auditSB.append(" ");
    }
    Enumeration saleLines = compositePOSTransaction.getSaleLineItems();
    if (saleLines != null) {
      OutOfEntireLoop:while (saleLines.hasMoreElements()) {
        SaleLineItem saleLineItem = (SaleLineItem)saleLines.nextElement();
        Enumeration lineDetails = saleLineItem.getLineItemDetails();
        if (lineDetails != null) {
          while (lineDetails.hasMoreElements()) {
            POSLineItemDetail lineDetail = (POSLineItemDetail)lineDetails.nextElement();
            if (lineDetail instanceof SaleLineItemDetail)
              if (((SaleLineItemDetail)lineDetail).isReturned()) {
                auditSB.append(res.getString("Sale Contains Returned Items") + " ");
                break OutOfEntireLoop;
              }
          }
        }
      }
    }
    if (compositePOSTransaction.getLayawayTransaction() != null)
      if (compositePOSTransaction.getLayawayTransaction().getLayaway() != null) {
        LayawayPaymentInfo[] paymentInfo = compositePOSTransaction.getLayawayTransaction().
            getLayaway().getPaymentInfo();
        String comma = "";
        for (int i = 0; i < paymentInfo.length; i++) {
          auditSB.append(comma + res.getString(" Payment ") + (i + 1) + res.getString(" on ")
              + dateFormat.format(paymentInfo[i].getDate()) + " "
              + paymentInfo[i].getAmount().formattedStringValue());
          comma = ", ";
        }
        if (compositePOSTransaction.getLayawayTransaction().getLayaway().isRTS())
          auditSB.append(res.getString("Layaway has been Returned To Stock" + " "));
      }
    if (compositePOSTransaction.getLoyaltyCard() != null)
      auditSB.append("\n" + res.getString("Loyalty card: ")
          + compositePOSTransaction.getLoyaltyCard().getLoyaltyNumber());
    auditSB.append("\n");
    auditSB.append(super.getAuditString());
    return (auditSB.toString());
  }

  /**
   * @return
   */
  public ResourceBundleKey getReceiptLabel() {
    ;
    try {
      int returnLineCount = compositePOSTransaction.getReturnLineItemsArray().length;
      int saleLineCount = compositePOSTransaction.getSaleLineItemsArray().length;
      int layawayLineCount = compositePOSTransaction.getLayawayLineItemsArray().length;
      int consignmentLineCount = compositePOSTransaction.getConsignmentLineItemsArray().length;
      int presaleLineCount = compositePOSTransaction.getPresaleLineItemsArray().length;
      int reservationLineCount = compositePOSTransaction.getReservationLineItemsArray().length;
      ArmCurrency totalPaymentAmount = compositePOSTransaction.getTotalPaymentAmount();
      ArmCurrency totalSaleAmountDue = compositePOSTransaction.getSaleTotalAmountDue();
      ArmCurrency totalReturnAmountDue = compositePOSTransaction.getReturnTotalAmountDue();
      boolean isConsignmentClose = compositePOSTransaction.isConsignmentClose();
      boolean isPresaleClose = compositePOSTransaction.isPresaleClose();
      boolean isReservationClose = compositePOSTransaction.isReservationClose();
      if (isPrintingRetailExportReceipt) {
        return (new ResourceBundleKey("RETAIL EXPORT_ra"));
      } else if (printingMerchandiseReceipt) {
        return (new ResourceBundleKey("MERCHANDISE TAG_ra"));
      } else if (isPrintingVatInvoice) {
        return (new ResourceBundleKey("VAT_ra"));
      } else if (this.hasEmployeeDiscount()) {
        return (new ResourceBundleKey("ASSOCIATE PURCHASE_ra"));
      } else if (returnLineCount > 0 && returnLineCount == saleLineCount
          && totalPaymentAmount.doubleValue() == 0
          && totalSaleAmountDue.equals(totalReturnAmountDue)) {
        return (new ResourceBundleKey("EVEN EXCHANGE_ra"));
      } else if (layawayLineCount > 0) {
        return (new ResourceBundleKey("LAYAWAY_ra"));
      } else if (consignmentLineCount > 0) {
        return (new ResourceBundleKey("CONSIGNMENT_ra"));
      } else if (presaleLineCount > 0) {
        return (new ResourceBundleKey("PRESALE_ra"));
      } else if (reservationLineCount > 0) {
        return (new ResourceBundleKey("RESERVATION_OPEN_ra"));
      } else if (isConsignmentClose) {
        return (new ResourceBundleKey("CONSIGNMENT_CLOSE_ra"));
      } else if (isPresaleClose) {
        return (new ResourceBundleKey("PRESALE_CLOSE_ra"));
      } else if (isReservationClose) {
        return (new ResourceBundleKey("RESERVATION_CLOSE_ra"));
      } else if (returnLineCount == 0 && saleLineCount > 0) {
        if (compositePOSTransaction.hasShippingRequests()) {
          return (new ResourceBundleKey("SEND SALE_ra"));
        } else {
          return (new ResourceBundleKey("SALE_ra"));
        }
      } else if (returnLineCount > 0 && saleLineCount == 0) {
        return (new ResourceBundleKey("RETURN_ra"));
      } else {
        return (new ResourceBundleKey("SALE AND RETURN_ra"));
      }
    } catch (Exception e) {
      System.out.println("Exception on getReceiptLabel: " + e);
      e.printStackTrace();
      return new ResourceBundleKey("");
    }
  }

  //Fix for defect# 1601: Sales Receipt for Send Sale needs fine tuning
  public String getFormattedReceiptLabel() {	  
	  String receiptLabel = res.getString(getReceiptLabel().getKey());	  
	  int numberOfSpaces = ((totalLength - receiptLabel.length()) / 2);	  
	  String prefix = "";
	  String postfix = "";
	  for(int i = 0; i < numberOfSpaces; i++) {
		  prefix += " ";
		  postfix += " ";
	  }
	  receiptLabel = prefix + receiptLabel + postfix;	  
	  return receiptLabel;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean hasEmployeeDiscount() {
    Discount[] txnDiscounts = compositePOSTransaction.getDiscountsArray();
    if (txnDiscounts != null && txnDiscounts.length > 0) {
      for (int i = 0; i < txnDiscounts.length; i++) {
        if (txnDiscounts[i] instanceof CMSEmployeeDiscount) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Employee getPurchaseEmployee() {
    Discount[] txnDiscounts = compositePOSTransaction.getDiscountsArray();
    if (txnDiscounts != null && txnDiscounts.length > 0) {
      for (int i = 0; i < txnDiscounts.length; i++) {
        if (txnDiscounts[i] instanceof CMSEmployeeDiscount) {
          return ((CMSEmployeeDiscount)txnDiscounts[i]).getEmployee();
        }
      }
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getOriginalConsignmentOpenTransactionId() {
    for (Enumeration em = compositePOSTransaction.getSaleLineItems(); em.hasMoreElements(); ) {
      CMSSaleLineItem line = (CMSSaleLineItem)em.nextElement();
      if (line.getConsignmentLineItem() != null) {
        return line.getConsignmentLineItem().getTransaction().getCompositeTransaction().getId();
      }
    }
    for (Enumeration em = compositePOSTransaction.getReturnLineItems(); em.hasMoreElements(); ) {
      CMSReturnLineItem line = (CMSReturnLineItem)em.nextElement();
      if (line.getConsignmentLineItem() != null) {
        return line.getConsignmentLineItem().getTransaction().getCompositeTransaction().getId();
      }
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getOriginalPresaleOpenTransactionId() {
    for (Enumeration em = compositePOSTransaction.getSaleLineItems(); em.hasMoreElements(); ) {
      CMSSaleLineItem line = (CMSSaleLineItem)em.nextElement();
      if (line.getPresaleLineItem() != null) {
        return line.getPresaleLineItem().getTransaction().getCompositeTransaction().getId();
      }
    }
    for (Enumeration em = compositePOSTransaction.getReturnLineItems(); em.hasMoreElements(); ) {
      CMSReturnLineItem line = (CMSReturnLineItem)em.nextElement();
      if (line.getPresaleLineItem() != null) {
        return line.getPresaleLineItem().getTransaction().getCompositeTransaction().getId();
      }
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[] getOriginalSaleTransactionIdArray() {
    Set set = new TreeSet();
    POSLineItem[] lines = this.compositePOSTransaction.getReturnLineItemsArray();
    for (int i = 0; i < lines.length; i++) {
      POSLineItem saleLine = ((ReturnLineItem)lines[i]).getSaleLineItem();
      if (saleLine != null) {
        set.add(saleLine.getTransaction().getCompositeTransaction().getId());
      }
    }
    return (String[])set.toArray(new String[0]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getOriginalSaleTransactionId() {
    String[] array = this.getOriginalSaleTransactionIdArray();
    if (array == null || array.length == 0) {
      return null;
    } else {
      return array[0];
    }
  }

  /**
   *
   * @return
   */
  public StringWrapper[] getReturnComments() {
    Enumeration e = compositePOSTransaction.getReturnLineItems();
    if (e != null && e.hasMoreElements()) {
      ReturnLineItem line = (ReturnLineItem)e.nextElement();
      String comments = line.getComments();
      if (comments.length() == 0)
        return (null);
      else {
        StringTokenizer st = new StringTokenizer(comments);
        ArrayList commentsList = new ArrayList();
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens()) {
          String nextWord = st.nextToken();
          if (nextWord.length() + sb.length() > 41) {
            commentsList.add(new StringWrapper(sb.toString()));
            sb = new StringBuffer();
          }
          sb.append(nextWord + " ");
        }
        commentsList.add(new StringWrapper(sb.toString()));
        return (StringWrapper[])commentsList.toArray(new StringWrapper[commentsList.size()]);
      }
    } else
      return (null);
  }

  public class StringWrapper {
    private String theString;

    /**
     *
     * @param       String theString
     */
    public StringWrapper(String theString) {
      this.theString = theString;
    }

    /**
     *
     * @return
     */
    public String getTheString() {
      return (theString);
    }
  }


  /**
   * @return
   */
  public CustomerAppModel getCustomerAppModel() {
    return (new CMSCustomerAppModel((CMSCustomer)compositePOSTransaction.getCustomer()));
  }

  /**
   * @return
   */
  public EmployeeAppModel getConsultantAppModel() {
    return (new CMSEmployeeAppModel((CMSEmployee)compositePOSTransaction.getConsultant()));
  }

  /**
   * @return
   */
  public Employee getConsultant() {
    return (compositePOSTransaction.getConsultant());
  }

  /**
   * @return
   */
  public Customer getCustomer() {
    if (compositePOSTransaction.getCustomer() != null) {
      return compositePOSTransaction.getCustomer();
    } else {
      return super.getCustomer();
    }
  }

  /**
   * Returns Double Byte names for customer
   * @return String
   */
  public String getDoubleByteCustomerName() {
    String sNameSeperator = res.getString("NAME_SEPERATOR");
    if(sNameSeperator.equals("NAME_SEPERATOR")) sNameSeperator = ",";
    if (compositePOSTransaction.getCustomer() != null) {
      CMSCustomer cmsCustomer =  (CMSCustomer) compositePOSTransaction.getCustomer();
      String sName = "";
      if(cmsCustomer.getDBFirstName()!=null &&
         cmsCustomer.getDBFirstName().indexOf("null")==-1)
      sName +=    cmsCustomer.getDBFirstName();

    if(cmsCustomer.getDBLastName()!=null &&
         cmsCustomer.getDBLastName().indexOf("null")==-1)
      {
        if(sName.length()>1) sName += sNameSeperator;
        sName += cmsCustomer.getDBLastName();
      }
      return sName;
    }
    return "";
  }

  /*
   * method to reset the cached arrays
   */
  private static void resetGroupLineArrays() {
      // Reset the arrays to make sure that we have uptodate information in them
      itemsArray = null;
      itemsArrayForAlterations = null;
      itemsArrayForOpenDeposit = null;
  }
  /**
   *
   * @param theAppMgr
   */
  public void printReceipt(IApplicationManager theAppMgr) {
    resetGroupLineArrays();
    printReceipts(theAppMgr, true, true);
  }

  /**
   *
   * @param theAppMgr
   */
  public void rePrintReceipt(IApplicationManager theAppMgr) {
	
    // Commented as Armani does not want to print a seperate signature copy .. issue #825
    //boolean sigs = (getSignaturePaymentsArray().length > 0 || isDiscountSignatureRequired())
    //        && (theAppMgr.showOptionDlg("Store copy?", "Do you also want to print a payment signature copy?"));
    resetGroupLineArrays();
    printReceipts(theAppMgr, false, false);
  }

  /**
   *
   * @param theAppMgr
   */
  public void printCancelReceipt(IApplicationManager theAppMgr) {
       resetGroupLineArrays();
	if (compositePOSTransaction.getLineItemsArray().length > 0) {  
    if (compositePOSTransaction.getLayawayLineItemsArray().length == 0) {
      printTxnReceipt(theAppMgr, ReceiptBlueprintInventory.CMSCompositePOSTransaction_Sale_Cancel, true);
    } else {
      printTxnReceipt(theAppMgr, ReceiptBlueprintInventory.CMSLayaway_Cancel, true);
    }
  }
  }

  /**
   *
   * @param theAppMgr
   */
  public void printPostFailedReceipt(IApplicationManager theAppMgr) {
    resetGroupLineArrays();
    printTxnReceipt(theAppMgr, ReceiptBlueprintInventory.CMSCompositePOSTransaction_Post_Failed, true);
  }

  /**
   *
   * @param theAppMgr
   */
  public void printSuspendedReceipt(IApplicationManager theAppMgr) {
    resetGroupLineArrays();
    if (compositePOSTransaction.getLayawayLineItemsArray().length == 0) {
      printTxnReceipt(theAppMgr, ReceiptBlueprintInventory.CMSCompositePOSTransaction_Sale_Suspend, true);
    } else {
      printTxnReceipt(theAppMgr, ReceiptBlueprintInventory.CMSLayaway_Suspend, true);
    }
  }

  /**
   *
   * @return
   */
  public String getReturnReason() {
    ReturnReasonStruct returnReasonStruct = getReturnReasonStruct();
    if (returnReasonStruct != null)
      return (returnReasonStruct.reason);
    return (null);
  }

  /**
   *
   * @return
   */
  public ReturnReasonStruct getReturnReasonStruct() {
    Enumeration e = compositePOSTransaction.getReturnLineItems();
    if (e != null && e.hasMoreElements()) {
      ReturnLineItem line = (ReturnLineItem)e.nextElement();
      try {
        return (ReturnReasonCfgMgr.getReturnReason(line.getReasonId()));
      } catch (Exception ex) {
        System.out.println("Exception on getting Return Reason from ReturnReasonCfgMgr:  " + ex);
        return (null);
      }
    }
    return (null);
  }

  /**
   * @return
   */
  public Enumeration getDiscounts() {
    return (compositePOSTransaction.getDiscounts());
  }

  /**
   */
  public boolean isDiscountSignatureRequired() {
    for (Enumeration enm = getDiscounts(); enm.hasMoreElements(); ) {
      Discount d = (Discount)enm.nextElement();
      if (d.isSignatureRequired()) {
        return (true);
      }
    }
    return (false);
  }

  /**
   * @return
   */
  public ArmCurrency getCompositeNetAmount() {
    return (compositePOSTransaction.getCompositeNetAmount());
  }

  public ArmCurrency getCompositeNetAmountWithoutOpenDeposit() {
    ArmCurrency compositeNetAmountWithoutOpenDeposit = getCompositeNetAmount();
    try {
      compositeNetAmountWithoutOpenDeposit = compositeNetAmountWithoutOpenDeposit.subtract(getCompositeOpenDepositAmount());
    	  compositeNetAmountWithoutOpenDeposit.subtract(getCompositeOpenDepositAmount());
    } catch (CurrencyException ex) {
      ex.printStackTrace();
    }
    return compositeNetAmountWithoutOpenDeposit;
  }
  
  public ArmCurrency getCompositeOpenDepositAmount() {
    GroupedReceiptLine[] groupedReceiptLine = GroupedReceiptLine.createGroupLineItemArrayToPrintForOpenDeposit(compositePOSTransaction);
    ArmCurrency compositeOpenDepositAmt = new ArmCurrency(0);
    if (groupedReceiptLine.length > 0) {
       for(int i=0; i<groupedReceiptLine.length; i++) {
         try {
           compositeOpenDepositAmt = compositeOpenDepositAmt.add(groupedReceiptLine[i].getExtendedSellingPrice());
         } catch (CurrencyException ex) {
           ex.printStackTrace();
         }
       }
    }
    return compositeOpenDepositAmt;
  }
  
  public GroupedReceiptLine[] getDepositLineItems() {
    POSLineItem[] posLineItems = this.compositePOSTransaction.getLineItemsArray();
    ArrayList list = new ArrayList();
    
    for (int i = 0; i < posLineItems.length; i++) {
      if (LineItemPOSUtil.isDeposit(posLineItems[i].getMiscItemId())) {
    	  list.addAll(Arrays.asList(GroupedReceiptLine.createGroupedReceiptLine(posLineItems[i])));
      }
    }
    return (GroupedReceiptLine[]) list.toArray(new GroupedReceiptLine[list.size()]);
  }
  
  public ArmCurrency getCompositeBalanceAmountAfterOpenDeposit() {
    ArmCurrency compositeBalanceAmountAfterOpenDeposit = getCompositeNetAmountWithoutOpenDeposit();
    try {
      //Fix for 1642: Reservation Receipt: We want to print 0 on a reservation receipt if 
      //remaining balance is negative value.
      compositeBalanceAmountAfterOpenDeposit = compositeBalanceAmountAfterOpenDeposit.subtract(getCompositeOpenDepositAmount());
      if (isHavingOpenDeposit()) {
    	  if (compositeBalanceAmountAfterOpenDeposit.doubleValue() < 0) {
    		  return new ArmCurrency(0.0d);
    	  }
      }
    } catch (CurrencyException ex) {
      ex.printStackTrace();
    }
    return compositeBalanceAmountAfterOpenDeposit;
  }

  /**
   * @return
   */
  public ArmCurrency getCompositeRetailAmount() {
    return (compositePOSTransaction.getCompositeRetailAmount());
  }

  /**
   * @return
   */
  public ArmCurrency getCompositeReductionAmount() {
    return (compositePOSTransaction.getCompositeReductionAmount());
  }

  /**
   * @return
   */
  public ArmCurrency getCompositeTaxAmount() {
    if (isVatEnabled())
      return null;
   // try {
	//getStateTaxAmount();
	//} catch (CurrencyException e) {
		// TODO Auto-generated catch block
	//	e.printStackTrace();
//} 
  /*  try {
		getCityTaxAmount();
	} catch (CurrencyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
    
    return (compositePOSTransaction.getCompositeTaxAmount());
  }
  
  public ArmCurrency getCityTaxAmount() throws CurrencyException {
	    if (isVatEnabled())
	      return null;
	    else
	    	if(getStateTaxAmount()!=null){
	    		if(compositePOSTransaction.getCompositeTotalAmountDue().equals(new ArmCurrency(0.0))){
	    			return new ArmCurrency(0.0);
	    		}
		  return (compositePOSTransaction.getCompositeTaxAmount().absoluteValue().subtract(getStateTaxAmount().absoluteValue()));
	    	}
	    	else 
	    		return null;
	  }

  public ArmCurrency getStateTaxAmount() throws CurrencyException {
	  
	//  System.out.println("compositePOSTransaction.getTransactionType()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+compositePOSTransaction.getTransactionType());
	//  System.out.println("compositePOSTransaction.getTaxableLineItemDetailsArray().length>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+compositePOSTransaction.getTaxableLineItemDetailsArray().length);
	  
	  ArmCurrency statetax = new ArmCurrency(0.0) ;
	  CMSStore cmsStore = ((CMSStore)compositePOSTransaction.getStore());
	  POSLineItemDetail [] posLineItemDetails = compositePOSTransaction.getTaxableLineItemDetailsArray();
	  
	  if(compositePOSTransaction.getStore().getCountry().equalsIgnoreCase("USA") && compositePOSTransaction.getStore().getState().equalsIgnoreCase("PR")){
		if(compositePOSTransaction.getTransactionType().contains("RETN")){
			POSLineItem[] lineitem = compositePOSTransaction.getLineItemsArray();
			POSLineItem[] itms =  null;
			CMSCompositePOSTransaction OrgSaleTxn = null;
			
			for(int i =0 ; i<lineitem.length ;i++){
			    if(lineitem[i] instanceof CMSReturnLineItem ){
				     CMSReturnLineItem rtnLnItm = (CMSReturnLineItem) lineitem[i];
				     CMSReturnLineItemDetail rtnLnItmDtl = (CMSReturnLineItemDetail) rtnLnItm.getLineItemDetailsArray()[0];
				     CMSSaleLineItemDetail saleLnItmDtl = (CMSSaleLineItemDetail) rtnLnItmDtl.getSaleLineItemDetail();
				     if(saleLnItmDtl!=null){
					     CompositePOSTransaction txnObject = saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction();
					     ArmCurrency amtDue = new ArmCurrency(0.0d);
					     amtDue = compositePOSTransaction.getCompositeTotalAmountDue();
					     OrgSaleTxn = ((CMSCompositePOSTransaction) txnObject);
					     break;
				     }
			    }
			}
			
			if(OrgSaleTxn == null){
				//System.out.println("No reciept return ::: ");			
			 
			    if (isVatEnabled())
			      return null;
			    
			    POSLineItem itm = null;
			 	ArmCurrency saleAmt = new ArmCurrency(0.0) ;
			    ArmCurrency itemStateTax = new ArmCurrency(0.0) ;
			    ArmCurrency itemCityTax = new ArmCurrency(0.0) ;
			    int lineItemLength = compositePOSTransaction.getTaxableLineItemDetailsArray().length;
			    for(int k =0 ; k<lineItemLength ; k++){			
					saleAmt = saleAmt.add ((posLineItemDetails[k].getLineItem().getNetAmount()));
					itm = posLineItemDetails[k].getLineItem();
					itemStateTax = itm.getNetAmount().multiply(cmsStore.getStateTax());
					//Vivek Mishra : Added to resolve wrong tax printing issue in case of no receipt return(sale-return combination)
					if(posLineItemDetails[k] instanceof CMSReturnLineItemDetail)
					{
						statetax = (statetax.subtract(itemStateTax.absoluteValue())).absoluteValue();
					}
					//Ends
					else{
					statetax = statetax.add(itemStateTax.absoluteValue());
					}
					itm.setStateTax(itemStateTax.absoluteValue());
					itemCityTax = itm.getTaxAmount().subtract(itm.getStateTax());
					itm.setCityTax(itemCityTax.absoluteValue());					
			    }
			    
			} else {
				//System.out.println("Return with original sale ::::  ");
				 
				itms = OrgSaleTxn.getLineItemsArray();
				for(int i=0;i<lineitem.length;i++){
					for(POSLineItem itm : itms){
						if((itm.getItem().getId() == lineitem[i].getItem().getId()) && (itm.getTaxAmount().equals(lineitem[i].getTaxAmount().absoluteValue()))){
							if(itm.getStateTax() != null){
								statetax = statetax.add(itm.getStateTax().absoluteValue());
								lineitem[i].setStateTax(itm.getStateTax().absoluteValue());
							}
							if(itm.getCityTax() != null){
								lineitem[i].setCityTax(itm.getCityTax().absoluteValue());
							}
								break;
							
						}
						else  if(!(itm.getItem().getId() == lineitem[i].getItem().getId())){
							if(cmsStore.getStateTax() != null){
								statetax = (statetax.subtract((lineitem[i].getNetAmount()).multiply(cmsStore.getStateTax()))).absoluteValue();
								lineitem[i].setStateTax(lineitem[i].getNetAmount().multiply(cmsStore.getStateTax()));
								lineitem[i].setCityTax(lineitem[i].getTaxAmount().subtract(lineitem[i].getStateTax()));
							}
							break;
						}
					}
				}
			}
			return statetax;
		}	
		else if(compositePOSTransaction.getTaxableLineItemDetailsArray().length == 0)
		{
			POSLineItem[] itms = compositePOSTransaction.getLineItemsArray();
			for(POSLineItem itm : itms){
				//Anjana: null pointer is needed for non taxable items
				if(itm.getStateTax()!=null){
					statetax = statetax.add(itm.getStateTax().absoluteValue());
				}
			}
			return statetax;
		}
		else{
			
			    if (isVatEnabled())
			      return null;
			   
			    ArmCurrency saleAmt = new ArmCurrency(0.0) ;
			    ArmCurrency shippingStatetax = null ;
			    ArmCurrency itemStateTax = null ;
			    ArmCurrency itemCityTax = new ArmCurrency(0.0) ;
			    POSLineItem itm = null;
			    Double shipItemStateTax;
			    ArmCurrency shipLineItemStateTax = null;
			    
			   int lineItemLength = compositePOSTransaction.getTaxableLineItemDetailsArray().length;
			   
			   for(int i =0 ; i<lineItemLength ; i++){
		    	try {
		    		if((posLineItemDetails[i].getLineItem() instanceof SaleLineItem) && ((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest()!=null){
		    			shipItemStateTax =  ((CMSShippingRequest) (((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest())).getStateTax();
		    			if( ((CMSShippingRequest) (((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest())).getState().equalsIgnoreCase("PR")){
			    			saleAmt = new ArmCurrency(0.0) ;
			    			shippingStatetax =  new ArmCurrency(0.0) ;
			    			
			    			if(shipItemStateTax == null){
			    				shipLineItemStateTax = compositePOSTransaction.getStateTax();			    			
			    			}
			    			
			    			if(shipItemStateTax!=null || shipLineItemStateTax!=null ){
			    				itm = posLineItemDetails[i].getLineItem();
			    				
				    			if(shipItemStateTax!=null){
				    				itemStateTax = (itm.getNetAmount()).multiply(shipItemStateTax.doubleValue());
				    			}
				    			
				    			if(itemStateTax == null){
				    				if(shipLineItemStateTax!=null){
				    					itemStateTax = (itm.getNetAmount()).multiply(shipLineItemStateTax.doubleValue());
				    				}
				    			}
				    			
				    			if(itemStateTax!=null){
					    			shippingStatetax = shippingStatetax.add(itemStateTax);
					    			itm.setStateTax(itemStateTax);
				    			}
				    			
				    			itemCityTax = itm.getTaxAmount().subtract(itm.getStateTax()); 
				    			itm.setCityTax(itemCityTax);
			    			}
		    			}else{
			    			saleAmt = null;
			    			shippingStatetax = null;
		    			}
		    		}else{
						//saleAmt = saleAmt.add ((posLineItemDetails[i].getLineItem().getItemSellingPrice()));
			    		saleAmt = saleAmt.add ((posLineItemDetails[i].getLineItem().getNetAmount()));	
						itm = posLineItemDetails[i].getLineItem();
						//itemStateTax = itm.getItemSellingPrice().multiply(cmsStore.getStateTax());
						itemStateTax = itm.getNetAmount().multiply(cmsStore.getStateTax());
						itm.setStateTax(itemStateTax);
		    			itemCityTax = itm.getTaxAmount().subtract(itm.getStateTax());
		    			itm.setCityTax(itemCityTax);
		    		}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }
			   try {
		    	if(shippingStatetax!=null){
		    		statetax = (saleAmt.multiply(cmsStore.getStateTax())).add(shippingStatetax);
		    	}else{
		    		if(saleAmt!=null){
		    			statetax = (saleAmt.multiply(cmsStore.getStateTax()));
		    		}
		    	}
			   } catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			   }		   
		   		return statetax;
			}
		}else{
			return null;
		}
	  }
  /**
   * put your documentation comment here
   * @return
   */
  public boolean isVatEnabled() {
    return new ConfigMgr("vat.cfg").getString("VAT_ENABLED").equalsIgnoreCase("TRUE");
  }

  /**
   * Programatically return a null, because the object doesn't "believe"
   * in a null value, but the receipts need a null here to know not to print it.
   * @return
   */
  public ArmCurrency getCompositeRegionalTaxAmount() {
    return (compositePOSTransaction.getStore().usesRegionalTaxCalculations()
        ? compositePOSTransaction.getCompositeRegionalTaxAmount() : null);
  }

  /**
   * @return
   */
  public ArmCurrency getCompositeTotalAmountDue() {
    return (compositePOSTransaction.getCompositeTotalAmountDue());
  }
  public ArmCurrency getCompositeTotalAmountDueWithoutOpenDeposit() {
    ArmCurrency compositeTotalAmountDueWithoutOpenDeposit = getCompositeTotalAmountDue();
    try {
      compositeTotalAmountDueWithoutOpenDeposit = compositeTotalAmountDueWithoutOpenDeposit.subtract(getCompositeOpenDepositAmount());
    } catch (CurrencyException ex) {
      ex.printStackTrace();
    }
    return compositeTotalAmountDueWithoutOpenDeposit;
  }

  /**
   * @return
   */
  public ArmCurrency getSaleNetAmount() {
    return (compositePOSTransaction.getSaleNetAmount());
  }

  /**
   * @return
   */
  public ArmCurrency getSaleReductionAmount() {
    return (compositePOSTransaction.getSaleReductionAmount());
  }

  /**
   * @return
   */
  public ArmCurrency getSaleTaxAmount() {
    return (compositePOSTransaction.getSaleTaxAmount());
  }

  /**
   * Return Formatted Date string for a said format.
   * @param sFormat String
   * @return String
   */
/*
  public String getFormattedSaleDate() {
    try {
       return dateFormat.format(compositePOSTransaction.getSubmitDate());
    }
    catch (Exception e) {
      System.out.println("****Exception formatting Transaction date**** CMSCompositePOSTransactionAppModel-> getFormattedSaleDate()");
    }
    return compositePOSTransaction.getSubmitDate().toString();
  }
*/
  /**
   * Return Process Date string for a said format.
   * @return String
   */
/*
  public String getProcessDate() {
    try {
       return dateFormat.format(compositePOSTransaction.getProcessDate());
    }
    catch (Exception e) {
      System.out.println("****Exception process Transaction date**** CMSCompositePOSTransactionAppModel-> getProcessDate()");
    }
    return compositePOSTransaction.getProcessDate().toString();
  }
*/
  /**
   * @return
   */
  public ArmCurrency getSaleTotalAmountDue() {
    return (compositePOSTransaction.getSaleTotalAmountDue());
  }

  /**
   * @return
   */
  public POSLineItem[] getLineItemsArray() {
    return (compositePOSTransaction.getLineItemsArray());
  }

  /**
   * @return
   */
  public double getLayawayDepositPercent() {
    LayawayTransaction layawayTxn = this.compositePOSTransaction.getLayawayTransaction();
    return (layawayTxn.getDepositPercent() * 100d);
  }

  /**
   * @return
   */
  public double getLayawayRestockingPercent() {
    LayawayTransaction layawayTxn = this.compositePOSTransaction.getLayawayTransaction();
    return (layawayTxn.isRestockingFree() ? 0d : layawayTxn.getRestockingPercent() * 100d);
  }

  /**
   * @return
   */
  public Integer getLayawayPaymentFrequency() {
    LayawayTransaction layawayTxn = this.compositePOSTransaction.getLayawayTransaction();
    return (layawayTxn.getPaymentFrequency());
  }

  /**
   * @return
   */
  public boolean isLayawayRestockingFree() {
    LayawayTransaction layawayTxn = this.compositePOSTransaction.getLayawayTransaction();
    return (layawayTxn.isRestockingFree());
  }

  /**
   * @return
   */
  public Date getLayawayFinalPmtDue() {
    ConfigMgr config = new ConfigMgr("layaway.cfg");
    String offset = config.getString("PAYOFF_DAYS");
    Date currDate = compositePOSTransaction.getProcessDate();
    int mustpay;
    try {
      mustpay = Integer.parseInt(offset);
    } catch (Exception e) {
      mustpay = 60;
    }
    Calendar calendarPlusOffset = new GregorianCalendar();
    calendarPlusOffset.setTime(currDate);
    calendarPlusOffset.add(calendarPlusOffset.DATE, mustpay);
    return (calendarPlusOffset.getTime());
  }

  /**
   * @return
   */
  public String getTaxExemptId() {
    return (compositePOSTransaction.getTaxExemptId());
  }

  /**
   * @return
   */
  public String getRegionalTaxExemptId() {
    return (compositePOSTransaction.getRegionalTaxExemptId());
  }

  /**
   * @param id
   * @exception BusinessRuleException
   */
  public void setRegionalTaxExemptId(String id)
      throws BusinessRuleException {
    compositePOSTransaction.setRegionalTaxExemptId(id);
  }

  /**
   * @param id
   * @exception BusinessRuleException
   */
  public void setTaxExemptId(String id)
      throws BusinessRuleException {
    compositePOSTransaction.setTaxExemptId(id);
  }

  /**
   *
   * @return
   */
  public ShippingRequest getCurrentShippingRequest() {
    return (currentShippingRequest);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public GroupedReceiptLine[] getShippingRequestLinesToPrintOnReceipt() {
    List list = new ArrayList();
    if (currentShippingRequest != null) {
      CMSShippingRequest ship = (CMSShippingRequest)currentShippingRequest;
      POSLineItem[] lines = ship.getLineItemsArray();
      for (int i = 0; i < lines.length; i++) {
        list.add(GroupedReceiptLine.createGroupedReceiptLine(lines[i])[0]);
      }
      POSLineItem[] conlines = ship.getConsignmentLineItemsArray();
      for (int i = 0; i < conlines.length; i++) {
        list.add(GroupedReceiptLine.createGroupedReceiptLine(conlines[i])[0]);
      }
      POSLineItem[] prelines = ship.getPresaleLineItemsArray();
      for (int i = 0; i < prelines.length; i++) {
        list.add(GroupedReceiptLine.createGroupedReceiptLine(prelines[i])[0]);
      }
    }
    return (GroupedReceiptLine[])list.toArray(new GroupedReceiptLine[0]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isHavingShippingRequests() {
    return this.compositePOSTransaction.hasShippingRequests();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSShippingRequest[] getShippingRequestArray() {
    List list = new ArrayList();
    if (compositePOSTransaction.hasShippingRequests()) {
      ShippingRequest[] shippingRequests = compositePOSTransaction.getShippingRequestsArray();
      for (int i = 0; i < shippingRequests.length; i++) {
        list.add(shippingRequests[i]);
      }
    }
    return (CMSShippingRequest[])list.toArray(new CMSShippingRequest[0]);
  }

  /////////////////////////////////////////////////////////////
  //
  //  Protected Methods
  //
  /////////////////////////////////////////////////////////////
  /**
   *
   * @param theAppMgr
   * @param localeSetter
   * @param isFirstPrint
   * @param isPrintSigCopies
   */
  protected void printDuebillReceipts(IApplicationManager theAppMgr
      , ReceiptLocaleSetter localeSetter, boolean isFirstPrint, boolean isPrintSigCopies) {
    ReceiptFactory receiptFactory;
    Object[] arguments = {this
    };
    if (getDueBillIssuePaymentsArray().length > 0) {
      printTxnReceipt(theAppMgr, ReceiptBlueprintInventory.CMSDueBillIssue, isFirstPrint);
    }
  }

  /**
   *
   * @param theAppMgr
   * @param localeSetter
   * @param isFirstPrint
   * @param isPrintSigCopies
   */
  protected void printReturnReceipts(IApplicationManager theAppMgr
      , ReceiptLocaleSetter localeSetter, boolean isFirstPrint, boolean isPrintSigCopies) {
    ReceiptFactory receiptFactory;
    Object[] arguments = {this
    };
    ReturnReasonStruct returnReasonStruct = getReturnReasonStruct();
    if (returnReasonStruct != null && returnReasonStruct.printMerchandiseReceipt) {
      printingMerchandiseReceipt = true;
      printTxnReceipt(theAppMgr, ReceiptBlueprintInventory.CMSCompositePOSTransaction_MerchTag
          , isFirstPrint);
      printingMerchandiseReceipt = false;
    }
  }

  /**
   *
   * @param theAppMgr
   * @param localeSetter
   * @param isFirstPrint
   * @param isPrintSigCopies
   */
  protected void printShippingReceipts(IApplicationManager theAppMgr
      , ReceiptLocaleSetter localeSetter, boolean isFirstPrint, boolean isPrintSigCopies) {
    if (compositePOSTransaction.hasShippingRequests()) {
      ShippingRequest[] shippingRequests = compositePOSTransaction.getShippingRequestsArray();
      for (int i = 0; i < shippingRequests.length; i++) {
        CMSShippingRequest cmsShipReq = (CMSShippingRequest)shippingRequests[i];

        POSLineItem posLineItem = null;
        if(cmsShipReq.getLineItems().hasMoreElements())
          posLineItem = (POSLineItem)cmsShipReq.getLineItems().nextElement();

        if ((posLineItem != null
                && posLineItem instanceof CMSSaleLineItem
                && ((CMSSaleLineItem)posLineItem).getConsignmentLineItem() == null)
            || cmsShipReq.getConsignmentLineItems().hasMoreElements()
            || cmsShipReq.getPresaleLineItems().hasMoreElements()) {
          currentShippingRequest = shippingRequests[i];
          printTxnReceipt(theAppMgr, ReceiptBlueprintInventory.CMSCompositePOSTransaction_Shipping
              , isFirstPrint);
        }
      }
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean hasAlternations() {
    boolean b = false;
    POSLineItemDetail[] lineDetails = compositePOSTransaction.getLineItemDetailsArray();
    if (lineDetails == null || lineDetails.length == 0) {
      return false;
    }
    for (int i = 0; i < lineDetails.length; i++) {
      if (lineDetails[i] instanceof CMSSaleLineItemDetail) {
        CMSSaleLineItemDetail saleDetail = (CMSSaleLineItemDetail)lineDetails[i];
        AlterationLineItemDetail[] alternationsArray = saleDetail.getAlterationLineItemDetailArray();
        if (alternationsArray != null && alternationsArray.length > 0) {
          return true;
        }
      }
    }
    return b;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @param localeSetter
   * @param isFirstPrint
   * @param isPrintSigCopies
   */
  protected void printAlterationReceipts(IApplicationManager theAppMgr
      , ReceiptLocaleSetter localeSetter, boolean isFirstPrint, boolean isPrintSigCopies) {
    if (!this.hasAlternations()) {
      return;
    }
    printTxnReceipt(theAppMgr, ReceiptBlueprintInventory.CMSAlterationReceipt, isFirstPrint);
  }

  /**
   *
   * @param theAppMgr
   * @param isFirstPrint
   * @param isPrintSigCopies
   */
  protected void printReceipts(IApplicationManager theAppMgr, boolean isFirstPrint
      , boolean isPrintSigCopies) {
	  
	if("JP".equalsIgnoreCase(Version.CURRENT_REGION) && compositePOSTransaction.isReservationClose()){
	
		  compositePOSTransaction.setPrintingReservationCloseReceipt(true);
		  
  	} else{
  		compositePOSTransaction.setPrintingReservationCloseReceipt(false);
  	}    
	  
    setRetailExportOriginalVat();
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(compositePOSTransaction.getStore()
        , getCustomer());
    System.out.println("in Duplicate receipts >>>>>>>>>>>>>>."+localeSetter);
    printLayawayReceipts(theAppMgr, localeSetter, isFirstPrint, isPrintSigCopies);
    printDuebillReceipts(theAppMgr, localeSetter, isFirstPrint, isPrintSigCopies);
    printReturnReceipts(theAppMgr, localeSetter, isFirstPrint, isPrintSigCopies);
    printShippingReceipts(theAppMgr, localeSetter, isFirstPrint, isPrintSigCopies);
    printAlterationReceipts(theAppMgr, localeSetter, isFirstPrint, isPrintSigCopies);
    printNewReservationReceipts(theAppMgr, localeSetter, isFirstPrint, isPrintSigCopies);
    
    compositePOSTransaction.setPrintingReservationCloseReceipt(false);
  }

  /**
   *
   * @param theAppMgr
   * @param localeSetter
   * @param isFirstPrint
   * @param isPrintSigCopies
   */
  protected void printNewReservationReceipts(IApplicationManager theAppMgr
      , ReceiptLocaleSetter localeSetter, boolean isFirstPrint, boolean isPrintSigCopies) {
    ReceiptFactory receiptFactory;

    if (compositePOSTransaction.getNewReservationOpenTxn()==null) {
      // No New Reservation Transaction
    } else {
    	
    	if("JP".equalsIgnoreCase(Version.CURRENT_REGION) && compositePOSTransaction.isReservationClose()){
    		resetGroupLineArrays();
    	}      	
      // Print a New Reservation Transaction
      Object[] arguments = {new CMSCompositePOSTransactionAppModel(compositePOSTransaction.getNewReservationOpenTxn())};
      receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSCompositePOSTransaction_Sale);
      localeSetter.setLocale(receiptFactory);
      if (isFirstPrint) {
        receiptFactory.print(theAppMgr);
      } else {
        receiptFactory.reprint(theAppMgr);
      }
    }
}

  /**
   *
   * @param theAppMgr
   * @param localeSetter
   * @param isFirstPrint
   * @param isPrintSigCopies
   */
  protected void printLayawayReceipts(IApplicationManager theAppMgr
      , ReceiptLocaleSetter localeSetter, boolean isFirstPrint, boolean isPrintSigCopies) {
    ReceiptFactory receiptFactory;
    Object[] arguments = {this
    };
    if (compositePOSTransaction.getLayawayLineItemsArray().length == 0) {
      receiptFactory = new ReceiptFactory(arguments
          , ReceiptBlueprintInventory.CMSCompositePOSTransaction_Sale);
      localeSetter.setLocale(receiptFactory);
      if (isFirstPrint) {
        receiptFactory.print(theAppMgr);
      } else {
        // Use a different receipt blueprint for reprints (CMSCompositePOSTransaction_Sale_Dup) as only one copy
        // is needed instead of the 3 copies printed in the first print
    	//  System.out.println("printing duplicate reciepts>>>>>>>>>>>>>>>>.");
        receiptFactory = new ReceiptFactory(arguments
            , ReceiptBlueprintInventory.CMSCompositePOSTransaction_Sale_Dup);
       // System.out.println("printing duplicate reciepts>>>>>>>222>>>>>>>>>.");
        localeSetter.setLocale(receiptFactory);
        receiptFactory.reprint(theAppMgr);
      }
      // Will never go below in re-print case and isPrintSigCopies is always false for Armani
      if ((isFirstPrint && (getSignaturePaymentsArray().length > 0 || isDiscountSignatureRequired()))
          || (!isFirstPrint && isPrintSigCopies)) {
    	 // System.out.println("222222222222222222");
        receiptFactory = new ReceiptFactory(arguments
            , ReceiptBlueprintInventory.CMSCompositePOSTransaction_Sale_Sigs);
        localeSetter.setLocale(receiptFactory);
        if (isFirstPrint) {
          receiptFactory.print(theAppMgr);
        } else {
        	//  System.out.println("33333333333333");
          receiptFactory.reprint(theAppMgr);
        }
      }
    } else {
      receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSLayaway);
      localeSetter.setLocale(receiptFactory);
      if (isFirstPrint) {
        receiptFactory.print(theAppMgr);
      } else {
    	 // System.out.println("444444444444444444");
        receiptFactory.reprint(theAppMgr);
      }
      if ((getSignaturePaymentsArray().length > 0 || isDiscountSignatureRequired())
          && isPrintSigCopies) {
        receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSLayaway_Sigs);
        localeSetter.setLocale(receiptFactory);
        if (isFirstPrint) {
          receiptFactory.print(theAppMgr);
        } else {
        	//System.out.println("5555555555555555555555");
          receiptFactory.reprint(theAppMgr);
        }
      }
    }
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printVATInvoice(IApplicationManager theAppMgr) {
    this.isPrintingVatInvoice = true;
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = null;
    ArmCurrency value = getSaleTotalAmountDue();
    try {
      if (value.greaterThan(com.chelseasystems.cs.tax.CMSValueAddedTaxHelper.
          getValueTooHighToPrintInvoice())) {
        receiptFactory = new ReceiptFactory(arguments
            , ReceiptBlueprintInventory.CMSNotIssuedVATInvoice);
        theAppMgr.showErrorDlg(res.getString(
            "The Sale amount was too large to print a VAT invoice, please call the home office."));
      } else if (this.getSignaturePaymentsArray().length > 0) {
        receiptFactory = new ReceiptFactory(arguments
            , ReceiptBlueprintInventory.CMSNotIssuedVATInvoice);
        theAppMgr.showErrorDlg(res.getString(
            "Unable to print a VAT invoice, please call the home office."));
      } else if (((CMSCompositePOSTransaction)this.getPaymentTransaction()).isPostAndPack()) {
        theAppMgr.showErrorDlg(res.getString(
            "VAT invoice is not allowed for Post and Pack transactions."));
        return;
      } else {
        receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSVATInvoice);
      }
    } catch (Exception e) {
      receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSVATInvoice);
    }
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(getPaymentTransaction().getStore()
        , getCustomer());
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
    this.isPrintingVatInvoice = false;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printRetailExportReceipt(IApplicationManager theAppMgr) {
    this.isPrintingRetailExportReceipt = true;
    Object[] arguments = {this
    };
    if (((CMSCompositePOSTransaction)this.getPaymentTransaction()).isPostAndPack()) {
      theAppMgr.showErrorDlg(res.getString(
          "Retail Export is not allowed for Post and Pack transactions."));
      return;
    }
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSRetailExportReceipt);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(getPaymentTransaction().getStore()
        , getCustomer());
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
    this.isPrintingRetailExportReceipt = false;
  }

  /////////////////////////////////////////////////////////////
  //
  //  Private Methods
  //
  /////////////////////////////////////////////////////////////
  /**
   *
   * @param theAppMgr
   * @param receiptName
   */
  private void printTxnReceipt(IApplicationManager theAppMgr, String receiptName
      , boolean isFirstPrint) {
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(compositePOSTransaction.getStore()
        , getCustomer());
    ReceiptFactory receiptFactory = new ReceiptFactory(new Object[] {this
    }, receiptName);
    localeSetter.setLocale(receiptFactory);
    if (isFirstPrint)
      receiptFactory.print(theAppMgr);
    else{
    //	System.out.println("666666666666666666666666");
      receiptFactory.reprint(theAppMgr);
  }}

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getRetailExportTotalVat() {
    ArmCurrency rtnVal = new ArmCurrency(0.00);
    POSLineItemWrapper[] array = getRetailExportLineItemsArray();
    for (int idx = 0; idx < array.length; idx++) {
      try {
        rtnVal = rtnVal.add(array[idx].getActualVATAmount().round()).round();
      } catch (CurrencyException ce) {
        ce.printStackTrace();
      }
    }
    return (rtnVal);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getRetailExportOriginalVat() {
    return originalVatAmount;
  }

  /**
   * put your documentation comment here
   * @return
   * @exception CurrencyException
   */
  public ArmCurrency getRetailExportOriginalVatLessTotalVat()
      throws CurrencyException {
    return getRetailExportOriginalVat().subtract(getRetailExportTotalVat());
  }

  /**
   * put your documentation comment here
   */
  private void setRetailExportOriginalVat() {
    if (!isVatEnabled())
      return;
    if (originalVatAmount != null)
      return;
    try {
      ValueAddedTax tax = com.chelseasystems.cs.tax.CMSValueAddedTaxHelper.getOriginalValueAddedTax(
          BrowserManager.getInstance(), (CMSCompositePOSTransaction)compositePOSTransaction
          , (CMSStore)compositePOSTransaction.getStore()
          , (CMSStore)compositePOSTransaction.getStore(), compositePOSTransaction.getProcessDate());
      ValueAddedTaxDetail[] taxDetails = tax.getValueAddedTaxDetailArray();
      POSLineItemDetail[] taxableLineItemDetails = compositePOSTransaction.getLineItemDetailsArray();
      originalVatAmount = new ArmCurrency(0);
      if (taxDetails.length != taxableLineItemDetails.length)
        throw new ArrayIndexOutOfBoundsException("Unable to apply taxes.  Index out of range.");
      for (int i = 0; i < taxDetails.length; i++) {
        //System.out.println("adding to orig vat: " + taxDetails[i].getAmount().round().formattedStringValue());
        if (taxableLineItemDetails[i].getLineItem().getItem().getVatRate() != null
            && taxableLineItemDetails[i].getLineItem().getItem().getVatRate().doubleValue() > 0)
          if (taxableLineItemDetails[i].getLineItem() instanceof ReturnLineItem)
            originalVatAmount = originalVatAmount.subtract(taxDetails[i].getAmount().round());
          else
            originalVatAmount = originalVatAmount.add(taxDetails[i].getAmount().round());
      }
      //System.out.println("orig vat calcs to: " + originalVatAmount.formattedStringValue());
    } catch (Exception e) {
      ((IApplicationManager)BrowserManager.getInstance()).showExceptionDlg(e);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItemWrapper[] getRetailExportLineItemsArray() {
    Vector rtnVal = new Vector();
    POSLineItem[] items = compositePOSTransaction.getLineItemsArray();
    for (int idx = 0; idx < items.length; idx++) {
      //if(!((CMSItem)items[idx].getItem()).isConcessionItem() && !((CMSItem)items[idx].getItem()).getVatCode().equals("0"))
      if (((CMSItem)items[idx].getItem()).getVatRate() != null
          && ((CMSItem)items[idx].getItem()).getVatRate().doubleValue() > 0) {
        rtnVal.add(items[idx]);
      }
    }
    Set set = new HashSet();
    for (Iterator it = rtnVal.iterator(); it.hasNext(); ) {
      POSLineItem line = (POSLineItem)it.next();
      set.add(line.getLineItemGrouping());
    }
    return (getWrappersForGroupings((POSLineItemGrouping[])set.toArray(new POSLineItemGrouping[0])));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public VatDetail[] getRetailExportVatSummaries() {
    POSLineItem[] items = compositePOSTransaction.getLineItemsArray();
    HashMap vatDetails = new HashMap();
    for (int idx = 0; idx < items.length; idx++) {
      //if(!((CMSItem)items[idx].getItem()).isConcessionItem() && !((CMSItem)items[idx].getItem()).getVatCode().equals("0"))
      if (((CMSItem)items[idx].getItem()).getVatRate() != null
          && ((CMSItem)items[idx].getItem()).getVatRate().doubleValue() > 0) {
        int signMult = 1;
        if (items[idx] instanceof ReturnLineItem)
          signMult = -1;
        double rate = items[idx].getItem().getVatRate().doubleValue();
        if (vatDetails.get(((CMSItem)items[idx].getItem()).getVatRate()) == null) {
          VatDetail detail = new VatDetail(items[idx].getExtendedNetAmount().multiply(signMult)
              , items[idx].getExtendedVatAmount().multiply(signMult), rate
              , items[idx].getNetAmount().multiply(rate / (1 + rate)).multiply(signMult).round().
              multiply(items[idx].getQuantity().intValue()), items[idx].getQuantity().intValue());
          // to do:  need a new vat service at item level rather than calcing right here,  vatAmount = lineItemDetail.getNetAmount().multiply((1.0 - creditCardRatio * CREDIT_CARD_CHARGE_RATE) *  rate / (1.0 + rate));
          vatDetails.put(items[idx].getItem().getVatRate(), detail);
        } else {
          VatDetail detail = (VatDetail)vatDetails.get(((CMSItem)items[idx].getItem()).getVatRate());
          detail.addCompositeAmount(items[idx].getExtendedNetAmount().multiply(signMult));
          detail.addVatAmount(items[idx].getExtendedVatAmount().multiply(signMult));
          detail.addOriginalVatAmount(items[idx].getNetAmount().multiply(rate / (1 + rate)).
              multiply(signMult).round().multiply(items[idx].getQuantity().intValue()));
          detail.addItemQty(items[idx].getQuantity().intValue());
        }
      }
    }
    return (VatDetail[])vatDetails.values().toArray(new VatDetail[0]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getRetailExportAmountDue() {
    ArmCurrency rtnVal = new ArmCurrency(0.00);
    POSLineItemWrapper[] array = getRetailExportLineItemsArray();
    for (int idx = 0; idx < array.length; idx++) {
      try {
        rtnVal = rtnVal.add(array[idx].getSubTotal().round()).round();
      } catch (CurrencyException ce) {
        ce.printStackTrace();
      }
    }
    return (rtnVal);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getRetailExportDiscountAmount() {
    ArmCurrency rtnVal = new ArmCurrency(0.00);
    POSLineItemWrapper[] array = getRetailExportLineItemsArray();
    for (int idx = 0; idx < array.length; idx++) {
      try {
        rtnVal = rtnVal.add(array[idx].getCompositeDiscountAmount().round()).round();
      } catch (CurrencyException ce) {
        ce.printStackTrace();
      }
    }
    return (rtnVal);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getRetailExportCompositeAmountDue() {
    return (getRetailExportAmountDue());
  }

  /**
   * put your documentation comment here
   * @param groups
   * @return
   */
  protected POSLineItemWrapper[] getWrappersForGroupings(POSLineItemGrouping[] groups) {
    Vector rtnVal = new Vector();
    for (int idx = 0; idx < groups.length; idx++) {
      POSLineItemGrouping grp = groups[idx];
      POSLineItemDetail[] details = grp.getLineItemDetailsArray();
      for (int dIdx = 0; dIdx < details.length; dIdx++) {
        POSLineItemWrapper wrap = new POSLineItemWrapper(details[dIdx]);
        for (Iterator it = rtnVal.iterator(); it.hasNext(); ) {
          if (((POSLineItemWrapper)it.next()).addWrapper(wrap)) {
            wrap = null;
            break;
          }
        }
        if (wrap != null) {
          rtnVal.add(wrap);
        }
      }
    }
    return ((POSLineItemWrapper[])rtnVal.toArray(new POSLineItemWrapper[0]));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public GroupedReceiptLine[] getReceiptLineItem() {
    if (itemsArray == null) {
        itemsArray = GroupedReceiptLine.createGroupLineItemArrayToPrint((CMSCompositePOSTransaction)this.
                compositePOSTransaction);
    }

    return itemsArray;
  }
  
  public GroupedReceiptLine[] getReceiptLineItemForAlteration() {
    if (itemsArrayForAlterations == null) {
        itemsArrayForAlterations = GroupedReceiptLine.createGroupLineItemArrayToPrintForAlteration((CMSCompositePOSTransaction)this.
        compositePOSTransaction);
  }

    return itemsArrayForAlterations;
  }

  public GroupedReceiptLine[] getReceiptLineItemForDeposit() {
    if (itemsArrayForOpenDeposit == null) {
        itemsArrayForOpenDeposit = GroupedReceiptLine.createGroupLineItemArrayToPrintForOpenDeposit((CMSCompositePOSTransaction)this.
                compositePOSTransaction);
    }

    return itemsArrayForOpenDeposit;
  }
  /**
   * put your documentation comment here
   * @return
   */
  public String getLoyaltyNumber() {
    if (compositePOSTransaction.getLoyaltyCard() != null)
      return compositePOSTransaction.getLoyaltyCard().getLoyaltyNumber();
    else
      return null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getLoyaltyType() {
    if (compositePOSTransaction.getLoyaltyCard() != null
        && compositePOSTransaction.getLoyaltyCard().getStoreType() != null)
      return res.getString(compositePOSTransaction.getLoyaltyCard().getStoreType());
    else
      return null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double getLoyaltyBalance() {
    if (compositePOSTransaction.getLoyaltyCard() != null) {
    	//Fix for US issue 886: Receipt for a sale involving a loyalty customer needs 
    	//to show that points were earned
    	if (isSourceFromTxnDetailApplet() ||
          (com.chelseasystems.cr.swing.CMSApplet.theAppMgr!=null &&
    			 com.chelseasystems.cr.swing.CMSApplet.theAppMgr.getStateObject("THE_TXN") != null)) {
    		return compositePOSTransaction.getLoyaltyCard().getCurrBalance();
    	} else {
      return compositePOSTransaction.getLoyaltyCard().getCurrBalance()
    			- compositePOSTransaction.getUsedLoyaltyPoints()
    			+ this.getTxnLoyaltyPoints();
    	}
    }
    else {
      return 0.0;
    }
  }
  
  public double getTxnLoyaltyPoints(){
	  Loyalty txnLoyalty = compositePOSTransaction.getLoyaltyCard();
	  if ( txnLoyalty != null){
		  return (compositePOSTransaction.getLoyaltyPoints()); 
//		  - compositePOSTransaction.getUsedLoyaltyPoints());
//		  - (compositePOSTransaction.getUsedLoyaltyPoints() * txnLoyalty.getPremioRewardRatioMultiplier()));
    }
	  else return 0.0;
  }  

  public double getCurrentYearLoyaltyPointsForTxn(){
  	return calculateYearlyLoyaltyPoints(0);
  }
  
  public double getLastYearLoyaltyPointsForTxn(){
  	return calculateYearlyLoyaltyPoints(1);
  }

  // year 0 - current year
  // year 1 - last year
  private double calculateYearlyLoyaltyPoints(int year){
  	Loyalty txnLoyalty = compositePOSTransaction.getLoyaltyCard();
		if (txnLoyalty == null){
			return 0.0;
		}else if(!txnLoyalty.isYearlyComputed()){
			return 0.0;
		}
		
		if(com.chelseasystems.cr.swing.CMSApplet.theAppMgr!=null &&
  			com.chelseasystems.cr.swing.CMSApplet.theAppMgr.getStateObject("THE_TXN") != null){
  		// this is an inquiry
			if (year == 0) {
				return txnLoyalty.getCurrYearBalance();
			} else {
				return txnLoyalty.getLastYearBalance();
			}
		}
		
		double loyaltyUsed = compositePOSTransaction.getUsedLoyaltyPoints();
		double points = compositePOSTransaction.getLoyaltyPoints();

		double currBal = txnLoyalty.getCurrBalance();
		double currYearBal = txnLoyalty.getCurrYearBalance();
		double lastYearBal = txnLoyalty.getLastYearBalance();
		if (loyaltyUsed > 0) {
			if (txnLoyalty.getLastYearBalance() < loyaltyUsed) {
				currYearBal = currYearBal - (loyaltyUsed - lastYearBal);
				lastYearBal = 0.0;
			} else {
				lastYearBal -= loyaltyUsed;
			}
			currBal -= loyaltyUsed;
		} else {
			currBal -= loyaltyUsed;
			currYearBal -= loyaltyUsed;
		}

		if (txnLoyalty.isYearlyComputed()) {
			currYearBal += points;
		} else {
			currYearBal = 0.0;
			lastYearBal = 0.0;
		}

		if (year == 0) {
			return currYearBal;
		} else {
			return lastYearBal;
		}
  }

  public double getCurrentYearLoyaltyPoints(){
  	Loyalty txnLoyalty = compositePOSTransaction.getLoyaltyCard();
	  if ( txnLoyalty != null){
		  return (txnLoyalty.getCurrYearBalance()); 
	  }
	  else return 0.0;
  }


  /** 
   * @param isSourceFromTxnDetailApplet
   */
  public void setIsSourceFromTxnDetailApplet(boolean isSourceFromTxnDetailApplet) {
	  this.isSourceFromTxnDetailApplet = isSourceFromTxnDetailApplet;
  }
  
  /**
   * put your documentation comment here
   * @return
   */
  public boolean isSourceFromTxnDetailApplet() {
	  return this.isSourceFromTxnDetailApplet;
  }
  
  //Fix for defect# 886
  public ArmCurrency getLoyaltyBalanceInCurrencyFormat() {
	  double loyaltyPointsBalance = getLoyaltyBalance();
	  ArmCurrency rtnVal = new ArmCurrency(loyaltyPointsBalance);
	  return (rtnVal);
  }
	
  public class POSLineItemWrapper {
    protected String id;
    protected String desc;
    protected int qty;
    protected ArmCurrency price;
    protected ArmCurrency settleAmt;
    protected double settlePct;
    protected ArmCurrency manualAmt;
    protected ArmCurrency promoAmt;
    protected ArmCurrency vat;
    protected ArmCurrency itemOriginalVat;
    protected boolean isReturn;

    /**
     * put your documentation comment here
     * @param         POSLineItemDetail detail
     */
    public POSLineItemWrapper(POSLineItemDetail detail) {
      this.id = detail.getLineItem().getItem().getId();
      this.desc = detail.getLineItem().getItemDescription();
      this.qty++;
      this.price = detail.getLineItem().getItemRetailPrice();
      this.isReturn = detail.getLineItem() instanceof com.chelseasystems.cr.pos.ReturnLineItem;
      this.vat = detail.getVatAmount();
      this.itemOriginalVat = detail.getLineItem().getNetAmount().multiply(detail.getLineItem().
          getItem().getVatRate().doubleValue()).round();
      Reduction[] reds = detail.getReductionsArray();
      for (int idx = 0; idx < reds.length; idx++) {
        String reason = reds[idx].getReason();
        //                if(reason.equalsIgnoreCase("PRIVILEGE Discount"))
        //                {
        //                    privAmt = reds[idx].getAmount();
        //                    PrivilegeDiscount disc = getPrivilegeDiscount();
        //                    try
        //                    {
        //                        privPct = disc.getPercent(detail.getLineItem(), compositePOSTransaction.getStore().getId());
        //                        continue;
        //                    }
        //                    catch(Exception e)
        //                    {
        //                    }
        //                }
        //                else if(reason.equalsIgnoreCase("DRIVERS Discount"))
        //                {
        //                    coachAmt = reds[idx].getAmount();
        //                    DriversDiscount disc = getDriversDiscount();
        //                    try
        //                    {
        //                        coachPct = disc.getPercent(detail.getLineItem(), compositePOSTransaction.getStore().getId());
        //                        continue;
        //                    }
        //                    catch(Exception e)
        //                    {
        //                    }
        //                }
        //                else if(reason.equalsIgnoreCase("CONCESSIONAIRE Discount"))
        //                {
        //                    concessAmt = reds[idx].getAmount();
        //                    ConcessionaireDiscount disc = getConcessionaireDiscount();
        //                    try
        //                    {
        //                        concessPct = disc.getPercent(detail.getLineItem(), compositePOSTransaction.getStore().getId());
        //                        continue;
        //                    }
        //                    catch(Exception e)
        //                    {
        //                    }
        //                }
        //                else if(reason.equalsIgnoreCase("SETTLEMENT"))
        //                {
        //                    settleAmt = reds[idx].getAmount();
        //                    SettlementDiscount disc = getSettlementDiscount();
        //                    try
        //                    {
        //                        settlePct = disc.getPercent();
        //                        continue;
        //                    }
        //                    catch(Exception e)
        //                    {
        //                    }
        //                }
        //                else
        if (reason.equalsIgnoreCase("Manual Markdown")) {
          manualAmt = reds[idx].getAmount();
        } else {
          promoAmt = reds[idx].getAmount();
        }
      }
    }

    /**
     * put your documentation comment here
     * @return
     */
    public boolean isReturn() {
      return (isReturn);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getID() {
      return (id);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getDescription() {
      return (desc);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public int getQty() {
      return (isReturn ? qty * -1 : qty);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getPrice() {
      return (getPrice(true));
    }

    /**
     * put your documentation comment here
     * @param withQty
     * @return
     */
    public ArmCurrency getPrice(boolean withQty) {
      ArmCurrency rtnVal = price;
      if (withQty) {
        rtnVal = price.multiply((double)qty).round();
      }
      if (isReturn && rtnVal != null) {
        rtnVal = rtnVal.multiply( -1).round();
      }
      return (rtnVal);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getSettlementDiscountAmount() {
      ArmCurrency rtnVal = settleAmt;
      if (isReturn && rtnVal != null) {
        rtnVal = rtnVal.multiply( -1).round();
      }
      return (rtnVal);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public double getSettlementDiscountPercent() {
      return (settlePct);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getManualDiscountAmount() {
      ArmCurrency rtnVal = manualAmt;
      if (isReturn && rtnVal != null) {
        rtnVal = rtnVal.multiply( -1).round();
      }
      return (rtnVal);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getActualVATAmount() {
      ArmCurrency rtnVal = vat;
      if (isReturn && rtnVal != null) {
        rtnVal = rtnVal.multiply( -1).round();
      }
      return (rtnVal);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getOriginalVATAmount() {
      ArmCurrency rtnVal = this.itemOriginalVat;
      if (isReturn && rtnVal != null) {
        rtnVal = rtnVal.multiply( -1).round();
      }
      return (rtnVal);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getPromotionalDiscountAmount() {
      ArmCurrency rtnVal = promoAmt;
      if (isReturn && rtnVal != null) {
        rtnVal = rtnVal.multiply( -1).round();
      }
      return (rtnVal);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getCompositeDiscountAmount() {
      ArmCurrency rtnVal = new ArmCurrency(0.00);
      try {
        rtnVal = rtnVal.add(settleAmt != null ? settleAmt : new ArmCurrency(0.00)).round();
        rtnVal = rtnVal.add(manualAmt != null ? manualAmt : new ArmCurrency(0.00)).round();
        rtnVal = rtnVal.add(promoAmt != null ? promoAmt : new ArmCurrency(0.00)).round();
      } catch (CurrencyException ce) {
        ce.printStackTrace();
      }
      if (isReturn && rtnVal != null) {
        rtnVal = rtnVal.multiply( -1).round();
      }
      return (rtnVal);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getSubTotal() {
      try {
        return (getPrice().subtract(getCompositeDiscountAmount()).round());
      } catch (CurrencyException ce) {
        ce.printStackTrace();
      }
      return (getPrice());
    }

    /**
     * put your documentation comment here
     * @param wrapper
     * @return
     */
    public boolean addWrapper(POSLineItemWrapper wrapper) {
      if (!canAddWrapper(wrapper)) {
        return (false);
      }
      qty++;
      try {
        settleAmt = settleAmt != null
            ? settleAmt.add(wrapper.getSettlementDiscountAmount().absoluteValue()).round() : null;
        manualAmt = manualAmt != null
            ? manualAmt.add(wrapper.getManualDiscountAmount().absoluteValue()).round() : null;
        promoAmt = promoAmt != null
            ? promoAmt.add(wrapper.getPromotionalDiscountAmount().absoluteValue()).round() : null;
        vat = vat != null ? vat.add(wrapper.getActualVATAmount().absoluteValue()).round() : null;
        itemOriginalVat = itemOriginalVat != null
            ? itemOriginalVat.add(wrapper.getOriginalVATAmount().absoluteValue()).round() : null;
      } catch (CurrencyException ce) {
        ce.printStackTrace();
      }
      return (true);
    }

    /**
     * put your documentation comment here
     * @param wrapper
     * @return
     */
    protected boolean canAddWrapper(POSLineItemWrapper wrapper) {
      try {
        if (!wrapper.getID().equals(this.getID())
            || !wrapper.getDescription().equals(this.getDescription())
            || !wrapper.getPrice(false).equals(this.getPrice(false))
            || wrapper.isReturn() != this.isReturn()) {
          return (false);
        }
      } catch (CurrencyException ce) {
        ce.printStackTrace();
        return (false);
      }
      if (!(wrapper.getSettlementDiscountAmount() != null && this.getSettlementDiscountAmount() != null)
          && !(wrapper.getSettlementDiscountAmount() == null && this.getSettlementDiscountAmount() == null)) {
        return (false);
      }
      if (!(wrapper.getPromotionalDiscountAmount() != null && this.getPromotionalDiscountAmount() != null)
          && !(wrapper.getPromotionalDiscountAmount() == null && this.getPromotionalDiscountAmount() == null)) {
        return (false);
      }
      if (!(wrapper.getManualDiscountAmount() != null && this.getManualDiscountAmount() != null)
          && !(wrapper.getManualDiscountAmount() == null && this.getManualDiscountAmount() == null)) {
        return (false);
      }
      return (true);
    }
  }


  public class VatDetail {
    ArmCurrency compositeAmount;
    ArmCurrency vatAmount;
    ArmCurrency originalVatAmount;
    int itemQty;
    double vatRate;

    /**
     * put your documentation comment here
     * @param         ArmCurrency compositeAmount
     * @param         ArmCurrency vatAmount
     * @param         double vatRate
     */
    public VatDetail(ArmCurrency compositeAmount, ArmCurrency vatAmount, double vatRate
        , ArmCurrency originalVat, int itemQty) {
      this.compositeAmount = compositeAmount;
      this.vatAmount = vatAmount;
      this.vatRate = vatRate;
      this.originalVatAmount = originalVat;
      this.itemQty = itemQty;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getOriginalVatAmount() {
      return this.originalVatAmount;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public int getItemQty() {
      return itemQty;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getCompositeAmount() {
      return this.compositeAmount;
    }

    /**
     * put your documentation comment here
     * @param compositeAmount
     */
    public void addCompositeAmount(ArmCurrency compositeAmount) {
      try {
        this.compositeAmount = this.compositeAmount.add(compositeAmount);
      } catch (CurrencyException ex) {
        System.out.println("currency excpetion: " + ex);
      }
    }

    /**
     * put your documentation comment here
     * @return
     */
    public ArmCurrency getActualVatAmount() {
      return this.vatAmount;
    }

    /**
     * put your documentation comment here
     * @param vatAmount
     */
    public void addVatAmount(ArmCurrency vatAmount) {
      try {
        this.vatAmount = this.vatAmount.add(vatAmount);
      } catch (CurrencyException ex) {
        System.out.println("currency excpetion: " + ex);
      }
    }

    /**
     * put your documentation comment here
     * @param originalVatAmount
     */
    public void addOriginalVatAmount(ArmCurrency originalVatAmount) {
      try {
        this.originalVatAmount = this.originalVatAmount.add(originalVatAmount);
      } catch (CurrencyException ex) {
        System.out.println("currency excpetion: " + ex);
      }
    }

    /**
     * put your documentation comment here
     * @param itemQty
     */
    public void addItemQty(int itemQty) {
      this.itemQty += itemQty;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Double getVatRate() {
      return new Double(vatRate);
    }
  }
  
  /**
   * put your documentation comment here
   * @return
   */
  public String getOriginalReservationOpenTransactionId() {
    for (Enumeration em = compositePOSTransaction.getSaleLineItems(); em.hasMoreElements(); ) {
      CMSSaleLineItem line = (CMSSaleLineItem)em.nextElement();
      if (line.getReservationLineItem() != null) {
        return line.getReservationLineItem().getTransaction().getCompositeTransaction().getId();
      }
    }
    for (Enumeration em = compositePOSTransaction.getReturnLineItems(); em.hasMoreElements(); ) {
      CMSReturnLineItem line = (CMSReturnLineItem)em.nextElement();
      if (line.getReservationLineItem() != null) {
        return line.getReservationLineItem().getTransaction().getCompositeTransaction().getId();
      }
    }
    return null;
  }

  //Issue #1446
  public ArmCurrency getTotalAlterationAmount() {
    ArmCurrency total = new ArmCurrency(0);
    String unlinkedAlterationDesc = res.getString((new ConfigMgr("item.cfg")).getString("ALTERATION.MISC_ITEM_DESC"));
    String unlinkedAlterationDescChoices = res.getString((new ConfigMgr("item.cfg")).getString("ALTERATION.DESC_CHOICES"));
    // Optimization for slow receipt printing.  This may not be directly related but
    // must account for stale data in group.
    itemsArray = null;
    // End opt change.
    GroupedReceiptLine[] receiptLineItems = getReceiptLineItem();
    try {
      for (int i = 0; i < receiptLineItems.length; i++) {
        if (((GroupedReceiptLine)receiptLineItems[i]).getItemId().equals((new ConfigMgr("item.cfg")).getString("ALTERATION.BASE_ITEM")))
          total = total.add(receiptLineItems[i].getExtendedSellingPrice());
        else
          total = total.add(receiptLineItems[i].getLineItemTotalAlterationAmount());
      }
    } catch (CurrencyException ex) {
      ex.printStackTrace();
    }
    return total;
  }

  public boolean isHavingOpenDeposit() {
    return isReservationOpen() && (compositePOSTransaction.getDepositLineItems().length > 0);
  }

  public boolean isReservationOpen(){
	  return compositePOSTransaction.isReservationOpen();
  }
  
//Vivek Mishra : Added for signature display
	
	public String getSignLocation() {
		return signLocation;
	}

	public void setSignLocation(String signLocation) {
		this.signLocation = signLocation;
	}
	
//Ends

//Mayuri Edhara : Code modified for on methods for CANADA Taxes.. 
	// tested for even exchange, no receipt return corrected -ve amounts, send sale scenarios.

	public ArmCurrency getGsthstTaxAmt() throws Exception {
		
		ArmCurrency gstHstTaxAmt = new  ArmCurrency(0.0);
		Double offlineGstHstTax;
       	Double value = 0.0d;
      	Double netAMt =  0.0d;
      	Double belowThreshTaxAmt = 0.0d;
      	Double taxAmt = 0.0d;
      	Double valueExc = 0.0d;
	    ArmCurrency itemGstHstTax = new ArmCurrency(0.0) ;
		CMSStore cmsStore = ((CMSStore)compositePOSTransaction.getStore());
		
		if(cmsStore.getCountry().equals("CAN")){
		if(compositePOSTransaction.getTransactionType().contains("RETN")){
			POSLineItem[] lineitem = compositePOSTransaction.getLineItemsArray();
			CMSCompositePOSTransaction OrgSaleTxn = null;
			
			for(int i =0 ; i<lineitem.length ;i++){
			    if(lineitem[i] instanceof CMSReturnLineItem ){
			     CMSReturnLineItem rtnLnItm = (CMSReturnLineItem) lineitem[i];
			     CMSReturnLineItemDetail rtnLnItmDtl = (CMSReturnLineItemDetail) rtnLnItm.getLineItemDetailsArray()[0];
			     CMSSaleLineItemDetail saleLnItmDtl = (CMSSaleLineItemDetail) rtnLnItmDtl.getSaleLineItemDetail();
				     if(saleLnItmDtl!=null){
				     CompositePOSTransaction txnObject = saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction();
				     OrgSaleTxn = ((CMSCompositePOSTransaction) txnObject);
				   		 break;
				    }
			    }
			}		
			
			if(OrgSaleTxn == null){
				POSLineItem[] itms = compositePOSTransaction.getLineItemsArray();
				for(POSLineItem itm : itms){
					if(itm.getGsthstTaxAmt()!=null){
						if(itm instanceof CMSReturnLineItem)
						{
							gstHstTaxAmt = (gstHstTaxAmt.subtract(itm.getGsthstTaxAmt().absoluteValue())).absoluteValue();
						}
							gstHstTaxAmt = gstHstTaxAmt.add(itm.getGsthstTaxAmt().absoluteValue());
					}
				}
			} else{
				
				POSLineItem[] itms = OrgSaleTxn.getLineItemsArray();
				
				for(int i=0;i<lineitem.length;i++){
					for(POSLineItem itm : itms){
						if((itm.getItem().getId() == lineitem[i].getItem().getId()) && (itm.getTaxAmount().equals(lineitem[i].getTaxAmount().absoluteValue()))){	
							if(itm.getGsthstTaxAmt() != null){
								gstHstTaxAmt = gstHstTaxAmt.add(itm.getGsthstTaxAmt().absoluteValue());
								lineitem[i].setGsthstTaxAmt(itm.getGsthstTaxAmt().absoluteValue());
								break;
							}
						}
						else if(!(itm.getItem().getId() == lineitem[i].getItem().getId())){	
							if(lineitem[i].getGsthstTaxAmt() == null){
								if(cmsStore.getGst_hstTAX() != null){
									itemGstHstTax = lineitem[i].getNetAmount().multiply(cmsStore.getGst_hstTAX()).absoluteValue();
									lineitem[i].setGsthstTaxAmt(itemGstHstTax);
									gstHstTaxAmt = (gstHstTaxAmt.subtract(itemGstHstTax)).absoluteValue();

								}
							} else if(lineitem[i].getGsthstTaxAmt()!=null){	
								gstHstTaxAmt = (gstHstTaxAmt.subtract(lineitem[i].getGsthstTaxAmt())).absoluteValue();	

							}						   
						}
					}
				}
			}
			//System.out.println("gstHstTaxAmt return, sale/return even exchange>>>>>>>>>>>>> " + gstHstTaxAmt);
			return gstHstTaxAmt.absoluteValue();
		}	
		else if(compositePOSTransaction.getTaxableLineItemDetailsArray().length == 0)
		{
			POSLineItem[] itms = compositePOSTransaction.getLineItemsArray();
			for(POSLineItem itm : itms){
				if(itm.getGsthstTaxAmt()!=null)
				gstHstTaxAmt = gstHstTaxAmt.add(itm.getGsthstTaxAmt().absoluteValue());
			}
			return gstHstTaxAmt.absoluteValue();
		}
		else {		 
		   if (isVatEnabled())
		      return null;
		   
		    POSLineItemDetail [] posLineItemDetails = compositePOSTransaction.getTaxableLineItemDetailsArray();

		    POSLineItem itm = null;
		   
		    int lineItemLength = compositePOSTransaction.getTaxableLineItemDetailsArray().length;
		    for(int i =0 ; i<lineItemLength ; i++){
		    	try {
		    		itm = posLineItemDetails[i].getLineItem();
		    		if((posLineItemDetails[i].getLineItem() instanceof SaleLineItem) && ((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest()!=null){
		    			offlineGstHstTax =  ((CMSShippingRequest) (((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest())).getOfflineHstTax();
		    			if(offlineGstHstTax == null){									
							value = getTaxMapValue("HST");
			              	itm.setGsthstTaxAmt(itm.getNetAmount().multiply(value));			              	
			              	Double thresAmt = compositePOSTransaction.getThreshAmt()!=null ? compositePOSTransaction.getThreshAmt().doubleValue():0.0d;
			              	valueExc = getTaxExecMapValue("HST");
			              	if(compositePOSTransaction.getThrehRule()!=null && compositePOSTransaction.getThrehRule().equalsIgnoreCase("P")){
			              		if(posLineItemDetails[i].getLineItem().getNetAmount().greaterThan(new ArmCurrency(thresAmt))){			              		  
			              			belowThreshTaxAmt =  thresAmt*valueExc;
			              			netAMt = (posLineItemDetails[i].getLineItem().getNetAmount().doubleValue()) - thresAmt ;
			              		}
			              		taxAmt = belowThreshTaxAmt + (netAMt*value);
			              		itm.setGsthstTaxAmt(new ArmCurrency(taxAmt));
			              	} else {
			              		itm.setGsthstTaxAmt(itm.getNetAmount().multiply(value));  
			              	}			              	
						}else if(offlineGstHstTax!=null){
		                	itm.setGsthstTaxAmt(itm.getNetAmount().multiply(offlineGstHstTax.doubleValue()));
		                }
		    			
		    			gstHstTaxAmt = (gstHstTaxAmt.subtract(itm.getGsthstTaxAmt())).absoluteValue();	
		    			
		    		} else {
		    			
						itm = posLineItemDetails[i].getLineItem();
							if(itm.getGsthstTaxAmt()==null){
								if(cmsStore.getGst_hstTAX()!=null){
									itemGstHstTax = itm.getNetAmount().multiply(cmsStore.getGst_hstTAX());
									itm.setGsthstTaxAmt(itemGstHstTax);
								}
							}
					}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    try {
		    	for(POSLineItemDetail line : posLineItemDetails){
		    		if(line.getLineItem().getGsthstTaxAmt()!=null) {
		    			gstHstTaxAmt = gstHstTaxAmt.add(line.getLineItem().getGsthstTaxAmt());
		    		}
		    	}
		    	
				//System.out.println("gstHstTaxAmt >>>>>>SALE>>>>>>>"+gstHstTaxAmt);
			} catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return gstHstTaxAmt.absoluteValue();
			}
			} // end country check.
			return gstHstTaxAmt;
		  } // end getGsthstTaxAmt
	
	
	public ArmCurrency getGstTaxAmt() throws Exception {
		
		ArmCurrency gstTaxAmt = new  ArmCurrency(0.0);
		Double offlineGstTax;
       	Double value = 0.0d;
      	Double netAMt =  0.0d;
      	Double belowThreshTaxAmt = 0.0d;
      	Double taxAmt = 0.0d;
      	Double valueExc = 0.0d;
      	ArmCurrency itemGstTax = new ArmCurrency(0.0);
      	
		CMSStore cmsStore = ((CMSStore)compositePOSTransaction.getStore());
		
		if(cmsStore.getCountry().equals("CAN")){
		if(compositePOSTransaction.getTransactionType().contains("RETN")){
			POSLineItem[] lineitem = compositePOSTransaction.getLineItemsArray();
			CMSCompositePOSTransaction OrgSaleTxn = null;
			
			for(int i =0 ; i<lineitem.length ;i++){
			    if(lineitem[i] instanceof CMSReturnLineItem ){
			     CMSReturnLineItem rtnLnItm = (CMSReturnLineItem) lineitem[i];
			     CMSReturnLineItemDetail rtnLnItmDtl = (CMSReturnLineItemDetail) rtnLnItm.getLineItemDetailsArray()[0];
			     CMSSaleLineItemDetail saleLnItmDtl = (CMSSaleLineItemDetail) rtnLnItmDtl.getSaleLineItemDetail();
				     if(saleLnItmDtl!=null){
				     CompositePOSTransaction txnObject = saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction();
				     OrgSaleTxn = ((CMSCompositePOSTransaction) txnObject);
				   		 break;
				    }
			    }
			}
			
			if(OrgSaleTxn == null){
				POSLineItem[] itms = compositePOSTransaction.getLineItemsArray();
				for(POSLineItem itm : itms){
					if(itm.getGstTaxAmt()!=null){
						if(itm instanceof CMSReturnLineItem){
							gstTaxAmt = gstTaxAmt.subtract(itm.getGstTaxAmt().absoluteValue());
						}
						else{
							gstTaxAmt = gstTaxAmt.add(itm.getGstTaxAmt().absoluteValue());
						}
					}
				}
			} else{
				
				POSLineItem[] itms = OrgSaleTxn.getLineItemsArray();
				for(int i=0;i<lineitem.length;i++){
					for(POSLineItem itm : itms){
						if((itm.getItem().getId() == lineitem[i].getItem().getId()) && (itm.getTaxAmount().equals(lineitem[i].getTaxAmount().absoluteValue()))){	
							if(itm.getGstTaxAmt() != null){								
								gstTaxAmt = gstTaxAmt.add(itm.getGstTaxAmt().absoluteValue());
								lineitem[i].setGstTaxAmt(itm.getGstTaxAmt().absoluteValue());
								break;
							}
						}
						else if(!(itm.getItem().getId() == lineitem[i].getItem().getId())){	
							if(lineitem[i].getGstTaxAmt() == null){
								if(cmsStore.getGstTAX() != null){
									itemGstTax = lineitem[i].getNetAmount().multiply(cmsStore.getGstTAX()).absoluteValue();
									lineitem[i].setGstTaxAmt(itemGstTax);
									gstTaxAmt = (gstTaxAmt.subtract((lineitem[i].getNetAmount()).multiply(cmsStore.getGstTAX()))).absoluteValue();

								}
							} else if(lineitem[i].getGstTaxAmt()!=null){	
								gstTaxAmt = (gstTaxAmt.subtract(lineitem[i].getGstTaxAmt())).absoluteValue();	
							}						   
						}
					}
				}
			}
			//System.out.println("gstTaxAmt return, sale/return even exchange>>>>>>>>>>>>> " + gstTaxAmt);
			return gstTaxAmt.absoluteValue();
		}	
		else if(compositePOSTransaction.getTaxableLineItemDetailsArray().length == 0)
		{
			POSLineItem[] itms = compositePOSTransaction.getLineItemsArray();
			for(POSLineItem itm : itms){
				if(itm.getGstTaxAmt()!=null)
					gstTaxAmt = gstTaxAmt.add(itm.getGstTaxAmt().absoluteValue());
			}
			return gstTaxAmt.absoluteValue();
		}
		else {		 
		   if (isVatEnabled())
		      return null;
		   
		    POSLineItemDetail [] posLineItemDetails = compositePOSTransaction.getTaxableLineItemDetailsArray();

		    POSLineItem itm = null;
		   
		    int lineItemLength = compositePOSTransaction.getTaxableLineItemDetailsArray().length;
		    for(int i =0 ; i<lineItemLength ; i++){
		    	try {
		    		itm = posLineItemDetails[i].getLineItem();
		    		if((posLineItemDetails[i].getLineItem() instanceof SaleLineItem) && ((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest()!=null){
		    			offlineGstTax =  ((CMSShippingRequest) (((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest())).getOfflineGstTax();
		    			if(offlineGstTax == null){
			    			value = getTaxMapValue("GST");
			              	itm.setGstTaxAmt(itm.getNetAmount().multiply(value));	    			
			                Double thresAmt = compositePOSTransaction.getThreshAmt()!=null ? compositePOSTransaction.getThreshAmt().doubleValue():0.0d;
			                valueExc = getTaxExecMapValue("GST");
			              	if(compositePOSTransaction.getThrehRule()!=null && compositePOSTransaction.getThrehRule().equalsIgnoreCase("P")){
			              		if(posLineItemDetails[i].getLineItem().getNetAmount().greaterThan(new ArmCurrency(thresAmt))){ 
			              		  
			              			belowThreshTaxAmt =  thresAmt*valueExc;
			              			netAMt = (posLineItemDetails[i].getLineItem().getNetAmount().doubleValue()) - thresAmt ;
			              		}
			              		taxAmt = belowThreshTaxAmt + (netAMt*value);
			              		itm.setGstTaxAmt(new ArmCurrency(taxAmt));
			              	} else {
			              	  	itm.setGstTaxAmt(itm.getNetAmount().multiply(value));  
			              	}
		    			} else if(offlineGstTax!=null){
		                	 itm.setGstTaxAmt(itm.getNetAmount().multiply(offlineGstTax.doubleValue()));  
		                }
		    		} else {						
						itm = posLineItemDetails[i].getLineItem();
							if(itm.getGstTaxAmt()==null){
								if(cmsStore.getGstTAX()!=null){
									itemGstTax = itm.getNetAmount().multiply(cmsStore.getGstTAX());
									itm.setGstTaxAmt(itemGstTax);
								}
							}
					}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    try {
		    	for(POSLineItemDetail line : posLineItemDetails){
		    		if(line.getLineItem().getGstTaxAmt()!=null) {
		    			gstTaxAmt = gstTaxAmt.add(line.getLineItem().getGstTaxAmt());
		    		}
		    	}
		    	
			//	System.out.println("gstTaxAmt>>>>>>SALE>>>>>>>"+gstTaxAmt);
			} catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return gstTaxAmt.absoluteValue();
			}
			} // end country check.
			return gstTaxAmt;
		  } // end getGstTaxAmt
	
	public ArmCurrency getPstTaxAmt() throws Exception {
		
		ArmCurrency pstTaxAmt = new  ArmCurrency(0.0);
		Double offlinePstTax;
       	Double value = 0.0d;
      	Double netAMt =  0.0d;
      	Double belowThreshTaxAmt = 0.0d;
      	Double taxAmt = 0.0d;
      	Double valueExc = 0.0d;
      	ArmCurrency itemPstTax = new ArmCurrency(0.0) ;
		CMSStore cmsStore = ((CMSStore)compositePOSTransaction.getStore());
		
		if(cmsStore.getCountry().equals("CAN")){
		if(compositePOSTransaction.getTransactionType().contains("RETN")){
			POSLineItem[] lineitem = compositePOSTransaction.getLineItemsArray();
			CMSCompositePOSTransaction OrgSaleTxn = null;
			
			for(int i =0 ; i<lineitem.length ;i++){
			    if(lineitem[i] instanceof CMSReturnLineItem ){
			     CMSReturnLineItem rtnLnItm = (CMSReturnLineItem) lineitem[i];
			     CMSReturnLineItemDetail rtnLnItmDtl = (CMSReturnLineItemDetail) rtnLnItm.getLineItemDetailsArray()[0];
			     CMSSaleLineItemDetail saleLnItmDtl = (CMSSaleLineItemDetail) rtnLnItmDtl.getSaleLineItemDetail();
				     if(saleLnItmDtl!=null){
				     CompositePOSTransaction txnObject = saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction();
				     OrgSaleTxn = ((CMSCompositePOSTransaction) txnObject);
				   		 break;
				    }
			    }
			}
			
			if(OrgSaleTxn == null){
				POSLineItem[] itms = compositePOSTransaction.getLineItemsArray();
				for(POSLineItem itm : itms){
					if(itm.getPstTaxAmt()!=null){
						if(itm instanceof CMSReturnLineItem){
							pstTaxAmt = pstTaxAmt.subtract(itm.getPstTaxAmt().absoluteValue());
						}
						else{
							pstTaxAmt = pstTaxAmt.add(itm.getPstTaxAmt().absoluteValue());
						}
					}
				}
			} else{
				
				POSLineItem[] itms = OrgSaleTxn.getLineItemsArray();
				for(int i=0;i<lineitem.length;i++){
					for(POSLineItem itm : itms){
						if((itm.getItem().getId() == lineitem[i].getItem().getId()) && (itm.getTaxAmount().equals(lineitem[i].getTaxAmount().absoluteValue()))){	
							if(itm.getPstTaxAmt() != null){
								pstTaxAmt = pstTaxAmt.add(itm.getPstTaxAmt().absoluteValue());
								lineitem[i].setPstTaxAmt(itm.getPstTaxAmt().absoluteValue());
								break;
							}
						}
						else if(!(itm.getItem().getId() == lineitem[i].getItem().getId())){	
							if(lineitem[i].getPstTaxAmt() == null){
								if(cmsStore.getPstTAX() != null){
									itemPstTax = lineitem[i].getNetAmount().multiply(cmsStore.getPstTAX()).absoluteValue();
									lineitem[i].setPstTaxAmt(itemPstTax);
									pstTaxAmt = (pstTaxAmt.subtract((lineitem[i].getNetAmount()).multiply(cmsStore.getPstTAX()))).absoluteValue();

								}
							} else if(lineitem[i].getPstTaxAmt() != null){	
								pstTaxAmt = (pstTaxAmt.subtract(lineitem[i].getPstTaxAmt())).absoluteValue();
							}						   
						}
					}
				}
			}
			//System.out.println("pstTaxAmt return, sale/return even exchange>>>>>>>>>>>>> " + pstTaxAmt);
			return pstTaxAmt.absoluteValue();
		}	
		else if(compositePOSTransaction.getTaxableLineItemDetailsArray().length == 0)
		{
			POSLineItem[] itms = compositePOSTransaction.getLineItemsArray();
			for(POSLineItem itm : itms){
				if(itm.getPstTaxAmt()!=null)
					pstTaxAmt = pstTaxAmt.add(itm.getPstTaxAmt().absoluteValue());
			}
			return pstTaxAmt.absoluteValue();
		}
		else {		 
		   if (isVatEnabled())
		      return null;
		   
		    POSLineItemDetail [] posLineItemDetails = compositePOSTransaction.getTaxableLineItemDetailsArray();

		    POSLineItem itm = null;
		   
		    int lineItemLength = compositePOSTransaction.getTaxableLineItemDetailsArray().length;
		    for(int i =0 ; i<lineItemLength ; i++){
		    	try {
		    		itm = posLineItemDetails[i].getLineItem();
		    		if((posLineItemDetails[i].getLineItem() instanceof SaleLineItem) && ((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest()!=null){
		    			offlinePstTax =  ((CMSShippingRequest) (((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest())).getOfflinePstTax();
		    			if(offlinePstTax == null){
			    			value = getTaxMapValue("PST");		              	
			              	itm.setPstTaxAmt(itm.getNetAmount().multiply(value));	    			
			                Double thresAmt = compositePOSTransaction.getThreshAmt()!=null ? compositePOSTransaction.getThreshAmt().doubleValue():0.0d;
			                valueExc = getTaxExecMapValue("PST");
			              	if(compositePOSTransaction.getThrehRule()!=null && compositePOSTransaction.getThrehRule().equalsIgnoreCase("P")){
			              		if(posLineItemDetails[i].getLineItem().getNetAmount().greaterThan(new ArmCurrency(thresAmt))){ 			              		  
			              			belowThreshTaxAmt =  thresAmt*valueExc;
			              			netAMt = (posLineItemDetails[i].getLineItem().getNetAmount().doubleValue()) - thresAmt ;
			              		}
			              		taxAmt = belowThreshTaxAmt + (netAMt*value);
			              		itm.setPstTaxAmt(new ArmCurrency(taxAmt));
			              	} else {
			              	  	itm.setPstTaxAmt(itm.getNetAmount().multiply(value));  
			              	}
		    			}
		                if(offlinePstTax!=null){
		                	 itm.setPstTaxAmt(itm.getNetAmount().multiply(offlinePstTax.doubleValue()));  
		                }		                
		    		} else {						
						itm = posLineItemDetails[i].getLineItem();
							if(itm.getPstTaxAmt()==null){
								if(cmsStore.getPstTAX()!=null){
									itemPstTax = itm.getNetAmount().multiply(cmsStore.getPstTAX());
									itm.setPstTaxAmt(itemPstTax);
								}
							}
					}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    try {
		    	for(POSLineItemDetail line : posLineItemDetails){
		    		if(line.getLineItem().getPstTaxAmt()!=null) {
		    			pstTaxAmt = pstTaxAmt.add(line.getLineItem().getPstTaxAmt());
		    		}
		    	}
		    	
				//System.out.println("pstTaxAmt>>>>>>SALE>>>>>>>"+pstTaxAmt);
			} catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return pstTaxAmt.absoluteValue();
			}
			} // end country check.
			return pstTaxAmt;
		  } // end getPstTaxAmt


	public ArmCurrency getQstTaxAmt() throws Exception {
		
		ArmCurrency qstTaxAmt = new  ArmCurrency(0.0);
		Double offlineQstTax;
       	Double value = 0.0d;
      	Double netAMt =  0.0d;
      	Double belowThreshTaxAmt = 0.0d;
      	Double taxAmt = 0.0d;
      	Double valueExc = 0.0d;
	    ArmCurrency itemqstTax = new ArmCurrency(0.0) ;
		CMSStore cmsStore = ((CMSStore)compositePOSTransaction.getStore());
		
		if(cmsStore.getCountry().equals("CAN")){
		if(compositePOSTransaction.getTransactionType().contains("RETN")){
			POSLineItem[] lineitem = compositePOSTransaction.getLineItemsArray();
			CMSCompositePOSTransaction OrgSaleTxn = null;
			
			for(int i =0 ; i<lineitem.length ;i++){
			    if(lineitem[i] instanceof CMSReturnLineItem ){
			     CMSReturnLineItem rtnLnItm = (CMSReturnLineItem) lineitem[i];
			     CMSReturnLineItemDetail rtnLnItmDtl = (CMSReturnLineItemDetail) rtnLnItm.getLineItemDetailsArray()[0];
			     CMSSaleLineItemDetail saleLnItmDtl = (CMSSaleLineItemDetail) rtnLnItmDtl.getSaleLineItemDetail();
				     if(saleLnItmDtl!=null){
				     CompositePOSTransaction txnObject = saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction();
				     OrgSaleTxn = ((CMSCompositePOSTransaction) txnObject);
				   		 break;
				    }
			    }
			}		 
				
			if(OrgSaleTxn == null){
				POSLineItem[] itms = compositePOSTransaction.getLineItemsArray();
				for(POSLineItem itm : itms){
					if(itm.getQstTaxAmt()!=null){
						if(itm instanceof CMSReturnLineItem){
							qstTaxAmt = qstTaxAmt.subtract(itm.getQstTaxAmt().absoluteValue());
						}
						else{
							qstTaxAmt = qstTaxAmt.add(itm.getQstTaxAmt().absoluteValue());
						}
					}
				}
			} else{
				
				POSLineItem[] itms = OrgSaleTxn.getLineItemsArray();
				for(int i=0;i<lineitem.length;i++){
					for(POSLineItem itm : itms){
						if((itm.getItem().getId() == lineitem[i].getItem().getId()) && (itm.getTaxAmount().equals(lineitem[i].getTaxAmount().absoluteValue()))){	
							if(itm.getQstTaxAmt() != null){
								qstTaxAmt = qstTaxAmt.add(itm.getQstTaxAmt().absoluteValue());
								lineitem[i].setQstTaxAmt(itm.getQstTaxAmt().absoluteValue());
								break;
							}
						}
						else if(!(itm.getItem().getId() == lineitem[i].getItem().getId())){								
							if(lineitem[i].getQstTaxAmt() == null){
								if(cmsStore.getQstTAX() != null){
									itemqstTax = lineitem[i].getNetAmount().multiply(cmsStore.getQstTAX()).absoluteValue();
									lineitem[i].setQstTaxAmt(itemqstTax);
									qstTaxAmt = (qstTaxAmt.subtract((lineitem[i].getNetAmount()).multiply(cmsStore.getQstTAX()))).absoluteValue();

								}
							} else if(lineitem[i].getQstTaxAmt() != null){	
								qstTaxAmt = (qstTaxAmt.subtract(lineitem[i].getQstTaxAmt())).absoluteValue();
							}						   
						}
					}
				}
			}
			//System.out.println("qstTaxAmt sale/return even exchange>>>>>>>>>>>>> " + qstTaxAmt);
			return qstTaxAmt.absoluteValue();
		}	
		else if(compositePOSTransaction.getTaxableLineItemDetailsArray().length == 0)
		{
			POSLineItem[] itms = compositePOSTransaction.getLineItemsArray();
			for(POSLineItem itm : itms){
				if(itm.getQstTaxAmt()!=null)
					qstTaxAmt = qstTaxAmt.add(itm.getQstTaxAmt().absoluteValue());
			}
			return qstTaxAmt;
		}
		else {		 
		   if (isVatEnabled())
		      return null;
		   
		    POSLineItemDetail [] posLineItemDetails = compositePOSTransaction.getTaxableLineItemDetailsArray();

		    POSLineItem itm = null;
		   
		    int lineItemLength = compositePOSTransaction.getTaxableLineItemDetailsArray().length;
		    for(int i =0 ; i<lineItemLength ; i++){
		    	try {
		    		itm = posLineItemDetails[i].getLineItem();
		    		if((posLineItemDetails[i].getLineItem() instanceof SaleLineItem) && ((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest()!=null){
		    			offlineQstTax =  ((CMSShippingRequest) (((CMSSaleLineItem)posLineItemDetails[i].getLineItem()).getShippingRequest())).getOfflineQstTax();
		    			if(offlineQstTax == null){
			    			value = getTaxMapValue("QST");
				            itm.setQstTaxAmt(itm.getNetAmount().multiply(value));		    			
				            Double thresAmt = compositePOSTransaction.getThreshAmt()!=null ? compositePOSTransaction.getThreshAmt().doubleValue():0.0d;			                
				            valueExc = getTaxExecMapValue("QST");
			              	if(compositePOSTransaction.getThrehRule()!=null && compositePOSTransaction.getThrehRule().equalsIgnoreCase("P")){
			              		if(posLineItemDetails[i].getLineItem().getNetAmount().greaterThan(new ArmCurrency(thresAmt)) ) { 
			              		  
			              			belowThreshTaxAmt =  thresAmt*valueExc;
			              			netAMt = (posLineItemDetails[i].getLineItem().getNetAmount().doubleValue()) - thresAmt ;
			              		}
			              		taxAmt = belowThreshTaxAmt + (netAMt*value);
			              		itm.setQstTaxAmt(new ArmCurrency(taxAmt));
			              	} else {
			              	  	itm.setQstTaxAmt(itm.getNetAmount().multiply(value));  
			              	}
		    			} else if(offlineQstTax!=null){
		                	 itm.setQstTaxAmt(itm.getNetAmount().multiply(offlineQstTax.doubleValue()));  
		                }
		    		} else {						
						itm = posLineItemDetails[i].getLineItem();
							if(itm.getQstTaxAmt()==null){
								if(cmsStore.getQstTAX()!=null){
									itemqstTax = itm.getNetAmount().multiply(cmsStore.getQstTAX());
									itm.setQstTaxAmt(itemqstTax);
								}
							}
					}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    try {
		    	for(POSLineItemDetail line : posLineItemDetails){
		    		if(line.getLineItem().getQstTaxAmt()!=null) {
		    			 qstTaxAmt = qstTaxAmt.add(line.getLineItem().getQstTaxAmt());
		    		}
		    	}
		    	
			//	System.out.println("qstTaxAmt>>>>>SALE>>>>>>>>"+qstTaxAmt);
			} catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return qstTaxAmt;
			}
			} // end country check.
			return qstTaxAmt;
		  } // end getQstTaxAmt
	
	public Double getTaxMapValue(String MatchKey) throws Exception{
		Double value = 0.0d;
		Map<String , Double > map = compositePOSTransaction.getTaxMap();		

      	if(map!=null){
      	  for (String key : map.keySet()) {     	          	  
      	      if(key.toString().contains(MatchKey)){
      	    	value = map.get(key);
      	    //	System.out.println("Key = " + key + ", Value = " + value);  
     	    	break;
     	      } 
      	   }
      	 }
		return value;
	}
	
	public Double getTaxExecMapValue(String MatchKey) throws Exception{
		Double valueExc = 0.0d;
		Map<String , Double > mapExc = compositePOSTransaction.getTaxExcMap()!=null ? compositePOSTransaction.getTaxExcMap():null;      	

      	if(mapExc!=null){
      	  for (String key : mapExc.keySet()) {      		              	  
      	      if(key.toString().contains(MatchKey)){	
      	    	valueExc = mapExc.get(key);      			
          	//    System.out.println("Key = " + key + ", Value = " + valueExc);
      	    	 break;
     	      }			              	    
      	  }
      	}
		return valueExc;
	}

} //end AppModel
