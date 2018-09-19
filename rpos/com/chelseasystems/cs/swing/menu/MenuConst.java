/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.menu;


/**
 * put your documentation comment here
 */
public class MenuConst {
  /******************* MENU OPTIONS ************************
   Add BACKUP Role -> ADD_ROLE_BACKUP
   Add Benefit -> ADD_BENEFIT
   Add Constraint -> ADD_CONSTRAINT
   Add Employee History -> ADD_EMPL_HIST
   Add IN -> ADD_IN
   Add Item -> ADD_ITEM : OffLine
   Add NOT Available -> NOT_AVAILABLE
   Add OUT -> ADD_OUT
   Add PREFER Available -> ADD_PREFER
   Add PREFERRED Role -> ADD_ROLE_PREFER
   Add Prior Period Benefit -> ADD_PRIOR_BENEFIT
   Add Reference -> ADD_REFERENCE
   Add Related Employee -> ADD_RELATED
   Add Request -> ADD_REQUEST
   Add Role -> ADD_ROLE
   Add Shift -> ADD_SHIFT
   Add Time -> ADD_TIME
   Additional Information -> ADDITIONAL_EMP_INFO
   Adhoc Query -> ADHOC_QUERY
   All -> ALL
   Amount -> AMOUNT
   Benefit Time -> BENEFIT
   Buy Back Redeemable -> BUY_BACK
   Calibrate Screen -> CALIBRATE_SCREEN : OffLine
   Cancel -> CANCEL : OffLine
   Cancel Txn -> CANCEL_TXN : OffLine
   Cashier End of Session -> CASHIER_EOS
   Cashier Session -> CASHIER_SESSION : OffLine
   Change Quantity -> CHANGE_QTY
   Change Deposit -> CHANGE_DEPOSIT : OffLine
   Clear -> CLEAR : OffLine
   Clear All Constraints -> CLEAR_ALL
   Clear Totals -> CLEAR_TOTALS : OffLine
   Clock In -> CLOCK_IN : OffLine
   Clock In/Out -> CLOCK_IN_OUT : OffLine
   Clock Out -> CLOCK_OUT : OffLine
   Complete Register Closeout -> COMPLETE_EOD : OffLine
   Complete Transfer In -> COMPLETE
   Consultant -> CHANGE_CONS : OffLine
   Consultant -> CONSULTANT
   Consultant Net Sales -> NET_SALES_CONS
   Continue -> CONTINUE : OffLine
   Copy Schedule -> CLONE
   Create New -> CREATE_NEW
   Credit Application -> CREDIT_APP
   Current Timecard -> CW_TIME
   Customer -> CHANGE_CUST : OffLine
   Customer -> CUSTOMER
   Customer Menu -> CUST_MENU
   Customer Lookup -> CUST_LOOKUP
   Customer Merge -> CUST_MERGE
   Date -> DATE
   De-Select All -> DE_SELECT_ALL
   Delete Schedule -> DELETE_SCHEDULE
   Delete All Transactions -> DELETE_ALL_SUSPENDED_TRANSACTIONS
   Delete Available -> DELETE_AVAILABLE
   Delete Benefit -> DELETE_BENEFIT
   Delete Employee Goal -> DELETE_GOAL
   Delete IN/OUT -> DEL_IN_OUT
   Delete Item -> DEL_ITEM : OffLine
   Delete Line Item Consultant -> RESTORE_CONS : OffLine
   Delete Manual Tax -> RESTORE_TAX : OffLine
   Delete Payment -> DEL_PAY : OffLine
   Delete Query -> DELETE_QUERY
   Delete Request -> DELETE_REQUEST
   Delete Role -> DELETE_ROLE
   Delete Row -> DEL_ROW
   Delete Shift -> DELETE_SHIFT
   Department Net Sales -> NET_SALES_DEPT
   Discount -> DISCOUNT : OffLine
   Edit Constraint -> EDIT_CONSTRAINT
   Employee Goals -> GOAL_ENTRY
   Employee Management -> EMP_MGMT
   Employee Security -> EMP_SECURITY
   Employee Short Name -> EMPLOYEE_SHORTNAME
   Employee Scheduling Information -> SCHEDULE_RESOURCE
   Employee Search -> EMPLOYEE_SEARCH
   Employment Application -> EMP_APPLICATION
   End of Session -> END_SESSION : OffLine
   Endorse Check -> ENDORSE_CHECK
   Enter Date -> ENTER_DATE
   Enter Employee -> ENTER_EMPLOYEE
   Enter Media Totals -> MEDIA_TOTALS : OffLine
   Enter Comment -> COMMENT
   Enter Comment -> ENTER_COMMENT : OffLine
   Enter Employee Goal -> ENTER_GOAL
   Enter Store Id -> ENTER_STORE_ID
   Even Exchange -> EVEN_EXCHANGE : OffLine
   Finalize Timecards -> FINALIZE
   Find Date -> FIND_DATE
   Find Item -> FIND_ITEM
   Find Item In Stock -> FIND_IT
   Find Transaction -> FIND_TXN
   Find by Name -> FIND_NAME
   Find by Phone -> FIND_PHONE
   Form I-9 -> I9
   Form W-4 -> W4
   Funeral -> BENEFIT_FUNERAL
   Generate Assignements -> GENERATE
   House ID -> HOUSE_ID : OffLine
   ID -> ID
   Insert IN/OUT -> INS_IN_OUT
   Inventory Menu -> INVENTORY_MENU
   Item -> ITEM
   Item Code Net sales -> NET_SALES_ITEM
   Items -> ITEMS : OffLine
   Jury Duty -> BENEFIT_JURY
   Layaway -> LAYAWAY : OffLine
   Layaway Management -> LAY_MGMT
   Layaway Payment -> LAY_PAYMENT
   Line Item Detail -> ITEM_DETAIL : OffLine
   Log Off -> LOG_OFF : OffLine
   Log Off -> MAIN_LOG_OFF : OffLine
   Look Up Another -> LOOK_UP
   Magstripe Test -> MAGSTRIPE_TEST : OffLine
   Maintain Schedule -> SCHEDULE
   Maintain Availability -> AVAILABILITY
   Maintain Hours -> MAINTAIN_HOURS
   Maintain Requests -> REQUESTS
   Maintain Roles -> ROLE
   Manage Shifts -> MODIFY
   Management Menu -> MGMT_MENU : OffLine
   Manual Override -> MAN_OVERRIDE : OffLine
   Manually Assign Employee -> ASSIGN_SHIFT
   Markdown Price -> MOD_PRICE : OffLine
   Markdown $ -> MARK_AMT : OffLine
   Markdown % -> MARK_PCT : OffLine
   Markdowns -> MARKDOWNS : OffLine
   Merchandise Lookup -> MERCH_LOOKUP
   Merchandise Menu -> MERCH_MENU
   Merchandise Return -> MERCH_RETURN : OffLine
   Miscellaneous Return -> MISC_RET : OffLine
   Modify Customer -> MOD_CUST : OffLine
   Modify Drawer Fund -> MOD_FUND : OffLine
   Modify Employee Goal -> MODIFY_GOAL
   Modify Information -> MODIFY_INFO
   Modify Restocking Fee -> MOD_RESTOCK_FEE
   Modify Unit Price -> MOD_UNIT_PRICE : OffLine
   Modify Benefit -> MODIFY_BENEFIT
   Modify Deposit -> MOD_DEP : OffLine
   Modify Employee Information -> MODIFY_EMPLOYEE
   Modify Form -> MOD_FORM
   Modify IN -> MOD_IN
   Modify Line Item Consultant -> CHANGE_LINE_CONS : OffLine
   Modify Line Item Tax -> MODIFY_TAX : OffLine
   Modify Line Item Tax % -> MODIFY_TAX_PERCENT : OffLine
   Modify OUT -> MOD_OUT
   Modify Quantity -> MOD_QTY : OffLine
   Modify Reason -> MODIFY_REASON
   Modify Shift -> MODIFY_SHIFT
   Modify Totals -> MODIFY_TOTALS : OffLine
   Name Lookup -> NAME_LOOKUP
   Net Sales / Projections -> NET_SALES_PROJ
   Net Sales Comparisons -> NET_SALES_COMP
   Net Sales by Payment -> NET_SALES_TENDER
   Net Sales by Transaction -> NET_SALES_BY_TXN
   New Customer -> NEW_CUST : OffLine
   New Employee -> NEW_EMPLOYEE
   New Form -> NEW_FORM
   New Query -> NEW_QUERY
   Next Transaction -> NEXT_TXN
   No Sale -> NO_SALE : OffLine
   OK -> OK : OffLine
   Operator -> OPERATOR
   Operator Pick List -> OPERATOR_SEARCH
   Operator Short Name -> OPERATOR_SHORT_NAME
   Operator Net Sales -> NET_SALES_OPER
   Options -> OPTIONS
   Other Benefit -> BENEFIT_OTHER
   Outstanding -> OUTSTANDING
   Overdue -> OVERDUE
   P.O.S. Menu -> POS_MENU : OffLine
   Paidins -> COLLECTIONS
   Paidouts -> PAIDOUTS
   Partial Transfer In -> PARTIAL
   Payment -> PAYMENT
   Payment Type -> PMT_TYPE
   Post Transaction -> POST_TXN : OffLine
   Post and Pack -> POST_AND_PACK
   Previous -> PREV : OffLine
   Previous Menu -> RETURN_TO_PAYMENTS : OffLine
   Previous Transaction -> PREV_TXN
   Previous Week's Timecard -> PW_TIME
   Print -> PRINT : OffLine
   Print Employee -> PRINT_EMPLOYEE : OffLine
   Print All Gift Items -> PRINT_ALL_GIFT : OffLine
   Print Alteration Stub -> PRINT_STUB : OffLine
   Print Chart -> PRINT_CHART : OffLine
   Print Chart Preview -> PRINT_PREVIEW
   Print Gift Receipt -> PRINT_GIFT : OffLine
   Print Receipt -> PRINT_RECEIPT : OffLine
   Print Retail Export Receipt -> PRINT_EXPORT : OffLine
   Print Selected Gift Items -> PRINT_SELECT_ITEMS : OffLine
   Print Trial -> PRINT_TRIAL : OffLine
   Print VAT Invoice -> PRINT_VAT : OffLine
   Printer Test -> PRINTER_TEST : OffLine
   Process -> PROCESS : OffLine
   Re-Entry Mode -> RE_ENTRY : OffLine
   Recall Transaction -> RECALL_TXN : OffLine
   Redeemable Inquiry -> REDEEMABLE_INQUIRY
   Register -> REGISTER
   Register Closeout -> END_DAY : OffLine
   Register Fingerprint -> REGISTER_FINGERPRINT
   Remove Constraint -> REMOVE_CONSTRAINTS
   Remove Tax Exempt -> REMOVE_TAX_EXEMPT : OffLine
   Remove Existing -> REMOVE_EXISTING
   Remove Prior Benefit -> REMOVE_PRIOR_BENEFIT
   Rename Schedule -> ADD_COMMENTS
   Reports -> REPORT_MENU : OffLine
   Resend Broken Transactions -> RESEND_BROKEN_TXNS
   Reset Password -> RESET_PASSWORD
   Restore Deleted Goal -> RESTORE_GOAL
   Return Item -> RETURN_ITEM : OffLine
   Return Transaction -> RETURN_TXN : OffLine
   Return to Stock -> RETURN_STOCK
   Return to Transaction -> RETURN_TRANS : OffLine
   Ring a Sale -> RING_SALE : OffLine
   Sale -> SALE : OffLine
   Sale Express -> SALE_EXPRESS : OffLine
   Sales Per Period -> SALES_PER_PERIOD
   Save -> SAVE
   Search -> SEARCH
   Search Again -> SEARCH_AGAIN
   Select All -> SELECT_ALL
   Select Employee -> SELECT_EMPLOYEE
   Select Existing -> SELECT_EXISTING
   Select Other Employee -> OTHER_EMPLOYEE
   Select Other Timecard -> OTHER_TIMECARD
   Select Schedule -> SELECT_SCHEDULE
   Shipping -> SHIPPING : OffLine
   Show Incoming Transfers -> TRANSFERS_IN
   Show Misc Items -> SHOW_MISC_ITEMS : OffLine
   Show Outgoing Transfers -> TRANSFERS_OUT
   Start of Day -> SHUTDOWN : OffLine
   Sick -> BENEFIT_SICK
   Skip -> SKIP : OffLine
   Special Order -> SPECIAL_ORDER
   Standard Information -> ADDITIONAL_EMP_INFO_PREV
   Start Register Closeout -> START_EOD
   Store Closeout -> STORE_CLOSEOUT
   Store Information -> STORE_INFO
   Store Net Sales -> NET_SALES
   Submit -> SUBMIT
   Suspend -> SUSPEND : OffLine
   System Utilities -> SYSTEM_UTILS : OffLine
   TOTAL -> TOTAL : OffLine
   Tax Exempt -> TAX_EXEMPT : OffLine
   Terminate Employee -> REMOVE_EMPLOYEE
   Time -> TIME_OF_DAY
   Time Off Request -> VAC_REQUEST
   Timecard Management -> TIMECARD_MGMT
   Toggle Item Details -> TOGGLE_DETAIL
   Transaction -> TRANSACTION
   Transaction History -> TXN_HIST
   Transaction Management -> TRANS_MGMT
   Transaction Activity -> TRANSACTION_ACTIVITY
   Transaction Type -> TXN_TYPE
   Transfer In -> TRANSFER_IN
   Transfer Out -> TRANSFER_OUT
   Unassign ALL Employees -> UNASSIGN_ALL_EMPLOYEES
   Unassign Employee -> UNASSIGN_SHIFT
   Update Employee File -> UPDATE_EMPLOYEE_FILE
   Update Item File -> UPDATE_ITEM_FILE
   Update Maximum -> MAX
   Update Role Strength -> UPDATE_STRENGTH
   Update Target -> TARGET
   Vacation -> BENEFIT_VACATION
   Verify Deposit -> VERIFY_DEPOSIT : OffLine
   View Adjustments -> VIEW_ADJUSTMENTS
   View Comments -> VIEW_COMMENTS
   View by Operator -> OPERATOR_VIEW
   View by Store -> STORE_VIEW
   View Graph -> VIEW_GRAPH
   View Pending Transfers -> VIEW_PENDING_TRANSFERS
   View Receipt Log -> VIEW_RECEIPT_LOG : OffLine
   View Transaction -> VIEW_TXN
   Void Transaction -> VOID_TXN
   Void Transfer -> VOID_TRANSFER
   Zoom In -> ZOOM_IN
   Zoom Out -> ZOOM_OUT
   ******************* MENU OPTIONS ************************/
  //********************** MENUS **************************
   public static final String ADHOC_QUERY_TXN = "adhoc_query_txn";
   public static final String CANCEL_ONLY = "cancel_only";
  public static final String CHART_OPTIONS = "chart_options";
  public static final String CLEAR_ONLY = "clear_only";
  public static final String CLOCK_IN = "clock_in";
  public static final String CLOCK_IN_OUT = "clock_in_out";
  public static final String CLOCK_OUT = "clock_out";
  public static final String CONSULTANT = "consultant";
  public static final String CONSULTANT_LINE = "consultant_line";
  public static final String CONSULTANT_LINE_EUR = "consultant_line_eur";
  public static final String CUST_MGMT = "cust_mgmt";
  public static final String CUST_ONLY = "cust_only";
  public static final String DESELECT_ALLONLY = "deselect_allonly";
  public static final String EMP_MAINTAIN_HOURS = "emp_maintain_hours";
  public static final String EMP_MGMT = "emp_mgmt";
  public static final String EMP_RESOURCE_AVAIL = "emp_resource_avail";
  public static final String EMP_RESOURCE_EMPLOYEES = "emp_resource_employees";
  public static final String EMP_RESOURCE_INIT = "emp_resource_init";
  public static final String EMP_RESOURCE_REQUESTS = "emp_resource_requests";
  public static final String EMP_RESOURCE_ROLE = "emp_resource_role";
  public static final String EMP_RESOURCE_SUMMARY = "emp_resource_summary";
  public static final String EMPL_APPLICATION = "empl_application";
  public static final String EMPLOYEE_ACCESS = "employee_access";
  public static final String EMPLOYEE_ACCESS_FOUND = "employee_access_found";
  public static final String EMPLOYEE_ACCESS_INQUIRE = "employee_access_inquire";
  public static final String EMPLOYEE_ACCESS_MODIFY = "employee_access_modify";
  public static final String ENTER_COMMENT = "enter_comment";
  public static final String EOD_REPRINT = "eod_reprint";
  public static final String FIND_IT = "find_it";
  public static final String FIND_NAME_PHONE = "find_name_phone";
  public static final String FIND_PHONEONLY = "find_phoneonly";
  public static final String FOUND_CUST = "found_cust";
  public static final String GIFT_RECEIPTS = "gift_receipts";
  public static final String GOAL_MAIN = "goal_main";
  public static final String HOUSE_IDONLY = "house_idonly";
  public static final String INVENTORY_MENU = "inventory_menu";
  public static final String LAY_MGMT = "lay_mgmt";
  public static final String MAIN = "main";
  public static final String MANAGEMENT = "management";
  public static final String MARKDOWN_MENU = "markdown_menu";
  public static final String MERCH_MENU = "merch_menu";
  public static final String MOD_RESTOCK_FEEONLY = "mod_restock_feeonly";
  public static final String MODIFY_FORMONLY = "modify_formonly";
  public static final String NAME_LOOKUPONLY = "name_lookuponly";
  public static final String NET_SALES_TYPE = "net_sales_type";
  public static final String NET_SALES_TYPE_STORE = "net_sales_type_store";
  public static final String NEW_CUSTONLY = "new_custonly";
  public static final String NEW_FORMONLY = "new_formonly";
  public static final String NEWCUST_HIST = "newcust_hist";
  public static final String NEWMOD_CUST = "newmod_cust";
  public static final String NEWSKIP_CUST = "newskip_cust";
  public static final String NEXT_PREV_TXN = "next_prev_txn";
  public static final String NO_BUTTONS = "no_buttons";
  public static final String OK_CANCEL = "ok_cancel";
  public static final String OK_CANCEL_PREVIOUS = "ok_cancel_previous";
  public static final String OK_ONLY = "ok_only";
  public static final String PENDING_TRANSACTIONS = "pending_transactions";
  public static final String POS = "pos";
  public static final String POS_CASHIER_SESSION = "pos_cashier_session";
  public static final String POS_CONTINUE = "pos_continue";
  public static final String POS_LAYAWAY = "pos_layaway";
  public static final String POS_REENTER = "pos_reenter";
  public static final String POS_RETURN = "pos_return";
  public static final String OK_OVERRIDE = "ok_override";
  public static final String POS_SALE = "pos_sale";
  public static final String OK_PREVIOUS = "ok_previous";
  public static final String POST = "post";
  public static final String POST_AUTH = "post_auth";
  public static final String PREV_ONLY = "prev_only";
  public static final String PREV_ONLY_TXN = "prev_only_txn";
  public static final String PREV_PRINT = "prev_print";
  public static final String PRINT_ONLY = "print_only";
  public static final String PRINT_RECEIPT = "print_receipt";
  public static final String PROCESS_EOD = "process_eod";
  public static final String QUERY_MANAGEMENT = "query_management";
  public static final String RECALL_SUS_TRANS = "recall_sus_trans";
  public static final String RECEIPT_LOG = "receipt_log";
  public static final String RECEIPT_LOG_AGAIN = "receipt_log_again";
  public static final String REDEEMABLE_BUY_BACK = "redeemable_buy_back";
  public static final String REGISTER_CLOSEOUT_COMPLETE = "register_closeout_complete";
  public static final String REPORTS = "reports";
  public static final String RETURN_HIST = "return_hist";
  public static final String RETURN_TXNONLY = "return_txnonly";
  public static final String RUN_EOD = "run_eod";
  public static final String RUN_EOD_ONLY = "run_eod_only"; //Added by Himani on 07/06/2016
  public static final String SCHED = "sched";
  public static final String SCHED_INIT = "sched_init";
  public static final String SCHED_MODIFY = "sched_modify";
  public static final String SCHED_PRINT_PREVIEW = "sched_print_preview";
  public static final String SEARCH_AND_SEARCH_AGAIN = "search_and_search_again";
  public static final String SEARCH_ONLY = "search_only";
  public static final String SELECT_ALLONLY = "select_allonly";
  public static final String SHIPPING_DETAIL_SELECT_ALL = "shipping_detail_select_all";
  public static final String SHIPPING_DETAIL_DESELECT_ALL = "shipping_detail_deselect_all";
  public static final String SHIPPING_HEADER = "shipping_header";
  public static final String SHIPPING_HEADER_INQUIRY = "shipping_header_inquiry";
  public static final String SHUTDOWN_ONLY = "shutdown_only";
  public static final String SKIP_ONLY = "skip_only";
  public static final String SPECIAL_ORDER = "special_order";
  public static final String START_EOD = "start_eod";
  public static final String SYSTEM_UTILS = "system_utils";
  public static final String TIMECARD_BENEFIT = "timecard_benefit";
  public static final String TIMECARD_BENEFIT_LIST = "timecard_benefit_list";
  public static final String TIMECARD_IN = "timecard_in";
  public static final String TIMECARD_MGMT = "timecard_mgmt";
  public static final String TIMECARD_OUT = "timecard_out";
  public static final String TIMECARD_SALES = "timecard_sales";
  public static final String TRANS_MGMT = "trans_mgmt";
  public static final String TRANSFER_IN = "transfer_in";
  public static final String TRANSFER_OUT = "transfer_out";
  public static final String VAC_REQUEST = "vac_request";
  public static final String VERIFY_EOD = "verify_eod";
  public static final String VIEW_TXN = "view_txn";
  public static final String VOID_TRANSACTION = "void_transaction";
  public static final String DELIVERY_REQUEST = "delivery_request";
  public static final String DELIVERY_ADDRESS = "delivery_address";
  public static final String POS_DELIVERY_SALE = "pos_delivery_sale";
  public static final String SKIP_SEARCH = "skip_search";
  public static final String INVOICE_LOOK_UP = "invoice_lookup";
  public static final String LOOKUP_CUST = "lookup_cust";
  public static final String PAY_INVOICE = "pay_invoice";
  public static final String DELIVERY_MAINTENANCE = "delivery_maintenance";
  public static final String DELIVERY_REQUEST_MAINTENANCE = "delivery_request_maintenance";
  public static final String BARCODE_MAINTENANCE = "barcode_maintenance";
  public static final String MERCH_PICKUP = "merch_pickup";
  public static final String DELIVERY_RETURN = "delivery_return";
  public static final String SKIP_SEARCH1 = "skip_search1";
  public static final String NEXT_PREV_PRINT_TXN = "next_prev_print_txn";
  public static final String ITEM_LOOKUP = "item_lookup";
  public static final String ITEM_LOOKUP_FOUND = "item_lookup_found";
  public static final String SEARCH = "search";
  public static final String LOCATE = "locate";
  public static final String CUST_REFERRAL_LOOKUP = "Cust_referral_lookup";
  public static final String CUSTOMER_LOOKUP_INIT = "Customer_lookup_init";
  public static final String CUSTOMER_LOOKUP_INIT_OK = "Customer_lookup_init_OK";
  public static final String CUSTOMER_LOOKUP_SALE = "Customer_lookup_sale";
  public static final String CUST_NAME = "cust_name";
  public static final String ADVANCED_SEARCH = "search_new_search_prev";
  public static final String INQUIRIES = "INQUIRIES";
  public static final String PRINT_FISCAL = "print_fiscal";
  public static final String LOYALTY = "loyalty";
  public static final String LOYALTY_DETAILS = "loyalty_details";
  public static final String ADD_ITEM_MENU = "add_item_menu";
  public static final String OPTIONS = "options";
  public static final String PRICING = "pricing";
  public static final String TENDER_MENU_US = "tender_menu_us";
  public static final String TENDER_MENU_EU = "tender_menu_eu";
  public static final String TENDER_MENU_JAPAN = "tender_menu_japan";
  public static final String POST_TXN_US = "post_txn_us";
  public static final String POST_TXN_EU = "post_txn_eu";
  public static final String POST_TXN_JAPAN = "post_txn_japan";
  public static final String PRINT_GIFT_RECEIPT = "print_gift_receipt";
  public static final String OTHER_TXNS_MENU = "other_txns_menu";
  public static final String CONSIGNMENTS = "consignments";
  public static final String PRESALES = "presales";
  public static final String RESERVATIONS = "reservations";
  public static final String MGMT_MENU = "mgmt_menu";
  public static final String SUBTOTAL_DISCOUNT = "subtotal_disc";
  public static final String ACCOUNT_MAINTENANCE = "account_maintenance";
  public static final String VIEW_COMMENTS = "view_comments";
  public static final String VIEW_CARD_DETAILS = "view_card_details";
  public static final String CREDIT_CARD = "credit_card";
  public static final String VIEW_ADDRESS = "view_addresses";
  public static final String VIEW_PRE_SALES = "ViewPreSales";
  public static final String VIEW_RESERVATIONS = "ViewReservations";
  public static final String VIEW_CONSIGNMENTS = "ViewConsignments";
  public static final String ADD_RETURN_MENU = "add_return_menu";
  public static final String TAX_EXEMPT = "tax_exempt";
  public static final String REMOVE_TAX_EXEMPT = "remove_tax_exempt";
  public static final String USE_PREV = "use_prev";
  public static final String SELECT_ADDRESS = "select_addresses";
  public static final String COLLECTIONS_MENU = "collections_menu";
  public static final String CONSIGNMENTS_IN = "consignment_in";
  public static final String CONSIGNMENTS_OUT = "consignment_out";
  public static final String CONS_SELL_RETURN = "ConsSellReturnItems";
  public static final String CONS_SELL_RETURN_DESELECT_ALL = "ConsSellReturnItemsDeSelectAll";
  public static final String PRE_SALE_OPEN = "Pre_Sale_Open";
  public static final String PRE_SALE_CLOSE = "Pre_Sale_Close";
  public static final String PRE_SALE_SELECTED = "PresaleSelected";
  public static final String SELL_RETURN = "SellReturnItems";
  public static final String SELL_RETURN_SELECT_ALL = "SellReturnItemsSelectAll";
  public static final String BACK_OFFICE = "back_office";
  public static final String ALTERATIONS = "Aterations";
  public static final String CUST_TXN_HIST = "CUST_TXN_HIST";
  public static final String RETN_TXN_HIST = "RETN_TXN_HIST";
  public static final String SEARCH_PREV = "search_and_prev";
  public static final String PREV_CANCEL = "PREV_CANCEL";
  public static final String NEXT_PREV_TXN_DETAILS = "next_prev_txn_details";
  public static final String NEXT_PREV_TXN_LINE_ITEM_DETAILS = "next_prev_txn_line_item_details";
  public static final String RET_EUROPE = "RET_EUROPE";
  public static final String RET_DESELECT_ALL_EUROPE = "RET_DESELECT_ALL_EUROPE";
  public static final String RETURN_EU = "RETURN_EU";
  public static final String ASIS_EUROPE = "ASIS_EUROPE";
  public static final String PAID_IN = "PAID_IN";
  public static final String PAID_OUT = "PAID_OUT";
  public static final String PAID_OUT_CUST = "PAID_OUT_CUST";
  public static final String DEPOSIT_PREV = "DEPOSIT_PREV";
  public static final String PRINT_FISCAL_DOCUMENT = "print_fiscal_document";
  public static final String NO_SALE_PRINT_FISCAL_DOCUMENT = "print_fiscal_document_noSale";
  public static final String FISCAL_CUST_DETAILS = "Fiscal_Cust_Details";
  public static final String MODIFY_FISCAL = "modify_fiscal_num";
  public static final String nfs_menu = "nfs_menu";
  public static final String CUST_ADVANCED_SEARCH = "Customer_Advanced_Search";
  public static final String RESERVATION_CLOSE = "Reservation_Close";
  public static final String RESERVATION_SELECT = "SelectReservationItems";
  public static final String RESERVATION_DESELECT = "DeSelectReservationItems";
  public static final String PRINT_FISCAL_DOCUMENT_VIEW = "print_fiscal_document_view";
  // David's new menu for consignment details
  public static final String CONSIGNMENT_DETAIL_REPORT = "consignment_detail_report";
  public static final String DEPOSIT_HISTORY = "DepositHistory";
  //PCR1256 Style locator enhancement
  public static final String CREDIT_CARD_PRESALE_CLOSE = "credit_card_presale_close";
  public static final String RESERVATION_DETAIL_REPORT = "reservation_detail_report";
  public static final String PRESALE_DETAIL_REPORT = "presale_detail_report";
  public static final String CREDIT_HISTORY = "CreditHistory";
  public static final String UPDATE_TXN_DATA="UPDATE_TXN_DATA";
  public static final String UPDATE_TXN_DATA_NO_CUSTOMER="UPDATE_TXN_DATA_NO_CUSTOMER";
  public static final String CHANGE_ASSOCIATE="CHANGE_ASSOCIATE";
  public static final String CHANGE_CUSTOMER="CHANGE_CUSTOMER";
  public static final String UPDATE_CONSULTANT="UPDATE_CONSULTANT";
  public static final String UPDATE_ITEM_DETAIL="UPDATE_ITEM_DETAIL";
  public static final String CUSTOMER_DEFAULT_SEARCH="Customer_Default_Search";
  public static final String VIEW_TXN_LIST = "VIEW_TXN_LIST";
  public static final String NEXT_COLOR_AND_PREV_AND_ITEM_AVAILABLE = "next_color_and_prev_and_item_available";
  public static final String NEXT_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE = "next_color_and_prev_and_item_unavailable";
  public static final String NEXT_AND_PREVIOUS_COLOR_AND_PREV_AND_ITEM_AVAILABLE = "next_and_previous_color_and_prev_and_item_available";
  public static final String NEXT_AND_PREVIOUS_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE = "next_and_previous_color_and_prev_and_item_unavailable";
  public static final String PREVIOUS_COLOR_AND_PREV_AND_ITEM_AVAILABLE = "previous_color_and_prev_and_item_available";
  public static final String PREVIOUS_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE = "previous_color_and_prev_and_item_unavailable";
  public static final String LOCATE_WITH_NEXT_COLOR_AND_ITEM_AVAILABLE = "locate_with_next_color_and_item_available";
  public static final String LOCATE_WITH_NEXT_COLOR_AND_ITEM_UNAVAILABLE = "locate_with_next_color_and_item_unavailable";
  public static final String LOCATE_WITH_NEXT_AND_PREVIOUS_COLOR_AND_ITEM_AVAILABLE = "locate_with_next_and_previous_color_and_item_available";
  public static final String LOCATE_WITH_NEXT_AND_PREVIOUS_COLOR_AND_ITEM_UNAVAILABLE = "locate_with_next_and_previous_color_and_item_unavailable";
  public static final String LOCATE_WITH_PREVIOUS_COLOR_AND_ITEM_AVAILABLE = "locate_with_previous_color_and_item_available";
  public static final String LOCATE_WITH_PREVIOUS_COLOR_AND_ITEM_UNAVAILABLE = "locate_with_previous_color_and_item_unavailable";
  public static final String LOCATE_WITH_PREV_AND_ITEM_AVAILABLE = "locate_with_prev_and_item_available";
  public static final String LOCATE_WITH_PREV_AND_ITEM_UNAVAILABLE = "locate_with_prev_and_item_unavailable";
  public static final String LOCATE_AND_ITEM_AVAILABLE = "locate_and_item_available";
  public static final String LOCATE_AND_ITEM_UNAVAILABLE = "locate_and_item_unavailable"; 
  public static final String SEND_REQUEST_AND_ITEM_UNAVAILABLE = "send_request_and_item_unavailable";
  public static final String SEND_REQUEST_AND_ITEM_AVAILABLE = "send_request_and_item_available";

  //GC menus
  public static final String GIFT_CARD = "gift_card";
  public static final String GIFTCARD_SERVICES = "GIFTCARD_SERVICES";
  
  
  
  public static final String V12_BASKET = "v12_basket";
  
  public static final String SELECTED_BASKET = "SELECTED_BASKET";
  
}

