/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 09-07-2005 | Manpreet  | 900       | Reading NoSale reasons from ArmaniCommon.cfg |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 06-14-2005 | Vikram    | 200       | Open Cash Drawer - doesn't open, no response |
 |      |            |           |           | from system                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 06-13-2005 | Vikram    | 163       | No Sale/Open Cash Drawer: Reason dialog was  |
 |      |            |           |           | not displayed when SOLICIT_NO_SALE_TYPES=true|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1   | 04-19-2005 | Mukesh    | N/A       |1.Added 3 new methods to capture and process   |
 |      |            |           |           | No sale button event.                        |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.menu;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.swing.panel.MenuPanel;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.rules.RulesInfo;
import java.util.Enumeration;
import com.chelseasystems.cr.config.*;
import java.util.*;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSNoSaleTransaction;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.txnnumber.CMSTransactionNumberHelper;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cr.receipt.CMSDrawer;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.receipt.ReceiptConfigInfo;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.fiscaldocument.FiscalInterface;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.util.ArmConfigLoader;


/**
 * put your documentation comment here
 */
public class TransactionMgmtMenuApplet extends CMSApplet {

  private FiscalInterface fiscalInterface = null;
  private static ConfigMgr config;
  private String fipay_flag;

  //Initialize the applet
  public void init() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
    //Fiscal interface
    try {
      ConfigMgr configMgr = new ConfigMgr("fiscal_document.cfg");
      String sClassName = configMgr.getString("FISCAL_DOCUMENT_PRINTER");
      Class cls = Class.forName(sClassName);
      fiscalInterface = (FiscalInterface)cls.newInstance();
    } catch (Exception e) {
      System.out.println("No Fiscal Interface could be initialized");
    }
    //end fiscal
  }


  //Component initialization
  private void jbInit()
      throws Exception {
    MenuPanel pnlMenu = new MenuPanel();
    this.getContentPane().add(pnlMenu, BorderLayout.CENTER);
    pnlMenu.setAppMgr(theAppMgr);
    pnlMenu.setTitle(res.getString("Transaction Management Menu"));
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
    theAppMgr.showMenu(MenuConst.TRANS_MGMT, theOpr, theAppMgr.PREV_BUTTON);
    theAppMgr.setSingleEditArea(res.getString("Select option."));
    theAppMgr.startTimeOut(60);
    theAppMgr.removeStateObject("TXN_CUSTOMER");
    theAppMgr.removeStateObject("CUST_NFS");
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


  //Destroy the applet
  public void destroy() {}


  //Get Applet information
  public String getVersion() {
    return "$Revision: 1.1 $";
  }


  /**
   * @return
   */
  public String getScreenName() {
    return res.getString("Transaction Mgmt Menu");
  }


  /** Added this method to capture No sale button event
   * @param anEvent
   * @return void
   * @author Mukesh
   */
  public void appButtonEvent(CMSActionEvent event) {
    String sCommand = event.getActionCommand();
    if (sCommand.equals("NO_SALE")) {
      openCashDrawerDlg();
    }
  }


  /** Added this method to create the dialog for selecting the no sale reason
   * @return void
   * @author Mukesh
   */
  private void openCashDrawerDlg() {
    String types[];
    String desc[];
    String title[];
    ConfigMgr config = new ConfigMgr("no_sale.cfg");
    String strSolicitNoSale = config.getString("SOLICIT_NO_SALE_TYPES");
    if (strSolicitNoSale != null && strSolicitNoSale.trim().equalsIgnoreCase("true")) {
      //MSB - 09/07/05
      //Read reasons from ArmaniCommon.cfg
//      ConfigMgr armConfig = new ConfigMgr("ArmaniCommon.cfg")
      ArmConfigLoader armConfig = new ArmConfigLoader();
      String reasonTypes = armConfig.getString("NO_SL_REASON_CD");
      //            String reasonTypes = config.getString("NO_SALE_REASON_CODES");
      ArrayList list = new ArrayList();
      String nextToken;
      String strSelectedReason;
      title = new String[1];
      title[0] = res.getString("No_Sale_Screen_Title");
      if ((reasonTypes != null) && (reasonTypes.trim().length() > 0)) {
        StringTokenizer stk = new StringTokenizer(reasonTypes, ",");
        types = new String[stk.countTokens()];
        desc = new String[stk.countTokens()];
        int i = 0;
        while (stk.hasMoreTokens()) {
          nextToken = stk.nextToken();
          types[i] = armConfig.getString(nextToken + ".CODE");
          desc[i] = armConfig.getString(nextToken + ".LABEL");
          if ((desc[i] != null) && (desc[i].trim().length() > 0)) {
            // MSB -09/07/05 -- Reasons are already locale specific
            // in ArmaniCommon.cfg
            // desc[i] = res.getString(desc[i]);
            list.add(new GenericChooserRow(new String[] {desc[i]
            }
                , types[i]));
          }
          i++;
        }
        GenericChooserRow[] chooserRows = (GenericChooserRow[])list.toArray(new GenericChooserRow[0]);
        GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
            , theAppMgr, chooserRows, title);
        dlg.setVisible(true);
        if (dlg.isOK()) {
          strSelectedReason = (String)dlg.getSelectedRow().getRowKeyData();
          try {
            processNoSale(strSelectedReason);
          } catch (BusinessRuleException bre) {
            theAppMgr.showErrorDlg(res.getString(bre.getMessage()));
          } catch (Exception e) {
            theAppMgr.showExceptionDlg(e);
          }
          return;
        } else {
          // Cancel was pressed .. don't process the no sale. Issue #344
          return;
        }
      }
    }
    try {
      processNoSale(null);
    } catch (BusinessRuleException bre) {
      theAppMgr.showErrorDlg(res.getString(bre.getMessage()));
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
    }
  }


  /** Added this method to process no sale request
   * @return void
   * @param String
   * @author Mukesh
   */
  private void processNoSale(String selectedReason)
      throws Exception {
    CMSNoSaleTransaction txn = new CMSNoSaleTransaction((CMSStore)theStore);
    txn.setId(CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr, (CMSStore)theStore
        , (CMSRegister)theAppMgr.getGlobalObject("REGISTER")));
    txn.setSubmitDate(new Date());
    txn.setProcessDate((Date)theAppMgr.getGlobalObject("PROCESS_DATE"));
    txn.setTheOperator((CMSEmployee)theOpr);
    if (selectedReason != null)
      txn.setComment(selectedReason);
    if (!CMSTxnPosterHelper.post(theAppMgr, txn))
      throw new Exception("The no sale transaction failed to post.");
    System.out.println("******       POSTED NOSALE   " + txn.getId() + "    ******");
    PaymentTransactionAppModel appModel = txn.getAppModel(CMSAppModelFactory.getInstance()
        , theAppMgr);
    // pop drawer
    openDrawer();
    if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
      String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + "NoSaleTransaction.rdo";
      try {
        ObjectStore objectStore = new ObjectStore(fileName);
        objectStore.write(appModel);
      } catch (Exception e) {
        System.out.println("exception on writing object to blueprint folder: " + e);
      }
    }
    appModel.printReceipt(theAppMgr);
  }

  private void openDrawer() {
    System.out.println("->Pop Drawer...");
    try {
      if (fiscalInterface != null && fiscalInterface.openDrawer()) //try the FiscalInterface if available
        return;
    } catch (Exception e) {
      System.out.println("Could not find or use FiscalInterface cash drawer. Trying CMSDrawer...");
    }
    try { //else try CMSDrawer
      CMSDrawer.openDevice(ReceiptConfigInfo.getInstance().getCashDrawerName());
      CMSDrawer.openDrawer();
      CMSDrawer.closeDevice();
    } catch (Exception ex) {
      // CashDrawer not found!
      System.out.println("Cash Drawer Not Found");
    }
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
