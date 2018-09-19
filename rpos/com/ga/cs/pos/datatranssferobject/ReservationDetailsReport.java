/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05/03/2006 | Sandhya   | N/A       | Reservation Details Report                         |
 -------------------------------------------------------------------------------------------------- 
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.ga.cs.pos.datatranssferobject;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.ga.cs.dataaccess.artsoracle.databean.ReservationDetailsReportBean;
import com.ga.cs.swing.report.ReportConstants;


/**
 * put your documentation comment here
 */
public class ReservationDetailsReport implements java.io.Serializable {
  static final long serialVersionUID = -6572138789941892281L;
  public ReservationDetailsReportBean bean;

  /**
   * put your documentation comment here
   * @return
   */
  public String toString() {
    return bean.toString();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAssociateId() {
    return this.bean.associateId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAssociateName() {
    return this.bean.associateName;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getItemStatus() {
    return this.bean.itemStatus;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[] toStringArray() {
    String[] array = new String[10];
    array[0] = bean.associateName;
    //padding to get the itemDesc in next line
    String itemStr = bean.itemId;
    while(itemStr.length()<15) {
      itemStr = itemStr + " ";
    }
    array[1] = itemStr + " " + bean.itemDesc;
    array[2] = new SimpleDateFormat(ReportConstants.DATE_FORMAT_SLASH_MDY).format(bean.dateIssued);
    array[3] = "" + bean.origQty;
    array[4] = bean.origAmt.formattedStringValue();
    array[5] = bean.itemStatus;
    array[6] = (bean.dateSoldReturn == null) ? ""
        : new SimpleDateFormat(ReportConstants.DATE_FORMAT_SLASH_MDY).format(bean.dateSoldReturn);
    array[7] = "" + bean.remainingQty;
    array[8] = bean.remainingAmt.formattedStringValue();
    array[9] = bean.transactionId;
    return array;
  }

  /**
   * put your documentation comment here
   * @param colZero
   * @param totalOrigQty
   * @param totalOrigAmt
   * @param totalRemainQty
   * @param totalRemainAmt
   * @return
   */
  public static String[] getTotalArray(String colZero, int totalOrigQty, ArmCurrency totalOrigAmt
      , int totalRemainQty, ArmCurrency totalRemainAmt) {
    String[] array = new String[10];
    array[0] = colZero;
    array[1] = "";
    array[2] = "";
    array[3] = "" + totalOrigQty;
    array[4] = totalOrigAmt.formattedStringValue();
    array[5] = "";
    array[6] = "";
    array[7] = "" + totalRemainQty;
    array[8] = totalRemainAmt.formattedStringValue();
    array[9] = "";
    return array;
  }

  /**
   * put your documentation comment here
   * @param all
   * @param associateId
   * @return
   */
  public static ReservationDetailsReport[] filterByAssociate(ReservationDetailsReport[] all
      , String associateId) {
    if (all == null || all.length == 0) {
      return new ReservationDetailsReport[0];
    }
    if (associateId == null || associateId.length() == 0) {
      return all;
    }
    List list = new ArrayList();
    for (int i = 0; i < all.length; i++) {
      if (all[i].getAssociateId().equals(associateId.trim())) {
        list.add(all[i]);
      }
    }
    return (ReservationDetailsReport[])list.toArray(new ReservationDetailsReport[0]);
  }

  /**
   * put your documentation comment here
   * @param all
   * @param associateId
   * @return
   */
  public static ReservationDetailsReport[] filterOpenByAssociate(ReservationDetailsReport[] all
      , String associateId) {
    if (all == null || all.length == 0) {
      return new ReservationDetailsReport[0];
    }
    List list = new ArrayList();
    for (int i = 0; i < all.length; i++) {
      if (all[i].getItemStatus().equals("OPEN")) {
        if (associateId == null || associateId.length() == 0
            || all[i].getAssociateId().equals(associateId.trim())) {
          list.add(all[i]);
        }
      }
    }
    return (ReservationDetailsReport[])list.toArray(new ReservationDetailsReport[0]);
  }

  /**
   * put your documentation comment here
   * @param all
   * @param associateId
   * @return
   */
  public static ReservationDetailsReport[] filterCloseByAssociate(ReservationDetailsReport[] all
      , String associateId) {
    if (all == null || all.length == 0) {
      return new ReservationDetailsReport[0];
    }
    List list = new ArrayList();
    for (int i = 0; i < all.length; i++) {
      if (!all[i].getItemStatus().equals("OPEN")) { //NOT OPEN is CLOSED; ie returned or sold
        if (associateId == null || associateId.length() == 0
            || all[i].getAssociateId().equals(associateId.trim())) {
          list.add(all[i]);
        }
      }
    }
    return (ReservationDetailsReport[])list.toArray(new ReservationDetailsReport[0]);
  }
}
