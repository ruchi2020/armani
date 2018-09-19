/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.payment;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.authorization.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cs.ajbauthorization.AJBValidation;
import org.apache.commons.codec.binary.Base64;




/**
 *  The CreditCard class provides an abstraction to deal with the methods and
 *  variables for all credit cards as payment.
 *  @author John Gray
 *  @version 1.0a
 */
public class CreditCard extends Payment implements IAuthRequired, IRuleEngine
    , IAuthCancellationRequired, ISignatureValidatable {

	//Vivek Mishra : Added for capturing signature in 3-byte Ascii as received from AJB with Signature Capture 150 message response. 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3160216956596855098L;

	private String signatureAscii;
	
	private byte[] signByteCode;
	//Ends
	
	//Vivek Mishra : Added for capturing the unique AJBSequesnce number which goes with every request as Invoice number
	
	private String ajbSequence;
	
	//Vivek Mishra : Added to display signature on receipt. Used ImageIcon here as java.awt.BufferedImage is not serializable. 
	
	private ImageIcon signature = null;
	
	//Vivek Mishra : Added to capture the processed tender type from response field 6 IxDebitCredit.
	
	private String processedTyp = null;
	
	//Vishal Yevale 31 Aug 2016 = added to display EMV Fields on Receipt.
	
	private String AID;
	
	private String TVR;
	
	private String IAD;
	
	private String TSI;
	
	private String ARC;
	
	private String CVM;
	
		
  public String getCVM() {
		return CVM;
	}

	public void setCVM(String cVM) {
		CVM = cVM;
	}

public String getAID() {
		return AID;
	}

	public void setAID(String aID) {
		AID = aID;
	}

	public String getTVR() {
		return TVR;
	}

	public void setTVR(String tVR) {
		TVR = tVR;
	}

	public String getIAD() {
		return IAD;
	}

	public void setIAD(String iAD) {
		IAD = iAD;
	}

	public String getTSI() {
		return TSI;
	}

	public void setTSI(String tSI) {
		TSI = tSI;
	}

	public String getARC() {
		return ARC;
	}

	public void setARC(String aRC) {
		ARC = aRC;
	}
	
	//END Vishal Yevale 31 Aug 2016 : EMV fields on Receipt

/** This instance is used to create credit card validation strings */
  transient protected PaymentValidationRequests validationRequest = (PaymentValidationRequests)new
      ConfigMgr("credit_auth.cfg").getObject("VALIDATION_REQUESTS");

  // Start REQUEST fields
  // ISD HEADER field #1 - Message Length <1,4>
  /**
   *  The type of authorization request required by ISD.  Corresponds to string
   *  position <5,6> in the ISD request header with a character length of two.
   *  A default value should be set by the constructor that implements this
   *  class.  ISD Header Field #2
   *  CA = Credit Application
   *  CC = Credit Card
   *  CK = Check
   *  GR = Gift Certificate Redeem
   *  RX = Pharmacy
   */
  protected String messageIdentifier = "";
  // ISD HEADER field #3 - Journal Key <7,22>
  /**
   *  The message type required by the authorizor ISD.  Corresponds to string
   *  position <23> in the ISD request header with a character length of one.
   *  A default value should be set by the constructor that implements this class.
   *  ISD Header field #4.
   *  0 = Sale Authorization Request/Normal request for this message identifier
   *  4 = Credit Reversal (Void)
   *  5 = Credit echoed to POS and not passed on to authorizer
   *  I = Incremental Auth (Only used for hotel & transport)
   *  C = Return
   *  P = Payment on account
   *  R = Replace Dup. Journal Key Authorization
   */
  protected String messageType = "";
  /**
   *  The tender type required by the authorizor ISD.  Corresponds to string
   *  position <24,25> in the ISD request header with a character length of two.
   *  A default value should be set by the constructor that implements this class.
   *  ISD Header field #5.
   *  02 = Checks
   *  03 = Credit Card
   *  04 = GECC Payment on Account
   *  05 = Credit Application
   */
  protected String tenderType = "";
  /**
   *  The account number required by the authorizor ISD.  Corresponds to string
   *  position <26,44> in the ISD request header with a character length of 19.
   *  A default value should be set by the constructor that implements this class.
   *  ISD Header field #6.
   */
  protected String accountNumber = "";
  // ISD HEADER field #7 - Payment amount <45,55>
  // ISD HEADER field #8 - Store Number <56,59>
  // ISD HEADER field #9 - Terminal Number <60,63>
  // ISD HEADER field #10 - Message Sequence <64,67>
  // ISD HEADER field #11 - User Data <68,87>
  // ISD HEADER field #12 - Filler <88,110>
  // Position <111>  Begin of Credit Card data
  /**
   *  The reversal replacement amount required by the authorizor ISD.
   *  Corresponds to string position <111,122> in the ISD request header with
   *  a character length of twelve (12).  Dollar amount for credit.
   *  ISD credit card field #14.   Waiting to hear if I need.  alt
   */
  //protected ArmCurrency reversalReplacementAmount;
  // ISD credit card field #15 - System Trace Audit Number <123,128>
  // ISD credit card field #16 - Local Time (for Debit cards) <129,134>
  // ISD credit card field #17 - Local Transaction Date YYMMDD (for Debit cards) <135,140>
  /**
   *  The expiration date required by the authorizor ISD.  Corresponds to string
   *  position <141,144> in the ISD request header with a character length of
   *  four (4).  String sent in the format of YYMM.
   *  ISD credit card field #18.
   */
  protected Date expirationDate = null;
  /**
   *  The track number required by the authorizor ISD.  Corresponds to string
   *  position <145> in the ISD request header with a character length of
   *  one (1).  Acceptable values are 0=keyed, 1=Track One or 2=Track 2.
   *  ISD credit card field #19.
   */
  protected String trackNumber = "";
  // ISD credit card field #20 - Point of Sale Condition Code <146,147>
  /**
   *  The track data required by the authorizor ISD.  Corresponds to string
   *  position <148,226> in the ISD request header with a character length of
   *  seventy-nine (79).  If Track 1, must be terminated by a "?" if less than
   *  76 bytes and does not contain the begin and end sentinel or LRC.  If
   *  Track 2, must use either an equals sign "=" or apostrophe (') as the
   *  separator character and any unused bytes set to spaces.
   *  ISD credit card field #21.
   */
  protected String trackData = "";
  // ISD credit card field #22 - Original Retrieval Reference <227,238>
  // ISD credit card field #23 - Original Authorization Code <239,244>
  // ISD credit card field #24 - Account Data Source <245,246>
  // ISD credit card field #25 - ArmCurrency Code <247,249>
  // ISD credit card field #26 - Pin Data <250,265>
  // ISD credit card field #27 - AMEX Security Related Control Information CID <266,281> See the AMEX Object
  // ISD credit card field #28 - Additional Amounts (Cash Back) <282,293>
  // ISD credit card field #29 - Point of Sale Terminal Type <294>
  // ISD credit card field #30 - Point of Sale Terminal Entry <295>
  // ISD credit card field #31 - Original Payment Service Indicator <296>
  // ISD credit card field #32 - Original Transaction Identifier <297,311>
  // ISD credit card field #33 - Customer Zip Code <312,320>
  // ISD credit card field #34 - Customer Address <321,340>
  // ISD credit card field #35 - Point of Sale Pin Entry <341>
  // End of ISD credit card request
  // Start ISD Header RESPONSE fields
  /**
   *  The unique key that corresponds to this credit request.  Consists of 4 digit
   *  store number, max up to 4 digit register number, and a 12 digit time stamp.
   *  Corresponds to string position <68,87> in the ISD response header
   *  with a character length of twenty (20).  ISD Header response field "User Data" .
   */
  protected String respId = "";
  /**
   *  The authorizors approval date received from the approval string.
   *  Corresponds to string position <88,95> in the ISD response header
   *  with a character length of eight (8).  ISD Header response field "Filler".
   */
  protected Date approvalDate = new Date();
  /**
   *  The authorizors field Host Action Code received from the approval string.
   *  Corresponds to string position <102> in the ISD response header
   *  with a character length of one.   ISD Header response field #14.
   *  A = Approved
   *  D = Declined
   *  O = Other
   *  N = Other, not journaled
   */
  protected String respHostActionCode = "";
  /**
   *  The authorizors field Status Code received from the approval string.
   *  Corresponds to string position <103,104> in the ISD response header
   *  with a character length of two.  ISD Header response field #15.
   *  01 = Approved
   *  02 = Declined
   *  03 = Referral/Warning - call center
   *  04 = Timeout
   *  05 = System Failure
   */
  protected String respStatusCode = "";
  /**
   *  The authorizors Status Code Description used for GUI display.
   *  This field is set when the respStatusCode is set from the authorizor,
   *  therefore requires no set method.  Only valid responses are:
   *  Will return "Approved"  if respStatusCode = 01
   *  Will return "Declined" if respStatusCode = 02
   *  Will return "Call Center"  if respStatusCode = 03, 04, 05 or null
   */
  protected String respStatusCodeDesc = new String("CALL CENTER");
  /**
   *  The authorizors field Authorization Code received from the approval string.
   *  Corresponds to string position <150,155> in the ISD response header
   *  with a character length of six.
   *  This is the authorization code that will be saved with the transaction
   *  as the approval code.    ISD Header response field #21.
   */
  protected String respAuthorizationCode = "";
  /**
   *  The authorizors field Authorization Response Code received from the approval
   *  string. Corresponds to string position <156,159> in the ISD response
   *  header with a character length of four.   See GE Capital Corp. Point
   *  of Sale Credit Processing "Design Document" for a further explanation
   *  of these codes.    ISD Header response field #22.
   *  0000 = Transaction processed
   *  0001 = Previously declined 30 days                  respHostActionCode = 02
   *  0002 = Approved within 60 days (existing acct-call) respHostActionCode = 03
   *  0003 = Additional info needed                       respHostActionCode = 03
   *  0004 = Invalid packet number                        respHostActionCode = 05
   *  0005 = Approved more than 60 days (existing acct-call) respHostActionCode = 03
   *  0006 = Decline                                      respHostActionCode = 02
   *  0007 = Field edit errors
   *  0008 = Invalid client Id                            respHostActionCode = 05
   *  0009 = Approved with CSP insurance                  respHostActionCode = 01
   *  0010 = Approved without CSP insurance               respHostActionCode = 01
   *  0011 = GECC System network error - call             respHostActionCode = 01
   *  0012 = Application queued                           respHostActionCode = 04
   *  0013 = Invalid route code                           respHostActionCode = 05
   *  0014 = Account not found                            respHostActionCode = 03
   *  0015 = Cannot access account                        respHostActionCode = 03
   *  0016 = Credit Bureau processing - waiting           respHostActionCode = 03
   *  0017 = Authorized buyer error                       respHostActionCode = 02
   *  0018 = Invalid record length                        respHostActionCode = 05
   *  0019 = Invalid store number                         respHostActionCode = 05
   *  0020 = Account already processed                    respHostActionCode = 02
   *  0021 = Conditional approval                         respHostActionCode = 01
   *  0022 = Conditional approved with CSP insurance      respHostActionCode = 01
   *  0023 = Conditional approved without CSP insurance   respHostActionCode = 01
   *  0024 = Declined No-hit                              respHostActionCode = 02
   *  0025 = Invalid terminal Id                          respHostActionCode = 05
   */
  protected String respAuthorizationResponseCode = "";
  // Start ISD Credit Card RESPONSE fields (
  // Credit Card response fields start at position 191.  Only pulling the ones we need.
  /**
   *  The POS Entry Mode field required by the authorizor ISD.  Corresponds
   *  to string position <195,196> in the ISD CCard response with a character length
   *  of two (2).
   */
  protected String respPOSEntryMode = "";
  /**
   *  The Authorization Source field required by the authorizor ISD.  Corresponds
   *  to string position <209> in the ISD CCard response with a character length
   *  of one (1).  Specifies who authorized the transaction
   *  i.e. Issuing Bank vs Visa.
   */
  protected String respAuthorizationSource = "";
  /**
   *  The Address Verification field required by the authorizor ISD.  Corresponds
   *  to string position <210> in the ISD response with a character length
   *  of one.
   */
  protected String respAddressVerification = "";
  /**
   * The PS/2000 payment service indicator returned from authorizer.
   * Corresponds to string position <215> in the ISD CCard response with
   * a character length of one (1).
   */
  protected String respPaymentServiceIndicator = "";
  /**
   * The PS/2000 transaction identifier returned from the authorizer.
   * For Visa transactions, this field will contain the transaction
   * identifier.
   * Corresponds to string position <216,230> in the ISD CCard response with
   * a character length of fifteen (15).
   * For MasterCard transactions, this field will contain:
   * 4 byte bank net date,
   * 9 byte bank net reference number,
   * 2 byte spaces
   */
  protected String respTransactionIdentifier = "";
  /**
   * The PS/2000 validation code returned from the authorizer.  Corresponds
   * to string position <231,234> in the ISD CCard response with a character
   * length of four (4).
   * For MasterCard transactions, this field will contain:
   * 1 byte CVC Indicator (Y or space),
   * 1 byte Entry Indicator (Y or space),
   * 1 byte Strip Quality (A - J),
   * 1 byte space
   */
  protected String respValidationCode = "";

  /** The name of the person the card is issued to. */
  protected String creditCardHolderName = "";

  /** The name of the company who issued the card.  */
  protected String creditCardCompanyName = "";
  //Anjana added for credit card entry mode
    protected String luNmbCrdSwpKy = "";
//ends
    //Anjana added for masking card number
    protected String maskCardNum= "";
    //Ends
/** Variable for determining if credit card requires authorization */
  public boolean authRequired = true;

  /** Variable for determining if manual override was selected in authorization */
  public boolean manualOverride = false;

  /** Digital Signature */
  protected DigitalSignature digitalSignature = new DigitalSignature();
  protected boolean isManuallyKeyed;

  /** the security code on the back of the card **/
  protected String cv2SecurityCode = "";
  protected Boolean processAsDCC;
  protected Hashtable dccInformation;
  protected boolean isSignatureValidationRequired = true;
  protected boolean isSignatureValid;
  protected boolean isReturnWithReceipt = false;

  private String errordiscription; 
  protected boolean partialAuth = false;
 
 



/**
   * New Field Added
   */
  protected String cardIdentifier;
  private String zipCode;

  private String sCardPlanCode;
  private String sCardPlanDesc;
  
  //Vivek Mishra : Added for token number in AJB-POS integration
  private String tokenNo; 
  //Ends
  /**
   * put your documentation comment here
   */
  protected CreditCard() {
    this(null);
//    sCardPlanCode = new String("");
//    sCardPlanDesc = new String("");
  }

  /**
   * @param transactionPaymentName a key representing this payment
   */
  public CreditCard(String transactionPaymentName) {
    super(transactionPaymentName);
//    sCardPlanCode = new String("");
  }

  /**
   * put your documentation comment here
   * @param isManuallyKeyed
   */
  public void setManuallyKeyed(boolean isManuallyKeyed) {
    this.isManuallyKeyed = isManuallyKeyed;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isManuallyKeyed() {
    return isManuallyKeyed;
  }

  /**
   * put your documentation comment here
   * @param processAsDCC
   */
  public void setProcessAsDCC(boolean processAsDCC) {
    this.processAsDCC = new Boolean(processAsDCC);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Boolean isProcessAsDCC() {
    return processAsDCC;
  }

  /**
   * put your documentation comment here
   * @param cv2SecurityCode
   */
  public void setCv2SecurityCode(String cv2SecurityCode) {
    this.cv2SecurityCode = cv2SecurityCode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCv2SecurityCode() {
    return cv2SecurityCode;
  }

  /**
   *
   * @return String
   */
  public String getCardIdentifier() {
    return cardIdentifier;
  }

  /**
   *
   * @param cardIdentifier
   */
  public void setCardIdentifier(String cardIdentifier) {
    this.cardIdentifier = cardIdentifier;
  }

  /**
   * Tests if the payment requires authorization by a third party.
   * @return true if the payment requires authorization by a third party
   */
  public boolean isAuthRequired() {
    return (authRequired);
  }

  /**
   * Tests if the payment requires the customer to sign a receipt.
   * @return true if the payment requires the customer to sign a receipt
   */
  public boolean isSignatureRequired() {
    return (true);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isSignatureValidationRequired() {
    return isSignatureValidationRequired;
  }

  /**
   * put your documentation comment here
   * @param isSignatureValidationRequired
   */
  public void setSignatureValidationRequired(boolean isSignatureValidationRequired) {
    this.isSignatureValidationRequired = isSignatureValidationRequired;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isSignatureValid() {
    System.out.println("\n In new CC\n");
    return isSignatureValid;
  }

  /**
   * put your documentation comment here
   * @param isSignatureValid
   */
  public void setSignatureValid(boolean isSignatureValid) {
    setSignatureValidationRequired(false);
    this.isSignatureValid = isSignatureValid;
  }

  /**
   * Tests if the payment is valid for more than the current amount due.
   * @return true if the payment is valid for more than the current amount due
   */
  public boolean isOverPaymentAllowed() {
    return (false);
  }

  /**
   * Tests if the payment requires the cash register to print on the back of
   * the document submitted for payment.
   * @return true if the payment requires the cash register to print on the
   * back of the document submitted for payment
   */
  public boolean isFrankingRequired() {
    return (false);
  }

  // Start Methods
  /**
   *  Returns the message identifier field required by ISD credit request.
   *  Corresponds to string position <5,6> in the ISD request header with
   *  a character length of two.
   *  A default value should be set by the constructor that implements this class.
   *  CA = Credit Application
   *  CC = Credit Card
   *  CK = Check
   *  GR = Gift Certificate Redeem
   *  RX = Pharmacy
   */
  public String getMessageIdentifier() {
    return (messageIdentifier);
  }

  /**
   *  Sets the message identifier field required by ISD.  Corresponds to string
   *  position <5,6> in the ISD request header with a character length of two.
   *  A default value should be set by the constructor that implements this class.
   *  CA = Credit Application
   *  CC = Credit Card
   *  CK = Check
   *  GR = Gift Certificate Redeem
   *  RX = Pharmacy
   */
  public void setMessageIdentifier(String messageIdentifier) {
    this.messageIdentifier = messageIdentifier;
  }

  /**
   *  Gets the message type field required by ISD.  Corresponds to string position
   *  <23> in the ISD request header with a character length of one.  A
   *  default value should be set by the constructor that implements this class.
   *  0 = Sale Authorization Request/Normal request for this message identifier
   *  4 = Credit Reversal (Void)
   *  5 = Credit echoed to POS and not passed on to authorizer
   *  I = Incremental Auth (Only used for hotel & transport)
   *  C = Return
   *  P = Payment on account
   *  R = Replace Dup. Journal Key Authorization
   */
  public String getMessageType() {
    return (messageType);
  }

  /**
   *  Sets the message type field required by ISD.  Corresponds to string position
   *  <23> in the ISD request header with a character length of one.  A
   *  default value should be set by the constructor that implements this class.
   *  0 = Sale Authorization Request/Normal request for this message identifier
   *  4 = Credit Reversal (Void)
   *  5 = Credit echoed to POS and not passed on to authorizer
   *  I = Incremental Auth (Only used for hotel & transport)
   *  C = Return
   *  P = Payment on account
   *  R = Replace Dup. Journal Key Authorization
   */
  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  /**
   *  Gets the tender type field required by ISD.  Corresponds to string position
   *  <24,25> in the ISD request header with a character length of two.  A
   *  default value should be set by the constructor that implements this class.
   *  02 = Checks
   *  03 = Credit Card
   *  04 = GECC Payment on Account
   *  05 = Credit Application
   */
  public String getTenderType() {
    return (tenderType);
  }

  /**
   *  Sets the tender type field required by ISD.  Corresponds to string
   *  position <24,25> in the ISD request header with a character length of two.
   *  A default value should be set by the constructor that implements this class.
   *  02 = Checks
   *  03 = Credit Card
   *  04 = GECC Payment on Account
   *  05 = Credit Application
   */
  public void setTenderType(String tenderType) {
    this.tenderType = tenderType;
  }

  /**
   * Returns the credit card account number.
   * @return the credit card account number
   */
  public String getAccountNumber() {
    return (accountNumber);
  }

  /**
   * Sets the credit card account number.
   * @param accountNum the credit card account number
   */
  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  /**
   * Returns the credit card expiration date.
   * @return the credit card expiration date
   */
  public Date getExpirationDate() {
    return (expirationDate);
  }

  /**
   * Sets the credit card expiration date.
   * @param expireDate the credit card expiration date
   */
  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }

  /**  Gets the track number required by the authorizor ISD.  Corresponds to string
   *  position <145> in the ISD request header with a character length of
   *  one (1).  Acceptable values are 0=keyed, 1=Track One or 2=Track 2.
   *  ISD credit card field #19.
   */
  public String getTrackNumber() {
    return (trackNumber);
  }

  /**
   *  Sets the track number required by the authorizor ISD.  Corresponds to string
   *  position <145> in the ISD request header with a character length of
   *  one (1).  Acceptable values are 0=keyed, 1=Track One or 2=Track 2.
   *  ISD credit card field #19.
   */
  public void setTrackNumber(String trackNumber) {
    this.trackNumber = trackNumber;
  }

  /**
   *  Gets the track data required by the authorizor ISD.  Corresponds to string
   *  position <148,226> in the ISD request header with a character length of
   *  seventy-nine (79).  If Track 1, must be terminated by a "?" if less than
   *  76 bytes and does not contain the begin and end sentinel or LRC.  If
   *  Track 2, must use either an equals sign "=" or apostrophe (') as the
   *  separator character and any unused bytes set to spaces.
   *  ISD credit card field #21.
   */
  public String getTrackData() {
    return (trackData);
  }

  /**
   *  Sets the track data required by the authorizor ISD.  Corresponds to string
   *  position <148,226> in the ISD request header with a character length of
   *  seventy-nine (79).  If Track 1, must be terminated by a "?" if less than
   *  76 bytes and does not contain the begin and end sentinel or LRC.  If
   *  Track 2, must use either an equals sign "=" or apostrophe (') as the
   *  separator character and any unused bytes set to spaces.
   *  ISD credit card field #21.
   */
  public void setTrackData(String trackData) {
    this.trackData = trackData;
  }

  /**
   * Returns the unique Id for this credit request.
   * @return the unique Id for this credit request.
   */
  public String getRespId() {
    return (respId);
  }

  /**
   * Sets the unique Id for this credit request.
   * @param respId the unique Id for this credit request.
   */
  public void setRespId(String respId) {
    this.respId = respId;
  }

  /**
   * Returns the approval date provided by the payment guaranteer.
   * @return the approval date provided by the payment guaranteer
   */
  public Date getApprovalDate() {
    return (approvalDate);
  }

  /**
   * Sets the approval date from the payment guaranteer, in the format MMDDYY.
   * @param approvalDate the approval date from the payment guaranteer
   */
  public void setApprovalDate(Date approvalDate) {
    this.approvalDate = approvalDate;
  }

  /**
   *  Gets the ISD field Host Action Code received from the approval string.
   *  Corresponds to string position <102> in the ISD response header
   *  with a character length of one.
   *  A = Approved
   *  D = Declined
   *  O = Other
   *  N = Other, not journaled
   */
  public String getRespHostActionCode() {
    return (respHostActionCode);
  }

  /**
   *  Sets the ISD field Host Action Code received from the approval string.
   *  Corresponds to string position <102> in the ISD response header
   *  with a character length of one.
   *  A = Approved
   *  D = Declined
   *  O = Other
   *  N = Other, not journaled
   */
  public void setRespHostActionCode(String respHostActionCode) {
    this.respHostActionCode = respHostActionCode;
  }

  /**
   *  Gets the ISD field Status Code received from the approval string.
   *  Corresponds to string position <103,104> in the ISD response header
   *  with a character length of two.
   *  01 = Approved                          GECC_APPROVED
   *  02 = Declined                          GECC_DECLINED
   *  03 = Referral/Warning - call center    GECC_REFERRED
   *  04 = Timeout                           GECC_TIMEOUT
   *  05 = System Failure                    GECC_FAILURE
   */
  public String getRespStatusCode() {
    return (respStatusCode);
  }

  /**
   *  Sets the ISD field Status Code received from the approval string.
   *  Corresponds to string position <103,104> in the ISD response header
   *  with a character length of two.
   *  01 = Approved                          GECC_APPROVED
   *  02 = Declined                          GECC_DECLINED
   *  03 = Referral/Warning - call center    GECC_REFERRED
   *  04 = Timeout                           GECC_TIMEOUT
   *  05 = System Failure                    GECC_FAILURE
   *  10 = Partial Auth(1940)
   *
   */
  public void setRespStatusCode(String respStatusCode) {
    this.respStatusCode = respStatusCode;
    // If not an approval, set to false
    //if ( !( respStatusCode.equals("01") ) ) { super.setIsApproved( false ); }
    // Set the Description for the status.  Display the auth code if approved.
    //if ( respStatusCode.equals("01") ) { this.respStatusCodeDesc = this.respAuthorizationCode; }
    //if ( respStatusCode.equals("02") ) { this.respStatusCodeDesc = "CALL CENTER" ; }
    //if ( respStatusCode.equals("03") ) { this.respStatusCodeDesc = "CALL CENTER" ; }
    //if ( respStatusCode.equals("04") ) { this.respStatusCodeDesc = "CALL CENTER" ; }
    //if ( respStatusCode.equals("05") ) { this.respStatusCodeDesc = "CALL CENTER" ; }
  }

  /**  Gets the authorizors Status Code Description used for GUI display.
   *  This field is set when the respStatusCode is set from the authorizor,
   *  therefore requires no set method.  Only valid responses are:
   *  Will return "Approved"  if respStatusCode = 01
   *  Will return "Declined" if respStatusCode = 02
   *  Will return "Call Center"  if respStatusCode = 03, 04, 05 or null
   *  1940 "Partial auth" if respStatusCode = 01"
   *  AJB CODES
   *  0 = approved
	1 = declined
	2 = Call Bank (referral)
	3 = Bank link down
	6 = Format Error
	8 = Try again (try later)
	10 = Time Out

   */
  public String getRespStatusCodeDesc() {
    // Set the Description for the status.  Display the auth code if approved.
 if (respStatusCode.equals("0")) {
      respStatusCodeDesc = this.respAuthorizationCode;
    } else if (respStatusCode.equals("1")) {
      respStatusCodeDesc = "DECLINED";
    }
//Vivek Mishra : Added response code 3 entry after discussion with Armani and AJB as this response code will also be eligible for SAF : 28-SEP-2016
    else if (respStatusCode.equals("2") || respStatusCode.equals("3")) {
      respStatusCodeDesc = new String(PaymentMgr.getDefaultCallCenterDisplay(
          getTransactionPaymentName()));
    }
      else if (respStatusCode.equals("5")) {
          respStatusCodeDesc = new String(PaymentMgr.getDefaultCallCenterDisplay(
              getTransactionPaymentName()));
    } else if (respStatusCode.equals("6")) {
        respStatusCodeDesc = "DECLINED";
    } else if (respStatusCode.equals("8")) {
      respStatusCodeDesc = "TRY LATER";//put dialogue for cashier --> try later on click of OK it will go on payment screen 
      }
    //timeout
    else if(respStatusCode.equals("10")){
    	respStatusCodeDesc = new String(PaymentMgr.getDefaultCallCenterDisplay(
  	          getTransactionPaymentName()));   // Manually authorize if time out happens	
    }//Vivek Mishra : Added for new return flow
    else if (respStatusCode.equals("99")) {
        respStatusCodeDesc = "NOT REFUNDED";
    }//Ends
    else {
    	//Vivek Mishra : Added for displaying Default call center number in case of AJB offline scenario.	  
      //respStatusCodeDesc = "PENDING POST";
    	respStatusCodeDesc = new String(PaymentMgr.getDefaultCallCenterDisplay(
    	          getTransactionPaymentName()));
    	//Ends
    }
    return (respStatusCodeDesc);
  }

  /**
   *  Gets the ISD field Authorization Code received from the approval string.
   *  Corresponds to string position <150,155> in the ISD response header
   *  with a character length of six.
   *  This is the authorization code that will be saved with the transaction
   *  as the approval code.
   */
  public String getRespAuthorizationCode() {
    return (respAuthorizationCode);
  }

  /**
   * This method set the DigitalSignature for this credit card
   */
  public void setDigitalSignature(DigitalSignature signature) {
    this.digitalSignature = signature;
  }

  /**
   *  This method return the digital signature of this credit card
   */
  public DigitalSignature getDigitalSignature() {
    return (this.digitalSignature);
  }

  /**
   *  Sets the ISD field Authorization Code received from the approval string.
   *  Corresponds to string position <150,155> in the ISD response header
   *  with a character length of six.
   *  This is the authorization code that will be saved with the transaction
   *  as the approval code.
   */
  public void setRespAuthorizationCode(String respAuthorizationCode) {
    this.respAuthorizationCode = respAuthorizationCode;
  }

  /**
   *  Sets the ISD field Authorization Response Code received from the approval
   *  string. Corresponds to string position <156,159> in the ISD response
   *  header with a character length of four.   See GE Capital Corp. Point
   *  of Sale Credit Processing "Design Document" for a further explanation
   *  of these codes.
   *  0000 = Transaction processed
   *  0001 = Previously declined 30 days                  respHostActionCode = 02
   *  0002 = Approved within 60 days (existing acct-call) respHostActionCode = 03
   *  0003 = Additional info needed                       respHostActionCode = 03
   *  0004 = Invalid packet number                        respHostActionCode = 05
   *  0005 = Approved more than 60 days (existing acct-call) respHostActionCode = 03
   *  0006 = Decline                                      respHostActionCode = 02
   *  0007 = Field edit errors
   *  0008 = Invalid client Id                            respHostActionCode = 05
   *  0009 = Approved with CSP insurance                  respHostActionCode = 01
   *  0010 = Approved without CSP insurance               respHostActionCode = 01
   *  0011 = GECC System network error - call             respHostActionCode = 01
   *  0012 = Application queued                           respHostActionCode = 04
   *  0013 = Invalid route code                           respHostActionCode = 05
   *  0014 = Account not found                            respHostActionCode = 03
   *  0015 = Cannot access account                        respHostActionCode = 03
   *  0016 = Credit Bureau processing - waiting           respHostActionCode = 03
   *  0017 = Authorized buyer error                       respHostActionCode = 02
   *  0018 = Invalid record length                        respHostActionCode = 05
   *  0019 = Invalid store number                         respHostActionCode = 05
   *  0020 = Account already processed                    respHostActionCode = 02
   *  0021 = Conditional approval                         respHostActionCode = 01
   *  0022 = Conditional approved with CSP insurance      respHostActionCode = 01
   *  0023 = Conditional approved without CSP insurance   respHostActionCode = 01
   *  0024 = Declined No-hit                              respHostActionCode = 02
   *  0025 = Invalid terminal Id                          respHostActionCode = 05
   */
  public String getRespAuthorizationResponseCode() {
    return (respAuthorizationResponseCode);
  }

  /**
   *  Gets the ISD field Authorization Response Code received from the approval
   *  string. Corresponds to string position <156,159> in the ISD response
   *  header with a character length of four.   See GE Capital Corp. Point
   *  of Sale Credit Processing "Design Document" for a further explanation
   *  of these codes.
   *  0000 = Transaction processed
   *  0001 = Previously declined 30 days                  respHostActionCode = 02
   *  0002 = Approved within 60 days (existing acct-call) respHostActionCode = 03
   *  0003 = Additional info needed                       respHostActionCode = 03
   *  0004 = Invalid packet number                        respHostActionCode = 05
   *  0005 = Approved more than 60 days (existing acct-call) respHostActionCode = 03
   *  0006 = Decline                                      respHostActionCode = 02
   *  0007 = Field edit errors
   *  0008 = Invalid client Id                            respHostActionCode = 05
   *  0009 = Approved with CSP insurance                  respHostActionCode = 01
   *  0010 = Approved without CSP insurance               respHostActionCode = 01
   *  0011 = GECC System network error - call             respHostActionCode = 01
   *  0012 = Application queued                           respHostActionCode = 04
   *  0013 = Invalid route code                           respHostActionCode = 05
   *  0014 = Account not found                            respHostActionCode = 03
   *  0015 = Cannot access account                        respHostActionCode = 03
   *  0016 = Credit Bureau processing - waiting           respHostActionCode = 03
   *  0017 = Authorized buyer error                       respHostActionCode = 02
   *  0018 = Invalid record length                        respHostActionCode = 05
   *  0019 = Invalid store number                         respHostActionCode = 05
   *  0020 = Account already processed                    respHostActionCode = 02
   *  0021 = Conditional approval                         respHostActionCode = 01
   *  0022 = Conditional approved with CSP insurance      respHostActionCode = 01
   *  0023 = Conditional approved without CSP insurance   respHostActionCode = 01
   *  0024 = Declined No-hit                              respHostActionCode = 02
   *  0025 = Invalid terminal Id                          respHostActionCode = 05
   */
  public void setRespAuthorizationResponseCode(String respAuthorizationResponseCode) {
    this.respAuthorizationResponseCode = respAuthorizationResponseCode;
  }

  /**
   * Returns POS Entry Mode
   * @return POS Entry Mode
   */
  public String getRespPOSEntryMode() {
    return (respPOSEntryMode);
  }

  /**
   * Sets the POS Entry Mode
   * @param respPOSEntryMode the POS Entry Mode
   */
  public void setRespPOSEntryMode(String respPOSEntryMode) {
    this.respPOSEntryMode = respPOSEntryMode;
  }

  /**
   * Returns who authorized the transaction (issuing bank vs Visa).
   * @return who authorized the transaction (issuing bank vs Visa)
   */
  public String getRespAuthorizationSource() {
    return (respAuthorizationSource);
  }

  /**
   * Sets who authorized the transaction, (issuing bank vs Visa).
   * @param authSourceCode who authorized the transaction
   */
  public void setRespAuthorizationSource(String respAuthorizationSource) {
    this.respAuthorizationSource = respAuthorizationSource;
  }

  /**
   * Returns the address verification code.
   * @return the address verification code
   */
  public String getRespAddressVerification() {
    return (respAddressVerification);
  }

  /**
   * Sets the address verification code.
   * @param addressVerificationCode AVS code returned from the authorizer
   */
  public void setRespAddressVerification(String respAddressVerification) {
    this.respAddressVerification = respAddressVerification;
  }

  /**
   * Returns the PS/2000 payment service indicator provided by the authorizer.
   * @return the PS/2000 payment service indicator provided by the authorizer
   */
  public String getRespPaymentServiceIndicator() {
    return (respPaymentServiceIndicator);
  }

  /**
   * Sets the PS/2000 payment service indicator provided by the authorizer.
   * @param authPmtSvcIndicator the payment service indicator
   */
  public void setRespPaymentServiceIndicator(String respPaymentServiceIndicator) {
    this.respPaymentServiceIndicator = respPaymentServiceIndicator;
  }

  /**
   * Returns the PS/2000 transaction identifier returned from the authorizer.
   * @return the PS/2000 transaction identifier returned from the authorizer
   */
  public String getRespTransactionIdentifier() {
    return (respTransactionIdentifier);
  }

  /**
   * Sets the PS/2000 transaction identifier returned from the authorizer.
   * @param transId the PS/2000 transaction id returned from the authorizer
   */
  public void setRespTransactionIdentifier(String respTransactionIdentifier) {
    this.respTransactionIdentifier = respTransactionIdentifier;
  }

  /**
   * Returns the PS/2000 validation code provided by the authorizer.
   * @return the PS/2000 validation code provided by the authorizer
   */
  public String getRespValidationCode() {
    return (respValidationCode);
  }

  /**
   * Sets the PS/2000 validation code returned from the authorizer.
   * @param authValidationCode the PS/2000 validation code from the authorizer
   */
  public void setRespValidationCode(String respValidationCode) {
    this.respValidationCode = respValidationCode;
  }

  /**
   * Returns the name of the person on the credit card.
   * @return the name of the person on the credit card
   */
  public String getCreditCardHolderName() {
    return (creditCardHolderName);
  }

  /**
   * Sets the name of the person the card is issued to.
   * @param creditCardHolderName the name of the person the card is issued to
   */
  public void setCreditCardHolderName(String creditCardHolderName) {
    this.creditCardHolderName = creditCardHolderName;
  }

  /**
   * Returns the name of the company that issued the credit card.
   * @return the name of the company that issued the credit card
   */
  public String getCreditCardCompanyName() {
    return (creditCardCompanyName);
  }

  /**
   * Sets the name of the company that issued the credit card.
   * @param creditCardCompanyName the name of the credit card company
   */
  public void setCreditCardCompanyName(String creditCardCompanyName) {
    this.creditCardCompanyName = creditCardCompanyName;
  }

  /**
   * Sets the payment to true if approval is needed, false otherwise.
   * This method is used to set the payment to false after an approval is obtained
   * from the credit authorizor in order for that payment not to be re-submitted
   * in case of problems.
   * @param isAuthRequired a boolean of true if the payment requires
   * authorization, false otherwise.
   */
  public void setAuthRequired(boolean isAuthRequired) {
    this.authRequired = isAuthRequired;
  }

  /**
   * Returns true if the credit card authorization code was entered manually, false otherwise.
   * @return boolean of true if the credit card authorization code was entered manually, false otherwise.
   */
  public boolean getManualOverride() {
    return (manualOverride);
  }

  /**
   * Sets the manual override information for a credit card
   * @param the approval code received from the call to the authorizor
   */
  public void setManualOverride(String approvalCode) {
    this.manualOverride = true;
  //  this.respStatusCode = "01";
    //Anjana added the proper approval code
    this.respStatusCode = "0";
    // Display the approval code instead of the word "APPROVED"
    this.respStatusCodeDesc = approvalCode;
    this.respAuthorizationCode = approvalCode;
    this.authRequired = false;
    //super.setIsApproved( true );
  }

  /**
   * This method returns a string containing a credit card
   * validation request.
   * @param   store     Store Number
   * @param   terminal  Terminal Number
   * @return            String containing validation request
   */
  public Object getValidationRequest(String store, String terminal) {
    // If at all we need to make changes to the what type of method is
    // called, based on the type of the cc, amex or any other card...
    // call the method from here.
    return (validationRequest.getCreditCardValidationRequest(this, store, terminal));
  }
 
 /*Vivek Mishra : Added for generating refund request in AJB flow*/  
  /**
   * This method returns a string containing a credit card
   * validation request.
   * @param   store     Store Number
   * @param   terminal  Terminal Number
   * @param   isRefundPaymentRequired  isRefundPaymentRequired boolean
   * @return            String containing validation request
   */
   //Added arguement by Anjana for manual oveerride
  public Object getValidationRequest(String store, String terminal, boolean isRefundPaymentRequired, boolean isManualOverride) {
    // If at all we need to make changes to the what type of method is
    // called, based on the type of the cc, amex or any other card...
    // call the method from here.
	  if(validationRequest==null){
		  validationRequest = (PaymentValidationRequests)new
			      ConfigMgr("credit_auth.cfg").getObject("VALIDATION_REQUESTS");
	  }
    return (((AJBValidation)validationRequest).getCreditCardValidationRequest(this, store, terminal, isRefundPaymentRequired, isManualOverride));
  }
  

 public Object getSAFValidationRequest(String store, String terminal, boolean isRefundPaymentRequired, boolean isManualOverride) {
	  if(validationRequest==null){
		  validationRequest = (PaymentValidationRequests)new
			      ConfigMgr("credit_auth.cfg").getObject("VALIDATION_REQUESTS");
	  }
   return (((AJBValidation)validationRequest).getCreditCardSAFValidationRequest(this, store, terminal, isRefundPaymentRequired, isManualOverride));
 }

 
	/* mayuri edhara : for timeout scenarios.
	 **/
	 public Object getTimeoutValidationReversalRequest() {
		  if(validationRequest==null){
			  validationRequest = (PaymentValidationRequests)new
				      ConfigMgr("credit_auth.cfg").getObject("VALIDATION_REQUESTS");
		  }
	  return (((AJBValidation)validationRequest).getCreditCardTimeoutValidationReversalRequest(this));
	}
/*End*/  
  /**
   * This method sets authorization based upon the
   * authorization responce.
   * @param responce  - Contains the authorization responce
   * @return          - String containing the authorizationStatusCode
   */
  public String setCreditAuthorization(Object response) {
	validationRequest.setCreditCardAuthorization(response, this);
    return (respStatusCode);
  }

  /**
   * This method returns an object containing a credit card
   * dcc request.
   * @param   store     Store Number
   * @param   terminal  Terminal Number
   * @return            String containing validation request
   */
  public Object getDCCRequest(String store, String terminal) {
    return (validationRequest.getDCCRequest(this, store, terminal));
  }

  /**
   * This method sets the DCC information
   *
   * @param responce  - Contains the authorization responce
   */
  public void setDCCRequest(Object response) {
    validationRequest.setDCC(response, this);
  }

  /**
   * This method returns an object containing a credit card
   * dcc request.
   * @param   store     Store Number
   * @param   terminal  Terminal Number
   * @return            String containing validation request
   */
  public Object getSignatureValidationRequest(String store, String terminal) {
    return (validationRequest.getSignatureValidationRequest(this, store, terminal));
  }

  /**
   * This method sets the signature validation response
   *
   * @param responce  - Contains the authorization responce
   */
  public void setSignatureValidation(Object response) {
    validationRequest.setSignatureValidation(response, this);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isCancellationRequired() {
    return (this.getRespStatusCode().length() > 0);
  }

  /**
   * put your documentation comment here
   * @param store
   * @param terminal
   * @return
   */
  public Object getCancellationRequest(String store, String terminal) {
    return (validationRequest.getCancelRequest(this, store, terminal));
  }
  
  /*Vivek Mishra : Added for AJB VoidSale flow*/
  
  /**
   * put your documentation comment here
   * @param store
   * @param terminal
   * @return
   */
  public Object getCancellationRequest(String store, String terminal, CreditCard cc, String orgTxnNo, boolean isVoidRefund, boolean isManualOverride) {
	  PaymentValidationRequests validationRequest = (PaymentValidationRequests)new
      ConfigMgr("credit_auth.cfg").getObject("VALIDATION_REQUESTS");
	  return (((AJBValidation)validationRequest).getCancelRequest(cc, store, terminal, orgTxnNo, isVoidRefund, isManualOverride));
  }
  
  public Object getSAFCancellationRequest(String store, String terminal, CreditCard cc, String orgTxnNo, boolean isVoidRefund, boolean isManualOverride) {
	  PaymentValidationRequests validationRequest = (PaymentValidationRequests)new
      ConfigMgr("credit_auth.cfg").getObject("VALIDATION_REQUESTS");
	  return (((AJBValidation)validationRequest).getSAFCancelRequest(cc, store, terminal, orgTxnNo, isVoidRefund, isManualOverride));
  }
  

  /**
   * put your documentation comment here
   * @param response
   */
  public void setCancellationRequest(Object response) {
    validationRequest.setAuthCancelled(response, this);
  }

  /**
   * put your documentation comment here
   * @param dccInformation
   */
  public void setDCCInformation(Hashtable dccInformation) {
    this.dccInformation = dccInformation;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getDCCInformation() {
    return dccInformation;
  }

  /**
   *
   * @return String
   */
  public String getZipCode() {
    return this.zipCode;
  }

  /**
   *
   * @param zipCode String
   */
  public void setZipCode(String zipCode) {
    doSetZipCode(zipCode);
  }

  /**
   *
   * @param zipCode String
   */
  public void doSetZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  /**
   * This is used to store
   * Credit card plan code.
   * @param sCode String
   */
  public void setCardPlanCode(String sCode)
  {
    if(sCode==null || sCode.trim().length()<1 || sCode.indexOf("null")!=-1) return;
    doSetCardPlanCode(sCode);
  }

  /**
   * Set CreditCard Plan code.
   * @param sCode String
   */
  public void doSetCardPlanCode(String sCode) {
    this.sCardPlanCode = sCode;
  }

  /**
   * Get CreditCard plan code.
   * @return String
   */
  public String getCardPlanCode()
  {
    return this.sCardPlanCode;
  }


  /**
   * This is used to store
   * Credit card plan code.
   * @param sCode String
   */
  public void setGUICardPlanDesc(String sCardPlanDesc)
  {
    if(sCardPlanDesc==null || sCardPlanDesc.trim().length()<1 || sCardPlanDesc.indexOf("null")!=-1) return;
    this.sCardPlanDesc = sCardPlanDesc;
  }


  /**
   * Get CreditCard plan code.
   * @return String
   */
  public String getGUICardPlanDesc()
  {
    return this.sCardPlanDesc;
  }

  //Vivek Mishra : Added for capturing signature in 3-byte Ascii as received from AJB with Signature Capture 150 message response. 
  
  public String getSignatureAscii() 
  {
	return signatureAscii;
  }
 
  public void setSignatureAscii(String signatureAscii) 
  {
	this.signatureAscii = signatureAscii;
  }

  public byte[] getSignByteCode() 
  {
	return signByteCode;
  }

  public void setSignByteCode(byte[] signByteCode) 
  {
	this.signByteCode = signByteCode;
  }

  //End
  
  //Anjana added for card entry mode
  public String getLuNmbCrdSwpKy() {
		return luNmbCrdSwpKy;
	}

	public void setLuNmbCrdSwpKy(String luNmbCrdSwpKy) {
		this.luNmbCrdSwpKy = luNmbCrdSwpKy;
	}
	
	//Vivek Mishra : Added for token number in AJB-POS integration

	public String getTokenNo() {
		return tokenNo;
	}

	public void setTokenNo(String tokenNo) {
		this.tokenNo = tokenNo;
	}
	
	//Ends
	  //Anjana added making card number
	  public String getMaskCardNum() {
			return maskCardNum;
		}

		public void setMaskCardNum(String maskCardNum) {
			this.maskCardNum = maskCardNum;
		}

	//Vivek Mishra : Added for capturing the unique AJBSequesnce number which goes with every request as Invoice number
		public String getAjbSequence() {
			return ajbSequence;
		}

		public void setAjbSequence(String ajbSequence) {
			this.ajbSequence = ajbSequence;
		}
	//Ends		
		//Vivek Mishra : Added to capture the processed tender type from response field 6 IxDebitCredit.	
		public String getProcessedTyp() {
			return processedTyp;
		}

		public void setProcessedTyp(String processedTyp) {
			this.processedTyp = processedTyp;
		}
		
	//Ends	
	//Vivek Mishra : Added for showing Signature on file on receipt.	
		    public String getSignatureStaus() {
				    String signStaus = "";
				    if (this.getSignByteCode()!=null) {
				    	signStaus = "Signature on file.";
				    }
				      return signStaus;
				  }
	//Ends		  
    public boolean isReturnWithReceipt() {
    	return isReturnWithReceipt;
    }

    public void setReturnWithReceipt(boolean isReturnWithReceipt) {
    	this.isReturnWithReceipt = isReturnWithReceipt;
    }

	public String getErrordiscription() {
		return errordiscription;
	}

	public void setErrordiscription(String errordiscription) {
		this.errordiscription = errordiscription;
	}
	
	public boolean isPartialAuth() {
		return partialAuth;
	}

	public void setPartialAuth(boolean partialAuth) {
		this.partialAuth = partialAuth;
	}
	

	
}

