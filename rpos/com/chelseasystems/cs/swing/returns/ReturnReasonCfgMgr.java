/**
 * Title:        <p>
 * Description:  <Singleton to manage return reasons from config file>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author       Dan Reading
 * @version 1.0
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.returns;

import java.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.util.ArmConfigLoader;

public class ReturnReasonCfgMgr {
	private static ReturnReasonStruct[] returnReasonsArray;
	private static String types[];
	private static ConfigMgr config = new ConfigMgr("returns.cfg");

	private ReturnReasonCfgMgr() throws Exception {
		String strIsReasonDerived = config.getString("IS_REASON_DERIVED_FROM_DB");
		boolean isReasonDerived = false;
		if (strIsReasonDerived != null) {
			isReasonDerived = strIsReasonDerived.equalsIgnoreCase("true");
		}
		if (isReasonDerived)
			loadReturnReasonsFromCommonConfig();
		else
			loadReturnReasonsFromConfig();
	}

	public static ReturnReasonStruct[] getReturnReasons() throws Exception {
		if (returnReasonsArray == null)
			new ReturnReasonCfgMgr();
		return returnReasonsArray;
	}

	public static ReturnReasonStruct getReturnReason(String key) throws Exception {
		if (returnReasonsArray == null)
			new ReturnReasonCfgMgr();
		for (int i = 0; i < returnReasonsArray.length; i++)
			if (key.equals(returnReasonsArray[i].key))
				return returnReasonsArray[i];
		return null;
	}

	private void loadReturnReasonsFromConfig() throws Exception {
		ArrayList returnReasons = new ArrayList();
		INIFile file = new INIFile(FileMgr.getLocalFile("config", "returns.cfg"), true);
		StringTokenizer st;
		String reasonList = file.getValue("REASON_LIST");
		st = new StringTokenizer(reasonList, ",");
		while(st.hasMoreElements()) {
			String key = (String) st.nextElement();
			String reason = file.getValue(key, "REASON", "");
			boolean printMerchandiseReceipt = (file.getValue(key, "PRINT_MERCHANDISE_RECEIPT", "")).equals("YES");
			boolean commentsRequired = (file.getValue(key, "COMMENTS", "")).equals("YES");
			returnReasons.add(new ReturnReasonStruct(key, reason, printMerchandiseReceipt, commentsRequired));
		}
		returnReasonsArray = (ReturnReasonStruct[]) returnReasons.toArray(new ReturnReasonStruct[returnReasons.size()]);
	}

	private void loadReturnReasonsFromCommonConfig() throws Exception {
		ArrayList returnReasons = new ArrayList();
		ArmConfigLoader configMgr = new ArmConfigLoader();
		String reasonTypes = configMgr.getString("RETURN_REASON_CD");
		if (reasonTypes != null && reasonTypes.length() > 1) {
			StringTokenizer stk = new StringTokenizer(reasonTypes, ",");
			types = new String[stk.countTokens()];
			int i = -1;
			// String reasonLbl;
			while(stk.hasMoreTokens()) {
				types[++i] = stk.nextToken();
				String key = configMgr.getString(types[i] + ".CODE");
				// String code = config.getString(types[i] + ".DESC");
				String reason = configMgr.getString(types[i] + ".LABEL");
				boolean printMerchandiseReceipt = false;
				boolean commentsRequired = false;
				returnReasons.add(new ReturnReasonStruct(key, reason, printMerchandiseReceipt, commentsRequired));
				returnReasonsArray = (ReturnReasonStruct[]) returnReasons.toArray(new ReturnReasonStruct[returnReasons.size()]);
			}
		}
	}
}
