package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.sos.TransactionSOS;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.dataaccess.SosDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkSosOracleBean;
import com.chelseasystems.cs.eod.CMSTransactionEOD;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.sos.CMSTransactionSOS;
import com.chelseasystems.cs.store.CMSStore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Referenced classes of package com.chelseasystems.cs.dataaccess.artsoracle.dao:
//            BaseOracleDAO, CommonTransactionOracleDAO

public class SosOracleDAO extends BaseOracleDAO
    implements SosDAO
{

    public SosOracleDAO()
    {
    }

    public TransactionSOS selectById(String id)
        throws SQLException
    {
        com.chelseasystems.cr.transaction.CommonTransaction transaction = commonTransactionDAO.selectById(id);
        if(transaction instanceof TransactionSOS)
            return (TransactionSOS)transaction;
        else
            return null;
    }

    ParametricStatement[] getInsertSQL(TransactionSOS object)
        throws SQLException
    {
        List statements = new ArrayList();
		 //Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
        CMSRegister cmsRegister=(CMSRegister)object.getRegister();
        if(((CMSStore)object.getStore()).isFranceStore() && ((!cmsRegister.isCheckedEopAtSos()) || (!cmsRegister.isEopPersisted())) &&
        		cmsRegister.isMonthEnd()){
        	ArmEopDtlOracleDAO eopDtlDAO=new ArmEopDtlOracleDAO();
        	ParametricStatement[] parametricStatements = eopDtlDAO.getInsertSQL((TransactionSOS) object);
        	if(parametricStatements!=null && parametricStatements.length>=1)
			statements.addAll(Arrays.asList(parametricStatements));
        }
		 // end Vishal Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
        statements.add(new ParametricStatement(RkSosOracleBean.insertSql, toRkSosBean(object).toList()));
        return (ParametricStatement[])(ParametricStatement[])statements.toArray(new ParametricStatement[0]);
    }

    ParametricStatement[] getUpdateSQL(TransactionSOS object)
        throws SQLException
    {
        List statements = new ArrayList();
        List params = toRkSosBean(object).toList();
        params.add(object.getId());
        statements.add(new ParametricStatement((new StringBuilder()).append(RkSosOracleBean.updateSql).append(where(RkSosOracleBean.COL_AI_TRN)).toString(), params));
        return (ParametricStatement[])(ParametricStatement[])statements.toArray(new ParametricStatement[0]);
    }

    private RkSosOracleBean toRkSosBean(TransactionSOS object)
    {
        RkSosOracleBean bean = new RkSosOracleBean();
        bean.setStoreId(object.getRegister().getStoreId());
        bean.setDrawerFund(object.getRegister().getDrawerFund());
        bean.setAiTrn(object.getId());
        return bean;
    }

    TransactionSOS getById(String transactionId, Store store, String registerId)
        throws SQLException
    {
        List params = new ArrayList();
        params.add(transactionId);
        com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean beans[] = query(new RkSosOracleBean(), where(RkSosOracleBean.COL_AI_TRN), params);
        if(beans == null || beans.length == 0)
        {
            return null;
        } else
        {
            RkSosOracleBean bean = (RkSosOracleBean)beans[0];
            CMSTransactionSOS object = new CMSTransactionSOS(store);
            object.doSetId(bean.getAiTrn());
            CMSRegister register = new CMSRegister(registerId, store.getId());
            register.doSetDrawerFund(bean.getDrawerFund());
            object.doSetRegister(register);
            return object;
        }
    }

    private static CommonTransactionOracleDAO commonTransactionDAO = new CommonTransactionOracleDAO();

}
