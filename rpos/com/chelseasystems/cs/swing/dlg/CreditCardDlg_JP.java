/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 */

package  com.chelseasystems.cs.swing.dlg;

import  java.awt.*;
import  javax.swing.*;
import  javax.swing.table.*;

import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.swing.model.CreditCardModel_JP;
import com.chelseasystems.cr.swing.IMainFrame;
import  com.chelseasystems.cr.swing.ScrollProcessor;
import com.chelseasystems.cr.swing.ScrollableTableModel;

import  java.awt.event.*;

/**
 * <p>Title:CreditCardDlg_JP.java </p>
 *
 * <p>Description: Display four credit card names in one row </p>
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
 | 1    | 03-06-2006 | Sandhya   | PCR1327	 | Display four credit card names in one row          | 
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

public class CreditCardDlg_JP extends JDialog implements ScrollProcessor {
	
   static final long serialVersionUID = 0;
	
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
   CreditCardModel_JP model = null;
   CreditCardTable_JP tblItem = new CreditCardTable_JP(model, JCMSTable.SELECT_CELL);
   DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
   private boolean ok = false;
   private GenericChooserRow selectedRow;
   JPanel jPanel6 = new JPanel();
   BorderLayout borderLayout3 = new BorderLayout();
   JCMSToolTip ttip;
   private String[] columnTitles = null;
   private boolean bCellSelectionEnabled = false;
   int nSelectedRow = -1;
   int nSelectedColumn = -1;
   
   java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

   protected CreditCardDlg_JP () {}

   /**
    * @param     Frame frame
    * @param     IApplicationManager theAppMgr
    * @param     GenericChooserRow[] chooserRows
    * @param     String[] columnTitles
    */
   public CreditCardDlg_JP (Frame frame, IApplicationManager theAppMgr,
         GenericChooserRow[] chooserRows, String[] columnTitles) {
      super(frame, "", true);
      try {
         this.theAppMgr = theAppMgr;
         this.setTitle(res.getString("Highlight your selection and press OK"));
         ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
         ttip.setType(JCMSToolTip.FOLLOWING);
         jbInit(null);
         pack();
         double r = com.chelseasystems.cr.swing.CMSApplet.r;
         setSize((int)(r*800), (int)(r*420));
         setResizable(false);
         model = new CreditCardModel_JP(columnTitles);
         tblItem.setModel(model);
         tblItem.setAppMgr(theAppMgr);
         loadItems(chooserRows);
         if(model.getTotalRowCount() > 0)
        	 tblItem.selectCell(0,0);
         this.setModal(true);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
   
   /**
    * @param frame
    * @param theAppMgr
    * @param chooserRows
    * @param columnTitles
    * @param iNumRowsShownPerPage
    */
   public CreditCardDlg_JP (Frame frame, IApplicationManager theAppMgr,
         GenericChooserRow[] chooserRows, String[] columnTitles, int iNumRowsShownPerPage) {
      super(frame, "", true);
      try {
         this.theAppMgr = theAppMgr;
         this.columnTitles = columnTitles;
         this.setTitle(res.getString("Highlight your selection and press OK"));
         ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
         ttip.setType(JCMSToolTip.FOLLOWING);         
         jbInit(columnTitles[0]);
         pack();
         double r = com.chelseasystems.cr.swing.CMSApplet.r;
         double dHeight = (80* iNumRowsShownPerPage);
         setSize((int)(r*800), (int)(r*dHeight));
         setResizable(false);
         model = new CreditCardModel_JP(4);
         bCellSelectionEnabled = true;
         tblItem.setModel(model);         
         tblItem.setAppMgr(theAppMgr);
         loadItems(chooserRows);
         if(model.getTotalRowCount() > 0)
        	 tblItem.selectCell(0,0);         
         this.setModal(true);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   /**
    * @param tblHeader
    * @throws Exception
    */
   void jbInit (String tblHeader) throws Exception {
      panel1.setLayout(borderLayout1);
      jPanel6.setLayout(borderLayout3);
      jPanel1.setLayout(new GridLayout(1, 4, 0, 0));
      jPanel2.setLayout(borderLayout2);
      
      btnScrollUp = theAppMgr.getTheme().getDefaultBtn();
      btnScrollUp.setText(res.getString("Page Up"));
      btnScrollUp.setMnemonic(res.getString("Mnemonic_PageUp").charAt(0));
      btnScrollUp.setDefaultCapable(false);
      btnScrollUp.addActionListener(new java.awt.event.ActionListener() {

         /**
          *
          * @param e
          */
         public void actionPerformed (ActionEvent e) {
            btnScrollUp_actionPerformed(e);
         }
      });
      
      btnScrollDown = theAppMgr.getTheme().getDefaultBtn();
      btnScrollDown.setText(res.getString("Page Down"));
      btnScrollDown.setMnemonic(res.getString("Mnemonic_PageDown").charAt(0));
      btnScrollDown.setDefaultCapable(false);
      btnScrollDown.addActionListener(new java.awt.event.ActionListener() {

         /**
          *
          * @param e
          */
         public void actionPerformed (ActionEvent e) {        	 
            btnScrollDown_actionPerformed(e);
         }
      });
      
      btnOK = theAppMgr.getTheme().getDefaultBtn();
      btnOK.setText(res.getString("OK"));
      btnOK.setMnemonic(res.getString("Mnemonic_OK").charAt(0));
      btnOK.addActionListener(new java.awt.event.ActionListener() {

         /**
          *
          * @param e
          */
         public void actionPerformed (ActionEvent e) {
            btnOK_actionPerformed(e);
         }
      });
      
      btnCancel = theAppMgr.getTheme().getDefaultBtn();
      btnCancel.setText(res.getString("Cancel"));
      btnCancel.setMnemonic(res.getString("Mnemonic_Cancel").charAt(0));
      btnCancel.addActionListener(new java.awt.event.ActionListener() {

         /**
          *
          * @param e
          */
         public void actionPerformed (ActionEvent e) {
            btnCancel_actionPerformed(e);
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
      if (tblHeader == null)
    	  jPanel6.add(tblItem.getTableHeader(), BorderLayout.NORTH);
      else
    	  jPanel6.add(new JLabel(tblHeader), BorderLayout.NORTH);
      jPanel6.add(tblItem, BorderLayout.CENTER);
      jPanel1.setBackground(theAppMgr.getBackgroundColor());
      jPanel2.setBackground(theAppMgr.getBackgroundColor());
      jPanel3.setBackground(theAppMgr.getBackgroundColor());
      jPanel4.setBackground(theAppMgr.getBackgroundColor());
      jPanel5.setBackground(theAppMgr.getBackgroundColor());
      jPanel6.setBackground(theAppMgr.getBackgroundColor());
      
      KeyListener keyListener = new KeyListener() {
         public void keyReleased (KeyEvent e) {}
         public void keyPressed (KeyEvent e) {}
         public void keyTyped (KeyEvent e) {        	 
            if(e.isConsumed())
                   return;
            e.consume();
            char ch = e.getKeyChar();
            if (ch == '\n') { //KeyEvent.VK_ENTER
               btnOK.doClick();
               e.consume();
            }
            else if (ch == 0x1B) { //KeyEvent.VK_ESCAPE
               btnCancel.doClick();
               e.consume();
            }  else  {            	
                switch (e.getKeyCode()) {                	
                    case KeyEvent.VK_PAGE_UP:
                    case KeyEvent.VK_U:
                        btnScrollUp.doClick();
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                    case KeyEvent.VK_D:
                        btnScrollDown.doClick();
                        break;
                    case KeyEvent.VK_O:
                        btnOK.doClick();
                        break;
                    case KeyEvent.VK_C:
                        btnCancel.doClick();
                        break;
                }
             }
         }
      };
      this.addKeyListener(keyListener);
      ttip.addKeyListener(keyListener);     
      
      tblItem.addMouseListener(new MouseAdapter() {

          /**
           * put your documentation comment here
           * @param e
           */
          public void mousePressed(MouseEvent e) {
          	tblItem_mousePressed(e);
          }

          /**
           * put your documentation comment here
           * @param e
           */
          public void mouseReleased(MouseEvent e) {
          	tblItem_mouseReleased(e);
          }

          /**
           * put your documentation comment here
           * @param e
           */
          public void mouseClicked(MouseEvent e) {
          	tblItem_mouseClicked(e);
          }
      });         
      //Fix for 1479 - [Credit Card Plans Dialog] Cannot use Page Down Button	
      if (tblHeader == null) {
    	  tblItem.addComponentListener(new ComponentAdapter() {
     
    		  /**
    		   * put your documentation comment here
    		   * @param e
    		   */
    		  public void componentResized(ComponentEvent e) {
    			  model.setRowsShown((tblItem.getHeight() / tblItem.getRowHeight()) * 1);
    		  }
    	  });
      } else {
      tblItem.addComponentListener(new ComponentAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void componentResized(ComponentEvent e) {
          model.setRowsShown((tblItem.getHeight() / tblItem.getRowHeight()) * 4);        
        }
      });      
      }
      tblItem.registerKeyboardAction(tblItem, JCMSTable.SELECT_CMD, 
    		  KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JTable.WHEN_FOCUSED);
   } 

   // this event is called by the JCMSTable when the user arrows up beyond the displayed rows
   public void prevPage () {
	   btnScrollUp.doClick();
	   tblItem.selectCell(0, 0);
   }
   
    // this event is called by the JCMSTable when the user arrows dn beyond the displayed rows
   public void nextPage () {
	   btnScrollDown.doClick();
	   tblItem.selectCell(0, 0);
   }

   // return model page number
   public int getCurrentPageNumber () {
      return  model.getCurrentPageNumber();
   }

   // return total page number
   public int getPageCount () {
      return  model.getPageCount();
   }
   
   /**
    * @param e
    */
   void btnScrollUp_actionPerformed (ActionEvent e) {
      model.prevPage();

      if(model.getRowCount() > 0) {
          tblItem.selectCell(0, 0);
      }

      fireButtonClick(e);
	  
   }

   /**
    * @param e
    */
   void btnScrollDown_actionPerformed (ActionEvent e) {
      model.nextPage();

      if(model.getRowCount() > 0) {
          tblItem.selectCell(0, 0);
      }

      fireButtonClick(e);
   }

   /**
    * @param e
    */
   private void fireButtonClick (ActionEvent e) {
      MouseEvent me = new MouseEvent((JComponent)e.getSource(), e.getID(),
            System.currentTimeMillis(), e.getModifiers(), -40, -5, 1, false);
      ttip.setText(res.getString("Page") + ": " + (model.getCurrentPageNumber() + 1));
      ttip.show(me);
   }

   /**
    * @param chooserRows
    */
   public void loadItems (GenericChooserRow[] chooserRows) {
      if (chooserRows == null)
         return;     
      for (int x = 0; x < chooserRows.length; x++) {
    	  model.addRow(chooserRows[x]);    	  
      }      
      tblItem.selectCell(0, 0);
      tblItem.setCellSelectionEnabled(true);
      tblItem.repaint();
      tblItem.requestFocus(); 
      // this is necessary for registered kbrd enter action listener to activate in JCMSTable
      // if enter is pressed before moving within cells (can't leave focus on cell).   djr
   }

   /**
    * @param visible
    */
   public void setVisible (boolean visible) {
      if (visible) {
         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
         setLocation((d.width - getSize().width)/2, (d.height - getSize().height)/2);
         model.firstPage();
         SwingUtilities.invokeLater(new Runnable() {

            /**
             *
             */
            public void run () {
               model.setRowsShown(tblItem.getHeight()/tblItem.getRowHeight());
			/*bug-28360-start-28-09-2017 - to show card details 4 in a row.*/
               tblItem.repaint();
            /*bug-28360-end-28-09-2017 - to show card details 4 in a row.*/
            }
         });
      } else {
    	//TD
    	//ttip.hide();
    	ttip.setVisible(false);
      }
      super.setVisible(visible);
   }

   /**
    * @param visible
    * @param defaultRowIndex
    */
   public void setVisible (boolean visible, int inDefaultRowIndex) {
      final int defaultRowIndex = inDefaultRowIndex;
      if (visible && defaultRowIndex > -1)
         SwingUtilities.invokeLater(new Runnable() {

            /**
             *
             */
            public void run () {
               int relativeIndex = defaultRowIndex;
               model.setRowsShown(tblItem.getHeight()/tblItem.getRowHeight());
               while (relativeIndex >= model.getRowsShown()) {
                  relativeIndex -= model.getRowsShown();
                  nextPage();
               }
               tblItem.selectCell(relativeIndex, 0);
               tblItem.repaint();
            }
         });
      setVisible(visible);
   }

   /**
    * @param e
    */
   void tblItem_mousePressed (MouseEvent e) {
	   handleMouseSelection(e);
   }

   /**
    * @param e
    */
   void tblItem_mouseReleased(MouseEvent e) {
	   handleMouseSelection(e);
   }

   /**
    * @param e
    */
   void tblItem_mouseClicked (MouseEvent e) {
	   handleMouseSelection(e);
   }
   
   private void handleMouseSelection(MouseEvent e) {
		   int rowIndexStart = tblItem.getSelectedRow();
  		   int rowIndexEnd = tblItem.getSelectionModel().getMaxSelectionIndex();
  		   int colIndexStart = tblItem.getSelectedColumn();
  		   int colIndexEnd = tblItem.getColumnModel().getSelectionModel().getMaxSelectionIndex();
  		   
  		   int r = -1;
  		   int c = -1;
  		   for (int row = rowIndexStart; row <= rowIndexEnd; row++) {
  			   for (int col = colIndexStart; col <= colIndexEnd; col++) {
  				   if (tblItem.isCellSelected(row, col)) {
  					   r = row;
  					   c = col;
  				   }
  		   		}
  		   }

  		   if (r > -1 && c > -1) {
			   // If the new selection is for the row that is blank, do nothing  			    			   
			   if (r * model.getColumnCount() + c >= model.getCurrentPage().size()) {
				   tblItem.selectCell(nSelectedRow, nSelectedColumn);
				   e.consume();
				   if(e.getClickCount() == 2){
					   CreditCardDlg_JP.this.btnOK.doClick();
				   }
			   } else {
				   tblItem.selectCell(r, c);	
				   if(e.getClickCount() == 2){
					   CreditCardDlg_JP.this.btnOK.doClick();					   
				   }
			   }
  		   }
   }

   /**
    * @param e
    */
   void btnCancel_actionPerformed (ActionEvent e) {
      ok = false;
      dispose();
   }

   /**
    * @param e
    */
   void btnOK_actionPerformed (ActionEvent e) {
	   //If Cell selection is Enabled, the selected values have to be fetched differently
	   if (bCellSelectionEnabled) {
		   int rowIndexStart = tblItem.getSelectedRow();
		   int rowIndexEnd = tblItem.getSelectionModel().getMaxSelectionIndex();
		   int colIndexStart = tblItem.getSelectedColumn();
		   int colIndexEnd = tblItem.getColumnModel().getSelectionModel().getMaxSelectionIndex();
		   
		   for (int row = rowIndexStart; row <= rowIndexEnd; row++) {
			   for (int col = colIndexStart; col <= colIndexEnd; col++) {
				   if (tblItem.isCellSelected(row, col)) {					   
					   selectedRow = (GenericChooserRow) model.getObjectAt(row, col);
				   }
		   		}
		   	}  	   
	  } else {
		  int row = tblItem.getSelectedRow();      
		  if (row < 0) {
			  theAppMgr.showErrorDlg(res.getString("No row is selected."));
			  return;
		  }
		  selectedRow = model.getRow(row);
	  }
	  if (selectedRow != null) {
		  ok = true;
	  }
      dispose();
   }

   /**
    * @return
    */
   public boolean isOK () {
      return  ok;
   }

   /**
    *
    * @return
    */
   public GenericChooserRow getSelectedRow () {
      return  selectedRow;
   }

   /**
    * @return
    */
   public JCMSTable getTable () {
      return  this.tblItem;
   }

   /**
    * @return
    */
   public static DefaultTableCellRenderer getCenterRenderer () {
      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
      return  centerRenderer;
   }

   /**
    * @return
    */
   public static DefaultTableCellRenderer getRightRenderer () {
      DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
      rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
      return  rightRenderer;
   }
   
   /**
    * Extended this class to prevent Eu from selecting empty cells
    */
   private class CreditCardTable_JP extends JCMSTable {

     /**
      * put your documentation comment here
      * @param         ScrollableTableModel model
      * @param         int type
      */
     public CreditCardTable_JP(ScrollableTableModel model, int type) {
       super(model, type);
     }


     /**
      * put your documentation comment here
      * @param e
      */
     public void actionPerformed(ActionEvent e) {
    	 IMainFrame frame = getMainFrame();
         String command = e.getActionCommand();         
         if(command.equals("SELECT_CMD")) {
        	 CreditCardDlg_JP.this.btnOK.doClick();
//             processMouseEvent(new MouseEvent(this, 500, System.currentTimeMillis(), 0, 0, 0, 1, false));
         } else {
        	 if(command.equals("UP_CMD") || command.equals("DOWN_CMD") || 
        		command.equals("RIGHT_CMD") || command.equals("LEFT_CMD")) {
        		 handleKeySelection(command);
 			} else if(command.equals("START_EDIT_CMD")) {
                if(frame != null)
                	frame.selectF2();
             } else if(command.equals("PAGEUP_CMD")) {
            	 ScrollProcessor parent = getScrollProcessor();
                 if(parent != null)
                     parent.prevPage();
             } else if(command.equals("PAGEDOWN_CMD")) {
                 ScrollProcessor parent = getScrollProcessor();
                 if(parent != null)
                     parent.nextPage();
             }
             scrollRectToVisible(getCellRect(super.getSelectedRow(), getSelectedColumn(), true));
//             if(frame != null)
//                 frame.getGlobalBar().setTextAreaFocus();
         }
     }

     public void handleKeySelection(String command) {    	     	 
    	 if (bCellSelectionEnabled) {
    		int rowIndexStart = super.getSelectedRow();
  		   	int rowIndexEnd = super.getSelectionModel().getMaxSelectionIndex();
  		   	int colIndexStart = super.getSelectedColumn();
  		    int colIndexEnd = super.getColumnModel().getSelectionModel().getMaxSelectionIndex();  		   
  		    
  		    int r = -1;
  		    int c = -1;
  		    for (int row = rowIndexStart; row <= rowIndexEnd; row++) {
  		    	for (int col = colIndexStart; col <= colIndexEnd; col++) {
  		    		if (isCellSelected(row, col)) {
  					   r = row;
  					   c = col;
  				   }
  		   		}
  		    }  		    
  		    
  		    boolean bMakeSelection = true;
  		    if (r > -1 && c > -1) {
  		    	if (command.equals("LEFT_CMD")) {
  		    		//System.out.println("@@@ KEYPAD LEFT PRESSED");
  		    		if (r > 0 && c > 0) 			c--;
  		    		else if (r > 0 && c == 0) {	r--; c = model.getColumnCount()-1; }
  		    		else if (r == 0 && c > 0)	c--;
  		    		else if (r == 0 && c == 0) {  		    			
  		    			if (model.getCurrentPageNumber() > 0) {  		    			
  		    				model.prevPage(); 
  		    				r = model.getRowCount() - 1;
  		    				c = model.getColumnCount() - 1;
  		    			}
  		    		}
  		    		else {
  		    			prevPage();
  		    			bMakeSelection = false;
  		    		}
  		    	}  else if (command.equals("RIGHT_CMD")) {
  		    		//System.out.println("@@@ KEYPAD RIGHT PRESSED");
  		    		if (r < model.getRowCount()-1 && c < model.getColumnCount()-1) 			c++;
  		    		else if (r < model.getRowCount()-1 && c == model.getColumnCount()-1) {	r++; c = 0; }
  		    		else if (r == model.getRowCount()-1 && c < model.getColumnCount()-1)		c++;
  		    		else {
  		    			nextPage();
  		    			bMakeSelection = false;
  		    		}
  		    	} else if (command.equals("UP_CMD")) {
  		    		if (r > 0)
  		    			r--;
  		    		else {
  					   prevPage();
  					   bMakeSelection = false;
  		    		}
  		    	} else if (command.equals("DOWN_CMD")) {
  		    		if (r < model.getRowCount()-1)
  		    			r++;
  		    		else {
  		    			nextPage();
  		    			bMakeSelection = false;
  		    		}
  		    	}
  			   
  		    	// If the new selection is for the row that is blank, do nothing
  		    	if (r * model.getColumnCount() + c >= model.getCurrentPage().size())
  		    		bMakeSelection = false; 			   
  		    	 
  		    	// Select the cell at (r, c)
  		    	if (bMakeSelection)
  		    		selectCell(r, c);
  		    }
  	   	} else {
  	   		if(command.equals("UP_CMD"))
  	   		{
  	   			int index = super.getSelectedRow();
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
  	   				int index = super.getSelectedRow();
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
  	   			}
  	   	}
     }
     
     public void selectCell(int row, int column) {    	 	
    	setRowSelectionInterval(row, row);
    	setColumnSelectionInterval(column, column);
    	// Save the selected row and column
    	nSelectedRow = row;
    	nSelectedColumn = column;
     }
   }
}