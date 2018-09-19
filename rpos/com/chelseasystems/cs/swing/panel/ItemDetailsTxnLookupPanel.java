/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import javax.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


/**
 * put your documentation comment here
 */
public class ItemDetailsTxnLookupPanel extends JPanel {
  private JCMSLabel lblModel;
  private JCMSLabel lblStyle;
  private JCMSLabel lblSupplier;
  private JCMSLabel lblSku;
  private JCMSLabel lblFabric;
  private JCMSLabel lblColor;
  private JCMSLabel lblYear;
  private JCMSLabel lblSeason;
  private JCMSTextField txtSku;
  private JCMSTextField txtModel;
  private JCMSTextField txtStyle;
  private JCMSTextField txtSupplier;
  private JCMSTextField txtFabric;
  private JCMSTextField txtColor;
  private JCMSTextField txtYear;
  private JCMSTextField txtSeason;

  /**
   * put your documentation comment here
   */
  public ItemDetailsTxnLookupPanel() {
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
    this.setBackground(theAppMgr.getBackgroundColor());
    txtSku.setAppMgr(theAppMgr);
    txtModel.setAppMgr(theAppMgr);
    txtStyle.setAppMgr(theAppMgr);
    txtSupplier.setAppMgr(theAppMgr);
    txtFabric.setAppMgr(theAppMgr);
    txtColor.setAppMgr(theAppMgr);
    txtYear.setAppMgr(theAppMgr);
    txtSeason.setAppMgr(theAppMgr);
    lblSku.setAppMgr(theAppMgr);
    lblModel.setAppMgr(theAppMgr);
    lblStyle.setAppMgr(theAppMgr);
    lblSupplier.setAppMgr(theAppMgr);
    lblFabric.setAppMgr(theAppMgr);
    lblColor.setAppMgr(theAppMgr);
    lblYear.setAppMgr(theAppMgr);
    lblSeason.setAppMgr(theAppMgr);
  }

  /**
   * put your documentation comment here
   */
  public void reset() {
    txtSku.setText("");
    txtModel.setText("");
    txtStyle.setText("");
    txtSupplier.setText("");
    txtFabric.setText("");
    txtColor.setText("");
    txtYear.setText("");
    txtSeason.setText("");
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSku(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtSku.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSku() {
    return txtSku.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setModel(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtModel.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getModel() {
    return txtModel.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setStyle(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtStyle.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getStyle() {
    return txtStyle.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSupplier(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtSupplier.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSupplier() {
    return txtSupplier.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setFabric(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtFabric.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFabric() {
    return txtFabric.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setColor(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtColor.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getColor() {
    return txtColor.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setYear(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtYear.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getYear() {
    return txtYear.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSeason(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtSeason.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSeason() {
    return txtSeason.getText().trim();
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    GridBagLayout gridBagLayout1;
    lblSku = new JCMSLabel();
    txtSku = new JCMSTextField();
    lblModel = new JCMSLabel();
    lblStyle = new JCMSLabel();
    lblSupplier = new JCMSLabel();
    txtModel = new JCMSTextField();
    txtStyle = new JCMSTextField();
    txtSupplier = new JCMSTextField();
    lblFabric = new JCMSLabel();
    lblColor = new JCMSLabel();
    lblYear = new JCMSLabel();
    lblSeason = new JCMSLabel();
    txtFabric = new JCMSTextField();
    txtColor = new JCMSTextField();
    txtYear = new JCMSTextField();
    txtSeason = new JCMSTextField();
    txtYear.addKeyListener(new NumericListener());
    gridBagLayout1 = new GridBagLayout();
    setLayout(gridBagLayout1);
    setPreferredSize(new Dimension(400, 165));
    this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , CMSApplet.res.getString("Item Details")));
    lblSku.setText(CMSApplet.res.getString("SKU"));
    lblModel.setText(CMSApplet.res.getString("Model"));
    lblStyle.setText(CMSApplet.res.getString("Style/Ref"));
    lblSupplier.setText(CMSApplet.res.getString("Supplier"));
    lblFabric.setText(CMSApplet.res.getString("Fabric"));
    lblColor.setText(CMSApplet.res.getString("Color"));
    lblYear.setText(CMSApplet.res.getString("Year"));
    lblSeason.setText(CMSApplet.res.getString("Season"));
    this.add(lblSku
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(10, 2, 0, 0), 68, 7));
    this.add(txtSku
        , new GridBagConstraints(1, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 17), 509, 4));
    this.add(lblModel
        , new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(10, 2, 0, 0), 139, 7));
    this.add(lblStyle
        , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(10, 33, 0, 0), 127, 7));
    this.add(txtModel
        , new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 164, 4));
    this.add(txtStyle
        , new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 33, 0, 0), 166, 4));
    this.add(lblSupplier
        , new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(10, 16, 0, 17), 171, 7));
    this.add(txtSupplier
        , new GridBagConstraints(3, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 16, 0, 17), 208, 4));
    this.add(lblFabric
        , new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 140, 7));
    this.add(lblColor
        , new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 33, 0, 0), 145, 7));
    this.add(lblYear
        , new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 42, 7));
    this.add(lblSeason
        , new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 0, 17), 104, 7));
    this.add(txtFabric
        , new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 4, 0), 165, 4));
    this.add(txtColor
        , new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 33, 4, 0), 166, 4));
    this.add(txtYear
        , new GridBagConstraints(3, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 15, 4, 5), 58, 4));
    this.add(txtSeason
        , new GridBagConstraints(4, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 17), 138, 4));
  }

  private class NumericListener implements KeyListener {
    private final String KEYS_ALLOWED = "0123456789";

    /**
     * put your documentation comment here
     * @param ke
     */
    public void keyPressed(KeyEvent ke) {}

    /**
     * put your documentation comment here
     * @param ke
     */
    public void keyTyped(KeyEvent ke) {
      JCMSTextField txt = (JCMSTextField)ke.getComponent();
      if (KEYS_ALLOWED.indexOf(ke.getKeyChar()) == -1) {
        ke.consume();
        return;
      }
      if (txt == txtYear && txt.getText().trim().length() + 1 > 4) {
        ke.consume();
        return;
      }
    }

    /**
     * put your documentation comment here
     * @param ke
     */
    public void keyReleased(KeyEvent ke) {}
  }
}

