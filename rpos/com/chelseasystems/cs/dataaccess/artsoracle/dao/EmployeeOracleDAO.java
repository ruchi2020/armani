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
 | 6    | 03-03-2005 | Manpreet  |ExternalId | Modification to  store ArmaniExternalID      |
 --------------------------------------------------------------------------------------------
 | 5    | 03-02-2005 | Manpreet  |DateoFBirth| Modification to  add Date of birth.          |
 --------------------------------------------------------------------------------------------
 | 4    | 02-14-2005 | Anand     | N/A       | Modification to  extract External ID         |
 --------------------------------------------------------------------------------------------
 | 3    | 01-27-2005 | Manpreet  | N/A       | Modified  to add Armani External ID          |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 01-26-2005 | Manpreet  | N/A       | Modified  to add AddressLine2, Country, Email|
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cr.database.*;
import  com.chelseasystems.cr.employee.*;
import  com.chelseasystems.cs.employee.*;
import  com.chelseasystems.cr.telephone.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cr.rules.BusinessRuleException;
import  java.sql.*;
import  java.util.*;


/**
 *
 *  Employee Data Access Object.<br>
 *  This object encapsulates all database access for Employee.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>AccountingId</td><td>PA_EM</td><td>ID_PLN_EM_CMN</td></tr>
 *    <tr><td>Address</td><td>LO_ADS_NSTD</td><td>A1_ADS</td></tr>
 *    <tr><td>City</td><td>LO_ADS_NSTD</td><td>NM_UN</td></tr>
 *    <tr><td>EmploymentStatus</td><td>PA_EM</td><td>SC_EM</td></tr>
 *    <tr><td>FirstName</td><td>PA_PRS</td><td>FN_PRS</td></tr>
 *    <tr><td>HireDate</td><td>ST_EM_ASGMT</td><td>DC_EF</td></tr>
 *    <tr><td>HomeStoreId</td><td>PA_OPR</td><td>ID_STR_RT</td></tr>
 *    <tr><td>JobCodeId</td><td>CO_PST</td><td>CD_JOB</td></tr>
 *    <tr><td>LastName</td><td>PA_PRS</td><td>LN_PRS</td></tr>
 *    <tr><td>MiddleName</td><td>PA_PRS</td><td>MN_PRS</td></tr>
 *    <tr><td>NickName</td><td>PA_PRS</td><td>NN_PRS</td></tr>
 *    <tr><td>Password</td><td>PA_OPR</td><td>PW_ACS_OPR</td></tr>
 *    <tr><td>PreferredISOCountry</td><td>PA_PRTY</td><td>ED_CO</td></tr>
 *    <tr><td>PreferredISOLanguage</td><td>PA_PRTY</td><td>ED_LA</td></tr>
 *    <tr><td>Privileges</td><td>PA_EM</td><td>IT_ACCESS_SCRTY</td></tr>
 *    <tr><td>RightHanded</td><td>PA_PRS</td><td>FL_HND_PRS</td></tr>
 *    <tr><td>SSN</td><td>PA_EM</td><td>UN_NMB_SCL_SCTY</td></tr>
 *    <tr><td>ShortName</td><td>PA_EM</td><td>NM_EM</td></tr>
 *    <tr><td>SickLeaveBalance</td><td>PA_EM</td><td>QY_LV_SCK</td></tr>
 *    <tr><td>State</td><td>LO_ADS_NSTD</td><td>TE_NM</td></tr>
 *    <tr><td>TerminationDate</td><td>ST_EM_ASGMT</td><td>DC_EP</td></tr>
 *    <tr><td>VacationLeaveBalance</td><td>PA_EM</td><td>HI_LV_VCTN</td></tr>
 *    <tr><td>ZipCode</td><td>LO_ADS_NSTD</td><td>PC_NM</td></tr>
 *  </table>
 *
 *  @see Employee
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.CoPlnEmCmnOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.CoPstOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LeAcntStrFnOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsNstdOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsPrtyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaEmOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaOprOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrsOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrtyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.PaRoPrtyOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.StEmAsgmtOracleBean
 *
 */
public class EmployeeOracleDAO extends BaseOracleDAO
    implements EmployeeDAO {
  private static TelephoneOracleDAO telephoneDAO = new TelephoneOracleDAO();

  /**
   *
   * @param object
   * @exception SQLException
   */
  public void insert (Employee object) throws SQLException {
    executeCaseSensitive(getInsertSQL(object));
  }

  /**
   *
   * @param Employee
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (Employee object) throws SQLException {
    List statements = new ArrayList();
    // party
    PaPrtyOracleBean paPrtyBean = this.toPaPrtyBean(object);
    statements.add(new ParametricStatement(paPrtyBean.getInsertSql(), paPrtyBean.toList()));
    // person
    PaPrsOracleBean paPrsBean = this.toPaPrsBean(object);
    statements.add(new ParametricStatement(paPrsBean.getInsertSql(), paPrsBean.toList()));
    // party role
    PaRoPrtyOracleBean paRoPrtyBean = this.toPaRoPrtyBean(object);
    statements.add(new ParametricStatement(paRoPrtyBean.getInsertSql(), paRoPrtyBean.toList()));
    // telephone
    statements.addAll(Arrays.asList(telephoneDAO.getInsertSql(object.getTelephone(), object.getId())));
    // address
    LoAdsOracleBean loAdsBean = this.toLoAdsBean(object, this.getNextChelseaId());
    statements.add(new ParametricStatement(loAdsBean.getInsertSql(), loAdsBean.toList()));
    // party address
    LoAdsPrtyOracleBean loAdsPrtyBean = this.toLoAdsPrtyBean(object, loAdsBean.getIdAds());
    statements.add(new ParametricStatement(loAdsPrtyBean.getInsertSql(), loAdsPrtyBean.toList()));
    // non-standard address
    LoAdsNstdOracleBean loAdsNstdBean = this.toLoAdsNstdBean(object, loAdsBean.getIdAds());
    statements.add(new ParametricStatement(loAdsNstdBean.getInsertSql(), loAdsNstdBean.toList()));
    // Email -- Manpreet S Bawa (01/26/2005)
    LoEmlAdsOracleBean loEmlAdsBean = this.toLoEmlAdsBean(object);
    if (loEmlAdsBean != null)
      statements.add(new ParametricStatement(loEmlAdsBean.getInsertSql(), loEmlAdsBean.toList()));
    // store financial ledger account
    LeAcntStrFnOracleBean leAcntStrFnBean = this.toLeAcntStrFnBean(object, this.getNextChelseaId());
    statements.add(new ParametricStatement(leAcntStrFnBean.getInsertSql(), leAcntStrFnBean.toList()));
    // employee commision plan
    CoPlnEmCmnOracleBean coPlnEmCmnBean = this.toCoPlnEmCmnBean(object, leAcntStrFnBean.getIdAcntLdg());
    statements.add(new ParametricStatement(coPlnEmCmnBean.getInsertSql(), coPlnEmCmnBean.toList()));
    // employee
    PaEmOracleBean paEmBean = this.toPaEmBean(object);
    statements.add(new ParametricStatement(paEmBean.getInsertSql(), paEmBean.toList()));
    // position
    CoPstOracleBean coPstBean = null;
    ArrayList queryParams = new ArrayList();
    queryParams.add(object.getJobCodeId());
    BaseOracleBean[] beans = this.query(new CoPstOracleBean(), where(CoPstOracleBean.COL_CD_JOB), queryParams);
    if (beans != null && beans.length > 0) {
      coPstBean = (CoPstOracleBean)beans[0];
    } 
    else {
      coPstBean = this.toCoPstBean(object, this.getNextChelseaId());
      statements.add(new ParametricStatement(coPstBean.getInsertSql(), coPstBean.toList()));
    }
    // employee assignment
    StEmAsgmtOracleBean stEmAsgmtBean = this.toStEmAsgmtBean(object, coPstBean.getIdPst());
    statements.add(new ParametricStatement(stEmAsgmtBean.getInsertSql(), stEmAsgmtBean.toList()));
    //Convert statements to Upper case
    statements = toUpperCase(statements);
    // operator
    //not to be converted to upper case as it persists password
    PaOprOracleBean paOprBean = this.toPaOprBean(object, this.getNextChelseaId());
    statements.add(new ParametricStatement(paOprBean.getInsertSql(), paOprBean.toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   *
   * @param object
   * @exception SQLException
   */
  public void update (Employee object) throws SQLException {
    executeCaseSensitive(this.getUpdateSQL(object));
  }

  /**
   *
   * @param Employee
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQL (Employee object) throws SQLException {
    List statements = new ArrayList();
    List params = null;
    BaseOracleBean[] beans = null;
    // position
    CoPstOracleBean coPstBean = null;
    ArrayList queryParams = new ArrayList();
    queryParams.add(object.getJobCodeId());
    beans = this.query(new CoPstOracleBean(), where(CoPstOracleBean.COL_CD_JOB), queryParams);
    if (beans != null && beans.length > 0) {
      coPstBean = (CoPstOracleBean)beans[0];
    } 
    else {
      coPstBean = this.toCoPstBean(object, this.getNextChelseaId());
      statements.add(new ParametricStatement(coPstBean.getInsertSql(), coPstBean.toList()));
    }
    // employee assignment
    StEmAsgmtOracleBean stEmAsgmtBean = this.toStEmAsgmtBean(object, coPstBean.getIdPst());
    params = stEmAsgmtBean.toList();
    params.add(object.getId());
    statements.add(new ParametricStatement(stEmAsgmtBean.getUpdateSql() + where(StEmAsgmtOracleBean.COL_ID_EM), params));
    // employee
    PaEmOracleBean paEmBean = this.toPaEmBean(object);
    params = paEmBean.toList();
    params.add(object.getId());
    statements.add(new ParametricStatement(paEmBean.getUpdateSql() + where(PaEmOracleBean.COL_ID_EM), params));
    // employee commision plan
    CoPlnEmCmnOracleBean coPlnEmCmnBean = null;
    queryParams.clear();
    queryParams.add(object.getId());
    beans = this.query(new CoPlnEmCmnOracleBean(), where(CoPlnEmCmnOracleBean.COL_ID_PLN_EM_CMN), queryParams);
    if (beans == null || beans.length == 0) {
      // store financial ledger account
      LeAcntStrFnOracleBean leAcntStrFnBean = this.toLeAcntStrFnBean(object, this.getNextChelseaId());
      statements.add(new ParametricStatement(leAcntStrFnBean.getInsertSql(), leAcntStrFnBean.toList()));
      // employee commision plan
      coPlnEmCmnBean = this.toCoPlnEmCmnBean(object, leAcntStrFnBean.getIdAcntLdg());
      statements.add(new ParametricStatement(coPlnEmCmnBean.getInsertSql(), coPlnEmCmnBean.toList()));
    }
    // party address
    LoAdsPrtyOracleBean loAdsPrtyBean = new LoAdsPrtyOracleBean();
    queryParams.clear();
    queryParams.add(object.getId());
    beans = this.query(loAdsPrtyBean, where(LoAdsPrtyOracleBean.COL_ID_PRTY), queryParams);
    if (beans != null && beans.length > 0) {
      loAdsPrtyBean = (LoAdsPrtyOracleBean)beans[0];
    }
    // non-standard address
    LoAdsNstdOracleBean loAdsNstdBean = this.toLoAdsNstdBean(object, loAdsPrtyBean.getIdAds());
    params = loAdsNstdBean.toList();
    params.add(loAdsPrtyBean.getIdAds());
    statements.add(new ParametricStatement(loAdsNstdBean.getUpdateSql() + where(LoAdsNstdOracleBean.COL_ID_ADS), params));
    // Email
    LoEmlAdsOracleBean loEmlAdsBean = this.toLoEmlAdsBean(object);
    if (loEmlAdsBean != null) {
      params = this.toLoEmlAdsBean(object).toList();
      params.add(object.getId());
      statements.add(new ParametricStatement(LoEmlAdsOracleBean.updateSql + this.where(LoEmlAdsOracleBean.COL_ID_PRTY), params));
    } 
    else {
      params = new ArrayList();
      params.add(object.getId());
      statements.add(new ParametricStatement(LoEmlAdsOracleBean.deleteSql + this.where(LoEmlAdsOracleBean.COL_ID_PRTY), params));
    }
    // telephone
    statements.addAll(Arrays.asList(telephoneDAO.getUpdateSql(new Telephone[] {
      object.getTelephone()
    }, object.getId())));
    // person
    PaPrsOracleBean paPrsBean = this.toPaPrsBean(object);
    params = paPrsBean.toList();
    params.add(object.getId());
    statements.add(new ParametricStatement(paPrsBean.getUpdateSql() + where(PaPrsOracleBean.COL_ID_PRTY_PRS), params));
    // party
    PaPrtyOracleBean paPrtyBean = this.toPaPrtyBean(object);
    params = paPrtyBean.toList();
    params.add(object.getId());
    statements.add(new ParametricStatement(paPrtyBean.getUpdateSql() + where(PaPrtyOracleBean.COL_ID_PRTY), params));
    //Convert statements to Upper case
    statements = toUpperCase(statements);
    // operator
    //not to be converted to upper case as it persists password
    PaOprOracleBean paOprBean = this.toPaOprBean(object, this.getNextChelseaId());
    params = paOprBean.toList();
    params.add(object.getId());
    statements.add(new ParametricStatement(paOprBean.getUpdateSql() + where(PaOprOracleBean.COL_ID_EM), params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  // Manpreet S Bawa -  to persist Email (01/26/2005)
  LoEmlAdsOracleBean toLoEmlAdsBean (Employee object) {
    CMSEmployee cmsEmployee = (CMSEmployee)object;
    if (cmsEmployee.getEmail() == null || cmsEmployee.getEmail().length() == 0)
      return  null;
    LoEmlAdsOracleBean bean = new LoEmlAdsOracleBean();
    bean.setIdPrty(object.getId());
    bean.setEmAds(cmsEmployee.getEmail());
    return  bean;
  }

  // Manpreet S Bawa - 01/27/05
  // Retrieves Employee using Armani External Id
  public CMSEmployee selectByExternalId (String sExternalID) throws SQLException {
    CMSEmployee object = null;
    List params = null;
    BaseOracleBean[] beans = null;
    params = new ArrayList();
    params.add(sExternalID);
    // Create bean with Armani External ID
    beans = this.query(new PaEmOracleBean(), where(PaEmOracleBean.COL_ARM_EXTERNAL_ID), params);
    if (beans != null && beans.length > 0) {
      PaEmOracleBean paEmBean = (PaEmOracleBean)beans[0];
      // System.out.println("emp ext id : "+sExternalID+" id : "+paEmBean.getIdEm());
      // if employee exists select by employee id
      return  (CMSEmployee)selectById(paEmBean.getIdEm());
    }
    return  object;
  }

  /**
   *
   * @param id
   * @return
   * @exception SQLException
   */
  public Employee selectById (String sEmpID) throws SQLException {
    Employee object = new CMSEmployee(sEmpID);
    List params = null;
    BaseOracleBean[] beans = null;
    // party
    params = new ArrayList();
    params.add(sEmpID);
    params.add(ArtsConstants.PARTY_ROLE_TYPE_EMPLOYEE);
    beans = this.query(new PaPrtyOracleBean(), where(PaPrtyOracleBean.COL_ID_PRTY, PaPrtyOracleBean.COL_TY_PRTY), params);
    if (beans == null || beans.length == 0)
      return  null;
    PaPrtyOracleBean paPrtyBean = (PaPrtyOracleBean)beans[0];
    object.doSetPreferredISOCountry(paPrtyBean.getEdCo());
    object.doSetPreferredISOLanguage(paPrtyBean.getEdLa());
    // person
    params.clear();
    params.add(sEmpID);
    beans = this.query(new PaPrsOracleBean(), where(PaPrsOracleBean.COL_ID_PRTY_PRS), params);
    PaPrsOracleBean paPrsBean = (PaPrsOracleBean)beans[0];
    object.doSetFirstName(paPrsBean.getFnPrs());
    object.doSetLastName(paPrsBean.getLnPrs());
    object.doSetMiddleName(paPrsBean.getMnPrs());
    object.doSetNickName(paPrsBean.getNnPrs());
    object.doSetRightHanded(paPrsBean.getFlHndPrs().booleanValue());
    // telephone
    object.doSetTelephone(telephoneDAO.selectByPartyIdAndTelephoneType(object.getId(), TelephoneType.EMPLOYEE));
    // non-standard address
    params.clear();
    params.add(sEmpID);
    beans = this.query(new LoAdsPrtyOracleBean(), where(LoAdsPrtyOracleBean.COL_ID_PRTY), params);
    if (beans != null && beans.length > 0) {
      LoAdsPrtyOracleBean loAdsPrtyBean = (LoAdsPrtyOracleBean)beans[0];
      String addressId = loAdsPrtyBean.getIdAds();
      params.clear();
      params.add(addressId);
      beans = this.query(new LoAdsNstdOracleBean(), where(LoAdsNstdOracleBean.COL_ID_ADS), params);
      LoAdsNstdOracleBean loAdsNstdBean = (LoAdsNstdOracleBean)beans[0];
      // Set address line1 and line2
      try {
        if (loAdsNstdBean.getA1Ads() != null) {
          ((CMSEmployee)object).setAddress(loAdsNstdBean.getA1Ads());
        } 
        else {
          ((CMSEmployee)object).setAddress("");
        }
        ((CMSEmployee)object).setAddressLine2(loAdsNstdBean.getA2Ads());
      } catch (BusinessRuleException be) {
        be.printStackTrace();
      }
      //object.doSetAddress(loAdsNstdBean.getA1Ads());
      object.doSetCity(loAdsNstdBean.getNmUn());
      object.doSetState(loAdsNstdBean.getTeNm());
      object.doSetZipCode(loAdsNstdBean.getPcNm());
      ((CMSEmployee)object).doSetCountry(loAdsNstdBean.getCoNm());
    }
    // Email
    // Manpreet S Bawa  - 01/27/05
    params.clear();
    params.add(sEmpID);
    beans = this.query(new LoEmlAdsOracleBean(), where(LoEmlAdsOracleBean.COL_ID_PRTY), params);
    if (beans != null && beans.length > 0) {
      LoEmlAdsOracleBean loEmlAdsOracleBean = (LoEmlAdsOracleBean)beans[0];
      String sEmailID = loEmlAdsOracleBean.getEmAds();
      ((CMSEmployee)object).setEmail(sEmailID);
    }
    // employee
    params.clear();
    params.add(sEmpID);
    beans = this.query(new PaEmOracleBean(), where(PaEmOracleBean.COL_ID_EM), params);
    if (beans != null && beans.length > 0) {
      PaEmOracleBean paEmBean = (PaEmOracleBean)beans[0];
      object.doSetExternalID(paEmBean.getArmExternalId());      // EXTERNAL ID -- Manpreet S Bawa 01/27/05
      object.doSetId(paEmBean.getIdEm());
      object.doSetAccountingId(paEmBean.getIdPlnEmCmn());
      object.doSetShortName(paEmBean.getNmEm());
      object.doSetSSN(paEmBean.getUnNmbSclScty());
      object.doSetEmploymentStatus(Integer.parseInt(paEmBean.getScEm().trim()));
      object.doSetVacationLeaveBalance(paEmBean.getHiLvVctn());
      object.doSetSickLeaveBalance(paEmBean.getQyLvSck());
      object.doSetPrivileges(paEmBean.getItAccessScrty().longValue());
    }
    // employee assignment
    params.clear();
    params.add(sEmpID);
    beans = this.query(new StEmAsgmtOracleBean(), where(StEmAsgmtOracleBean.COL_ID_EM), params);
    if (beans != null && beans.length > 0) {
      StEmAsgmtOracleBean stEmAsgmtBean = (StEmAsgmtOracleBean)beans[0];
      String positionId = stEmAsgmtBean.getIdPst();
      object.doSetHireDate(stEmAsgmtBean.getDcEf());
      object.doSetTerminationDate(stEmAsgmtBean.getDcEp());
      // position
      params.clear();
      params.add(positionId);
      beans = this.query(new CoPstOracleBean(), where(CoPstOracleBean.COL_ID_PST), params);
      if (beans != null && beans.length > 0) {
        CoPstOracleBean coPstBean = (CoPstOracleBean)beans[0];
        object.doSetJobCodeId(coPstBean.getCdJob());
      }
    }
    // operator
    params.clear();
    params.add(sEmpID);
    beans = this.query(new PaOprOracleBean(), where(PaOprOracleBean.COL_ID_EM), params);
    if (beans != null && beans.length > 0) {
      PaOprOracleBean paOprBean = (PaOprOracleBean)beans[0];
      object.doSetPassword(paOprBean.getPwAcsOpr());
      object.doSetHomeStoreId(paOprBean.getIdStrRt());
    }
    return  object;
  }

  /**
   *
   * @param ids
   * @return
   * @exception SQLException
   */
  public Employee[] selectByIds (String[] ids) throws SQLException {
    ids = ArtsUtil.removeDupString(ids);
    List result = new ArrayList();
    for (int i = 0; i < ids.length; i++) {
      Employee object = this.selectById(ids[i]);
      if (object != null)
        result.add(object);
    }
    return  (Employee[])result.toArray(new CMSEmployee[result.size()]);
  }

  /**
   *
   * @param ssn
   * @return
   * @exception SQLException
   */
  public Employee[] selectBySSN (String ssn) throws SQLException {
    String sql = "select PA_EM.ID_PRTY from PA_EM " + "where PA_EM.UN_NMB_SCL_SCTY = ?";
    List params = new ArrayList();
    params.add(ssn);
    return  this.selectByIds(this.queryForIds(sql, params));
  }

  /**
   *
   * @param homeStoreId
   * @return
   * @exception SQLException
   */
  public Employee[] selectByHomeStoreId (String homeStoreId) throws SQLException {
    String sql = "select PA_EM.ID_PRTY from PA_EM, PA_OPR " + "where PA_EM.ID_PRTY = PA_OPR.ID_EM " + "and PA_OPR.ID_STR_RT = ?";
    List params = new ArrayList();
    params.add(homeStoreId);
    return  this.selectByIds(this.queryForIds(sql, params));
  }

  /**
   *
   * @param shortName
   * @return
   * @exception SQLException
   */
  public Employee[] selectByShortName (String shortName) throws SQLException {
    String sql = "select PA_EM.ID_PRTY from PA_EM " + "where PA_EM.NM_EM = ?";
    List params = new ArrayList();
    params.add(shortName);
    return  this.selectByIds(this.queryForIds(sql, params));
  }

  /**
  *
  * @param shortName
  * @return
  * @exception SQLException
  */
 public Employee[] selectByLastName (String lastName) throws SQLException {

   String sql = "select  PA_EM.ID_PRTY from PA_EM , PA_PRS  " +
                                "where PA_PRS.ID_PRTY_PRS = PA_EM.ID_PRTY and upper(PA_PRS.LN_PRS) like ?";
   List params = new ArrayList();
   params.add(lastName.toUpperCase()+"%");
   return  this.selectByIds(this.queryForIds(sql, params));
 }


  /**
   * Party
   * @param object
   * @return
   */
  private PaPrtyOracleBean toPaPrtyBean (Employee object) {
    PaPrtyOracleBean bean = new PaPrtyOracleBean();
    bean.setIdPrty(object.getId());
    bean.setTyPrty(ArtsConstants.PARTY_ROLE_TYPE_EMPLOYEE);
    bean.setLuOrgLg(null);
    bean.setEdCo(object.getPreferredISOCountry());
    bean.setEdLa(object.getPreferredISOLanguage());
    return  bean;
  }

  /**
   * Person
   * @param object
   * @return
   */
  private PaPrsOracleBean toPaPrsBean (Employee object) {
    PaPrsOracleBean bean = new PaPrsOracleBean();
    bean.setIdPrtyPrs(object.getId());
    bean.setNmPrsSln(null);
    bean.setLnPrs(object.getLastName());
    bean.setMnPrs(object.getMiddleName());
    bean.setFnPrs(object.getFirstName());
    bean.setNnPrs(object.getNickName());
    bean.setFlHndPrs(object.doGetRightHanded());
    //bean.setDcPrsBrt(null);
    // Manpreet S Bawa - 03/02/05
    // Date of birth wasnt being added.
    bean.setDcPrsBrt(object.getBirthDate());
    bean.setTyGndPrs(null);
    return  bean;
  }

  /**
   * Party Role
   * @param object
   * @return
   */
  private PaRoPrtyOracleBean toPaRoPrtyBean (Employee object) {
    PaRoPrtyOracleBean bean = new PaRoPrtyOracleBean();
    bean.setIdPrty(object.getId());
    bean.setTyRoPrty(ArtsConstants.PARTY_ROLE_TYPE_EMPLOYEE);
    bean.setDcRoPrtyEf(null);
    bean.setDcRoPrtyEp(null);
    return  bean;
  }

  /**
   * Address
   * @param object
   * @return
   */
  private LoAdsOracleBean toLoAdsBean (Employee object, String addressId) {
    LoAdsOracleBean bean = new LoAdsOracleBean();
    bean.setIdAds(addressId);
    return  bean;
  }

  /**
   * Party Address
   * @param object
   * @param addressId
   * @return
   */
  private LoAdsPrtyOracleBean toLoAdsPrtyBean (Employee object, String addressId) {
    LoAdsPrtyOracleBean bean = new LoAdsPrtyOracleBean();
    bean.setIdAds(addressId);
    bean.setIdPrty(object.getId());
    bean.setTyRoPrty(ArtsConstants.PARTY_ROLE_TYPE_EMPLOYEE);
    bean.setDcEf(null);
    bean.setDcEp(null);
    bean.setScPrtyAds(null);
    return  bean;
  }

  /**
   * Non Standard Address
   * @param object
   * @param addressId
   * @return
   */
  private LoAdsNstdOracleBean toLoAdsNstdBean (Employee object, String addressId) {
    CMSEmployee cmsEmployee = (CMSEmployee)object;
    LoAdsNstdOracleBean bean = new LoAdsNstdOracleBean();
    bean.setA1Ads(object.getAddress());
    bean.setA2Ads(cmsEmployee.getAddressLine2());               // Manpreet S Bawa - Add addressLine2 (01/26/2005)
    bean.setCoNm(cmsEmployee.getCountry());                     // Manpreet S Bawa - Add Country (01/26/2005)
    bean.setIdAds(addressId);
    bean.setNmUn(object.getCity());
    bean.setPcNm(object.getZipCode());
    bean.setTeNm(object.getState());
    return  bean;
  }

  /**
   * StoreFinancialLedgerAccount
   * @param object
   * @return
   */
  private LeAcntStrFnOracleBean toLeAcntStrFnBean (Employee object, String ledgerAccountId) {
    LeAcntStrFnOracleBean bean = new LeAcntStrFnOracleBean();
    bean.setIdAcntLdg(ledgerAccountId);
    bean.setTyAcntFnLdg(null);
    bean.setIdStrRt(object.getHomeStoreId());
    bean.setDeAcntFnLdg(null);
    bean.setMoBlncBgnFnLdg(null);
    bean.setMoBlncCrtFnLdg(null);
    return  bean;
  }

  /**
   * EmployeeCommisionPlan
   * @param object
   * @return
   */
  private CoPlnEmCmnOracleBean toCoPlnEmCmnBean (Employee object, String ledgerAccountId) {
    CoPlnEmCmnOracleBean bean = new CoPlnEmCmnOracleBean();
    if (object.getAccountingId() == null || object.getAccountingId().length() == 0)
      bean.setIdPlnEmCmn(object.getId()); 
    else 
      bean.setIdPlnEmCmn(object.getAccountingId());
    bean.setIdStrRt(object.getHomeStoreId());
    bean.setIdAcntLdg(ledgerAccountId);
    bean.setDePlnEmCmn(null);
    return  bean;
  }

  /**
   * Employee
   * @param object
   * @return
   */
  private PaEmOracleBean toPaEmBean (Employee object) {
    PaEmOracleBean bean = new PaEmOracleBean();
    bean.setIdEm(object.getId());
    bean.setTyRoPrty(ArtsConstants.PARTY_ROLE_TYPE_EMPLOYEE);
    //  ARMANI EXTERNAL ID -- Manpreet S Bawa 03/03/05
    bean.setArmExternalId(object.getExternalID());
    // Commented By Manpreet S Bawa - 03/03/05
    // Wasn't storing ArmaniExternal ID.
    //    bean.setArmExternalId(((CMSEmployee)object).getExternalId()); //  ARMANI EXTERNAL ID -- Manpreet S Bawa 01/27/05
    //    bean.setIdCt(((CMSEmployee)object).getCustomerID()); // Stores CustomerID for this Employee
    //      bean.setIdPlnEmCmn(object.getAccountingId());
    bean.setIdPlnEmCmn(object.getId());
    bean.setNmEm(object.getShortName());
    bean.setUnNmbSclScty(object.getSSN());
    bean.setIdPrty(object.getId());
    bean.setScEm(Integer.toString(object.doGetEmploymentStatus()));
    bean.setTyEmCmpsn(null);
    bean.setHiLvVctn(object.getVacationLeaveBalance());
    bean.setQyLvSck(object.getSickLeaveBalance());
    bean.setItAccessScrty(object.doGetPrivileges());
    return  bean;
  }

  /**
   * Position
   * @param object
   * @return
   */
  private CoPstOracleBean toCoPstBean (Employee object, String positionId) {
    CoPstOracleBean bean = new CoPstOracleBean();
    bean.setIdPst(positionId);
    if (object.getJobCodeId() != null && object.getJobCodeId().length() > 0)
      bean.setCdJob(object.getJobCodeId()); 
    else 
      bean.setCdJob(JobCode.UNDECLARE_JOB_CODE.getId());
    bean.setDePst(null);
    bean.setNmPstTtl(null);
    bean.setCdPyrlTyp(null);
    bean.setFlPstShr(null);
    bean.setFlCmn(null);
    bean.setCdAcsLv(null);
    return  bean;
  }

  /**
   * EmployeeAssignment
   * @param object
   * @return
   */
  private StEmAsgmtOracleBean toStEmAsgmtBean (Employee object, String positionId) {
    StEmAsgmtOracleBean bean = new StEmAsgmtOracleBean();
    bean.setIdEm(object.getId());
    bean.setIdPst(positionId);
    bean.setScEmAsgmt(null);
    bean.setDcEf(object.getHireDate());
    bean.setDcEp(object.getTerminationDate());
    return  bean;
  }

  /**
   * Operator
   * @param object
   * @return
   */
  private PaOprOracleBean toPaOprBean (Employee object, String operatorId) {
    PaOprOracleBean bean = new PaOprOracleBean();
    bean.setIdOpr(operatorId);
    bean.setIdStrRt(object.getHomeStoreId());
    bean.setIdEm(object.getId());
    bean.setPwAcsOpr(object.getPassword());
    return  bean;
  }
}



