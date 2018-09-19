/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
//import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkItemVwOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.PaEmOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkTxnHeaderOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmFiscalDocumentOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmRtlTrnOracleBean;
import com.chelseasystems.cs.util.TransactionUtil;

import java.util.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;


/**
 * <p>Title: TransactionSearchString</p>
 * <p>Description:Builds search string for advance query </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 09-12-2005 | Manpreet  | 1006      | Modified query to search by color                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 07-27-2005 | Vikram    | 558       | End date in date range did not work properly.      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05-09-2005 | Manpreet  | N/A       | POS_104665_TS_TranactionHistory_Rev0               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class TransactionSearchString extends BusinessObject implements Serializable {
  private String sTransId;
  private String sCustomerId;
  private String sCustomerFirstName;
  private String sCustomerLastName;
  private String sStoreId;
  private String sCompanyCode;
  private String sRegisterId;
  private String sStoreBrand;
  private String sTransactionType;
  private String sAssociateId;
  private String sCashierId;
  private String sCurrencyCode;
  private String sSku;
  private String sModel;
  private String sStyle;
  private String sSupplier;
  private String sFabric;
  private String sColor;
  private String sYear;
  private String sSeason;
  private ArmCurrency curTransStartAmount;
  private ArmCurrency curTransEndAmount;
  private String sFiscalRecieptNum;
  private String sFiscalDocNum;
  private String sFiscalDocType;
  private Date dtProcess;
  private Date dtStart;
  private Date dtEnd;
  private boolean bIsShipping;
  private boolean bPerformSearch;
  private String sSearchedCriteria;
  private SimpleDateFormat dateFormat;
  private boolean bFiscalSearch;
  private String payType;
  ConfigMgr cfg = new ConfigMgr("pos.cfg");
  int maxRecords = cfg.getInt("MAX_RECORDS_TO_RETRIEVE");

  /**
   * put your documentation comment here
   */
  public TransactionSearchString() {
    dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
    sSearchedCriteria = new String("");
    sTransId = new String("");
    sCustomerId = new String("");
    sCustomerFirstName = new String("");
    sCustomerLastName = new String("");
    sStoreId = new String("");
    sCompanyCode = new String("");
    sRegisterId = new String("");
    sStoreBrand = new String("");
    sTransactionType = new String("");
    sFiscalRecieptNum = new String("");
    sFiscalDocNum = new String("");
    sFiscalDocType = new String("");
    curTransStartAmount = null;
    curTransEndAmount = null;
    dtStart = null;
    dtEnd = null;
    dtProcess = null;
    bIsShipping = false;
    bPerformSearch = false;
    bFiscalSearch = false;
    payType=new String("");
  }


  /**
   * put your documentation comment here
   * @return
   */
  public boolean isSearchRequired() {
    return bPerformSearch;
  }


  /**
   * put your documentation comment here
   * @param bSearch
   */
  public void setSearchRequired(boolean bSearch) {
    bPerformSearch = bSearch;
  }

  public void setFiscalSearch(boolean bSearch) {
    bFiscalSearch = bSearch;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCurrencyCode(String sValue) {
    this.sCurrencyCode = sValue;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setAssociate(String sValue) {
    this.sAssociateId = sValue;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCashier(String sValue) {
    this.sCashierId = sValue;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSku(String sValue) {
    this.sSku = sValue;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setModel(String sValue) {
    this.sModel = sValue;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setStyle(String sValue) {
    this.sStyle = sValue;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSupplier(String sValue) {
    this.sSupplier = sValue;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setFabric(String sValue) {
    this.sFabric = sValue;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setColor(String sValue) {
    this.sColor = sValue;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setYear(String sValue) {
    this.sYear = sValue;
  }


  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setSeason(String sValue) {
    this.sSeason = sValue;
  }


  /**
   * put your documentation comment here
   * @param bIsShipping
   */
  public void setIsShipping(boolean bIsShipping) {
    this.bIsShipping = bIsShipping;
  }


  /**
   * put your documentation comment here
   * @param curTransEndAmount
   */
  public void setTransactionEndAmount(ArmCurrency curTransEndAmount) {
    this.curTransEndAmount = curTransEndAmount;
  }


  /**
   * put your documentation comment here
   * @param curTransStartAmount
   */
  public void setTransactionStartAmount(ArmCurrency curTransStartAmount) {
    this.curTransStartAmount = curTransStartAmount;
  }


  /**
   * put your documentation comment here
   * @param dtEnd
   */
  public void setEndDate(Date dtEnd) {
    this.dtEnd = dtEnd;
  }


  /**
   * put your documentation comment here
   * @param dtProcess
   */
  public void setProcessDate(Date dtProcess) {
    this.dtProcess = dtProcess;
  }


  /**
   * put your documentation comment here
   * @param dtStart
   */
  public void setStartDate(Date dtStart) {
    this.dtStart = dtStart;
  }


  /**
   * put your documentation comment here
   * @param sCompanyCode
   */
  public void setCompanyCode(String sCompanyCode) {
    this.sCompanyCode = sCompanyCode;
  }


  /**
   * put your documentation comment here
   * @param sCustomerFirstName
   */
  public void setCustomerFirstName(String sCustomerFirstName) {
    this.sCustomerFirstName = sCustomerFirstName;
  }


  /**
   * put your documentation comment here
   * @param sCustomerId
   */
  public void setCustomerId(String sCustomerId) {
    this.sCustomerId = sCustomerId;
  }


  /**
   * put your documentation comment here
   * @param sCustomerLastName
   */
  public void setCustomerLastName(String sCustomerLastName) {
    this.sCustomerLastName = sCustomerLastName;
  }


  /**
   * put your documentation comment here
   * @param sFiscalDocNum
   */
  public void setFiscalDocNum(String sFiscalDocNum) {
    this.sFiscalDocNum = sFiscalDocNum;
  }

  public void setFiscalDocType(String sFiscalDocType) {
    this.sFiscalDocType = sFiscalDocType;
  }


  /**
   * put your documentation comment here
   * @param sFiscalRecieptNum
   */
  public void setFiscalRecieptNum(String sFiscalRecieptNum) {
    this.sFiscalRecieptNum = sFiscalRecieptNum;
  }


  /**
   * put your documentation comment here
   * @param sRegisterId
   */
  public void setRegisterId(String sRegisterId) {
    this.sRegisterId = sRegisterId;
  }


  /**
   * put your documentation comment here
   * @param sStoreBrand
   */
  public void setStoreBrand(String sStoreBrand) {
    this.sStoreBrand = sStoreBrand;
  }


  /**
   * put your documentation comment here
   * @param sStoreId
   */
  public void setStoreId(String sStoreId) {
    this.sStoreId = sStoreId;
  }


  /**
   * put your documentation comment here
   * @param sTransactionType
   */
  public void setTransactionType(String sTransactionType) {
    this.sTransactionType = sTransactionType;
  }


  /**
   * put your documentation comment here
   * @param sTransId
   */
  public void setTransactionId(String sTransId) {
    this.sTransId = sTransId;
  }

  /**
   * put your documentation comment here
   * @param payType String
   */
  public void setPayType(String payType) {
    this.payType = payType;
  }


  /**
   * put your documentation comment here
   * @return
   */
  public String getSearchCriteria() {
    return this.sSearchedCriteria;
  }

  public String buildFiscalQueryString(List params) {
    String sQuery = "";
    String sQueryPrefix = "";
    boolean bFirstParamFound = false;

    sQueryPrefix = "SELECT * FROM " + RkTxnHeaderOracleBean.TABLE_NAME;
    if (sTransId != null && sTransId.length() > 0) {
      sQuery += RkTxnHeaderOracleBean.COL_AI_TRN + " = ?";
      params.add(sTransId);
    } else {
      if (dtProcess != null) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dtProcess);
        cal.set(Calendar.HOUR,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        Date nextToEndDate = cal.getTime();
        sQuery += RkTxnHeaderOracleBean.COL_TS_TRN_SBM + " >= ? AND "
            + RkTxnHeaderOracleBean.COL_TS_TRN_SBM + " <= ?";
        params.add(dtProcess);
        params.add(nextToEndDate);
        bFirstParamFound = true;
      }
      if (sRegisterId != null && sRegisterId.length() > 0) {
        if (bFirstParamFound)
          sQuery += " AND ";
        sQuery += RkTxnHeaderOracleBean.COL_REGISTER_ID + " = ?";
        params.add(sRegisterId);
        bFirstParamFound = true;
      }
      if (sStoreId != null && sStoreId.length() > 0) {
        if (bFirstParamFound)
          sQuery += " AND ";
        sQuery += RkTxnHeaderOracleBean.COL_ID_STR_RT + " = ?";
        params.add(sStoreId);
        bFirstParamFound = true;
      } else {
        if (sCompanyCode != null && sCompanyCode.length() > 0) {
          if (bFirstParamFound)
            sQuery += " AND ";
          sQuery += "lower(" + RkTxnHeaderOracleBean.COL_ID_STR_RT + ") LIKE ?";
          params.add(("*%" + sCompanyCode + "*%").toLowerCase());
          bFirstParamFound = true;
        }
        if (sStoreBrand != null && sStoreBrand.length() > 0) {
          if (bFirstParamFound)
            sQuery += " AND ";
          sQuery += RkTxnHeaderOracleBean.COL_TY_STR_RT + " = ?";
          params.add(sStoreBrand);
          bFirstParamFound = true;
        }
      }

      if (sFiscalRecieptNum != null && sFiscalRecieptNum.length() > 0) {
        if (bFirstParamFound)sQuery += " AND ";
        sQuery += RkTxnHeaderOracleBean.COL_FISCAL_RECEIPT_NUM + " = ?";
        params.add(sFiscalRecieptNum);
        bFirstParamFound = true;
      }

      if ((sFiscalDocNum != null && sFiscalDocNum.length() > 0)
          ||
          (sFiscalDocType != null && sFiscalDocType.length() > 0)
          ) {
        if (bFirstParamFound)sQuery += " AND ";
        sQuery += " EXISTS ( SELECT * FROM " +
            ArmFiscalDocumentOracleBean.TABLE_NAME +
            " WHERE ";
//        sQuery += RkTxnHeaderOracleBean.COL_FISCAL_DOC_NUMBERS + " LIKE '*%?'||"
//            + ArmFiscalDocumentOracleBean.COL_DOC_NUM +
//            "||'*%' AND ";


        if ((sFiscalDocNum != null && sFiscalDocNum.length() > 0)
            &&
            (sFiscalDocType != null && sFiscalDocType.length() > 0)
            )
        {
          sQuery += RkTxnHeaderOracleBean.COL_FISCAL_DOC_NUMBERS + " LIKE '*%"+ sFiscalDocType.trim()+ "|'||"
              + ArmFiscalDocumentOracleBean.COL_DOC_NUM +
              "||'*%' AND ";
          sQuery += ArmFiscalDocumentOracleBean.COL_AI_TRN + " = " + RkTxnHeaderOracleBean.COL_AI_TRN + " AND " +
              ArmFiscalDocumentOracleBean.COL_DOC_NUM + " = ?";
          sQuery += " AND "+ArmFiscalDocumentOracleBean.COL_TY_DOC + " = ? )";
          params.add(sFiscalDocNum);
          params.add(sFiscalDocType);
        }
        else if (sFiscalDocType != null && sFiscalDocType.length() > 0)
        {
          sQuery += RkTxnHeaderOracleBean.COL_FISCAL_DOC_NUMBERS + " LIKE '*%"+ sFiscalDocType.trim()+ "|'||"
              + ArmFiscalDocumentOracleBean.COL_DOC_NUM +
              "||'*%' AND ";
          sQuery +=  ArmFiscalDocumentOracleBean.COL_AI_TRN + " = " + RkTxnHeaderOracleBean.COL_AI_TRN
              + " AND " + ArmFiscalDocumentOracleBean.COL_TY_DOC + " = ? )";
          params.add(sFiscalDocType);
        }
        else
        {
          sQuery += RkTxnHeaderOracleBean.COL_FISCAL_DOC_NUMBERS + " LIKE '*%'||"
              + ArmFiscalDocumentOracleBean.COL_DOC_NUM +
              "||'*%' AND ";
          sQuery +=  ArmFiscalDocumentOracleBean.COL_DOC_NUM + " = ? )";
          params.add(sFiscalDocNum);
        }
        bFirstParamFound = true;
      }

    }
    if (sQuery.length() > 0)
      return sQueryPrefix + " WHERE " + sQuery +" AND ROWNUM <= "+maxRecords;

    return sQuery;
  }


  /**
   * put your documentation comment here
   * @param params
   * @return
   */
  public String buildQuery(List params) {
    String sQuery = "";
    String sQueryPrefix = "";
    String sItmSelect = "";
    String sItemSelectInnerQuery = "";
    String sJoinSelect;
    boolean bFirstParamFound = false;
    boolean itemSearch = false;
    //sQueryPrefix = "SELECT * FROM " + RkTxnHeaderOracleBean.TABLE_NAME;
    sQueryPrefix = "SELECT " + RkTxnHeaderOracleBean.COL_AI_TRN + ", " +
    	RkTxnHeaderOracleBean.COL_CONSULTANT_ID + ", " + RkTxnHeaderOracleBean.COL_CURRENCY_CD + ", " + 
    	RkTxnHeaderOracleBean.COL_CUST_ID + ", " + RkTxnHeaderOracleBean.COL_DISCOUNT_TYPES + ", " + 
    	RkTxnHeaderOracleBean.COL_EXP_DT + ", " + RkTxnHeaderOracleBean.COL_FISCAL_DOC_NUMBERS + ", " + 
    	RkTxnHeaderOracleBean.COL_FISCAL_RECEIPT_DT + ", " + RkTxnHeaderOracleBean.COL_FISCAL_RECEIPT_NUM + ", " +  
    	RkTxnHeaderOracleBean.COL_FN_PRS + ", " + RkTxnHeaderOracleBean.COL_ID_OPR + ", " + 
    	RkTxnHeaderOracleBean.COL_ID_STR_RT + ", " + RkTxnHeaderOracleBean.COL_IS_SHIPPING + ", " +  
    	RkTxnHeaderOracleBean.COL_ITEMS_IDS + ", " + RkTxnHeaderOracleBean.COL_LN_PRS + ", " + 
    	RkTxnHeaderOracleBean.COL_NET_AMOUNT + ", " + RkTxnHeaderOracleBean.COL_PAY_TYPES + ", " + 
    	RkTxnHeaderOracleBean.COL_REDUCTION_AMOUNT + ", " + RkTxnHeaderOracleBean.COL_REF_ID + ", " + 
    	RkTxnHeaderOracleBean.COL_REGISTER_ID + ", " + RkTxnHeaderOracleBean.COL_STORE_DESC + ", " +
    	RkTxnHeaderOracleBean.COL_TOTAL_AMT + ", " + RkTxnHeaderOracleBean.COL_TS_TRN_PRC + ", " + 
    	RkTxnHeaderOracleBean.COL_TS_TRN_SBM + ", " + RkTxnHeaderOracleBean.COL_TY_GUI_TRN + ", " + 
    	RkTxnHeaderOracleBean.COL_TY_STR_RT + ", " + RkTxnHeaderOracleBean.COL_TY_TRN + 
    	" FROM " + RkTxnHeaderOracleBean.TABLE_NAME;
    
    sItemSelectInnerQuery = "RK_TXN_HEADER.AI_TRN IN" +
    	" (SELECT DISTINCT AI_TRN FROM TR_LTM_RTL_TRN WHERE ID_ITM IN" + 
    	" (SELECT ID_ITM FROM V_ARM_TXN_ITEM WHERE ";

    if (bFiscalSearch)return buildFiscalQueryString(params);
    if (sCustomerId != null && sCustomerId.length() > 0) {
      sQuery += RkTxnHeaderOracleBean.COL_CUST_ID + " = ?";
      params.add(sCustomerId);
      bFirstParamFound = true;
    }
    if (dtProcess != null) {
      if (bFirstParamFound)sQuery += " AND ";
      Calendar cal = Calendar.getInstance();
      cal.setTime(dtProcess);
      cal.set(Calendar.HOUR,23);
      cal.set(Calendar.MINUTE,59);
      cal.set(Calendar.SECOND,59);
      Date nextToEndDate = cal.getTime();
      sQuery += RkTxnHeaderOracleBean.COL_TS_TRN_SBM + " >= ? AND "
          + RkTxnHeaderOracleBean.COL_TS_TRN_SBM + " <= ?";
      params.add(dtProcess);
      params.add(nextToEndDate);
      bFirstParamFound = true;
    }
    if (sRegisterId != null && sRegisterId.length() > 0) {
      if (bFirstParamFound)
        sQuery += " AND ";
      sQuery += RkTxnHeaderOracleBean.COL_REGISTER_ID + " = ?";
      params.add(sRegisterId);
      bFirstParamFound = true;
    }
    if (sStoreId != null && sStoreId.length() > 0) {
      if (bFirstParamFound)
        sQuery += " AND ";
      sQuery += RkTxnHeaderOracleBean.COL_ID_STR_RT + " = ?";
      params.add(sStoreId);
      bFirstParamFound = true;
    } else {
      if (sCompanyCode != null && sCompanyCode.length() > 0) {
        if (bFirstParamFound)
          sQuery += " AND ";
        sQuery += "lower(" + RkTxnHeaderOracleBean.COL_ID_STR_RT + ") LIKE ?";
        params.add(("*%" + sCompanyCode + "*%").toLowerCase());
        bFirstParamFound = true;
      }
      if (sStoreBrand != null && sStoreBrand.length() > 0) {
        if (bFirstParamFound)
          sQuery += " AND ";
        sQuery += RkTxnHeaderOracleBean.COL_TY_STR_RT + " = ?";
        params.add(sStoreBrand);
        bFirstParamFound = true;
      }
    }

    if (sAssociateId != null && sAssociateId.length() > 0) {
      if (bFirstParamFound)
        sQuery += " AND ";
      //sQuery += RkTxnHeaderOracleBean.COL_CONSULTANT_ID + " = ?";
      sQuery += RkTxnHeaderOracleBean.COL_AI_TRN + " IN (SELECT " + TrLtmRtlTrnOracleBean.COL_AI_TRN + 
      	" FROM " + TrLtmRtlTrnOracleBean.TABLE_NAME + " INNER JOIN " + PaEmOracleBean.TABLE_NAME + 
      	" ON " + TrLtmRtlTrnOracleBean.COL_ADD_CONSULTANT_ID + " = " + PaEmOracleBean.COL_ID_EM + 
      	" AND " + PaEmOracleBean.COL_ARM_EXTERNAL_ID + " = ?)";
      params.add(sAssociateId);
      bFirstParamFound = true;
    }
    if (sCashierId != null && sCashierId.length() > 0) {
      if (bFirstParamFound)
        sQuery += " AND ";
      sQuery += RkTxnHeaderOracleBean.COL_ID_OPR + " = ?";
      params.add(sCashierId);
      bFirstParamFound = true;
    }
    if (sTransactionType != null && sTransactionType.length() > 0) {
      if (bFirstParamFound)
        sQuery += " AND ";
      Thread.dumpStack();
      sQuery += RkTxnHeaderOracleBean.COL_TY_GUI_TRN + " = ?";
      params.add(sTransactionType);
      bFirstParamFound = true;
    }

    if (payType != null && payType.length() > 0) {
        if (bFirstParamFound)
          sQuery += " AND ";
        sQuery += RkTxnHeaderOracleBean.COL_PAY_TYPES + " like ?";
        params.add("%*"+payType+"*%");
        bFirstParamFound = true;
      }

    if (sCurrencyCode != null && sCurrencyCode.length() > 0) {
      if (bFirstParamFound)
        sQuery += " AND ";
      sQuery += RkTxnHeaderOracleBean.COL_CURRENCY_CD + " = ?";
      params.add(sCurrencyCode.toUpperCase());
      bFirstParamFound = true;
    }
    if (curTransStartAmount != null && curTransEndAmount != null) {
      if (bFirstParamFound)
        sQuery += " AND ";
      sQuery += "Arm_Util_Pkg.Convert_To_Number(" + RkTxnHeaderOracleBean.COL_TOTAL_AMT;
      sQuery += ", " + RkTxnHeaderOracleBean.COL_CURRENCY_CD + ") BETWEEN ? AND ? ";
      params.add(new Double(curTransStartAmount.doubleValue()));
      params.add(new Double(curTransEndAmount.doubleValue()));
      bFirstParamFound = true;
    }
    if (dtStart != null && dtEnd != null) {
      //V.M.: Introduced nextToEndDate as dtEnd is 12:00am for end day and hence not inclusive
      Calendar cal = Calendar.getInstance();
      cal.setTime(dtEnd);
      cal.add(Calendar.DATE, 1);
      Date nextToEndDate = cal.getTime();
      if (bFirstParamFound)
        sQuery += " AND ";
      sQuery += RkTxnHeaderOracleBean.COL_TS_TRN_SBM + " >= ? AND "
          + RkTxnHeaderOracleBean.COL_TS_TRN_SBM + " < ?";
      params.add(dtStart);
      params.add(nextToEndDate);
      bFirstParamFound = true;
    }
    sJoinSelect = " EXISTS ( SELECT * FROM V_ARM_TXN_ITEM WHERE ";
    sJoinSelect += RkTxnHeaderOracleBean.COL_ITEMS_IDS
        + " LIKE '*%'||V_ARM_TXN_ITEM.ID_ITM||'*%' AND ";
    // ---- ITEM DETAILS -----
    if (sSku != null && sSku.length() > 0) {
      itemSearch = true;
      if (bFirstParamFound) {
    	  if (itemSearch) {
    		  sQuery += " AND ";
    	  } else {
        sQuery += " AND (";
    	  }
      }
        // Entered SKU could be sku or barcode
      sJoinSelect = " EXISTS ( SELECT * FROM V_ARM_TXN_ITEM WHERE (";
      sJoinSelect += RkTxnHeaderOracleBean.COL_ITEMS_IDS
          + " LIKE '*%'||V_ARM_TXN_ITEM.ID_ITM||'*%' AND V_ARM_TXN_ITEM.ID_ITM = ?) OR ("
          + RkTxnHeaderOracleBean.COL_ITEMS_IDS
          + " LIKE '*%'||V_ARM_TXN_ITEM.BARCODE||'*%' AND V_ARM_TXN_ITEM.BARCODE = ?))";
      //sItmSelect =  + "OR " + RkItemVwOracleBean.COL_BARCODE + " = ?)";
      if (itemSearch) {
    	  sQuery += sItemSelectInnerQuery + " (UPPER(V_ARM_TXN_ITEM.ID_ITM) = ? OR " + 
    	  	"UPPER(V_ARM_TXN_ITEM.BARCODE) = ?)))";
      } else {
      sQuery += sJoinSelect + " )";
      }
      params.add(sSku);
      params.add(sSku);
      return sQueryPrefix + " WHERE " + sQuery +" AND ROWNUM <= "+maxRecords;
    } else {
      sItmSelect = buildItemsQueryString(params);
    }
    if (sItmSelect.length() > 0) {
      itemSearch = true;
      if (bFirstParamFound) {    	  
        sQuery += " AND ";
      }
      if (itemSearch) {
    	sQuery += sItemSelectInnerQuery + sItmSelect + " ))";
      } else {
      sQuery += sJoinSelect + sItmSelect + " )";
    }
    }
    if (sQuery.length() > 0)
      return sQueryPrefix + " WHERE " + sQuery+" AND ROWNUM <= "+maxRecords;
    return sQueryPrefix ;
  }


  /**
   * put your documentation comment here
   * @param params
   * @return
   */
  private String buildItemsQueryString(List params) {
    String sTmp = "";
    if (sModel != null && sModel.length() > 0) {
      sTmp += "upper(V_ARM_TXN_ITEM.MODEL) = ?";
      params.add(sModel.toUpperCase());
    }
    if (sStyle != null && sStyle.length() > 0) {
      if (sTmp.length() > 0)
        sTmp += " AND ";
      sTmp += "upper(V_ARM_TXN_ITEM.STYLE_NUM) = ?";
      params.add(sStyle);
    }
    if (sSupplier != null && sSupplier.length() > 0) {
      if (sTmp.length() > 0)
        sTmp += " AND ";
      sTmp += "( upper(V_ARM_TXN_ITEM.ID_SPR) = ? OR ";
      sTmp += "upper(V_ARM_TXN_ITEM.NM_SPR) LIKE ? )";
      params.add(sSupplier.toUpperCase());
      params.add(sSupplier.toUpperCase()+"%");
    }
    if (sFabric != null && sFabric.length() > 0) {
      if (sTmp.length() > 0)
        sTmp += " AND ";
      sTmp += "upper(V_ARM_TXN_ITEM.FABRIC) = ?";
      params.add(sFabric);
    }
    if (sColor != null && sColor.length() > 0) {
      if (sTmp.length() > 0)
        sTmp += " AND ";
      sTmp += "( upper(V_ARM_TXN_ITEM.DE_COLOR) LIKE ? OR ";
      sTmp += "upper(V_ARM_TXN_ITEM.ID_COLOR) = ? )";
      params.add(sColor.toUpperCase()+"%");
      params.add(sColor.toUpperCase());
    }
    if (sYear != null && sYear.length() > 0) {
      if (sTmp.length() > 0)
        sTmp += " AND ";
      sTmp += "upper(V_ARM_TXN_ITEM.FY) = ?";
      params.add(sYear);
    }
    if (sSeason != null && sSeason.length() > 0) {
      if (sTmp.length() > 0)
        sTmp += " AND ";
      sTmp += "upper(V_ARM_TXN_ITEM.SEASON) = ?";
      params.add(sSeason);
    }
    return sTmp;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String toString() {
    if (isFiscalSearch()){
    	return TransactionUtil.getTransactionFiscalSearchString(this);
    }else{
    	return TransactionUtil.getTransactionSearchString(this);
    }
  }


	public boolean isFiscalSearch() {
		return bFiscalSearch;
	}


	public boolean isShipping() {
		return bIsShipping;
	}


	public boolean isPerformSearch() {
		return bPerformSearch;
	}


	public ArmCurrency getCurTransEndAmount() {
		return curTransEndAmount;
	}


	public ArmCurrency getCurTransStartAmount() {
		return curTransStartAmount;
	}


	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}


	public Date getEndDate() {
		return dtEnd;
	}


	public Date getDtProcess() {
		return dtProcess;
	}


	public Date getStartDate() {
		return dtStart;
	}


	public String getPayType() {
		return payType;
	}


	public String getAssociateId() {
		return sAssociateId;
	}


	public String getCashierId() {
		return sCashierId;
	}


	public String getColor() {
		return sColor;
	}


	public String getCompanyCode() {
		return sCompanyCode;
	}


	public String getCurrencyCode() {
		return sCurrencyCode;
	}


	public String getCustomerFirstName() {
		return sCustomerFirstName;
	}


	public String getCustomerId() {
		return sCustomerId;
	}


	public String getCustomerLastName() {
		return sCustomerLastName;
	}


	public String getFabric() {
		return sFabric;
	}


	public String getFiscalDocNum() {
		return sFiscalDocNum;
	}


	public String getFiscalDocType() {
		return sFiscalDocType;
	}


	public String getFiscalRecieptNum() {
		return sFiscalRecieptNum;
	}


	public String getModel() {
		return sModel;
	}


	public String getRegisterId() {
		return sRegisterId;
	}


	public String getSearchedCriteria() {
		return sSearchedCriteria;
	}


	public String getSeason() {
		return sSeason;
	}


	public String getSku() {
		return sSku;
	}


	public String getStoreBrand() {
		return sStoreBrand;
	}


	public String getStoreId() {
		return sStoreId;
	}


	public String getStyle() {
		return sStyle;
	}


	public String getSupplier() {
		return sSupplier;
	}


	public String getTransactionType() {
		return sTransactionType;
	}


	public String getTransId() {
		return sTransId;
	}


	public String getYear() {
		return sYear;
	}
}
