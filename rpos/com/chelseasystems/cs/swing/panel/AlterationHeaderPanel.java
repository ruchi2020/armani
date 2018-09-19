/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.JPanel;
import java.awt.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import javax.swing.BorderFactory;
import java.awt.event.*;
import javax.swing.SwingUtilities;
import com.chelseasystems.cr.appmgr.mask.MaskManager;


/**
 * <p>Title:AlterationHeaderPanel </p>
 * <p>Description: HeaderPanel  </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04-21-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05-14-2005 | Manpreet  | N/A       | Allowing Backspace and Delete Key in datefields    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class AlterationHeaderPanel extends JPanel {
  private JPanel pnlItemDetails;
  private JPanel pnlAlterationDetails;
  private JCMSLabel lblSku;
  private JCMSLabel lblSkuValue;
  private JCMSLabel lblFabric;
  private JCMSLabel lblFabricValue;
  private JCMSLabel lblStyle;
  private JCMSLabel lblStyleValue;
  private JCMSLabel lblColor;
  private JCMSLabel lblColorValue;
  private JCMSLabel lblModel;
  private JCMSLabel lblModelValue;
  private JCMSLabel lblSupplier;
  private JCMSLabel lblSupplierValue;
  private JCMSLabel lblAlterationId = new JCMSLabel();
  private JCMSTextField txtAlterationID = new JCMSTextField();
  private JCMSLabel lblFitterID = new JCMSLabel();
  private JCMSTextField txtFitterID = new JCMSTextField();
  private JCMSLabel lblTryDate = new JCMSLabel();
  private JCMSTextField txtTryDate = new JCMSTextField();
  private JCMSLabel lblTailorID = new JCMSLabel();
  private JCMSTextField txtTailorID = new JCMSTextField();
  private JCMSLabel lblPromiseDate = new JCMSLabel();
  private JCMSTextField txtPromiseDate = new JCMSTextField();
  private JCMSLabel lblTotalPrice = new JCMSLabel();
  private JCMSTextField txtTotalPrice = new JCMSTextField();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  public static final int ALTERATION_ID = 0;
  public static final int PROMISE_DATE = 1;
  public static final int FITTER_ID = 2;
  public static final int TAILOR_ID = 3;
  public static final int TRY_DATE = 4;
  public static final int TOTAL_PRICE = 5;
  private final String CURRENCY_FILTER = "$0123456789.";
  private final String DATE_FILTER = "0123456789/";
  private boolean bPriceOverrideAllowed = true;
  private int iFocusableIndex = 0;
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private IApplicationManager theAppMgr;

  /**
   * put your documentation comment here
   */
  public AlterationHeaderPanel() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.theAppMgr = theAppMgr;
    int iCtr = 0;
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlItemDetails.setBackground(theAppMgr.getBackgroundColor());
    pnlAlterationDetails.setBackground(theAppMgr.getBackgroundColor());
    //  jpan2.setBackground(theAppMgr.getBackgroundColor());
    //   jpan1.setBackground(theAppMgr.getBackgroundColor());
    Font fntLabel = theAppMgr.getTheme().getTextFieldFont();
    //Loop through and set Application managers for all
    // JCMS components.
    for (iCtr = 0; iCtr < pnlItemDetails.getComponentCount(); iCtr++) {
      Component component = pnlItemDetails.getComponent(iCtr);
      if (component instanceof JCMSTextField) {
        ((JCMSTextField)component).setAppMgr(theAppMgr);
        ((JCMSTextField)component).setFont(fntLabel);
      } else if (component instanceof JCMSLabel) {
        ((JCMSLabel)component).setAppMgr(theAppMgr);
        ((JCMSLabel)component).setFont(fntLabel);
      }
    }
    for (iCtr = 0; iCtr < pnlAlterationDetails.getComponentCount(); iCtr++) {
      Component component = pnlAlterationDetails.getComponent(iCtr);
      if (component instanceof JCMSTextField) {
        ((JCMSTextField)component).setAppMgr(theAppMgr);
        ((JCMSTextField)component).setFont(fntLabel);
      } else if (component instanceof JCMSLabel) {
        ((JCMSLabel)component).setAppMgr(theAppMgr);
        ((JCMSLabel)component).setFont(fntLabel);
      }
    }
  }

  /**
   * put your documentation comment here
   * @param iFocusField
   */
  public void requestFocusTo(int iFocusField) {
    iFocusableIndex = iFocusField;
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        SwingUtilities.invokeLater(new Runnable() {

          /**
           * put your documentation comment here
           */
          public void run() {
            switch (iFocusableIndex) {
              case ALTERATION_ID:
                txtAlterationID.requestFocus();
                break;
              case TRY_DATE:
                txtTryDate.requestFocus();
                break;
              case PROMISE_DATE:
                txtPromiseDate.requestFocus();
                break;
              case FITTER_ID:
                txtFitterID.requestFocus();
                break;
              case TAILOR_ID:
                txtTailorID.requestFocus();
                break;
              case TOTAL_PRICE:
                txtTotalPrice.requestFocus();
            }
          }
        });
      }
    });
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSku(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    lblSkuValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setFabric(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    lblFabricValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setStyle(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    lblStyleValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setColor(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    lblColorValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setModel(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    lblModelValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSupplier(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    lblSupplierValue.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFitterID() {
    return txtFitterID.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setFitterID(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtFitterID.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTailorID() {
    return txtTailorID.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setTailorID(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtTailorID.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getPromiseDate() {
    return txtPromiseDate.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setPromiseDate(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtPromiseDate.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotalPrice() {
    return txtTotalPrice.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setTotalPrice(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtTotalPrice.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTryDate() {
    return txtTryDate.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setTryDate(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtTryDate.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAlterationID() {
    return txtAlterationID.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setAlterationID(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtAlterationID.setText(sValue);
  }

  /**
   * put your documentation comment here
   */
  public void reset() {
    lblColorValue.setText("");
    lblSkuValue.setText("");
    lblFabricValue.setText("");
    lblSupplierValue.setText("");
    lblModelValue.setText("");
    lblStyleValue.setText("");
    txtAlterationID.setText("");
    txtFitterID.setText("");
    txtTryDate.setText("");
    txtTailorID.setText("");
    txtPromiseDate.setText("");
    txtTotalPrice.setText("");
    bPriceOverrideAllowed = true;
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    lblSku = new JCMSLabel();
    lblSkuValue = new JCMSLabel();
    lblFabric = new JCMSLabel();
    lblFabricValue = new JCMSLabel();
    lblStyle = new JCMSLabel();
    lblStyleValue = new JCMSLabel();
    lblColor = new JCMSLabel();
    lblColorValue = new JCMSLabel();
    lblModel = new JCMSLabel();
    lblModelValue = new JCMSLabel();
    lblSupplier = new JCMSLabel();
    lblSupplierValue = new JCMSLabel();
    lblAlterationId = new JCMSLabel();
    txtAlterationID = new JCMSTextField();
    lblFitterID = new JCMSLabel();
    txtFitterID = new JCMSTextField();
    lblTryDate = new JCMSLabel();
    txtTryDate = new JCMSTextField();
    lblTailorID = new JCMSLabel();
    txtTailorID = new JCMSTextField();
    lblPromiseDate = new JCMSLabel();
    txtPromiseDate = new JCMSTextField();
    lblTotalPrice = new JCMSLabel();
    txtTotalPrice = new JCMSTextField();
    txtPromiseDate.setName("PromiseDate");
    txtTryDate.setName("TryDate");
    txtTotalPrice.setName("TotalPrice");
    txtPromiseDate.addKeyListener(new KeyVerifier());
    txtTryDate.addKeyListener(new KeyVerifier());
    txtTotalPrice.addKeyListener(new KeyVerifier());
    txtTailorID.setEnabled(false);
    txtFitterID.setEnabled(false);
    txtAlterationID.setEnabled(false);
    //TD
    //txtPromiseDate.setNextFocusableComponent(txtTotalPrice);
    //txtTryDate.setNextFocusableComponent(txtPromiseDate);
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    lblSku.setText(res.getString("SKU") + ":");
    lblFabric.setText(res.getString("Fabric") + ":");
    lblStyle.setText(res.getString("Style") + ":");
    lblColor.setText(res.getString("Color") + ":");
    lblModel.setText(res.getString("Model") + ":");
    lblSupplier.setText(res.getString("Supplier") + ":");
    lblAlterationId.setLabelFor(txtAlterationID);
    lblAlterationId.setText(res.getString("Alteration Id") + ":");
    lblFitterID.setLabelFor(txtFitterID);
    lblFitterID.setText(res.getString("FitterID") + ":");
    lblTryDate.setLabelFor(txtTryDate);
    lblTryDate.setText(res.getString("Try Date") + ":");
    lblTailorID.setLabelFor(txtTailorID);
    lblTailorID.setText(res.getString("Tailor ID") + ":");
    lblPromiseDate.setLabelFor(txtPromiseDate);
    lblPromiseDate.setText(res.getString("Promise Date") + ":");
    lblTotalPrice.setLabelFor(txtTotalPrice);
    lblTotalPrice.setText(res.getString("Total Price") + ":");
    this.setLayout(new BorderLayout());
    pnlItemDetails = new JPanel();
    pnlAlterationDetails = new JPanel();
    pnlAlterationDetails.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
    pnlItemDetails.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
    pnlAlterationDetails.setLayout(gridBagLayout1);
    pnlAlterationDetails.setPreferredSize(new Dimension(833, 65));
    pnlItemDetails.setAlignmentX((float)0.5);
    pnlItemDetails.setPreferredSize(new Dimension(300, 65));
    this.add(pnlItemDetails, BorderLayout.NORTH);
    this.add(pnlAlterationDetails, BorderLayout.CENTER);
    pnlItemDetails.setLayout(gridBagLayout2);
    pnlItemDetails.add(lblSku
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 3, 0, 0), 21, 1));
    pnlItemDetails.add(lblModel
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 3, 2, 0), 13, 1));
    pnlItemDetails.add(lblStyle
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 19, 2));
    pnlItemDetails.add(lblSkuValue
        , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 81, 2));
    pnlItemDetails.add(lblStyleValue
        , new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 77, 2));
    pnlItemDetails.add(lblModelValue
        , new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 68, 1));
    pnlItemDetails.add(lblFabric
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 67, 0, 0), 22, 2));
    pnlItemDetails.add(lblColor
        , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 67, 0, 0), 25, 2));
    pnlItemDetails.add(lblSupplier
        , new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 67, 2, 0), 9, 1));
    pnlItemDetails.add(lblFabricValue
        , new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 0, 0, 184), 67, 2));
    pnlItemDetails.add(lblColorValue
        , new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 0, 184), 70, 2));
    pnlItemDetails.add(lblSupplierValue
        , new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 2, 184), 52, 1));
    pnlAlterationDetails.add(txtAlterationID
        , new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 112, 4));
    pnlAlterationDetails.add(lblAlterationId
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 12, 0, 0), 13, 2));
    pnlAlterationDetails.add(lblFitterID
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 12, 0, 0), 39, 1));
    pnlAlterationDetails.add(txtFitterID
        , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 112, 4));
    pnlAlterationDetails.add(lblTryDate
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 6, 0, 0), 13, 2));
    pnlAlterationDetails.add(lblTailorID
        , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 6, 0, 0), 13, 2));
    pnlAlterationDetails.add(txtTryDate
        , new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 104, 4));
    pnlAlterationDetails.add(txtTailorID
        , new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 102, 4));
    pnlAlterationDetails.add(lblPromiseDate
        , new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 7, 0, 0), 16, 2));
    pnlAlterationDetails.add(txtPromiseDate
        , new GridBagConstraints(5, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 100, 4));
    pnlAlterationDetails.add(lblTotalPrice
        , new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 6, 0, 0), 35, 2));
    pnlAlterationDetails.add(txtTotalPrice
        , new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 10), 99, 4));
    //TD
    SwingUtilities.invokeLater(new Runnable(){
    	public void run(){
    	    doSetPolicy();
        }
    });
  }

  private void doSetPolicy() {  
  	getFocusCycleRootAncestor().setFocusTraversalPolicy(new AlterationHeaderFocusPolicy(getFocusCycleRootAncestor().getFocusTraversalPolicy()));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getPromiseDateTxt() {
    return txtPromiseDate;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getTryDateTxt() {
    return txtTryDate;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getTotalPriceTxt() {
    return txtTotalPrice;
  }

  private class KeyVerifier implements KeyListener {

    /**
     * put your documentation comment here
     * @param ke
     */
    public void keyPressed(KeyEvent ke) {
      if (ke.getComponent().getName().equals("TotalPrice")) {
        if (ke.isControlDown()) {
          ke.consume();
          return;
        }
      }
    }

    /**
     * put your documentation comment here
     * @param ke
     */
    public void keyTyped(KeyEvent ke) {
      if ((ke.getComponent().getName().equals("TryDate")
          || ke.getComponent().getName().equals("PromiseDate"))) {
        JCMSTextField txtField = (JCMSTextField)ke.getComponent();
        if (DATE_FILTER.indexOf(ke.getKeyChar()) == -1 && ke.getKeyCode() != ke.VK_BACK_SPACE
            && ke.getKeyCode() != ke.VK_DELETE) {
          ke.consume();
          return;
        }
        if (txtField.getText().length() + 1 > 10) {
          ke.consume();
          return;
        }
      }
      if (ke.getComponent().getName().equals("TotalPrice")) {
        String sTmp = txtTotalPrice.getText();
        if (ke.isControlDown()) {
          ke.consume();
          return;
        }
        if (MaskManager.getInstance(theAppMgr).getMaskVerify(txtTotalPrice.getText()+ke.getKeyChar(), theAppMgr.CURRENCY_MASK) == null) {
          ke.consume();
          return;
        }
        if (txtTotalPrice.getText().trim().length() > 0)
          bPriceOverrideAllowed = false;
      }
    }

    /**
     * put your documentation comment here
     * @param ke
     */
    public void keyReleased(KeyEvent ke) {}
  }


  /**
   * put your documentation comment here
   * @return
   */
  public boolean isAutoPriceUpdateAllowed() {
    return (bPriceOverrideAllowed);
  }

  /**
   * put your documentation comment here
   */
  public void clearPromiseDate() {
    txtPromiseDate.setText("");
  }

    class AlterationHeaderFocusPolicy extends FocusTraversalPolicy {
		FocusTraversalPolicy ftp = null;
		public AlterationHeaderFocusPolicy( FocusTraversalPolicy fPolicy ) {
			ftp = fPolicy;
		}
		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			if (aComponent.equals(txtTryDate)) {
                return txtPromiseDate;
            } else if (aComponent.equals(txtPromiseDate)) {
                return txtTotalPrice;
            } else {
            	return ftp.getComponentAfter(focusCycleRoot, aComponent);
            }
		}
		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			if (aComponent.equals(txtPromiseDate)) {
                return txtTryDate;
            } else if (aComponent.equals(txtTotalPrice)) {
                return txtPromiseDate;
            } else {
            	return ftp.getComponentBefore(focusCycleRoot, aComponent);
            }
		}
		public Component getDefaultComponent(Container focusCycleRoot) {
            return ftp.getDefaultComponent(focusCycleRoot);
		}
		public Component getLastComponent(Container focusCycleRoot) {
            return ftp.getLastComponent(focusCycleRoot);
		}
		public Component getFirstComponent(Container focusCycleRoot) {
            return ftp.getFirstComponent(focusCycleRoot);
		}
    }		

}

