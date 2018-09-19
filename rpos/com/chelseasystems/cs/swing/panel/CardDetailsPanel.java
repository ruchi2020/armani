/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04/12/2005 | Anand     | N/A       | Change Request to accomodate and attach credit card|
 |      |            |           |           | numbers to the customer during creation/mod        |
 --------------------------------------------------------------------------------------------------
 | 1    | 03-05-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import java.awt.*;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import java.util.ResourceBundle;
import com.chelseasystems.cr.swing.bean.JCMSCheckBox;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.JCMSComboBox;
import com.chelseasystems.cr.swing.bean.JCMSTextArea;
import com.chelseasystems.cr.swing.bean.JCMSMaskedTextField;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cs.swing.CMSTextFilter;

/**
 * <p>Title:CardDetailsPanel </p>
 *
 * <p>Description: Displays CardDetails</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNetInc</p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class CardDetailsPanel extends JPanel {
  private IApplicationManager theAppMgr;
  /**
   * Cards Panel
   */
  private JPanel pnlCards;
  /**
   * Credit card no. 1
   */
  private JCMSLabel lblCardNum1;
  /**
   * Txt for CardNum1
   */
  private JCMSTextField txtCardNum1;
  /**
   * CardDetails 1
   */
  private JCMSLabel lblCardDet1;
  /**
   * CardDetails 1 value
   */
  private JCMSTextField txtCardDet1;
  /**
   * Card Num2
   */
  private JCMSLabel lblCardNum2;
  /**
   * CardNum2 value
   */
  private JCMSTextField txtCardDet2;
  /**
   * CardDetails 2 value
   */
  private JCMSTextField txtCardNum2;
  /**
   * Card Details2
   */
  private JCMSLabel lblCardDet2;
  /**
   * Resource
   */
  private final ResourceBundle RESOURCE;
  /**
   * ALPHA_NUMERIC WITH SPECIAL CHARACTER.
   */
  public static final String ALPHA_NUMERIC_SPEC =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";

  /**
   * Default Constructor
   */
  public CardDetailsPanel() {
    RESOURCE = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Get CreditCardNumber1
   * @return CreditCardNumber1
   */
  public String getCardNumber1() {
    return txtCardNum1.getText().trim();
  }

  /**
   * Set CreditCardNumber1
   * @param sValue CreditCardNumber1
   */
  public void setCardNumber1(String sValue) {
    if (sValue == null || sValue.trim().length() < 1) {
      return;
    }
    txtCardNum1.setText(sValue);
  }

  /**
   * Get CardDetails1
   * @return CardDetails1
   */
  public String getCardDetails1() {
    return txtCardDet1.getText().trim();
  }

  /**
   * Set CardDetails1
   * @param sValue CardDetails1
   */
  public void setCardDetails1(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtCardDet1.setText(sValue);
  }

  /**
   * Get CreditCardNumber2
   * @return CreditCardNumber2
   */
  public String getCardNumber2() {
    return txtCardNum2.getText().trim();
  }

  /**
   * Set CreditCardNumber2
   * @param sValue CreditCardNumber2
   */
  public void setCardNumber2(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtCardNum2.setText(sValue);
  }

  /**
   * Get CardDetails2
   * @return CardDetails2
   */
  public String getCardDetails2() {
    return txtCardDet2.getText().trim();
  }

  /**
   * Set CardDetails2
   * @param sValue CardDetails2
   */
  public void setCardDetails2(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtCardDet2.setText(sValue);
  }

  /**
   * put your documentation comment here
   */
  public void requestFocusOnCardDet1() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        txtCardDet1.requestFocus();
      }
    });
  }

  /**
   * put your documentation comment here
   */
  public void requestFocusOnCardDet2() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        txtCardDet2.requestFocus();
      }
    });
  }

  /**
   * put your documentation comment here
   */
  public void requestFocusOnCardNum1() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        txtCardNum1.requestFocus();
      }
    });
  }

  /**
   * put your documentation comment here
   */
  public void requestFocusOnCardNum2() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        txtCardNum2.requestFocus();
      }
    });
  }

  /**
   * Reset view.
   */
  public void reset() {
    txtCardNum1.setText("");
    txtCardDet2.setText("");
    txtCardDet1.setText("");
    txtCardNum2.setText("");
  }

  /**
   * Set Application Manager
   * @param theAppMgr ApplicationManager
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.theAppMgr = theAppMgr;
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlCards.setBackground(theAppMgr.getBackgroundColor());
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    //Loop through and set Application managers for all
    // JCMS components.
    for (int iCtr = 0; iCtr < pnlCards.getComponentCount(); iCtr++) {
      Component component = pnlCards.getComponent(iCtr);
      Font lblFont = theAppMgr.getTheme().getLabelFont();
      Font txtFont = theAppMgr.getTheme().getTextFieldFont();
      if (component instanceof JCMSTextField) {
        ((JCMSTextField)component).setAppMgr(theAppMgr);
        ((JCMSTextField)component).setFont(txtFont);
        ((JCMSTextField)component).setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
      } else if (component instanceof JCMSTextArea) {
        ((JCMSTextArea)component).setAppMgr(theAppMgr);
      } else if (component instanceof JCMSLabel) {
        ((JCMSLabel)component).setAppMgr(theAppMgr);
      } else if (component instanceof JCMSComboBox) {
        ((JCMSComboBox)component).setAppMgr(theAppMgr);
      } else if (component instanceof JCMSMaskedTextField) {
        ((JCMSMaskedTextField)component).setAppMgr(theAppMgr);
      } else if (component instanceof JCMSCheckBox) {
        ((JCMSCheckBox)component).setAppMgr(theAppMgr);
      }
    }
  }

  /**
   * Initialize and layout components.
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    pnlCards = new JPanel();
    lblCardNum1 = new JCMSLabel();
    txtCardNum1 = new JCMSTextField();
    lblCardDet1 = new JCMSLabel();
    txtCardDet1 = new JCMSTextField();
    lblCardNum2 = new JCMSLabel();
    txtCardDet2 = new JCMSTextField();
    txtCardNum2 = new JCMSTextField();
    lblCardDet2 = new JCMSLabel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    this.setLayout(new BorderLayout());
    pnlCards.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , RESOURCE.getString("Credit Card Details")));
    pnlCards.setSize(833, 112);
    pnlCards.setLayout(gridBagLayout1);
    lblCardNum1.setText(RESOURCE.getString("Card Number 1"));
    txtCardNum1.setText("");
    lblCardDet1.setText(RESOURCE.getString("Card type and Expiration Date"));
    txtCardDet1.setText("");
    lblCardNum2.setText(RESOURCE.getString("Card Number 2"));
    txtCardNum2.setText("");
    lblCardDet2.setText(RESOURCE.getString("Card type and Expiration Date"));
    this.add(pnlCards, BorderLayout.NORTH);
    pnlCards.add(lblCardNum1
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(8, 10, 0, 0), 106, 4));
    pnlCards.add(txtCardNum1
        , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 180, -1));
    pnlCards.add(lblCardDet1
        , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(8, 11, 0, 10), 12, 4));
    pnlCards.add(txtCardDet1
        , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 11, 0, 10), 163, -1));
    pnlCards.add(lblCardNum2
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 106, 4));
    pnlCards.add(txtCardDet2
        , new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 11, 11, 10), 163, -1));
    pnlCards.add(txtCardNum2
        , new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 10, 11, 0), 180, -1));
    pnlCards.add(lblCardDet2
        , new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(6, 11, 0, 10), 13, 3));
    /**
     * Added TextVerifier
     */
    // MSB 02/10/2006
    // To resolve Double byte issues
    // we need to make sure text isn't filtered
    // and only length restriction is imposed on input.
    txtCardDet1.setDocument(new CMSTextFilter(30));
    txtCardDet2.setDocument(new CMSTextFilter(30));
    txtCardNum1.setDocument(new CMSTextFilter(30));
    txtCardNum2.setDocument(new CMSTextFilter(30));
//    txtCardDet1.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 30));
//    txtCardDet2.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 30));
//    txtCardNum1.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 30));
//    txtCardNum2.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 30));
  }
}

