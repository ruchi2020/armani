/*
 *
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 7    | 07-22-2005 | Vikram    | 286       | Changes for shipping to foreign countries.   |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 6    | 07-18-2005 | Vikram    | 602       |Added dialog prompt to be displayed for "Home"|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 5    | 07-14-2005 | Megha     | PCR       | Zip Code Verifier                            |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 06-02-2005 | Vikram    | 98        |Removed explicit use of "USA" as store country|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 04-14-2005 | KS/Megha  | N/A       |Panel arrangements                            |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 04-12-2005 | Khyati    | N/A       |1.Send Sale specification.                    |
 --------------------------------------------------------------------------------------------
 | 1    | 04-12-2005 | Base      | N/A       |                                              |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.pos;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.event.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.swing.customer.RedeemableInquiryApplet;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.address.Address;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.address.AddressMgr;
import java.util.Vector;
import com.chelseasystems.cs.tax.TaxUtilities;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.panel.ViewAddressPanel;
import com.chelseasystems.cs.swing.panel.AddressPanel;
import com.chelseasystems.cs.swing.panel.BaseAddressPanel;
import com.ga.fs.fsbridge.ARMFSBridge;


/**
 */
public class ShippingHeaderApplet extends CMSApplet {
  CMSCompositePOSTransaction theTxn;
  CMSShippingRequest shippingRequest;
  int mode;
  private static final int ADD_MODE = 0;
  private static final int UPDATE_MODE = 1;
  private static final int INQUIRE_MODE = 2;
  // For state.
  private static final String ALPHA_ONLY_SPEC =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String NAME_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-' ";
  public static final String NAME_SPEC_NUM = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.-' ";
  JCMSTextField txtFirstName = new JCMSTextField();
  JCMSTextField txtLastName = new JCMSTextField();
  JCMSTextArea txtSpecialInstructions = new JCMSTextArea();
  JCMSTextArea txtGiftMessage = new JCMSTextArea();
  private ViewAddressPanel pnlViewAddress;
  private AddressPanel pnlAddress;
  Store store = null;
  ArmCurrency offlineTaxValue = null;

  /**
   */
  public void init() {
    try {
      jbInit();
      AddressMgr adMgr = new AddressMgr();
      Vector city = new Vector();
    } catch (Exception e) {}
  }

  /**
   */
  public void stop() {
	  offlineTaxValue = null;
  }

  //
  public String getScreenName() {
    return (res.getString("Send Sale"));
  }

  //
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * Start the applet.
   */
  public void applyCustomer() {
    if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
      try {
        CMSCustomer txnCust = (CMSCustomer)theAppMgr.getStateObject("TXN_CUSTOMER");
        theTxn.setCustomer(txnCust);
        if (!((CMSStore)store).isVATEnabled()) {
          TaxUtilities.applyTax(theAppMgr, theTxn, (CMSStore)theTxn.getStore()
              , (CMSStore)theTxn.getStore(), theTxn.getProcessDate());
        }
      } catch (Exception e) {
        theAppMgr.removeStateObject("TXN_CUSTOMER");
      }
    }
  }

  /**
   * put your documentation comment here
   */
  public void start() {
    theAppMgr.removeStateObject("NO_CHECK");
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    store = (Store)theAppMgr.getGlobalObject("STORE");
    theAppMgr.addStateObject("ARM_FROM_SHIPPING_ADDRESS", "FROM_SHIPPING");
    clear();
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN");
    shippingRequest = (CMSShippingRequest)theAppMgr.getStateObject("SHIPPING_REQUEST");
    applyCustomer();
    if (theAppMgr.getStateObject("SHIPPING_INQUIRY_ONLY") != null) {
      mode = INQUIRE_MODE;
      //ks: menu for Inquire Mode
      theAppMgr.showMenu(MenuConst.SHIPPING_HEADER_INQUIRY, theOpr);
    } else {
      mode = UPDATE_MODE;
      //ks:menu for update mode
      theAppMgr.showMenu(MenuConst.SHIPPING_HEADER, theOpr);
    }
    if (shippingRequest != null) {
      displayShippingRequest();
    } else {
      if (theTxn == null && theAppMgr.getStateObject("ADD_FROM_INQUIRY") != null)
        theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("THE_TXN");
      shippingRequest = new CMSShippingRequest(theTxn);
      CMSCustomer customer = (CMSCustomer)theTxn.getCustomer();
      Address custPrimAddress = customer.getPrimaryAddress();
      shippingRequest.doSetFirstName(customer.getFirstName());
      shippingRequest.doSetLastName(customer.getLastName());
      ((CMSShippingRequest)shippingRequest).setAddress(custPrimAddress);
      theAppMgr.addStateObject("SHIPPING_REQUEST", shippingRequest);
      displayShippingRequest();
    }
    if (mode == INQUIRE_MODE) {
      setFieldsEnabled(false);
    } else {
      setFieldsEnabled(true);
    }
    pleaseContinue();
  }

  /**
   *
   * @param mode
   */
  private void pleaseContinue() {
    if (mode == UPDATE_MODE) {
    	if (!theAppMgr.isOnLine()) {
    		System.out.println("shippingRequest.getCountry()=====" + shippingRequest.getCountry());
    		if(shippingRequest.getCountry().equalsIgnoreCase("CAN")){
    			System.out.println("shippingRequest.getCountry()===== is CANADA");
    			theAppMgr.setSingleEditArea(res.getString("Enter GST tax %."),"PROVINCE_GST_SALE_OFFLINE","",theAppMgr.CURRENCY_MASK);
   			 	theAppMgr.setEditAreaFocus();
    		}else if(shippingRequest.getCountry().equalsIgnoreCase("USA")){
        		if(shippingRequest.getState().equalsIgnoreCase("PR")){
	    			 theAppMgr.setSingleEditArea(res.getString("Enter city tax %."),"SEND_SALE_OFFLINE","",theAppMgr.CURRENCY_MASK);
	    				theAppMgr.setEditAreaFocus();
	    		 }else{
	    			 theAppMgr.setSingleEditArea(res.getString("Enter shipping tax %."),"SEND_SALE_OFFLINE","",theAppMgr.CURRENCY_MASK);
     				 theAppMgr.setEditAreaFocus();
	    		 }    			
    		}
		}
		if (theAppMgr.isOnLine()) {
			theAppMgr.setSingleEditArea(res.getString("Modify shipping information.") + "  "
					+ res.getString("Select 'Items' to continue to item selection."));
		}
    } else if (mode == INQUIRE_MODE) {
      theAppMgr.setSingleEditArea(res.getString("Select 'Items' to view shipment items."));
    } else {
      theAppMgr.setSingleEditArea(res.getString("Complete shipping information.") + "  "
          + res.getString("Select 'Items' to continue to item selection."));
    }
  }

  /**
   *
   */
  private void displayShippingRequest() {
    txtFirstName.setText(shippingRequest.getFirstName());
    txtLastName.setText(shippingRequest.getLastName());
    pnlAddress.setShippingRequest(shippingRequest);
    txtSpecialInstructions.setText(shippingRequest.getSpecialInstructions());
  }

  /**
   *
   * @param enable
   */
  private void setFieldsEnabled(boolean enable) {
    txtFirstName.setEditable(enable);
    txtFirstName.setEnabled(enable);
    txtLastName.setEditable(enable);
    txtLastName.setEnabled(enable);
    txtSpecialInstructions.setEditable(enable);
    txtSpecialInstructions.setEnabled(enable);
    pnlAddress.setEnabled(enable);
  }

  /**
   *
   */
  private void clear() {
    txtFirstName.setText("");
    txtLastName.setText("");
    txtSpecialInstructions.setText("");
    pnlAddress.reset();
  }

  /**
   *
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    //System.out.println("sAction: " + sAction);
    try {
      if (sAction.equals("ITEMS")) {
    	  if(!theAppMgr.isOnLine()){
    		  if(offlineTaxValue == null){
    			  theAppMgr.showErrorDlg(res.getString("System is offline . Please enter the correct tax rate to continue"));
    			 anEvent.consume();
    			  return;
    		  }
    	  }
        shippingRequest.setGiftMessage(txtGiftMessage.getText());
        shippingRequest.setSpecialInstructions(txtSpecialInstructions.getText());
        if (mode != INQUIRE_MODE) {
          // FirstName is optional for Japan
          //if(!((CMSStore)theStore).getPreferredISOCountry().equals("JP"))
          if (!"JP".equalsIgnoreCase(Version.CURRENT_REGION))
            shippingRequest.verifyFirstName(txtFirstName.getText());
          shippingRequest.setFirstName(txtFirstName.getText());
          shippingRequest.setLastName(txtLastName.getText());
          if (!verifyAllScreenInput()) {
            anEvent.consume();
            return;
          }
          pnlAddress.getCurrentAddressPanel().populateShippingRequest(shippingRequest);
        }
        theAppMgr.addStateObject("SHIPPING_REQUEST", shippingRequest);
      } else if (sAction.equals("CANCEL")) {
    	  offlineTaxValue = null;
    	if (mode != INQUIRE_MODE) {
          anEvent.consume();
          if ((theAppMgr.showOptionDlg(res.getString("Cancel Shipping Request")
              , res.getString("Are you sure you want to cancel this Shipping Request?"))) == true) {
            shippingRequest.delete();
            theAppMgr.removeStateObject("SHIPPING_REQUEST");
            theAppMgr.removeStateObject("TXN");
            theAppMgr.removeStateObject("SHIPPING_INQUIRY_ONLY");
            theAppMgr.fireButtonEvent("PREV");
          } else {
            anEvent.consume();
            return;
          }
        }
      } else if (sAction.equals("CLEAR")) {
        clear();
        anEvent.consume();
      } else if (sAction.equals("SPECIAL_INSTRUCTIONS")) {
        txtSpecialInstructions.requestFocus();
        anEvent.consume();
      } else if (sAction.equals("PREV")) {
    	  offlineTaxValue = null;
        theAppMgr.removeStateObject("TXN");
        //theAppMgr.removeStateObject("SHIPPING_INQUIRY_ONLY");
        if (shippingRequest.getLastName().length() == 0) {
          shippingRequest.delete();
        }
      }
    } catch (BusinessRuleException r) {
      anEvent.consume();
      theAppMgr.showErrorDlg(r.getMessage());
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
    }
  }

  /**
   *
   * @return
   */
  private boolean verifyAllScreenInput()
      throws BusinessRuleException {
    return txtFirstName.getVerifier().verify(txtFirstName)
        && txtLastName.getVerifier().verify(txtLastName) && pnlAddress.isValidAddress();
  }

  /**
   *
   * @param command
   * @param value
   */
  public void editAreaEvent(String command, String value) {
    try {
      if (command.equals("SCHED_BEGIN_DATE")) {} else if (command.equals("SCHED_END_DATE")) {}
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
    }
  }

  public void editAreaEvent(String command, ArmCurrency value) {
	    try {
	      if (command.equals("SEND_SALE_OFFLINE")) {
	    	
	    	// if(value.doubleValue()<1 && value.doubleValue()>=0){
	    		 shippingRequest.setOfflineShipTax(value.offlineDivide(10000));
	    		 offlineTaxValue = value.divide(100);
	    		 if(shippingRequest.getState().equalsIgnoreCase("PR")){
		    	 shippingRequest.setOfflineCityTax(value.offlineDivide(10000));
		  		 }
	    		 if(shippingRequest.getState().equalsIgnoreCase("PR")){
	    			 offlineTaxValue = null;
	    			 theAppMgr.setSingleEditArea(res.getString("Enter state tax %."),"STATE_TAX_OFFLINE","",theAppMgr.CURRENCY_MASK);
	    			theAppMgr.setEditAreaFocus();
	    		 }else{
	    			 theAppMgr.setSingleEditArea(res.getString("Modify shipping information.") + "  "
	   	    		          + res.getString("Select 'Items' to continue to item selection."));
	    		 }
	    		
	    	/* }else {
	    		 	offlineTaxValue = null;
	       	  theAppMgr.showErrorDlg(res.getString("Please enter the correct tax rate."));
	       	  
	       	 }*/
	   return;
	      }
	      
	      if (command.equals("STATE_TAX_OFFLINE")) {
	    	// if(value.doubleValue()<1 && value.doubleValue()>=0){
	    		 offlineTaxValue = value.divide(100);
	    		 shippingRequest.setOfflineStateTax(value.offlineDivide(10000));
	    		 shippingRequest.setStateTax(value.offlineDivide(10000));
	    	theAppMgr.setSingleEditArea(res.getString("Modify shipping information.") + "  "
	   	    		          + res.getString("Select 'Items' to continue to item selection."));
	    		// }
	    	/* else {
	    		 offlineTaxValue = null;
	       	  theAppMgr.showErrorDlg(res.getString("Please enter the correct tax rate."));
	       	
	  		 }*/
	    	   return;	
	    	 }
	
	      if(command.equals("PROVINCE_GST_SALE_OFFLINE")){	    	  
	    	  double gstTaxValue = value.offlineDivide(10000);
	    	  shippingRequest.setOfflineGstTax(gstTaxValue);
	    	  offlineTaxValue = null;
	    	  theAppMgr.setSingleEditArea(res.getString("Enter QST tax %."),"PROVINCE_QST_SALE_OFFLINE","",theAppMgr.CURRENCY_MASK);
  			  theAppMgr.setEditAreaFocus();	    	  
	      }
	      if(command.equals("PROVINCE_QST_SALE_OFFLINE")){
	    	  double qstTaxValue = value.offlineDivide(10000);
	    	  shippingRequest.setOfflineQstTax(qstTaxValue);
	    	  offlineTaxValue = null;
	    	  theAppMgr.setSingleEditArea(res.getString("Enter PST tax %."),"PROVINCE_PST_SALE_OFFLINE","",theAppMgr.CURRENCY_MASK);
  			  theAppMgr.setEditAreaFocus();
	      }
	      if(command.equals("PROVINCE_PST_SALE_OFFLINE")){
	    	  Double pstTaxValue = value.offlineDivide(10000);
	    	  shippingRequest.setOfflinePstTax(pstTaxValue);
	    	 offlineTaxValue = null;
	    	  theAppMgr.setSingleEditArea(res.getString("Enter HST tax %."),"PROVINCE_HST_SALE_OFFLINE","",theAppMgr.CURRENCY_MASK);
  			  theAppMgr.setEditAreaFocus();	 
	      }
	      if(command.equals("PROVINCE_HST_SALE_OFFLINE")){
	    	  double hstTaxValue =  value.offlineDivide(10000);
	    	  shippingRequest.setOfflineHstTax(hstTaxValue);
	    	  offlineTaxValue = new ArmCurrency(0.0d);
	    	  theAppMgr.setSingleEditArea(res.getString("Modify shipping information.") + "  "
	    		          + res.getString("Select 'Items' to continue to item selection."));
  			  theAppMgr.setEditAreaFocus();	 
	      }
	      
	    } catch (Exception e) {
	      theAppMgr.showExceptionDlg(e);
	    }
	    
	  }
  /**
   * ks: Modified to Add ShippingAddressPanel to the existing Applet.
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    pnlAddress = new AddressPanel(BaseAddressPanel.SHIPPING_ADDRESS_PANEL);
    pnlViewAddress = new ViewAddressPanel(pnlAddress);
    pnlAddress.setAppMgr(theAppMgr);
    pnlViewAddress.setAppMgr(theAppMgr);
    JPanel jPanel1 = new JPanel();
    JPanel commentPanel = new JPanel();
    JPanel mainPanel = new JPanel();
    JCMSLabel lblSpecialInstructions = new JCMSLabel();
    JCMSLabel lblFirstName = new JCMSLabel();
    JCMSLabel lblLastName = new JCMSLabel();
    JScrollPane giftPane;
    JScrollPane instructionPane;
    JPanel pnlBuffer = new JPanel();
    pnlBuffer.setOpaque(false);
    jPanel1.setLayout(new GridBagLayout());
    jPanel1.add(lblFirstName
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.RELATIVE, new Insets(5, 10, 2, 0), 0, 0));
    jPanel1.add(lblLastName
        , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.RELATIVE, new Insets(5, 10, 2, 0), 0, 0));
    jPanel1.add(txtFirstName
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.RELATIVE, new Insets(0, 10, 5, 0), 0, 0));
    jPanel1.add(txtLastName
        , new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.RELATIVE, new Insets(0, 10, 5, 0), 0, 0));
    jPanel1.add(pnlAddress
        , new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST
        , GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    //Vivek Mishra : Added in order to avoid null dimension error while send sale.
    //System.out.println("**************************** AddressPanel width : "+jPanel1.getComponent(4).getPreferredSize().width+"***************************");
    int i = jPanel1.getComponent(4).getPreferredSize().width;
    //System.out.println("**************************** AddressPanel width : "+jPanel1.getComponent(4).getPreferredSize().height+"*****************************");
    //Ends
    lblSpecialInstructions.setText(res.getString("Special Instructions:"));
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.setLayout(new BorderLayout());
    lblFirstName.setText(res.getString("First Name:"));
    lblLastName.setText(res.getString("Last Name:"));
    mainPanel.add(jPanel1, BorderLayout.NORTH);
    jPanel1.setBorder(BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.
        createLineBorder(new Color(153, 153, 153), 2), res.getString("Shipping Information"))
        , BorderFactory.createEmptyBorder(0, 5, 5, 5)));
    commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
    instructionPane = new JScrollPane(txtSpecialInstructions);
    instructionPane.setBorder(BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.
        createLineBorder(new Color(153, 153, 153), 2), "Special Instructions:")
        , BorderFactory.createEmptyBorder(0, 5, 5, 5)));
    instructionPane.setBackground(theAppMgr.getBackgroundColor());
    commentPanel.add(instructionPane, null);
    mainPanel.add(commentPanel, BorderLayout.CENTER);
    jPanel1.setBackground(theAppMgr.getBackgroundColor());
    lblFirstName.setAppMgr(theAppMgr);
    lblLastName.setAppMgr(theAppMgr);
    txtFirstName.setAppMgr(theAppMgr);
    txtLastName.setAppMgr(theAppMgr);
    lblSpecialInstructions.setAppMgr(theAppMgr);
    txtSpecialInstructions.setAppMgr(theAppMgr);
    txtFirstName.setVerifier(new FirstNameVerifier());
    txtLastName.setVerifier(new LastNameVerifier());
    txtFirstName.setPreferredSize(new Dimension((int)(250 * r), (int)(25 * r)));
    txtLastName.setPreferredSize(new Dimension((int)(250 * r), (int)(25 * r)));
    txtLastName.setDocument(new TextFilter(NAME_SPEC_NUM, 30));
    txtFirstName.setDocument(new TextFilter(NAME_SPEC_NUM, 30));
  }

  /************************************************************************/
  private class FirstNameVerifier extends CMSInputVerifier {

    /**
     *
     * @param input
     * @return
     */
    public boolean verify(JComponent input) {
      try {
        if (((JCMSTextField)input).getText().length() == 0) {
          throw new BusinessRuleException(res.getString("This is a required field"));
        }
        shippingRequest.setFirstName(((JCMSTextField)input).getText());
      } catch (BusinessRuleException bx) {
        ((JCMSTextField)input).requestFocus();
        theAppMgr.showErrorDlg(bx.getMessage());
        return (false);
      }
      return (true);
    }
  }


  private class LastNameVerifier extends CMSInputVerifier {

    /**
     *
     * @param input
     * @return
     */
    public boolean verify(JComponent input) {
      try {
        if (((JCMSTextField)input).getText().length() == 0) {
          throw new BusinessRuleException(res.getString("This is a required field"));
        }
        shippingRequest.setLastName(((JCMSTextField)input).getText());
      } catch (BusinessRuleException bx) {
        ((JCMSTextField)input).requestFocus();
        theAppMgr.showErrorDlg(bx.getMessage());
        return (false);
      }
      return (true);
    }
  }


  private class AddressVerifier extends CMSInputVerifier {

    /**
     *
     * @param input
     * @return
     */
    public boolean verify(JComponent input) {
      try {
        if (((JCMSTextField)input).getText().length() == 0) {
          throw new BusinessRuleException(res.getString("This is a required field"));
        }
        shippingRequest.setAddress(((JCMSTextField)input).getText());
      } catch (BusinessRuleException bx) {
        ((JCMSTextField)input).requestFocus();
        theAppMgr.showErrorDlg(bx.getMessage());
        return (false);
      }
      return (true);
    }
  }


  private class CityVerifier extends CMSInputVerifier {

    /**
     *
     * @param input
     * @return
     */
    public boolean verify(JComponent input) {
      try {
        if (((String)((JCMSTextField)input).getText()) != null) {
          if (((String)((JCMSTextField)input).getText()).length() == 0) {
            throw new BusinessRuleException(res.getString("City: This is a required field"));
          }
          shippingRequest.setCity((String)((JCMSTextField)input).getText());
        }
      } catch (BusinessRuleException bx) {
        ((JCMSTextField)input).requestFocus();
        theAppMgr.showErrorDlg(bx.getMessage());
        return (false);
      }
      return (true);
    }
  }


  private class StateVerifier extends CMSInputVerifier {

    /**
     *
     * @param input
     * @return
     */
    public boolean verify(JComponent input) {
      try {
        if (((JCMSTextField)input).getText().length() == 0) {
          throw new BusinessRuleException(res.getString("State: This is a required field"));
        }
        shippingRequest.setState(((JCMSTextField)input).getText());
      } catch (BusinessRuleException bx) {
        ((JCMSTextField)input).requestFocus();
        theAppMgr.showErrorDlg(bx.getMessage());
        return (false);
      }
      return (true);
    }
  }


  private class ZipVerifier extends CMSInputVerifier {

    /**
     *
     * @param input
     * @return
     */
    public boolean verify(JComponent input) {
      try {
        String zipCode = ((JCMSTextField)input).getText();
        if (zipCode.length() == 0) {
          throw new BusinessRuleException(res.getString("Zip: This is a required field"));
        }
        shippingRequest.setZipCode(((JCMSTextField)input).getText());
      } catch (BusinessRuleException bx) {
        ((JCMSTextField)input).requestFocus();
        theAppMgr.showErrorDlg(bx.getMessage());
        return (false);
      }
      return (true);
    }
  }


  private class CountryVerifier extends CMSInputVerifier {

    /**
     *
     * @param input
     * @return
     */
    public boolean verify(JComponent input) { //ks: Commented this since not used for US code drop need to modify for Europe and GLobal
      return (true);
    }
  }


  private class PhoneVerifier extends CMSInputVerifier {

    /**
     *
     * @param input
     * @return
     */
    public boolean verify(JComponent input) {
      try {
        if (((JCMSTextField)input).getText().length() == 0) {
          throw new BusinessRuleException(res.getString("Phone: This is a required field"));
        }
        shippingRequest.setPhone(((JCMSTextField)input).getText());
      } catch (BusinessRuleException bx) {
        ((JCMSTextField)input).requestFocus();
        theAppMgr.showErrorDlg(bx.getMessage());
        return (false);
      }
      return (true);
    }
  }


  /**
   * @return
   */
  /**
   * MP: Home pressed at customer display exits transaction with no message
   * @return
   */
  public boolean isHomeAllowed() 
	{
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr
				.getStateObject("TXN_POS");
		if (cmsTxn == null) {
			return (true);
		}
		/*
		 * Added by Yves Agbessi (05-Dec-2017) Handles the posting of the Sign
		 * Off event for Fiscal Solutions Service START--
		 */
		boolean goToHomeView = (theAppMgr
				.showOptionDlg(
						res.getString("Cancel Transaction"),
						res.getString("Are you sure you want to cancel this transaction?")));
		if (goToHomeView) {
			ARMFSBridge.postARMSignOffTransaction((CMSEmployee) theOpr);
		}

	return goToHomeView;
		/*
		 * --END
		 */

 }
}

