/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-01-2005 | Anand     | N/A       |1.Modification to query by Armani Associate ID|
 --------------------------------------------------------------------------------------------
 | 3    | 04-26-2005 | Manpreet  | N/A       |1.Modifiation to use it in Alterations        |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 07-19-2005 | Vikram    | 271       |Added fuctionality to Sort Associates.        |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.swing.model;

import java.util.Comparator;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cs.employee.CMSEmployee;
import java.util.TreeMap;
import com.chelseasystems.cr.util.ResourceManager;


/**
 */
public class EmpListModel extends ScrollableTableModel {
  private Vector employeeVec;
  static TreeMap sortKeysMap = new TreeMap();
  static{
		ConfigMgr cfg = new ConfigMgr("employee.cfg");
		String associateSortKeys = cfg.getString("ASSOCIATE_SORT_KEYS");
		if(associateSortKeys == null || "".equals(associateSortKeys)){
			associateSortKeys = "EMPID";
			sortKeysMap.put(new Integer(1),"EMPID");
		}else{
			StringTokenizer tokenizer = new StringTokenizer(associateSortKeys,",");
			String value = "";
			Integer key;
			try {
				while (tokenizer.hasMoreTokens()) {
					value = (String) tokenizer.nextToken();
					if (cfg.getString(value + ".SORT_SEQ") != null) {
						key = new Integer(Integer.parseInt(cfg.getString(value + ".SORT_SEQ")));
						sortKeysMap.put(key, value);
					}
				}
			} catch (Exception e) {
				System.out
					.println("Exception occured while reading associate sort order from employee.cfg!!!");
			}
		}
		if(sortKeysMap.size() == 0)
			sortKeysMap.put(new Integer(1),"EMPID");
  }

  /**
   */
  public EmpListModel() {
    this(new String[] {ResourceManager.getResourceBundle().getString("Associate ID")
        , ResourceManager.getResourceBundle().getString("First Name")
        , ResourceManager.getResourceBundle().getString("Last Name")
    });
  }

  /**
   * Make a model with set column headers -- MSB
   * @param sIdentifiers String[]
   */
  public EmpListModel(String sIdentifiers[]) {
    employeeVec = new Vector();
    setColumnIdentifiers(sIdentifiers);
  }

  /**
   * @param emp
   */
  public void addEmployee(Employee emp) {
    addRow(emp);
    employeeVec.add(emp);
  }

  /**
   * @param row
   * @return
   */
  public Employee getEmployee(int row) {
    return (Employee)getRowInPage(row);
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return 3;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = this.getCurrentPage();
    CMSEmployee emp = (CMSEmployee)vTemp.elementAt(row);
    switch (column) {
      case 0:
        return emp.getExternalID();
      case 1:
        return emp.getFirstName();
      case 2:
        return emp.getLastName();
      default:
        return " ";
    }
  }

  /**
   * @param table
   */
  public void setColumnWidth(JTable table) {
    TableColumn ShtNameCol = table.getColumnModel().getColumn(0);
    ShtNameCol.setPreferredWidth(115);
    TableColumn FNameCol = table.getColumnModel().getColumn(1);
    FNameCol.setPreferredWidth(220);
    TableColumn LNameCol = table.getColumnModel().getColumn(2);
    LNameCol.setPreferredWidth(table.getWidth()
        - (ShtNameCol.getPreferredWidth() + FNameCol.getPreferredWidth()));
  }

  /**
   * put your documentation comment here
   */
  public void sortEmp() {
    TreeMap sortColumnMap = new TreeMap(new AssociateComparator(sortKeysMap));
    Employee tmpEmp = null;
    for (int i = 0; i < employeeVec.size(); i++) {
      tmpEmp = (Employee)employeeVec.elementAt(i);
      /*sortColumnMap.put(tmpEmp.getLastName().toLowerCase().trim() + ","
          + tmpEmp.getFirstName().toLowerCase().trim() + "," + tmpEmp.getExternalID().trim()
          , tmpEmp);*/
      //___Tim: Bug 1708: Provide new sort capability for associates
      sortColumnMap.put(tmpEmp,tmpEmp);
    }
    if (sortColumnMap.size() > 0) {
      for (int i = 0; i < employeeVec.size(); i++) {
        removeRowInModel(employeeVec.elementAt(i));
      }
      Vector sortedEmpVec = new Vector(sortColumnMap.values());
      for (int i = 0; i < sortedEmpVec.size(); i++) {
        tmpEmp = (Employee)sortedEmpVec.elementAt(i);
        addRow(tmpEmp);
      }
      fireTableDataChanged();
    }
  }

  /**
   * put your documentation comment here
   */
  public void clear() {
    employeeVec = new Vector();
    super.clear();
  }
  
  private class AssociateComparator implements Comparator{

  	private TreeMap sortKeysMap;
  	public AssociateComparator(TreeMap sortKeysMap){
  		this.sortKeysMap = sortKeysMap;
  	}
  	
  	public int compare(Object obj1, Object obj2){
/*  		if(!(obj1 instanceof Employee) || !(obj2 instanceof Employee))
  			throw new ClassCastException();*/
  		Employee emp1 = (Employee)obj1;
  		Employee emp2 = (Employee)obj2;
  		Iterator iter = sortKeysMap.values().iterator();
  		StringBuffer empString1 = new StringBuffer(""); 
  		StringBuffer empString2 = new StringBuffer("");
  		String sortKey;
  		while(iter.hasNext()){
  			sortKey = (String)iter.next();
  			if("EMPID".equalsIgnoreCase(sortKey)){
  				empString1.append(emp1.getExternalID().trim());
  				empString2.append(emp2.getExternalID().trim());
  			}else if("FNAME".equalsIgnoreCase(sortKey)){
  				empString1.append(emp1.getFirstName().toLowerCase().trim());
  				empString2.append(emp2.getFirstName().toLowerCase().trim());
  			}else if("LNAME".equalsIgnoreCase(sortKey)){
  				empString1.append(emp1.getLastName().toLowerCase().trim());
  				empString2.append(emp2.getLastName().toLowerCase().trim());
  			}
  		}
  		return (empString1.toString()).compareTo(empString2.toString());
  	}
  }
}

