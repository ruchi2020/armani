// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 9/13/2006 12:31:49 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RedeemableBuybackOracleDAO.java

package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.pos.RedeemableBuyBackTransaction;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.dataaccess.RedeemableBuybackDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPayTrnOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkRedmBybkOracleBean;
import com.chelseasystems.cs.pos.CMSRedeemableBuyBackTransaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.chelseasystems.cs.dataaccess.artsoracle.dao:
//            BaseOracleDAO, RedeemableIssueOracleDAO

public class RedeemableBuybackOracleDAO extends BaseOracleDAO
    implements RedeemableBuybackDAO {

    public RedeemableBuybackOracleDAO() {}

    ParametricStatement getInsertSQL(RedeemableBuyBackTransaction object)
        throws SQLException {
        return new ParametricStatement(insertSql, fromObjectToBean(object).toList());
    }

    ParametricStatement getUpdateSQL(RedeemableBuyBackTransaction object)
        throws SQLException {
        List params = fromObjectToBean(object).toList();
        params.add(object.getId());
        return new ParametricStatement(updateSql, params);
    }

    ParametricStatement getDeleteSQL(String transactionId)
        throws SQLException {
        List params = new ArrayList();
        params.add(transactionId);
        return new ParametricStatement(deleteSql, params);
    }

    RedeemableBuyBackTransaction getByTransactionId(String transactionId, Store store, String custId)
        throws SQLException {
        String whereSql = BaseOracleDAO.where(RkRedmBybkOracleBean.COL_AI_TRN);
        RedeemableBuyBackTransaction redeemableBuybackTransactions[] = fromBeansToObjects(query(new RkRedmBybkOracleBean(), whereSql, transactionId), store, custId);
        if(redeemableBuybackTransactions == null || redeemableBuybackTransactions.length == 0)
            return null;
        else {
            return redeemableBuybackTransactions[0];
        }
    }

    private RedeemableBuyBackTransaction[] fromBeansToObjects(BaseOracleBean beans[], Store store, String custId)
        throws SQLException {
        RedeemableBuyBackTransaction array[] = new RedeemableBuyBackTransaction[beans.length];
        for(int i = 0; i < array.length; i++)
            array[i] = fromBeanToObject(beans[i], store, custId);

        return array;
    }

    private RedeemableBuyBackTransaction fromBeanToObject(BaseOracleBean baseBean, Store store, String custId)
        throws SQLException {
        RkRedmBybkOracleBean bean = (RkRedmBybkOracleBean)baseBean;
        CMSRedeemableBuyBackTransaction object = new CMSRedeemableBuyBackTransaction(store);
        object.doSetId(bean.getAiTrn());
        Redeemable redeemable = redeemableDAO.selectRedeemableById(bean.getIdNmbSrzGfCf());
        object.doSetRedeemable(redeemable);
        object.doSetComment(bean.getComments());
        object.doSetAmount(bean.getAmount());
        object.doSetCustomer((CMSCustomer)customerDAO.selectById(custId));
        return object;
    }

    private RkRedmBybkOracleBean fromObjectToBean(RedeemableBuyBackTransaction object) {
        RkRedmBybkOracleBean bean = new RkRedmBybkOracleBean();
        bean.setAiTrn(object.getId());
        Redeemable redeemable = object.getRedeemable();
        bean.setIdNmbSrzGfCf(redeemable.getId());
        bean.setTyGfCf(redeemableDAO.getRedeemableIssueType(redeemable));
        bean.setComments(object.getComment());
        bean.setAmount(object.getAmount());
        return bean;
    }
    
    private static RedeemableIssueOracleDAO redeemableDAO = new RedeemableIssueOracleDAO();
    private static CustomerOracleDAO customerDAO = new CustomerOracleDAO();
    private static String selectSql;
    private static String insertSql;
    private static String updateSql;
    private static String deleteSql;

    static 
    {
        selectSql = RkRedmBybkOracleBean.selectSql;
        insertSql = RkRedmBybkOracleBean.insertSql;
        updateSql = RkRedmBybkOracleBean.updateSql + BaseOracleDAO.where(RkRedmBybkOracleBean.COL_AI_TRN);
        deleteSql = RkRedmBybkOracleBean.deleteSql + BaseOracleDAO.where(RkRedmBybkOracleBean.COL_AI_TRN);
    }
}