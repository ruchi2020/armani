/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.dlg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.swing.panel.ItemDetailsTxnLookupPanel;
import com.chelseasystems.cs.pos.TransactionSearchString;


/**
 * put your documentation comment here
 */
public class ItemDetailsTxnLookupDlg extends JDialog implements ActionListener {
  private JButton btnOk;
  private JButton btnCancel;
  private JPanel pnlButtons;
  private ItemDetailsTxnLookupPanel pnlLookup;
  private IApplicationManager theAppMgr;
  private TransactionSearchString txnSrchStr;

  /**
   * put your documentation comment here
   * @param   Frame frame
   * @param   IApplicationManager theAppMgr
   * @param   TransactionSearchString txnSrchStr
   */
  public ItemDetailsTxnLookupDlg(Frame frame, IApplicationManager theAppMgr
      , TransactionSearchString txnSrchStr) {
    super(frame, "Transaction History Lookup", true);
    this.theAppMgr = theAppMgr;
    this.txnSrchStr = txnSrchStr;
    init();
    pnlLookup.setAppMgr(theAppMgr);
  }

  /**
   * put your documentation comment here
   */
  private void init() {
    pnlLookup = new ItemDetailsTxnLookupPanel();
    pnlButtons = new JPanel(new BorderLayout());
    btnOk = theAppMgr.getTheme().getDefaultBtn();
    btnOk.setText("Ok");
    btnOk.setMnemonic('O');
    btnOk.addActionListener(this);
    pnlButtons.add(btnOk, BorderLayout.WEST);
    btnCancel = theAppMgr.getTheme().getDefaultBtn();
    btnCancel.setText("Cancel");
    btnCancel.setMnemonic('C');
    btnCancel.addActionListener(this);
    pnlButtons.add(btnCancel, BorderLayout.EAST);
    pnlButtons.setBackground(theAppMgr.getBackgroundColor());
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(pnlLookup, BorderLayout.CENTER);
    this.getContentPane().add(pnlButtons, BorderLayout.SOUTH);
    this.setResizable(false);
    setSize(520, 250);
  }

  /**
   * put your documentation comment here
   * @param visible
   */
  public void setVisible(boolean visible) {
    if (visible) {
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
    }
    super.setVisible(visible);
  }

  /**
   * put your documentation comment here
   * @param ae
   */
  public void actionPerformed(ActionEvent ae) {
    String sCommand = ae.getActionCommand();
    if (sCommand.equals("Ok")) {
      txnSrchStr.setSearchRequired(true);
      txnSrchStr.setSku(pnlLookup.getSku());
      txnSrchStr.setStyle(pnlLookup.getStyle());
      txnSrchStr.setSupplier(pnlLookup.getSupplier());
      txnSrchStr.setFabric(pnlLookup.getFabric());
      txnSrchStr.setModel(pnlLookup.getModel());
      txnSrchStr.setColor(pnlLookup.getColor());
      txnSrchStr.setYear(pnlLookup.getYear());
      txnSrchStr.setSeason(pnlLookup.getSeason());
    } else if (sCommand.equals("Cancel")) {
      txnSrchStr.setSearchRequired(false);
    }
    this.dispose();
  }
}

