/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.util;

import com.chelseasystems.cr.config.*;
import java.io.*;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import java.util.Locale;
import java.util.StringTokenizer;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentResponse;

/**
 * <p>Title:FiscalDocumentUtil </p>
 * <p>Description:Utility to read/write FiscalDocument number to Master register </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 06-15-2005 | Manpreet  | N/A       | POS_104665_TS_FiscalDocuments_Rev0                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class FiscalDocumentUtil {
	/**
	 * FiscalFileLocation
	 */
	private static String FISCAL_FILE_LOCATION;

	/**
	 * Fiscal File Name
	 */

	private static ConfigMgr configFiscalDoc = new ConfigMgr("fiscal_document.cfg");

	private final static String FISCAL_FILE = "/FiscalDocumentNumber.ser";

	/**
	 * Backup File Name
	 */
	private final static String BACKUP_FILE = "/FiscalDocumentNumber.bkup";

	/**
	 * Bad File Name
	 */
	private final static String BAD_FILE = "/FiscalDocumentNumber.bad";

	/**
	 * Document path string
	 */
	private final static String FISCAL_DOCUMENT_PATH = "FISCAL_DOCUMENT_PATH";

	/**
	 * DDT type
	 */
	private final static int FISCAL_TYPE_DDT = 1;

	/**
	 * VAT type
	 */
	private final static int FISCAL_TYPE_VAT = 2;

	/**
	 * Credit Note Type
	 */
	private final static int FISCAL_TYPE_CREDITNOTE = 3;
	private boolean bIsACreditNoteCountry = false;

	private static String FISCAL_ISO_COUNTRY_LIST = null;
	// Sergio
	private static String FISCAL_STATE = null;
	private int responseCode = -1;

	private static int MAX_RETRIES = 0;
	private static int MAX_WAIT = 0;

	static {
		// Sergio
		// FISCAL_ISO_COUNTRY_LIST = configFiscalDoc.getString("FISCAL_ISO_COUNTRY_LIST");
		FISCAL_STATE = configFiscalDoc.getString("FISCAL_STATE");
		if ((configFiscalDoc.getInteger("MAX_RETRIES")) != null) {
			MAX_RETRIES = (configFiscalDoc.getInteger("MAX_RETRIES")).intValue();
		}
		if ((configFiscalDoc.getInteger("MAX_WAIT")) != null) {
			MAX_WAIT = (configFiscalDoc.getInteger("MAX_WAIT")).intValue();
		}
	}

	public FiscalDocumentUtil() {
		CMSStore cmsStore = (CMSStore) com.chelseasystems.cr.appmgr.AppManager.getInstance().getGlobalObject("STORE");
		if (cmsStore != null) {
			String sCountry = cmsStore.getPreferredISOCountry();
			String sState = cmsStore.getState();
			// if (FISCAL_ISO_COUNTRY_LIST != null) {
			if (FISCAL_STATE != null) {
				// String sTmp = new String(FISCAL_ISO_COUNTRY_LIST);
				String sTmp = new String(FISCAL_STATE);
				StringTokenizer sTokens = new StringTokenizer(sTmp, ",");
				while(sTokens.hasMoreTokens()) {
					sTmp = sTokens.nextToken();
					if (sTmp != null) {
						// if (sTmp.indexOf(sCountry) != -1 || sTmp.equalsIgnoreCase(sCountry)) {
						if (sTmp.indexOf(sState) != -1 || sTmp.equalsIgnoreCase(sState)) {
							bIsACreditNoteCountry = true;
						}
					}
				}
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public boolean isCreditNoteCountry() {
		return bIsACreditNoteCountry;
	}

	/**
	 * Get Next DDT Number without updating FiscalFile
	 * @return String
	 */
	public String getAvailableDDTNumber() {
		try {
			return readWriteFiscalNumber(false, FISCAL_TYPE_DDT);
		} catch (Exception ex) {
			return "-1";
		}

	}

	/**
	 * Get Next DDT Number and update FiscalFile
	 * @return String
	 */
	public String getNextDDTNumber() {
		try {
			return readWriteFiscalNumber(true, FISCAL_TYPE_DDT);
		} catch (Exception ex) {
			return "-1";
		}

	}

	/**
	 * Get Next VAT Number without updating FiscalFile
	 * @return String
	 */
	public String getAvailableVATNumber() {
		try {
			System.out.println("getAvailableVATNumber ");
			return readWriteFiscalNumber(false, FISCAL_TYPE_VAT);
		} catch (Exception ex) {
			return "-1";
		}

	}

	/**
	 * Get Next VAT Number and update FiscalFile
	 * 
	 * @return String
	 */
	public String getNextVATNumber() {
		try {
			System.out.println("getNextVATNumber ");
			return readWriteFiscalNumber(true, FISCAL_TYPE_VAT);
		} catch (Exception ex) {
			return "-1";
		}

	}

	/**
	 * Get Next CreditNote Number without updating FiscalFile
	 * 
	 * @return String
	 */
	public String getAvailableCreditNoteNumber() {
		try {
			return readWriteFiscalNumber(false, FISCAL_TYPE_CREDITNOTE);
		} catch (Exception ex) {
			return "-1";
		}

	}

	/**
	 * Get Next CreditNote Number and update FiscalFile
	 * @return String
	 */
	public String getNextCreditNoteNumber() {
		try {
			return readWriteFiscalNumber(true, FISCAL_TYPE_CREDITNOTE);
		} catch (Exception ex) {
			return "-1";
		}

	}

	/**
	 * Set new DDT number in Fiscal File.
	 * @param sDDTNumber
	 *            String
	 * @return boolean
	 */
	public boolean setNextDDTNumber(String sDDTNumber) {
		return updateFiscalNumber(Long.parseLong(sDDTNumber), FISCAL_TYPE_DDT);
	}

	/**
	 * Set new VAT number in Fiscal File.
	 * @param sDDTNumber
	 *            String
	 * @return boolean
	 */
	public boolean setNextVATNumber(String sDDTNumber) {
		return updateFiscalNumber(Long.parseLong(sDDTNumber), FISCAL_TYPE_VAT);
	}

	/**
	 * Set new CreditNote Number in Fiscal File
	 * @param sNumber
	 *            String
	 * @return boolean
	 */
	public boolean setNextCreditNoteNumber(String sNumber) {
		return updateFiscalNumber(Long.parseLong(sNumber), FISCAL_TYPE_CREDITNOTE);
	}

	/**
	 * Update FiscalNumber for the given type.
	 * @param iFiscalNumber
	 *            int
	 * @param iFiscalNumberType
	 *            int
	 * @return boolean
	 */
	private boolean updateFiscalNumber(long lFiscalNumber, int iFiscalNumberType) {
		int i = 0;
		File lock = null;
		responseCode = FiscalDocumentResponse.RESPONSE_PENDING;
		while(i < MAX_RETRIES) {
			i++;
			try {
				FISCAL_FILE_LOCATION = configFiscalDoc.getString(FISCAL_DOCUMENT_PATH);
				lock = new File(FISCAL_FILE_LOCATION + "/lock.lck");
				if (!lock.createNewFile()) {
					throw new FileLockException();
				} else {

					File backup = new File(FISCAL_FILE_LOCATION + BACKUP_FILE);
					File fiscalDocNoFile = new File(FISCAL_FILE_LOCATION + FISCAL_FILE);
					File badFiscalDocNoFile = new File(FISCAL_FILE_LOCATION + BAD_FILE);

					FiscalDocumentNumber fiscalDocNum = null;
					try {
						fiscalDocNum = this.getFiscalDocumentNumberFromFile(fiscalDocNoFile);
					} catch (EOFException e) {
						if (badFiscalDocNoFile.exists())
							badFiscalDocNoFile.delete();
						fiscalDocNoFile.renameTo(badFiscalDocNoFile);
						if (backup.exists()) {
							try {
								// test backup
								this.getFiscalDocumentNumberFromFile(backup);
								backup.renameTo(fiscalDocNoFile);
							} catch (Exception e1) {
								backup.delete();
							}
						}
					}
					if (fiscalDocNum == null) {
						lock.delete();
						responseCode = FiscalDocumentResponse.RESPONSE_NO_DOC_NUMBER;
						return false;
					}
					if (lFiscalNumber > getNextNumber(fiscalDocNum, iFiscalNumberType)) {
						switch (iFiscalNumberType) {
							case FISCAL_TYPE_DDT:
								fiscalDocNum.setNextDDTNo(lFiscalNumber);
								break;
							case FISCAL_TYPE_VAT:
								fiscalDocNum.setNextVATNo(lFiscalNumber);
								break;
							case FISCAL_TYPE_CREDITNOTE:
								fiscalDocNum.setNextCreditNoteNo(lFiscalNumber);
								break;
						}
						if (backup.exists()) {
							backup.delete();
						}
						fiscalDocNoFile.renameTo(backup);
						this.writeFiscalDocumentNumberToFile(fiscalDocNoFile, fiscalDocNum);
						try {
							// test file
							this.getFiscalDocumentNumberFromFile(fiscalDocNoFile);
							responseCode = FiscalDocumentResponse.RESPONSE_SUCCESS;
						} catch (Exception e) {
							// corrupt file
							if (fiscalDocNoFile.exists())
								fiscalDocNoFile.delete();
							backup.renameTo(fiscalDocNoFile);
							throw e;
						}
						// update backup
						try {
							if (backup.exists())
								backup.delete();
							this.writeFiscalDocumentNumberToFile(backup, fiscalDocNum);
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
						System.out.println("print " + fiscalDocNum.getNextVATNo() + " " + lock.exists());
						i = MAX_RETRIES;
						lock.delete();
						return true;
					}
					i = MAX_RETRIES;
					lock.delete();
					responseCode = FiscalDocumentResponse.RESPONSE_NO_UPDATE;
					return false;
				}
			} catch (FileLockException e) {
				System.out.println(e.getMessage());
				try {
					Thread.sleep(MAX_WAIT);
					System.out.println("MAX_WAIT " + MAX_WAIT);
					// i++;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println(e.getMessage());
				if (lock != null && lock.exists())
					lock.delete();
				try {
					Thread.sleep(MAX_WAIT);
					System.out.println("MAX_WAIT " + MAX_WAIT);
					// i++;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				responseCode = FiscalDocumentResponse.RESPONSE_NO_DOC_NUMBER;
				// return false;
			}

		}
		if (responseCode == FiscalDocumentResponse.RESPONSE_PENDING)
			responseCode = FiscalDocumentResponse.RESPONSE_TIME_OUT;
		return false;
	}

	/**
	 * Read/Write Fiscal Numbers to FiscalFile
	 * @param bUpdateFiscalFile
	 *            boolean
	 * @param iFiscalNumberType
	 *            int
	 * @return String
	 */
	private String readWriteFiscalNumber(boolean bUpdateFiscalFile, int iFiscalNumberType) throws Exception {
		responseCode = FiscalDocumentResponse.RESPONSE_PENDING;
		int i = 0;
		File lock = null;
		File backup = null;
		File fiscalDocNoFile = null;
		File badFiscalDocNoFile = new File(FISCAL_FILE_LOCATION + BAD_FILE);
		long lDocuNumber = -1;
		// responseCode = -1;
		while(i < MAX_RETRIES) {
			i++;
			try {
				FISCAL_FILE_LOCATION = configFiscalDoc.getString(FISCAL_DOCUMENT_PATH);
				lock = new File(FISCAL_FILE_LOCATION + "/lock.lck");
				if (!lock.createNewFile()) {
					throw new FileLockException();
				} else {
					backup = new File(FISCAL_FILE_LOCATION + BACKUP_FILE);
					if (backup != null && backup.exists()) {
						backup.delete();
					}
					fiscalDocNoFile = new File(FISCAL_FILE_LOCATION + FISCAL_FILE);
					FiscalDocumentNumber fiscalDocNum = null;
					try {
						fiscalDocNum = getFiscalDocumentNumberFromFile(fiscalDocNoFile);
					} catch (EOFException e) {
						// file is corrupt
						System.out.println("ERROR: " + e.getMessage());
						if (backup.exists()) {
							if (badFiscalDocNoFile.exists())
								badFiscalDocNoFile.delete();
							if (fiscalDocNoFile.exists())
								fiscalDocNoFile.renameTo(badFiscalDocNoFile);
							backup.renameTo(fiscalDocNoFile);
						}
						try {
							fiscalDocNum = getFiscalDocumentNumberFromFile(fiscalDocNoFile);
						} catch (EOFException e1) {
							responseCode = FiscalDocumentResponse.RESPONSE_NO_DOC_NUMBER;
							lock.delete();
							return "-1";
						}
					}
					if (fiscalDocNum == null) {
						responseCode = FiscalDocumentResponse.RESPONSE_NO_DOC_NUMBER;
						lock.delete();
						return "-1";
					}
					if (!bUpdateFiscalFile) {
						long temp = getNextNumber(fiscalDocNum, iFiscalNumberType);
						lock.delete();
						this.responseCode = FiscalDocumentResponse.RESPONSE_SUCCESS;
						return temp + "";
						// return getNextNumber(fiscalDocNum, iFiscalNumberType) + "";
					}
					fiscalDocNoFile.renameTo(backup);
					try {
						if (lDocuNumber < 0)
							lDocuNumber = setNextNumber(fiscalDocNum, iFiscalNumberType);
						writeFiscalDocumentNumberToFile(fiscalDocNoFile, fiscalDocNum);
					} catch (IOException e) {
						// the FiscalDocNoFile may be bad/corrupted, so revert back to the backup file.
						// try reading....
						try {
							this.getFiscalDocumentNumberFromFile(fiscalDocNoFile);
							this.responseCode = FiscalDocumentResponse.RESPONSE_SUCCESS;
						} catch (EOFException e1) {
							// it is corrupted
							if (backup.exists() && lDocuNumber < 0) {
								if (badFiscalDocNoFile.exists())
									badFiscalDocNoFile.delete();
								if (fiscalDocNoFile.exists())
									fiscalDocNoFile.renameTo(badFiscalDocNoFile);
								backup.renameTo(fiscalDocNoFile);
							} else if (lDocuNumber > -1) {
								System.out.println("ERROR: While writing new fiscal document number: " + lDocuNumber);
							}
						}
						throw e;
					}
					// update backup
					try {
						if (backup.exists())
							backup.delete();
						writeFiscalDocumentNumberToFile(backup, fiscalDocNum);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
					lock.delete();
					this.responseCode = FiscalDocumentResponse.RESPONSE_SUCCESS;
					return lDocuNumber + "";
				}
			} catch (FileLockException e) {
				responseCode = FiscalDocumentResponse.RESPONSE_NO_DOC_NUMBER;
				try {
					Thread.sleep(MAX_WAIT);
				} catch (InterruptedException ex1) {
					ex1.printStackTrace();
				}
				System.out.println("MAX_WAIT " + MAX_WAIT);
				// i++;
			} catch (IOException e) {
				lock.delete();
				responseCode = FiscalDocumentResponse.RESPONSE_NO_DOC_NUMBER;
				try {
					Thread.sleep(MAX_WAIT);
				} catch (InterruptedException ex2) {
					ex2.printStackTrace();
				}
				System.out.println("ERROR: " + e.getMessage());
				System.out.println("MAX_WAIT " + MAX_WAIT);
				// i++;
			} catch (Exception ex) {
				ex.printStackTrace();
				this.responseCode = FiscalDocumentResponse.RESPONSE_NO_DOC_NUMBER;
				lock.delete();
			}
		}
		if (lock != null)
			lock.delete();
		if (responseCode == FiscalDocumentResponse.RESPONSE_PENDING) {
			responseCode = FiscalDocumentResponse.RESPONSE_TIME_OUT;
		}
		// Alex
		return "-1";
		// return "N/A";
	}

	private FiscalDocumentNumber getFiscalDocumentNumberFromFile(File fiscalDocNoFile) throws Exception {
		FileInputStream fileReader = new FileInputStream(fiscalDocNoFile);
		ObjectInputStream objectReader = new ObjectInputStream(fileReader);
		FiscalDocumentNumber fiscalDocNum = (FiscalDocumentNumber) objectReader.readObject();
		fileReader.close();
		objectReader.close();
		return fiscalDocNum;
	}

	private void writeFiscalDocumentNumberToFile(File fiscalDocNoFile, FiscalDocumentNumber fiscalDocNum) throws Exception {
		FileOutputStream fileWriter = null;
		ObjectOutputStream objectWriter = null;
		try {
			fiscalDocNoFile.createNewFile();
			fileWriter = new FileOutputStream(fiscalDocNoFile);
			objectWriter = new ObjectOutputStream(fileWriter);
			objectWriter.writeObject(fiscalDocNum);
			objectWriter.flush();
		} finally {
			if (objectWriter != null)
				objectWriter.close();
			if (fileWriter != null)
				fileWriter.close();
		}
	}

	/**
	 * Update FiscalNumber of specified type to FiscalDocumentNumber object.
	 * @param docNum
	 *            FiscalDocumentNumber
	 * @param iType
	 *            int
	 */
	private long setNextNumber(FiscalDocumentNumber docNum, int iType) {
		long lDocNumber = getNextNumber(docNum, iType);
		switch (iType) {
			case FISCAL_TYPE_DDT:
				docNum.setNextDDTNo(lDocNumber);
				break;
			case FISCAL_TYPE_VAT:
				docNum.setNextVATNo(lDocNumber);
				break;
			case FISCAL_TYPE_CREDITNOTE:
				if (bIsACreditNoteCountry) {
					docNum.setNextCreditNoteNo(lDocNumber);
				} else {
					docNum.setNextVATNo(lDocNumber);
				}
		}
		return lDocNumber;
	}

	/**
	 * Get FiscalNumber of specified type from FiscalDocumentNumber object.
	 * @param docNum
	 *            FiscalDocumentNumber
	 * @param iType
	 *            int
	 * @return long
	 */
	private long getNextNumber(FiscalDocumentNumber docNum, int iType) {
		switch (iType) {
			case FISCAL_TYPE_DDT:
				return docNum.getNextDDTNo() + 1;
			case FISCAL_TYPE_VAT:
				return docNum.getNextVATNo() + 1;
			case FISCAL_TYPE_CREDITNOTE:
				if (bIsACreditNoteCountry) {
					return docNum.getNextCreditNoteNo() + 1;
				} else {
					return docNum.getNextVATNo() + 1;
				}
		}
		return -1;
	}

	public int getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(int code) {
		this.responseCode = code;
	}

	private class FileLockException extends Exception {
		public FileLockException() {
			super();
		}

		public FileLockException(String message) {
			super(message);
		}
	}
}
