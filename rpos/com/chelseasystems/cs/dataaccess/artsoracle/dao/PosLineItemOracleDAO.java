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
 | 9    | 04-28-2005 | Pankaja   | N/A       | Modify to support all line item (PRS/CSG)          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.LayawayLineItem;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.ReturnLineItem;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cr.pos.ShippingRequest;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmRtlTrnOracleBean;
import com.chelseasystems.cs.pos.CMSLayawayLineItem;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.pos.CMSPresaleLineItem;
import com.chelseasystems.cs.pos.CMSConsignmentLineItem;
import com.chelseasystems.cs.pos.CMSReservationLineItem;
import com.chelseasystems.cs.pos.CMSNoSaleLineItem;
import com.chelseasystems.cs.pos.CMSNoReturnLineItem;
import com.chelseasystems.cs.pos.CMSVoidLineItem;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.pos.CMSShippingRequest;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.discount.*;
import java.util.List;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.item.CMSItem;

/**
 *
 *  POSLineItem Data Access Object.<br>
 *  This object encapsulates all database access for POSLineItem.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>AdditionalConsultant</td><td>TR_LTM_RTL_TRN</td><td>ADDITIONAL_CONSULTANT_ID</td></tr>
 *    <tr><td>Comments</td><td>TR_LTM_RTL_TRN</td><td>RETURN_COMMENTS</td></tr>
 *    <tr><td>ItemRetailPrice</td><td>TR_LTM_RTL_TRN</td><td>ITEM_RETAIL_PRICE</td></tr>
 *    <tr><td>ItemSellingPrice</td><td>TR_LTM_RTL_TRN</td><td>ITEM_SELLING_PRICE</td></tr>
 *    <tr><td>ManualMarkdownAmount</td><td>TR_LTM_RTL_TRN</td><td>MANUAL_MARKDOWN_AMOUNT</td></tr>
 *    <tr><td>ManualMarkdownReason</td><td>TR_LTM_RTL_TRN</td><td>MANUAL_MARKDOWN_REASON</td></tr>
 *    <tr><td>ManualUnitPrice</td><td>TR_LTM_RTL_TRN</td><td>MANUAL_UNIT_PRICE</td></tr>
 *    <tr><td>MiscItemComment</td><td>TR_LTM_RTL_TRN</td><td>MISC_ITEM_COMMENT</td></tr>
 *    <tr><td>MiscItemDescription</td><td>TR_LTM_RTL_TRN</td><td>MISC_ITEM_DESCRIPTION</td></tr>
 *    <tr><td>MiscItemGLAccount</td><td>TR_LTM_RTL_TRN</td><td>MISC_ITEM_GL_ACCOUNT</td></tr>
 *    <tr><td>MiscItemId</td><td>TR_LTM_RTL_TRN</td><td>MISC_ITEM_ID</td></tr>
 *    <tr><td>MiscItemRegionalTaxable</td><td>TR_LTM_RTL_TRN</td><td>MISC_ITEM_REGIONAL_TXABLE</td></tr>
 *    <tr><td>MiscItemTaxable</td><td>TR_LTM_RTL_TRN</td><td>MISC_ITEM_TAXABLE</td></tr>
 *    <tr><td>Quantity</td><td>TR_LTM_RTL_TRN</td><td>QUANTITY</td></tr>
 *    <tr><td>ReasonId</td><td>TR_LTM_RTL_TRN</td><td>RETURN_REASON_ID</td></tr>
 *    <tr><td>RegionalTaxExemptId</td><td>TR_LTM_RTL_TRN</td><td>REGIONAL_TAX_EXEMPT_ID</td></tr>
 *    <tr><td>TaxExemptId</td><td>TR_LTM_RTL_TRN</td><td>TAX_EXEMPT_ID</td></tr>
 *    <tr><td>ValidNetAmountFlag</td><td>TR_LTM_RTL_TRN</td><td>VALID_NET_AMOUNT_FLAG</td></tr>
 *  </table>
 *
 *  @see POSLineItem
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmRtlTrnOracleBean
 *
 */
public class PosLineItemOracleDAO extends BaseOracleDAO {
	private static ItemOracleDAO itemDAO = new ItemOracleDAO();
	private static EmployeeOracleDAO employeeDAO = new EmployeeOracleDAO();
	private static PosLineItemDetailOracleDAO posLineItemDetailDAO = new PosLineItemDetailOracleDAO();
	private static LineItemDscDtlOracleDAO lineItemDscDtlDAO = new LineItemDscDtlOracleDAO();
	private static ArmFiscalDocLnItmDtlOracleDAO fiscDocLnItmDtlDAO = new ArmFiscalDocLnItmDtlOracleDAO();
	private static String selectSql = TrLtmRtlTrnOracleBean.selectSql;
	private static String insertSql = TrLtmRtlTrnOracleBean.insertSql;
	private static String updateSql = TrLtmRtlTrnOracleBean.updateSql
			+ where(TrLtmRtlTrnOracleBean.COL_AI_TRN, TrLtmRtlTrnOracleBean.COL_AI_LN_ITM);
	private static String updateFiscalDocNumSql =
    "update TR_LTM_RTL_TRN set DOC_NUM = ? , TY_DOC= ?, VAT_COMMENTS= ? "
			+ where(TrLtmRtlTrnOracleBean.COL_AI_TRN, TrLtmRtlTrnOracleBean.COL_AI_LN_ITM);
  private static String deleteSql = TrLtmRtlTrnOracleBean.deleteSql
      + where(TrLtmRtlTrnOracleBean.COL_AI_TRN);
  private static String updateConsultantSQL = "update TR_LTM_RTL_TRN set ADD_CONSULTANT_ID = ? "
      + where(TrLtmRtlTrnOracleBean.COL_AI_TRN, TrLtmRtlTrnOracleBean.COL_AI_LN_ITM);
  private static String updateShipReqSQL = "update TR_LTM_RTL_TRN set SHIP_REQ_SEQ_NUM = ? "
      + where(TrLtmRtlTrnOracleBean.COL_AI_TRN, TrLtmRtlTrnOracleBean.COL_AI_LN_ITM);
  private static ShippingRequestOracleDAO shippingRequestDAO = new ShippingRequestOracleDAO();
  


	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	ParametricStatement[] getInsertSQL(POSLineItem object) throws SQLException {
		ArrayList statements = new ArrayList();
		statements.add(new ParametricStatement(insertSql, fromObjectToBean(object).toList()));
		POSLineItemDetail[] details = object.getLineItemDetailsArray();
		for (int i = 0; i < details.length; i++)
			statements.addAll(Arrays.asList(posLineItemDetailDAO.getInsertSQL(details[i])));
		//  if (object instanceof CMSSaleLineItem || object instanceof CMSConsignmentLineItem || object instanceof CMSPresaleLineItem){
		if (object.getDiscountsArray() != null) {
			statements.addAll(Arrays.asList(lineItemDscDtlDAO.getInsertSQL(object)));
		}
		//}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * put your documentation comment here
	 * @param transactionId
	 * @return
	 * @exception SQLException
	 */
	Boolean doesTransactionHaveSippingRequests(String transactionId) throws SQLException {
		String whereSql = where(TrLtmRtlTrnOracleBean.COL_AI_TRN);
		BaseOracleBean[] beans = query(new TrLtmRtlTrnOracleBean(), whereSql, transactionId);
    if (beans == null || beans.length == 0)
      return  null;
		for (int i = 0; i < beans.length; i++) {
			Long shippingRequestSeqNum = ((TrLtmRtlTrnOracleBean) beans[i]).getShipReqSeqNum();
      if (shippingRequestSeqNum != null)
        return  Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * put your documentation comment here
	 * @param composite
	 * @exception SQLException
	 */
	void addLineItemsToComposite(CompositePOSTransaction composite) throws SQLException {
		String whereSql = where(TrLtmRtlTrnOracleBean.COL_AI_TRN);
		fromBeansToObjects(composite, query(new TrLtmRtlTrnOracleBean(), whereSql, composite.getId()));
		
	
	}

	/**
	 * put your documentation comment here
	 * @param lineItem
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getUpdateFiscalDocNumSQL(POSLineItem lineItem,
			FiscalDocument document) throws SQLException {
		ArrayList statements = new ArrayList();
		List params = new ArrayList();
		params.add(document.getDocumentNumber());
		params.add(document.getDocumentType());
		if (lineItem instanceof CMSNoSaleLineItem) {
      String vatComments = ((CMSNoSaleLineItem)lineItem).getFiscalDocComment(document.getDocumentType());
      if (vatComments != null) {
        params.add(vatComments);
      } else {
        params.add("");
      }
    } else if (lineItem instanceof CMSNoReturnLineItem) {
      String vatComments = ((CMSNoReturnLineItem)lineItem).getFiscalDocComment(document.getDocumentType());
			if (vatComments != null) {
				params.add(vatComments);
			} else {
				params.add("");
			}
		} else if (lineItem instanceof CMSSaleLineItem) {
      String vatComments = ((CMSSaleLineItem)lineItem).getFiscalDocComment(document.getDocumentType());
			if (vatComments != null) {
				params.add(vatComments);
			} else {
				params.add("");
			}
		} else if (lineItem instanceof CMSPresaleLineItem) {
      String vatComments = ((CMSPresaleLineItem)lineItem).getFiscalDocComment(document.getDocumentType());
			if (vatComments != null) {
				params.add(vatComments);
			} else {
				params.add("");
			}
		} else if (lineItem instanceof CMSReservationLineItem) {
      String vatComments = ((CMSReservationLineItem)lineItem).getFiscalDocComment(document.getDocumentType());
			if (vatComments != null) {
				params.add(vatComments);
			} else {
				params.add("");
			}
		} else if (lineItem instanceof CMSConsignmentLineItem) {
      String vatComments = ((CMSConsignmentLineItem)lineItem).getFiscalDocComment(document.getDocumentType());
			if (vatComments != null) {
				params.add(vatComments);
			} else {
				params.add("");
			}
		} else if (lineItem instanceof CMSReturnLineItem) {
      String vatComments = ((CMSReturnLineItem)lineItem).getFiscalDocComment(document.getDocumentType());
			if (vatComments != null) {
				params.add(vatComments);
			} else {
				params.add("");
			}
		}

		params.add(lineItem.getTransaction().getCompositeTransaction().getId());
		params.add(lineItem.getSequenceNumber() + "");
		statements.add(new ParametricStatement(this.updateFiscalDocNumSql, params));
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	protected BaseOracleBean getDatabeanInstance() {
		return new TrLtmRtlTrnOracleBean();
	}

	/**
	 * put your documentation comment here
	 * @param composite
	 * @param beans
	 * @exception SQLException
	 */
	private void fromBeansToObjects(CompositePOSTransaction composite, BaseOracleBean[] beans)
			throws SQLException {
		for (int i = 0; i < beans.length; i++)
			fromBeanToObject(composite, beans[i]);
	}

	/**
	 * put your documentation comment here
	 * @param composite
	 * @param baseBean
	 * @exception SQLException
	 */
	private void fromBeanToObject(CompositePOSTransaction composite, BaseOracleBean baseBean)
			throws SQLException {
		TrLtmRtlTrnOracleBean bean = (TrLtmRtlTrnOracleBean) baseBean;
		CMSItem item = itemDAO.selectByIDOrBarcode(bean.getIdItm(), composite.getStore().getId());
		Long type = bean.getPosLnItmTyId();
		int sequenceNumber = bean.getAiLnItm().intValue();
		POSLineItem object = null;
		if (type.equals(POS_LINE_ITEM_TYPE_SALE))
			object = new CMSSaleLineItem(composite.getSaleTransaction(), item, sequenceNumber);
		else if (type.equals(POS_LINE_ITEM_TYPE_RETURN))
			object = new CMSReturnLineItem(composite.getReturnTransaction(), item, sequenceNumber);
		else if (type.equals(POS_LINE_ITEM_TYPE_VOID))
			object = new CMSVoidLineItem(composite.getReturnTransaction(), item, sequenceNumber);
		else if (type.equals(POS_LINE_ITEM_TYPE_LAYAWAY))
			object = new CMSLayawayLineItem(composite.getLayawayTransaction(), item, sequenceNumber);
		else if (type.equals(POS_LINE_ITEM_TYPE_PRESALE)) {
			com.chelseasystems.cs.pos.CMSCompositePOSTransaction cmsComposite = (com.chelseasystems.cs.pos.CMSCompositePOSTransaction) composite;
			object = new CMSPresaleLineItem(cmsComposite.getPresaleTransaction(), item, sequenceNumber);
		} else if (type.equals(POS_LINE_ITEM_TYPE_CONSIGNMENT)) {
			com.chelseasystems.cs.pos.CMSCompositePOSTransaction cmsComposite = (com.chelseasystems.cs.pos.CMSCompositePOSTransaction) composite;
			object = new CMSConsignmentLineItem(cmsComposite.getConsignmentTransaction(), item,
					sequenceNumber);
		} else if (type.equals(POS_LINE_ITEM_TYPE_RESERVATION)) {
			com.chelseasystems.cs.pos.CMSCompositePOSTransaction cmsComposite = (com.chelseasystems.cs.pos.CMSCompositePOSTransaction) composite;
			object = new CMSReservationLineItem(cmsComposite.getReservationTransaction(), item,
					sequenceNumber);
		} else if (type.equals(POS_LINE_ITEM_TYPE_NOSALE)) {
			com.chelseasystems.cs.pos.CMSCompositePOSTransaction cmsComposite = (com.chelseasystems.cs.pos.CMSCompositePOSTransaction) composite;
      object = new CMSNoSaleLineItem(cmsComposite.getNonFiscalNoSaleTransaction(), item, sequenceNumber);
    }  else if (type.equals(POS_LINE_ITEM_TYPE_NORETURN)) {
      com.chelseasystems.cs.pos.CMSCompositePOSTransaction cmsComposite = (com.chelseasystems.cs.pos.CMSCompositePOSTransaction)composite;
      object = new CMSNoReturnLineItem(cmsComposite.getNonFiscalNoReturnTransaction(), item, sequenceNumber);
    }
    if(bean.getMiscItemId()!=null){
    	if(LineItemPOSUtil.isDeposit(bean.getMiscItemId())){
    		((CMSItem)item).setIsDeposit(true);
    	}		
		}
		object.doSetItemRetailPrice(bean.getItmRetailPrice());
		object.doSetQuantity(new Integer(bean.getQuantity().intValue()));
		object.doSetValidNetAmountFlag(bean.getValidNetAmtFl().booleanValue());
		object.doSetManualMarkdownAmount(bean.getManualMdAmt());
		object.doSetManualMarkdownReason(bean.getManualMdReason());
		object.doSetManualUnitPrice(bean.getManualUnitPrice());
		object.doSetMiscItemId(bean.getMiscItemId());
		object.doSetMiscItemDescription(bean.getMiscItemDesc());
		object.doSetMiscItemTaxable(bean.getMiscItemTxbl());
		object.doSetMiscItemGLAccount(bean.getMiscItemGlAcc());
		object.doSetMiscItemComment(bean.getMiscItemComment());
		object.doSetRegionalTaxExemptId(bean.getRegTaxExemptId());
		object.doSetMiscItemRegionalTaxable(bean.getMiscItemRgTxbl());
		object.doSetItemSellingPrice(bean.getItmSelPrice());
		//Vivek Mishra : Added to fetch AJB sequence number in case of gift card activation and reload
		object.setAjbSequence(bean.getAjbSeq());
		if(bean.getExtBarcode() != null){
			object.doSetExtendedBarCode(bean.getExtBarcode());
		}
		if (bean.getAddConsultantId() != null)
			object.doSetAdditionalConsultant(employeeDAO.selectById(bean.getAddConsultantId()));
		object.doSetTaxExemptId(bean.getTaxExemptId());
		posLineItemDetailDAO.addDetailsToLineItem(object);
		if (object instanceof CMSReturnLineItem) {
			((ReturnLineItem) object).doSetReasonId(bean.getReturnReasonId());
			((ReturnLineItem) object).doSetComments(bean.getReturnComments());
			((CMSReturnLineItem) object).doSetIsForExchange(bean.getFlExchange());
		}
		if (object instanceof SaleLineItem && bean.getShipReqSeqNum() != null) {
			Long shippingRequsetSeqNum = bean.getShipReqSeqNum();
			ShippingRequest shippingRequest = composite.getShippingRequestsArray()[shippingRequsetSeqNum
					.intValue()];
			SaleLineItem saleLineItem = (SaleLineItem) object;
			saleLineItem.doSetShippingRequest(shippingRequest);
			shippingRequest.doAddLineItem(saleLineItem);
		}
		//ks: Added Consignmentlineitem support
		if (object instanceof CMSConsignmentLineItem && bean.getShipReqSeqNum() != null) {
			Long shippingRequsetSeqNum = bean.getShipReqSeqNum();
			ShippingRequest shippingRequest = composite.getShippingRequestsArray()[shippingRequsetSeqNum
					.intValue()];
			CMSConsignmentLineItem consLineItem = (CMSConsignmentLineItem) object;
			consLineItem.doSetShippingRequest(shippingRequest);
			((CMSShippingRequest) shippingRequest).doAddConsignmentLineItem(consLineItem);
		}
		if (object instanceof CMSPresaleLineItem && bean.getShipReqSeqNum() != null) {
			Long shippingRequsetSeqNum = bean.getShipReqSeqNum();
			ShippingRequest shippingRequest = composite.getShippingRequestsArray()[shippingRequsetSeqNum
					.intValue()];
			CMSPresaleLineItem prsLineItem = (CMSPresaleLineItem) object;
			prsLineItem.doSetShippingRequest(shippingRequest);
			((CMSShippingRequest) shippingRequest).doAddPresaleLineItem(prsLineItem);
		}
		object = fiscDocLnItmDtlDAO.addFiscalDocumentsToLineItem(object);
		Discount discounts[] = composite.getDiscountsArray();
		if (discounts != null && discounts.length > 0) {
			for (int i = 0; i < discounts.length; i++) {
				CMSDiscount cmsDisc = (CMSDiscount) discounts[i];
				LineItemDiscountDetail lineItemDiscountDtl[] = lineItemDscDtlDAO.getLineItemDiscounts(
						composite.getId(), sequenceNumber, cmsDisc.getSequenceNumber());
				if (lineItemDiscountDtl != null && lineItemDiscountDtl.length > 0) {
					cmsDisc.setIsLineItemDiscount(true);
					object.doAddDiscount(cmsDisc);
				}
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 */
	private TrLtmRtlTrnOracleBean fromObjectToBean(POSLineItem object) {
		TrLtmRtlTrnOracleBean bean = new TrLtmRtlTrnOracleBean();
		bean.setAiTrn(object.getTransaction().getCompositeTransaction().getId());
		bean.setAiLnItm(object.getSequenceNumber());
		bean.setIdItm(object.getItem().getId());
		bean.setItmRetailPrice(object.getItemRetailPrice());
		bean.setQuantity(object.getQuantity().intValue());
		bean.setValidNetAmtFl(object.isNetAmountValid());
		bean.setManualMdAmt(object.getManualMarkdownAmount());
		bean.setManualMdReason(object.getManualMarkdownReason());
		bean.setMiscItemId(object.getMiscItemId());
		bean.setMiscItemDesc(object.doGetMiscItemDescription());
		bean.setMiscItemTxbl(object.doGetMiscItemTaxable());
		bean.setMiscItemGlAcc(object.getMiscItemGLAccount());
		bean.setMiscItemComment(object.getMiscItemComment());
		if(object.getExtendedBarCode() != null){
			bean.setExtBarcode(object.getExtendedBarCode());
		}
		if (object.getManualUnitPrice() != null){
			bean.setManualUnitPrice(object.getManualUnitPrice());
		}
		if (object.getAdditionalConsultant() != null){
			bean.setAddConsultantId(object.getAdditionalConsultant().getId());
		}
		bean.setTaxExemptId(object.getTaxExemptId());
		bean.setRegTaxExemptId(object.getRegionalTaxExemptId());
		bean.setMiscItemRgTxbl(object.doGetMiscItemRegionalTaxable());
		//Persist taxes and selling price (retail - reduction)
		//Fix for issue #1878 Uncomment this code becuase of Presale Detail Report item selling price was coming 0.00
		if (object instanceof CMSPresaleLineItem) {
			try {
				bean.setItmSelPrice(object.getExtendedRetailAmount().subtract( ( (
						CMSPresaleLineItem) object).getExtendedReductionAmount()));
			}catch (Exception ex){
				bean.setItmSelPrice(object.getItemSellingPrice());
			}
		} else {
			bean.setItmSelPrice(object.getItemSellingPrice());
		}
		//bean.setItmSelPrice(object.getItemSellingPrice());
		if (object instanceof CMSNoSaleLineItem)
			bean.setPosLnItmTyId(ArtsConstants.POS_LINE_ITEM_TYPE_NOSALE);
		else if (object instanceof CMSNoReturnLineItem)
			bean.setPosLnItmTyId(ArtsConstants.POS_LINE_ITEM_TYPE_NORETURN);
		else if (object instanceof SaleLineItem)
			bean.setPosLnItmTyId(ArtsConstants.POS_LINE_ITEM_TYPE_SALE);
		else if (object instanceof CMSReturnLineItem) {
			if (object instanceof CMSVoidLineItem)
				bean.setPosLnItmTyId(POS_LINE_ITEM_TYPE_VOID);
			else
				bean.setPosLnItmTyId(POS_LINE_ITEM_TYPE_RETURN);
			bean.setFlExchange(((CMSReturnLineItem) object).getIsForExchange());
		} else if (object instanceof LayawayLineItem)
			bean.setPosLnItmTyId(ArtsConstants.POS_LINE_ITEM_TYPE_LAYAWAY);
		else if (object instanceof CMSPresaleLineItem) //{
			bean.setPosLnItmTyId(ArtsConstants.POS_LINE_ITEM_TYPE_PRESALE);
		/* CMSPresaleLineItem cmsPresaleLineItem = (CMSPresaleLineItem)object;
      	if (cmsPresaleLineItem.getAlterationLineItem() != null) {
        	bean.setShipReqSeqNum(getShippingRequestSeqNum(cmsPresaleLineItem.getAlterationLineItem()));
        	return  bean;
      	}
    } */
		else if (object instanceof CMSConsignmentLineItem)
			bean.setPosLnItmTyId(ArtsConstants.POS_LINE_ITEM_TYPE_CONSIGNMENT);
		else if (object instanceof CMSReservationLineItem)
			bean.setPosLnItmTyId(ArtsConstants.POS_LINE_ITEM_TYPE_RESERVATION);
		/*else if (object instanceof CMSNoSaleLineItem)
      	bean.setPosLnItmTyId(ArtsConstants.POS_LINE_ITEM_TYPE_NOSALE);*/
		if (object instanceof ReturnLineItem) {
			bean.setReturnReasonId(((ReturnLineItem) object).getReasonId());
			bean.setReturnComments(((ReturnLineItem) object).getComments());
			//Added by Rachana for apporval of return transaction
			bean.setApproverId(object.getApprover());
		}
		//Vivek Mishra : Added to save AJB sequence number in case of gift card activation and reload
		bean.setAjbSeq(object.getAjbSequence());
		//Ends here
		bean.setShipReqSeqNum(getShippingRequestSeqNum(object));
		return bean;
	}
	

	/**
	 * put your documentation comment here
	 * @param lineItem
	 * @return
	 */
	private Long getShippingRequestSeqNum(POSLineItem lineItem) {
		//ks: Added support for ConsignmentLineItem
		ShippingRequest shippingRequest = null;
		if (lineItem instanceof CMSConsignmentLineItem)
			shippingRequest = ((CMSConsignmentLineItem) lineItem).getShippingRequest();
		else if (lineItem instanceof CMSPresaleLineItem)
			shippingRequest = ((CMSPresaleLineItem) lineItem).getShippingRequest();
		else if (lineItem instanceof SaleLineItem)
			shippingRequest = ((SaleLineItem) lineItem).getShippingRequest();
    else
      return null;
    ShippingRequest[] shippingRequestArray = lineItem.getTransaction().getCompositeTransaction().
        getShippingRequestsArray();
    if (shippingRequest == null)
      return null;
		for (int i = 0; i < shippingRequestArray.length; i++) {
      if (shippingRequest == shippingRequestArray[i]) {
        if (((CMSShippingRequest)shippingRequest).getSeqNum() != null)
          return new Long(((CMSShippingRequest)shippingRequest).getSeqNum());
        else
          return new Long(i);
      }
		}
		return null;
	}

  /**
   * put your documentation comment here
   * @param lineItem
   * @return
   * @exception SQLException
   */
  private ParametricStatement[] getUpdateConsutantSQL(POSLineItem lineItem)
      throws SQLException {
    ArrayList statements = new ArrayList();
    List params = new ArrayList();
    params.add(lineItem.getAdditionalConsultant().getId());
    params.add(lineItem.getTransaction().getCompositeTransaction().getId());
    params.add(lineItem.getSequenceNumber() + "");
    statements.add(new ParametricStatement(this.updateConsultantSQL, params));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
}

  /**
   * put your documentation comment here
   * @param lineItems
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateConsutantSQL(POSLineItem[] lineItems)
      throws SQLException {
    ArrayList statements = new ArrayList();
    if (lineItems != null) {
      for (int i = 0; i < lineItems.length; i++) {
         statements.addAll(Arrays.asList(getUpdateConsutantSQL(lineItems[i])));
      }
    }
    System.out.println("returning from getUpdateConsutant SQL " + statements.size());
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  
  
  public ParametricStatement[] getUpdateShippingRequestSQL(CMSCompositePOSTransaction composite)
      throws SQLException {
    ArrayList statements = new ArrayList();
    POSLineItem[] lineItems = composite.getLineItemsArray();
    if (lineItems != null) {
      for (int i = 0; i < lineItems.length; i++) {
        ShippingRequest shippingRequest = null;
        if (lineItems[i] instanceof CMSConsignmentLineItem)
          shippingRequest = ((CMSConsignmentLineItem)lineItems[i]).getShippingRequest();
        else if (lineItems[i] instanceof CMSPresaleLineItem)
          shippingRequest = ((CMSPresaleLineItem)lineItems[i]).getShippingRequest();
        else if (lineItems[i] instanceof SaleLineItem)
          shippingRequest = ((SaleLineItem)lineItems[i]).getShippingRequest();
        else
          shippingRequest = null;

        if (shippingRequest != null)
          statements.addAll(Arrays.asList(getUpdateShippingRequestSQL(lineItems[i])));
      }
      ShippingRequest shippingRequest[] = composite.getShippingRequestsArray();
      for (int i = 0; i < shippingRequest.length; i++)
        if (((CMSShippingRequest)shippingRequest[i]).getSeqNum() == null)
          statements.add(shippingRequestDAO.getInsertSQL(shippingRequest[i], i));
    }
    System.out.println("returning from getUpdateShippingRequestSQL SQL " + statements.size());
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  private ParametricStatement[] getUpdateShippingRequestSQL(POSLineItem lineItem)
      throws SQLException {
    ArrayList statements = new ArrayList();
    List params = new ArrayList();
    Long shipReqNum = this.getShippingRequestSeqNum(lineItem);
    params.add(shipReqNum);
    params.add(lineItem.getTransaction().getCompositeTransaction().getId());
    params.add(lineItem.getSequenceNumber() + "");
    statements.add(new ParametricStatement(this.updateShipReqSQL, params));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  

  
}


