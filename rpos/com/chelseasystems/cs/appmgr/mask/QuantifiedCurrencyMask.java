package com.chelseasystems.cs.appmgr.mask;

import java.util.ResourceBundle;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.mask.IMask;
import com.chelseasystems.cr.appmgr.mask.MaskGenericRenderer;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.util.ResourceManager;

public class QuantifiedCurrencyMask extends MaskGenericRenderer implements IMask {

	private ResourceBundle res;

	/**
	 * This mask will return a <code>QuantifiedCurrencyInput</code> object representing
	 * the currency and multiplier passed to it.
	 */
	public QuantifiedCurrencyMask() {
		res = ResourceManager.getResourceBundle();
	}

	/**
	 * Perform the validation.
	 * @param theAppMgr
	 * @param edtValue
	 * @return QuantifiedCurrencyInput
	 */
	public Object validateMask(IApplicationManager theAppMgr, Object edtValue) {
		try {
			QuantifiedCurrencyInput input = new QuantifiedCurrencyInput((String) edtValue);
			if (input.getCurrency() == null) {
				theAppMgr.showErrorDlg(res.getString("Invalid input. Please enter currency*multiplier"));
				return null;
			} else {
			ArmCurrency currency = (ArmCurrency) (new CMSCurrencyMask()).validateMask(theAppMgr, input.getCurrency());
				if (currency == null) { // Error message is thrown by the CurrencyMask
					theAppMgr.showErrorDlg(res.getString("Invalid input. Please enter currency*multiplier"));
					return null;
				}
			}
			return input;
		} catch (Exception ex) {
			theAppMgr.showErrorDlg(res.getString("Invalid input. Please enter currency*multiplier"));
			return null;
		}
	}

	/**
	 * make sure that this mask will only be passed <code>String</code>s
	 * @param initialValue
	 * @return
	 */
	public boolean validateInitialValueType(Object initialValue) {
		return (initialValue instanceof String);
	}

}
