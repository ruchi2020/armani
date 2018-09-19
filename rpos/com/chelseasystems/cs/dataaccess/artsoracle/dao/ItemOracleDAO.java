/*
 * @copyright (c) 1998-2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 4	4/19/05	        KS	Item Lookup specs               Implementation
 *
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import  java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import  java.util.Enumeration;
import  java.util.Iterator;
import  java.util.List;
import  java.util.Map;
import  com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.item.Item;
import  com.chelseasystems.cr.item.RelatedItem;
import com.chelseasystems.cr.payment.Payment;
import  com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.customer.CMSVIPMembershipDetail;
import  com.chelseasystems.cs.dataaccess.ItemDAO;
import  com.chelseasystems.cs.dataaccess.ItemVwDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmAlternClssGrpOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmItmHistOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmItmSohOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmVipDiscountDetailOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.AsItmOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.AsItmRtlStrOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.CoAsctnRltdItmOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.CoCfgCpnOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.IdIdnPsOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.LePrcSplCtTypOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.RkCompPrmPkOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkItemVwOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPrmItmOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmCpnTndOracleBean;
import  com.chelseasystems.cs.item.CMSItem;
import  com.chelseasystems.cs.item.ItemSearchString;
import  com.chelseasystems.cs.item.ItemStock;
import com.chelseasystems.cs.payment.CMSCoupon;
import com.chelseasystems.cs.payment.Coupon;
import com.chelseasystems.cs.payment.ICMSPaymentConstants;
import com.chelseasystems.cs.payment.MallCert;



/**
 *
 *  Item Data Access Object.<br>
 *  This object encapsulates all database access for Item.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>DefaultQuantity</td><td>ID_IDN_PS</td><td>QU_UN_BLK_MNM</td></tr>
 *    <tr><td>Department</td><td>AS_ITM</td><td>ID_DPT_POS</td></tr>
 *    <tr><td>Description</td><td>AS_ITM</td><td>DE_ITM</td></tr>
 *    <tr><td>MarkdownAmount</td><td>AS_ITM_RTL_STR</td><td>RP_SLS_CRT</td></tr>
 *    <tr><td>Message</td><td>CO_ASCTN_RLTD_ITM</td><td>NA_ASCTN_RLTD_ITM</td></tr>
 *    <tr><td>RedeemableType</td><td>AS_ITM</td><td>TY_ITM</td></tr>
 *    <tr><td>RegionalTaxable</td><td>AS_ITM</td><td>LU_EXM_TX</td></tr>
 *    <tr><td>RelatedItemId</td><td>CO_ASCTN_RLTD_ITM</td><td>ID_ITM_RLTD</td></tr>
 *    <tr><td>RequiresManualUnitPrice</td><td>ID_IDN_PS</td><td>FL_ENR_PRC_RQ</td></tr>
 *    <tr><td>RetailPrice</td><td>AS_ITM_RTL_STR</td><td>RP_PR_SLS</td></tr>
 *    <tr><td>Taxable</td><td>AS_ITM</td><td>LU_EXM_TX</td></tr>
 *  </table>
 *
 *  @see Item
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.AsItmOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.AsItmRtlStrOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.CoAsctnRltdItmOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.CoPrmOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.LePrcSplCtTypOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.IdIdnPsOracleBean
 *
 */
public class ItemOracleDAO extends BaseOracleDAO
    implements ItemDAO {
  private static ItemVwOracleDAO itemVwDao = new ItemVwOracleDAO();
  
  //added by satin for exception item
  private static String whereSql = "where BARCODE = ?";
  private static String whereSql1 = "where ID_ITM = ?";
  //private static String selectSql = AsItmOracleBean.selectSql1;
  private static String insertAsItmRtlStrSql = "insert into AS_ITM_RTL_STR (ID_STR_RT, ID_ITM, SC_ITM_SLS, ID_GP_TX, DC_ITM_SLS, SC_ITM, RP_PR_SLS, FL_MKD_ORGL_PRC_PR, QU_MKD_PR_PRC_PR, FL_STK_UPDT_ON_HD, DC_PRC_EF_PRN_RT, RP_SLS_CRT, TY_PRC_RT, FL_PRC_RT_PNT_ALW, DC_PRC_SLS_EF_CRT, DC_PRC_SLS_EP_CRT, RP_PRC_MF_RCM_RT, DC_PRC_MF_RCM_RT, RP_PRC_CMPR_AT_SLS, CURRENCY_CODE, CD_VAT, VAT_RATE, FL_VAT_CLS, LU_EXM_TX, UPDATE_DT, NM_ITM) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  
  /**
   *
   * @param object
   * @exception SQLException
   */
  public void insert (Item object) throws SQLException {
    execute(getInsertSQL(object));
  }

  /**
   *
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (Item object) throws SQLException {
    List statements = new ArrayList();
    //
    List params = new ArrayList();
    params.add(object.getId());
    BaseOracleBean[] beans = this.query(new AsItmOracleBean(), where(AsItmOracleBean.COL_ID_ITM), params);
    if (beans == null || beans.length == 0) {
      // item
      AsItmOracleBean asItmBean = this.toAsItmBean(object);
      statements.add(new ParametricStatement(asItmBean.getInsertSql(), asItmBean.toList()));
      // related item association
      CoAsctnRltdItmOracleBean[] coAsctnRltdItmBeans = this.toCoAsctnRltdItmBeans(object);
      for (int i = 0; i < coAsctnRltdItmBeans.length; i++)
        statements.add(new ParametricStatement(coAsctnRltdItmBeans[i].getInsertSql(), coAsctnRltdItmBeans[i].toList()));
    }
    String currencyCode = object.getRetailPrice().getCurrencyType().getCode();
    // retail store item
    AsItmRtlStrOracleBean asItmRtlStrBean = this.toAsItmRtlStrBean(object, currencyCode);
    statements.add(new ParametricStatement(asItmRtlStrBean.getInsertSql(), asItmRtlStrBean.toList()));
    // pos identity
    IdIdnPsOracleBean idIdnPsBean = this.toIdIdnPsBean(object, this.getNextChelseaId(), currencyCode);
    statements.add(new ParametricStatement(idIdnPsBean.getInsertSql(), idIdnPsBean.toList()));
    // Loyalty mark down for key customer
    LePrcSplCtTypOracleBean lePrcSplCtTypBean = this.toLePrcSplCtTypOracleBean(object);
    if (lePrcSplCtTypBean != null)
      statements.add(new ParametricStatement(lePrcSplCtTypBean.getInsertSql(), lePrcSplCtTypBean.toList()));
    //Promotion ids
    RkPrmItmOracleBean[] rkPrmItmOracleBeans = toLRkPrmItmOracleBeans(object);
    if (rkPrmItmOracleBeans != null && rkPrmItmOracleBeans.length > 0) {
      for (int i = 0; i < rkPrmItmOracleBeans.length; i++)
        statements.add(new ParametricStatement(rkPrmItmOracleBeans[i].getInsertSql(), rkPrmItmOracleBeans[i].toList()));
    }
    //Promotion components
    RkCompPrmPkOracleBean[] rkCompPrmPkOracleBeans = toRkCompPrmPkOracleBeans(object);
    if (rkCompPrmPkOracleBeans != null && rkCompPrmPkOracleBeans.length > 0) {
      for (int i = 0; i < rkCompPrmPkOracleBeans.length; i++)
        statements.add(new ParametricStatement(rkCompPrmPkOracleBeans[i].getInsertSql(), rkCompPrmPkOracleBeans[i].toList()));
    }
    //
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
  * Inserts CMSItem object into AS_ITM_RTL_STR table
  * @param object
  * @return
  * @exception SQLException
  */
 public ParametricStatement[] getInsertSQLForItemRetailStore (CMSItem object, String storeId) 
 	throws SQLException {
   List statements = new ArrayList();
   AsItmRtlStrOracleBean asItmRtlStrBean = getAsItmRtlStrObject(object, storeId);
   statements.add(new ParametricStatement(asItmRtlStrBean.getInsertSql(), asItmRtlStrBean.toList()));
   statements.addAll(Arrays.asList(getInsertSQLForItemHistory(object, storeId)));
      
   return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
 }
  
 /**
  * Inserts CMSItem object into ARM_ITM_HIST table
  * @param object
  * @return
  * @exception SQLException
  */
 private ParametricStatement[] getInsertSQLForItemHistory (CMSItem object, String storeId) 
 	throws SQLException {
   List statements = new ArrayList();
   ArmItmHistOracleBean armItmHistBean;   
   armItmHistBean = getArmItmHistObject(object, storeId, 
		   "RETAIL_PRICE", (object.getCurrencyCode() + object.getRetailPrice().doubleValue()));
   statements.add(new ParametricStatement(armItmHistBean.getInsertSql(), armItmHistBean.toList()));
   armItmHistBean = getArmItmHistObject(object, storeId, 
		   "TAXABLE", (object.isTaxable() ? "Y" : "N"));
   statements.add(new ParametricStatement(armItmHistBean.getInsertSql(), armItmHistBean.toList()));
   armItmHistBean = getArmItmHistObject(object, storeId, 
		   "VAT_RATE", (object.getVatRate().intValue()) + "");
   statements.add(new ParametricStatement(armItmHistBean.getInsertSql(), armItmHistBean.toList()));
   armItmHistBean = getArmItmHistObject(object, storeId,
		   "MANUAL_PRICE_ENTRY", "O");
   statements.add(new ParametricStatement(armItmHistBean.getInsertSql(), armItmHistBean.toList()));
   armItmHistBean = getArmItmHistObject(object, storeId,
		   "STATUS", "A");
   statements.add(new ParametricStatement(armItmHistBean.getInsertSql(), armItmHistBean.toList()));
   
   return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
 }

  /**
   *
   * @param object
   * @exception SQLException
   */
  public void update (Item object) throws SQLException {
    this.execute(this.getUpdateSQL(object));
  }

  /**
   *
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQL (Item object) throws SQLException {
    String currencyCode = object.getRetailPrice().getCurrencyType().getCode();
    List statements = new ArrayList();
    List params = new ArrayList();
    // item
    AsItmOracleBean asItmBean = this.toAsItmBean(object);
    statements.add(new ParametricStatement(asItmBean.getUpdateSql(), asItmBean.toList()));
    // retail store item
    AsItmRtlStrOracleBean asItmRtlStrBean = this.toAsItmRtlStrBean(object, currencyCode);
    statements.add(new ParametricStatement(asItmRtlStrBean.getUpdateSql(), asItmRtlStrBean.toList()));
    // pos identity
    params = new ArrayList();
    params.add(object.getId());
    params.add(currencyCode);
    BaseOracleBean[] beans = this.query(new IdIdnPsOracleBean(), where(IdIdnPsOracleBean.COL_ID_ITM, IdIdnPsOracleBean.COL_CURRENCY_CODE), params);
    IdIdnPsOracleBean idIdnPsBean = (IdIdnPsOracleBean)beans[0];
    idIdnPsBean = this.toIdIdnPsBean(object, idIdnPsBean.getIdItmPos(), currencyCode);
    statements.add(new ParametricStatement(idIdnPsBean.getUpdateSql(), idIdnPsBean.toList()));
    // related item association
    statements.add(new ParametricStatement(new CoAsctnRltdItmOracleBean().getDeleteSql() + where(CoAsctnRltdItmOracleBean.COL_ID_ITM), params));
    CoAsctnRltdItmOracleBean[] coAsctnRltdItmBeans = this.toCoAsctnRltdItmBeans(object);
    for (int i = 0; i < coAsctnRltdItmBeans.length; i++)
      statements.add(new ParametricStatement(coAsctnRltdItmBeans[i].getInsertSql(), coAsctnRltdItmBeans[i].toList()));
    // Loyalty mark down for key customer
    params = new ArrayList();
    params.add(object.getId());
    params.add(currencyCode);
    statements.add(new ParametricStatement(new LePrcSplCtTypOracleBean().getDeleteSql() + where(LePrcSplCtTypOracleBean.COL_ID_ITM, LePrcSplCtTypOracleBean.COL_CURRENCY_CODE), params));
    LePrcSplCtTypOracleBean lePrcSplCtTypBean = this.toLePrcSplCtTypOracleBean(object);
    if (lePrcSplCtTypBean != null)
      statements.add(new ParametricStatement(lePrcSplCtTypBean.getInsertSql(), lePrcSplCtTypBean.toList()));
    //Promotion ids
    params = new ArrayList();
    params.add(object.getId());
    params.add(currencyCode);
    statements.add(new ParametricStatement(new RkPrmItmOracleBean().getDeleteSql() + where(RkPrmItmOracleBean.COL_ID_ITM, RkPrmItmOracleBean.COL_CURRENCY_CODE), params));
    RkPrmItmOracleBean[] rkPrmItmOracleBeans = toLRkPrmItmOracleBeans(object);
    if (rkPrmItmOracleBeans != null && rkPrmItmOracleBeans.length > 0) {
      for (int i = 0; i < rkPrmItmOracleBeans.length; i++)
        statements.add(new ParametricStatement(rkPrmItmOracleBeans[i].getInsertSql(), rkPrmItmOracleBeans[i].toList()));
    }
    //Promotion components
    params = new ArrayList();
    params.add(object.getId());
    params.add(currencyCode);
    statements.add(new ParametricStatement(new RkCompPrmPkOracleBean().getDeleteSql() + where(RkCompPrmPkOracleBean.COL_ID_ITM, RkCompPrmPkOracleBean.COL_CURRENCY_CODE), params));
    RkCompPrmPkOracleBean[] rkCompPrmPkOracleBeans = toRkCompPrmPkOracleBeans(object);
    if (rkCompPrmPkOracleBeans != null && rkCompPrmPkOracleBeans.length > 0) {
      for (int i = 0; i < rkCompPrmPkOracleBeans.length; i++)
        statements.add(new ParametricStatement(rkCompPrmPkOracleBeans[i].getInsertSql(), rkCompPrmPkOracleBeans[i].toList()));
    }
    //
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
  * Updates CMSItem object into AS_ITM_RTL_STR table
  * @param object
  * @return
  * @exception SQLException
  */
 public ParametricStatement[] getUpdateSQLForItemRetailStore (CMSItem object, String storeId) throws SQLException {
	 List statements = new ArrayList();	      
	 AsItmRtlStrOracleBean asItmRtlStrBean = getAsItmRtlStrObject(object, storeId);
	 statements.add(new ParametricStatement(asItmRtlStrBean.getUpdateSql(), asItmRtlStrBean.toList()));	  
	 
	 return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
 }

  /**
   *
   * @param id String
   * @param curreny String
   * @throws SQLException
   * @return Item
   */
  public Item selectById (String id, String currency) throws SQLException {
    return  (CMSItem)itemVwDao.selectById(id);    /*
     Item object = new CMSItem(id);
     List params = null;
     BaseOracleBean[] beans = null;
     // retail store item
     params = new ArrayList();
     params.add(id);
     params.add(currencyCode);
     beans = this.query(new AsItmRtlStrOracleBean(),
     where(AsItmRtlStrOracleBean.COL_ID_ITM, AsItmRtlStrOracleBean.COL_CURRENCY_CODE), params);
     if (beans == null || beans.length == 0) return null;
     AsItmRtlStrOracleBean asItmRtlStrBean = (AsItmRtlStrOracleBean)beans[0];
     object.doSetRetailPrice(asItmRtlStrBean.getRpPrSls());
     object.doSetMarkdownAmount(asItmRtlStrBean.getRpSlsCrt());
     object.doSetVatRate(asItmRtlStrBean.getVatRate());   //
     // item
     params = new ArrayList();
     params.add(id);
     beans = this.query(new AsItmOracleBean(), where(AsItmOracleBean.COL_ID_ITM), params);
     if (beans != null && beans.length > 0) {
     AsItmOracleBean asItmBean = (AsItmOracleBean)beans[0];
     object.doSetDepartment(asItmBean.getIdDptPos());
     object.doSetDescription(asItmBean.getDeItm());
     object.doSetRedeemableType(asItmBean.getTyItm());
     object.doSetTaxable(isFederalTaxable(asItmBean.getLuExmTx()));
     object.doSetRegionalTaxable(isRegionalTaxable(asItmBean.getLuExmTx()));
     }
     // pos identity
     params = new ArrayList();
     params.add(id);
     params.add(currencyCode);
     beans = this.query(new IdIdnPsOracleBean(),
     where(IdIdnPsOracleBean.COL_ID_ITM, IdIdnPsOracleBean.COL_CURRENCY_CODE), params);
     if (beans != null && beans.length > 0) {
     IdIdnPsOracleBean idIdnPsBean = (IdIdnPsOracleBean)beans[0];
     /**
     * RMS supports three values for requiresManualUnitPrice.
     * R - required
     * O - optional
     * P - prohibited
     */
    /*  object.doSetRequiresManualUnitPrice("R".equalsIgnoreCase(idIdnPsBean.getCdEnrPrcRq()));
     object.doSetManualPriceEntryProhibited("P".equalsIgnoreCase(idIdnPsBean.getCdEnrPrcRq()));
     object.doSetDefaultQuantity(new Integer(idIdnPsBean.getQuUnBlkMnm().intValue()));
     }
     // related item
     params = new ArrayList();
     params.add(id);
     beans = this.query(new CoAsctnRltdItmOracleBean(), where(CoAsctnRltdItmOracleBean.COL_ID_ITM), params);
     if (beans != null) {
     for (int i = 0; i < beans.length; i++) {
     CoAsctnRltdItmOracleBean coAsctnRltdItmBean = (CoAsctnRltdItmOracleBean)beans[i];
     RelatedItem relatedItem = new RelatedItem();
     relatedItem.doSetMessage(coAsctnRltdItmBean.getNaAsctnRltdItm());
     relatedItem.doSetRelatedItemId(coAsctnRltdItmBean.getIdItmRltd());
     object.doAddRelatedItem(relatedItem);
     }
     }
     //Loyalty Mark Down Price
     params = new ArrayList();
     params.add(id);
     params.add(currencyCode);
     beans = this.query(  new LePrcSplCtTypOracleBean(), where(LePrcSplCtTypOracleBean.COL_ID_ITM, LePrcSplCtTypOracleBean.COL_CURRENCY_CODE), params);
     if (beans != null && beans.length > 0) {
     object.doSetLoyaltyMarkdownAmount(((LePrcSplCtTypOracleBean)beans[0]).getRpSlsCrt());
     }
     //Promotion ids
     params = new ArrayList();
     params.add(id);
     params.add(currencyCode);
     beans = this.query(new RkPrmItmOracleBean(), where(RkPrmItmOracleBean.COL_ID_ITM, RkPrmItmOracleBean.COL_CURRENCY_CODE), params);
     if (beans != null && beans.length > 0) {
     ArrayList list = new ArrayList();
     for (int i = 0; i < beans.length; i++) list.add(((RkPrmItmOracleBean)beans[i]).getIdRuPrdv());
     object.doSetPromotionIds(list);
     }
     //Promotion components
     for (Iterator it = object.getPromotionIds(); it.hasNext();) {
     String promotionId = (String)it.next();
     params = new ArrayList();
     params.add(id);
     params.add(currencyCode);
     params.add(promotionId);
     beans = this.query(new RkCompPrmPkOracleBean(), where(RkCompPrmPkOracleBean.COL_ID_ITM, RkCompPrmPkOracleBean.COL_CURRENCY_CODE, RkCompPrmPkOracleBean.COL_ID_RU_PRDV), params);
     if (beans != null && beans.length > 0) {
     int[] comps = new int[beans.length];
     for (int i = 0; i < beans.length; i++) comps[i] = ((RkCompPrmPkOracleBean)beans[i]).getCompNum().intValue();
     object.doAddPromotionComponents(promotionId, comps);
     }
     }
     return  object;
     */

  }

  /**
   *
   * @param barcode String
   * @param storeId String
   * @throws SQLException
   * @return CMSItem
   */
  public CMSItem selectByBarcode (String barcode, String storeId) throws SQLException {
    //     String whereSql = "where BARCODE = ? and ID_STR_RT = ?";
    //     List params = new ArrayList();
    //     params.add(barcode);
    //     params.add(storeId);
    //     CMSItem[] aCmsItem = fromBeansToObjects(query(new RkItemVwOracleBean(), selectSql + whereSql, params));
    //     CMSItem cmsItem = null;
    //     for (int i = 0; aCmsItem != null && i < aCmsItem.length; i++){
    //       cmsItem = aCmsItem[i];
    //     }
    return  (CMSItem)itemVwDao.selectByBarcode(barcode, storeId);
  }
  
  /**
  *Added by Anjana to fetch the SAP Item
  * @param barcode String
  * @param storeId String
  *  @param itemIdLength String
  * @throws SQLException
  * @return CMSItem
  */
  public CMSItem findSAPBarCode (String barcode, String storeId , String itemIdLength) throws SQLException {
	    return  (CMSItem)itemVwDao.findSAPBarCode(barcode, storeId ,itemIdLength);
	  }

  /**
   * @param itemId String
   * @param storeId String
   * @throws SQLException
   * @return CMSItem
   */
  public CMSItem selectByItemIdAndStoreId (String itemId, String storeId) throws SQLException {
	  String whereSql = "where ID_ITM = ? and ID_STR_RT = ?";  	
	  List params = new ArrayList();
	  params.add(itemId);
	  params.add(storeId);
	  
	  BaseOracleBean[] beans = this.query(new AsItmRtlStrOracleBean(), 
			  AsItmRtlStrOracleBean.selectSql + whereSql, params);
	  if (beans != null && beans.length > 0) {
	      return this.fromBeanToObject(beans[0]);
	  }
	  return null;  
  }

  /**
   *
   * @param barcode String
   * @param storeId String
   * @throws SQLException
   * @return CMSItem
   */
  public CMSItem selectByIDOrBarcode (String barcode, String storeId) throws SQLException {
    return  (CMSItem)itemVwDao.selectByIDOrBarcode(barcode, storeId);
  }

  /**
   *
   * @param description String
   * @param storeId String
   * @throws SQLException
   * @return CMSItem[]
   */
  public CMSItem[] selectByDescriptionAndStoreId (String description, String storeId) throws SQLException {
    return  (CMSItem[])itemVwDao.selectByDescriptionAndStoreId(description, storeId);
  }

  /**
   *
   * @param ids
   * @return
   * @exception SQLException
   */
  public Item[] selectByIds (String[] ids, String currencyCode) throws SQLException {
    ids = ArtsUtil.removeDupString(ids);
    List result = new ArrayList();
    for (int i = 0; i < ids.length; i++) {
      Item object = this.selectById(ids[i], currencyCode);
      if (object != null)
        result.add(object);
    }
    return  (Item[])result.toArray(new CMSItem[result.size()]);
  }

  /**
   *
   * @param description
   * @param currencyCode
   * @return
   * @throws SQLException
   */
  public Item[] selectByDescription (String description, String currencyCode) throws SQLException {
    String sql = "select " + AsItmOracleBean.COL_ID_ITM + " from " + AsItmOracleBean.TABLE_NAME + " where lower(" + AsItmOracleBean.COL_DE_ITM + ") like ?";
    List params = new ArrayList();
    params.add(description.toLowerCase());
    return  this.selectByIds(this.queryForIds(sql, params), currencyCode);
  }

  /**
   *
   * @param currencyCode
   * @return
   * @throws SQLException
   */
  public Item[] selectAll (String currencyCode) throws SQLException {
    return  this.selectByIds(this.queryForIds(new AsItmOracleBean().getSelectSql(), new ArrayList()), currencyCode);
  }

  /**
   * Return a string like "00", "01", "10", "00" to represent the taxability
   * of an item.  The first char represents the federal tax.  The second char
   * represents the regional tax.
   * @param isFederalTaxable
   * @param isRegionalTaxable
   * @return two character string to represent the taxability of an item
   */
  public static String getTaxExemptCode (boolean isFederalTaxable, boolean isRegionalTaxable) {
    StringBuffer buff = new StringBuffer();
    buff.append(isFederalTaxable ? "1" : "0");
    buff.append(isRegionalTaxable ? "1" : "0");
    return  buff.toString();
  }

  /**
   * Used in conjuction with Arts.Item.TaxExemptCode, this method will determine
   * if an item is taxable at the federal level.
   * @see getTaxExemptCode
   * @param taxExemptCode
   * @return
   */
  public static boolean isFederalTaxable (String taxExemptCode) {
    if (taxExemptCode != null && taxExemptCode.length() == 2)
      return  taxExemptCode.charAt(0) == '1';
    return  false;
  }

  /**
   * Used in conjuction with Arts.Item.TaxExemptCode, this method will determine
   * if an item is taxable at the regional level.
   * @see getTaxExemptCode
   * @param taxExemptCode
   * @return
   */
  public static boolean isRegionalTaxable (String taxExemptCode) {
    if (taxExemptCode != null && taxExemptCode.length() == 2)
      return  taxExemptCode.charAt(1) == '1';
    return  false;
  }

  /**
   * Item
   * @param object
   * @return
   */
  private AsItmOracleBean toAsItmBean (Item object) {
    AsItmOracleBean bean = new AsItmOracleBean();
    bean.setIdItm(object.getId());
    bean.setIdLnPrc(null);
    bean.setIdStrcMr(null);
    bean.setLuHrcMrLv(null);
    bean.setFlItmDsc(null);
    bean.setLuSbsn(null);
    bean.setLuSn(null);
    bean.setFy(null);
    bean.setIdDptPos(object.getDepartment());
    bean.setFlAdtItmPrc(null);
    bean.setNmBrn(null);
    bean.setFlAznFrSls(null);
    bean.setLuItmUsg(null);
    bean.setNmItm(null);
    bean.setDeItm(object.getDescription());
    bean.setTyItm(object.getRedeemableType());
    bean.setLuKtSt(null);
    bean.setFlItmSbstIdn(null);
    bean.setLuClnOrd(null);
    bean.setLuExmTx(getTaxExemptCode(object.isTaxable(), object.isRegionalTaxable()));
    return  bean;
  }

  /**
   * RetailStoreItem
   * @param object
   * @return
   */
  private AsItmRtlStrOracleBean toAsItmRtlStrBean (Item object, String currencyCode) {
    AsItmRtlStrOracleBean bean = new AsItmRtlStrOracleBean();
    bean.setCurrencyCode(currencyCode);
    bean.setIdStrRt(null);
    bean.setIdItm(object.getId());
    bean.setScItmSls(null);
    bean.setIdGpTx(null);
    bean.setDcItmSls(null);
    bean.setScItm(null);
    bean.setRpPrSls(object.getRetailPrice());
    bean.setFlMkdOrglPrcPr(null);
    bean.setQuMkdPrPrcPr(null);
    bean.setFlStkUpdtOnHd(null);
    bean.setDcPrcEfPrnRt(null);
    bean.setRpSlsCrt(object.getMarkdownAmount());
    bean.setTyPrcRt(null);
    bean.setFlPrcRtPntAlw(null);
    bean.setDcPrcSlsEfCrt(null);
    bean.setDcPrcSlsEpCrt(null);
    bean.setRpPrcMfRcmRt(null);
    bean.setDcPrcMfRcmRt(null);
    bean.setRpPrcCmprAtSls(null);
    bean.setVatRate(object.getVatRate());
    return  bean;
  }

  /**
   * Contructs As_Itm_Rtl_Str object
   * @param object
   * @return
   */
  private AsItmRtlStrOracleBean getAsItmRtlStrObject(CMSItem object, String storeId) {
	  AsItmRtlStrOracleBean bean = new AsItmRtlStrOracleBean();
	  bean.setIdStrRt(storeId);
	  bean.setIdItm(object.getId());
	  bean.setScItmSls(null);
	  bean.setIdGpTx(null);
	  bean.setDcItmSls(null);
	  bean.setScItm(null);
	  bean.setRpPrSls(object.getRetailPrice());
	  bean.setFlMkdOrglPrcPr(null);
	  bean.setQuMkdPrPrcPr(null);
	  bean.setFlStkUpdtOnHd(null);
	  bean.setDcPrcEfPrnRt(object.getUpdateDate());
	  bean.setRpSlsCrt(null);
	  bean.setTyPrcRt(null);
	  bean.setFlPrcRtPntAlw(null);
	  bean.setDcPrcSlsEfCrt(null);
	  bean.setDcPrcSlsEpCrt(null);
	  bean.setRpPrcMfRcmRt(null);
	  bean.setDcPrcMfRcmRt(null);
	  bean.setRpPrcCmprAtSls(null);
	  bean.setCurrencyCode(object.getCurrencyCode());	  
	  bean.setVatRate(object.getVatRate());
	  bean.setUpdateDt(object.getUpdateDate());
	  bean.setNmItm(object.getDescription());
	  return bean;
  }
  
  /**
   * Constructs Arm_Itm_Hist object
   * @param object
   * @return
   */
  private ArmItmHistOracleBean getArmItmHistObject (CMSItem object, String storeId, String key, String value) {
	ArmItmHistOracleBean bean = new ArmItmHistOracleBean();
    bean.setIdItm(object.getId());
    bean.setIdStrRt(storeId);
    bean.setIdItmHist(null);
    bean.setKey(key);
    bean.setValue(value);
    bean.setEffectiveDt(object.getUpdateDate());
    bean.setExpirationDt(null);
    return bean;
  }

  /**
   * POSIdentity
   * @param object
   * @return
   */
  private IdIdnPsOracleBean toIdIdnPsBean (Item object, String posIdentityId, String currencyCode) {
    IdIdnPsOracleBean bean = new IdIdnPsOracleBean();
    bean.setIdItmPos(posIdentityId);
    bean.setIdItm(object.getId());
    bean.setCurrencyCode(currencyCode);
    bean.setLuGpTndRst(null);
    bean.setFlPntFqShprEl(null);
    bean.setIdMf(null);
    bean.setFlDscAfDscAlw(null);
    bean.setFlDscMrkBskAlw(null);
    bean.setFcFmyMf(null);
    bean.setFlDscCtAcntAlw(null);
    bean.setIdItmMfUpc(null);
    bean.setIdAgntRtn(null);
    bean.setDeItmPos(null);
    bean.setFlDscEmAlw(null);
    bean.setFlCpnAlwMulty(null);
    bean.setLuVlCpn(null);
    bean.setDtEndPsCpnOfr(null);
    bean.setFlFdStpAlw(null);
    bean.setQuUnBlkMnm(object.getDefaultQuantity().intValue());
    bean.setFlCpnEltnc(null);
    bean.setQuUnBlkMxm(null);
    bean.setFlCpnRst(null);
    /**
     * RMS supports three values for requiresManualUnitPrice.
     * R - required
     * O - optional
     * P - prohibited
     */
    if (object.requiresManualUnitPrice()) {
      bean.setCdEnrPrcRq("R");
    } 
    else if (object.isManualPriceEntryProhibited()) {
      bean.setCdEnrPrcRq("P");
    } 
    else {
      bean.setCdEnrPrcRq("O");
    }
    bean.setFlEnrWtRq(null);
    bean.setFlKyPrhQty(null);
    bean.setFlRtnPrh(null);
    bean.setFlItmGwy(null);
    bean.setFlItmWic(null);
    bean.setFlPrcVsVr(null);
    bean.setFlKyPrhRpt(null);
    bean.setQuPntFqShpr(null);
    bean.setRpSlsPosCrt(null);
    return  bean;
  }

  /**
   * RelatedItemAssociation
   * @param object
   * @return
   */
  private CoAsctnRltdItmOracleBean[] toCoAsctnRltdItmBeans (Item object) {
    ArrayList list = new ArrayList();
    for (Enumeration enm = object.getRelatedItems(); enm.hasMoreElements();) {
      RelatedItem relatedItem = (RelatedItem)enm.nextElement();
      CoAsctnRltdItmOracleBean bean = new CoAsctnRltdItmOracleBean();
      bean.setIdItm(object.getId());
      bean.setIdItmRltd(relatedItem.getRelatedItemId());
      bean.setNmAsctnRltdItm(null);
      bean.setNmAsctnRltdItm(null);
      bean.setNaAsctnRltdItm(relatedItem.getMessage());
      list.add(bean);
    }
    return  (CoAsctnRltdItmOracleBean[])list.toArray(new CoAsctnRltdItmOracleBean[list.size()]);
  }

  /**
   * Customer Type Special Price
   * @param object
   * @return
   */
  private LePrcSplCtTypOracleBean toLePrcSplCtTypOracleBean (Item object) {
    ArmCurrency loyaltyMarkdownAmount = object.getLoyaltyMarkdownAmount();
    if (loyaltyMarkdownAmount == null || loyaltyMarkdownAmount.doubleValue() == 0.0)
      return  null;
    LePrcSplCtTypOracleBean bean = new LePrcSplCtTypOracleBean();
    bean.setCurrencyCode(object.getRetailPrice().getCurrencyType().getCode());
    bean.setFlPrcSlsSplDsc(true);
    bean.setRpSlsCrt(object.getLoyaltyMarkdownAmount());
    bean.setIdItm(object.getId());
    bean.setIdGpId(ArtsConstants.CUSTOMER_GROUP_LOYALTY);
    return  bean;
  }

  /**
   * Item Promotion Table
   * @param object
   * @return
   */
  private RkPrmItmOracleBean[] toLRkPrmItmOracleBeans (Item object) {
    List list = new ArrayList();
    for (Iterator it = object.getPromotionIds(); it.hasNext();) {
      RkPrmItmOracleBean bean = new RkPrmItmOracleBean();
      bean.setCurrencyCode(object.getRetailPrice().getCurrencyType().getCode());
      bean.setIdItm(object.getId());
      bean.setIdRuPrdv((String)it.next());
      list.add(bean);
    }
    return  (RkPrmItmOracleBean[])list.toArray(new RkPrmItmOracleBean[0]);
  }

  /**
   * Package Promotion Components
   * @param object
   * @return
   */
  private RkCompPrmPkOracleBean[] toRkCompPrmPkOracleBeans (Item object) {
    List list = new ArrayList();
    for (Iterator it = object.getPromotionIds(); it.hasNext();) {
      String promotionId = (String)it.next();
      int[] comps = object.getPromotionComponents(promotionId);
      if (comps == null || comps.length == 0)
        continue;
      for (int i = 0; i < comps.length; i++) {
        RkCompPrmPkOracleBean bean = new RkCompPrmPkOracleBean();
        bean.setCompNum(comps[i]);
        bean.setCurrencyCode(object.getRetailPrice().getCurrencyType().getCode());
        bean.setIdItm(object.getId());
        bean.setIdRuPrdv(promotionId);
        list.add(bean);
      }
    }
    return  (RkCompPrmPkOracleBean[])list.toArray(new RkCompPrmPkOracleBean[0]);
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return
   */
  private CMSItem fromBeanToObject(BaseOracleBean baseBean) {
    AsItmRtlStrOracleBean bean = (AsItmRtlStrOracleBean)baseBean;
    CMSItem object = new CMSItem(bean.getIdItm());
    return object;
  }

  /**
   *
   * @param store Store
   * @throws SQLException
   * @return Map
   */
  public Map getSupplierSeasonYear (Store store) throws SQLException {
    return  itemVwDao.getSupplierSeasonYear(store);
  }

  /**
   *
   * @param searchString ItemSearchString
   * @throws SQLException
   * @return CMSItem[]
   */
  public CMSItem[] selectByItemSearchString (ItemSearchString searchString) throws SQLException {
    return  itemVwDao.selectByItemSearchString(searchString);
  }

  /**
	 * Added by Satin to search an itemId corresponding to entered barcode for exception item.
	 * @param barcode String
	 * @throws SQLException
	 * @return String
	 */
  public String selectItemIdFromAsItm(String barcode) throws SQLException {
	  
	  	String itemId = null;
	    BaseOracleBean[] beans = this.query(new AsItmOracleBean(), AsItmOracleBean.selectSql + whereSql, barcode);
		  if (beans != null && beans.length > 0) 
		     {itemId =  this.fromBeanToObject1(beans[0]);}
		  return itemId;
  }

  // Added by Satin for Exception item.
  private String fromBeanToObject1 (BaseOracleBean baseBean) {
	    AsItmOracleBean bean = (AsItmOracleBean)baseBean;
	    String object = new String(bean.getIdItm());
	    return  object;
	  }
 
  /**
	 * Added by Satin to select an item corresponding to entered itemId for exception item.
	 * @param itemId String
	 * @throws SQLException
	 * @return String
	 */
  public String selectItemFromAsItm(String itemId) throws SQLException {
	  
	  	String itemId1 = null;
	    BaseOracleBean[] beans = this.query(new AsItmOracleBean(), AsItmOracleBean.selectSql + whereSql1, itemId);
		  if (beans != null && beans.length > 0) 
		     {itemId1 =  this.fromBeanToObjectAsItm(beans[0]);}
		  return itemId1;
}
  
  private String fromBeanToObjectAsItm (BaseOracleBean baseBean) {
	    AsItmOracleBean bean = (AsItmOracleBean)baseBean;
	    String object = new String(bean.getIdItm());
	    return  object;
	  }

  /**
	 * Added by Satin.
	 * This method is used to insert a record into AS_ITM_RTL_STR for exception item.
	 * @param theAppMgr IRepositoryManager
	 * @param storeId String
	 * @param itemId String
	 * @param retailPrice ArmCurrency
	 * @param currencyCode String
	 * @param vatRate Double
	 * @param taxable String
	 * @return void
	 * @throws SQLException
	 */
    public void insertExceptionItemtoAsItmRtlStr(String storeId, String itemId, ArmCurrency retailPrice, String currencyCode, String itemDesc, Double vatRate, String taxable) throws SQLException{
	  ParametricStatement pStmt = new ParametricStatement(AsItmRtlStrOracleBean.insertSql, fromObjectToBean(storeId, itemId, retailPrice, currencyCode, itemDesc, vatRate, taxable).toList());
	  execute(pStmt);
  }
  
  private AsItmRtlStrOracleBean fromObjectToBean (String storeId, String itemId, ArmCurrency retailPrice, String currencyCode, String itemDesc, Double vatRate, String taxable) {
	  AsItmRtlStrOracleBean bean = new AsItmRtlStrOracleBean();
	  Date date = new Date();
	  
	  bean.setIdStrRt(storeId);
	  bean.setIdItm(itemId);
	  bean.setRpPrSls(retailPrice);
	  bean.setCurrencyCode(currencyCode);
	  bean.setNmItm(itemDesc);
	  bean.setVatRate(vatRate);
	  bean.setLuExmTx(taxable);
	  bean.setDcPrcEfPrnRt(date);
	  bean.setUpdateDt(date);
	  
	  return bean;
  }
  
}
