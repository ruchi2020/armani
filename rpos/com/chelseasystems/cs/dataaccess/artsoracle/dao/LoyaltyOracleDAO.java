/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 05/18/2005 | Vikram    | N/A       | Added getUpdateHistoryWithNewLoyaltySQL method     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05/10/2005 | Vikram    | N/A       | POS_104665_TS_LoyaltyManagement_Rev0               |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmLoyaltyOracleBean;
import  java.util.ArrayList;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.LoyaltyDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmLoyaltyRuleOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmLoyaltyHistOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPosLnItmDtlOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.VArmPremioHistOracleBean;

import  java.sql.SQLException;
import  com.chelseasystems.cs.loyalty.*;
import  com.chelseasystems.cs.customer.CMSCustomer;
import  java.util.Calendar;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class LoyaltyOracleDAO extends BaseOracleDAO
    implements LoyaltyDAO {

  /**
   * insert
   * @throws SQLException
   * @param loyalty Loyalty
   */
  public void insert (Loyalty loyalty) throws SQLException {
    execute(getInsertSQL(loyalty));
  }

  /**
   * insertRule
   * @throws SQLException
   * @param loyaltyRule LoyaltyRule
   */
  public void insertRule (LoyaltyRule loyaltyRule) throws SQLException {
    execute(getInsertRuleSQL(loyaltyRule));
  }

  /**
   * insertHistory
   * @throws SQLException
   * @param loyaltyHistory LoyaltyHistory
   */
  public void insertHistory (LoyaltyHistory loyaltyHistory) throws SQLException {
    execute(getInsertHistorySQL(loyaltyHistory));
  }

  /**
   * selectById
   * @throws SQLException
   * @param loyaltyNumber String
   * @return Loyalty
   */
  public Loyalty selectById (String loyaltyNumber) throws SQLException {
    String selectSQL = ArmLoyaltyOracleBean.selectSql + where(ArmLoyaltyOracleBean.COL_LOYALTY_NUM);
    BaseOracleBean[] beans = this.query(new ArmLoyaltyOracleBean(), selectSQL, loyaltyNumber);
    if (beans != null && beans.length > 0)
      return  fromBeanToObject((ArmLoyaltyOracleBean)beans[0]);
    return  null;
  }

  /**
   * selectById
   * @throws SQLException
   * @param loyaltyNumber String
   * @return Loyalty
   */
  public LoyaltyRule selectRuleById (String ruleId) throws SQLException {
    String selectSQL = ArmLoyaltyRuleOracleBean.selectSql + where(ArmLoyaltyRuleOracleBean.COL_ID_RULE);
    BaseOracleBean[] beans = this.query(new ArmLoyaltyRuleOracleBean(), selectSQL, ruleId);
    if (beans != null && beans.length > 0)
      return  fromBeanToObject((ArmLoyaltyRuleOracleBean)beans[0]);
    return  null;
  }

  /**
   * selectByCustomerId
   * @throws SQLException
   * @param customerId String
   * @return Loyalty[]
   */
  public Loyalty[] selectByCustomerId (String customerId) throws SQLException {
    String selectSQL = ArmLoyaltyOracleBean.selectSql + where(ArmLoyaltyOracleBean.COL_ID_CT);
    BaseOracleBean[] beans = this.query(new ArmLoyaltyOracleBean(), selectSQL, customerId);
    if (beans == null)
      return  new Loyalty[0];
    Loyalty[] loyalties = new Loyalty[beans.length];
    for (int i = 0; i < beans.length; i++) {
      loyalties[i] = fromBeanToObject((ArmLoyaltyOracleBean)beans[i]);
    }
    return  loyalties;
  }

  /**
   * selectHistoryByLoyaltyIdForDateRange
   * @throws SQLException
   * @param loyaltyNumber String
   * @return LoyaltyHistory[]
   */
  public LoyaltyHistory[] selectHistoryByLoyaltyIdForDateRange (String loyaltyNumber, Date fromDate, Date toDate) throws SQLException {
    String selectSQL = ArmLoyaltyHistOracleBean.selectSql + where(ArmLoyaltyHistOracleBean.COL_LOYALTY_NUM) + " and (" + ArmLoyaltyHistOracleBean.COL_DC_TRANSACTION + " between ? and ?)";
    ArrayList list = new ArrayList();
    list.add(loyaltyNumber);
    list.add(fromDate);
    //V.M.: Introduced nextToEndDate as dtEnd is 12:00am for end day and hence not inclusive
    Calendar cal = Calendar.getInstance();
    cal.setTime(toDate);
    cal.add(Calendar.DATE, 1);
    Date nextToEndDate = cal.getTime();
    list.add(nextToEndDate);
    BaseOracleBean[] beans = this.query(new ArmLoyaltyHistOracleBean(), selectSQL, list);
    if (beans == null)
      return  new LoyaltyHistory[0];
    LoyaltyHistory[] loyaltyHistoryArray = new LoyaltyHistory[beans.length];
    Hashtable loyaltyTable = new Hashtable();
    for (int i = 0; i < beans.length; i++) {
      loyaltyHistoryArray[i] = fromBeanToObject((ArmLoyaltyHistOracleBean)beans[i]);
      if(loyaltyTable.get(loyaltyHistoryArray[i].getTransactionId()) == null){
    	  loyaltyTable.put(loyaltyHistoryArray[i].getTransactionId(),loyaltyHistoryArray[i]);
      }else{
    	  if(loyaltyHistoryArray[i].getPointEarned()!=0){
    		  ((LoyaltyHistory)loyaltyTable.get(loyaltyHistoryArray[i].getTransactionId())).setPointEarned(loyaltyHistoryArray[i].getPointEarned());
    	  }else if(loyaltyHistoryArray[i].getPointUsed()!=0){
    		  ((LoyaltyHistory)loyaltyTable.get(loyaltyHistoryArray[i].getTransactionId())).setPointUsed(loyaltyHistoryArray[i].getPointUsed());
    	  }
      }
    }
/*    if(loyaltyTable!=null){
    	loyaltyHistoryArray = (LoyaltyHistory[])loyaltyTable.entrySet().toArray(new LoyaltyHistory[0]);
    }*/
    loyaltyHistoryArray = new  LoyaltyHistory[loyaltyTable.size()];
    int i=0;
    for(Enumeration e=loyaltyTable.elements();e.hasMoreElements();){
    	if(i<loyaltyHistoryArray.length)
    	loyaltyHistoryArray[i++] = ((LoyaltyHistory)e.nextElement());
    }
    return  loyaltyHistoryArray;
  }

  /**
   * selectHistoryByLoyaltyIdForDateRange
   * @throws SQLException
   * @param loyaltyNumber String
   * @return LoyaltyHistory[]
   */
  public LoyaltyHistory[] selectHistoryByTransactionId (String id) throws SQLException {
    String selectSQL = ArmLoyaltyHistOracleBean.selectSql + where(ArmLoyaltyHistOracleBean.COL_AI_TRN);
    ArrayList list = new ArrayList();
    list.add(id);
    BaseOracleBean[] beans = this.query(new ArmLoyaltyHistOracleBean(), selectSQL, list);
    if (beans == null)
      return  new LoyaltyHistory[0];
    LoyaltyHistory[] loyaltyHistoryArray = new LoyaltyHistory[beans.length];
    for (int i = 0; i < beans.length; i++) {
      loyaltyHistoryArray[i] = fromBeanToObject((ArmLoyaltyHistOracleBean)beans[i]);
    }
    return  loyaltyHistoryArray;
  }
  
  /**
   * selectHistoryByTxnIdAndLoyaltyNumber
   * @throws SQLException
   * @param loyaltyNumber String
   * @return LoyaltyHistory[]
   */
  public LoyaltyHistory selectHistoryByTxnIdAndLoyaltyNumber (String txnId, String loyaltyNumber) throws SQLException {
    String selectSQL = ArmLoyaltyHistOracleBean.selectSql + where(ArmLoyaltyHistOracleBean.COL_AI_TRN,
    		ArmLoyaltyHistOracleBean.COL_LOYALTY_NUM);
    ArrayList list = new ArrayList();    
    list.add(txnId);
    list.add(loyaltyNumber);
    BaseOracleBean[] beans = this.query(new ArmLoyaltyHistOracleBean(), selectSQL, list);    
    if (beans != null && beans.length > 0) {
    	return fromBeanToObject((ArmLoyaltyHistOracleBean)beans[0]);
    } 	    
    return  null;
  }

  //    /**
  //     * selectRewardsByCustomerId
  //     * @throws SQLException
  //     * @param customerId String
  //     * @return RewardCard[]
  //     */
  //    public RewardCard[] selectRewardsByCustomerId(String customerId) throws SQLException
  //    {
  //        return null;
  //    }
  /**
   * selectRulesByStoreIdForDateRange
   * @throws SQLException
   * @param storeId String
   * @param fromDate Date
   * @param toDate Date
   * @return LoyaltyRule[]
   */
  public LoyaltyRule[] selectRulesByStoreIdForDateRange (String storeId, Date fromDate, Date toDate) throws SQLException {
    ArrayList params = new ArrayList();
    String selectSQL = ArmLoyaltyRuleOracleBean.selectSql;
    if (storeId != null) {
      selectSQL += where(ArmLoyaltyRuleOracleBean.COL_ID_STR_RT);
      params.add(storeId);
    }
    if (fromDate != null) {
      if (params.size() > 0)
        selectSQL += " and "; 
      else 
        selectSQL += " where ";
      selectSQL += ArmLoyaltyRuleOracleBean.COL_DC_START + " <= ? ";
      params.add(fromDate);
    }
    if (toDate != null) {
      if (params.size() > 0)
        selectSQL += " and "; 
      else 
        selectSQL += " where ";
      selectSQL += "((" + ArmLoyaltyRuleOracleBean.COL_DC_END + " >= ?) or (" + ArmLoyaltyRuleOracleBean.COL_DC_END + " is null))";
      params.add(toDate);
    }
    BaseOracleBean[] beans = this.query(new ArmLoyaltyRuleOracleBean(), selectSQL, params);
    if (beans == null)
      return  new LoyaltyRule[0];
    LoyaltyRule[] loyaltyRules = new LoyaltyRule[beans.length];
    for (int i = 0; i < beans.length; i++) {
      loyaltyRules[i] = fromBeanToObject((ArmLoyaltyRuleOracleBean)beans[i]);
    }
    return  loyaltyRules;
  }

  /**
   * put your documentation comment here
   * @param loyaltyNumber
   * @return CMSPremioHistory[]
   * @exception Exception
   */
  public CMSPremioHistory[] selectPremioDiscountHistoryByLoyaltyId(String loyaltyNumber) throws SQLException{
	  if(loyaltyNumber == null || loyaltyNumber.length() == 0)
		  return null;
	  
	  ArrayList params =  new ArrayList();
	  params.add(loyaltyNumber);
	  
	  StringBuffer selectSQL = new StringBuffer(VArmPremioHistOracleBean.selectSql);
	  selectSQL.append(where(VArmPremioHistOracleBean.COL_LOYALTY_NUM));
	  
	  BaseOracleBean[] beans = this.query(new VArmPremioHistOracleBean(), selectSQL.toString(), params);
	  if(beans == null)	  
		  return new CMSPremioHistory[0];
	  
	  CMSPremioHistory[] premioHistory = new CMSPremioHistory[beans.length];
	  for(int i=0; i<beans.length; i++){
		  premioHistory[i] = fromBeanToObject((VArmPremioHistOracleBean)beans[i]);
	  }
	  return premioHistory;
  }
  
  /**
   * updatePoints
   * @throws SQLException
   * @param loyaltyId String
   * @param currentBalance double
   * @param lifetimeBalance double
   */
  public void updatePoints (String loyaltyId, double currentBalance, double lifetimeBalance, double currentYearBalance, double lastYearBalance) throws SQLException {
	    execute(getUpdatePointsSQL(loyaltyId, currentBalance, lifetimeBalance, currentYearBalance,lastYearBalance));
	  }

  /**
   * updateStatus
   * @throws SQLException
   * @param loyaltyId String
   * @param status boolean
   */
  public void updateStatus (String loyaltyId, boolean status) throws SQLException {
    execute(getUpdateStatusSQL(loyaltyId, status));
  }

  private CMSPremioHistory fromBeanToObject(BaseOracleBean baseBean) {
		VArmPremioHistOracleBean bean = (VArmPremioHistOracleBean) baseBean;
		CMSPremioHistory object = new CMSPremioHistory();
		object.doSetStoreName(bean.getStoreName());
		object.doSetTransactionID(bean.getTxnNum());
		object.doSetTransactionDate(bean.getTxnDate());
		object.doSetRedeemedPoints(new Double(bean.getRedeemedPoints()));
		object.doSetTransactionPremioDiscountAmt(bean.getTxnAmt());
		object.doSetLoyaltyNumber(bean.getLoyaltyNum());
		return object;
	}

  /**
   * fromObjectToBean
   *
   * @param obj Loyalty
   * @return ArmLoyaltyOracleBean
   */
  private ArmLoyaltyOracleBean fromObjectToBean (Loyalty obj) {
    ArmLoyaltyOracleBean bean = new ArmLoyaltyOracleBean();
    bean.setIdCt(obj.getCustomer().getId());
    bean.setLoyaltyNum(obj.getLoyaltyNumber());
    bean.setTyStrRt(obj.getStoreType());
    bean.setDcIssued(obj.getIssueDate());
    bean.setIssuedBy(obj.getIssuedBy());
    bean.setCurrentBal(obj.getCurrBalance());
    bean.setLifetimeBal(obj.getLifeTimeBalance());
    bean.setLastYearBal(obj.getLastYearBalance());
    bean.setCurrentYearBal(obj.getCurrYearBalance());
    bean.setFlStatus(obj.getStatus());
    return  bean;
  }

  /**
   * fromObjectToBean
   *
   * @param obj LoyaltyRule
   * @return ArmLoyaltyRuleOracleBean
   */
  private ArmLoyaltyRuleOracleBean fromObjectToBean (LoyaltyRule obj) {
    ArmLoyaltyRuleOracleBean bean = new ArmLoyaltyRuleOracleBean();
    bean.setIdRule(obj.getRuleID());
    bean.setDcStart(obj.getStartDate());
    bean.setDcEnd(obj.getEndDate());
    bean.setIdStrRt(obj.getStoreID());
    bean.setTyCt(obj.getCustType());
    bean.setPoints(obj.getPointsRatio());
    bean.setIdDptPos(obj.getItemDepart());
    bean.setIdClss(obj.getItemClass());
    bean.setIdSbcl(obj.getItemSubclass());
    bean.setStyleNum(obj.getStyleNumber());
    return  bean;
  }

  /**
   * fromObjectToBean
   *
   * @param obj LoyaltyHistory
   * @return ArmLoyaltyHistOracleBean
   */
  private ArmLoyaltyHistOracleBean fromObjectToBean (LoyaltyHistory obj) {
    ArmLoyaltyHistOracleBean bean = new ArmLoyaltyHistOracleBean();
    bean.setLoyaltyNum(obj.getLoyaltyNumber());
    bean.setDcTransaction(obj.getTransactionDate());
    bean.setIdStrRt(obj.getStoreID());
    bean.setAiTrn(obj.getTransactionId());
    bean.setPoints(obj.getPointEarned());
    bean.setTyTrn(obj.getTransactionType());
    bean.setCdReason(obj.getReasonCode());
    return  bean;
  }

  /**
   * fromBeanToObject
   *
   * @param bean ArmLoyaltyOracleBean
   * @return Loyalty
   */
  private Loyalty fromBeanToObject (ArmLoyaltyOracleBean bean) {
	    Loyalty obj = new Loyalty();
	    CMSCustomer customer = null;
	    CustomerOracleDAO customerDAO = new CustomerOracleDAO();
	    try {
	      customer = (CMSCustomer)customerDAO.selectById(bean.getIdCt());
	    } catch (SQLException ex) {
	      ex.printStackTrace();
	    }
	    obj.setCustomer(customer);
	    obj.setLoyaltyNumber(bean.getLoyaltyNum());
	    obj.setStoreType(bean.getTyStrRt());
	    obj.setIssueDate(bean.getDcIssued());
	    obj.setIssuedBy(bean.getIssuedBy());
	    if (bean.getCurrentBal() != null)
	      obj.setCurrBalance(bean.getCurrentBal().doubleValue());
	    if (bean.getLifetimeBal() != null)
	      obj.setLifeTimeBalance(bean.getLifetimeBal().doubleValue());
	    if (bean.getCurrentYearBal() != null)
	        obj.setCurrYearBalance(bean.getCurrentYearBal().doubleValue());
	    if (bean.getLastYearBal() != null)
	        obj.setLastYearBalance(bean.getLastYearBal().doubleValue());
	    obj.setStatus(bean.getFlStatus().booleanValue());
	    return  obj;
	  }

  /**
   * fromBeanToObject
   *
   * @param bean ArmLoyaltyRuleOracleBean
   * @return LoyaltyRule
   */
  private LoyaltyRule fromBeanToObject (ArmLoyaltyRuleOracleBean bean) {
    LoyaltyRule obj = new LoyaltyRule();
    obj.setRuleID(bean.getIdRule());
    obj.setStartDate(bean.getDcStart());
    obj.setEndDate(bean.getDcEnd());
    obj.setStoreID(bean.getIdStrRt());
    obj.setCustType(bean.getTyCt());
    obj.setPointsRatio(bean.getPoints().doubleValue());
    obj.setItemDepart(bean.getIdDptPos());
    obj.setItemClass(bean.getIdClss());
    obj.setItemSubclass(bean.getIdSbcl());
    obj.setStyleNumber(bean.getStyleNum());
    return  obj;
  }

  /**
   * fromBeanToObject
   *
   * @param bean ArmLoyaltyHistOracleBean
   * @return LoyaltyHistory
   */
  private LoyaltyHistory fromBeanToObject (ArmLoyaltyHistOracleBean bean) {
	    LoyaltyHistory obj = new LoyaltyHistory();
	    obj.setLoyaltyNumber(bean.getLoyaltyNum());
	    obj.setTransactionDate(bean.getDcTransaction());
	    obj.setStoreID(bean.getIdStrRt());
	    obj.setTransactionId(bean.getAiTrn());
    if (bean.getPoints() != null){
    	if(bean.getPoints().doubleValue()>0){
	      obj.setPointEarned(bean.getPoints().doubleValue());
    	}else{
    		obj.setPointUsed(bean.getPoints().doubleValue());
    	}
    }
	    obj.setTransactionType(bean.getTyTrn());
	    obj.setReasonCode(bean.getCdReason());
	    return  obj;
	  }

  /**
   *
   * @param loyalty Loyalty
   * @return ParametricStatement
   * @exception SQLException
   */
  public ParametricStatement getInsertSQL (Loyalty loyalty) throws SQLException {
    ArmLoyaltyOracleBean bean = fromObjectToBean(loyalty);
    return  new ParametricStatement(bean.getInsertSql(), bean.toList());
  }

  /**
   *
   * @param loyaltyRule LoyaltyRule
   * @return ParametricStatement
   * @exception SQLException
   */
  protected ParametricStatement getInsertRuleSQL (LoyaltyRule loyaltyRule) throws SQLException {
    ArmLoyaltyRuleOracleBean bean = fromObjectToBean(loyaltyRule);
    return  new ParametricStatement(bean.getInsertSql(), bean.toList());
  }

  /**
   *
   * @param loyaltyHistory LoyaltyHistory
   * @return ParametricStatement
   * @exception SQLException
   */
  public ParametricStatement getInsertHistorySQL (LoyaltyHistory loyaltyHistory) throws SQLException {
    ArmLoyaltyHistOracleBean bean = fromObjectToBean(loyaltyHistory);
    return  new ParametricStatement(bean.getInsertSql(), bean.toList());
  }

  /**
   *
   * @param loyalty Loyalty
   * @return ParametricStatement
   * @exception SQLException
   */
  public ParametricStatement getUpdateSQL (Loyalty loyalty) throws SQLException {
    ArmLoyaltyOracleBean bean = fromObjectToBean(loyalty);
    return  new ParametricStatement(bean.getUpdateSql(), bean.toList());
  }

  /**
   *
   * @param loyaltyRule LoyaltyRule
   * @return ParametricStatement
   * @exception SQLException
   */
  protected ParametricStatement getUpdateRuleSQL (LoyaltyRule loyaltyRule) throws SQLException {
    ArmLoyaltyRuleOracleBean bean = fromObjectToBean(loyaltyRule);
    return  new ParametricStatement(bean.getUpdateSql(), bean.toList());
  }

  /**
   *
   * @param loyaltyHistory LoyaltyHistory
   * @return ParametricStatement
   * @exception SQLException
   */
  protected ParametricStatement getUpdateHistorySQL (LoyaltyHistory loyaltyHistory) throws SQLException {
    ArmLoyaltyHistOracleBean bean = fromObjectToBean(loyaltyHistory);
    return  new ParametricStatement(bean.getUpdateSql(), bean.toList());
  }

  /**
   *
   * @param oldLoyaltyId String
   * @param newLoyalty Loyalty
   * @return ParametricStatement
   * @throws SQLException
   */
  public ParametricStatement getUpdateHistoryWithNewLoyaltySQL (String oldLoyaltyId, Loyalty newLoyalty) throws SQLException {
    String sql = "update " + ArmLoyaltyHistOracleBean.TABLE_NAME + " set " + ArmLoyaltyHistOracleBean.COL_LOYALTY_NUM + " = ? " + where(ArmLoyaltyHistOracleBean.COL_LOYALTY_NUM);
    ArrayList list = new ArrayList();
    list.add(newLoyalty.getLoyaltyNumber());
    list.add(oldLoyaltyId);
    return  new ParametricStatement(sql, list);
  }

  /**
   *
   * @param loyaltyId String
   * @param activeStatus boolean
   * @return ParametricStatement
   * @exception SQLException
   */
  public ParametricStatement getUpdateStatusSQL (String loyaltyId, boolean activeStatus) throws SQLException {
    String sql = "update " + ArmLoyaltyOracleBean.TABLE_NAME + " set " + ArmLoyaltyOracleBean.COL_FL_STATUS + " = ? " + where(ArmLoyaltyOracleBean.COL_LOYALTY_NUM);
    ArrayList list = new ArrayList();
    list.add(new Boolean(activeStatus));
    list.add(loyaltyId);
    return  new ParametricStatement(sql, list);
  }

  /**
   *
   * @param loyaltyId String
   * @param currentBalance double
   * @param lifetimeBalance double
   * @return ParametricStatement
   * @exception SQLException
   */
  public ParametricStatement getUpdatePointsSQL (String loyaltyId, double currentBalance, double lifetimeBalance, double currentYearBalance, double lastYearBalance) throws SQLException {
	    String sql = "update " + ArmLoyaltyOracleBean.TABLE_NAME + " set " + ArmLoyaltyOracleBean.COL_CURRENT_BAL + " = ?, " + ArmLoyaltyOracleBean.COL_LIFETIME_BAL + " = ?, "+ArmLoyaltyOracleBean.COL_CURRENT_YEAR_BAL + " = ? , "+ArmLoyaltyOracleBean.COL_LAST_YEAR_BAL+ " = ? " + where(ArmLoyaltyOracleBean.COL_LOYALTY_NUM);
	    ArrayList list = new ArrayList();
	    list.add(new Double(currentBalance));
	    list.add(new Double(lifetimeBalance));
	    list.add(new Double(currentYearBalance));
	    list.add(new Double(lastYearBalance));
	    list.add(loyaltyId);
	    return  new ParametricStatement(sql, list);
	  }
  /*
   public static void main(String args[])
   {
   LoyaltyOracleDAO loyaltyDAO = new LoyaltyOracleDAO();
   Loyalty loyalty = new Loyalty();
   loyalty.setCustomerID("106801");
   loyalty.setLoyaltyNumber("3000123456");
   loyalty.setStoreType("05065910159");
   loyalty.setIssueDate(new Date());
   loyalty.setIssuedBy("104350");
   loyalty.setCurrBalance(100);
   loyalty.setLifeTimeBalance(1200);
   loyalty.setStatus(true);
   try
   {
   loyaltyDAO.insert(loyalty);
   }
   catch (Exception ex)
   {
   ex.printStackTrace();
   }
   try
   {
   loyalty = loyaltyDAO.selectById("3000123456");
   System.out.println(loyalty.getCustomerID());
   System.out.println(loyalty.getLoyaltyNumber());
   System.out.println(loyalty.getStoreType());
   System.out.println(loyalty.getIssueDate());
   System.out.println(loyalty.getIssuedBy());
   System.out.println(loyalty.getCurrBalance());
   System.out.println(loyalty.getLifeTimeBalance());
   System.out.println(loyalty.getStatus());
   }
   catch (Exception ex1)
   {
   ex1.printStackTrace();
   }
   try
   {
   Loyalty[] loyalties = loyaltyDAO.selectByCustomerId("106801");
   System.out.println("Size of loyaltyServ.findByCustomerId is " + loyalties.length);
   loyalty = loyalties[0];
   System.out.println(loyalty.getCustomerID());
   System.out.println(loyalty.getLoyaltyNumber());
   System.out.println(loyalty.getStoreType());
   System.out.println(loyalty.getIssueDate());
   System.out.println(loyalty.getIssuedBy());
   System.out.println(loyalty.getCurrBalance());
   System.out.println(loyalty.getLifeTimeBalance());
   System.out.println(loyalty.getStatus());
   }
   catch (Exception ex1)
   {
   ex1.printStackTrace();
   }
   }
   */
}


