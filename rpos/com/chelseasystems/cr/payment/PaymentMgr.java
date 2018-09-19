/*
 * @copyright (c) 2002 Retek Inc.
 */

package com.chelseasystems.cr.payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.IConfig;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.util.Trace;
import java.util.ResourceBundle;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.util.Version;

/**
 */
public class PaymentMgr implements IConfig {
	public final static int PAYMENT = 0;
	public final static int CHANGE = 1;
	public final static int REFUND = 2;
	public final static int CREDIT = PAYMENT;
	public final static int DEBIT = CHANGE;
	private static ConfigMgr mgr = null;
	private static Map htPayments = new HashMap();
	private static ArrayList alPayments = new ArrayList();
	private static Map htPaymentRenderers = new HashMap();
	private static Map htSeq = new HashMap();
	private static Map htSession = new HashMap();
	private static Map htCallCenterDesc = new HashMap();
	private static Map htRptPaymentsDesc = new HashMap();
	private static Vector rptCreditPayments = new Vector();
	private static ResourceBundle res = null;
	// init
	static {
		res = ResourceManager.getResourceBundle();
		mgr = new ConfigMgr("payment.cfg");
		String payments = mgr.getString("PAYMENTS");
		StringTokenizer tok = new StringTokenizer(payments, ",");
		while(tok.hasMoreElements()) {
			String key = (String) tok.nextElement();
			// load instance of payment
			try {
				htPayments.put(key, getPayment(key));
				alPayments.add(key);
			} catch (Exception ex) {
				Trace.out("PaymentMgr.init() " + key + "->" + ex);
			}
			// load desc
			htCallCenterDesc.put(key, mgr.getString(key + ".CALL_CENTER_DISPLAY"));
			// load sequence
			htSeq.put(key, mgr.getInteger(key + ".SEQUENCE"));
		}
		payments = mgr.getString("EOD_PAYMENTS");
		tok = new StringTokenizer(payments, ",");
		while(tok.hasMoreTokens()) {
			String key = tok.nextToken();
			// load should hide totals
			Boolean bFlag = new Boolean(mgr.getString(key + ".HIDE_AMOUNT"));
			htSession.put(key, bFlag);
		}
		payments = mgr.getString("RPT_PAYMENTS");
		tok = new StringTokenizer(payments, ",");
		while(tok.hasMoreTokens()) {
			String key = tok.nextToken();
			Boolean bFlag = new Boolean(mgr.getString("RPT_PAYMENTS_" + key + ".CREDIT"));
			if (bFlag.booleanValue())
				rptCreditPayments.add(key);
			htRptPaymentsDesc.put(key, res.getString(mgr.getString("RPT_PAYMENTS_" + key + ".DESC")));
			htCallCenterDesc.put(key, mgr.getString(key + ".CALL_CENTER_DISPLAY"));
		}
	}

	/**
	 * @return
	 */
	public static String[] getReportPayments() {
		String[] array = (String[]) htRptPaymentsDesc.keySet().toArray(new String[htRptPaymentsDesc.size()]);
		return array;
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getReportPaymentDesc(String key) {
		return (String) htRptPaymentsDesc.get(key);
	}

	/**
	 * @return
	 */
	public static String[] getReportCreditPayments() {
		String[] array = (String[]) rptCreditPayments.toArray(new String[rptCreditPayments.size()]);
		return array;
	}

	/**
	 * @return the description for each valid credit payment type, (ex. American Express)
	 */
	public static String[] getCreditPaymentDesc() {
		String[] creditPayments = getReportCreditPayments();
		for (int i = 0; i < creditPayments.length; i++)
			creditPayments[i] = getReportPaymentDesc(creditPayments[i]);
		return creditPayments; /*
		 Vector vCredit = new Vector();
		 String payments = mgr.getString("CREDITDESC");
		 StringTokenizer tokenizer = new StringTokenizer(payments, ",");
		 while(tokenizer.hasMoreElements())
		 {
		 String sCredit = (String)tokenizer.nextElement();
		 vCredit.addElement(sCredit);
		 }
		 String[] pay = new String[vCredit.size()];
		 for(int x = 0; x < pay.length; x++)
		 {
		 pay[x] = (String)vCredit.elementAt(x);
		 System.out.println("*********CREDIT PAYMENT IS " + pay[x]);
		 }
		 return(pay);
		 */

	}

	/**
	 * @return list of <b>end session</b> payment types.
	 */
	public static String[] getEndSessionPaymentTypes() {
		String[] array = (String[]) htSession.keySet().toArray(new String[0]);
		Arrays.sort(array);
		return (array);
	}

	/**
	 * @return name of group to include payment in
	 */
	public static String getEndSessionGrouping(String key) {
		return (mgr.getString(key + ".EOD_GROUP"));
	}

	/**
	 * @return name of group to total payment with
	 */
	public static String getEndSessionBankDepositGrouping(String key) {
		return (mgr.getString(key + ".EOD_DEPOSIT_GROUP"));
	}

	/**
	 * @return whether payment type should hide its total on <b>end session</b>.
	 */
	public static boolean getShouldHideTotal(String key) {
		Boolean hidden = (Boolean) htSession.get(key);
		return (hidden == null) ? false : hidden.booleanValue();
	}

	/**
	 * @return an array of all payments that are used by the system
	 */
	public static Payment[] getAllPayments() {
		return (Payment[]) htPayments.values().toArray(new Payment[htPayments.size()]);
	}

	/**
	 * @return an array of all the keys that relate to payments that are used by the system
	 */
	public static String[] getAllPaymentKeys() {
		return (String[]) htPayments.keySet().toArray(new String[htPayments.size()]);
	}

	/**
	 * @param key
	 * @return
	 * @exception Exception
	 */
	public static Payment getPayment(String key) throws Exception {
		return (getPayment(key, false));
	}

	/**
	 * @return Payment
	 */
	public static Payment getPayment(String key, boolean uniqueInstance) throws Exception {
		Payment payment = (Payment) htPayments.get(key);
		if (payment == null) {
			String paymentClass = mgr.getString(key + ".CLASS");
			Class cls = Class.forName(paymentClass);
			payment = (Payment) cls.newInstance();
			// apply protected attributes
			payment.setTransactionPaymentName(key);
			payment.setGUIPaymentName(res.getString(mgr.getString(key + ".DESC_KEY")));
		} else if (uniqueInstance) {
			return ((Payment) payment.clone());
		}
		return (payment);
	}

	/**
	 * @return an array of keys for valid payments
	 */
	public static String[] getPayments(int type, PaymentTransaction theTxn) {
		long start = System.currentTimeMillis();
		Vector vResults = new Vector();
		RulesInfo rc = null;
		//for (Iterator iter = htPayments.keySet().iterator(); iter.hasNext();) {
		for (Iterator iter = alPayments.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Payment pay = (Payment) htPayments.get(key);
			switch (type) {
				case PAYMENT:
					try {
						pay.isValidAsPayment(theTxn);
						vResults.addElement(key);
					} catch (BusinessRuleException ex) {
					}
					break;
				case CHANGE:
					try {
						pay.isValidAsChange(theTxn);
						vResults.addElement(key);
					} catch (BusinessRuleException ex) {
					}
					break;
				case REFUND:
					try {
						pay.isValidAsRefund(theTxn);
						vResults.addElement(key);
					} catch (BusinessRuleException ex) {
					}
					break;
			}
		}
		if (vResults.size() == 0)
			return (null);
		//      String[] results = new String[vResults.size()];
		//      vResults.copyInto(results);
		 String[] results = new String[vResults.size()];
		if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
			  vResults.copyInto(results);	
    		  return results;
		}
		return (sortPayments(vResults));
		
	}

	
	
	
	/**
	 * @param aKey
	 * @return
	 */
	public static String getPaymentDescriptionKey(String aKey) {
		Payment payment = (Payment) htPayments.get(aKey);
		if (payment == null) {
			String desc = mgr.getString(aKey + ".DESC_KEY");
			return (desc != null && desc.length() > 0) ? res.getString(desc) : "Payment";
		} else {
			return payment.getGUIPaymentName();
		}
	}

	/**
	 * @param vPayments
	 * @return
	 */
	private static String[] sortPayments(Vector vPayments) {
		String[] result = new String[vPayments.size()];
		boolean unsorted = false;
		do {
			// Assume they are in order.
			unsorted = false;
			// Check the sequence of adjacent elements.
			for (int i = 0; i < vPayments.size() - 1; i++) {
				Integer thisSeq = (Integer) htSeq.get((String) vPayments.elementAt(i));
				Integer nextSeq = (Integer) htSeq.get((String) vPayments.elementAt(i + 1));
				if (thisSeq.intValue() > nextSeq.intValue()) {
					// Not in order, so swap elements.
					String spare = (String) vPayments.elementAt(i);
					vPayments.setElementAt(vPayments.elementAt(i + 1), i);
					vPayments.setElementAt(spare, i + 1);
					// Signal they are unsorted, go again.
					unsorted = true;
				}
			}
		} while(unsorted); // end of unsorted ins loop
		vPayments.copyInto(result);
		return (result);
	}

	/**
	 * @param aKey
	 */
	public void processConfigEvent(String[] aKey) {
	}

	/**
	 * @param aKey
	 * @return
	 */
	public static String getDefaultCallCenterDisplay(String aKey) {
		return (String) htCallCenterDesc.get(aKey);
	}

	/**
	 * @param payment
	 * @return
	 */
	public static String getPaymentBuilder(String payment) {
		return (mgr.getString(payment + ".BUILDER"));
	}

	/**
	 * @param payment
	 * @return
	 */
	public static PaymentRenderer getRendererClassForPayment(Payment payment) {
		String paymentName = payment.getTransactionPaymentName();
		PaymentRenderer rendererInstance = null;
		if (htPaymentRenderers.containsKey(paymentName)) {
			rendererInstance = (PaymentRenderer) htPaymentRenderers.get(paymentName);
		} else {
			String rendererClassName = mgr.getString(paymentName + ".RENDERER");
			try {
				rendererInstance = (PaymentRenderer) Class.forName(rendererClassName).newInstance();
				htPaymentRenderers.put(paymentName, rendererInstance);
			} catch (Exception e) {
				System.out.println("Exception thrown upon attempt to obtain renderer class for payment: " + paymentName);
				System.out.println("Class Name: " + rendererClassName);
			}
		}
		if (rendererInstance != null) {
			rendererInstance.setPayment(payment);
		}
		return (rendererInstance);
	}
	
	/**
	 * @param aKey
	 * @return
	 * Introduced new tender buttons for Canada
	 */
	public static String getPaymentDescriptionKeyforMobileTerminal(String aKey) {
		Payment payment = (Payment) htPayments.get(aKey);
		if (payment == null) {
			String desc = mgr.getString(aKey + ".DESC_KEY_FOR_MOBILE");
			return (desc != null && desc.length() > 0) ? res.getString(desc) : "Payment";
		} else {
			return payment.getGUIPaymentNameForMobileTerminal();
		}
	}

}
