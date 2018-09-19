package  com.chelseasystems.cs.fiscaldocument;

import java.util.Enumeration;
import java.util.Vector;

import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.POSLineItem;


/**
 * <p>Title:VATInvoice </p>
 * <p>Author: GA SPA </p>
 */
public class ReceiptDocument extends FiscalDocument {
  
    private Vector vecPayments;
  
    /**
   * Default Constructor
   */
  
    
    public ReceiptDocument () {
        
     vecPayments = new Vector();    
   
  }

    public POSLineItem[] getPaymentArray() {
        return (POSLineItem[])vecPayments.toArray(new POSLineItem[vecPayments.size()]);
      }

      /**
       * Enumerated POSLineItems
       * @return Enumeration
       */
      public Enumeration getPayments() {
        return vecPayments.elements();
      }

      /**
       * Add line Item
       * @param lineItem POSLineItem
       */
      public void doAddLinePayment(Payment lineItem) {
          vecPayments.addElement(lineItem);
      }
    
  
  
  
}



