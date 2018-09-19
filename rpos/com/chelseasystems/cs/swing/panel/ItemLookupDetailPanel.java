/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 04-04-2006 | Sandhya   | PCR1256   | Style locator enhancement                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 07-07-2005 | Vikram    | 368       | Item selection for "Locate" to work with Keyboard  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 05/14/2005 | Vikram    | N/A       | Updated for POS_104665_FS_ItemLookup_Rev1          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/27/2005 | Vikram    | N/A       | POS_104665_FS_ItemLookup_Rev1                      |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.ItemSearchString;
import com.chelseasystems.cs.swing.model.ItemLookupDetailModel;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.util.HTML;
import com.chelseasystems.cs.util.HTMLColumnHeaderUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.config.ConfigMgr;


/**
 * <p>Title:ItemLookupDetailPanel </p>
 * <p>Description: View Grid of Items with different stores, color and size</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class ItemLookupDetailPanel extends JPanel {
  private ItemLookupDetailModel modelItemLookupDetail;
  public JCMSTable tblItem;
  
  private TextRenderer renderer;
  private String homeStoreId;
  private IApplicationManager theAppMgr;
  private HTMLColumnHeaderUtil htmlUtil;
  private static boolean searchItemInMultipleStore = false;
 
	public ItemLookupDetailPanel(IApplicationManager theAppMgr, CMSItem[] cmsItems) {
		this(theAppMgr,cmsItems, null);
	}
  /**
   * put your documentation comment here
   * @param   IApplicationManager theAppMgr
   * @param   CMSItem[] cmsItems
   */
  	public ItemLookupDetailPanel(IApplicationManager theAppMgr, CMSItem[] cmsItems, String currentColorId) {
  		
    this.theAppMgr = theAppMgr;
    
    // TD 
    ConfigMgr configMgr = new ConfigMgr("item.cfg");
	String itemSearchStatus = configMgr.getString("ITEM_SEARCH_IN_MULTIPLE_STORE");
	if( itemSearchStatus != null && itemSearchStatus.trim().equalsIgnoreCase("true") ){
		searchItemInMultipleStore = true;
	}else{
		searchItemInMultipleStore = false;
	}
        
    try {
      if (theAppMgr != null) {
        CMSStore store = (CMSStore)theAppMgr.getGlobalObject("STORE");
        if (store != null)
          homeStoreId = store.getId();
      }
      int iFontSize = theAppMgr.getTheme().getTextFieldFont().getSize();
      String sFontName = theAppMgr.getTheme().getTextFieldFont().getFontName();
      Font fontValues = new Font(sFontName, Font.BOLD, iFontSize);
      htmlUtil = new HTMLColumnHeaderUtil(fontValues);

      ItemSearchString itmSearchStr = (ItemSearchString)theAppMgr.getStateObject("ITEM_LOCATE_SEARCHSTRING");
      String sColorId = null;
      if(itmSearchStr != null) {
      	//Fix for 1842
      	if(itmSearchStr.getColor() != null && itmSearchStr.getColor().trim().length()>0)
    	  sColorId = itmSearchStr.getColor();
    	  //System.out.println("--- sColorId: "+sColorId+" currentColorId: "+currentColorId );
      }else{
    	  //System.out.println("--- ItemSearchString is NULL: " );
      }
      //modelItemLookupDetail = new ItemLookupDetailModel(cmsItems, homeStoreId, currentColorId);
      modelItemLookupDetail = getItemLoolupModel(cmsItems, homeStoreId, currentColorId);
      tblItem = new JCMSTable(modelItemLookupDetail, JCMSTable.SELECT_ROW);
      renderer = new TextRenderer();
      renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
      this.setLayout(new BorderLayout());
      this.add(tblItem.getTableHeader(), BorderLayout.NORTH);
      this.add(tblItem, BorderLayout.CENTER);
      // ---- Commented by - MSB (12/06/2005)
      // Sets number of rows shown to 13
      // and creates vague display.
      // Number of rows calculation is down after
      // resize occurs and this makes sure
      // rows are equally divided in each page.
//      modelItemLookupDetail.setRowsShown(13); // arbitrarily set until resize occurs
      //-----
      TableColumnModel modelColumn = tblItem.getColumnModel();
      for (int i = 0; i < modelItemLookupDetail.getColumnCount(); i++) {
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
      }); tblItem.setRowSelectionAllowed(false);
      tblItem.setCellSelectionEnabled(true);
      if (theAppMgr != null) {
        tblItem.setAppMgr(theAppMgr);
        renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
      }
      //init selection
      rowLoop: {
        for (int i = 0; i < tblItem.getRowCount(); i++) {
          for (int j = 0; j < tblItem.getColumnCount(); j++) {
            CMSItem item = modelItemLookupDetail.getItemAt(i, j);
            if (item != null && item.getStoreId() != null && item.getStoreId().equals(homeStoreId)) {
              tblItem.setRowSelectionInterval(i, i);
              tblItem.setColumnSelectionInterval(j, j);
              break rowLoop;
            }
          }
        }
      }
      tblItem.requestFocus();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  	/**
  	 * Provides the New instance of ItemLookupModel.
  	 * @param cmsItms
  	 * @param hStoreId
  	 * @param sColorId
  	 * @return
  	 */
  	protected ItemLookupDetailModel getItemLoolupModel(CMSItem[] cmsItms, String hStoreId, String sColorId) {
        return new ItemLookupDetailModel(cmsItms, hStoreId, sColorId);
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
   * put your documentation comment here
   * @param itemRowMap
   */
  public void addItemRow(Map itemRowMap) {
    modelItemLookupDetail.addItemRow(itemRowMap);
    selectLastRow();
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void deleteSelectedItem()
      throws BusinessRuleException {
    int row = tblItem.getSelectedRow();
    Map itemRowMap = getItemRowAt(row);
    deleteItem(itemRowMap);
  }

  /**
   * put your documentation comment here
   * @param itemRowMap
   * @exception BusinessRuleException
   */
  public void deleteItem(Map itemRowMap)
      throws BusinessRuleException {
    modelItemLookupDetail.removeRowInModel(itemRowMap);
    modelItemLookupDetail.fireTableDataChanged();
    this.selectLastRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageUp() {
    modelItemLookupDetail.prevPage();
    selectFirstRow();
  }

  /**
   * put your documentation comment here
   */
  public void pageDown() {
    modelItemLookupDetail.nextPage();
    selectLastRow();
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public Map getItemRowAt(int row) {
    return modelItemLookupDetail.getItemRowAt(row);
  }

  /**
   * put your documentation comment here
   * @param l
   */
  public void addMouseListener(MouseListener l) {
    tblItem.addMouseListener(l);
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
  public int getSelectedColumnIndex() {
    return tblItem.getSelectedColumn();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Map getSelectedItemRow() {
    if (getSelectedRowIndex() == -1)
      return null;   
     return getItemRowAt(getSelectedRowIndex());
    
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSItem getSelectedItem() {
    if (getSelectedRowIndex() < 0 || this.getSelectedColumnIndex() < 0)
      return null;   
    return modelItemLookupDetail.getItemAt(getSelectedRowIndex(), getSelectedColumnIndex());
  }
  
  /**
   * put your documentation comment here
   */
  public void selectLastRow() {
    int rowCount = tblItem.getRowCount();
    if (rowCount < 1) {
      modelItemLookupDetail.prevPage();
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
    modelItemLookupDetail.clear();
  }

  /**
   * put your documentation comment here
   */
  public void update() {
    modelItemLookupDetail.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ItemLookupDetailModel getAddressModel() {
    return modelItemLookupDetail;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public JCMSTable getTable() {
    return (this.tblItem);
  }

  /**
   * put your documentation comment here
   */
  public void resetColumnWidths() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    int iWidth = 0;
    TableColumn skuCol = tblItem.getColumnModel().getColumn(ItemLookupDetailModel.STORE);
    skuCol.setPreferredWidth((int)(85 * r));
    TableColumn colorCol = tblItem.getColumnModel().getColumn(ItemLookupDetailModel.COLOR);
    colorCol.setPreferredWidth((int)(65 * r));
    TableColumn col = null;
    for (int i = 2; i < modelItemLookupDetail.getColumnNames().length; i++) {
      col = tblItem.getColumnModel().getColumn(i);
      col.setPreferredWidth((int)(65 * r));
    } 
    this.modelItemLookupDetail.setRowsShown(tblItem.getHeight() / tblItem.getRowHeight());
    tblItem.getTableHeader().setPreferredSize(new Dimension((int)r, 30));
  }

  /**
   * @return int
   */
  public int getCurrentPageNumber() {
    return (modelItemLookupDetail.getCurrentPageNumber());
  }

  /**
   * @return int
   */
  public int getTotalPages() {
    return (modelItemLookupDetail.getPageCount());
  }

  /**
   * put your documentation comment here
   */
  public void nextPage() {
    modelItemLookupDetail.nextPage();
  }

	/**
	 * put your documentation comment here
	 */
	public void prevPage() {
		modelItemLookupDetail.prevPage();
	}

	//___Tim: 1709:Update menus to add "toggle" button. 
	public void toggleShowAvailableQty(boolean showItemAvailableQty){
		renderer.toggleShowAvailableQty(showItemAvailableQty);
		update();
	}
	 
	private class TextRenderer extends JLabel implements TableCellRenderer {
		private Color DEFAULT_FOREGROUND = new Color(0, 0, 175);
		private Color DEFAULT_BACKGROUND = Color.white;
		private Color DISABLED_BACKGROUND = Color.lightGray;
		private Color SELECTION_FOREGROUND = null;
		private Color SELECTION_BACKGROUND = null;
		private boolean showAvailableQty = true;
		private Object operator = null;
		private int noOfFixedColumns = 0;

    /**
     * put your documentation comment here
     */
    public TextRenderer() {
      super();
      setForeground(DEFAULT_FOREGROUND);
      setBackground(DEFAULT_BACKGROUND);
      setOpaque(true);
      //TODO - Need to set default from some property.
      showAvailableQty = true;
      noOfFixedColumns = modelItemLookupDetail.getNoOfFixedColumns();
      setHorizontalAlignment(SwingConstants.CENTER);
    }

    //TD
    public boolean isFocusable(){ 
  	  return false;
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
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected , boolean hasFocus, int row, int col) {
		CMSItem cmsItemToRender = null;
		String textToSet = "";
		String storeId = "";
		if(SELECTION_BACKGROUND == null || SELECTION_FOREGROUND == null ) {
			SELECTION_BACKGROUND = table.getSelectionBackground();
			SELECTION_FOREGROUND = table.getSelectionForeground();
		}
		if (value == null) {
			return this;
		}
		if(operator == null) {
			operator = theAppMgr.getStateObject("OPERATOR");
		}
		
		//if(col == ItemLookupDetailModel.STORE || col == ItemLookupDetailModel.COLOR ) {
		if(col < noOfFixedColumns ) {
			//setHorizontalAlignment(JLabel.CENTER);
			textToSet = value.toString();
			setForeground(DEFAULT_FOREGROUND);
			setBackground(DISABLED_BACKGROUND);
			htmlUtil.setForeground(DEFAULT_FOREGROUND);
			//System.out.println("Value:" + textToSet);
			setText(htmlUtil.getHTMLHeaderFor(textToSet,HTMLColumnHeaderUtil.CENTER));
			//System.out.println("HTML Value:" + htmlUtil.getHTMLHeaderFor(textToSet,HTMLColumnHeaderUtil.LEFT));
		} else {
			ItemLookupDetailModel.ItemRowKey itemRowKey = (ItemLookupDetailModel.ItemRowKey)value;			
			storeId = itemRowKey.storeId;		
	        if (modelItemLookupDetail.getItemRowItemsArrayHash().get(itemRowKey) != null) {
	        	cmsItemToRender = ((CMSItem[])modelItemLookupDetail.getItemRowItemsArrayHash().get(itemRowKey))[col - noOfFixedColumns];	        	
	        }
			if(cmsItemToRender != null) {
				if(searchItemInMultipleStore){
			      	if(showAvailableQty){
						textToSet = cmsItemToRender.getItemStock().getTotalAvailableQuantity() + "\n" + 
						cmsItemToRender.getRetailPrice().getFormattedStringValue();						
			      	}else{
			          	// Tim: 1709:show Unavailable quantity if toggle button clicked
			          	textToSet = cmsItemToRender.getItemStock().getTotalUnAvailableQuantity() + "\n"+
			            cmsItemToRender.getRetailPrice().getFormattedStringValue();
		          	}
				}else{
					// Default as it was in Global Version 
					textToSet = cmsItemToRender.getItemStock().getNetAvailableQuantity() + "\n" +
					cmsItemToRender.getRetailPrice().getFormattedStringValue();
				}
			}else{
				textToSet = getNoCellValueText();
			}
			//setHorizontalAlignment(JLabel.LEFT);
			if(isSelected){
				setForeground(SELECTION_FOREGROUND);
				setBackground(SELECTION_BACKGROUND);
			}else{
				setForeground(DEFAULT_FOREGROUND);
				setBackground(DEFAULT_BACKGROUND);
			}
			if (cmsItemToRender == null || operator == null
					|| (!storeId.equals(homeStoreId))) {
				if (isSelected) {
					setForeground(SELECTION_FOREGROUND);
					setBackground(SELECTION_BACKGROUND);
				} else {
					setForeground(DEFAULT_FOREGROUND);
					setBackground(DISABLED_BACKGROUND);
				}

			}
			htmlUtil.setForeground(getForeground());
			setText(htmlUtil.getHTMLHeaderFor(textToSet,HTMLColumnHeaderUtil.CENTER));
		}
		return (this);
    }

    //___Tim: 1709:Update menus to add "toggle" button. 
    public void toggleShowAvailableQty(boolean showItemAvailableQty){
    	showAvailableQty = showItemAvailableQty;
    }
  }
	protected String getNoCellValueText() {
		return "";
	}
	/*Started #issue 1941 stock locator by neeti
	 * creating new constructor of panel with different arguments for refreshing new panel and
	 * get the item from new model for perticular store in the same applet 
	*/
	 public ItemLookupDetailPanel(CMSItem[] cmsItems,
			IApplicationManager theAppMgr, String currentColorId) {
		if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
			this.theAppMgr = theAppMgr;
			// TD 
			ConfigMgr configMgr = new ConfigMgr("item.cfg");
			String itemSearchStatus = configMgr
					.getString("ITEM_SEARCH_IN_MULTIPLE_STORE");
			if (itemSearchStatus != null
					&& itemSearchStatus.trim().equalsIgnoreCase("true")) {
				searchItemInMultipleStore = true;
			} else {
				searchItemInMultipleStore = false;
			}

			try {
				if (theAppMgr != null) {
					CMSStore store = (CMSStore) theAppMgr
							.getGlobalObject("STORE");
					if (store != null)
						homeStoreId = store.getId();
				}
				int iFontSize = theAppMgr.getTheme().getTextFieldFont()
						.getSize();
				String sFontName = theAppMgr.getTheme().getTextFieldFont()
						.getFontName();
				Font fontValues = new Font(sFontName, Font.BOLD, iFontSize);
				htmlUtil = new HTMLColumnHeaderUtil(fontValues);

				ItemSearchString itmSearchStr = (ItemSearchString) theAppMgr
						.getStateObject("ITEM_LOCATE_SEARCHSTRING");
				String sColorId = null;
				if (itmSearchStr != null) {
					if (itmSearchStr.getColor() != null
							&& itmSearchStr.getColor().trim().length() > 0)
						sColorId = itmSearchStr.getColor();
				} else {

				}
				modelItemLookupDetail = getItemLookupStockModel(cmsItems,
						homeStoreId, currentColorId);
				tblItem = new JCMSTable(modelItemLookupDetail,
						JCMSTable.SELECT_ROW);
				renderer = new TextRenderer();
				renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
				this.setLayout(new BorderLayout());
				this.add(tblItem.getTableHeader(), BorderLayout.NORTH);
				this.add(tblItem, BorderLayout.CENTER);
				TableColumnModel modelColumn = tblItem.getColumnModel();
				for (int i = 0; i < modelItemLookupDetail.getColumnCount(); i++) {
					modelColumn.getColumn(i).setCellRenderer(renderer);
				}
				this
						.addComponentListener(new java.awt.event.ComponentAdapter() {
							public void componentResized(ComponentEvent e) {
								resetColumnWidths();
							}
						});
				tblItem.setRowSelectionAllowed(false);
				tblItem.setCellSelectionEnabled(true);
				if (theAppMgr != null) {
					tblItem.setAppMgr(theAppMgr);
					renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
				}
				rowLoop: {
					for (int i = 0; i < tblItem.getRowCount(); i++) {
						for (int j = 0; j < tblItem.getColumnCount(); j++) {
							CMSItem item = modelItemLookupDetail
									.getItemAt(i, j);
							if (item != null && item.getStoreId() != null
									&& item.getStoreId().equals(homeStoreId)) {
								tblItem.setRowSelectionInterval(i, i);
								tblItem.setColumnSelectionInterval(j, j);
								break rowLoop;
							}
						}
					}
				}
				tblItem.requestFocus();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public ItemLookupDetailModel getItemLookupStockModel(CMSItem[] cmsItms,
			String hStoreId, String sColorId) {
		return new ItemLookupDetailModel(hStoreId, cmsItms, sColorId);
	}
	 
	 //Ended #issue 1941 stock locator by neeti
	 
}

