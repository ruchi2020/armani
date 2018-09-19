/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.customer;

import java.awt.*;
import javax.swing.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.bean.*;
import java.awt.event.*;
import com.chelseasystems.cs.customer.CMSCustomerMessage;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.customer.CMSCustomer;


/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 05-20-2005 | Megha    | N/A        | Customer Messages                            |
 --------------------------------------------------------------------------------------------
 */
/**
 * <p>Title:CustomerMsgDlg </p>
 * <p>Description:CustomerMsg Dailog </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Megha
 * @version 1.0
 */
public class CustomerMessageDialog extends JDialog {
  private JPanel mainPanel = new JPanel();
  private JCMSTextArea txtQuestion;
  private JCMSLabel lblQuestion;
  private JCMSTextArea txtAns;
  private JCMSLabel lblAns;
  private String question;
  private boolean searchById;
  private CMSCustomerMessage cmsCustMsg;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  BorderLayout borderlayout1 = new BorderLayout();
  JButton btnOK = new JButton();
  JButton btnCancel = new JButton();
  // For Message Type
  private JCMSTextArea txtMessage;
  private JCMSLabel lblMessage;
  JButton btnOKForM = new JButton();
  protected IApplicationManager theAppMgr;
  private boolean ok = false;
  double r = com.chelseasystems.cr.swing.CMSApplet.r;
  JCMSToolTip ttip;

  /**
   * @param    Frame frame
   * @param    IApplicationManager theAppMgr
   * @param    String[] grades
   */
  public CustomerMessageDialog(Frame frame, IApplicationManager theAppMgr, String question
      , boolean searchById) {
    super(frame, "Customer Message", true);
    try {
      this.question = question;
      this.theAppMgr = theAppMgr;
      this.searchById = searchById;
      cmsCustMsg = new CMSCustomerMessage();
      ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
      ttip.setType(JCMSToolTip.FOLLOWING);
      jbInit();
      pack();
      setSize((int)(450), (int)(250));
      this.setResizable(false);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @exception Exception
   */
  void jbInit()
      throws Exception {
    this.getContentPane().setLayout(borderlayout1);
    this.getContentPane().add(mainPanel, BorderLayout.NORTH);
    this.setBackground(theAppMgr.getBackgroundColor());
    mainPanel.setPreferredSize(new Dimension(833, 50));
    mainPanel.setLayout(gridBagLayout1);
    // For Message
    btnOKForM = theAppMgr.getTheme().getDefaultBtn();
    btnOKForM.setText("OK");
    btnOKForM.setMnemonic('O');
    btnOKForM.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnOKForM_actionPerformed(e);
      }
    });
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
    //    For Message
    lblMessage = new JCMSLabel();
    txtMessage = new JCMSTextArea();
    lblMessage.setLabelFor(txtMessage);
    lblMessage.setText("Message");
    lblMessage.setFont(theAppMgr.getTheme().getTextFieldFont());
    txtMessage.setText(this.question);
    txtMessage.setAppMgr(theAppMgr);
    txtMessage.setFont(theAppMgr.getTheme().getTextFieldFont());
    txtMessage.setBorder(BorderFactory.createLineBorder(Color.black));
    txtMessage.setBackground(Color.white);
    // For the Question
    lblQuestion = new JCMSLabel();
    lblAns = new JCMSLabel();
    txtQuestion = new JCMSTextArea();
    txtAns = new JCMSTextArea();
    txtAns.setBorder(BorderFactory.createLineBorder(Color.black));
    txtAns.setBackground(Color.white);
    lblQuestion.setLabelFor(txtQuestion);
    lblQuestion.setText("Question");
    lblQuestion.setFont(theAppMgr.getTheme().getTextFieldFont());
    txtQuestion.setText(this.question);
    txtQuestion.setAppMgr(theAppMgr);
    txtQuestion.setFont(theAppMgr.getTheme().getTextFieldFont());
    txtQuestion.setBorder(BorderFactory.createLineBorder(Color.black));
    txtQuestion.setBackground(Color.white);
    lblAns.setLabelFor(txtAns);
    lblAns.setText("Answer");
    lblAns.setFont(theAppMgr.getTheme().getTextFieldFont());
    txtAns.setText("");
    txtAns.setAppMgr(theAppMgr);
    txtAns.setFont(theAppMgr.getTheme().getTextFieldFont());
    this.getContentPane().add(mainPanel);
    mainPanel.setBackground(theAppMgr.getBackgroundColor());
    layoutForQAndA();
    layoutForM();
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

  /**
   * Fields Visibility in case messageType = "M"
   */
  public void setVisibleForMType() {
    txtMessage.setVisible(true);
    txtMessage.setEnabled(false);
    lblMessage.setVisible(true);
    txtQuestion.setVisible(false);
    lblQuestion.setVisible(false);
    txtAns.setVisible(false);
    lblAns.setVisible(false);
    btnOK.setVisible(false);
    btnCancel.setVisible(false);
    btnOKForM.setVisible(true);
  }

  /**
   * Fields Visibility in case messageType = "Q"
   */
  public void setVisibleForQandA() {
    txtMessage.setVisible(false);
    lblMessage.setVisible(false);
    txtQuestion.setVisible(true);
    lblQuestion.setVisible(true);
    txtAns.setVisible(true);
    lblAns.setVisible(true);
    btnOK.setVisible(true);
    btnCancel.setVisible(true);
    btnOKForM.setVisible(false);
    txtQuestion.setEnabled(false);
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
  void btnCancel_actionPerformed(ActionEvent e) {
    ok = false;
    dispose();
  }

  /**
   * put your documentation comment here
   * @param e
   */
  void btnOKForM_actionPerformed(ActionEvent e) {
    dispose();
  }

  /**
   * In case if 'OK' is pressed and the message Type = Q
   * @param e
   */
  void btnOK_actionPerformed(ActionEvent e) {
    CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject(
        "TXN_POS");
    CMSCustomer cmsCust = (CMSCustomer)theTxn.getCustomer();
    CMSCustomerMessage custMsg = cmsCust.getCustomerMessage();
    String response = txtAns.getText();
    if (!this.searchById) {
      custMsg.doSetCustomerId(cmsCust.getId());
      custMsg.doSetCustomerType(cmsCust.getCustomerType());
      custMsg.doSetMessage(" " + question);
      custMsg.doSetMessageType("Q");
      custMsg.doSetStatus("O");
      custMsg.doSetResponse(response);
      custMsg.doSetisSearchedById(false);
    } else if (this.searchById) {
      custMsg.doSetCustomerId(cmsCust.getId());
      custMsg.doSetCustomerType(cmsCust.getCustomerType());
      custMsg.doSetMessage(" " + question);
      custMsg.doSetMessageType("Q");
      custMsg.doSetStatus("P");
      custMsg.doSetResponse(response);
      custMsg.doSetisSearchedById(true);
    }
    try {
      cmsCust.setCustomerMessage(custMsg);
      theTxn.setCustomer(cmsCust);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    dispose();
  }

  /**
   * @return
   */
  public boolean isOK() {
    return ok;
  }

  /**
   * Layout in case if the message type is Q & A
   */
  public void layoutForQAndA() {
    mainPanel.add(txtAns
        , new GridBagConstraints(1, 1, 3, 1, 1.0, 0.0, GridBagConstraints.EAST
        , GridBagConstraints.NONE, new Insets(14, 20, 9, 85), 350, 27));
    mainPanel.add(lblQuestion
        , new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(19, 6, 0, 0), 20, 4));
    mainPanel.add(lblAns
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(24, 4, 29, 0), 20, 4));
    mainPanel.add(txtQuestion
        , new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0, GridBagConstraints.EAST
        , GridBagConstraints.NONE, new Insets(40, 20, 0, 85), 350, 27));
    mainPanel.add(btnCancel
        , new GridBagConstraints(3, 2, 1, 1, 1.0, 0.0, GridBagConstraints.EAST
        , GridBagConstraints.NONE, new Insets(4, 20, 52, 60), 0, 7));
    mainPanel.add(btnOK
        , new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(4, 7, 52, 0), 15, 9));
  }

  /**
   * Layout in case the message type is M
   */
  public void layoutForM() {
    mainPanel.add(lblMessage
        , new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(19, 6, 0, 0), 20, 4));
    mainPanel.add(txtMessage
        , new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(13, 0, -3, 7), 200, 27));
    mainPanel.add(btnOKForM
        , new GridBagConstraints(2, 10, 1, 1, 0.0, 0.0, GridBagConstraints.EAST
        , GridBagConstraints.NONE, new Insets(0, 70, 10, 40), 15, 9));
  }
}

