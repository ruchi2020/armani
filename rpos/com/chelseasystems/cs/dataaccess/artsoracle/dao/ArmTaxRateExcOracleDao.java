package  com.chelseasystems.cs.dataaccess.artsoracle.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.chelseasystems.cs.config.ArmTaxRateConfig;
import com.chelseasystems.cs.dataaccess.ArmTaxRateExcDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmTaxRateExcOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmTaxRateOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;


public class ArmTaxRateExcOracleDao extends BaseOracleDAO
implements ArmTaxRateExcDAO{



	  /**
	   * put your documentation comment here
	   * @param sCountry
	   * @param sLanguage
	   * @return 
	   * @exception SQLException
	   */
	  public ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcode(String sState, String sZipcode) throws SQLException {
		 String sSelectSQL = ArmTaxRateExcOracleBean.selectSql + where(ArmTaxRateExcOracleBean.COL_STATE, ArmTaxRateExcOracleBean.COL_ZIP_CODE);
		//String sSelectSQL = ArmTaxRateExcOracleBean.selectSql + where(ArmTaxRateExcOracleBean.COL_STATE);
	    List params = new ArrayList();
	    params.add(sState);
	    params.add(sZipcode);
	    ArmTaxRateConfig[] exceptionTaxDetailByStateAndZipcode = fromBeansToObjects(query(new ArmTaxRateExcOracleBean(), sSelectSQL, params));
	    System.out.println("query executed."  +params);
	  	    
	        if (exceptionTaxDetailByStateAndZipcode == null || exceptionTaxDetailByStateAndZipcode.length == 0)
	        	return null;
	        
	    return  exceptionTaxDetailByStateAndZipcode;
	  }

	  /**Added by Anjana for Send Sale tax calculation
	   * put your documentation comment here
	   * @param zip
	   * @param city
	   * @param state
	   * @return 
	   * @exception SQLException
	   * 
	   * Dec 29, 2016 vishal_k:Added product number as argument for fetching record from ARM_TAX_RATE_EXC with combination of state, zip, catagory,  and product .
	   * because of getting exact tax rate of any item with these combination.
	   * Vishal Yevale : modified method : added process_dt to get validated data
	   */
	  
	  public ArmTaxRateConfig selectByZipCityStateForSAP (String state, String zip, String category, String product, java.sql.Date process_dt) throws SQLException {
	    try {
	      List params = new ArrayList();
	      boolean flag = false;
	      String whereStr = "where " + ArmTaxRateExcOracleBean.COL_ZIP_CODE + " = ? and " + "UPPER(" + ArmTaxRateExcOracleBean.COL_STATE + ") = ? and " +  ArmTaxRateExcOracleBean.COL_CATEGORY + " = ? and " + ArmTaxRateExcOracleBean.COL_PRODUCT + " = ? ";
	      whereStr = whereStr + " AND ("+ ArmTaxRateExcOracleBean.COL_EXPIRATION_DT+ " IS NULL  OR "+ArmTaxRateExcOracleBean.COL_EXPIRATION_DT + ">= TO_DATE('"+process_dt+"','YYYY-MM-DD') )";
	      params.add(zip);
	     params.add(state.toUpperCase());
	     params.add(category);
	     params.add(product);
	     ArmTaxRateExcOracleBean taxBean = null;
	      ArmTaxRateExcOracleBean initTaxBean = null;
	      BaseOracleBean[] beans = this.query(new ArmTaxRateExcOracleBean(), whereStr + " order by " + ArmTaxRateExcOracleBean.COL_EFFECTIVE_DT + " desc ", params);
	      Date today = Calendar.getInstance().getTime();
	      for (int i = 0; i < beans.length; i++) {
	        taxBean = (ArmTaxRateExcOracleBean)beans[i];
	        Date eftDate = taxBean.getEffectiveDt();
	        if (initTaxBean == null)
	            initTaxBean = (ArmTaxRateExcOracleBean)beans[i];
	           return  fromBeanToObject(taxBean);
	   	 }
	      if (initTaxBean != null)
	        return  fromBeanToObject(initTaxBean);
	    } catch (Exception ex) {
	      System.out.println("Exception in ArmTaxRateConfig selectByZipCityStateForSAP" + ex.getMessage());
	      ex.printStackTrace();
	      return  null;
	    }
	    return  null;
	  }

	  /**Added by Anjana for Send Sale tax calculation
	   * put your documentation comment here
	   * @param zip
	   * @param city
	   * @param state
	   * @return 
	   * @exception SQLException
	   */
	  public ArmTaxRateConfig selectByZipCityStateForSAP (String state, String zip, String category) throws SQLException {
	    try {
	      List params = new ArrayList();
	      boolean flag = false;
	      String whereStr = "where " + ArmTaxRateExcOracleBean.COL_ZIP_CODE + " = ? and " + "UPPER(" + ArmTaxRateExcOracleBean.COL_STATE + ") = ? and " +  ArmTaxRateExcOracleBean.COL_CATEGORY + " = ? ";
	     params.add(zip);
	     params.add(state.toUpperCase());
	     params.add(category);
	     ArmTaxRateExcOracleBean taxBean = null;
	      ArmTaxRateExcOracleBean initTaxBean = null;
	      BaseOracleBean[] beans = this.query(new ArmTaxRateExcOracleBean(), whereStr + " order by " + ArmTaxRateExcOracleBean.COL_EFFECTIVE_DT + " desc ", params);
	      Date today = Calendar.getInstance().getTime();
	      for (int i = 0; i < beans.length; i++) {
	        taxBean = (ArmTaxRateExcOracleBean)beans[i];
	        Date eftDate = taxBean.getEffectiveDt();
	        if (initTaxBean == null)
	            initTaxBean = (ArmTaxRateExcOracleBean)beans[i];
	           return  fromBeanToObject(taxBean);
	   	      
	      }
	      if (initTaxBean != null)
	        return  fromBeanToObject(initTaxBean);
	    } catch (Exception ex) {
	      System.out.println("Exception in ArmTaxRateConfig selectByZipCityStateForSAP" + ex.getMessage());
	      ex.printStackTrace();
	      return  null;
	    }
	    return  null;
	  }

	  
	  
	  public ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcodeCategory(String sState, String sZipcode, String category) throws SQLException {
			 String sSelectSQL = ArmTaxRateExcOracleBean.selectSql + where(ArmTaxRateExcOracleBean.COL_STATE, ArmTaxRateExcOracleBean.COL_ZIP_CODE, ArmTaxRateExcOracleBean.COL_CATEGORY );
			//String sSelectSQL = ArmTaxRateExcOracleBean.selectSql + where(ArmTaxRateExcOracleBean.COL_STATE);
		    List params = new ArrayList();
		    params.add(sState);
		    params.add(sZipcode);
		    params.add(category);
		    ArmTaxRateConfig[] exceptionTaxDetailByStateAndZipcode = fromBeansToObjects(query(new ArmTaxRateExcOracleBean(), sSelectSQL, params));
		    System.out.println("query executed."  +params);
		    if (exceptionTaxDetailByStateAndZipcode == null || exceptionTaxDetailByStateAndZipcode.length == 0)
			      return null;
		 
		    return  exceptionTaxDetailByStateAndZipcode;
		  }
		  
	  /**
	   * put your documentation comment here
	   * @return 
	   */
	  protected BaseOracleBean getDatabeanInstance () {
	    return  new ArmTaxRateExcOracleBean();
	  }

	  /**
	   * put your documentation comment here
	   * @param beans
	   * @return 
	   */
	  private ArmTaxRateConfig[] fromBeansToObjects (BaseOracleBean[] beans) {
		  ArmTaxRateConfig[] array = new ArmTaxRateConfig[beans.length];
	    for (int i = 0; i < array.length; i++)
	      array[i] = fromBeanToObject(beans[i]);
	    return  array;
	  }

	  /**
	   * put your documentation comment here
	   * @param baseBean
	   * @return 
	   */
	  private ArmTaxRateConfig fromBeanToObject (BaseOracleBean baseBean) {
		ArmTaxRateExcOracleBean bean = (ArmTaxRateExcOracleBean)baseBean;
		ArmTaxRateConfig object = new ArmTaxRateConfig();
		
		object.setIdexc(bean.getIdExc());
		object.setState(bean.getState());  
		object.setAmountThr(bean.getAmountThr());  
		object.setZipCode(bean.getZipCode());
		 object.setTaxJur(bean.getTaxJur());
		 object.setTaxRate(bean.getTaxRate());
	     object.setThrRule(bean.getThrRule());
		 object.setEffectiveDt(bean.getEffectiveDt());
		object.setIdRate(bean.getIdRate());
		object.setNation(bean.getNation());
		 object.setBrand(bean.getBrand());
		object.setCategory(bean.getCategory());
		object.setProduct(bean.getProduct());
		object.setDatains(bean.getDataIns());
		object.setDataMod(bean.getDataMod());
		object.setTaxType(bean.getTaxType());
		
		//Poonam S.: Added for expiration date issue on Nov 15,2016 
		object.setExpirationDt(bean.getExpiration_dt());
		//ends here
		return  object;
	  }

}
