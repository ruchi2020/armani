/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.timecard;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.text.NumberFormat;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.store.CMSStore;
//import com.chelseasystems.cs.swing.model.TimecardModel;
import com.chelseasystems.cs.txnposter.*;
import java.awt.*;
import javax.swing.*;
import com.chelseasystems.cr.timecard.*;
import com.chelseasystems.cs.timecard.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.rules.*;
import java.io.*;
import com.chelseasystems.rb.ReceiptFactory;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cs.receipt.*;
import com.chelseasystems.cr.swing.bean.*;
import java.util.*;
import java.text.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.logging.*;


/**
 */
public class TimecardApplet extends CMSApplet {
  //public class TimecardApplet extends JPanel{
  private int actionPerformed; // should track whether timecard has changed.
  private CMSEmployeeTimecard timecard;
  private CMSEmployeeTimecard currentTimecard;
  private SimpleDateFormat fm = new SimpleDateFormat(res.getString("hh/mm/ss/SSS"));
  JCMSTextField VacHrsFld = new JCMSTextField();
  JCMSTextField SickHrsFld = new JCMSTextField();
  JCMSTextField NameTxtFld = new JCMSTextField();
  JCMSTextField TotHrsFld = new JCMSTextField();
  JCMSTextField floatHrsFld = new JCMSTextField();
  JCMSTextField asOfFld = new JCMSTextField();
  JCMSWrapLabel edtStatus = new JCMSWrapLabel();
  //TimecardModel model = new TimecardModel();
  ScrollableTableModel model = new ScrollableTableModel(new String[] {res.getString("HDR_Store")
      , res.getString("Clock In"), res.getString("Adjust Reason In"), res.getString("Clock Out")
      , res.getString("Adjust Reason Out"), res.getString("Total Hours")
  });
  JCMSTable tblList = new JCMSTable(model, JCMSTable.VIEW_ROW);
  JCMSTextField OvertimeHrsFld = new JCMSTextField();
  SimpleDateFormat logFmt = new SimpleDateFormat(res.getString("MM/dd/yy hh:mm:ss"));
  CMSEmployee theOpr = (CMSEmployee)super.theOpr;
  CMSStore theStore = (CMSStore)super.theStore;

  //Default constructor
  public TimecardApplet() {
  }

  //Initialize the applet
  public void init() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Screen components' initialization
  private void jbInit()
      throws Exception {
    JPanel jPanel1 = new JPanel();
    JPanel pnlSouth = new JPanel();
    JPanel pnlCenter = new JPanel();
    JPanel pnlNorth = new JPanel();
    JPanel pnlNW = new JPanel();
    JPanel pnlNE = new JPanel();
    JCMSLabel TotHrsLbl = new JCMSLabel();
    JCMSLabel LocationLbl = new JCMSLabel();
    JCMSLabel empNameLbl = new JCMSLabel();
    TotHrsLbl.setText(res.getString("Current Hours Worked"));
    JCMSLabel VacHrslbl = new JCMSLabel();
    VacHrslbl.setText(res.getString("Available Vacation Hours"));
    JCMSLabel floatHrslbl = new JCMSLabel();
    floatHrslbl.setText(res.getString("Available Floating Hours"));
    JCMSLabel asOfLbl = new JCMSLabel("");
    JCMSLabel SickHrslbl = new JCMSLabel();
    SickHrslbl.setText(res.getString("Available Sick Hours"));
    jPanel1.setOpaque(false);
    JCMSLabel OvertimeHrsLbl = new JCMSLabel();
    OvertimeHrsLbl.setText(res.getString("Overtime Hours"));
    OvertimeHrsFld.setEnabled(false);
    OvertimeHrsFld.setBackground(theAppMgr.getBackgroundColor());
    pnlNE.setLayout(new GridBagLayout());
    empNameLbl.setText(res.getString("Employee Name") + ":");
    asOfLbl.setText(res.getString("Hours Available as of"));
    pnlNW.setLayout(new GridBagLayout());
    this.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
    this.setBackground(theAppMgr.getBackgroundColor());
    this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
    this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
    this.getContentPane().add(pnlCenter, BorderLayout.CENTER);
    pnlCenter.setLayout(new BorderLayout());
    pnlCenter.add(tblList, BorderLayout.CENTER);
    pnlCenter.add(tblList.getTableHeader(), BorderLayout.NORTH);
    pnlSouth.setBackground(theAppMgr.getBackgroundColor());
    pnlSouth.add(LocationLbl);
    pnlSouth.add(edtStatus);
    pnlNorth.setBackground(theAppMgr.getBackgroundColor());
    pnlNorth.setLayout(new BorderLayout());
    pnlNorth.add(pnlNW, BorderLayout.CENTER);
    //pnlNW.add(jPanel1, null);
    pnlNW.add(empNameLbl
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    pnlNW.setOpaque(false);
    pnlNW.add(NameTxtFld
        , new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 1.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 3));
    pnlNorth.add(pnlNE, BorderLayout.EAST);
    pnlNE.add(VacHrslbl
        , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 10, 0, 0), (int)(75 * r), 0));
    pnlNE.add(VacHrsFld
        , new GridBagConstraints(1, 1, GridBagConstraints.REMAINDER, 1, 1.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 0, 3));
    pnlNE.add(TotHrsLbl
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 5, 0, 0), (int)(75 * r), 0));
    pnlNE.add(OvertimeHrsLbl
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
    pnlNE.add(TotHrsFld
        , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 3));
    pnlNE.add(OvertimeHrsFld
        , new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 3));
    pnlNE.add(floatHrslbl
        , new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 0, 0));
    pnlNE.add(floatHrsFld
        , new GridBagConstraints(1, 3, GridBagConstraints.REMAINDER, 1, 1.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 0, 3));
    pnlNE.add(SickHrslbl
        , new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 0, 0));
    pnlNE.add(SickHrsFld
        , new GridBagConstraints(1, 5, GridBagConstraints.REMAINDER, 1, 1.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 0, 3));
    pnlNE.add(asOfLbl
        , new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 0, 0));
    pnlNE.add(asOfFld
        , new GridBagConstraints(1, 7, GridBagConstraints.REMAINDER, 1, 1.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 0, 3));
    pnlNE.setBorder(BorderFactory.createTitledBorder(res.getString("SUMMARY")));
    pnlNE.setBackground(theAppMgr.getBackgroundColor());
    edtStatus.setOpaque(false);
    LocationLbl.setVerticalAlignment(SwingConstants.TOP);
    tblList.setAppMgr(theAppMgr);
    tblList.addComponentListener(new ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        tblList_componentResized(e);
      }
    });
    VacHrsFld.setBackground(theAppMgr.getBackgroundColor());
    floatHrsFld.setBackground(theAppMgr.getBackgroundColor());
    asOfFld.setBackground(theAppMgr.getBackgroundColor());
    SickHrsFld.setBackground(theAppMgr.getBackgroundColor());
    OvertimeHrsFld.setBackground(theAppMgr.getBackgroundColor());
    NameTxtFld.setBackground(theAppMgr.getBackgroundColor());
    TotHrsFld.setBackground(theAppMgr.getBackgroundColor());
    VacHrsFld.setEnabled(false);
    floatHrsFld.setEnabled(false);
    asOfFld.setEnabled(false);
    SickHrsFld.setEnabled(false);
    OvertimeHrsFld.setEnabled(false);
    NameTxtFld.setEnabled(false);
    TotHrsFld.setEnabled(false);
    ClockInOutRowRenderer renderer = new ClockInOutRowRenderer();
    TextAreaClockInOutRowRenderer taClockRenderer = new TextAreaClockInOutRowRenderer();
    tblList.getColumn(tblList.getColumnName(0)).setCellRenderer(renderer);
    tblList.getColumn(tblList.getColumnName(1)).setCellRenderer(renderer);
    tblList.getColumnModel().getColumn(2).setCellRenderer(taClockRenderer);
    tblList.getColumn(tblList.getColumnName(3)).setCellRenderer(renderer);
    tblList.getColumnModel().getColumn(4).setCellRenderer(taClockRenderer);
    tblList.getColumn(tblList.getColumnName(5)).setCellRenderer(renderer);
  }

  //Start the applet
  public void start() {
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    actionPerformed = theAppMgr.PREV_BUTTON;
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    LoggingServices.getCurrent().logMsg(new LoggingInfo("TimecardApplet", "start()"
        , "applet started, there should be clock log after this message with emp: "
        + theOpr.getShortName(), "N/A", LoggingServices.INFO));
    NameTxtFld.setText(" " + theOpr.getFirstName() + " " + theOpr.getLastName());
    if (theAppMgr.isOnLine())
      retrieveTimecard();
    else
      createOfflineTimecard();
    displayInfo();
  }

  //Stop the applet
  public void stop() {
    LoggingServices.getCurrent().logMsg(new LoggingInfo("TimecardApplet", "stop()"
        , "applet stopped, there should be clock log before this message with emp: "
        + theOpr.getShortName(), "N/A", LoggingServices.INFO));
  }

  //Get screen name
  public String getScreenName() {
    return (res.getString("Employee Timecard"));
  }

  //Get screen name
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * This method retreives the operator's current timecard as of this weekend.
   */
  private void retrieveTimecard() {
    try {
      Date weekEndingDate = DateUtil.getWeekEndingDay();
      timecard = (CMSEmployeeTimecard)CMSTimecardHelper.findTimecard(theAppMgr, (CMSEmployee)theOpr
          , weekEndingDate);
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
    }
  }

  /**
   * put your documentation comment here
   */
  private void retrievePWTimecard() {
    try {
      Date weekEndingDate = DateUtil.getWeekEndingDay();
      Calendar lastWeek = Calendar.getInstance();
      lastWeek.setTime(weekEndingDate);
      lastWeek.add(Calendar.DATE, -7);
      timecard = (CMSEmployeeTimecard)CMSTimecardHelper.findTimecard(theAppMgr, (CMSEmployee)theOpr
          , lastWeek.getTime());
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
    }
  }

  /**
   * This method creates a new <code>OFFLINE</code> <code>EmployeeTimecard</code>.
   * The timecard values will be set to zeroes. The Eu will also be notified.
   */
  private void createOfflineTimecard() {
    timecard = new CMSEmployeeTimecard(theOpr, theStore.getId(), ITimecardConst.OFFLINE, 0, 0, 0, 0
        , 0, 0, 0, new Vector(), new Vector(), new Vector()
        , FiscalDate.computeWeekEndingDate(new Date()), false, true);
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        if (theAppMgr.isOnLine())
          theAppMgr.showErrorDlg(res.getString("The system was unable to "
              + "retrieve a current timecard for you.  Please make your appropriate"
              + " punch and notify the Help Desk."));
        else
          theAppMgr.showErrorDlg(res.getString("The system cannot retrieve your current "
              + "timecard while in off-line mode.  Any timecard punches made while"
              + " off-line will post and be reviewable when the system is on-line."));
      }
    });
  }

  /**
   * Method to display the information on screen by filling up the JTable.
   * It also shows the total hours and the corresponding error message.
   */
  private void displayInfo() {
    try {
      displayMenu();
      // lets show menu and prompt, then check for null
      if (timecard == null)
        return;
      clear();
      //  custom code for Pier 1:  pull down 3.0 enhancements that refactor the way this is loaded,
      //  then customize it for Pier to add adj reason and render adj in alternate color
      loadModel(); // Pier 1 style
      //            int index = 0;
      //            String[] theList = timecard.getInOutList();
      //            /* First check to see if we have an non-matching length of theList vs
       //             the number of columns and whether the employee is supposed to be
       //             clocked-in or clocked-out. This will happen if employee clocks out
       //             while off-line. */
      //            if(theList.length > 0)
      //                if(((theList.length % 4) == 2 & timecard.getCurStatus() != ITimecardConst.CLOCK_IN) |
      //                   ((theList.length % 4) == 0 & timecard.getCurStatus() == ITimecardConst.CLOCK_IN))
      //                {
      //                    model.addRow(res.getString[] {
      //                                     theStore.getId(), res.getString("UNKNOWN"), theList[0], theList[1]});
      //                    index = 2;
      //                }
      //            for(int i = index; i < theList.length; i++)
      //            {
      //                if(i + 4 > theList.length)
      //                {
      //                    model.addRow(res.getString[] {
      //                                     theList[i], theList[++i], "", ""});
      //                }
      //                else
      //                {
      //                    model.addRow(res.getString[] {
      //                                     theList[i], theList[++i], theList[++i], theList[++i]});
      //                }
      //            }
      //            //Display info in text labels
      try {
        TotHrsFld.setText(" " + timecard.formatTime(timecard.getRoundedWorkTimeAsOfNow()));
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);
        VacHrsFld.setText(" "
            + timecard.formatTime((int)(((CMSEmployee)timecard.getEmp()).getVacationLeaveBalance().
            doubleValue() * 60d * 60 * 1000)));
        floatHrsFld.setText(" "
            + timecard.formatTime((int)(((CMSEmployee)timecard.getEmp()).getFloatingHolidayBalance().
            doubleValue() * 60d * 60 * 1000)));
        SickHrsFld.setText(" "
            + timecard.formatTime((int)(((CMSEmployee)timecard.getEmp()).getSickLeaveBalance().
            doubleValue() * 60d * 60 * 1000)));
        OvertimeHrsFld.setText(" " + timecard.formatTime(timecard.getOvertime()));
        // CR5942 - add a "As of" date for above available times
        SimpleDateFormat df = new SimpleDateFormat(res.getString("MM/dd/yyyy"));
        Date lastWeekEndingDate = new Date();
        //if (((CMSStore)theStore).isWeeklyHourlyPay() && ((CMSEmployee)theOpr).isWagesHourly())
        //{
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        lastWeekEndingDate = FiscalDate.computeWeekEndingDate(calendar.getTime());
        //}
        //else
        //{
        //  Calendar calendar = Calendar.getInstance();
        //  calendar.add(Calendar.DATE, -14);
        //  lastWeekEndingDate = FiscalDate.computeWeekEndingDate(calendar.getTime());
        //  lastWeekEndingDate = ((CMSStore)theStore).getPeriodEndDateForWeekEndDate(lastWeekEndingDate);
        //}
        asOfFld.setText(" " + df.format(lastWeekEndingDate));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
    }
  }

  /**
   * put your documentation comment here
   */
  private void loadModel() {
    Vector inOutTransactions = timecard.getTimeInOut();
    Iterator itr = inOutTransactions.iterator();
    TransactionTimecardIn in = null;
    TransactionTimecardOut out = null;
    while (itr.hasNext()) {
      Object tran = itr.next();
      if (tran instanceof TransactionTimecardIn) {
        if (in != null)
          model.addRow(new ClockInOutRow(in, out));
        in = (TransactionTimecardIn)tran;
      } else {
        model.addRow(new ClockInOutRow(in, (TransactionTimecardOut)tran));
        in = null;
      }
    }
    if (in == null) {} else
      model.addRow(new ClockInOutRow(in));
  }

  /**
   * Method to empty textfields and timecard table.
   */
  public void clear() {
    model.clear();
    TotHrsFld.setText("");
    //edtStatus.setText("");
    asOfFld.setText("");
  }

  /**
   * Method to display the apprtheOpriate menu based on timecard's status and
   * display prompt to user.
   */
  private void displayMenu() {
    //if(timecard.getWeekEndingDate().compareTo(new Date()) > 0)
    SimpleDateFormat compareFormat = new SimpleDateFormat(res.getString("yyyyMMdd"));
    //System.out.println("compareing tc week end to now: " + compareFormat.format(timecard.getWeekEndingDate()) + " " + compareFormat.format(new Date()) );
    if (compareFormat.format(timecard.getWeekEndingDate()).compareTo(compareFormat.format(new Date()))
        >= 0)
      switch (timecard.getCurStatus()) {
        case ITimecardConst.CLOCK_IN:
          theAppMgr.showMenu(MenuConst.CLOCK_OUT, "", theOpr, actionPerformed);
          break;
        case ITimecardConst.IN_TRANSIT:
        case ITimecardConst.CLOCK_OUT:
          theAppMgr.showMenu(MenuConst.CLOCK_IN, "", theOpr, actionPerformed);
          break;
        default:
          theAppMgr.showMenu(MenuConst.CLOCK_IN_OUT, "", theOpr, actionPerformed);
      }
    else
      theAppMgr.showMenu(MenuConst.PREV_ONLY, "PW_TIME_MODE"
          , (CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
    selectOption();
  }

  /**
   * Method to respond to Eu button clicks.
   */
  public void appButtonEvent(String buttonHeader, CMSActionEvent anEvent) {
    if (buttonHeader.equals("PW_TIME_MODE")) {
      anEvent.consume();
      timecard = currentTimecard;
      displayInfo();
    } else
      appButtonEvent(anEvent);
  }

  /**
   * put your documentation comment here
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    LoggingServices.getCurrent().logMsg(new LoggingInfo("TimecardApplet", "appButtonEvent()"
        , "applet button pressed: " + sAction + " with emp: " + theOpr.getShortName(), "N/A"
        , LoggingServices.INFO));
    if (sAction.equals("PW_TIME")) {
      currentTimecard = timecard;
      this.retrievePWTimecard();
      //if(timecard == null || !timecard.isAdjustable()) {
      //   theAppMgr.showErrorDlg(res.getString("Last week's timecard is not available for employee viewing."));
      //   timecard = currentTimecard;
      //}
      if (timecard == null
          || (timecard.getInOutList().length == 0 && timecard.getBenefitTimeList().length == 0)) {
        theAppMgr.showErrorDlg(res.getString(
            "No regular or benefit time activity from last week to report"));
        timecard = currentTimecard;
      }
      displayInfo();
    } else if (sAction.equals("CLOCK_IN")) {
      Date punch = new Date();
      try {
        Calendar punchCal = Calendar.getInstance();
        punchCal.setTime(punch);
        timecard.generateBreakTransactions(theOpr, punchCal, theStore);
        timecard.clockIn(theOpr, punch, theStore);
        doClockIn(punch);
        timecard.setCurStatus(ITimecardConst.CLOCK_IN);
        actionPerformed = theAppMgr.OK_BUTTON;
        displayInfo();
        model.lastPage();
      } catch (BusinessRuleException e) {
        LoggingServices.getCurrent().logMsg(new LoggingInfo("TimecardApplet", "appButtonEvent()"
            , "bre failure on clock in: " + e.getMessage() + " with emp: " + theOpr.getShortName()
            , "N/A", LoggingServices.INFO));
        theAppMgr.showErrorDlg(res.getString(e.getMessage()));
      }
    } else if (sAction.equals("CLOCK_OUT")) {
      Date punch = new Date();
      try {
        timecard.clockOut(theOpr, punch, theStore);
        doClockOut(punch);
        timecard.setCurStatus(ITimecardConst.CLOCK_OUT);
        actionPerformed = theAppMgr.OK_BUTTON;
        displayInfo();
        model.lastPage();
      } catch (BusinessRuleException e) {
        LoggingServices.getCurrent().logMsg(new LoggingInfo("TimecardApplet", "appButtonEvent()"
            , "bre failure on clock out: " + e.getMessage() + " with emp: " + theOpr.getShortName()
            , "N/A", LoggingServices.INFO));
        theAppMgr.showErrorDlg(res.getString(e.getMessage()));
      }
    } else if (sAction.equals("IN_TRANSIT")) {
      //enterTransit();
    }
  }

  /**
   * put your documentation comment here
   */
  private void selectOption() {
    theAppMgr.setSingleEditArea(res.getString("Select option."));
  }

  /**
   * put your documentation comment here
   */
  private void enterTransit() {
    //theAppMgr.showMenu(MenuConst.CANCEL, "CANCEL", theOpr);
    theAppMgr.setSingleEditArea(res.getString("Enter destination."), "DEST");
  }

  /**
   * Method to respond to typed input.
   */
  public void editAreaEvent(String command, String value) {
    try {
      if (command.equals("DEST")) {}
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }

  /**
   * Method to post a <code>TransactionTimecardIn</code>
   * @param punch the Date the punch occured
   * @return result whether the transaction posted.
   */
  private boolean doClockIn(Date punch) {
    return doClockIn(punch, "");
  }

  /**
   * put your documentation comment here
   * @param punch
   * @param reason
   * @return
   */
  private boolean doClockIn(Date punch, String reason) {
    try {
      TransactionTimecardIn in = new TransactionTimecardIn(theStore);
      in.setTimeStamp(punch);
      in.setTheOperator(theOpr);
      in.setEmployee(theOpr);
      in.setWeekEndingDate(FiscalDate.computeWeekEndingDate(punch));
      in.setId(System.currentTimeMillis() + "");
      in.setModificationReason(reason);
      serializeObject(in); // test code for receipt
      Object[] arguments = {in
      };
      ReceiptFactory receiptFactory = new ReceiptFactory(arguments
          , ReceiptBlueprintInventory.CMSClockIn);
      ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(in.getStore(), in.getEmployee());
      localeSetter.setLocale(receiptFactory);
      receiptFactory.print(theAppMgr);
      return (CMSTxnPosterHelper.post(theAppMgr, in));
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
      return (false);
    }
  }

  /**
   * put your documentation comment here
   * @param object
   */
  private void serializeObject(Serializable object) {
    if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
      String txnClassName = object.getClass().getName();
      String shortClassName = txnClassName.substring(txnClassName.lastIndexOf(".") + 1
          , txnClassName.length());
      String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + shortClassName + ".rdo";
      try {
        ObjectStore objectStore = new ObjectStore(fileName);
        objectStore.write(object);
      } catch (Exception e) {
        System.out.println("exception on writing object to blueprint folder: " + e);
      }
    }
  }

  /**
   * Method to post a <code>TransactionTimecardOut</code>
   * @param punch the Date the punch occured
   * @return result whether the transaction posted.
   */
  private boolean doClockOut(Date punch) {
    return doClockOut(punch, "");
  }

  /**
   * put your documentation comment here
   * @param punch
   * @param reason
   * @return
   */
  private boolean doClockOut(Date punch, String reason) {
    try {
      TransactionTimecardOut out = new TransactionTimecardOut(theStore);
      out.setTheOperator(theOpr);
      out.setEmployee(theOpr);
      out.setWeekEndingDate(FiscalDate.computeWeekEndingDate(punch));
      out.setTimeStamp(punch);
      out.setId(System.currentTimeMillis() + "");
      out.setModificationReason(reason);
      //serializeObject(out);    //test code for receipt
      Object[] arguments = {out
      };
      ReceiptFactory receiptFactory = new ReceiptFactory(arguments
          , ReceiptBlueprintInventory.CMSClockOut);
      ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(out.getStore(), out.getEmployee());
      localeSetter.setLocale(receiptFactory);
      receiptFactory.print(theAppMgr);
      return (CMSTxnPosterHelper.post(theAppMgr, out));
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
      return (false);
    }
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

  /**
   * put your documentation comment here
   * @param e
   */
  void tblList_componentResized(ComponentEvent e) {
    model.setRowsShown(tblList.getHeight() / tblList.getRowHeight());
    tblList.repaint();
  }

  // custom Pier 1 Row object and renderer
  public class ClockInOutRow {
    public String storeId;
    public String clockInTime;
    public String clockOutTime;
    public String duration;
    public String inModificationReason = "";
    public String outModificationReason = "";
    public boolean inIsMadeUp;
    public boolean outIsMadeUp;
    public TransactionTimecardIn in;
    public TransactionTimecardOut out;
    private DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT
        , DateFormat.SHORT, LocaleManager.getInstance().getDefaultLocale());

    /**
     * put your documentation comment here
     * @param         TransactionTimecardIn in
     * @param         TransactionTimecardOut out
     */
    public ClockInOutRow(TransactionTimecardIn in) {
      this.in = in;
      storeId = in.getStore().getId();
      clockOutTime = "";
      duration = "";
      outIsMadeUp = false;
      inIsMadeUp = in.getIsMadeUp();
      clockInTime = formatter.format(in.getRoundedTimeStamp());
      inModificationReason = in.getModificationReason() == null ? "" : in.getModificationReason();
    }

    /**
     * put your documentation comment here
     * @param     TransactionTimecardIn in
     * @param     TransactionTimecardOut out
     */
    public ClockInOutRow(TransactionTimecardIn in, TransactionTimecardOut out) {
      this.in = in;
      this.out = out;
      if (in == null) {
        storeId = out.getStore().getId();
        clockInTime = res.getString("UNKNOWN");
        duration = "";
        outIsMadeUp = out.getIsMadeUp();
        inIsMadeUp = true;
        clockOutTime = formatter.format(out.getRoundedTimeStamp());
        outModificationReason = out.getModificationReason() == null ? ""
            : out.getModificationReason();
      } else if (out == null) {
        storeId = in.getStore().getId();
        clockOutTime = res.getString("UNKNOWN");
        duration = "";
        outIsMadeUp = true;
        inIsMadeUp = in.getIsMadeUp();
        clockInTime = formatter.format(in.getRoundedTimeStamp());
        inModificationReason = in.getModificationReason() == null ? "" : in.getModificationReason();
      } else {
        storeId = in.getStore().getId();
        duration = formatDuration(out.getRoundedTimeStamp().getTime()
            - in.getRoundedTimeStamp().getTime());
        outIsMadeUp = out.getIsMadeUp();
        inIsMadeUp = in.getIsMadeUp();
        clockInTime = formatter.format(in.getRoundedTimeStamp());
        clockOutTime = formatter.format(out.getRoundedTimeStamp());
        inModificationReason = in.getModificationReason() == null ? "" : in.getModificationReason();
        outModificationReason = out.getModificationReason() == null ? ""
            : out.getModificationReason();
        if (outIsMadeUp && outModificationReason.length() == 0)
          outModificationReason = res.getString("System Created");
        if (inIsMadeUp && inModificationReason.length() == 0)
          inModificationReason = res.getString("System Created");
      }
    }

    /**
     * put your documentation comment here
     * @param msElapsed
     * @return
     */
    private String formatDuration(long msElapsed) {
      long hrElapsed = msElapsed / (3600 * 1000);
      long minElapsed = (msElapsed % (3600 * 1000)) / (60 * 1000);
      long secElapsed = (msElapsed / 1000) - (3600 * hrElapsed) - (60 * minElapsed);
      //round off the minute if needed
      if (secElapsed > 30)
        minElapsed++;
      return hrElapsed + res.getString("timecard.format.hrs") + minElapsed
          + res.getString("timecard.format.min");
    }
  }


  public class ClockInOutRowRenderer extends javax.swing.table.DefaultTableCellRenderer {
    Color defaultBackground;
    Color defaultForeground;

    /**
     * put your documentation comment here
     */
    public ClockInOutRowRenderer() {
      defaultBackground = getBackground();
      defaultForeground = getForeground();
      setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * put your documentation comment here
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int column) {
      ClockInOutRow clockRow = (ClockInOutRow)value;
      setFont(table.getFont());
      switch (column) {
        case 0:
          setText(clockRow.storeId);
          setForeground(defaultForeground);
          break;
        case 1:
          setText(clockRow.clockInTime);
          if (clockRow.inIsMadeUp || clockRow.inModificationReason.length() > 0) {
            if (clockRow.inModificationReason.equals("Paid Break_Rsrc_Key"))
              setForeground(Color.gray);
            else
              setForeground(theAppMgr.getTheme().getColorSignalDanger());
          } else {
            setForeground(defaultForeground);
          }
          break;
        case 3:
          setText(clockRow.clockOutTime);
          if (clockRow.outIsMadeUp || clockRow.outModificationReason.length() > 0) {
            if (clockRow.outModificationReason.equals("Paid Break_Rsrc_Key"))
              setForeground(Color.gray);
            else
              setForeground(theAppMgr.getTheme().getColorSignalDanger());
          } else {
            setForeground(defaultForeground);
          }
          break;
        case 5:
          setText(clockRow.duration);
          setForeground(defaultForeground);
          break;
        default:
          System.out.println("try to render invalid col!!!" + column);
      }
      return this;
    }
  }


  public class TextAreaClockInOutRowRenderer extends JTextArea implements javax.swing.table.
      TableCellRenderer {
    Color defaultBackground;
    Color defaultForeground;

    /**
     * put your documentation comment here
     */
    public TextAreaClockInOutRowRenderer() {
      defaultBackground = getBackground();
      defaultForeground = getForeground();
      setLineWrap(true);
      setOpaque(true);
      this.setLineWrap(true);
      this.setWrapStyleWord(true);
      this.setMargin(new Insets(5, 5, 0, 0));
      //setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * put your documentation comment here
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int column) {
      ClockInOutRow clockRow = (ClockInOutRow)value;
      setFont(table.getFont());
      switch (column) {
        case 2:
          if (clockRow.inModificationReason.endsWith("Rsrc_Key")) {
            setText(res.getString(clockRow.inModificationReason));
          } else
            setText(clockRow.inModificationReason); // in adjustment reason
          if (clockRow.inIsMadeUp || clockRow.inModificationReason.length() > 0) {
            if (clockRow.inModificationReason.equals("Paid Break_Rsrc_Key"))
              setForeground(Color.gray);
            else
              setForeground(theAppMgr.getTheme().getColorSignalDanger());
          } else {
            setForeground(defaultForeground);
          }
          break;
        case 4:
          if (clockRow.outModificationReason.endsWith("Rsrc_Key"))
            setText(res.getString(clockRow.outModificationReason));
          else
            setText(clockRow.outModificationReason); // out adjustment reason
          if (clockRow.outIsMadeUp || clockRow.outModificationReason.length() > 0) {
            if (clockRow.outModificationReason.equals("Paid Break_Rsrc_Key"))
              setForeground(Color.gray);
            else
              setForeground(theAppMgr.getTheme().getColorSignalDanger());
          } else {
            setForeground(defaultForeground);
          }
          break;
        default:
          System.out.println("ta try to render invalid col!!!" + column);
      }
      return this;
    }
  }
} // end TimecardApplet

