/* History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 |      | 11-21-2008 | Neeti     | PCR1941   | Stock locator                          			  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.item;

import java.awt.Color;
import java.awt.Font;
import java.awt.print.PageFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.report.Reportable;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.swing.menu.ReportsMgmtMenuApplet;
import com.ga.cs.swing.report.BaseReportApplet;
import com.klg.jclass.page.EndOfFrameException;
import com.klg.jclass.page.JCDocument;
import com.klg.jclass.page.JCFlow;
import com.klg.jclass.page.JCFrame;
import com.klg.jclass.page.JCPage;
import com.klg.jclass.page.JCPageTable;
import com.klg.jclass.page.JCPageTemplate;
import com.klg.jclass.page.JCTableStyle;
import com.klg.jclass.page.JCTextStyle;
import com.klg.jclass.page.JCUnit;
import com.klg.jclass.page.TextMacro;
import com.klg.jclass.page.awt.JCAWTPrinter;


import java.text.SimpleDateFormat;

import javax.swing.JTable;


/**
 * put your documentation comment here
 */
public class ArmaniStockLocatorPrinter{
  private static ResourceBundle res;
  private static ConfigMgr jposPeripheralsConfigMgr = new ConfigMgr("JPOS_peripherals.cfg");
  
  
  private static String portraitTemplate = "<?xml version=\"1.0\"?>"
      + "<!DOCTYPE JCPAGETEMPLATE SYSTEM \"JCPageTemplate.dtd\">"
      + "<JCPAGETEMPLATE TITLE=\"8p5x11\">" + "<PAGE NAME=\"8p5x11\" UNIT=\"inches\">"
      + "<LOCATION X=\"0\" Y=\"0\"/>" + "<SIZE WIDTH=\"8.5\" HEIGHT=\"11\"/>"
      + "<FRAME NAME=\"header\" UNIT=\"inches\">" + "<LOCATION X=\"1\" Y=\"0.5\"/>"
      + "<SIZE WIDTH=\"7.0\" HEIGHT=\"0.5\"/>" + "</FRAME>"
      + "<FRAME NAME=\"body\" UNIT=\"inches\">" + "<LOCATION X=\"1\" Y=\"1\"/>"
      + "<SIZE WIDTH=\"7.0\" HEIGHT=\"9.0\"/>" + "<COLUMN COUNT=\"1\"/>" + "</FRAME>"
      + "<FRAME NAME=\"footer\" UNIT=\"inches\">" + "<LOCATION X=\"1\" Y=\"10\"/>"
      + "<SIZE WIDTH=\"7.0\" HEIGHT=\"1\"/>" + "</FRAME>" + "<FLOWFRAME NAME=\"body\"/>"
      + "<FLOWPAGE NAME=\"8p5x11\"/>" + "<FLOWSECTION NAME=\"8p5x11\"/>" + "</PAGE>"
      + "</JCPAGETEMPLATE>";
  private static String landscapeTemplate = "<?xml version=\"1.0\"?>"
      + "<!DOCTYPE JCPAGETEMPLATE SYSTEM \"JCPageTemplate.dtd\">\n"
      + "<JCPAGETEMPLATE TITLE=\"11x8p5\">\n" + "    <PAGE NAME=\"11x8p5\" UNIT=\"inches\">"
      + "        <LOCATION X=\"0\" Y=\"0\"/>" + "        <SIZE WIDTH=\"11\"  HEIGHT=\"8.5\"/>"
      + "        <FRAME NAME=\"header\" UNIT=\"inches\">"
      + "            <LOCATION X=\"0.5\" Y=\"0.5\"/>"
      + "            <SIZE WIDTH=\"10.5\"  HEIGHT=\"1.5\"/>" + "        </FRAME>"
      + "        <FRAME NAME=\"body\" UNIT=\"inches\">"
      + "            <LOCATION X=\"0.5\" Y=\"1.25\"/>"
      + "            <SIZE WIDTH=\"10.5\"  HEIGHT=\"5.0\"/>" + "        </FRAME>"
      + "        <FRAME NAME=\"footer\" UNIT=\"inches\">"
      + "            <LOCATION X=\".75\" Y=\"7.5\"/>"
      + "            <SIZE WIDTH=\"10.5\"  HEIGHT=\"2\"/>" + "        </FRAME>"
      + "        <FLOWFRAME NAME=\"body\"/>" + "        <FLOWPAGE NAME=\"11x8p5\"/>"
      + "        <FLOWSECTION NAME=\"11x8p5\"/>" + "    </PAGE>" + "</JCPAGETEMPLATE>\n";
  protected JCPage template_page;
  private boolean isLandscape;  
  private JCAWTPrinter printer;
  private double widthOfPage;
  private String template;
  protected JCFlow flow; 
  private CMSItem item;
  String[][] reportElement;
  StringBuffer data, label;
  private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
  public static int requestNo = 0;
 
  /**
   * put your documentation comment here
   */
  public ArmaniStockLocatorPrinter() {
    if (res == null) {
      res = ResourceManager.getResourceBundle();
    }
  }

  /**
   * put your documentation comment here
   * @param   Reportable reportable
   */
  public ArmaniStockLocatorPrinter(CMSItem item, String empId, String miscItemDesc) {
    if (res == null) {
      res = ResourceManager.getResourceBundle();
    }   
    data = new StringBuffer();
    data.append(empId+",");
    data.append(sdf.format(new Date())+",");
    data.append(item.getBrand()+",");
    data.append(item.getDepartment()+",");
    data.append(item.getItemDetail().getClassDesc()+" "+item.getClassId()+",");
    data.append(item.getItemDetail().getSeasonDesc()+" "+item.getSeason()+",");
    data.append(item.getDescription()+",");
    data.append(item.getColorId()+" "+item.getItemDetail().getColorDesc()+",");  
    data.append(item.getItemDetail().getSizeIndx()+",");
    data.append(item.getBarCode()+",");
    data.append(miscItemDesc+",");
    
    label = new StringBuffer();
    label.append("Requestor:");
    label.append("Date:");
    label.append("Division:");
    label.append("Dept:");
    label.append("Class:");
    label.append("Season:");
    label.append("Item:");
    label.append("Color:");
    label.append("Size:");
    label.append("Barcode:"); 
    label.append("Drop Off:");
   
    this.item = item;
    } 

  /**
   * put your documentation comment here
   */
  public void setLandscape() {
    isLandscape = true;
  }

  /**
   * put your documentation comment here
   */
  public void setPortrait() {
    isLandscape = false;
  }

  /**
   * put your documentation comment here
   * @param template
   */
  public void setTemplate(String template) {
    this.template = template;
  }

  /**
   * put your documentation comment here
   * For printing the document
   */
  public void print() {
    if (template == null) {
      if (isLandscape) {
        template = landscapeTemplate;
      } else {
        template = portraitTemplate;
      }
    }
    widthOfPage = isLandscape ? 10.0 : 6.9;
    PageFormat pageFormat = new PageFormat();
    pageFormat.setOrientation(isLandscape ? PageFormat.LANDSCAPE : PageFormat.PORTRAIT);
    try {
    	if("TRUE".equalsIgnoreCase(jposPeripheralsConfigMgr.getString("JCAWTPRINTER_SHOW_PRINT_DIALOG"))){
    		printer = new JCAWTPrinter(pageFormat, true);
    	}else{
    		printer = new JCAWTPrinter(pageFormat, false); 
    	}      
    } catch (JCAWTPrinter.PrinterJobCancelledException e) {
      System.out.println("Print Job Cancelled by user");
      return;
    }   
    JCDocument document = null;
    try {
      document = new JCDocument(printer, JCPageTemplate.loadTemplates(new StringReader(template)));
    } catch (Exception e) {
      System.err.println("Error loading template = " + e);
      return;
    }
    template_page = document.stringToTemplate(isLandscape ? "11x8p5" : "8p5x11");
    getStockRequestTitle();
    flow = new JCFlow(document);
    JCUnit.Measure[] colWidths = new JCUnit.Measure[2];    
    double[] reportColumnWidths = getReportColumnWidths();
    for (int i = 0; i < colWidths.length; i++) {
          colWidths[i] = new JCUnit.Measure(JCUnit.INCHES, reportColumnWidths[i] * widthOfPage);
    }  
    JCPageTable table = new JCPageTable(document,colWidths, JCTableStyle.STYLE_0);
    int[] reportColumnAlignments = getReportColumnAlignments();
    if (reportColumnAlignments == null) {
      reportColumnAlignments = new int[2];
      for (int i = 0; i < reportColumnAlignments.length; i++) {
        reportColumnAlignments[i] = JCTextStyle.ALIGNMENT_LEFT;
      }
    }    
    String[] labelValue = new String(label).split(":");
    String[] dataValue =  new String(data).split(",");
    String [][] resultData = new String[dataValue.length][2];
    for( int k = 0; k < dataValue.length; k++){
    	resultData[k][0]= labelValue[k];
    	resultData[k][1]= dataValue[k];    	
    }  
    for (int i = 0; i < resultData.length; i++) {
      for (int j = 0; j < resultData[0].length; j++) {
    	  Font f = new Font("Times New Roman", 0, 16);   	 
    	  JCTextStyle normal = (JCTextStyle)JCTextStyle.BOLD.clone();  
    	  normal.setFont(f);
          normal.setAlignment(reportColumnAlignments[j]);
         table.printToCell(i, j,normal, resultData[i][j]);
      }
    }     
    // flow table onto document
    flow.print(table);
    // print document
    document.print();
    CMSApplet.theAppMgr.showErrorDlg(res.getString("Document is sending to printer."));
   }
  
  public void getStockRequestTitle(){
		JCFrame header_frame = template_page.stringToFrame("header");
	    try {
	    	requestNo = requestNo + 1; 
			header_frame.print(JCTextStyle.DEFAULT_HEADER, res.getString("REQUEST # ")+" "+requestNo);
			header_frame.newLine(JCTextStyle.DEFAULT_HEADER);
		} catch (EndOfFrameException e) {
			e.printStackTrace();
		}	     
	}
  
  public double[] getReportColumnWidths() {
	  double[] reportColumnWidths = new double[2];
	    if (reportColumnWidths.length == 2) {
	      reportColumnWidths[0] =  0.20;
	      reportColumnWidths[1] =  0.40;	     
	    } else {
	      for (int i = 0; i < reportColumnWidths.length; i++) {
	        reportColumnWidths[i] = (int) (1.0d / reportColumnWidths.length);
	      }
	    }	   
	    return reportColumnWidths;
	  }
  
  public int[] getReportColumnAlignments() {
	    return null; //All left justifies
	  }

	
}

