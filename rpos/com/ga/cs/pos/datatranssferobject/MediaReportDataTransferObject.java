/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import java.util.Hashtable;
import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.ga.cs.currency.ComparableCurrency;
import com.ga.cs.swing.report.ReportConstants;


/**
 * Data transfer object for Media Report
 * @author fbulah
 *
 */
public class MediaReportDataTransferObject extends BusinessObject implements java.io.Serializable {
  static final long serialVersionUID = 3100388933506077831L;
  protected int mediaCount;
  protected String typeId;
  protected ArmCurrency grossAmount;
  protected ArmCurrency creditReturn;
  protected ArmCurrency netAmount;
  protected String tenderClassCode;

  /**
   *
   */
  public MediaReportDataTransferObject() {
    mediaCount = 0;
    grossAmount = new ArmCurrency(0);
    creditReturn = new ArmCurrency(0);
    netAmount = new ArmCurrency(0);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getMediaCount() {
    return mediaCount;
  }

  /**
   * put your documentation comment here
   * @param mediaCount
   */
  public void setMediaCount(int mediaCount) {
    this.mediaCount = mediaCount;
  }

  /**
   * returns gross amount as a ComparableCurrency
   * @return
   */
  public ComparableCurrency getGrossAmount() {
    return new ComparableCurrency(grossAmount.getDoubleValue());
  }

  /**
   * put your documentation comment here
   * @param grossAmount
   */
  public void setGrossAmount(ArmCurrency grossAmount) {
    this.grossAmount = new ArmCurrency(grossAmount.doubleValue());
  }

  /**
   * returns net amount as a ComparableCurrency
   * @return
   */
  public ComparableCurrency getNetAmount() {
    return new ComparableCurrency(netAmount.getDoubleValue());
  }

  /**
   * calculates net amount = gross amount + credit returns [RPOS stores credit/returns as negative]
   */
  public void setNetAmount() {
    try {
      if ((grossAmount != null) && (creditReturn != null)) {
        this.netAmount = new ArmCurrency(grossAmount.add(creditReturn).doubleValue());
      }
    } catch (CurrencyException e) {
      System.out.println("setNetAmount: CurrencyException: msg=" + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param netAmount
   */
  public void setNetAmount(ArmCurrency netAmount) {
    this.netAmount = new ArmCurrency(netAmount.doubleValue());
  }

  /**
   * returns credit return as a ComparableCurrency
   * @return
   */
  public ComparableCurrency getCreditReturn() {
    return new ComparableCurrency(creditReturn.getDoubleValue());
  }

  /**
   * put your documentation comment here
   * @param creditReturn
   */
  public void setCreditReturn(ArmCurrency creditReturn) {
    this.creditReturn = new ArmCurrency(creditReturn.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTypeId() {
    return typeId;
  }

  /**
   * put your documentation comment here
   * @param typeId
   */
  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDisplayTypeId() {
    return ReportConstants.getPaymentType(typeId);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getMediaCountString() {
    return "" + mediaCount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGrossAmountString() {
    return grossAmount.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCreditReturnString() {
    return creditReturn.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getNetAmountString() {
    return netAmount.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTenderClassCode() {
    return tenderClassCode;
  }

  /**
   * put your documentation comment here
   * @param tenderClassCode
   */
  public void setTenderClassCode(String tenderClassCode) {
    this.tenderClassCode = tenderClassCode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String toString() {
    StringBuffer sb = new StringBuffer(super.toString());
    sb.append("[");
    sb.append(this.mediaCount);
    sb.append("][");
    sb.append(this.typeId);
    sb.append("][");
    sb.append(this.grossAmount);
    sb.append("][");
    sb.append(this.creditReturn);
    sb.append("][");
    sb.append(this.netAmount);
    sb.append("][");
    sb.append(this.tenderClassCode);
    sb.append("]");
    return sb.toString();
  }

  /**
   * put your documentation comment here
   * @param that
   * @return
   * @exception CurrencyException
   */
  public MediaReportDataTransferObject add(MediaReportDataTransferObject that)
      throws CurrencyException {
    if (that == null) {
      return (MediaReportDataTransferObject)this.clone();
    }
    if (this.typeId.equals(that.typeId)) {
      MediaReportDataTransferObject sum = new MediaReportDataTransferObject();
      sum.typeId = this.typeId;
      sum.mediaCount = this.mediaCount + that.mediaCount;
      sum.grossAmount = this.grossAmount.add(that.grossAmount);
      sum.creditReturn = this.creditReturn.add(that.creditReturn);
      sum.netAmount = this.netAmount.add(that.netAmount);
      return sum;
    } else {
      Exception exp = new Exception("Tender types are not the same.  Cannot be added.");
      System.out.println("Exception --> " + exp);
      exp.printStackTrace();
      return null;
    }
  }

  /**
   * put your documentation comment here
   * @param array
   * @return
   * @exception CurrencyException
   */
  public static Hashtable groupByTypes(MediaReportDataTransferObject[] array)
      throws CurrencyException {
    if (array == null || array.length == 0) {
      return new Hashtable();
    }
    Hashtable table = new Hashtable();
    for (int i = 0; i < array.length; i++) {
      String type = array[i].getTypeId();
      if (type != null && type.trim().equalsIgnoreCase(ReportConstants.PAYMENT_TYPE_BCRD)) {
        type = ReportConstants.getPaymentType(array[i].getTenderClassCode());
      }
      Object obj = table.get(type);
      MediaReportDataTransferObject sum = array[i].add((MediaReportDataTransferObject)obj);
      table.put(type, sum);
    }
    return table;
  }
}

