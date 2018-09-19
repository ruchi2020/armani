/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.item.ItemStock;
import java.util.Date;
import java.sql.*;
import java.util.*;


/**
 * put your documentation comment here
 */
public class ArmItmSohOracleDAO extends BaseOracleDAO implements ArmItmSohDAO {
  private static String selectSql = ArmItmSohOracleBean.selectSql;
  private static String insertSql = ArmItmSohOracleBean.insertSql;
  private static String updateSql = ArmItmSohOracleBean.updateSql;

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert(ItemStock object)
      throws SQLException {
    this.execute(this.getInsertSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL(ItemStock object)
      throws SQLException {
    List statements = new ArrayList();
    statements.add(new ParametricStatement(insertSql, this.fromObjectToBean(object).toList()));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void update(ItemStock object)
      throws SQLException {
    this.execute(this.getUpdateSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQL(ItemStock object)
      throws SQLException {
    List statements = new ArrayList();
    String sql = selectSql + " where " + ArmItmSohOracleBean.COL_STORE_ID + " = '"
        + object.getStoreId() + "' and " + ArmItmSohOracleBean.COL_ITEM_ID + " = '"
        + object.getItemId() + "' for update";
    String updtSql = "update " + ArmItmSohOracleBean.TABLE_NAME + " set "
        + ArmItmSohOracleBean.COL_QU_STORE_AVAILABLE + " = " + object.getAvailableStoreQty() + ", "
        + ArmItmSohOracleBean.COL_QU_STORE_UNAVAILABLE + " = " + object.getUnAvailableStoreQty()
        + " where " + ArmItmSohOracleBean.COL_ITEM_ID
        + " = '" + object.getItemId() + "' and " + ArmItmSohOracleBean.COL_STORE_ID + " = '"
        + object.getStoreId() + "'";
    // need to lock the row for update
    statements.add(new ParametricStatement(sql, new ArrayList()));
    statements.add(new ParametricStatement(updtSql, new ArrayList()));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param itemId
   * @param storeId
   * @return
   * @exception SQLException
   */
  public ItemStock selectById(String itemId, String storeId)
      throws SQLException {
    List params = new ArrayList();
    params.add(itemId);
    params.add(storeId);
    BaseOracleBean[] beans = this.query(new ArmItmSohOracleBean()
        , BaseOracleDAO.where(ArmItmSohOracleBean.COL_ITEM_ID, ArmItmSohOracleBean.COL_STORE_ID)
        , params);
    if (beans != null && beans.length > 0) {
      return this.fromBeanToObject(beans[0]);
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param pTxn
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] createForPaymentTransaction(PaymentTransaction pTxn)
      throws SQLException {
    List statements = new ArrayList();
    boolean isVoidTxn = false;
    if (pTxn instanceof CompositePOSTransaction) {
      return processTxn((CMSCompositePOSTransaction)pTxn, isVoidTxn);
    }
    if (pTxn instanceof VoidTransaction) {
      if (((VoidTransaction)pTxn).getOriginalTransaction() instanceof CompositePOSTransaction) {
        CMSCompositePOSTransaction cPOSTxn = (CMSCompositePOSTransaction)((VoidTransaction)pTxn).
            getOriginalTransaction();
        isVoidTxn = true;
        return processTxn(cPOSTxn, isVoidTxn);
      }
    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param cPOSTxn
   * @param isVoidTxn
   * @return
   * @exception SQLException
   */
  private ParametricStatement[] processTxn(CMSCompositePOSTransaction cPOSTxn, boolean isVoidTxn)
      throws SQLException {
    List statements = new ArrayList();
    POSLineItemGrouping lnItmGrp = null;
    POSLineItemGrouping[] lnItmGrpArr = null;
    String storeId = "", itemId = "";
    int currItemQty = 0, itemQty = 0;
    HashMap itemMap = null;
    ItemStock itemStock = null;
    //if (pTxn instanceof CompositePOSTransaction) {
    storeId = cPOSTxn.getStore().getId();
    itemMap = new HashMap();
    // check for Sale..
    lnItmGrpArr = cPOSTxn.getSaleLineItemGroupingsArray();
    currItemQty = 0;
    if (lnItmGrpArr != null && lnItmGrpArr.length > 0) {
      for (int i = 0; i < lnItmGrpArr.length; i++) {
        lnItmGrp = lnItmGrpArr[i];
        itemId = lnItmGrp.getItem().getId();
        itemQty = lnItmGrp.getQuantity();
        POSLineItem[] lnItmArr = lnItmGrp.getLineItemsArray();
        // Check if the given sale is not a Presale or Consgn
        if (!isVoidTxn) {
          if (((CMSSaleLineItem)lnItmArr[0]).getPresaleLineItem() == null
              && ((CMSSaleLineItem)lnItmArr[0]).getConsignmentLineItem() == null
              && (lnItmArr[0] != null && lnItmArr[0].getMiscItemId() == null)
              && ((CMSSaleLineItem)lnItmArr[0]).getReservationLineItem() == null) {
            if (itemMap.containsKey(itemId)) {
              currItemQty = ((Integer)itemMap.get(itemId)).intValue();
              itemMap.put(itemId, new Integer(currItemQty - itemQty));
            } else {
              itemMap.put(itemId, new Integer(currItemQty - itemQty));
            }
          }
        } else { // void txn
          if (lnItmArr[0] != null && lnItmArr[0].getMiscItemId() == null) {
            if (itemMap.containsKey(itemId)) {
              currItemQty = ((Integer)itemMap.get(itemId)).intValue();
              itemMap.put(itemId, new Integer(currItemQty + itemQty));
            } else {
              itemMap.put(itemId, new Integer(currItemQty + itemQty));
            }
          }
        }
      }
    }
    // check for Consignment ..
    lnItmGrpArr = cPOSTxn.getConsignmentLineItemGroupingsArray();
    currItemQty = 0;
    if (lnItmGrpArr != null && lnItmGrpArr.length > 0) {
      for (int i = 0; i < lnItmGrpArr.length; i++) {
        lnItmGrp = lnItmGrpArr[i];
        itemId = lnItmGrp.getItem().getId();
        itemQty = lnItmGrp.getQuantity();
        POSLineItem[] lnItmArr = lnItmGrp.getLineItemsArray();
        if (lnItmArr[0] != null && lnItmArr[0].getMiscItemId() == null) {
          if (itemMap.containsKey(itemId)) {
            currItemQty = ((Integer)itemMap.get(itemId)).intValue();
            if (isVoidTxn)
              itemMap.put(itemId, new Integer(currItemQty + itemQty));
            else
              itemMap.put(itemId, new Integer(currItemQty - itemQty));
          } else {
            if (isVoidTxn)
              itemMap.put(itemId, new Integer(currItemQty + itemQty));
            else
              itemMap.put(itemId, new Integer(currItemQty - itemQty));
          }
        }
      }
    }
    // check for Presale ..
    lnItmGrpArr = cPOSTxn.getPresaleLineItemGroupingsArray();
    currItemQty = 0;
    if (lnItmGrpArr != null && lnItmGrpArr.length > 0) {
      for (int i = 0; i < lnItmGrpArr.length; i++) {
        lnItmGrp = lnItmGrpArr[i];
        itemId = lnItmGrp.getItem().getId();
        itemQty = lnItmGrp.getQuantity();
        POSLineItem[] lnItmArr = lnItmGrp.getLineItemsArray();
        if (lnItmArr[0] != null && lnItmArr[0].getMiscItemId() == null) {
          if (itemMap.containsKey(itemId)) {
            currItemQty = ((Integer)itemMap.get(itemId)).intValue();
            if (isVoidTxn)
              itemMap.put(itemId, new Integer(currItemQty + itemQty));
            else
              itemMap.put(itemId, new Integer(currItemQty - itemQty));
          } else {
            if (isVoidTxn)
              itemMap.put(itemId, new Integer(currItemQty + itemQty));
            else
              itemMap.put(itemId, new Integer(currItemQty - itemQty));
          }
        }
      }
    }
    // check for Return ..
    lnItmGrpArr = cPOSTxn.getReturnLineItemGroupingsArray();
    currItemQty = 0;
    if (lnItmGrpArr != null && lnItmGrpArr.length > 0) {
      for (int i = 0; i < lnItmGrpArr.length; i++) {
        lnItmGrp = lnItmGrpArr[i];
        itemId = lnItmGrp.getItem().getId();
        itemQty = lnItmGrp.getQuantity();
        POSLineItem[] lnItmArr = lnItmGrp.getLineItemsArray();
        if (lnItmArr[0] != null && lnItmArr[0].getMiscItemId() == null) {
          if (itemMap.containsKey(itemId)) {
            currItemQty = ((Integer)itemMap.get(itemId)).intValue();
            if (isVoidTxn)
              itemMap.put(itemId, new Integer(currItemQty - itemQty));
            else
              itemMap.put(itemId, new Integer(currItemQty + itemQty));
          } else {
            if (isVoidTxn)
              itemMap.put(itemId, new Integer(currItemQty - itemQty));
            else
              itemMap.put(itemId, new Integer(currItemQty + itemQty));
          }
        }
      }
    }
    // check for Reservation ..
    if (cPOSTxn.getNewReservationOpenTxn() != null)
      lnItmGrpArr = cPOSTxn.getNewReservationOpenTxn().getReservationLineItemGroupingsArray();
    else
      lnItmGrpArr = cPOSTxn.getReservationLineItemGroupingsArray();
    currItemQty = 0;
    if (lnItmGrpArr != null && lnItmGrpArr.length > 0) {
      for (int i = 0; i < lnItmGrpArr.length; i++) {
        lnItmGrp = lnItmGrpArr[i];
        itemId = lnItmGrp.getItem().getId();
        itemQty = lnItmGrp.getQuantity();
        POSLineItem[] lnItmArr = lnItmGrp.getLineItemsArray();
        if (lnItmArr[0] != null && lnItmArr[0].getMiscItemId() == null) {
          if (itemMap.containsKey(itemId)) {
            currItemQty = ((Integer)itemMap.get(itemId)).intValue();
            if (isVoidTxn)
              itemMap.put(itemId, new Integer(currItemQty + itemQty));
            else
              itemMap.put(itemId, new Integer(currItemQty - itemQty));
          } else {
            if (isVoidTxn)
              itemMap.put(itemId, new Integer(currItemQty + itemQty));
            else
              itemMap.put(itemId, new Integer(currItemQty - itemQty));
          }
        }
      }
    }
    Iterator itr = itemMap.keySet().iterator();
    while (itr.hasNext()) {
      itemId = (String)itr.next();
      currItemQty = ((Integer)itemMap.get(itemId)).intValue();
      itemStock = this.selectById(itemId, storeId);
      if (itemStock != null) { // item exists update
        itemQty = itemStock.getAvailableStoreQty();
        itemStock.setAvailableStoreQty(itemQty + currItemQty);
        statements.addAll(Arrays.asList(this.getUpdateSQL(itemStock)));
      } else { // insert
        itemStock = new ItemStock();
        itemStock.setItemId(itemId);
        itemStock.setStoreId(storeId);
        itemStock.setAvailableStoreQty(currItemQty);
        statements.addAll(Arrays.asList(this.getInsertSQL(itemStock)));
      }
    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return
   */
  private ItemStock fromBeanToObject(BaseOracleBean baseBean) {
    ArmItmSohOracleBean bean = (ArmItmSohOracleBean)baseBean;
    ItemStock object = new ItemStock();
    object.setItemId(bean.getItemId());
    object.setStoreId(bean.getStoreId());
    object.setAvailableStoreQty(bean.getQuStoreAvailable().intValue());
    object.setUnAvailableStoreQty(bean.getQuStoreUnavailable().intValue());
    return object;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private ArmItmSohOracleBean fromObjectToBean(ItemStock object) {
    ArmItmSohOracleBean bean = new ArmItmSohOracleBean();
    bean.setItemId(object.getItemId());
    bean.setStoreId(object.getStoreId());
    bean.setQuStoreAvailable(object.getAvailableStoreQty());
    bean.setQuStoreUnavailable(object.getUnAvailableStoreQty());
    return bean;
  }
  /*
   // for testing only
   public static void main(String[] args) {
   try {
   ArmItmSohOracleDAO dao = new ArmItmSohOracleDAO();
   ItemStock is = new ItemStock();
   //      is.setStoreId("051010");
   //      is.setItemId("19374450011");
   //      is.setAvailableStoreQty( -10);
   //dao.insert(is);
   is = dao.selectById("19374450011", "051010");
   is.setAvailableStoreQty(is.getAvailableStoreQty() + 15);
   dao.update(is);
   is = dao.selectById("19374450011", "051010");
   System.out.println("---------------------------------");
   System.out.println(is.getStoreId());
   System.out.println(is.getItemId());
   System.out.println(is.getAvailableStoreQty());
   } catch (Exception e) {
   e.printStackTrace();
   }
   }*/
}

