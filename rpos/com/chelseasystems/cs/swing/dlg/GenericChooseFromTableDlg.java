/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 */


package  com.chelseasystems.cs.swing.dlg;

import  java.awt.*;
import  javax.swing.*;
import  javax.swing.table.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.item.*;
import  com.chelseasystems.cr.swing.bean.*;
import  com.chelseasystems.cs.swing.model.GenericChooseFromTableModel;
import  com.chelseasystems.cr.swing.ScrollProcessor;
import  java.awt.event.*;


/*
 * @author  Dan Reading
 *
 *  You pass this option dialog an Array of Arrays of Objects.  It will display the .toStrings
 *  in a table and allow the user to select a row which you can then obtain from the
 *  getSelectedRow method.
 */
public class GenericChooseFromTableDlg extends JDialog
      implements ScrollProcessor {
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
   protected GenericChooseFromTableModel model;
   JCMSTable tblItem = new JCMSTable(model, JCMSTable.SELECT_ROW);
   //JScrollPane pane = new JScrollPane(tblItem);
   private boolean ok = false;
   private GenericChooserRow selectedRow;
   JPanel jPanel6 = new JPanel();
   BorderLayout borderLayout3 = new BorderLayout();
   JCMSToolTip ttip;

   java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

   /**
    *
    */
   protected GenericChooseFromTableDlg () {
   }

   /**
    *
    * @param     Frame frame
    * @param     IApplicationManager theAppMgr
    * @param     GenericChooserRow[] chooserRows
    * @param     String[] columnTitles
    */
   public GenericChooseFromTableDlg (Frame frame, IApplicationManager theAppMgr,
         GenericChooserRow[] chooserRows, String[] columnTitles) {
      super(frame, "", true);
      try {
         this.theAppMgr = theAppMgr;
         this.setTitle(res.getString("Highlight your selection and press OK"));
         ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
         ttip.setType(JCMSToolTip.FOLLOWING);
         jbInit();
         pack();
         double r = com.chelseasystems.cr.swing.CMSApplet.r;
         setSize((int)(r*800), (int)(r*420));
         // CR: 3451 Make the dialog not - resizable
         setResizable(false);
         model = new GenericChooseFromTableModel(columnTitles);
		 //Vivek Mishra : Merged updated code from source provided by Sergio 19-MAY-16
         //Added by deepika to handle the rows in the Services menu issue in ARM 1.6
         model.setRowsShown(5);
		 //Ends here
         tblItem.setModel(model);
         tblItem.setAppMgr(theAppMgr);
         loadItems(chooserRows);
         if(model.getTotalRowCount() > 0)
            tblItem.setRowSelectionInterval(0,0);
         this.setModal(true);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }


   public GenericChooseFromTableDlg (Frame frame, IApplicationManager theAppMgr,
         GenericChooserRow[] chooserRows, String[] columnTitles, int iNumRowsShownPerPage) {
      super(frame, "", true);
      try {
         this.theAppMgr = theAppMgr;
         this.setTitle(res.getString("Highlight your selection and press OK"));
         ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
         ttip.setType(JCMSToolTip.FOLLOWING);
         jbInit();
         pack();
         double r = com.chelseasystems.cr.swing.CMSApplet.r;
         double dHeight = (80* iNumRowsShownPerPage);
         setSize((int)(r*800), (int)(r*dHeight));
         // CR: 3451 Make the dialog not - resizable
         setResizable(false);
         model = new GenericChooseFromTableModel(columnTitles);
         tblItem.setModel(model);
         tblItem.setAppMgr(theAppMgr);
         loadItems(chooserRows);
         if(model.getTotalRowCount() > 0)
            tblItem.setRowSelectionInterval(0,0);
         this.setModal(true);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   /**
    *
    * @exception Exception
    */
   void jbInit () throws Exception {
      panel1.setLayout(borderLayout1);
      jPanel6.setLayout(borderLayout3);
      jPanel1.setLayout(new GridLayout(1, 4, 0, 0));
      jPanel2.setLayout(borderLayout2);
      btnScrollUp = theAppMgr.getTheme().getDefaultBtn();
      btnScrollUp.setText(res.getString("Page Up"));
      btnScrollUp.setMnemonic(res.getString("Mnemonic_PageUp").charAt(0));
      btnScrollDown = theAppMgr.getTheme().getDefaultBtn();
      btnScrollDown.setText(res.getString("Page Down"));
      btnScrollDown.setMnemonic(res.getString("Mnemonic_PageDown").charAt(0));
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
      tblItem.addMouseListener(new java.awt.event.MouseAdapter() {

         /**
          *
          * @param e
          */
         public void mouseClicked (MouseEvent e) {
            tblItem_mouseClicked(e);
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
            }  else
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_PAGE_UP:case KeyEvent.VK_U:
                        btnScrollUp.doClick();
                        break;
                    case KeyEvent.VK_PAGE_DOWN:case KeyEvent.VK_D:
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
      };
      this.addKeyListener(keyListener);
      ttip.addKeyListener(keyListener);
   }

   // this event is called by the JCMSTable when the user arrows up beyond the displayed rows
   public void prevPage () {
      btnScrollUp.doClick();
   }

   // this event is called by the JCMSTable when the user arrows dn beyond the displayed rows
   public void nextPage () {
      btnScrollDown.doClick();
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
    *
    * @param e
    */
   void btnScrollUp_actionPerformed (ActionEvent e) {
      model.prevPage();

      if(model.getRowCount() > 0)
      {
          tblItem.setRowSelectionInterval(0, 0);
      }

      fireButtonClick(e);
   }

   /**
    * @param e
    */
   void btnScrollDown_actionPerformed (ActionEvent e) {
      model.nextPage();

      if(model.getRowCount() > 0)
      {
          tblItem.setRowSelectionInterval(0, 0);
      }

      fireButtonClick(e);
   }

   /**
    */
   private void fireButtonClick (ActionEvent e) {
      MouseEvent me = new MouseEvent((JComponent)e.getSource(), e.getID(),
            System.currentTimeMillis(), e.getModifiers(), -40, -5, 1, false);
      ttip.setText(res.getString("Page") + ": " + (model.getCurrentPageNumber() + 1));
      ttip.show(me);
   }

   /**
    *
    * @param chooserRows
    */
   public void loadItems (GenericChooserRow[] chooserRows) {
      if (chooserRows == null)
         return;
      for (int x = 0; x < chooserRows.length; x++) {
         model.addRow(chooserRows[x]);
      }
   }

   /**
    *
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
            	   //Edwin reported misc items are not fully displayed on page one
            	   //Fixed by Mayuri-03/10/16
            	   tblItem.repaint();
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
    *
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
               tblItem.setRowSelectionInterval(relativeIndex, relativeIndex);
               tblItem.repaint();
            }
         });
      setVisible(visible);
   }

   /**
    *
    * @param e
    */
   void tblItem_mouseClicked (MouseEvent e) {
      btnOK_actionPerformed(null);
   }

   /**
    *
    * @param e
    */
   void btnCancel_actionPerformed (ActionEvent e) {
      ok = false;
      dispose();
   }

   /**
    *
    * @param e
    */
   void btnOK_actionPerformed (ActionEvent e) {
      int row = tblItem.getSelectedRow();
      if (row < 0) {
         theAppMgr.showErrorDlg(res.getString("No row is selected."));
         return;
      }
      selectedRow = model.getRow(row);
      ok = true;
      dispose();
   }

   /**
    *
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
    *
    * @return
    */
   public JCMSTable getTable () {
      return  this.tblItem;
   }

   /**
    *
    * @return
    */
   public static DefaultTableCellRenderer getCenterRenderer () {
      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
      return  centerRenderer;
   }

   /**
    *
    * @return
    */
   public static DefaultTableCellRenderer getRightRenderer () {
      DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
      rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
      return  rightRenderer;
   }


}



