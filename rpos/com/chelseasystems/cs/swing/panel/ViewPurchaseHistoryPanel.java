/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.JPanel;
import javax.swing.*;
import com.chelseasystems.cs.swing.model.PurchaseHistoryModel;
import javax.swing.table.TableCellRenderer;
import com.chelseasystems.cs.customer.CustomerSaleSummary;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentEvent;
import javax.swing.table.*;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title:ViewPurchaseHistoryPanel </p>
 * <p>Description: View Customers Purchase History</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company:Skill Net Inc </p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ViewPurchaseHistoryPanel extends JPanel {
  private JCMSTable tblPurchHistory;
  private JPanel pnlHeader;
  private JPanel pnlTable;
  private PurchaseHistoryModel modelPurchaseHistory;
  private JCMSLabel lblName;
  private JCMSLabel lblNameValue;
  private TextRenderer renderer;

  /**
   * put your documentation comment here
   */
  public ViewPurchaseHistoryPanel() {
    try {
      renderer = new TextRenderer();
      jbInit();
      TableColumnModel modelColumn = tblPurchHistory.getColumnModel();
      for (int i = 0; i < modelPurchaseHistory.getColumnCount(); i++) {
        modelColumn.getColumn(i).setCellRenderer(renderer);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param arraySummary[]
   */
  public void setCustomerSummary(CustomerSaleSummary arraySummary[]) {
    if (arraySummary == null || arraySummary.length < 1)
      return;
    modelPurchaseHistory.setCustomerSaleSummary(arraySummary);
  }

  /**
   * put your documentation comment here
   * @param sName
   */
  public void setCustomerName(String sName) {
    if (sName == null || sName.length() < 1)
      return;
    lblNameValue.setText(sName);
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    tblPurchHistory.setAppMgr(theAppMgr);
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlHeader.setBackground(theAppMgr.getBackgroundColor());
    pnlTable.setBackground(theAppMgr.getBackgroundColor());
    renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblName.setFont(theAppMgr.getTheme().getLabelFont());
    lblNameValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    lblName.setPreferredSize(new Dimension((int)(25 * r), (int)(30 * r)));
    lblNameValue.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    lblName = new JCMSLabel();
    lblNameValue = new JCMSLabel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    modelPurchaseHistory = new PurchaseHistoryModel();
    pnlTable = new JPanel(new BorderLayout());
    pnlHeader = new JPanel(gridBagLayout1);
    tblPurchHistory = new JCMSTable(modelPurchaseHistory, JCMSTable.SELECT_ROW);
    tblPurchHistory.getTableHeader().setPreferredSize(new Dimension(833, 50));
    tblPurchHistory.setCellSelectionEnabled(false);
    tblPurchHistory.setColumnSelectionAllowed(false);
    tblPurchHistory.setRowSelectionAllowed(false);
    tblPurchHistory.setRequestFocusEnabled(false);
    lblName.setLabelFor(lblNameValue);
    lblName.setRequestFocusEnabled(false);
    lblName.setText(CMSApplet.res.getString("Name"));
    lblNameValue.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
    lblNameValue.setIconTextGap(4);
    lblNameValue.setText("");
    pnlTable.add(tblPurchHistory.getTableHeader(), BorderLayout.NORTH);
    pnlTable.add(tblPurchHistory, BorderLayout.CENTER);
    pnlHeader.setPreferredSize(new Dimension(200, 50));
    pnlHeader.add(lblName
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(14, 5, 15, 0), 35, 5));
    pnlHeader.add(lblNameValue
        , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(14, 0, 15, 20), 302, 19));
    this.setLayout(new BorderLayout());
    this.add(pnlHeader, BorderLayout.NORTH);
    this.add(pnlTable, BorderLayout.CENTER);
    this.doLayout();
  }

  /**
   * put your documentation comment here
   */
  public void clear() {
    modelPurchaseHistory.clear();
  }

  private class TextRenderer extends JLabel implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     */
    public TextRenderer() {
      super();
      setFont(new Font("Helvetica", 1, 12));
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
      setOpaque(true);
    }

    /**
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int col) {
      if (value != null)
        setText(value.toString());
      else
        setText("");
      switch (col) {
        case PurchaseHistoryModel.TXN_TOTAL:
          setHorizontalAlignment(JLabel.CENTER);
          break;
        default:
          setHorizontalAlignment(JLabel.RIGHT);
      }
      return (this);
    }
  }
}

