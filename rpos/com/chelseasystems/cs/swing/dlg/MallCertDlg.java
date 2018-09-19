/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.dlg;

import java.awt.*;
import javax.swing.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.ScrollProcessor;
import java.awt.event.*;
import com.chelseasystems.cs.swing.model.MallCertModel;
import java.util.*;


/**
 */
public class MallCertDlg extends JDialog implements ScrollProcessor {
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
  protected MallCertModel model = new MallCertModel();
  JCMSTable tblMallCert = new JCMSTable(model, JCMSTable.SELECT_ROW);
  private boolean ok = false;
  private String selectedMallCert;
  double r = com.chelseasystems.cr.swing.CMSApplet.r;
  JPanel jPanel6 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JCMSToolTip ttip;

  /**
   * @param    Frame frame
   * @param    IApplicationManager theAppMgr
   * @param    String[] grades
   */
  public MallCertDlg(Frame frame, IApplicationManager theAppMgr, Hashtable mallCertArray) {
    super(frame, "MallCert", true);
    try {
      this.theAppMgr = theAppMgr;
      ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
      ttip.setType(JCMSToolTip.FOLLOWING);
      jbInit();
      tblMallCert.setAppMgr(theAppMgr);
      //System.out.println("the value of r: " + r);
      pack();
      setSize((int)(r * 800), (int)(r * 400));
      loadMallCerts(mallCertArray);
      this.setModal(true);
      this.setResizable(false);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @param ids
   * @exception Exception
   */
  public void loadMallCerts(Hashtable htLabels)
      throws Exception {
    if (htLabels.isEmpty())
      return;
    model.addMallCert(htLabels);
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
    btnScrollUp.setText("Page Up");
    btnScrollUp.setMnemonic('U');
    btnScrollDown = theAppMgr.getTheme().getDefaultBtn();
    btnScrollDown.setText("Page Down");
    btnScrollDown.setMnemonic('D');
    btnOK = theAppMgr.getTheme().getDefaultBtn();
    btnOK.setText("OK");
    btnOK.setMnemonic('O');
    btnOK.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnOK_actionPerformed(e);
      }
    });
    btnCancel = theAppMgr.getTheme().getDefaultBtn();
    btnCancel.setText("Cancel");
    btnCancel.setMnemonic('C');
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
    tblMallCert.addMouseListener(new java.awt.event.MouseAdapter() {

      /**
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        tblMallCert_mouseClicked(e);
      }
    });
    tblMallCert.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        tblMallCert_componentResized(e);
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
    jPanel6.add(tblMallCert.getTableHeader(), BorderLayout.NORTH);
    jPanel6.add(tblMallCert, BorderLayout.CENTER);
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
    tblMallCert.setRowSelectionInterval(0, 0);
    fireButtonClick(e);
  }

  /**
   * @param e
   */
  void btnScrollDown_actionPerformed(ActionEvent e) {
    model.nextPage();
    tblMallCert.setRowSelectionInterval(0, 0);
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
  public String getSelectedMallCert() {
    return selectedMallCert;
  }

  /**
   * @param e
   */
  void tblMallCert_mouseClicked(MouseEvent e) {
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
    int row = tblMallCert.getSelectedRow();
    if (row < 0) {
      theAppMgr.showErrorDlg(com.chelseasystems.cr.util.ResourceManager.getResourceBundle().
          getString("You must first select a MallCert type."));
      return;
    }
    selectedMallCert = model.getSelectedMallCert(row);
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
  void tblMallCert_componentResized(ComponentEvent e) {
    model.setColumnWidth(tblMallCert);
    model.setRowsShown(tblMallCert.getHeight() / tblMallCert.getRowHeight());
  }
}

