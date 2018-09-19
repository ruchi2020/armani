/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.rb;

import jpos.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cr.util.*;
import java.io.Serializable;
import java.awt.*;
import java.text.*;
import java.util.*;

/**
 * The element contains the instructions for a piece of print on a receipt.  An element
 * is a literal, it is extended by ReceiptMethodElement which is for values pulled from an object.
 * The basic job of the element is to return a formated string to print.  An element may
 * be made dependent upon the existence of another element (eg, only print this heading if the
 * corresponding data exists).  An element also specifies carriage return.
 * @author Dan Reading
 */
public class ReceiptElement implements Serializable, JposConst, POSPrinterConst, AttributeConstants {
	private HashMap foreignStringsToPrint;
	//private MutableString franceStringToPrint = new MutableString("NO_FRENCH_VALUE");
	//private MutableString canadaStringToPrint = new MutableString("NO_CANADIAN_VALUE");
	//private MutableString mexicoStringToPrint = new MutableString("NO_MEXICAN_VALUE");
	static final long serialVersionUID = -7157919414267272568L;
	protected MutableString stringToPrint = new MutableString();
	protected MutableBoolean isFontDoubleWidth = new MutableBoolean();
	protected MutableBoolean isPrintedAsBarcode = new MutableBoolean();
	protected MutableBoolean isLineFeed = new MutableBoolean();
	//protected ReceiptMethodElement dependsOnPresenceOf = null;
	protected MutableBoolean isPrecededBySpace = new MutableBoolean(true);
	protected MutableInteger specifiedSize = new MutableInteger();
	protected MutableString justified = new MutableString();
	protected MutableString dateFormat = new MutableString(this.DATE_FORMAT_MONTHDDYYYY);
	protected MutableBoolean isSignReversed = new MutableBoolean(false);
	protected MutableString leadingFormatPositive = new MutableString();
	protected MutableString trailingFormatPositive = new MutableString();
	protected MutableString leadingFormatNegative = new MutableString();
	protected MutableString trailingFormatNegative = new MutableString();
	protected MutableString valuePrintedIfTrue = new MutableString();
	protected MutableString valuePrintedIfFalse = new MutableString();
	protected MutableInteger precision = new MutableInteger(2);
	protected MutableBoolean printIfZero = new MutableBoolean(false);
	protected MutableBoolean printIfLengthZero = new MutableBoolean(false);
	protected MutableString displayMethodPath = new MutableString("");
	protected MutableString multiplyBy = new MutableString();
	//  What I am doing here is to redefine a reference to these pointers so
	//  I can access them as a group (and change the contents of their references
	//  when I need to) in an array or with getters and setters as specific
	//  named items.
	protected Object[][] nameValuePairs = { { ATTR_DEPENDS_ON, null }, { ATTR_VALUE_TO_PRINT, stringToPrint }, { ATTR_FIXED_LENGTH, specifiedSize }, { ATTR_LINE_FEED, isLineFeed },
			{ ATTR_JUSTIFIED, justified }, { ATTR_DOUBLE_WIDTH, isFontDoubleWidth }, { ATTR_BARCODE, isPrintedAsBarcode }, { ATTR_PRECEDED_BY_SPACE, isPrecededBySpace },
			{ ATTR_REVERSE_SIGN, isSignReversed }, { ATTR_LEADING_FORMAT_POSITIVE, leadingFormatPositive }, { ATTR_TRAILING_FORMAT_POSITIVE, trailingFormatPositive },
			{ ATTR_LEADING_FORMAT_NEGATIVE, leadingFormatNegative }, { ATTR_TRAILING_FORMAT_NEGATIVE, trailingFormatNegative }, { ATTR_DATE_FORMAT, dateFormat },
			{ ATTR_PRINT_IF_TRUE, valuePrintedIfTrue }, { ATTR_PRINT_IF_FALSE, valuePrintedIfFalse }, { ATTR_PRECISION, precision }, { ATTR_CLASS_METHODS, displayMethodPath },
			{ ATTR_PRINT_IF_ZERO, printIfZero }, { ATTR_PRINT_IF_LENGTH_ZERO, printIfLengthZero }, { ATTR_MULTIPLY_BY, multiplyBy } };
	private Locale locale;

	/**
	 */
	public ReceiptElement() {
		this("");
	}

	/**
	 */
	public ReceiptElement(String stringToPrint) {
		this.stringToPrint.value = stringToPrint;
		justified.value = JUST_LEFT;
		foreignStringsToPrint = new HashMap();
		loadLocales();
	}

	private void loadLocales() {
		Locale[] locales = LocaleManager.getInstance().getSupportedLocales();
		for (int i = 0; i < locales.length; i++) {
			if (locales[i].equals(LocaleManager.getInstance().getDefaultLocale())) {
			} else if (foreignStringsToPrint.containsKey(locales[i])) {
			} else
				foreignStringsToPrint.put(locales[i], new MutableString("NO_FOREIGN_VALUE"));
		}
	}

	public ReceiptElement cloneAsCurrentClassVersion() {
		ReceiptElement currentVersion = new ReceiptElement();
		setElements(currentVersion);
		return (currentVersion);
	}

	public Locale getLocale() {
		return (locale);
	}

	public void setLocale(Locale locale) {
		if (foreignStringsToPrint == null) // eg this was serialized prior to the locale enhancements
			return;

		if (locale.equals(LocaleManager.getInstance().getDefaultLocale())) {
			nameValuePairs[1][1] = stringToPrint;
			this.locale = locale;
		} else if (foreignStringsToPrint.containsKey(locale)) {
			nameValuePairs[1][1] = foreignStringsToPrint.get(locale);
			this.locale = locale;
		}
	}

	protected void setElements(ReceiptElement currentVersion) {
		if (foreignStringsToPrint != null)
			currentVersion.foreignStringsToPrint = foreignStringsToPrint;
		currentVersion.loadLocales(); // add any new locales supported since this class last instantiated
		if (stringToPrint != null)
			currentVersion.stringToPrint.value = stringToPrint.value;
		if (isFontDoubleWidth != null)
			currentVersion.isFontDoubleWidth.value = isFontDoubleWidth.value;
		if (isPrintedAsBarcode != null)
			currentVersion.isPrintedAsBarcode.value = isPrintedAsBarcode.value;
		if (isLineFeed != null)
			currentVersion.isLineFeed.value = isLineFeed.value;
		if (isPrecededBySpace != null)
			currentVersion.isPrecededBySpace.value = isPrecededBySpace.value;
		if (specifiedSize != null)
			currentVersion.specifiedSize.value = specifiedSize.value;
		if (justified != null)
			currentVersion.justified.value = justified.value;
		if (dateFormat != null)
			currentVersion.dateFormat.value = dateFormat.value;
		if (isSignReversed != null)
			currentVersion.isSignReversed.value = isSignReversed.value;
		if (leadingFormatPositive != null)
			currentVersion.leadingFormatPositive.value = leadingFormatPositive.value;
		if (trailingFormatPositive != null)
			currentVersion.trailingFormatPositive.value = trailingFormatPositive.value;
		if (leadingFormatNegative != null)
			currentVersion.leadingFormatNegative.value = leadingFormatNegative.value;
		if (trailingFormatNegative != null)
			currentVersion.trailingFormatNegative.value = trailingFormatNegative.value;
		if (valuePrintedIfTrue != null)
			currentVersion.valuePrintedIfTrue.value = valuePrintedIfTrue.value;
		if (valuePrintedIfFalse != null)
			currentVersion.valuePrintedIfFalse.value = valuePrintedIfFalse.value;
		if (precision != null)
			currentVersion.precision.value = precision.value;
		if (printIfZero != null)
			currentVersion.printIfZero.value = printIfZero.value;
		if (printIfLengthZero != null)
			currentVersion.printIfLengthZero.value = printIfLengthZero.value;
		if (displayMethodPath != null)
			currentVersion.displayMethodPath.value = displayMethodPath.value;
		if (multiplyBy != null)
			currentVersion.multiplyBy.value = multiplyBy.value;
		if (getDependsOnPresenceOf() != null) {
			currentVersion.setDependsOnPresenceOf(getDependsOnPresenceOf());
			//if(currentVersion.getDependsOnPresenceOf() == null)
			//   System.out.println("set not working");
			//else {
			//   System.out.println("set working...");
			//   System.out.println(currentVersion.nameValuePairs[0][1]);
			//}
		}
	}

	/**
	 * @param receiptBlueprint
	 * @return
	 * @exception Exception
	 */
	public PrintReady print(ReceiptBlueprint receiptBlueprint) throws Exception {
		PrintReady printReady = new PrintReady();
		if (isPrintedAsBarcode()) {
			printReady.isLineFeed(isLineFeed.value);
			//printReady.setStringToPrint("{A" + getStringToPrint(receiptBlueprint).trim());
			printReady.setStringToPrint(getStringToPrint(receiptBlueprint).trim());
			printReady.isBarCode(true);
		} else {
			printReady.isCentered(justified.value.equals(JUST_CENTER));
			printReady.isLineFeed(isLineFeed.value);
			printReady.isFontDoubleWidth(isFontDoubleWidth.value);
			printReady.setStringToPrint(getStringToPrintWithFormattingInfo(receiptBlueprint));
		}
		return (printReady);
	}

	/**
	 * The supported functions the user may choose from.
	 */
	public static String[] getFunctions() {
		return (new String[] { "@FX MM/DD/YYYY", "@FX MONTH DD, YYYY", "@FX HH:MM:SS", "@FX H:MM a", "@FX H:MM:SS a", "@FX DD/MM/YYYY", "@FX YYYY/MM/DD" });
	}

	/**
	 * @param receiptBlueprint
	 * @return
	 * @exception Exception
	 */
	public String getStringToPrint(ReceiptBlueprint receiptBlueprint) throws Exception {
		// if I am dependent upon a variable element and that element is not present, print nothing
		if (getDependsOnPresenceOf() != null && !getDependsOnPresenceOf().isPresent(receiptBlueprint))
			return ("");

		String holdStringToPrint;

		if (nameValuePairs[1][1].equals("NO_FOREIGN_VALUE"))
			holdStringToPrint = stringToPrint.value;
		else
			holdStringToPrint = ((MutableString) nameValuePairs[1][1]).value;
		if (holdStringToPrint.equals("@FX MM/DD/YYYY")) {
			SimpleDateFormat df = new SimpleDateFormat("M/dd/yyyy");
			return (df.format(new Date()));
		} else if (holdStringToPrint.equals("@FX DD/MM/YYYY")) {
			SimpleDateFormat df = new SimpleDateFormat("d/MM/yyyy");
			return (df.format(new Date()));
		} else if (holdStringToPrint.equals("@FX MONTH DD, YYYY")) {
			SimpleDateFormat df = new SimpleDateFormat("MMMMMMMMM d, yyyy");
			return (df.format(new Date()));
		} else if (holdStringToPrint.equals("@FX HH:MM:SS")) {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			return (df.format(new Date()));
		} else if (holdStringToPrint.equals("@FX H:MM a")) {
			SimpleDateFormat df = new SimpleDateFormat("h:mm a");
			return (df.format(new Date()));
		} else if (holdStringToPrint.equals("@FX H:MM:SS a")) {
			SimpleDateFormat df = new SimpleDateFormat("h:mm:ss a");
			return (df.format(new Date()));
		} else if (holdStringToPrint.equals("@FX YYYY/MM/DD")) {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			return (df.format(new Date()));

		}
		return (holdStringToPrint);
	}

	/**
	 * @param stringToPrint
	 */
	public void setStringToPrint(String stringToPrint) {
		this.stringToPrint.value = stringToPrint;
	}

	/**
	 * @return
	 */
	public ReceiptMethodElement getDependsOnPresenceOf() {
		return (ReceiptMethodElement) nameValuePairs[0][1];
	}

	public void setDependsOnPresenceOf(ReceiptMethodElement receiptMethodElement) {
		nameValuePairs[0][1] = receiptMethodElement;
	}

	/**
	 * Add the specified formatting information to the print buffer.  This
	 * might vary by printer target?
	 */
	public String getStringToPrintWithFormattingInfo(ReceiptBlueprint receiptBlueprint) throws Exception {
		String stringToPrint = getStringToPrint(receiptBlueprint);
		//System.out.println("String to print: " + stringToPrint);
		if (stringToPrint.length() == 0) {
			//System.out.println("empty, don't append system info");
			return ("");
		}
		StringBuffer sb = new StringBuffer();
		// no longer is formatting info embedded in the string.
		// instead the string is encapsulated in a PrintReady along with it's
		// formatting info so the print implementation can decide how to render the format
		//sb.append(isLineFeed.value ? CMSPrinter.CRLF : "");
		//sb.append(isTextCentered() ? CMSPrinter.ESC + "|cA" : "");
		//sb.append(isFontDoubleWidth.value ? CMSPrinter.ESC + "|2C" : "");
		sb.append(stringToPrint);
		return (sb.toString());
	}

	/**
	 * @return what this element should be represented as in the GUI.
	 * @see <code>MethodElement</code>
	 */
	public String getArchitectString() {
		String holdStringToPrint;
		if (nameValuePairs[1][1].equals("NO_FOREIGN_VALUE"))
			holdStringToPrint = stringToPrint.value;
		else
			holdStringToPrint = ((MutableString) nameValuePairs[1][1]).value;
		return (holdStringToPrint);
		//return  stringToPrint.value;
	}

	/**
	 * @return whether this element is complete.
	 * @see <code>MethodElement</code>
	 */
	public boolean allIterationsReturned() {
		return (true);
	}

	/**
	 * @return printable representation of the object.
	 * @see <code>MethodElement</code>
	 */
	private String convertToString(Object printableObject) {
		return (printableObject.toString());
	}

	/**
	 * @return
	 */
	public boolean isLineFeed() {
		return (isLineFeed.value);
	}

	/**
	 * @param isLineFeed
	 */
	public void isLineFeed(boolean isLineFeed) {
		this.isLineFeed.value = isLineFeed;
	}

	/**
	 * @return
	 */
	public boolean isTextCentered() {
		return (justified.value.equals(JUST_CENTER));
	}

	/**
	 * @param isTextCentered
	 */
	public void isTextCentered(boolean isTextCentered) {
		if (isTextCentered)
			justified.value = JUST_CENTER;
	}

	/**
	 * @return
	 */
	public boolean isJustRight() {
		return (justified.value.equals(JUST_RIGHT));
	}

	/**
	 * @param justRight
	 */
	public void isJustRight(boolean justRight) {
		if (justRight)
			justified.value = JUST_RIGHT;
	}

	/**
	 * @return
	 */
	public boolean isFontDoubleWidth() {
		return (isFontDoubleWidth.value);
	}

	/**
	 * @param isFontDoubleWidth
	 */
	public void isFontDoubleWidth(boolean isFontDoubleWidth) {
		this.isFontDoubleWidth.value = isFontDoubleWidth;
	}

	/**
	 * @return
	 */
	public boolean isPrintedAsBarcode() {
		return (isPrintedAsBarcode.value);
	}

	/**
	 * @param isPrintedAsBarcode
	 */
	public void isPrintedAsBarcode(boolean isPrintedAsBarcode) {
		this.isPrintedAsBarcode.value = isPrintedAsBarcode;
	}

	public Double getMultiplyBy() {
		if (multiplyBy == null)
			return null;
		if (multiplyBy.value == null || multiplyBy.value.length() == 0)
			return null;
		return new Double(multiplyBy.value);
	}

	/**
	 * @return
	 */
	public boolean isPrecededBySpace() {
		return (false);
	}

	/**
	 * @param precededBySpace
	 */
	public void isPrecededBySpace(boolean precededBySpace) {
	}

	/**
	 * @return
	 */
	public int getSpecifiedSize() {
		if (Arrays.asList(getFunctions()).contains(stringToPrint.value))
			return (0);
		try {
			//return this.getStringToPrint(null).length();
			// don't want to test for dependecy presence in this case
			return (stringToPrint.value.length());
		} catch (Exception e) {
			return (0);
		} // this can't really happen
	}

	/**
	 * @param specifiedSize
	 */
	public void setSpecifiedSize(int specifiedSize) {
	}

	/**
	 * @return
	 */
	public Object[][] getNameValuePairs() {
		specifiedSize.value = getSpecifiedSize();
		return (nameValuePairs);
	}

	/**
	 * @return gui font style in the ReceiptArchitect
	 */
	public int getArchitectFontStyle() {
		return (Font.PLAIN);
	}

	/**
	 * @return gui font name in the ReceiptArchitect
	 */
	public String getArchitectFontName() {
		return ("DialogInput");
	}
}
