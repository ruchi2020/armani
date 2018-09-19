/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;

import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import javax.swing.table.TableColumn;
import java.awt.Dimension;
import java.util.Vector;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.swing.model.CustomerListModel;

/**
 * <p>Title: CustomerListPanel_JP.java</p>
 *
 * <p>Description: View list of customers </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:  Skillnet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 01-25-2007 | Sandhya   | N/A       | Sort customers by home store color                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerListPanel_JP extends CustomerListPanel { //implements PageNumberGetter
   
  private static final long serialVersionUID = 1L;
  private TextRenderer_JP renderer;
  
  /**
   * put your documentation comment here
   */
  public CustomerListPanel_JP() {
    try {
      modelCustList = new CustomerListModel();
      tblCustomer = new JCMSTable(modelCustList, JCMSTable.SELECT_ROW);
      renderer = new TextRenderer_JP();
      this.setLayout(new BorderLayout());
      this.add(tblCustomer.getTableHeader(), BorderLayout.NORTH);
      this.add(tblCustomer, BorderLayout.CENTER);
      modelCustList.setRowsShown(13); // arbitrarily set until resize occurs
      TableColumnModel modelColumn = tblCustomer.getColumnModel();
      for (int i = 0; i < modelCustList.getColumnCount(); i++) {
        modelColumn.getColumn(i).setCellRenderer(renderer);
      }
      this.addComponentListener(new java.awt.event.ComponentAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void componentResized(ComponentEvent e) {
          resetColumnWidths();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public CustomerListPanel_JP(String sISOCountry) {
    try {
      modelCustList = new CustomerListModel(sISOCountry);
      tblCustomer = new JCMSTable(modelCustList, JCMSTable.SELECT_ROW);
      renderer = new TextRenderer_JP();
      this.setLayout(new BorderLayout());
      this.add(tblCustomer.getTableHeader(), BorderLayout.NORTH);
      this.add(tblCustomer, BorderLayout.CENTER);
      modelCustList.setRowsShown(13); // arbitrarily set until resize occurs
      TableColumnModel modelColumn = tblCustomer.getColumnModel();
      for (int i = 0; i < modelCustList.getColumnCount(); i++) {
        modelColumn.getColumn(i).setCellRenderer(renderer);
      }
      this.addComponentListener(new java.awt.event.ComponentAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void componentResized(ComponentEvent e) {
          if(modelCustList.isModelForJapan())
            resetColumnWidths_JP();
          else
            resetColumnWidths();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class TextRenderer_JP extends JLabel implements TableCellRenderer {
    private ConfigMgr custConfigMgr;
    private int custAAALength=0;
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     */
    public TextRenderer_JP() {
      super();
      custConfigMgr= new ConfigMgr("customer.cfg");
      String strCustAAALength=custConfigMgr.getString("CUSTOMER_AAA_LENGTH");
      if(strCustAAALength!=null){
        custAAALength = new Integer(strCustAAALength).intValue();
      }
      setFont(new Font("Helvetica", 1, 12));
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
      setOpaque(true);
    }

    /**
	 */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
    		, boolean hasFocus, int row, int col) {
    	// Check for Alpha Numeric Customer ID
    	CMSCustomer customer=getCustomerAt(row);
	    boolean custHasChar=false;
	    String custId = customer.getId();
	    int num = custId.trim().length();
	    if (num == custAAALength) {
	    	int custNumber=0;
	        try{
	          custNumber = new Integer(custId).intValue();
	          // Change the color to Red
	        }catch (Exception ex){
	          // If Customer Id has Char, then throws Number Format Exception
	          custHasChar=true;
	        }
	      }

	      if (value != null){
	        setText(value.toString());
	      }
	      else {
	        setText("");
	        setHorizontalAlignment(JLabel.CENTER);
	      }

	      if (modelCustList.isCustomerFromCurrentStore(customer)) {
				setForeground(Color.red);
				setBackground((isSelected) ? new Color(0, 0, 128) : DefaultBackground);
	      } else {
				if (isSelected)	{
					setForeground(Color.white);
					setBackground(new Color(0, 0, 128));
				} else if(custHasChar) {
					// Change the Color into red
					setForeground(Color.red);
					setBackground(DefaultBackground);
				} else {
					setBackground(DefaultBackground);
					setForeground(DefaultForeground);
				}
		}
	    return (this);
    }
  }
  
  /**
   * put your documentation comment here
   * @param selectedCustomerID
   */
  public void sortCustomer(String selectedCustomerID) {
	int selectedCustRowNew = -1;
	if (selectedCustomerID == null && getSelectedCustomer() != null)
		selectedCustRowNew = modelCustList.orderCustomerByHomeStore(getSelectedCustomer().getId());
	else
		selectedCustRowNew = modelCustList.orderCustomerByHomeStore(selectedCustomerID);
	if (selectedCustRowNew > -1) {
		modelCustList.setLastSelectedCustomerRow(selectedCustRowNew);
		selectRow(selectedCustRowNew);
	}
  }
}

