//Title:        ChelseaStore
//Version:
//Copyright:    Copyright (c)2000
//Author:       Ruben Rodriguez
//Company:      Chelsea Systems
//Description:

package com.chelseasystems.cr.appmgr.mask;

import com.chelseasystems.cr.appmgr.*;

public class RequiredMask extends MaskGenericRenderer implements IMask {

	private IApplicationManager theAppMgr;
	java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

	public RequiredMask() {
	}

	public Object validateMask(IApplicationManager theAppMgr, Object edtValue) {
		this.theAppMgr = theAppMgr;
		String edtString = (String) edtValue;
		if (!testMask(edtString))
			return null;
		else
			return edtValue;
	}

	private boolean testMask(String value) {
		try {
			if (value.length() == 0) {
				theAppMgr.showErrorDlg(res.getString("A value must be entered."));
				return false;
			}
			return true;
		} catch (Exception ex) {
			theAppMgr.showErrorDlg(res.getString("A value must be entered."));
			return false;
		}
	}

	public boolean validateInitialValueType(Object initialValue) {
		System.out.println("**********INSIDE THE NEW VALIDATE INITIAL VALUE CLASS");
		return true;

	}
}
