/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */

package com.chelseasystems.rb;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.xml.XMLUtil;

import java.util.*;
import java.text.*;
import com.chelseasystems.oi.*;
import java.awt.*;
import java.text.*;

/**
 * A variable receipt element, provides the additional intelligence to ReceiptElement
 * to pass back a printable string from an Object.  Understands how to iterate thru
 * an array structure using "iterator nodes" so the next value is passed on successive
 * requests for my print String.
 * @author Dan Reading
 */
public class ReceiptMethodElement extends ReceiptElement implements AttributeConstants {

	static final long serialVersionUID = -1470494375300547957L;
	protected Object[][] classMethodPairs;
	protected int argumentOrdinal = 0;
	private static final int UNSPECIFIED = 0;
	private boolean suppressAdvance = false;
	private boolean allIterationsReturned = true;
	private String lastStringPrinted;
	private String encoding = "";

	/**
	 */
	public ReceiptMethodElement() {
	}

	/**
	 * @param    MethodPath methodPath
	 */
	public ReceiptMethodElement(MethodPath methodPath) {
		NodeObject[] nodeObjects = methodPath.getNodeObjects();
		// discard the first (root node) and last (sample object returned by method) node object
		// that's why you see the weird index constraints
		Vector buildClassMethodPairs = new Vector();
		StringBuffer sb = new StringBuffer();
		try {
			for (int x = 1, y = 0; y < nodeObjects.length / 2 - 1; y++) {
				// nodeObjects in the array from Object Inspector alternate between objects and methods,
				// while we want to load classMethodPairs with pairs of class objects
				// and method names.  So, basically convert a one dim array into a two dim array.
				//
				// Note: After some bitter experience, I changed the class objects to class names and
				// instantiate the classes on-the-fly when I need them.  This makes the element better
				// able to survive the creation of new versions of the objects it is navigating -
				// so long as the class names and method names remain the same, everything
				// will still work.
				//
				// By the way, we also need to insert an "iterator node" whenever we see a
				// type of object that is a repeating structure (eg Array)
				Object[] classMethodPair = new Object[2];
				//classMethodPair[0] = nodeObjects[x++].oLevel.getClass();
				// avoid storing the classes themselves because run into serialver problems
				Class currentClass = nodeObjects[x].oLevel.getClass();
				classMethodPair[0] = nodeObjects[x++].oLevel.getClass().getName();
				classMethodPair[1] = ((MethodWrapper) nodeObjects[x++].oLevel).getName();
				buildClassMethodPairs.add(classMethodPair);
				//System.out.println("the class name is: " +  (String)classMethodPair[0]);
				//System.out.println("the method name is: " + (String)classMethodPair[1]);
				Class returnType = currentClass.getMethod((String) classMethodPair[1], null).getReturnType();
				sb.append("\nUse class " + currentClass.getName());
				sb.append("\nExecute " + (String) classMethodPair[1] + "\n     yielding a " + returnType.getName());
				if (returnType.isArray()) {
					Object[] arrayIteratorNode = new Object[2];
					arrayIteratorNode[0] = "Array Iterator Node";
					arrayIteratorNode[1] = new Integer(0);
					buildClassMethodPairs.add(arrayIteratorNode);
				}
				//else {
				//   if (((String)classMethodPair[1]).equals("toArray")) {
				//      System.out.println("I have found a toArray, adding an iterator node");
				//      Object[] arrayIteratorNode = new Object[2];
				//      arrayIteratorNode[0] = "Array Iterator Node";
				//      arrayIteratorNode[1] = new Integer(0);
				//      buildClassMethodPairs.add(arrayIteratorNode);
				//   }
				//}
			}
		} catch (Exception e) {
			System.out.println("excdeption on create receipt method element: " + e);
		}
		classMethodPairs = new Object[buildClassMethodPairs.size()][2];
		//System.out.println("Method element built, following are class/method pairs:  ");
		for (int i = 0; i < classMethodPairs.length; i++) {
			Object[] classMethodPair = (Object[]) buildClassMethodPairs.elementAt(i);
			classMethodPairs[i][0] = classMethodPair[0];
			classMethodPairs[i][1] = classMethodPair[1];
			//System.out.println("Class " + classMethodPairs[i][0] +
			//  "  Method: " + classMethodPairs[i][1]);
		}
		displayMethodPath.setValue(sb.toString().substring(1));
		// set the superclass value for what to show in architect
		stringToPrint.setValue(this.getArchitectString());
		setSpecifiedSize(0);
		//System.out.println("setting initial specified size");
	}

	public ReceiptElement cloneAsCurrentClassVersion() {
		ReceiptMethodElement currentVersion = new ReceiptMethodElement();
		setElements(currentVersion);
		currentVersion.classMethodPairs = classMethodPairs;
		currentVersion.argumentOrdinal = argumentOrdinal;
		return (currentVersion);
	}

	/**
	 * This is a kludge to get the default combobox renderer to show what I want
	 * when loaded with objects of this class for the "depends on" attribute...
	 * @todo write a custom renderer that knows how to render this class
	 */
	public String toString() {
		return (getArchitectString());
	}

	/**
	 * Get the trail of Class/Method pairs to run
	 */
	public Object[][] getClassMethodsToRun() {
		return (classMethodPairs);
	}

	/**
	 * @param classMethodPairs
	 */
	public void setClassMethodsToRun(Object[][] classMethodPairs) {
		this.classMethodPairs = classMethodPairs;
	}

	/**
	 * @return
	 */
	public int getSpecifiedSize() {
		return (specifiedSize.value);
	}

	/**
	 * @param specifiedSize
	 */
	public void setSpecifiedSize(int specifiedSize) {
		this.specifiedSize.value = specifiedSize;
	}

	/**
	 * @return
	 */
	public int getArgumentOrdinal() {
		return (argumentOrdinal);
	}

	/**
	 * @param argumentOrdinal
	 */
	public void setArgumentOrdinal(int argumentOrdinal) {
		this.argumentOrdinal = argumentOrdinal;
	}

	/**
	 * @return
	 */
	public boolean allIterationsReturned() {
		return (allIterationsReturned);
	}

	/**
	 * Another element may wish to know if I am present (eg, my heading will
	 * want to know if I exist, if I don't, he may want to suppress his print).
	 */
	public boolean isPresent(ReceiptBlueprint receiptBlueprint) throws Exception {
		suppressAdvance = true;
		// OK, figure out if the guy who depends on my presence prints before me or after me
		// in the LineGroup and then see what my prior or next value is accordingly
		// (Since of course I could be in a repeating group, I want to make sure I respond at the
		// same occurence level as my requestor).
		ReceiptElement[] currentReceiptElements = receiptBlueprint.getCurrentReceiptReport().getCurrentReceiptLine().getReceiptElements();
		ReceiptElement whoWantsToKnow = receiptBlueprint.getCurrentReceiptReport().getCurrentReceiptLine().getCurrentReceiptElement();
		int positionOfWhoWantsToKnow = ReceiptBuilderUtility.findIndexOf(whoWantsToKnow, currentReceiptElements);
		int positionOfMe = ReceiptBuilderUtility.findIndexOf(this, currentReceiptElements);
		String dependentString;
		if (positionOfMe > positionOfWhoWantsToKnow)
			dependentString = getStringToPrint(receiptBlueprint);
		else
			dependentString = lastStringPrinted;
		suppressAdvance = false;
		boolean returnValue = !isEmptyValue(dependentString);
		return (returnValue);
	}

	/**
	 * What does "empty" really mean anyway?  Change the meaning here if certain values
	 * should always be considered not present.
	 * Is zero a value can be conditionally controlled by setting suppres printing of
	 * zero flag in blueprint (if zero means "not present" for your circumstance).
	 */
	private boolean isEmptyValue(String testEmpty) {
		if (testEmpty == null || testEmpty.length() == 0) {
			return (true);
		} else {
			return (false);
		}
		// if there are any characters besides these anywhere in the string, then
		// it is not empty.
		//      for (int i = 0; i < testEmpty.length(); i++) {
		//         if (!(testEmpty.substring(i, i + 1).equals("0") || testEmpty.substring(i,
		//               i + 1).equals(" ") || testEmpty.substring(i, i + 1).equals("$")
		//               || testEmpty.substring(i, i + 1).equals("-") || testEmpty.substring(i,
		//               i + 1).equals(".")))
		//            return  false;
		//      }
		//      return  true;
	}

	/**
	 * Apply the navigation trail specified by the Architect when I was designed
	 * against the object that I am being applied against now and return the
	 * <code>String</code> at the end of the rainbow.
	 */
	public String getStringToPrint(ReceiptBlueprint receiptBlueprint) throws Exception {
		if (getDependsOnPresenceOf() != this) {
			if (getDependsOnPresenceOf() != null && !getDependsOnPresenceOf().isPresent(receiptBlueprint)) {
				//System.out.println("returning nothing to print because dependent element is not present");
				lastStringPrinted = "";
				return ("");
			}
		}
		Object[] receiptObjects = receiptBlueprint.getReceiptObjects();
		Object baseObjectToRunAgainst = receiptObjects[argumentOrdinal];
		Object derivedObject = baseObjectToRunAgainst;
		allIterationsReturned = true;
		for (int i = 0; i < classMethodPairs.length; i++) {
			//System.out.println("here 1");
			if (derivedObject == null || (derivedObject.getClass().isArray() && Array.getLength(derivedObject) == 0)) {
				// if we find a null return or an empty array in the method path, then return "" and stop iterating
				allIterationsReturned = true;
				lastStringPrinted = "";
				return ("");
			}
			if (derivedObject.getClass().isArray()) {
				Object arrayObject = derivedObject;
				derivedObject = Array.get(derivedObject, ((Integer) classMethodPairs[i][1]).intValue());
				if (!suppressAdvance) {
					classMethodPairs[i][1] = new Integer(((Integer) classMethodPairs[i][1]).intValue() + 1);
					if (Array.getLength(arrayObject) != ((Integer) classMethodPairs[i][1]).intValue()) {
						allIterationsReturned = false;
					} else {
						// reset back to zero when done so primed for the next copy
						classMethodPairs[i][1] = new Integer(0);
					}
				}
			} else {
				try {
					//               derivedObject = derivedObject.getClass().getMethod((String)classMethodPairs[i][1],
					//                     null).invoke(derivedObject, null);
					Method deriver = derivedObject.getClass().getMethod((String) classMethodPairs[i][1], null);
					derivedObject = deriver.invoke(derivedObject, null);
				} catch (NoSuchMethodException nsme) {
					System.out.println("Receipt Builder, method not found returning null: " + (String) classMethodPairs[i][1]);
					derivedObject = null;
				} catch (Exception e) {
					System.out.println("exception on invoking method to build receipt");
					throw (e); // also throw exception back to client to display appmgr exception dialogue
				}
			}
		}
		String stringToReturn = convertToString(derivedObject);
		if (stringToReturn.length() > 0 || this.isPrintedIfLengthZero())
			stringToReturn = sizeToSpecification(justify(stringToReturn));
		//System.out.println("derived string: " + stringToReturn);
		if (getDependsOnPresenceOf() == this) // how could that be?
			if (isEmptyValue(stringToReturn)) {
				lastStringPrinted = "";
				return ("");
			}
		if (isPrecededBySpace() && stringToReturn.length() > 0)
			lastStringPrinted = " " + stringToReturn;
		else
			lastStringPrinted = stringToReturn;
		return (lastStringPrinted);
	}

	/**
	 * At design time, return a representative string for what the method returns.
	 * This will display in the architect.
	 * @todo a tool tip that shows the method path would be really cool.
	 */
	public String getArchitectString() {
		String stringToReturn;
		int i = classMethodPairs.length - 1;
		return ((String) classMethodPairs[i][1]).substring(((String) classMethodPairs[i][1]).lastIndexOf("get") + 3);
	}

	/**
	 * The object can be a ArmCurrency, Float, Int, etc. figure out what it is,
	 * then run the appropriate method on it to convert it to a formatted
	 * string, eg if currency return (ArmCurrency)printableObject.toFormatedString()
	 * @todo Allow the user to specify a format string based on object type
	 */
	private String convertToString(Object printableObject) {
		if (printableObject == null)
			return ("");
		String returnString;
		//System.out.println("the convert to string type is: " + printableObject.getClass().getName());
		if (printableObject.getClass().getName().endsWith("Currency")) {
			try {
				if (!isPrintedIfZero() && ((ArmCurrency) printableObject).doubleValue() == 0)
					returnString = "";
				else {
					printableObject = formatCurrency((ArmCurrency) printableObject);
					Method format = Class.forName(printableObject.getClass().getName()).getMethod("formattedStringValue", null);
					returnString = (String) format.invoke(printableObject, null);
				}
			} catch (Exception e) {
				System.out.println("error formatting currency" + e);
				e.printStackTrace();
				returnString = "** error **";
			}
		} else if (printableObject instanceof Date)
			returnString = formatDate((Date) printableObject);
		else if (printableObject instanceof Boolean) {
			returnString = ((Boolean) printableObject).booleanValue() ? getValuePrintedIfTrue() : getValuePrintedIfFalse();
		} else if (printableObject instanceof Double || printableObject instanceof Integer || printableObject instanceof Float)
			returnString = formatNumber(printableObject.toString());
		else if (printableObject instanceof ResourceBundleKey)
			returnString = ResourceManager.getResourceBundle(this.getLocale()).getString(((ResourceBundleKey) printableObject).getKey());
		else {
			returnString = printableObject.toString();
		}
		return (returnString);
	}

	/**
	 * @param stringToSize
	 * @return
	 */
	private String sizeToSpecification(String stringToSize) throws UnsupportedEncodingException {
		if (specifiedSize.value == UNSPECIFIED)
			return (stringToSize);
		encoding = XMLUtil.getEncoding();
		if (isJustRight()) {
			if (specifiedSize.value < stringToSize.getBytes(encoding).length) {
				return (stringToSize.substring(stringToSize.getBytes(encoding).length - specifiedSize.value));
			} else {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < specifiedSize.value - stringToSize.getBytes(encoding).length; i++)				
					sb.append(" ");
				return (sb.toString() + stringToSize);
			}
		} else {
			if (specifiedSize.value < stringToSize.getBytes(encoding).length) {
				//Modified by deepika to handle the StringOutofBoundsException for chinese locale in ARM1.6
				//return (stringToSize.substring(0, specifiedSize.value));
				return (stringToSize.substring(0,stringToSize.length()));
			} else {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < specifiedSize.value - stringToSize.getBytes(encoding).length; i++) {
					sb.append(" ");
				}
				return (stringToSize + sb.toString());
			}
		}
	}

	/**
	 * I believe the printer will handle centering.
	 */
	private String justify(String stringToJust) {
		if (!isJustRight())
			return (stringToJust);
		StringBuffer sb = new StringBuffer(stringToJust.getBytes().length);
		int i = stringToJust.length() - 1;
		for (; i > 0 && stringToJust.substring(i, i + 1).equals(" "); i--) {
			sb.append(" ");
		}
		i++;
		for (int a = 0; a < i; a++) {
			sb.append(stringToJust.substring(a, a + 1));
		}
		return (sb.toString());
	}

	public ArmCurrency formatCurrency(ArmCurrency currency) {
		if (isSignReversed())
			currency = currency.multiply(-1d);
		return (currency);
	}

	/**
	 * @param date
	 * @return
	 */
	public String formatDate(Date date) {
		String dateFormatSpec = getDateFormat();
		if (dateFormatSpec.equals(this.DATE_FORMAT_MMDDYYYY)) {
			SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
			return (formatter.format(date));
		} else if (dateFormatSpec.equals(DATE_FORMAT_MONTHDDYYYY)) {
			SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
			return (formatter.format(date));
		} else if (dateFormatSpec.equals(DATE_FORMAT_YYYYMMDD)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d");
			return (formatter.format(date));
		} else if (dateFormatSpec.equals(DATE_FORMAT_MMDD)) {
			SimpleDateFormat formatter = new SimpleDateFormat("M/dd");
			return (formatter.format(date));
		} else if (dateFormatSpec.equals(DATE_FORMAT_HH_MM_A)) {
			SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
			return (formatter.format(date));
		} else if (dateFormatSpec.equals(DATE_FORMAT_HH_MM)) {
			SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
			return (formatter.format(date));
		} else if (dateFormatSpec.equals(DATE_FORMAT_DAY_OF_WEEK_A)) {
			SimpleDateFormat formatter = new SimpleDateFormat("EEE");
			return (formatter.format(date));
		} else if (dateFormatSpec.equals(DATE_FORMAT_DAY_OF_WEEK)) {
			SimpleDateFormat formatter = new SimpleDateFormat("EEEEEEEEE");
			return (formatter.format(date));
		} else if (dateFormatSpec.equals(DATE_FORMAT_DDMM)) {
			SimpleDateFormat formatter = new SimpleDateFormat("d/M");
			return (formatter.format(date));
		} else if (dateFormatSpec.equals(DATE_FORMAT_MMYY)) {
			SimpleDateFormat formatter = new SimpleDateFormat("M/yy");
			return (formatter.format(date));
		} else if (dateFormatSpec.equals(DATE_FORMAT_DDMMYYYY)) {
			SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
			return (formatter.format(date));
		} else {
			System.out.println("Date format not matched, something is wrong, returning default");
			return (DateFormat.getDateInstance().format(date));
		}
	}

	/**
	 * @param number
	 * @return
	 */
	public String formatNumber(String number) {
		double workingNumber = Double.parseDouble(number);
		if (!isPrintedIfZero())
			if (workingNumber == 0)
				return ("");
		if (isSignReversed())
			workingNumber = workingNumber * -1;
		if (getMultiplyBy() != null)
			workingNumber = workingNumber * getMultiplyBy().doubleValue();
		String leadingFormat;
		String trailingFormat;
		if (workingNumber < 0) {
			leadingFormat = getLeadingFormatNegative();
			trailingFormat = getTrailingFormatNegative();
		} else {
			leadingFormat = getLeadingFormatPositive();
			trailingFormat = getTrailingFormatPositive();
		}
		StringBuffer formatString = new StringBuffer("###,###,##0");
		int precision = getPrecision();
		if (precision > 0)
			formatString.append(".");
		while(precision > 0) {
			formatString.append("0");
			--precision;
		}
		DecimalFormat precisionFormatter = new DecimalFormat(formatString.toString());
		String formattedNumber = precisionFormatter.format(workingNumber);
		formattedNumber = leadingFormat + formattedNumber + trailingFormat;
		//System.out.println("formatted number: " + formattedNumber);
		return (formattedNumber);
	}

	/**
	 * @return
	 */
	public boolean isPrintedIfZero() {
		if (printIfZero != null && printIfZero.value)
			return (true);
		else
			return (false);
	}

	/**
	 * @return
	 */
	public boolean isPrintedIfLengthZero() {
		if (printIfLengthZero != null && printIfLengthZero.value)
			return (true);
		else
			return (false);
	}

	/**
	 * This is useful for abutting two MethodElements where a space inbetween
	 * is/is not desired
	 */
	public boolean isPrecededBySpace() {
		return (isPrecededBySpace.value);
	}

	/**
	 * @param isPrecededBySpace
	 */
	public void isPrecededBySpace(boolean isPrecededBySpace) {
		this.isPrecededBySpace.value = isPrecededBySpace;
	}

	/**
	 * Make me look different from a fixed element in the Architect.
	 */
	public int getArchitectFontStyle() {
		return (Font.ITALIC);
	}

	/**
	 * @return
	 */
	public String getArchitectFontName() {
		return ("Dialog");
	}

	/**
	 * @return
	 */
	public String getValuePrintedIfTrue() {
		return (valuePrintedIfTrue.value);
	}

	/**
	 * @return
	 */
	public String getValuePrintedIfFalse() {
		return (valuePrintedIfFalse.value);
	}

	/**
	 * @return
	 */
	public String getDateFormat() {
		if (dateFormat != null && dateFormat.value.length() > 0)
			return (dateFormat.value);
		else
			return (DATE_FORMAT_MONTHDDYYYY);
	}

	/**
	 * @return
	 */
	public boolean isSignReversed() {
		return (isSignReversed.value);
	}

	/**
	 * @return
	 */
	public String getLeadingFormatPositive() {
		return (leadingFormatPositive.value);
	}

	/**
	 * @return
	 */
	public String getTrailingFormatPositive() {
		return (trailingFormatPositive.value);
	}

	/**
	 * @return
	 */
	public String getLeadingFormatNegative() {
		return (leadingFormatNegative.value);
	}

	/**
	 * @return
	 */
	public String getTrailingFormatNegative() {
		return (trailingFormatNegative.value);
	}

	/**
	 * @return
	 */
	public int getPrecision() {
		return (precision.value);
	}

	// this looks weird, but this compare is used by the combo box depends on to
	// test the objects for equality, can't use == becuase of the way they are re-instantiated
	// after returning from serialization.
	public boolean equals(Object compareTo) {
		if (compareTo instanceof ReceiptMethodElement)
			if (((ReceiptMethodElement) compareTo).classMethodPairs == classMethodPairs) {
				return (true);
			}
		return (false);
	}

}
