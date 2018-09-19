/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.eod.TransactionEOD;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.transaction.CommonTransaction;
import com.chelseasystems.cs.dataaccess.EodDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkEodOracleBean;
import com.chelseasystems.cs.eod.CMSTransactionEOD;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.store.CMSStore;

import java.util.Arrays;

/**
 *
 *  TransactionEOD Data Access Object.<br>
 *  This object encapsulates all database access for TransactionEOD.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>DrawerFund</td><td>RK_EOD</td><td>REGISTER_DRAWER_FUND</td></tr>
 *    <tr><td>Id</td><td>RK_EOD</td><td>AI_TRN</td></tr>
 *    <tr><td>MgrSaysAmex</td><td>RK_EOD</td><td>MGR_SAYS_AMEX</td></tr>
 *    <tr><td>MgrSaysBcrd</td><td>RK_EOD</td><td>MGR_SAYS_BCRD</td></tr>
 *    <tr><td>MgrSaysDisc</td><td>RK_EOD</td><td>MGR_SAYS_DISC</td></tr>
 *    <tr><td>MgrSaysMoneyOrders</td><td>RK_EOD</td><td>MGR_SAYS_MONEY_ORDERS</td></tr>
 *    <tr><td>MgrSaysTravelersChecks</td><td>RK_EOD</td><td>MGR_SAYS_TRAVELERS_CHECKS</td></tr>
 *  </table>
 *
 *  @see TransactionEOD
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkEodOracleBean
 *
 */
public class EodOracleDAO extends BaseOracleDAO implements EodDAO {
	private static TransactionOracleDAO transactionDAO = new TransactionOracleDAO();
	private static ArmEodDtlOracleDAO eodDtlDAO = new ArmEodDtlOracleDAO();
	private static String selectSql = RkEodOracleBean.selectSql;
	private static String insertSql = RkEodOracleBean.insertSql;
	private static String updateSql = RkEodOracleBean.updateSql + where(RkEodOracleBean.COL_AI_TRN);
	private static String deleteSql = RkEodOracleBean.deleteSql + where(RkEodOracleBean.COL_AI_TRN);

	/**
	 * put your documentation comment here
	 * @param id
	 * @return 
	 * @exception SQLException
	 */
	public TransactionEOD selectById(String id) throws SQLException {
		CommonTransaction transaction = transactionDAO.selectById(id);
		if (transaction instanceof TransactionEOD)
			return (TransactionEOD) transaction;
		return null;
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return 
	 * @exception SQLException
	 */
	ParametricStatement[] getInsertSQL(TransactionEOD object) throws SQLException {
		ArrayList statements = new ArrayList();
		try {
			statements.add(new ParametricStatement(insertSql, fromObjectToBean(object).toList()));
			statements.addAll(Arrays.asList(eodDtlDAO.getInsertSQL((CMSTransactionEOD) object)));
			 //Mahesh Nandure : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
			if(((CMSStore)object.getStore()).isFranceStore()){
			 ArmEopDtlOracleDAO eopDtlDAO=new ArmEopDtlOracleDAO();
			statements.addAll(Arrays.asList(eopDtlDAO.getInsertSQL((CMSTransactionEOD) object)));
			}
			 // End Mahesh Nandure : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return 
	 * @exception SQLException
	 */
	ParametricStatement[] getUpdateSQL(TransactionEOD object) throws SQLException {
		ArrayList statements = new ArrayList();
		List params = fromObjectToBean(object).toList();
		params.add(object.getId());
		statements.add(new ParametricStatement(updateSql, params));
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * put your documentation comment here
	 * @param transactionId
	 * @param store
	 * @param registerId
	 * @return 
	 * @exception SQLException
	 */
	TransactionEOD getById(String transactionId, Store store, String registerId) throws SQLException {
		String whereSql = where(RkEodOracleBean.COL_AI_TRN);
		List params = new ArrayList();
		params.add(transactionId);
		TransactionEOD[] transactionEODs = fromBeansToObjects(query(new RkEodOracleBean(), whereSql, params), store, registerId);
		if (transactionEODs == null || transactionEODs.length == 0)
			return null;
		CMSTransactionEOD eodTxn = eodDtlDAO.selectByID((CMSTransactionEOD) transactionEODs[0]);
		return eodTxn;
	}

	/**
	 * put your documentation comment here
	 * @param beans
	 * @param store
	 * @param registerId
	 * @return 
	 * @exception SQLException
	 */
	private TransactionEOD[] fromBeansToObjects(BaseOracleBean[] beans, Store store, String registerId) throws SQLException {
		TransactionEOD[] array = new CMSTransactionEOD[beans.length];
		for (int i = 0; i < array.length; i++)
			array[i] = fromBeanToObject(beans[i], store, registerId);
		return array;
	}

	/**
	 * put your documentation comment here
	 * @param baseBean
	 * @param store
	 * @param registerId
	 * @return 
	 * @exception SQLException
	 */
	private TransactionEOD fromBeanToObject(BaseOracleBean baseBean, Store store, String registerId) throws SQLException {
		RkEodOracleBean bean = (RkEodOracleBean) baseBean;
		CMSTransactionEOD object = new CMSTransactionEOD(store);
		object.doSetId(bean.getAiTrn());
	 ArmCurrency[] cashes = fromStringToCurrencies(bean.getMgrSaysCashList());
		for (int i = 0; i < cashes.length; i++)
			object.doAddMgrSaysCash(cashes[i]);
	 ArmCurrency[] checks = fromStringToCurrencies(bean.getMgrSaysChekList());
		for (int i = 0; i < checks.length; i++)
			object.doAddMgrSaysCheck(checks[i]);
		object.doSetMgrSaysBcrd(bean.getMgrSaysBcrd());
		object.doSetMgrSaysAmex(bean.getMgrSaysAmex());
		object.doSetMgrSaysDisc(bean.getMgrSaysDisc());
		object.doSetMgrSaysTravelersChecks(bean.getMgrSaysTrChks());
		object.doSetMgrSaysMoneyOrders(bean.getMgrSaysMnyOdr());
		object.doSetMgrSaysMallCert(bean.getMgrSaysMallcertificate());
		CMSRegister register = new CMSRegister(registerId, store.getId());
		register.doSetDrawerFund(bean.getRegDrwrFund());
		object.doSetRegister(register);
		return object;
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return 
	 */
	private RkEodOracleBean fromObjectToBean(TransactionEOD object) {
		RkEodOracleBean bean = new RkEodOracleBean();
		bean.setAiTrn(object.getId());
		bean.setMgrSaysCashList(fromCurrenciesToString(object.getMgrSaysCash()));
		bean.setMgrSaysChekList(fromCurrenciesToString(object.getMgrSaysCheck()));
		bean.setMgrSaysBcrd(object.getMgrSaysBcrd());
		bean.setMgrSaysAmex(object.getMgrSaysAmex());
		bean.setMgrSaysDisc(object.getMgrSaysDisc());
		bean.setMgrSaysTrChks(object.getMgrSaysTravelersChecks());
		bean.setMgrSaysMnyOdr(object.getMgrSaysMoneyOrders());
		bean.setMgrSaysMallcertificate(((CMSTransactionEOD) object).getMgrSaysMallCert());
		if (object.getRegister() != null) {
			bean.setRegDrwrFund(object.getRegister().getDrawerFund());
		}
		return bean;
	}

	/**
	 * put your documentation comment here
	 * @param string
	 * @return 
	 * @exception SQLException
	 */
	public ArmCurrency[] fromStringToCurrencies(String string) throws SQLException {
		if (string == null || string.length() == 0)
			return new ArmCurrency[0];
		List list = new ArrayList();
		StringTokenizer st = new StringTokenizer(string, "*");
		try {
			while(st.hasMoreTokens())
				list.add(ArmCurrency.valueOf(st.nextToken()));
			return (ArmCurrency[]) list.toArray(new ArmCurrency[0]);
		} catch (Exception ex) {
			throw new SQLException(ex.getMessage());
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param currencies
	 * @return
	 */
	private String fromCurrenciesToString(ArmCurrency[] currencies) {
		if (currencies == null || currencies.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < currencies.length; i++)
			sb.append(currencies[i].toDelimitedString() + "*");
		return sb.toString();
	}
}
