/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 04-08-2005 | Anand    | N/A        |1.barcode parsing specification of Add Item   |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.appmgr.*;
import java.util.*;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 *
 * <p>Title: ItemEntryParser</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ItemEntryParser {
  private ConfigMgr config;
  private String strRules;
  private String strBarcodeLength;
  private String strBarcodeStartPosn;
  private String strBarcodeEndPosn;
  private HashMap hmBarcodes;
  private String sBarcode;
  private String strRetailBarcode;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;

  /**
   * Default Constructor
   */
  public ItemEntryParser() {
  }

  /**
   * This method returns the retail barcode of the item from the entire barcode
   * The rules that define how the get the retail barcode are specified in the
   * item.cfg file
   * @param applet CMSApplet
   * @param theAppMgr IApplicationManager
   * @param strEntireBarcode String
   * @param strLength String
   * @return String
   */
  public String getRetailBarcode(CMSApplet applet, IApplicationManager theAppMgr
      , String strEntireBarcode, String strLength) {
    this.applet = applet;
    this.theAppMgr = theAppMgr;
    config = new ConfigMgr("item.cfg");
        //Sergio
        if (config.getString("ITEM_COUNTRY") != null){

            
      String cc = "";
      String check = "";
      String sku = "";
      // 
      //For Europe has to be always like this;no drop
  /*    if ( (strEntireBarcode.length() == 34) ) {
        strLength = new String("13");
        //For Europe has to be always like this;no drop
        cc = strEntireBarcode.substring(0, 1);
        sku = strEntireBarcode.substring(2, 12);
      //  check = strEntireBarcode.substring(12, 1);
        
        strEntireBarcode = cc + '1' + sku+'0';  */
        
       /* if (check.compareTo("0")==0)
        {
         strEntireBarcode = cc + '1' + sku+'0';
        }else
        {
            
            
        }*/
        
      // Gestione 2a scelta eliminando il checkdigit
      	if(strEntireBarcode.length()==13 ){
      		cc = "0";
    	  	sku = strEntireBarcode.substring(0, 12)+ cc;
    	  	if (!strEntireBarcode.substring(0,1).equalsIgnoreCase("8")) {
    	  		strEntireBarcode= sku;
    	  	}
      	}
      // Fine
        
        if(strEntireBarcode.length()==12){
                  //strEntireBarcode="0"+strEntireBarcode;
        	strLength=new String("13");
                  //For Europe has to be always like this;no drop
        	cc = "0";
        	
        	/* Lorenzo commentato per problema EA Manzoni
        	 * sku = strEntireBarcode.substring(1, 11);
                	  strEntireBarcode=cc + '1'+ sku+'0'; */
        	
        	sku = cc + strEntireBarcode.substring(0, 11);
          	strEntireBarcode= sku + cc;
        }
      
//		 //For Europe has to be always like this;no drop
//		 String cc= strEntireBarcode.substring(0,1);
//		 String sku= strEntireBarcode.substring(2,12);
//		 strEntireBarcode=cc + '1' + sku;
          }
    //check for null

        if (config.getString("ITEM_BARCODE_RULES") != null)
      strRules = config.getString("ITEM_BARCODE_RULES");
    else {
      theAppMgr.showErrorDlg(applet.res.getString("No Barcode rules defined."));
    }
    StringTokenizer strTokBarcode = new StringTokenizer(strRules, ",");
    hmBarcodes = new HashMap();
    while (strTokBarcode.hasMoreTokens()) {
      sBarcode = strTokBarcode.nextToken();
      strBarcodeLength = config.getString(sBarcode + ".BARCODE_LENGTH");
      hmBarcodes.put(strBarcodeLength, sBarcode);
    }
    if (hmBarcodes.containsKey(strLength)) {
      String value = (String)hmBarcodes.get(strLength);
      strBarcodeStartPosn = config.getString(value + ".BARCODE_START_POSITION");
      strBarcodeEndPosn = config.getString(value + ".BARCODE_END_POSITION");
    }
    if (strBarcodeStartPosn != null && strBarcodeEndPosn != null) {
      // make sure startpos >= 0 and endpos < barcode.length
      strRetailBarcode = strEntireBarcode.substring(new Integer(strBarcodeStartPosn).intValue()
          , new Integer(strBarcodeEndPosn).intValue());
       //Riccardino per atom24 
      //Sergio
     /* if (config.getString("ITEM_COUNTRY") != null) {
        if (strRetailBarcode.length() == 12) {
          strLength = new String("12");
          String cc = strRetailBarcode.substring(0, 1);
          cc = "0";
          String sku = strRetailBarcode.substring(2, 12);
          strRetailBarcode = cc + '1' + sku;
        }
      }*/

      return strRetailBarcode;
    } else {
      return null;
    }
  }
}

