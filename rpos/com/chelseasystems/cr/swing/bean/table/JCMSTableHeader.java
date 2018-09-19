package com.chelseasystems.cr.swing.bean.table;

import com.chelseasystems.cr.swing.layout.VerticalFlowLayout;
import java.awt.*;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;

// Referenced classes of package com.chelseasystems.cr.swing.bean.table:
//            JCMSHeaderRow

public class JCMSTableHeader extends JTableHeader
{
    class CMSTableHeaderRenderer extends JPanel
        implements TableCellRenderer
    {

        private JLabel lbl;
        final JCMSTableHeader this$0;

        public void setFont(Font headerFont)
        {
            super.setFont(headerFont);
            if(lbl != null)
            {
                lbl.setFont(headerFont);
            }
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            if(value != null)
            {
                lbl.setText(value.toString());
            } else
            {
                lbl.setText("");
            }
            return this;
        }

        public CMSTableHeaderRenderer()
        {
        	super();
            this$0 = JCMSTableHeader.this;
           // super();
            lbl = new JLabel();
            setLayout(new BorderLayout());
            add(lbl, "Center");
            lbl.setHorizontalAlignment(0);
            setBorder(BorderFactory.createBevelBorder(0, new Color(235, 235, 235), new Color(235, 235, 235), new Color(134, 134, 134), Color.darkGray));
            setFont(getHeaderFont());
        }
    }


    public static final int HEIGHT = 21;
    public static final String MERGE_RIGHT = "merge_right";
    private Vector headerRows;
    private JPanel pnlColumnHeaders;
    private Vector headerComponents;
    private CMSTableHeaderRenderer renderer;

    public JCMSTableHeader(TableColumnModel columnModel)
    {
        super(columnModel);
        headerRows = new Vector();
        pnlColumnHeaders = new JPanel();
        headerComponents = new Vector();
        renderer = null;
        jbInit();
    }

    private void jbInit()
    {
        setPreferredSize(new Dimension(800, 21));
        setLayout(new VerticalFlowLayout(3, 0, 0, 0, true, true));
        setReorderingAllowed(false);
        setResizingAllowed(false);
        setRequestFocusEnabled(false);
        setFont(new Font("Helvetica", 1, 12));
        pnlColumnHeaders.setPreferredSize(getPreferredSize());
        pnlColumnHeaders.setOpaque(false);
        add(pnlColumnHeaders);
        renderer = new CMSTableHeaderRenderer();
        setDefaultRenderer(renderer);
    }

    private void printComponents(Component comps[])
    {
        for(int i = 0; i < comps.length; i++)
        {
            if((comps[i] instanceof Container) || (comps[i] instanceof JPanel))
            {
                printComponents(((Container)comps[i]).getComponents());
            } else
            {
                System.out.println((new StringBuilder()).append("--->JCMSTableHeader:: Componments >>>: ").append(comps[i].getClass().getName()).toString());
            }
            if(comps[i] instanceof JLabel)
            {
                System.out.println((new StringBuilder()).append("--->JCMSTableHeader:: Componments: ").append(comps[i].getClass().getName()).append(" Text: ->").append(((JLabel)comps[i]).getText()).append("<").toString());
            } else
            {
                System.out.println((new StringBuilder()).append("--->JCMSTableHeader:: Componments: ").append(comps[i].getClass().getName()).toString());
            }
        }

    }

    public JPanel getColumnHeaderPanel()
    {
        return pnlColumnHeaders;
    }

    public void addHeaderRow(String headerLabels[])
        throws Exception
    {
        if(headerLabels.length != getColumnModel().getColumnCount())
        {
            throw new Exception("Array length not equal to number of columns.");
        } else
        {
            JCMSHeaderRow pnlHeaderRow = new JCMSHeaderRow(getColumnModel(), headerLabels);
            setPreferredSize(new Dimension(getWidth(), getPreferredSize().height + 21));
            headerRows.add(pnlHeaderRow);
            add(pnlHeaderRow);
            return;
        }
    }

    public void setBackground(Color bg)
    {
        super.setBackground(bg);
        if(headerRows != null)
        {
            for(Enumeration enm = headerRows.elements(); enm.hasMoreElements(); ((JPanel)enm.nextElement()).setBackground(bg)) { }
        }
        if(renderer != null)
        {
            renderer.setBackground(bg);
        }
    }

    public int getHeaderRowCount()
    {
        if(headerRows != null)
        {
            return headerRows.size();
        } else
        {
            return 0;
        }
    }
	//commented by shushma as it was throwing error
    
	/*public JCMSHeaderRow getHeaderRow(int index)
    {
        if(headerRows == null)
        {
            break MISSING_BLOCK_LABEL_24;
        }
        return (JCMSHeaderRow)headerRows.elementAt(index);
        Exception e;
        e;
        e.printStackTrace();
        return null;
    }

    public void setHeaderPreferredHeight(int index, int h)
        throws Exception
    {
        int diff = h - getHeaderRow(index).getPreferredHeight();
        setPreferredSize(new Dimension(getWidth(), getPreferredSize().height + diff));
        getHeaderRow(index).setPreferredHeight(h);
    }
*/
    public Rectangle getHeaderRect(int columnIndex)
    {
		//changes made by shushma due to upgradation of jdk1.5 to jdk1.6, the columnIndex value is changed
	  //  if(columnIndex < 0 || columnIndex >= columnModel.getColumnCount())

        if(columnIndex < -1 || columnIndex >= columnModel.getColumnCount())
        {
            throw new IllegalArgumentException("Column index out of range");
        }
        int rectX = 0;
        int column = 0;
        int columnMargin = getColumnModel().getColumnMargin();
        Enumeration enumeration = getColumnModel().getColumns();
        do
        {
            if(!enumeration.hasMoreElements())
            {
                break;
            }
            TableColumn aColumn = (TableColumn)enumeration.nextElement();
            if(pnlColumnHeaders == null)
            {
                break;
            }
            if(column == columnIndex)
            {
                return new Rectangle(rectX, pnlColumnHeaders.getY(), aColumn.getWidth() + columnMargin, pnlColumnHeaders.getHeight());
            }
            rectX += aColumn.getWidth() + columnMargin;
            column++;
        } while(true);
        return new Rectangle();
    }

    public void setFont(Font headerFont)
    {
        super.setFont(headerFont);
        if(renderer != null)
        {
            renderer.setFont(headerFont);
        }
    }

    public Font getHeaderFont()
    {
        return getFont();
    }
}
