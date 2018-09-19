/*
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing;

import com.chelseasystems.cr.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import javax.swing.InputMap;
import javax.swing.AbstractAction;


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
public class CMSScrollableToolBarPanel extends ScrollableToolBarPanel {

  /**
   */
  public void registerKeyboardActions() {
    KeyAction keyAction = new KeyAction();
    registerKeyboardAction(keyAction, "PREV", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)
        , WHEN_IN_FOCUSED_WINDOW);
    super.registerKeyboardActions();
  }

  /**
   */
  public void unRegisterKeyboardActions() {
    unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
    super.unRegisterKeyboardActions();
  }

  private class KeyAction extends AbstractAction {

    /**
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
      fireApplicationKeyEvent(new CMSActionEvent(e));
    }
  }
}

