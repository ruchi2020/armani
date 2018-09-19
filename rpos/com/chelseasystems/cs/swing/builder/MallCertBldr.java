/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;


/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 09-08-2005 | Manpreet  | 902       | Read Mall Cert typs from ArmaniCommon.cfg    |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | N/A        | N?A       | N/A       | Initial version                              |
 +------+------------+-----------+-----------+----------------------------------------------+
 */
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cs.payment.MallCert;
import com.chelseasystems.cs.swing.*;
import java.util.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.dlg.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Enumeration;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.util.ArmConfigLoader;


/**
 */
public class MallCertBldr implements IObjectBuilder {
  private MallCert mallCert = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private boolean searchMode;
  private String command;
  private Hashtable htLabels;
  private static String types[];
  private CMSPaymentTransactionAppModel theTxn = null;


  /**
   */
  public MallCertBldr() {
  }


  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
  }


  /**
   */
  public void cleanup() {}


  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("MALLCERT")) {
      //mallCert.setAmount((ArmCurrency)theEvent);
      String paymentTypeView = mallCert.getGUIPaymentName();
      String paymentType = mallCert.getTransactionPaymentName();
      if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
          , (ArmCurrency)theEvent))
        mallCert.setAmount((ArmCurrency)theEvent);
      completeAttributes();
    }
    if (completeAttributes()) {
      Store store = (Store)theAppMgr.getGlobalObject("STORE");
      CMSRegister register = (CMSRegister)theAppMgr.getGlobalObject("REGISTER");
      ((MallCert)mallCert).setStoreId(store.getId());
      ((MallCert)mallCert).setRegisterId(register.getId());
      theBldrMgr.processObject(applet, "PAYMENT", mallCert, this);
      mallCert = null;
    }
  }


  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    this.applet = applet;
    this.command = Command;
    mallCert = new MallCert((String)initValue);
    if (completeAttributes()) {
      Store store = (Store)theAppMgr.getGlobalObject("STORE");
      CMSRegister register = (CMSRegister)theAppMgr.getGlobalObject("REGISTER");
      ((MallCert)mallCert).setStoreId(store.getId());
      ((MallCert)mallCert).setRegisterId(register.getId());
      theBldrMgr.processObject(applet, "PAYMENT", mallCert, this);
      mallCert = null;
    }
  }


  /**
   * @return
   */
  private boolean completeAttributes() {
    if (mallCert.getAmount() == null) {
      // Issue # 711
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amtLeft = appModel.getCompositeTotalAmountDue().subtract(theTxn.
            getTotalPaymentAmount());
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "MALLCERT"
            , amtLeft.absoluteValue(), theAppMgr.CURRENCY_MASK);
        return false;
      } catch (Exception e) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "MALLCERT"
            , theAppMgr.CURRENCY_MASK);
        e.printStackTrace();
        return false;
      }
    } else {
      if (isListRequired()) {
        if (((MallCert)mallCert).getType() == null) {
          createMallCertDlg();
          //theAppMgr.setSingleEditArea(applet.res.getString("Enter coupon type or 'S' for search."), "TYPE");
          return false;
        } else
          return true;
      } else
        return true;
    }
    //return true;
  }


  /**
   * put your documentation comment here
   */
  private void createMallCertDlg() {
    // MSB - 09/08/05 - Read MallCert Types from ArmaniCommon.Cfg
    //        ConfigMgr config = new ConfigMgr("mallcert.cfg");
//    ConfigMgr config = new ConfigMgr("ArmaniCommon.cfg");
    ArmConfigLoader config = new ArmConfigLoader();

    htLabels = new Hashtable();
    // String reasonTypes = config.getString("ACTIVE_CERT_TYPES");
    String reasonTypes = config.getString("MALL_CERT_TYPE");
    if (reasonTypes != null && reasonTypes.length() > 1) {
      StringTokenizer stk = new StringTokenizer(reasonTypes, ",");
      types = new String[stk.countTokens()];
      int i = -1;
      //       String reasonLbl;
      while (stk.hasMoreTokens()) {
        types[++i] = stk.nextToken();
        String key = config.getString(types[i] + ".CODE");
        //            String code = config.getString(types[i] + ".DESC");
        String label = config.getString(types[i] + ".LABEL");
        htLabels.put(key, label);
      }
    }
    MallCertDlg dlg = new MallCertDlg(theAppMgr.getParentFrame(), theAppMgr, htLabels);
    dlg.setVisible(true);
    if (dlg.isOK()) {
      ((MallCert)mallCert).setType(dlg.getSelectedMallCert());
      ((MallCert)mallCert).setDesc((String)htLabels.get(dlg.getSelectedMallCert()));
      //String selectedReason = dlg.getSelectedCouponReason();
    }
  }


  /**
   * put your documentation comment here
   * @param type
   * @return
   */
  private boolean isTypeInList(String type) {
    // MSB - 09/08/05 - Read MallCert Types from ArmaniCommon.Cfg
    //    	ConfigMgr cfgMgr = new ConfigMgr("mallcert.cfg");
    //	String activeCertificates = cfgMgr.getString("ACTIVE_CERT_TYPES");
//    ConfigMgr cfgMgr = new ConfigMgr("ArmaniCommon.cfg");
     ArmConfigLoader cfgMgr = new ArmConfigLoader();
    String activeCertificates = cfgMgr.getString("MALL_CERT_TYPE");
    Vector v = new Vector();
    StringTokenizer st = new StringTokenizer(activeCertificates, ",");
    while (st.hasMoreTokens()) {
      String coupons = st.nextToken();
      v.add(coupons);
    }
    if (v.contains(type)) {
      return true;
    }
    return false;
  }


  /**
   * put your documentation comment here
   * @return
   */
  private Vector getTypes() {
    // MSB - 09/08/05 - Read MallCert Types from ArmaniCommon.Cfg
    //    	ConfigMgr cfgMgr = new ConfigMgr("mallcert.cfg");
    //		String activeCertificates = cfgMgr.getString("ACTIVE_CERT_TYPES");
//    ConfigMgr cfgMgr = new ConfigMgr("ArmaniCommon.cfg");
    ArmConfigLoader cfgMgr = new ArmConfigLoader();
    String activeCertificates = cfgMgr.getString("MALL_CERT_TYPE");
    Vector v = new Vector();
    StringTokenizer st = new StringTokenizer(activeCertificates, ",");
    while (st.hasMoreTokens()) {
      String certificates = st.nextToken();
      v.add(certificates);
    }
    return v;
  }


  /**
   * put your documentation comment here
   * @param amount
   * @return
   */
  private boolean isValidCertificates(ArmCurrency amount) {
    theTxn = (CMSPaymentTransactionAppModel)((PaymentTransaction)theAppMgr.getStateObject("TXN_POS")).
        getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
    ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue();
    //amtDue = amtDue.subtract(theTxn.getTotalPaymentAmount());
    try {
      if (amount.lessThan(amtDue)) {
        return true;
      } else
        return false;
    } catch (CurrencyException ex) {
      ex.printStackTrace();
      return false;
    }
  }


  /**
   * put your documentation comment here
   * @return
   */
  private boolean isListRequired() {
    ConfigMgr cfgMgr = new ConfigMgr("mallcert.cfg");
    String listReqd = cfgMgr.getString("SOLICIT_CERT_TYPES");
    if (listReqd.equalsIgnoreCase("true")) {
      return true;
    } else
      return false;
  }
}
