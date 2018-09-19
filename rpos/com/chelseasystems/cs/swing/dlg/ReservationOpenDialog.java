package com.chelseasystems.cs.swing.dlg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultFocusManager;
import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.CMSFocusManager;
import com.chelseasystems.cr.util.ResourceManager;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 11-04-2007 |Ken Hubbard | PCR 1868    | Popup dialog for when there is open res         |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

/**
 * <p>Title:RefineAddressDlg.java </p>
 *
 * <p>Description: RefineAddressDlg </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Skillnet inc.</p>
 *
 * @author Ken Hubbard
 * @version 1.0
 */

public class ReservationOpenDialog extends JDialog {

  private IApplicationManager theAppMgr;

  JButton btnYes;
  JButton btnNo;
  JLabel message = new JLabel();
  JPanel messagePanel = new JPanel();
  JPanel toolbarPanel = new JPanel();
  boolean yes = false;
  ResourceBundle res = ResourceManager.getResourceBundle();

  public ReservationOpenDialog(Frame frame, IApplicationManager theAppMgr) {
    super(frame, "Open Reservation Warning", true);
    try {
      setAppMgr(theAppMgr);
      jbInit();
      pack();
      double r = CMSApplet.r;
      this.setSize((int)(r * 800D), (int)(r * 250D));
      this.setResizable(false);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit()
      throws Exception {
    btnYes = theAppMgr.getTheme().getDefaultBtn();
    btnYes.setText("Yes");
    btnYes.setMnemonic(res.getString("Mnemonic_YES").charAt(0));
    btnYes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnYes_actionPerformed(e);
      }
    });

    btnNo = theAppMgr.getTheme().getDefaultBtn();
    btnNo.setText("No");
    btnNo.setMnemonic(res.getString("Mnemonic_NO").charAt(0));
    btnNo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnNo_actionPerformed(e);
      }
    });

    message.setFont(theAppMgr.getTheme().getDialogFont());
    message.setText(res.getString("No deposit. Do you want to proceed?"));
    
    messagePanel.add(message);
    messagePanel.setBackground(theAppMgr.getBackgroundColor());

    this.getContentPane().setBackground(theAppMgr.getBackgroundColor());
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(messagePanel, BorderLayout.CENTER);
    this.getContentPane().add(toolbarPanel, BorderLayout.SOUTH);

    toolbarPanel.add(btnYes, null);
    toolbarPanel.add(btnNo, null);

    toolbarPanel.setBackground(theAppMgr.getBackgroundColor());
    toolbarPanel.setBorder(BorderFactory.createEtchedBorder());

  }

  void btnYes_actionPerformed(ActionEvent e) {
    try {
      yes = true;
      dispose();
    } catch (Exception ex) {
      System.out.println("Button Yes in RefineAddressDlg Dialog failed.");
    }
  }

  void btnNo_actionPerformed(ActionEvent e) {
    try {
      yes = false;
      dispose();
    } catch (Exception ex) {
      System.out.println("Button No in RefineAddressDlg Dialog failed.");
    }
  }

  public void setVisible(boolean visible) {
    if (visible) {
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
      FocusManager.setCurrentManager(new CMSFocusManager());
    }
    super.setVisible(visible);
  }

  public void setAppMgr(IApplicationManager theAppMgr) {
    this.theAppMgr = theAppMgr;
  }

  public void actionPerformed(ActionEvent e) {
    dispose();
  }

  public boolean isYes() {
    return this.yes;
  }


}