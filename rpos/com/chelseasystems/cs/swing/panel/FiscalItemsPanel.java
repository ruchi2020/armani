/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.swing.panel;

import  java.awt.*;
import  java.awt.event.*;
import  javax.swing.*;
import  javax.swing.table.*;
import  com.chelseasystems.cr.appmgr.IApplicationManager;
import  com.chelseasystems.cr.pos.*;
import  com.chelseasystems.cr.swing.bean.JCMSTable;
import  com.chelseasystems.cr.swing.bean.JCMSTextField;
import  com.chelseasystems.cs.swing.model.FiscalItemsModel;
import  com.chelseasystems.cr.swing.CMSApplet;
import  com.chelseasystems.cr.rules.BusinessRuleException;
import  com.chelseasystems.cs.pos.*;
import  javax.swing.table.TableCellRenderer;


/**
 *
 * <p>Title: FiscalItemsPanel</p>
 * <p>Description:Panel for FiscalItems list </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class FiscalItemsPanel extends JPanel
    implements PageNumberGetter {
  private FiscalItemsModel modelList;
  private JCMSTable tblList;
  private TextRenderer renderer;
  private boolean bSelectionAllowed = false;
  private boolean bSelectRows = false;
  private boolean bSelectable = false;
  private boolean bIsVATInvoice;
  private final int SKU = 0;
  private final int QUANTITY = 1;
  private final int ITEM_DESCRIPTION = 2;
  private final int ITEM_COMMENT = 3;

  /**
   * Default Constructor
   */
  public FiscalItemsPanel (boolean bVATInvoice, String docType, boolean selectable) {
    try {
      bIsVATInvoice = bVATInvoice;
      modelList = new FiscalItemsModel(bVATInvoice, docType, selectable);
      bSelectable = selectable;
      tblList = new JCMSTable(modelList, JCMSTable.SELECT_ROW);
      renderer = new TextRenderer();
      jbInit();
      tblList.addMouseListener(new MouseAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseClicked (MouseEvent e) {
          SwingUtilities.invokeLater(new Runnable() {

            /**
             * put your documentation comment here
             */
            public void run () {
              SwingUtilities.invokeLater(new Runnable() {

                /**
                 * put your documentation comment here
                 */
                public void run () {
                  modelList.setLastSelectedItemRow(getSelectedRow());
                  if (isFiscalDocAlreadyPrinted())
                    return;
                  toggleRowSelection();
                }
              });
            }
          });
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isFiscalDocAlreadyPrinted () {
    //System.out.println("SELECTED ROW@!#!@#@!#!@#$$ " + getSelectedRow());
    //Sergio
    if (!modelList.isFiscalDocumentAlreadyPrintedForLineItem(tblList.getSelectedRow())) {
      //      CMSApplet.theAppMgr.showErrorDlg("Fiscal document already printed for this row");
      return  true;
    }
    if (!bSelectable)
      return  true;
    return  false;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr (IApplicationManager theAppMgr) {
    if (theAppMgr != null) {
      tblList.setAppMgr(theAppMgr);
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
    }
  }

  /**
   * @exception Exception
   */
  private void jbInit () throws Exception {
    this.setLayout(new BorderLayout());
    tblList.getTableHeader().setPreferredSize(new Dimension(833, 45));
    this.add(tblList.getTableHeader(), BorderLayout.NORTH);
    this.add(tblList, BorderLayout.CENTER);
    modelList.setRowsShown(13);
    TableColumnModel modelColumn = tblList.getColumnModel();
    for (int i = 0; i < modelList.getColumnCount(); i++) {
      modelColumn.getColumn(i).setCellRenderer(renderer);
    }
    //    if (this.bIsVATInvoice) {
    /*   CommentsEditor commentsEditor = new CommentsEditor();
     modelColumn.getColumn(ITEM_COMMENT).setCellEditor(commentsEditor);*/
    //    }
    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized (ComponentEvent e) {
        resetColumnWidths();
      }
    });
    tblList.setRequestFocusEnabled(false);
    tblList.setCellSelectionEnabled(false);
    tblList.setRowSelectionAllowed(true);
    tblList.setColumnSelectionAllowed(false);
  }

  /**
   */
  public void clear () {
    modelList.clear();
  }

  /**
   */
  public void update () {
    modelList.fireTableDataChanged();
  }

  /**
   * Set RowSelection ON/OFF
   * @param bSelectionOn True/False
   */
  public void setSelectionOn (boolean bSelectionOn) {
    tblList.setRowSelectionAllowed(bSelectionOn);
    bSelectionAllowed = bSelectionOn;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isSelectionAllowed () {
    return  bSelectionAllowed;
  }

  /**
   * put your documentation comment here
   */
  public void toggleListSelection () {
    modelList.toggleRowsSelection(bSelectRows);
    modelList.fireTableDataChanged();
    if (bSelectRows) {
      selectRow(0, false);
    }
    bSelectRows = !bSelectRows;
  }

  /**
   * put your documentation comment here
   */
  public void toggleRowSelection () {
    int iRow = tblList.getSelectedRow();
    modelList.setRowSelected(iRow);
    modelList.fireTableDataChanged();
    if (modelList.getRowSelected(iRow))
      selectRow(iRow, false);
  }

  /**
   * @param row
   */
  public void selectRow (int row, boolean bEnforceSelection) {
    if (bEnforceSelection) {
      modelList.setRowSelected(row, true);
      modelList.fireTableDataChanged();
    }
    ListSelectionModel model = tblList.getSelectionModel();
    model.setSelectionInterval(row, row);
  }

  /**
   */
  public FiscalItemsModel getModel () {
    return  (this.modelList);
  }

  /**
   */
  public JCMSTable getTable () {
    return  (this.tblList);
  }

  /**
   */
  public void selectLastRow () {
    int rowCount = tblList.getRowCount();
    if (rowCount < 1) {
      modelList.prevPage();
    }
    if (rowCount > 0) {
      tblList.setRowSelectionInterval(rowCount - 1, rowCount - 1);
    }
  }

  /**
   */
  public void selectFirstRow () {
    selectRow(0, false);
  }

  /**
   * @param line
   */
  public void addLineItem (POSLineItem line) {
    //    if (line instanceof CMSPresaleLineItem) {
    //      bSelectRows = false;
    //    } else {
    //      bSelectRows = true;
    //    }
    bSelectRows = true;
    modelList.addLineItem(line);
    //selectLastRow();
  }

  /**
   */
  public void nextPage () {
    /*CommentsEditor commentsEditor = (CommentsEditor)tblList.getColumnModel().getColumn(ITEM_COMMENT).getCellEditor();
     commentsEditor.stopCellEditing();*/
    modelList.nextPage();
    selectFirstRow();    /*   String trueString = (String)modelList.getValueAt(tblList.getSelectedRow(), ITEM_COMMENT);
     commentsEditor.txtField.setText(trueString);*/

  }

  /**
   */
  public void prevPage () {
    /*    CommentsEditor commentsEditor = (CommentsEditor)tblList.getColumnModel().getColumn(ITEM_COMMENT).getCellEditor();
     commentsEditor.stopCellEditing();*/
    modelList.prevPage();
    selectLastRow();    /*    String trueString = (String)modelList.getValueAt(tblList.getSelectedRow(), ITEM_COMMENT);
     commentsEditor.txtField.setText(trueString);*/

  }

  /**
   * @return
   */
  public int getCurrentPageNumber () {
    return  (modelList.getCurrentPageNumber());
  }

  /**
   * @return
   */
  public int getTotalPages () {
    return  (modelList.getPageCount());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getSelectedRow () {
    if (tblList.getSelectedRow() < 0)
      return  tblList.getSelectedRow();
    int page = modelList.getCurrentPageNumber();
    int row = (page*modelList.getRowsShown()) + tblList.getSelectedRow();
    return  row;
  }

  /**
   * @param row
   * @return
   */
  public POSLineItem getLineItem (int row) {
    return  (modelList.getLineItem(row));
  }
  
  public POSLineItem[] getLineItems() {
	    return modelList.getLineItems();
	  }

  /**
   * @return
   */
  public POSLineItem getSelectedLineItem () {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return  (null);
    return  (modelList.getLineItem(row));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getSelectedLineItems () {
    return  modelList.getSelectedLineItems();
  }
  
  /**
   */
  public void deleteSelectedLineItem () throws BusinessRuleException {
    int row = tblList.getSelectedRow();
    POSLineItem line = getLineItem(row);
    deleteLineItem(line);
  }

  /**
   */
  public void deleteLineItem (POSLineItem line) throws BusinessRuleException {
    line.delete();
    modelList.removeRowInModel(line);
    modelList.fireTableDataChanged();
    this.selectLastRow();
  }

  /**
   * @return
   */
  public boolean isRowSelected () {
    int row = tblList.getSelectedRow();
    if (row < 0)
      return  (false);
    else
      return  (true);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isRowChecked () {
    return  modelList.getRowSelected(tblList.getSelectedRow());
  }

  /*  private class CommentsEditor extends DefaultCellEditor {
   */
  /**
   * Textfield
   */
  /*
   private JCMSTextField txtField;
   */
  /**
   * DefaultBackground
   */
  /*
   private Color DefaultBackground;
   */
  /**
   * DefaultForeground
   */
  /*
   private Color DefaultForeground;
   */
  /**
   * Default Constructor
   */
  /*
   public CommentsEditor() {
   super(new JCMSTextField());
   txtField = (JCMSTextField)getComponent();
   txtField.setFont(new Font("Helvetica", 1, 12));
   txtField.setForeground(new Color(0, 0, 128));
   txtField.setBackground(Color.white);
   txtField.setEditable(false);
   txtField.setEnabled(false);
   setForeground(new Color(0, 0, 128));
   setBackground(Color.white);
   DefaultBackground = getBackground();
   DefaultForeground = getForeground();
   setOpaque(true);
   txtField.setOpaque(true);
   this.setClickCountToStart(1);
   //Enables to stop editing of cell
   txtField.addFocusListener(new FocusAdapter() {
   */
  /**
   * put your documentation comment here
   * @param fe
   */
  /*
   public void focusLost(FocusEvent fe) {
   //stopCellEditing();
   }
   });
   }
   */
  /**
   * Stop editing of cell
   * @return boolean
   */
  /*
   public boolean stopCellEditing() {
   updateComment(txtField.getText());
   super.fireEditingStopped();
   return true;
   }
   */
  /**
   * put your documentation comment here
   * @param sText
   */
  /*
   private void updateComment(String sText) {
   if (tblList.getSelectedRow() < 0 || sText == null)
   return;
   modelList.setCommentAt(sText, getSelectedRow());
   }
   public void updateLastComment() {
   if (tblList.getSelectedRow() < 0 || txtField.getText() == null)
   return;
   modelList.setCommentAt(txtField.getText(), modelList.getLastSelectedItemRow());
   }
   */
  /**
   * Return editing component
   * @param table JTable
   * @param value Object
   * @param isSelected boolean
   * @param row int
   * @param column int
   * @return Component
   */
  /*
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected
   , int row, int column) {
   JCMSTextField txt = new JCMSTextField();
   txt.setAppMgr(CMSApplet.theAppMgr);
   JCMSTextField ftf = (JCMSTextField)super.getTableCellEditorComponent(table, value, isSelected
   , row, column);
   if (value == null)
   ftf.setText("");
   else
   ftf.setText((String)value);
   modelList.setLastSelectedItemRow(getSelectedRow());
   if (isFiscalDocAlreadyPrinted()) {
   System.out.println("INSIDE #@)*$)(#*$)(#*$)*@)(#*$)(#*$)(#*)(*$#)");
   CMSApplet.theAppMgr.setEditAreaFocus();
   return ftf;
   }
   selectRow(row, true);
   if (isSelected) {
   setForeground(Color.white);
   setBackground(new Color(0, 0, 128));
   ftf.setForeground(Color.white);
   ftf.setBackground(new Color(0, 0, 128));
   } else {
   setBackground(DefaultBackground);
   setForeground(DefaultForeground);
   ftf.setBackground(DefaultBackground);
   ftf.setForeground(DefaultForeground);
   }
   return ftf;
   }
   }*/
  private class CheckBoxRenderer extends JCheckBox
      implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     */
    public CheckBoxRenderer () {
      super();
      setHorizontalAlignment(SwingConstants.CENTER);
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
    }

    /**
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
      Boolean b = (Boolean)value;
      if (b != null)
        setSelected(b.booleanValue());
      setBackground(DefaultBackground);
      boolean rowSelected = modelList.getRowSelected(row);
      this.setEnabled(true);
      if (rowSelected)
        setForeground(Color.red);
      else
        setForeground(DefaultForeground);
      return  this;
    }
  }

  private class TextRenderer extends JLabel
      implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     */
    public TextRenderer () {
      super();
      setFont(new Font("Helvetica", 1, 12));
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
      setOpaque(true);
    }

    /**
     */
    public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int col) {
      if (value != null)
        setText(value.toString());
      else
        setText("");
      switch (col) {
        case SKU:case ITEM_DESCRIPTION:case ITEM_COMMENT:case QUANTITY:
          setHorizontalAlignment(JLabel.LEFT);
          break;
        default:
          setHorizontalAlignment(JLabel.CENTER);
          break;
      }
      if (isSelected) {
        setForeground(Color.white);
        FiscalItemsModel itemModel = (FiscalItemsModel)table.getModel();
        if (itemModel.isSale(row))
          setBackground(new Color(0, 0, 128));
        else if (itemModel.isLayaway(row))
          setBackground(new Color(0, 132, 121));
        else if (itemModel.isPreSaleOpen(row))
          setBackground(new Color(0, 128, 255));
        else
          setBackground(Color.red);
      }
      else {
        setBackground(DefaultBackground);
        FiscalItemsModel itemModel = (FiscalItemsModel)table.getModel();
        if (itemModel.isSale(row))
          setForeground(DefaultForeground);
        else if (itemModel.isLayaway(row))
          setForeground(new Color(0, 132, 121));
        else if (itemModel.isPreSaleOpen(row))
          setForeground(new Color(0, 128, 255));
        else
          setForeground(Color.red);
      }
      return  (this);
    }
  }

  /**
   * @param e
   */
  public void resetColumnWidths () {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    TableColumnModel tblColModel = tblList.getColumnModel();
    TableColumn colITem = tblColModel.getColumn(SKU);
    colITem.setPreferredWidth((int)(115*r));
    TableColumn colQty = tblColModel.getColumn(QUANTITY);
    colQty.setPreferredWidth((int)(50*r));
    //    if (bIsVATInvoice) {
    TableColumn colComment = tblColModel.getColumn(ITEM_COMMENT);
    colComment.setPreferredWidth((int)(300*r));
    TableColumn colItemDesc = tblColModel.getColumn(ITEM_DESCRIPTION);
    colItemDesc.setPreferredWidth(tblList.getWidth() - (colITem.getPreferredWidth()/* + colSelect.getPreferredWidth()*/
        + colQty.getPreferredWidth() + colComment.getPreferredWidth()));
    //    } else {
    //      TableColumn colItemDesc = tblColModel.getColumn(ITEM_DESCRIPTION);
    //      colItemDesc.setPreferredWidth(tblList.getWidth()
    //          - (colITem.getPreferredWidth() + colSelect.getPreferredWidth() + colQty.getPreferredWidth()));
    //    }
    modelList.setRowsShown(tblList.getHeight()/tblList.getRowHeight());
  }

  /**
   * put your documentation comment here
   * @param value
   */
  public void updateComment (String value) {
    modelList.setCommentAt(value, getSelectedRow());
  }
}



