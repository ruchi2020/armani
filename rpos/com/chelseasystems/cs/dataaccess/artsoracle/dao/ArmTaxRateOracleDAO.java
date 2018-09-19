/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 06-21-2005 | Rajesh    | N/A       | Add new field TAX_JUR                        |
 --------------------------------------------------------------------------------------------
 | 1    | 02-17-2005 | Anand     | N/A       | Modified to add method to facilitate the     |
 |      |            |           |           | select location from table based on ZIP.     |
 --------------------------------------------------------------------------------------------
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.tax.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cr.database.*;


/**
 * put your documentation comment here
 */
public class ArmTaxRateOracleDAO extends BaseOracleDAO
    implements ArmTaxRateDAO {
  private static String selectSql = ArmTaxRateOracleBean.selectSql;
  private static String insertSql = ArmTaxRateOracleBean.insertSql;

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (ArmTaxRate object) throws SQLException {
    execute(getInsertSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (ArmTaxRate object) throws SQLException {
    List statements = new ArrayList();
    ArmTaxRateOracleBean armTaxRateBean = this.fromObjectToBean(object);
    statements.add(new ParametricStatement(armTaxRateBean.getInsertSql(), armTaxRateBean.toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param zip
   * @param city
   * @param state
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate selectByZipCityState (String zip, String city, String state) throws SQLException {
    try {
      List params = new ArrayList();
      boolean flag = false;
      String whereStr = "where " + ArmTaxRateOracleBean.COL_ZIP_CODE + " = ? and " + "UPPER(" + ArmTaxRateOracleBean.COL_STATE + ") = ? ";
      params.add(zip);
      params.add(state.toUpperCase());
      ArmTaxRateOracleBean taxBean = null;
      ArmTaxRateOracleBean initTaxBean = null;
      BaseOracleBean[] beans = this.query(new ArmTaxRateOracleBean(), whereStr + " order by " + ArmTaxRateOracleBean.COL_EFFECTIVE_DT + " desc ", params);
      Date today = Calendar.getInstance().getTime();
      for (int i = 0; i < beans.length; i++) {
        taxBean = (ArmTaxRateOracleBean)beans[i];
        Date eftDate = taxBean.getEffectiveDt();
        if ((eftDate.compareTo(today) == 0) || (eftDate.before(today))) {
          if (initTaxBean == null)
            initTaxBean = (ArmTaxRateOracleBean)beans[i];
          if (beans.length > 1) {
            if ((city != null && city.length() > 1)) {
              if (city.equalsIgnoreCase(taxBean.getCity()))
                return  fromBeanToObject(taxBean); 
              else 
                continue;
            }
          } 
          else {
            return  fromBeanToObject(taxBean);
          }
        }
      }
      if (initTaxBean != null)
        return  fromBeanToObject(initTaxBean);
    } catch (Exception ex) {
      System.out.println("Exception in ArmTaxRate selectByZipCityState" + ex.getMessage());
      ex.printStackTrace();
      return  null;
    }
    return  null;
  }

  /**
   * put your documentation comment here
   * @param zip
   * @param city
   * @param state
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate selectByZipCityState (String zip, String city, String state,java.sql.Date process_dt) throws SQLException {
    try {
      List params = new ArrayList();
      boolean flag = false;
      String whereStr = "WHERE " + ArmTaxRateOracleBean.COL_ZIP_CODE + " = ? AND " + "UPPER(" + ArmTaxRateOracleBean.COL_STATE + ") = ? ";
      whereStr = whereStr + " AND ("+ ArmTaxRateOracleBean.COL_EXPIRATION_DT+ " IS NULL  OR "+ArmTaxRateOracleBean.COL_EXPIRATION_DT + ">= TO_DATE('"+process_dt+"','YYYY-MM-DD')  )";
      params.add(zip);
      params.add(state.toUpperCase());
      //params.add(process_dt);
      ArmTaxRateOracleBean taxBean = null;
      ArmTaxRateOracleBean initTaxBean = null;
      BaseOracleBean[] beans = this.query(new ArmTaxRateOracleBean(), whereStr + " order by " + ArmTaxRateOracleBean.COL_EFFECTIVE_DT + " desc ", params);
      Date today = Calendar.getInstance().getTime();
      for (int i = 0; i < beans.length; i++) {
        taxBean = (ArmTaxRateOracleBean)beans[i];
        Date eftDate = taxBean.getEffectiveDt();
        //vishal Yevale : 9 jan 2017 : 
        //Date expDate = taxBean.getExpiration_dt();
        if ((eftDate.compareTo(today) == 0) || (eftDate.before(today))/* && (expDate == null ||(expDate.compareTo(process_dt) == 0) || (process_dt.before(expDate)) )*/) {
          if (initTaxBean == null)
            initTaxBean = (ArmTaxRateOracleBean)beans[i];
          if (beans.length > 1) {
            if ((city != null && city.length() > 1)) {
              if (city.equalsIgnoreCase(taxBean.getCity()))
                return  fromBeanToObject(taxBean); 
              else 
                continue;
            }
          } 
          else {
            return  fromBeanToObject(taxBean);
          }
        }
      }
      if (initTaxBean != null)
        return  fromBeanToObject(initTaxBean);
    } catch (Exception ex) {
      System.out.println("Exception in ArmTaxRate selectByZipCityState" + ex.getMessage());
      ex.printStackTrace();
      return  null;
    }
    return  null;
  }

  /**
   * put your documentation comment here
   * @param zipCode
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate selectByZipCode (String zipCode) throws SQLException {
    BaseOracleBean[] bean = null;
    BaseOracleBean[] beans = null;
    ArrayList queryParams = new ArrayList();
    queryParams.add(zipCode);
    bean = query(new ArmTaxRateOracleBean(), where(ArmTaxRateOracleBean.COL_ZIP_CODE), queryParams);
    beans = new BaseOracleBean[bean.length];
    if (bean != null && bean.length > 0) {
      Calendar now = Calendar.getInstance();
      Date nowDate = now.getTime();
      int count = 0;
      //get all the effective dates in the table before today
      for (int i = 0; i < bean.length; i++) {
        if (((ArmTaxRateOracleBean)bean[i]).getEffectiveDt().before(nowDate)) {
          System.out.println("Effective Date is " + ((ArmTaxRateOracleBean)bean[i]).getEffectiveDt() + "and nowDate is" + nowDate);
          beans[count] = bean[i];
          count++;
        }
      }
      Date[] date = new Date[beans.length];
      Date vDate = ((ArmTaxRateOracleBean)beans[0]).getEffectiveDt();
      ArmTaxRateOracleBean armTaxRateBean = (ArmTaxRateOracleBean)beans[0];
      // if there are mutilple effective dates before today for a particular zip, then get the most recent effective
      //date
      for (int i = 0; i < beans.length; i++) {
        ArmTaxRateOracleBean armTaxRateOracleBean = (ArmTaxRateOracleBean)beans[i];
        if (beans[i] != null)
          date[i] = ((ArmTaxRateOracleBean)beans[i]).getEffectiveDt();
        if (date[i] != null) {
          if (date[i].after(vDate)) {
            vDate = ((ArmTaxRateOracleBean)beans[i]).getEffectiveDt();
            armTaxRateBean = (ArmTaxRateOracleBean)beans[i];
          }
        }
      }
      return  fromBeanToObject(armTaxRateBean);
    } 
    else {
      ArmTaxRateOracleBean vBean = new ArmTaxRateOracleBean();
      vBean.setTaxRate(new Double("0.00").doubleValue());
      return  fromBeanToObject(vBean);
    }
  }

  /**
   * put your documentation comment here
   * @param city
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate[] selectByCity (String city) throws SQLException {
    BaseOracleBean[] beans = null;
    ArrayList queryParams = new ArrayList();
    queryParams.add(city);
    beans = query(new ArmTaxRateOracleBean(), where(ArmTaxRateOracleBean.COL_CITY), queryParams);
    return  fromBeansToObjects(beans);
  }

  /**
   * put your documentation comment here
   * @param county
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate[] selectByCounty (String county) throws SQLException {
    BaseOracleBean[] beans = null;
    ArrayList queryParams = new ArrayList();
    queryParams.add(county);
    beans = query(new ArmTaxRateOracleBean(), where(ArmTaxRateOracleBean.COL_COUNTY), queryParams);
    return  fromBeansToObjects(beans);
  }

  /**
   * put your documentation comment here
   * @param state
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate[] selectByState (String state) throws SQLException {
    BaseOracleBean[] beans = null;
    ArrayList queryParams = new ArrayList();
    queryParams.add(state);
    beans = query(new ArmTaxRateOracleBean(), where(ArmTaxRateOracleBean.COL_STATE), queryParams);
    return  fromBeansToObjects(beans);
  }

  /**
   * put your documentation comment here
   * @param taxRate
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate[] selectByTaxRate (Double taxRate) throws SQLException {
    BaseOracleBean[] beans = null;
    ArrayList queryParams = new ArrayList();
    queryParams.add(taxRate);
    beans = query(new ArmTaxRateOracleBean(), where(ArmTaxRateOracleBean.COL_TAX_RATE), queryParams);
    return  fromBeansToObjects(beans);
  }

  /**
   * put your documentation comment here
   * @param effectiveDt
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate[] selectByEffectiveDt (Date effectiveDt) throws SQLException {
    BaseOracleBean[] beans = null;
    ArrayList queryParams = new ArrayList();
    queryParams.add(effectiveDt);
    beans = query(new ArmTaxRateOracleBean(), where(ArmTaxRateOracleBean.COL_EFFECTIVE_DT), queryParams);
    return  fromBeansToObjects(beans);
  }

  //
  //Non public methods begin here!
  //
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmTaxRateOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   */
  private ArmTaxRate[] fromBeansToObjects (BaseOracleBean[] beans) {
    ArmTaxRate[] array = new ArmTaxRate[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  // Note: This method is used for obtaining zipCode, City & State only.
  private ArmTaxRate[] ToArmTaxRateObjs (BaseOracleBean[] beans) {
    ArmTaxRate[] array = new ArmTaxRate[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = toArmTaxRate(beans[i]);
    return  array;
  }

  // Note: This method is used for obtaining zipCode, City & State.
  private ArmTaxRate toArmTaxRate (BaseOracleBean baseBean) {
    ArmTaxRateZipCodeOracleBean bean = (ArmTaxRateZipCodeOracleBean)baseBean;
    ArmTaxRate object = new ArmTaxRate();
    object.doSetZipCode(bean.getZipCode());
    object.doSetCity(bean.getCity());
    object.doSetState(bean.getState());
    return  object;
  }

  /////this method needs to customized
  private ArmTaxRate fromBeanToObject (BaseOracleBean baseBean) {
    ArmTaxRateOracleBean bean = (ArmTaxRateOracleBean)baseBean;
    ArmTaxRate object = new ArmTaxRate();
    object.doSetZipCode(bean.getZipCode());
    object.doSetCity(bean.getCity());
    if (bean.getCounty() != null)
      object.doSetCounty(bean.getCounty());
    object.doSetState(bean.getState());
    if (bean.getTaxRate() != null)
      object.doSetTaxRate(bean.getTaxRate());
    if (bean.getEffectiveDt() != null)
      object.doSetEffectiveDt(bean.getEffectiveDt());
    if (bean.getTaxJur() != null)
      object.doSetTaxJurisdiction(bean.getTaxJur());
    if (bean.getNation() != null)
        object.setNation(bean.getNation());
    if (bean.getTax_type() != null)
        object.setTaxType(bean.getTax_type());
    
    //Poonam S: Added for expiration date issue on Nov 15,2016
    if (bean.getExpiration_dt() != null)
        object.setExpirationDt(bean.getExpiration_dt());
    //ends here
    return  object;
  }

  /////these method needs to customized
  private ArmTaxRateOracleBean fromObjectToBean (ArmTaxRate object) {
    ArmTaxRateOracleBean bean = new ArmTaxRateOracleBean();
    bean.setZipCode(object.getZipCode());
    bean.setCity(object.getCity());
    bean.setCounty(object.getCounty());
    bean.setState(object.getState());
    bean.setTaxRate(object.getTaxRate());
    bean.setEffectiveDt(object.getEffectiveDt());
    bean.setTaxJur(object.getTaxJurisdiction());
    bean.setNation(object.getNation());
    bean.setTax_type(object.getTaxType());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param zipCode
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate[] multipleSelectByZipCode (String zipCode) throws SQLException {
    BaseOracleBean[] beans = null;
    ArrayList queryParams = new ArrayList();
    ArmTaxRateZipCodeOracleBean armTaxBean = new ArmTaxRateZipCodeOracleBean();
    String sql = "Select distinct ZIP_CODE, CITY, STATE from ARM_TAX_RATE where ZIP_CODE = ?";
    queryParams.add(zipCode);
    beans = query(armTaxBean, sql, queryParams);
    return  ToArmTaxRateObjs(beans);
  }
  
  /**
   * put your documentation comment here
   * @param zip
   * @param city
   * @param state
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate selectByStateAndNullZip (String state) throws SQLException {
    try {
      List params = new ArrayList();
      boolean flag = false;
      String whereStr = "where "  + "UPPER(" + ArmTaxRateOracleBean.COL_STATE + ") = ? and " + ArmTaxRateOracleBean.COL_ZIP_CODE + " is null";
      params.add(state.toUpperCase());
      ArmTaxRateOracleBean taxBean = null;
     BaseOracleBean[] beans = this.query(new ArmTaxRateOracleBean(), whereStr + " order by " + ArmTaxRateOracleBean.COL_EFFECTIVE_DT + " desc ", params);
      Date today = Calendar.getInstance().getTime();
      for (int i = 0; i < beans.length; i++) {
        taxBean = (ArmTaxRateOracleBean)beans[i];
        Date eftDate = taxBean.getEffectiveDt();
        if ((eftDate.compareTo(today) == 0) || (eftDate.before(today))) {
      return  fromBeanToObject(taxBean);
       
        }
      }
    
    } catch (Exception ex) {
      System.out.println("Exception in ArmTaxRate selectByStateAndNullZip" + ex.getMessage());
      ex.printStackTrace();
      return  null;
    }
    return  null;
  }

  /**
Vishal Yevale :  9 jan 2017 :
   * @param zip
   * @param city
   * @param state
   * @return 
   * @exception SQLException
   */
  public ArmTaxRate selectByStateAndNullZip (String state, java.sql.Date process_dt) throws SQLException {
    try {
      List params = new ArrayList();
      boolean flag = false;
      String whereStr = "where "  + "UPPER(" + ArmTaxRateOracleBean.COL_STATE + ") = ? and " + ArmTaxRateOracleBean.COL_ZIP_CODE + " is null";
      whereStr = whereStr + " AND ("+ ArmTaxRateOracleBean.COL_EXPIRATION_DT+ " IS NULL  OR "+ArmTaxRateOracleBean.COL_EXPIRATION_DT + ">= TO_DATE('"+process_dt+"','YYYY-MM-DD')  )";
      params.add(state.toUpperCase());
      ArmTaxRateOracleBean taxBean = null;
     BaseOracleBean[] beans = this.query(new ArmTaxRateOracleBean(), whereStr + " order by " + ArmTaxRateOracleBean.COL_EFFECTIVE_DT + " desc ", params);
      Date today = Calendar.getInstance().getTime();
      for (int i = 0; i < beans.length; i++) {
        taxBean = (ArmTaxRateOracleBean)beans[i];
        Date eftDate = taxBean.getEffectiveDt();
        if ((eftDate.compareTo(today) == 0) || (eftDate.before(today))) {
      return  fromBeanToObject(taxBean);
       
        }
      }
    
    } catch (Exception ex) {
      System.out.println("Exception in ArmTaxRate selectByStateAndNullZip" + ex.getMessage());
      ex.printStackTrace();
      return  null;
    }
    return  null;
  }
  
  
  public ArmTaxRate[] selectTaxTypeAndRate (String zip, String nation, String state) throws SQLException {
  try{
	  List params = new ArrayList();
	    String whereStr = "where "  + "UPPER(" + ArmTaxRateOracleBean.COL_STATE + ") = ? and " + ArmTaxRateOracleBean.COL_ZIP_CODE + " = ? and " +
	    						ArmTaxRateOracleBean.COL_NATION + " = ?";
	      params.add(state.toUpperCase());
	      params.add(zip);
	      params.add(nation);
	      
	      ArmTaxRateOracleBean taxBean = null;
	     BaseOracleBean[] beans = this.query(new ArmTaxRateOracleBean(), whereStr + " order by " + ArmTaxRateOracleBean.COL_EFFECTIVE_DT + " desc ", params);
	      Date today = Calendar.getInstance().getTime();
	      for (int i = 0; i < beans.length; i++) {
	          taxBean = (ArmTaxRateOracleBean)beans[i];
	          beans = query(taxBean, whereStr, params);
	          return  fromBeansToObjects(beans);
	      }
	  
	      
	      } catch (Exception ex) {
	        System.out.println("Exception in ArmTaxRate selectTaxTypeAndRate" + ex.getMessage());
	        ex.printStackTrace();
	     
	      }
	      return  null;
}
 

public ArmTaxRate[] selectTaxTypeAndRate (String zip, String nation, String state, java.sql.Date process_dt) throws SQLException {
	  try{
		  List params = new ArrayList();
		    String whereStr = "where "  + "UPPER(" + ArmTaxRateOracleBean.COL_STATE + ") = ? and " + ArmTaxRateOracleBean.COL_ZIP_CODE + " = ? and " +
		    						ArmTaxRateOracleBean.COL_NATION + " = ?";
		      whereStr = whereStr + " AND ("+ ArmTaxRateOracleBean.COL_EXPIRATION_DT+ " IS NULL  OR "+ArmTaxRateOracleBean.COL_EXPIRATION_DT + ">= TO_DATE('"+process_dt+"','YYYY-MM-DD')  )";
		      params.add(state.toUpperCase());
		      params.add(zip);
		      params.add(nation);		      
		      ArmTaxRateOracleBean taxBean = null;
		     BaseOracleBean[] beans = this.query(new ArmTaxRateOracleBean(), whereStr + " order by " + ArmTaxRateOracleBean.COL_EFFECTIVE_DT + " desc ", params);
		      Date today = Calendar.getInstance().getTime();
		      for (int i = 0; i < beans.length; i++) {
		          taxBean = (ArmTaxRateOracleBean)beans[i];
		          beans = query(taxBean, whereStr, params);
		          return  fromBeansToObjects(beans);
		      }
		  
		      
		      } catch (Exception ex) {
		        System.out.println("Exception in ArmTaxRate selectTaxTypeAndRate" + ex.getMessage());
		        ex.printStackTrace();
		     
		      }
		      return  null;
	}
	 
	}

