/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.report.Reportable;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.mask.CMSMaskConstants;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.ga.cs.pos.POSReportHelper;
import com.ga.cs.pos.POSReportHelperInterface;
import com.ga.cs.swing.model.BaseReportModel;
import com.ga.cs.swing.table.cellrenderer.MultipleRowCellRenderer;


/**
 * Abstact class that provides standardized default implementations of CMSApplet
 * methods used in GA reports
 *
 * @author fbulah
 *
 */
public abstract class BaseReportApplet extends CMSApplet implements Reportable {
  protected BaseReportModel model = new BaseReportModel();
  protected JCMSLabel lblTitle = new JCMSLabel();
  protected JCMSTable tbl;
  protected Date startDate;
  protected String TITLE;
  protected MultipleRowCellRenderer multiRowCellRenderer;
  protected POSReportHelperInterface posReportHelper = POSReportHelper.getInstance();
  protected static final int SCROLL_VIEWPORT_WIDTH_DEFAULT = 500;
  protected static final int SCROLL_VIEWPORT_HEIGHT_DEFAULT = 500;
  protected int viewPortWidth = SCROLL_VIEWPORT_WIDTH_DEFAULT;
  protected int viewPortHeight = SCROLL_VIEWPORT_HEIGHT_DEFAULT;
  protected JScrollPane scrollPane;

  /**
   * put your documentation comment here
   */
  public BaseReportApplet() {
  }

  /**
   * put your documentation comment here
   */
  public void init() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  protected void jbInit()
      throws Exception {
    JPanel pnlCntr = new JPanel();
    JPanel pnlWest = new JPanel();
    JPanel pnlEast = new JPanel();
    JPanel pnlNorth = new JPanel();
    JPanel pnlSouth = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    pnlCntr.setLayout(borderLayout1);
    pnlCntr.setBackground(theAppMgr.getBackgroundColor());
    this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
    this.getContentPane().add(pnlWest, BorderLayout.WEST);
    this.getContentPane().add(pnlEast, BorderLayout.EAST);
    this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
    this.getContentPane().add(pnlCntr, BorderLayout.CENTER);
    scrollPane = new JScrollPane(tbl);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    tbl.setPreferredScrollableViewportSize(new Dimension(1000, 1000));
    pnlCntr.add(scrollPane, BorderLayout.CENTER);
    pnlNorth.add(lblTitle, null);
    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
    lblTitle.setText(res.getString(getTitle()) + " " + theStore.getId());
    lblTitle.setFont(theAppMgr.getTheme().getHeaderFont());
    pnlNorth.setBackground(theAppMgr.getBackgroundColor());
    pnlSouth.setBackground(theAppMgr.getBackgroundColor());
    pnlEast.setBackground(theAppMgr.getBackgroundColor());
    pnlWest.setBackground(theAppMgr.getBackgroundColor());
    tbl.setAppMgr(theAppMgr);
    lblTitle.setPreferredSize(new Dimension((int)(450 * r), (int)(25 * r)));
    pnlNorth.setPreferredSize(new Dimension((int)(10 * r), (int)(35 * r)));
    pnlSouth.setPreferredSize(new Dimension((int)(10 * r), (int)(20 * r)));
    pnlEast.setPreferredSize(new Dimension((int)(20 * r), (int)(10 * r)));
    pnlWest.setPreferredSize(new Dimension((int)(20 * r), (int)(10 * r)));
    pnlCntr.setPreferredSize(new Dimension((int)(20 * r), (int)(10 * r)));
    tbl.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        tbl_componentResized(e);
      }
    });
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  protected void jbInitNOSCROLL()
      throws Exception {
    JPanel pnlCntr = new JPanel();
    JPanel pnlWest = new JPanel();
    JPanel pnlEast = new JPanel();
    JPanel pnlNorth = new JPanel();
    JPanel pnlSouth = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    pnlCntr.setLayout(borderLayout1);
    pnlCntr.setBackground(theAppMgr.getBackgroundColor());
    this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
    this.getContentPane().add(pnlWest, BorderLayout.WEST);
    this.getContentPane().add(pnlEast, BorderLayout.EAST);
    this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
    this.getContentPane().add(pnlCntr, BorderLayout.CENTER);
    pnlCntr.add(tbl, BorderLayout.CENTER);
    pnlCntr.add(tbl.getTableHeader(), BorderLayout.NORTH);
    pnlNorth.add(lblTitle, null);
    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
    lblTitle.setText(res.getString(getTitle()) + " " + theStore.getId());
    lblTitle.setFont(theAppMgr.getTheme().getHeaderFont());
    pnlNorth.setBackground(theAppMgr.getBackgroundColor());
    pnlSouth.setBackground(theAppMgr.getBackgroundColor());
    pnlEast.setBackground(theAppMgr.getBackgroundColor());
    pnlWest.setBackground(theAppMgr.getBackgroundColor());
    tbl.setAppMgr(theAppMgr);
    lblTitle.setPreferredSize(new Dimension((int)(450 * r), (int)(25 * r)));
    pnlNorth.setPreferredSize(new Dimension((int)(10 * r), (int)(35 * r)));
    pnlSouth.setPreferredSize(new Dimension((int)(10 * r), (int)(20 * r)));
    pnlEast.setPreferredSize(new Dimension((int)(20 * r), (int)(10 * r)));
    pnlWest.setPreferredSize(new Dimension((int)(20 * r), (int)(10 * r)));
    pnlCntr.setPreferredSize(new Dimension((int)(20 * r), (int)(10 * r)));
    tbl.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        tbl_componentResized(e);
      }
    });
  }

  /**
   * @return Returns the Title.
   */
  public String getTitle() {
    return TITLE;
  }

  /**
   * @param Title
   *            The Title to set.
   */
  public void setTitle(String title) {
    TITLE = title;
  }

  /**
   * Resizing of the content pane
   */
  void tbl_componentResized(ComponentEvent e) {
    //	  	getModel().setRowsShown(tbl.getHeight()/tbl.getRowHeight());
    tbl.repaint();
  }

  //Start the applet
  public void start() {
    getModel().clear();
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    theStore = (CMSStore)theAppMgr.getGlobalObject("STORE");
    theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr, theAppMgr.CANCEL_BUTTON);
    lblTitle.setText(res.getString(getTitle()) + " " + theStore.getId());
    theAppMgr.setSingleEditArea(res.getString(
        "Enter start date. (MM/DD/YYYY)  or  \"C\" for Calendar."), "STARTDATE", new Date()
        , CMSMaskConstants.CALENDAR_MASK);
  }

  /**
   * put your documentation comment here
   * @param command
   * @param date
   */
  public void editAreaEvent(String command, Date date) {
    if (command.equals("STARTDATE")) {
      startDate = date;
      theAppMgr.setSingleEditArea(res.getString(
          "Enter end date. (MM/DD/YYYY)  or  \"C\" for Calendar."), "ENDDATE", new Date()
          , CMSMaskConstants.CALENDAR_MASK);
    }
    if (command.equals("ENDDATE")) {
      if (date.compareTo(startDate) >= 0) {
        populateScreen(startDate, date);
        theAppMgr.setSingleEditArea(res.getString(
            "Enter start date. (MM/DD/YYYY)  or  \"C\" for Calendar."), "STARTDATE", new Date()
            , CMSMaskConstants.CALENDAR_MASK);
      } else { // end date is less than start date; request re-entry
        theAppMgr.setSingleEditArea(res.getString("End date is earlier than start date. Please re-enter end date. (MM/DD/YYYY)  or  \"C\" for Calendar.")
            , "ENDDATE", new Date(), CMSMaskConstants.CALENDAR_MASK);
      }
    }
  }

  /**
   * method to populate the scrren
   */
  abstract public void populateScreen(Date startDate, Date endDate);

  /**
   * put your documentation comment here
   * @return
   */
  public BaseReportModel getModel() {
    return model;
  }

  /**
   * @param model
   *            The model to set.
   */
  public void setModel(BaseReportModel model) {
    this.model = model;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.chelseasystems.cr.swing.CMSApplet#stop()
   */
  public void stop() {}

  /**
   * @return Returns the viewPortHeight.
   */
  public int getViewPortHeight() {
    return viewPortHeight;
  }

  /**
   * @param viewPortHeight
   *            The viewPortHeight to set.
   */
  public void setViewPortHeight(int viewPortHeight) {
    this.viewPortHeight = viewPortHeight;
  }

  /**
   * @return Returns the viewPortWidth.
   */
  public int getViewPortWidth() {
    return viewPortWidth;
  }

  /**
   * @param viewPortWidth
   *            The viewPortWidth to set.
   */
  public void setViewPortWidth(int viewPortWidth) {
    this.viewPortWidth = viewPortWidth;
  }

  /**
   * put your documentation comment here
   * @param size
   */
  public void setScrollableViewSize(Dimension size) {
    scrollPane.getViewport().getView().setSize(size);
  }

  /**
   * put your documentation comment here
   * @param size
   */
  public void setTableSize(Dimension size) {
    tbl.setPreferredScrollableViewportSize(size);
  }

  //Methods for printing to printer
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("PRINT")) {
      if (model.getRowCount() == 0) {
        theAppMgr.showErrorDlg(res.getString("There is no data to print."));
        return;
      }
      ArmaniReportPrinter reportPrinter = new ArmaniReportPrinter(this);
      reportPrinter.setLandscape();
      reportPrinter.print();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[][] getReportElements() {
    int width = model.getColumnCount();
    int length = model.getRowCount();
    String[][] reportElements = new String[length][width];
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
        reportElements[i][j] = model.getValueAt(i, j).toString();
      }
    }
    return reportElements;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double[] getReportColumnWidths() {
    double[] reportColumnWidths = new double[this.model.getColumnCount()];
    for (int i = 0; i < reportColumnWidths.length; i++) {
      reportColumnWidths[i] = 1.0d / reportColumnWidths.length;
    }
    return reportColumnWidths;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[] getReportHeadings() {
    int columnCount = this.model.getColumnCount();
    String[] columns = new String[columnCount];
    for (int i = 0; i < columnCount; i++) {
      columns[i] = this.model.getColumnName(i);
    }
    return columns;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[] getReportTotals() {
    return null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getReportTitle() {
    return model.getFullTitle();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int[] getReportColumnAlignments() {
    return null; //All left justifies
  }

  //=====================================================================================
  //
  //  testing methods
  //
  //=====================================================================================
  public void setAppMgr(IApplicationManager appMgr) {
    theAppMgr = appMgr;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ResourceBundle getResourceBundle() {
    //System.out.println("getResourceBundle: returning res=" + res);
    return (res);
  }

  /**
   * put your documentation comment here
   * @param resourceBundle
   */
  public void setResourceBundle(ResourceBundle resourceBundle) {
    //System.out.println("setResourceBundle: setting res=" + resourceBundle);
    res = resourceBundle;
  }

  /**
   * Sets the POS report helper interface.  This is used for testing only
   * @param posReportHelper The posReportHelper to set.
   */
  public void setPosReportHelper(POSReportHelperInterface posReportHelper) {
    this.posReportHelper = posReportHelper;
  }

  /**
   * put your documentation comment here
   * @param args
   */
  public static void main(String[] args) {}
}

