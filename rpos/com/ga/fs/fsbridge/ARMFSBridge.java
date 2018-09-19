/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ga.fs.fsbridge;

import com.chelseasystems.cr.appmgr.AppManager;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.fiscalsolutions.s4fs.S4FiscalService;
import com.fiscalsolutions.s4fs.S4FiscalServiceException;
import com.ga.fs.fsbridge.object.ARMFSBOCashInObject;
import com.ga.fs.fsbridge.object.ARMFSBOCashOutObject;
import com.ga.fs.fsbridge.object.ARMFSBOPosEventObject;
import com.ga.fs.fsbridge.object.ARMFSBOSignOffTransactionObject;
import com.ga.fs.fsbridge.object.ARMFSBOSignOnTransactionObject;
import com.ga.fs.fsbridge.object.ARMFSBridgeObject;
import com.ga.fs.fsbridge.utils.ARMEventIDGenerator;
import com.ga.fs.fsbridge.utils.ConfigurationManager;
import com.ga.fs.fsbridge.utils.FilesUtils;
import com.ga.fs.fsbridge.utils.MessageTypeCode;
import com.ga.fs.fsbridge.utils.PosEvent;
import com.ga.fs.fsbridge.utils.StringUtils;
import com.ga.fs.fsbridge.utils.TransactionTypeCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Yves Agbessi
 */
public class ARMFSBridge {

	public boolean isBuilt = false;
	public static boolean isCountryAllowed = false;
	public String country = null;
	private HashMap<String, Boolean> policies;
	private static boolean isOfflineHolderActive = false;
	private static File OfflineHolderFolder = new File(
			new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath,
					"FS_OFFLINE_HOLDER_FOLDER"));
	private static File OfflineHolder = new File(OfflineHolderFolder.getPath()
			+ "/" + "FSBridgeOfflineHolder.lock");
	public static S4FiscalService S4FS;
	public static boolean hasAlreadyReposted = false;

	public ARMFSBridge() {

	}

	public ARMFSBridge build() {


		// *******************************************************************//

		System.out.println("[FISCAL SOLUTIONS BRIDGE] : S4FS Service value : "
				+ S4FS.S4FS_SUCCESS);

		/*
		 * Checks if the current country needs to build FSBridge
		 */
		String[] allowedToBuildCountries = null;
		try{
			setCountry(new ConfigMgr("arm.cfg").getString("LOCALE").substring(3));
			allowedToBuildCountries = new ConfigurationManager()
			.getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath,
					"ALLOWED_TO_BUILD_COUNTRIES").split("\\s+");
		} catch (NullPointerException e){
			isCountryAllowed = false;
			isBuilt = false;
			return this;
			
		}
		
		
		for (String c : allowedToBuildCountries) {

			if (getCountry().equalsIgnoreCase(c)) {

				isBuilt = true;
				isCountryAllowed = true;
				System.out
						.println("[FISCAL SOLUTIONS BRIDGE] : Bridge between ARM and Fiscal Solutions Service built correctly ");
				
				
				// PURGING SAVED REQUESTS
				purge(new ConfigurationManager().getConfig(
						ARMFSBridgeObject.fsBridgeCfgFilePath,
						"FS_BRIDGE_REQUESTS_FOLDER"),
						Integer.parseInt(new ConfigurationManager().getConfig(
								ARMFSBridgeObject.fsBridgeCfgFilePath,
								"FS_BRIDGE_KEEP_REQUESTS_FOR_DAYS")), ".xml");
				

				// PURGING SAVED RESPONSES
				purge(new ConfigurationManager().getConfig(
						ARMFSBridgeObject.fsBridgeCfgFilePath,
						"FS_BRIDGE_RESPONSES_FOLDER"),
						Integer.parseInt(new ConfigurationManager().getConfig(
								ARMFSBridgeObject.fsBridgeCfgFilePath,
								"FS_BRIDGE_KEEP_RESPONSES_FOR_DAYS")), ".xml");

				// REPOSTING NOT POSTED TRANSACTIONS
				repostNotPostedRequests();

				
				return this;

			}

		}

		isCountryAllowed = false;
		System.out.println("[FISCAL SOLUTIONS BRIDGE] : Country '"
				+ getCountry()
				+ "' is not allowed to use Fiscal Solutions Service");
		isBuilt = false;
		return this;

	}

	public boolean postObject(ARMFSBridgeObject object) {
		boolean postingResult = false;

		if (!isBuilt) {
			System.out
					.println("ARM Fiscal Solutions Bridge is not built. Use build() to build a new bridge");

			return false;
		}

		if (object.generateXML() == true) {
			postingResult = object.post();

		}
		return postingResult;

	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public boolean getPolicy(String policy) {
		String ps = new ConfigurationManager().getConfig(
				ARMFSBridgeObject.fsBridgeCfgFilePath, policy);
		return Boolean.parseBoolean(ps);
	}

	/**
	 * @return
	 */
	public boolean generateOfflineHolder() {

		System.out.println(System.getProperty("user.dir"));

		if (!OfflineHolderFolder.exists()) {
			OfflineHolderFolder.mkdir();
		}

		if (!OfflineHolder.exists()) {
			try {

				boolean fileCreationResult = OfflineHolder.createNewFile();
				if (fileCreationResult) {
					isOfflineHolderActive = true;
				}
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				isOfflineHolderActive = false;
				return false;
			}
		}
		return true;

	}

	public static boolean removeOfflineHolder() {

		while (OfflineHolder.exists()) {

			if (OfflineHolder.delete()) {

				isOfflineHolderActive = false;
				return true;
			}
		}

		return false;
	}

	public boolean isOfflineHolderActive() {
		if (OfflineHolder.exists()) {
			isOfflineHolderActive = true;
		} else {
			isOfflineHolderActive = false;
		}
		return isOfflineHolderActive;
	}

	public static String documentToString(Document doc) {
		String output = null;
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		} catch (TransformerConfigurationException ex) {
			Logger.getLogger(ARMFSBridge.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (TransformerException ex) {
			Logger.getLogger(ARMFSBridge.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		return output;
	}

	public static void postARMSignOffTransaction(CMSEmployee employee) {
		if(employee == null){
			
			employee = new CMSEmployee();
			try {
				employee.setFirstName("DIRETTORE");
				employee.setLastName("DIRETTORE");
				employee.setExternalID("000401000");
			} catch (BusinessRuleException e) {
				e.printStackTrace();
			}

		}
		ARMFSBridge fsBridge = new ARMFSBridge().build();
		if (fsBridge.isCountryAllowed) {

			Date now = new Date();
			String id = ARMEventIDGenerator.generate(now);
			ARMFSBOSignOffTransactionObject signOffTransaction = new ARMFSBOSignOffTransactionObject(
					MessageTypeCode.MTC_TRANSACTION, id,
					TransactionTypeCode.TTC_SIGN_OFF_TRANSACTION, now, id,
					employee.getExternalID(), employee.getFirstName() + " "
							+ employee.getLastName());

			if (fsBridge.postObject(signOffTransaction)) {

				String response = signOffTransaction.processResponse();
				if (response.contains("ERROR") || response.contains("UNABLE")) {
					AppManager
							.getCurrent()
							.showErrorDlg(
									"[FS BRIDGE] "
											+ response
											+ "\n Please call the Help Desk Immediately. Press OK to continue");

				}

			} else {

				AppManager
						.getCurrent()
						.showErrorDlg(
								"[FS BRIDGE] : Unable to post SignOff transaction.Please call the Help Desk Immediately."
										+ " Press OK to continue\n");

			}

		}

	}

	public static void postARMSignOffTransaction() {
		
		ARMFSBridge fsBridge = new ARMFSBridge().build();
		if (fsBridge.isCountryAllowed) {

			Date now = new Date();
			String id = ARMEventIDGenerator.generate(now);
			ARMFSBOSignOffTransactionObject signOffTransaction = new ARMFSBOSignOffTransactionObject(
					MessageTypeCode.MTC_TRANSACTION, id,
					TransactionTypeCode.TTC_SIGN_OFF_TRANSACTION, now, id,
					new ConfigurationManager().getConfig(
							ARMFSBridgeObject.registerCfgFilePath, "STOREID")
							+ new ConfigurationManager().getConfig(
									ARMFSBridgeObject.registerCfgFilePath,
									"REGISTER_ID") + "001",
					"DIRETTORE DIRETTORE");

			if (fsBridge.postObject(signOffTransaction)) {

				String response = signOffTransaction.processResponse();
				if (response.contains("ERROR") || response.contains("UNABLE")) {
					AppManager
							.getCurrent()
							.showErrorDlg(
									"[FS BRIDGE] "
											+ response
											+ "\n Please call the Help Desk Immediately. Press OK to continue");

				}

			} else {

				AppManager
						.getCurrent()
						.showErrorDlg(
								"[FS BRIDGE] : Unable to post SignOff transaction.Please call the Help Desk Immediately."
										+ " Press OK to continue\n");

			}

		}

	}

	public void purge(String dirPath, long days, String fileExtension) {

		File folder = new File(dirPath);

		if (folder.exists()) {

			File[] listFiles = folder.listFiles();
			
			long eligibleForDeletion = System.currentTimeMillis()
					- (days * 24 * 60 * 60 * 1000L);

			for (File listFile : listFiles) {

				if (listFile.getName().endsWith(fileExtension)
						&& listFile.lastModified() < eligibleForDeletion) {

					if (!listFile.delete()) {

						System.out
								.println("[FISCAL SOLUTIONS BRIDGE] Unable to Purge FS Requests Files..");

					}
				}
			}
		}
	}

	public static void repostNotPostedRequests() {
		
		if(hasAlreadyReposted==false){
			
		System.out.println("");
		System.out.println("[FS BRIDGE] - * Reposting Not Posted Requests *");
		System.out.println("");
		String folder = new ConfigurationManager().getConfig(
				ARMFSBridgeObject.fsBridgeCfgFilePath,
				"FS_BRIDGE_NOT_POSTED_FOLDER");
		String store = new ConfigurationManager().getConfig(
				ARMFSBridgeObject.registerCfgFilePath, "STORE_ID");
		String register = new ConfigurationManager().getConfig(
				ARMFSBridgeObject.registerCfgFilePath, "REGISTER_ID");
		String taxIdentity = new ConfigurationManager().getConfig(
				ARMFSBridgeObject.fsBridgeCfgFilePath,
				"FS_BRIDGE_TAX_IDENTITY_NUMBER");

		File[] notPostedRequests = new File(
				new ConfigurationManager().getConfig(
						ARMFSBridgeObject.fsBridgeCfgFilePath,
						"FS_BRIDGE_NOT_POSTED_FOLDER")).listFiles();
		
		if(notPostedRequests.length>0){
			
			
		

		if (ARMFSBridge.S4FS == null) {
			ARMFSBridge.S4FS = new S4FiscalService();
			try {
				ARMFSBridge.S4FS.start(new ConfigurationManager().getConfig(
						ARMFSBridgeObject.fsBridgeCfgFilePath,
						"FS_BRIDGE_TAX_IDENTITY_NUMBER"),
						new ConfigurationManager().getConfig(
								ARMFSBridgeObject.registerCfgFilePath,
								"STORE_ID"), new ConfigurationManager()
								.getConfig(
										ARMFSBridgeObject.registerCfgFilePath,
										"REGISTER_ID"));
			} catch (S4FiscalServiceException e) {
				e.printStackTrace();
				return;

			}
		}
		
		for (File f : notPostedRequests) {

			System.out.println("[FISCAL SOLUTIONS BRIDGE] : Posting Object...");
			System.out.println("TaxIdentityNumber : " + taxIdentity);
			System.out.println("Store : " + store);
			System.out.println("Workstation : " + register);
			System.out.println();

			try {
				String content = FilesUtils
						.getNotPostedFileContent(f.getName());
				String response = ARMFSBridge.S4FS.process(content);
				
				System.out.println("Response :" + response);

				// Write response to xml file
				String ResponsesFolder = new ConfigurationManager().getConfig(
						ARMFSBridgeObject.fsBridgeCfgFilePath, "FS_BRIDGE_RESPONSES_FOLDER");
				// If the response folder does not exists, create it
				if (!new File(ResponsesFolder).exists()) {

					new File(ResponsesFolder.substring(0,
							ResponsesFolder.length() - 1)).mkdir();
				}
				File responseFile = new File(ResponsesFolder + f.getName());
				FilesUtils.stringToFile(responseFile.getPath(), response);
				
				
				processRepostResponse(f);
			} catch (S4FiscalServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		}
			hasAlreadyReposted = true;
		}
	}

	public static String processRepostResponse(File file) {

		String SignatureCode = "UNABLE_TO_RETRIEVE_SIGNATURE_CODE";
		String ResponseResultCodeTag = new ConfigurationManager().getConfig(
				ARMFSBridgeObject.fsBridgeCfgFilePath,
				"FS_BRIDGE_RESPONSE_RESULTCODE_TAG");

		String ResponseMessageSuccess = new ConfigurationManager().getConfig(
				ARMFSBridgeObject.fsBridgeCfgFilePath,
				"FS_BRIDGE_RESPONSE_SUCCESS");
		String ResponseMessageError = new ConfigurationManager().getConfig(
				ARMFSBridgeObject.fsBridgeCfgFilePath,
				"FS_BRIDGE_RESPONSE_ERROR");

		try {

			/**
			 * 
			 * Getting response fields mapping from configuration
			 * 
			 */
			// START
			// **************************************************************************************************************
			String ResponserFolder = new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath,
					"FS_BRIDGE_RESPONSES_FOLDER");

			String FiscalValueTag = new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath,
					"FS_BRIDGE_FISCALVALUE_TAG");
			String FiscalValueValueTag = new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath,
					"FS_BRIDGE_FISCALVALUE_VALUE_TAG");
			int FiscalValueIndex = Integer.valueOf(new ConfigurationManager()
					.getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath,
							"FS_BRIDGE_FISCALVALUE_VALUE_INDEX"));

			String ResponseStatusTag = new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath,
					"FS_BRIDGE_RESPONSE_STATUS_TAG");

			int ResponseResultCodeTagIndex = Integer
					.valueOf(new ConfigurationManager().getConfig(
							ARMFSBridgeObject.fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_RESULTCODE_TAG_INDEX"));

			String ResponseStatusCodeTag = new ConfigurationManager()
					.getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSCODE_TAG");
			int ResponseStatusCodeIndex = Integer
					.valueOf(new ConfigurationManager().getConfig(
							ARMFSBridgeObject.fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSCODE_TAG_INDEX"));

			String ResponseStatusCodeNumberTag = new ConfigurationManager()
					.getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSCODENUMBER_TAG");
			int ResponseStatusCodeNumberTagIndex = Integer
					.valueOf(new ConfigurationManager().getConfig(
							ARMFSBridgeObject.fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSCODENUMBER_TAG_INDEX"));

			String ResponseStatusDescriptionTag = new ConfigurationManager()
					.getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSDESCRIPTION_TAG");
			int ResponseStatusDescriptionTagIndex = Integer
					.valueOf(new ConfigurationManager().getConfig(
							ARMFSBridgeObject.fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSDESCRIPTION_TAG_INDEX"));

			String ResponseMessageTextTag = new ConfigurationManager()
					.getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_MESSAGETEXT_TAG");
			int ResponseMessageTextTagIndex = Integer
					.valueOf(new ConfigurationManager().getConfig(
							ARMFSBridgeObject.fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_MESSAGETEXT_TAG_INDEX"));

			// END
			// **************************************************************************************************************
			//
			//
			String responseFile = ResponserFolder + file.getName();

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(responseFile);

			NodeList elements;
			elements = doc.getElementsByTagName(ResponseStatusTag);

			String ResultCode = getString(ResponseResultCodeTag,
					(Element) elements.item(ResponseResultCodeTagIndex));

			// If ResultCode == Error
			if (ResultCode.equalsIgnoreCase(ResponseMessageError)) {

				// Retrieving error code number, description, message
				String StatusCode = getString(ResponseStatusCodeTag,
						(Element) elements.item(ResponseStatusCodeIndex));
				String StatusCodeNumber = getString(
						ResponseStatusCodeNumberTag,
						(Element) elements
								.item(ResponseStatusCodeNumberTagIndex));
				String StatusDescription = getString(
						ResponseStatusDescriptionTag,
						(Element) elements
								.item(ResponseStatusDescriptionTagIndex));
				String MessageLocalText = getString(ResponseMessageTextTag,
						(Element) elements.item(ResponseMessageTextTagIndex));

				SignatureCode = "FISCAL SOLUTIONS ERROR" + " ["
						+ StatusCodeNumber + "]. \n" + "Description : "
						+ StatusDescription + ". \n";

				String[] notPostedRequestIdentifiers = new ConfigurationManager()
						.getConfig(ARMFSBridgeObject.fsBridgeCfgFilePath,
								"FS_BRIDGE_NOT_POSTED_REQUEST_IDENTIFIERS")
						.split("\\s+");

				if (StringUtils.stringContainsItemFromList(StatusDescription,
						notPostedRequestIdentifiers)) {
						System.out.println("[FISCAL SOLUTIONS BRIDGE] !! Unable to repost request !!");
					
				}

				return SignatureCode;

			}

			// If ResultCode == Success
			if (ResultCode.equalsIgnoreCase(ResponseMessageSuccess)) {

				elements = doc.getElementsByTagName(FiscalValueTag);
				SignatureCode = getString(FiscalValueValueTag,
						(Element) elements.item(FiscalValueIndex));

				System.out
						.println("[FISCAL SOLUTIONS BRIDGE] : SignatureCode retrieved : "
								+ SignatureCode);
				
		        if(file.delete())
		        {
		            System.out.println("File deleted successfully");
		        }
				return SignatureCode;

			}

		} catch (ParserConfigurationException ex) {
			Logger.getLogger(ARMFSBridgeObject.class.getName()).log(
					Level.SEVERE, null, ex);

			return SignatureCode;
		} catch (SAXException ex) {
			Logger.getLogger(ARMFSBridgeObject.class.getName()).log(
					Level.SEVERE, null, ex);

			return SignatureCode;
		} catch (IOException ex) {
			Logger.getLogger(ARMFSBridgeObject.class.getName()).log(
					Level.SEVERE, null, ex);

			return SignatureCode;
		} catch (NullPointerException ex) {
			Logger.getLogger(ARMFSBridgeObject.class.getName()).log(
					Level.SEVERE, null, ex);

			return SignatureCode
					+ " No 'FiscalValue' found in xml response file. ";

		}

		return SignatureCode
				+ " - UNKNOWN EXCEPTION \n Check the response xml at key "
				+ ResponseResultCodeTag + "\n " + "It must be '"
				+ ResponseMessageSuccess + "' or '" + ResponseMessageError
				+ "'.\n" + "Otherwise check ARM FS Bridge Config File";

	}

	protected static String getString(String tagName, Element element) {
		NodeList list = element.getElementsByTagName(tagName);
		if (list != null && list.getLength() > 0) {
			NodeList subList = list.item(0).getChildNodes();

			if (subList != null && subList.getLength() > 0) {
				return subList.item(0).getNodeValue();
			}
		}

		return null;
	}

}
