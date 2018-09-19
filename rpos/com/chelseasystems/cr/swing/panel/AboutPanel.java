/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.chelseasystems.cr.swing.panel;

import com.chelseasystems.cr.swing.layout.VerticalFlowLayout;
import com.chelseasystems.cr.util.ResourceManager;
import java.awt.*;
import java.util.ResourceBundle;
import javax.swing.*;

public class AboutPanel extends JPanel
{

    public AboutPanel()
    {
        super(new BorderLayout());
        res = ResourceManager.getResourceBundle();
        product = "";
        version = "";
        copyright = "";
        trademark = "";
        comments = "";
        warning = "";
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit()
        throws Exception
    {
        Font font = new Font("Helvetica", 0, 12);
        Font bold = new Font("Helvetica", 1, 12);
        txtProduct = constructTextArea((new StringBuilder()).append(getProduct()).append(" ").append(getVersion()).toString(), null, bold, 525, 25);
        txtCopyright = constructTextArea(getCopyright(), null, font, 525, 17);
        txtTrademark = constructTextArea(getTrademark(), null, font, 525, 15);
        txtComments = constructTextArea(getComments(), Color.white, font, 0, 0);
        txtWarning = constructTextArea(getWarning(), null, font, 525, 90);
        scroller = new JScrollPane(txtComments);
        scroller.getViewport().setPreferredSize(new Dimension(525, 90));
        JPanel main = getSpacerPanel();
        main.setLayout(new VerticalFlowLayout(8, true, false));
        main.add(txtProduct);
        main.add(txtCopyright);
        main.add(txtTrademark);
        main.add(scroller);
        main.add(txtWarning);
        add(getSpacerPanel(), "North");
        add(getSpacerPanel(), "South");
        add(getSpacerPanel(), "East");
        add(getSpacerPanel(), "West");
        add(main, "Center");
    }

    private JTextArea constructTextArea(String text, Color color, Font font, int width, int height)
    {
        JTextArea rtnVal = new JTextArea(text);
        rtnVal.setBackground(color);
        rtnVal.setEditable(false);
        rtnVal.setFont(font);
        rtnVal.setForeground(Color.black);
        rtnVal.setLineWrap(true);
        if(width != 0 && height != 0)
            rtnVal.setPreferredSize(new Dimension(width, height));
        rtnVal.setWrapStyleWord(true);
        return rtnVal;
    }

    private JPanel getSpacerPanel()
    {
        JPanel rtnVal = new JPanel();
        rtnVal.setBackground(null);
        return rtnVal;
    }

    private void refreshAll()
    {
        txtProduct.setText((new StringBuilder()).append(getProduct()).append(" ").append(getVersion()).toString());
        txtProduct.repaint();
        txtCopyright.setText(getCopyright());
        txtCopyright.repaint();
        txtTrademark.setText(getTrademark());
        txtTrademark.repaint();
        txtComments.setText(getComments());
        txtComments.repaint();
        txtWarning.setText(getWarning());
        txtWarning.repaint();
    }

    public void setProduct(String productname)
    {
        product = productname;
        refreshAll();
    }

    public void setVersion(String version)
    {
        this.version = version;
        refreshAll();
    }

    public void setCopyright(String copyright)
    {
        this.copyright = copyright;
        refreshAll();
    }

    public void setTrademark(String trademark)
    {
        this.trademark = trademark;
        refreshAll();
    }

    public void setComments(String comments)
    {
        this.comments = comments;
        refreshAll();
    }

    public void setWarning(String warning)
    {
        this.warning = warning;
        refreshAll();
    }

    public String getProduct()
    {
        return product;
    }

    public String getVersion()
    {
        return version;
    }

    public String getCopyright()
    {
        if(copyright != null && copyright.length() > 0)
            return copyright;
        else
            return res.getString("copyright text");
    }

    public String getTrademark()
    {
        if(trademark != null && trademark.length() > 0)
            return trademark;
        else
            return "";
    }

    public String getComments()
    {
        if(comments != null && comments.length() > 0)
            return comments;
        else
            return (new StringBuilder()).append(getProduct()).append(" ").append(res.getString("comment text")).toString();
    }

    public String getWarning()
    {
        if(warning != null && warning.length() > 0)
            return warning;
        else
            return res.getString("warning text");
    }

    private ResourceBundle res;
    private String product;
    private String version;
    private String copyright;
    private String trademark;
    private String comments;
    private String warning;
    private JTextArea txtProduct;
    private JTextArea txtCopyright;
    private JTextArea txtTrademark;
    private JTextArea txtComments;
    private JTextArea txtWarning;
    private JScrollPane scroller;
}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\clientwindows1.5\US\retek\library\retek_platform.jar
	Total time: 334 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/