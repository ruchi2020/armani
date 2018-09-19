/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.pos;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.rmi.Remote;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.park.IParkRmiServer;
import com.chelseasystems.cr.park.Park;
import com.chelseasystems.cr.park.ParkFileServices;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.Transaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cr.util.ResourceBundleKey;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.pos.CMSParkTransactionAppModel;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.paidout.CMSMiscPaidOut;

/**
 */
public class ParkApplet extends CMSApplet {
  //public class ParkApplet extends JApplet {
  private static final SimpleDateFormat fmtDate = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateTimeFormat();
  private static final int DATE = 0;
  private static final int TYPE = 1;
  private static final int OPR = 2;
  private static final int COMMENTS = 3;

  /* hack for detecting double click */
  private boolean clickInProgress = false;
  ParkModel model;
  JCMSTable tblList;

  //Construct the applet
  public ParkApplet() {
  }

  //Initialize the applet
  public void init() {
    try {
      model = new ParkModel();
      tblList = new JCMSTable(model, JCMSTable.SELECT_ROW);
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit()
      throws Exception {
    JPanel pnlNorth = new JPanel();
    JPanel pnlWest = new JPanel();
    JPanel pnlSouth = new JPanel();
    JPanel pnlEast = new JPanel();
    JPanel pnlCenter = new JPanel();
    this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
    this.getContentPane().add(pnlWest, BorderLayout.WEST);
    this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
    this.getContentPane().add(pnlEast, BorderLayout.EAST);
    this.getContentPane().add(pnlCenter, BorderLayout.CENTER);
    pnlCenter.setLayout(new BorderLayout());
    pnlNorth.setBackground(theAppMgr.getBackgroundColor());
    pnlWest.setBackground(theAppMgr.getBackgroundColor());
    pnlNorth.setPreferredSize(new Dimension(10, (int)(40 * r)));
    pnlSouth.setPreferredSize(new Dimension(10, (int)(30 * r)));
    pnlSouth.setBackground(theAppMgr.getBackgroundColor());
    pnlEast.setBackground(theAppMgr.getBackgroundColor());
    pnlCenter.add(tblList, BorderLayout.CENTER);
    pnlCenter.add(tblList.getTableHeader(), BorderLayout.NORTH);
    tblList.setAppMgr(theAppMgr);
    tblList.addMouseListener(new java.awt.event.MouseAdapter() {

      /**
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        clickEvent(e);
      }
    });
    tblList.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        resizeColumnWidths();
      }
    });
  }

  //Start the applet
  public void start() {
    clickInProgress = false;
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    model.clear();
    theAppMgr.showMenu(MenuConst.RECALL_SUS_TRANS, theOpr);
    theAppMgr.setSingleEditArea(res.getString("Select suspended transaction."));
    loadTransactionsInBackground();
    theAppMgr.setEditAreaFocus();
  }

  //Stop the applet
  public void stop() {}

  /**
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    theAppMgr.unRegisterSingleEditArea(); // cancel any builders in progress
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("DELETE_ALL_SUSPENDED_TRANSACTIONS")) {
      if (model.getTotalRowCount() > 0) {
        if (theAppMgr.showOptionDlg(res.getString("Delete All?")
            , res.getString("Are you sure you want to delete ALL of the suspended transactions for this store?"))) {
          this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          Vector parks = model.getAllRows();
          for (Iterator it = parks.iterator(); it.hasNext(); ) {
            Park park = (Park)it.next();
            Object txn = getParkedTransaction(park);
                /* Commented as it is setting the last transaction as TXN_POS
                         if(txn instanceof CompositePOSTransaction)
                         {
                         CompositePOSTransaction theTxn = (CompositePOSTransaction)txn;
                         if(theTxn != null)
                         {
                         try
                         {
                         theTxn.setTheOperator((CMSEmployee)theOpr);
                         theAppMgr.addStateObject("TXN_POS", theTxn);
                         }
                         catch(BusinessRuleException ex)
                         {
                         theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
                         }
                         }
                         }
            */

          }
          this.setCursor(Cursor.getDefaultCursor());
          theAppMgr.fireButtonEvent("CANCEL");
        }
      } else {
        theAppMgr.showErrorDlg("There are no suspended transactions to delete.");
      }
    }
  }

  /**
   * Method loads parked transactions into the applet model. Method will first
   * get local serialized txns, then retrieve others from peers. The local txns
   * are retreived because when off-line, the host will not see them.
   */
  private void loadTransactionsInBackground() {
    new Thread() {

      /**
       * put your documentation comment here
       */
      public void run() {
        Park[] pks = null;
        theAppMgr.setWorkInProgress(true);
        try {
          // get serialized txns
          ParkFileServices parkServices = new ParkFileServices();
          pks = parkServices.recall();
          for (int i = 0; i < pks.length; i++)
            model.addPark(pks[i]);
          // get remotes
          IParkRmiServer parkServer = null;
          Remote[] rmts = theAppMgr.getPeerStubs("park");
          if (rmts == null)
            return;
          // display non-modal dialog
          double size = rmts.length;
          for (int x = 0; x < size; x++) {
            try {
              parkServer = (IParkRmiServer)rmts[x];
              pks = parkServer.recall();
              if (pks == null)
                continue;
              for (int y = 0; y < pks.length; y++) {
                model.addPark(pks[y]);
              }
            } catch (Exception e) {
              theAppMgr.removeRemotePeerStub("park", parkServer);
            }
          }
        } catch (Exception ex) {
          theAppMgr.showExceptionDlg(ex);
        } finally {
          theAppMgr.setWorkInProgress(false);
          tblList.repaint();
        }
      }
    }.start();
  }

  //
  public String getScreenName() {
    return (res.getString("Suspended Transactions"));
  }

  //
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * @param e
   */
  public void clickEvent(MouseEvent me) {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return;
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    if (!clickInProgress) {
      clickInProgress = true;
      Park park = (Park)model.getRowInPage(row);
      Object tempTxn = getParkedTransaction(park);
      if (tempTxn == null) {
        theAppMgr.showErrorDlg(res.getString(
            "Unable to recall this transaction.  Select another transaction."));
        model.removeRowInPage(row);
        loadTransactionsInBackground();
      } else {
        printReceipt(tempTxn);
        System.out.println("tempClass " + tempTxn.getClass());
        if (tempTxn instanceof CompositePOSTransaction) {
          CompositePOSTransaction theTxn = (CompositePOSTransaction)tempTxn;
          if (theTxn != null) {
            try {
              theTxn.setTheOperator((CMSEmployee)theOpr);
              theAppMgr.addStateObject("TXN_POS", theTxn);
              theAppMgr.fireButtonEvent("OK");
            } catch (BusinessRuleException ex) {
              theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
            }
          } else {
            theAppMgr.showErrorDlg(res.getString(
                "Unable to recall this transaction.  Select another transaction."));
          }
        } else if (tempTxn instanceof CMSMiscCollection){
          CMSMiscCollection miscColTxn = (CMSMiscCollection)tempTxn;
          if (miscColTxn != null){
            try {
              miscColTxn.setTheOperator((CMSEmployee)theOpr);
              theAppMgr.addStateObject("TXN_POS", miscColTxn);
              theAppMgr.fireButtonEvent("PAYMENT_APPLET");
            }
            catch (BusinessRuleException ex) {
              theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
            }
          }
        } else if (tempTxn instanceof CMSMiscPaidOut){
          CMSMiscPaidOut miscPaidOut = (CMSMiscPaidOut)tempTxn;
          if (miscPaidOut != null){
            try {
              miscPaidOut.setTheOperator((CMSEmployee)theOpr);
              theAppMgr.addStateObject("TXN_POS", miscPaidOut);
              theAppMgr.fireButtonEvent("PAYMENT_APPLET");
            }
            catch (BusinessRuleException ex) {
              theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
            }
          }

        }

      }
      this.setCursor(Cursor.getDefaultCursor());
    }
  }

  /**
   * put your documentation comment here
   * @param txn
   */
  private void printReceipt(Object txn) {
    if (txn instanceof Transaction) {
      CMSParkTransactionAppModel appModel = new CMSParkTransactionAppModel(new ResourceBundleKey(
          "Recall Parked Transaction"), (CMSEmployee)theOpr, (Transaction)txn);
      appModel.print(theAppMgr, ReceiptBlueprintInventory.CMSRecallParkedTxn);
    }
  }

  /**
   * @param park
   * @return
   */
  private Transaction getParkedTransaction(Park park) {
    try {
      // try locally
      Transaction theTxn = null;
      ParkFileServices parkServices = new ParkFileServices();
      theTxn = parkServices.recall(park.getId());
      // try remotely
      if (theTxn == null) {
        System.out.println("Suspended trans is null locally , find remotely");
        Remote[] rmts = theAppMgr.getPeerStubs("park");
        for (int x = 0; x < rmts.length; x++) {
          try {
            IParkRmiServer rmi = (IParkRmiServer)rmts[x];
            Park[] allParks = rmi.recall();
            theTxn = rmi.recall(park.getId());
            if (theTxn != null) //found transaction, stop the search
              break;
          } catch (Exception ex) {
            System.out.println("Exception in ParkApplet getTransactions()");
            //theAppMgr.showExceptionDlg(ex);
          }
        }
      } //return local or remote transaction
      return (theTxn);
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
      return (null);
    }
  }

  /**
   * @param e
   */
  public void resizeColumnWidths() {
    tblList.getColumnModel().getColumn(DATE).setPreferredWidth((int)(150 * r));
    tblList.getColumnModel().getColumn(TYPE).setPreferredWidth((int)(100 * r));
    tblList.getColumnModel().getColumn(OPR).setPreferredWidth((int)(100 * r));
    tblList.getColumnModel().getColumn(COMMENTS).setPreferredWidth(tblList.getWidth()
        - (tblList.getColumnModel().getColumn(DATE).getPreferredWidth()
        + tblList.getColumnModel().getColumn(TYPE).getPreferredWidth()
        + tblList.getColumnModel().getColumn(OPR).getPreferredWidth()));
    DefaultTableCellRenderer CntrAlignRenderer = new DefaultTableCellRenderer();
    CntrAlignRenderer.setHorizontalAlignment(0);
    tblList.getColumnModel().getColumn(TYPE).setCellRenderer(CntrAlignRenderer);
    model.setRowsShown(tblList.getHeight() / tblList.getRowHeight());
  }

  /**
   * callback when <b>Page Down</b> icon is clicked
   */
  public void pageDown(MouseEvent e) {
    model.nextPage();
    theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
  }

  /**
   * callback when <b>Page Up</b> icon is clicked
   */
  public void pageUp(MouseEvent e) {
    model.prevPage();
    theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
  }

  /*******************************************************************/
  private class ParkModel extends ScrollableTableModel {
    private INIFile file;
    private HashMap tranCode;

    /**
     */
    public ParkModel() {
      super(new String[] {res.getString("Suspend Date/Time"), res.getString("Trans Type")
          , res.getString("Operator"), res.getString("Comments")
      });
      try {
        file = new INIFile(FileMgr.getLocalFile("config", "pos.cfg"), true);
        tranCode = new HashMap();
        String transTypes = file.getValue("TRANS_TYPES");
        StringTokenizer st = new StringTokenizer(transTypes, ",");
        while (st.hasMoreElements()) {
          String key = (String)st.nextElement();
          String transTypeCode = file.getValue(key, "CODE", "");
          String transTypeShortDesc = file.getValue(key, "SHORT_DESC", "");
          tranCode.put(transTypeCode, transTypeShortDesc);
        }
      } catch (Exception e) {
        CMSApplet.theAppMgr.showExceptionDlg(e);
      }
    }

    /**
     * Method to add a parked txn to the model and eliminate duplicates.
     */
    public void addPark(Park park) {
      if (park == null)
        return;
      Vector parks = this.getAllRows();
      for (java.util.Enumeration enm = parks.elements(); enm.hasMoreElements(); ) {
        Park existing = (Park)enm.nextElement();
        if (existing.getId().equals(park.getId()))
          return;
      }
      super.addRow(park);
    }

    /**
     * @param row
     */
    public void addRow(Object row) {
      this.addPark((Park)row);
    }

    /**
     * @param row
     * @param column
     * @return
     */
    public Object getValueAt(int row, int column) {
      Vector vTemp = this.getCurrentPage();
      if (vTemp == null || row >= vTemp.size())
        return ("");
      Park park = (Park)vTemp.elementAt(row);
      switch (column) {
        case DATE:
          return (fmtDate.format(park.getParkDate()));
        case TYPE:
          return (park.getTransactionType().length() == 0
              || park.getTransactionType().equals("PEND") ? "PENDING"
              : tranCode.get(park.getTransactionType()));
        case OPR:
          return (park.getOperatorName());
        case COMMENTS:
          return (park.getComment());
        default:
          return (" ");
      }
    }
  }
}

