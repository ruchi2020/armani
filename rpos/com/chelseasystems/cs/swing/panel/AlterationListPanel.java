/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cs.swing.model.AlterationListModel;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.swing.bean.ArmJCMSTable;
import com.chelseasystems.cr.appmgr.mask.MaskManager;
import java.util.ResourceBundle;


/**
 * <p>Title:AlterationListPanel </p>
 * <p>Description: Alteration List </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04-21-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class AlterationListPanel extends JPanel implements PageNumberGetter { //, PropertyChangeListener
  /**
   * Model
   */
  private AlterationListModel modelList;
  /**
   * Renderer
   */
  private TextRenderer renderer;
  /**
   * Table
   */
  private JCMSTable tblList;
  /**
   * IApplicationManager
   */
  private IApplicationManager theAppMgr;
  // This label is just used to
  // update subtotal on mainscreen.
  // -- propertyChanged() event is
  // captured in applet to update the price
  private JLabel lblSubTotal = new JLabel();
  /**
   * Select column
   */
  private final int SELECT = 0;
  /**
   * Alteration Column
   */
  private final int ALTERATION_CODE = 1;
  /**
   * Description Column
   */
  private final int DESCRIPTION = 2;
  /**
   * Price Column
   */
  private final int PRICE = 3;
  /**
   * Key filter for Currency
   */
  private final String CURRENCY_FILTER = "$0123456789.,";
  private IntegerEditor integerEditor;
  private ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

  /**
   * Default Constructor
   */
  public AlterationListPanel() {
    modelList = new AlterationListModel();
    tblList = new ArmJCMSTable(modelList, JCMSTable.SELECT_ROW);
    renderer = new TextRenderer();
    integerEditor = new IntegerEditor();
    try {
      jbInit();
      tblList.addMouseListener(new MouseAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseClicked(MouseEvent e) {
          toggleRowSelection();
          //            tblList.getCellEditor(tblList.getSelectedRow(), PRICE).getTableCellEditorComponent(tblList, "", true, tblList.getSelectedRow(), PRICE).setBackground(Color.red);
          //            //tblList.getCellEditor(tblList.getSelectedRow(), PRICE).shouldSelectCell(e);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Set AppMgr
   * @param theAppMgr IApplicationManager
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    if (theAppMgr != null) {
      this.theAppMgr = theAppMgr;
      tblList.setAppMgr(theAppMgr);
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
      integerEditor.txtField.setFont(theAppMgr.getTheme().getTextFieldFont());
    }
  }

  /**
   * Select a line by alteration code in current page
   * @param sAlterationCode String
   * @return boolean
   */
  public boolean selectByAlterationCodeInPage(String sAlterationCode) {
    int iRowToSelect = modelList.findByAlterationCode(sAlterationCode);
    if (iRowToSelect == -1)
      return false;
    selectRow(iRowToSelect, true);
    return true;
  }

  /**
   * Select a line by alteration code in whole list.
   * @param sAlterationCode String
   */
  public void selectByAlterationCodeInList(String sAlterationCode) {
    modelList.selectRowByAlterationCode(sAlterationCode);
    modelList.fireTableDataChanged();
  }

  /**
   * Select a row by AlterationDetail
   * @param alterationDetail AlterationDetail
   */
  public void selectByAlterationDetailInList(AlterationDetail alterationDetail) {
    if (alterationDetail == null)
      return;
    modelList.selectByAlterationDetailInList(alterationDetail);
    modelList.fireTableDataChanged();
  }

  /**
   * Add alteration detail to the list
   * @param alterationDetail AlterationDetail
   */
  public void addAlterationDetail(AlterationDetail alterationDetail) {
    if (alterationDetail == null)
      return;
    modelList.addALteration(alterationDetail, false);
  }

  /**
   * Return Checked alterationDetails
   * @return AlterationDetail[]
   */
  public AlterationDetail[] getSelectedAlterationDetails() {
    return modelList.getSelectedAlterationDetails();
  }

  /**
   * Update price for selected row into the alteration detail object.
   * @param sText String
   */
  private void updatePrice(String sText) {
    if (tblList.getSelectedRow() < 0 || sText == null || sText.trim().length() < 1)
      return;
    modelList.setPriceAt(new ArmCurrency(sText), tblList.getSelectedRow());
    fireSubtotalChange();
  }

  /**
   * This method internally updates
   * new subtotal for Applet
   */
  private void fireSubtotalChange() {
    lblSubTotal.setText(modelList.getSubTotal().formattedStringValue());
  }

  /**
   * Reference to subtotal label
   * @return JLabel
   */
  public JLabel getSubTotalLbl() {
    return lblSubTotal;
  }

  /**
   * Toggle row selection
   */
  public void toggleRowSelection() {
    int iRow = tblList.getSelectedRow();
    modelList.setRowSelected(iRow);
    modelList.fireTableDataChanged();
    fireSubtotalChange();
    if (modelList.getRowSelected(iRow)) {
      selectRow(iRow, false);
    }
  }

  /**
   * Select Last row
   */
  public void selectLastRow() {
    int rowCount = tblList.getRowCount();
    if (rowCount < 1) {
      modelList.prevPage();
    }
    if (rowCount > 0) {
      tblList.setRowSelectionInterval(rowCount - 1, rowCount - 1);
    }
    fireSubtotalChange();
  }

  /**
   * Select Row
   * @param row ROWINDEX
   * @param bEnforceSelection ToggleCheckbox
   */
  public void selectRow(int row, boolean bEnforceSelection) {
    if (bEnforceSelection) {
      modelList.selectRow(row);
      modelList.fireTableDataChanged();
    }
    ListSelectionModel model = tblList.getSelectionModel();
    model.setSelectionInterval(row, row);
    fireSubtotalChange();
  }

  /**
   * Delete all rows.
   */
  public void clear() {
    if (modelList.getAllRows().size() < 1)
      return;
    modelList.clear();
    modelList.fireTableDataChanged();
  }

  /**
   * Get Subtotal
   * @return Currency
   */
  public ArmCurrency getSubTotal() {
    return modelList.getSubTotal();
  }

  /**
   * Select first row
   */
  public void selectFirstRow() {
    selectRow(0, true);
  }

  /**
   * Total Pages
   * @return int
   */
  public int getTotalPages() {
    return (modelList.getPageCount());
  }

  /**
   * Current PageNumber
   * @return int
   */
  public int getCurrentPageNumber() {
    return (modelList.getCurrentPageNumber());
  }

  /**
   * Nextpage
   */
  public void nextPage() {
    modelList.nextPage();
  }

  /**
   * PreviousPage
   */
  public void prevPage() {
    modelList.prevPage();
  }

  /**
   * Initialize components
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    this.setLayout(new BorderLayout());
    tblList.getTableHeader().setPreferredSize(new Dimension(833, 45));
    this.add(tblList.getTableHeader(), BorderLayout.NORTH);
    this.add(tblList, BorderLayout.CENTER);
    modelList.setRowsShown(13);
    TableColumnModel modelColumn = tblList.getColumnModel();
    for (int i = 0; i < modelList.getColumnCount(); i++) {
      if (i != PRICE)
        modelColumn.getColumn(i).setCellRenderer(renderer);
    }
    CheckBoxRenderer boxRenderer = new CheckBoxRenderer();
    modelColumn.getColumn(0).setCellRenderer(boxRenderer);
    modelColumn.getColumn(PRICE).setCellEditor(integerEditor);
    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        resetColumnWidths();
      }
    }); tblList.setRequestFocusEnabled(false);
    tblList.setCellSelectionEnabled(false);
    tblList.setColumnSelectionAllowed(false);
    tblList.setRowSelectionAllowed(true);
  }

  /**
   * Reset Column widths
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    TableColumnModel tblColModel = tblList.getColumnModel();
    TableColumn colSelect = tblColModel.getColumn(SELECT);
    colSelect.setPreferredWidth((int)(65 * r));
    TableColumn colCode = tblColModel.getColumn(ALTERATION_CODE);
    colCode.setPreferredWidth((int)(65 * r));
    TableColumn colPrice = tblColModel.getColumn(PRICE);
    colPrice.setPreferredWidth((int)(125 * r));
    TableColumn colDesc = tblColModel.getColumn(DESCRIPTION);
    colDesc.setPreferredWidth(tblList.getWidth()
        - (colSelect.getPreferredWidth() + colCode.getPreferredWidth() + colPrice.getPreferredWidth()));
    modelList.setRowsShown(tblList.getHeight() / tblList.getRowHeight());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public AlterationListModel getAddressModel() {
    return modelList;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTable getTable() {
    return tblList;
  }

  /**
   *
   * <p>Title:IntegerEditor </p>
   * <p>Description:Editor for price cell. </p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Manpreet S Bawa
   * @version 1.0
   */
  private class IntegerEditor extends DefaultCellEditor {
    /**
     * Textfield
     */
    private JCMSTextField txtField;
    /**
     * DefaultBackground
     */
    private Color DefaultBackground;
    /**
     * DefaultForeground
     */
    private Color DefaultForeground;

    /**
     * Default Constructor
     */
    public IntegerEditor() {
      super(new JCMSTextField());
      txtField = (JCMSTextField)getComponent();
      txtField.setFont(new Font("Helvetica", 1, 12));
      txtField.setForeground(new Color(0, 0, 128));
      txtField.setBackground(Color.white);
      setForeground(new Color(0, 0, 128));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
      setOpaque(true);
      txtField.setOpaque(true);
      this.setClickCountToStart(1);
      //Enables to stop editing of cell
      txtField.addFocusListener(new FocusAdapter() {

        /**
         * put your documentation comment here
         * @param fe
         */
        public void focusLost(FocusEvent fe) {
          stopCellEditing();
        }

        /**
         * put your documentation comment here
         * @param e
         */
        public void focusGained(FocusEvent e) {
          if (e.getSource() instanceof JCMSTextField) {
            //                 System.out.println("getComponent Focus:: "+e.getComponent().getClass().getName());
            ((JCMSTextField)e.getSource()).invalidate();
            ((JCMSTextField)e.getSource()).selectAll();
          }
        }
      });
      txtField.addMouseListener(new MouseAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseClicked(MouseEvent e) {
          if (e.getSource() instanceof JCMSTextField) {
            //                          System.out.println("getComponent Mouse:: " + e.getComponent().getClass().getName());
            ((JCMSTextField)e.getSource()).invalidate();
            ((JCMSTextField)e.getSource()).selectAll();
            ((JCMSTextField)e.getSource()).requestFocus();
          }
        }

        /**
         * put your documentation comment here
         * @param e
         */
        public void mousePressed(MouseEvent e) {
          if (e.getSource() instanceof JCMSTextField) {
            //                         System.out.println("getComponent Mouse:: " + e.getComponent().getClass().getName());
            ((JCMSTextField)e.getSource()).invalidate();
            ((JCMSTextField)e.getSource()).selectAll();
            ((JCMSTextField)e.getSource()).requestFocus();
          }
        }
      });
      // Filtering the text
      // to make sure only numeric value is added.
      txtField.addKeyListener(new KeyAdapter() {

        /**
         * put your documentation comment here
         * @param ke
         */
        public void keyPressed(KeyEvent ke) {
          if (ke.isControlDown()) {
            ke.consume();
            return;
          }
        }

        /**
         * put your documentation comment here
         * @param ke
         */
        public void keyTyped(KeyEvent ke) {
          StringBuffer sTmp = new StringBuffer(txtField.getText());
          if (CURRENCY_FILTER.indexOf(ke.getKeyChar()) == -1) {
            ke.consume();
            return;
          }
          if (ke.getKeyChar() == '$' && txtField.getCaretPosition() > 0) {
            ke.consume();
            return;
          }
          if (ke.getKeyChar() == '.' && sTmp.toString().indexOf(".") > 0) {
            ke.consume();
            return;
          }
        }
      });
    }

    /**
     * Stop editing of cell
     * @return boolean
     */
    public boolean stopCellEditing() {

      Object amount=MaskManager.getInstance(theAppMgr).getMaskVerify(txtField.getText(), theAppMgr.CURRENCY_MASK);
      if (amount== null){        
        txtField.setText("0.00");
        theAppMgr.showErrorDlg(res.getString("Please enter the amount in correct format"));
        return false;
      }else{        
        updatePrice(txtField.getText());
        super.fireEditingStopped();
        return true;
      }
    }

    /**
     * Return editing component
     * @param table JTable
     * @param value Object
     * @param isSelected boolean
     * @param row int
     * @param column int
     * @return Component
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected
        , int row, int column) {
      JCMSTextField txt = new JCMSTextField();
      txt.setAppMgr(theAppMgr);
      JCMSTextField ftf = (JCMSTextField)super.getTableCellEditorComponent(table, value, isSelected
          , row, column);
      String sTmp = (String)value;
      // Remove $ sign if present.
      if (sTmp.indexOf("$") != -1) {
        sTmp = sTmp.substring(1);
      }
      ftf.setText(sTmp);
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
  }


  /**
   *
   * <p>Title:TextRenderer </p>
   * <p>Description: Renders all text cells.</p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Manpreet S Bawa
   * @version 1.0
   */
  private class TextRenderer extends JLabel implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     * Default constructor.
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
     * Renderer
     * @param table JTable
     * @param value Object
     * @param isSelected boolean
     * @param hasFocus boolean
     * @param row int
     * @param col int
     * @return Component
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int col) {
      if (value != null)
        setText(value.toString());
      else
        setText("");
      switch (col) {
        case SELECT:
        case ALTERATION_CODE:
          setHorizontalAlignment(JLabel.CENTER);
          break;
        case DESCRIPTION:
          setHorizontalAlignment(JLabel.LEFT);
          break;
        case PRICE:
          setHorizontalAlignment(JLabel.RIGHT);
          break;
        default:
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
      return (this);
    }
  }


  /**
   *
   * <p>Title: CheckBoxRenderer</p>
   * <p>Description: Renderer for Checkbox cell</p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Manpreet S Bawa
   * @version 1.0
   */
  private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     */
    public CheckBoxRenderer() {
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
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int column) {
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
      return this;
    }
  }
}

