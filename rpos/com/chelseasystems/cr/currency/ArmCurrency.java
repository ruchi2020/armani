/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cr.currency;

import java.util.Locale;
import java.util.StringTokenizer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.config.ConfigMgr;

import java.lang.Long;

/**
 */
public class ArmCurrency implements java.io.Serializable {
	static final long serialVersionUID = 5859793329355900728L;
	/**
	 * Multiply <code>double</code> currency amounts by this factor to turn them
	 * into <code>long</code> values for internal storage.
	 */
	protected static long SCALING_FACTOR = 100; // must be six significant digits for Euro conversions.
	/**
	 * Contains information about the default currency type for this locale.
	 * This value is read from the currency.cfg file.
	 */
	private static CurrencyType baseCurrencyType;
	/**
	 * Contains information about this specific currency type (like US Dollars
	 * or British Pounds)
	 */
	private final CurrencyType type;
	/**
	 * The amount of the currency specified by "type".
	 */
	private final long amount;
	/**
	 * If this ArmCurrency was converted from some other currency type, the details
	 * of the conversion are found here.
	 */
	private final ArmCurrency convertedFrom;

	private int roundFactor = 1;

	private boolean applyTruncation = false;

	private int truncateFactor = 0;

	private NumberFormat nf = null;

	private static ConfigMgr config = new ConfigMgr("currency.cfg");

	/**
	 */
	static {
		try {
			try {
				//        config = new ConfigMgr("currency.cfg");
				//INIFile ifile = new INIFile(AppManager.getLocalFile("..\\config\\currency.cfg"), false);
				String currencyType = config.getString("BASE_CURRENCY_TYPE");
				baseCurrencyType = CurrencyType.getCurrencyType(currencyType);
				/*      // commented by Tim while merging  
				 roundFactor = config.getInt(currencyType + "_ROUND_FACTOR");
				 SCALING_FACTOR = config.getLong(currencyType + "_SCALING_FACTOR").longValue();
				 String truncateRule = config.getString("APPLY_TRUNCATE_RULE");
				 if (truncateRule != null)
				 applyTruncation = (truncateRule.equalsIgnoreCase("true"));
				 truncateFactor = config.getInt(currencyType + "_TRUNCATE_FACTOR");
				 */
			} catch (Exception ie) {
				baseCurrencyType = CurrencyType.getCurrencyType("USD");
				LoggingServices.getCurrent().logMsg("com.chelseasystems.common.currency.Currency", "Currency(double amount)", "Cannot read currency.cfg file.  Defaulting base currency to USD.",
						"Make sure currency.cfg exists and has read permissions.", LoggingServices.CRITICAL, ie);
			}
		} catch (UnsupportedCurrencyTypeException ucte) {
			LoggingServices.getCurrent().logMsg(
					"com.chelseasystems.common.currency.Currency",
					"Currency(double amount)",
					"Unsupported base currency type.",
					"currency.cfg is missing the BASE_CURRENCY_TYPE entry or the" + " code listed there is not supported by this system.  Look in"
							+ " currencytypes.dat for the list of supported currency codes.", LoggingServices.CRITICAL, ucte);
			System.exit(1);
		} catch (CurrencyException ce) {
			LoggingServices.getCurrent().logMsg("com.chelseasystems.common.currency.Currency", "Currency(double amount)", "Unknown CurrencyException", "N/A", LoggingServices.CRITICAL, ce);
			System.exit(1);
		}
	}

	/**
	 * Create a ArmCurrency object with "amount" of the base currency type.
	 * The base currency type is read from a config file (which can throw Exceptions).
	 * If you cannot find the base currency type, log the error and use a default
	 * of "USD".
	 * <p>
	 * Note: ArmCurrency objects created without a currency type code will use the
	 * default base currency type set in the currency.cfg file. CurrencyExceptions
	 * will be likely if the object is created on a server with one default base
	 * currency type and then sent to a client with another base currency type.
	 *
	 * @param amount the amount of this type of currency
	 */
	public ArmCurrency(double amount) {
		this(ArmCurrency.baseCurrencyType, (long) Math.round(SCALING_FACTOR * amount), null);
	}

	/**
	 * Create a ArmCurrency object with "amount" of "type" currency.
	 * @param type the CurrencyType object that describes this particular currency
	 * @param amount the amount of this type of currency
	 */
	public ArmCurrency(CurrencyType type, double amount) {
		this(type, (long) Math.round(SCALING_FACTOR * amount), null);
	}

	/**
	 * Create a ArmCurrency object by passing a string that should parse into a numeral
	 * and optionally contain a CurrencyType Code.  If the string does
	 * not contain anything other than numbers or a decimal then the CurrencyType will
	 * be the default.  Example "CAD$5.00"
	 * <p>
	 * Note: ArmCurrency objects created without a currency type code will use the
	 * default base currency type set in the currency.cfg file. CurrencyExceptions
	 * will be likely if the object is created on a server with one default base
	 * currency type and then sent to a client with another base currency type.
	 *
	 * @param sAmount the amount of this currency with an optional type code
	 */
	public ArmCurrency(String sAmount) {
		StringBuffer buffAmount = new StringBuffer();
		StringBuffer buffCode = new StringBuffer();
		for (int idx = 0; idx < sAmount.length(); idx++) {
			if (Character.getType(sAmount.charAt(idx)) != 26) {
				if (Character.isLetter(sAmount.charAt(idx)) && idx < 3) {
					buffCode.append(sAmount.charAt(idx));
				} else if (((int) sAmount.charAt(idx)) < 129) { //ascii char only
					buffAmount.append(sAmount.charAt(idx));
				}
			}
		}
		buffAmount = new StringBuffer(buffAmount.toString().trim());
		if (buffAmount.length() == 0) {
			buffAmount.append('0');
		}
		this.convertedFrom = null;
		CurrencyType tempType = null;
		try {
			tempType = CurrencyType.getCurrencyType(buffCode.toString().trim());
		} catch (UnsupportedCurrencyTypeException ucte) {
			tempType = ArmCurrency.getBaseCurrencyType();
		}
		this.type = tempType;
		//    ConfigMgr config = new ConfigMgr("currency.cfg");
		roundFactor = config.getInt(type.getCode() + "_ROUND_FACTOR");
		SCALING_FACTOR = config.getLong(type.getCode() + "_SCALING_FACTOR").longValue();
		String truncateRule = config.getString("APPLY_TRUNCATE_RULE");
		if (truncateRule != null)
			applyTruncation = (truncateRule.equalsIgnoreCase("true"));
		truncateFactor = config.getInt(type.getCode() + "_TRUNCATE_FACTOR");
		String key = "FORMAT_" + type.getCode() + "_" + type.getLocale().getLanguage() + "_" + type.getLocale().getCountry();
		String decimalSeparator = config.getString(key + ".DECIMAL_SEPARATOR");
		String groupSeparator = config.getString(key + ".GROUP_SEPARATOR");
		nf = NumberFormat.getNumberInstance(type.getLocale());
		if (nf instanceof DecimalFormat) {
			DecimalFormatSymbols localizedDFS = ((DecimalFormat) nf).getDecimalFormatSymbols();
			if (decimalSeparator != null)
				localizedDFS.setDecimalSeparator(decimalSeparator.charAt(0));
			if (groupSeparator != null)
				localizedDFS.setGroupingSeparator(groupSeparator.charAt(0));
			((DecimalFormat) nf).setDecimalFormatSymbols(localizedDFS);
			nf.setMaximumFractionDigits(baseCurrencyType.getDecimalPlaces());
			nf.setMinimumFractionDigits(baseCurrencyType.getDecimalPlaces());

		}

		long tempAmount = 0;
		try {
			tempAmount = (long) Math.round(SCALING_FACTOR * nf.parse(buffAmount.toString()).doubleValue());
		} catch (ParseException pe2) {
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(type.getLocale());
			for (int idx = 0; idx < buffAmount.length(); idx++) {
				if (buffAmount.charAt(idx) == dfs.getGroupingSeparator()) {
					buffAmount.deleteCharAt(idx);
				}
			}
			try {
				tempAmount = (long) Math.round(SCALING_FACTOR * nf.parse(buffAmount.toString()).doubleValue());
			} catch (ParseException pe) {
				tempAmount = 0;
			}
		}
		this.amount = tempAmount;
	}

	/**
	 * Create a ArmCurrency object with "amount" of "type" currency, and how is it convertedFrom.
	 * This method is intended to be used by the backend to reconstruct a currency from the database.
	 * @param type the CurrencyType object that describes this particular currency
	 * @param amount the amount of this type of currency
	 * @param convertedFrom the currency from which it was converted.
	 */
	public ArmCurrency(CurrencyType type, double amount, ArmCurrency convertedFrom) {
		this(type, (long) Math.round(SCALING_FACTOR * amount), convertedFrom);
	}

	/**
	 * Create a ArmCurrency object with "amount" of "type" currency.  This constructor
	 * is used internally as a means to copy a ArmCurrency object since the user may
	 * not be aware of the scale factor to use when converting the "double" amount
	 * to a "long" amount.
	 */
	protected ArmCurrency(CurrencyType type, long amount) {
		this(type, amount, null);
	}

	/**
	 * Create a ArmCurrency object with "amount" of "type" currency.  This constructor
	 * is used internally as a means to copy a ArmCurrency object since the user may
	 * not be aware of the scale factor to use when converting the "double" amount
	 * to a "long" amount.
	 */
	protected ArmCurrency(CurrencyType type, long amount, ArmCurrency convertedFrom) {
		this.type = type;
		this.amount = amount;
		this.convertedFrom = convertedFrom;
		//    ConfigMgr config = new ConfigMgr("currency.cfg");
		roundFactor = config.getInt(type.getCode() + "_ROUND_FACTOR");
		SCALING_FACTOR = config.getLong(type.getCode() + "_SCALING_FACTOR").longValue();
		String truncateRule = config.getString("APPLY_TRUNCATE_RULE");
		if (truncateRule != null)
			applyTruncation = (truncateRule.equalsIgnoreCase("true"));
		truncateFactor = config.getInt(type.getCode() + "_TRUNCATE_FACTOR");
		String key = "FORMAT_" + type.getCode() + "_" + type.getLocale().getLanguage() + "_" + type.getLocale().getCountry();
		String decimalSeparator = config.getString(key + ".DECIMAL_SEPARATOR");
		String groupSeparator = config.getString(key + ".GROUP_SEPARATOR");
		nf = NumberFormat.getNumberInstance(type.getLocale());
		if (nf instanceof DecimalFormat) {
			DecimalFormatSymbols localizedDFS = ((DecimalFormat) nf).getDecimalFormatSymbols();
			if (decimalSeparator != null)
				localizedDFS.setDecimalSeparator(decimalSeparator.charAt(0));
			if (groupSeparator != null)
				localizedDFS.setGroupingSeparator(groupSeparator.charAt(0));
			((DecimalFormat) nf).setDecimalFormatSymbols(localizedDFS);
			nf.setMaximumFractionDigits(baseCurrencyType.getDecimalPlaces());
			nf.setMinimumFractionDigits(baseCurrencyType.getDecimalPlaces());

		}

	}

	/**
	 * @return the base currency type for the jvm
	 */
	public static CurrencyType getBaseCurrencyType() {
		return (baseCurrencyType);
	}

	/**
	 * @return the currency type of this object.
	 */
	public CurrencyType getCurrencyType() {
		return (type);
	}

	/**
	 * Add the value of this ArmCurrency to the value of the ArmCurrency object passed
	 * as the parameter and return a new ArmCurrency object with the sum of the two
	 * as its value.  Conversion history is lost.
	 * @param value the ArmCurrency object to add to this one.
	 * @return ArmCurrency a new ArmCurrency object whose value is the sum of the original
	 *                  ArmCurrency and the ArmCurrency that was passed as a parameter
	 */
	public ArmCurrency add(ArmCurrency value) throws CurrencyException {
		// Make sure the operands are the same currency type.
		if (!(type.getCode().equals(value.getCurrencyType().getCode()))) {
			throw new CurrencyException("Trying to add dissimilar currencies.");
		}
		if (this.amount == 0) {
			return (value);
		}
		if (value.amount == 0) {
			return (this);
		}
		return (new ArmCurrency(type, amount + value.longValue()));
	}

	/**
	 * Add the value of this ArmCurrency to the value of the ArmCurrency object passed
	 * as the parameter and return a new ArmCurrency object with the sum of the two
	 * as its value and the required new currency type.
	 * @param value the ArmCurrency object to add to this one.
	 * @param requiredType the currency type to be returned.
	 * @return ArmCurrency a new ArmCurrency object whose value is the sum of the original
	 *                  ArmCurrency and the ArmCurrency that was passed as a parameter
	 */
	public ArmCurrency add(ArmCurrency value, CurrencyType requiredType) throws CurrencyException {
	ArmCurrency convertedThis = this.convertTo(requiredType);
	ArmCurrency convertedValue = value.convertTo(requiredType);
		return (new ArmCurrency(requiredType, convertedThis.amount + convertedValue.amount));
	}

	/**
	 * Subtact the value of the ArmCurrency passed as a parameter from the value of
	 * this ArmCurrency object.  Return a new ArmCurrency object containing the diference.
	 * Conversion history is lost.
	 * @param value the ArmCurrency object to subtract from this one.
	 * @return ArmCurrency a new ArmCurrency object whose value is the difference of the
	 *                  original ArmCurrency and the ArmCurrency that was passed as a parameter
	 */
	public ArmCurrency subtract(ArmCurrency value) throws CurrencyException {
		// Make sure the operands are the same currency type.
		if (!(type.getCode().equals(value.getCurrencyType().getCode()))) {
			throw new CurrencyException("Trying to subtract dissimilar currencies.");
		}
		if (this.amount == 0) {
			return (new ArmCurrency(value.type, -value.longValue(), value.convertedFrom));
		}
		if (value.amount == 0) {
			return (this);
		}
		return (new ArmCurrency(type, amount - value.longValue()));
	}

	/**
	 * Subtact the value of the ArmCurrency passed as a parameter from the value of
	 * this ArmCurrency object.  Return a new ArmCurrency object containing the diference
	 * whth the required currency type.
	 * Conversion history is lost.
	 * @param value the ArmCurrency object to subtract from this one.
	 * @param requiredType the currency type to be returned.
	 * @return ArmCurrency a new ArmCurrency object with required type, the amount is the difference of the
	 *      original ArmCurrency and the ArmCurrency that was passed as a parameter
	 */
	public ArmCurrency subtract(ArmCurrency value, CurrencyType requiredType) throws CurrencyException {
	ArmCurrency convertedThis = this.convertTo(requiredType);
	ArmCurrency convertedValue = value.convertTo(requiredType);
		return (new ArmCurrency(requiredType, convertedThis.amount - convertedValue.amount));
	}

	/**
	 * Multiply the value of this ArmCurrency object by the value of the parameter.
	 * Conversion history is preserved.
	 * @param value the multiplier
	 * @return ArmCurrency a new ArmCurrency object whose value is the amount of the
	 *                  original ArmCurrency object multiplied by the multiplier that
	 *                  is passed in as the parameter.
	 */
	public ArmCurrency multiply(int value) {
	ArmCurrency newCurrency = new ArmCurrency(type, amount * (long) value, convertedFrom);
		return (newCurrency);
	}

	/**
	 * Multiply the value of this ArmCurrency object by the value of the parameter.
	 * Round the result to SCALE_FACTOR - DECIMAL_PLACES precision.  Conversion history
	 * is preserved.
	 * @param value the multiplier
	 * @return ArmCurrency a new ArmCurrency object whose value is the amount of the
	 *                  original ArmCurrency object multiplied by the multiplier that
	 *                  is passed in as the parameter.
	 */
	public ArmCurrency multiply(double value) {
	ArmCurrency newCurrency = new ArmCurrency(type, (long) Math.round(amount * value), convertedFrom);
		return (newCurrency);
	}

	/**
	 * Divide the value of this ArmCurrency object by the value of the parameter.
	 * Round the result to SCALE_FACTOR - DECIMAL_PLACES precision.  Conversion
	 * history information is preserved.
	 * @param value the divisor
	 * @return ArmCurrency a new ArmCurrency object whose value is the amount of the
	 *                  original ArmCurrency object divided by the divisor that
	 *                  is passed in as the parameter.
	 */
	public ArmCurrency divide(int value) {
	ArmCurrency newCurrency = new ArmCurrency(type, (long) Math.round((double) amount / (long) value), convertedFrom);
		return (newCurrency);
	}
	
	public Double offlineDivide(int value) {
		Double newCurrency = new Double(((double) amount / value));
		return (newCurrency);
		}

	/**
	 * Divide the value of this ArmCurrency object by the value of the parameter.
	 * Round the result to SCALE_FACTOR - DECIMAL_PLACES precision.  Conversion history
	 * is preserved, but a call to getUnconvertedAmount() may now be imprecise.
	 * @param value the divisor
	 * @return ArmCurrency a new ArmCurrency object whose value is the amount of the
	 *                  original ArmCurrency object divided by the divisor that
	 *                  is passed in as the parameter.
	 */
	public ArmCurrency divide(double value) {
	ArmCurrency newCurrency = new ArmCurrency(type, (long) Math.round((double) amount / value), convertedFrom);
		return (newCurrency);
	}

	/**
	 * Return a ArmCurrency whose amount is the absolute value of the amount of this
	 * ArmCurrency object.  Conversion history is preserved.
	 */
	public ArmCurrency absoluteValue() {
	ArmCurrency result;
		if (doubleValue() < 0.0) {
			result = new ArmCurrency(type, (amount * -1), convertedFrom);
		} else {
			result = new ArmCurrency(type, amount);
		}
		return (result);
	}

	/**
	 * Helper method name for OI. In simple mode (eg ReceiptBuilder) OI filters methods to "gets".
	 * This should be considered a get method.
	 */
	public ArmCurrency getAbsoluteValue() {
		return (absoluteValue());
	}

	/**
	 *
	 * Returns <code>true</code> if the two Currencies are of the same type and
	 * amount.  Throws a CurrencyException if you try to compare dissimilar currency
	 * types.
	 */
	public boolean equals(ArmCurrency value) throws CurrencyException {
		if (value.amount == 0L || this.amount == 0L) {
			return (value.amount == this.amount);
		}
		// Make sure the operands are the same currency type.
		if (!(type.getCode().equals(value.getCurrencyType().getCode()))) {
			throw new CurrencyException("Trying to compare dissimilar currencies.");
		}
		return (amount == value.amount);
	}

	/**
	 * If this object is a ArmCurrency compare to this ArmCurrency instance and check
	 * if they are the same currency type and same amount.
	 * @param obj the ArmCurrency Object
	 * @return <code>true</code> if the obj is a Currecy and it is the same currency type
	 * and amount as this currency.
	 */
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof ArmCurrency)) {
			return (false);
		}
	ArmCurrency value = (ArmCurrency) obj;
		if (value.amount == 0L || this.amount == 0L) {
			return (value.amount == this.amount);
		}
		// Make sure the operands are the same currency type.
		if (!(type.getCode().equals(value.getCurrencyType().getCode()))) {
			return (false);
		}
		return (amount == value.amount);
	}

	/**
	 */
	public boolean greaterThan(ArmCurrency value) throws CurrencyException {
		if (this.amount == 0L || value.amount == 0L) {
			return (this.amount > value.amount);
		}
		// Make sure the operands are the same currency type.
		if (!(type.getCode().equals(value.getCurrencyType().getCode()))) {
			throw new CurrencyException("Trying to compare dissimilar currencies.");
		}
		return (amount > value.amount);
	}

	/**
	 */
	public boolean greaterThanOrEqualTo(ArmCurrency value) throws CurrencyException {
		if (this.amount == 0L || value.amount == 0L) {
			return (this.amount >= value.amount);
		}
		// Make sure the operands are the same currency type.
		if (!(type.getCode().equals(value.getCurrencyType().getCode()))) {
			throw new CurrencyException("Trying to compare dissimilar currencies.");
		}
		return (amount >= value.amount);
	}

	/**
	 */
	public boolean lessThan(ArmCurrency value) throws CurrencyException {
		if (this.amount == 0L || value.amount == 0L) {
			return (this.amount < value.amount);
		}
		// Make sure the operands are the same currency type.
		if (!(type.getCode().equals(value.getCurrencyType().getCode()))) {
			throw new CurrencyException("Trying to compare dissimilar currencies.");
		}
		return (amount < value.amount);
	}

	/**
	 */
	public boolean lessThanOrEqualTo(ArmCurrency value) throws CurrencyException {
		if (this.amount == 0L || value.amount == 0L) {
			return (this.amount <= value.amount);
		}
		// Make sure the operands are the same currency type.
		if (!(type.getCode().equals(value.getCurrencyType().getCode()))) {
			throw new CurrencyException("Trying to compare dissimilar currencies.");
		}
		return (amount <= value.amount);
	}

	/**
	 * Return a ArmCurrency object with a value rounded to decimalPlaces precision.
	 */
	public ArmCurrency round() {
		long newAmount;
		long lroundFactor = 0; // Was 5 originally
		long truncFactor = 1; // Was 10 originally
		for (int i = roundFactor - type.getDecimalPlaces(); i > 0; i--) { // Was i = 5 originally
			lroundFactor *= 10;
			truncFactor *= 10;
		}
		if (amount >= 0) {
			newAmount = amount + lroundFactor;
		} else {
			newAmount = amount - lroundFactor;
		}
		return (new ArmCurrency(type, (long) ((newAmount * truncFactor) / truncFactor), convertedFrom));
	}

	/**
	 * Helper method name for OI. In simple mode (eg ReceiptBuilder) OI filters methods to "gets".
	 * This should be considered a get method.
	 */
	public ArmCurrency getRound() {
		return (round());
	}

	/**
	 * Convert this ArmCurrency object into a ArmCurrency object of the specified type.  This method
	 * looks up the appropriate exchange rate(s).
	 *
	 * Conversion rates are based on one unit of the CurrencyType that the ArmCurrency object
	 * representing the conversion rate contains.  For instance, if you are converting US dollars
	 * to British pounds and you get a conversion rate that has a type of "GBP" and an amount of 1.6379,
	 * that means that 1 british pound equals 1.6379 US dollars.  If the conversion rate has a type
	 * of "USD" and an amount of .5826, that means that 1 US dollar is equivalent to .5826 British pounds.
	 *
	 * @param newType a CurrencyType object that describes the currency to convert to.
	 * @return ArmCurrency a new ArmCurrency object of type "newType" with an amount equivalent
	 *                  to the unconverted ArmCurrency object (according to current exchange rates).
	 */
	public ArmCurrency convertTo(CurrencyType newType) throws MissingExchangeRateException, UnsupportedCurrencyTypeException {
		if (this.type.equals(newType)) {
			return (this);
		}
	ArmCurrency rate = ExchangeRateServices.getCurrent().getExchangeRate(type, newType);
		return (convertTo(newType, rate));
	}

	/**
	 * This method is used to force a conversion to happen at a given (possibly
	 *    historical) conversion rate.  Should only be used when reconstructing
	 *    foreign currency payments from the database.
	 *
	 * Conversion rates are based on one unit of the CurrencyType that the
	 *    ArmCurrency object representing the conversion rate contains.  For
	 *    instance, if you are converting US dollars to British pounds and you
	 *    get a conversion rate that has a type of "GBP" and an amount of 1.6379,
	 *    that means that 1 british pound equals 1.6379 US dollars.  If the
	 *    conversion rate has a type of "USD" and an amount of .5826, that means
	 *    that 1 US dollar is equivalent to .5826 British pounds.
	 *
	 * If the CurrencyType specified by the <code>rate</code> object is the same
	 *    as the type of this object, then this amount will get multiplied by the
	 *    rate, else the amount will get divided by the rate.
	 *
	 * @param newType a CurrencyType object that describes the currency to convert to.
	 * @param rate a ArmCurrency object that holds exchange rate information
	 * @return ArmCurrency a new ArmCurrency object of type "newType" with an amount equivalent
	 *    to the unconverted ArmCurrency object (according to current exchange rates).
	 * @throws UnsupportedCurrencyTypeException if the number calculated to too
	 *    big to fit in a <code>long</code>, which is 8 bytes.
	 */
	public ArmCurrency convertTo(CurrencyType newType, ArmCurrency rate) throws UnsupportedCurrencyTypeException {
		BigInteger newAmount;
		if (type.equals(rate.getCurrencyType())) {
			newAmount = BigInteger.valueOf(amount).multiply(BigInteger.valueOf(rate.longValue())).divide(BigInteger.valueOf(SCALING_FACTOR));
		} else {
			
			newAmount = BigInteger.valueOf(amount).multiply(BigInteger.valueOf(SCALING_FACTOR)).divide(BigInteger.valueOf(rate.longValue()));
		}
		if (newAmount.bitLength() > 63) {
			throw new UnsupportedCurrencyTypeException("amount too large to convert");
		}
		return (new ArmCurrency(newType, newAmount.longValue(), new ArmCurrency(type, amount, convertedFrom)));
	}

	/**
	 * Return the value of the Currnecy object in a form suitable for printing.  Does
	 * NOT include the currency symbol.
	 * @return String the value of the ArmCurrency object in a form suitable for printing
	 *                but without the currency symbol.
	 */
	public String stringValue() {
		return (nf.format(this.getDoubleValue()));
	}

	/**
	 * Return the value of the ArmCurrency object in a form suitable for printing including
	 * the currency symbol.
	 * @return String the value of the ArmCurrency object in a form suitable for printing
	 *                including the currency symbol.
	 */
	public String formattedStringValue() {
		return CurrencyFormat.format(this);
	}

	/**
	 * put your documentation comment here
	 * @param aLocale
	 * @return
	 */
	public String formattedStringValue(java.util.Locale aLocale) {
		return CurrencyFormat.format(this, aLocale);
	}

	// helper for OI
	public String getFormattedStringValue() {
		return (formattedStringValue());
	}

	/**
	 * Return the amount of the ArmCurrency object as a double.
	 * @return double the double value of the ArmCurrency object.
	 */
	public double doubleValue() {
		return (double) amount / SCALING_FACTOR;
	}

	/**
	 * @return
	 */
	public double getDoubleValue() {
		return (doubleValue());
	}

	/**
	 * Return the currency amount as a long (including whatever scaling factor
	 * was used to turn the double value into a long value.  This is for internal
	 * consumption only, hence the "protected" designation.
	 */
	protected long longValue() {
		return (amount);
	}

	/**
	 * Returns <code>true</code> if this ArmCurrency object was original converted from
	 * another currency type.
	 * @return boolean <code>true</code> if this ArmCurrency object was original converted from
	 *                 another currency type.
	 */
	public boolean isConverted() {
		return (null != convertedFrom);
	}

	/**
	 * Return the object (if any) that was the amount and type before this was converted.
	 * @return ArmCurrency an object that describes the last currency conversion
	 *                            that was performed on this ArmCurrency object.
	 */
	public ArmCurrency getConvertedFrom() {
		return (convertedFrom);
	}

	/**
	 * If this ArmCurrency object was converted from some other currency type, this method
	 * returns a ArmCurrency object of the original type and amount.
	 *
	 * If this ArmCurrency object was never converted, this method return just <code>this</code>.
	 * @return ArmCurrency the original amount of currency in the original currency type
	 */
	public ArmCurrency getUnconvertedAmount() {
	ArmCurrency rv = this;
		while(rv.getConvertedFrom() != null) {
			rv = rv.getConvertedFrom();
		}
		return (rv);
	}

	/**
	 * Used for debugging purposes only.
	 * @return String for use in debugging only.
	 */
	public String toString() {
		StringBuffer s = new StringBuffer(type.getCode());
		s.append(", ");
		s.append(longValue());
		s.append("->");
		s.append(doubleValue());
		return (s.toString());
	}

	/**
	 * Return a pipe-delimited list of the value of this ArmCurrency and all of
	 *   its conversions.  The first three chars of every token will be the
	 *   CurrencyType code of the following decimal value.
	 * @return a delimited list of this currency and its conversions
	 */
	public String toDelimitedString() {
	ArmCurrency currency = this;
		StringBuffer buffer = new StringBuffer();
		while(currency.isConverted()) {
			buffer.append(currency.getCurrencyType().getCode());
			buffer.append(currency.stringValue());
			buffer.append("|");
			currency = currency.getConvertedFrom();
		}
		buffer.append(currency.getCurrencyType().getCode());
		buffer.append(currency.stringValue());
		return (buffer.toString());
	}

	/**
	 * Return a ArmCurrency object that is represented by the specified string.
	 *   The string can be delimited between the Currency's conversions with
	 *   a pipe character.  The first three chars of each token may be a
	 *   CurrencyType code, else each remaining substring of a token must
	 *   parse to valid decimal.
	 * @param string the specified string to convert to a Currency
	 * @return a ArmCurrency object with values from specified string
	 * @throws UnsupportedCurrencyTypeException if CurrencyType is not supported
	 */
	public static ArmCurrency valueOf(String string) throws UnsupportedCurrencyTypeException {
		if (string == null) {
			return (null);
		}
		StringTokenizer tokenizer = new StringTokenizer(string, "|");
	 ArmCurrency[] conversionHistory = new ArmCurrency[tokenizer.countTokens()];
		for (int i = 0; i < conversionHistory.length; i++) {
			conversionHistory[i] = new ArmCurrency(tokenizer.nextToken());
		}
	ArmCurrency value = conversionHistory[conversionHistory.length - 1];
		if (conversionHistory.length > 1) {
			for (int i = conversionHistory.length - 2; i >= 0; i--) {
				value = new ArmCurrency(conversionHistory[i].getCurrencyType(), conversionHistory[i].doubleValue(), value);
			}
		}
		return (value);
	}

	/**
	 * Returns a hashcode for this ArmCurrency object.
	 *
	 * @return  a hash code value for this object.
	 */
	public int hashCode() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(type.getCode());
		sbuf.append(amount);
		String s = sbuf.toString();
		int h = 0;
		//int off = offset;
		int off = 0;
		char val[] = s.toCharArray();
		//int len = count;
		int len = s.length();
		if (len < 16) {
			for (int i = len; i > 0; i--) {
				h = (h * 37) + val[off++];
			}
		} else {
			// only sample some characters
			int skip = len / 8;
			for (int i = len; i > 0; i -= skip, off += skip) {
				h = (h * 39) + val[off];
			}
		}
		return (h);
	}

	/**
	 */
	public String formatForeignCurrency() {
		StringBuffer buf = new StringBuffer();
	ArmCurrency foreignAmt = this.getUnconvertedAmount();
	if (foreignAmt != null) {
			buf.append(foreignAmt.stringValue());
			buf.append(" (");
			buf.append(foreignAmt.getCurrencyType().getCode());
			buf.append(") ,1 (");
			buf.append(this.getCurrencyType().getCode());
			buf.append(")=");
			buf.append(ArmCurrency.getConversionRate(this, foreignAmt));
			buf.append(" (");
			buf.append(foreignAmt.getCurrencyType().getCode());
			buf.append(")");
		} else {
			buf.append(this.formattedStringValue());
		}
		return (buf.toString());
	}
	
	
	
	
	

	/**
	 * This method will calculate and return a rate, which is a double between
	 * zero and 100, that the specified currencies where converted by.
	 * @param convertedTo    the converted to amount
	 * @param convertedFrom  the converted from amount
	 */
	static public Double getConversionRate(ArmCurrency convertedTo, ArmCurrency convertedFrom) {
		return (getConversionRate(convertedTo, convertedFrom, 6));
	}

	/**
	 * This method will calculate and return a rate, which is a double between
	 * zero and 100, that the specified currencies where converted by.
	 * @param convertedTo    the converted to amount
	 * @param convertedFrom  the converted from amount
	 * @param sigDigits      the number of decimal places to keep
	 */
	static public Double getConversionRate(ArmCurrency convertedTo, ArmCurrency convertedFrom, int sigDigits) {
		double rate = convertedFrom.doubleValue() / convertedTo.doubleValue();
		rate *= Math.pow(10, sigDigits);
		rate = Math.floor(rate);
		rate /= Math.pow(10, sigDigits);
		return (new Double(rate));
	}

	/**
	 * Return a ArmCurrency object that holds the smallest possible amount of the
	 * specified currency type.
	 * @return ArmCurrency the smallest possible amount of the currency type.
	 */
	static public ArmCurrency getMinimumDenomination(CurrencyType aType) {
		double smallestIncr = 1 / (Math.pow(10, aType.getDecimalPlaces()));
		return (new ArmCurrency(aType, smallestIncr));
	}

	/**
	 * Return a currency object which is the sum of currencies passed in.  The
	 * types of currencies must be the same.
	 * @param currencies an array of ArmCurrency to be added
	 * @return Return a currency object which is the sum of currencies passed in.
	 */
	static public ArmCurrency sum(ArmCurrency[] currencies) throws CurrencyException {
		if (currencies == null || currencies.length == 0) {
			return (new ArmCurrency(0.0));
		}
		if (currencies.length == 1) {
			return (currencies[0]);
		}
	ArmCurrency total = currencies[0];
		for (int i = 1; i < currencies.length; i++) {
			total = total.add(currencies[i]);
		}
		return (total);
	}

	/**
	 * Return a currency object which is the sum of currencies passed in.  The
	 * types of currencies do not have to be the same.
	 * @param currencies an array of ArmCurrency to be added
	 * @param requiredType the required currency type to be returned
	 * @return Return a currency object which is the sum of currencies passed in.
	 */
	static public ArmCurrency sum(ArmCurrency[] currencies, CurrencyType requiredType) throws CurrencyException {
		if (currencies == null || currencies.length == 0) {
			return (new ArmCurrency(requiredType, 0L));
		}
		long total = 0L;
		for (int i = 0; i < currencies.length; i++) {
			total += currencies[i].convertTo(requiredType).amount;
		}
		return (new ArmCurrency(requiredType, total));
	}

	public ArmCurrency truncate(int factor) {
		if (this.applyTruncation) {
			if (factor == -1)
				factor = this.truncateFactor;
			long truncFactor = 1;
			double dAmount = this.doubleValue();
			for (int i = factor; i > 0; i--){
				truncFactor *= 10;
			}
			
			dAmount = Math.floor(dAmount * truncFactor) / truncFactor;
			return new ArmCurrency(dAmount);
		}
		return this;
	}
	
	public ArmCurrency roundAndTruncate(int factor) {
		if (this.applyTruncation) {
			if (factor == -1)
				factor = this.truncateFactor;
			long truncFactor = 1;
			double dAmount = this.doubleValue();
			for (int i = factor; i > 0; i--){
				truncFactor *= 10;
			}
			
			dAmount = Math.round(dAmount * truncFactor) / truncFactor;
			return new ArmCurrency(dAmount);
		}
		return this;
	}

	public NumberFormat getNumberFormatInstance() {
		return nf;
	}

	public ArmCurrency convertTo(CurrencyType newType, Double rate) throws UnsupportedCurrencyTypeException {
		double newAmount;
		if (type.equals(ArmCurrency.baseCurrencyType)) {
			newAmount = (amount * rate.doubleValue()) / SCALING_FACTOR;
		} else {
			//Mahesh Nandure : 26 APR 2017 : Rounding selling price for Foreign currency
			  ConfigMgr cfgMgr = new ConfigMgr("currency.cfg");
			  	String Rounding_Mode=cfgMgr.getString("ROUNDING_SELLING_PRICE");
			  	
			  	if (Rounding_Mode == null) {
			  		Rounding_Mode = "false";
			  	}
			  	if(Rounding_Mode.equalsIgnoreCase("true"))
			  	{
			  		newAmount = (amount / rate.doubleValue()) / SCALING_FACTOR;
			  		newAmount=roundMode(newAmount, 0);
			  	} //end Mahesh Nandure : 26 APR 2017 : Rounding selling price for Foreign currency
			  	else
			  	{
			  		newAmount = (amount / rate.doubleValue()) / SCALING_FACTOR;
			  	}
		}
		//    if (newAmount.bitLength() > 63) {
		//      throw new UnsupportedCurrencyTypeException("amount too large to convert");
		//    }
		return (new ArmCurrency(newType, newAmount, new ArmCurrency(type, amount, convertedFrom)));
	}
	
	
	//Mahesh Nandure : 26 APR 2017 : Rounding selling price for Foreign currency
	  public static double roundMode(double value, int places)
		{
			if (places < 0) throw new IllegalArgumentException();
			
			
			BigDecimal bd = new BigDecimal(value);
			ConfigMgr cfgMgr = new ConfigMgr("currency.cfg");
			String Rounding_Mode_Flag=cfgMgr.getString("ROUNDING_MODE");
			if(Rounding_Mode_Flag !=null){
			if(Rounding_Mode_Flag.equalsIgnoreCase("UP"))
			{
			bd =bd.setScale(places,RoundingMode.UP);
			}
			else if(Rounding_Mode_Flag.equalsIgnoreCase("DOWN"))
			{
				bd =bd.setScale(places,RoundingMode.DOWN);
			}
			else if(Rounding_Mode_Flag.equalsIgnoreCase("FLOOR"))
			{
				bd =bd.setScale(places,RoundingMode.FLOOR);
			}
			else if(Rounding_Mode_Flag.equalsIgnoreCase("CEILING"))
			{
				bd =bd.setScale(places,RoundingMode.CEILING);
			}
			else if(Rounding_Mode_Flag.equalsIgnoreCase("HALF_UP"))
			{
				bd =bd.setScale(places,RoundingMode.HALF_UP);
			}
			else if(Rounding_Mode_Flag.equalsIgnoreCase("HALF_DOWN"))
			{
				bd =bd.setScale(places,RoundingMode.HALF_DOWN);
			}
			else if(Rounding_Mode_Flag.equalsIgnoreCase("HALF_EVEN"))
			{
				bd =bd.setScale(places,RoundingMode.HALF_EVEN);
			}
			else if(Rounding_Mode_Flag.equalsIgnoreCase("UNNECESSARY"))
			{
				bd =bd.setScale(places,RoundingMode.UNNECESSARY);
			}
			}
			
			return bd.doubleValue();
			
		} //end Mahesh Nandure : 26 APR 2017 : Rounding selling price for Foreign currency

	public boolean isApplyTruncation(){
		return applyTruncation;
	}
}
