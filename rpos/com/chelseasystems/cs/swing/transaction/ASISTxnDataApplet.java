/*
 History:
 --------------------------------------------------------------------------------------------------
 | 1    | 03-05-2005 | Megha     | N/A       | Europe Development                                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.swing.*;
import java.awt.event.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import java.awt.*;
import com.chelseasystems.cs.swing.panel.ASISTxnDataPanel;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.pos.ASISTxnData;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.SwingUtilities;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import java.text.ParseException;
import javax.swing.JComponent;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;


/**
 * <p>Title:CustomerLookupApplet </p>
 * <p>Description: Searches customer on provided search criteria</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ASISTxnDataApplet extends CMSApplet {
	private static final long serialVersionUID = 1L;

	private ASISTxnDataPanel pnlASISTxnData;
	PaymentTransactionAppModel appModel;
	private CMSCustomer cmsCust;
	private SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();

	public void stop() {
	}

	/**
	 * put your documentation comment here
	 */
	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void start() {
		try {
			sdf.setLenient(false);
			if (theAppMgr.getStateObject("ASIS_TXN_DATA") == null) {
				reset();
			}
			if (theAppMgr.getStateObject("FROM_TXN_DETAIL_APPLET") == null) {
				// If the source is from InitialSaleApplet_EUR
				pnlASISTxnData.setEnabled(true);
				theAppMgr.showMenu(MenuConst.ASIS_EUROPE, theOpr);
				enter();
				SwingUtilities.invokeLater(new Runnable() {

					/**
					 * put your documentation comment here
					 */
					public void run() {
						pnlASISTxnData.requestFocusOnFirstField();
					}
				});
			} else {
				// If the source is from TxnDetailApplet
				pnlASISTxnData.setEnabled(false);
				theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
			}

			if (theAppMgr.getStateObject("THE_TXN") != null) {
				PaymentTransaction txn = (PaymentTransaction) theAppMgr.getStateObject("THE_TXN");
				pnlASISTxnData.showTransaction(txn, theAppMgr);
			}
			if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
				cmsCust = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
				if (cmsCust != null) {
					pnlASISTxnData.setCustName(cmsCust.getFirstName() + " " + cmsCust.getLastName());
					pnlASISTxnData.setCustNo(cmsCust.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void reset() {
		pnlASISTxnData.reset();
	}

	/**
	 * put your documentation comment here
	 */
	private void enter() {
		theAppMgr.setSingleEditArea(res.getString("Enter ASIS transaction information. Select 'OK' when complete."));
	}

	/**
	 * put your documentation comment here
	 * @param sCommand
	 * @param sInput
	 */
	public void editAreaEvent(String sCommand, String sInput) {
		if (sCommand.equals("CUSTOMER_LOOKUP")) {
		}
	}

	/**
	 * put your documentation comment here
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("CUSTOMER_LOOKUP")) {
			ASISTxnData asisTxnData = new ASISTxnData();
			try {
				asisTxnData = populateObject();
			} catch (BusinessRuleException bre) {
				theAppMgr.showErrorDlg(bre.getMessage());

			}
			if (asisTxnData != null) {
				theAppMgr.addStateObject("ASIS_TXN_DATA", asisTxnData);
			}
		}
		if (sAction.equals("TRANSACTION_HISTORY")) {
			if (pnlASISTxnData.getCustNo().length() == 0) {
				theAppMgr.showErrorDlg(res.getString("Please select customer before Transaction History"));
				anEvent.consume();
				return;
			} else {
				if (cmsCust != null) {
					theAppMgr.addStateObject("CUSTOMER_LOOKUP", cmsCust);
				}
			}
		}
		if (sAction.equals("OK")) {
			if (!completeAttributes()) {
				anEvent.consume();
				return;
			}
			ASISTxnData asisTxnData = new ASISTxnData();
			try {
				asisTxnData = populateObject();
			} catch (BusinessRuleException bre) {
				theAppMgr.showErrorDlg(bre.getMessage());
				anEvent.consume();
				return;
			}
			theAppMgr.addStateObject("ASIS_TXN_DATA", asisTxnData);
			theAppMgr.addStateObject("FROM_ASIS", "FROM_ASIS");
		} else if (sAction.equals("CANCEL")) {
			theAppMgr.addStateObject("FROM_ASIS_CANCEL", "FROM_ASIS_CANCEL");
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getVersion() {
		return ("$Revision: 1.1 $");
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getScreenName() {
		return "ASIS TXN Data";
	}

	/**
	 * put your documentation comment here
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		pnlASISTxnData = new ASISTxnDataPanel();
		BorderLayout borderLayout1 = new BorderLayout();
		this.getContentPane().setLayout(borderLayout1);
		this.getContentPane().add(pnlASISTxnData, BorderLayout.NORTH);
		this.setLayout(borderLayout1);
		this.setBackground(theAppMgr.getBackgroundColor());
		pnlASISTxnData.setAppMgr(theAppMgr);
		Verifiers verifiers = new Verifiers();
		pnlASISTxnData.getTxnDateJTXT().setVerifier(verifiers.getTransactionDateVerifier());
		pnlASISTxnData.getFiscalReceiptDateJTXT().setVerifier(verifiers.getFiscalReceiptDateVerifier());
		pnlASISTxnData.getFiscalDocDateJTXT().setVerifier(verifiers.getFiscalDocDateVerifier());
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void pageDown(MouseEvent e) {
		// pnlASISTxnData.nextPage();
		// theAppMgr.showPageNumber(e, pnlASISTxnData.getCurrentPageNumber()
		// + 1, pnlASISTxnData.getTotalPages());
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void pageUp(MouseEvent e) {
		// pnlASISTxnData.prevPage();
		// theAppMgr.showPageNumber(e, pnlASISTxnData.getCurrentPageNumber()
		// + 1, pnlASISTxnData.getTotalPages());
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public ASISTxnData populateObject() throws BusinessRuleException {
		ASISTxnData asisTxn = new ASISTxnData();
		asisTxn.setCompanyCode(pnlASISTxnData.getCompanyCode());
		asisTxn.setStoreId(pnlASISTxnData.getStoreId());
		asisTxn.setRegId(pnlASISTxnData.getRegId());
		asisTxn.setTxnNo(pnlASISTxnData.getTxnNo());
		asisTxn.setTxnAmount(new ArmCurrency(pnlASISTxnData.getTxnAmount()));
		try {
			if (pnlASISTxnData.getTxnDate() != null && pnlASISTxnData.getTxnDate().trim().length() > 0)
				asisTxn.setTxnDate(sdf.parse(pnlASISTxnData.getTxnDate()));
		} catch (ParseException ex) {
		}
		try {
			if (pnlASISTxnData.getFiscalDocDate() != null && pnlASISTxnData.getFiscalDocDate().trim().length() > 0)
				asisTxn.setFiscalDocDate(sdf.parse(pnlASISTxnData.getFiscalDocDate()));
		} catch (ParseException ex) {
		}
		try {
			if (pnlASISTxnData.getFiscalReceiptDate() != null && pnlASISTxnData.getFiscalReceiptDate().trim().length() > 0)
				asisTxn.setFiscalReceiptDate(sdf.parse(pnlASISTxnData.getFiscalReceiptDate()));
		} catch (ParseException ex) {
		}
		try {
			if (pnlASISTxnData.getOrderDate() != null && pnlASISTxnData.getOrderDate().trim().length() > 0)
				asisTxn.setOrderDate(sdf.parse(pnlASISTxnData.getOrderDate()));
		} catch (ParseException ex) {
		}
		try {
			if (pnlASISTxnData.getSupplierDate() != null && pnlASISTxnData.getSupplierDate().trim().length() > 0)
				asisTxn.setSupplierDate(sdf.parse(pnlASISTxnData.getSupplierDate()));
		} catch (ParseException ex) {
		}
		asisTxn.setFiscalReceiptNo(pnlASISTxnData.getFiscalReceiptNo());
		asisTxn.setFiscalDocNo(pnlASISTxnData.getFiscalDocNo());
		asisTxn.setDocType(pnlASISTxnData.getDocType());
		asisTxn.setCustNo(pnlASISTxnData.getCustNo());
		asisTxn.setCustName(pnlASISTxnData.getCustName());
		asisTxn.setComments(pnlASISTxnData.getComments());
		asisTxn.setOrderNo(pnlASISTxnData.getOrderNo());
		asisTxn.setSupplierNo(pnlASISTxnData.getSupplierNo());
		asisTxn.setNotes(pnlASISTxnData.getNotes());
		return asisTxn;
	}

	// ------------------------------------------------------------------
	// ********************* Verifier Classes ***********************
	// ------------------------------------------------------------------
	private class Verifiers {

		/**
		 * @return
		 */
		public CMSInputVerifier getTransactionDateVerifier() {
			return (new CMSInputVerifier() {

				/**
				 * @param c
				 * @return
				 */
				public boolean verify(JComponent input) {
					try {
						ASISTxnData asisTxn = new ASISTxnData();
						String strDate = pnlASISTxnData.getTxnDate();
						if (strDate != null && strDate.trim().length() > 0) {
							asisTxn.setTxnDate(sdf.parse(pnlASISTxnData.getTxnDate()));
							return (true);
						}
					} catch (BusinessRuleException bx) {
						((JCMSTextField) input).requestFocus();
						theAppMgr.showErrorDlg(bx.getMessage());
						return (false);
					} catch (ParseException bx) {
						((JCMSTextField) input).requestFocus();
						theAppMgr.showErrorDlg(res.getString("Please enter valid date (dd/MM/yyyy) in Transaction Date field"));
						return (false);
					}
					return true;
				}
			});
		} // getTransactionDate

		/**
		 * put your documentation comment here
		 * @return
		 */
		public CMSInputVerifier getFiscalReceiptDateVerifier() {
			return (new CMSInputVerifier() {

				/**
				 * @param c
				 * @return
				 */
				public boolean verify(JComponent input) {
					try {
						ASISTxnData asisTxn = new ASISTxnData();
						String strDate = pnlASISTxnData.getFiscalReceiptDate();
						if (strDate != null && strDate.trim().length() > 0) {
							asisTxn.setFiscalReceiptDate(sdf.parse(pnlASISTxnData.getFiscalReceiptDate()));
							return (true);
						}
					} catch (BusinessRuleException bx) {
						((JCMSTextField) input).requestFocus();
						theAppMgr.showErrorDlg(bx.getMessage());
						return (false);
					} catch (ParseException bx) {
						((JCMSTextField) input).requestFocus();
						theAppMgr.showErrorDlg(res.getString("Please enter valid date (dd/MM/yyyy) in FiscalReceipt Date field"));
						return (false);
					}
					return true;
				}
			});
		}

		/**
		 * put your documentation comment here
		 * @return
		 */
		public CMSInputVerifier getFiscalDocDateVerifier() {
			return (new CMSInputVerifier() {

				/**
				 * @param c
				 * @return
				 */
				public boolean verify(JComponent input) {
					try {
						ASISTxnData asisTxn = new ASISTxnData();
						String strDate = pnlASISTxnData.getFiscalDocDate();
						if (strDate != null && strDate.trim().length() > 0) {
							asisTxn.setFiscalDocDate(sdf.parse(pnlASISTxnData.getFiscalDocDate()));
							return (true);
						}
					} catch (BusinessRuleException bx) {
						((JCMSTextField) input).requestFocus();
						theAppMgr.showErrorDlg(bx.getMessage());
						return (false);
					} catch (ParseException bx) {
						((JCMSTextField) input).requestFocus();
						theAppMgr.showErrorDlg(res.getString("Please enter valid date (dd/MM/yyyy) in FiscalDoc Date field"));
						return (false);
					}
					return true;
				}
			});
		}
	}

	private boolean completeAttributes() {
		return (verify(pnlASISTxnData.getTxnDateJTXT()) && verify(pnlASISTxnData.getFiscalReceiptDateJTXT()) && verify(pnlASISTxnData.getFiscalDocDateJTXT())
				&& verifyTxnAmount(pnlASISTxnData.getTxnAmountJTXT()) && verify(pnlASISTxnData.getOrderDateJTXT()))
				&& verify(pnlASISTxnData.getSupplierDateJTXT());
	}

	private boolean verify(JCMSTextField input) {
		try {
			String strDate = input.getText();

			ASISTxnData asisTxn = new ASISTxnData();
			if (strDate != null && strDate.trim().length() > 0) {
				asisTxn.setTxnDate(sdf.parse(strDate));
				return (true);
			}
		} catch (BusinessRuleException bx) {
			((JCMSTextField) input).requestFocus();
			theAppMgr.showErrorDlg(bx.getMessage());
			return (false);
		} catch (ParseException bx) {
			((JCMSTextField) input).requestFocus();
			theAppMgr.showErrorDlg(res.getString("Please enter valid date (dd/mm/yyyy) in date field"));
			return (false);
		}
		return true;
	}

	private boolean verifyTxnAmount(JCMSTextField input) {
		java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		double dAmount;

		String strTxnAmount = input.getText();
		if (strTxnAmount != null && strTxnAmount.trim().length() > 0) {
			try {
				dAmount = new ArmCurrency(0.0d).getNumberFormatInstance().parse(strTxnAmount).doubleValue();
			} catch (Exception nfe) {
				nfe.printStackTrace();
				theAppMgr.showErrorDlg(res.getString("The value is not a valid currency."));
				return false;
			}
			if (dAmount < 0.0) {
				theAppMgr.showErrorDlg(res.getString("Negative currency is not allowed."));
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
}
