/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import com.chelseasystems.cs.swing.model.CustomerListModel;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import java.awt.Font;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import javax.swing.table.TableColumn;
import java.awt.Dimension;
import com.chelseasystems.cr.rules.BusinessRuleException;
import java.util.Vector;
import com.chelseasystems.cr.config.ConfigMgr;


/**
 * <p>Title: CustomerListPanel.java</p>
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
 | 1    | 03-25-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerListPanel extends JPanel { // implements PageNumberGetter
	protected CustomerListModel modelCustList;
	protected JCMSTable tblCustomer;
	protected TextRenderer renderer;

	/**
	 * put your documentation comment here
	 */
	public CustomerListPanel() {
		try {
			modelCustList = new CustomerListModel();
			tblCustomer = new JCMSTable(modelCustList, JCMSTable.SELECT_ROW);
			renderer = new TextRenderer();
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
				 * 
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

	public CustomerListPanel(String sISOCountry) {
		try {
			modelCustList = new CustomerListModel(sISOCountry);
			tblCustomer = new JCMSTable(modelCustList, JCMSTable.SELECT_ROW);
			renderer = new TextRenderer();
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
				 * 
				 * @param e
				 */
				public void componentResized(ComponentEvent e) {
					if (modelCustList.isModelForJapan())
						resetColumnWidths_JP();
					else
						resetColumnWidths();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int getNoOfRows() {
		return modelCustList.getRowCount();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param row
	 */
	public void selectRow(int row) {
		if (row < 0)
			return;
		modelCustList.pageContainingRow(row);
		selectRowIfInCurrentPage(row);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param absoluteRow
	 */
	public void selectRowIfInCurrentPage(int absoluteRow) {
		if (absoluteRow < 0)
			return;
		int rowInPage = modelCustList.getCurrentPage().indexOf(modelCustList.getAllRows().elementAt(absoluteRow));
		modelCustList.setLastSelectedCustomerRow(absoluteRow);
		if (rowInPage < 0)
			return;
		ListSelectionModel model = tblCustomer.getSelectionModel();
		model.setSelectionInterval(rowInPage, rowInPage);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param cmsCustomer
	 */
	public void addCustomer(CMSCustomer cmsCustomer) {
		modelCustList.addCustomer(cmsCustomer);
		selectLastRow();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @exception BusinessRuleException
	 */
	public void deleteSelectedCustomer() throws BusinessRuleException {
		int row = tblCustomer.getSelectedRow();
		CMSCustomer cmsCustomer = getCustomerAt(row);
		deleteCustomer(cmsCustomer);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param cmsCustomer
	 * @exception BusinessRuleException
	 */
	public void deleteCustomer(CMSCustomer cmsCustomer) throws BusinessRuleException {
		modelCustList.removeRowInModel(cmsCustomer);
		modelCustList.fireTableDataChanged();
		this.selectLastRow();
	}

	/**
	 * put your documentation comment here
	 */
	public void pageUp() {
		modelCustList.prevPage();
		selectFirstRow();
	}

	/**
	 * put your documentation comment here
	 */
	public void pageDown() {
		modelCustList.nextPage();
		selectLastRow();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param row
	 * @return
	 */
	public CMSCustomer getCustomerAt(int row) {
		return (modelCustList.getCustomerAt(row));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param l
	 */
	public void addMouseListener(MouseListener l) {
		tblCustomer.addMouseListener(l);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int getSelectedRowIndex() {
		return tblCustomer.getSelectedRow();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public CMSCustomer getSelectedCustomer() {
		if (getSelectedRowIndex() < 0)
			return null;
		return getCustomerAt(getSelectedRowIndex());
	}

	/**
	 * put your documentation comment here
	 */
	public void selectLastRow() {
		int rowCount = tblCustomer.getRowCount();
		if (rowCount < 1) {
			modelCustList.prevPage();
		}
		if (rowCount > 0) {
			tblCustomer.setRowSelectionInterval(rowCount - 1, rowCount - 1);
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void selectFirstRow() {
		selectRow(0);
	}

	/**
	 * put your documentation comment here
	 */
	public void clear() {
		modelCustList.clear();
	}

	/**
	 * put your documentation comment here
	 */
	public void update() {
		modelCustList.fireTableDataChanged();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public CustomerListModel getAddressModel() {
		return modelCustList;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public JCMSTable getTable() {
		return (this.tblCustomer);
	}

	/**
	 * put your documentation comment here
	 */
	public void resetColumnWidths() {
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		int iWidth = 0;
		TableColumn IDCol = tblCustomer.getColumnModel().getColumn(CustomerListModel.ID);
		IDCol.setPreferredWidth((int) (125 * r));
		TableColumn NameCol = tblCustomer.getColumnModel().getColumn(CustomerListModel.LAST_FIRST_NAME);
		NameCol.setPreferredWidth((int) (250 * r));
		iWidth = tblCustomer.getWidth() - (IDCol.getPreferredWidth() + NameCol.getPreferredWidth());
		TableColumn AddressCol = tblCustomer.getColumnModel().getColumn(CustomerListModel.ADDRESS);
		AddressCol.setPreferredWidth(iWidth);
		modelCustList.setRowsShown(tblCustomer.getHeight() / tblCustomer.getRowHeight());
		// MP: Reduced the header width
		tblCustomer.getTableHeader().setPreferredSize(new Dimension((int) r, 30));
	}

	public void resetColumnWidths_JP() {
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		int iWidth = 0;
		TableColumn IDCol = tblCustomer.getColumnModel().getColumn(CustomerListModel.ID);
		IDCol.setPreferredWidth((int) (125 * r));
		TableColumn NameCol = tblCustomer.getColumnModel().getColumn(CustomerListModel.LAST_FIRST_NAME);
		NameCol.setPreferredWidth((int) (200 * r));
		TableColumn Name_JPCol = tblCustomer.getColumnModel().getColumn(CustomerListModel.LAST_FIRST_NAME_JP);
		Name_JPCol.setPreferredWidth((int) (200 * r));

		iWidth = tblCustomer.getWidth() - (IDCol.getPreferredWidth() + NameCol.getPreferredWidth() + Name_JPCol.getPreferredWidth());
		TableColumn AddressCol = tblCustomer.getColumnModel().getColumn(CustomerListModel.ADDRESS_JP);
		AddressCol.setPreferredWidth(iWidth);
		modelCustList.setRowsShown(tblCustomer.getHeight() / tblCustomer.getRowHeight());
		// MP: Reduced the header width
		tblCustomer.getTableHeader().setPreferredSize(new Dimension((int) r, 30));

	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 */
	public void setAppMgr(IApplicationManager theAppMgr) {
		if (theAppMgr != null) {
			tblCustomer.setAppMgr(theAppMgr);
			renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
		}
	}

	/**
	 * @return
	 */
	public int getCurrentPageNumber() {
		return (modelCustList.getCurrentPageNumber());
	}

	/**
	 * @return
	 */
	public int getTotalPages() {
		return (modelCustList.getPageCount());
	}

	/**
	 * put your documentation comment here
	 */
	public void nextPage() {
		modelCustList.nextPage();
	}

	/**
	 */
	public void prevPage() {
		modelCustList.prevPage();
	}

	private class TextRenderer extends JLabel implements TableCellRenderer {
		private ConfigMgr custConfigMgr;
		private int custAAALength = 0;
		private Color DefaultBackground;
		private Color DefaultForeground;

		/**
		 */
		public TextRenderer() {
			super();
			custConfigMgr = new ConfigMgr("customer.cfg");
			String strCustAAALength = custConfigMgr.getString("CUSTOMER_AAA_LENGTH");
			if (strCustAAALength != null) {
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
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			// Check for Alpha Numeric Customer ID
			CMSCustomer customer = getCustomerAt(row);
			boolean custHasChar = false;
			String custId = customer.getId();
			int num = custId.trim().length();
			if (num == custAAALength) {
				int custNumber = 0;
				try {
					custNumber = new Integer(custId).intValue();
					// Change the color to Red
				} catch (Exception ex) {
					// If Customer Id has Char, then throws Number Format Exception
					custHasChar = true;
				}
			}

			if (value != null) {
				setText(value.toString());
			} else
				setText("");
			// switch (col) {
			// case CustomerListModel.ID:
			// case CustomerListModel.LAST_FIRST_NAME:
			// case CustomerListModel.ADDRESS:
			setHorizontalAlignment(JLabel.CENTER);
			// break;
			// }
			if (isSelected) {
				setForeground(Color.white);
				setBackground(new Color(0, 0, 128));
			} else if (custHasChar) {
				// Change the Color into red
				setForeground(Color.red);
				setBackground(DefaultBackground);
			} else {
				setBackground(DefaultBackground);
				setForeground(DefaultForeground);
			}
			return (this);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param selectedCustomerID
	 */
	public void sortCustomer(String selectedCustomerID) {
		int selectedCustRowNew = -1;
		if (selectedCustomerID == null && getSelectedCustomer() != null)
			selectedCustRowNew = modelCustList.sortCustomers(getSelectedCustomer().getId());
		else
			selectedCustRowNew = modelCustList.sortCustomers(selectedCustomerID);
		if (selectedCustRowNew > -1) {
			modelCustList.setLastSelectedCustomerRow(selectedCustRowNew);
			selectRow(selectedCustRowNew);
		}
	}
}
