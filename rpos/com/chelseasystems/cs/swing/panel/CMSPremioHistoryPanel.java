package com.chelseasystems.cs.swing.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.MultiLineCellRenderer;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.loyalty.CMSPremioHistory;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cs.swing.loyalty.LoyaltyManagementApplet;
//import com.chelseasystems.cs.swing.loyalty.RewardDisplayModel;
import com.chelseasystems.cs.swing.model.CMSPremioHistoryModel;

public class CMSPremioHistoryPanel extends JPanel {

	private CMSPremioHistoryModel premioHistoryModel;
	private CMSPremioHistoryTable premioHistoryTable;
	private TextRenderer textRenderer;
	private IApplicationManager theAppMgr;
	private JComponent m_objParent = null;
	 
	public CMSPremioHistoryPanel(IApplicationManager theAppMgr) {
		this.theAppMgr = theAppMgr;
		try {
			premioHistoryModel = new CMSPremioHistoryModel(theAppMgr);
			premioHistoryTable = new CMSPremioHistoryTable(premioHistoryModel, JCMSTable.SELECT_ROW);
			textRenderer = new TextRenderer();
			WrapTextRenderer wrapRenderer = new WrapTextRenderer();
			this.setLayout(new BorderLayout());
			this.add(premioHistoryTable.getTableHeader(), BorderLayout.NORTH);
			this.add(premioHistoryTable, BorderLayout.CENTER);
			premioHistoryTable.setAlignmentX(CENTER_ALIGNMENT);
			premioHistoryTable.setAlignmentY(CENTER_ALIGNMENT);
			premioHistoryModel.setRowsShown(13); // arbitrarily set until
													// resize occurs
			TableColumnModel modelColumn = premioHistoryTable.getColumnModel();
			for (int i = 0; i < premioHistoryModel.getColumnCount(); i++) {
				switch (i) {
				case CMSPremioHistoryModel.LOYALTY_NUM:
				case CMSPremioHistoryModel.TXN_NUM:
				case CMSPremioHistoryModel.TXN_DATE:
				case CMSPremioHistoryModel.PREMIO_DISCOUNT_AMT:
				case CMSPremioHistoryModel.REDEEMED_POINTS:
				case CMSPremioHistoryModel.STORE_NAME:
					modelColumn.getColumn(i).setCellRenderer(textRenderer);
				}
			}
			premioHistoryTable.registerKeyboardAction(premioHistoryTable, JCMSTable.SELECT_CMD, KeyStroke
					.getKeyStroke(KeyEvent.VK_ENTER, 0), JTable.WHEN_FOCUSED);

			this.addComponentListener(new java.awt.event.ComponentAdapter() {

				public void componentResized(ComponentEvent e) {
					resetColumnWidths();
				}
			});
			premioHistoryTable.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					premioHistoryTable_mousePressed(e);
				}

				public void mouseReleased(MouseEvent e) {
					premioHistoryTable_mouseReleased(e);
				}

				public void mouseClicked(MouseEvent e) {
					premioHistoryTable_mouseClicked(e);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void premioHistoryTable_mousePressed (MouseEvent e) {
		//handleMouseSelection(e);
	}

	void premioHistoryTable_mouseReleased(MouseEvent e) {
		//handleMouseSelection(e);
	}
 
	void premioHistoryTable_mouseClicked (MouseEvent e) {
		handleMouseSelection(e);
	}
	
	private void handleMouseSelection(MouseEvent e) {      
	// Unselect the other table's selection
		if (m_objParent != null && m_objParent instanceof LoyaltyManagementApplet)
			((LoyaltyManagementApplet)m_objParent).updateTableSelection(this);  
	}
	
	public JCMSTable getTable() {
		return premioHistoryTable;
	}
	
	/**
	 * Sets the parent panel for this component.
	 * @param parent The parent component.
	*/
	public void setParentComponent(JComponent parent) {
	  m_objParent = parent;
	}

	/**
	 * put your documentation comment here
	 * @param row
	 */
	public void selectRow(int row) {
		ListSelectionModel model = premioHistoryTable.getSelectionModel();
		model.setSelectionInterval(row, row);
	}

	/**
	 * put your documentation comment here
	 * @param rewardCard[]
	 */
	public void setItems(CMSPremioHistory premioHistory[]) {
		premioHistoryModel.setItems(premioHistory);    
	}
	
	/**
	 * Add Items to the panel
	 * @param CMSPremioHistory premioHistory
	 */
	public void addItem(CMSPremioHistory premioHistory) {
	    premioHistoryModel.addItem(premioHistory);
	}

	/**
	 * put your documentation comment here
	 * @exception BusinessRuleException
	 */
	public void deleteSelectedItem() throws BusinessRuleException {
		int row = premioHistoryTable.getSelectedRow();
		CMSPremioHistory premioHistory = getItemAt(row);
		deleteItem(premioHistory);
	}

	/**
	 * Deletes Items from the panel
	 * 
	 * @param rewardCard
	 *            RewardCard
	 * @throws BusinessRuleException
	 */
	public void deleteItem(CMSPremioHistory premioHistory) throws BusinessRuleException {
		premioHistoryModel.removeRowInModel(premioHistory);
		premioHistoryModel.fireTableDataChanged();
		this.selectLastRow();
	}

	/**
	 * put your documentation comment here
	 */
	public void pageUp() {
		premioHistoryModel.prevPage();
		selectFirstRow();
	}

	/**
	 * put your documentation comment here
	 */
	public void pageDown() {
		premioHistoryModel.nextPage();
		selectLastRow();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param row
	 * @return
	 */
	public CMSPremioHistory getItemAt(int row) {
		return (premioHistoryModel.getItemAt(row));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int getSelectedRowIndex() {
		return premioHistoryTable.getSelectedRow();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public CMSPremioHistory getSelectedItem() {
		if (getSelectedRowIndex() == -1)
			return null;
		return getItemAt(getSelectedRowIndex());
	}

	/**
	 * put your documentation comment here
	 */
	public void selectLastRow() {
		int rowCount = premioHistoryTable.getRowCount();
		if (rowCount < 1) {
			premioHistoryModel.prevPage();
		}
		if (rowCount > 0) {
			premioHistoryTable.setRowSelectionInterval(rowCount - 1, rowCount - 1);
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
		premioHistoryModel.clear();
	}

	/**
	 * put your documentation comment here
	 */
	public void update() {
		premioHistoryModel.fireTableDataChanged();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public CMSPremioHistoryModel getAddressModel() {
		return premioHistoryModel;
	}

	/**
	 * Resets the column width
	 */
	public void resetColumnWidths() {
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		TableColumn txnNoCol = premioHistoryTable.getColumnModel().getColumn(CMSPremioHistoryModel.TXN_NUM);
		txnNoCol.setPreferredWidth((int) (105 * r));
		TableColumn loyaltyNoCol = premioHistoryTable.getColumnModel().getColumn(CMSPremioHistoryModel.LOYALTY_NUM);
		loyaltyNoCol.setPreferredWidth((int) (65 * r));
		TableColumn txnDateCol = premioHistoryTable.getColumnModel().getColumn(CMSPremioHistoryModel.TXN_DATE);
		txnDateCol.setPreferredWidth((int) (55 * r));
		TableColumn storeNameCol = premioHistoryTable.getColumnModel().getColumn(CMSPremioHistoryModel.STORE_NAME);
		storeNameCol.setPreferredWidth((int) (50 * r));
		TableColumn redeemedPointsCol = premioHistoryTable.getColumnModel().getColumn(CMSPremioHistoryModel.REDEEMED_POINTS);
		redeemedPointsCol.setPreferredWidth((int) (45 * r));
		TableColumn premioDiscountAmtCol = premioHistoryTable.getColumnModel().getColumn(CMSPremioHistoryModel.PREMIO_DISCOUNT_AMT);
		premioDiscountAmtCol.setPreferredWidth((int) (75 * r));
		this.premioHistoryModel.setRowsShown(premioHistoryTable.getHeight() / premioHistoryTable.getRowHeight());
		premioHistoryTable.getTableHeader().setPreferredSize(new Dimension((int) r, 30));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 */
	public void setAppMgr(IApplicationManager theAppMgr) {
		if (theAppMgr != null) {
			premioHistoryTable.setAppMgr(theAppMgr);
			textRenderer.setFont(theAppMgr.getTheme().getTextFieldFont());
		}
	}

	/**
	 * @return int
	 */
	public int getCurrentPageNumber() {
		return (premioHistoryModel.getCurrentPageNumber());
	}

	/**
	 * @return int
	 */
	public int getTotalPages() {
		return (premioHistoryModel.getPageCount());
	}

	/**
	 * put your documentation comment here
	 */
	public void nextPage() {
		int rowSelected = premioHistoryTable.getSelectedRow();
		premioHistoryModel.nextPage();
		if (rowSelected > -1)
			selectFirstRow();
	}

	/**
	 */
	public void prevPage() {
		int rowSelected = premioHistoryTable.getSelectedRow();
		premioHistoryModel.prevPage();
		if (rowSelected > -1)
			selectFirstRow();
	}

	/**
	 */
	public void downCommandNextPage() {
		premioHistoryModel.nextPage();
		selectFirstRow();
	}

	/**
	 */
	public void upCommandPrevPage() {
		premioHistoryModel.prevPage();
		selectLastRow();
	}
	private class TextRenderer extends JLabel implements TableCellRenderer {
	    private Color DefaultBackground;
	    private Color DefaultForeground;

	    /**
	     * put your documentation comment here
	     */
	    public TextRenderer() {
	      super();
	      setFont(new Font("Helvetica", 1, 12));
	      setForeground(new Color(0, 0, 175));
	      setBackground(Color.white);
	      DefaultBackground = getBackground();
	      DefaultForeground = getForeground();
	      setOpaque(true);
	    }

	    /**
	     * put your documentation comment here
	     * @param table
	     * @param value
	     * @param isSelected
	     * @param hasFocus
	     * @param row
	     * @param col
	     * @return
	     */
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
	        , boolean hasFocus, int row, int col) {
	      String textToSet = "";
	      if (value != null)
	        textToSet = value.toString();
	      switch (col) {
	      case CMSPremioHistoryModel.LOYALTY_NUM:
          case CMSPremioHistoryModel.TXN_NUM:
          case CMSPremioHistoryModel.TXN_DATE:
          case CMSPremioHistoryModel.PREMIO_DISCOUNT_AMT:
	          setHorizontalAlignment(JLabel.CENTER);
	          break;
	      }
	      if (isSelected) {
	        setForeground(Color.white);
	        setBackground(new Color(0, 0, 128));
	      } else {
	        setBackground(DefaultBackground);
	        setForeground(DefaultForeground);
	      }
	      setText(textToSet);
	      return (this);
	    }
	  }

	  private class WrapTextRenderer extends MultiLineCellRenderer {
	    private Color DefaultBackground;
	    private Color DefaultForeground;

	    /**
	     * put your documentation comment here
	     */
	    public WrapTextRenderer() {
	      super();
	      setFont(new Font("Helvetica", 1, 12));
	      setForeground(new Color(0, 0, 175));
	      setBackground(Color.white);
	      DefaultBackground = getBackground();
	      DefaultForeground = getForeground();
	      setOpaque(true);
	    }

	    /**
	     * put your documentation comment here
	     * @param table
	     * @param value
	     * @param isSelected
	     * @param hasFocus
	     * @param row
	     * @param col
	     * @return
	     */
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
	        , boolean hasFocus, int row, int col) {
	      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	      setWrapStyleWord(true);
	      String textToSet = "";
	      if (value != null)
	        textToSet = value.toString();
	      setAlignmentX(CENTER_ALIGNMENT);
	      setAlignmentY(CENTER_ALIGNMENT);
	      if (isSelected) {
	        setForeground(Color.white);
	        setBackground(new Color(0, 0, 128));
	      } else {
	        setBackground(DefaultBackground);
	        setForeground(DefaultForeground);
	      }
	      setText(textToSet);
	      return (this);
	    }
	  }
		  
	
   /**
	* Extended this class to prevent Eu from selecting empty cells
	*/
	private class CMSPremioHistoryTable extends JCMSTable {

		/**
		 * put your documentation comment here
		 * @param         ScrollableTableModel model
		 * @param         int type
		 */
		public CMSPremioHistoryTable(ScrollableTableModel model, int type) {
		  super(model, type);
		}
		
		
		/**
		 * put your documentation comment here
		 * @param e
		 */
		public void actionPerformed(ActionEvent e) {
		    String command = e.getActionCommand();        
		    if(command.equals("SELECT_CMD")) {
		        processMouseEvent(new MouseEvent(this, 500, System.currentTimeMillis(), 0, 0, 0, 1, false));
		    } else {
		        if(command.equals("UP_CMD")) {
		            int index = getSelectedRow();
		            if(index > 0)
		                setRowSelectionInterval(index - 1, index - 1);
		            else if(getModel() instanceof ScrollableTableModel) {
		                ScrollableTableModel model = (ScrollableTableModel)getModel();                    
		                if(model.getCurrentPageNumber() > 0) {                        
		                	upCommandPrevPage();
		                } else if(index == -1) {
		                    int rowCount = model.getRowCount();
		                    if(rowCount > 0)
		                        setRowSelectionInterval(rowCount - 1, rowCount - 1);
		                }
		            }
		        } else if(command.equals("DOWN_CMD")) {
		            int index = getSelectedRow();                
		            if(index < getRowCount() - 1)
		                setRowSelectionInterval(index + 1, index + 1);
		            else if(getModel() instanceof ScrollableTableModel) {
		                ScrollableTableModel model = (ScrollableTableModel)getModel();                    
		                if(model.getCurrentPageNumber() < model.getPageCount() - 1) {                        
		                	downCommandNextPage();
		                }
		            }
		        } else if(command.equals("PAGEUP_CMD")) {
		        	prevPage();
		        } else if(command.equals("PAGEDOWN_CMD")) {
		            	nextPage();
		            }
		            scrollRectToVisible(getCellRect(getSelectedRow(), getSelectedColumn(), true));            
		        }
		    }   
	}
}
