/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.model.EmpListModel;


/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | N/A        | N/A       | N/A       | Base Version                                       |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-26-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
/**
 */
public class EmpListPanel extends JPanel {
  EmpListModel model;
  JCMSTable tblEmp;

  /**
   */
  public EmpListPanel() {
    try {
      model = new EmpListModel();
      tblEmp = new JCMSTable(model, JCMSTable.SELECT_ROW);
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param   String sColIdentifiers[]
   */
  public EmpListPanel(String sColIdentifiers[]) {
    try {
      model = new EmpListModel(sColIdentifiers);
      tblEmp = new JCMSTable(model, JCMSTable.SELECT_ROW);
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    this.setLayout(new BorderLayout());
    tblEmp.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        resetColumnWidths();
      }
    });
    this.add(tblEmp.getTableHeader(), BorderLayout.NORTH);
    this.add(tblEmp, BorderLayout.CENTER);
    tblEmp.setRequestFocusEnabled(false);
    tblEmp.setCellSelectionEnabled(false);
    tblEmp.setColumnSelectionAllowed(false);
    tblEmp.setRowSelectionAllowed(true);
  }

  /**
   * @param emps
   */
  public void setEmployees(Employee[] emps) {
    clear();
    for (int x = 0; x < emps.length; x++) {
      addEmployee(emps[x]);
    }
    sortEmp();
  }

  /**
   * @param emp
   */
  public void addEmployee(Employee emp) {
    model.addEmployee(emp);
  }

  /**
   * @param l mouse listener added to table
   */
  public void addMouseListener(MouseListener l) {
    tblEmp.addMouseListener(l);
  }

  /**
   */
  public void clear() {
    model.clear();
  }

  /**
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    tblEmp.getTableHeader().setBackground(theAppMgr.getBackgroundColor());
  }

  /**
   */
  public Employee getSelectedEmployee() {
    int row = tblEmp.getSelectedRow();
    return (row > -1) ? model.getEmployee(row) : null;
  }

  /**
   * @return
   */
  public JCMSTable getTable() {
    return tblEmp;
  }

  /**
   * @param e
   */
  public void resetColumnWidths() {
    model.setColumnWidth(tblEmp);
    model.setRowsShown(tblEmp.getHeight() / tblEmp.getRowHeight());
  }

  /**
   * @param e
   */
  public void doClick() {
    int index = tblEmp.getSelectedRow();
    if (index >= 0) {
      Rectangle r = tblEmp.getCellRect(index, 0, true);
      int x = r.x + r.width / 2;
      int y = r.y + r.height / 2;
      MouseEvent me = new MouseEvent(tblEmp, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis()
          , 0, x, y, 1, false);
      tblEmp.dispatchEvent(me);
    }
  }

  /**
   */
  public void nextPage() {
    model.nextPage();
  }

  /**
   */
  public void prevPage() {
    model.prevPage();
  }

  /**
   * @return
   */
  public int getTotalPages() {
    return model.getPageCount();
  }

  /**
   * @return
   */
  public int getCurrentPageNumber() {
    return model.getCurrentPageNumber();
  }

  /**
   * put your documentation comment here
   */
  public void sortEmp() {
    model.sortEmp();
  }
}

