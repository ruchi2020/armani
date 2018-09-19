/**
 * Description:ItemVw Oracle DAO
 * Created By:Khyati Shah
 * Created Date:2/2/2005
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 9    4/04/06         SA  PCR 1167                        Sku lookup and visibility       
 * 8	5/03/05	        VM	Item Lookup specs               corrected use of countryCode
 * 7	4/30/05	        VM	Item Lookup specs               VAT may not exist
 * 6	4/19/05	        KS	Item Lookup specs               Implementation
 * 1	2/2/05	        KS	POS_IS_ItemDownload_Rev1	ItemVwOracleDAO
 *
 */

package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.ItemStock;
import com.chelseasystems.cs.item.CMSItemDetail;
import com.chelseasystems.cs.item.ItemSearchString;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.currency.ArmCurrency;
import java.util.Date;
import java.sql.*;
import java.util.*;

/**
 * put your documentation comment here
 */
public class ItemVwOracleDAO extends BaseOracleDAO implements ItemVwDAO {
	private static String selectSql = RkItemVwOracleBean.selectSql;

	/**
	 * @param idItm
	 *            String
	 * @throws SQLException
	 * @return CMSItem[]
	 */
	public CMSItem[] selectByIdItm(String idItm) throws SQLException {
		String whereSql = "where ID_ITM = ?";
		return fromBeansToObjects(query(new RkItemVwOracleBean(), selectSql + whereSql, idItm));
	}

	/**
	 * @param barcode
	 *            String
	 * @param storeId
	 *            String
	 * @throws SQLException
	 * @return CMSItem
	 */
	
	public CMSItem selectByBarcode(String barcode, String storeId) throws SQLException {
		String whereSql;
		
		
		if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
        	whereSql = "";
			whereSql = " where id_itm = getId_item(?, ?) AND ID_STR_RT = ? "; 
		} else {
        	whereSql = "";
			//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
        	whereSql = " where id_itm = getId_item(?, ?) AND ID_STR_RT = ? " ;
        	//whereSql = " where BARCODE = ? and ID_STR_RT = ?";
			//Ends here
	    }
		
        List params = new ArrayList();
        //Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
        //params.add(barcode);
        //Ends here
        if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
        	//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
			params.add(barcode);
			//Ends here 
        	params.add(storeId);
        }
		//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
        params.add(barcode);
		//Ends here
        params.add(storeId);
         
		CMSItem[] aCmsItem = fromBeansToObjects(query(new RkItemVwOracleBean(), selectSql + whereSql, params));
		CMSItem cmsItem = null;
		for (int i = 0; aCmsItem != null && i < aCmsItem.length; i++) {
			cmsItem = aCmsItem[i];
		}
		return cmsItem;
	}
	
	/**Added by Anjana to fetch the SAP Item
	 * @param barcode String
	 * @param storeId String
	 * @param itemIdLength String
	 * @throws SQLException
	 * @return CMSItem
	 */
	
	public CMSItem findSAPBarCode(String barcode, String storeId, String itemIdLength) throws SQLException {
		String whereSql;
        	whereSql = "";
           whereSql = " where BARCODE = ? and ID_STR_RT = ? and Length(id_itm)>?";
        	
        	//whereSql = " where id_itm = getId_item(?, ?) AND ID_STR_RT = ? "; 
	
        List params = new ArrayList();
        List param = new ArrayList();
        params.add(barcode);
        params.add(storeId);
        params.add(itemIdLength);
        CMSItem[] aCmsItem = fromBeansToObjects(query(new RkItemVwOracleBean(), selectSql + whereSql, params));
        //Anjana added to resolve some of the NOF issues  related to the special items with multiple-barcodes
        if(aCmsItem==null){
        	whereSql =  " where id_itm = getId_item(?, ?) AND ID_STR_RT = ? "; 
        	param.add(barcode);
            param.add(storeId);
            param.add(storeId);
            aCmsItem = fromBeansToObjects(query(new RkItemVwOracleBean(), selectSql + whereSql, param));
        }
		CMSItem cmsItem = null;
		for (int i = 0; aCmsItem != null && i < aCmsItem.length; i++) {
			cmsItem = aCmsItem[i];
		}
		return cmsItem;
	}
	
	/**
	 * @param barcode
	 *            String
	 * @param storeId
	 *            String
	 * @throws SQLException
	 * @return CMSItem
	 */
	public CMSItem selectByIDOrBarcode(String barcode, String storeId) throws SQLException {
		String whereSql = "where (" + RkItemVwOracleBean.COL_BARCODE + " = ? " + "or " + RkItemVwOracleBean.COL_ID_ITM + " = ? )" + " and " + RkItemVwOracleBean.COL_ID_STR_RT + " = ? ";
		List params = new ArrayList();
		params.add(barcode);
		params.add(barcode);
		params.add(storeId);
		CMSItem[] aCmsItem = fromBeansToObjects(query(new RkItemVwOracleBean(), selectSql + whereSql, params));
		CMSItem cmsItem = null;
		for (int i = 0; aCmsItem != null && i < aCmsItem.length; i++) {
			cmsItem = aCmsItem[i];
		}
		return cmsItem;
	}

	/**
	 * @param barcode
	 *            String
	 * @param storeId
	 *            String
	 * @throws SQLException
	 * @return CMSItem
	 */
	public CMSItem selectById(String id) throws SQLException {
		String whereSql = "where ID_ITM = ?";
		List params = new ArrayList();
		params.add(id);
		CMSItem[] aCmsItem = fromBeansToObjects(query(new RkItemVwOracleBean(), selectSql + whereSql, params));
		CMSItem cmsItem = null;
		for (int i = 0; aCmsItem != null && i < aCmsItem.length; i++) {
			cmsItem = aCmsItem[i];
		}
		return cmsItem;
	}

	/**
	 * @param description
	 *            String
	 * @param storeId
	 *            String
	 * @throws SQLException
	 * @return CMSItem[]
	 */
	public CMSItem[] selectByDescriptionAndStoreId(String description, String storeId) throws SQLException {
		String whereSql = "where lower(NM_ITM) like ? and ID_STR_RT = ?";
		List params = new ArrayList();
		params.add(("%" + description + "%").toLowerCase());
		params.add(storeId);
		return fromBeansToObjects(query(new RkItemVwOracleBean(), selectSql + whereSql, params));
	}

	/**
	 * @return BaseOracleBean
	 */
	protected BaseOracleBean getDatabeanInstance() {
		return new RkItemVwOracleBean();
	}

	/**
	 * @param beans
	 *            BaseOracleBean[]
	 * @return CMSItem[]
	 */
	private CMSItem[] fromBeansToObjects(BaseOracleBean[] beans) {
		RkItemVwOracleBean itemVwOracleBean = new RkItemVwOracleBean();
		if (beans == null || beans.length == 0) {
			return null;
		} else {
			String currStoreItem = "";
			CMSItem object = null;
			Hashtable hCMSItem = new Hashtable();
			for (int i = 0; i < beans.length; i++) {
				itemVwOracleBean = (RkItemVwOracleBean) beans[i];
				String testStoreAndItemId = itemVwOracleBean.getIdItm();
				testStoreAndItemId = testStoreAndItemId + itemVwOracleBean.getIdStrRt();
				if (currStoreItem.equals(testStoreAndItemId)) {
					if (hCMSItem.containsKey(testStoreAndItemId)) {
						CMSItem cmsItem = (CMSItem) hCMSItem.get(testStoreAndItemId);
						hCMSItem.remove(testStoreAndItemId);
						ArrayList lPromotionList = new ArrayList();
						if (cmsItem.getPromotionIds() != null) {
							for (Iterator it = cmsItem.getPromotionIds(); it.hasNext();) {
								lPromotionList.add(it.next());
							}
						}
						if (null != itemVwOracleBean.getIdRuPrdv())
							lPromotionList.add(itemVwOracleBean.getIdRuPrdv());
						cmsItem.doSetPromotionIds(lPromotionList);
						hCMSItem.put(testStoreAndItemId, cmsItem);
					}
				} // current store and Item id == oracleVw bean's current item and store id
				else {
					object = new CMSItem(itemVwOracleBean.getIdItm());
					object.setIsItemFromCurrentStore(true);
					object.doSetLabel(itemVwOracleBean.getLabel());
					object.doSetForTheYear(itemVwOracleBean.getFy());
					object.doSetBarCode(itemVwOracleBean.getBarcode());
					object.doSetSubline(itemVwOracleBean.getSubline());
					object.doSetGender(itemVwOracleBean.getGender());
					object.doSetCategory(itemVwOracleBean.getCategory());
					object.doSetItemDrop(itemVwOracleBean.getItemDrop());
					object.doSetVariant(itemVwOracleBean.getVariant());
					object.doSetModel(itemVwOracleBean.getModel());
					object.doSetFabric(itemVwOracleBean.getFabric());
					object.doSetStyleNum(itemVwOracleBean.getStyleNum());
					object.doSetProductNum(itemVwOracleBean.getProductNum());
					object.doSetDescription(itemVwOracleBean.getNmItm());
					object.doSetRetailPrice(new ArmCurrency(itemVwOracleBean.getRetailValue() == null ? itemVwOracleBean.getCurrencyCode() + "0" : itemVwOracleBean.getRetailValue()));
					object.doSetMarkdownAmount(new ArmCurrency(itemVwOracleBean.getMarkdownValue() == null ? itemVwOracleBean.getCurrencyCode() + "0" : itemVwOracleBean.getMarkdownValue()));
					if (itemVwOracleBean.getTaxableValue() != null && itemVwOracleBean.getTaxableValue().equalsIgnoreCase("N")) {
						object.doSetTaxable(false);
					} else if (itemVwOracleBean.getTaxableValue() != null && itemVwOracleBean.getTaxableValue().equalsIgnoreCase("Y")) {
						object.doSetTaxable(true);
					} else {
						object.doSetTaxable(false);
					}
					object.doSetVatRate(itemVwOracleBean.getVatRate() == null ? new Double(0) : new Double(itemVwOracleBean.getVatRate()));
					object.doSetScItemSls(itemVwOracleBean.getScItmSls());
					object.doSetBrand(itemVwOracleBean.getIdBrn());
					object.doSetClassId(itemVwOracleBean.getIdClass());
					object.doSetColorId(itemVwOracleBean.getIdColor());
					object.doSetSeason(itemVwOracleBean.getSeason());
					object.doSetSizeId(itemVwOracleBean.getIdSize());
					object.doSetKidsSize(itemVwOracleBean.getIdSizeKids());
					object.doSetSupplierId(itemVwOracleBean.getIdSpr());
					object.doSetSubClassId(itemVwOracleBean.getIdSbcl());
					object.doSetStoreId(itemVwOracleBean.getIdStrRt());
					object.doSetStoreName(itemVwOracleBean.getNmOrgn());
					object.doSetCurrencyCode(itemVwOracleBean.getCurrencyCode());
					object.doSetDepartment(itemVwOracleBean.getIdDptPos());
					object.doSetUpdateDate(itemVwOracleBean.getUpdateDt());
					/**
					 * RMS supports three values for requiresManualUnitPrice. R - required O - optional P - prohibited
					 */
					if (itemVwOracleBean.getFlEnPrcRq() != null) {
						object.doSetRequiresManualUnitPrice("R".equalsIgnoreCase(itemVwOracleBean.getFlEnPrcRq().toString()));
						object.doSetManualPriceEntryProhibited("P".equalsIgnoreCase(itemVwOracleBean.getFlEnPrcRq().toString()));
					}
					CMSItemDetail itemDetail = object.getItemDetail();
					itemDetail.doSetBrandDesc(itemVwOracleBean.getNmBrn());
					itemDetail.doSetCategoryDesc(itemVwOracleBean.getNmCategory());
					itemDetail.doSetClassDesc(itemVwOracleBean.getNmClass());
					itemDetail.doSetColorDesc(itemVwOracleBean.getDeColor());
					itemDetail.doSetDropDesc(itemVwOracleBean.getDeDrop());
					itemDetail.doSetSizeIndx(itemVwOracleBean.getSizeIndex());
					itemDetail.doSetExtSizeIndx(itemVwOracleBean.getExtSizeIndex());
					itemDetail.doSetGenderDesc(itemVwOracleBean.getDeGender());
					itemDetail.doSetKidsSizeDesc(itemVwOracleBean.getDeSizeKids());
					itemDetail.doSetLabelDesc(itemVwOracleBean.getNmLabel());
					itemDetail.doSetPosDeptDesc(itemVwOracleBean.getNmDptPos());
					itemDetail.doSetProductDesc(itemVwOracleBean.getDeProduct());
					itemDetail.doSetSeasonDesc(itemVwOracleBean.getDeSeason());
					itemDetail.doSetSizeDesc(itemVwOracleBean.getDeSize());
					itemDetail.doSetSubClassDesc(itemVwOracleBean.getNmSbcl());
					itemDetail.doSetSublineDesc(itemVwOracleBean.getDeSubline());
					itemDetail.doSetSupplierName(itemVwOracleBean.getNmSpr());
					ItemStock itemStock = object.getItemStock();
					itemStock.setAvailableQty(itemVwOracleBean.getQuAvailable().intValue());
					itemStock.setUnAvailableQty(itemVwOracleBean.getQuUnavailable().intValue());
					itemStock.setAvailableStoreQty(itemVwOracleBean.getQuStoreAvailable().intValue());
					itemStock.setUnAvailableStoreQty(itemVwOracleBean.getQuStoreUnavailable().intValue());
					if (null != itemVwOracleBean.getIdRuPrdv()) {
						ArrayList lPromotionList = new ArrayList();
						lPromotionList.add(itemVwOracleBean.getIdRuPrdv());
						object.doSetPromotionIds(lPromotionList);
					}
					currStoreItem = object.getId() + object.getStoreId();
					hCMSItem.put(currStoreItem, object);
				}
			} // get hashtable cmsItem to array
			return (CMSItem[]) hCMSItem.values().toArray(new CMSItem[0]);
		}
	}

	/**
	 * @param baseBean
	 *            BaseOracleBean
	 * @return CMSItem
	 */
	// private CMSItem fromBeanToObject(BaseOracleBean baseBean) {
	// RkItemVwOracleBean bean = (RkItemVwOracleBean) baseBean;
	// CMSItem object = new CMSItem(bean.getIdItm());
	// object.doSetLabel(bean.getLabel());
	// object.doSetForTheYear(bean.getFy());
	// object.doSetBarCode(bean.getBarcode());
	// object.doSetSubline(bean.getSubline());
	// object.doSetGender(bean.getGender());
	// object.doSetCategory(bean.getCategory());
	// object.doSetItemDrop(bean.getItemDrop());
	// object.doSetVariant(bean.getVariant());
	// object.doSetModel(bean.getModel());
	// object.doSetFabric(bean.getFabric());
	// object.doSetStyleNum(bean.getStyleNum());
	// object.doSetProductNum(bean.getProductNum());
	// object.doSetDescription(bean.getNmItm());
	// object.doSetRetailPrice(bean.getRetailValue());
	// object.doSetMarkdownAmount(bean.getMarkdownValue());
	// object.doSetTaxableValue(bean.getTaxableValue());
	// object.doSetVatRate(bean.getVatRate());
	// object.doSetScItemSls(bean.getScItmSls());
	// object.doSetNmBrn(bean.getNmBrn());
	// object.doSetNmClass(bean.getNmClass());
	// object.doSetColorId(bean.getDeColor());
	// object.doSetSeason(bean.getDeSeason());
	// object.doSetSizeId(bean.getDeSize());
	// object.doSetKidsSize(bean.getDeSizeKids());
	// object.doSetSupplierName(bean.getNmSpr());
	// object.doSetSubClassName(bean.getNmSbcl());
	// object.doSetStoreId(bean.getIdStrRt());
	// object.doSetCurrencyCode(bean.getCurrencyCode());
	// ArrayList al = new ArrayList();
	// al.add(bean.getIdRuPrdv());
	// object.doSetPromotionIds(al);
	// object.doSetPosDept(bean.getNmDptPos());
	// return object;
	// }
	/**
	 * @param object
	 *            CMSItem
	 * @return RkItemVwOracleBean
	 */
	private RkItemVwOracleBean fromObjectToBean(CMSItem object) {
		RkItemVwOracleBean bean = new RkItemVwOracleBean();
		CMSItemDetail itemDetail = object.getItemDetail();
		bean.setIdItm(object.getId());
		bean.setLabel(object.getLabel());
		bean.setFy(object.getForTheYear());
		bean.setBarcode(object.getBarCode());
		bean.setSubline(object.getSubline());
		bean.setGender(object.getGender());
		bean.setCategory(object.getCategory());
		bean.setItemDrop(object.getItemDrop());
		bean.setVariant(object.getVariant());
		bean.setModel(object.getModel());
		bean.setFabric(object.getFabric());
		bean.setStyleNum(object.getStyleNum());
		bean.setProductNum(object.getProductNum());
		bean.setRetailValue(object.getRetailPrice() == null ? null : object.getRetailPrice().getFormattedStringValue());
		bean.setMarkdownValue(object.getMarkdownAmount() == null ? null : object.getMarkdownAmount().getFormattedStringValue());
		if (object.isTaxable() == false)
			bean.setTaxableValue("00");
		else
			bean.setTaxableValue("10");
		bean.setVatRate(object.getVatRate() == null ? null : object.getVatRate().toString());
		bean.setScItmSls(object.getScItemSls());
		bean.setIdBrn(object.getBrand());
		bean.setIdClass(object.getClassId());
		bean.setDeColor(object.getColorId());
		bean.setDeSeason(object.getSeason());
		bean.setDeSize(object.getSizeId());
		bean.setDeSizeKids(object.getKidsSize());
		bean.setNmSpr(itemDetail.getSupplierName());
		bean.setIdSbcl(object.getSubClassId());
		bean.setNmSbcl(itemDetail.getSubClassDesc());
		bean.setIdStrRt(object.getStoreId());
		bean.setCurrencyCode(object.getCurrencyCode());
		bean.setIdRuPrdv(object.getPromotion());
		bean.setNmDptPos(object.getPosDept());
		bean.setDeColor(itemDetail.getColorDesc());
		bean.setDeDrop(itemDetail.getDropDesc());
		bean.setDeGender(itemDetail.getGenderDesc());
		bean.setDeProduct(itemDetail.getProductDesc());
		bean.setDeSeason(itemDetail.getSeasonDesc());
		bean.setDeSize(itemDetail.getSizeDesc());
		bean.setDeSizeKids(itemDetail.getKidsSizeDesc());
		bean.setDeSubline(itemDetail.getSubLineDesc());
		bean.setNmBrn(itemDetail.getBrandDesc());
		bean.setNmCategory(itemDetail.getCategoryDesc());
		bean.setNmClass(itemDetail.getClassDesc());
		bean.setNmDptPos(itemDetail.getPosDeptDesc());
		bean.setNmLabel(itemDetail.getLabelDesc());
		bean.setNmSbcl(itemDetail.getSubClassDesc());
		bean.setNmSpr(itemDetail.getSupplierName());
		bean.setProductNum(itemDetail.getProductDesc());
		bean.setUpdateDt(object.getUpdateDate());
		return bean;
	}

	/**
	 * @param store
	 *            CMSStore
	 * @throws SQLException
	 * @return Map
	 */
	public Map getSupplierSeasonYear(Store store) throws SQLException {
		Map map = new HashMap();
		Map supplierMap = new HashMap();
		Map seasonMap = new HashMap();
		Map yearMap = new HashMap();
		BaseOracleBean[] suppBeans = this.query(new ArmSprOracleBean(), ArmSprOracleBean.selectSql, null);
		for (int i = 0; i < suppBeans.length; i++) {
			ArmSprOracleBean bean = (ArmSprOracleBean) suppBeans[i];
			supplierMap.put(bean.getIdSpr(), bean.getNmSpr());
		}
		map.put("supplier", supplierMap);
		List params = new ArrayList();
		params.add(store.getPreferredISOCountry());
		BaseOracleBean[] seasonBeans = this.query(new ArmSeasonOracleBean(), BaseOracleDAO.where(ArmSeasonOracleBean.COL_ED_CO), params);
		for (int i = 0; i < seasonBeans.length; i++) {
			ArmSeasonOracleBean bean = (ArmSeasonOracleBean) seasonBeans[i];
			seasonMap.put(bean.getIdSeason(), bean.getDeSeason());
		}
		map.put("season", seasonMap);
		/*
		 * BaseOracleBean[] yearBeans = this.query(new ArmYearOracleBean(), ArmYearOracleBean.selectSql, null); for (int i=0; i<yearBeans.length;
		 * i++) { ArmYearOracleBean bean = (ArmYearOracleBean)yearBeans[i]; yearMap.put(bean.getIdYear(), bean.getNmYear()); } map.put("year",
		 * yearMap);
		 */
		return map;
	}

	/**
	 * @param searchString
	 *            ItemSearchString
	 * @throws SQLException
	 * @return CMSItem[]
	 */
	public CMSItem[] selectByItemSearchString(ItemSearchString searchString) throws SQLException {
		List params = new ArrayList();
		BaseOracleBean[] beans = this.query(new RkItemVwOracleBean(), searchString.buildQuery(), params);
		return this.fromBeansToObjects(beans);
	}

  /*
   //For testing only
   public static void main(String[] args) {
   com.chelseasystems.cs.store.CMSStore store = new com.chelseasystems.cs.store.CMSStore("051010");
   store.doSetCountry("US");
   ItemVwOracleDAO dao = new ItemVwOracleDAO();
   //    try {
   //      Map map = dao.getSupplierSeasonYear(store);
   //      Map sm = (Map)map.get("supplier");
   //      for (Iterator i = sm.keySet().iterator(); i.hasNext();
   //           System.out.println(sm.get((String)i.next())));
   //      System.out.println("_______________________________________________");
   //      sm = (HashMap)map.get("season");
   //      for (Iterator i = sm.keySet().iterator(); i.hasNext();
   //           System.out.println(sm.get((String)i.next())));
   //      System.out.println("_______________________________________________");
   //      sm = (Map)map.get("year");
   //      for (Iterator i = sm.keySet().iterator(); i.hasNext();
   //           System.out.println(sm.get((String)i.next())));
   //    } catch (Exception e) { e.printStackTrace();}
   try {
   ItemSearchString ss = new ItemSearchString();
   // ss.setSKU("19374470021");
   ss.setStore("051010");
   ss.setStyle("123");
   ss.setModel("u4V30");
   ss.setYear("03");
   //ss.setColor("Black");
   ss.setSupplier("700");
   ss.setFabric("85");
   ss.setSeason("02");
   ss.setIsAdvancedSearch(true);
   CMSItem[] itmList = dao.selectByItemSearchString(ss);
   if (itmList.length > 0) {
   System.out.println("----- Total Item = " + itmList.length + "----------");
   for (int i = 0; i < itmList.length; i++) {
   System.out.println(">>>>>>>>> Item <<<<<<<<<<<<<<<<<<<<<<");
   if (ss.isAdvancedSearch()) {
   System.out.println("store Name = " + itmList[i].getStoreName());
   System.out.println("quantity = "+ itmList[i].getItemStock().getQuantity());
   }
   System.out.println(itmList[i].getId());
   System.out.println(itmList[i].getStyleNum());
   System.out.println(itmList[i].getModel());
   System.out.println(itmList[i].getItemDetail().getColorDesc());
   System.out.println(itmList[i].getSizeId());
   }
   }
   } catch (Exception e) { e.printStackTrace();}
   }*/
}
