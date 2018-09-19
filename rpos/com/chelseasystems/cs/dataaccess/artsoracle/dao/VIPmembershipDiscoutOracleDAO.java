package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmVipDiscountDetailOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmDscOracleBean;

public class VIPmembershipDiscoutOracleDAO extends BaseOracleDAO {
	 public Map[] selectByMembershipNo (String membershipNo) throws SQLException {
		    String whereSql = where(ArmVipDiscountDetailOracleBean.COL_MEMBERSHIP_NUMBER);
		    return  (fromBeansToObjects(query(new ArmVipDiscountDetailOracleBean(), whereSql, membershipNo)));
		  }
	
	 private Map[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
		 Map[] array = new Map[beans.length];
		    for (int i = 0; i < array.length; i++)
		      array[i] = fromBeanToObject(beans[i]);
		    return  (array);
		  }
	 
	 
	 private Map fromBeanToObject (BaseOracleBean baseBean) throws SQLException {
		 ArmVipDiscountDetailOracleBean bean =  (ArmVipDiscountDetailOracleBean) baseBean;
		 Map object= new HashMap();
		 if(bean!= null){
			 object.put("CUSTOMER_ID",bean.getCust_id());
			 object.put("MEMBERSHIP_NO",bean.getMembership_no());
			 object.put("EXPIRY_DATE",bean.getExpiryDate());
		
		 }
		 return object;
	 }
}
