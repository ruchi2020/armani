/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.loyalty;

import com.chelseasystems.cs.loyalty.Loyalty;
import javax.swing.*;
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
import com.chelseasystems.cs.swing.model.ItemLookupListModel;
import com.chelseasystems.cs.item.CMSItem;
import javax.swing.table.JTableHeader;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.Event;
import com.chelseasystems.cr.swing.MultiLineCellRenderer;
import com.chelseasystems.cr.swing.ScrollableTableModel;



/**
 * <p>Title:LoyaltyManagementPanel </p>
 * <p>Description: Manage Loyalty</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class LoyaltyManagementPanel extends JPanel {
  private LoyaltyManagementModel modelLoyaltyManagement;
  //private JCMSTable tblItem;
  LoyaltyManagementTable tblItem;
  private TextRenderer renderer;
  private IApplicationManager theAppMgr;
  private JComponent m_objParent = null;
  
  /**
   * put your documentation comment here
   * @param   IApplicationManager theAppMgr
   */
  public LoyaltyManagementPanel(IApplicationManager theAppMgr) {
    this.theAppMgr = theAppMgr;
    try {
      modelLoyaltyManagement = new LoyaltyManagementModel(theAppMgr);
      //tblItem = new JCMSTable(modelLoyaltyManagement, JCMSTable.SELECT_ROW);
      tblItem = new LoyaltyManagementTable(modelLoyaltyManagement, JCMSTable.SELECT_ROW);
      renderer = new TextRenderer();
      WrapTextRenderer wrapRenderer = new WrapTextRenderer();
      this.setLayout(new BorderLayout());
      this.add(tblItem.getTableHeader(), BorderLayout.NORTH);
      this.add(tblItem, BorderLayout.CENTER);
      tblItem.setAlignmentX(CENTER_ALIGNMENT);
      tblItem.setAlignmentY(CENTER_ALIGNMENT);
      modelLoyaltyManagement.setRowsShown(13); // arbitrarily set until resize occurs
      TableColumnModel modelColumn = tblItem.getColumnModel();
      for (int i = 0; i < modelLoyaltyManagement.getColumnCount(); i++) {
        switch (i) {
          case LoyaltyManagementModel.LOYALTY_NUM:
          case LoyaltyManagementModel.TYPE:
          case LoyaltyManagementModel.ISSUED_BY:
          case LoyaltyManagementModel.ISSUE_DATE:
          case LoyaltyManagementModel.CURR_BAL:
          case LoyaltyManagementModel.LIFETIME_BAL:
          case LoyaltyManagementModel.ACTIVE:
            modelColumn.getColumn(i).setCellRenderer(renderer);
        }
      }
      tblItem.registerKeyboardAction(tblItem, JCMSTable.SELECT_CMD, 
    		  KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JTable.WHEN_FOCUSED);
      
      this.addComponentListener(new java.awt.event.ComponentAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void componentResized(ComponentEvent e) {
          resetColumnWidths();
        }
      });      
      
      tblItem.addMouseListener(new MouseAdapter() {
    	  public void mousePressed(MouseEvent e) {
          	tblItem_mousePressed(e);
          }
     
          public void mouseReleased(MouseEvent e) {
          	tblItem_mouseReleased(e);
          }

          public void mouseClicked(MouseEvent e) {
          	tblItem_mouseClicked(e);
          }
      });      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  void tblItem_mousePressed (MouseEvent e) {
	   //handleMouseSelection(e);
  }

  void tblItem_mouseReleased(MouseEvent e) {
	   //handleMouseSelection(e);
  }
  
  void tblItem_mouseClicked (MouseEvent e) {
	   handleMouseSelection(e);
  }
  
  private void handleMouseSelection(MouseEvent e) {     
      // Unselect the other table's selection
      if (m_objParent != null && m_objParent instanceof LoyaltyManagementApplet)
   		  ((LoyaltyManagementApplet)m_objParent).updateTableSelection(this);      
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTable getTable() {
    return tblItem;
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void selectRow(int row) {
    ListSelectionModel model = tblItem.getSelectionModel();
    model.setSelectionInterval(row, row);
  }

  /**
   * Sets item at given model
   * @param loyalty Loyalty[]
   */
  public void setItems(Loyalty loyalty[]) {
    modelLoyaltyManagement.setItems(loyalty);
 // selectFirstRow();
  }

  /**
   * Adds Item in given panel
   * @param loyalty Loyalty
   */
  public void addItem(Loyalty loyalty) {
    modelLoyaltyManagement.addItem(loyalty);
  }

  /**
   * Deletes selected item
   * @throws BusinessRuleException
   */
  public void deleteSelectedItem()
      throws BusinessRuleException {
    int row = tblItem.getSelectedRow();
    Loyalty loyalty = getItemAt(row);
    deleteItem(loyalty);
  }

  /**
   * Deletes item from the panel
   * @param loyalty Loyalty
   * @throws BusinessRuleException
   */
  public void deleteItem(Loyalty loyalty)
      throws BusinessRuleException {
    modelLoyaltyManagement.removeRowInModel(loyalty);
    modelLoyaltyManagement.fireTableDataChanged();
    this.selectLastRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageUp() {
    modelLoyaltyManagement.prevPage();
    selectFirstRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageDown() {
    modelLoyaltyManagement.nextPage();
    selectLastRow();
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public Loyalty getItemAt(int row) {
    return (modelLoyaltyManagement.getItemAt(row));
  } 

  /**
   * put your documentation comment here
   * @return
   */
  public int getSelectedRowIndex() {
    return tblItem.getSelectedRow();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Loyalty getSelectedItem() {
    if (getSelectedRowIndex() == -1)
      return null;
    return getItemAt(getSelectedRowIndex());
  }

  /**
   * put your documentation comment here
   */
  public void selectLastRow() {
    int rowCount = tblItem.getRowCount();
    if (rowCount < 1) {
      modelLoyaltyManagement.prevPage();
    }
    if (rowCount > 0) {
      tblItem.setRowSelectionInterval(rowCount - 1, rowCount - 1);
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
    modelLoyaltyManagement.clear();
  }

  /**
   * put your documentation comment here
   */
  public void update() {
    modelLoyaltyManagement.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public LoyaltyManagementModel getAddressModel() {
    return modelLoyaltyManagement;
  }

  /**
   * Resets the column width
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    int iWidth = 0;
    TableColumn LoyaltyNoCol = tblItem.getColumnModel().getColumn(LoyaltyManagementModel.
        LOYALTY_NUM);
    LoyaltyNoCol.setPreferredWidth((int)(105 * r));
    TableColumn TypeCol = tblItem.getColumnModel().getColumn(LoyaltyManagementModel.TYPE);
    TypeCol.setPreferredWidth((int)(65 * r));
    TableColumn IssuedByCol = tblItem.getColumnModel().getColumn(LoyaltyManagementModel.ISSUED_BY);
    IssuedByCol.setPreferredWidth((int)(55 * r));
    TableColumn IssueDateCol = tblItem.getColumnModel().getColumn(LoyaltyManagementModel.ISSUE_DATE);
    IssueDateCol.setPreferredWidth((int)(55 * r));
    TableColumn CurrBalCol = tblItem.getColumnModel().getColumn(LoyaltyManagementModel.CURR_BAL);
    CurrBalCol.setPreferredWidth((int)(50 * r));
    TableColumn LifeTimeCol = tblItem.getColumnModel().getColumn(LoyaltyManagementModel.
        LIFETIME_BAL);
    LifeTimeCol.setPreferredWidth((int)(55 * r));
    TableColumn ActiveCol = tblItem.getColumnModel().getColumn(LoyaltyManagementModel.ACTIVE);
    ActiveCol.setPreferredWidth((int)(55 * r));
    iWidth = tblItem.getWidth()
        - (LoyaltyNoCol.getPreferredWidth() + TypeCol.getPreferredWidth()
        + IssuedByCol.getPreferredWidth() + IssueDateCol.getPreferredWidth()
        + CurrBalCol.getPreferredWidth() + LifeTimeCol.getPreferredWidth()
        + ActiveCol.getPreferredWidth());
    this.modelLoyaltyManagement.setRowsShown(tblItem.getHeight() / tblItem.getRowHeight());
    tblItem.getTableHeader().setPreferredSize(new Dimension((int)r, 30));
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
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    if (theAppMgr != null) {
      tblItem.setAppMgr(theAppMgr);
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    }
  }

  /**
   * @return int
   */
  public int getCurrentPageNumber() {
    return (modelLoyaltyManagement.getCurrentPageNumber());
  }

  /**
   * @return int
   */
  public int getTotalPages() {
    return (modelLoyaltyManagement.getPageCount());
  }

  /**
   * put your documentation comment here
   */
  public void nextPage() {
	int rowSelected = tblItem.getSelectedRow();	
	modelLoyaltyManagement.nextPage();	
	if (rowSelected > -1)
		selectFirstRow();
  }
  
  /**
   */
  public void prevPage() {
	int rowSelected = tblItem.getSelectedRow();
    modelLoyaltyManagement.prevPage();    
	if (rowSelected > -1)
		selectFirstRow();
  }
  
  /**
   * put your documentation comment here
   */
  public void downCommandNextPage() {	  
    modelLoyaltyManagement.nextPage();
    selectFirstRow();
  }

  /**
   */
  public void upCommandPrevPage() {
    modelLoyaltyManagement.prevPage();
    selectLastRow();
  }  
  
  /**
   */
  public void keyPressedPrevPage() {
    modelLoyaltyManagement.prevPage();
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
        case LoyaltyManagementModel.LOYALTY_NUM:
        case LoyaltyManagementModel.TYPE:
        case LoyaltyManagementModel.ISSUED_BY:
        case LoyaltyManagementModel.ISSUE_DATE:
        case LoyaltyManagementModel.CURR_BAL:
        case LoyaltyManagementModel.LIFETIME_BAL:
        case LoyaltyManagementModel.ACTIVE:
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
  private class LoyaltyManagementTable extends JCMSTable {

    /**
     * put your documentation comment here
     * @param         ScrollableTableModel model
     * @param         int type
     */
    public LoyaltyManagementTable(ScrollableTableModel model, int type) {
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
                int oldIndex = getSelectedRow();
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
                if(oldIndex !=  getSelectedRow())
                	processKeyEvent(new KeyEvent(this,KeyEvent.KEY_PRESSED, System.currentTimeMillis(),0,KeyEvent.VK_ENTER,KeyEvent.CHAR_UNDEFINED));
            } else if(command.equals("DOWN_CMD")) {
                int index = getSelectedRow();
                int oldIndex = getSelectedRow();
                if(index < getRowCount() - 1)
                    setRowSelectionInterval(index + 1, index + 1);
                else if(getModel() instanceof ScrollableTableModel) {
                    ScrollableTableModel model = (ScrollableTableModel)getModel();                    
                    if(model.getCurrentPageNumber() < model.getPageCount() - 1) {
                    	downCommandNextPage();
                    }
                }
                if(oldIndex !=  getSelectedRow())
                	processKeyEvent(new KeyEvent(this,KeyEvent.KEY_PRESSED, System.currentTimeMillis(),0,KeyEvent.VK_ENTER,KeyEvent.CHAR_UNDEFINED));
            } else if(command.equals("PAGEUP_CMD")) {
            	prevPage();
            	processKeyEvent(new KeyEvent(this,KeyEvent.KEY_PRESSED, System.currentTimeMillis(),0,KeyEvent.VK_ENTER,KeyEvent.CHAR_UNDEFINED));
            } else if(command.equals("PAGEDOWN_CMD")) {                
            	nextPage();
            	processKeyEvent(new KeyEvent(this,KeyEvent.KEY_PRESSED, System.currentTimeMillis(),0,KeyEvent.VK_ENTER,KeyEvent.CHAR_UNDEFINED));
            }
            scrollRectToVisible(getCellRect(getSelectedRow(), getSelectedColumn(), true));            
        }
    }   
  }
}


