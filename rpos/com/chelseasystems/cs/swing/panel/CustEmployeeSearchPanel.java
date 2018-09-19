package com.chelseasystems.cs.swing.panel;

import javax.swing.JPanel;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import java.awt.GridBagLayout;
import com.chelseasystems.cr.swing.CMSApplet;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CustEmployeeSearchPanel extends JPanel {
  private JCMSLabel lblFamilyName;
  private JCMSTextField txtFamilyName;
  private JCMSLabel lblCustomerID;
  private JCMSTextField txtCustomerID;

  public CustEmployeeSearchPanel() {
    try {
      init();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public String getFamilyName() {
    return txtFamilyName.getText();
  }

  public String getCustomerID() {
    return txtCustomerID.getText();
  }

  public void reset() {
    txtFamilyName.setText("");
    txtCustomerID.setText("");
  }

  public void requestFocusToFamilyName()
  {
    txtFamilyName.requestFocus();
  }

  public void setAppMgr(IApplicationManager theAppMgr) {
    setBackground(theAppMgr.getBackgroundColor());
    lblFamilyName.setAppMgr(theAppMgr);
    txtFamilyName.setAppMgr(theAppMgr);
    lblCustomerID.setAppMgr(theAppMgr);
    txtCustomerID.setAppMgr(theAppMgr);
  }

  private void init() throws Exception {
    lblFamilyName = new JCMSLabel();
    txtFamilyName = new JCMSTextField();
    lblCustomerID = new JCMSLabel();
    txtCustomerID = new JCMSTextField();
    setLayout(new GridBagLayout());
    lblFamilyName.setText(CMSApplet.res.getString("Family Name"));
    lblCustomerID.setText(CMSApplet.res.getString("Customer ID"));

    add(lblFamilyName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                              , GridBagConstraints.WEST, GridBagConstraints.NONE
                                              , new Insets(0, 15, 0, 5), 5, 0));
    add(txtFamilyName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
                                              , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL
                                              , new Insets(0, 5, 0, 25), 5, 0));
    add(lblCustomerID, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                                              , GridBagConstraints.WEST, GridBagConstraints.NONE
                                              , new Insets(0, 25, 0, 5), 5, 0));
    add(txtCustomerID, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
                                              , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL
                                              , new Insets(0, 5, 0, 15), 5, 0));
  }


  public void resize() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    lblFamilyName.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
    lblFamilyName.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
    lblFamilyName.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
    txtFamilyName.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
    txtFamilyName.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
    txtFamilyName.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
    lblCustomerID.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
    lblCustomerID.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
    lblCustomerID.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
    txtCustomerID.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
    txtCustomerID.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
    txtCustomerID.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
  }
}
