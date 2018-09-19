/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 08/23/2005 | Vikram    | 626       | Created to handle JCMSTable with Checkbox - select.|
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.bean;

import javax.swing.table.*;
import com.chelseasystems.cr.swing.bean.*;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JTextField;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ArmJCMSTable extends JCMSTable {
  boolean spaceCaptureEnabled = true;

  /**
   * put your documentation comment here
   */
  public ArmJCMSTable() {
    this(SELECT_ROW);
  }

  /**
   * put your documentation comment here
   * @param   int rowType
   */
  public ArmJCMSTable(int rowType) {
    this(null, rowType);
  }

  /**
   * put your documentation comment here
   * @param   TableModel model
   * @param   int rowType
   */
  public ArmJCMSTable(TableModel model, int rowType) {
    super(model, rowType);
    spaceCaptureEnabled = true;
  }

  /**
   * put your documentation comment here
   */
  public void registerKeyboardActions() {
    registerKeyboardAction(this, "SPACE_CMD", KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0)
        , WHEN_FOCUSED);
    registerKeyboardAction(this, "SPACE_CMD", KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0)
        , WHEN_IN_FOCUSED_WINDOW);
    super.registerKeyboardActions();
  }

  /**
   * put your documentation comment here
   */
  public void unregisterKeyboardActions() {
    unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
    super.unregisterKeyboardActions();
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    if (command.equals("SPACE_CMD") && spaceCaptureEnabled) {
      boolean processed = false;
      Component[] components = ((Container)getMainFrame().getGlobalBar()).getComponents();
      for (int i = 0; i < components.length; i++) {
        if (components[i] instanceof JTextField) {
          processed = true;
          if (((JTextField)components[i]).getText().trim().length() == 0) {
            components[i].dispatchEvent(new KeyEvent(this, KeyEvent.KEY_PRESSED
                , System.currentTimeMillis(), 0, KeyEvent.VK_ENTER));
          }
        }
      }
      if (!processed)
        processMouseEvent(new MouseEvent(this, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis()
            , 0, 0, 0, 1, false));
    }
    super.actionPerformed(e);
    if ((command.equals("SPACE_CMD") || command.equals("SELECT_CMD"))
        && getSelectionModel().getAnchorSelectionIndex() > -1
        && getSelectionModel().getLeadSelectionIndex() > -1) {
      setRowSelectionInterval(getSelectionModel().getAnchorSelectionIndex()
          , getSelectionModel().getLeadSelectionIndex());
    }
  }

  /**
   * put your documentation comment here
   * @param enable
   */
  public void enableSpaceCapture(boolean enable) {
    spaceCaptureEnabled = enable;
  }
}

