/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ga.fs.fsbridge.object;

import com.ga.fs.fsbridge.local.ARMFSBTransactionItem;
import com.ga.fs.fsbridge.local.ARMFSBTransactionPayment;
import com.ga.fs.fsbridge.utils.ConfigurationManager;
import com.ga.fs.fsbridge.utils.DateUtils;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;




/**
 *
 * @author Yves Agbessi
 */
public class ARMFSBOCashInObject extends ARMFSBridgeObject {



    /**
     * ************ HEADER ************
     */
    private int InterfaceVersion;
    private String ClientID;
    private String MessageTypeCode;
    private String MessageID;
    private Date MessageDateTime;
    private String CountryCode;
    private String ProjectCode;


    /**
     * ************ BODY ************
     */
    //TransactionInfo
    private String TransactionTypeCode;
    private Date TransactionDateTime;
    private String TransactionID;
    private boolean TrainingTransaction;
    
    private double TransactionTotalValue;


    //Operator
    private String OperatorID;
    private String OperatorDisplayName;
    
    
    private ConfigurationManager config = new ConfigurationManager();
    private final static String XML_FILE_PATH = xmlFolder+"CashIn.xml";

    
    
     /**
     * @return the InterfaceVersion
     */
    public int getInterfaceVersion() {
        return InterfaceVersion;
    }

    /**
     * @param InterfaceVersion the InterfaceVersion to set
     */
    public void setInterfaceVersion(int InterfaceVersion) {
        this.InterfaceVersion = InterfaceVersion;
    }

    /**
     * @return the ClientID
     */
    public String getClientID() {
        return ClientID;
    }

    /**
     * @param ClientID the ClientID to set
     */
    public void setClientID(String ClientID) {
        this.ClientID = ClientID;
    }

    /**
     * @return the MessageTypeCode
     */
    public String getMessageTypeCode() {
        return MessageTypeCode;
    }

    /**
     * @param MessageTypeCode the MessageTypeCode to set
     */
    public void setMessageTypeCode(String MessageTypeCode) {
        this.MessageTypeCode = MessageTypeCode;
    }

    /**
     * @return the MessageID
     */
    public String getMessageID() {
        return MessageID;
    }

    /**
     * @param MessageID the MessageID to set
     */
    public void setMessageID(String MessageID) {
        this.MessageID = MessageID;
    }

    /**
     * @return the MessageDateTime
     */
    public Date getMessageDateTime() {
        return MessageDateTime;
    }

    /**
     * @param MessageDateTime the MessageDateTime to set
     */
    public void setMessageDateTime(Date MessageDateTime) {
        this.MessageDateTime = MessageDateTime;
    }

    /**
     * @return the CountryCode
     */
    public String getCountryCode() {
        return CountryCode;
    }

    /**
     * @param CountryCode the CountryCode to set
     */
    public void setCountryCode(String CountryCode) {
        this.CountryCode = CountryCode;
    }

    /**
     * @return the ProjectCode
     */
    public String getProjectCode() {
        return ProjectCode;
    }

    /**
     * @param ProjectCode the ProjectCode to set
     */
    public void setProjectCode(String ProjectCode) {
        this.ProjectCode = ProjectCode;
    }

    /**
     * @return the TaxIdentityNumber
     */
    public String getTaxIdentityNumber() {
        return TaxIdentityNumber;
    }

    /**
     * @param TaxIdentityNumber the TaxIdentityNumber to set
     */
    public void setTaxIdentityNumber(String TaxIdentityNumber) {
        this.TaxIdentityNumber = TaxIdentityNumber;
    }

    /**
     * @return the StoreID
     */
    public String getStoreID() {
        return StoreID;
    }

    /**
     * @param StoreID the StoreID to set
     */
    public void setStoreID(String StoreID) {
        this.StoreID = StoreID;
    }

    /**
     * @return the WorkstationID
     */
    public String getWorkstationID() {
        return WorkstationID;
    }

    /**
     * @param WorkstationID the WorkstationID to set
     */
    public void setWorkstationID(String WorkstationID) {
        this.WorkstationID = WorkstationID;
    }

    /**
     * @return the TransactionTypeCode
     */
    public String getTransactionTypeCode() {
        return TransactionTypeCode;
    }

    /**
     * @param TransactionTypeCode the TransactionTypeCode to set
     */
    public void setTransactionTypeCode(String TransactionTypeCode) {
        this.TransactionTypeCode = TransactionTypeCode;
    }

    /**
     * @return the TransactionDateTime
     */
    public Date getTransactionDateTime() {
        return TransactionDateTime;
    }

    /**
     * @param TransactionDateTime the TransactionDateTime to set
     */
    public void setTransactionDateTime(Date TransactionDateTime) {
        this.TransactionDateTime = TransactionDateTime;
    }

    /**
     * @return the TransactionID
     */
    public String getTransactionID() {
        return TransactionID;
    }

    /**
     * @param TransactionID the TransactionID to set
     */
    public void setTransactionID(String TransactionID) {
        this.TransactionID = TransactionID;
    }

    /**
     * @return the TransactionTotalValue
     */
    public double getTransactionTotalValue() {
        return TransactionTotalValue;
    }

    /**
     * @param TransactionTotalValue the TransactionTotalValue to set
     */
    public void setTransactionTotalValue(double TransactionTotalValue) {
        this.TransactionTotalValue = TransactionTotalValue;
    }

    /**
     * @return the TrainingTransaction
     */
    public boolean isTrainingTransaction() {
        return TrainingTransaction;
    }

    /**
     * @param TrainingTransaction the TrainingTransaction to set
     */
    public void setTrainingTransaction(boolean TrainingTransaction) {
        this.TrainingTransaction = TrainingTransaction;
    }


    /**
     * @return the OperatorID
     */
    public String getOperatorID() {
        return OperatorID;
    }

    /**
     * @param OperatorID the OperatorID to set
     */
    public void setOperatorID(String OperatorID) {
        this.OperatorID = OperatorID;
    }

    /**
     * @return the OperatorDisplayName
     */
    public String getOperatorDisplayName() {
        return OperatorDisplayName;
    }

    /**
     * @param OperatorDisplayName the OperatorDisplayName to set
     */
    public void setOperatorDisplayName(String OperatorDisplayName) {
        this.OperatorDisplayName = OperatorDisplayName;
    }


    @Override
    public int getType() {

        return ARMFSBridgeObjectTypes.SALES_TRANSACTION;
    }
    

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //             
    //               C O N S T R U C T O R S   &   M E T H O D S
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public ARMFSBOCashInObject(CMSCompositePOSTransaction transaction) {

        
        this.Document = XML_FILE_PATH;
        //Getting 'InterfaceVersion' value from arm.cfg
        String interfaceVersionConfigValue = config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_INTERFACE_VERSION");
        this.InterfaceVersion = Integer.valueOf(interfaceVersionConfigValue);

        //Getting 'ClientID' value from register.cfg    
        String clientIDConfigValue = config.getConfig(registerCfgFilePath, "STORE_ID") + config.getConfig(registerCfgFilePath, "REGISTER_ID");
        this.ClientID = clientIDConfigValue;

        //Setting 'MessageTypeCode' value from available MessageTypeCodes
        this.MessageTypeCode = "Transaction";

        //MessageID = TransactionID as per internal policy
        this.MessageID = transaction.getId();
        //
        //Creating a new date for the 'MessageDateTime' value
        this.MessageDateTime = new Date();

        /**
         * Getting 'CountryCode' value from arm.cfg. This line of code retrieves
         * the LOCALE value and gets the substring starting from the 3rd
         * character of the string Example : LOCALE = it_IT CountryCode = IT
         */
        this.CountryCode = (config.getConfig(fsBridgeCfgFilePath, "LOCALE")).substring(3);

        //Getting 'ProjectCode' value from arm.cfg      
        this.ProjectCode = config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_PROJECT_CODE");

        //Getting 'TaxIdentityNumber' (Company VAT Code) value from arm.cfg     
        this.TaxIdentityNumber = config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_TAX_IDENTITY_NUMBER");

        //Getting 'StoreID' value from register.cfg  
        this.StoreID = config.getConfig(registerCfgFilePath, "STORE_ID");

        //Getting 'WorkstationID' value from register.cfg  
        this.WorkstationID = config.getConfig(registerCfgFilePath, "REGISTER_ID");

        this.TransactionTypeCode = com.ga.fs.fsbridge.utils.TransactionTypeCode.TTC_CASH_IN;;
        this.TransactionDateTime = transaction.getCreateDate();
        this.TransactionID = transaction.getId();
        this.TransactionTotalValue = transaction.getTotalPaymentAmount().doubleValue();
        
        //OperatorID
        this.OperatorID = transaction.getTheOperator().getExternalID();
        //OperatorDisplayName
        this.OperatorDisplayName = transaction.getTheOperator().getFirstName() + " " + transaction.getTheOperator().getLastName();

    }
    
        /**
         * 
         * 
         * @param 
         * 
         * 
         */
        
        public ARMFSBOCashInObject(String MessageTypeCode, String MessageID, String TransactionTypeCode, Date TransactionDateTime,
                                String TransactionID, String OperatorID, String OperatorDisplayName) {

        
            this.Document = XML_FILE_PATH;
        //Getting 'InterfaceVersion' value from arm.cfg
        String interfaceVersionConfigValue = config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_INTERFACE_VERSION");
        this.InterfaceVersion = Integer.valueOf(interfaceVersionConfigValue);

        //Getting 'ClientID' value from register.cfg    
        String clientIDConfigValue = config.getConfig(registerCfgFilePath, "STORE_ID") + config.getConfig(registerCfgFilePath, "REGISTER_ID");
        this.ClientID = clientIDConfigValue;

        //Setting 'MessageTypeCode' value from available MessageTypeCodes
        this.MessageTypeCode = MessageTypeCode;

        //MessageID
        this.MessageID = MessageID;
        //
        //Creating a new date for the 'MessageDateTime' value
        this.MessageDateTime = new Date();

        /**
         * Getting 'CountryCode' value from arm.cfg. This line of code retrieves
         * the LOCALE value and gets the substring starting from the 3rd
         * character of the string Example : LOCALE = it_IT CountryCode = IT
         */
        this.CountryCode = (config.getConfig(fsBridgeCfgFilePath, "LOCALE")).substring(3);

        //Getting 'ProjectCode' value from arm.cfg      
        this.ProjectCode = config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_PROJECT_CODE");

        //Getting 'TaxIdentityNumber' (Company VAT Code) value from arm.cfg     
        this.TaxIdentityNumber = config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_TAX_IDENTITY_NUMBER");

        //Getting 'StoreID' value from register.cfg  
        this.StoreID = config.getConfig(registerCfgFilePath, "STORE_ID");

        //Getting 'WorkstationID' value from register.cfg  
        this.WorkstationID = config.getConfig(registerCfgFilePath, "REGISTER_ID");

        this.TransactionTypeCode = TransactionTypeCode;
        this.TransactionDateTime = TransactionDateTime;
        this.TransactionID = TransactionID;
        
        //OperatorID
        this.OperatorID = OperatorID;
        //OperatorDisplayName
        this.OperatorDisplayName = OperatorDisplayName;

    }

    public ARMFSBOCashInObject(String MessageTypeCode, String MessageID, Date MessageDateTime, String TransactionTypeCode, 
            Date TransactionDateTime, String TransactionID, double TransactionTotalValue, String OperatorID, String OperatorDisplayName) {
        
    	this.Document = XML_FILE_PATH;
        this.InterfaceVersion = Integer.valueOf(config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_INTERFACE_VERSION"));  
        String clientIDConfigValue = config.getConfig(registerCfgFilePath, "STORE_ID") + config.getConfig(registerCfgFilePath, "REGISTER_ID");
        this.ClientID = clientIDConfigValue;
        this.MessageTypeCode = MessageTypeCode;
        this.MessageID = MessageID;
        this.MessageDateTime = MessageDateTime;
        this.CountryCode = (config.getConfig(fsBridgeCfgFilePath, "LOCALE")).substring(3);
        this.ProjectCode = config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_PROJECT_CODE");
        this.TaxIdentityNumber = config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_TAX_IDENTITY_NUMBER");
        this.StoreID = config.getConfig(registerCfgFilePath, "STORE_ID");
        this.WorkstationID = config.getConfig(registerCfgFilePath, "REGISTER_ID");;
        this.TransactionTypeCode = TransactionTypeCode;
        this.TransactionDateTime = TransactionDateTime;
        this.TransactionID = TransactionID;
        this.TransactionTotalValue = TransactionTotalValue;
        this.OperatorID = OperatorID;
        this.OperatorDisplayName = OperatorDisplayName;
        
        
        
    }   
        

   
    
    public ARMFSBOCashInObject() {
        
        this.InterfaceVersion = Integer.valueOf(config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_INTERFACE_VERSION"));  
        String clientIDConfigValue = config.getConfig(registerCfgFilePath, "STORE_ID") + config.getConfig(registerCfgFilePath, "REGISTER_ID");
        this.ClientID = clientIDConfigValue;
        this.MessageTypeCode = "Transaction";
        this.MessageID = "MESSAGE_ID_TEST";
        this.MessageDateTime = new Date();
        this.CountryCode = (config.getConfig(fsBridgeCfgFilePath, "LOCALE")).substring(3);
        this.ProjectCode = config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_PROJECT_CODE");
        this.TaxIdentityNumber = config.getConfig(fsBridgeCfgFilePath, "FS_BRIDGE_TAX_IDENTITY_NUMBER");
        this.StoreID = "1";
        this.WorkstationID = "1";
        this.TransactionTypeCode = "CashIn";
        this.TransactionDateTime = new Date();
        this.TransactionID = "TRANSACTION_ID_TEST";
        this.TransactionTotalValue = 100.0;
        
        this.OperatorID = "000401001";
        this.OperatorDisplayName = "YVES AGBESSI";
        
        
        
    }
    
    


    @Override
    public boolean generateXML() {
        System.out.println("[FISCAL SOLUTIONS BRIDGE] : Generating xml...");
        try {

            //---------------------------------------------------------------------------------
            //
            //Initializing xml document
            
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            /*
            ---------------------------------------------------------------------------------*/

            //Filling the document
            //Appending the default root (Message) element
            Element rootElement = doc.createElement("Message");
            doc.appendChild(rootElement);

            //Appending Request element
            Element Request = doc.createElement("Request");
            rootElement.appendChild(Request);

            /* * * * * H E A D E R * * * * */
            //--
            //Creating Header element
            Element Header = doc.createElement("Header");
            Request.appendChild(Header);
            //Creating Header's elements
            ArrayList<Element> headerElements = new ArrayList<Element>();

            Element InterfaceVersion = doc.createElement("InterfaceVersion");
            InterfaceVersion.appendChild(doc.createTextNode(String.valueOf(this.InterfaceVersion)));
            headerElements.add(InterfaceVersion);

            Element ClientID = doc.createElement("ClientID");
            ClientID.appendChild(doc.createTextNode(String.valueOf(this.ClientID)));
            headerElements.add(ClientID);

            Element MessageTypeCode = doc.createElement("MessageTypeCode");
            MessageTypeCode.appendChild(doc.createTextNode(String.valueOf(this.MessageTypeCode)));
            headerElements.add(MessageTypeCode);
            
            Element MessageID = doc.createElement("MessageID");
            MessageID.appendChild(doc.createTextNode(String.valueOf(this.MessageID)));
            headerElements.add(MessageID);
            
            Element MessageDateTime = doc.createElement("MessageDateTime");
            MessageDateTime.appendChild(doc.createTextNode(String.valueOf(DateUtils.formatDateForXML(this.MessageDateTime))));
            headerElements.add(MessageDateTime);
            
            Element CountryCode = doc.createElement("CountryCode");
            CountryCode.appendChild(doc.createTextNode(String.valueOf(this.CountryCode)));
            headerElements.add(CountryCode);
            
            Element ProjectCode = doc.createElement("ProjectCode");
            ProjectCode.appendChild(doc.createTextNode(String.valueOf(this.ProjectCode)));
            headerElements.add(ProjectCode);
            
            Element TaxIdentityNumber = doc.createElement("TaxIdentityNumber");
            TaxIdentityNumber.appendChild(doc.createTextNode(String.valueOf(this.TaxIdentityNumber)));
            headerElements.add(TaxIdentityNumber);
            
            Element StoreID = doc.createElement("StoreID");
            StoreID.appendChild(doc.createTextNode(String.valueOf(this.StoreID)));
            headerElements.add(StoreID);
            
            Element WorkstationID = doc.createElement("WorkstationID");
            WorkstationID.appendChild(doc.createTextNode(String.valueOf(this.WorkstationID)));
            headerElements.add(WorkstationID);

            //****** Appending Header's elements *******//
            
            for (Element e : headerElements) {

                Header.appendChild(e);
            }
            
            //--
            //--
            /* * * * * E N D   O F   H E A D E R * * * * */
            
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            
            /* * * * * B O D Y  * * * * */
            //--
            
            // Appending 'Body' element
            Element Body = doc.createElement("Body");
            Request.appendChild(Body);
            
            ArrayList<Element> bodyElements = new ArrayList<Element>();
            
            //Creating and appending 'TransactionInfo' element
            Element TransactionInfo = doc.createElement("TransactionInfo");
            Body.appendChild(TransactionInfo);
            bodyElements.add(TransactionInfo);
            
            Element TransactionTypeCode = doc.createElement("TransactionTypeCode");
            TransactionTypeCode.appendChild(doc.createTextNode(String.valueOf(this.TransactionTypeCode)));
            TransactionInfo.appendChild(TransactionTypeCode);
            
            Element TransactionDateTime = doc.createElement("TransactionDateTime");
            TransactionDateTime.appendChild(doc.createTextNode(String.valueOf(DateUtils.formatDateForXML(this.TransactionDateTime))));
            TransactionInfo.appendChild(TransactionDateTime);
            
            Element TransactionID = doc.createElement("TransactionID");
            TransactionID.appendChild(doc.createTextNode(String.valueOf(this.TransactionID)));
            TransactionInfo.appendChild(TransactionID);
            
            Element TransactionTotal = doc.createElement("TransactionTotal");
            bodyElements.add(TransactionTotal);
            
            Element TransactionTotalValue = doc.createElement("Amount");
            TransactionTotalValue.appendChild(doc.createTextNode(String.valueOf(this.TransactionTotalValue)));
            TransactionTotal.appendChild(TransactionTotalValue);
            
            /* Creating and appending 'Operator' element
             *
            -*/
            Element Operator = doc.createElement("Operator");
            Body.appendChild(Operator);
            bodyElements.add(Operator);  
            
            Element OperatorID = doc.createElement("OperatorID");
            OperatorID.appendChild(doc.createTextNode(String.valueOf(this.OperatorID)));
            Operator.appendChild(OperatorID);
            
            Element OperatorDisplayName = doc.createElement("OperatorDisplayName");
            OperatorDisplayName.appendChild(doc.createTextNode(String.valueOf(this.OperatorDisplayName)));
            Operator.appendChild(OperatorDisplayName);     
            
			Element OperatorTypeCode = doc.createElement("OperatorTypeCode");
			OperatorTypeCode.appendChild(doc.createTextNode(String
					.valueOf("Cashier")));
			Operator.appendChild(OperatorTypeCode);
            
            /* End of 'Operator' fields
             *
            -*/
            //---
            //--- 
			
			//FISCAL DATA			
			Element FiscalData = doc.createElement("FiscalData");
			Body.appendChild(FiscalData);
			bodyElements.add(FiscalData);
			
			//SIRET
			Element FiscalValue1 = doc.createElement("FiscalValue");
			FiscalData.appendChild(FiscalValue1);
			
			Element FiscalValueCode1 = doc.createElement("FiscalValueCode");
			FiscalValueCode1.appendChild(doc.createTextNode(String
					.valueOf("CustomerCustomerNumber")));
			FiscalValue1.appendChild(FiscalValueCode1);
			
			Element Value = doc.createElement("Value");
			Value.appendChild(doc.createTextNode(String
					.valueOf(this.SIRET)));
			FiscalValue1.appendChild(Value);
			
			//NAF
			Element FiscalValue2 = doc.createElement("FiscalValue");
			FiscalData.appendChild(FiscalValue2);
			
			Element FiscalValueCode2 = doc.createElement("FiscalValueCode");
			FiscalValueCode2.appendChild(doc.createTextNode(String
					.valueOf("CustomerCustomerCode")));
			FiscalValue2.appendChild(FiscalValueCode2);
			
			Element Value2 = doc.createElement("Value");
			Value2.appendChild(doc.createTextNode(String
					.valueOf(this.NAF)));
			FiscalValue2.appendChild(Value2);
            
            
            
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            
            //****** Appending Body's elements *******//
            
            for (Element e : bodyElements) {

                Body.appendChild(e);
            }
            
            //--
            //--
            /* * * * * E N D   O F   B O D Y  * * * * */        
            
            //--------------------------------------------------------------------------------------------------------//
            
            
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------//
            //--------------------------------------------------------------------------------------------------------// 
            
            

            //---------------------------------------------------------------------------------
            //Generating and saving xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(XML_FILE_PATH).getPath());
            transformer.transform(source, result);
            System.out.println("[FISCAL SOLUTIONS BRIDGE] : xml file generated. Available in " + XML_FILE_PATH);
            System.out.println("");
            setXmlDocument(doc);
            /*
            ---------------------------------------------------------------------------------*/

        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
            return false;
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ARMFSBOCashInObject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (TransformerException ex) {
            Logger.getLogger(ARMFSBOCashInObject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SecurityException ex) {
            Logger.getLogger(ARMFSBOCashInObject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ARMFSBOCashInObject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;

    }
    
    
    
    

    public static void main(String[] args) {
        
        ARMFSBOCashInObject CashIn = new ARMFSBOCashInObject();
       
            boolean generationResult = CashIn.generateXML();
            
            /*if (generationResult) {

                File file = new File(XML_FILE_PATH);
                Desktop desktop = Desktop.getDesktop();

                
                ARMFSBOSaleTransactionObject.post(saleTransaction.getTaxIdentityNumber(), saleTransaction.getStoreID(), 
                        saleTransaction.getWorkstationID(), XML_FILE_PATH, saleTransaction.getMessageID());
            
                
            }*/
            
            //processResponse();
            

    }
    
    
    
    

}
