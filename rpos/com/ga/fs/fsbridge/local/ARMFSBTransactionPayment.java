/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ga.fs.fsbridge.local;

import com.ga.fs.fsbridge.ARMFSBridge;
import com.ga.fs.fsbridge.object.ARMFSBridgeObject;
import com.ga.fs.fsbridge.utils.ConfigurationManager;

/**
 * 
 * @author Yves Agbessi
 */
public class ARMFSBTransactionPayment {

	/**
	 * @return the PaymentMediaCode
	 */
	public String getPaymentMediaCode() {
		return PaymentMediaCode;
	}

	public String getPaymentMediaCode(ARMFSBTransactionPayment payment) {
		return payment.getPaymentMediaCode();
	}

	/**
	 * @param PaymentMediaCode
	 *            the PaymentMediaCode to set
	 */
	public void setPaymentMediaCode(String PaymentMediaCode) {
		this.PaymentMediaCode = PaymentMediaCode;
	}

	/**
	 * @return the AmountLocal
	 */
	public double getAmountLocal() {
		return AmountLocal;
	}

	public double getAmountLocal(ARMFSBTransactionPayment payment) {
		return payment.getAmountLocal();
	}

	/**
	 * @param AmountLocal
	 *            the AmountLocal to set
	 */
	public void setAmountLocal(double AmountLocal) {
		this.AmountLocal = AmountLocal;
	}

	// PaymentMedia
	public String PaymentMediaCode;
	public double AmountLocal;

	public ARMFSBTransactionPayment(String PaymentMediaCode, double AmountLocal) {

		if (PaymentMediaCode.toUpperCase().contains("CASH")) {
			this.PaymentMediaCode = new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath, "PMC_CASH");

		} else if (PaymentMediaCode.toLowerCase().contains("étrang")
				|| PaymentMediaCode.toUpperCase().contains("FOREIGN")) {

			this.PaymentMediaCode = new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath, "PMC_FOREIGN");
		}

		else if (PaymentMediaCode.toUpperCase().contains("CHECK")) {

			this.PaymentMediaCode = new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath, "PMC_CHECK");
		}

		else if (PaymentMediaCode.toUpperCase().contains("VISA")
				|| (PaymentMediaCode.toUpperCase().contains("MASTER"))
				|| (PaymentMediaCode.toUpperCase().contains("BANCOMAT"))
				|| (PaymentMediaCode.toUpperCase().contains("AMEX"))
				|| (PaymentMediaCode.toUpperCase().contains("MAESTRO"))
				|| (PaymentMediaCode.toUpperCase().contains("DINERS"))
				|| (PaymentMediaCode.toUpperCase().contains("JCB"))
				|| (PaymentMediaCode.toUpperCase().contains("CREDIT"))
				|| (PaymentMediaCode.toUpperCase().contains("CUP"))) {

			this.PaymentMediaCode = new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath, "PMC_CARD");
		}
		
		else if(PaymentMediaCode.toUpperCase().contains("BANQUE")||
				PaymentMediaCode.toUpperCase().contains("BANK")){
			this.PaymentMediaCode = new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath, "PMC_BANK_TRANSFER");
			
		}

		else {

			this.PaymentMediaCode = new ConfigurationManager().getConfig(
					ARMFSBridgeObject.fsBridgeCfgFilePath, "PMC_OTHER");
		}

		this.AmountLocal = AmountLocal;
	}

}
