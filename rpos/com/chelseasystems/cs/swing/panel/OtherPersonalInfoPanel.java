/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import java.awt.*;

import javax.swing.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.*;

import java.util.ResourceBundle;
import java.util.Vector;
import java.util.StringTokenizer;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.CMSApplet;
import java.awt.event.*;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cs.util.ArmConfigLoader;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.swing.CMSTextFilter;
import com.chelseasystems.cs.swing.bean.JCMSTextField_JP;


/**
 * <p>Title: OtherPersonalInfoPanel.java</p>
 *
 * <p>Description: Displays Other Personal Information </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 09/06/2005 | Manpreet  | N/A       |Try catch block in populate methods                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 09/01/2005 | Manpreet  | 894/895   | Change Request to pick-up Profession, Education and|
 |      |            |           |           | Special Event type from ArmaniCommong.cfg file     |
 --------------------------------------------------------------------------------------------------
 | 2    | 04/12/2005 | Anand     | N/A       | Change Request to pick-up Profession, Education and|
 |      |            |           |           | Special Event type from configuration file         |
 --------------------------------------------------------------------------------------------------
 | 1    | 03-07-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class OtherPersonalInfoPanel extends JPanel {
  /**
   * ReferredBy
   */
  private JCMSLabel lblReferredBy;
  /**
   * ReferredBy Company/Employee No.
   */
  private JCMSTextField txtReferredBy;
  /**
   * Profession
   */
  private JCMSLabel lblProfession;
  /**
   * Profession selection
   */
  private JCMSComboBox cbxProfession;
  /**
   * Education
   */
  private JCMSLabel lblEducation;
  /**
   * Education list
   */
  private JCMSComboBox cbxEducation;
  /**
   * Notes 1
   */
  private JCMSLabel lblNotes1;
  /**
   * Notes 1 text
   */
  private JCMSTextArea txtNotes1;
  /**
   * Notes 2
   */
  private JCMSLabel lblNotes2;
  /**
   * Notes 2 text
   */
  private JCMSTextArea txtNotes2;
  /**
   * Partner family name
   */
  private JCMSLabel lblPartenerFamName;
  /**
   * Partner Name
   */
  private JCMSLabel lblPartnerName;
  /**
   * Partner Family Name value
   */
  private JCMSTextField txtPartFamName;
  /**
   * Partner Name value
   */
  private JCMSTextField txtPartName;
  /**
   * Birth Place
   */
  private JCMSLabel lblBirthPlc;
  /**
   * Special Event
   */
  private JCMSLabel lblSpecialEvt;
  /**
   * Special Date
   */
  private JCMSLabel lblSpecialDate;
  /**
   * BirthPlace value
   */
  private JCMSTextField txtBirthPlc;
  /**
   * Special Event list.
   */
  private JCMSComboBox cbxSpcEvt;
  /**
   * Special Date value
   */
  private JCMSTextField txtSpclDate;
  /**
   * Children names
   */
  private JCMSLabel lblChildNames;
  /**
   * Children names value
   */
  private JCMSTextField txtChildNames;
  /**
   * Number of children
   */
  private JCMSLabel lblNumChildren;
  /**
   * Number of children value
   */
  private JCMSTextField txtNumChildren;
  /**
   * ApplicationManager
   */
  private IApplicationManager theAppMgr;
  private final ResourceBundle RESOURCE;
  // added by Anand on 04/12/2005. Vectors for storing Special Events (first for key and second for value)
  private Vector vecSpecialEventKeys;
  private Vector vecSpecialEventLabels;
  // added by Anand on 04/12/2005. Vectors for storing Education Types (first for key and second for value)
  private Vector vecEducationKeys;
  private Vector vecEducationLabels;
  // added by Anand on 04/12/2005. Vectors for storing Profession Types (first for key and second for value)
  private Vector vecProfessionKeys;
  private Vector vecProfessionLabels;
//  private ConfigMgr config;
  private ArmConfigLoader config;

  /**
   * ALPHA_NUMERIC WITH SPECIAL CHARACTER.
   */
  public static final String ALPHA_NUMERIC_SPEC =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";
  public static final String NAME_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-' ";
  public static final String MULTI_NAME_SPEC =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-', ";
  private static String types[];
  ConfigMgr configMgr;
  public JButton btnLookup = new JButton();

  /**
   * Default Constructor
   */
  public OtherPersonalInfoPanel() {
    configMgr = new ConfigMgr("customer.cfg");
    RESOURCE = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    try {
      jbInit();
      setEnabled(false);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   * To populate the different special event types as elements of the relevant combo-box
   * @param <none>
   */
  private void populateSpecialEvent() {
    try {
      vecSpecialEventKeys = new Vector();
      vecSpecialEventLabels = new Vector();
      StringTokenizer stk = null;
      //MSB -09/01/05  -- Changed configuration file and Keys
      //             config = new ConfigMgr("customer.cfg");
      //             String strSubTypes = config.getString("SPECIAL_EVENT_TYPES");
//      config = new ConfigMgr("ArmaniCommon.cfg");
      config = new ArmConfigLoader();
      String strSubTypes = config.getString("SPECIAL_EVT_TYPE");
      int i = -1;
      if (strSubTypes != null && strSubTypes.trim().length() > 0) {
        stk = new StringTokenizer(strSubTypes, ",");
      } else
        return;
      types = new String[stk.countTokens()];
      while (stk.hasMoreTokens()) {
        types[++i] = stk.nextToken();
        String key = config.getString(types[i] + ".CODE");
        vecSpecialEventKeys.add(key);
        String value = config.getString(types[i] + ".LABEL");
        vecSpecialEventLabels.add(value);
      }
      cbxSpcEvt.setModel(new DefaultComboBoxModel(vecSpecialEventLabels));
    } catch (Exception e) {}
  }

  /**
   * set the special event Type
   * @param sValue eventType (String)
   */
  public void setSelectedSpecialEvent(String sValue) {
    if (sValue == null || sValue.trim().length() < 1 || vecSpecialEventKeys.indexOf(sValue) == -1)
      return;
    cbxSpcEvt.setSelectedIndex(vecSpecialEventKeys.indexOf(sValue));
  }

  /**
   * Get the selected event type (key)
   * @return key as String
   */
  public String getSelectedSpecialEvent() {
    if (cbxSpcEvt.getSelectedIndex() > 0 && cbxSpcEvt.getSelectedIndex() < vecSpecialEventKeys.size())
      return ((String)vecSpecialEventKeys.elementAt(cbxSpcEvt.getSelectedIndex())).trim();
    return "";
  }

  /**
   * To populate the profession types as elements of the relevant combo-box
   * @param <none>
   */
  private void populateProfession() {
    try {
      vecProfessionKeys = new Vector();
      StringTokenizer stk = null;
      vecProfessionLabels = new Vector();
      //MSB -09/01/05  -- Changed configuration file.
      //             config = new ConfigMgr("customer.cfg");
//      config = new ConfigMgr("ArmaniCommon.cfg");
      config = new ArmConfigLoader();
      String strSubTypes = config.getString("PROFESSION");
      int i = -1;
      if (strSubTypes != null && strSubTypes.trim().length() > 0) {
        stk = new StringTokenizer(strSubTypes, ",");
      } else
        return;
      if (stk != null) {
        types = new String[stk.countTokens()];
        while (stk.hasMoreTokens()) {
          types[++i] = stk.nextToken();
          String key = config.getString(types[i] + ".CODE");
          vecProfessionKeys.add(key);
          String value = config.getString(types[i] + ".LABEL");
          vecProfessionLabels.add(value);
        }
      }
      cbxProfession.setModel(new DefaultComboBoxModel(vecProfessionLabels));
    } catch (Exception e) {}
  }

  /**
   * set the profession Type
   * @param sValue (professionType)
   */
  public void setSelectedProfession(String sValue) {
    if (sValue == null || sValue.trim().length() < 1 || vecProfessionKeys.indexOf(sValue) == -1)
      return;
    cbxProfession.setSelectedIndex(vecProfessionKeys.indexOf(sValue));
  }

  /**
   * Get the selected profession type (key)
   * @return profession key as String
   */
  public String getSelectedProfession() {
    if (cbxProfession.getSelectedIndex() > 0 && cbxProfession.getSelectedIndex() < vecProfessionKeys.size())
      return ((String)vecProfessionKeys.elementAt(cbxProfession.getSelectedIndex())).trim();
    return "";
  }

  /**
   * To populate the education types as elements of the relevant combo-box
   * @param <none>
   */
  private void populateEducation() {
    try {
      vecEducationKeys = new Vector();
      vecEducationLabels = new Vector();
      //MSB -09/01/05  -- Changed configuration file and Keys
      //             config = new ConfigMgr("customer.cfg");
      //             String strSubTypes = config.getString("EDUCATION_TYPES");
//      config = new ConfigMgr("ArmaniCommon.cfg");
      config = new ArmConfigLoader();
      String strSubTypes = config.getString("EDUCATION");
      StringTokenizer stk = null;
      int i = -1;
      if (strSubTypes != null && strSubTypes.trim().length() > 0) {
        stk = new StringTokenizer(strSubTypes, ",");
      } else
        return;
      if (stk != null) {
        types = new String[stk.countTokens()];
        while (stk.hasMoreTokens()) {
          types[++i] = stk.nextToken();
          String key = config.getString(types[i] + ".CODE");
          vecEducationKeys.add(key);
          String value = config.getString(types[i] + ".LABEL");
          vecEducationLabels.add(value);
        }
      }
      cbxEducation.setModel(new DefaultComboBoxModel(vecEducationLabels));
    } catch (Exception e) {}
  }

  /**
   * set the education Type
   * @param sValue (educationType)
   */
  public void setSelectedEducation(String sValue) {
    if (sValue == null || sValue.trim().length() < 1 || vecEducationKeys.indexOf(sValue) == -1)
      return;
    cbxEducation.setSelectedIndex(vecEducationKeys.indexOf(sValue));
  }

  /**
   * Get the selected education type (key)
   * @return key for education as String
   */
  public String getSelectedEducation() {
    if (cbxEducation.getSelectedIndex() > 0 && cbxEducation.getSelectedIndex() < vecEducationKeys.size())
      return ((String)vecEducationKeys.elementAt(cbxEducation.getSelectedIndex())).trim();
    return "";
  }

  /**
   * Populate SpecialEvent combo-box
   */
  /**
   * Set Application Manager
   * @param theAppMgr ApplicationManager
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.theAppMgr = theAppMgr;
    this.setBackground(theAppMgr.getBackgroundColor());
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    //Loop through and set Application managers for all
    // JCMS components.
    for (int iCtr = 0; iCtr < this.getComponentCount(); iCtr++) {
      Component component = this.getComponent(iCtr);
      if (component instanceof JCMSTextField) {
        ((JCMSTextField)component).setAppMgr(theAppMgr);
        ((JCMSTextField)component).setFont(theAppMgr.getTheme().getTextFieldFont());
        ((JCMSTextField)component).setPreferredSize(new Dimension((int)(65 * r), (int)(25 * r)));
        ((JCMSTextField)component).setMaximumSize(new Dimension((int)(65 * r), (int)(25 * r)));
        ((JCMSTextField)component).setMinimumSize(new Dimension((int)(65 * r), (int)(25 * r)));
      } else if (component instanceof JCMSTextArea) {
        ((JCMSTextArea)component).setAppMgr(theAppMgr);
        ((JCMSTextArea)component).setFont(theAppMgr.getTheme().getTextFieldFont());
        ((JCMSTextArea)component).setPreferredSize(new Dimension((int)(65 * r), (int)(50 * r)));
      } else if (component instanceof JCMSLabel) {
        ((JCMSLabel)component).setAppMgr(theAppMgr);
        ((JCMSLabel)component).setFont(theAppMgr.getTheme().getLabelFont());
        ((JCMSLabel)component).setPreferredSize(new Dimension((int)(115 * r), (int)(25 * r)));
      } else if (component instanceof JCMSComboBox) {
        ((JCMSComboBox)component).setAppMgr(theAppMgr);
        ((JCMSComboBox)component).setPreferredSize(new Dimension((int)(65 * r), (int)(25 * r)));
        ((JCMSComboBox)component).setMaximumSize(new Dimension((int)(65 * r), (int)(25 * r)));
        ((JCMSComboBox)component).setMinimumSize(new Dimension((int)(65 * r), (int)(25 * r)));
      } else if (component instanceof JCMSMaskedTextField) {
        ((JCMSMaskedTextField)component).setAppMgr(theAppMgr);
        ((JCMSMaskedTextField)component).setFont(theAppMgr.getTheme().getTextFieldFont());
        ((JCMSMaskedTextField)component).setPreferredSize(new Dimension((int)(65 * r), (int)(25 * r)));
        ((JCMSMaskedTextField)component).setMaximumSize(new Dimension((int)(65 * r), (int)(25 * r)));
        ((JCMSMaskedTextField)component).setMinimumSize(new Dimension((int)(65 * r), (int)(25 * r)));
      }
    }
    lblReferredBy.setPreferredSize(new Dimension((int)(135 * r), (int)(25 * r)));
    lblPartenerFamName.setPreferredSize(new Dimension((int)(135 * r), (int)(25 * r)));
  }

  /**
   * Set ReferredBy
   * @param sValue EmployeeID/CompanyID
   */
  public void setReferredBy(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtReferredBy.setText(sValue);
    //      if(theAppMgr.getStateObject("REFERRED") != null){
    //         txtReferredBy.setEditable(false);
    //      }
  }

  /**
   * Get ReferredBy
   * @return EmployeeID/CompanyID
   */
  public String getReferredBy() {
    return txtReferredBy.getText().trim();
  }

  /**
   * Get SelectedEducation
   * @return Education
   */
  /**
   * Notes1
   * @param sValue Notes1
   */
  public void setNotes1(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtNotes1.setText(sValue);
  }

  /**
   * Get Notes1
   * @return Notes1
   */
  public String getNotes1() {
    return txtNotes1.getText().trim();
  }

  /**
   * Set Notes2
   * @param sValue Notes2
   */
  public void setNotes2(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtNotes2.setText(sValue);
  }

  /**
   * Get Notes2
   * @return Notes2
   */
  public String getNotes2() {
    return txtNotes2.getText().trim();
  }

  /**
   * PartnerFamilyName
   * @param sValue PartnerFamilyName
   */
  public void setPartnerFamilyName(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtPartFamName.setText(sValue);
  }

  /**
   * PartnerFamilyName
   * @return PartnerFamilyName
   */
  public String getPartnerFamilyName() {
    return txtPartFamName.getText().trim();
  }

  /**
   * PartnerName
   * @param sValue PartnerName
   */
  public void setPartnerName(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtPartName.setText(sValue);
  }

  /**
   * PartnerName
   * @return PartnerName
   */
  public String getPartnerName() {
    return txtPartName.getText().trim();
  }

  /**
   * Set PlaceOfBirth
   * @param sValue PlaceOfBirth
   */
  public void setBirthPlace(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtBirthPlc.setText(sValue);
  }

  /**
   * BirthPlace
   * @return BirthPlace
   */
  public String getBirthPlace() {
    return txtBirthPlc.getText().trim();
  }

  /**
   * DateOfIssue
   * @return DateOfIssue
   */
  /**
   * Set SpecialDate
   * @param sValue SpecialDate
   */
  public String getSpecialDate() {
    return txtSpclDate.getText().trim();
  }

  /**
   * Set Method
   * @param sValue String
   */
  public void setSpecialDate(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtSpclDate.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTextField getSpecialDateTxt() {
    return this.txtSpclDate;
  }

  /**
   * Get SpecialDate
   * @return SpecialDate
   */
  /**
   * Set Children names
   * @param sValue ChildrenNames
   */
  public void setChildNames(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtChildNames.setText(sValue);
  }

  /**
   * Get ChildNames
   * @return ChildNames
   */
  public String getChildNames() {
    return txtChildNames.getText().trim();
  }

  /**
   * NumberChildren
   * @param sValue NumberOfChildren
   */
  public void setNumChildren(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtNumChildren.setText(sValue);
  }

  /**
   * Get NumberOfChildren
   * @return NumberOfChildren
   */
  public String getNumChildren() {
    return txtNumChildren.getText().trim();
  }
  
	private JCMSTextField getJCMSTextField(){
		if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
			return new JCMSTextField_JP();
		}
		
		return  new JCMSTextField();
	}

  

  /**
   * put your documentation comment here
   */
  public void reset() {
    txtReferredBy.setText("");
    txtReferredBy.setBackground(theAppMgr.getBackgroundColor());
    //txtReferredBy.setEditable(false);
    //txtReferredBy.setBackground(Color.lightGray);
    if (cbxProfession.getItemCount() > 0)
      cbxProfession.setSelectedIndex(0);
    if (cbxEducation.getItemCount() > 0)
      cbxEducation.setSelectedIndex(0);
    txtNotes1.setText("");
    txtNotes2.setText("");
    txtPartFamName.setText("");
    txtPartName.setText("");
    txtBirthPlc.setText("");
    if (cbxSpcEvt.getItemCount() > 0)
      cbxSpcEvt.setSelectedIndex(0);
    txtSpclDate.setText("");
    txtChildNames.setText("");
    txtNumChildren.setText("");
  }

  /**
   * put your documentation comment here
   * @param bEnabled
   */
  public void setEnabled(boolean bEnabled) {
    txtReferredBy.setEnabled(false);
    cbxProfession.setEnabled(bEnabled);
    cbxEducation.setEnabled(bEnabled);
    txtNotes1.setEnabled(bEnabled);
    txtNotes2.setEnabled(bEnabled);
    txtPartFamName.setEnabled(bEnabled);
    txtPartName.setEnabled(bEnabled);
    txtBirthPlc.setEnabled(bEnabled);
    cbxSpcEvt.setEnabled(bEnabled);
    txtSpclDate.setEnabled(bEnabled);
    txtChildNames.setEnabled(bEnabled);
    txtNumChildren.setEnabled(bEnabled);
    btnLookup.setEnabled(bEnabled);
  }

  /**
   * Initialize and layout components.
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    lblReferredBy = new JCMSLabel();
    txtReferredBy = getJCMSTextField();
    lblProfession = new JCMSLabel();
    cbxProfession = new JCMSComboBox();
    lblEducation = new JCMSLabel();
    cbxEducation = new JCMSComboBox();
    lblNotes1 = new JCMSLabel();
    txtNotes1 = new JCMSTextArea();
    lblNotes2 = new JCMSLabel();
    txtNotes2 = new JCMSTextArea();
    lblPartenerFamName = new JCMSLabel();
    lblPartnerName = new JCMSLabel();
    txtPartFamName = getJCMSTextField();
    txtPartName = getJCMSTextField();
    lblBirthPlc = new JCMSLabel();
    lblSpecialEvt = new JCMSLabel();
    lblSpecialDate = new JCMSLabel();
    txtBirthPlc = getJCMSTextField();
    cbxSpcEvt = new JCMSComboBox();
    //txtSpclDate = new JCMSMaskedTextField();
    txtSpclDate = getJCMSTextField();
    //txtSpclDate.setMask(JCMSMaskedTextField.DATE_MASK);
    txtSpclDate.setName("DATE_SPECIAL");
    lblChildNames = new JCMSLabel();
    txtChildNames = getJCMSTextField();
    txtChildNames.setName("ChildNames");
    lblNumChildren = new JCMSLabel();
    txtNumChildren = getJCMSTextField();
    populateSpecialEvent();
    populateProfession();
    populateEducation();
    this.setLayout(new GridBagLayout());
    this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
        , RESOURCE.getString("Other Personal Information ")));
    lblReferredBy.setHorizontalAlignment(SwingConstants.RIGHT);
    lblReferredBy.setHorizontalTextPosition(SwingConstants.RIGHT);
    lblReferredBy.setIconTextGap(4);
    lblReferredBy.setLabelFor(txtReferredBy);
    lblReferredBy.setText(RESOURCE.getString("Referred by employee/Company"));
    lblProfession.setText(RESOURCE.getString("Profession"));
    lblNotes1.setText(RESOURCE.getString("Notes 1"));
    lblNotes2.setText(RESOURCE.getString("Notes 2"));
    lblPartenerFamName.setText(RESOURCE.getString("Partner Farmily Name"));
    lblPartnerName.setText(RESOURCE.getString("Partner Name"));
    lblBirthPlc.setText(RESOURCE.getString("Birth Place"));
    lblSpecialEvt.setText(RESOURCE.getString("Special Event"));
    lblSpecialDate.setText(RESOURCE.getString("Special Date"));
    lblChildNames.setText(RESOURCE.getString("Children Names"));
    lblNumChildren.setText(RESOURCE.getString("Number of Children"));
    //      cbxEducation.setPreferredSize(new Dimension(127, 18));
    //
    txtNotes1.setPreferredSize(new Dimension(452, 16));
    txtNotes1.setAutoscrolls(true);
    txtNotes2.setPreferredSize(new Dimension(452, 16));
    //txtReferredBy.setEditable(false);
    txtNotes2.setAutoscrolls(true);
    lblEducation.setHorizontalAlignment(SwingConstants.TRAILING);
    lblEducation.setLabelFor(cbxEducation);
    lblEducation.setText(RESOURCE.getString("Education"));
    //btnLookup.setHorizontalAlignment(SwingConstants.LEFT);
    btnLookup.setText("  "+CMSApplet.res.getString("Lookup")+"  ");
	btnLookup.setOpaque(false);
    btnLookup.setPreferredSize(new Dimension(70,25));    
	btnLookup.setBorder(BorderFactory.createRaisedBevelBorder());
	btnLookup.setContentAreaFilled(false);
	//btnLookup.setHorizontalTextPosition(SwingConstants.CENTER);
    
    if (!CMSApplet.theAppMgr.isOnLine())
      btnLookup.setEnabled(false);
    btnLookup.addActionListener(new ActionListener() {

      /**
       * put your documentation comment here
       * @param ae
       */
      public void actionPerformed(ActionEvent ae) {
        theAppMgr.fireButtonEvent("CUST_LOOKUP");
      }
    });
    txtReferredBy.setHorizontalAlignment(SwingConstants.LEFT);
    this.add(cbxProfession
        , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST //CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 0), -2, 1));
    this.add(lblPartnerName
        , new GridBagConstraints(3, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(6, 0, 0, 10), 17, 0));
    this.add(lblSpecialDate
        , new GridBagConstraints(6, 8, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 3), 23, 0));
    this.add(txtNumChildren
        , new GridBagConstraints(7, 10, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 3), 30, 1));
    this.add(txtSpclDate
        , new GridBagConstraints(6, 9, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 3), 10, 1));
    this.add(lblReferredBy
        , new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.EAST //TD
        , GridBagConstraints.NONE, new Insets(1, 0, 0, 0), 35, 0));
    this.add(lblProfession
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(8, 0, 0, 0), 4, 0));
    this.add(lblNotes1
        , new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 0, 105), 34, 0));
    this.add(lblNotes2
        , new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 0, 107), 32, 0));
    this.add(lblPartenerFamName
        , new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(6, 0, 0, 55), 6, 0));
    this.add(lblBirthPlc
        , new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(6, 0, 0, 34), 89, 0));
    this.add(txtNotes2
        , new GridBagConstraints(0, 5, 8, 1, 1.0, 1.0, GridBagConstraints.CENTER
        , GridBagConstraints.BOTH, new Insets(0, 0, 0, 3), 0, 0));
    this.add(txtNotes1
        , new GridBagConstraints(0, 3, 8, 1, 1.0, 1.0, GridBagConstraints.CENTER
        , GridBagConstraints.BOTH, new Insets(0, 0, 0, 3), 0, 0));
    this.add(lblEducation
        , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(8, 15, 0, 0), 0, 0));
    this.add(txtPartFamName
        , new GridBagConstraints(0, 7, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 1));
    this.add(txtPartName
        , new GridBagConstraints(3, 7, 3, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 1));
    this.add(txtBirthPlc
        , new GridBagConstraints(0, 9, 4, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 65), 1, 1));
    this.add(cbxSpcEvt
        , new GridBagConstraints(4, 9, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(5, -45, 0, 0), 0, 1));
    this.add(lblSpecialEvt
        , new GridBagConstraints(4, 8, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(6, -45, 0, 0), 20, 0));
    this.add(lblNumChildren
        , new GridBagConstraints(5, 10, 2, 1, 0.0, 0.0, GridBagConstraints.EAST
        , GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 0), 0, 0));
    this.add(lblChildNames
        , new GridBagConstraints(0, 10, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 0, 0, 47), 44, 0));
    this.add(txtChildNames
        , new GridBagConstraints(0, 11, 8, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 3), 435, 1));
    this.add(txtReferredBy
        , new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(1, 0, 0, 13), 20, 1));
    this.add(btnLookup
        , new GridBagConstraints(4, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 1));
    this.add(cbxEducation
        , new GridBagConstraints(3, 1, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(9, 9, 1, 39), 36, 1));
        
    /**
     * Added Text Filters
     */
    // MSB 02/10/2006
    // To resolve Double byte issues
    // we need to make sure text isn't filtered
    // and only length restriction is imposed on input.
    txtNotes1.setDocument(new CMSTextFilter(100));
    txtNotes2.setDocument(new CMSTextFilter(100));
    txtPartFamName.setDocument(new TextFilter(NAME_SPEC, 30));
    txtPartName.setDocument(new TextFilter(NAME_SPEC, 30));
    txtBirthPlc.setDocument(new CMSTextFilter(30));
    txtSpclDate.setDocument(new CMSTextFilter(10));
    txtNumChildren.setDocument(new TextFilter(TextFilter.NUMERIC, 2));
    txtChildNames.setDocument(new TextFilter(MULTI_NAME_SPEC, 50));
//    txtNotes1.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 100));
//    txtNotes2.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 100));
//    txtPartFamName.setDocument(new TextFilter(NAME_SPEC, 30));
//    txtPartName.setDocument(new TextFilter(NAME_SPEC, 30));
//    txtBirthPlc.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 30));
//    txtSpclDate.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 10));
//    txtNumChildren.setDocument(new TextFilter(TextFilter.NUMERIC, 2));
//    txtChildNames.setDocument(new TextFilter(MULTI_NAME_SPEC, 50));
    txtNotes1.setBorder(txtReferredBy.getBorder());
    txtNotes2.setBorder(txtReferredBy.getBorder());
  }

  /**
   * put your documentation comment here
   */
  public void requestFocusOnFirstField() {
    btnLookup.requestFocus();
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
            //txtReferredBy.requestFocus(); (commented because the lookup should always be through btnLookup
            btnLookup.requestFocus();
          }
        });
      }
    });
  }

  /**
   * Added RequestFocus on IssueDate
   */
  public void requestFocusToSpecialDate() {
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
            txtSpclDate.requestFocus();
          }
        });
      }
    });
  }
}

