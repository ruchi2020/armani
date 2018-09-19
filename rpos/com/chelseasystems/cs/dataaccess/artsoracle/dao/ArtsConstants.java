/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;


/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    |            |           |           | Initial Version                                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 06-03-2005 | Megha     | 92        | Changed PAYMENT_TYPE_diners to PAYMENT_TYPE_DINERS |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public interface ArtsConstants {
  public static final String PARTY_ROLE_TYPE_STORE = "ST";
  public static final String PARTY_ROLE_TYPE_EMPLOYEE = "EM";
  public static final String PARTY_ROLE_TYPE_CUSTOMER = "CU";
  // -- Salil V Gangal 2/14/05
  // -- Change begins
  // -- Added the following constant
  public static final String ADDRESS_TYPE_COMPANY = "COMPANY";
  public static final String ADDRESS_TYPE_STORE = "STORE";
  // -- Change ends
  // -- Salil V Gangal 2/14/05
  public static final String TELEPHONE_TYPE_PRIMARY = "PRI";
  public static final String TELEPHONE_TYPE_SECONDARY = "SEC";
  public static final String TELEPHONE_TYPE_TERNARY = "TER";
  public static final String PARTY_TYPE_PERSON = "PERSON";
  public static final String PARTY_TYPE_ORGANIZATION = "ORGANIZATION";
  public static final String ADDRESS_TYPE_STANDARDIZED = "STANDARDIZED";
  public static final String ADDRESS_TYPE_NON_STANDARDIZED = "NON_STANDARDIZED";
  public static final String FOREIGN_DOMESTIC_TYPE_FOREIGN = "FOREIGN";
  public static final String FOREIGN_DOMESTIC_TYPE_DOMESTIC = "DOMESTIC";
  public static final String CUST_RECORD_TYPE_B = "B";
  public static final String CUST_RECORD_TYPE_D = "D";
  public static final String CUST_RECORD_TYPE_A = "A";
  public static final String CUST_RECORD_TYPE_CC = "CC";
  public static final String CUST_RECORD_TYPE_CA = "CA";
  public static final Long FORM_TYPE_W4 = new Long(1);
  public static final Long FORM_TYPE_I9 = new Long(2);
  public static final Long FORM_TYPE_VAC_REQ = new Long(3);
  public static final Long FORM_I9_DOC_PRIMARY = new Long(1);
  public static final Long FORM_I9_DOC_SECONDARY = new Long(2);
  public static final Long FORM_I9_DOC_REHIRE = new Long(3);
  public static final String EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_REGIONAL_MANAGER = "RM";
  public static final String EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_DISTRICT_MANAGER = "DM";
  public static final String EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_MANAGER = "MG";
  public static final String EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_ASST_MANAGER = "AM";
  public static final String EMPLOYEE_RETAIL_STORE_ASSIGNMENT_STATUS_CODE_DEFAULT_CONSULTANT = "DC";
  public static final String COORDINATE_TYPE_CODE_DEFAULT = "PY";
  public static final String CUSTOMER_GROUP_LOYALTY = "CU_LOYALTY";
  public static final String ITEM_TYPE_REDEEMABLE_TYPE_CODE = "ITEM_TYPE_REDEEMABLE_TYPE_CODE";
  public static final String ITEM_TYPE_SERVICE_TYPE_CODE = "ITEM_TYPE_SERVICE_TYPE_CODE";
  public static final String ITEM_TYPE_STOCK_TYPE_CODE = "ITEM_TYPE_STOCK_TYPE_CODE";
  public static final String TRANSACTION_TYPE_UNKNOWN = "TRNUNK";
  public static final String TRANSACTION_TYPE_SOS = "TRNSOS";
  public static final String TRANSACTION_TYPE_EOS = "TRNEOS";
  public static final String TRANSACTION_TYPE_EOD = "TRNEOD";
  public static final String TRANSACTION_TYPE_TRANSFER = "TRNFER";
  public static final String TRANSACTION_TYPE_TRANSFER_IN = "TRNRIN";
  public static final String TRANSACTION_TYPE_TRANSFER_OUT = "TRNOUT";
  public static final String TRANSACTION_TYPE_PAYMENT = "TRNPAY";
  public static final String TRANSACTION_TYPE_COMPOSITE_POS = "TRNPOS";
  public static final String TRANSACTION_TYPE_LAYAWAY_RTS = "TRNRTS";
  public static final String TRANSACTION_TYPE_LAYAWAY_PAYMENT = "TRNLPA";
  public static final String TRANSACTION_TYPE_COLLECTION = "TRNCOL";
  public static final String TRANSACTION_TYPE_PAID_OUT = "TRNPOT";
  public static final String TRANSACTION_TYPE_VOID = "TRNVOD";
  public static final String TRANSACTION_TYPE_NO_SALE = "TRNNOS";
  public static final String TRANSACTION_TYPE_REDEEMABLE_BUYBACK = "TRNBBK";
  public static final String TRANSACTION_TYPE_TIMECARD = "TRNTCD";
  public static final String TRANSACTION_TYPE_TIMECARD_IN = "TRNTCI";
  public static final String TRANSACTION_TYPE_TIMECARD_OUT = "TRNTCO";
  public static final String TRANSACTION_TYPE_TIMECARD_TRANSIT = "TRNTCT";
  public static final String TRANSACTION_TYPE_TIMECARD_BENEFIT = "TRNTCB";
  public static final String TRANSACTION_TYPE_TIMECARD_MOD = "TRNTCM";
  public static final String TRANSACTION_TYPE_COMPOSITE_PRS = "TRNPSO";
  public static final String TRANSACTION_TYPE_COMPOSITE_CSG = "TRNCGO";
  public static final String TRANSACTION_TYPE_COMPOSITE_RSV = "TRNRSV";
  public static final String TRANSACTION_TYPE_COMPOSITE_NFS = "TRNNFS";
  public static final Long EMP_RESOURCE_ROLE_TYPE_PREFERRED = new Long(1);
  public static final Long EMP_RESOURCE_ROLE_TYPE_BACKUP = new Long(2);
  public static final Long TIME_RANGE_TYPE_PREFERRED_SUNDAY = new Long(10);
  public static final Long TIME_RANGE_TYPE_PREFERRED_MONDAY = new Long(11);
  public static final Long TIME_RANGE_TYPE_PREFERRED_TUESDAY = new Long(12);
  public static final Long TIME_RANGE_TYPE_PREFERRED_WEDNESDAY = new Long(13);
  public static final Long TIME_RANGE_TYPE_PREFERRED_THURSDAY = new Long(14);
  public static final Long TIME_RANGE_TYPE_PREFERRED_FRIDAY = new Long(15);
  public static final Long TIME_RANGE_TYPE_PREFERRED_SATURDAY = new Long(16);
  public static final Long TIME_RANGE_TYPE_UNAVAILABLE_SUNDAY = new Long(20);
  public static final Long TIME_RANGE_TYPE_UNAVAILABLE_MONDAY = new Long(21);
  public static final Long TIME_RANGE_TYPE_UNAVAILABLE_TUESDAY = new Long(22);
  public static final Long TIME_RANGE_TYPE_UNAVAILABLE_WEDNESDAY = new Long(23);
  public static final Long TIME_RANGE_TYPE_UNAVAILABLE_THURSDAY = new Long(24);
  public static final Long TIME_RANGE_TYPE_UNAVAILABLE_FRIDAY = new Long(25);
  public static final Long TIME_RANGE_TYPE_UNAVAILABLE_SATURDAY = new Long(26);
  public static final Long TIME_RANGE_TYPE_UNAVAILABLE_SPECIAL_REQUESTS = new Long(29);
  public static final String DISCOUNT_TYPE_BASIC = "BD";
  public static final String DISCOUNT_TYPE_EMPLOYEE = "ED";
  public static final String DISCOUNT_TYPE_REWARD = "RD";
  public static final String DISCOUNT_TYPE_REWARD_CARD = "REWARD_CARD";
  public static final Long POS_LINE_ITEM_TYPE_SALE = new Long(1);
  public static final Long POS_LINE_ITEM_TYPE_RETURN = new Long(2);
  public static final Long POS_LINE_ITEM_TYPE_LAYAWAY = new Long(3);
  public static final Long POS_LINE_ITEM_TYPE_PRESALE = new Long(4);
  public static final Long POS_LINE_ITEM_TYPE_CONSIGNMENT = new Long(5);
  public static final Long POS_LINE_ITEM_TYPE_RESERVATION = new Long(6);
  public static final Long POS_LINE_ITEM_TYPE_VOID = new Long(7);
  public static final Long POS_LINE_ITEM_TYPE_NOSALE = new Long(8);
  public static final Long POS_LINE_ITEM_TYPE_NORETURN = new Long(9);
  public static final String PAYMENT_TYPE_UNKNOWN = "????";
  public static final String PAYMENT_TYPE_CASH = "CASH";
  public static final String PAYMENT_TYPE_CAD_CASH = "CAD_CASH";
  public static final String PAYMENT_TYPE_CHECK = "CHECK";
  public static final String PAYMENT_TYPE_EMPLOYEE_CHECK = "EMPLOYEE_CHECK";
  public static final String PAYMENT_TYPE_BANK_CHECK = "BANK_CHECK";
  public static final String PAYMENT_TYPE_CAD_BANK_CHECK = "CAD_BANK_CHECK";
  public static final String PAYMENT_TYPE_BUSINESS_CHECK = "BUSINESS_CHECK";
  public static final String PAYMENT_TYPE_CAD_BUSINESS_CHECK = "CAD_BUSINESS_CHECK";
  public static final String PAYMENT_TYPE_MONEY_ORDER = "MONEY_ORDER";
  public static final String PAYMENT_TYPE_TRAVELLERS_CHECK = "TRAVELLERS_CHECK";
  public static final String PAYMENT_TYPE_CREDIT_CARD = "CREDIT_CARD";
  public static final String PAYMENT_TYPE_AMEX = "AMEX";
  public static final String PAYMENT_TYPE_DISCOVER = "DISCOVER";
  public static final String PAYMENT_TYPE_MASTER_CARD = "MASTER_CARD";
  public static final String PAYMENT_TYPE_VISA = "VISA";
  public static final String PAYMENT_TYPE_JCB = "JCB";
  public static final String PAYMENT_TYPE_DINERS = "DINERS";
  public static final String PAYMENT_TYPE_DEBIT_CARD = "DEBIT";
  public static final String PAYMENT_TYPE_REDEEMABLE = "REDEEMABLE";
  public static final String PAYMENT_TYPE_GIFT_CERTIFICATE = "GIFT_CERTIFICATE";
  public static final String PAYMENT_TYPE_GIFT_CERT = "GIFT_CERT";
  public static final String PAYMENT_TYPE_DUE_BILL = "DUE_BILL";
  public static final String PAYMENT_TYPE_DUE_BILL_ISSUE = "DUE_BILL_ISSUE";
  public static final String PAYMENT_TYPE_STORE_VALUE_CARD = "STORE_VALUE_CARD";
  public static final String PAYMENT_TYPE_MANUFACTURE_COUPON = "MANUFACTURE_COUPON";
  public static final String PAYMENT_TYPE_HOUSE_ACCOUNT = "HOUSE_ACCOUNT";
  public static final String PAYMENT_TYPE_MALLCERT = "MALL_CERTIFICATE";
  public static final String PAYMENT_TYPE_MAIL_CHECK = "MAIL_CHECK";
  public static final String PAYMENT_TYPE_PREMIO_DISCOUNT = "PREMIO_DISCOUNT";
  //ks: New payment types for Europe
  public static final String PAYMENT_TYPE_LOCAL_CHECK = "LOCAL_CHECK";
  public static final String PAYMENT_TYPE_OUT_OF_AREA_CHECK = "OUT_OF_AREA_CHECK";
  public static final String PAYMENT_TYPE_ROUND_PAYMENT = "ROUND_PAYMENT";
  public static final String PAYMENT_TYPE_CREDIT = "CREDIT";
  public static final String PAYMENT_TYPE_MONETA = "MONETA";
  public static final String PAYMENT_TYPE_COUPON = "COUPON";
  // Job Codes
  public static final String JC_MANAGER_ID = "1001";
  public static final String JC_MANAGER_DE = "MANAGER LEVEL 1";
  public static final String JC_STORE_ASSOCIATE_ID = "1004";
  public static final String JC_STORE_ASSOCIATE_DE = "SALES LEVEL 1";
  public static final String JC_CASHIER_ID = "1011";
  public static final String JC_CASHIER_DE = "CASHIER";
  public static final String JC_TECHNICAL_SUPPORT_ID = "1012";
  public static final String JC_TECHNICAL_SUPPORT_DE = "TECHNICAL_SUPPORT";
  public static final String JC_NON_STORE_ASSOCIATE_ID = "1013";
  public static final String JC_NON_STORE_ASSOCIATE_DE = "NON_STORE_ASSOCIATE";
  public static final String JC_TAILOR_ID = "1014";
  public static final String JC_TAILOR_DE = "TAILOR";
  public static final String JC_FITTER_ID = "1015";
  public static final String JC_FITTER_DE = "FITTER";
  public static final String TENDER_GROUP_CODE = "TND";
  public static final String TXN_GROUP_CODE = "TXN";
  public static final String TAX_GROUP_CODE = "TAX";
  public static final String FISCAL_DOC_TYPE_DDT = "DD";
  public static final String FISCAL_DOC_TYPE_TAXFREE = "TF";
  public static final String FISCAL_DOC_TYPE_VATINVOICE = "VI";
  public static final String FISCAL_DOC_TYPE_CREDITNOTE = "CN";  
  
}



