/*
 * @copyright (c) 1998-2002 Retek Inc
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Megha      |           |  Added new method selectByStoreAndRegID
 |      |            |           |           |   selectByStoreID                               |
 |      |            |           |           |                                                 |
 |      |            |           |           |                                                 |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  java.sql.SQLException;
import  java.util.ArrayList;
import  java.util.List;
import  com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.register.Register;
import  com.chelseasystems.cr.store.Store;
import  com.chelseasystems.cs.dataaccess.RegisterDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.AsTlOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.register.CMSRegister;


/**
 *
 *  Register Data Access Object.<br>
 *  This object encapsulates all database access for Register.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Id</td><td>AS_TL</td><td>ID_RPSTY_TND</td></tr>
 *    <tr><td>StoreID</td><td>AS_TL</td><td>ID_STR_RT</td></tr>
 *    <tr><td>DrawerFund</td><td>AS_TL</td><td>CP_BLNC_DFLT_OPN</td></tr>
 *  </table>
 *
 *  @see Register
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.AsTlOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *
 */
public class RegisterOracleDAO extends BaseOracleDAO
    implements RegisterDAO {

  /**
   *
   * @param object
   * @exception SQLException
   */
  public void insert (Register object) throws SQLException {
    this.execute(this.getInsertSQL(object));
  }

  /**
   *
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (Register object) throws SQLException {
    List statements = new ArrayList();
    // till
    AsTlOracleBean asTlBean = this.toAsTlBean((CMSRegister)object);
    statements.add(new ParametricStatement(asTlBean.getInsertSql(), asTlBean.toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   *
   * @param object
   * @exception SQLException
   */
  public void update (Register object) throws SQLException {
    this.execute(this.getUpdateSQL(object));
  }

  /**
   *
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQL (Register object) throws SQLException {
    List statements = new ArrayList();
    // till
    AsTlOracleBean asTlBean = this.toAsTlBean((CMSRegister)object);
    List params = asTlBean.toList();
    params.add(object.getId());
    params.add(object.getStoreId());
    statements.add(new ParametricStatement(asTlBean.getUpdateSql() + where(AsTlOracleBean.COL_ID_RPSTY_TND, AsTlOracleBean.COL_ID_STR_RT), params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   *
   * @param storeId
   * @param defaultId
   * @return
   * @exception SQLException
   */
  public Register getNextRegister (String storeId, String defaultId) throws SQLException {
    Register[] registers = selectByStoreId(storeId);
    Register nextRegister = null;
    if (registers == null || registers.length == 0) {
      nextRegister = new CMSRegister(defaultId, storeId);
    } 
    else {
      long currentMaxId = 0L;
      for (int i = 0; i < registers.length; i++) {
        long id = new Long(registers[i].getId()).longValue();
        currentMaxId = Math.max(currentMaxId, id);
      }
      String nextId = "" + (currentMaxId + 1L);
      if (currentMaxId < 1000L) {
        nextRegister = new CMSRegister(nextId, storeId);
      } 
      else {
        nextRegister = new CMSRegister(defaultId, storeId);
      }
    }
    if (nextRegister != null) {
      Store store = new StoreOracleDAO().selectById(storeId);
      nextRegister.doSetDrawerFund(new ArmCurrency(store.getCurrencyType(), 0.0d));
      insert(nextRegister);
    }
    return  nextRegister;
  }

  /**
   *
   * @param StoreID & RegisterID
   * @return Register[]
   * @throws SQLException
   */
  public CMSRegister selectByStoreAndRegID (String storeid, String registerid) throws SQLException {
    CMSRegister object = null;
    List params = null;
    BaseOracleBean[] beans = null;
    // till
    params = new ArrayList();
    params.add(registerid);
    params.add(storeid);
    beans = this.query(new AsTlOracleBean(), where(AsTlOracleBean.COL_ID_RPSTY_TND, AsTlOracleBean.COL_ID_STR_RT), params);
    if (beans != null && beans.length > 0) {
      object = this.toRegisterObject((AsTlOracleBean)beans[0]);
    }
    return  object;
  }

  /**
   *
   * @param ids
   * @return
   * @exception SQLException
   */
  public CMSRegister[] selectByIds (String storeId, String[] ids) throws SQLException {
    ids = ArtsUtil.removeDupString(ids);
    List result = new ArrayList();
    for (int i = 0; i < ids.length; i++) {
      Register object = this.selectByStoreAndRegID(storeId, ids[i]);
      if (object != null) {
        result.add(object);
      }
    }
    return  (CMSRegister[])result.toArray(new CMSRegister[result.size()]);
  }

  /**
   * 
   * @param storeId
   * @return
   * @throws SQLException
   */
  public CMSRegister[] selectByStoreId (String storeId) throws SQLException {
    return  this.selectByIds(storeId, this.selectRegisterIdsByStoreId(storeId));
  }

  /**
   * 
   * @param storeId
   * @return
   * @throws SQLException
   */
  public String[] selectRegisterIdsByStoreId (String storeId) throws SQLException {
    String sql = "select " + AsTlOracleBean.COL_ID_RPSTY_TND + " from " + AsTlOracleBean.TABLE_NAME + " where " + AsTlOracleBean.COL_ID_STR_RT + " = ?";
    List params = new ArrayList();
    params.add(storeId);
    return  this.queryForIds(sql, params);
  }

  /**
   * @deprecated do not use.
   * @return
   * @exception SQLException
   */
  public Register[] selectAll () throws SQLException {
    return  new CMSRegister[0];
  }

  /**
   * Till
   * @param
   * @return bean
   */
  private AsTlOracleBean toAsTlBean (CMSRegister object) {
    AsTlOracleBean bean = new AsTlOracleBean();
    bean.setIdRpstyTnd(object.getId());
    bean.setIdStrRt(object.getStoreId());
    bean.setIdOpr(object.getOperatorID());
    bean.setDeRpstyTnd(object.getRegisterDesc());
    bean.setTyRpstyTnd(object.getRegisterType());
    bean.setCpBlncDfltOpn(object.getDrawerFund());
    //Rounding Type
    bean.setTyRnd(object.getRoundType());
    return  bean;
  }

  /**
   *
   * @param asTlBean
   * @return object
   * @exception SQLException
   */
  private CMSRegister toRegisterObject (AsTlOracleBean bean) throws SQLException {
    // Setting regID & Store ID
    CMSRegister object = new CMSRegister(bean.getIdRpstyTnd(), bean.getIdStrRt());
    // Reg Type & Desc
    object.doSetRegisterType(bean.getTyRpstyTnd());
    object.doSetRegisterDesc(bean.getDeRpstyTnd());
    // Drawer Fund
    object.doSetDrawerFund(bean.getCpBlncDfltOpn());
    // Operator ID
    object.doSetOperatorID(bean.getIdOpr());
    // Rounding Type
    object.doSetRoundType(bean.getTyRnd());
    return  object;
  }
}



