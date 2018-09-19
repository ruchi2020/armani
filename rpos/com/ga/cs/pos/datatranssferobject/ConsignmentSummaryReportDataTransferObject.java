/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.util.DateUtil;
import com.ga.cs.swing.report.ReportConstants;


/**
 * put your documentation comment here
 */
public class ConsignmentSummaryReportDataTransferObject implements Serializable, Comparable {
  static final long serialVersionUID = 4045170348226177161L;
  private String transactionId;
  private Date time;
  private String consignmentId;
  private String customerId;
  private String consultantId;
  private ArmCurrency price;
  private Long quantity;
  private String customerLastName;
  private String customerFistName;

  /**
   * put your documentation comment here
   */
  public ConsignmentSummaryReportDataTransferObject() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCustomerNumberAndName() {
    return this.getCustomerLastName() + ", " + this.getCustomerFistName() + "-"
        + this.getCustomerId();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCreationDate() {
    SimpleDateFormat sdf = new SimpleDateFormat(ReportConstants.DATE_FORMAT_SLASH_MDY);
    return sdf.format(this.getTime());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ConsignmentSummaryItemData getAged_0_15() {
    return getAgedInRange(15, 0);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ConsignmentSummaryItemData getAged_16_30() {
    return getAgedInRange(30, 16);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ConsignmentSummaryItemData getAged_31_60() {
    return getAgedInRange(60, 31);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ConsignmentSummaryItemData getAged_61() {
    return getAgedInRange(36500, 61);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ConsignmentSummaryItemData getTotals() {
    return new ConsignmentSummaryItemData(this.getQuantity().toString()
        , this.getPrice().formattedStringValue());
  }

  /**
   * @return Returns the customerLastName.
   */
  public String getCustomerLastName() {
    return customerLastName;
  }

  /**
   * @param customerLastName
   *            The customerLastName to set.
   */
  public void setCustomerLastName(String customerLastName) {
    this.customerLastName = customerLastName;
  }

  /**
   * @return Returns the price.
   */
  public ArmCurrency getPrice() {
    return price;
  }

  /**
   * @param price
   *            The price to set.
   */
  public void setPrice(ArmCurrency price) {
    this.price = price;
  }

  /**
   * @return Returns the quantity.
   */
  public Long getQuantity() {
    return quantity;
  }

  /**
   * @param quantity
   *            The quantity to set.
   */
  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  /**
   * @return Returns the time.
   */
  public Date getTime() {
    return time;
  }

  /**
   * @param time
   *            The time to set.
   */
  public void setTime(Date time) {
    this.time = time;
  }

  /**
   * @return Returns the transactionId.
   */
  public String getTransactionId() {
    return transactionId;
  }

  /**
   * @param transactionId
   *            The transactionId to set.
   */
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  /**
   * @return Returns the consignmentId.
   */
  public String getConsignmentId() {
    return consignmentId;
  }

  /**
   * @param consignmentId
   *            The consignmentId to set.
   */
  public void setConsignmentId(String consignmentId) {
    this.consignmentId = consignmentId;
  }

  /**
   * @return Returns the consultantId.
   */
  public String getConsultantId() {
    return consultantId;
  }

  /**
   * @param consultantId
   *            The consultantId to set.
   */
  public void setConsultantId(String consultantId) {
    this.consultantId = consultantId;
  }

  /**
   * @return Returns the customerFistName.
   */
  public String getCustomerFistName() {
    return customerFistName;
  }

  /**
   * @param customerFistName
   *            The customerFistName to set.
   */
  public void setCustomerFistName(String customerFistName) {
    this.customerFistName = customerFistName;
  }

  /**
   * @return Returns the customerId.
   */
  public String getCustomerId() {
    return customerId;
  }

  /**
   * @param customerId The customerId to set.
   */
  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  /**
   * put your documentation comment here
   * @param qty
   * @param amount
   * @exception CurrencyException
   */
  public void addQtyAndPrice(Long qty, ArmCurrency amount)
      throws CurrencyException {
    if (this.getQuantity() == null) {
      this.setQuantity(qty);
    } else {
      this.setQuantity(new Long(this.getQuantity().longValue() + qty.longValue()));
    }
    if (this.getPrice() == null) {
      this.setPrice(amount);
    } else {
      this.setPrice(this.getPrice().add(amount));
    }
  }

  /**
   * put your documentation comment here
   * @param beginDaysAgo
   * @param endDaysAgo
   * @return
   */
  private ConsignmentSummaryItemData getAgedInRange(int beginDaysAgo, int endDaysAgo) {
    Date begin = DateUtil.getBeginingOfDay(ConsignmentSummaryReportDataTransferObject.calculateDate( -
        beginDaysAgo));
    Date end = DateUtil.getEndOfDay(ConsignmentSummaryReportDataTransferObject.calculateDate( -
        endDaysAgo));
    if (ConsignmentSummaryReportDataTransferObject.areInOrder(begin, this.getTime(), end)) {
      return new ConsignmentSummaryItemData(this.getQuantity().toString()
          , this.getPrice().formattedStringValue());
    } else {
      return new ConsignmentSummaryItemData();
    }
  }

  /**
   * put your documentation comment here
   * @param date1
   * @param date2
   * @param date3
   * @return
   */
  private static boolean areInOrder(Date date1, Date date2, Date date3) {
    if (date1 == null || date2 == null || date3 == null) {
      return false;
    }
    return date1.before(date2) && date2.before(date3);
  }

  private static final long DAY = 86400000L; //24*60*60*1000L;

  /**
   * put your documentation comment here
   * @param date
   * @param daysLater
   * @return
   */
  private static Date calculateDate(Date date, int daysLater) {
    long time1 = date.getTime();
    long time2 = time1 + daysLater * DAY;
    return new Date(time2);
  }

  /**
   * put your documentation comment here
   * @param daysLater
   * @return
   */
  private static Date calculateDate(int daysLater) {
    return calculateDate(new Date(), daysLater);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  public int compareTo(Object object) {
    if (!(object instanceof ConsignmentSummaryReportDataTransferObject)) {
      return 0;
    }
    ConsignmentSummaryReportDataTransferObject that = (ConsignmentSummaryReportDataTransferObject)
        object;
    int diff = this.getCustomerNumberAndName().toUpperCase().compareTo(that.
        getCustomerNumberAndName().toUpperCase());
    if (diff != 0) {
      return diff;
    } else {
      return this.getConsignmentId().compareTo(that.getConsignmentId());
    }
  }
}

