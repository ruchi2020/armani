package com.chelseasystems.cs.ajbauthorization.bankcard;

import java.rmi.RemoteException;
import java.text.NumberFormat;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.IConfig;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.ajbauthorization.AJBServiceManager;
import com.chelseasystems.cs.authorization.bankcard.CMSCreditAuthServices;
import com.chelseasystems.cs.util.CreditAuthUtil;

/**
 * 
 * <p>
 * Title: CMSCreditAuthAJBServices
 * </p>
 * 
 * <p>
 * Description: This class specifies the AJB Service implementation of
 * CreditAuthServices.
 * </p>
 */

public class CMSCreditAuthAJBServices extends CMSCreditAuthServices implements
		IConfig {

	/* Test and debug features from configuration file */
	boolean localTestMode = false;
	boolean verboseMode = false;

	/* Reference variable to the configuration manager object */
	private ConfigMgr config;

	/* Indicates if data digits or characters */
	private static final int DIGITS = 1;
	private static final int CHARACTERS = 2;

	/**
	 * Constuctor for CreditAuthAJBServices
	 **/
	public CMSCreditAuthAJBServices() {
		config = new ConfigMgr("credit_auth.cfg");
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
	 * 
	 * @param requests
	 *            Object
	 * @return Object
	 */
	public Object getDCC(Object requests) {
		return null;
	}

	/**
	 * 
	 * @param requests
	 *            Object
	 * @return Object
	 */
	public Object setSignatureValidation(Object requests) {
		return null;
	}

	/**
	 * 
	 * @param requests
	 *            Object
	 * @return Object
	 */
	public Object setAuthCancelled(Object requests) {
		return null;
	}

	/**
	 * This method call AJBServiceManager for cleanup if the CreditAuthServer
	 * dies
	 */
	protected void finalize() throws Throwable {
		AJBServiceManager.getCurrent().finalize();
	}

	/**
	 * This is helper method for determining if the system is available to
	 * return to up-time mode.
	 **/
	public boolean ping() throws RemoteException {
		return true;
	}

	/**
	 * Method to check that the fields within the header are correct.
	 * 
	 * @param result
	 *            String containing header
	 */
	private void verifyResponseHeader(String result) {
		fieldOK(4, 0, 4, DIGITS, result, "Message Length");
		fieldOK(2, 4, 6, CHARACTERS, result, "Message Identification");
		fieldOK(16, 6, 22, CHARACTERS, result, "Journal Key");
		fieldOK(1, 22, 23, CHARACTERS, result, "Message Type");
		fieldOK(2, 23, 25, DIGITS, result, "Tender Type");
		fieldOK(19, 25, 44, DIGITS, result, "Account Number");
		fieldOK(11, 44, 55, DIGITS, result, "Authorized Amount");
		fieldOK(4, 55, 59, DIGITS, result, "Store Number");
		fieldOK(4, 59, 63, DIGITS, result, "Terminal Number");
		fieldOK(4, 63, 67, DIGITS, result, "Message Sequence");
		fieldOK(20, 67, 87, CHARACTERS, result, "User Data");
		fieldOK(8, 87, 95, DIGITS, result, "Auth Date YYYYMMDD");
		fieldOK(6, 95, 101, DIGITS, result, "Auth Time ");
		fieldOK(1, 101, 102, CHARACTERS, result, "Host Action Code");
		fieldOK(2, 102, 104, DIGITS, result, "Status Code 01/02/03/04/05");
		fieldOK(38, 104, 142, CHARACTERS, result, "Response Message");
		fieldOK(2, 142, 144, DIGITS, result, "Message Number");
		fieldOK(2, 144, 146, DIGITS, result, "Message Length");
		fieldOK(2, 146, 148, CHARACTERS, result, "Print Flag");
		fieldOK(1, 148, 149, CHARACTERS, result, "More Flag");
		fieldOK(6, 149, 155, CHARACTERS, result, "Authorization Code");
		fieldOK(4, 155, 159, CHARACTERS, result, "Authorization Response Code");
		fieldOK(4, 159, 163, CHARACTERS, result, "Response Reason");
		fieldOK(27, 163, 190, CHARACTERS, result, "Spaces/Filler");
	}

	/**
	 * Method which checks the fields within the credit card reply to determine
	 * if they are correct. Used for display output to GUI. To display output to
	 * log file, use verifyResponseCreditCardLog().
	 * 
	 * @param result
	 *            String containing credit reply
	 */
	private void verifyResponseCreditCard(String result) {
		System.out.println("\nVERIFYING CREDIT CARD REPLY");
		verifyResponseHeader(result);
		fieldOK(4, 190, 194, DIGITS, result, "Merchant Category Code");
		fieldOK(2, 194, 196, CHARACTERS, result, "POS Entry Mode");
		fieldOK(12, 196, 208, CHARACTERS, result, "Retrieval Reference Number");
		fieldOK(1, 208, 209, CHARACTERS, result, "Authorization Source");
		fieldOK(1, 209, 210, CHARACTERS, result, "Address Verification");
		fieldOK(1, 210, 211, CHARACTERS, result,
				"Card Type B-Bus, R-Corp, S-Purchasing");
		fieldOK(3, 211, 214, DIGITS, result, "Currency Code");
		fieldOK(1, 214, 215, CHARACTERS, result, "Payment Service Indicator");
		fieldOK(15, 215, 230, CHARACTERS, result,
				"Transaction Identifier/Bank Net Info");
		fieldOK(4, 230, 234, CHARACTERS, result, "Validation Code");
		fieldOK(6, 234, 240, CHARACTERS, result, "Local Trans Date YYMMDD");
		fieldOK(2, 240, 242, CHARACTERS, result, "Issuers Return Code");
		fieldOK(6, 242, 248, CHARACTERS, result,
				"Local Transaction Time 6 CHAR hhmmss");
		fieldOK(6, 248, 254, DIGITS, result,
				"System Trace Audit Number 6 DIGITS");
		fieldOK(65, 254, 319, CHARACTERS, result, "EBT Response Test");
	}

	/**
	 * Method which checks the fields within the credit card reply to determine
	 * if they are correct. Used for display output to GUI. To display output to
	 * log file, use verifyResponseCreditCardLog().
	 * 
	 * @param result
	 *            String containing credit reply
	 */
	private void verifyResponseCreditCardLog(String result) {
		// StringBuffer logString = new StringBuffer();
		// logString.append("\nVERIFYING CREDIT CARD REPLY");
		// verifyResponseHeader( result );
		fieldOK(4, 190, 194, DIGITS, result, "Merchant Category Code");
		fieldOK(2, 194, 196, CHARACTERS, result, "POS Entry Mode");
		fieldOK(12, 196, 208, CHARACTERS, result, "Retrieval Reference Number");
		fieldOK(1, 208, 209, CHARACTERS, result, "Authorization Source");
		fieldOK(1, 209, 210, CHARACTERS, result, "Address Verification");
		fieldOK(1, 210, 211, CHARACTERS, result,
				"Card Type B-Bus, R-Corp, S-Purchasing");
		fieldOK(3, 211, 214, DIGITS, result, "Currency Code");
		fieldOK(1, 214, 215, CHARACTERS, result, "Payment Service Indicator");
		fieldOK(15, 215, 230, CHARACTERS, result,
				"Transaction Identifier/Bank Net Info");
		fieldOK(4, 230, 234, CHARACTERS, result, "Validation Code");
		fieldOK(6, 234, 240, CHARACTERS, result, "Local Trans Date YYMMDD");
		fieldOK(2, 240, 242, CHARACTERS, result, "Issuers Return Code");
		fieldOK(6, 242, 248, CHARACTERS, result,
				"Local Transaction Time 6 CHAR hhmmss");
		fieldOK(6, 248, 254, DIGITS, result,
				"System Trace Audit Number 6 DIGITS");
		fieldOK(65, 254, 319, CHARACTERS, result, "EBT Response Test");
	}

	/**
	 * Method which checks the fields within the check reply to determine if
	 * they are correct.
	 * 
	 * @param result
	 *            String containing credit reply
	 */
	private void verifyResponseCheck(String result) {
		System.out.println("\nVERIFYING CHECK REPLY");
		verifyResponseHeader(result);
		fieldOK(76, 110, 186, CHARACTERS, result, "MICR Data 76 CHARS");
		fieldOK(1, 190, 191, CHARACTERS, result,
				"Check Truncation Indicator 1 CHAR");
		fieldOK(22, 191, 213, CHARACTERS, result, "Transaction ID 22 CHARS");
		fieldOK(5, 213, 218, CHARACTERS, result, "Service Charge 5 CHARS");
	}

	/**
	 * Method which checks the fields within the check request to determine if
	 * they are correct.
	 * 
	 * @param request
	 *            String containing credit request
	 */
	private void verifyRequestCheck(String request) {
		System.out.println("\nVERIFYING CHECK REquest");
		// verifyResponseHeader( result );
		fieldOK(76, 110, 186, CHARACTERS, request, "MICR Data 76 CHARS");
	}

	/**
	 * This method verifies if the length and data type are correct.
	 * 
	 * @param length
	 *            Length of field
	 * @param start
	 *            Starting location of field
	 * @param end
	 *            Ending location of field
	 * @param data_type
	 *            Type of data: DIGITS or CHARACTERS
	 * @param str
	 *            String containing field
	 * @param name
	 *            Name of field
	 * @return A boolean indicating whether field is correct
	 */
	private boolean fieldOK(int length, int start, int end, int data_type,
			String str, String name) {
		if ((str.length() < end) || ((end - start) != length)) {
			System.out.println("Field: " + name
					+ " length too short, length = " + str.length()
					+ " required length = " + length + " start = " + start
					+ " end = " + end);
			return false;
		}
		String field = str.substring(start, end);
		if (data_type == DIGITS)
			for (int i = 0; i < length; i++) {
				char ch = field.charAt(i);
				if (!Character.isDigit(ch)) {
					System.out.println("Field: " + name
							+ " Contains Non Digits" + " At index=" + i
							+ " char = " + ch + " Field value =" + field);
					return false;
				}
			}
		if (name.equalsIgnoreCase("Account Number")) {
			field = CreditAuthUtil.maskCreditCardNo(field);
		}
		System.out.println("Field: " + name + " Value is:>" + field + "< OK ");
		return true;
	}

	/**
	 * Method to create a test string used in testing only. Not needed in
	 * production.
	 * 
	 * @return result String containing header and credit card info
	 **/
	/**
	 * Changed the function to validate different String
	 * 
	 * @param i
	 *            double
	 * @return String
	 */
	private String createCCTestApproval(double i) {
		StringBuffer app = new StringBuffer();
		app.append("0494"); // Message Length
		app.append("CC"); // Message Identifier CC=credit card, CA=credit app,
							// CK=check
		app.append("1234567890123456"); // Journal Key
		app.append("0"); // Message Type0=credit auth P=payment
		app.append("03"); // Tender Type 03=cc 02=checks
		app.append("0004444333322221111"); // Account Number
		app.append("00000000390"); // Tender Amount
		app.append("1099"); // Store Number
		app.append("0033"); // Terminal Number
		app.append("0001"); // Message Sequence
		app.append("00000000000000000023"); // User Data
		app.append("20050325"); // auth date
		app.append("090345"); // auth time
		// app.append("0");// host action code
		System.out.println("******************** The value of i = " + i);
		// Status Code Based on the amount of money needed to be authorized
		if (i <= 101.00 || i >= 106.00) {
			app.append("A"); // A=approved, D=declined, O=other, N=other
			app.append("01"); // 01=Approved, 02=Declined, 03=Refer, 04=Timeout,
								// 05=System Failure
		}
		if (i == 102.00) {
			app.append("D"); // A=approved, D=declined, O=other, N=other
			app.append("02"); // 01=Approved, 02=Declined, 03=Refer, 04=Timeout,
								// 05=System Failure
			System.out.println("In createTestApproval" + app);
		}
		if (i == 103.00) {
			app.append("O"); // A=approved, D=declined, O=other, N=other
			app.append("03"); // 01=Approved, 02=Declined, 03=Refer, 04=Timeout,
								// 05=System Failure
		}
		if (i == 104.00) {
			app.append("O"); // A=approved, D=declined, O=other, N=other
			app.append("04"); // 01=Approved, 02=Declined, 03=Refer, 04=Timeout,
								// 05=System Failure
		}
		if (i == 105.00) {
			app.append("O"); // A=approved, D=declined, O=other, N=other
			app.append("05"); // 01=Approved, 02=Declined, 03=Refer, 04=Timeout,
								// 05=System Failure
		}
		// app.append("0");
		// app.append("04");
		app.append("12345678901234567890123456789012345678"); // Response
																// Message
		app.append("99"); // Message Number
		app.append("38"); // Message Length
		app.append("000"); // N/A
		app.append("456789"); // Auth Code
		app.append("DONE"); // Auth Response Code
		app.append("00000000000000000000"); // N/A
		app.append("00000000000"); // N/A
		app.append("1234"); // Merchant Category Code
		app.append("00"); // POS Entry Mode
		app.append("123456789012"); // Retrieval Reference Number
		app.append("0"); // Authorization Source
		app.append("0"); // Address Verification
		app.append("S"); // Card Type B-business, R-Corporate, S-Purchasig
		app.append("840"); // ArmCurrency Code
		app.append("0"); // Payment Service Indicator
		app.append("123456789012345"); // Transaction Identifier/Bank Net Info
		app.append("1234"); // Validation Code
		app.append("991225"); // Local Transaction Date YYMMDD
		app.append("01"); // Association Return code
		app.append("120101"); // Local Transaction time hhmmss
		app.append("123456"); // System Trace Audit Number
		app.append("1234567890123456789012345678901234567890123456789012345678790"); // 60
																						// //
																						// EBT
																						// Response
																						// Test
																						// 141
																						// Char
		app.append("1234567890123456789012345678901234567890123456789012345678790"); // 60
		app.append("123456789012345678901"); // 21
		app.append("12345678901234567"); // N/A
		app.append("01"); // Card Identifier
		// Filler
		app.append("00000000000000000000");
		app.append("00000000000000000000");
		app.append("00000000000000000000");
		app.append("000000000000000");
		// String str = new
		// String("9999CC12345678901234560031234567890123456789123456789011099003300001234567890123456789019991225123456A0112345678901234567890123456789012345678003800M6666669999123412345678901234567890123456712340012345678901200S8400123456789012345123499122501");
		return app.toString();
	}

	/**
	 * Method to create a test string used in testing only. Not needed in
	 * production.
	 * 
	 * @return result String containing header and check info
	 **/
	private String createCKTestApproval(double i) {
		StringBuffer app = new StringBuffer();
		app.append("0218"); // Length of the String.
		app.append("CK"); // CC=credit card, CA=credit app, CK=check
		app.append("1234567890123456"); // Journal Key
		app.append("0"); // 0=credit auth P=payment
		app.append("02"); // 03=cc 02=checks
		app.append("1234567890123456789"); // Account Number
		app.append("12345678901"); // Authorized Amount
		app.append("1099"); // store id
		app.append("0033"); // reg id
		app.append("0000");
		app.append("12345678901234567890");
		app.append("19991225"); // auth date
		app.append("123456");
		if (i <= 101.00 || i >= 106.00) {
			app.append("A"); // A=approved, D=declined, O=other, N=other
			app.append("01"); // 01=Approved, 02=Declined, 03=Refer, 04=Timeout,
								// 05=System Failure
		}
		if (i == 102.00) {
			app.append("D"); // A=approved, D=declined, O=other, N=other
			app.append("02"); // 01=Approved, 02=Declined, 03=Refer, 04=Timeout,
								// 05=System Failure
		}
		if (i == 103.00) {
			app.append("O"); // A=approved, D=declined, O=other, N=other
			app.append("03"); // 01=Approved, 02=Declined, 03=Refer, 04=Timeout,
								// 05=System Failure
		}
		if (i == 104.00) {
			app.append("O"); // A=approved, D=declined, O=other, N=other
			app.append("04"); // 01=Approved, 02=Declined, 03=Refer, 04=Timeout,
								// 05=System Failure
		}
		if (i == 105.00) {
			app.append("O"); // A=approved, D=declined, O=other, N=other
			app.append("05"); // 01=Approved, 02=Declined, 03=Refer, 04=Timeout,
								// 05=System Failure
		}
		app.append("12345678901234567890123456789012345678"); // Response
																// Message
		app.append("99"); // Message Number
		app.append("38"); // Message Length
		app.append("00"); // Print Flag 00-don't print
		app.append("0"); // Filler
		app.append("666666"); // auth code
		app.append("9999"); // response code
		app.append("0000"); // Response Reason
		app.append("123456789012345678901234567"); // filler
		app.append("N"); // Check Truncation Indicator
		app.append("1234567890123456789012"); // Transaction Id
		app.append("12345"); // Service Charge
		// app.append("12340012345678901200S8400123456789012345123499122501");
		// // rest
		// String str = new
		// String("9999CC12345678901234560031234567890123456789123456789011099003300001234567890123456789019991225123456A0112345678901234567890123456789012345678003800M6666669999123412345678901234567890123456712340012345678901200S8400123456789012345123499122501");
		return app.toString();
	}

	/**
	 * This method allows the configuration parameters to be changed.
	 * 
	 * @param aKey
	 *            An array of strings containing keys
	 */
	public void processConfigEvent(String[] aKey) {
		for (int x = 0; x < aKey.length; x++) {
			if (aKey[x].equalsIgnoreCase("LOCAL_TEST_MODE")) {
				String strLocalTestMode = config.getString("LOCAL_TEST_MODE");
				if (strLocalTestMode.equalsIgnoreCase("TRUE")) {
					localTestMode = true;
				} else {
					localTestMode = false;
				}
			}
			if (aKey[x].equalsIgnoreCase("VERBOSE_MODE")) {
				String strVerboseMode = config.getString("VERBOSE_MODE");
				if (strVerboseMode.equalsIgnoreCase("TRUE")) {
					verboseMode = true;
				} else {
					verboseMode = false;
				}
			}
		} // End of for (int x = 0; x < aKey.length; x++)
	} // End of public void processConfigEvent(String[] aKey)

	@Override
	public Object[] getAuth(Object[] arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
