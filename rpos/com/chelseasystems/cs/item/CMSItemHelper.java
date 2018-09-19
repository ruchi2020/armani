/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	2/4/05	        KS		                        Base
 * 2	2/4/05	        KS	POS_IS_ItemDownload_Rev1	CMSItem
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.item;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.ajbauthorization.AJBPingThread;
import com.chelseasystems.cs.ajbauthorization.AJBServiceManager;
import com.chelseasystems.cs.ajbauthorization.AJBValidation;
import com.chelseasystems.cs.authorization.bankcard.CMSCreditAuthHelper;
import com.chelseasystems.cs.config.AJBPingThreadBootStrap;
import com.chelseasystems.cs.payment.CMSCoupon;
import com.chelseasystems.cs.payment.CMSRedeemableClientServices;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSTransactionPOSClientServices;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.register.CMSRegisterClientServices;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.Version;



import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * <p>
 * Title: CMSItemHelper
 * </p>
 * 
 * <p>
 * Description: This class have static convenience methods to manipulate Client
 * Services.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author Khyati Shah
 * @version 1.0
 */
public class CMSItemHelper {
	static boolean verboseMode = false;
	static boolean localTestMode = false;
	private ConfigMgr config;
	private static Logger log = Logger.getLogger(CMSItemHelper.class.getName());
	public CMSItemHelper(){
		 String strLocalTestMode = config.getString("LOCAL_TEST_MODE");
		    String strVerboseMode = config.getString("VERBOSE_MODE");
				if (strLocalTestMode.trim().equalsIgnoreCase("TRUE")) {
					localTestMode = true;
				}
				if (strVerboseMode.trim().equalsIgnoreCase("TRUE")) {
					verboseMode = true;
				}
				}
	
	/**
	 * This method is used to search an item by its item id
	 * 
	 * @param theMgr
	 *            IRepositoryManager
	 * @param id
	 *            String
	 * @return CMSItem
	 * @throws Exception
	 */
	public static CMSItem findById(IRepositoryManager theMgr, String id)
			throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.findById(id);
	}

	/**
	 * Added by Anjana to select the proper barcode if it is a SAP Store
	 * 
	 * @param theMgr
	 *            IRepositoryManager
	 * @param barCode
	 *            String
	 * @param storeId
	 *            String
	 * @throws Exception
	 * @return CMSItem
	 */
	public static CMSItem findSAPItem(IRepositoryManager theMgr,
			String barCode, String storeId) throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.findSAPItem(barCode, storeId);
	}

	/**
	 * This method is used to search an item by its bar code for a given store
	 * 
	 * @param theMgr
	 *            IRepositoryManager
	 * @param barCode
	 *            String
	 * @param storeId
	 *            String
	 * @throws Exception
	 * @return CMSItem
	 */
	public static CMSItem findByBarCode(IRepositoryManager theMgr,
			String barCode, String storeId) throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theMgr
				.getGlobalObject("ITEM_SRVC");
		// System.out.println("CMSItem " + cs.getClass());
		return cs.findByBarCode(barCode, storeId);
	}

	/**
	 * This method is used to find items by its description for a given store
	 * 
	 * @param theMgr
	 *            IRepositoryManager
	 * @param description
	 *            String
	 * @param storeId
	 *            String
	 * @return String[]
	 * @throws Exception
	 */
	public static String[] findIDListByDescription(IRepositoryManager theMgr,
			String description, String storeId) throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.findIDListByDescription(description, storeId);
	}

	/**
	 * This method returns all items
	 * 
	 * @param theMgr
	 *            IRepositoryManager
	 * @return CMSItem[]
	 * @throws Exception
	 */
	public static CMSItem[] findAllItems(IRepositoryManager theMgr)
			throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.findAllItems();
	}

	/**
	 * This method returns all items for a given store
	 * 
	 * @param theMgr
	 *            IRepositoryManager
	 * @param store
	 *            Store
	 * @return CMSItem[]
	 * @throws Exception
	 */
	public static CMSItem[] findAllItemsForStore(IRepositoryManager theMgr,
			Store store) throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.findAllItemsForStore(store);
	}

	/**
	 * This method is used to update an Item, specified by its Id, with a new
	 * price, which is specified by the amount. Only local services need to
	 * implement this method.
	 * 
	 * @param theMgr
	 *            IRepositoryManager
	 * @param itemId
	 *            String
	 * @param amount
	 *            Currency
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean updateItemPrice(IRepositoryManager theMgr,
			String itemId, ArmCurrency amount) throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.updateItemPrice(itemId, amount);
	}

	/**
	 * @param theMgr
	 *            IRepositoryManager
	 * @param store
	 *            Store
	 * @return Map
	 * @throws Exception
	 */
	public static Map getSupplierSeasonYear(IRepositoryManager theMgr,
			Store store) throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.getSupplierSeasonYear(store);
	}

	/**
	 * This method is used to search item on the basis of specified search
	 * parameter
	 * 
	 * @param theMgr
	 *            IRepositoryManager
	 * @param searchString
	 *            ItemSearchString
	 * @throws Exception
	 * @return Item[]
	 */
	public static Item[] findItems(IRepositoryManager theMgr,
			ItemSearchString searchString) throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.findItems(searchString);
	}

	/**
	 * This method is used to find items with zero unit price
	 * 
	 * @param theMgr
	 *            IRepositoryManager
	 * @return CMSItem[]
	 * @throws Exception
	 */
	public static CMSItem[] findItemsWithNoUnitPrice(IRepositoryManager theMgr)
			throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.findItemsWithNoUnitPrice();
	}

	/**
	 * Added by Satin. This method is used to search an itemId corresponding to
	 * entered barcode for exception item.
	 * 
	 * @param theMgr
	 *            IRepositoryManager
	 * @param barcode
	 *            String
	 * @param storeId
	 *            String
	 * @return String
	 * @throws Exception
	 */
	public static String selectItemIdFromAsItm(IRepositoryManager theAppMgr,
			String barcode) throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theAppMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.selectItemIdFromAsItm(barcode);
	}

	/**
	 * Added by Satin. This method is used to select item corresponding to
	 * entered itemId for exception item.
	 * 
	 * @param theAppMgr
	 *            IRepositoryManager
	 * @param itemId
	 *            String
	 * @return String
	 * @throws Exception
	 */
	public static String selectItemFromAsItm(IRepositoryManager theAppMgr,
			String itemId) throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theAppMgr
				.getGlobalObject("ITEM_SRVC");
		return cs.selectItemFromAsItm(itemId);
	}

	/**
	 * @param theMgr
	 * @param s
	 * @return
	 * @exception Exception
	 */
	public static String sendMessageDatatoVerifone(IRepositoryManager theMgr,
			String request) throws Exception {
		int request_length = request.length();
		String reply = null;
		String tempReply = null;
		//String testType = "";
		//String strDollarAmount = "";
		//double testInt = 0.0;
		//Vivek Mishra : Added to send 106 ping just before sending 150 request
	if (verboseMode) {
			log.info("CMSItemHelper.sendMessageDatatoVerifone(): No requests = "
							+ request_length);
		}
			try {
				if (!localTestMode) {
					// Initialize reply in case exception is thrown in
					// getCreditAuth() method.
					reply = "";
					/*Vivek Mishra : Added for Connection Validation*/
					//if(AJBServiceManager.getCurrent().error_message!=null)
					//Vivek Mishra : Changed the condition for fixing AJB sever down bug.
					/*if((AJBServiceManager.getCurrent()).getErrorMessage()!=null)	
					{
						//reply=AJBServiceManager.getCurrent().error_message;
						reply = (AJBServiceManager.getCurrent()).getErrorMessage();
						return reply;
					}*/
					//End
					//Vivek Mishra : Changed the method to getItemAuth()
					/*tempReply = AJBServiceManager.getCurrent()
					.getCreditAuth((String) request);*/
					tempReply = AJBServiceManager.getCurrent().getItemAuth((String) request);
					if (tempReply != null) {
						reply = tempReply;
			
					}
				}
		
			} catch (Exception e) { // End of Try
				LoggingServices.getCurrent().logMsg(CMSItemHelper.class.toString(),
						"getAuth()", "Exception occured: " + e,
						"Verify AJBService running", LoggingServices.MAJOR);
				log.info("CMSItemHelper.sendMessageDatatoVerifone(): exception = "
								+ e);
				log.error(e.toString());
				return reply;
			}
		 // End of for (int i = 0; i< request_length; i++)
		
		return reply;
	}

	/**
	 * Added by Satin. This method is used to insert a record into
	 * AS_ITM_RTL_STR for exception item.
	 * 
	 * @param theAppMgr
	 *            IRepositoryManager
	 * @param storeId
	 *            String
	 * @param itemId
	 *            String
	 * @param retailPrice
	 *            ArmCurrency
	 * @param currencyCode
	 *            String
	 * @param vatRate
	 *            Double
	 * @param taxable
	 *            String
	 * @return void
	 * @throws Exception
	 */
	public static void insertIntoAsItmRtlStr(IRepositoryManager theAppMgr,
			String storeId, String itemId, ArmCurrency retailPrice,
			String currencyCode, String itemDesc, Double vatRate, String taxable)
			throws Exception {
		CMSItemClientServices cs = (CMSItemClientServices) theAppMgr
				.getGlobalObject("ITEM_SRVC");
		cs.insertIntoAsItmRtlStr(storeId, itemId, retailPrice, currencyCode,
				itemDesc, vatRate, taxable);

	}

	/**
	 * Added By Anjana to Validate the request sent to verifone
	 * */
	public static String[] validate(IRepositoryManager theMgr,
			CMSCompositePOSTransaction theTxn, String terminalId,POSLineItem line,
			POSLineItem[] lineItemArray,boolean Refresh,boolean idleMessage, boolean clearMessage, String discountAmt, boolean fromPayment)
			throws Exception {
		
		//Vivek Mishra : Added region check in order to avoid fatal exception for other regions except US
		if("US".equalsIgnoreCase(Version.CURRENT_REGION)  ){
		AJBValidation ajbValidation = new AJBValidation();
		String error_message = "All the Ajb Servers are down at the moment";
		//Vivek Mishra : This Ping thread call needs to be commented if it is not required to be send before 150 item bucket message in future.  
		//AJBPingThreadBootStrap.getPingThread().ping();
		String request = null;
		// String reply = null;
		// Vivek Mishra : Changed for fixing the null pointer exception in AJB
		// server off line scenario.
		/*
		 * String finalReply[] = null; String[] myIntArray= null;
		 */
		String finalReply[] = new String[10];
		// End
		// First obtain the validation requests
		//Vivek Mishra : Added IRepositoryManager in the parameter list in order to avoid null theMgr for send sale tax calculation
		//request = ajbValidation.sendMessageDatatoVerifone(line, lineItemArray,Refresh,idleMessage,clearMessage,discountAmt,fromPayment);
		//if(AJBPingThreadBootStrap.getPingThread().error_message.equals(null))
		request = ajbValidation.sendMessageDatatoVerifone(theMgr, line, lineItemArray,Refresh,idleMessage,clearMessage,discountAmt,fromPayment);

		// Vivek Mishra : Commented unnecessary code.
		// request = myObj;
		// End
		// Now get the replies for each credit request
		try {
			if (request != null) {
				finalReply[0] = sendMessageDatatoVerifone(theMgr, request);
				// System.out.println("reply in Item helper to check exceptio ::::"+
				// reply+":::::replylemgth::::::"+reply.length);
			}
		} catch (Exception e) {
			LoggingServices.getCurrent().logMsg(
					CMSCreditAuthHelper.class.toString(),
					"validate()",
					"Problem with CMSItemHelper,validate method exception: "
							+ e, "Verify Services is running",
					LoggingServices.MAJOR);
			throw e;
		}
		// Start Issue # 991
		boolean rawDataRequest = false;
		ConfigMgr config = new ConfigMgr("credit_auth.cfg");
		String strRawDataRequest = config.getString("RAW_DATA_REQUEST");
		if (strRawDataRequest.equalsIgnoreCase("TRUE")) {
			rawDataRequest = true;
		}
		System.out.println("RAW DATA REQUEST=" + rawDataRequest);

		if (rawDataRequest) {
			System.out.println("setting reply: " + finalReply[0]);
		}
		// Vivek Mishra : Commented unnecessary code.
		/*
		 * if(finalReply[0].toString().contains(error_message)){ finalReply[0] =
		 * error_message; return finalReply; }
		 */
		// End
		if (rawDataRequest) {
			System.out.println("the return from setting auth: ");
		}

		// else
		// finalReply[i] = new String(payments[i].setCreditAuthorization(""));

		request = null;
		return finalReply;
		}
		else
		 return null;
	}
	// }

}
