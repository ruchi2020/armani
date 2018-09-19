/*
 * @copyright (c) 1998-2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 5    | 03-03-2005 | Pankaja   | N/A       | Added missing columns                        |
 |      |            |           |           |                                              |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 02-15-2005 | Salil     | N/A       | Modified  per the specs                      |
 |      |            |           |           |                                              |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 01-25-2005 | Manpreet  | N/A       | Modified  to add check if AlternateTelephone |
 |      |            |           |           |                 and Fascimile exist          |
 |      |            |           |           |       -- Insert AddressLine2 in Store        |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 01-24-2005 | Manpreet  | N/A       | Modified to add BrandId                      |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cr.database.*;
import  com.chelseasystems.cr.currency.CurrencyType;
import  com.chelseasystems.cr.currency.UnsupportedCurrencyTypeException;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cs.store.*;
import  com.chelseasystems.cr.telephone.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cs.tax.*;

import  java.sql.*;
import  java.util.*;
import java.util.Date;


/**
 *
 */
/**
 *
 *  Store Data Access Object.<br>
 *  This object encapsulates all database access for Store.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Address</td><td>LO_ADS_NSTD</td><td>A1_ADS</td></tr>
 *    <tr><td>AsstManagerId</td><td>ST_ASGMT_EM_STR</td><td>ID_EM</td></tr>
 *    <tr><td>City</td><td>LO_ADS_NSTD</td><td>NM_UN</td></tr>
 *    <tr><td>ClosingPhysicalDate</td><td>PA_STR_RTL</td><td>DC_CL_RT_STR</td></tr>
 *    <tr><td>CompanyAccountNumberForFranking</td><td>PA_ORGN</td><td>CD_FRNK_CMY</td></tr>
 *    <tr><td>CompanyId</td><td>ST_ORGN_STRC</td><td>ID_PRTY_ORGN_PRNT</td></tr>
 *    <tr><td>CompanyNameForFranking</td><td>PA_ORGN</td><td>ON_FRNK_CMY</td></tr>
 *    <tr><td>Country</td><td>LO_ADS_NSTD</td><td>CO_NM</td></tr>
 *    <tr><td>CurrencyType</td><td>PA_STR_RTL</td><td>TY_CNY</td></tr>
 *    <tr><td>DefaultConsultantId</td><td>ST_ASGMT_EM_STR</td><td>ID_EM</td></tr>
 *    <tr><td>DefaultRegionalTaxRate</td><td>PA_STR_RTL</td><td>PE_TX_RGNL</td></tr>
 *    <tr><td>DefaultTaxRate</td><td>PA_STR_RTL</td><td>PE_TX</td></tr>
 *    <tr><td>GeoCode</td><td>LO_ADS_LCN_PHY</td><td>CD_LCN_PHY</td></tr>
 *    <tr><td>GroupIdentifier</td><td>PA_ORGN</td><td>ID_GRP_MSG</td></tr>
 *    <tr><td>ManagerId</td><td>ST_ASGMT_EM_STR</td><td>ID_EM</td></tr>
 *    <tr><td>Name</td><td>PA_ORGN</td><td>NM_ORGN</td></tr>
 *    <tr><td>OpeningPhysicalDate</td><td>PA_STR_RTL</td><td>DC_OPN_RT_STR</td></tr>
 *    <tr><td>Password</td><td>PA_STR_RTL</td><td>PW_ACS_STR</td></tr>
 *    <tr><td>PreferredISOCountry</td><td>PA_PRTY</td><td>ED_CO</td></tr>
 *    <tr><td>PreferredISOLanguage</td><td>PA_PRTY</td><td>ED_LA</td></tr>
 *    <tr><td>RegionalTaxLabel</td><td>PA_STR_RTL</td><td>DE_TX_RGNL</td></tr>
 *    <tr><td>State</td><td>LO_ADS_NSTD</td><td>TE_NM</td></tr>
 *    <tr><td>TaxLabel</td><td>PA_STR_RTL</td><td>DE_TX</td></tr>
 *    <tr><td>UsesRegionalTaxCalculationsFlag</td><td>PA_STR_RTL</td><td>FL_TX_RGNL</td></tr>
 *    <tr><td>ZipCode</td><td>LO_ADS_NSTD</td><td>PC_NM</td></tr>
 *    //Mayuri for RegistrationId
 *    <tr><td>RegistrationId</td><td>PA_STR_RTL</td><td>REGISTRATION_ID</td></tr>
 *  </table>
 *
 *  @see Store
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsLcnPhyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsNstdOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsPrtyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoCrdnTypOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoLcnPhyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoPhOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaOrgnOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrtyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaRoPrtyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaStrRtlOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.StAsgmtEmStrOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.StOrgnStrcOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.loLcnPhyOracleBean
 *
 */
public class StoreOracleDAO extends BaseOracleDAO
    implements StoreDAO {
  private static TelephoneOracleDAO telephoneDAO = new TelephoneOracleDAO();

  /**
   *
   * @param object
   * @exception SQLException
   */
  public void insert (Store object) throws SQLException {
    execute(getInsertSQL(object));
  }

  /**
   *
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (Store object) throws SQLException {
    // System.out.println("StoreOracleDAO:getInsertSQL ");
    List statements = new ArrayList();
    // party
    PaPrtyOracleBean paPrtyBean = this.toPaPrtyBean(object);
    statements.add(new ParametricStatement(paPrtyBean.getInsertSql(), paPrtyBean.toList()));
    // organization
    PaOrgnOracleBean paOrgnBean = this.toPaOrgnBean(object);
    statements.add(new ParametricStatement(paOrgnBean.getInsertSql(), paOrgnBean.toList()));
    // organizational structure
    StOrgnStrcOracleBean stOrgnStrcBean = this.toStOrgnStrcBean(object);
    statements.add(new ParametricStatement(stOrgnStrcBean.getInsertSql(), stOrgnStrcBean.toList()));
    // party role
    PaRoPrtyOracleBean paRoPrtyBean = this.toPaRoPrtyBean(object);
    statements.add(new ParametricStatement(paRoPrtyBean.getInsertSql(), paRoPrtyBean.toList()));
    // telephones
    statements.addAll(Arrays.asList(telephoneDAO.getInsertSql(object.getTelephone(), object.getId())));
    // If AlternateTelephone exists in store  --- Added by Manpreet S Bawa 01/25/2005.
    if (object.getAlternateTelephone() != null)
      statements.addAll(Arrays.asList(telephoneDAO.getInsertSql(object.getAlternateTelephone(), object.getId())));
    // If Facsimile exists in Stoe  --- Added by Manpreet S Bawa 01/25/2005.
    if (object.getFacsimile() != null)
      statements.addAll(Arrays.asList(telephoneDAO.getInsertSql(object.getFacsimile(), object.getId())));
    //physical location
    LoLcnPhyOracleBean loLcnPhyBean = this.toLoLcnPhyBean(object);
    statements.add(new ParametricStatement(loLcnPhyBean.getInsertSql(), loLcnPhyBean.toList()));
    // address
    LoAdsOracleBean loAdsBean = this.toLoAdsBean(object, getNextChelseaId());
    statements.add(new ParametricStatement(loAdsBean.getInsertSql(), loAdsBean.toList()));
    // party address
    LoAdsPrtyOracleBean loAdsPrtyBean = this.toLoAdsPrtyBean(object, loAdsBean.getIdAds(), ArtsConstants.ADDRESS_TYPE_STORE);                   // send store
    statements.add(new ParametricStatement(loAdsPrtyBean.getInsertSql(), loAdsPrtyBean.toList()));
    // non-standard address
    LoAdsNstdOracleBean loAdsNstdBean = this.toLoAdsNstdBean(object, loAdsBean.getIdAds(), ArtsConstants.ADDRESS_TYPE_STORE);
    statements.add(new ParametricStatement(loAdsNstdBean.getInsertSql(), loAdsNstdBean.toList()));
    // physical location address
    LoAdsLcnPhyOracleBean loAdsLcnPhyBean = this.toLoAdsLcnPhyBean(object, loAdsBean.getIdAds());
    statements.add(new ParametricStatement(loAdsLcnPhyBean.getInsertSql(), loAdsLcnPhyBean.toList()));
    // Salil V Gangal 2/15/05
    // added following statements
    // change begins
    // address
    LoAdsOracleBean loAdsBean2 = this.toLoAdsBean(object, this.getNextChelseaId());
    statements.add(new ParametricStatement(loAdsBean2.getInsertSql(), loAdsBean2.toList()));
    // party address
    LoAdsPrtyOracleBean loAdsPrtyBean2 = this.toLoAdsPrtyBean(object, loAdsBean2.getIdAds(), ArtsConstants.ADDRESS_TYPE_COMPANY);               // send compnay
    statements.add(new ParametricStatement(loAdsPrtyBean2.getInsertSql(), loAdsPrtyBean2.toList()));
    // non-standard address
    LoAdsNstdOracleBean loAdsNstdBean2 = this.toLoAdsNstdBean(object, loAdsBean2.getIdAds(), ArtsConstants.ADDRESS_TYPE_COMPANY);
    statements.add(new ParametricStatement(loAdsNstdBean2.getInsertSql(), loAdsNstdBean2.toList()));
    // physical location address
    LoAdsLcnPhyOracleBean loAdsLcnPhyBean2 = this.toLoAdsLcnPhyBean(object, loAdsBean2.getIdAds());
    statements.add(new ParametricStatement(loAdsLcnPhyBean2.getInsertSql(), loAdsLcnPhyBean2.toList()));
    // change ends
    // retail store
    PaStrRtlOracleBean paStrRtlBean = this.toPaStrRtlBean(object, paRoPrtyBean.getIdPrty(), paRoPrtyBean.getTyRoPrty());
    statements.add(new ParametricStatement(paStrRtlBean.getInsertSql(), paStrRtlBean.toList()));
    // employee retail store assignments
    StAsgmtEmStrOracleBean[] stEmAsgmtBeans = this.toStAsgmtEmStrBeans(object);
    for (int i = 0; i < stEmAsgmtBeans.length; i++)
      statements.add(new ParametricStatement(stEmAsgmtBeans[i].getInsertSql(), stEmAsgmtBeans[i].toList()));
        return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   *
   * @param object
   * @exception SQLException
   */
  public void update (Store object) throws SQLException {
    this.execute(this.getUpdateSQL(object));
  }

  /**
   *
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQL (Store object) throws SQLException {
    // System.out.println("StoreOracleDAO:getUpdateSQL ");
    List statements = new ArrayList();
    List params = null;
    BaseOracleBean[] beans = null;
    // organization
    PaOrgnOracleBean paOrgnBean = this.toPaOrgnBean(object);
    params = paOrgnBean.toList();
    params.add(object.getId());
    statements.add(new ParametricStatement(paOrgnBean.getUpdateSql() + where(PaOrgnOracleBean.COL_ID_PRTY_ORGN), params));
    // organizational structure
    StOrgnStrcOracleBean stOrgnStrcBean = this.toStOrgnStrcBean(object);
    params = stOrgnStrcBean.toList();
    params.add(object.getId());
    statements.add(new ParametricStatement(stOrgnStrcBean.getUpdateSql() + where(StOrgnStrcOracleBean.COL_ID_PRTY_ORGN_SUB), params));
    // telephone
    statements.addAll(Arrays.asList(telephoneDAO.getDeleteSql(object.getId())));
    statements.addAll(Arrays.asList(telephoneDAO.getInsertSql(object.getTelephone(), object.getId())));
    statements.addAll(Arrays.asList(telephoneDAO.getInsertSql(object.getAlternateTelephone(), object.getId())));
    // If Facsimile exists in Stoe  --- Added by Manpreet S Bawa 01/25/2005.
    if (object.getFacsimile() != null)
      statements.addAll(Arrays.asList(telephoneDAO.getInsertSql(object.getFacsimile(), object.getId())));
    // party address
    LoAdsPrtyOracleBean loAdsPrtyBean = new LoAdsPrtyOracleBean();
    ArrayList queryParams = new ArrayList();
    queryParams.add(object.getId());
    beans = this.query(loAdsPrtyBean, where(LoAdsPrtyOracleBean.COL_ID_PRTY), queryParams);
    if (beans != null) {
      for (int i = 0; i < beans.length; i++) {
        loAdsPrtyBean = (LoAdsPrtyOracleBean)beans[i];
        // non-standard address
        LoAdsNstdOracleBean loAdsNstdBean = this.toLoAdsNstdBean(object, loAdsPrtyBean.getIdAds(), loAdsPrtyBean.getTyAds());
        if (loAdsNstdBean != null) {
          params = loAdsNstdBean.toList();
          params.add(loAdsPrtyBean.getIdAds());
          statements.add(new ParametricStatement(loAdsNstdBean.getUpdateSql() + where(LoAdsNstdOracleBean.COL_ID_ADS), params));
        }
      }
    }
    // retail store
    PaStrRtlOracleBean paStrRtlBean = this.toPaStrRtlBean(object, object.getId(), ArtsConstants.PARTY_ROLE_TYPE_STORE);
    params = paStrRtlBean.toList();
    params.add(object.getId());
    statements.add(new ParametricStatement(paStrRtlBean.getUpdateSql() + where(PaStrRtlOracleBean.COL_ID_PRTY), params));
    // employee retail store assignments
    params = new ArrayList();
    params.add(object.getId());
    statements.add(new ParametricStatement(new StAsgmtEmStrOracleBean().getDeleteSql() + where(StAsgmtEmStrOracleBean.COL_ID_STR_RT), params));
    StAsgmtEmStrOracleBean[] stEmAsgmtBeans = this.toStAsgmtEmStrBeans(object);
    for (int i = 0; i < stEmAsgmtBeans.length; i++)
      statements.add(new ParametricStatement(stEmAsgmtBeans[i].getInsertSql(), stEmAsgmtBeans[i].toList()));
    // physical location address
    LoAdsLcnPhyOracleBean loAdsLcnPhyBean = this.toLoAdsLcnPhyBean(object, loAdsPrtyBean.getIdAds());
    params = loAdsLcnPhyBean.toList();
    params.add(loAdsPrtyBean.getIdAds());
    statements.add(new ParametricStatement(loAdsLcnPhyBean.getUpdateSql() + where(LoAdsLcnPhyOracleBean.COL_ID_ADS), params));
    // party
    PaPrtyOracleBean paPrtyBean = this.toPaPrtyBean(object);
    params = paPrtyBean.toList();
    params.add(object.getId());
    statements.add(new ParametricStatement(paPrtyBean.getUpdateSql() + where(PaPrtyOracleBean.COL_ID_PRTY), params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @return
   * @exception SQLException
   */
  public CMSStore[] selectAllStores () throws SQLException {
    CMSStore[] cmsStores = null;
    try {
      cmsStores = fromBeansToObjects(query(new PaStrRtlOracleBean(), "", null));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return  cmsStores;
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   * @exception SQLException
   */
  private CMSStore[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
    CMSStore[] array = new CMSStore[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = (CMSStore)fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return
   * @exception SQLException
   */
  private CMSStore fromBeanToObject (BaseOracleBean baseBean) throws SQLException {
    PaStrRtlOracleBean bean = (PaStrRtlOracleBean)baseBean;
    CMSStore object = new CMSStore(bean.getIdStrRt());
    object.setBrandID(bean.getIdBrand());
    object.setCompanyCode(bean.getIdCmy());
    object.setShopDescription(bean.getDeStrRt());
    object.setMessage(bean.getMessage());
   //Mayuri -- 
    object.setRegistrationId(bean.getRegistrationId());
    try {
      object.doSetCurrencyType(CurrencyType.getCurrencyType(bean.getTyCny()));
    }
    catch (Exception e)
      {}
      return object;
  }

  /**Poonam S.
   * Added for Expiry_DATE issue on Nov 22,2016 
   * @param id
   * @return
   * @exception SQLException
   */
  //@Override
  public Store selectByStoreId(String id,java.sql.Date process_dt) throws SQLException {
    CMSStore object = new CMSStore(id);
    List params = null;
    BaseOracleBean[] beans = null;
    // party
    params = new ArrayList();
    params.add(id);
    params.add(ArtsConstants.PARTY_ROLE_TYPE_STORE);
    beans = this.query(new PaPrtyOracleBean(), where(new String[] {
      PaPrtyOracleBean.COL_ID_PRTY, PaPrtyOracleBean.COL_TY_PRTY
    }), params);
    if (beans == null || beans.length == 0)
      return  null;
    PaPrtyOracleBean paPrtyBean = (PaPrtyOracleBean)beans[0];
    object.doSetPreferredISOCountry(paPrtyBean.getEdCo());
    object.doSetPreferredISOLanguage(paPrtyBean.getEdLa());
    // organization
    params.clear();
    params.add(id);
    beans = this.query(new PaOrgnOracleBean(), where(PaOrgnOracleBean.COL_ID_PRTY_ORGN), params);
    if (beans != null && beans.length > 0) {
      PaOrgnOracleBean paOrgnBean = (PaOrgnOracleBean)beans[0];
      object.doSetName(paOrgnBean.getNmOrgn());
      object.doSetGroupIdentifier(paOrgnBean.getIdGrpMsg());
      object.doSetCompanyAccountNumberForFranking(paOrgnBean.getCdFrnkCmy());
      object.doSetCompanyNameForFranking(paOrgnBean.getOnFrnkCmy());
    }
    // organizational structure
    params.clear();
    params.add(id);
    beans = this.query(new StOrgnStrcOracleBean(), where(StOrgnStrcOracleBean.COL_ID_PRTY_ORGN_SUB), params);
    if (beans != null && beans.length > 0) {
      StOrgnStrcOracleBean stOrgnStrcBean = (StOrgnStrcOracleBean)beans[0];
      object.doSetCompanyId(stOrgnStrcBean.getIdPrtyOrgnPrnt());
    }
    // retail store
    params.clear();
    params.add(id);
    params.add(ArtsConstants.PARTY_ROLE_TYPE_STORE);
    beans = this.query(new PaStrRtlOracleBean(), where(new String[] {
      PaStrRtlOracleBean.COL_ID_PRTY, PaStrRtlOracleBean.COL_TY_RO_PRTY
    }), params);
    
    if (beans != null && beans.length > 0) {
      PaStrRtlOracleBean paStrRtlBean = (PaStrRtlOracleBean)beans[0];
      object.doSetOpeningPhysicalDate(paStrRtlBean.getDcOpnRtStr());
      object.doSetClosingPhysicalDate(paStrRtlBean.getDcClRtStr());
      object.doSetDefaultTaxRate(paStrRtlBean.getPeTx());
      object.doSetDefaultRegionalTaxRate(paStrRtlBean.getPeTxRgnl());
      object.doSetTaxLabel(paStrRtlBean.getDeTx());
      object.doSetRegionalTaxLabel(paStrRtlBean.getDeTxRgnl());
      if (paStrRtlBean.getFlTxRgnl() != null)
        object.doSetUsesRegionalTaxCalculationsFlag(paStrRtlBean.getFlTxRgnl().booleanValue());
      object.doSetCompanyCode(paStrRtlBean.getIdCmy());
      object.doSetBrandID(paStrRtlBean.getIdBrand());
      object.doSetCompanyDescription(paStrRtlBean.getDeCmy());
      object.doSetShopDescription(paStrRtlBean.getDeStrRt());
      try {
        object.doSetCurrencyType(CurrencyType.getCurrencyType(paStrRtlBean.getTyCny()));
      } catch (UnsupportedCurrencyTypeException uscte) {
        throw  new SQLException("UnsupportedCurrencyTypeException: " + uscte.getMessage());
      }
      object.doSetPassword(paStrRtlBean.getPwAcsStr());
      object.setMessage(paStrRtlBean.getMessage());
    }
    // employee retail store assignment
    params.clear();
    params.add(id);
    beans = this.query(new StAsgmtEmStrOracleBean(), where(StAsgmtEmStrOracleBean.COL_ID_STR_RT), params);
    if (beans != null && beans.length > 0) {
      for (int i = 0; i < beans.length; i++) {
        StAsgmtEmStrOracleBean stAsgmtEmStrBean = (StAsgmtEmStrOracleBean)beans[i];
        String statusCode = stAsgmtEmStrBean.getScAsgmtStr();
        if (statusCode != null) {
          if (statusCode.equals(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_REGIONAL_MANAGER)) {
            object.doSetRegionalManagerId(stAsgmtEmStrBean.getIdEm());
          }
          else if (statusCode.equals(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_DISTRICT_MANAGER)) {
            object.doSetDistrictManagerId(stAsgmtEmStrBean.getIdEm());
          }
          else if (statusCode.equals(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_MANAGER)) {
            object.doSetManagerId(stAsgmtEmStrBean.getIdEm());
          }
          else if (statusCode.equals(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_ASST_MANAGER)) {
            object.doSetAsstManagerId(stAsgmtEmStrBean.getIdEm());
          }
          else if (statusCode.equals(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_DEFAULT_CONSULTANT)) {
            object.doSetDefaultConsultantId(stAsgmtEmStrBean.getIdEm());
          }
        }
      }
    }
    // telephone
    object.doSetTelephone(telephoneDAO.selectByPartyIdAndTelephoneType(object.getId(), TelephoneType.STORE));
    object.doSetAlternateTelephone(telephoneDAO.selectByPartyIdAndTelephoneType(object.getId(), TelephoneType.STORE_ALT));
    object.doSetFacsimile(telephoneDAO.selectByPartyIdAndTelephoneType(object.getId(), TelephoneType.STORE_FAX));
    // address
    params.clear();
    params.add(id);
    beans = this.query(new LoAdsPrtyOracleBean(), where(LoAdsPrtyOracleBean.COL_ID_PRTY), params);
    if (beans != null && beans.length > 0) {
      for (int i = 0; i < beans.length; i++) {
        LoAdsPrtyOracleBean loAdsPrtyBean = (LoAdsPrtyOracleBean)beans[i];
        String addressId = loAdsPrtyBean.getIdAds();
        params.clear();
        params.add(addressId);
        // non standard address
        BaseOracleBean[] loAdsNstdbeans = this.query(new LoAdsNstdOracleBean(), where(LoAdsNstdOracleBean.COL_ID_ADS), params);
        LoAdsNstdOracleBean loAdsNstdBean = (LoAdsNstdOracleBean)loAdsNstdbeans[0];
        if (loAdsPrtyBean.getTyAds().equals(ArtsConstants.ADDRESS_TYPE_STORE)) {
          object.doSetAddress(loAdsNstdBean.getA1Ads());
          object.doSetAddressLine2(loAdsNstdBean.getA2Ads());
          object.doSetState(loAdsNstdBean.getTeNm());
          object.doSetCountry(loAdsNstdBean.getCoNm());
          object.doSetZipCode(loAdsNstdBean.getPcNm());
          object.doSetCity(loAdsNstdBean.getNmUn());
          // physical location address
          BaseOracleBean[] loAdsLcnPhybeans = this.query(new LoAdsLcnPhyOracleBean(), where(LoAdsLcnPhyOracleBean.COL_ID_ADS), params);
          LoAdsLcnPhyOracleBean loAdsLcnPhyBean = (LoAdsLcnPhyOracleBean)loAdsLcnPhybeans[0];
          object.doSetGeoCode(loAdsLcnPhyBean.getCdLcnPhy());
        }
        else if (loAdsPrtyBean.getTyAds().equals(ArtsConstants.ADDRESS_TYPE_COMPANY)) {
          object.doSetCompAddressLine1(loAdsNstdBean.getA1Ads());
          object.doSetCompAddressLine2(loAdsNstdBean.getA2Ads());
          object.doSetCompState(loAdsNstdBean.getTeNm());
          object.doSetCompCountry(loAdsNstdBean.getCoNm());
          object.doSetCompPostalCode(loAdsNstdBean.getPcNm());
          object.doSetCompCity(loAdsNstdBean.getNmUn());
        }
        else
          continue;
      }
    }
    
    
    // Mayuri START-- retail store set Registration ID 
    params.clear();
    params.add(id);
    params.add(ArtsConstants.PARTY_ROLE_TYPE_STORE);
    beans = this.query(new PaStrRtlOracleBean(), where(new String[] {
      PaStrRtlOracleBean.COL_ID_PRTY, PaStrRtlOracleBean.COL_TY_RO_PRTY
    }), params);
    
    if (beans != null && beans.length > 0) {
      PaStrRtlOracleBean paStrRtlBean = (PaStrRtlOracleBean)beans[0];   
      if((object.getCountry().equalsIgnoreCase("CAN"))){
    	  object.doSetRegistrationId(paStrRtlBean.getRegistrationId());
      }
      
    }
    // Mayuri END 
    
    
    // check if there exits a entry for the Store zip/city/state in the tax rate table.
    ArmTaxRateDAO taxDAO = new ArmTaxRateOracleDAO();
    ArmTaxRate taxRate = taxDAO.selectByZipCityState(object.getZipCode(), object.getCity(), object.getState(),process_dt);
    ArmTaxRate stateTax = taxDAO.selectByStateAndNullZip(object.getState(),process_dt);
    ArmTaxRate[] taxRateandType = null;
    

    		
    if((object.getCountry().equalsIgnoreCase("CAN"))){
     taxRateandType =   taxDAO.selectTaxTypeAndRate(object.getZipCode(), object.getCountry(), object.getState(), process_dt);
    }
  
    String taxtype;
    Double gstTax = 0.0;
    Double gst_HstTax =  0.0;
    Double pstTax  = 0.0;
    Double qstTax  =  0.0;
    if((object.getCountry().equalsIgnoreCase("CAN"))){
    for(int i=0; i<taxRateandType.length;i++){
    	taxtype = taxRateandType[i].getTaxType();
    	if(taxtype.equalsIgnoreCase("GST")){
    		gstTax = taxRateandType[i].getTaxRate();
    	    object.doSetGstTAX(gstTax);
    	}
    	if(taxtype.equalsIgnoreCase("PST")){
    		pstTax = taxRateandType[i].getTaxRate();
    		 object.doSetPstTAX(pstTax);
    	}
    	if(taxtype.equalsIgnoreCase("QST")){
    		qstTax = taxRateandType[i].getTaxRate();
    		 object.doSetQstTAX(qstTax);
    	}
    	if(taxtype.contains("HST")){
    		gst_HstTax = taxRateandType[i].getTaxRate();
    		object.doSetGst_hstTAX(gst_HstTax);
    	}
    }
    }
    if (taxRate != null) {
	    		object.doSetDefaultTaxRate(taxRate.getTaxRate());
	    	
      object.doSetTaxJurisdiction(taxRate.getTaxJurisdiction());
      if((object.getState().equalsIgnoreCase("PR"))){
    	
     	if(stateTax!=null)
    	  object.doSetStateTax(stateTax.getTaxRate());
    	 
      }
    }
    	
	//ends here
    return  object;
  }
  //ends here
  
  /**
  *
  * @param id
  * @return
  * @exception SQLException
  */
 public Store selectById(String id) throws SQLException {
   CMSStore object = new CMSStore(id);
   List params = null;
   BaseOracleBean[] beans = null;
   // party
   params = new ArrayList();
   params.add(id);
   params.add(ArtsConstants.PARTY_ROLE_TYPE_STORE);
   beans = this.query(new PaPrtyOracleBean(), where(new String[] {
     PaPrtyOracleBean.COL_ID_PRTY, PaPrtyOracleBean.COL_TY_PRTY
   }), params);
   if (beans == null || beans.length == 0)
     return  null;
   PaPrtyOracleBean paPrtyBean = (PaPrtyOracleBean)beans[0];
   object.doSetPreferredISOCountry(paPrtyBean.getEdCo());
   object.doSetPreferredISOLanguage(paPrtyBean.getEdLa());
   // organization
   params.clear();
   params.add(id);
   beans = this.query(new PaOrgnOracleBean(), where(PaOrgnOracleBean.COL_ID_PRTY_ORGN), params);
   if (beans != null && beans.length > 0) {
     PaOrgnOracleBean paOrgnBean = (PaOrgnOracleBean)beans[0];
     object.doSetName(paOrgnBean.getNmOrgn());
     object.doSetGroupIdentifier(paOrgnBean.getIdGrpMsg());
     object.doSetCompanyAccountNumberForFranking(paOrgnBean.getCdFrnkCmy());
     object.doSetCompanyNameForFranking(paOrgnBean.getOnFrnkCmy());
   }
   // organizational structure
   params.clear();
   params.add(id);
   beans = this.query(new StOrgnStrcOracleBean(), where(StOrgnStrcOracleBean.COL_ID_PRTY_ORGN_SUB), params);
   if (beans != null && beans.length > 0) {
     StOrgnStrcOracleBean stOrgnStrcBean = (StOrgnStrcOracleBean)beans[0];
     object.doSetCompanyId(stOrgnStrcBean.getIdPrtyOrgnPrnt());
   }
   // retail store
   params.clear();
   params.add(id);
   params.add(ArtsConstants.PARTY_ROLE_TYPE_STORE);
   beans = this.query(new PaStrRtlOracleBean(), where(new String[] {
     PaStrRtlOracleBean.COL_ID_PRTY, PaStrRtlOracleBean.COL_TY_RO_PRTY
   }), params);
   
   if (beans != null && beans.length > 0) {
     PaStrRtlOracleBean paStrRtlBean = (PaStrRtlOracleBean)beans[0];
     //Mahesh Nandure: 12-1-2017: markdown markup cr by sergio for EUROPE 
     object.setStoreType(paStrRtlBean.getStoreType());
     object.setTimeZone(paStrRtlBean.getTimeZone());
     //ends Mahesh Nandure :12-1-2017
     object.doSetOpeningPhysicalDate(paStrRtlBean.getDcOpnRtStr());
     object.doSetClosingPhysicalDate(paStrRtlBean.getDcClRtStr());
     object.doSetDefaultTaxRate(paStrRtlBean.getPeTx());
     object.doSetDefaultRegionalTaxRate(paStrRtlBean.getPeTxRgnl());
     object.doSetTaxLabel(paStrRtlBean.getDeTx());
     object.doSetRegionalTaxLabel(paStrRtlBean.getDeTxRgnl());
     if (paStrRtlBean.getFlTxRgnl() != null)
       object.doSetUsesRegionalTaxCalculationsFlag(paStrRtlBean.getFlTxRgnl().booleanValue());
     object.doSetCompanyCode(paStrRtlBean.getIdCmy());
     object.doSetBrandID(paStrRtlBean.getIdBrand());
     object.doSetCompanyDescription(paStrRtlBean.getDeCmy());
     object.doSetShopDescription(paStrRtlBean.getDeStrRt());
     try {
       object.doSetCurrencyType(CurrencyType.getCurrencyType(paStrRtlBean.getTyCny()));
     } catch (UnsupportedCurrencyTypeException uscte) {
       throw  new SQLException("UnsupportedCurrencyTypeException: " + uscte.getMessage());
     }
     object.doSetPassword(paStrRtlBean.getPwAcsStr());
     object.setMessage(paStrRtlBean.getMessage());
   }
   // employee retail store assignment
   params.clear();
   params.add(id);
   beans = this.query(new StAsgmtEmStrOracleBean(), where(StAsgmtEmStrOracleBean.COL_ID_STR_RT), params);
   if (beans != null && beans.length > 0) {
     for (int i = 0; i < beans.length; i++) {
       StAsgmtEmStrOracleBean stAsgmtEmStrBean = (StAsgmtEmStrOracleBean)beans[i];
       String statusCode = stAsgmtEmStrBean.getScAsgmtStr();
       if (statusCode != null) {
         if (statusCode.equals(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_REGIONAL_MANAGER)) {
           object.doSetRegionalManagerId(stAsgmtEmStrBean.getIdEm());
         }
         else if (statusCode.equals(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_DISTRICT_MANAGER)) {
           object.doSetDistrictManagerId(stAsgmtEmStrBean.getIdEm());
         }
         else if (statusCode.equals(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_MANAGER)) {
           object.doSetManagerId(stAsgmtEmStrBean.getIdEm());
         }
         else if (statusCode.equals(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_ASST_MANAGER)) {
           object.doSetAsstManagerId(stAsgmtEmStrBean.getIdEm());
         }
         else if (statusCode.equals(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_DEFAULT_CONSULTANT)) {
           object.doSetDefaultConsultantId(stAsgmtEmStrBean.getIdEm());
         }
       }
     }
   }
   // telephone
   object.doSetTelephone(telephoneDAO.selectByPartyIdAndTelephoneType(object.getId(), TelephoneType.STORE));
   object.doSetAlternateTelephone(telephoneDAO.selectByPartyIdAndTelephoneType(object.getId(), TelephoneType.STORE_ALT));
   object.doSetFacsimile(telephoneDAO.selectByPartyIdAndTelephoneType(object.getId(), TelephoneType.STORE_FAX));
   // address
   params.clear();
   params.add(id);
   beans = this.query(new LoAdsPrtyOracleBean(), where(LoAdsPrtyOracleBean.COL_ID_PRTY), params);
   if (beans != null && beans.length > 0) {
     for (int i = 0; i < beans.length; i++) {
       LoAdsPrtyOracleBean loAdsPrtyBean = (LoAdsPrtyOracleBean)beans[i];
       String addressId = loAdsPrtyBean.getIdAds();
       params.clear();
       params.add(addressId);
       // non standard address
       BaseOracleBean[] loAdsNstdbeans = this.query(new LoAdsNstdOracleBean(), where(LoAdsNstdOracleBean.COL_ID_ADS), params);
       LoAdsNstdOracleBean loAdsNstdBean = (LoAdsNstdOracleBean)loAdsNstdbeans[0];
       if (loAdsPrtyBean.getTyAds().equals(ArtsConstants.ADDRESS_TYPE_STORE)) {
         object.doSetAddress(loAdsNstdBean.getA1Ads());
         object.doSetAddressLine2(loAdsNstdBean.getA2Ads());
         object.doSetState(loAdsNstdBean.getTeNm());
         object.doSetCountry(loAdsNstdBean.getCoNm());
         object.doSetZipCode(loAdsNstdBean.getPcNm());
         object.doSetCity(loAdsNstdBean.getNmUn());
         // physical location address
         BaseOracleBean[] loAdsLcnPhybeans = this.query(new LoAdsLcnPhyOracleBean(), where(LoAdsLcnPhyOracleBean.COL_ID_ADS), params);
         LoAdsLcnPhyOracleBean loAdsLcnPhyBean = (LoAdsLcnPhyOracleBean)loAdsLcnPhybeans[0];
         object.doSetGeoCode(loAdsLcnPhyBean.getCdLcnPhy());
       }
       else if (loAdsPrtyBean.getTyAds().equals(ArtsConstants.ADDRESS_TYPE_COMPANY)) {
         object.doSetCompAddressLine1(loAdsNstdBean.getA1Ads());
         object.doSetCompAddressLine2(loAdsNstdBean.getA2Ads());
         object.doSetCompState(loAdsNstdBean.getTeNm());
         object.doSetCompCountry(loAdsNstdBean.getCoNm());
         object.doSetCompPostalCode(loAdsNstdBean.getPcNm());
         object.doSetCompCity(loAdsNstdBean.getNmUn());
       }
       else
         continue;
     }
   }
   
   
   // Mayuri START-- retail store set Registration ID 
   params.clear();
   params.add(id);
   params.add(ArtsConstants.PARTY_ROLE_TYPE_STORE);
   beans = this.query(new PaStrRtlOracleBean(), where(new String[] {
     PaStrRtlOracleBean.COL_ID_PRTY, PaStrRtlOracleBean.COL_TY_RO_PRTY
   }), params);
   
   if (beans != null && beans.length > 0) {
     PaStrRtlOracleBean paStrRtlBean = (PaStrRtlOracleBean)beans[0];   
     if((object.getCountry().equalsIgnoreCase("CAN"))){
   	  object.doSetRegistrationId(paStrRtlBean.getRegistrationId());
     }
     
   }
   // Mayuri END 
   
   
   // check if there exits a entry for the Store zip/city/state in the tax rate table.
   ArmTaxRateDAO taxDAO = new ArmTaxRateOracleDAO();
   ArmTaxRate taxRate = taxDAO.selectByZipCityState(object.getZipCode(), object.getCity(), object.getState());
   ArmTaxRate stateTax = taxDAO.selectByStateAndNullZip(object.getState());
   ArmTaxRate[] taxRateandType = null;
   
   if((object.getCountry().equalsIgnoreCase("CAN"))){
    taxRateandType =   taxDAO.selectTaxTypeAndRate(object.getZipCode(), object.getCountry(), object.getState());
   }
 
   String taxtype;
   Double gstTax = 0.0;
   Double gst_HstTax =  0.0;
   Double pstTax  = 0.0;
   Double qstTax  =  0.0;
   if((object.getCountry().equalsIgnoreCase("CAN"))){
   for(int i=0; i<taxRateandType.length;i++){
   	taxtype = taxRateandType[i].getTaxType();
   	if(taxtype.equalsIgnoreCase("GST")){
   		gstTax = taxRateandType[i].getTaxRate();
   	    object.doSetGstTAX(gstTax);
   	}
   	if(taxtype.equalsIgnoreCase("PST")){
   		pstTax = taxRateandType[i].getTaxRate();
   		 object.doSetPstTAX(pstTax);
   	}
   	if(taxtype.equalsIgnoreCase("QST")){
   		qstTax = taxRateandType[i].getTaxRate();
   		 object.doSetQstTAX(qstTax);
   	}
   	if(taxtype.contains("HST")){
   		gst_HstTax = taxRateandType[i].getTaxRate();
   		object.doSetGst_hstTAX(gst_HstTax);
   	}
   }
   }
   if (taxRate != null) {
     object.doSetDefaultTaxRate(taxRate.getTaxRate());
     object.doSetTaxJurisdiction(taxRate.getTaxJurisdiction());
     if((object.getState().equalsIgnoreCase("PR"))){
   	
    	if(stateTax!=null)
   	  object.doSetStateTax(stateTax.getTaxRate());
   	 
     }
   }
   return  object;
 }

  /**
   *
   * @param ids
   * @return
   * @exception SQLException
   */
  public Store[] selectByIds (String[] ids) throws SQLException {
    ids = ArtsUtil.removeDupString(ids);
    List result = new ArrayList();
    for (int i = 0; i < ids.length; i++) {
      Store object = this.selectById(ids[i]);
      if (object != null)
        result.add(object);
    }
    // Commented by MSB -- Code was throwing ClassCastException
    // return (Store[])result.toArray(new Store[result.size()]);
    return  (Store[])result.toArray(new CMSStore[result.size()]);
  }

  /**
   *
   * @param city
   * @return
   * @exception SQLException
   */
  public Store[] selectByCity (String city) throws SQLException {
    String sql = "select PA_STR_RTL.ID_PRTY from PA_STR_RTL, LO_ADS_PRTY, LO_ADS_NSTD " + "where PA_STR_RTL.ID_PRTY = LO_ADS_PRTY.ID_PRTY " + "and LO_ADS_PRTY.ID_ADS = LO_ADS_NSTD.ID_ADS " + "and LO_ADS_NSTD.NM_UN = ?";                     // city
    List params = new ArrayList();
    params.add(city);
    return  this.selectByIds(this.queryForIds(sql, params));
  }

  /**
   *
   * @param state
   * @return
   * @exception SQLException
   */
  public Store[] selectByState (String state) throws SQLException {
    String sql = "select PA_STR_RTL.ID_PRTY from PA_STR_RTL, LO_ADS_PRTY, LO_ADS_NSTD " + "where PA_STR_RTL.ID_PRTY = LO_ADS_PRTY.ID_PRTY " + "and LO_ADS_PRTY.ID_ADS = LO_ADS_NSTD.ID_ADS " + "and LO_ADS_NSTD.TE_NM = ?";                     // state
    List params = new ArrayList();
    params.add(state);
    return  this.selectByIds(this.queryForIds(sql, params));
  }

  /**
   *
   * @param city
   * @param state
   * @return
   * @exception SQLException
   */
  public Store[] selectByCityAndState (String city, String state) throws SQLException {
    String sql = "select PA_STR_RTL.ID_PRTY from PA_STR_RTL, LO_ADS_PRTY, LO_ADS_NSTD " + "where PA_STR_RTL.ID_PRTY = LO_ADS_PRTY.ID_PRTY " + "and LO_ADS_PRTY.ID_ADS = LO_ADS_NSTD.ID_ADS " + "and LO_ADS_NSTD.NM_UN = ? " +                   // city
    "and LO_ADS_NSTD.TE_NM = ?";                // state
    List params = new ArrayList();
    params.add(city);
    params.add(state);
    return  this.selectByIds(this.queryForIds(sql, params));
  }

  /**
   *
   * @return
   * @exception SQLException
   */
  public Store[] selectAll () throws SQLException {
    String sql = "select PA_STR_RTL.ID_PRTY from PA_STR_RTL";
    List params = new ArrayList();
    return  this.selectByIds(this.queryForIds(sql, params));
  }

  /**
   * Party
   * @param object
   * @return
   */
  private PaPrtyOracleBean toPaPrtyBean (Store object) {
    PaPrtyOracleBean bean = new PaPrtyOracleBean();
    bean.setIdPrty(object.getId());
    bean.setTyPrty(ArtsConstants.PARTY_ROLE_TYPE_STORE);
    bean.setLuOrgLg(null);
    bean.setEdCo(object.getPreferredISOCountry());
    bean.setEdLa(object.getPreferredISOLanguage());
    return  bean;
  }

  /**
   * Organization
   * @param object
   * @return
   */
  private PaOrgnOracleBean toPaOrgnBean (Store object) {
    PaOrgnOracleBean bean = new PaOrgnOracleBean();
    bean.setIdPrtyOrgn(object.getId());
    //bean.setNmOrgn(object.getName());
    bean.setNmOrgn(((CMSStore)object).getShopDescription());
    bean.setIdTxOrgn(null);
    bean.setScOrgnLg(null);
    bean.setIdTxFdl(null);
    bean.setIdTxSt(null);
    bean.setIdGrpMsg(object.getGroupIdentifier());
    bean.setOnFrnkCmy(object.getCompanyNameForFranking());
    bean.setCdFrnkCmy(object.getCompanyAccountNumberForFranking());
    return  bean;
  }

  /**
   * OrganizationalStructure
   * @param object
   * @return
   */
  private StOrgnStrcOracleBean toStOrgnStrcBean (Store object) {
    StOrgnStrcOracleBean bean = new StOrgnStrcOracleBean();
    bean.setIdPrtyOrgnPrnt(object.getCompanyId());
    bean.setIdPrtyOrgnSub(object.getId());
    return  bean;
  }

  /**
   * Party Role
   * @param object
   * @return
   */
  private PaRoPrtyOracleBean toPaRoPrtyBean (Store object) {
    PaRoPrtyOracleBean bean = new PaRoPrtyOracleBean();
    bean.setIdPrty(object.getId());
    bean.setTyRoPrty(ArtsConstants.PARTY_ROLE_TYPE_STORE);
    bean.setDcRoPrtyEf(null);
    bean.setDcRoPrtyEp(null);
    return  bean;
  }

  /**
   * Address
   * @param object
   * @return
   * @exception SQLException
   */
  private LoAdsOracleBean toLoAdsBean (Store object, String addressId) {
    LoAdsOracleBean bean = new LoAdsOracleBean();
    bean.setIdAds(addressId);
    //bean.set
    return  bean;
  }

  /**
   * Non Standard Address
   * @param object
   * @param addressId
   * @return
   * @exception SQLException
   */
  private LoAdsNstdOracleBean toLoAdsNstdBean (Store object, String addressId, String addressType) {
    LoAdsNstdOracleBean bean = new LoAdsNstdOracleBean();
    if (addressType.equals(ArtsConstants.ADDRESS_TYPE_STORE)) {
      bean.setA1Ads(object.getAddress());
      bean.setA2Ads(((CMSStore)object).getAddressLine2());      // Added by Manpreet S Bawa -- 01/25/05
      bean.setCoNm(object.getCountry());
      bean.setIdAds(addressId);
      bean.setNmUn(object.getCity());
      bean.setPcNm(object.getZipCode());
      bean.setTeNm(object.getState());
    }
    else if (addressType.equals(ArtsConstants.ADDRESS_TYPE_COMPANY)) {
      bean.setA1Ads(((CMSStore)object).getCompAddressLine1());
      bean.setA2Ads(((CMSStore)object).getCompAddressLine2());                  // Added by Manpreet S Bawa -- 01/25/05
      bean.setCoNm(((CMSStore)object).getCompCountry());
      bean.setIdAds(addressId);
      bean.setNmUn(((CMSStore)object).getCompCity());
      bean.setPcNm(((CMSStore)object).getCompPostalCode());
      bean.setTeNm(((CMSStore)object).getCompState());
    }
    else
      return  null;
    return  bean;
  }

  /**
   * Party Address
   * @param object
   * @param addressId
   * @return
   */
  private LoAdsPrtyOracleBean toLoAdsPrtyBean (Store object, String addressId, String sAddressType) {
    LoAdsPrtyOracleBean bean = new LoAdsPrtyOracleBean();
    bean.setIdAds(addressId);
    bean.setIdPrty(object.getId());
    bean.setTyRoPrty(ArtsConstants.PARTY_ROLE_TYPE_STORE);
    bean.setTyAds(sAddressType);
    bean.setDcEf(null);
    bean.setDcEp(null);
    bean.setScPrtyAds(null);
    return  bean;
  }

  /**
   * Retail Store
   * @param object
   * @param partyId
   * @param partyRoleTypeCode
   * @return
   */
  private PaStrRtlOracleBean toPaStrRtlBean (Store object, String partyId, String partyRoleTypeCode) {
    CMSStore cmsStore = (CMSStore)object;
    PaStrRtlOracleBean bean = new PaStrRtlOracleBean();
    bean.setIdStrRt(object.getId());
    //--- Manpreet S Bawa
    //-- Fiscalcode, BrandId , ShopDescription, CompanyDescription
    bean.setIdCmy(cmsStore.getCompanyCode());
    //      System.out.println("fiscal code: "+ cmsStore.getFiscalCode());
    //      System.out.println("getBrandID: "+ cmsStore.getBrandID());
    //      System.out.println("getShopDescription: "+ cmsStore.getShopDescription());
    //      System.out.println("getCompanyDescription: "+ cmsStore.getCompanyDescription());
    bean.setFiscalCode(cmsStore.getFiscalCode());
    bean.setIdBrand(cmsStore.getBrandID());
    bean.setDeStrRt(cmsStore.getShopDescription());             // Shop Description
    bean.setDeCmy(cmsStore.getCompanyDescription());            // Company Description
    //-- End -----
    bean.setDcOpnRtStr(object.getOpeningPhysicalDate());
    bean.setIdPrty(partyId);
    bean.setDcClRtStr(object.getClosingPhysicalDate());
    bean.setTyRoPrty(partyRoleTypeCode);
    bean.setQuSzStr(0.0);       // required field
    bean.setQuSzArSl(0.0);      // required field
    bean.setLuZnPrcRtStr(null);
    // Probably should become some part of TaxableGroup/TaxGroupRule
    bean.setPeTx(object.getDefaultTaxRate());
    bean.setPeTxRgnl(object.getDefaultRegionalTaxRate());
    bean.setDeTx(object.getTaxLabel());
    bean.setDeTxRgnl(object.getRegionalTaxLabel());
    bean.setFlTxRgnl(object.usesRegionalTaxCalculations());
    // this should be a foriegn key of CurrencyID in table Currency
    bean.setTyCny(object.getCurrencyType().getCode());
    bean.setPwAcsStr(object.getPassword());
    return  bean;
  }

  /**
   * Employee Retail Store Assignment
   * @param object
   * @return
   */
  private StAsgmtEmStrOracleBean[] toStAsgmtEmStrBeans (Store object) {
    ArrayList list = new ArrayList();
    StAsgmtEmStrOracleBean bean = null;
    if (object.getRegionalManagerId() != null && object.getRegionalManagerId().length() > 0) {
      bean = new StAsgmtEmStrOracleBean();
      bean.setIdEm(object.getRegionalManagerId());
      bean.setIdStrRt(object.getId());
      bean.setDcAsgmtStrEf(null);
      bean.setDcAsgmtStrEp(null);
      bean.setScAsgmtStr(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_REGIONAL_MANAGER);
      list.add(bean);
    }
    if (object.getDistrictManagerId() != null && object.getDistrictManagerId().length() > 0) {
      bean = new StAsgmtEmStrOracleBean();
      bean.setIdEm(object.getDistrictManagerId());
      bean.setIdStrRt(object.getId());
      bean.setDcAsgmtStrEf(null);
      bean.setDcAsgmtStrEp(null);
      bean.setScAsgmtStr(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_DISTRICT_MANAGER);
      list.add(bean);
    }
    if (object.getManagerId() != null && object.getManagerId().length() > 0) {
      bean = new StAsgmtEmStrOracleBean();
      bean.setIdEm(object.getManagerId());
      bean.setIdStrRt(object.getId());
      bean.setDcAsgmtStrEf(null);
      bean.setDcAsgmtStrEp(null);
      bean.setScAsgmtStr(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_MANAGER);
      list.add(bean);
    }
    if (object.getAsstManagerId() != null && object.getAsstManagerId().length() > 0) {
      bean = new StAsgmtEmStrOracleBean();
      bean.setIdEm(object.getAsstManagerId());
      bean.setIdStrRt(object.getId());
      bean.setDcAsgmtStrEf(null);
      bean.setDcAsgmtStrEp(null);
      bean.setScAsgmtStr(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_ASST_MANAGER);
      list.add(bean);
    }
    if (object.getDefaultConsultantId() != null && object.getDefaultConsultantId().length() > 0) {
      bean = new StAsgmtEmStrOracleBean();
      bean.setIdEm(object.getDefaultConsultantId());
      bean.setIdStrRt(object.getId());
      bean.setDcAsgmtStrEf(null);
      bean.setDcAsgmtStrEp(null);
      bean.setScAsgmtStr(ArtsConstants.EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_DEFAULT_CONSULTANT);
      list.add(bean);
    }
    return  (StAsgmtEmStrOracleBean[])list.toArray(new StAsgmtEmStrOracleBean[list.size()]);
  }

  /**
   * Physical Location
   * @param object
   * @return a loLcnPhyOracleBean
   */
  private LoLcnPhyOracleBean toLoLcnPhyBean (Store object) {
    LoLcnPhyOracleBean bean = new LoLcnPhyOracleBean();
    bean.setCdLcnPhy(object.getGeoCode());
    bean.setTyCrdn(ArtsConstants.COORDINATE_TYPE_CODE_DEFAULT);
    return  bean;
  }

  /**
   * Physical Location Address
   * @param object
   * @param coordinateTypeCode
   * @return
   */
  private LoAdsLcnPhyOracleBean toLoAdsLcnPhyBean (Store object, String addressId) {
    LoAdsLcnPhyOracleBean bean = new LoAdsLcnPhyOracleBean();
    bean.setCdLcnPhy(object.getGeoCode());
    bean.setTyCrdn(ArtsConstants.COORDINATE_TYPE_CODE_DEFAULT);
    bean.setIdAds(addressId);
    return  bean;
  }

}



