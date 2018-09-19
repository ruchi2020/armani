/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.armaniinterfaces.ArmStgEmployeeData;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cr.database.ParametricStatement;


/**
 * put your documentation comment here
 */
public class ArmStgEmployeeOracleDAO extends BaseOracleDAO
    implements ArmStgEmployeeDAO {
  private static String selectSql = ArmStgEmployeeOracleBean.selectSql;
  private static String insertSql = ArmStgEmployeeOracleBean.insertSql;

  /**
   * put your documentation comment here
   * @return 
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmStgEmployeeOracleBean();
  }

  /**
   * put your documentation comment here
   * @param armStgEmpData
   * @exception SQLException
   */
  public void insert (ArmStgEmployeeData armStgEmpData) throws SQLException {
    throw  new SQLException("Insert Operation Not Allowed On this Object");
  }

  /**
   * put your documentation comment here
   * @param armStgEmpData
   * @exception SQLException
   */
  public void update (ArmStgEmployeeData armStgEmpData) throws SQLException {
    ParametricStatement[] ps = getUpdateSql(armStgEmpData);
    execute(ps);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  public ParametricStatement[] getUpdateSql (ArmStgEmployeeData object) {
    List statements = new ArrayList();
    ArmStgEmployeeOracleBean armRMSEmployeeDataOracleBean = fromObjectToBean(object);
    List params = new ArrayList();
    params.add(object.getStgStatus());
    params.add(object.getStgErrorMessage());
    params.add(object.getStgEventId());
    statements.add(new ParametricStatement(armRMSEmployeeDataOracleBean.getUpdateSql(), params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   */
  private ArmStgEmployeeData[] fromBeansToObjects (BaseOracleBean[] beans) {
    int iCtr = 0;
    ArmStgEmployeeData[] arrayArmStgEmployeeData = new ArmStgEmployeeData[beans.length];
    for (iCtr = 0; iCtr < arrayArmStgEmployeeData.length; iCtr++)
      arrayArmStgEmployeeData[iCtr] = fromBeanToObject(beans[iCtr]);
    return  arrayArmStgEmployeeData;
  }

  /**
   * put your documentation comment here
   * @param baseOraBean
   * @return 
   */
  private ArmStgEmployeeData fromBeanToObject (BaseOracleBean baseOraBean) {
    ArmStgEmployeeOracleBean armStgEmpOraBean = (ArmStgEmployeeOracleBean)baseOraBean;
    ArmStgEmployeeData armStgEmpData = new ArmStgEmployeeData();
    armStgEmpData.setTranType(armStgEmpOraBean.getTranType());
    armStgEmpData.setEmployeeID(armStgEmpOraBean.getId());
    armStgEmpData.setFirstName(armStgEmpOraBean.getFirstName());
    armStgEmpData.setLastName(armStgEmpOraBean.getLastName());
    armStgEmpData.setDateOfBirth(armStgEmpOraBean.getDateOfBirth());
    armStgEmpData.setEmail(armStgEmpOraBean.getEmail());
    armStgEmpData.setHomePhoneArea(armStgEmpOraBean.getHomePhoneArea());
    armStgEmpData.setHomePhoneNumber(armStgEmpOraBean.getHomePhoneNumber());
    armStgEmpData.setAddressLine1(armStgEmpOraBean.getAddressLine1());
    armStgEmpData.setAddressLine2(armStgEmpOraBean.getAddressLine2());
    armStgEmpData.setCity(armStgEmpOraBean.getCity());
    armStgEmpData.setState(armStgEmpOraBean.getState());
    armStgEmpData.setPostalCode(armStgEmpOraBean.getPostalCode());
    armStgEmpData.setCountry(armStgEmpOraBean.getCountry());
    armStgEmpData.setEmployeeStatus(armStgEmpOraBean.getStatus());
    armStgEmpData.setHomeStoreId(armStgEmpOraBean.getHomeStoreId());
    armStgEmpData.setHireDate(armStgEmpOraBean.getHireDate());
    armStgEmpData.setTermDate(armStgEmpOraBean.getTermDate());
    armStgEmpData.setRole(armStgEmpOraBean.getRole());
    armStgEmpData.setStgEventId(armStgEmpOraBean.getStgEventId());
    armStgEmpData.setStgStatus(armStgEmpOraBean.getStgStatus());
    armStgEmpData.setStgErrorMessage(armStgEmpOraBean.getStgErrorMessage());
    armStgEmpData.setStgLoadDate(armStgEmpOraBean.getStgLoadDate());
    armStgEmpData.setStgProcessDate(armStgEmpOraBean.getStgProcessDate());
    return  armStgEmpData;
  }

  /**
   * put your documentation comment here
   * @param armStgEmpData
   * @return 
   */
  private ArmStgEmployeeOracleBean fromObjectToBean (ArmStgEmployeeData armStgEmpData) {
    ArmStgEmployeeOracleBean armStgEmpOraBean = new ArmStgEmployeeOracleBean();
    armStgEmpOraBean.setTranType(armStgEmpData.getTranType());
    armStgEmpOraBean.setId(armStgEmpData.getEmployeeID());
    armStgEmpOraBean.setFirstName(armStgEmpData.getFirstName());
    armStgEmpOraBean.setLastName(armStgEmpData.getLastName());
    armStgEmpOraBean.setDateOfBirth(armStgEmpData.getDateOfBirth());
    armStgEmpOraBean.setEmail(armStgEmpData.getEmail());
    armStgEmpOraBean.setHomePhoneArea(armStgEmpData.getHomePhoneArea());
    armStgEmpOraBean.setHomePhoneNumber(armStgEmpData.getHomePhoneNumber());
    armStgEmpOraBean.setAddressLine1(armStgEmpData.getAddressLine1());
    armStgEmpOraBean.setAddressLine2(armStgEmpData.getAddressLine2());
    armStgEmpOraBean.setCity(armStgEmpData.getCity());
    armStgEmpOraBean.setState(armStgEmpData.getState());
    armStgEmpOraBean.setPostalCode(armStgEmpData.getPostalCode());
    armStgEmpOraBean.setCountry(armStgEmpData.getCountry());
    armStgEmpOraBean.setStatus(armStgEmpData.getEmployeeStatus());
    armStgEmpOraBean.setHomeStoreId(armStgEmpData.getHomeStoreId());
    armStgEmpOraBean.setHireDate(armStgEmpData.getHireDate());
    armStgEmpOraBean.setTermDate(armStgEmpData.getTermDate());
    armStgEmpOraBean.setRole(armStgEmpData.getRole());
    armStgEmpOraBean.setStgEventId(armStgEmpData.getStgEventId());
    armStgEmpOraBean.setStgStatus(armStgEmpData.getStgStatus());
    armStgEmpOraBean.setStgErrorMessage(armStgEmpData.getStgErrorMessage());
    armStgEmpOraBean.setStgLoadDate(armStgEmpData.getStgLoadDate());
    armStgEmpOraBean.setStgProcessDate(armStgEmpData.getStgProcessDate());
    return  armStgEmpOraBean;
  }

  /**
   * put your documentation comment here
   * @return 
   */
  public ArmStgEmployeeData[] getNewArmStgEmpData () {
    ArmStgEmployeeData arrayArmStgEmpData[];
    // Lorenzo added STG_PROCESS_DATE
    String sWhereSQL = "where NVL(ARM_STG_EMPLOYEE.STG_STATUS, 0) IN (0, 2) and STG_PROCESS_DATE is null";
    try {
      arrayArmStgEmpData = fromBeansToObjects(this.query(new ArmStgEmployeeOracleBean(), sWhereSQL, null));
      return  arrayArmStgEmpData;
    } catch (SQLException sQex) {
      sQex.printStackTrace();
      return  new ArmStgEmployeeData[0];
    }
  }
}



