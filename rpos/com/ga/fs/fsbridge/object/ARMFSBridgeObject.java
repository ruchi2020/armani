/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ga.fs.fsbridge.object;

import com.fiscalsolutions.s4fs.S4FiscalService;
import com.fiscalsolutions.s4fs.S4FiscalServiceException;
import com.ga.fs.fsbridge.ARMFSBridge;
import com.ga.fs.fsbridge.utils.ConfigurationManager;
import com.ga.fs.fsbridge.utils.FilesUtils;
import com.ga.fs.fsbridge.utils.StringUtils;
import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.CMSApplet;
import java.util.HashMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import static java.lang.System.out;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import sun.misc.IOUtils;

/**
 * 
 * @author Yves Agbessi
 */

/*
 * ARMFSBridgeObject
 */
public class ARMFSBridgeObject {

	public static String fsBridgeCfgFilePath = "../rpos/files/prod/config/FSBridge.cfg";
	public static String registerCfgFilePath = "../rpos/files/prod/config/register.cfg";

	public static String xmlFolder = new ConfigurationManager().getConfig(
			fsBridgeCfgFilePath, "FS_BRIDGE_XML_FOLDER");

	public static String TaxIdentityNumber;
	public static String StoreID;
	public static String WorkstationID;
	public static String Document;
	public static String MessageID;

	private static Document xmlDocument;

	public int type;
	
	private static String requestContent;
	
	// Seller
	public String SellerID;
	public String SellerDisplayName;
	public String SellerTaxNumber = " ";
	
	//Operator Tax Number 
	public String OperatorTaxNumber = " ";
	
	
	//Fiscal Data
	public String SIRET = new ConfigMgr("FSBridge.cfg").getString("SIRET");
	public String NAF = new ConfigMgr("FSBridge.cfg").getString("NAF");
	
	//XML FILE NAME
	public String XML_FILE_COMPOSITE_NAME;
	
	public static String POS_EVENT_TYPE; 
	
	public static String COPY_NUMBER = "";
	
	public static String COPY_DIGITAL_KEY = "";

	/**
	 * @return the TaxIdentityNumber
	 */
	public String getTaxIdentityNumber() {
		return TaxIdentityNumber;
	}

	/**
	 * @param TaxIdentityNumber
	 *            the TaxIdentityNumber to set
	 */
	public void setTaxIdentityNumber(String TaxIdentityNumber) {
		this.TaxIdentityNumber = TaxIdentityNumber;
	}

	/**
	 * @return the Store
	 */
	public String getStoreID() {
		return StoreID;
	}

	/**
	 * @param Store
	 *            the Store to set
	 */
	public void setStore(String Store) {
		this.StoreID = Store;
	}

	/**
	 * @return the WorkStation
	 */
	public String getWorkstationID() {
		return WorkstationID;
	}

	/**
	 * @param Workstation
	 *            the WorkStation to set
	 */
	public void setWorkStation(String Workstation) {
		this.WorkstationID = Workstation;
	}

	/**
	 * @return the Document
	 */
	public String getDocument() {
		return Document;
	}

	/**
	 * @param Document
	 *            the Document to set
	 */
	public void setDocument(String Document) {
		this.Document = Document;
	}

	/**
	 * @return the MessageID
	 */
	public String getMessageID() {
		return MessageID;
	}

	/**
	 * @param MessageID
	 *            the MessageID to set
	 */
	public void setMessageID(String MessageID) {
		this.MessageID = MessageID;
	}

	public static void setXmlDocument(Document xmlDocument) {
		ARMFSBridgeObject.xmlDocument = xmlDocument;
	}

	public static Document getXmlDocument() {
		return xmlDocument;
	}

	public ARMFSBridgeObject() {
		super();

	}

	public int getType() {

		return type;

	}

	public boolean generateXML() {

		return false;

	}

	public boolean post() {

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
				return false;

			}
		}
		// Print parameters
		System.out.println("[FISCAL SOLUTIONS BRIDGE] : Posting Object...");
		System.out
				.println("TaxIdentityNumber : " + this.getTaxIdentityNumber());
		System.out.println("Store : " + this.getStoreID());
		System.out.println("Workstation : " + this.getWorkstationID());
		System.out.println("Document : " + this.getDocument());
		System.out.println();

		File f = new File(this.getDocument());

		// Check if specified parameter is file
		if (!f.exists()) {
			System.out.println("File " + this.getDocument()
					+ " does not exist.");
			System.out.println();
			return false;
		}

		// Convert read data to string
		String content = ARMFSBridge.documentToString(xmlDocument);

		try {

			
			String response = ARMFSBridge.S4FS.process(content);
			
			//Saving the request file
			saveRequest(this,this.getMessageID(), content);
			setRequestContent(content);
			
			System.out.println("Response :" + response);

			// Write response to xml file
			String ResponsesFolder = new ConfigurationManager().getConfig(
					fsBridgeCfgFilePath, "FS_BRIDGE_RESPONSES_FOLDER");
			// If the response folder does not exists, create it
			if (!new File(ResponsesFolder).exists()) {

				new File(ResponsesFolder.substring(0,
						ResponsesFolder.length() - 1)).mkdir();
			}
			
			String pref = null;
			String objectClassName = this.getClass().getSimpleName();
			pref = objectClassName.substring(7,objectClassName.length()-"Object".length());
			String posEventType =  (this instanceof ARMFSBOPosEventObject) ? "_"+POS_EVENT_TYPE : "";
			
			XML_FILE_COMPOSITE_NAME = pref + this.getMessageID() + posEventType;
			File responseFile = new File(ResponsesFolder +  XML_FILE_COMPOSITE_NAME
					+ ".xml");
			FilesUtils.stringToFile(responseFile.getPath(), response);

			
			

		} catch (S4FiscalServiceException ex) {
			System.out.println("Error processing file " + this.getDocument()
					+ ", check log files.");
			ex.printStackTrace();
			return false;
		}

		return true;

	}

	public String processResponse() {
		
		if(this instanceof ARMFSBOPosEventObject || this instanceof ARMFSBOSignOnTransactionObject
				|| this instanceof ARMFSBOSignOffTransactionObject 
				|| this instanceof ARMFSBOCashInObject 
				|| this instanceof ARMFSBOCashOutObject){
			return "";
			
		}
		
		String SignatureCode = "UNABLE_TO_RETRIEVE_SIGNATURE_CODE";
		String ResponseResultCodeTag = new ConfigurationManager().getConfig(
				fsBridgeCfgFilePath, "FS_BRIDGE_RESPONSE_RESULTCODE_TAG");

		String ResponseMessageSuccess = new ConfigurationManager().getConfig(
				fsBridgeCfgFilePath, "FS_BRIDGE_RESPONSE_SUCCESS");
		String ResponseMessageError = new ConfigurationManager().getConfig(
				fsBridgeCfgFilePath, "FS_BRIDGE_RESPONSE_ERROR");

		try {

			/**
			 * 
			 * Getting response fields mapping from configuration
			 * 
			 */
			// START
			// **************************************************************************************************************
			String ResponserFolder = new ConfigurationManager().getConfig(
					fsBridgeCfgFilePath, "FS_BRIDGE_RESPONSES_FOLDER");

			String FiscalValueTag = new ConfigurationManager().getConfig(
					fsBridgeCfgFilePath, "FS_BRIDGE_FISCALVALUE_TAG");
			String FiscalValueValueTag = new ConfigurationManager().getConfig(
					fsBridgeCfgFilePath, "FS_BRIDGE_FISCALVALUE_VALUE_TAG");
			int FiscalValueIndex = Integer.valueOf(new ConfigurationManager()
					.getConfig(fsBridgeCfgFilePath,
							"FS_BRIDGE_FISCALVALUE_VALUE_INDEX"));

			String ResponseStatusTag = new ConfigurationManager().getConfig(
					fsBridgeCfgFilePath, "FS_BRIDGE_RESPONSE_STATUS_TAG");
			// int ResponseStatusTagIndex = Integer.valueOf(new
			// ConfigurationManager().getConfig(armCfgFilePath,
			// "FS_BRIDGE_RESPONSE_STATUS_TAG_INDEX"));

			int ResponseResultCodeTagIndex = Integer
					.valueOf(new ConfigurationManager().getConfig(
							fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_RESULTCODE_TAG_INDEX"));

			String ResponseStatusCodeTag = new ConfigurationManager()
					.getConfig(fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSCODE_TAG");
			int ResponseStatusCodeIndex = Integer
					.valueOf(new ConfigurationManager().getConfig(
							fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSCODE_TAG_INDEX"));

			String ResponseStatusCodeNumberTag = new ConfigurationManager()
					.getConfig(fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSCODENUMBER_TAG");
			int ResponseStatusCodeNumberTagIndex = Integer
					.valueOf(new ConfigurationManager().getConfig(
							fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSCODENUMBER_TAG_INDEX"));

			String ResponseStatusDescriptionTag = new ConfigurationManager()
					.getConfig(fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSDESCRIPTION_TAG");
			int ResponseStatusDescriptionTagIndex = Integer
					.valueOf(new ConfigurationManager().getConfig(
							fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_STATUSDESCRIPTION_TAG_INDEX"));

			String ResponseMessageTextTag = new ConfigurationManager()
					.getConfig(fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_MESSAGETEXT_TAG");
			int ResponseMessageTextTagIndex = Integer
					.valueOf(new ConfigurationManager().getConfig(
							fsBridgeCfgFilePath,
							"FS_BRIDGE_RESPONSE_MESSAGETEXT_TAG_INDEX"));

			// END
			// **************************************************************************************************************
			//
			//
			String responseFile = ResponserFolder + XML_FILE_COMPOSITE_NAME
					+ ".xml";

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
						"FS_BRIDGE_NOT_POSTED_REQUEST_IDENTIFIERS").split("\\s+");
				
				
				
				if(StringUtils.stringContainsItemFromList(StatusDescription, notPostedRequestIdentifiers)){
					
					System.out.println("Saving unposted request...");
					
					saveNotPostedRequest(this, this.getMessageID(), this.getRequestContent());
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
				
				if(this instanceof ARMFSBOReceiptCopyObject){
					NodeList copyelements;
					copyelements = doc.getElementsByTagName("TransactionInfo");
					
					COPY_NUMBER = getString("ReceiptCopySequence", (Element)(copyelements.item(0)));
					COPY_DIGITAL_KEY = SignatureCode;
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

	public Exception evaluateResponseString(String response) {

		if (response.contains("ERROR")) {

			return new Exception();

		}

		return null;

	}

	public static void main(String[] args) {

	}
	
	
	public static void saveRequest(ARMFSBridgeObject object, String name, String content){
		
		String RequestsFolder = new ConfigurationManager().getConfig(
				fsBridgeCfgFilePath, "FS_BRIDGE_REQUESTS_FOLDER");
		
		if (!new File(RequestsFolder).exists()) {

			new File(RequestsFolder.substring(0,
					RequestsFolder.length() - 1)).mkdir();
		}
		
		String pref = null;
		String objectClassName = object.getClass().getSimpleName();
		pref = objectClassName.substring(7,objectClassName.length()-"Object".length());
		String posEventType =  (object instanceof ARMFSBOPosEventObject) ? "_"+POS_EVENT_TYPE : "";
		
		File requestFile = new File( RequestsFolder + pref + name + posEventType
				+ ".xml");
		FilesUtils.stringToFile(requestFile.getPath(), content);
		
		
	}
	
	
	public static void saveNotPostedRequest(ARMFSBridgeObject object, String name, String content){
		
		String NotPostedFolder = new ConfigurationManager().getConfig(
				fsBridgeCfgFilePath, "FS_BRIDGE_NOT_POSTED_FOLDER");
		
		if (!new File(NotPostedFolder).exists()) {

			new File(NotPostedFolder.substring(0,
					NotPostedFolder.length() - 1)).mkdir();
		}
		
		String pref = null;
		String objectClassName = object.getClass().getSimpleName();
		pref = objectClassName.substring(7,objectClassName.length()-"Object".length());
		String posEventType =  (object instanceof ARMFSBOPosEventObject) ? "_"+POS_EVENT_TYPE : "";
			

		File requestFile = new File(NotPostedFolder + pref + name + posEventType
				+ ".xml");
		FilesUtils.stringToFile(requestFile.getPath(), content);
		
		
	}

	public static void setRequestContent(String requestContent) {
		ARMFSBridgeObject.requestContent = requestContent;
	}

	public static String getRequestContent() {
		return requestContent;
	}
	

	
	


}
