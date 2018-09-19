package com.chelseasystems.cs.address.qas;

import java.io.Serializable;

import com.chelseasystems.cs.address.Address;
import com.qas.proweb.AddressLine;
import com.qas.proweb.FormattedAddress;
import com.qas.proweb.PicklistItem;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+ 
 | 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

/**
 * <p>Title:QASUtil.java </p>
 *
 * <p>Description: QASUtil </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Skillnet inc.</p>
 *
 * @author David Fung
 * @version 1.0
 */

public class QASUtil implements Serializable {

	public static String[] getInputForSearch(Address address) {
		String[] userInput = new String[6];
		userInput[0] = address.getAddressLine1();
		userInput[1] = address.getAddressLine2();
		userInput[2] = "";
		userInput[3] = address.getCity();
		userInput[4] = address.getZipCode();
		userInput[5] = address.getState();
		return userInput;
	}

	public static Address fixAddress(Address address, FormattedAddress qasAddress, String verifyLevel) {
		String line1 = null;
		String line2 = null;
		String city = null;
		String state = null;
		String zip = null;
		String zipExt = null;
		AddressLine[] lines = qasAddress.getAddressLines();
		if (lines.length > 0) {
			//Reset address object
			address.setAddressLine1("");
			address.setAddressLine2("");
			address.setCity("");
			address.setState("");
			address.setZipCode("");
			address.setZipCodeExtension("");
		}
		for (int i = 0; i < lines.length; i++) {
			String type = lines[i].getLineType();
			String lable = lines[i].getLabel();
			String value = lines[i].getLine();
			if (type.equals("None") && lable.equals("") && !value.equals("")) {
				if (line1 == null) {
					line1 = value;
				} else if (line2 == null) {
					line2 = value;
				}
			} else if (type.equals("Address") && lable.equals("City name") && !value.equals("")) {
				city = value;
			} else if (type.equals("Address") && lable.equals("State code") && !value.equals("")) {
				state = value;
			} else if (type.equals("Address") && lable.equals("") && !value.equals("")) {
				if (value.length() == 10) {
					zip = value.substring(0, 5);
					zipExt = value.substring(6);
				} else {
					zip = value;
				}
			}
		}
		if (line1 != null) {
			address.setAddressLine1(line1);
			address.setQasModified(true);
		}
		if (line2 != null) {
			address.setAddressLine2(line2);
			address.setQasModified(true);
		}
		if (city != null) {
			address.setCity(city);
			address.setQasModified(true);
		}
		if (state != null) {
			address.setState(state);
			address.setQasModified(true);
		}
		if (zip != null) {
			address.setZipCode(zip);
			address.setQasModified(true);
		}
		if (zipExt != null) {
			address.setZipCodeExtension(zipExt);
			address.setQasModified(true);
		}
		address.setQasVerifyLevel(verifyLevel);
		return address;
	}

	public static String formattedAddressToString(FormattedAddress address) {
		if (address == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		AddressLine[] lines = address.getAddressLines();
		for (int i = 0; i < lines.length; i++) {
			sb.append("\n<<[" + i + "]Type:" + lines[i].getLineType() + "|Label:" + lines[i].getLabel()
					+ "|Value:" + lines[i].getLine() + ">>");
		}
		return sb.toString();
	}

	public static String picklistItemToString(PicklistItem item) {
		if (item == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<<PicklistItem:");
		sb.append("[Monikers:" + item.getMoniker() + "]");
		sb.append("[Text:" + item.getText() + "]");
		sb.append("[PartialAddress:" + item.getPartialAddress() + "]");
		sb.append("[Postcode:" + item.getPostcode() + "]>>");
		sb.append("[Multiples:" + item.isMultiples() + "]>>");
		return sb.toString();
	}

	public static String picklistItemToString(QASResponse response, PicklistItem item) {
		if (item == null) {
			return null;
		}
		FormattedAddress address = response.getFormattedAddress(item.getMoniker());
		if (address != null) {
			return QASUtil.formattedAddressToString(address);
		} else {
			return QASUtil.picklistItemToString(item);
		}
	}
}
