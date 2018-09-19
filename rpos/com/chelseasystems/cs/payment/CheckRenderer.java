/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 07/08/2005 | Vikram    | 411       | updated GUIPaymentDetail                           |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05/26/2005 | Vikram    | N/A       | updated GUIPaymentDetail                           |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CheckRenderer extends PaymentRenderer {

  /**
   * put your documentation comment here
   */
  public CheckRenderer() {
  }

  /**
   * @return
   */
  public String getGUIPaymentDetail() {
    //        if (getPayment() instanceof BusinessCheck) {
    //            BusinessCheck businessCheck = (BusinessCheck)getPayment();
    //            StringBuffer buf = new StringBuffer();
    //            if (null != businessCheck.getCheckMICRdata()) {
    //                buf.append("MICR: ");
    //                if (businessCheck.getIsCheckScannedIn()) {
    //                    buf.append(businessCheck.getCheckNumber() + "/" + businessCheck.getTransitNumber()
    //                            + "/" + businessCheck.getAccountNumber());
    //                }
    //                else {
    //                    buf.append(businessCheck.getCheckMICRdata());
    //                }
    //            }
    //            buf.append(" " + super.getGUIPaymentDetail());      // format foreign currency
    //            return  buf.toString();
    //        }
    //        else {
    //            BankCheck bankCheck = (BankCheck)getPayment();
    //            StringBuffer buf = new StringBuffer();
    //            if (null != bankCheck.getDriversLicenseNumber() && bankCheck.getDriversLicenseNumber().length() > 0) {
    //                buf.append("ID No. ");
    //                buf.append(bankCheck.getDriversLicenseNumber());
    //            }
    //            buf.append(" " + super.getGUIPaymentDetail());      // format foreign currency
    //            return  (buf.toString());
    //        }
    StringBuffer buf = new StringBuffer();
    if (getPayment() instanceof BankCheck) {
      BankCheck bankCheck = (BankCheck)getPayment();
      if (null != bankCheck.getCheckMICRdata()) {
        //VM: As per Bug #411, changed MICR string is:
        buf.append("T" + bankCheck.getTransitNumber() + "A" + bankCheck.getAccountNumber() + "C"
            + bankCheck.getCheckNumber());
      }
    }
    buf.append(" " + super.getGUIPaymentDetail()); // format foreign currency
    return buf.toString();
  }

  /**
   * @return
   */
  public String getMaskedPaymentDetail() {
    StringBuffer buf = new StringBuffer();
    if (getPayment() instanceof BankCheck) {
      BankCheck bankCheck = (BankCheck)getPayment();
      buf.append(bankCheck.getCheckNumber());
      buf.append(" " + super.getGUIPaymentDetail()); // format foreign currency
    } else if (getPayment() instanceof LocalCheck) {
      LocalCheck theCheck = (LocalCheck)getPayment();
      buf.append(" " + super.getGUIPaymentDetail()); // format foreign currency
    } else if (getPayment() instanceof OutOfAreaCheck) {
      OutOfAreaCheck theCheck = (OutOfAreaCheck)getPayment();
      buf.append(" " + super.getGUIPaymentDetail()); // format foreign currency
    }
    return buf.toString();
  }
}
