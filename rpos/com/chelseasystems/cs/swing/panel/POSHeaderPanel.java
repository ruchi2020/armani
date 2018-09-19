/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-03-2005 | Manpreet  | N/A       | Modified  to add Loyalty place holder       |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.swing.panel;

import java.util.ResourceBundle;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.config.*;


/**
 */
public class POSHeaderPanel extends JPanel {
  JPanel pnlOperator;
  JPanel pnlConsultant;
  JPanel pnlCustomer;
  JCMSLabel lblOperator;
  JCMSLabel lblConsultant;
  //JCMSLabel lblCustomerPhone;
  JCMSLabel lblCustomerId;
  JCMSLabel lblOperatorId;
  JCMSLabel lblConsultantId;
  JCMSLabel lblCustomerName;
  JCMSLabel lblLoyalty;
  JCMSLabel lblTaxExemptID;
  JCMSLabel lblExempt;
  String sLoyaltyString;
  String sTaxExemptIDString;
  private final ResourceBundle RESOURCE_BUNDLE;
  IApplicationManager myAppMgr;
  static double loyaltyRewardLevel;
  private static final String CONFIGURATION_FILE = "pos.cfg";
  private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
  private String viewAssocAddItemHeaderPanelString = 
	  	configMgr.getString("POS.VIEW_ASSOCIATE_ADD_ITEM_HEADER_PANEL");
  /**
   */
  static {
    ConfigMgr cfg = new ConfigMgr("loyalty.cfg");
    String rewdAmt = cfg.getString("LOYALTY_REWARD_AMOUNT");
    String rewdRedemptionRatio = cfg.getString("LOYALTY_REWARD_REDEMPTION_RATIO");
    loyaltyRewardLevel = new Double(rewdAmt).doubleValue()
        * new Double(rewdRedemptionRatio).doubleValue();
  }

  /**
   * put your documentation comment here
   */
  public POSHeaderPanel() {
    RESOURCE_BUNDLE = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    try {
      sLoyaltyString = RESOURCE_BUNDLE.getString("Points to Reward");
      sTaxExemptIDString = RESOURCE_BUNDLE.getString("Tax Exempt ID");
      pnlOperator = new JPanel();
      pnlConsultant = new JPanel();
      pnlCustomer = new JPanel();
      lblOperator = new JCMSLabel();
      lblConsultant = new JCMSLabel();
      //Following commented as per Armani requirement
      //lblCustomerPhone = new JCMSLabel();
      lblCustomerId = new JCMSLabel();
      lblOperatorId = new JCMSLabel();
      lblConsultantId = new JCMSLabel();
      lblCustomerName = new JCMSLabel();
      lblExempt = new JCMSLabel();
      lblLoyalty = new JCMSLabel("");
      lblTaxExemptID = new JCMSLabel(sTaxExemptIDString);
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
    final ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setLayout(new GridLayout(0, 3));
    this.setPreferredSize(new Dimension(844, 75));
    lblOperator.setText("operator");
    lblConsultant.setText("consult");
    lblCustomerName.setText("name");
    //commented following on 03/15/2005 as armani does not require phone number to be shown. Show Cust Id instead
    //lblCustomerPhone.setText("phone");
    lblCustomerId.setText("ID: ");
    lblOperatorId.setText("ID: ");
    lblConsultantId.setText("ID: ");
    lblExempt.setText("");
    this.add(pnlOperator, null);
    this.add(pnlConsultant, null);
    this.add(pnlCustomer, null);
    pnlOperator.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , res.getString("Operator")));
    pnlOperator.setOpaque(false);
    pnlOperator.setLayout(new GridBagLayout());
    pnlConsultant.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , res.getString("Associate")));
    pnlConsultant.setOpaque(false);
    pnlConsultant.setLayout(new GridBagLayout());
    pnlCustomer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , res.getString("Customer")));
    pnlCustomer.setOpaque(false);
    pnlCustomer.setLayout(new GridBagLayout());
    lblOperator.setFont(new Font("Helvetica", 1, 12));
    lblConsultant.setFont(new Font("Helvetica", 1, 12));
    //Following line commented by Anand on 03/15/2005 as Armani now requires to show customer ID instead of phone
    //lblCustomerPhone.setFont(new Font("Helvetica", 1, 12));
    lblCustomerId.setFont(new Font("Helvetica", 1, 12));
    lblOperatorId.setFont(new Font("Helvetica", 1, 12));
    lblConsultantId.setFont(new Font("Helvetica", 1, 12));
    lblCustomerName.setFont(new Font("Helvetica", 1, 12));
    lblLoyalty.setFont(new Font("Helvetica", 1, 12));
    lblExempt.setFont(new Font("Helvetica", 1, 12));
    pnlOperator.add(lblOperator
        , new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    pnlConsultant.add(lblConsultant
        , new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    //code added by Anand on 03/15 to show relevant IDs for Armani
    pnlConsultant.add(lblConsultantId
        , new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(2, 5, 0, 0), 0, 0));
    pnlOperator.add(lblOperatorId
        , new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(2, 5, 0, 0), 0, 0));
    pnlCustomer.add(lblCustomerName
        , new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    pnlCustomer.add(lblCustomerId
        , new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(2, 5, 0, 0), 0, 0));
    pnlCustomer.add(lblLoyalty
        , new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(2, 5, 0, 0), 0, 0));
    pnlCustomer.add(lblExempt
        , new GridBagConstraints(0, 3, 1, 1, 0.1, 0.2, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(2, 5, 0, 5), 0, 0));
  }

  /**
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.setBackground(theAppMgr.getBackgroundColor());
    lblOperator.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblConsultant.setFont(theAppMgr.getTheme().getTextFieldFont());
    //lblCustomerPhone.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblCustomerId.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblOperatorId.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblConsultantId.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblCustomerName.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblLoyalty.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblExempt.setFont(theAppMgr.getTheme().getTextFieldFont());
    myAppMgr = theAppMgr;
  }

  /**
   */
  public void setProperties(CMSCustomer cust, CMSEmployee opr, CMSEmployee con, CMSStore store
      , String federalId, String regionalId) {
    this.setProperties(cust, opr, con);
    this.setTaxExemptId(store, federalId, regionalId);
  }

  /**
   */
  public void setProperties(CMSCustomer cust, CMSEmployee opr, CMSEmployee con) {
	if (cust == null) {
      lblCustomerName.setText("");
      lblCustomerId.setText("");
      lblLoyalty.setText("");
    } else {
    	//PCR1328 View Associate Add Item fix for Armani Japan
    	if(viewAssocAddItemHeaderPanelString == null) {
    		lblCustomerName.setText(cust.getFirstName() + " " + cust.getLastName());
    	} else {
    		lblCustomerName.setText(cust.getLastName() + " " + cust.getFirstName());
    	}
      //lblCustomerPhone.setText(cust.getTelephone() == null ? "" : cust.getTelephone().getFormattedNumber());
      //following code added by Anand on 03/15/2005 as per change request by Armani
      lblCustomerId.setText("ID: " + cust.getId());
      // --Manpreet S Bawa (02/03/2005)
      // To-DO ... Get amount of loyality for the customer and display it.
      // -- Part of Loyalty Spec.
      this.updateAmtToReward();
    }
    if (opr == null) {
      lblOperator.setText("");
      lblOperatorId.setText("");
    } else {
    	if(viewAssocAddItemHeaderPanelString == null) {
    		lblOperator.setText(opr.getFirstName() + " " + opr.getLastName());
    	} else {
    		lblOperator.setText(opr.getLastName() + " " + opr.getFirstName());
    	}
      lblOperatorId.setText("ID: " + ((CMSEmployee)opr).getExternalID());
    }
    if (con == null) {
      lblConsultant.setText("");
      lblConsultantId.setText("");
    } else {
    	if(viewAssocAddItemHeaderPanelString == null) {
    		lblConsultant.setText(con.getFirstName() + " " + con.getLastName());
    	} else {
    		lblConsultant.setText(con.getLastName() + " " + con.getFirstName());
    	}
      lblConsultantId.setText("ID: " + ((CMSEmployee)con).getExternalID());
    }
  }

  /**
   */
  public void setTaxExemptId(CMSStore store, String federalId, String regionalId) {
    //final ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    StringBuffer buf = new StringBuffer();
    if (federalId.length() > 0) {
      buf.append(RESOURCE_BUNDLE.getString(store.getTaxLabel()));
    }
    if (regionalId.length() > 0) { //theStore.usesRegionalTaxCalculations()
      if (buf.length() > 0) {
        buf.append("/");
      }
      buf.append(RESOURCE_BUNDLE.getString(store.getRegionalTaxLabel()));
    }
    if (buf.length() > 0) {
      buf.append(" ");
      buf.append(RESOURCE_BUNDLE.getString("Exempt ID"));
      buf.append(": ");
    }
    // #bug 358
    if (federalId.length() > 0) {
    	//Fix for 1763: 
    	//Add tender> Displays in customer column. "Sale Tax Exempt ID: Tax Exempt ID:...."
      //buf.append("Tax Exempt ID: ");
      buf.append(federalId);
    }
    if (regionalId.length() > 0) { //theStore.usesRegionalTaxCalculations()
      if (federalId.length() > 0) {
        buf.append("/");
      }
      buf.append(regionalId);
    }
    lblExempt.setText(buf.toString());
  }

  /**
   * put your documentation comment here
   */
  public void updateAmtToReward() {
    PaymentTransaction txn = (PaymentTransaction)myAppMgr.getStateObject("TXN_POS");
    if (txn instanceof CMSCompositePOSTransaction) {
      CMSCompositePOSTransaction theTxn = ((CMSCompositePOSTransaction)txn);
      if (theTxn.getCustomer() != null && theTxn.getLoyaltyCard() != null) {
        // Applicable only to sale transactions
        if (theTxn.getTransactionType().startsWith("SALE")
            || theTxn.getTransactionType().startsWith("RETN")
            || theTxn.getTransactionType().startsWith("PEND")) {
          double currpoints = theTxn.getLoyaltyPoints();
          //System.out.println("Curr Points : " + currpoints);
          double loyaltyBalance = theTxn.getLoyaltyCard().getCurrBalance();
          //System.out.println("loyaltyBalance : " + loyaltyBalance);
          double x = java.lang.Math.floor(new Double(((loyaltyBalance + currpoints)
              / loyaltyRewardLevel)).doubleValue()) + 1;
          double nextRewardLevel = x * loyaltyRewardLevel;
          java.text.NumberFormat numFor = java.text.NumberFormat.getInstance();
          numFor.setMaximumFractionDigits(2);
          lblLoyalty.setText(sLoyaltyString + ": "
              + numFor.format((nextRewardLevel - (loyaltyBalance + currpoints))));
        }
      } else
        lblLoyalty.setText("");
      if (theTxn.getCustomer() == null) {
        lblCustomerName.setText("");
        lblCustomerId.setText("");
      } else {
    	//PCR1328 View Associate Add Item fix for Armani Japan
      	if(viewAssocAddItemHeaderPanelString == null) {
      		lblCustomerName.setText(theTxn.getCustomer().getFirstName() + " "
      				+ theTxn.getCustomer().getLastName());
      	} else {
      		lblCustomerName.setText(theTxn.getCustomer().getLastName() + " "
      				+ theTxn.getCustomer().getFirstName());
      	}
        //lblCustomerPhone.setText(cust.getTelephone() == null ? "" : cust.getTelephone().getFormattedNumber());
        //following code added by Anand on 03/15/2005 as per change request by Armani
        lblCustomerId.setText("ID: " + theTxn.getCustomer().getId());
      }
    }
  }
}

