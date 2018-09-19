/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.JPanel;
import com.chelseasystems.cr.swing.bean.*;
import java.awt.*;
import javax.swing.BorderFactory;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.util.Version;


/**
 * <p>Title: TxnListHeaderPanel</p>
 * <p>Description: Header panel</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class TxnListHeaderPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private JCMSLabel lblName;
  private JCMSTextField txtName;
  private JCMSLabel lblSearch;
  private JCMSTextField txtSearch;
  private JCMSLabel lblTotal;
  private JCMSTextField txtTotal;
  private JCMSLabel lblDtRange;
  private JCMSTextField txtDtRange;

  /**
   * put your documentation comment here
   */
  public TxnListHeaderPanel() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param bVisible
   */
  public void setNameVisible(boolean bVisible) {
    lblName.setVisible(false);
    txtName.setVisible(false);
  }
  
  /**
   * put your documentation comment here
   * @param bVisible
   */
  public void setTotalVisible(boolean bVisible) {
	lblTotal.setVisible(bVisible);
	txtTotal.setVisible(bVisible);
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.setBackground(theAppMgr.getBackgroundColor());
    lblName.setFont(theAppMgr.getTheme().getLabelFont());
    lblSearch.setFont(theAppMgr.getTheme().getLabelFont());
    lblTotal.setFont(theAppMgr.getTheme().getLabelFont());
    lblDtRange.setFont(theAppMgr.getTheme().getLabelFont());
    txtName.setFont(theAppMgr.getTheme().getTextFieldFont());
    txtSearch.setFont(theAppMgr.getTheme().getTextFieldFont());
    txtTotal.setFont(theAppMgr.getTheme().getTextFieldFont());
    txtDtRange.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblName.setAppMgr(theAppMgr);
    lblSearch.setAppMgr(theAppMgr);
    lblTotal.setAppMgr(theAppMgr);
    lblDtRange.setAppMgr(theAppMgr);
    txtName.setAppMgr(theAppMgr);
    txtSearch.setAppMgr(theAppMgr);
    txtTotal.setAppMgr(theAppMgr);
    txtDtRange.setAppMgr(theAppMgr);
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCustomerName(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtName.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCustomerName() {
    return txtName.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSearchCriteria(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtSearch.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSearchCriteria() {
    return txtSearch.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setTotal(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtTotal.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotal() {
    return txtTotal.getText().trim();
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setDateRange(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtDtRange.setText(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDateRange() {
    return txtDtRange.getText().trim();
  }

  /**
   * put your documentation comment here
   */
  public void reset() {
    txtName.setText("");
    txtSearch.setText("");
    txtTotal.setText("");
    txtDtRange.setText("");
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    java.util.ResourceBundle resBundle = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    lblName = new JCMSLabel();
    txtName = new JCMSTextField();
    lblSearch = new JCMSLabel();
    txtSearch = new JCMSTextField();
    lblTotal = new JCMSLabel();
    txtTotal = new JCMSTextField();
    lblDtRange = new JCMSLabel();
    txtDtRange = new JCMSTextField();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    txtName.setRequestFocusEnabled(false);
    txtName.setEditable(false);
    txtSearch.setEditable(false);
    txtTotal.setRequestFocusEnabled(false);
    txtTotal.setEditable(false);
    txtDtRange.setRequestFocusEnabled(false);
    txtDtRange.setEditable(false);
    txtName.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
    txtSearch.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
    txtTotal.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
    txtDtRange.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));    
    lblName.setText(resBundle.getString("Name"));
    this.setLayout(gridBagLayout1);
    lblSearch.setText(resBundle.getString("Search Criteria"));
    lblTotal.setText(resBundle.getString("Total"));
    lblDtRange.setText(resBundle.getString("Date Range"));
    this.add(lblName
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 15, 1));
    this.add(txtName
        , new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 190, 1));
    this.add(lblSearch
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 8, 0, 3), 1, 1));
    this.add(txtSearch
        , new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 282, 1));
    //PCR 1817 - Improvements for Europe
    if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
		this.add(lblTotal
		    , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
		    , GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 15, 1));
		this.add(txtTotal
            , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 190, 1));
	}
    this.add(lblDtRange
        , new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 8, 0, 0), 1, 1));
    this.add(txtDtRange
        , new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 45, 1));
  }
}

