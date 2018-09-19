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
 | 3    | 06-01-2005 | Sameena   | 89        | Customer details are not shown when a        |
 |      |            |           |           | transaction is looked up. Modified the method|
 |      |            |           |           | 'showTransaction'.
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 05-10-2005 | Anand     | N/A       |2. Modifications as per specifications for    |
 |      |            |           |           |   Txn History                                |
 -------------------------------------------------------------------------------------------|
 | 1    | 04-28-2005 | Mukesh    | N/A       |1.Modified showTransaction() method to get the|
 |      |            |           |           | customer details in void trxn screen.        |
 --------------------------------------------------------------------------------------------
 */

package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import javax.swing.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import java.util.ResourceBundle;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import com.chelseasystems.cr.config.ConfigMgr;

/**
 */
public class TxnHeaderPanel extends JPanel {
	JCMSTextField edtFiscalDocType;
	JCMSTextField edtTxnId;
	JCMSTextField edtDate;
	JCMSTextField edtType;
	JCMSTextField edtConsultant;
	JCMSTextField edtRTNReason;
	JCMSTextField edtStore;
	JCMSTextField edtPhone;
	JCMSTextField edtCustName;
	JCMSTextField edtAddress1;
	JCMSTextField edtAddress2;
	JCMSTextField edtCity;
	JCMSTextField edtState;
	JPanel pnlCityStateZip;
	JCMSTextField edtZip;
	JCMSTextField edtOperator;
	JCMSLabel jCMSLabel1;
	IApplicationManager theAppMgr;
	boolean showReturnReason = false;

	private static final String CONFIGURATION_FILE = "pos.cfg";
	private static ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
	private static String txnHeaderPanelString = configMgr.getString("TRANSACTION.TXN_HEADER_PANEL");

	/**
	 */
	public TxnHeaderPanel(boolean showReturnReason) {
		try {
			this.showReturnReason = showReturnReason;
			edtTxnId = new JCMSTextField();
			edtDate = new JCMSTextField();
			edtType = new JCMSTextField();
			edtConsultant = new JCMSTextField();
			edtRTNReason = new JCMSTextField();
			edtStore = new JCMSTextField();
			edtPhone = new JCMSTextField();
			edtFiscalDocType = new JCMSTextField();
			edtCustName = new JCMSTextField();
			edtAddress1 = new JCMSTextField();
			edtAddress2 = new JCMSTextField();
			pnlCityStateZip = new JPanel();
			edtCity = new JCMSTextField();
			edtState = new JCMSTextField();
			edtZip = new JCMSTextField();
			edtOperator = new JCMSTextField();
			jbInit();
			setEnabled(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public TxnHeaderPanel() {
		this(false);
	}

	/**
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		final ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		JPanel pnlRight = new JPanel();
		JPanel pnlLeft = new JPanel();
		jCMSLabel1 = new JCMSLabel();
		JCMSLabel jCMSLabel2 = new JCMSLabel();
		JCMSLabel jCMSLabel3 = new JCMSLabel();
		JCMSLabel jCMSLabel4 = new JCMSLabel();
		JCMSLabel jCMSLabel5 = new JCMSLabel();
		JCMSLabel jCMSLabel6 = new JCMSLabel();
		JCMSLabel jCMSLabel7 = new JCMSLabel();
		JCMSLabel jCMSLabel8 = new JCMSLabel();
		JCMSLabel jCMSLabel9 = new JCMSLabel();
		JCMSLabel jCMSLabel10 = new JCMSLabel();
		JCMSLabel jCMSLabel11 = new JCMSLabel();
		JCMSLabel jCMSLabe210 = new JCMSLabel();
		this.setLayout(new GridLayout(0, 2));
		pnlRight.setOpaque(false);
		pnlRight.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlLeft.setOpaque(false);
		pnlCityStateZip.setOpaque(false);
		pnlLeft.setLayout(new FlowLayout(FlowLayout.LEFT));
		jCMSLabel1.setText(res.getString("Transaction No") + ":");
		jCMSLabel1.setLabelFor(edtTxnId);
		jCMSLabel2.setText(res.getString("Date") + ":");
		jCMSLabel2.setLabelFor(edtDate);
		jCMSLabel3.setText(res.getString("Type") + ":");
		jCMSLabel3.setLabelFor(edtType);
		if (showReturnReason) {
			jCMSLabel4.setText(res.getString("RTN Reason") + ":");
			jCMSLabel4.setLabelFor(edtRTNReason);
		} else {
			jCMSLabel4.setText(res.getString("Associate") + ":");
			jCMSLabel4.setLabelFor(edtConsultant);
		}
		// PCR1326 View Transaction Details fix for Armani Japan
		if (txnHeaderPanelString == null) {
			jCMSLabel5.setText(res.getString("Store Id") + ":");
		} else {
			jCMSLabel5.setText(res.getString("Store Name") + ":");
		}
		jCMSLabel5.setLabelFor(edtStore);
		jCMSLabel6.setText(res.getString("Customer Name") + ":");
		jCMSLabel6.setLabelFor(edtCustName);
		jCMSLabel7.setText(res.getString("Address Line 1") + ":");
		jCMSLabel7.setLabelFor(edtAddress1);
		jCMSLabel8.setText(res.getString("Address Line 2") + ":");
		jCMSLabel8.setLabelFor(edtAddress2);
		jCMSLabel9.setText(res.getString("City/State/Zip") + ":");
		jCMSLabel9.setLabelFor(pnlCityStateZip);
		jCMSLabel10.setText(res.getString("Primary Phone") + ":");
		jCMSLabel10.setLabelFor(edtPhone);
		// Added Fiscal Doc Type for Europe
		jCMSLabe210.setText(res.getString("Fiscal Doc Type") + ":");
		jCMSLabe210.setLabelFor(edtFiscalDocType);

		jCMSLabel11.setText(res.getString("Cashier") + ":");
		jCMSLabel11.setLabelFor(edtOperator);
		this.add(pnlLeft, null);
		pnlLeft.add(jCMSLabel1);
		pnlLeft.add(edtTxnId);
		pnlLeft.add(jCMSLabel2);
		pnlLeft.add(edtDate, null);
		pnlLeft.add(jCMSLabel3, null);
		pnlLeft.add(edtType, null);
		pnlLeft.add(jCMSLabel4, null);
		if (showReturnReason)
			pnlLeft.add(edtRTNReason, null);
		else
			pnlLeft.add(edtConsultant, null);
		pnlLeft.add(jCMSLabel5, null);
		pnlLeft.add(edtStore, null);
		pnlLeft.add(jCMSLabel11, null);
		pnlLeft.add(edtOperator, null);
		this.add(pnlRight, null);
		pnlRight.add(jCMSLabel6, null);
		pnlRight.add(edtCustName, null);
		pnlRight.add(jCMSLabel7, null);
		pnlRight.add(edtAddress1, null);
		pnlRight.add(jCMSLabel8, null);
		pnlRight.add(edtAddress2, null);
		pnlRight.add(jCMSLabel9, null);
		pnlRight.add(pnlCityStateZip, null);
		pnlCityStateZip.add(edtCity, null);
		pnlCityStateZip.add(edtState, null);
		pnlCityStateZip.add(edtZip, null);
		pnlRight.add(jCMSLabel10, null);
		pnlRight.add(edtPhone, null);
		pnlRight.add(jCMSLabe210, null);
		pnlRight.add(edtFiscalDocType, null);

		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		this.setPreferredSize(new Dimension((int) (844 * r), (int) (200 * r)));
		jCMSLabel1.setPreferredSize(new Dimension((int) (120 * r), (int) (25 * r)));
		jCMSLabel2.setPreferredSize(new Dimension((int) (120 * r), (int) (25 * r)));
		jCMSLabel3.setPreferredSize(new Dimension((int) (120 * r), (int) (25 * r)));
		jCMSLabel4.setPreferredSize(new Dimension((int) (120 * r), (int) (25 * r)));
		jCMSLabel5.setPreferredSize(new Dimension((int) (120 * r), (int) (25 * r)));
		jCMSLabel6.setPreferredSize(new Dimension((int) (150 * r), (int) (25 * r)));
		jCMSLabel7.setPreferredSize(new Dimension((int) (150 * r), (int) (25 * r)));
		jCMSLabel8.setPreferredSize(new Dimension((int) (150 * r), (int) (25 * r)));
		jCMSLabel9.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		jCMSLabel10.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		jCMSLabel11.setPreferredSize(new Dimension((int) (120 * r), (int) (30 * r)));
		jCMSLabe210.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		edtCustName.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		edtTxnId.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		edtDate.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		edtType.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		if (showReturnReason)
			edtRTNReason.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		else
			edtConsultant.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		edtStore.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		edtPhone.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		edtFiscalDocType.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		edtAddress1.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		edtAddress2.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
		pnlCityStateZip.setPreferredSize(new Dimension((int) (255 * r), (int) (30 * r)));
		edtZip.setPreferredSize(new Dimension((int) (75 * r), (int) (25 * r)));
		edtCity.setPreferredSize(new Dimension((int) (120 * r), (int) (25 * r)));
		edtState.setPreferredSize(new Dimension((int) (40 * r), (int) (25 * r)));
		edtOperator.setPreferredSize(new Dimension((int) (250 * r), (int) (25 * r)));
	}

	/**
	 * @param theAppMgr
	 */
	public void setAppMgr(IApplicationManager theAppMgr) {
		edtTxnId.setAppMgr(theAppMgr);
		edtDate.setAppMgr(theAppMgr);
		edtType.setAppMgr(theAppMgr);
		if (edtRTNReason != null)
			edtRTNReason.setAppMgr(theAppMgr);
		if (edtConsultant != null)
			edtConsultant.setAppMgr(theAppMgr);
		edtStore.setAppMgr(theAppMgr);
		edtCustName.setAppMgr(theAppMgr);
		edtPhone.setAppMgr(theAppMgr);
		edtFiscalDocType.setAppMgr(theAppMgr);
		edtAddress1.setAppMgr(theAppMgr);
		edtAddress2.setAppMgr(theAppMgr);
		edtCity.setAppMgr(theAppMgr);
		edtState.setAppMgr(theAppMgr);
		edtZip.setAppMgr(theAppMgr);
		edtOperator.setAppMgr(theAppMgr);
		setBackground(theAppMgr.getBackgroundColor());
		this.theAppMgr = theAppMgr;
	}

	/**
	 */
	public void setEditable(boolean editable) {
		edtTxnId.setEditable(editable);
		edtDate.setEditable(editable);
		edtType.setEditable(editable);
		if (edtRTNReason != null)
			edtRTNReason.setEditable(editable);
		if (edtConsultant != null)
			edtConsultant.setEditable(editable);
		edtStore.setEditable(editable);
		edtCustName.setEditable(editable);
		edtPhone.setEditable(editable);
		edtFiscalDocType.setEditable(editable);
		edtAddress1.setEditable(editable);
		edtAddress2.setEditable(editable);
		edtCity.setEditable(editable);
		edtState.setEditable(editable);
		edtZip.setEditable(editable);
		edtOperator.setEditable(editable);
	}

	/**
	 */
	public void setEnabled(boolean enabled) {
		edtTxnId.setEnabled(enabled);
		edtDate.setEnabled(enabled);
		edtType.setEnabled(enabled);
		if (edtRTNReason != null)
			edtRTNReason.setEnabled(enabled);
		if (edtConsultant != null)
			edtConsultant.setEnabled(enabled);
		edtStore.setEnabled(enabled);
		edtCustName.setEnabled(enabled);
		edtPhone.setEnabled(enabled);
		edtFiscalDocType.setEnabled(enabled);
		edtAddress1.setEnabled(enabled);
		edtAddress2.setEnabled(enabled);
		edtCity.setEnabled(enabled);
		edtState.setEnabled(enabled);
		edtZip.setEnabled(enabled);
		edtOperator.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	/**
	 * GIORGIO ARMANI SPA METHOD ADDED IN ORDER TO HAVE A FIX Recupera il metodo da richiamare per la determinazione del valore
	 * 
	 * @author Alessandro
	 * @param decoder
	 *            istanza dell'oggetto che contiene il metodo
	 * @param methodName
	 *            nome del metodo
	 * @return
	 */
	private Method getMethod(Object decoder, String methodName) {
		Method retMethod = null;
		String decoderName = "";
		if (decoder != null && methodName != null) {
			decoderName = decoder.getClass().getName();
			try {
				retMethod = decoder.getClass().getMethod(methodName, null);
				if (!retMethod.isAccessible()) {
					retMethod.setAccessible(true);
				}
			} catch (SecurityException e) {
				System.out.println("Security for method: " + methodName + " in class " + decoderName);
			} catch (NoSuchMethodException e) {
				System.out.println("Method not present: " + methodName + " in class " + decoderName);
			}
		}
		return retMethod;
	}

	/**
	 * GIORGIO ARMANI SPA METHOD ADDED IN ORDER TO HAVE A FIX
	 * 
	 * @author Alessandro
	 */
	private String getItalianReceiptNumber(PaymentTransaction aTxn) {
		String receiptNumber = "";
		try {
			Method mapFunc = getMethod(aTxn, "getFiscalReceiptNumber");
			Object value = mapFunc.invoke(aTxn, null);
			receiptNumber = (String) value;
		} catch (NullPointerException e) {
			System.out.println("Null pointer in retreiving receipt number");
		} catch (IllegalAccessException e) {
			System.out.println("Method not accessible in retreiving receipt number");
		} catch (InvocationTargetException e) {
			System.out.println("Method spruzzed in retreiving receipt number");
		} catch (Exception e) {
			System.out.println("Method stra-spruzzed  in retreiving receipt number");
		}
		return receiptNumber;
	}

	/**
	 * @param txn
	 */
	public void showTransaction(PaymentTransactionAppModel aTxn) {
		final ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		/*
		 * Alex: not all transaction can be casted to a CMSCompositePOSTransaction edtTxnId.setText( " " + aTxn.getPaymentTransaction().getId() + " / " +
		 * ((CMSCompositePOSTransaction) (aTxn.getPaymentTransaction())).getFiscalReceiptNumber());
		 */
		String receiptNumber = "";
		// Alex: This is the fix
		receiptNumber = getItalianReceiptNumber(aTxn.getPaymentTransaction());
		if (receiptNumber != null && !receiptNumber.equals("")) {
			edtTxnId.setText(" " + aTxn.getPaymentTransaction().getId() + " / " + receiptNumber);
			jCMSLabel1.setText(res.getString("Txn.#/Fisc.Rcpt") + ":");
		} else {
			edtTxnId.setText(" " + aTxn.getPaymentTransaction().getId());
			jCMSLabel1.setText(res.getString("Transaction No") + ":");
		}
		SimpleDateFormat fmt = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
		SimpleDateFormat fmtTime = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateTimeFormat();
		edtDate.setText(" " + fmt.format(aTxn.getPaymentTransaction().getProcessDate()) + " / " + fmtTime.format(aTxn.getPaymentTransaction().getSubmitDate()));
		// will have to be changed to return a long trans name
		edtType.setText(" " + res.getString(aTxn.getPaymentTransaction().getTransactionType()));
		if (showReturnReason) {
			if (theAppMgr.getStateObject("RETURN_REASON") != null) {
				edtRTNReason.setText(" " + theAppMgr.getStateObject("RETURN_REASON"));
			} else {
				edtRTNReason.setText("");
			}
		} else {
			if (aTxn.getConsultant() != null) {
				Employee cons = aTxn.getConsultant();
				if (txnHeaderPanelString == null) {
					edtConsultant.setText(" " + cons.getFirstName() + " " + cons.getLastName());
				} else {
					edtConsultant.setText(" " + cons.getLastName() + " " + cons.getFirstName());
				}
			} else {
				edtConsultant.setText("");
			}
		}
		if (aTxn.getPaymentTransaction().getStore() != null) {
			if (txnHeaderPanelString == null) {
				edtStore.setText(" " + aTxn.getPaymentTransaction().getStore().getId());
			} else {
				edtStore.setText(" " + aTxn.getPaymentTransaction().getStore().getName());
			}
		} else {
			edtStore.setText("");
		}
		if (aTxn.getCustomer() != null) {
			CMSCustomer cmscust = (CMSCustomer) aTxn.getCustomer();
			// SP: Added the code to display the customer name outside the check for
			// address, as the address is not a mandatory field for customer info
			if (txnHeaderPanelString == null) {
				edtCustName.setText(" " + cmscust.getFirstName() + " " + cmscust.getLastName());
			} else {
				edtCustName.setText(" " + cmscust.getLastName() + " " + cmscust.getFirstName());
			}
			if (cmscust.getPrimaryAddress() != null) {
				CMSStore store = (CMSStore) theAppMgr.getGlobalObject("STORE");
				if (store.getPreferredISOCountry().equals("JP") && (!cmscust.testIfAddressViewable(store.getId()))) {
					String sVIPAddressString = res.getString("VIP_CUSTOMER_ADDRESS");
					edtPhone.setText(sVIPAddressString);
					edtAddress1.setText(sVIPAddressString);
					edtAddress2.setText(sVIPAddressString);
					edtCity.setText(sVIPAddressString);
					edtState.setText(sVIPAddressString);
					edtZip.setText(sVIPAddressString);
				} else {
					edtPhone.setText(" " + (cmscust.getPrimaryAddress().getPrimaryPhone() == null ? "" : cmscust.getPrimaryAddress().getPrimaryPhone().getTelephoneNumber()));
					StringBuffer address1 = new StringBuffer();
					StringBuffer address2 = new StringBuffer();
					if (cmscust.getPrimaryAddress().getAddressLine1() != null)
						address1.append(" " + cmscust.getPrimaryAddress().getAddressLine1().toUpperCase());
					if (cmscust.getPrimaryAddress().getAddressLine2() != null)
						address2.append(" " + cmscust.getPrimaryAddress().getAddressLine2().toUpperCase());
					edtAddress1.setText(address1.toString());
					edtAddress2.setText(address2.toString());
					if (cmscust.getPrimaryAddress().getCity() != null)
						edtCity.setText(" " + cmscust.getPrimaryAddress().getCity().toUpperCase());
					else
						edtCity.setText("");
					if (cmscust.getPrimaryAddress().getState() != null)
						edtState.setText(" " + cmscust.getPrimaryAddress().getState().toUpperCase());
					else
						edtState.setText("");
					if (cmscust.getPrimaryAddress().getZipCode() != null)
						edtZip.setText(" " + cmscust.getPrimaryAddress().getZipCode().toUpperCase());
					else
						edtZip.setText("");
				}
			} else {
				edtAddress1.setText("");
				edtAddress2.setText("");
				edtCity.setText("");
				edtState.setText("");
				edtZip.setText("");
				edtPhone.setText("");
			}
		} else {
			edtCustName.setText("");
			edtAddress1.setText("");
			edtAddress2.setText("");
			edtCity.setText("");
			edtState.setText("");
			edtZip.setText("");
			edtPhone.setText("");
		}
		if (aTxn.getPaymentTransaction().getTheOperator() != null) {
			if (txnHeaderPanelString == null) {
				edtOperator.setText(" " + aTxn.getPaymentTransaction().getTheOperator().getFirstName() + " " + aTxn.getPaymentTransaction().getTheOperator().getLastName());
			} else {
				edtOperator.setText(" " + aTxn.getPaymentTransaction().getTheOperator().getLastName() + " " + aTxn.getPaymentTransaction().getTheOperator().getFirstName());
			}
		} else {
			edtOperator.setText("");
		}

		// Add Fiscal Doc Type
		if (aTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) {
			CMSCompositePOSTransaction compositePOSTransaction = (CMSCompositePOSTransaction) aTxn.getPaymentTransaction();
			FiscalDocument[] fiscalDocuments = compositePOSTransaction.getFiscalDocumentArray();
			String docType = "";
			String docDDType = "";
			String docVATType = "";
			String docTFType = "";
			String docCNType = "";
			int lenFiscalDocuments = fiscalDocuments.length;
			int lenDDT = 0;
			int lenVAT = 0;
			int lenTF = 0;
			int lenCN = 0;
			String ddtDocNum = "";
			String vatDocNum = "";
			String tfDocNum = "";
			String cnDocNum = "";

			if (fiscalDocuments != null && fiscalDocuments.length > 0) {
				for (int i = 0; i < lenFiscalDocuments; i++) {
					if (fiscalDocuments[i].isDDTDocument()) {
						lenDDT = lenDDT + 1;
						ddtDocNum = fiscalDocuments[i].getDocumentNumber();
						docDDType = fiscalDocuments[i].getDocumentType();
					} else if (fiscalDocuments[i].isVatInvoiceDocument()) {
						lenVAT = lenVAT + 1;
						vatDocNum = fiscalDocuments[i].getDocumentNumber();
						docVATType = fiscalDocuments[i].getDocumentType();
					} else if (fiscalDocuments[i].isTaxFreeDocument()) {
						lenTF = lenTF + 1;
						tfDocNum = fiscalDocuments[i].getDocumentNumber();
						docTFType = fiscalDocuments[i].getDocumentType();
					} else if (fiscalDocuments[i].isCreditNoteDocument()) {
						lenCN = lenCN + 1;
						cnDocNum = fiscalDocuments[i].getDocumentNumber();
						docCNType = fiscalDocuments[i].getDocumentType();
					}
				}

				if (lenDDT > 0)
					docType = docType + docDDType + "(" + ddtDocNum + ")";
				if (lenTF > 0) {
					if (docType.length() > 0)
						docType = docType + ",";
					docType = docType + docTFType + "(" + tfDocNum + ")";
				}
				if (lenVAT > 0) {
					if (docType.length() > 0)
						docType = docType + ",";
					docType = docType + docVATType + "(" + vatDocNum + ")";
				}
				if (lenCN > 0) {
					if (docType.length() > 0)
						docType = docType + ",";
					docType = docType + docCNType + "(" + cnDocNum + ")";
				}
				edtFiscalDocType.setText(docType);
				edtFiscalDocType.setVisible(true);
			} else {
				edtFiscalDocType.setVisible(false);
			}
		}
	}

	/**
	 */
	public void clear() {
		edtTxnId.setText("");
		edtDate.setText("");
		edtType.setText("");
		if (edtRTNReason != null)
			edtRTNReason.setText("");
		if (edtConsultant != null)
			edtConsultant.setText("");
		edtStore.setText("");
		edtCustName.setText("");
		edtPhone.setText("");
		edtFiscalDocType.setText("");
		edtAddress1.setText("");
		edtAddress2.setText("");
		edtCity.setText("");
		edtState.setText("");
		edtZip.setText("");
		edtOperator.setText("");
	}
}
