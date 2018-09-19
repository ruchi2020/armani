package com.chelseasystems.cs.dataaccess;

import java.util.HashMap;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.eod.CMSTransactionEOD;

public interface ArmEopDtlDAO extends BaseDAO {
	  
	  /**
	   * Ruchi Gupta : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
	   * @param txnObject (CMSTransactionSOS OR CMSTransactionEOD)
	   * @return MAP (Calculated  netAmount of Month and Year (if end of year)
	   * @throws Exception
	   */
	public abstract HashMap<String, Double> submitAndReturnNetAmount(Object txnObject) throws Exception;

}
