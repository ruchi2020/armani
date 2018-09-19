/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) definits braces deadcode noctor radix(10) lradix(10)
// Source File Name:   SerializeCorrectionUtil.java

package com.chelseasystems.cs.util;

import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.pos.CMSReduction;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cr.pos.*;
import java.io.File;
import java.util.*;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cs.txnposter.*;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * put your documentation comment here
 */
public class ReadPaymentSummarySerializedObjectUtil {

	/**
	 * put your documentation comment here
	 * @param fileName
	 */
	public void openAndReadFile() {
		try {
			File paySerFile = new File("PaymentSummary.txt");
			System.out.println("" + paySerFile.getAbsolutePath());
			FileWriter fw = new FileWriter(paySerFile);
			BufferedWriter bf = new BufferedWriter(fw);
			String code[] = CMSTxnSummary_EUR.getPaymentCodesByRegister();
			System.out.println("code: " + code.length);
			String sfileType = "===============Payment Summary by payment code=============";
			bf.write(sfileType);
			bf.newLine();
			for (int i = 0; code != null && i < code.length; i++) {
				if (code[i] != null) {
				ArmCurrency cur = CMSTxnSummary_EUR.getPaymentAmountByCodeForRegister(code[i]);
					String toFile = code[i] + " = " + cur.formattedStringValue();
					bf.write(toFile);
					bf.newLine();
				}
			}
			String type[] = CMSTxnSummary_EUR.getPaymentsByRegister();
			System.out.println(type.length);
			sfileType = "===============Payment Summary by payment type=============";
			bf.write(sfileType);
			bf.newLine();
			for (int i = 0; type != null && i < type.length; i++) {
				if (type[i] != null) {
				ArmCurrency cur = CMSTxnSummary_EUR.getPaymentAmountByRegister(type[i]);
					String toFile = type[i] + " = " + cur.formattedStringValue();
					bf.write(toFile);
					bf.newLine();
				}
			}
			bf.flush();
			bf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @param args[]
	 */
	public static void main(String args[]) {
		ReadPaymentSummarySerializedObjectUtil util = new ReadPaymentSummarySerializedObjectUtil();
		util.openAndReadFile();
	}
}
