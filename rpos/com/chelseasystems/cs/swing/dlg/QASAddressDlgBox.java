package com.chelseasystems.cs.swing.dlg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.ScrollProcessor;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.bean.JCMSToolTip;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.swing.model.GenericChooseFromTableModel;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+ 
| 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:QASAddressDlgBox.java </p>
*
* <p>Description: QASAddressDlgBox </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

public class QASAddressDlgBox extends JDialog implements ScrollProcessor {

    protected QASAddressDlgBox() {
        panel1 = new JPanel();
        borderLayout1 = new BorderLayout();
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        borderLayout2 = new BorderLayout();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        jPanel5 = new JPanel();
        btnScrollUp = new JButton();
        btnScrollDown = new JButton();
        btnOK = new JButton();
        btnCancel = new JButton();
        tblItem = new JCMSTable(model, 49);
        ok = false;
        jPanel6 = new JPanel();
        borderLayout3 = new BorderLayout();
        res = ResourceManager.getResourceBundle();
    }

    public QASAddressDlgBox(Frame frame, IApplicationManager theAppMgr, GenericChooserRow chooserRows[], String columnTitles[]) {
        super(frame, "", true);
        panel1 = new JPanel();
        borderLayout1 = new BorderLayout();
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        borderLayout2 = new BorderLayout();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        jPanel5 = new JPanel();
        btnScrollUp = new JButton();
        btnScrollDown = new JButton();
        btnOK = new JButton();
        btnCancel = new JButton();
        tblItem = new JCMSTable(model, 49);
        ok = false;
        jPanel6 = new JPanel();
        borderLayout3 = new BorderLayout();
        res = ResourceManager.getResourceBundle();
        try {
            this.theAppMgr = theAppMgr;
            setTitle(res.getString("Highlight your selection and press OK"));
            ttip = new JCMSToolTip(this, theAppMgr.getTheme(), "");
            ttip.setType(1);
            jbInit();
            pack();
            double r = CMSApplet.r;
            setSize((int) (r * 800D), (int) (r * 420D));
            setResizable(false);
            model = new GenericChooseFromTableModel(columnTitles);
            tblItem.setModel(model);
            tblItem.setAppMgr(theAppMgr);
            loadItems(chooserRows);
            if (model.getTotalRowCount() > 0)
                tblItem.setRowSelectionInterval(0, 0);
            setModal(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
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
        btnOK.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnOK_actionPerformed(e);
            }

        });

        btnCancel = theAppMgr.getTheme().getDefaultBtn();
        btnCancel.setText(res.getString("Cancel"));
        btnCancel.setMnemonic(res.getString("Mnemonic_Cancel").charAt(0));
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnCancel_actionPerformed(e);
            }

        });        
        
        btnScrollUp.setDefaultCapable(false);
        btnScrollUp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnScrollUp_actionPerformed(e);
            }

        });
        btnScrollDown.setDefaultCapable(false);
        btnScrollDown.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnScrollDown_actionPerformed(e);
            }

        });
        tblItem.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                tblItem_mouseClicked(e);
            }

        });
        getContentPane().add(panel1);
        panel1.add(jPanel1, "South");
        jPanel1.add(btnScrollUp, null);
        jPanel1.add(btnScrollDown, null);
        jPanel1.add(btnOK, null);
        jPanel1.add(btnCancel, null);
        
        panel1.add(jPanel2, "Center");
        jPanel2.add(jPanel3, "North");
        jPanel2.add(jPanel4, "West");
        jPanel2.add(jPanel5, "East");
        jPanel2.add(jPanel6, "Center");
        jPanel6.add(tblItem.getTableHeader(), "North");
        jPanel6.add(tblItem, "Center");
        jPanel1.setBackground(theAppMgr.getBackgroundColor());
        jPanel2.setBackground(theAppMgr.getBackgroundColor());
        jPanel3.setBackground(theAppMgr.getBackgroundColor());
        jPanel4.setBackground(theAppMgr.getBackgroundColor());
        jPanel5.setBackground(theAppMgr.getBackgroundColor());
        jPanel6.setBackground(theAppMgr.getBackgroundColor());
        KeyListener keyListener = new KeyListener() {

            public void keyReleased(KeyEvent keyevent) {
            }

            public void keyPressed(KeyEvent keyevent) {
            }

            public void keyTyped(KeyEvent e) {
                if (e.isConsumed())
                    return;
                e.consume();
                char ch = e.getKeyChar();
                if (ch == '\n') {
                    btnOK.doClick();
                    e.consume();
                } else {
                    switch (e.getKeyCode()) {
                    case 33: // '!'
                    case 85: // 'U'
                        btnScrollUp.doClick();
                        break;

                    case 34: // '"'
                    case 68: // 'D'
                        btnScrollDown.doClick();
                        break;

                    case 79: // 'O'
                        btnOK.doClick();
                        break;

                    }
                }
            }

        };
        addKeyListener(keyListener);
        ttip.addKeyListener(keyListener);
    }

    public void prevPage() {
        btnScrollUp.doClick();
    }

    public void nextPage() {
        btnScrollDown.doClick();
    }

    public int getCurrentPageNumber() {
        return model.getCurrentPageNumber();
    }

    public int getPageCount() {
        return model.getPageCount();
    }

    void btnScrollUp_actionPerformed(ActionEvent e) {
        model.prevPage();
        if (model.getRowCount() > 0)
            tblItem.setRowSelectionInterval(0, 0);
        fireButtonClick(e);
    }

    void btnScrollDown_actionPerformed(ActionEvent e) {
        model.nextPage();
        if (model.getRowCount() > 0)
            tblItem.setRowSelectionInterval(0, 0);
        fireButtonClick(e);
    }

    private void fireButtonClick(ActionEvent e) {
        MouseEvent me = new MouseEvent((JComponent) e.getSource(), e.getID(), System.currentTimeMillis(), e.getModifiers(), -40, -5, 1, false);
        ttip.setText(res.getString("Page") + ": " + (model.getCurrentPageNumber() + 1));
        ttip.show(me);
    }

    public void loadItems(GenericChooserRow chooserRows[]) {
        if (chooserRows == null)
            return;
        for (int x = 0; x < chooserRows.length; x++)
            model.addRow(chooserRows[x]);

    }

    public void setVisible(boolean visible) {
        if (visible) {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
            model.firstPage();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    model.setRowsShown(tblItem.getHeight() / tblItem.getRowHeight());
                }

            });
        } else {
            //TD
            //ttip.hide();
            ttip.setVisible(false);
        }
        super.setVisible(visible);
    }

    public void setVisible(boolean visible, int inDefaultRowIndex) {
        final int defaultRowIndex = inDefaultRowIndex;
        if (visible && defaultRowIndex > -1)
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    int relativeIndex = defaultRowIndex;
                    model.setRowsShown(tblItem.getHeight() / tblItem.getRowHeight());
                    for (; relativeIndex >= model.getRowsShown(); nextPage())
                        relativeIndex -= model.getRowsShown();

                    tblItem.setRowSelectionInterval(relativeIndex, relativeIndex);
                    tblItem.repaint();
                }

            });
        setVisible(visible);
    }

    void tblItem_mouseClicked(MouseEvent e) {
        btnOK_actionPerformed(null);
    }

    void btnOK_actionPerformed(ActionEvent e) {
        int row = tblItem.getSelectedRow();
        if (row < 0) {
            theAppMgr.showErrorDlg(res.getString("No row is selected."));
            return;
        } else {
            selectedRow = model.getRow(row);
            ok = true;
            dispose();
            return;
        }
    }

    void btnCancel_actionPerformed(ActionEvent e) {
        ok = false;
        dispose();
        return;
    }
    
    public boolean isOK() {
        return ok;
    }

    public GenericChooserRow getSelectedRow() {
        return selectedRow;
    }

    public JCMSTable getTable() {
        return tblItem;
    }

    public static DefaultTableCellRenderer getCenterRenderer() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(0);
        return centerRenderer;
    }

    public static DefaultTableCellRenderer getRightRenderer() {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(4);
        return rightRenderer;
    }

    JPanel panel1 = null;

    BorderLayout borderLayout1 = null;

    JPanel jPanel1 = null;

    JPanel jPanel2 = null;

    BorderLayout borderLayout2 = null;

    JPanel jPanel3 = null;

    JPanel jPanel4 = null;

    JPanel jPanel5 = null;

    JButton btnScrollUp = null;

    JButton btnScrollDown = null;

    JButton btnOK = null;
    
    JButton btnCancel = null;

    protected IApplicationManager theAppMgr = null;

    protected GenericChooseFromTableModel model = null;

    JCMSTable tblItem = null;

    private boolean ok = false;

    private GenericChooserRow selectedRow = null;

    JPanel jPanel6 = null;

    BorderLayout borderLayout3 = null;

    JCMSToolTip ttip = null;

    ResourceBundle res = null;
}