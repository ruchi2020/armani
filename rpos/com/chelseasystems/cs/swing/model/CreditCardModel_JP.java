/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.model;

import java.util.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;

/**
 * <p>Title:CreditCardModel_JP.java </p>
 *
 * <p>Description: Model to display four credit card names in one row </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc. </p>
 *
 * @author Sandhya Ajit
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-06-2006 | Sandhya   | PCR1327	 | Model to display four credit card names in one row |          
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

/**
 */
public class CreditCardModel_JP extends ScrollableTableModel {

  static final long serialVersionUID = 0;
	
  private int columnCount = 1;
	
  /**
   * @param columnCount
   */
  public CreditCardModel_JP(int columnCount) {
	  this.columnCount = columnCount;
	  String[] columns = new String[columnCount];
	  for (int i = 0; i < columns.length; i++)
		  columns[i] = "";
    this.setColumnIdentifiers(columns);
  }
  
  /**
   * @param columnTitles
   */
  public CreditCardModel_JP(String columnTitles[]) {
  	super(columnTitles);
  }  
  
  /**
   * Returns column count
   */
  public int getColumnCount() {
    return columnCount;
  }
  
  /**
   * Return the Column class for the specified columnIndex.
   * @param columnIndex index of the column requested.
   * @return Class class of the column.
   */
  public Class getColumnClass(int columnIndex) {
      return GenericChooserRow.class;
  }
  
  /** 
   * @param row
   * @return
   */
  public GenericChooserRow getRow(int row) {
      return (GenericChooserRow)getRowInPage(row);
  }

  public boolean isCellEditable(int row, int column) {
      return false;
  }
	
  /**
   * Return the Row count in the table.
   * @return int row count.
   */
  public int getRowCount()  {
	int rowCount = 0;
    Vector vTemp = getCurrentPage();
      if (vTemp != null && vTemp.size() > 0)
		{
			int quotient = vTemp.size() / getColumnCount();
			int remainder = vTemp.size() % getColumnCount();
			
			if (quotient == 0 && remainder > 0)
				rowCount = 1;
			else if (quotient > 0 && remainder > 0)
				rowCount = quotient + 1;
			else
				rowCount = quotient;
		}
		
      return rowCount;
  }
  
  /**
   * Return the object at row and column
   */
  public Object getValueAt(int row, int column) {
      Vector vTemp = getCurrentPage();
      
      if ((row > getRowCount()) || (column > getColumnCount()))
          return null;
      
      String value = "";
      GenericChooserRow gcr = null;
		int index = getIndexAt(row, column);		
		if (index > -1 && index < vTemp.size())
			gcr = (GenericChooserRow)vTemp.elementAt(index);
		if (gcr != null)
			value = gcr.getDisplayRow()[0].toString();
      return value;
  }
  
  /**
   * Return the object at row and column
   */
  public Object getObjectAt(int row, int column) {
	Vector vTemp = getCurrentPage();
    
	if ((row > getRowCount()) || (column > getColumnCount()))
		return null;    
		
	GenericChooserRow gcr = null;
	int index = getIndexAt(row, column);
	if (index < vTemp.size())
		gcr = (GenericChooserRow)vTemp.elementAt(index);
	return gcr;
  } 
	
  /**
	* Returns the index in the list that corresponds to the specified row and col.
	* 
	* In other words, this table model consists of a list whose elements are displayed in a row/col table.
	* Special handling has to be done, to be able to treat each cell in the table as an element on its own, 
	* instead of the standard row architecture for a table.
	* 
	* @param rowIndex Index of the row selected.
	* @param colIndex Index of the column selected.
	* @return index Index of the datalist element for the corresponding cell.
	*				 -1 is returned for an invalid row/col value.
	*/
    private int getIndexAt(int rowIndex, int columnIndex) {
    	int index = -1;
		
		if (rowIndex > -1 && columnIndex > -1)
			index = rowIndex * getColumnCount() + columnIndex;
		
		return index;
   }
} // end of class

