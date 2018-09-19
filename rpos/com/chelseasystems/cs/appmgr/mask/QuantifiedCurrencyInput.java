package com.chelseasystems.cs.appmgr.mask;

public class QuantifiedCurrencyInput {
	public static final String[] QTY_MULT_DELIMETERS = new String[] { "*" };
	double multiplier;
	String currency;
	Object input;

	/**
	 * @param String
	 *            inputString
	 */
	public QuantifiedCurrencyInput(String inputString) {
		multiplier = 1;
		input = inputString;
		for (int i = QTY_MULT_DELIMETERS.length - 1; i >= 0; i--) {
			if (inputString.indexOf(QTY_MULT_DELIMETERS[i]) > -1) {
				int delimiter_index = inputString.indexOf(QTY_MULT_DELIMETERS[i]);
				currency = inputString.substring(0, delimiter_index);
				multiplier = Double.parseDouble(inputString.substring(delimiter_index + 1));
				input = inputString.substring(delimiter_index + 1);
				break;
			}
		}
		if (currency == null) // in case only currency is entered
			currency = inputString;
	}

	public Object getInput() {
		return input;
	}

	public String getCurrency() {
		return currency;
	}

	public double getMultiplier() {
		return multiplier;
	}

}
