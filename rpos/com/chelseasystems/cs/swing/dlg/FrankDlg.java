/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.dlg;

import jpos.*;
import jpos.events.*;
import java.util.*;
import java.text.DateFormat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.MallCert;
import com.chelseasystems.cr.payment.TravellersCheck;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.bean.JCMSToolTip;
import com.chelseasystems.cr.swing.ScrollProcessor;
import java.text.SimpleDateFormat;
import java.io.*;


/**
 */
public class FrankDlg extends JDialog implements JposConst, DataListener, POSPrinterConst
    , MICRConst, ScrollProcessor {
  private ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  private IApplicationManager theAppMgr;
  private PaymentTransaction theTxn;
  private String customerPhone = "";
  JButton btnDown;
  JButton btnUp;
  JButton btnFrank;
  JButton btnOK;
  JTextPane txtInstruct = new JTextPane();
  FrankModel model = new FrankModel();
  JCMSTable tblCheck = new JCMSTable(model, JCMSTable.SELECT_ROW);
  JCMSToolTip ttip;

  /** The static reference to the receipt printer **/
  static private POSPrinter posPrinter = CMSPrinter.getInstance();

  /** The static reference to the MICR on the printer **/
  static private MICR posMICR = CMSMICR.getInstance();

  /** The configuration information from the JPOS_peripherals.cfg file needed by the printer **/
  private ReceiptConfigInfo receiptInfo = ReceiptConfigInfo.getInstance();

  /** A flag to determine if reprint after an error has occured **/
  private boolean responseToContinue = true;
  private FrankBtnListener btnFrankListener;

  /**
   * @param    Frame frame
   * @param    IApplicationManager theAppMgr
   * @param    PaymentTransaction theTxn
   */
  public FrankDlg(IApplicationManager theAppMgr, PaymentTransaction theTxn, String customerPhone) {
    super(theAppMgr.getParentFrame(), true);
    this.setResizable(false);
    this.setTitle(res.getString("Endorse Check"));
    try {
      this.theAppMgr = theAppMgr;
      this.theTxn = theTxn;
      this.customerPhone = customerPhone;
      //this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      this.setModal(true);
      ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
      ConfigMgr cfg = new ConfigMgr("JPOS_peripherals.cfg");
      receiptInfo = CMSPrinter.getConfigInfo(cfg);
      if (null == receiptInfo.getPrinterName()) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "FrankDlg()"
            , res.getString("Cannot get instance of Receipt printer & MICR reader."), res.getString("Make sure JPOS_peripherals.cfg contains an entry with key LOGICAL_PRINTER_NAME that matches a logical printer name defined in JavaPOS.inf.")
            , LoggingServices.MAJOR);
      }
      jbInit();
      loadFrankChecks();
      setSize(750, 388);
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
    btnFrank = theAppMgr.getTheme().getDefaultBtn();
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
    txtInstruct.setText("    "
        + res.getString("Place document in validation slot and select the appropriate entry from list below.")
        + "\n    " + res.getString("Press 'Endorse' when ready."));
    txtInstruct.setForeground(theAppMgr.getTheme().getMsgTextColor());
    txtInstruct.setFont(theAppMgr.getTheme().getDialogFont());
    txtInstruct.setOpaque(false);
    btnOK.setText(res.getString("Close"));
    btnOK.setMnemonic(btnOK.getText().charAt(0));
    btnOK.setVisible(true);
    posMICR.addDataListener(this);
    btnOK.addActionListener(new java.awt.event.ActionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnOK_actionPerformed(e);
      }
    });
    btnFrank.setText(res.getString("Endorse"));
    btnFrank.setMnemonic(btnFrank.getText().charAt(0));
    btnFrankListener = new FrankBtnListener();
    btnFrank.addActionListener(btnFrankListener);
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
    pnlSouth.add(btnFrank, null);
    pnlSouth.add(btnOK, null);
    pnlMain.add(pnlCenter, BorderLayout.CENTER);
    pnlCenter.add(pnlTop, BorderLayout.NORTH);
    pnlTop.add(txtInstruct, BorderLayout.CENTER);
    pnlCenter.add(pnlLeft, BorderLayout.WEST);
    pnlCenter.add(pnlRight, BorderLayout.EAST);
    pnlCenter.add(pnlTable, BorderLayout.CENTER);
    pnlTable.add(tblCheck.getTableHeader(), BorderLayout.NORTH);
    pnlTable.add(tblCheck, BorderLayout.CENTER);
    ttip.setType(JCMSToolTip.FOLLOWING);
    this.getRootPane().setDefaultButton(btnFrank);
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
          btnFrank.doClick();
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
   * @param e
   */
  void btnOK_actionPerformed(ActionEvent e) {
    try {
      posMICR.removeDataListener(this);
      posMICR.setDataEventEnabled(false);
      posPrinter.clearOutput();
      CMSPrinter.release();
      posMICR.clearInput();
      CMSMICR.release();
      this.dispose();
    } catch (Exception ex) {
      System.out.println("FrankDlg.btnOK_actionPerformed()->" + ex);
      this.dispose();
    }
  }

  /**
   * @param e
   */
  void btnFrank_actionPerformed(ActionEvent e) {
    int row = tblCheck.getSelectedRow();
    if (row < 0) {
      theAppMgr.showErrorDlg(res.getString("Please select which check to endorse."));
      return;
    }
    btnFrank.removeActionListener(btnFrankListener);
    // Frank the check OR gift certificate
    Payment pay = model.getPayment(row);
    if (pay instanceof GiftCert)
      frankGiftCertificate();
    else if (pay instanceof MallCert) {
      frankMallCertificate(); // Uses Direct I/O Method
    } else if (pay instanceof TravellersCheck)
      frankTravellersCheck();
    else if (pay instanceof Check) {
      frankCheck();
    }
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        btnFrank.addActionListener(btnFrankListener);
      }
    });
    //Issue # 706
    //Testing
    //model.getEndorse(row).setEndorsed("Y");
    int rowNum = tblCheck.getRowCount();
    if (rowNum == 1 && model.getEndorse(row).getEndorsed().equalsIgnoreCase("Y")) {
      // Close the Endorse Dialog Box
      btnOK_actionPerformed(e);
    }
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
  private void loadFrankChecks() {
    try {
      for (Enumeration enm = theTxn.getPayments(); enm.hasMoreElements(); ) {
        Payment pay = (Payment)enm.nextElement();
        if (pay.isFrankingRequired()) {
          model.addEndorse(new Endorse(pay));
        }
      }
      // Gift certificate franking.
      if (theTxn instanceof CompositePOSTransaction) {
        POSLineItem[] lines = ((CompositePOSTransaction)theTxn).getSaleLineItemsArray();
        for (int i = lines.length - 1; i >= 0; i--)
          if (lines[i].getItem().getRedeemableType().equals(Redeemable.GIFT_CERTIFICATE_TYPE)) {
            POSLineItemDetail[] details = lines[i].getLineItemDetailsArray();
            for (int j = details.length - 1; j >= 0; j--) {
              GiftCert gc = new GiftCert(IPaymentConstants.GIFT_CERTIFICATE);
              gc.setAmount(details[j].getNetAmount());
              gc.setControlNum(details[j].getGiftCertificateId());
              gc.setId(details[j].getGiftCertificateId());
              model.addEndorse(new Endorse(gc));
            }
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
  private void updateStatus() {
    int row = tblCheck.getSelectedRow();
    if (row < 0)
      return;
    Endorse endorse = model.getEndorse(row);
    endorse.setEndorsed();
    model.fireTableDataChanged();
    tblCheck.repaint();
  }

  /**
   */
  private void updateEndorsed() {
    int row = tblCheck.getSelectedRow();
    if (row < 0)
      return;
    Endorse endorse = model.getEndorse(row);
    endorse.setEndorsed("Y");
    model.fireTableDataChanged();
    tblCheck.repaint();
  }

  /**
   * Perform the print function to back of a TMW gift certificate on the printer.
   **/
  public void frankGiftCertificate() {
    try {
      int[] iData = null;
      byte[] bObject;
      CMSPrinter.openDevice(receiptInfo.getPrinterName(), theAppMgr);
      CMSPrinter.reclaim(theAppMgr);
      posPrinter.beginInsertion(5000); //-1);  5212 - added timeout so to not wait forever
      posPrinter.endInsertion();
      // Per JT Thoman (epson consultant 425-635-5181) these directIO of codes would
      // access the endorsement print head.
      bObject = new byte[10];
      bObject[0] = 29;
      bObject[1] = 40;
      bObject[2] = 71;
      bObject[3] = 2;
      bObject[4] = 0;
      bObject[5] = 48;
      bObject[6] = 68;
      //            posPrinter.directIO(100, iData, bObject);
      // Check Endorsement
      //  What to do if row is not selected
      int row = tblCheck.getSelectedRow();
      Payment pay = model.getPayment(row);
      if (pay instanceof GiftCert) {
        GiftCert giftCert = (GiftCert)pay;
        StringBuffer lineBuf = new StringBuffer();
        // Perform 35 reverse line feeds before printing.
        posPrinter.printNormal(PTR_S_SLIP, CMSPrinter.ESC + "|35rF");
        posPrinter.printNormal(PTR_S_SLIP, res.getString("FOR DEPOSIT ONLY") + CMSPrinter.CRLF);
        lineBuf.append("  " + res.getString("CERTIFICATE NUMBER") + "  ");
        lineBuf.append(giftCert.getId());
        lineBuf.append(CMSPrinter.CRLF);
        lineBuf.append(CMSPrinter.CRLF);
        posPrinter.printNormal(PTR_S_SLIP, lineBuf.toString());
        lineBuf = new StringBuffer();
        lineBuf.append("  " + res.getString("TRAN") + " # ");
        lineBuf.append(theTxn.getId());
        lineBuf.append("    " + res.getString("AMT") + " ");
        lineBuf.append(giftCert.getAmount().stringValue());
        lineBuf.append(CMSPrinter.CRLF);
        posPrinter.printNormal(PTR_S_SLIP, lineBuf.toString());
      }
      posPrinter.beginRemoval( -1);
      posPrinter.endRemoval();
      // Per JT Thoman (epson consultant 425-635-5181) first line of codes would
      // reset to the front slip print head.  The second line would be directIO
      // codes for reverse print of 5 line feeds.
      //bObject[0] = 29;   27
      //bObject[1] = 40;   75
      //bObject[2] = 71;   5   // JT Thoman would be directIO of reverse 5 line feeds.
      //bObject[3] = 2;    0
      //bObject[4] = 0;    0
      //bObject[5] = 48;   0
      //bObject[6] = 4;    0
      //posPrinter.directIO(100, iData, bObject);
      //CMSPrinter.release();
      //Issue # 706
      updateEndorsed();
    } catch (JposException jpe) {
      String errMessage = "";
      System.out.println("FRANK GiftCertificate Code e -> " + jpe);
      System.out.println("FRANK GiftCertificateError Code -> " + jpe.getErrorCode());
      System.out.println("FRANK GiftCertificateExt Error Code -> " + jpe.getErrorCodeExtended());
      errMessage = res.getString("Printer Error") + ":  " + jpe.getMessage() + ".";
      if (jpe.getErrorCode() == 106) {
        System.out.println("RECEIVED ERROR CODE 106");
        return;
      }
      if (jpe.getErrorCode() == 112) {
        errMessage = res.getString(
            "Printer Error:  Gift Certificate not inserted in printer.  Do you want to try again?");
        responseToContinue = theAppMgr.showOptionDlg(res.getString("Printer Error."), errMessage);
        if (responseToContinue) {
          frankGiftCertificate();
        } else {
          return;
        }
      }
    } catch (Exception e) {
      String errMessage = "";
      System.out.println("FRANK GiftCertificate MICR Code e -> " + e);
      System.out.println("FRANK GiftCertificate MICRError Code -> " + e.getMessage());
      errMessage = res.getString("Printer Error") + ":  " + e.getMessage() + ".";
      theAppMgr.showErrorDlg(errMessage);
    }
  }

  /**
   * Perform the print function to back of a Mall certificate on the printer.
   **/
  public void frankMallCertificate() {
    try {
      int[] iData = null;
      byte[] bObject;
      CMSPrinter.openDevice(receiptInfo.getPrinterName(), theAppMgr);
      CMSPrinter.reclaim(theAppMgr);
      posPrinter.beginInsertion(5000); //-1);  5212 - added timeout so to not wait forever
      posPrinter.endInsertion();
      // Per JT Thoman (epson consultant 425-635-5181) these directIO of codes would
      // access the endorsement print head.
      bObject = new byte[10];
      bObject[0] = 29;
      bObject[1] = 40;
      bObject[2] = 71;
      bObject[3] = 2;
      bObject[4] = 0;
      bObject[5] = 48;
      bObject[6] = 68;
      //            posPrinter.directIO(100, iData, bObject);
      // Check Endorsement
      //  What to do if row is not selected
      int row = tblCheck.getSelectedRow();
      Payment pay = model.getPayment(row);
      if (pay instanceof MallCert) {
        MallCert mallCert = (MallCert)pay;
        // Perform 35 reverse line feeds before printing.
        posPrinter.printNormal(PTR_S_SLIP, CMSPrinter.ESC + "|35rF");
        posPrinter.printNormal(PTR_S_SLIP, getFrankableString());
      }
      posPrinter.beginRemoval( -1);
      posPrinter.endRemoval();
      // Per JT Thoman (epson consultant 425-635-5181) first line of codes would
      // reset to the front slip print head.  The second line would be directIO
      // codes for reverse print of 5 line feeds.
      //bObject[0] = 29;   27
      //bObject[1] = 40;   75
      //bObject[2] = 71;   5   // JT Thoman would be directIO of reverse 5 line feeds.
      //bObject[3] = 2;    0
      //bObject[4] = 0;    0
      //bObject[5] = 48;   0
      //bObject[6] = 4;    0
      //posPrinter.directIO(100, iData, bObject);
      //CMSPrinter.release();
      //Issue # 706
      updateEndorsed();
    } catch (JposException jpe) {
      String errMessage = "";
      System.out.println("FRANK MallCertificate Code e -> " + jpe);
      System.out.println("FRANK MallCertificateError Code -> " + jpe.getErrorCode());
      System.out.println("FRANK MallCertificateExt Error Code -> " + jpe.getErrorCodeExtended());
      errMessage = res.getString("Printer Error") + ":  " + jpe.getMessage() + ".";
      if (jpe.getErrorCode() == 106) {
        System.out.println("RECEIVED ERROR CODE 106");
        return;
      }
      if (jpe.getErrorCode() == 112) {
        errMessage = res.getString(
            "Printer Error:  Mall Certificate not inserted in printer.  Do you want to try again?");
        responseToContinue = theAppMgr.showOptionDlg(res.getString("Printer Error."), errMessage);
        if (responseToContinue) {
          frankMallCertificate();
        } else {
          return;
        }
      }
    } catch (Exception e) {
      String errMessage = "";
      System.out.println("FRANK MallCertificate MICR Code e -> " + e);
      System.out.println("FRANK MallCertificate MICRError Code -> " + e.getMessage());
      errMessage = res.getString("Printer Error") + ":  " + e.getMessage() + ".";
      theAppMgr.showErrorDlg(errMessage);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean frankTravellersCheck() {
    String coName = "";
    String accountNumber = "";
    String stateIdCode = "";
    String storeId = "";
    int totalRow = tblCheck.getRowCount();
    int row = tblCheck.getSelectedRow();
    try {
      int[] iData = null;
      byte[] bObject;
      CMSPrinter.openDevice(receiptInfo.getPrinterName(), theAppMgr);
      CMSPrinter.reclaim(theAppMgr);
      posPrinter.beginInsertion(5000); //-1);  5212 - added timeout so to not wait forever
      posPrinter.endInsertion();
      // Per JT Thoman (epson consultant 425-635-5181) these directIO of codes would
      // access the endorsement print head.
      bObject = new byte[10];
      bObject[0] = 29;
      bObject[1] = 40;
      bObject[2] = 71;
      bObject[3] = 2;
      bObject[4] = 0;
      bObject[5] = 48;
      bObject[6] = 68;
      //            posPrinter.directIO(100, iData, bObject);
      // Check Endorsement
      //  What to do if row is not selected
      Payment pay = model.getPayment(row);
      StringBuffer lineBuf = new StringBuffer();
      // Perform 35 reverse line feeds before printing.
      //      posPrinter.printNormal(PTR_S_SLIP, CMSPrinter.ESC + "|35rF");
      // Perform 6 reverse line feeds before printing. Since the size of the document
      // is unknown at run endorse at the trailing edge
      posPrinter.printNormal(PTR_S_SLIP, CMSPrinter.ESC + "|35rF");
      Store store = theTxn.getStore();
      if (null != store) {
        storeId = store.getId();
        coName = store.getCompanyNameForFranking();
        accountNumber = store.getCompanyAccountNumberForFranking();
      }
      posPrinter.printNormal(PTR_S_SLIP, getFrankableString());
      posPrinter.beginRemoval( -1);
      posPrinter.endRemoval();
      model.setValueAt("Endorsed", row, 3);
      if (row < (totalRow - 1))
        tblCheck.setRowSelectionInterval(row + 1, row + 1);
      this.repaint();
      //Issue # 706
      updateEndorsed();
    } catch (JposException jpe) {
      String errMessage = "";
      System.out.println("FRANK Travellers check Code e -> " + jpe);
      System.out.println("FRANK Travellers check Code -> " + jpe.getErrorCode());
      System.out.println("FRANK Travellers check Error Code -> " + jpe.getErrorCodeExtended());
      errMessage = res.getString("Printer Error") + ":  " + jpe.getMessage() + ".";
      if (jpe.getErrorCode() == 106) {
        System.out.println("RECEIVED ERROR CODE 106");
        return false;
      }
      if (jpe.getErrorCode() == 112) {
        errMessage = res.getString(
            "Printer Error:  Traveler's check not inserted in printer.  Do you want to try again?");
        responseToContinue = theAppMgr.showOptionDlg(res.getString("Printer Error."), errMessage);
        if (responseToContinue) {
          frankTravellersCheck();
        } else {
          return false;
        }
      }
      return false;
    } catch (Exception e) {
      String errMessage = "";
      System.out.println("FRANK TravelerCheck MICR Code e -> " + e);
      System.out.println("FRANK TravelerCheck MICRError Code -> " + e.getMessage());
      errMessage = res.getString("Printer Error") + ":  " + e.getMessage() + ".";
      theAppMgr.showErrorDlg(errMessage);
      return false;
    } finally {
      System.out.println("FRANK FINALLY BEING CALLED");
      CMSPrinter.release();
    }
    return true;
  }

  /**
   * Perform the print function to back of check on the printer.
   **/
  public void frankCheck() {
    int row = tblCheck.getSelectedRow();
    Payment pay = model.getPayment(row);
    try {
      CMSPrinter.openDevice(receiptInfo.getPrinterName(), theAppMgr);
      CMSPrinter.reclaim(theAppMgr);
      CMSMICR.openDevice(receiptInfo.getMICRName(), theAppMgr);
      CMSMICR.reclaim(theAppMgr);
      posMICR.clearInput();
      posMICR.beginInsertion(5000);
      posMICR.endInsertion();
      if (pay instanceof BankCheck) {
        posMICR.setDataEventEnabled(true);
      } else {
        this.printToCheck();
      }
    } catch (JposException jpe) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "print()"
          , res.getString("Error during receipt printing."), res.getString("NA")
          , LoggingServices.MAJOR, jpe);
      String errMessage = "";
      System.out.println("FRANK READ MICR Code e -> " + jpe);
      System.out.println("FRANK READ MICRError Code -> " + jpe.getErrorCode());
      System.out.println("FRANK READ MICRExt Error Code -> " + jpe.getErrorCodeExtended());
      errMessage = res.getString("Printer Error") + ":  " + jpe.getMessage() + ".  "
          + res.getString("Do you want to try again?");
      //if ( jpe.getErrorCode() == 106 ) {
      //    System.out.println("RECEIVED ERROR CODE 106");
      //    return;
      //}
      if (jpe.getErrorCode() == 112 || jpe.getErrorCode() == 114) {
        System.out.println("RECEIVED frankCheck() error 112 or 114");
        errMessage = res.getString("Printer Error:  Check not inserted in printer.");
        theAppMgr.showErrorDlg(errMessage);
      }
      //if ( jpe.getErrorCode() == 112 ) {
      //   errMessage = "Printer Error:  Check not inserted in printer.  Do you want to try again?";
      //   responseToContinue = theAppMgr.showOptionDlg("Printer Error.", errMessage);
      //   if ( responseToContinue ) { frankCheck(); } else { return; }
      //}
      //If any other errors... just fail the MICR and let them manually endorse.   alt
      if (jpe.getErrorCode() != 112 & jpe.getErrorCode() != 114) {
        System.out.println("RECEIVED frankCheck() general jpos catch all error.");
        errMessage = res.getString("MICR Read Error.   Check must be endorsed manually.");
        theAppMgr.showErrorDlg(errMessage);
        LoggingServices.getCurrent().logMsg(getClass().getName(), "frankCheck readMICR()"
            , res.getString("Cannot complete check read for endorsement.")
            , res.getString("Make sure MICR reader is working properly."), LoggingServices.MINOR
            , jpe);
        try {
          posMICR.beginRemoval(1000);
          posMICR.endRemoval();
          posMICR.setDataEventEnabled(false);
          posMICR.clearInput();
          CMSMICR.release();
          posPrinter.clearOutput();
          CMSPrinter.release();
        } catch (JposException pe) {
          LoggingServices.getCurrent().logMsg(getClass().getName(), "frankCheck readMICR()"
              , res.getString("Cannot complete check read for endorsement.")
              , res.getString("Make sure MICR reader is working properly."), LoggingServices.MINOR
              , pe);
        }
      }
    } catch (Exception ex) {
      String errMessage = "";
      System.out.println("RECEIVED frankCheck() general catch all error.");
      errMessage = res.getString("MICR Read Error.   Check must be endorsed manually.");
      theAppMgr.showErrorDlg(errMessage);
      LoggingServices.getCurrent().logMsg(getClass().getName(), "frankCheck readMICR()"
          , res.getString("Cannot complete check read for endorsement.")
          , res.getString("Make sure MICR reader is working properly."), LoggingServices.MINOR, ex);
      try {
        posMICR.beginRemoval(1000);
        posMICR.endRemoval();
        posMICR.setDataEventEnabled(false);
        posMICR.clearInput();
        CMSMICR.release();
        posPrinter.clearOutput();
        CMSPrinter.release();
      } catch (JposException pe) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "frankCheck readMICR()"
            , res.getString("Cannot complete check read for endorsement.")
            , res.getString("Make sure MICR reader is working properly."), LoggingServices.MINOR
            , pe);
      }
    }
  }

  // Required by jpos.events.ErrorEvent
  public void dataOccurred(DataEvent event) {
    this.printToCheck();
  }

  /**
   * put your documentation comment here
   */
  private void printToCheck() {
    try {
      // Check Endorsement
      //  What to do if row is not selected
      int row = tblCheck.getSelectedRow();
      Payment pay = model.getPayment(row);
      if (pay instanceof Check) {
        String coName = "";
        String accountNumber = "";
        String stateIdCode = "";
        String approvalCode = "";
        String driversLicense = "";
        String storeId = "";
        if (pay instanceof BankCheck) {
          if (((BankCheck)pay).getStateIdCode() != null) {
            stateIdCode = ((BankCheck)pay).getStateIdCode();
          }
          if (((BankCheck)pay).getRespAuthorizationCode() != null) {
            approvalCode = ((BankCheck)pay).getRespAuthorizationCode();
          }
          if (((BankCheck)pay).getDriversLicenseNumber() != null) {
            driversLicense = ((BankCheck)pay).getDriversLicenseNumber();
          }
        }
        Store store = theTxn.getStore();
        if (null != store) {
          storeId = store.getId();
          coName = store.getCompanyNameForFranking();
          accountNumber = store.getCompanyAccountNumberForFranking();
        }
        String padCharacters = "           ";
        /*writeToFile(padIBMCharacters + coName + "\n");
         writeToFile(padIBMCharacters + res.getString("ACCT") + " # " + accountNumber + "    " + res.getString("STATE") + " " + stateIdCode + "\n");
         writeToFile(padIBMCharacters + res.getString("TRAN") + " # " + theTxn.getId() + "   " + customerPhone + "\n");
         posPrinter.printNormal(PTR_S_SLIP, padIBMCharacters + res.getString("APPR") + " # " + approvalCode + "        " + res.getString("AMT") + " " + pay.getAmount().formattedStringValue() + CMSPrinter.CRLF);
         posPrinter.printNormal(PTR_S_SLIP, padIBMCharacters + res.getString("DL") + "# " + driversLicense + "        " + res.getString("STORE") + "# " + storeId + CMSPrinter.CRLF);
         posPrinter.printNormal(PTR_S_SLIP, padIBMCharacters + res.getString("DATE") + ":  " + DateFormat.getDateInstance().format(theTxn.getProcessDate()) + CMSPrinter.CRLF);*/
        posPrinter.printNormal(PTR_S_SLIP
            , padCharacters + res.getString("FOR DEPOSIT ONLY") + CMSPrinter.CRLF);
        posPrinter.printNormal(PTR_S_SLIP, padCharacters + coName + CMSPrinter.CRLF);
        posPrinter.printNormal(PTR_S_SLIP
            , padCharacters + res.getString("ACCT") + " # " + accountNumber + "    "
            + res.getString("STATE") + " " + stateIdCode + CMSPrinter.CRLF);
        posPrinter.printNormal(PTR_S_SLIP
            , padCharacters + res.getString("TRAN") + " # " + theTxn.getId() + "   "
            + customerPhone + CMSPrinter.CRLF);
        posPrinter.printNormal(PTR_S_SLIP
            , padCharacters + res.getString("APPR") + " # " + approvalCode + "        "
            + res.getString("AMT") + " " + pay.getAmount().formattedStringValue() + CMSPrinter.CRLF);
        posPrinter.printNormal(PTR_S_SLIP
            , padCharacters + res.getString("DL") + "# " + driversLicense + "        "
            + res.getString("STORE") + "# " + storeId + CMSPrinter.CRLF);
        posPrinter.printNormal(PTR_S_SLIP
            , padCharacters + res.getString("DATE") + ":  "
            + DateFormat.getDateInstance().format(theTxn.getProcessDate()) + CMSPrinter.CRLF);
      }
      posMICR.beginRemoval(6000);
      posMICR.endRemoval();
      posMICR.setDataEventEnabled(false);
      //posMICR.clearInput();
      //posMICR.removeDataListener(this);
      updateStatus();
      //Issue # 706
      updateEndorsed();
      //if (model.isAllFranked()) {
      //   btnOK.setVisible(true);
      //}
    } catch (JposException jpe) {
      String errMessage = "";
      jpe.printStackTrace();
      System.err.println("FRANK DATA OCCURED Code e -> " + jpe);
      System.err.println("FRANK DATA OCCUREDError Code -> " + jpe.getErrorCode());
      System.err.println("FRANK DATA OCCUREDExt Error Code -> " + jpe.getErrorCodeExtended());
      errMessage = res.getString("Printer Error") + ":  " + jpe.getMessage() + ".   "
          + res.getString("Do you want to try again?");
      if (jpe.getErrorCode() == 106) {
        System.out.println("DATA EVENT RECEIVED ERROR CODE 106");
        return;
      }
      LoggingServices.getCurrent().logMsg("com.chelseasystems.common.swing.dlg.FrankDlg"
          , "dataOccured()", res.getString("Error during receipt printing."), res.getString("NA")
          , LoggingServices.MAJOR, jpe);
    } catch (Exception e) {
      System.err.println("FRANK DATA OCCURED REGULAR EXCEPTION e -> " + e);
    }
    //finally {
    //   System.out.println("FRANK FINALLY BEING CALLED");
    //   CMSPrinter.release();
    //   posMICR.removeDataListener(this);
    //   CMSMICR.release();
    //}
  }

  /************************************************************************/
  public class Endorse {
    public String sStatus = "";
    public Payment pay;

    /**
     * @param       Payment pay
     */
    public Endorse(Payment pay) {
      this.pay = pay;
    }

    /**
     */
    public void setEndorsed() {
      sStatus = "Endorsed";
    }

    public String endorsed = "N";

    /**
     * put your documentation comment here
     * @return
     */
    public String getEndorsed() {
      return this.endorsed;
    }

    /**
     * put your documentation comment here
     * @param endorsed
     */
    public void setEndorsed(String endorsed) {
      this.endorsed = endorsed;
    }
  }


  /************************************************************************/
  private class FrankBtnListener implements ActionListener {

    /**
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
      btnFrank_actionPerformed(e);
    }
  }


  /************************************************************************/
  private class FrankModel extends ScrollableTableModel {

    /**
     */
    public FrankModel() {
      this.setColumnIdentifiers(new String[] {res.getString("Document Type")
          , res.getString("Document Number"), res.getString("Amount"), res.getString("Endorsed")
      });
    }

    /**
     * @param endorse
     */
    public void addEndorse(FrankDlg.Endorse endorse) {
      addRow(endorse);
    }

    /**
     * @param row
     * @return
     */
    public Payment getPayment(int row) {
      FrankDlg.Endorse endorse = (FrankDlg.Endorse)this.getRowInPage(row);
      return (endorse.pay);
    }

    /**
     * @param row
     * @return
     */
    public FrankDlg.Endorse getEndorse(int row) {
      return (FrankDlg.Endorse)this.getRowInPage(row);
    }

    /**
     * @return
     */
    public int getColumnCount() {
      return (4);
    }

    /**
     * @return
     */
    public boolean isAllFranked() {
      for (Enumeration enm = this.getAllRows().elements(); enm.hasMoreElements(); ) {
        FrankDlg.Endorse endorse = (FrankDlg.Endorse)enm.nextElement();
        if (endorse.sStatus.length() == 0) {
          return (false);
        }
      }
      return (true);
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
      FrankDlg.Endorse endorse = (FrankDlg.Endorse)vTemp.elementAt(row);
      Payment pmt = endorse.pay;
      switch (column) {
        case 0:
          return (pmt.getGUIPaymentName());
        case 1:
          if (pmt instanceof BankCheck)
            return ((BankCheck)pmt).getCheckNumber();
          else if (pmt instanceof MallCert)
            return ((MallCert)pmt).getType();
          else
            return ("");
        case 2:
          return (pmt.getAmount().formattedStringValue());
        case 3:
          return (endorse.getEndorsed());
        default:
          return (" ");
      }
    }

    /**
     * @param table
     */
    public void setColumnWidth(JTable table) {
      table.getColumnModel().getColumn(1).setPreferredWidth(175);
      table.getColumnModel().getColumn(2).setPreferredWidth(175);
      table.getColumnModel().getColumn(3).setPreferredWidth(175);
      table.getColumnModel().getColumn(0).setPreferredWidth(table.getWidth()
          - (table.getColumnModel().getColumn(1).getPreferredWidth()
          + table.getColumnModel().getColumn(2).getPreferredWidth()
          + table.getColumnModel().getColumn(3).getPreferredWidth()));
    }
  }


  /**
   * put your documentation comment here
   * @param aString
   */
  private void writeToFile(String aString) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter("frank.txt", true));
      out.write(aString);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private String getFrankableString() {
    //  What to do if row is not selected
    String padCharacters = "           ";
    int row = tblCheck.getSelectedRow();
    Payment pay = model.getPayment(row);
    StringBuffer lineBuf = new StringBuffer();
    lineBuf.append(padCharacters + res.getString("FOR DEPOSIT ONLY"));
    lineBuf.append(CMSPrinter.CRLF);
    lineBuf.append(padCharacters + theTxn.getStore().getCompanyNameForFranking());
    lineBuf.append(CMSPrinter.CRLF);
    lineBuf.append(CMSPrinter.CRLF);
    lineBuf.append(padCharacters + res.getString("ACCT # "));
    lineBuf.append(theTxn.getStore().getCompanyAccountNumberForFranking());
    lineBuf.append(CMSPrinter.CRLF);
    lineBuf.append(padCharacters + res.getString("TRAN # "));
    lineBuf.append(theTxn.getId());
    lineBuf.append(CMSPrinter.CRLF);
    lineBuf.append(padCharacters + res.getString("AMT "));
    lineBuf.append(pay.getAmount().formattedStringValue());
    lineBuf.append(CMSPrinter.CRLF);
    lineBuf.append(padCharacters + res.getString("Store # "));
    lineBuf.append(theTxn.getStore().getId());
    lineBuf.append(CMSPrinter.CRLF);
    Date txnDate = theTxn.getCreateDate();
    lineBuf.append(padCharacters + res.getString("DATE "));
    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
    String createDate = formatter.format(txnDate);
    lineBuf.append(createDate);
    lineBuf.append(CMSPrinter.CRLF);
    return lineBuf.toString();
  }
}

