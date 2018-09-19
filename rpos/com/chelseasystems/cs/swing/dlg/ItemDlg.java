/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.dlg;

import java.awt.*;
import javax.swing.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.IMainFrame;
import com.chelseasystems.cr.swing.MultiLineCellRenderer;
import com.chelseasystems.cr.swing.ScrollProcessor;
import com.chelseasystems.cr.swing.ScrollableTableModel;

import java.awt.event.*;
import com.chelseasystems.cs.swing.model.ItemModel;
import com.chelseasystems.cs.swing.model.ItemModel_JP;
import com.chelseasystems.cs.util.Version;

import java.util.*;
import com.chelseasystems.cr.store.Store;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


/**
 */
public class ItemDlg extends JDialog implements ScrollProcessor {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  JPanel jPanel5 = new JPanel();
  JButton btnScrollUp = new JButton();
  JButton btnScrollDown = new JButton();
  JButton btnOK = new JButton();
  JButton btnCancel = new JButton();
  protected IApplicationManager theAppMgr;
  protected ItemModel model = null;
  DialogTable tblItem = null;
  //JScrollPane pane = new JScrollPane(tblItem);
  private boolean ok = false;
  private CMSItem item;
  double r = com.chelseasystems.cr.swing.CMSApplet.r;
  JPanel jPanel6 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JCMSToolTip ttip;
  // private static MyCellRenderer myCellRenderer = new ItemDlg.MyCellRenderer();
  private static DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
  static {
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
  }

  /**
   * @param    Frame frame
   * @param    IApplicationManager theAppMgr
   * @param    String[] itemIDs
   */
  public ItemDlg(Frame frame, IApplicationManager theAppMgr, String[] itemIDs) {
    super(frame, "Item", true);
    try {
    	
      // Required for pcr 1859
      if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
    	model = new ItemModel_JP();
      } else {
    	model = new ItemModel();
      }
      tblItem = new DialogTable(model, JCMSTable.SELECT_ROW);
      
      this.theAppMgr = theAppMgr;
      ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
      ttip.setType(JCMSToolTip.FOLLOWING);
      jbInit();
      tblItem.setAppMgr(theAppMgr);
      //System.out.println("the value of r: " + r);
      pack();
      setSize((int)(r * 800), (int)(r * 400));
      loadItems(itemIDs);
      this.setModal(true);
      this.setResizable(false);
      TableColumnModel modelColumn = tblItem.getColumnModel();
      for (int i = 0; i < model.getColumnCount(); i++) {
        modelColumn.getColumn(i).setCellRenderer(centerRenderer);
      }
      modelColumn.getColumn(1).setCellRenderer(new MyCellRenderer());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @param    Frame frame
   * @param    IApplicationManager theAppMgr
   * @param    String[] itemIDs
   */
  public ItemDlg(Frame frame, IApplicationManager theAppMgr, Item[] itemArray) {
    super(frame, "Item", true);
    try {
        // Required for pcr 1859
        if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
      	  System.out.println("KEN: Creating Japanese model.");
      	model = new ItemModel_JP();
        } else {
      	model = new ItemModel();
        }
        tblItem = new DialogTable(model, JCMSTable.SELECT_ROW);
        
      this.theAppMgr = theAppMgr;
      ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
      ttip.setType(JCMSToolTip.FOLLOWING);
      jbInit();
      tblItem.setAppMgr(theAppMgr);
      //System.out.println("the value of r: " + r);
      pack();
      setSize((int)(r * 800), (int)(r * 400));
      loadItems(removeDuplicatesAndSortItems(itemArray));
      this.setModal(true);
      this.setResizable(false);
      TableColumnModel modelColumn = tblItem.getColumnModel();
      for (int i = 0; i < model.getColumnCount(); i++) {
    		modelColumn.getColumn(i).setCellRenderer(centerRenderer);
      }
      // Fix for issue 1859
      modelColumn.getColumn(1).setCellRenderer(new MyCellRenderer());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @param ids
   * @exception Exception
   */
  private void loadItems(String[] ids)
      throws Exception {
    if (ids == null)
      return;
    CMSItem[] items = new CMSItem[ids.length];
    Store store = (Store)theAppMgr.getGlobalObject("STORE");
    for (int x = 0; x < ids.length; x++) {
      //         items[x] = CMSItemHelper.findById(theAppMgr, ids[x]);
      items[x] = CMSItemHelper.findByBarCode(theAppMgr, ids[x], store.getId());
    }
    Arrays.sort(items, new Comparator() {

      /**Fix for 1842
       * put your documentation comment here
       * @param o1
       * @param o2
       * @return
       */
      public int compare(Object o1, Object o2) {
        CMSItem cmsItem1 = (CMSItem)o1;
        CMSItem cmsItem2 = (CMSItem)o2;
        // Sort on basis of
        // Description, Color and Size
        if(cmsItem1.getColorId()!= null) {
        String s1 = cmsItem1.getDescription().toLowerCase().trim() +","+
            cmsItem1.getColorId().toLowerCase().trim() + "," + getSizeIndex(cmsItem1)
            +","+ cmsItem1.getId().trim();
        String s2 = cmsItem2.getDescription().toLowerCase().trim() +","+
            cmsItem2.getColorId().toLowerCase().trim() + "," + getSizeIndex(cmsItem2)
            +","+ cmsItem2.getId().trim();
        return s1.compareTo(s2);
        }else {
        	String s1 = cmsItem1.getDescription().toLowerCase().trim() +","+
           "," + getSizeIndex(cmsItem1)+","+ cmsItem1.getId().trim();	
        	String s2 = cmsItem2.getDescription().toLowerCase().trim() +","+
          "," + getSizeIndex(cmsItem2)+","+ cmsItem2.getId().trim();
        	return s1.compareTo(s2);
        }
        }
    });
    loadItems(items);
  }

  private void loadItems(CMSItem[] items){
    for (int i = 0; i < items.length; i++)
      model.addItem(items[i]);
  }

  private void loadItems(Item[] items){
	  for (int i = 0; i < items.length; i++)
	      model.addItem((CMSItem)items[i]);
  }
  //Fix for 1842
  private String getItemKey(CMSItem item){
  	StringBuffer itemKey = new StringBuffer();
  	if(item.getColorId() != null) {
  	itemKey.append(item.getId().trim()).append(",")
  		.append(item.getDescription().trim()).append(",")
  		.append(getSizeIndex(item)).append(",")
  		.append(item.getColorId().trim());
  	return itemKey.toString();
  	}else {
  		itemKey.append(item.getId().trim()).append(",")
  		.append(item.getDescription().trim()).append(",")
  		.append(getSizeIndex(item));
  		return itemKey.toString();	
  	}
  	
  	
  }
  
  private Item[] removeDuplicatesAndSortItems(Item[] items){
  	Item[] noDuplicateItems;
  	HashMap itemsHashMap = new HashMap();// to remove duplicates 	
	for(int i=0; i<items.length; i++){
  		if(!itemsHashMap.containsKey(getItemKey((CMSItem)items[i])))
  			itemsHashMap.put(getItemKey((CMSItem)items[i]),items[i]);
  	}
  	
  	ArrayList noDuplicatesItemsList = new ArrayList(itemsHashMap.values());
  	noDuplicateItems = (Item[])noDuplicatesItemsList.toArray(new Item[0]);

// to sort based on ItemID, 
//	however this removes items with same ID but different prices coming from different stores  	
//  	TreeMap itemsTreeMap = new TreeMap(); // to sort based on ItemID
//  	for(int i=0; i<noDuplicateItems.length; i++){
//  		itemsTreeMap.put(noDuplicateItems[i].getId().trim(),noDuplicateItems[i]);
//  	}
//  	ArrayList sortedItemsList = new ArrayList(itemsTreeMap.values());
//  	return (Item[])sortedItemsList.toArray(new Item[0]);
 
//  to sort based only on ItemID,   	
    // Required for pcr 1859
    if (!"JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
    	Arrays.sort(noDuplicateItems, new Comparator() {
    		public int compare(Object o1, Object o2) {
    			Item item1 = (Item)o1;
    			Item item2 = (Item)o2;
    			// Sort on basis of itemID
    			String s1 = item1.getId().trim();
    			String s2 = item2.getId().trim();
    			return s1.compareTo(s2);
    		}
    	});
    } else {
      	Arrays.sort(noDuplicateItems, new Comparator() {
    		public int compare(Object o1, Object o2) {
    			CMSItem item1 = (CMSItem)o1;
    			CMSItem item2 = (CMSItem)o2;
    			// year and season descending
    			String year1 = item1.getForTheYear().trim();
    			String year2 = item2.getForTheYear().trim();
    			if (year1.equals(year2)) {
    				String season1 = item1.getSeason();
    				String season2 = item2.getSeason();
    				if (season1.equals(season2)){
    					String color1 = getColor(item1);
    					String color2 = getColor(item2);
    					if (color1.equals(color2)) {
    						String size1 = getSize(item1);
    						String size2 = getSize(item2);
    						return size1.compareTo(size2);
    					} else {
    						return color1.compareTo(color2);
    					}
    				} else {
    					return -(season1.compareTo(season2));
    				}
    			} else {
    				return -(year1.compareTo(year2));
    			}
    		}
    	});
    }
  	
  	return noDuplicateItems;
  	
 }
  
  String getColor(CMSItem item) {
      String colorString = "";
      if (item.getColorId() != null && item.getColorId().trim().length() > 0)
        colorString = item.getColorId();
      if (item.getItemDetail().getColorDesc() != null
          && item.getItemDetail().getColorDesc().trim().length() > 0)
        colorString = colorString + "-" + item.getItemDetail().getColorDesc();
      return colorString;
  }
  
  String getSize(CMSItem item) {
      String sizeIndex = item.getItemDetail().getSizeIndx();
      if ((sizeIndex == null || sizeIndex.equals("")) && item.getSizeId() != null)
        sizeIndex = item.getSizeId().trim();
      else if (sizeIndex == null)
        sizeIndex = "";
      if (item.getItemDetail().getExtSizeIndx() != null
          && !item.getItemDetail().getExtSizeIndx().trim().equals(""))
        sizeIndex = sizeIndex.trim() + ":" + item.getItemDetail().getExtSizeIndx().trim();
      if (item.getItemDetail().getSizeDesc() != null
          && !item.getItemDetail().getSizeDesc().trim().equals(""))
        sizeIndex = sizeIndex.trim() + " (" + item.getItemDetail().getSizeDesc().trim() + ")";
      return sizeIndex;
  }
  
  private String getSizeIndex(CMSItem item)
  {
    String sizeIndex = item.getItemDetail().getSizeIndx();
    if ((sizeIndex == null || sizeIndex.equals("")) && item.getSizeId() != null)
      sizeIndex = item.getSizeId().trim();
    else if (sizeIndex == null)
      sizeIndex = "";
    if (item.getItemDetail().getExtSizeIndx() != null
        && !item.getItemDetail().getExtSizeIndx().trim().equals(""))
      sizeIndex = sizeIndex.trim() + ":" + item.getItemDetail().getExtSizeIndx().trim();
    if (item.getItemDetail().getSizeDesc() != null
        && !item.getItemDetail().getSizeDesc().trim().equals(""))
      sizeIndex = sizeIndex.trim() + " (" + item.getItemDetail().getSizeDesc().trim() + ")";
      return sizeIndex;
  }

  /**
   * @exception Exception
   */
  void jbInit()
      throws Exception {
    panel1.setLayout(borderLayout1);
    jPanel6.setLayout(borderLayout3);
    jPanel1.setLayout(new GridLayout(1, 4, 0, 0));
    jPanel2.setLayout(borderLayout2);
    btnScrollUp = theAppMgr.getTheme().getDefaultBtn();
    btnScrollUp.setText("Page Up");
    btnScrollUp.setMnemonic('U');
    btnScrollDown = theAppMgr.getTheme().getDefaultBtn();
    btnScrollDown.setText("Page Down");
    btnScrollDown.setMnemonic('D');
    btnOK = theAppMgr.getTheme().getDefaultBtn();
    btnOK.setText("OK");
    btnOK.setMnemonic('O');
    btnOK.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnOK_actionPerformed(e);
      }
    });
    btnCancel = theAppMgr.getTheme().getDefaultBtn();
    btnCancel.setText("Cancel");
    btnCancel.setMnemonic('C');
    btnCancel.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnCancel_actionPerformed(e);
      }
    });
    btnScrollUp.setDefaultCapable(false);
    btnScrollUp.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnScrollUp_actionPerformed(e);
      }
    });
    btnScrollDown.setDefaultCapable(false);
    btnScrollDown.addActionListener(new java.awt.event.ActionListener() {

      /**
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        btnScrollDown_actionPerformed(e);
      }
    });
    tblItem.addMouseListener(new java.awt.event.MouseAdapter() {

      /**
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        tblItem_mouseClicked(e);
      }
    });
    tblItem.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        tblItem_componentResized(e);
      }
    });
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(btnScrollUp, null);
    jPanel1.add(btnScrollDown, null);
    jPanel1.add(btnOK, null);
    jPanel1.add(btnCancel, null);
    panel1.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jPanel3, BorderLayout.NORTH);
    jPanel2.add(jPanel4, BorderLayout.WEST);
    jPanel2.add(jPanel5, BorderLayout.EAST);
    jPanel2.add(jPanel6, BorderLayout.CENTER);
    jPanel6.add(tblItem.getTableHeader(), BorderLayout.NORTH);
    jPanel6.add(tblItem, BorderLayout.CENTER);
    jPanel1.setBackground(theAppMgr.getBackgroundColor());
    jPanel2.setBackground(theAppMgr.getBackgroundColor());
    jPanel3.setBackground(theAppMgr.getBackgroundColor());
    jPanel4.setBackground(theAppMgr.getBackgroundColor());
    jPanel5.setBackground(theAppMgr.getBackgroundColor());
    jPanel6.setBackground(theAppMgr.getBackgroundColor());
    KeyListener keyListener = new KeyListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void keyReleased(KeyEvent e) {}

      /**
       * put your documentation comment here
       * @param e
       */
      public void keyPressed(KeyEvent e) {}

      /**
       * put your documentation comment here
       * @param e
       */
      public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        if (ch == '\n') { //KeyEvent.VK_ENTER
          btnOK.doClick();
          e.consume();
        } else if (ch == 0x1B) { //KeyEvent.VK_ESCAPE
          btnCancel.doClick();
          e.consume();
        }
      }
    };
    this.addKeyListener(keyListener);
    tblItem.registerKeyboardAction(tblItem, JCMSTable.SELECT_CMD,
  		  KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JTable.WHEN_FOCUSED);
  }

  // this event is called by the JCMSTable when the user arrows up beyond the displayed rows
  public void prevPage() {
    btnScrollUp.doClick();
  }

  // this event is called by the JCMSTable when the user arrows dn beyond the displayed rows
  public void nextPage() {
    btnScrollDown.doClick();
  }

  // return model page number
  public int getCurrentPageNumber() {
    return model.getCurrentPageNumber();
  }

  // return total page number
  public int getPageCount() {
    return model.getPageCount();
  }

  /**
   * @param e
   */
  void btnScrollUp_actionPerformed(ActionEvent e) {
    model.prevPage();
    tblItem.setRowSelectionInterval(0, 0);
    fireButtonClick(e);
  }

  /**
   * @param e
   */
  void btnScrollDown_actionPerformed(ActionEvent e) {
    model.nextPage();
    tblItem.setRowSelectionInterval(0, 0);
    fireButtonClick(e);
  }

  /**
   */
  private void fireButtonClick(ActionEvent e) {
    MouseEvent me = new MouseEvent((JComponent)e.getSource(), e.getID(), System.currentTimeMillis()
        , e.getModifiers(), -40, -5, 1, false);
    ttip.setText("Page: " + (model.getCurrentPageNumber() + 1));
    ttip.show(me);
  }

  /**
   * @param visible
   */
  public void setVisible(boolean visible) {
    if (visible) {
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
    } else {
    	//TD
        //ttip.hide();
    	ttip.setVisible(false);
    }
    super.setVisible(visible);
  }

  /**
   * @return
   */
  public String getItemId() {
    return item.getId();
  }

  public CMSItem getItem() {
    return item;
  }

  /**
   * @return
   */
  public String getItemBarCode() {
    return item.getBarCode();
  }

  /**
   * @param e
   */
  void tblItem_mouseClicked(MouseEvent e) {
    btnOK_actionPerformed(null);
  }

  /**
   * @param e
   */
  void btnCancel_actionPerformed(ActionEvent e) {
    ok = false;
    dispose();
  }

  /**
   * @param e
   */
  void btnOK_actionPerformed(ActionEvent e) {
    int row = tblItem.getSelectedRow();
    if (row < 0) {
      theAppMgr.showErrorDlg(com.chelseasystems.cr.util.ResourceManager.getResourceBundle().
          getString("You must first select an item."));
      return;
    }
    item = (CMSItem)model.getItem(row);
    ok = true;
    dispose();
  }

  /**
   * @return
   */
  public boolean isOK() {
    return ok;
  }

  /**
   * @param e
   */
  void tblItem_componentResized(ComponentEvent e) {
    model.setColumnWidth(tblItem);
    model.setRowsShown(tblItem.getHeight() / tblItem.getRowHeight());
  }

  // Fix for issue 1862
  private class MyCellRenderer extends MultiLineCellRenderer {
      public MyCellRenderer() {
    	super();
        // setLineWrap(true);
        // setWrapStyleWord(true);
        // setMargin(new Insets(0, 5, 0, 5));
        setForeground(centerRenderer.getForeground());
        setFont(centerRenderer.getFont());
        // centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
     }
  
    public Component getTableCellRendererComponent(JTable table, Object
            value, boolean isSelected, boolean hasFocus, int row, int column) {
    	super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setForeground(centerRenderer.getForeground());
        setFont(centerRenderer.getFont());
  	    setText((String)value);
  	    
    	// setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa bbbbbbbbbbbbbbbbbbb ccccccc ddddddddd jjjjjjjjjjjjjjjj dddddddddddddd ssssssssssssssssssssss dddddddddddddddddddddd aaaaaaaaaa");
  	     
        setSize(table.getColumnModel().getColumn(column).getWidth(),
                getPreferredSize().height);
        
        return this;
    }
 } 

  
  // extended this class to fix editArea focus problem. JCMSTable returns focus to parent frame. 
  // So if it is used in a dialog/pop-up te focus does not return to the parent frame.
  private class DialogTable extends JCMSTable{
	  
	  public DialogTable(ScrollableTableModel model, int type) {
		  super(model, type);
		  // Fix for issue 1862, set col width for description
		  setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		  TableColumn col0 = getColumnModel().getColumn(0);
		  col0.setPreferredWidth(100);
		  TableColumn col1 = getColumnModel().getColumn(1);
		  col1.setPreferredWidth(200);
	  }
	  public void actionPerformed(ActionEvent e)
	  {
		  IMainFrame frame = getMainFrame();
		  String command = e.getActionCommand();
		  if(command.equals("SELECT_CMD"))
		  {
			  processMouseEvent(new MouseEvent(this, 500, System.currentTimeMillis(), 0, 0, 0, 1, false));
		  } else
		  {
			  if(command.equals("UP_CMD"))
			  {
				  int index = getSelectedRow();
				  if(index > 0)
					  setRowSelectionInterval(index - 1, index - 1);
				  else
					  if(getModel() instanceof ScrollableTableModel)
					  {
						  ScrollableTableModel model = (ScrollableTableModel)getModel();
						  if(model.getCurrentPageNumber() > 0)
						  {
							  ScrollProcessor parent = getScrollProcessor();
							  if(parent != null)
								  parent.prevPage();
						  } else
							  if(index == -1)
							  {
								  int rowCount = model.getRowCount();
								  if(rowCount > 0)
									  setRowSelectionInterval(rowCount - 1, rowCount - 1);
							  }
					  }
			  } else
				  if(command.equals("DOWN_CMD"))
				  {
					  int index = getSelectedRow();
					  if(index < getRowCount() - 1)
						  setRowSelectionInterval(index + 1, index + 1);
					  else
						  if(getModel() instanceof ScrollableTableModel)
						  {
							  ScrollableTableModel model = (ScrollableTableModel)getModel();
							  if(model.getCurrentPageNumber() < model.getPageCount() - 1)
							  {
								  ScrollProcessor parent = getScrollProcessor();
								  if(parent != null)
									  parent.nextPage();
							  }
						  }
				  } /*else
					  if(command.equals("RIGHT_CMD"))
					  {
						  int index = getSelectedColumn();
						  if(index < getColumnCount() - 1)
						  {
							  setColumnSelectionInterval(index + 1, index + 1);
						  } else
						  {
							  ScrollProcessor parent = getScrollProcessor();
							  if(parent != null)
								  parent.prevPage();
						  }
					  } else
						  if(command.equals("LEFT_CMD"))
						  {
							  int index = getSelectedColumn();
							  if(index > 0)
							  {
								  setColumnSelectionInterval(index - 1, index - 1);
							  } else
							  {
								  ScrollProcessor parent = getScrollProcessor();
								  if(parent != null)
									  parent.nextPage();
							  }
						  } else
							  if(command.equals("START_EDIT_CMD"))
							  {
								  if(frame != null)
									  frame.selectF2();
							  } */else
								  if(command.equals("PAGEUP_CMD"))
								  {
									  ScrollProcessor parent = getScrollProcessor();
									  if(parent != null)
										  parent.prevPage();
								  } else
									  if(command.equals("PAGEDOWN_CMD"))
									  {
										  ScrollProcessor parent = getScrollProcessor();
										  if(parent != null)
											  parent.nextPage();
									  }
			  scrollRectToVisible(getCellRect(getSelectedRow(), getSelectedColumn(), true));
			  // commented to return focus to parent frame's edit area -- tim
//			  if(frame != null)
//				  frame.getGlobalBar().setTextAreaFocus();
		  }
	  }
  }
}

