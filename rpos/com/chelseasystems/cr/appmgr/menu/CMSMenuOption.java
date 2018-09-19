/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.appmgr.menu;

import javax.swing.JButton;
import javax.swing.KeyStroke;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RuleEngine;
import com.chelseasystems.cr.swing.NonFocusableButton;
import com.chelseasystems.cr.user.Accessible;
import com.chelseasystems.cr.user.Location;
import com.chelseasystems.cr.user.User;


/**
 */
public class CMSMenuOption implements java.io.Serializable, IRuleEngine, Accessible {
  private static final long serialVersionUID = 2775144973918825159L;
  private String label;
  private String command;
  private String shortcut; //should be KeyStroke when Swing is rmi compatible
  private boolean showOffLine = false;
  private boolean isVisible = false;
  private long assignedRoles;

  /**
   */
  public CMSMenuOption() {
    this("");
  }

  /**
   * @param    String command
   */
  public CMSMenuOption(String command) {
    this.command = command;
  }

  /**
   * To hide buttons instaed of allowing them to show beased on business
   *    rules, attch rules to buttons specifying this method.
   * @param offline
   * @param objs
   * @return
   */
  public JButton isShowable(User user, Location location)
      throws BusinessRuleException {
    RuleEngine.execute(command + ".isShowable", this, new Object[] {user, location
    });
    NonFocusableButton btn = new NonFocusableButton(label);
    btn.setActionCommand(command);
    btn.setMenuOption(this);
    return btn;
  }

  /**
   * To get application to display attached rules instead of hiding buttons,
   *    attached rules to buttons specifying this method.  The default
   *    behaviour of CMSApplet if to show a dialog.  Any subclass of CMSApplet
   *    can override this behaviour.
   * @throws BusinessRuleException
   */
  public void testIsActionAllowed()
      throws BusinessRuleException {
    RuleEngine.execute(command + ".testIsActionAllowed", this, new Object[0]);
  }

  /**
   * @return
   */
  public String getLabel() {
    return label;
  }

  /**
   * @return
   */
  public String getCommand() {
    return command;
  }

  /**
   * @return
   */
  public boolean isOffLine() {
    return showOffLine;
  }

  /**
   * @return
   */
  public boolean isVisible() {
    return isVisible;
  }

  /**
   * @param label
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * @param command
   */
  public void setCommand(String command) {
    this.command = command;
  }

  /**
   * @param offLine
   */
  public void setOffLine(boolean offline) {
    this.showOffLine = offline;
  }

  /**
   * @param shortcut
   */
  public void setShortcutKey(KeyStroke keyStroke) {
    this.shortcut = keyStroke.toString();
  }

  /**
   * @return KeyStroke
   */
  public KeyStroke getShortcutKey() {
    return KeyStroke.getKeyStroke(shortcut);
  }

  /**
   * @param visible
   */
  public void setVisible(boolean visible) {
    this.isVisible = visible;
  }

  /**
   * @deprecated as of 8/13/2002.  Use RuleEngine.addRule()
   * @param aRule
   */
  public void addRule(Rule aRule)
      throws Exception {
    RuleEngine.addRule(command, aRule.getClass().getName());
  }

  /**
   * @deprecated as of 8/13/2002.  Use RuleEngine.deleteRule()
   * @param aRule
   */
  public void removeRule(Rule aRule)
      throws Exception {
    RuleEngine.deleteRule(command, aRule.getClass().getName());
  }

  /**
   * @deprecated as of 8/13/2002.  Use RuleEngine.getRules()
   * @return
   */
  public String[] getRules() {
    try {
      return RuleEngine.getRules(command);
    } catch (Exception ex) {
      System.out.println("Exception CMSMenuOption.getRules()->" + ex);
      return null;
    }
  }

  /**
   * @param assignedRole
   */
  public void setAssignedRoles(long assignedRoles) {
    this.assignedRoles = assignedRoles;
  }

  /**
   * @return
   */
  public long getAssignedRoles() {
    return assignedRoles;
  }
}

