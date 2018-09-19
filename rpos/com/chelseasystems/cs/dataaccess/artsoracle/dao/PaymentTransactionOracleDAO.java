/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 05-18-2005 | Vikram    | N/A       | Loylty Hist date set to Submit-date                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 05-18-2005 | Vikram    | N/A       | Lifetime loyalty points behavior : Defect ID 83    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 05-10-2005 | Vikram    | N/A       | POS_104665_TS_LoyaltyManagement_Rev0               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-13-2005 | Rajesh    | N/A       | Specs Congignment impl                             |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Rajesh    | N/A       | Specs Presale impl                                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.layaway.LayawayPaymentTransaction;
import com.chelseasystems.cr.layaway.LayawayRTSTransaction;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.NoSaleTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.RedeemableBuyBackTransaction;
import com.chelseasystems.cr.pos.VoidTransaction;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.transaction.ITransaction;
import com.chelseasystems.cs.dataaccess.PaymentTransactionDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPayTrnOracleBean;
import com.chelseasystems.cs.pos.CMSRedeemableBuyBackTransaction;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrRtlOracleBean;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.pos.RewardTransaction;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.pos.PointManagement;
import com.chelseasystems.cs.loyalty.LoyaltyHistory;
import java.util.Collection;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cs.paidout.CMSMiscPaidOut;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
/**
 *
 *  PaymentTransaction Data Access Object.<br>
 *  This object encapsulates all database access for PaymentTransaction.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>id</td><td>RK_PAYMRNT_TRANSACTION</td><td>AI_TRN</td></tr>
 *    <tr><td>TotalPaymentAmount</td><td>RK_PAYMRNT_TRANSACTION</td><td>TOTAL_AMT</td></tr>
 *    <tr><td>PaymentTransactionType</td><td>RK_PAYMRNT_TRANSACTION</td><td>TYPE_ID</td></tr>
 *  </table>
 *
 *  @see PaymentTransaction
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPayTrnOracleBean
 *
 */
public class PaymentTransactionOracleDAO extends BaseOracleDAO 
		implements PaymentTransactionDAO {
	private static TransactionOracleDAO transactionDAO = new TransactionOracleDAO();
	private static PaidOutOracleDAO paidOutDAO = new PaidOutOracleDAO();
	private static CollectionOracleDAO collectionDAO = new CollectionOracleDAO();
	private static CompositeOracleDAO compositeDAO = new CompositeOracleDAO();
	private static LayawayPaymentOracleDAO layawayPaymentDAO = new LayawayPaymentOracleDAO();
	private static LayawayRtsOracleDAO layawayRtsDAO = new LayawayRtsOracleDAO();
	private static LayawayOracleDAO layawayDAO = new LayawayOracleDAO();
	private static VoidTransactionOracleDAO voidTransactionDAO = new VoidTransactionOracleDAO();
	private static NoSaleOracleDAO noSaleDAO = new NoSaleOracleDAO();
	private static RedeemableBuybackOracleDAO redeemableBuybackDAO = new RedeemableBuybackOracleDAO();
	private static PaymentOracleDAO paymentDAO = new PaymentOracleDAO();
	private static RedeemableIssueOracleDAO redeemableIssueDAO = new RedeemableIssueOracleDAO();
	private static LoyaltyOracleDAO loyaltyDAO = new LoyaltyOracleDAO();
	private static ArmAirportListOracleDAO airportDAO=new ArmAirportListOracleDAO();
	/**
	 * put your documentation comment here
	 * @param id
	 * @return
	 * @exception SQLException
	 */
	//Method modified by SonaliRaina to add boarding pass details in the main 
	//transaction object during viewing transaction
	public PaymentTransaction selectById(String id) throws SQLException {
		ITransaction transaction = transactionDAO.selectById(id);
		if (transaction == null || !(transaction instanceof PaymentTransaction))
			return null;
		else{
			try{
				CMSAirportDetails[] airportdetails=airportDAO.getBoardingPassObject(id);
				if(airportdetails!=null){
				((CMSCompositePOSTransaction)transaction).setAirportDetails(airportdetails[0]);}
				}catch(Exception e){}
			return (PaymentTransaction) transaction;
			}
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	ParametricStatement[] getInsertSQL(PaymentTransaction object) throws SQLException {
		ArrayList statements = new ArrayList();
		statements.add(new ParametricStatement(RkPayTrnOracleBean.insertSql, toRkPayTrnOracleBean(object).toList()));
		if (object instanceof CompositePOSTransaction)
			statements.addAll(Arrays.asList(compositeDAO.getInsertSQL((CompositePOSTransaction) object)));
		else if (object instanceof PaidOutTransaction)
			statements.add(paidOutDAO.getInsertSQL((PaidOutTransaction) object));
		else if (object instanceof CollectionTransaction)
			statements.add(collectionDAO.getInsertSQL((CollectionTransaction) object));
		else if (object instanceof LayawayPaymentTransaction)
			statements.add(layawayPaymentDAO.getInsertSQL((LayawayPaymentTransaction) object));
		else if (object instanceof LayawayRTSTransaction)
			statements.add(layawayRtsDAO.getInsertSQL((LayawayRTSTransaction) object));
		else if (object instanceof VoidTransaction) {
			statements.addAll(Arrays.asList(voidTransactionDAO.getInsertSQL((VoidTransaction) object)));
		} else if (object instanceof NoSaleTransaction) {
			statements.add(noSaleDAO.getInsertSQL((NoSaleTransaction) object));
			if (object instanceof RewardTransaction) {
				statements.addAll(Arrays.asList(redeemableIssueDAO.getInsertSQL(((RewardTransaction) object).getRewardCard())));
				Loyalty loyalty = ((RewardTransaction) object).getRewardCard().getLoyalty();
				// statements.add(loyaltyDAO.getUpdatePointsSQL(loyalty.getLoyaltyNumber(), loyalty.getCurrBalance() - ((RewardTransaction)object).getPoints(), loyalty.getLifeTimeBalance()));
				statements.add(loyaltyDAO.getUpdatePointsSQL(loyalty.getLoyaltyNumber(), 
					loyalty.getCurrBalance() - ((RewardTransaction) object).getPoints(), 
					loyalty.getLifeTimeBalance(), 
					loyalty.getCurrYearBalance() - ((RewardTransaction) object).getPoints(), 
					loyalty.getLastYearBalance()));
				LoyaltyHistory hist = new LoyaltyHistory();
				hist.setLoyaltyNumber(loyalty.getLoyaltyNumber());
				hist.setTransactionDate(((RewardTransaction) object).getSubmitDate());
				hist.setStoreID(((RewardTransaction) object).getStore().getId());
				hist.setTransactionId(((RewardTransaction) object).getId());
				hist.setPointEarned(((RewardTransaction) object).getPoints() * (-1));
				hist.setTransactionType(((RewardTransaction) object).getTransactionType());
				hist.setReasonCode(((RewardTransaction) object).getComment());
				statements.add(loyaltyDAO.getInsertHistorySQL(hist));
			}
			if (object instanceof PointManagement) {
				Loyalty loyalty = ((PointManagement) object).getLoyalty();
				long points = ((PointManagement) object).getPoints();
				if (loyalty.isYearlyComputed()) {
					statements.add(loyaltyDAO.getUpdatePointsSQL(loyalty.getLoyaltyNumber(), loyalty.getCurrBalance(), loyalty.getLifeTimeBalance(), loyalty.getCurrYearBalance(), loyalty.getLastYearBalance()));
				} else {
					statements.add(loyaltyDAO.getUpdatePointsSQL(loyalty.getLoyaltyNumber(), loyalty.getCurrBalance(), loyalty.getLifeTimeBalance(), 0, 0));
				}
				LoyaltyHistory hist = new LoyaltyHistory();
				hist.setLoyaltyNumber(loyalty.getLoyaltyNumber());
				hist.setTransactionDate(((PointManagement) object).getSubmitDate());
				hist.setStoreID(((PointManagement) object).getStore().getId());
				hist.setTransactionId(((PointManagement) object).getId());
				hist.setPointEarned(((PointManagement) object).getPoints());
				hist.setTransactionType(((PointManagement) object).getTransactionType());
				hist.setReasonCode(((PointManagement) object).getComment());
				statements.add(loyaltyDAO.getInsertHistorySQL(hist));
			}
		} else if (object instanceof RedeemableBuyBackTransaction)
			statements.add(redeemableBuybackDAO.getInsertSQL((RedeemableBuyBackTransaction) object));
		Payment[] payments = object.getPaymentsArray();
		for (int i = 0; i < payments.length; i++)
			statements.addAll(Arrays.asList(paymentDAO.getInsertSQL(object.getId(), i, payments[i])));
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[statements.size()]);
	}

	/**
	 * put your documentation comment here
	 * @param id
	 * @param store
	 * @return
	 * @exception SQLException
	 */
	PaymentTransaction getById(String id, Store store) throws SQLException {
		String whereSql = where(RkPayTrnOracleBean.COL_AI_TRN);
		List params = new ArrayList();
		params.add(id);
		BaseOracleBean[] baseBeans = query(new RkPayTrnOracleBean(), whereSql, params);
		if (baseBeans == null || baseBeans.length == 0)
			return null;
		RkPayTrnOracleBean bean = (RkPayTrnOracleBean) baseBeans[0];
		PaymentTransaction object = getNewPaymentTransaction(bean, store);
		Payment[] payments = paymentDAO.getByTransactionId(object.getId());
		for (int i = 0; i < payments.length; i++)
			object.doAddPayment(payments[i]);
		return object;
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	private RkPayTrnOracleBean toRkPayTrnOracleBean(PaymentTransaction object) throws SQLException {
		RkPayTrnOracleBean bean = new RkPayTrnOracleBean();
		bean.setAiTrn(object.getId());
		bean.setTotalAmt(object.getTotalPaymentAmount());
		bean.setTypeId(getPaymentTransactionType(object));
		Payment[] payments = object.getPaymentsArray();
		String payTypes = "*";
		for (int i = 0; i < payments.length; i++)
			payTypes += (payments[i].getTransactionPaymentName() + "*");
		bean.setPayTypes(payTypes);
		Customer customer = getCustomer(object);
		if (customer != null)
			bean.setCustId(customer.getId());
		return bean;
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 */
	public static String getPaymentTransactionType(PaymentTransaction object) {
		if (object instanceof CompositePOSTransaction) {
			CMSCompositePOSTransaction obj = (CMSCompositePOSTransaction) object;
			if (obj.getPresaleLineItemsArray() != null && obj.getPresaleLineItemsArray().length > 0)
				return ArtsConstants.TRANSACTION_TYPE_COMPOSITE_PRS;
			if (obj.getConsignmentLineItemsArray() != null && obj.getConsignmentLineItemsArray().length > 0)
				return ArtsConstants.TRANSACTION_TYPE_COMPOSITE_CSG;
			if (obj.getReservationLineItemsArray() != null && obj.getReservationLineItemsArray().length > 0)
				return ArtsConstants.TRANSACTION_TYPE_COMPOSITE_RSV;
			if ((obj.getNonFiscalNoSaleLineItemsArray() != null && obj.getNonFiscalNoSaleLineItemsArray().length > 0) && (obj.getNonFiscalNoReturnLineItemsArray() != null && obj.getNonFiscalNoReturnLineItemsArray().length > 0))
				return ArtsConstants.TRANSACTION_TYPE_COMPOSITE_NFS;
			return ArtsConstants.TRANSACTION_TYPE_COMPOSITE_POS;
		} else if (object instanceof CollectionTransaction)
			return ArtsConstants.TRANSACTION_TYPE_COLLECTION;
		else if (object instanceof PaidOutTransaction)
			return ArtsConstants.TRANSACTION_TYPE_PAID_OUT;
		else if (object instanceof LayawayRTSTransaction)
			return ArtsConstants.TRANSACTION_TYPE_LAYAWAY_RTS;
		else if (object instanceof LayawayPaymentTransaction)
			return ArtsConstants.TRANSACTION_TYPE_LAYAWAY_PAYMENT;
		else if (object instanceof VoidTransaction)
			return ArtsConstants.TRANSACTION_TYPE_VOID;
		else if (object instanceof NoSaleTransaction)
			return ArtsConstants.TRANSACTION_TYPE_NO_SALE;
		else if (object instanceof RedeemableBuyBackTransaction)
			return ArtsConstants.TRANSACTION_TYPE_REDEEMABLE_BUYBACK;
		return ArtsConstants.TRANSACTION_TYPE_UNKNOWN;
	}

	/**
	 * put your documentation comment here
	 * @param paymentTransactionBean
	 * @param store
	 * @return
	 * @exception SQLException
	 */
	private PaymentTransaction getNewPaymentTransaction(RkPayTrnOracleBean paymentTransactionBean, Store store) throws SQLException {
		String type = paymentTransactionBean.getTypeId();
		String transactionId = paymentTransactionBean.getAiTrn();
		String custId = paymentTransactionBean.getCustId();
		if (type.equals(ArtsConstants.TRANSACTION_TYPE_COMPOSITE_POS))
			return compositeDAO.getById(transactionId, store);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_COMPOSITE_PRS))
			return compositeDAO.getById(transactionId, store);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_COMPOSITE_CSG))
			return compositeDAO.getById(transactionId, store);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_COMPOSITE_RSV))
			return compositeDAO.getById(transactionId, store);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_COMPOSITE_NFS))
			return compositeDAO.getById(transactionId, store);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_PAID_OUT))
			return paidOutDAO.getByRkPayTrnBean(paymentTransactionBean, store);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_COLLECTION))
			return collectionDAO.getByRkPayTrnBean(paymentTransactionBean, store); // changed, so that customer info is also retrieved
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_LAYAWAY_PAYMENT))
			return layawayPaymentDAO.getByTransactionId(transactionId, store);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_LAYAWAY_RTS))
			return layawayRtsDAO.getByTransactionId(transactionId, store);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_VOID))
			return voidTransactionDAO.getByTransactionId(transactionId, store);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_NO_SALE))
			return noSaleDAO.getByTransactionId(transactionId, store);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_REDEEMABLE_BUYBACK))// changed, so that customer info is also retrieved
			return redeemableBuybackDAO.getByTransactionId(transactionId, store, custId);
		else if (type.equals(ArtsConstants.TRANSACTION_TYPE_UNKNOWN))
			return null;
		return null;
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	private Customer getCustomer(PaymentTransaction object) throws SQLException {
		Customer customer = null;
		if (object instanceof CompositePOSTransaction) {
			customer = ((CompositePOSTransaction) object).getCustomer();
		} else if (object instanceof LayawayRTSTransaction) {
			customer = ((LayawayRTSTransaction) object).getOriginalLayaway().getCustomer();
		} else if (object instanceof LayawayPaymentTransaction) {
			customer = layawayDAO.selectById(((LayawayPaymentTransaction) object).getOriginalLayawayId()).getCustomer();
		} else if (object instanceof VoidTransaction) {
			ITransaction originalTransaction = ((VoidTransaction) object).getOriginalTransaction();
			if (originalTransaction instanceof PaymentTransaction)
				customer = getCustomer((PaymentTransaction) originalTransaction);
		} else if (object instanceof CollectionTransaction) {
			customer = ((CMSMiscCollection) object).getCustomer();
		} else if (object instanceof CMSMiscPaidOut) {
			customer = ((CMSMiscPaidOut) object).getCustomer();
		} else if (object instanceof PointManagement) {
			customer = ((PointManagement) object).getLoyalty().getCustomer();
		} else if (object instanceof CMSRedeemableBuyBackTransaction) {
			customer = ((CMSRedeemableBuyBackTransaction) object).getCustomer();
		} else {
			return null;
		}
		return customer;
	}

	/**
	 * This is used to update Customer for transaction.
	 * @throws SQLException
	 * @return ParametricStatement
	 */
	public ParametricStatement getUpdateCustomerSQL(CompositePOSTransaction object) throws SQLException {
		List params = new ArrayList();
		RkPayTrnOracleBean bean = this.toRkPayTrnOracleBean(object);
		Customer customer = object.getCustomer();
		String sUpdateSQL = "UPDATE " + RkPayTrnOracleBean.TABLE_NAME + " SET ";
		if (customer != null) {
			sUpdateSQL += RkPayTrnOracleBean.COL_CUST_ID + " = ? ";
			params.add(customer.getId());
		}
		sUpdateSQL += where(RkPayTrnOracleBean.COL_AI_TRN);
		params.add(object.getId());
		return new ParametricStatement(sUpdateSQL, params);
	}
}
