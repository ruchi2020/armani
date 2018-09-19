/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.fiscaldocument;

import  java.util.Date;
import  com.chelseasystems.cr.currency.ArmCurrency;


/**
 * <p>Title: TaxFree</p>
 * <p>Description:TaxFree </p>
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
 | 1    | 05-26-2005 | Manpreet  | N/A       | POS_104665_TS_FiscalDocuments_Rev0                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class TaxFree extends FiscalDocument {
  private String sDetaxCode;
  private Date dtIssue;
  private String sIDType;
  private String sPaymentType;
  private String sPlaceOfIssue;
  // Added for loading the TaxFree data
  private String storeCode;
  private String descCode;
  private Double ratio;
  private Date startDate;
  private ArmCurrency netMinVal;
  private ArmCurrency grossMinValue;
  private String paymentCode;
  private ArmCurrency minPrice;
  private ArmCurrency maxPrice;
  private ArmCurrency minAmount;
  private ArmCurrency maxAmount;
  private String refundPercentage;
  private String commission;

  /**
   * Default Constructor
   */
  public TaxFree () {
    sDetaxCode = new String();
    sIDType = new String();
    sPaymentType = new String();
    sPlaceOfIssue = new String();
    dtIssue = null;
    storeCode = new String();
    descCode = new String();
    ratio = new Double(0.0);
    startDate = null;
    netMinVal = new ArmCurrency(0.0);
    grossMinValue = new ArmCurrency(0.0);
    paymentCode = new String();
    minPrice = new ArmCurrency(0.0);
    maxPrice = new ArmCurrency(0.0);
    minAmount = new ArmCurrency(0.0);
    maxAmount = new ArmCurrency(0.0);
    refundPercentage = new String();
    commission = new String();
  }

  /**
   * DetaxCode
   * @return String
   */
  public void setDetaxCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    doSetDetaxCode(sValue);
  }

  /**
   * DetaxCode
   * @param sValue String
   */
  public void doSetDetaxCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sDetaxCode = sValue;
  }

  /**
   * DetaxCode
   * @return String
   */
  public String getDetaxCode () {
    return  this.sDetaxCode;
  }

  /**
   * PaymentType
   * @return String
   */
  public void setPaymentType (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    doSetPaymentType(sValue);
  }

  /**
   * PaymentType
   * @param sValue String
   */
  public void doSetPaymentType (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }

    this.sPaymentType = sValue;
  }

  /**
   * PaymentType
   * @return String
   */
  public String getPaymentType () {
    return  this.sPaymentType;
  }

  /**
   * PlaceOfIssue
   * @return String
   */
  public void setPlaceOfIssue (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    doSetPlaceOfIssue(sValue);
  }

  /**
   * PlaceOfIssue
   * @param sValue String
   */
  public void doSetPlaceOfIssue (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }

    this.sPlaceOfIssue = sValue;
  }

  /**
   * PlaceOfIssue
   * @return String
   */
  public String getPlaceOfIssue () {
    return  this.sPlaceOfIssue;
  }

  /**
   * IDType
   * @return String
   */
  public void setIDType (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    doSetIDType(sValue);
  }

  /**
   * IDType
   * @param sValue String
   */
  public void doSetIDType (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sIDType = sValue;
  }

  /**
   * IDType
   * @return String
   */
  public String getIDType () {
    return  this.sIDType;
  }

  /**
   * IssueDate
   * @param Date dtFiscal
   */
  public void setIssueDate (Date dtFiscal) {
    if (dtFiscal == null) {
      return;
    }
    doSetIssueDate(dtFiscal);
  }

  /**
   * IssueDate
   * @param Date dtFiscal
   */
  public void doSetIssueDate (Date dtFiscal) {
    this.dtIssue = dtFiscal;
  }

  /**
   * IssueDate
   * @return Date IssueDate
   */
  public Date getIssueDate () {
    return  this.dtIssue;
  }

  /**
   * storeCode
   * @return String
   */
  public void setStoreCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    doSetStoreCode(sValue);
  }

  /**
   *storeCode
   * @param sValue String
   */
  public void doSetStoreCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.storeCode = sValue;
  }

  /**
   * storeCode
   * @return String
   */
  public String getStoreCode () {
    return  this.storeCode;
  }

  /**
   * descCode
   * @return String
   */
  public void setDescCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    doSetDescCode(sValue);
  }

  /**
   *storeCode
   * @param sValue String
   */
  public void doSetDescCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.descCode = sValue;
  }

  /**
   * storeCode
   * @return String
   */
  public String getDescCode () {
    return  this.descCode;
  }

  /**
   * Ratio
   * @return String
   */
  public void setRatio (Double sValue) {
    doSetRatio(sValue);
  }

  /**
   * Ratio
   * @param sValue String
   */
  public void doSetRatio (Double sValue) {
    this.ratio = sValue;
  }

  /**
   * Ratio
   * @return String
   */
  public Double getRatio () {
    return  this.ratio;
  }

  /**
   * Date
   * @return Date
   */
  public void setStartDate (Date sValue) {
    doSetStartDate(sValue);
  }

  /**
   * Date
   * @param sValue Date
   */
  public void doSetStartDate (Date sValue) {
    this.startDate = sValue;
  }

  /**
   * Date
   * @return Date
   */
  public Date getStartDate () {
    return  this.startDate;
  }

  /**
   * NetMinVal
   * @return Currency
   */
  public void setNetMinVal (ArmCurrency sValue) {
    doSetNetMinVal(sValue);
  }

  /**
   * NetMinVal
   * @param sValue Currency
   */
  public void doSetNetMinVal (ArmCurrency sValue) {
    this.netMinVal = sValue;
  }

  /**
   * NetMinVal
   * @return Currency
   */
  public ArmCurrency getNetMinVal () {
    return  this.netMinVal;
  }

  /**
   * GrossMinValue
   * @return Currency
   */
  public void setGrossMinValue (ArmCurrency sValue) {
    doSetGrossMinValue(sValue);
  }

  /**
   * GrossMinValue
   * @param sValue Currency
   */
  public void doSetGrossMinValue (ArmCurrency sValue) {
    this.grossMinValue = sValue;
  }

  /**
   * GrossMinValue
   * @return Currency
   */
  public ArmCurrency getGrossMinValue () {
    return  this.grossMinValue;
  }


  /**
   * PaymentCode
   * @return String
   */
  public void setPaymentCode (String sValue) {
    doSetPaymentCode(sValue);
  }

  /**
   * PaymentCode
   * @param sValue String
   */
  public void doSetPaymentCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.paymentCode = sValue;
  }

  /**
   * PaymentCode
   * @return String
   */
  public String getPaymentCode () {
    return  this.paymentCode;
  }

  /**
   * MinPrice
   * @return Currency
   */
  public void setMinPrice (ArmCurrency sValue) {
    doSetMinPrice(sValue);
  }

  /**
   * MinPrice
   * @param sValue Currency
   */
  public void doSetMinPrice (ArmCurrency sValue) {
    this.minPrice = sValue;
  }

  /**
   * MinPrice
   * @return Currency
   */
  public ArmCurrency getMinPrice () {
    return  this.minPrice;
  }


  /**
   * MaxPrice
   * @return Currency
   */
  public void setMaxPrice (ArmCurrency sValue) {
    doSetMaxPrice(sValue);
  }

  /**
   * MaxPrice
   * @param sValue Currency
   */
  public void doSetMaxPrice (ArmCurrency sValue) {
    this.maxPrice = sValue;
  }

  /**
   * MaxPrice
   * @return Currency
   */
  public ArmCurrency getMaxPrice () {
    return  this.maxPrice;
  }


  /**
   * MinAmount
   * @return Currency
   */
  public void setMinAmount (ArmCurrency sValue) {
    doSetMinAmount(sValue);
  }

  /**
   * MinAmount
   * @param sValue Currency
   */
  public void doSetMinAmount (ArmCurrency sValue) {
    this.minAmount = sValue;
  }

  /**
   * MinAmount
   * @return Currency
   */
  public ArmCurrency getMinAmount () {
    return  this.minAmount;
  }

  /**
   * MaxAmount
   * @return Currency
   */
  public void setMaxAmount (ArmCurrency sValue) {
    doSetMaxAmount(sValue);
  }

  /**
   * MaxAmount
   * @param sValue Currency
   */
  public void doSetMaxAmount (ArmCurrency sValue) {
    this.maxAmount = sValue;
  }

  /**
   * MaxAmount
   * @return Currency
   */
  public ArmCurrency getMaxAmount () {
    return  this.maxAmount;
  }

  /**
   * refundPercentage
   * @return String
   */
  public void setRefundPercentage (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    doSetRefundPercentage(sValue);
  }

  /**
   * RefundPercentage
   * @param sValue String
   */
  public void doSetRefundPercentage (String sValue) {
    this.refundPercentage = sValue;
  }

  /**
   * RefundPercentage
   * @return String
   */
  public String getRefundPercentage () {
    return  this.refundPercentage;
  }

  /**
   * commision
   * @return String
   */
  public void setCommission (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    doSetCommission(sValue);
  }

  /**
   * RefundPercentage
   * @param sValue String
   */
  public void doSetCommission (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.commission = sValue;
  }

  /**
   * RefundPercentage
   * @return String
   */
  public String getCommission () {
    return  this.commission;
  }
}



