/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.dlg;

import java.awt.*;
import javax.swing.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.ScrollProcessor;
import com.chelseasystems.cr.config.ConfigMgr;
import java.awt.event.*;
import com.chelseasystems.cs.swing.model.ReservationReasonModel;
import com.chelseasystems.cr.swing.CMSApplet;
import java.util.*;
import com.chelseasystems.cs.util.ArmConfigLoader;

/**
 * <p>Title:ReservationReasonDlg </p>
 * <p>Description: AlterationList Dailog </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ReservationReasonDlg extends JDialog implements ScrollProcessor {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  JPanel jPanel5 = new JPanel();
  JButton btnScrollUp = new JButton();
  JButton btnScrollDown = new JButton();
  JButton btnOK = new JButton();
  JButton btnCancel = new JButton();
  protected IApplicationManager theAppMgr;
  protected ReservationReasonModel model;
  JCMSTable tblReservationReasonList;
  private boolean ok = false;
  private String sSelectedReservationReason;
  double r = com.chelseasystems.cr.swing.CMSApplet.r;
  JPanel jPanel6 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JCMSToolTip ttip;
  private boolean bReasonsAvailable=false;
  /**
   * @param    Frame frame
   * @param    IApplicationManager theAppMgr
   * @param    String[] grades
   */
  public ReservationReasonDlg(Frame frame, IApplicationManager theAppMgr, String sHeader) {
    super(frame, sHeader, true);
    try {
      bReasonsAvailable=false;
      model = new ReservationReasonModel(sHeader);
      tblReservationReasonList = new JCMSTable(model, JCMSTable.SELECT_ROW);
      this.theAppMgr = theAppMgr;
      ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
      ttip.setType(JCMSToolTip.FOLLOWING);
      jbInit();
      tblReservationReasonList.setAppMgr(theAppMgr);
      pack();
      setSize((int)(r * 550), (int)(r * 400));
      loadReservationReasons();
      this.setModal(true);
      this.setResizable(false);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void selectReservationReason(String sReason) {
    if (sReason == null || sReason.trim().length() < 1)return;
    int iIndex = model.getReservationReasonIndex(sReason);
    if (iIndex != -1)
      tblReservationReasonList.setRowSelectionInterval(iIndex, iIndex);
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void loadReservationReasons()
      throws Exception {
    try
    {
//      ConfigMgr config = new ConfigMgr("ArmaniCommon.cfg");
      ArmConfigLoader config = new ArmConfigLoader();
      String sReservationReasonCodes = config.getString("RESERVATION_REASON_CD");
      StringTokenizer sTokens = new StringTokenizer(sReservationReasonCodes, ",");
      bReasonsAvailable=false;
      while (sTokens.hasMoreTokens())
      {
        String sCode = sTokens.nextToken();
        String sReservationReasonCode = config.getString(sCode + ".CODE");
        String sReservationReasonLabel = config.getString(sCode + ".LABEL");
        if (sReservationReasonCode != null && sReservationReasonCode.length() > 0
            && sReservationReasonLabel != null && sReservationReasonLabel.length() > 0)
        {
          model.addReservationReason(sReservationReasonCode
              , sReservationReasonLabel);
          // - MSB  02/01/2006
          // Reservation reasons are downloaded from database
          // hence already are locale specific.
//          model.addReservationReason(sReservationReasonCode
//              , CMSApplet.res.getString(sReservationReasonLabel));
          bReasonsAvailable=true;
        }
      }
    }catch(Exception e)
    {
      bReasonsAvailable = false;
      System.out.println("**** Exception: loading reservation reasons ***");
    }

  }

  public boolean areReservationReasonsAvailable()
  {
    return bReasonsAvailable;
  }

  /**
   * @exception Exception
   */
  void jbInit()
      throws Exception {
    panel1.setLayout(borderLayout1);
    jPanel6.setLayout(borderLayout3);
    jPanel1.setLayout(new GridLayout(1, 4, 0, 0));
    jPanel2.setLayout(borderLayout2);
    btnScrollUp = theAppMgr.getTheme().getDefaultBtn();
    btnScrollUp.setText(CMSApplet.res.getString("Page Up"));
    btnScrollUp.setMnemonic(CMSApplet.res.getString("Mnemonic_PageUp").charAt(0));
    btnScrollDown = theAppMgr.getTheme().getDefaultBtn();
    btnScrollDown.setText(CMSApplet.res.getString("Page Down"));
    btnScrollDown.setMnemonic(CMSApplet.res.getString("Mnemonic_PageDown").charAt(0));
    btnOK = theAppMgr.getTheme().getDefaultBtn();
    btnOK.setText(CMSApplet.res.getString("OK"));
    btnOK.setMnemonic(CMSApplet.res.getString("Mnemonic_OK").charAt(0));
    btnOK.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnOK_actionPerformed(e);
      }
    });
    btnCancel = theAppMgr.getTheme().getDefaultBtn();
    btnCancel.setText(CMSApplet.res.getString("Cancel"));
    btnCancel.setMnemonic(CMSApplet.res.getString("Mnemonic_Cancel").charAt(0));
    btnCancel.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnCancel_actionPerformed(e);
      }
    });
    btnScrollUp.setDefaultCapable(false);
    btnScrollUp.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnScrollUp_actionPerformed(e);
      }
    });
    btnScrollDown.setDefaultCapable(false);
    btnScrollDown.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnScrollDown_actionPerformed(e);
      }
    });
    tblReservationReasonList.addMouseListener(new java.awt.event.MouseAdapter() {

      /**
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        tblAlterationIDList_mouseClicked(e);
      }
    });
    tblReservationReasonList.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        tblReservationReasonList_componentResized(e);
      }
    });
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(btnScrollUp, null);
    jPanel1.add(btnScrollDown, null);
    jPanel1.add(btnOK, null);
    jPanel1.add(btnCancel, null);
    panel1.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jPanel3, BorderLayout.NORTH);
    jPanel2.add(jPanel4, BorderLayout.WEST);
    jPanel2.add(jPanel5, BorderLayout.EAST);
    jPanel2.add(jPanel6, BorderLayout.CENTER);
    jPanel6.add(tblReservationReasonList.getTableHeader(), BorderLayout.NORTH);
    jPanel6.add(tblReservationReasonList, BorderLayout.CENTER);
    jPanel1.setBackground(theAppMgr.getBackgroundColor());
    jPanel2.setBackground(theAppMgr.getBackgroundColor());
    jPanel3.setBackground(theAppMgr.getBackgroundColor());
    jPanel4.setBackground(theAppMgr.getBackgroundColor());
    jPanel5.setBackground(theAppMgr.getBackgroundColor());
    jPanel6.setBackground(theAppMgr.getBackgroundColor());
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
          btnOK.doClick();
          e.consume();
        } else if (ch == 0x1B) { //KeyEvent.VK_ESCAPE
          btnCancel.doClick();
          e.consume();
        }
      }
    };
    this.addKeyListener(keyListener);
  }

  // this event is called by the JCMSTable when the user arrows up beyond the displayed rows
  public void prevPage() {
    btnScrollUp.doClick();
  }

  // this event is called by the JCMSTable when the user arrows dn beyond the displayed rows
  public void nextPage() {
    btnScrollDown.doClick();
  }

  // return model page number
  public int getCurrentPageNumber() {
    return model.getCurrentPageNumber();
  }

  // return total page number
  public int getPageCount() {
    return model.getPageCount();
  }

  /**
   * @param e
   */
  void btnScrollUp_actionPerformed(ActionEvent e) {
    model.prevPage();
    tblReservationReasonList.setRowSelectionInterval(0, 0);
    fireButtonClick(e);
  }

  /**
   * @param e
   */
  void btnScrollDown_actionPerformed(ActionEvent e) {
    model.nextPage();
    tblReservationReasonList.setRowSelectionInterval(0, 0);
    fireButtonClick(e);
  }

  /**
   */
  private void fireButtonClick(ActionEvent e) {
    MouseEvent me = new MouseEvent((JComponent)e.getSource(), e.getID(), System.currentTimeMillis()
        , e.getModifiers(), -40, -5, 1, false);
    ttip.setText("Page: " + (model.getCurrentPageNumber() + 1));
    ttip.show(me);
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
   * @return
   */
  public String getSelectedReservationReason() {
    return sSelectedReservationReason;
  }

  /**
   * @param e
   */
  void tblAlterationIDList_mouseClicked(MouseEvent e) {
    btnOK_actionPerformed(null);
  }

  /**
   * @param e
   */
  void btnCancel_actionPerformed(ActionEvent e) {
    ok = false;
    dispose();
  }

  /**
   * @param e
   */
  void btnOK_actionPerformed(ActionEvent e) {
    int row = tblReservationReasonList.getSelectedRow();
    if (row < 0) {
      theAppMgr.showErrorDlg(com.chelseasystems.cr.util.ResourceManager.getResourceBundle().
          getString("You must first select a Reservation reason."));
      return;
    }
    sSelectedReservationReason = model.getSelectedReservationReason(row);
    ok = true;
    dispose();
  }

  /**
   * @return
   */
  public boolean isOK() {
    return ok;
  }

  /**
   * @param e
   */
  void tblReservationReasonList_componentResized(ComponentEvent e) {
    model.setColumnWidth(tblReservationReasonList);
    model.setRowsShown(tblReservationReasonList.getHeight() / tblReservationReasonList.getRowHeight());
  }

  public void setEnabled(boolean enable)
  {
    super.setEnabled(enable);
    tblReservationReasonList.setEnabled(enable);
  }
}

