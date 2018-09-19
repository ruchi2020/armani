
package com.chelseasystems.cs.swing.dlg;

import java.awt.*;

import javax.swing.*;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.ScrollProcessor;
import com.chelseasystems.cr.util.ResourceManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.peer.DialogPeer;

import com.chelseasystems.cs.swing.model.AlterationIDModel;

import java.util.*;


/**
 * <p>Title:CommunicationErrorDlg </p>
 * <p>Description: Communication error Buttons </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Anjana Asok
 * @version 1.0
 */
public class CommunicationErrorDlg extends JDialog implements ScrollProcessor {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  JPanel jPanel5 = new JPanel();
  JButton btnRetry = new JButton();
  public JTextPane txtInstruct = new JTextPane();
  public JButton btnManual = new JButton();
  //JButton btnOK = new JButton();
  JButton btnCancel = new JButton();
  protected IApplicationManager theAppMgr;
  protected AlterationIDModel model;
  JCMSTable tblAlterationIDList;
  private boolean ok = false;
  private String sSelectedAlterationID;
  double r = com.chelseasystems.cr.swing.CMSApplet.r;
 // JPanel jPanel6 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JCMSToolTip ttip;
  boolean retry = false;
  boolean manual = false;
  boolean cancel = false;
  ResourceBundle res = null;
  //public JLabel jlabel;
 //Vivek Mishra : Added to show Mobile terminal option while POS receive response other than 0 or timeout as asked by Jason 25-APR-2016
  public JTextArea jlabel;
  boolean isMobile = false;
  String errorMessage = null;
  boolean isGC = false;
  /**
   * @param    Frame frame
   * @param    IApplicationManager theAppMgr
   * @param    String[] grades
   */
//Vivek Mishra : Modified to show Mobile terminal option while POS receive response other than 0 or timeout as asked by Jason 25-APR-2016
  //public CommunicationErrorDlg(Frame frame, IApplicationManager theAppMgr) {
  public CommunicationErrorDlg(Frame frame, IApplicationManager theAppMgr, boolean isMobile, String errorMessage) {
    super(frame, "", true);
    try {
      res = ResourceManager.getResourceBundle();
      tblAlterationIDList = new JCMSTable(model, JCMSTable.SELECT_ROW);
      this.theAppMgr = theAppMgr;
      ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
      ttip.setType(JCMSToolTip.FOLLOWING);
      this.isMobile = isMobile;
      this.errorMessage = errorMessage;
      jbInit();
      tblAlterationIDList.setAppMgr(theAppMgr);
      pack();
      setSize((int)(r * 550), (int)(r * 400));
      this.setModal(true);
      this.setResizable(false);
      setTitle(res.getString("Message"));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  
  //Anjana added GC check to restrict the communication error dlg for GC 
  public CommunicationErrorDlg(Frame frame, IApplicationManager theAppMgr, boolean isMobile,boolean isGC, String errorMessage) {
    super(frame, "", true);
    try {
      res = ResourceManager.getResourceBundle();
      tblAlterationIDList = new JCMSTable(model, JCMSTable.SELECT_ROW);
      this.theAppMgr = theAppMgr;
      ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
      ttip.setType(JCMSToolTip.FOLLOWING);
      this.isMobile = isMobile;
      this.isGC = isGC;
      this.errorMessage = errorMessage;
      jbInit();
      tblAlterationIDList.setAppMgr(theAppMgr);
      pack();
      setSize((int)(r * 550), (int)(r * 400));
      this.setModal(true);
      this.setResizable(false);
      
      setTitle(res.getString("Message"));
       } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  /**
   * @param ids
   * @exception Exception
   */
  public void loadAlterationIDs(Hashtable htLabels)
      throws Exception {
    if (htLabels.isEmpty())
      return;
    model.addAlterationIDs(htLabels);
  }

  /**
   * @exception Exception
   */
  void jbInit()
      throws Exception {
    panel1.setLayout(borderLayout1);
    //jPanel6.setLayout(borderLayout3);
    jPanel1.setLayout(new GridLayout(1, 4, 0, 0));
    jPanel2.setLayout(borderLayout2);
    btnRetry = theAppMgr.getTheme().getDefaultBtn();
    btnRetry.setText("Retry");
    //btnScrollUp.setMnemonic('U');
    btnManual = theAppMgr.getTheme().getDefaultBtn();
    if(isMobile)
    	btnManual.setText("Mobile Terminal");
    else
        btnManual.setText("Manual Auth"); //Mayuri Edhara :: name changed to manual auth from manual:: 20 OCT 2016
    btnManual.addActionListener(new java.awt.event.ActionListener() {

        /**
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
          btnManual_actionPerformed(e);
        }
      });

    btnRetry.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnRetry_actionPerformed(e);
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
  
  
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(btnRetry, null);
    jPanel1.add(btnManual, null);
    jPanel1.add(btnCancel, null);
    panel1.add(jPanel2, BorderLayout.CENTER);
 //   jPanel2.add(txtInstruct,BorderLayout.CENTER);
  //  jPanel2.add(jPanel3, BorderLayout.NORTH);
   // jPanel2.add(jPanel4, BorderLayout.WEST);
   // jPanel2.add(jPanel5, BorderLayout.EAST);
   	jlabel = new JTextArea("Communication error. Please select one from below");

	  //Vivek Mishra : Modified to show Mobile terminal option while POS receive response other than 0 or timeout as asked by Jason 25-APR-2016
    if(errorMessage!=null)
    	jlabel.setText(errorMessage);

    //Ends
    jlabel.setFont(theAppMgr.getTheme().getDialogFont());
    jlabel.setLineWrap(true);
    jlabel.setWrapStyleWord(true);
    jlabel.setEditable(false);
    jlabel.setBackground(theAppMgr.getBackgroundColor());
    jPanel2.add(jlabel,BorderLayout.NORTH);
   /* if(errorMessage!=null)
    	txtInstruct.setText(errorMessage);
    else*/
    if(isMobile){
        txtInstruct.setText(res.getString("Communication error. Please select one from below"));
    }
    else if(isGC){
    	txtInstruct.setText("");
    }//Mayuri Edhara :: Error text added for manual auth:: 20 OCT 2016
    else {
    	 txtInstruct.setText(res.getString("Authorization failed on one or more of the card payments. Please click Retry to retry authorization, "
    	 		+ "Manual Auth button to call bank for authorization number, or Cancel."));
    }
        txtInstruct.setForeground(theAppMgr.getTheme().getMsgTextColor());
        txtInstruct.setFont(theAppMgr.getTheme().getDialogFont());
        txtInstruct.setOpaque(false);

      jPanel2.add(txtInstruct);
    //jPanel2.add(jPanel6, BorderLayout.CENTER);
   // jPanel6.add(tblAlterationIDList.getTableHeader(), BorderLayout.NORTH);
   // jPanel6.add(tblAlterationIDList, BorderLayout.CENTER);
    jPanel1.setBackground(theAppMgr.getBackgroundColor());
    jPanel2.setBackground(theAppMgr.getBackgroundColor());
  //  jPanel3.setBackground(theAppMgr.getBackgroundColor());
 //   jPanel4.setBackground(theAppMgr.getBackgroundColor());
//	    jPanel5.setBackground(theAppMgr.getBackgroundColor());
   // jPanel6.setBackground(theAppMgr.getBackgroundColor());
/*    KeyListener keyListener = new KeyListener() {

      *//**
       * put your documentation comment here
       * @param e
       *//*
      public void keyReleased(KeyEvent e) {}

      *//**
       * put your documentation comment here
       * @param e
       *//*
      public void keyPressed(KeyEvent e) {}

      *//**
       * put your documentation comment here
       * @param e
       *//*
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
    this.addKeyListener(keyListener);*/
  }

 /* // this event is called by the JCMSTable when the user arrows up beyond the displayed rows
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
*/
  /**
   * @param e
   */
  void btnRetry_actionPerformed(ActionEvent e) {
	  retry = true;
	  dispose();
  }

  /**
   * @param e
   */
  void btnManual_actionPerformed(ActionEvent e) {
	  manual = true;
	  dispose();
  }
  
  void btnCancel_actionPerformed(ActionEvent e) {
	  cancel = true;
	  dispose();
	  }

	public boolean isRetry() {
		return retry;
	}

	public boolean isManual() {
		return manual;
	}

	public boolean isCancel() {
		return cancel;
	}
 
/*  *//**
   *//*
  private void fireButtonClick(ActionEvent e) {
    MouseEvent me = new MouseEvent((JComponent)e.getSource(), e.getID(), System.currentTimeMillis()
        , e.getModifiers(), -40, -5, 1, false);
    ttip.setText("Page: " + (model.getCurrentPageNumber() + 1));
    ttip.show(me);
  }*/

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

public int getCurrentPageNumber() {
	// TODO Auto-generated method stub
	return 0;
}

public int getPageCount() {
	// TODO Auto-generated method stub
	return 0;
}

public void nextPage() {
	// TODO Auto-generated method stub
	
}

public void prevPage() {
	// TODO Auto-generated method stub
	
}


}

