/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.dlg;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.text.DateFormat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.MallCert;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.bean.JCMSToolTip;
import com.chelseasystems.cr.swing.ScrollProcessor;
import java.text.SimpleDateFormat;
import java.io.*;
import com.chelseasystems.cs.currency.CurrencyRate;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import java.math.BigDecimal;


/**
 */
public class ForeignCurrencyExchangeRateListDlg extends JDialog implements ScrollProcessor {
  private ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  private CurrencyRate[] currencyRates = null;
  private IApplicationManager theAppMgr;
  JButton btnDown;
  JButton btnUp;
  JButton btnCancel;
  JButton btnOK;
  //JTextPane txtInstruct = new JTextPane();
  JLabel txtInstruct = new JLabel();
  ForeignCurrencyExchangeRateListModel model = new ForeignCurrencyExchangeRateListModel();
  JCMSTable tblCheck = new JCMSTable(model, JCMSTable.SELECT_ROW);
  JCMSToolTip ttip;
  private CMSPaymentTransactionAppModel theTxn = null;
  private boolean isOk = false;

  /**
   * @param    Frame frame
   * @param    IApplicationManager theAppMgr
   * @param    PaymentTransaction theTxn
   */
  public ForeignCurrencyExchangeRateListDlg(IApplicationManager theAppMgr
      , CurrencyRate[] currencyRates, CMSPaymentTransactionAppModel theTxn) {
    super(theAppMgr.getParentFrame(), true);
    this.setResizable(false);
    this.setTitle(res.getString("Exchange Rates"));
    try {
      this.theAppMgr = theAppMgr;
      this.currencyRates = currencyRates;
      this.theTxn = theTxn;
      //this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      this.setModal(true);
      ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
      jbInit();
      loadCurrencyDetails();
      setSize(750, 388);
      this.isOk = false;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @exception Exception
   */
  void jbInit()
      throws Exception {
    JPanel pnlMain = new JPanel();
    JPanel pnlSouth = new JPanel();
    JPanel pnlCenter = new JPanel();
    JPanel pnlTop = new JPanel();
    JPanel pnlLeft = new JPanel();
    JPanel pnlRight = new JPanel();
    JPanel pnlTable = new JPanel();
    btnDown = theAppMgr.getTheme().getDefaultBtn();
    btnUp = theAppMgr.getTheme().getDefaultBtn();
    btnOK = theAppMgr.getTheme().getDefaultBtn();
    btnCancel = theAppMgr.getTheme().getDefaultBtn();
    pnlMain.setLayout(new BorderLayout());
    pnlMain.setBackground(theAppMgr.getBackgroundColor());
    pnlSouth.setPreferredSize(new Dimension(10, 84));
    pnlSouth.setOpaque(false);
    pnlCenter.setOpaque(false);
    pnlTop.setPreferredSize(new Dimension(10, 60));
    pnlTop.setLayout(new BorderLayout());
    pnlTop.setOpaque(false);
    pnlLeft.setPreferredSize(new Dimension(30, 10));
    pnlLeft.setOpaque(false);
    pnlRight.setPreferredSize(new Dimension(30, 10));
    pnlRight.setOpaque(false);
    pnlTable.setOpaque(false);
    pnlTable.setLayout(new BorderLayout());
    tblCheck.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        tblCheck_componentResized(e);
      }
    });
    tblCheck.setAppMgr(theAppMgr);
    txtInstruct.setText("    " + res.getString("Foreign Currency Rates."));
    txtInstruct.setForeground(theAppMgr.getTheme().getMsgTextColor());
    txtInstruct.setFont(theAppMgr.getTheme().getDialogFont());
    txtInstruct.setOpaque(false);
    btnOK.setText(res.getString("OK"));
    btnOK.setMnemonic(btnOK.getText().charAt(0));
    btnOK.setVisible(true);
    btnOK.addActionListener(new java.awt.event.ActionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnOK_actionPerformed(e);
      }
    });
    btnCancel.setText(res.getString("Cancel"));
    btnCancel.setMnemonic(btnCancel.getText().charAt(0));
    btnCancel.addActionListener(new java.awt.event.ActionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnCancel_actionPerformed(e);
      }
    });
    //        btnCancel.addActionListener(btnCancel_actionPerformed);
    btnUp.setText(res.getString("Page Up"));
    btnUp.setMnemonic(btnUp.getText().charAt(5));
    btnUp.setDefaultCapable(false);
    btnUp.addActionListener(new java.awt.event.ActionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnUp_actionPerformed(e);
      }
    });
    btnDown.setText(res.getString("Page Down"));
    btnDown.setMnemonic(btnDown.getText().charAt(5));
    btnDown.setDefaultCapable(false);
    btnDown.addActionListener(new java.awt.event.ActionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnDown_actionPerformed(e);
      }
    });
    pnlCenter.setLayout(new BorderLayout());
    getContentPane().add(pnlMain);
    pnlMain.add(pnlSouth, BorderLayout.SOUTH);
    pnlSouth.add(btnUp, null);
    pnlSouth.add(btnDown, null);
    pnlSouth.add(btnOK, null);
    pnlSouth.add(btnCancel, null);
    pnlMain.add(pnlCenter, BorderLayout.CENTER);
    pnlCenter.add(pnlTop, BorderLayout.NORTH);
    pnlTop.add(txtInstruct, BorderLayout.CENTER);
    pnlCenter.add(pnlLeft, BorderLayout.WEST);
    pnlCenter.add(pnlRight, BorderLayout.EAST);
    pnlCenter.add(pnlTable, BorderLayout.CENTER);
    pnlTable.add(tblCheck.getTableHeader(), BorderLayout.NORTH);
    pnlTable.add(tblCheck, BorderLayout.CENTER);
    ttip.setType(JCMSToolTip.FOLLOWING);
    this.getRootPane().setDefaultButton(btnOK);
    KeyListener keyListener = new KeyListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void keyReleased(KeyEvent e) {}

      /**
       * put your documentation comment here
       * @param e
       */
      public void keyPressed(KeyEvent e) {}

      /**
       * put your documentation comment here
       * @param e
       */
      public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        if (ch == '\n') { //KeyEvent.VK_ENTER
          btnCancel.doClick();
          e.consume();
        } else if (ch == 0x1B) { //KeyEvent.VK_ESCAPE
          btnOK.doClick();
          e.consume();
        }
      }
    };
    this.addKeyListener(keyListener);
  }

  // this event is called by the JCMSTable when the user arrows up beyond the displayed rows
  public void prevPage() {
    btnUp.doClick();
  }

  // this event is called by the JCMSTable when the user arrows dn beyond the displayed rows
  public void nextPage() {
    btnDown.doClick();
  }

  // return model page number
  public int getCurrentPageNumber() {
    return (model.getCurrentPageNumber());
  }

  // return total page number
  public int getPageCount() {
    return (model.getPageCount());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean getIsOk() {
    return this.isOk;
  }

  /**
   * @param e
   */
  void btnOK_actionPerformed(ActionEvent e) {
    try {
      this.isOk = true;
      this.dispose();
    } catch (Exception ex) {
      System.out.println("FrankDlg.btnOK_actionPerformed()->" + ex);
      this.dispose();
    }
  }

  /**
   * @param e
   */
  void btnCancel_actionPerformed(ActionEvent e) {
    this.isOk = false;
    this.dispose();
  }

  /**
   * @param e
   */
  void btnUp_actionPerformed(ActionEvent e) {
    model.prevPage();
    if (model.getRowCount() > 0)
      tblCheck.setRowSelectionInterval(0, 0);
    fireButtonClick(e);
  }

  /**
   * @param e
   */
  void btnDown_actionPerformed(ActionEvent e) {
    model.nextPage();
    if (model.getRowCount() > 0)
      tblCheck.setRowSelectionInterval(0, 0);
    fireButtonClick(e);
  }

  /**
   */
  private void fireButtonClick(ActionEvent e) {
    MouseEvent me = new MouseEvent((JComponent)e.getSource(), e.getID(), System.currentTimeMillis()
        , e.getModifiers(), -40, -5, 1, false);
    ttip.setText(res.getString("Page") + ":  " + (model.getCurrentPageNumber() + 1));
    ttip.show(me);
  }

  /**
   */
  private void loadCurrencyDetails() {
    try {
      if (currencyRates != null) {
        ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
        System.out.println("Ruchi inside loadCurrencyDetails   :"+amtDue);
        for (int i = 0; i < currencyRates.length; i++) {
          Hashtable ht = new Hashtable();
          CurrencyType curType = CurrencyType.getCurrencyType(currencyRates[i].getFromCurrency());
          /*
           * Fix for issue 1855. Need to calculate the new amount using a double rate.
           */
           // ArmCurrency exactAmt = amtDue.convertTo(curType
               // , new ArmCurrency(currencyRates[i].getConversionRate().doubleValue()));
          ArmCurrency exactAmt = amtDue.convertTo(curType, new Double(currencyRates[i].getConversionRate().doubleValue()));
          System.out.println("Ruchi inside ForeignCurrencyExchangeRate  loadCurrencyDetails :"+exactAmt.doubleValue());
          //System.out.println("Exact : " + exactAmt.getCurrencyType().getLocale());
          ArrayList al = new ArrayList();
          al.add(exactAmt);
          ht.put(currencyRates[i], al);
          model.addCurrencyRateTable(ht);
        }
      }
      if (model.getRowCount() > 0)
        tblCheck.setRowSelectionInterval(0, 0);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @param visible
   */
  public void setVisible(boolean visible) {
    if (visible) {
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
    } else {
      //TD
      //ttip.hide();
      ttip.setVisible(false);
    }
    super.setVisible(visible);
  }

  /**
   * @param e
   */
  void tblCheck_componentResized(ComponentEvent e) {
    model.setColumnWidth(tblCheck);
    // int height =  tblName.getSize().height;
    // Dimension dim = tblName.getIntercellSpacing();
    model.setRowsShown(tblCheck.getHeight() / tblCheck.getRowHeight());
  }

  /**
   */
  public Hashtable getSelectedRow() {
    int row = tblCheck.getSelectedRow();
    if (row < 0)
      return null;
    Hashtable currencyRateTable = model.getCurrencyRates(row);
    return currencyRateTable;
  }

  /************************************************************************/
  private class ForeignCurrencyExchangeRateListModel extends ScrollableTableModel {

    /**
     */
    public ForeignCurrencyExchangeRateListModel() {
      this.setColumnIdentifiers(new String[] {res.getString("From Currency")
          , res.getString("To Currency"), res.getString("Ex Rate"), res.getString("Exct Amount")
          , res.getString("Effective Date")
      });
    }

    /**
     * @param endorse
     */
    public void addCurrencyRateTable(Hashtable ht) {
      addRow(ht);
    }

    /**
     * @param row
     * @return
     */
    public Hashtable getCurrencyRates(int row) {
      Hashtable ht = (Hashtable)this.getRowInPage(row);
      return (ht);
    }

    /**
     * @return
     */
    public int getColumnCount() {
      return (5);
    }

    /**
     * @param row
     * @param column
     * @return
     */
    public boolean isCellEditable(int row, int column) {
      return (false);
    }

    /**
     * @param row
     * @param column
     * @return
     */
    public Object getValueAt(int row, int column) {
      Vector vTemp = this.getCurrentPage();
      Hashtable hashT = (Hashtable)vTemp.elementAt(row);
      Enumeration enm = hashT.keys();
      while (enm.hasMoreElements()) {
        CurrencyRate curRate = (CurrencyRate)enm.nextElement();
        ArrayList aList = (ArrayList)hashT.get(curRate);
        String date = new String();
        ConfigMgr config = new ConfigMgr("currency.cfg");
        if (curRate.getUpdateDate() != null) {
          try {
            //String dateFormat = config.getString("DATE_FORMAT_STRING");
            SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
            df.setLenient(false);
            date = df.format(curRate.getUpdateDate());
          } catch (Exception e) {}
        }
        switch (column) {
          case 0:
            return (curRate.getFromCurrency());
          case 1:
            return (curRate.getToCurrency());
          case 2:
            return (curRate.getConversionRate());
          case 3:
            ArmCurrency exactAmt = (com.chelseasystems.cr.currency.ArmCurrency)aList.get(0);
            return exactAmt.formattedStringValue(exactAmt.getCurrencyType().getLocale()); //exactAmt.getCurrencyType().getLocale()
          case 4:
            return (date.toString());
          default:
            return (" ");
        }
      }
      return null;
    }

    /**
     * @param table
     */
    public void setColumnWidth(JTable table) {
      table.getColumnModel().getColumn(1).setPreferredWidth(100);
      table.getColumnModel().getColumn(2).setPreferredWidth(100);
      table.getColumnModel().getColumn(3).setPreferredWidth(100);
      table.getColumnModel().getColumn(4).setPreferredWidth(100);
      //            table.getColumnModel().getColumn(0).setPreferredWidth(table.getWidth() - (table.getColumnModel().getColumn(1).getPreferredWidth()
      //                                                                                      + table.getColumnModel().getColumn(2).getPreferredWidth()));
    }
  }
}

