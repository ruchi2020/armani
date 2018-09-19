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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.LayawayLineItem;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.ReturnLineItem;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSReturnLineItemDetail;
import com.chelseasystems.cr.pos.VoidTransaction;
import com.chelseasystems.cs.dataaccess.EmpSaleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkEmpSaleOracleBean;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.pos.CMSEmpSales;
import com.chelseasystems.cs.pos.CMSConsignmentLineItem;
import com.chelseasystems.cs.pos.CMSPresaleLineItem;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.pos.CMSSaleLineItem;

/**
 * Employee Sale Data Access Object.<br>
 * This object encapsulates all database access for Employee Sale.
 * <p>
 * 
 * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkEmpSaleOracleBean
 */
public class EmpSaleOracleDAO extends BaseOracleDAO implements EmpSaleDAO {
	private static EmployeeOracleDAO employeeDAO = new EmployeeOracleDAO();
	private static String selectSql = RkEmpSaleOracleBean.selectSql;
	private static String insertSql = RkEmpSaleOracleBean.insertSql;

	/**
	 * put your documentation comment here
	 * 
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getInsertSQL(VoidTransaction object) throws SQLException {
		ParametricStatement[] statements = null;
		if (object.getOriginalTransaction() instanceof CompositePOSTransaction)
			statements = getInsertSQL((CompositePOSTransaction) object.getOriginalTransaction(), true);
		else
			statements = new ParametricStatement[0];
		return statements;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getInsertSQL(CompositePOSTransaction object) throws SQLException {
		return getInsertSQL(object, false);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param object
	 * @exception SQLException
	 */
	public void insert(VoidTransaction object) throws SQLException {
		execute(getInsertSQL(object));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param object
	 * @exception SQLException
	 */
	public void insert(CompositePOSTransaction object) throws SQLException {
		execute(getInsertSQL(object));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param object
	 * @param subtract
	 * @return
	 * @exception SQLException
	 */
	private ParametricStatement[] getInsertSQL(CompositePOSTransaction object, boolean subtract) throws SQLException {
		ArrayList statements = new ArrayList();
		String storeId = object.getStore().getId();
		String consultantId = object.getConsultant().getId();
		Date saleDate = object.getProcessDate();
	ArmCurrency amount = null;
	ArmCurrency netAmount = null;
		String transactionId = object.getId();
		int totalQty = 0;
		POSLineItem[] lineItems = object.getLineItemsArray();
		for (int i = 0; i < lineItems.length; i++) {
			if (lineItems[i].isMiscItem() && !LineItemPOSUtil.isNotOnFileItem(lineItems[i].getItem().getId()))
				continue;
			if (!(lineItems[i] instanceof LayawayLineItem) && !(lineItems[i] instanceof CMSConsignmentLineItem) && !(lineItems[i] instanceof CMSPresaleLineItem)) {
				if (subtract) {
					if (lineItems[i] instanceof CMSReturnLineItem) {
						CMSReturnLineItemDetail returnLnItmDtl = (CMSReturnLineItemDetail) lineItems[i].getLineItemDetailsArray()[0];
						if (returnLnItmDtl.getConsignmentLineItemDetail() != null || returnLnItmDtl.getPresaleLineItemDetail() != null)
							continue;
						amount = lineItems[i].getTotalAmountDue();
						netAmount = lineItems[i].getExtendedNetAmount();
						totalQty = lineItems[i].getQuantity().intValue();
					} else {
						amount = lineItems[i].getTotalAmountDue().multiply(-1);
						netAmount = lineItems[i].getExtendedNetAmount().multiply(-1);
						totalQty = lineItems[i].getQuantity().intValue() * -1;
					}
				} else {
					if (lineItems[i] instanceof CMSReturnLineItem) {
						CMSReturnLineItemDetail returnLnItmDtl = (CMSReturnLineItemDetail) lineItems[i].getLineItemDetailsArray()[0];
						if (returnLnItmDtl.getConsignmentLineItemDetail() != null || returnLnItmDtl.getPresaleLineItemDetail() != null)
							continue;
						amount = lineItems[i].getTotalAmountDue().multiply(-1);
						netAmount = lineItems[i].getExtendedNetAmount().multiply(-1);
						totalQty = lineItems[i].getQuantity().intValue() * -1;
					} else {
						amount = lineItems[i].getTotalAmountDue();
						netAmount = lineItems[i].getExtendedNetAmount();
						totalQty = lineItems[i].getQuantity().intValue();
					}
				}
				Employee additionalConsultant = lineItems[i].getAdditionalConsultant();
				if (additionalConsultant == null)
					statements.add(new ParametricStatement(insertSql, fromObjectToBean(storeId, consultantId, saleDate, amount, netAmount, transactionId, totalQty).toList()));
				else
					statements.add(new ParametricStatement(insertSql, fromObjectToBean(storeId, additionalConsultant.getId(), saleDate, amount, netAmount, transactionId, totalQty).toList()));
			}
		}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	public ParametricStatement[] getUpdateSql(CompositePOSTransaction object, POSLineItem[] posLineItems) {
		ArrayList statements = new ArrayList();
		String storeId = object.getStore().getId();
		String consultantId = object.getConsultant().getId();
		String oldConsultantId = null;
		Date saleDate = object.getProcessDate();
	ArmCurrency amount = null;
	ArmCurrency netAmount = null;
		String transactionId = object.getId();
		int totalQty = 0;
		POSLineItem[] lineItems = posLineItems;
		for (int i = 0; i < lineItems.length; i++) {
			if (lineItems[i].isMiscItem() && !LineItemPOSUtil.isNotOnFileItem(lineItems[i].getItem().getId()))
				continue;
			if (lineItems[i] instanceof CMSSaleLineItem) {
				if (((CMSSaleLineItem) lineItems[i]).getOldConsultant() != null && lineItems[i].getAdditionalConsultant() != null)
					oldConsultantId = ((CMSSaleLineItem) lineItems[i]).getOldConsultant().getId();
			} else if (lineItems[i] instanceof CMSReturnLineItem) {
				if (((CMSReturnLineItem) lineItems[i]).getOldConsultant() != null && lineItems[i].getAdditionalConsultant() != null)
					oldConsultantId = ((CMSReturnLineItem) lineItems[i]).getOldConsultant().getId();
			}
			if (oldConsultantId != null) {
				Employee additionalConsultant = lineItems[i].getAdditionalConsultant();
				amount = lineItems[i].getTotalAmountDue();
				netAmount = lineItems[i].getExtendedNetAmount();
				totalQty = lineItems[i].getQuantity().intValue();
				statements.add(new ParametricStatement(insertSql, fromObjectToBean(storeId, oldConsultantId, saleDate, amount.multiply(-1), netAmount.multiply(-1), transactionId, totalQty).toList()));
				statements.add(new ParametricStatement(insertSql, fromObjectToBean(storeId, additionalConsultant.getId(), saleDate, amount, netAmount, transactionId, totalQty).toList()));
			}
		}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param employeeId
	 * @param begin
	 * @param end
	 * @return
	 * @exception SQLException
	 */
	public CMSEmpSales[] selectByEmployeeAndDates(String employeeId, Date begin, Date end) throws SQLException {
		String whereSql = "where " + RkEmpSaleOracleBean.COL_ID_EM + " = ? and " + RkEmpSaleOracleBean.COL_SALE_DATE + " >= ? and " + RkEmpSaleOracleBean.COL_SALE_DATE + " <= ?";
		List params = new ArrayList();
		params.add(employeeId);
		params.add(begin);
		params.add(end);
		return fromBeansToObjects(query(new RkEmpSaleOracleBean(), whereSql, params));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param storeId
	 * @param begin
	 * @param end
	 * @return
	 * @exception SQLException
	 */
	public CMSEmpSales[] selectByStoreAndDates(String storeId, Date begin, Date end) throws SQLException {
		String whereSql = "where " + RkEmpSaleOracleBean.COL_ID_STR_RT + " = ? and " + RkEmpSaleOracleBean.COL_SALE_DATE + " >= ? and " + RkEmpSaleOracleBean.COL_SALE_DATE + " <= ?";
		List params = new ArrayList();
		params.add(storeId);
		params.add(begin);
		params.add(end);
		return fromBeansToObjects(query(new RkEmpSaleOracleBean(), whereSql, params));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param storeId
	 * @param begin
	 * @param end
	 * @return
	 * @exception SQLException
	 */
	public Employee[] selectEmployeesByStoreAndDates(String storeId, Date begin, Date end) throws SQLException {
		String whereSql = "where " + RkEmpSaleOracleBean.COL_ID_STR_RT + " = ? and " + RkEmpSaleOracleBean.COL_SALE_DATE + " >= ? and " + RkEmpSaleOracleBean.COL_SALE_DATE + " <= ?";
		List params = new ArrayList();
		params.add(storeId);
		params.add(begin);
		params.add(end);
		BaseOracleBean[] beans = query(new RkEmpSaleOracleBean(), whereSql, params);
		Set employeeIds = new HashSet();
		for (int i = 0; i < beans.length; i++)
			employeeIds.add(((RkEmpSaleOracleBean) beans[i]).getIdEm());
		List employees = new ArrayList();
		for (Iterator it = employeeIds.iterator(); it.hasNext();)
			employees.add(employeeDAO.selectById((String) it.next()));
		Employee[] e = (Employee[]) employees.toArray(new CMSEmployee[0]);
		return (Employee[]) employees.toArray(new CMSEmployee[0]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param storeId
	 * @param begin
	 * @param end
	 * @return
	 * @exception SQLException
	 */
	public CMSEmpSales[] selectTotalSaleByStoreAndDates(String storeId, Date begin, Date end) throws SQLException {
		String whereSql =
				" where RK_EMP_SALE.ID_EM = ID_PRTY_PRS AND PA_EM.ID_EM = RK_EMP_SALE.ID_EM AND RK_EMP_SALE.ID_STR_RT=PA_STR_RTL.ID_STR_RT AND " + RkEmpSaleOracleBean.COL_ID_STR_RT + " = ? and "
						+ RkEmpSaleOracleBean.COL_SALE_DATE + " >= ? and " + RkEmpSaleOracleBean.COL_SALE_DATE + " <= ?";
		String selectsql =
				"SELECT RK_EMP_SALE.ID_STR_RT, SALE_DATE, PA_EM.ARM_EXTERNAL_ID ID_EM, FN_PRS, LN_PRS, NULL TRANSACTION_NUMBER, Arm_Util_Pkg.Convert_To_CURRENCY(SUM(Arm_Util_Pkg.Convert_To_Number(AMOUNT, PA_STR_RTL.TY_CNY)),PA_STR_RTL.TY_CNY,'0') AMOUNT, Arm_Util_Pkg.Convert_To_CURRENCY(SUM(Arm_Util_Pkg.Convert_To_Number(NET_AMOUNT, PA_STR_RTL.TY_CNY)),PA_STR_RTL.TY_CNY,'0') NET_AMOUNT, SUM(TOTAL_QTY) TOTAL_QTY, "
						+ "COUNT(DISTINCT TRANSACTION_NUMBER) TXN_COUNT FROM RK_EMP_SALE, PA_EM, PA_PRS,PA_STR_RTL"
						+ whereSql
						+ " GROUP BY RK_EMP_SALE.ID_STR_RT, SALE_DATE, PA_EM.ARM_EXTERNAL_ID, FN_PRS, LN_PRS, PA_STR_RTL.TY_CNY";
		List params = new ArrayList();
		params.add(storeId);
		params.add(begin);
		params.add(end);
		return fromBeansToObjects(query(new RkEmpSaleOracleBean(), selectsql, params));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	protected BaseOracleBean getDatabeanInstance() {
		return new RkEmpSaleOracleBean();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param beans
	 * @return
	 * @exception SQLException
	 */
	private CMSEmpSales[] fromBeansToObjects(BaseOracleBean[] beans) throws SQLException {
		CMSEmpSales[] array = new CMSEmpSales[beans.length];
		for (int i = 0; i < array.length; i++)
			array[i] = fromBeanToObject(beans[i]);
		return array;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param baseBean
	 * @return
	 * @exception SQLException
	 */
	private CMSEmpSales fromBeanToObject(BaseOracleBean baseBean) throws SQLException {
		CMSEmpSales empSale = new CMSEmpSales();
		RkEmpSaleOracleBean bean = (RkEmpSaleOracleBean) baseBean;
		CMSEmployee employee = new CMSEmployee(bean.getIdEm());
		employee.doSetFirstName(bean.getFirstName());
		employee.doSetLastName(bean.getLastName());
		empSale.setConsultantId(employee);
		empSale.setQuantity(bean.getTotalQty());
		empSale.setSaleDate(bean.getSaleDate());
		empSale.setStoreId(bean.getIdStrRt());
		empSale.setTransactionId(bean.getTransactionNumber());
		empSale.setAmount(bean.getAmount());
		empSale.setNetAmount(bean.getNetAmount());
		empSale.setTxnCount(bean.getTxnCount());
		return empSale;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param storeId
	 * @param consultantId
	 * @param saleDate
	 * @param amount
	 * @param netAmount
	 * @param transactionId
	 * @param totalQty
	 * @return
	 */
	private RkEmpSaleOracleBean fromObjectToBean(String storeId, String consultantId, Date saleDate, ArmCurrency amount, ArmCurrency netAmount, String transactionId, int totalQty) {
		RkEmpSaleOracleBean bean = new RkEmpSaleOracleBean();
		bean.setIdStrRt(storeId);
		bean.setIdEm(consultantId);
		bean.setSaleDate(saleDate);
		bean.setAmount(amount);
		bean.setNetAmount(netAmount);
		bean.setTransactionNumber(transactionId);
		bean.setTotalQty(totalQty);
		return bean;
	}
}
