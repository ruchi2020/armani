/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//

package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmEmpSaleSummaryOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCustSaleSummaryOracleBean;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CustomerSaleSummary;
import com.chelseasystems.cs.customer.CustomerSearchString;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.VoidTransaction;

import java.sql.*;
import java.util.*;

/**
 * put your documentation comment here
 */
public class ArmCustSaleSummaryOracleDAO extends BaseOracleDAO implements ArmCustSaleSummaryDAO {
	private static String selectSql = ArmCustSaleSummaryOracleBean.selectSql;
	private static String insertSql = ArmCustSaleSummaryOracleBean.insertSql;

	/**
	 * put your documentation comment here
	 * @param object
	 * @exception SQLException
	 */
	public void insert(CompositePOSTransaction object) throws SQLException {
		this.execute(this.getInsertSQL(object));
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getInsertSQL(CompositePOSTransaction object) throws SQLException {
		return getInsertSQL(object, false);
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 */
	public ParametricStatement[] getInsertSQL(CompositePOSTransaction object, boolean subtract) {
		List statements = new ArrayList();
		ArmCustSaleSummaryOracleBean bean = null;
		CustomerSaleSummary custSaleSummary = null;
		// added by vivek
		ArmEmpSaleSummaryOracleBean empBean = null;
		String txnType = object.getTransactionType();
		if (txnType.equals("SALE")) {
			custSaleSummary = new CustomerSaleSummary();
			custSaleSummary.setCustomerId(object.getCustomer().getId());
			custSaleSummary.setStoreId(object.getStore().getId());
			custSaleSummary.setTxnDate(object.getProcessDate());
			custSaleSummary.setTxnType(txnType);
			//added by vivek on 23 Nov 08
			// we can add line item level discount in ARM_EMP_SALE_SUMMARY table.
			int len = object.getSaleLineItemsArray().length;
			POSLineItem[] array =object.getSaleLineItemsArray();
			for( int i=0;i<len; i++){
				array[i].getNetAmount();
				array[i].getExtendedNetAmount();
				array[i].getExtendedRetailAmount();
					if(array[i].getDiscount()!=null){					
						custSaleSummary.setDisPercent(array[i].getDiscount().getPercent());
				}
				else{
					custSaleSummary.setDisPercent(0.0d);
					
				}				
				custSaleSummary.setNetAmount(array[i].getNetAmount().multiply(array[i].getQuantity().intValue()));
				empBean = this.fromObjectToBeans(custSaleSummary);
				statements.add(new ParametricStatement(empBean.getInsertSql(), empBean.toList()));
			}
			//ended by vivek
			
			if (subtract) {
				custSaleSummary.setTxnAmount(object.getSaleNetAmount().multiply(-1));
			} else {
				custSaleSummary.setTxnAmount(object.getSaleNetAmount());
			}
			bean = this.fromObjectToBean(custSaleSummary);
			statements.add(new ParametricStatement(bean.getInsertSql(), bean.toList()));
		}
		if (txnType.equals("RETN")) {
			//subtract=true;
			custSaleSummary = new CustomerSaleSummary();
			custSaleSummary.setCustomerId(object.getCustomer().getId());
			custSaleSummary.setStoreId(object.getStore().getId());
			custSaleSummary.setTxnDate(object.getProcessDate());
			custSaleSummary.setTxnType(txnType);
			
			CMSCustomer customer = (CMSCustomer) object.getCustomer();
			if(customer!=null && customer.getCustomerType()!=null &&  customer.getCustomerType().equals(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE)){			
			// saptarshi-start code -for return on arm_emp_sale_summary table
			int len = object.getReturnLineItemsArray().length;
			POSLineItem[] array =object.getReturnLineItemsArray();
			//ArmCurrency dis=new ArmCurrency(0.0d);
			if(len>0){
			for( int i=0;i<len; i++){				
				
				ArmCurrency dis=null;
				try {
					dis = array[i].getExtendedRetailAmount().subtract(array[i].getNetAmount());
					Double extendedRetailAmount=array[i].getExtendedRetailAmount().doubleValue();
					dis=dis.divide(extendedRetailAmount);
					//Double discountPercent=dis.multiply(100).doubleValue();
					Double discountPercent=dis.doubleValue();
					if(discountPercent!=null){
						custSaleSummary.setDisPercent(discountPercent);
					}else{
						custSaleSummary.setDisPercent(0.0d);
					}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(Exception e){
					e.printStackTrace();
				}
				
				custSaleSummary.setNetAmount(array[i].getNetAmount());
				empBean = this.fromObjectToBeans(custSaleSummary);
				statements.add(new ParametricStatement(empBean.getInsertSql(), empBean.toList()));
			}
			}
			}
			//saptarshi-end code -for return on arm_emp_sale_summary table
			
			if (subtract) {
				custSaleSummary.setTxnAmount(object.getReturnNetAmount().multiply(-1));				
			} else {
				custSaleSummary.setTxnAmount(object.getReturnNetAmount());
			}
			bean = this.fromObjectToBean(custSaleSummary);
			statements.add(new ParametricStatement(bean.getInsertSql(), bean.toList()));
			
			
			
		}
		if (txnType.equals("SALE/RETN")) {
			
			
			//start-saptarshi- for the part of sale transaction when transaction type is sale/return
			
			custSaleSummary = new CustomerSaleSummary();
			custSaleSummary.setCustomerId(object.getCustomer().getId());
			custSaleSummary.setStoreId(object.getStore().getId());
			custSaleSummary.setTxnDate(object.getProcessDate());
			custSaleSummary.setTxnType("SALE");
			
			
			CMSCustomer customer = (CMSCustomer) object.getCustomer();
			if(customer!=null && customer.getCustomerType()!=null && customer.getCustomerType().equals(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE)){
			int len = object.getSaleLineItemsArray().length;
			if(len>0){
			POSLineItem[] array =object.getSaleLineItemsArray();
			for( int i=0;i<len; i++){
				
					if(array[i].getDiscount()!=null){					
						custSaleSummary.setDisPercent(array[i].getDiscount().getPercent());
				}
				else{
					custSaleSummary.setDisPercent(0.0d);
					
				}				
				custSaleSummary.setNetAmount(array[i].getNetAmount().multiply(array[i].getQuantity().intValue()));
				empBean = this.fromObjectToBeans(custSaleSummary);
				statements.add(new ParametricStatement(empBean.getInsertSql(), empBean.toList()));
			}
			
			}
			}
			
			if (subtract) {
				custSaleSummary.setTxnAmount(object.getSaleNetAmount().multiply(-1));
			} else {
				custSaleSummary.setTxnAmount(object.getSaleNetAmount());
			}
			
			bean = this.fromObjectToBean(custSaleSummary);
			statements.add(new ParametricStatement(bean.getInsertSql(), bean.toList()));
			
			
			//start-saptarshi- for the part of return transaction when transaction type is sale/return
			
			int lenReturn = object.getReturnLineItemsArray().length;
			if(lenReturn>0){
				//subtract=true;
				custSaleSummary.setTxnType("RETN");
				CMSCustomer customerReturn = (CMSCustomer) object.getCustomer();
				if(customerReturn!=null && customer.getCustomerType()!=null && customerReturn.getCustomerType().equals(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE)){
				POSLineItem[] array =object.getReturnLineItemsArray();
				for( int i=0;i<lenReturn; i++){
					
					
					ArmCurrency dis=null;
					try {
						dis = array[i].getExtendedRetailAmount().subtract(array[i].getNetAmount());
						Double extendedRetailAmount=array[i].getExtendedRetailAmount().doubleValue();
						dis=dis.divide(extendedRetailAmount);
						//Double discountPercent=dis.multiply(100).doubleValue();
						Double discountPercent=dis.doubleValue();
						if(discountPercent!=null){
							custSaleSummary.setDisPercent(discountPercent);
						}else{
							custSaleSummary.setDisPercent(0.0d);
						}
					} catch (CurrencyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch(Exception e){
						e.printStackTrace();
					}
					
					custSaleSummary.setNetAmount(array[i].getNetAmount());
					empBean = this.fromObjectToBeans(custSaleSummary);
					statements.add(new ParametricStatement(empBean.getInsertSql(), empBean.toList()));
				}
				}
				
				if (subtract) {
					custSaleSummary.setTxnAmount(object.getReturnNetAmount().multiply(-1));
				} else {
					custSaleSummary.setTxnAmount(object.getReturnNetAmount());
				}
				
				bean = this.fromObjectToBean(custSaleSummary);
				statements.add(new ParametricStatement(bean.getInsertSql(), bean.toList()));
				
			}
			
			
			

		}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 */
	public ParametricStatement[] getInsertSQL(VoidTransaction object) {
		ParametricStatement[] statements = null;
		if (object.getOriginalTransaction() instanceof CompositePOSTransaction) {
			statements = getInsertSQL((CompositePOSTransaction) object.getOriginalTransaction(), true);
		} else {
			statements = new ParametricStatement[0];
		}
		return statements;
	}

	/**
	 * put your documentation comment here
	 * @param custId
	 * @param storeId
	 * @return
	 * @exception SQLException
	 */
	public CustomerSaleSummary[] selectByCustNStoreId(String custId, String storeId) throws SQLException {
		List params = null;
		BaseOracleBean[] beans = null;
		CustomerSaleSummary[] cssList = null;
		params = new ArrayList();
		params.add(custId);
		params.add(storeId);
		beans = this.query(new ArmCustSaleSummaryOracleBean(), BaseOracleDAO.where(ArmCustSaleSummaryOracleBean.COL_ID_CT, ArmCustSaleSummaryOracleBean.COL_ID_STR_RT) + "ORDER BY "
				+ ArmCustSaleSummaryOracleBean.COL_TXN_DATE + " DESC", params);
		if (beans != null && beans.length > 0) {
			cssList = this.fromBeansToObjects(beans);
		}
		return cssList;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	protected BaseOracleBean getDatabeanInstance() {
		return new ArmCustSaleSummaryOracleBean();
	}

	/**
	 * put your documentation comment here
	 * @param beans
	 * @return
	 */
	private CustomerSaleSummary[] fromBeansToObjects(BaseOracleBean[] beans) {
		CustomerSaleSummary[] array = new CustomerSaleSummary[beans.length];
		for (int i = 0; i < array.length; i++)
			array[i] = fromBeanToObject(beans[i]);
		return array;
	}

	/**
	 * put your documentation comment here
	 * @param baseBean
	 * @return
	 */
	private CustomerSaleSummary fromBeanToObject(BaseOracleBean baseBean) {
		ArmCustSaleSummaryOracleBean bean = (ArmCustSaleSummaryOracleBean) baseBean;
		ArmEmpSaleSummaryOracleBean empBean = (ArmEmpSaleSummaryOracleBean) baseBean;
		
		CustomerSaleSummary object = new CustomerSaleSummary();
		object.setCustomerId(bean.getIdCt());
		object.setStoreId(bean.getIdStrRt());
		object.setTxnType(bean.getTxnType());
		object.setTxnDate(bean.getTxnDate());
		object.setTxnAmount(bean.getTxnAmount());
		//added by vivek
		object.setNetAmount(empBean.getNetAmount());
		object.setDisPercent(empBean.getDisPercent());
		//end by vivek
		return object;
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 */
	private ArmCustSaleSummaryOracleBean fromObjectToBean(CustomerSaleSummary object) {
		ArmCustSaleSummaryOracleBean bean = new ArmCustSaleSummaryOracleBean();
		bean.setIdCt(object.getCustomerId());
		bean.setIdStrRt(object.getStoreId());
		bean.setTxnType(object.getTxnType());
		bean.setTxnDate(object.getTxnDate());
		bean.setTxnAmount(object.getTxnAmount());
		return bean;
	}	

	private ArmEmpSaleSummaryOracleBean fromObjectToBeans(CustomerSaleSummary object) {
		ArmEmpSaleSummaryOracleBean bean = new ArmEmpSaleSummaryOracleBean();
		bean.setIdCt(object.getCustomerId());
		bean.setIdStrRt(object.getStoreId());
		bean.setTxnType(object.getTxnType());
		bean.setTxnDate(object.getTxnDate());
		bean.setNetAmount(object.getNetAmount());
		bean.setDisPercent(object.getDisPercent());
		return bean;
	}	

	
	public ParametricStatement[] getUpdateCustomerSQL(CompositePOSTransaction object) throws SQLException {
		return this.getInsertSQL(object);
	}

}
