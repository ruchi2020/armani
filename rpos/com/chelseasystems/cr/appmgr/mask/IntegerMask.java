/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */

package com.chelseasystems.cr.appmgr.mask;

import com.chelseasystems.cr.appmgr.*;

/**
 */
public class IntegerMask extends MaskGenericRenderer implements IMask {

	private IApplicationManager theAppMgr;
	java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

	/**
	 * @param theAppMgr
	 * @param edtValue
	 * @return
	 */
	public Object validateMask(IApplicationManager theAppMgr, Object edtValue) {
		this.theAppMgr = theAppMgr;
		String edtString = edtValue.toString();
		return testMask(edtString) ? new Integer(edtString) : null;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean testMask(String value) {
		try {
			int result = Integer.parseInt(value);
			if (result < 0) {
				theAppMgr.showErrorDlg(res.getString("Negative numbers are not allowed."));
				return false;
			}
			return true;
		} catch (Exception ex) {
			theAppMgr.showErrorDlg(res.getString("The value is not a number."));
			return false;
		}
	}

	/**
	 * @param initialValue
	 * @return
	 */
	public boolean validateInitialValueType(Object initialValue) {
		return (initialValue instanceof Integer);
	}
}
