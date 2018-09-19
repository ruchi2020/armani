/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.Vector;
import java.util.StringTokenizer;
import com.chelseasystems.cs.util.ArmConfigLoader;


/**
 * <p>Title:DDTDetailsPanel </p>
 * <p>Description: DDTDetailsPanel</p>
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
 | 1    | 05-27-2005 | Manpreet  | N/A       | POS_104665_TS_FiscalDocuments_Rev0                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class DDTDetailsPanel extends JPanel {
  /**
   * Destination Code
   */
  private JCMSLabel lblDestinationCode;


  /**
   * Goods Number
   */
  private JCMSLabel lblGoodsNum;


  /**
   * Expedition Code
   */
  private JCMSLabel lblExpeditionCode;


  /**
   * Destination Code List
   */
  private JCMSComboBox cbxDestinationCode;


  /**
   * Goods Number text
   */
  private JCMSTextField txtGoodsNum;


  /**
   * Expedition Code text
   */
  private JCMSTextField txtExpeditionCode;


  /**
   * Sender
   */
  private JCMSLabel lblSender;


  /**
   * Sender Code
   */
  private JCMSLabel lblSenderCode;


  /**
   * Sender text
   */
  private JCMSTextField txtSender;


  /**
   * Sender Code list
   */
  private JCMSComboBox cbxSenderCode;


  /**
   * Carrier Type
   */
  private JCMSLabel lblCarrierType;


  /**
   * Carrier Description
   */
  private JCMSLabel lblCarrierDesc;


  /**
   * Carrier Type text
   */
  private JCMSTextField txtCarrierType;


  /**
   * Carrier Description text
   */
  private JCMSTextField txtCarrierDesc;


  /**
   * Package type
   */
  private JCMSLabel lblPackageType;


  /**
   * Weight
   */
  private JCMSLabel lblWeight;


  /**
   * PackageType text
   */
  private JCMSTextField txtPackageType;


  /**
   * Weight text
   */
  private JCMSTextField txtWeight;


  /**
   * Notes
   */
  private JCMSLabel lblNotes;


  /**
   * Notes text
   */
  private JCMSTextField txtNotes;


  /**
   * Components panel
   */
  private JPanel pnlComponents;


  /**
   * Configuration File
   */
  private final String CONFIGURATION_FILE = "ArmaniCommon.cfg";


  /**
   * DestinationCodes
   */
  private Vector vecDestinationCodes;


  /**
   * SenderCodes
   */
  private Vector vecSenderCodes;


  /**
   * Carrier Code
   */
  private JCMSLabel lblCarrierCode;


  /**
   * Carrier Code Value
   */
  private JCMSTextField txtCarrierCode;
  private ArmConfigLoader configMgr;

  //for Sender Codes
  private Vector vecSendCodeKeys;
  private Vector vecSendCodeLabels;

  //for Destination Codes
  private Vector vecDestCodeKeys;
  private Vector vecDestCodeLabels;
  private static String types[];


  /**
   * Default Constructor
   */
  public DDTDetailsPanel() {
    try {
      jbInit();
      populateDestinationCodes();
      populateSenderCodes();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Set Application Manager
   * @param theAppMgr IApplicationManager
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlComponents.setBackground(theAppMgr.getBackgroundColor());
    txtExpeditionCode.setAppMgr(theAppMgr);
    txtGoodsNum.setAppMgr(theAppMgr);
    txtCarrierType.setAppMgr(theAppMgr);
    txtCarrierDesc.setAppMgr(theAppMgr);
    txtSender.setAppMgr(theAppMgr);
    txtPackageType.setAppMgr(theAppMgr);
    txtWeight.setAppMgr(theAppMgr);
    txtNotes.setAppMgr(theAppMgr);
    txtCarrierCode.setAppMgr(theAppMgr);
    lblDestinationCode.setAppMgr(theAppMgr);
    lblGoodsNum.setAppMgr(theAppMgr);
    lblExpeditionCode.setAppMgr(theAppMgr);
    lblSender.setAppMgr(theAppMgr);
    lblSenderCode.setAppMgr(theAppMgr);
    lblCarrierType.setAppMgr(theAppMgr);
    lblCarrierDesc.setAppMgr(theAppMgr);
    lblPackageType.setAppMgr(theAppMgr);
    lblWeight.setAppMgr(theAppMgr);
    lblNotes.setAppMgr(theAppMgr);
    lblCarrierCode.setAppMgr(theAppMgr);
    cbxDestinationCode.setAppMgr(theAppMgr);
    cbxSenderCode.setAppMgr(theAppMgr);
  }


  /**
   * ExpeditionCode
   * @param sValue String
   */
  public void setExpeditionCode(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtExpeditionCode.setText(sValue);
  }


  /**
   * ExpeditionCode
   * @return String
   */
  public String getExpeditionCode() {
    return txtExpeditionCode.getText().trim();
  }


  /**
   * GoodsNumber
   * @param sValue String
   */
  public void setGoodsNumber(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtGoodsNum.setText(sValue);
  }


  /**
   * GoodsNumber
   * @return String
   */
  public String getGoodsNumber() {
    return txtGoodsNum.getText().trim();
  }


  /**
   * CarrierType
   * @param sValue String
   */
  public void setCarrierType(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtCarrierType.setText(sValue);
  }


  /**
   * CarrierType
   * @return String
   */
  public String getCarrierType() {
    return txtCarrierType.getText().trim();
  }


  /**
   * CarrierDesc
   * @param sValue String
   */
  public void setCarrierDesc(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtCarrierDesc.setText(sValue);
  }


  /**
   * CarrierDesc
   * @return String
   */
  public String getCarrierDesc() {
    return txtCarrierDesc.getText().trim();
  }


  /**
   * CarrierCode
   * @param sValue String
   */
  public void setCarrierCode(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtCarrierCode.setText(sValue);
  }


  /**
   * CarrierCode
   * @return String
   */
  public String getCarrierCode() {
    return txtCarrierCode.getText().trim();
  }


  /**
   * Sender
   * @param sValue String
   */
  public void setSender(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtSender.setText(sValue);
  }


  /**
   * Sender
   * @return String
   */
  public String getSender() {
    return txtSender.getText().trim();
  }


  /**
   * PackageType
   * @param sValue String
   */
  public void setPackageType(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtPackageType.setText(sValue);
  }


  /**
   * PackageType
   * @return String
   */
  public String getPackageType() {
    return txtPackageType.getText().trim();
  }


  /**
   * Weight
   * @param sValue String
   */
  public void setWeight(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtWeight.setText(sValue);
  }


  /**
   * Weight
   * @return String
   */
  public String getWeight() {
    return txtWeight.getText().trim();
  }


  /**
   * Notes
   * @param sValue String
   */
  public void setNotes(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtNotes.setText(sValue);
  }


  /**
   * Notes
   * @return String
   */
  public String getNotes() {
    return txtNotes.getText().trim();
  }


  /**
   * DestinationCode
   * @param sValue String
   */
  public void setDestinationCode(String sValue) {
    if (sValue == null || sValue.length() < 1 || vecDestCodeKeys.indexOf(sValue) == -1)
      return;
    cbxDestinationCode.setSelectedIndex(vecDestCodeKeys.indexOf(sValue));
  }


  /**
   * DestinationCode
   * @return String
   */
  public String getDestinationCode() {
    if (vecDestCodeKeys.size() == 0)
      return " ";
    return (String)vecDestCodeKeys.elementAt(cbxDestinationCode.getSelectedIndex());
  }


  /**
   * SenderCode
   * @param sValue String
   */
  public void setSenderCode(String sValue) {
    if (sValue == null || sValue.length() < 1 || vecSendCodeKeys.indexOf(sValue) == -1)
      return;
    cbxSenderCode.setSelectedIndex(vecSendCodeKeys.indexOf(sValue));
  }


  /**
   * SenderCode
   * @return String
   */
  public String getSenderCode() {
    if (vecSendCodeKeys.size() == 0)
      return " ";
    return (String)vecSendCodeKeys.elementAt(cbxSenderCode.getSelectedIndex());
  }


  /**
   * Enable/Disable GUI
   * @param bEnabled boolean
   */
  public void setEnabled(boolean bEnabled) {
    txtExpeditionCode.setEnabled(bEnabled);
    txtGoodsNum.setEnabled(bEnabled);
    txtCarrierType.setEnabled(bEnabled);
    txtCarrierDesc.setEnabled(bEnabled);
    txtSender.setEnabled(bEnabled);
    txtPackageType.setEnabled(bEnabled);
    txtWeight.setEnabled(bEnabled);
    txtNotes.setEnabled(bEnabled);
    cbxDestinationCode.setEnabled(bEnabled);
    cbxSenderCode.setEnabled(bEnabled);
  }


  /**
   * Reset the GUI
   */
  public void reset() {
    txtExpeditionCode.setText("");
    txtGoodsNum.setText("");
    txtCarrierType.setText("");
    txtCarrierDesc.setText("");
    txtSender.setText("");
    txtPackageType.setText("");
    txtWeight.setText("");
    txtNotes.setText("");
    txtCarrierCode.setText("");
    if (cbxSenderCode.getItemCount() > 0)
      cbxSenderCode.setSelectedIndex(0);
    if (cbxDestinationCode.getItemCount() > 0)
      cbxDestinationCode.setSelectedIndex(0);
  }


  /**
   * put your documentation comment here
   */
  public void requestFocusToDestinationCode() {
    cbxDestinationCode.requestFocus();
  }


  /**
   * Populate Destination Codes from Configuration File
   */
  public void populateDestinationCodes() {
    try {
      vecDestCodeKeys = new Vector();
      vecDestCodeLabels = new Vector();
//      configMgr = new ConfigMgr(CONFIGURATION_FILE);
      configMgr = new ArmConfigLoader();
      String strSubCodes = configMgr.getString("DDT_DESTINATION_CODE");
      StringTokenizer stk = null;
      int i = -1;
      if (strSubCodes != null && strSubCodes.trim().length() > 0) {
        stk = new StringTokenizer(strSubCodes, ",");
      } else
        return;
      if (stk != null) {
        types = new String[stk.countTokens()];
        while (stk.hasMoreTokens()) {
          types[++i] = stk.nextToken();
          String key = configMgr.getString(types[i] + ".CODE");
          vecDestCodeKeys.add(key);
          String value = configMgr.getString(types[i] + ".LABEL");
          vecDestCodeLabels.add(value);
        }
      }
      cbxDestinationCode.setModel(new DefaultComboBoxModel(vecDestCodeLabels));
    } catch (Exception e) {}
  }


  /**
   * Populate Sender Codes from Configuration File
   */
  public void populateSenderCodes() {
    try {
      vecSendCodeKeys = new Vector();
      vecSendCodeLabels = new Vector();
//      ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
      configMgr = new ArmConfigLoader();
      String strSubCodes = configMgr.getString("DDT_SENDER_CODE");
      StringTokenizer stk = null;
      int i = -1;
      if (strSubCodes != null && strSubCodes.trim().length() > 0) {
        stk = new StringTokenizer(strSubCodes, ",");
      } else
        return;
      if (stk != null) {
        types = new String[stk.countTokens()];
        while (stk.hasMoreTokens()) {
          types[++i] = stk.nextToken();
          String key = configMgr.getString(types[i] + ".CODE");
          vecSendCodeKeys.add(key);
          String value = configMgr.getString(types[i] + ".LABEL");
          vecSendCodeLabels.add(value);
        }
      }
      cbxSenderCode.setModel(new DefaultComboBoxModel(vecSendCodeLabels));
    } catch (Exception e) {}
  }


  /**
   * Initialize Components
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    lblDestinationCode = new JCMSLabel();
    lblGoodsNum = new JCMSLabel();
    lblExpeditionCode = new JCMSLabel();
    cbxDestinationCode = new JCMSComboBox();
    cbxDestinationCode.setName("DESTINATION_CODE");
    txtGoodsNum = new JCMSTextField();
    txtExpeditionCode = new JCMSTextField();
    lblSender = new JCMSLabel();
    lblSenderCode = new JCMSLabel();
    txtSender = new JCMSTextField();
    cbxSenderCode = new JCMSComboBox();
    lblCarrierType = new JCMSLabel();
    lblCarrierDesc = new JCMSLabel();
    txtCarrierType = new JCMSTextField();
    txtCarrierDesc = new JCMSTextField();
    lblPackageType = new JCMSLabel();
    lblWeight = new JCMSLabel();
    txtPackageType = new JCMSTextField();
    txtWeight = new JCMSTextField();
    txtWeight.addKeyListener(new KeyAdapter() {

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
        StringBuffer sTmp = new StringBuffer(txtWeight.getText());
        if ("0123456789.".indexOf(ke.getKeyChar()) == -1) {
          ke.consume();
          return;
        }
        if (ke.getKeyChar() == '.' && sTmp.toString().indexOf(".") > 0) {
          ke.consume();
          return;
        }
      }
    });
    lblNotes = new JCMSLabel();
    txtNotes = new JCMSTextField();
    lblCarrierCode = new JCMSLabel();
    txtCarrierCode = new JCMSTextField();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    pnlComponents = new JPanel();
    lblDestinationCode.setText(CMSApplet.res.getString("Destination Code"));
    lblGoodsNum.setText(CMSApplet.res.getString("Goods No."));
    lblExpeditionCode.setText(CMSApplet.res.getString("Expedition Code"));
    lblSender.setText(CMSApplet.res.getString("Sender"));
    lblSenderCode.setText(CMSApplet.res.getString("Sender Code"));
    lblCarrierType.setText(CMSApplet.res.getString("Carrier Type"));
    lblCarrierDesc.setText(CMSApplet.res.getString("Carrier Desc."));
    lblPackageType.setText(CMSApplet.res.getString("Package Type"));
    lblWeight.setText(CMSApplet.res.getString("Weight"));
    lblNotes.setText(CMSApplet.res.getString("Notes"));
    lblCarrierCode.setText(CMSApplet.res.getString("Carrier Code"));
    this.setLayout(new BorderLayout());
    this.add(pnlComponents, BorderLayout.NORTH);
    pnlComponents.setLayout(gridBagLayout1);
    pnlComponents.setPreferredSize(new Dimension(833, 285));
    pnlComponents.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , CMSApplet.res.getString("DDT Details")));
    pnlComponents.add(lblDestinationCode
        , new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(19, 6, 0, 0), 106, 7));
    pnlComponents.add(cbxDestinationCode
        , new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(0, 6, 0, 0), 76, -2));
    pnlComponents.add(lblSender
        , new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 163, 7));
    pnlComponents.add(lblGoodsNum
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(19, 12, 0, 8), 65, 7));
    pnlComponents.add(txtGoodsNum
        , new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 12, 0, 8), 113, 4));
    pnlComponents.add(lblExpeditionCode
        , new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(19, 13, 0, 6), 52, 7));
    pnlComponents.add(txtExpeditionCode
        , new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 13, 0, 6), 131, 4));
    pnlComponents.add(lblSenderCode
        , new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 11, 0, 6), 215, 7));
    pnlComponents.add(txtSender
        , new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 6, 0, 0), 192, 4));
    pnlComponents.add(cbxSenderCode
        , new GridBagConstraints(2, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(0, 11, 0, 6), 161, -2));
    pnlComponents.add(lblCarrierType
        , new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 8, 0, 0), 28, 7));
    pnlComponents.add(lblCarrierDesc
        , new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 12, 0, 6), 214, 7));
    pnlComponents.add(txtCarrierType
        , new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 8, 0, 0), 84, 4));
    pnlComponents.add(txtCarrierDesc
        , new GridBagConstraints(2, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 12, 0, 6), 276, 4));
    pnlComponents.add(lblPackageType
        , new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 125, 7));
    pnlComponents.add(lblWeight
        , new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 12, 0, 0), 94, 7));
    pnlComponents.add(txtPackageType
        , new GridBagConstraints(0, 7, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 6, 0, 0), 192, 4));
    pnlComponents.add(txtWeight
        , new GridBagConstraints(2, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 12, 0, 0), 121, 4));
    pnlComponents.add(lblNotes
        , new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 170, 7));
    pnlComponents.add(txtNotes
        , new GridBagConstraints(0, 9, 4, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 6, 14, 6), 490, 4));
    pnlComponents.add(lblCarrierCode
        , new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 25, 7));
    pnlComponents.add(txtCarrierCode
        , new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 6, 0, 0), 90, 4));
  }
}
