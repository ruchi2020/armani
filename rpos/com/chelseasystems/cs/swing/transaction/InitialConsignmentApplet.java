/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.event.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet;
import com.chelseasystems.cs.pos.*;
import java.awt.*;
import java.util.Calendar;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.config.ConfigMgr;
import java.awt.*;
import java.text.SimpleDateFormat;
import com.chelseasystems.cr.customer.Customer;


/**
 * <p>Title: InitialConsignmentApplet </p>
 *
 * <p>Description: Used for Consignments </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc</p>
 *
 * @author Khyati S
 * @version 1.0
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 06-07-2005 | Vikram    | 65, 66    |Transfer Associate and Customer between sale /|
 |      |            |           |           |Pre-Sale / Consignment                        |
 --------------------------------------------------------------------------------------------
 | 1    | 04-14-2005  | Khyati     | N/A      1.Consignment Specification                   |
 --------------------------------------------------------------------------------------------
 */
public class InitialConsignmentApplet extends CMSApplet {
  private CMSCompositePOSTransaction theTxn;
	private static ConfigMgr config;
	private String fipay_flag;
	
  //Initialize the applet
  public void init() {
    try {
      MenuPanel pnlMenu = new MenuPanel();
      this.getContentPane().add(pnlMenu, BorderLayout.CENTER);
      pnlMenu.setAppMgr(theAppMgr);
      pnlMenu.setTitle(res.getString("Consignment MENU"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Start the applet
  public void start() {
	String fileName = "store_custom.cfg";
	config = new ConfigMgr(fileName);
	fipay_flag = config.getString("FIPAY_Integration");
	
	 //Default value of the flag is Y if its not present in credit_auth.cfg
	if (fipay_flag == null) {
		fipay_flag = "Y";
	}
		
    theAppMgr.setTransitionColor(theAppMgr.getTheme().getMenuBackground());
    theOpr = (com.chelseasystems.cs.employee.CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    theAppMgr.showMenu(MenuConst.CONSIGNMENTS, theOpr);
    theAppMgr.setSingleEditArea(res.getString("Select option."));
    theAppMgr.startTimeOut(60);
	boolean clearMessage =  true;
	//for clearing the screen on login screen
	if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
		sendIdleMessageData(null,null,false,true,clearMessage,"");
	}
  }

  //Stop the applet
  public void stop() {
    theAppMgr.endTimeOut();
  }

  /**
   *
   * @param anEvent CMSActionEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    try {
      theTxn = CMSTransactionPOSHelper.allocate(theAppMgr);
      //VM: Transfer of Associate and Customer between Sale, PreSale and Consignment
      Employee associate = (Employee)theAppMgr.getStateObject("ASSOCIATE");
      Customer customer = (Customer)theAppMgr.getStateObject("CUSTOMER");
      if (theTxn.getConsultant() == null) {
        if (associate != null)
          theTxn.setConsultant(associate);
        else
          theTxn.setConsultant((Employee)theOpr);
      }
      if (theTxn.getCustomer() == null && customer != null)
        theTxn.setCustomer(customer);
      theAppMgr.removeStateObject("ASSOCIATE");
      theAppMgr.removeStateObject("CUSTOMER");
      theAppMgr.addStateObject("TXN_POS", theTxn);
      theAppMgr.addStateObject("TXN_MODE", new Integer(InitialSaleApplet.SALE_MODE));
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (sAction.equals("CONSIGNMENTS_IN")) {
      //KS: Create New Transaction
      // Change TransactionMode to CONSIGNMENT_OPEN mode.
      theAppMgr.addStateObject("TXN_MODE", new Integer(InitialSaleApplet.CONSIGNMENT_OPEN));
      applyExpirationDate();
    } else if (sAction.equals("CONSIGNMENTS_OUT")) {
      theAppMgr.addStateObject("TXN_MODE", new Integer(InitialSaleApplet.CONSIGNMENT_CLOSE));
    }
  }

  /**
   * put your documentation comment here
   */
  private void applyExpirationDate() {
    try {
      Calendar calendar = Calendar.getInstance();
      ConfigMgr config = new ConfigMgr("pos.cfg");
      int iDays = -1;
      if (config != null) {
        iDays = Integer.parseInt(config.getString("CONSIGNMENT_EXPIRY_NUMBER_OF_DAYS"));
        iDays += calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, iDays);
        theTxn.getConsignmentTransaction().setExpirationDate(calendar.getTime());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Get Version
   * @return String
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * Get ScreenName
   * @return String
   */
  public String getScreenName() {
    return ("Consignment");
  }
  
  private boolean sendIdleMessageData(POSLineItem line,POSLineItem[] lineItemArray ,boolean Refresh, boolean idleMessage, boolean clearMessage, String discountAmt) {
		try {
			String result = "";
			//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
				//End
			String responseArray[] = null;
			String ajbResponse[] = null;
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
			
				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
				//Changes for Canada validate method needs to pass
				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				if(!storeCountry.equals("CAN")){
					//String tt = orgTxn.getTransactionType();
					responseArray = CMSItemHelper.validate(theAppMgr, txn, register.getId(),line,lineItemArray,Refresh,idleMessage,clearMessage,discountAmt,false);
					if(responseArray!=null){
					//End
					int length = responseArray.length;
					for (int i=0; i<length ;i++){
					//Vivek Mishar : Added one codition for blank respose when AJB server gets down in middle of transaction.	
					//if(responseArray[i] != null && (responseArray[i].toString().contains("All the Ajb Servers")||responseArray[i].equals(""))){
						//Vivek Mishra : Removed the blak condition as it was causing the All AJB Server down dialogue even in case of server is recover. 
						if(responseArray[i] != null && responseArray[i].toString().contains("All the Ajb Servers")){
						//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
						//if(!Refresh)
							//count++;
						/*if(count==qty)
						{*/
						theAppMgr.showErrorDlg("All the AJB servers are down");
						/*count=0;
						}//End
*/						return false;
					}
					}
					}
				}else
				
				if (responseArray == null) {
					return true;
				}
				//count=0;	
			return (true);
		} catch (Exception ex) {
			ex.printStackTrace();
			theAppMgr.showExceptionDlg(ex);
			return (false);
		}
	
}
}

