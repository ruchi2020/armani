/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report;

import java.awt.Color;
import java.awt.Font;
import java.awt.print.PageFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.ResourceBundle;
import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.report.Reportable;
import com.chelseasystems.cr.util.ResourceManager;
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


/**
 * put your documentation comment here
 */
public class ArmaniReportPrinter {
  private static ResourceBundle res;
  private static ConfigMgr jposPeripheralsConfigMgr = new ConfigMgr("JPOS_peripherals.cfg");
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH24:mm:ss");
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
  private Reportable reportable;
  private JCAWTPrinter printer;
  private double widthOfPage;
  private String template;
  protected JCFlow flow;

  /**
   * put your documentation comment here
   */
  public ArmaniReportPrinter() {
    if (res == null) {
      res = ResourceManager.getResourceBundle();
    }
  }

  /**
   * put your documentation comment here
   * @param   Reportable reportable
   */
  public ArmaniReportPrinter(Reportable reportable) {
    if (res == null) {
      res = ResourceManager.getResourceBundle();
    }
    this.reportable = reportable;
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
    		printer = new JCAWTPrinter(pageFormat, false); // show print dialog = false
    	}      
    } catch (JCAWTPrinter.PrinterJobCancelledException e) {
      System.out.println("Print Job Cancelled by user");
      return;
    }
    //JCDocument document = new JCDocument(printer,
    // JCDocument.BLANK_11X8p5);
    JCDocument document = null;
    try {
      document = new JCDocument(printer, JCPageTemplate.loadTemplates(new StringReader(template)));
    } catch (Exception e) {
      System.err.println("Error loading template = " + e);
      return;
    }
    template_page = document.stringToTemplate(isLandscape ? "11x8p5" : "8p5x11");
    createHeader();
    createFooter();
    flow = new JCFlow(document);
    double colWidth;
    JCUnit.Measure[] colWidths = new JCUnit.Measure[reportable.getReportHeadings().length];
    if (reportable.getReportColumnWidths() == null) {
      colWidth = widthOfPage / colWidths.length;
      for (int i = 0; i < colWidths.length; i++) {
        colWidths[i] = new JCUnit.Measure(JCUnit.INCHES, colWidth);
      }
    } else {
      double[] reportColumnWidths = reportable.getReportColumnWidths();
      for (int i = 0; i < colWidths.length; i++) {
        colWidths[i] = new JCUnit.Measure(JCUnit.INCHES, reportColumnWidths[i] * widthOfPage);
      }
    }
    JCPageTable table = new JCPageTable(document, colWidths, JCTableStyle.STYLE_14);
    int[] reportColumnAlignments = reportable.getReportColumnAlignments();
    if (reportColumnAlignments == null) {
      reportColumnAlignments = new int[reportable.getReportHeadings().length];
      for (int i = 0; i < reportColumnAlignments.length; i++) {
        reportColumnAlignments[i] = JCTextStyle.ALIGNMENT_LEFT;
      }
    }
    // dump report data into table
    String[][] reportElement = reportable.getReportElements();
    for (int i = 0; i < reportElement.length; i++) {
      for (int j = 0; j < reportElement[0].length; j++) {
        JCTextStyle normal = (JCTextStyle)JCTextStyle.NORMAL.clone();
        normal.setAlignment(reportColumnAlignments[j]);
        table.printToCell(i, j, normal, reportElement[i][j]);
      }
    }
    // create table headings
    JCPageTable header = table.createHeaders();
    try {
      String[] reportHeadings = reportable.getReportHeadings();
      for (int i = 0; i < reportHeadings.length; i++) {
        JCFrame frame = header.getCellFrame(0, i);
        JCTextStyle bold = (JCTextStyle)JCTextStyle.BOLD.clone();
        bold.setAlignment(reportColumnAlignments[i]);
        frame.print(bold, reportHeadings[i]);
      }
    } catch (EndOfFrameException e) {
      System.out.println("end of frame on printing headings?");
    }
    String[] reportTotals = reportable.getReportTotals();
    if (reportTotals != null) {
      int totalRow = table.getRowCount() + 1;
      for (int i = 0; i < reportTotals.length; i++) {
        JCTextStyle bold = (JCTextStyle)JCTextStyle.BOLD.clone();
        bold.setAlignment(reportColumnAlignments[i]);
        table.printToCell(totalRow, i, bold, reportTotals[i]);
      }
    }
    // flow table onto document
    flow.print(table);
    // print document
    document.print();
    if (this.reportable instanceof CMSApplet) {
      CMSApplet.theAppMgr.showErrorDlg(res.getString("Document is sending to printer."));
    }
  }

  /**
   * put your documentation comment here
   */
  protected void createHeader() {
    JCFrame header_frame = template_page.stringToFrame("header");
    try {
      BufferedReader br = new BufferedReader(new StringReader(reportable.getReportTitle()));
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println("putting to header: " + line);
        // If there is no printer installed on the box or the default
        // printer
        // has become invalid then this method never returns...
        header_frame.print(JCTextStyle.DEFAULT_HEADER, line);
        header_frame.newLine(JCTextStyle.DEFAULT_HEADER);
      }
    } catch (IOException ex) {
      System.out.println("IOException on header " + ex);
      ex.printStackTrace();
    } catch (EndOfFrameException e) {
      System.out.println("End of header frame ex = " + e);
    }
  }

  /**
   * put your documentation comment here
   */
  protected void createFooter() {
    JCFrame footer_frame = template_page.stringToFrame("footer");
    try {
      Boolean trainingMode = (Boolean)AppManager.getInstance().getGlobalObject("TRAINING");
      if (trainingMode == null || !trainingMode.booleanValue()) {} else {
        JCTextStyle normal = (JCTextStyle)JCTextStyle.NORMAL.clone();
        normal.setFont(new Font("Helvetica", Font.BOLD, 15));
        normal.setColor(new Color(150, 150, 150));
        footer_frame.print(normal
            , "  ****TRAINING****TRAINING****TRAINING****TRAINING****TRAINING****TRAINING****TRAINING****");
      }
      footer_frame.newLine(JCTextStyle.NORMAL);
      footer_frame.print(JCTextStyle.NORMAL, "Page: ");
      footer_frame.print(JCTextStyle.NORMAL, TextMacro.PAGE_NUMBER);
      footer_frame.nextTab(JCTextStyle.NORMAL);
      footer_frame.print(JCTextStyle.NORMAL, " Printed: " + sdf.format(new Date()));
    } catch (EndOfFrameException e) {
      System.err.println("End of footer frame ex = " + e);
    }
  }
}

