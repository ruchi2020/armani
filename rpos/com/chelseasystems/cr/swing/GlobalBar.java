/*
 * @copyright (c) 2002 Retek Inc.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.awt.image.ImageObserver;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.TextUI;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.mask.MaskManager;
import com.chelseasystems.cr.appmgr.mask.MaskConstants;
import com.chelseasystems.cr.swing.dlg.StatsDlg;


/**
 */
public class GlobalBar extends JPanel implements IGlobalBar, MaskConstants {
  private TimerThread timer;
  private String Command = null;
  private int theMask;
  private int anchor;
  private boolean isDefaultValue = false;
  private boolean showPendingDlg = true;
  private boolean showBrokenDlg = true;
  private Image imgBack = null;
  private Image imgEditAreaBorder = null;
  private Image imgStatusArea = null;
  private ImageIcon imgScroll = null;
  private ImageIcon imgScrollDown = null;
  private ImageIcon imgScrollUp = null;
  private HashMap hmLocations = new HashMap();
  private MaskManager theMaskMgr;
  private AppManager theAppMgr;
  private boolean alreadyBeenWarned;
  java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

  /** don't make this JCMSTextField or CMSFocusManager will send the focus away when Eu presses <Enter> */
  JPasswordField edtArea = new JPasswordField();
  JCMSWrapLabel labPrompt = new JCMSWrapLabel();
  JCMSLabel labTime = new JCMSLabel();
  JCMSLabel labUpDown = new JCMSLabel();
  JCMSLabel labScreen = new JCMSLabel();
  JCMSLabel labStore = new JCMSLabel();
  JCMSLabel labWork = new JCMSLabel();
  JCMSLabel labRegister = new JCMSLabel();
  NonFocusableButton btnHome = new NonFocusableButton();
  HelpButton btnHelp = new HelpButton();
  JCMSLabel labScroll = new JCMSLabel();
  JPanel pnlUpDown1 = new JPanel();
  JCMSToolTip ttip;
  Timer thinking = new Timer(200, new ThinkingAction());
  Timer training = new Timer(1000, new TrainingAction());
  Timer fontTimer = new Timer(750, new ActionListener() {
    int count = 1;

    /**
     * put your documentation comment here
     * @param ae
     */
    public void actionPerformed(ActionEvent ae) {
      count++;
      labUpDown.setText((count % 2 == 0) ? res.getString("Font Mode") : "");
    }
  });
  Timer buildHelpTimer = new Timer(750, new ActionListener() {
    int count = 1;

    /**
     * put your documentation comment here
     * @param ae
     */
    public void actionPerformed(ActionEvent ae) {
      count++;
      labUpDown.setText((count % 2 == 0) ? res.getString("Build Help Mode") : "");
    }
  });

  /**
   */
  public GlobalBar() {
    try {
      installListeners();
      timer = new TimerThread(labTime);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param anEvent
   */
  public void applicationKeyPressed(ActionEvent anEvent) {
    String command = anEvent.getActionCommand();
    if (command.equals(CMSSwingConstants.HELP_CMD)) {
      btnHelp.doClick();
    } else if (command.equals(CMSSwingConstants.HOME_CMD)) {
      btnHome.doClick();
    } else if (command.equals(CMSSwingConstants.UP_CMD)) {
      this.doClickPageUp();
    } else if (command.equals(CMSSwingConstants.DOWN_CMD)) {
      this.doClickPageDown();
    } else if (command.equals(CMSSwingConstants.STATS_CMD)) {
      this.labUpDown_mouseClicked(null);
    }
  }

  /**
   * Override this method to paint the background images in the proper orientation
   */
  public void paintComponent(Graphics g) {
    switch (anchor) {
      case IMainFrame.UPPER_LEFT_ANCHOR:
        g.drawImage(imgBack, getWidth(), getHeight(), 0, 0, 0, 0, getWidth(), getHeight()
            , getBackground(), this);
        break;
      case IMainFrame.UPPER_RIGHT_ANCHOR:
        g.drawImage(imgBack, 0, getHeight(), getWidth(), 0, 0, 0, getWidth(), getHeight()
            , getBackground(), this);
        break;
      case IMainFrame.BOTTOM_LEFT_ANCHOR:
        g.drawImage(imgBack, getWidth(), 0, 0, getHeight(), 0, 0, getWidth(), getHeight()
            , getBackground(), this);
        break;
      default:
        g.drawImage(imgBack, 0, 0, getWidth(), getHeight(), getBackground(), this);
    }
    Point[] pts = (Point[])hmLocations.get(imgEditAreaBorder);
    g.drawImage(imgEditAreaBorder, pts[anchor].x, pts[anchor].y, imgEditAreaBorder.getWidth(this)
        , imgEditAreaBorder.getHeight(this), this);
    pts = (Point[])hmLocations.get(imgStatusArea);
    g.drawImage(imgStatusArea, pts[anchor].x, pts[anchor].y, imgStatusArea.getWidth(this)
        , imgStatusArea.getHeight(this), this);
    super.paintChildren(g);
    g.dispose();
  }

  /**
   * @param theAppMgr
   */
  public void setAppMgr(AppManager theAppMgr) {
    this.theAppMgr = theAppMgr;
    theAppMgr.setLocale(this.getLocale());
    theMaskMgr = MaskManager.getInstance(theAppMgr);
    ttip = new JCMSToolTip(CMSSwingUtilities.getWindowAncestor(this), theAppMgr.getTheme(), "");
    this.setOnLine(theAppMgr.isOnLine());
  }

  /**
   * @see Theme
   */
  public void setAnchor(int anchor) {
    this.anchor = anchor;
    if (theAppMgr != null) {
      updateComponentsBounds();
    }
  }

  /**
   * @param aTheme A new <code>Theme</code> object to apply to this panel.
   */
  public void setTheme(Theme aTheme)
      throws Exception {
    loadImageInfo(aTheme);
    jbInit(aTheme);
    ttip = new JCMSToolTip(CMSSwingUtilities.getWindowAncestor(this), aTheme, "");
    updateComponentsBounds();
  }

  /**
   * @param aLocale a new locale to apply
   */
  public void setLocale(java.util.Locale aLocale) {
    res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    if (theAppMgr != null) {
      setPendingBroken(theAppMgr.getPendingTxnCount(), theAppMgr.getBrokenTxnCount());
    }
    try {
      setScreenName(theAppMgr.getAppletManager().getCurrentCMSApplet().getScreenName());
    } catch (NullPointerException ex) {}
    String text = "";
    if (labStore.getText().indexOf(": ") > 0) {
      text = labStore.getText().substring(labStore.getText().indexOf(": ") + 2);
    }
    setStoreNum(text);
    if (labRegister.getText().indexOf(": ") > 0) {
      text = labRegister.getText().substring(labRegister.getText().indexOf(": ") + 2);
    }
    setRegister(text);
    thinking.stop();
    thinking = new Timer(200, new ThinkingAction());
    super.setLocale(aLocale);
  }

  /**
   * setStatusMessage
   */
  public void setStatusMessage(String sMsg) {
    try {
      labPrompt.setText(sMsg);
    } catch (Exception ex) {}
  }

  /**
   * put your documentation comment here
   * @param Msg
   * @param Command
   * @param initialValue
   * @param mask
   */
  public void setSingleEdit(String Msg, String Command, Object initialValue, int mask) {
    if (Command == null) {
      isDefaultValue = false;
      setStatusMessage(Msg);
      //this.requestFocus();
      this.Command = null;
      setTextAreaEnabled(false);
      edtArea.setText("");
    } else {
      if (theMaskMgr.isMaskProtected(mask)) {
        edtArea.setSelectedTextColor(Color.white);
        setEchoEnabled(false);
      } else {
        edtArea.setSelectedTextColor(theAppMgr.getTheme().getSingleEditTextColor());
        setEchoEnabled(true);
      }
      setStatusMessage(Msg);
      this.Command = Command;
      this.theMask = mask;
      if (theMask == PIECEMEAL_MASK) {
        edtArea.setDocument(new javax.swing.text.PlainDocument());
        edtArea.getDocument().addDocumentListener(new IncrementalListener());
      } else {
        edtArea.getDocument().removeDocumentListener(new IncrementalListener());
      }
      if (initialValue == null) {
        edtArea.setText("");
        isDefaultValue = false;
        // this gives the mask a chance to clear any values it saves
        // from the initial value
        theMaskMgr.validateInitialValueType(null, mask);
      } else {
        isDefaultValue = true;
        if (theMaskMgr.validateInitialValueType(initialValue, mask)) {
          final Object value = initialValue;
          final int finalMask = mask;
          SwingUtilities.invokeLater(new Runnable() {

            /**
             * put your documentation comment here
             */
            public void run() {
              populateInitialValue(value, finalMask);
            }
          });
        } else {
          edtArea.setText("");
        }
      }
      setTextAreaEnabled(true);
      this.setTextAreaFocus();
    }
  }

  /**
   * @param echoOn
   */
  private void setEchoEnabled(boolean echoOn) {
    if (echoOn) {
      edtArea.setUI((TextUI)UIManager.getDefaults().getUI(new JTextField()));
    } else {
      edtArea.setUI((TextUI)UIManager.getDefaults().getUI(edtArea));
    }
  }

  /**
   * @param newColor
   */
  public void setTransitionColor(Color newColor) {
    this.setBackground(newColor);
  }

  /**
   * set the initial value for the edit area.
   * also highlights the value so it is easy to overwrite
   */
  private void populateInitialValue(Object initialValue, int mask) {
    edtArea.setText(theMaskMgr.getRenderedValue(initialValue, mask));
    /*     if(initialValue instanceof String)
     {
     edtArea.setText((String)initialValue);
     }
     else if(initialValue instanceof Integer)
     {
     Integer integer = (Integer)initialValue;
     edtArea.setText(integer.toString());
     }
     else if(initialValue instanceof Double)
     {
     Double dbl = (Double)initialValue;
     edtArea.setText(dbl.toString());
     }
     else if(initialValue instanceof ArmCurrency)
     {
     ArmCurrency currency = (ArmCurrency)initialValue;
     edtArea.setText(currency.stringValue());
     }
     else if(initialValue instanceof Date)
     {
     Date date = (Date)initialValue;
     SimpleDateFormat fmt = new SimpleDateFormat("M/dd/yyyy");
     edtArea.setText(fmt.format(date));
     } */
    isDefaultValue = true;
  }

  /**
   * setOnLine
   */
  private void setOnLine(boolean bFlag) {
    Boolean trainingMode = (Boolean)theAppMgr.getGlobalObject("TRAINING");
    boolean bTraining = (trainingMode != null && trainingMode.booleanValue());
    setUpDownLabel(bFlag, bTraining, theAppMgr.getPendingTxnCount(), theAppMgr.getBrokenTxnCount());
  }

  /**
   * setPendingTxnNum
   */
  public void setPendingBroken(int pendingTxn, int brokenTxn) {
    Boolean trainingMode = (theAppMgr != null) ? (Boolean)theAppMgr.getGlobalObject("TRAINING") : null;
    boolean bTraining = (trainingMode != null && trainingMode.booleanValue());
    boolean isOnline = (theAppMgr != null) ? theAppMgr.isOnLine() : false;
    setUpDownLabel(isOnline, bTraining, pendingTxn, brokenTxn);
  }

  /**
   */
  private void setUpDownLabel(boolean online, boolean training, int pending, int broken) {
    String text = res.getString("On-Line Mode");
    if (training) {
      text = res.getString("Training Mode");
    } else if (!online) {
      text = res.getString("Off-Line Mode");
    }
    if (pending > 0 || broken > 0) {
      text = text + " - " + pending + ":" + broken;
    }
    if (broken == 0) {
      alreadyBeenWarned = false;
    }
    if (broken > 0 && !alreadyBeenWarned) {
      alreadyBeenWarned = true;
      IMainFrame frame = theAppMgr.getMainFrame();
      if (frame == null || frame.isVisible()) {
        theAppMgr.showErrorDlgLater(res.getString("A broken transaction has been detected.  "
            + "Please alert store managment and call the help desk."));
      } else {
        // hack for ICE Linux windowing causing dlg behind mainframe - CG
        ((Window)frame).addWindowListener(new WindowAdapter() {

          /**
           * put your documentation comment here
           * @param we
           */
          public void windowOpened(WindowEvent we) {
            theAppMgr.showErrorDlgLater(res.getString("A broken transaction has been detected.  "
                + "Please alert store managment and call the help desk."));
          }
        });
      }
    }
    labUpDown.setText(text);
    if (online) {
      if (broken == 0) {
        labUpDown.setBackground(theAppMgr.getTheme().getColorSignalNormal()); //green
        labUpDown.setForeground(theAppMgr.getTheme().getStatusAreaTextColor());
      } else {
        labUpDown.setBackground(theAppMgr.getTheme().getColorSignalWarning()); //yellow
        labUpDown.setForeground(theAppMgr.getTheme().getStatusAreaTextColor());
      }
    } else if (theAppMgr != null) {
      labUpDown.setBackground(theAppMgr.getTheme().getColorSignalDanger()); //red
      labUpDown.setForeground(Color.white);
    }
  }

  /**
   * @param workIsInProgress
   * @see IWorkInProgressListener
   */
  public void workInProgressEvent(boolean workIsInProgress) {
    if (theAppMgr == null) {
      return;
    }
    AppletManager appletMgr = theAppMgr.getAppletManager();
    if (workIsInProgress) {
      thinking.start();
      theAppMgr.getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    } else {
      thinking.stop();
      if (appletMgr != null && appletMgr.getCurrentCMSApplet() != null) {
        setScreenName(appletMgr.getCurrentCMSApplet().getScreenName());
      } else {
        setScreenName("");
      }
      theAppMgr.getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  /**
   * @param start <code>true</code> if animation should start, <code>false</code> if not.
   */
  public void toggleTrainingAnimation(boolean start) {
    if (start) {
      labUpDown.setText(res.getString("Training Mode"));
      training.start();
    } else {
      training.stop();
    }
  }

  /**
   */
  public void toggleHelpButton() {
    btnHelp.doClick();
  }

  /**
   * @param start <code>true</code> if font choosing mode animation should start,
   * <code>false</code> if not.
   */
  public void toggleFontAnimation(boolean start) {
    if (start) {
      fontTimer.start();
    } else {
      fontTimer.stop();
    }
  }

  /**
   * @param start <code>true</code> if build help mode animation should start,
   * <code>false</code> if not.
   */
  public void toggleBuildHelpAnimation(boolean start) {
    if (start) {
      buildHelpTimer.start();
    } else {
      buildHelpTimer.stop();
    }
  }

  /**
   */
  public void setScreenName(String sName) {
    labScreen.setText(sName);
  }

  /**
   * @param sStoreNum
   */
  public void setStoreNum(String sStoreNum) {
    labStore.setText(res.getString("Store") + ": " + sStoreNum);
  }

  /**
   * @param sRegisterNum
   */
  public void setRegister(String sRegisterNum) {
    labRegister.setText(res.getString("gbar_reg") + ": " + sRegisterNum);
  }

  /**
   * @param sUser
   * This implementation is not presently displaying the current user info...   djr
   */
  public void setUser(String sUser) {}

  /**
   * @param sUser
   * This implementation is not presently displaying a passive status message...   djr
   */
  public void setPassiveStatusMessage(String sMessage) {}

  /**
   * @param sUser
   * This implementation is not presently displaying a passive status message...   djr
   */
  public void setPassiveStatusMessageCleared() {}

  /**
   */
  public void doClickPageUp() {
    final MouseEvent me = new MouseEvent(labScroll, MouseEvent.MOUSE_PRESSED
        , System.currentTimeMillis(), 1, 1, 0, 1, false);
    labScroll.setIcon(imgScrollUp);
    theAppMgr.getAppletManager().getCurrentCMSApplet().pageUp(me);
    ReleaseClick release = new ReleaseClick();
    release.start();
  }

  /**
   */
  public void doClickPageDown() {
    final MouseEvent me = new MouseEvent(labScroll, MouseEvent.MOUSE_PRESSED
        , System.currentTimeMillis(), labScroll.getWidth() - 1, labScroll.getHeight() - 1, 0, 1, false);
    labScroll.setIcon(imgScrollDown);
    theAppMgr.getAppletManager().getCurrentCMSApplet().pageDown(me);
    ReleaseClick release = new ReleaseClick();
    release.start();
  }

  /**
   * @param e
   */
  void btnHome_actionPerformed(ActionEvent e) {
    theAppMgr.goHome();
    this.setTextAreaFocus();
  }

  /**
   */
  public void downTimeEvent(boolean isOnline) {
    setOnLine(isOnline);
  }

  /**
   * Method to send call bcak to Application that help was pressed. For some
   * reason a JToggleButton will not display the pressed icon when selected,
   * so we add code to explicitly set the icon.
   * @param e
   */
  void btnHelp_actionPerformed(ActionEvent e) {
    btnHelp.setIcon(btnHelp.isSelected() ? theAppMgr.getTheme().getHelpDown()
        : theAppMgr.getTheme().getHelpUp());
    theAppMgr.showHelp(btnHelp.isSelected());
  }

  /**
   * created a new single edit area.
   * this is an attempt to keep edit area from locking up
   */
  public void allocEditArea() {
    JPasswordField copyArea = new JPasswordField();
    copyArea.addActionListener(new java.awt.event.ActionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        edtArea_actionPerformed(e);
      }
    });
    copyArea.addKeyListener(new java.awt.event.KeyAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void keyTyped(KeyEvent e) {
        edtArea_keyPressed(e);
      }
    });
    copyArea.setBounds(edtArea.getBounds());
    copyArea.setSelectionColor(copyArea.getSelectedTextColor());
    copyArea.setSelectedTextColor(edtArea.getSelectedTextColor());
    copyArea.setForeground(edtArea.getForeground());
    copyArea.setFont(edtArea.getFont());
    copyArea.setName("EDIT");
    //TD
    //copyArea.setNextFocusableComponent(copyArea);
    this.remove(edtArea);
    edtArea = copyArea;
    this.add(edtArea);
  }

  /**
   * @param e
   */
  void edtArea_keyPressed(KeyEvent e) {
    if (isDefaultValue && e.getKeyChar() != '\r') {
      isDefaultValue = false;
      edtArea.setText("");
    }
  }

  /**
   * Typcially used by JPOS scanners.  This method will place specified text
   * into the single edit area and trigger an ActionEvent on the edit area.
   * <P>
   * If the data is null or empty string, this method will ignore it.
   * @param data
   */
  public void textInputEvent(String data) {
    if (data == null || data.length() == 0) {
      return;
    }
    edtArea.setText(data);
    edtArea_actionPerformed(null);
  }

  /**
   * callback method based on type passed
   */
  void edtArea_actionPerformed(ActionEvent e) {
    // check for null commend
    if (Command == null) {
      return;
    }
    //TD
    //String value = edtArea.getText();
    String value = new String(edtArea.getPassword());
    switch (theMask) {
      case PIECEMEAL_MASK:
        edtArea.setDocument(new javax.swing.text.PlainDocument()); // kills old listener
        edtArea.setText(value);
        theAppMgr.editAreaEvent(PIECEMEAL_COMPLETED, value, value, theMask);
        break;
      case CREDIT_CARD_MASK:
      case INPUT_IS_OPTIONAL_MASK:
      case NO_MASK:
        theAppMgr.editAreaEvent(Command, value, value, theMask);
        break;
      default:
        Object objResult = theMaskMgr.getMaskVerify(value, theMask);
        if (objResult != null) {
          theAppMgr.editAreaEvent(Command, value, objResult, theMask);
        } else {
          edtArea.setText("");
        }
    }
  }

  /**
   */
  public void setTextAreaFocus() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        btnHome.transferFocus();
        edtArea.requestFocus();
      }
    });
  }

  /**
   * @param bFlag
   */
  public void setTextAreaEnabled(boolean bFlag) {
    //TD
    if (bFlag) {
      edtArea.setEnabled(true);
      edtArea.setEditable(true);
      edtArea.setBackground(Color.white);
      edtArea.setCaretColor(Color.black);
    } else {
      //edtArea.setEnabled(false);
      edtArea.setEditable(false);
      edtArea.setBackground(Color.lightGray);
      edtArea.setCaretColor(Color.lightGray);
    }
    edtArea.requestFocusInWindow();
    edtArea.repaint();
  }

  /**
   */
  public boolean isTextAreaEnabled() {
    return (edtArea.isEnabled());
  }

  /**
   * @param e
   */
  private void labScroll_mousePressed(MouseEvent e) {
    // the extra negative signs are because Point locations are
    // from upper-left instead of bottom left
    CMSApplet app = theAppMgr.getAppletManager().getCurrentCMSApplet();
    double width = labScroll.getWidth();
    // y[0] = the y value at (x = 0)
    // y[1] = the y value at (x = width)
    int[] y = (int[])hmLocations.get("SCROLL.MIDLINE");
    // m = (y2 - y1) / (x2 - x1)
    double m = ( -y[1] - -y[0]) / width;
    // y = m * x + b;
    if ( -e.getY() < m * e.getX() + -y[0]) {
      labScroll.setIcon(imgScrollDown);
      app.pageDown(e);
    } else {
      labScroll.setIcon(imgScrollUp);
      app.pageUp(e);
    }
    labScroll.repaint();
  }

  /**
   * @param e
   */
  private void labScroll_mouseReleased(MouseEvent e) {
    labScroll.setIcon(imgScroll);
    labScroll.repaint();
  }

  /**
   * @param e
   * @param tipText
   */
  public void showLabScrollToolTip(MouseEvent e, String tipText) {
    ttip.setText(tipText);
    ttip.show(e);
  }

  /**
   * @param e
   */
  private void labUpDown_mouseClicked(MouseEvent e) {
    theAppMgr.setWorkInProgress(true);
    StatsDlg dlg = new StatsDlg(theAppMgr);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    dlg.setBounds(0, dim.height - dlg.getHeight(), dlg.getWidth(), dlg.getHeight());
    theAppMgr.setWorkInProgress(false);
    dlg.setVisible(true);
    try {
      if (dlg.isAnchorSelectionChanged()) {
        theAppMgr.setAnchor(dlg.getAnchorSelection());
      }
      if (dlg.isThemeSelectionChanged()) {
        ThemeManager themeMgr = new ThemeManager();
        String key = dlg.getThemeSelection();
        Theme newTheme = themeMgr.getTheme(key);
        if (!theAppMgr.getTheme().equals(newTheme)) {
          dlg.dispose();
          dlg = null;
          theAppMgr.setTheme(newTheme);
          themeMgr.setDefaultTheme(key);
        }
      }
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
    setTextAreaFocus();
  }

  /**
   */
  private void installListeners() {
    edtArea.addActionListener(new java.awt.event.ActionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        edtArea_actionPerformed(e);
      }
    });
    edtArea.addKeyListener(new java.awt.event.KeyAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void keyTyped(KeyEvent e) {
        edtArea_keyPressed(e);
      }
    });
    labUpDown.addMouseListener(new MouseAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        labUpDown_mouseClicked(e);
      }
    });
    labScroll.addMouseListener(new MouseAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void mousePressed(MouseEvent e) {
        labScroll_mousePressed(e);
      }

      /**
       * put your documentation comment here
       * @param e
       */
      public void mouseReleased(MouseEvent e) {
        labScroll_mouseReleased(e);
      }
    });
    btnHome.addActionListener(new java.awt.event.ActionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnHome_actionPerformed(e);
      }
    });
    btnHelp.addActionListener(new java.awt.event.ActionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnHelp_actionPerformed(e);
      }
    });
  }

  /**
   */
  private void loadImageInfo(Theme theme) {
    btnHome.setIcon(theme.getHomeUp());
    btnHome.setPressedIcon(theme.getHomeDown());
    hmLocations.put(btnHome, theme.getHomeLocations());
    btnHelp.setIcon(theme.getHelpUp());
    btnHelp.setPressedIcon(theme.getHelpDown());
    hmLocations.put(btnHelp, theme.getHelpLocations());
    imgScroll = theme.getScrollBase();
    imgScrollDown = theme.getScrollDown();
    imgScrollUp = theme.getScrollUp();
    hmLocations.put(labScroll, theme.getScrollLocations());
    hmLocations.put("SCROLL.MIDLINE", theme.getScrollMidLine());
    imgEditAreaBorder = theme.getEditAreaBorder().getImage();
    hmLocations.put(imgEditAreaBorder, theme.getEditAreaBorderLocations());
    imgStatusArea = theme.getStatusArea().getImage();
    hmLocations.put(imgStatusArea, theme.getStatusAreaLocations());
    hmLocations.put(edtArea, theme.getEditAreaBounds());
    hmLocations.put(labPrompt, theme.getPromptAreaBounds());
    hmLocations.put(labUpDown, theme.getStatusOnlineBounds());
    hmLocations.put(labScreen, theme.getStatusScreenBounds());
    hmLocations.put(labStore, theme.getStatusStoreBounds());
    hmLocations.put(labRegister, theme.getStatusRegisterBounds());
    hmLocations.put(labTime, theme.getStatusTimeBounds());
    imgBack = theme.getGlobalBar().getImage();
  }

  /**
   */
  protected void updateComponentsBounds() {
    Point[] pts = (Point[])hmLocations.get(btnHome);
    btnHome.setBounds(pts[anchor].x, pts[anchor].y, btnHome.getIcon().getIconWidth()
        , btnHome.getIcon().getIconHeight());
    pts = (Point[])hmLocations.get(btnHelp);
    btnHelp.setBounds(pts[anchor].x, pts[anchor].y, btnHelp.getIcon().getIconWidth()
        , btnHelp.getIcon().getIconHeight());
    pts = (Point[])hmLocations.get(labScroll);
    labScroll.setBounds(pts[anchor].x, pts[anchor].y, labScroll.getIcon().getIconWidth()
        , labScroll.getIcon().getIconHeight());
    Rectangle[] rct = (Rectangle[])hmLocations.get(edtArea);
    edtArea.setBounds(rct[anchor]);
    rct = (Rectangle[])hmLocations.get(labPrompt);
    labPrompt.setBounds(rct[anchor]);
    rct = (Rectangle[])hmLocations.get(labUpDown);
    labUpDown.setBounds(rct[anchor]);
    rct = (Rectangle[])hmLocations.get(labScreen);
    labScreen.setBounds(rct[anchor]);
    rct = (Rectangle[])hmLocations.get(labStore);
    labStore.setBounds(rct[anchor]);
    rct = (Rectangle[])hmLocations.get(labRegister);
    labRegister.setBounds(rct[anchor]);
    rct = (Rectangle[])hmLocations.get(labTime);
    labTime.setBounds(rct[anchor]);
  }

  /**
   * @exception Exception
   */
  private void jbInit(Theme theme)
      throws Exception {
    this.setMinimumSize(new Dimension(imgBack.getWidth(this), imgBack.getHeight(this)));
    this.setPreferredSize(this.getMinimumSize());
    this.setMaximumSize(this.getMinimumSize());
    this.setLayout(null);
    /****************************
     // edit area
     *****************************/
    this.add(edtArea);
    edtArea.setSelectionColor(Color.white);
    edtArea.setSelectedTextColor(theme.getSingleEditTextColor());
    edtArea.setForeground(theme.getSingleEditTextColor());
    edtArea.setFont(theme.getEditAreaFont());
    edtArea.setName("EDIT");
    //TD
    //edtArea.setNextFocusableComponent(edtArea);
    this.setRequestFocusEnabled(false);
    /***********************
     // status area
     ************************/
    this.add(labPrompt);
    if (theme.getPromptAreaColor() != null) {
      labPrompt.setForeground(theme.getPromptAreaColor());
    } else {
      labPrompt.setForeground(theme.getMsgTextColor());
    }
    labPrompt.setFont(theme.getPromptAreaFont());
    labPrompt.setName("PROMPT");
    // time
    this.add(labTime);
    labTime.setForeground(theme.getStatusAreaTextColor());
    labTime.setFont(theme.getStatusAreaFont());
    labTime.setName("STATUS");
    labTime.setHorizontalAlignment(0);
    // up / down flag
    this.add(labUpDown);
    labUpDown.setBackground(Color.green);
    labUpDown.setOpaque(true);
    labUpDown.setHorizontalAlignment(0);
    labUpDown.setForeground(theme.getStatusAreaTextColor());
    labUpDown.setFont(theme.getStatusAreaFont());
    labUpDown.setName("STATUS");
    // Screen Name
    this.add(labScreen);
    labScreen.setForeground(theme.getStatusAreaTextColor());
    labScreen.setFont(theme.getStatusAreaFont());
    labScreen.setName("STATUS");
    labScreen.setHorizontalAlignment(0);
    // store number
    this.add(labStore);
    labStore.setForeground(theme.getStatusAreaTextColor());
    labStore.setFont(theme.getStatusAreaFont());
    labStore.setName("STATUS");
    labStore.setHorizontalAlignment(0);
    // register number
    this.add(labRegister);
    labRegister.setForeground(theme.getStatusAreaTextColor());
    labRegister.setFont(theme.getStatusAreaFont());
    labRegister.setName("STATUS");
    labRegister.setHorizontalAlignment(0);
    // scrolling
    this.add(labScroll);
    labScroll.setIcon(imgScroll);
    labScroll.setOpaque(false);
    labScroll.setHorizontalTextPosition(0);
    // home button
    this.add(btnHome);
    btnHome.setContentAreaFilled(false);
    btnHome.setBorderPainted(false);
    btnHome.setOpaque(false);
    btnHome.setFocusPainted(false);
    btnHome.setDefaultCapable(false);
    btnHome.setRequestFocusEnabled(false);
    //TD
    //btnHome.setNextFocusableComponent(edtArea);
    btnHome.setHorizontalTextPosition(0);
    // help button
    /*  Armani does not use Help -- DF
     this.add(btnHelp);
     btnHelp.setContentAreaFilled(false);
     btnHelp.setBorderPainted(false);
     btnHelp.setOpaque(false);
     btnHelp.setFocusPainted(false);
     btnHelp.setHorizontalTextPosition(0)
     */
    // others
    thinking.setInitialDelay(0);
    //TD
    SwingUtilities.invokeLater(new Runnable(){
	public void run(){
		doSetPolicy();
	}
    });
  }
	
  private void doSetPolicy() {  
	getFocusCycleRootAncestor().setFocusTraversalPolicy(new GlobalBarFocusPolicy(getFocusCycleRootAncestor().getFocusTraversalPolicy()));
  }

  /*******************************************************************/
  /**
   * Inner class to trigger action events call backs for every char typed
   *    into the edit area
   */
  class IncrementalListener implements DocumentListener {

    /**
     * put your documentation comment here
     * @param e
     */
    public void insertUpdate(DocumentEvent e) {
      if (e.getLength() > 0) {
        //TD
        //theAppMgr.editAreaEvent(Command, edtArea.getText(), edtArea.getText(), theMask);
      }
    }

    /**
     * put your documentation comment here
     * @param e
     */
    public void removeUpdate(DocumentEvent e) {}

    /**
     * put your documentation comment here
     * @param e
     */
    public void changedUpdate(DocumentEvent e) {}

    /**
     * put your documentation comment here
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
      return (obj instanceof IncrementalListener);
    }
  }


  /**
   * Inner class for call to pageUp/Down_doClick to wait before releasing.
   */
  class ReleaseClick extends Thread {

    /**
     * put your documentation comment here
     */
    public void run() {
      try {
        Thread.sleep(100);
      } catch (Exception ex) {}
      labScroll_mouseReleased(null);
    }
  }


  /**
   * Inner class needed to specify attributes for help button
   */
  public class HelpButton extends JToggleButton {

    /**
     * @return false to prevent FocusManager from passing focus.
     */
    //TD
    public boolean isFocusable(){ 
      return (false);
    }
  }


  /**
   * Inner class to respond to thinking timer events.
   */
  class ThinkingAction implements ActionListener {
    String THINKING = res.getString("Thinking") + "...";

    /**
     */
    public void actionPerformed(ActionEvent e) {
      String text = labScreen.getText();
      if (THINKING.equals(text) || !THINKING.startsWith(text)) {
        labScreen.setText("");
      } else {
        labScreen.setText(THINKING.substring(0, text.length() + 1));
      }
      labScreen.paintImmediately(labScreen.getVisibleRect());
    }
  }


  /**
   * Inner class to respond to training timer events.
   */
  class TrainingAction implements ActionListener {
    int count = 1;

    /**
     */
    public void actionPerformed(ActionEvent e) {
      count++;
      labUpDown.setForeground((count % 2 == 0) ? theAppMgr.getTheme().getStatusAreaTextColor()
          : labUpDown.getBackground());
    }
  }

	class GlobalBarFocusPolicy extends FocusTraversalPolicy {
		FocusTraversalPolicy ftp = null;
		public GlobalBarFocusPolicy( FocusTraversalPolicy fPolicy ) {
			ftp = fPolicy;
		}
		
		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			if (aComponent.equals(edtArea) && edtArea.isEnabled() ) {
                return edtArea;
            } else if (aComponent.equals(btnHome)) {
                return edtArea;
            } else {
            	return ftp.getComponentAfter(focusCycleRoot, aComponent);
            }
		}
		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			if (aComponent.equals(edtArea) && edtArea.isEnabled()) {
                return edtArea;
            } else if (aComponent.equals(btnHome)) {
                return edtArea;
            } else {
            	return ftp.getComponentBefore(focusCycleRoot, aComponent);
            }
		}
		public Component getDefaultComponent(Container focusCycleRoot) {
            return ftp.getDefaultComponent(focusCycleRoot);
		}
		public Component getLastComponent(Container focusCycleRoot) {
            return ftp.getLastComponent(focusCycleRoot);
		}
		public Component getFirstComponent(Container focusCycleRoot) {
            return ftp.getFirstComponent(focusCycleRoot);
		}
	}
}

