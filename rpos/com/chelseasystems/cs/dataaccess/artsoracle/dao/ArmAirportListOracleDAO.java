package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.currency.CurrencyRate;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.dataaccess.ArmAirportListDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmAirportListOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmBoardingPassOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCtDtlOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrsOracleBean;

public class ArmAirportListOracleDAO extends BaseOracleDAO implements
		ArmAirportListDAO {

	public CMSAirportDetails[] getAirportDetails() throws SQLException {
		// TODO Auto-generated method stub
		  String sql = "select * from " + ArmAirportListOracleBean.TABLE_NAME;
		    List params = new ArrayList();
		    return  fromBeansToObjects(query(new ArmAirportListOracleBean(), sql, params));
		  
	}
	
	  private CMSAirportDetails[] fromBeansToObjects (BaseOracleBean[] beans) {
		  CMSAirportDetails[] array = new CMSAirportDetails[beans.length];
		    for (int i = 0; i < array.length; i++)
		      array[i] = fromBeanToObject(beans[i]);
		    return  array;
		  }
	  
	  private CMSAirportDetails fromBeanToObject (BaseOracleBean baseBean) {
		  ArmAirportListOracleBean bean = (ArmAirportListOracleBean)baseBean;
		  CMSAirportDetails object = new CMSAirportDetails();
		    object.setAirportDesc(bean.getAirportDesc());
		    object.setAirportCode(bean.getAirportCode());
		    object.setAreaCode(bean.getAreaCode());
		    object.setFlag(bean.getFlag());
		    return  object;
		  }
	  protected BaseOracleBean getDatabeanInstance () {
		    return  new ArmAirportListOracleBean();
		  }

	public void insert(CMSAirportDetails airportDetails) throws SQLException {
		// TODO Auto-generated method stub
		this.execute(this.getInsertSQL(airportDetails));
	}

	public ParametricStatement[] getInsertSQL(CMSAirportDetails airportDetails) {
		// TODO Auto-generated method stub
		List statements = new ArrayList();
		try{
		ArmBoardingPassOracleBean boardingPassBean = this.toBoardingPassBean(airportDetails);
		statements.add(new ParametricStatement(boardingPassBean.getInsertSql(), boardingPassBean.toList()));
		}
	    catch(Exception e) {
	        e.printStackTrace();
	    }
	    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
	}

	ArmBoardingPassOracleBean toBoardingPassBean (CMSAirportDetails airportDetails) {
		ArmBoardingPassOracleBean bean = new ArmBoardingPassOracleBean();
	    bean.setDestination(airportDetails.getDestination());
	    bean.setCheckInNo(airportDetails.getCheckIn());
	    bean.setComp(airportDetails.getComp());
	    bean.setFlightNo(airportDetails.getFlightNo());
	    bean.setSeatNo(airportDetails.getSeatNo());
	    bean.setEntry(airportDetails.getEntryType());
	    bean.setTransID(airportDetails.getTransID());
	    bean.setCustName(airportDetails.getCustName());
	    return  bean;
	  }
	//Methods Added by SonaliRaina to add boarding pass details 
	//in main transaction object during viewing transaction
	private CMSAirportDetails[] fromArmBoardingPassBeanToObject (BaseOracleBean[] beans) {
		  CMSAirportDetails[] array = new CMSAirportDetails[beans.length];
		    for (int i = 0; i < array.length; i++)
		      array[i] = fromArmBoardingPassBeanToObject(beans[i]);
		    return  array;
		  }
	private CMSAirportDetails fromArmBoardingPassBeanToObject(BaseOracleBean baseBean) {
		  ArmBoardingPassOracleBean bean = (ArmBoardingPassOracleBean)baseBean;
		  CMSAirportDetails object = new CMSAirportDetails();
		    object.setDestination(bean.getDestination());
		    object.setCheckIn(bean.getCheckInNo());
		    object.setComp(bean.getComp());
		    object.setCustName(bean.getCustName());
		    object.setEntryType(bean.getEntry());
		    object.setFlightNo(bean.getFlightNo());
		    object.setSeatNo(bean.getSeatNo());
		    return  object;
		  }
	public CMSAirportDetails[] getBoardingPassObject(String id) throws SQLException
	{
		String whereSql = where(ArmBoardingPassOracleBean.COL_TRANSACTION_ID);
	    return fromArmBoardingPassBeanToObject(query(new ArmBoardingPassOracleBean(), whereSql, id));
		
		
		}

}
