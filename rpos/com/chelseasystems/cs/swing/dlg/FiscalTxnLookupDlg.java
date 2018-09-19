/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.dlg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.panel.FiscalTxnLookupPanel;
import com.chelseasystems.cs.pos.TransactionSearchString;
import java.text.SimpleDateFormat;

/**
 * put your documentation comment here
 */
public class FiscalTxnLookupDlg extends JDialog implements ActionListener {
  private JButton btnOk;
  private JButton btnCancel;
  private JPanel pnlButtons;
  private FiscalTxnLookupPanel pnlLookup;
  private IApplicationManager theAppMgr;
  private TransactionSearchString txnSrchStr;
  private SimpleDateFormat dateFormat;
  private CMSStore theStore;

  /**
   * put your documentation comment here
   * @param   Frame frame
   * @param   IApplicationManager theAppMgr
   */
  public FiscalTxnLookupDlg(Frame frame, IApplicationManager theAppMgr) {
    super(frame, "Transaction History Lookup", true);
    this.theAppMgr = theAppMgr;
    init();
    txnSrchStr = new TransactionSearchString();
    pnlLookup.setAppMgr(theAppMgr);
    theStore = ((CMSStore)theAppMgr.getGlobalObject("STORE"));
    pnlLookup.setStoreID(theStore.getId());
    dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
  }


  /**
   * put your documentation comment here
   */
  private void init() {
    pnlLookup = new FiscalTxnLookupPanel();
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
    setSize(680, 215);
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
      if(!completeAttributes())
      {
        theAppMgr.showErrorDlg("Please enter a search criteria");
        return;
      }
      txnSrchStr = new TransactionSearchString();
      try
      {
        if(pnlLookup.getDate().length() >0)
        {
          txnSrchStr.setProcessDate(dateFormat.parse(pnlLookup.getDate()));
        }
      }
      catch(Exception e)
      {
        theAppMgr.showErrorDlg("Please enter valid date MM/DD/YYYY");
        pnlLookup.requestFocusToTxtDate();
        return;
      }
      txnSrchStr.setSearchRequired(true);
      txnSrchStr.setFiscalSearch(true);
      txnSrchStr.setTransactionId(pnlLookup.getTransactionNumber());
      txnSrchStr.setCompanyCode(pnlLookup.getCompanyCode());
      txnSrchStr.setRegisterId(pnlLookup.getRegisterID());
      txnSrchStr.setStoreId(pnlLookup.getStoreID());
      txnSrchStr.setFiscalRecieptNum(pnlLookup.getFiscalReceiptNumber());
      txnSrchStr.setFiscalDocType(pnlLookup.getFiscalDocumentType());
      txnSrchStr.setFiscalDocNum(pnlLookup.getFiscalDocumentNumber());

    } else if (sCommand.equals("Cancel")) {
      txnSrchStr.setSearchRequired(false);
    }
    this.dispose();
  }

  private boolean completeAttributes()
  {
    if(pnlLookup.getDate().length() > 0) return true;
    if(pnlLookup.getTransactionNumber().length() > 0) return true;
    if(pnlLookup.getCompanyCode().length() > 0) return true;
    if(pnlLookup.getRegisterID().length() > 0) return true;
    if(pnlLookup.getStoreID().length() > 0) return true;
    if(pnlLookup.getFiscalReceiptNumber().length() > 0) return true;
    if(pnlLookup.getFiscalDocumentType().length() > 0) return true;
    if(pnlLookup.getFiscalDocumentNumber().length() > 0) return true;
    return false;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public TransactionSearchString getTransactionSearchString() {
    return txnSrchStr;
  }
}

