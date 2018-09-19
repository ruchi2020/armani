//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;
import com.chelseasystems.cr.currency.ArmCurrency;

/**
 *
 * This class is an object representation of the Arts database table RK_ITEM_VW<BR>
 * Followings are the column of the table: <BR>
 *     ID_ITM -- VARCHAR2(128)<BR>
 *     BARCODE -- VARCHAR2(50)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     RETAIL_VALUE -- VARCHAR2(255)<BR>
 *     MARKDOWN_VALUE -- VARCHAR2(255)<BR>
 *     TAXABLE_VALUE -- VARCHAR2(255)<BR>
 *     VAT_RATE -- VARCHAR2(255)<BR>
 *     FY -- CHAR(4)<BR>
 *     SEASON -- VARCHAR2(6)<BR>
 *     DE_SEASON -- VARCHAR2(30)<BR>
 *     ID_BRN -- VARCHAR2(6)<BR>
 *     NM_BRN -- VARCHAR2(30)<BR>
 *     LABEL -- VARCHAR2(30)<BR>
 *     NM_LABEL -- VARCHAR2(30)<BR>
 *     SUBLINE -- VARCHAR2(30)<BR>
 *     DE_SUBLINE -- VARCHAR2(30)<BR>
 *     GENDER -- VARCHAR2(30)<BR>
 *     DE_GENDER -- VARCHAR2(30)<BR>
 *     CATEGORY -- VARCHAR2(30)<BR>
 *     NM_CATEGORY -- VARCHAR2(30)<BR>
 *     ITEM_DROP -- VARCHAR2(30)<BR>
 *     DE_DROP -- VARCHAR2(30)<BR>
 *     VARIANT -- VARCHAR2(30)<BR>
 *     ID_SIZE -- VARCHAR2(6)<BR>
 *     DE_SIZE -- VARCHAR2(30)<BR>
 *     SIZE_INDEX -- VARCHAR2(10)<BR>
 *     EXT_SIZE_INDEX -- VARCHAR2(10)<BR>
 *     ID_SIZE_KIDS -- VARCHAR2(6)<BR>
 *     DE_SIZE_KIDS -- VARCHAR2(30)<BR>
 *     ID_SPR -- VARCHAR2(10)<BR>
 *     NM_SPR -- VARCHAR2(30)<BR>
 *     MODEL -- VARCHAR2(10)<BR>
 *     FABRIC -- VARCHAR2(10)<BR>
 *     ID_COLOR -- VARCHAR2(6)<BR>
 *     DE_COLOR -- VARCHAR2(30)<BR>
 *     PRODUCT_NUM -- VARCHAR2(30)<BR>
 *     DE_PRODUCT -- VARCHAR2(30)<BR>
 *     NM_ITM -- VARCHAR2(40)<BR>
 *     ID_DPT_POS -- VARCHAR2(128)<BR>
 *     NM_DPT_POS -- VARCHAR2(40)<BR>
 *     ID_CLASS -- VARCHAR2(128)<BR>
 *     NM_CLASS -- VARCHAR2(10)<BR>
 *     ID_SBCL -- VARCHAR2(128)<BR>
 *     NM_SBCL -- VARCHAR2(20)<BR>
 *     STYLE_NUM -- VARCHAR2(10)<BR>
 *     SC_ITM_SLS -- VARCHAR2(255)<BR>
 *     CURRENCY_CODE -- VARCHAR2(50)<BR>
 *     FL_EN_PRC_RQ -- VARCHAR2(255)<BR>
 *     ID_RU_PRDV -- VARCHAR2(128)<BR>
 *     QU_AVAILABLE -- NUMBER(4)<BR>
 *     QU_UNAVAILABLE -- NUMBER(4)<BR>
 *     QU_STORE_AVAILABLE -- NUMBER(4)<BR>
 *     QU_STORE_UNAVAILABLE -- NUMBER(4)<BR>
 *     NM_ORGN -- VARCHAR2(40)<BR>
 *     UPDATE_DT -- DATE(7)<BR>
 *
 */
public class RkItemVwOracleBean extends BaseOracleBean {

  public RkItemVwOracleBean() {}

  public static String selectSql = "select ID_ITM, BARCODE, ID_STR_RT, RETAIL_VALUE, MARKDOWN_VALUE, TAXABLE_VALUE, VAT_RATE, FY, SEASON, DE_SEASON, ID_BRN, NM_BRN, LABEL, NM_LABEL, SUBLINE, DE_SUBLINE, GENDER, DE_GENDER, CATEGORY, NM_CATEGORY, ITEM_DROP, DE_DROP, VARIANT, ID_SIZE, DE_SIZE, SIZE_INDEX, EXT_SIZE_INDEX, ID_SIZE_KIDS, DE_SIZE_KIDS, ID_SPR, NM_SPR, MODEL, FABRIC, ID_COLOR, DE_COLOR, PRODUCT_NUM, DE_PRODUCT, NM_ITM, ID_DPT_POS, NM_DPT_POS, ID_CLASS, NM_CLASS, ID_SBCL, NM_SBCL, STYLE_NUM, SC_ITM_SLS, CURRENCY_CODE, FL_EN_PRC_RQ, ID_RU_PRDV, QU_AVAILABLE, QU_UNAVAILABLE, QU_STORE_AVAILABLE, QU_STORE_UNAVAILABLE, NM_ORGN, UPDATE_DT from RK_ITEM_VW ";
  public static String insertSql = "insert into RK_ITEM_VW (ID_ITM, BARCODE, ID_STR_RT, RETAIL_VALUE, MARKDOWN_VALUE, TAXABLE_VALUE, VAT_RATE, FY, SEASON, DE_SEASON, ID_BRN, NM_BRN, LABEL, NM_LABEL, SUBLINE, DE_SUBLINE, GENDER, DE_GENDER, CATEGORY, NM_CATEGORY, ITEM_DROP, DE_DROP, VARIANT, ID_SIZE, DE_SIZE, SIZE_INDEX, EXT_SIZE_INDEX, ID_SIZE_KIDS, DE_SIZE_KIDS, ID_SPR, NM_SPR, MODEL, FABRIC, ID_COLOR, DE_COLOR, PRODUCT_NUM, DE_PRODUCT, NM_ITM, ID_DPT_POS, NM_DPT_POS, ID_CLASS, NM_CLASS, ID_SBCL, NM_SBCL, STYLE_NUM, SC_ITM_SLS, CURRENCY_CODE, FL_EN_PRC_RQ, ID_RU_PRDV, QU_AVAILABLE, QU_UNAVAILABLE, QU_STORE_AVAILABLE, QU_STORE_UNAVAILABLE, NM_ORGN, UPDATE_DT) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update RK_ITEM_VW set ID_ITM = ?, BARCODE = ?, ID_STR_RT = ?, RETAIL_VALUE = ?, MARKDOWN_VALUE = ?, TAXABLE_VALUE = ?, VAT_RATE = ?, FY = ?, SEASON = ?, DE_SEASON = ?, ID_BRN = ?, NM_BRN = ?, LABEL = ?, NM_LABEL = ?, SUBLINE = ?, DE_SUBLINE = ?, GENDER = ?, DE_GENDER = ?, CATEGORY = ?, NM_CATEGORY = ?, ITEM_DROP = ?, DE_DROP = ?, VARIANT = ?, ID_SIZE = ?, DE_SIZE = ?, SIZE_INDEX = ?, EXT_SIZE_INDEX = ?, ID_SIZE_KIDS = ?, DE_SIZE_KIDS = ?, ID_SPR = ?, NM_SPR = ?, MODEL = ?, FABRIC = ?, ID_COLOR = ?, DE_COLOR = ?, PRODUCT_NUM = ?, DE_PRODUCT = ?, NM_ITM = ?, ID_DPT_POS = ?, NM_DPT_POS = ?, ID_CLASS = ?, NM_CLASS = ?, ID_SBCL = ?, NM_SBCL = ?, STYLE_NUM = ?, SC_ITM_SLS = ?, CURRENCY_CODE = ?, FL_EN_PRC_RQ = ?, ID_RU_PRDV = ?, QU_AVAILABLE = ?, QU_UNAVAILABLE = ?, QU_STORE_AVAILABLE = ?, QU_STORE_UNAVAILABLE = ?, NM_ORGN = ?, UPDATE_DT = ? ";
  public static String deleteSql = "delete from RK_ITEM_VW ";

  // Changes for issue 1858
  public static String COL_ID_ITM_ = "ID_ITM";
  public static String COL_MODEL_ = "MODEL";
  public static String COL_FABRIC_ = "FABRIC";
  public static String COL_CURRENCY_CODE_ = "CURRENCY_CODE";
  public static String COL_BARCODE_ = "BARCODE";
  
  public static String TABLE_NAME = "RK_ITEM_VW";
  public static String COL_ID_ITM = "RK_ITEM_VW.ID_ITM";
  public static String COL_BARCODE = "RK_ITEM_VW.BARCODE";
  public static String COL_ID_STR_RT = "RK_ITEM_VW.ID_STR_RT";
  public static String COL_RETAIL_VALUE = "RK_ITEM_VW.RETAIL_VALUE";
  public static String COL_MARKDOWN_VALUE = "RK_ITEM_VW.MARKDOWN_VALUE";
  public static String COL_TAXABLE_VALUE = "RK_ITEM_VW.TAXABLE_VALUE";
  public static String COL_VAT_RATE = "RK_ITEM_VW.VAT_RATE";
  public static String COL_FY = "RK_ITEM_VW.FY";
  public static String COL_SEASON = "RK_ITEM_VW.SEASON";
  public static String COL_DE_SEASON = "RK_ITEM_VW.DE_SEASON";
  public static String COL_ID_BRN = "RK_ITEM_VW.ID_BRN";
  public static String COL_NM_BRN = "RK_ITEM_VW.NM_BRN";
  public static String COL_LABEL = "RK_ITEM_VW.LABEL";
  public static String COL_NM_LABEL = "RK_ITEM_VW.NM_LABEL";
  public static String COL_SUBLINE = "RK_ITEM_VW.SUBLINE";
  public static String COL_DE_SUBLINE = "RK_ITEM_VW.DE_SUBLINE";
  public static String COL_GENDER = "RK_ITEM_VW.GENDER";
  public static String COL_DE_GENDER = "RK_ITEM_VW.DE_GENDER";
  public static String COL_CATEGORY = "RK_ITEM_VW.CATEGORY";
  public static String COL_NM_CATEGORY = "RK_ITEM_VW.NM_CATEGORY";
  public static String COL_ITEM_DROP = "RK_ITEM_VW.ITEM_DROP";
  public static String COL_DE_DROP = "RK_ITEM_VW.DE_DROP";
  public static String COL_VARIANT = "RK_ITEM_VW.VARIANT";
  public static String COL_ID_SIZE = "RK_ITEM_VW.ID_SIZE";
  public static String COL_DE_SIZE = "RK_ITEM_VW.DE_SIZE";
  public static String COL_SIZE_INDEX = "RK_ITEM_VW.SIZE_INDEX";
  public static String COL_EXT_SIZE_INDEX = "RK_ITEM_VW.EXT_SIZE_INDEX";
  public static String COL_ID_SIZE_KIDS = "RK_ITEM_VW.ID_SIZE_KIDS";
  public static String COL_DE_SIZE_KIDS = "RK_ITEM_VW.DE_SIZE_KIDS";
  public static String COL_ID_SPR = "RK_ITEM_VW.ID_SPR";
  public static String COL_NM_SPR = "RK_ITEM_VW.NM_SPR";
  public static String COL_MODEL = "RK_ITEM_VW.MODEL";
  public static String COL_FABRIC = "RK_ITEM_VW.FABRIC";
  public static String COL_ID_COLOR = "RK_ITEM_VW.ID_COLOR";
  public static String COL_DE_COLOR = "RK_ITEM_VW.DE_COLOR";
  public static String COL_PRODUCT_NUM = "RK_ITEM_VW.PRODUCT_NUM";
  public static String COL_DE_PRODUCT = "RK_ITEM_VW.DE_PRODUCT";
  public static String COL_NM_ITM = "RK_ITEM_VW.NM_ITM";
  public static String COL_ID_DPT_POS = "RK_ITEM_VW.ID_DPT_POS";
  public static String COL_NM_DPT_POS = "RK_ITEM_VW.NM_DPT_POS";
  public static String COL_ID_CLASS = "RK_ITEM_VW.ID_CLASS";
  public static String COL_NM_CLASS = "RK_ITEM_VW.NM_CLASS";
  public static String COL_ID_SBCL = "RK_ITEM_VW.ID_SBCL";
  public static String COL_NM_SBCL = "RK_ITEM_VW.NM_SBCL";
  public static String COL_STYLE_NUM = "RK_ITEM_VW.STYLE_NUM";
  public static String COL_SC_ITM_SLS = "RK_ITEM_VW.SC_ITM_SLS";
  public static String COL_CURRENCY_CODE = "RK_ITEM_VW.CURRENCY_CODE";
  public static String COL_FL_EN_PRC_RQ = "RK_ITEM_VW.FL_EN_PRC_RQ";
  public static String COL_ID_RU_PRDV = "RK_ITEM_VW.ID_RU_PRDV";
  public static String COL_QU_AVAILABLE = "RK_ITEM_VW.QU_AVAILABLE";
  public static String COL_QU_UNAVAILABLE = "RK_ITEM_VW.QU_UNAVAILABLE";
  public static String COL_QU_STORE_AVAILABLE = "RK_ITEM_VW.QU_STORE_AVAILABLE";
  public static String COL_QU_STORE_UNAVAILABLE = "RK_ITEM_VW.QU_STORE_UNAVAILABLE";
  public static String COL_NM_ORGN = "RK_ITEM_VW.NM_ORGN";
  public static String COL_UPDATE_DT = "RK_ITEM_VW.UPDATE_DT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idItm;
  private String barcode;
  private String idStrRt;
  private String retailValue;
  private String markdownValue;
  private String taxableValue;
  private String vatRate;
  private String fy;
  private String season;
  private String deSeason;
  private String idBrn;
  private String nmBrn;
  private String label;
  private String nmLabel;
  private String subline;
  private String deSubline;
  private String gender;
  private String deGender;
  private String category;
  private String nmCategory;
  private String itemDrop;
  private String deDrop;
  private String variant;
  private String idSize;
  private String deSize;
  private String sizeIndex;
  private String extSizeIndex;
  private String idSizeKids;
  private String deSizeKids;
  private String idSpr;
  private String nmSpr;
  private String model;
  private String fabric;
  private String idColor;
  private String deColor;
  private String productNum;
  private String deProduct;
  private String nmItm;
  private String idDptPos;
  private String nmDptPos;
  private String idClass;
  private String nmClass;
  private String idSbcl;
  private String nmSbcl;
  private String styleNum;
  private String scItmSls;
  private String currencyCode;
  private String flEnPrcRq;
  private String idRuPrdv;
  private Long quAvailable;
  private Long quUnavailable;
  private Long quStoreAvailable;
  private Long quStoreUnavailable;
  private String nmOrgn;
  private Date updateDt;

  public String getIdItm() { return this.idItm; }
  public void setIdItm(String idItm) { this.idItm = idItm; }

  public String getBarcode() { return this.barcode; }
  public void setBarcode(String barcode) { this.barcode = barcode; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getRetailValue() { return this.retailValue; }
  public void setRetailValue(String retailValue) { this.retailValue = retailValue; }

  public String getMarkdownValue() { return this.markdownValue; }
  public void setMarkdownValue(String markdownValue) { this.markdownValue = markdownValue; }

  public String getTaxableValue() { return this.taxableValue; }
  public void setTaxableValue(String taxableValue) { this.taxableValue = taxableValue; }

  public String getVatRate() { return this.vatRate; }
  public void setVatRate(String vatRate) { this.vatRate = vatRate; }

  public String getFy() { return this.fy; }
  public void setFy(String fy) { this.fy = fy; }

  public String getSeason() { return this.season; }
  public void setSeason(String season) { this.season = season; }

  public String getDeSeason() { return this.deSeason; }
  public void setDeSeason(String deSeason) { this.deSeason = deSeason; }

  public String getIdBrn() { return this.idBrn; }
  public void setIdBrn(String idBrn) { this.idBrn = idBrn; }

  public String getNmBrn() { return this.nmBrn; }
  public void setNmBrn(String nmBrn) { this.nmBrn = nmBrn; }

  public String getLabel() { return this.label; }
  public void setLabel(String label) { this.label = label; }

  public String getNmLabel() { return this.nmLabel; }
  public void setNmLabel(String nmLabel) { this.nmLabel = nmLabel; }

  public String getSubline() { return this.subline; }
  public void setSubline(String subline) { this.subline = subline; }

  public String getDeSubline() { return this.deSubline; }
  public void setDeSubline(String deSubline) { this.deSubline = deSubline; }

  public String getGender() { return this.gender; }
  public void setGender(String gender) { this.gender = gender; }

  public String getDeGender() { return this.deGender; }
  public void setDeGender(String deGender) { this.deGender = deGender; }

  public String getCategory() { return this.category; }
  public void setCategory(String category) { this.category = category; }

  public String getNmCategory() { return this.nmCategory; }
  public void setNmCategory(String nmCategory) { this.nmCategory = nmCategory; }

  public String getItemDrop() { return this.itemDrop; }
  public void setItemDrop(String itemDrop) { this.itemDrop = itemDrop; }

  public String getDeDrop() { return this.deDrop; }
  public void setDeDrop(String deDrop) { this.deDrop = deDrop; }

  public String getVariant() { return this.variant; }
  public void setVariant(String variant) { this.variant = variant; }

  public String getIdSize() { return this.idSize; }
  public void setIdSize(String idSize) { this.idSize = idSize; }

  public String getDeSize() { return this.deSize; }
  public void setDeSize(String deSize) { this.deSize = deSize; }

  public String getSizeIndex() { return this.sizeIndex; }
  public void setSizeIndex(String sizeIndex) { this.sizeIndex = sizeIndex; }

  public String getExtSizeIndex() { return this.extSizeIndex; }
  public void setExtSizeIndex(String extSizeIndex) { this.extSizeIndex = extSizeIndex; }

  public String getIdSizeKids() { return this.idSizeKids; }
  public void setIdSizeKids(String idSizeKids) { this.idSizeKids = idSizeKids; }

  public String getDeSizeKids() { return this.deSizeKids; }
  public void setDeSizeKids(String deSizeKids) { this.deSizeKids = deSizeKids; }

  public String getIdSpr() { return this.idSpr; }
  public void setIdSpr(String idSpr) { this.idSpr = idSpr; }

  public String getNmSpr() { return this.nmSpr; }
  public void setNmSpr(String nmSpr) { this.nmSpr = nmSpr; }

  public String getModel() { return this.model; }
  public void setModel(String model) { this.model = model; }

  public String getFabric() { return this.fabric; }
  public void setFabric(String fabric) { this.fabric = fabric; }

  public String getIdColor() { return this.idColor; }
  public void setIdColor(String idColor) { this.idColor = idColor; }

  public String getDeColor() { return this.deColor; }
  public void setDeColor(String deColor) { this.deColor = deColor; }

  public String getProductNum() { return this.productNum; }
  public void setProductNum(String productNum) { this.productNum = productNum; }

  public String getDeProduct() { return this.deProduct; }
  public void setDeProduct(String deProduct) { this.deProduct = deProduct; }

  public String getNmItm() { return this.nmItm; }
  public void setNmItm(String nmItm) { this.nmItm = nmItm; }

  public String getIdDptPos() { return this.idDptPos; }
  public void setIdDptPos(String idDptPos) { this.idDptPos = idDptPos; }

  public String getNmDptPos() { return this.nmDptPos; }
  public void setNmDptPos(String nmDptPos) { this.nmDptPos = nmDptPos; }

  public String getIdClass() { return this.idClass; }
  public void setIdClass(String idClass) { this.idClass = idClass; }

  public String getNmClass() { return this.nmClass; }
  public void setNmClass(String nmClass) { this.nmClass = nmClass; }

  public String getIdSbcl() { return this.idSbcl; }
  public void setIdSbcl(String idSbcl) { this.idSbcl = idSbcl; }

  public String getNmSbcl() { return this.nmSbcl; }
  public void setNmSbcl(String nmSbcl) { this.nmSbcl = nmSbcl; }

  public String getStyleNum() { return this.styleNum; }
  public void setStyleNum(String styleNum) { this.styleNum = styleNum; }

  public String getScItmSls() { return this.scItmSls; }
  public void setScItmSls(String scItmSls) { this.scItmSls = scItmSls; }

  public String getCurrencyCode() { return this.currencyCode; }
  public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

  public String getFlEnPrcRq() { return this.flEnPrcRq; }
  public void setFlEnPrcRq(String flEnPrcRq) { this.flEnPrcRq = flEnPrcRq; }
  //public void setFlEnPrcRq(boolean flEnPrcRq) { this.flEnPrcRq = new Boolean(flEnPrcRq); }

  public String getIdRuPrdv() { return this.idRuPrdv; }
  public void setIdRuPrdv(String idRuPrdv) { this.idRuPrdv = idRuPrdv; }

  public Long getQuAvailable() { return this.quAvailable; }
  public void setQuAvailable(Long quAvailable) { this.quAvailable = quAvailable; }
  public void setQuAvailable(long quAvailable) { this.quAvailable = new Long(quAvailable); }
  public void setQuAvailable(int quAvailable) { this.quAvailable = new Long((long)quAvailable); }

  public Long getQuUnavailable() { return this.quUnavailable; }
  public void setQuUnavailable(Long quUnavailable) { this.quUnavailable = quUnavailable; }
  public void setQuUnavailable(long quUnavailable) { this.quUnavailable = new Long(quUnavailable); }
  public void setQuUnavailable(int quUnavailable) { this.quUnavailable = new Long((long)quUnavailable); }

  public Long getQuStoreAvailable() { return this.quStoreAvailable; }
  public void setQuStoreAvailable(Long quStoreAvailable) { this.quStoreAvailable = quStoreAvailable; }
  public void setQuStoreAvailable(long quStoreAvailable) { this.quStoreAvailable = new Long(quStoreAvailable); }
  public void setQuStoreAvailable(int quStoreAvailable) { this.quStoreAvailable = new Long((long)quStoreAvailable); }

  public Long getQuStoreUnavailable() { return this.quStoreUnavailable; }
  public void setQuStoreUnavailable(Long quStoreUnavailable) { this.quStoreUnavailable = quStoreUnavailable; }
  public void setQuStoreUnavailable(long quStoreUnavailable) { this.quStoreUnavailable = new Long(quStoreUnavailable); }
  public void setQuStoreUnavailable(int quStoreUnavailable) { this.quStoreUnavailable = new Long((long)quStoreUnavailable); }

  public String getNmOrgn() { return this.nmOrgn; }
  public void setNmOrgn(String nmOrgn) { this.nmOrgn = nmOrgn; }

  public Date getUpdateDt() { return this.updateDt; }
  public void setUpdateDt(Date updateDt) { this.updateDt = updateDt; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkItemVwOracleBean bean = new RkItemVwOracleBean();
      bean.idItm = getStringFromResultSet(rs, "ID_ITM");
      bean.barcode = getStringFromResultSet(rs, "BARCODE");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.retailValue = getStringFromResultSet(rs, "RETAIL_VALUE");
      bean.markdownValue = getStringFromResultSet(rs, "MARKDOWN_VALUE");
      bean.taxableValue = getStringFromResultSet(rs, "TAXABLE_VALUE");
      bean.vatRate = getStringFromResultSet(rs, "VAT_RATE");
      bean.fy = getStringFromResultSet(rs, "FY");
      bean.season = getStringFromResultSet(rs, "SEASON");
      bean.deSeason = getStringFromResultSet(rs, "DE_SEASON");
      bean.idBrn = getStringFromResultSet(rs, "ID_BRN");
      bean.nmBrn = getStringFromResultSet(rs, "NM_BRN");
      bean.label = getStringFromResultSet(rs, "LABEL");
      bean.nmLabel = getStringFromResultSet(rs, "NM_LABEL");
      bean.subline = getStringFromResultSet(rs, "SUBLINE");
      bean.deSubline = getStringFromResultSet(rs, "DE_SUBLINE");
      bean.gender = getStringFromResultSet(rs, "GENDER");
      bean.deGender = getStringFromResultSet(rs, "DE_GENDER");
      bean.category = getStringFromResultSet(rs, "CATEGORY");
      bean.nmCategory = getStringFromResultSet(rs, "NM_CATEGORY");
      bean.itemDrop = getStringFromResultSet(rs, "ITEM_DROP");
      bean.deDrop = getStringFromResultSet(rs, "DE_DROP");
      bean.variant = getStringFromResultSet(rs, "VARIANT");
      bean.idSize = getStringFromResultSet(rs, "ID_SIZE");
      bean.deSize = getStringFromResultSet(rs, "DE_SIZE");
      bean.sizeIndex = getStringFromResultSet(rs, "SIZE_INDEX");
      bean.extSizeIndex = getStringFromResultSet(rs, "EXT_SIZE_INDEX");
      bean.idSizeKids = getStringFromResultSet(rs, "ID_SIZE_KIDS");
      bean.deSizeKids = getStringFromResultSet(rs, "DE_SIZE_KIDS");
      bean.idSpr = getStringFromResultSet(rs, "ID_SPR");
      bean.nmSpr = getStringFromResultSet(rs, "NM_SPR");
      bean.model = getStringFromResultSet(rs, "MODEL");
      bean.fabric = getStringFromResultSet(rs, "FABRIC");
      bean.idColor = getStringFromResultSet(rs, "ID_COLOR");
      bean.deColor = getStringFromResultSet(rs, "DE_COLOR");
      bean.productNum = getStringFromResultSet(rs, "PRODUCT_NUM");
      bean.deProduct = getStringFromResultSet(rs, "DE_PRODUCT");
      bean.nmItm = getStringFromResultSet(rs, "NM_ITM");
      bean.idDptPos = getStringFromResultSet(rs, "ID_DPT_POS");
      bean.nmDptPos = getStringFromResultSet(rs, "NM_DPT_POS");
      bean.idClass = getStringFromResultSet(rs, "ID_CLASS");
      bean.nmClass = getStringFromResultSet(rs, "NM_CLASS");
      bean.idSbcl = getStringFromResultSet(rs, "ID_SBCL");
      bean.nmSbcl = getStringFromResultSet(rs, "NM_SBCL");
      bean.styleNum = getStringFromResultSet(rs, "STYLE_NUM");
      bean.scItmSls = getStringFromResultSet(rs, "SC_ITM_SLS");
      bean.currencyCode = getStringFromResultSet(rs, "CURRENCY_CODE");
      bean.flEnPrcRq = getStringFromResultSet(rs, "FL_EN_PRC_RQ");
      bean.idRuPrdv = getStringFromResultSet(rs, "ID_RU_PRDV");
      bean.quAvailable = getLongFromResultSet(rs, "QU_AVAILABLE");
      bean.quUnavailable = getLongFromResultSet(rs, "QU_UNAVAILABLE");
      bean.quStoreAvailable = getLongFromResultSet(rs, "QU_STORE_AVAILABLE");
      bean.quStoreUnavailable = getLongFromResultSet(rs, "QU_STORE_UNAVAILABLE");
      bean.nmOrgn = getStringFromResultSet(rs, "NM_ORGN");
      bean.updateDt = getDateFromResultSet(rs, "UPDATE_DT");
      list.add(bean);
    }
    return (RkItemVwOracleBean[]) list.toArray(new RkItemVwOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdItm(), Types.VARCHAR);
    addToList(list, this.getBarcode(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getRetailValue(), Types.VARCHAR);
    addToList(list, this.getMarkdownValue(), Types.VARCHAR);
    addToList(list, this.getTaxableValue(), Types.VARCHAR);
    addToList(list, this.getVatRate(), Types.VARCHAR);
    addToList(list, this.getFy(), Types.VARCHAR);
    addToList(list, this.getSeason(), Types.VARCHAR);
    addToList(list, this.getDeSeason(), Types.VARCHAR);
    addToList(list, this.getIdBrn(), Types.VARCHAR);
    addToList(list, this.getNmBrn(), Types.VARCHAR);
    addToList(list, this.getLabel(), Types.VARCHAR);
    addToList(list, this.getNmLabel(), Types.VARCHAR);
    addToList(list, this.getSubline(), Types.VARCHAR);
    addToList(list, this.getDeSubline(), Types.VARCHAR);
    addToList(list, this.getGender(), Types.VARCHAR);
    addToList(list, this.getDeGender(), Types.VARCHAR);
    addToList(list, this.getCategory(), Types.VARCHAR);
    addToList(list, this.getNmCategory(), Types.VARCHAR);
    addToList(list, this.getItemDrop(), Types.VARCHAR);
    addToList(list, this.getDeDrop(), Types.VARCHAR);
    addToList(list, this.getVariant(), Types.VARCHAR);
    addToList(list, this.getIdSize(), Types.VARCHAR);
    addToList(list, this.getDeSize(), Types.VARCHAR);
    addToList(list, this.getSizeIndex(), Types.VARCHAR);
    addToList(list, this.getExtSizeIndex(), Types.VARCHAR);
    addToList(list, this.getIdSizeKids(), Types.VARCHAR);
    addToList(list, this.getDeSizeKids(), Types.VARCHAR);
    addToList(list, this.getIdSpr(), Types.VARCHAR);
    addToList(list, this.getNmSpr(), Types.VARCHAR);
    addToList(list, this.getModel(), Types.VARCHAR);
    addToList(list, this.getFabric(), Types.VARCHAR);
    addToList(list, this.getIdColor(), Types.VARCHAR);
    addToList(list, this.getDeColor(), Types.VARCHAR);
    addToList(list, this.getProductNum(), Types.VARCHAR);
    addToList(list, this.getDeProduct(), Types.VARCHAR);
    addToList(list, this.getNmItm(), Types.VARCHAR);
    addToList(list, this.getIdDptPos(), Types.VARCHAR);
    addToList(list, this.getNmDptPos(), Types.VARCHAR);
    addToList(list, this.getIdClass(), Types.VARCHAR);
    addToList(list, this.getNmClass(), Types.VARCHAR);
    addToList(list, this.getIdSbcl(), Types.VARCHAR);
    addToList(list, this.getNmSbcl(), Types.VARCHAR);
    addToList(list, this.getStyleNum(), Types.VARCHAR);
    addToList(list, this.getScItmSls(), Types.VARCHAR);
    addToList(list, this.getCurrencyCode(), Types.VARCHAR);
    addToList(list, this.getFlEnPrcRq(), Types.VARCHAR);
    addToList(list, this.getIdRuPrdv(), Types.VARCHAR);
    addToList(list, this.getQuAvailable(), Types.DECIMAL);
    addToList(list, this.getQuUnavailable(), Types.DECIMAL);
    addToList(list, this.getQuStoreAvailable(), Types.DECIMAL);
    addToList(list, this.getQuStoreUnavailable(), Types.DECIMAL);
    addToList(list, this.getNmOrgn(), Types.VARCHAR);
    addToList(list, this.getUpdateDt(), Types.TIMESTAMP);
    return list;
  }

}
