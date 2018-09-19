/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * <p>Title: HTMLColumnHeaderUtil </p>
 * <p>Description: Converts a string into HTML label </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class HTMLColumnHeaderUtil {
	public static final int CENTER = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
   private Color foregroundColor = Color.black;
  /**
   * Font
   */
  private Font fontHeader;
  /**
   * String Value of font for HTML
   */
  private String sHTMLFont;
  /**
   * FontStyle
   */
  private String sFontStyle;
  /**
   * FontSize
   */
  private int iFontSize = 2;

  /**
   * Create Util with specified font.
   * @param fontHeader Font
   */
  public HTMLColumnHeaderUtil(Font fontHeader) {
  	  this.fontHeader = fontHeader;
	  createFontString();
  }
    private void createFontString() {
    	sHTMLFont = "<Font Family=";
    	switch (this.fontHeader.getStyle()) {
    		case Font.BOLD:
    			sFontStyle = "B";
    			break;
    		case Font.ROMAN_BASELINE:
    			sFontStyle = "U";
    			break;
    		case Font.ITALIC:
    			sFontStyle = "I";
    			break;
    	}

    	String sColor = " color=#"+Integer.toHexString(foregroundColor.getRed()) +Integer.toHexString(foregroundColor.getGreen())+Integer.toHexString(foregroundColor.getBlue())+" ";
    	iFontSize = fontHeader.getSize();
    	sHTMLFont += fontHeader.getFamily() + " Size=" + iFontSize + "pt"+sColor+">";//<" + sFontStyle + ">";
    }
  	public void setForeground(Color foreground){
  		foregroundColor = foreground;
  		createFontString();
  	}
  /**
   * Converts String into HTML value.
   * @param sHeader StringToBeConverted
   * @return HTMLString
   */
  public String getHTMLHeaderFor(String sHeader) {
	  return getHTMLHeaderFor(sHeader, LEFT);
  }

	public String getHTMLHeaderFor(String sHeader, int iAlignment ) {
		String alignment=null;
		if(iAlignment == LEFT) {
			alignment="left";
		}else if(iAlignment == RIGHT) {
			alignment="right";
		}else if(iAlignment == CENTER) {
			alignment="center";
		}
		String sTmp = "";
		if (sHeader.indexOf("\n") != -1) {
			sTmp = sHeader.substring(sHeader.indexOf("\n") + 1);
			sHeader = sHeader.substring(0, sHeader.indexOf("\n"));
		}
		if (sTmp.length() > 0) {
			if(sFontStyle != null && sFontStyle.trim().length() > 0 ) {
				sHeader = "<"+sFontStyle+">"+sHeader+ "</"+sFontStyle+">";
				sTmp = "<"+sFontStyle+">"+sTmp+ "</"+sFontStyle+">";
			}
			//sHeader = "<HTML>" + sHTMLFont +startAlign+ sHeader + endAlign+ "\n"+startAlign + sTmp + endAlign+"</"+sFontStyle+"></Font> </HTML>";
			sHeader = "<HTML>" +"<TABLE><TR><TD align='"+alignment+"'>"+ sHTMLFont + sHeader+"</Font>"+"</TD></TR><TR><TD align='"+ alignment +"'>" + sHTMLFont + sTmp+"</Font>"+"</TD></TR></TABLE></HTML>";
		}else {
			if(sFontStyle != null && sFontStyle.trim().length() > 0 ) {
				sHeader = "<"+sFontStyle+">"+sHeader+ "</"+sFontStyle+">";
			}
			sHeader = "<HTML>"+"<TABLE><TR><TD align='"+alignment+"'>"+ sHTMLFont + sHeader + "</Font>"+ "</TD></TR></TABLE>" + "</HTML>";
		}
		return sHeader;
	}
}

