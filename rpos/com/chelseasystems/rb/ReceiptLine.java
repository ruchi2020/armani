/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.rb;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cs.swing.pos.PaymentApplet;
import com.chelseasystems.cs.util.Version;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.*;
import jpos.*;

// Referenced classes of package com.chelseasystems.rb:
// ReceiptElement, ReceiptMethodElement, ReceiptAuditFile, ReceiptBlueprint,
// PrinterCodeConverter

/**
 * The main function of a Line is to group together a set of elements from a repeating group and control iteratively printing that set until the end
 * is reached. Note that a line is not necessarily a single print line - carriage return is controled at the element level so a line can contain
 * several lines of print.
 * 
 * @author Dan Reading
 */
public class ReceiptLine implements Serializable, POSPrinterConst, JposConst {

	static final long serialVersionUID = -8727643110762742907L;
	private int printerLineLen = 0;
	private boolean isPrinterIBM = false;
	//Vivek Mishra : Added for new Epson printer changes. 
	private boolean isPrinterEpson = false;
	protected ReceiptElement receiptElements[] = null;
	private ReceiptElement currentReceiptElement = null;
	//Vivek Mishra : Added for signature printing
	ConfigMgr mgr = new ConfigMgr("JPOS_peripherals.cfg");
    //Ends
	public ReceiptLine() {
		this(null);
	}

	public ReceiptLine(ReceiptElement receiptElements[]) {
		this.receiptElements = receiptElements;
	}

	public ReceiptLine cloneAsCurrentClassVersion() {
		ReceiptLine currentVersion = new ReceiptLine();
		currentVersion.receiptElements = new ReceiptElement[receiptElements.length];
		for (int i = 0; i < receiptElements.length; i++) {
			currentVersion.receiptElements[i] = receiptElements[i].cloneAsCurrentClassVersion();
		}

		for (int i = 0; i < currentVersion.receiptElements.length; i++) {
			if (currentVersion.receiptElements[i].getDependsOnPresenceOf() != null) {
				for (int j = 0; j < currentVersion.receiptElements.length; j++) {
					if (currentVersion.receiptElements[i].getDependsOnPresenceOf() != currentVersion.receiptElements[j]
							&& currentVersion.receiptElements[i].getDependsOnPresenceOf().equals(currentVersion.receiptElements[j])) {
						currentVersion.receiptElements[i].setDependsOnPresenceOf((ReceiptMethodElement) currentVersion.receiptElements[j]);
					}
				}

			}
		}

		return currentVersion;
	}

	public void printElectronicJournal(ReceiptBlueprint receiptBlueprint) throws Exception {
		boolean allIterationsReturned = false;
		Vector printReadys = new Vector();
		String emailreceipt = "";
		while(allIterationsReturned == false) { // print until end of set
			allIterationsReturned = true;
			for (int i = 0; i < receiptElements.length; i++) { // print each element
				currentReceiptElement = receiptElements[i];
				PrintReady newPrintReady = receiptElements[i].print(receiptBlueprint);
				printReadys.add(newPrintReady);
				if (!receiptElements[i].allIterationsReturned()) {
					allIterationsReturned = false;
				}
			}
		}
		for (Iterator i = printReadys.iterator(); i.hasNext();) {
			PrintReady printReady = (PrintReady) i.next();
			StringBuffer sb = new StringBuffer();
			if (printReady.getStringToPrint().length() > 0) {
				sb.append(printReady.isLineFeed() ? CMSPrinter.CRLF : "");
				sb.append(printReady.getStringToPrint());
				ReceiptAuditFile.getInstance().auditPrint(sb.toString());
			
			//Vivek Mishra : Added for e-receipt 	
			  /* if(receiptBlueprint.getName().equals("CMSCompositePOSTransaction_Sale") && sb.toString()!=null)   
				   emailreceipt=emailreceipt+sb.toString();*/
			}
		}
		//Vivek Mishra : Added for e-receipt
		/*System.out.println("emailreceipt>>>>>>>>>>>>>>>"+emailreceipt);
		System.out.println("PaymentApplet.ereceipt>>>>>>>>>>>>>"+PaymentApplet.ereceipt);*/
        //PaymentApplet.ereceipt=PaymentApplet.ereceipt+emailreceipt;
	}

	public Vector print(POSPrinter posPrinter, ReceiptBlueprint receiptBlueprint) throws Exception {
		setPrinterLineLen(receiptBlueprint.getReceiptInfo().getPrinterLineLen());
		//Vivek Mishra : Modified for new Epson printer changes.
		isPrinterIBM = receiptBlueprint.getReceiptInfo().isPrinterIBM();
		isPrinterEpson = receiptBlueprint.getReceiptInfo().isPrinterEpson();
		boolean allIterationsReturned = false;
		// Repeat the loop thru each of my elements until all elements report
		// that they have exhausted their data source (end of the array);
		Vector printReadys = new Vector();
		while(allIterationsReturned == false) { // print until end of set
			allIterationsReturned = true;
			for (int i = 0; i < receiptElements.length; i++) { // print each element
				currentReceiptElement = receiptElements[i];
				PrintReady newprintReady = receiptElements[i].print(receiptBlueprint);
				printReadys.add(newprintReady);
				if (!receiptElements[i].allIterationsReturned()) {
					allIterationsReturned = false;
				}
			}
		}
		Iterator i = printReadys.iterator();
		StringBuffer sb = new StringBuffer();
		while(i.hasNext()) {
			PrintReady printReady = (PrintReady) i.next();
			if (printReady.isBarCode()) {
				printBuffer(posPrinter, sb.toString(), 0);
				sb = new StringBuffer();
				sb.append(printReady.getStringToPrint());
				String barcodeString = sb.toString();
				int symbology = 0;
				if ("US".equalsIgnoreCase(Version.CURRENT_REGION)){
				//Anjana : Printer has been changed from IBM to EPSON so when transaction number length becomes 13 we were getting the error as "data" too big to fit
					// So symbology was chnged to PTR_BCS_Code128 for Epson to fit the extra length of the txn number
				
				if (CMSPrinter.isIbmBarCode()) {
				symbology = PTR_BCS_Code39;
				} else {
					symbology = PTR_BCS_Code128;
					}
				} else {
					symbology = PTR_BCS_Code128;
				}

				if (isPrinterIBM) {
					barcodeString = formatBarcodeString(barcodeString);
					if (CMSPrinter.isIbmBarCode()) {
						barcodeString = "{A" + barcodeString;
					}
				}
				//Vivek Mishra : Modified for new Epson printer changes.
				if (isPrinterEpson && "US".equalsIgnoreCase(Version.CURRENT_REGION)) {
					barcodeString = formatBarcodeString(barcodeString);
				}
				System.out.println("exit tran mode to print barcode");
				// if(isPrinterIBM) {
				posPrinter.transactionPrint(PTR_S_RECEIPT, PTR_TP_NORMAL);
				receiptBlueprint.setOutOfTranMode();
				posPrinter.printBarCode(PTR_S_RECEIPT, barcodeString, symbology, 100, 200, PTR_BC_CENTER, PTR_BC_TEXT_BELOW);
				// if(isPrinterIBM) {
				// posPrinter.transactionPrint(PTR_S_RECEIPT,PTR_TP_TRANSACTION);
				// }
				sb = new StringBuffer();
			}
			//Vivek Mishra : Added for signature printing
			else if(isPrinterEpson && printReady.getStringToPrint()!=null && printReady.getStringToPrint().contains(".bmp"))
			{
				//posPrinter.printBitmap(2, printReady.getStringToPrint(), 10, 10);
				posPrinter.printBitmap(2, "signature.bmp", mgr.getInt("JELEMENT"), mgr.getInt("KELEMENT"));
			}
			//Ends
			 else if (printReady.isFontDoubleWidth() && printReady.getStringToPrint().length() > 0) {
				printBuffer(posPrinter, sb.toString(), printerLineLen);
				sb = new StringBuffer();
				sb.append(printReady.isLineFeed() ? CMSPrinter.CRLF : "");
				sb.append(CMSPrinter.ESC + "|N");
				sb.append(printReady.isCentered() ? CMSPrinter.ESC + "|cA" : "");
				sb.append(CMSPrinter.ESC + "|2C");
				sb.append(printReady.getStringToPrint());
				printBuffer(posPrinter, sb.toString(), printerLineLen / 2);
				sb = new StringBuffer();
			} else {
				if (printReady.getStringToPrint().length() > 0) {
					sb.append(printReady.isLineFeed() ? CMSPrinter.CRLF : "");
					sb.append(CMSPrinter.ESC + "|N");
					sb.append(printReady.isCentered() ? CMSPrinter.ESC + "|cA" : "");
					sb.append(printReady.getStringToPrint());
				}
			}
		}
		if (sb.length() > 0) {
			printBuffer(posPrinter, sb.toString(), printerLineLen);
		}
		return printReadys;
	}

	/**
	 * @param barcodeString
	 * @return
	 */
	private String formatBarcodeString(String barcodeString) {
		if (barcodeString.lastIndexOf("*") == -1) {
			return barcodeString;
		}
		StringTokenizer st = new StringTokenizer(barcodeString, "*");
		StringBuffer buildReturnString = new StringBuffer();
		String component;
		for (; st.hasMoreTokens(); buildReturnString.append(component)) {
			component = st.nextToken();
			for (int i = component.length() - 5; i < 0; i++) {
				buildReturnString.append("0");
			}

		}

		return buildReturnString.toString();
	}

	public void printBuffer(POSPrinter posPrinter, String printBuffer, int widthToFill) throws Exception {
		boolean escapeCode = false;
		if (printBuffer.length() == 0) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		int lineLen = 0;
		for (int i = 0; i < printBuffer.length(); i++) {
			String nextChar = printBuffer.substring(i, i + 1);
			if (nextChar.equals(CMSPrinter.CRLF)) {
				lineLen = 0;
			} else if (nextChar.equals(CMSPrinter.ESC)) {
				escapeCode = true;
			} else if (escapeCode && Character.isUpperCase(nextChar.charAt(0))) {
				escapeCode = false;
			} else if (!escapeCode) {
				lineLen++;
			}
			if (lineLen > printerLineLen) {
				lineLen = 1;
				sb.append(CMSPrinter.CRLF);
			}
			sb.append(nextChar);
		}

		String printString = sb.toString();
		if (PrinterCodeConverter.isCharacterConversionNeeded()) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < sb.length(); i++) {
				char c = sb.charAt(i);
				char c2 = PrinterCodeConverter.switchCharacter(c);
				buffer.append(c2);
			}

			printString = buffer.toString();
		}
		// not nec in transaction mode
		// if(isPrinterIBM)
		// {
		// printString = padLastLineWithSpaces(printString, widthToFill);
		// if(printString.startsWith(CMSPrinter.CRLF))
		// if(printString.length() > 1)
		// posPrinter.printNormal(PTR_S_RECEIPT, printString.substring(1));
		// else
		// posPrinter.printNormal(PTR_S_RECEIPT, " ");
		// else
		// posPrinter.printNormal(PTR_S_RECEIPT, printString);
		// }
		// else {
		// System.out.println("sending to printer: " + printString);
		posPrinter.printNormal(PTR_S_RECEIPT, printString);
		// }
	}

	// private String padLastLineWithSpaces(String printString, int widthToFill)
	// {
	// // this is not necessary in transaction mode....
	// if(widthToFill == 0)
	// return(printString);
	// char CRLFchar = CMSPrinter.CRLF.charAt(0);
	// // find the offset from the end of the line of the CRLF
	// int len = printString.length();
	// int offset = 0;
	// while(--len != 0 && printString.charAt(len) != CRLFchar)
	// {
	// offset++;
	// }
	// if(printString.charAt(offset) == CRLFchar)
	// {
	// offset--;
	// }
	// // then fill it out with spaces to desired width to fill
	// StringBuffer filledString = new StringBuffer(printString);
	// while(offset++ < widthToFill)
	// filledString.append(" ");
	// return(filledString.toString());
	// }
	/**
	 * @return
	 */
	public ReceiptElement getCurrentReceiptElement() {
		return currentReceiptElement;
	}

	/**
	 * @return
	 */
	public ReceiptElement[] getReceiptElements() {
		return receiptElements;
	}

	/**
	 * @param receiptElements
	 */
	public void setReceiptElements(ReceiptElement receiptElements[]) {
		this.receiptElements = receiptElements;
	}

	/**
	 * @param printerLineLen
	 */
	public void setPrinterLineLen(int printerLineLen) {
		this.printerLineLen = printerLineLen;
	}

}
