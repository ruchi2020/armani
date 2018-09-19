/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2003, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cr.database.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cr.pricing.*;
import  com.chelseasystems.cs.pricing.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cs.store.*;
import  com.chelseasystems.cr.currency.*;
import  java.sql.*;
import  java.util.*;


/**
 *
 *  CMSPromotion Data Access Object.<br>
 *  This object encapsulates all database access for CMSPromotion.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *  </table>
 *
 *  @see IPromotion
 *  @see CMSPromotion
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RuPrdvOracleBean
 *
 */
public class PromotionOracleDAO extends BaseOracleDAO
    implements PromotionDAO {

  /**
   * put your documentation comment here
   * @param promotion
   * @param currencyCode
   * @return 
   * @exception SQLException
   */
  ParametricStatement[] getInsertSQL (CMSPromotion promotion, String currencyCode) throws SQLException {
    String idRuPrdv = getNextChelseaId();
    List statements = new ArrayList();
    //    BaseOracleBean[] beans = this.query(new RuPrdvOracleBean(), where(RuPrdvOracleBean.COL_ID_RU_PRDV), promotion.getId());
    //    boolean promotionDoesNotExist = (beans == null || beans.length == 0);
    //    if (promotionDoesNotExist)
    {
      //PriceDerivationRule
      statements.add(new ParametricStatement(RuPrdvOracleBean.insertSql, this.toRuPrdvBean(promotion, idRuPrdv).toList()));
      //Promotion
      statements.add(new ParametricStatement(CoPrmOracleBean.insertSql, this.toCoPrmBean(promotion).toList()));
      //PromotionPriceDerivationRule
      statements.add(new ParametricStatement(RuPrmPrdvOracleBean.insertSql, this.toRuPrmPrdvBean(promotion, idRuPrdv).toList()));
    }
    //ItemPriceDerivationRule
    statements.add(new ParametricStatement(RuPrdvItmOracleBean.insertSql, this.toRuPrdvItmBean(promotion, currencyCode, idRuPrdv).toList()));
    if (promotion instanceof MultiunitPromotion) {
      //      if (promotionDoesNotExist)
      {
        statements.add(new ParametricStatement(RkPrmMuOracleBean.insertSql, this.toRkPrmMuBean((MultiunitPromotion)promotion, idRuPrdv).toList()));
      }
    } 
    else if (promotion instanceof ItemThresholdPromotion) {
      statements.add(new ParametricStatement(RkPrmItOracleBean.insertSql, this.toRkPrmItBean((ItemThresholdPromotion)promotion, idRuPrdv).toList()));
    } 
    else if (promotion instanceof BuyXGetYPromotion) {
    //nothing
    } 
    else if (promotion instanceof PackagePromotion) {
      PackagePromotion packagePromotion = (PackagePromotion)promotion;
      //      if (promotionDoesNotExist)
      {
        statements.add(new ParametricStatement(RkPrmPkOracleBean.insertSql, this.toRkPrmPkBean(packagePromotion, idRuPrdv).toList()));
      }
      if (packagePromotion.isReductionByPercentageOff()) {
        //        if (promotionDoesNotExist)
        {
          RkPrmPkPeOracleBean[] rkPrmPkPeOracleBeans = this.toRkPrmPkPeBeans(packagePromotion, idRuPrdv);
          for (int i = 0; i < rkPrmPkPeOracleBeans.length; i++)
            statements.add(new ParametricStatement(RkPrmPkPeOracleBean.insertSql, rkPrmPkPeOracleBeans[i].toList()));
        }
      } 
      else if (packagePromotion.isReductionByFixedUnitPrice() || packagePromotion.isReductionByUnitPriceOff()) {
        RkPrmPkAmtOracleBean[] rkPrmPkAmtOracleBeans = this.toRkPrmPkAmtBeans(packagePromotion, idRuPrdv);
        for (int i = 0; i < rkPrmPkAmtOracleBeans.length; i++)
          statements.add(new ParametricStatement(RkPrmPkAmtOracleBean.insertSql, rkPrmPkAmtOracleBeans[i].toList()));
      }
    } 
    else if (promotion instanceof MultipriceBreakPromotion) {
      RkPrmMbOracleBean[] rkPrmMbOracleBeans = this.toRkPrmMbBeans((MultipriceBreakPromotion)promotion, idRuPrdv);
      for (int i = 0; i < rkPrmMbOracleBeans.length; i++)
        statements.add(new ParametricStatement(RkPrmMbOracleBean.insertSql, rkPrmMbOracleBeans[i].toList()));
    }
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (IPromotion object) throws SQLException {
    try {
      CMSPromotion promotion = (CMSPromotion)object;
      if (promotion.getReductionAmount() != null) {
        this.insert(promotion, promotion.getReductionAmount().getCurrencyType().getCode());
      } 
      else {
        this.insert(promotion, new ArmCurrency(0.0d).getCurrencyType().getCode());
      }
    } catch (ClassCastException ex) {
      throw  new SQLException(ex.getMessage());
    } catch (NullPointerException ex) {
      throw  new SQLException(ex.getMessage());
    }
  }

  /**
   * put your documentation comment here
   * @param object
   * @param currencyCode
   * @exception SQLException
   */
  public void insert (IPromotion object, String currencyCode) throws SQLException {
    try {
      this.execute(getInsertSQL((CMSPromotion)object, currencyCode));
    } catch (ClassCastException ex) {
      throw  new SQLException(ex.getMessage());
    }
  }

  /**
   * put your documentation comment here
   * @param store
   * @return 
   * @exception SQLException
   */
  public IPromotion[] selectByStore (Store store) throws SQLException {
    String currencyCode = store.getCurrencyType().getCode();
    //this statement gets the Promotion IDs
    String[] ids = selectIdsByStore(store);
    if (ids == null || ids.length == 0)
      return  new IPromotion[0];
    IPromotion[] array = new IPromotion[ids.length];
    for (int i = 0; i < array.length; i++)
      array[i] = selectById(ids[i], currencyCode, store.getId());
    return  array;
  }

  /**
   * put your documentation comment here
   * @param store
   * @return 
   * @exception SQLException
   */
  public String[] selectIdsByStore (Store store) throws SQLException {
    String currencyCode = store.getCurrencyType().getCode();
    BaseOracleBean[] beans = this.query(new CoPrmOracleBean(), where(CoPrmOracleBean.COL_ID_STR_RT), store.getId());
    if (beans == null || beans.length == 0)
      return  new String[0];
    String[] ids = new String[beans.length];
    for (int i = 0; i < beans.length; i++)
      ids[i] = ((CoPrmOracleBean)beans[i]).getIdPrm();
    return  ids;
  }

  /**
   * put your documentation comment here
   * @param id
   * @param currencyCode
   * @param store
   * @return 
   * @exception SQLException
   */
  public IPromotion selectById (String id, String currencyCode, String store) throws SQLException {
    CMSPromotion promotion = null;
    BaseOracleBean[] beans = null;
    List params = new ArrayList();
    params.add(id);
    params.add(store);
    beans = this.query(new CoPrmOracleBean(), where(CoPrmOracleBean.COL_ID_PRM, CoPrmOracleBean.COL_ID_STR_RT), params);
    if (beans == null || beans.length == 0)
      return  null;
    CoPrmOracleBean coPrmOracleBean = (CoPrmOracleBean)beans[0];
    params = new ArrayList();
    params.add(id);
    params.add(store);
    beans = this.query(new RuPrmPrdvOracleBean(), where(RuPrmPrdvOracleBean.COL_ID_PRM, RuPrmPrdvOracleBean.COL_ID_STR_RT), params);
    if (beans == null || beans.length == 0)
      return  null;
    RuPrmPrdvOracleBean ruPrmPrdvOracleBean = (RuPrmPrdvOracleBean)beans[0];
    String ruPrdvId = ruPrmPrdvOracleBean.getIdRuPrdv();
    params = new ArrayList();
    params.add(ruPrdvId);
    params.add(currencyCode);
    beans = this.query(new RuPrdvItmOracleBean(), where(RuPrdvItmOracleBean.COL_ID_RU_PRDV, RuPrdvItmOracleBean.COL_CURRENCY_CODE), params);
    RuPrdvItmOracleBean ruPrdvItmOracleBean = (RuPrdvItmOracleBean)beans[0];
    String methodOfReduction = ruPrdvItmOracleBean.getCdRdnMth();
    ArmCurrency reductionAmount = ruPrdvItmOracleBean.getMoRdn();
    Double reductionPrecent = ruPrdvItmOracleBean.getPeRdn();
    ArmCurrency newPriceAmount = ruPrdvItmOracleBean.getMoPrc();
    beans = this.query(new RuPrdvOracleBean(), where(RuPrdvOracleBean.COL_ID_RU_PRDV), ruPrdvId);
    if (beans == null || beans.length == 0)
      return  null;
    RuPrdvOracleBean ruPrdvOracleBean = (RuPrdvOracleBean)beans[0];
    if (ruPrdvOracleBean.getTyRuPrdv() != null) {
      if (ruPrdvOracleBean.getTyRuPrdv().equals("MU"))
        promotion = new MultiunitPromotion(id); 
      else if (ruPrdvOracleBean.getTyRuPrdv().equals("IT"))
        promotion = new ItemThresholdPromotion(id); 
      else if (ruPrdvOracleBean.getTyRuPrdv().equals("XY"))
        promotion = new BuyXGetYPromotion(id); 
      else if (ruPrdvOracleBean.getTyRuPrdv().equals("PK"))
        promotion = new PackagePromotion(id); 
      else if (ruPrdvOracleBean.getTyRuPrdv().equals("MB"))
        promotion = new MultipriceBreakPromotion(id);
      promotion.doSetBeginTime(coPrmOracleBean.getDcPrmEf());
      promotion.doSetEndTime(coPrmOracleBean.getDcPrmEp());
      promotion.doSetIdStrRt(coPrmOracleBean.getIdStrRt());
      //      promotion.doSetApplyTo(ruPrdvItmOracleBean.getApplyTo());
      promotion.doSetRuleDrvId(ruPrdvId);
      promotion.doSetDescription(ruPrdvOracleBean.getDeRuPrdv());
      if (promotion instanceof MultiunitPromotion)
        this.setPromotionAttributes((MultiunitPromotion)promotion, methodOfReduction, reductionAmount, reductionPrecent, newPriceAmount, currencyCode, ruPrdvId); 
      else if (promotion instanceof ItemThresholdPromotion)
        this.setPromotionAttributes((ItemThresholdPromotion)promotion, methodOfReduction, reductionAmount, reductionPrecent, newPriceAmount, currencyCode, ruPrdvId); 
      else if (promotion instanceof BuyXGetYPromotion)
        this.setPromotionAttributes((BuyXGetYPromotion)promotion, methodOfReduction, reductionAmount, reductionPrecent, newPriceAmount, currencyCode, ruPrdvId); 
      else if (promotion instanceof PackagePromotion)
        this.setPromotionAttributes((PackagePromotion)promotion, methodOfReduction, reductionAmount, reductionPrecent, newPriceAmount, currencyCode, ruPrdvId); 
      else if (promotion instanceof MultipriceBreakPromotion)
        this.setPromotionAttributes((MultipriceBreakPromotion)promotion, currencyCode, ruPrdvId);
    }
    return  promotion;
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @param methodOfReduction
   * @param reductionAmount
   * @param reductionPrecent
   * @param newPriceAmount
   * @param currencyCode
   * @param idRuPrdv
   * @exception SQLException
   */
  private void setPromotionAttributes (MultiunitPromotion promotion, String methodOfReduction, ArmCurrency reductionAmount, Double reductionPrecent, ArmCurrency newPriceAmount, String currencyCode, String idRuPrdv) throws SQLException {
    if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_FIXED_TOTAL_PRICE))
      promotion.doSetReductionByFixedTotalPrice(newPriceAmount); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_FIXED_UNIT_PRICE))
      promotion.doSetReductionByFixedUnitPrice(newPriceAmount); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_TOTAL_PRICE_OFF))
      promotion.doSetReductionByTotalPriceOff(reductionAmount); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_UNIT_PRICE_OFF))
      promotion.doSetReductionByUnitPriceOff(reductionAmount); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_PERCENTAGE_OFF))
      promotion.doSetReductionByPercentageOff(reductionPrecent.doubleValue());
    //
    BaseOracleBean[] beans = this.query(new RkPrmMuOracleBean(), where(RkPrmMuOracleBean.COL_ID_RU_PRDV), idRuPrdv);
    if (beans == null || beans.length == 0)
      return;
    RkPrmMuOracleBean rkPrmMuOracleBean = (RkPrmMuOracleBean)beans[0];
    promotion.doSetQuantityBreak(rkPrmMuOracleBean.getQtyBreak().intValue());
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @param methodOfReduction
   * @param reductionAmount
   * @param reductionPrecent
   * @param newPriceAmount
   * @param currencyCode
   * @param idRuPrdv
   * @exception SQLException
   */
  private void setPromotionAttributes (ItemThresholdPromotion promotion, String methodOfReduction, ArmCurrency reductionAmount, Double reductionPrecent, ArmCurrency newPriceAmount, String currencyCode, String idRuPrdv) throws SQLException {
    if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_FIXED_UNIT_PRICE))
      promotion.doSetReductionByFixedUnitPrice(newPriceAmount); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_UNIT_PRICE_OFF))
      promotion.doSetReductionByUnitPriceOff(reductionAmount); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_PERCENTAGE_OFF))
      promotion.doSetReductionByPercentageOff(reductionPrecent.doubleValue());
    //
    ArrayList params = new ArrayList();
    params.add(idRuPrdv);
    params.add(currencyCode);
    BaseOracleBean[] beans = this.query(new RkPrmItOracleBean(), where(RkPrmItOracleBean.COL_ID_RU_PRDV, RkPrmItOracleBean.COL_CURRENCY_CODE), params);
    if (beans == null || beans.length == 0)
      return;
    RkPrmItOracleBean rkPrmItOracleBean = (RkPrmItOracleBean)beans[0];
    promotion.doSetTriggerAmount(rkPrmItOracleBean.getTriggerAmt());
    if (rkPrmItOracleBean != null) {
      if (rkPrmItOracleBean.getTriggerQty() != null) {
        promotion.doSetTriggerQuantity(rkPrmItOracleBean.getTriggerQty().intValue());
      }
    } 
    else {
      promotion.doSetTriggerQuantity(new Integer("0").intValue());
    }
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @param methodOfReduction
   * @param reductionAmount
   * @param reductionPrecent
   * @param newPriceAmount
   * @param currencyCode
   * @param idRuPrdv
   * @exception SQLException
   */
  private void setPromotionAttributes (BuyXGetYPromotion promotion, String methodOfReduction, ArmCurrency reductionAmount, Double reductionPrecent, ArmCurrency newPriceAmount, String currencyCode, String idRuPrdv) throws SQLException {
    if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_FIXED_UNIT_PRICE))
      promotion.doSetReductionByFixedUnitPrice(newPriceAmount); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_UNIT_PRICE_OFF))
      promotion.doSetReductionByUnitPriceOff(reductionAmount); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_PERCENTAGE_OFF))
      promotion.doSetReductionByPercentageOff(reductionPrecent.doubleValue());
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @param methodOfReduction
   * @param reductionAmount
   * @param reductionPrecent
   * @param newPriceAmount
   * @param currencyCode
   * @param idRuPrdv
   * @exception SQLException
   */
  private void setPromotionAttributes (PackagePromotion promotion, String methodOfReduction, ArmCurrency reductionAmount, Double reductionPrecent, ArmCurrency newPriceAmount, String currencyCode, String idRuPrdv) throws SQLException {
    BaseOracleBean[] beans = this.query(new RkPrmPkOracleBean(), where(RkPrmPkOracleBean.COL_ID_RU_PRDV), promotion.getId());
    if (beans == null || beans.length == 0)
      return;
    RkPrmPkOracleBean rkPrmPkOracleBean = (RkPrmPkOracleBean)beans[0];
    int numberOfComponents = rkPrmPkOracleBean.getCompCounts().intValue();
    double[] componentReductionPercents = null;
    ArmCurrency[] componentReductionAmounts = null;
    if (rkPrmPkOracleBean.getIsPercent().booleanValue()) {
      BaseOracleBean[] rkPrmPkPeOracleBeans = this.query(new RkPrmPkPeOracleBean(), where(RkPrmPkPeOracleBean.COL_ID_RU_PRDV), idRuPrdv);
      componentReductionPercents = new double[rkPrmPkPeOracleBeans.length];
      for (int i = 0; i < rkPrmPkPeOracleBeans.length; i++) {
        RkPrmPkPeOracleBean rkPrmPkPeOracleBean = (RkPrmPkPeOracleBean)rkPrmPkPeOracleBeans[i];
        componentReductionPercents[rkPrmPkPeOracleBean.getSeqNum().intValue()] = rkPrmPkPeOracleBean.getPercent().doubleValue();
      }
    } 
    else {
      List params = new ArrayList();
      params.add(idRuPrdv);
      params.add(currencyCode);
      BaseOracleBean[] rkPrmPkAmtOracleBeans = this.query(new RkPrmPkAmtOracleBean(), where(RkPrmPkAmtOracleBean.COL_ID_RU_PRDV, RkPrmPkAmtOracleBean.COL_CURRENCY_CODE), params);
      componentReductionAmounts = new ArmCurrency[rkPrmPkAmtOracleBeans.length];
      for (int i = 0; i < rkPrmPkAmtOracleBeans.length; i++) {
        RkPrmPkAmtOracleBean rkPrmPkAmtOracleBean = (RkPrmPkAmtOracleBean)rkPrmPkAmtOracleBeans[i];
        componentReductionAmounts[rkPrmPkAmtOracleBean.getSeqNum().intValue()] = rkPrmPkAmtOracleBean.getAmount();
      }
    }
    if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_FIXED_TOTAL_PRICE))
      promotion.doSetReductionByFixedTotalPrice(newPriceAmount, numberOfComponents); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_FIXED_UNIT_PRICE))
      promotion.doSetReductionByFixedUnitPrice(componentReductionAmounts); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_TOTAL_PRICE_OFF))
      promotion.doSetReductionByTotalPriceOff(reductionAmount, numberOfComponents); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_UNIT_PRICE_OFF))
      promotion.doSetReductionByUnitPriceOff(componentReductionAmounts); 
    else if (methodOfReduction.equals(CMSPromotion.REDUCTION_BY_PERCENTAGE_OFF))
      promotion.doSetReductionByPercentageOff(componentReductionPercents);
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @param currencyCode
   * @param idRuPrdv
   * @exception SQLException
   */
  private void setPromotionAttributes (MultipriceBreakPromotion promotion, String currencyCode, String idRuPrdv) throws SQLException {
    ArrayList params = new ArrayList();
    params.add(idRuPrdv);
    params.add(currencyCode);
    BaseOracleBean[] beans = this.query(new RkPrmMbOracleBean(), where(RkPrmMbOracleBean.COL_ID_RU_PRDV, RkPrmMbOracleBean.COL_CURRENCY_CODE), params);
    ArmCurrency[] amounts = new ArmCurrency[beans.length];
    for (int i = 0; i < beans.length; i++) {
      RkPrmMbOracleBean rkPrmMbOracleBean = (RkPrmMbOracleBean)beans[i];
      amounts[rkPrmMbOracleBean.getSeqNum().intValue()] = rkPrmMbOracleBean.getAmount();
    }
    promotion.doSetPriceBreaks(amounts);
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @param idRuPrdv
   * @return 
   */
  private RuPrdvOracleBean toRuPrdvBean (CMSPromotion promotion, String idRuPrdv) {
    RuPrdvOracleBean bean = new RuPrdvOracleBean();
    bean.setIdRuPrdv(idRuPrdv);
    bean.setDeRuPrdv(promotion.getDescription());
    bean.setNmRuPrdv(promotion.getPromotionName());
    bean.setTyRuPrdv(toType(promotion));
    bean.setIsDiscount(new Boolean(promotion.getIsDiscount()));
    bean.setApplicableItm(promotion.getApplicableItm());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @param idRuPrdv
   * @return 
   */
  private RuPrmPrdvOracleBean toRuPrmPrdvBean (CMSPromotion promotion, String idRuPrdv) {
    RuPrmPrdvOracleBean bean = new RuPrmPrdvOracleBean();
    bean.setIdPrm(promotion.getId());
    bean.setIdRuPrdv(idRuPrdv);
    bean.setIdStrRt(promotion.getIdStrRt());
    bean.setIdGpTm(null);
    //bean.setPromotionNo(promotion.getPromotionNum());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @return 
   */
  private CoPrmOracleBean toCoPrmBean (CMSPromotion promotion) {
    CoPrmOracleBean bean = new CoPrmOracleBean();
    bean.setIdPrm(promotion.getId());
    bean.setIdStrRt(promotion.getIdStrRt());
    bean.setDcPrmEf(promotion.getBeginTime());
    bean.setDcPrmEp(promotion.getEndTime());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @param currencyCode
   * @param idRuPrdv
   * @return 
   * @exception SQLException
   */
  private RuPrdvItmOracleBean toRuPrdvItmBean (CMSPromotion promotion, String currencyCode, String idRuPrdv) throws SQLException {
    RuPrdvItmOracleBean bean = new RuPrdvItmOracleBean();
    bean.setIdRuPrdv(idRuPrdv);
    bean.setCurrencyCode(currencyCode);
    bean.setCdRdnMth(promotion.doGetMethodOfReduction());
    //bean.setApplyTo(promotion.getApplyTo());
    if (promotion.isReductionByFixedTotalPrice())
      bean.setMoPrc(promotion.getReductionAmount()); 
    else if (promotion.isReductionByFixedUnitPrice())
      bean.setMoPrc(promotion.getReductionAmount()); 
    else if (promotion.isReductionByPercentageOff())
      bean.setPeRdn(promotion.getReductionPercent()); 
    else if (promotion.isReductionByTotalPriceOff())
      bean.setMoRdn(promotion.getReductionAmount()); 
    else if (promotion.isReductionByUnitPriceOff())
      bean.setMoRdn(promotion.getReductionAmount());
    return  bean;
  }

  //MultiunitPromotion bean
  private RkPrmMuOracleBean toRkPrmMuBean (MultiunitPromotion multiunitPromotion, String idRuPrdv) {
    RkPrmMuOracleBean bean = new RkPrmMuOracleBean();
    bean.setIdRuPrdv(idRuPrdv);
    bean.setQtyBreak(multiunitPromotion.getQuantityBreak());
    return  bean;
  }

  //ItemThresholdPromotion bean
  private RkPrmItOracleBean toRkPrmItBean (ItemThresholdPromotion itemThresholdPromotion, String idRuPrdv) {
    RkPrmItOracleBean bean = new RkPrmItOracleBean();
    bean.setIdRuPrdv(idRuPrdv);
    bean.setCurrencyCode(itemThresholdPromotion.getTriggerAmount().getCurrencyType().getCode());
    bean.setTriggerQty(itemThresholdPromotion.getTriggerQuantity());
    bean.setTriggerAmt(itemThresholdPromotion.getTriggerAmount());
    return  bean;
  }

  //PackagePromotion bean
  private RkPrmPkOracleBean toRkPrmPkBean (PackagePromotion packagePromotion, String idRuPrdv) {
    RkPrmPkOracleBean bean = new RkPrmPkOracleBean();
    bean.setIdRuPrdv(idRuPrdv);
    bean.setCompCounts(packagePromotion.getNumberOfComponents());
    bean.setIsPercent(packagePromotion.isReductionByPercentageOff());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param packagePromotion
   * @param idRuPrdv
   * @return 
   */
  private RkPrmPkPeOracleBean[] toRkPrmPkPeBeans (PackagePromotion packagePromotion, String idRuPrdv) {
    if (!(packagePromotion.isReductionByPercentageOff()))
      return  new RkPrmPkPeOracleBean[0];
    RkPrmPkPeOracleBean[] array = new RkPrmPkPeOracleBean[packagePromotion.getNumberOfComponents()];
    double[] percents = packagePromotion.getComponentReductionPercents();
    for (int i = 0; i < array.length; i++) {
      array[i] = new RkPrmPkPeOracleBean();
      array[i].setIdRuPrdv(idRuPrdv);
      array[i].setSeqNum(i);
      array[i].setPercent(percents[i]);
    }
    return  array;
  }

  /**
   * put your documentation comment here
   * @param packagePromotion
   * @param idRuPrdv
   * @return 
   */
  private RkPrmPkAmtOracleBean[] toRkPrmPkAmtBeans (PackagePromotion packagePromotion, String idRuPrdv) {
    if (!(packagePromotion.isReductionByFixedUnitPrice() || packagePromotion.isReductionByUnitPriceOff()))
      return  new RkPrmPkAmtOracleBean[0];
    RkPrmPkAmtOracleBean[] array = new RkPrmPkAmtOracleBean[packagePromotion.getNumberOfComponents()];
    ArmCurrency[] amounts = packagePromotion.getComponentReductionAmounts();
    for (int i = 0; i < array.length; i++) {
      array[i] = new RkPrmPkAmtOracleBean();
      array[i].setIdRuPrdv(idRuPrdv);
      array[i].setCurrencyCode(amounts[i].getCurrencyType().getCode());
      array[i].setSeqNum(i);
      array[i].setAmount(amounts[i]);
    }
    return  array;
  }

  //MultipriceBreakPromotion bean
  private RkPrmMbOracleBean[] toRkPrmMbBeans (MultipriceBreakPromotion multipriceBreakPromotion, String idRuPrdv) {
    ArmCurrency[] amounts = multipriceBreakPromotion.getPriceBreaks();
    if (amounts == null)
      return  new RkPrmMbOracleBean[0];
    RkPrmMbOracleBean[] array = new RkPrmMbOracleBean[amounts.length];
    for (int i = 0; i < array.length; i++) {
      array[i] = new RkPrmMbOracleBean();
      array[i].setIdRuPrdv(idRuPrdv);
      array[i].setCurrencyCode(amounts[i].getCurrencyType().getCode());
      array[i].setSeqNum(i);
      array[i].setAmount(amounts[i]);
    }
    return  array;
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @return 
   */
  private String toType (CMSPromotion promotion) {
    if (promotion instanceof MultiunitPromotion)
      return  "MU";
    if (promotion instanceof ItemThresholdPromotion)
      return  "IT";
    if (promotion instanceof BuyXGetYPromotion)
      return  "XY";
    if (promotion instanceof PackagePromotion)
      return  "PK";
    if (promotion instanceof MultipriceBreakPromotion)
      return  "MB";
    return  "??";
  }
}



