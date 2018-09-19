/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSComboBox;
import com.chelseasystems.cr.swing.bean.JCMSTextArea;
import com.chelseasystems.cr.swing.bean.JCMSMaskedTextField;
import com.chelseasystems.cr.swing.bean.JCMSCheckBox;
import java.util.ResourceBundle;
import java.awt.event.*;
import java.util.Date;
import com.chelseasystems.cr.config.ConfigMgr;
import java.awt.*;
import java.util.*;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.event.MessageEvent;
import com.chelseasystems.cr.swing.event.MessageListener;
import com.chelseasystems.cs.customer.CMSCustomer;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.util.CustomerUtil;
import com.chelseasystems.cs.util.ArmConfigLoader;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.swing.CMSTextFilter;
import com.chelseasystems.cs.swing.bean.JCMSTextField_JP;


/**
 * <p>Title:CustomerDetailPanel.java </p>
 *
 * <p>Description: Displays Customer Detail Information</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 09/06/2005 | Manpreet  | N/A       |Try catch block in populate methods                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 09/01/2005 | Manpreet  | 894/895   | Change Request to pick-up Payment Type and Supplier|
 |      |            |           |           | Payment type from ArmaniCommon.cfg file            |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 07/15/2005 | Vikram    | 328       |Birth date and Age range related changes            |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 06/22/2005 | Vikram    | 267, 277  |Age range to automatically populate from Birth date.|
 |      |            |           |           |"Not Defined" to be included in Age range options.  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04/12/2005 | Anand     | N/A       | Change Request to pick-up Payment Type and Supplier|
 |      |            |           |           | Payment type from configuration file               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-05-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerDetailPanel extends JPanel {
	private CMSCustomer theCustomer;

	private static final long serialVersionUID = 1L;

	/**
	 * PersonalInfo Panel
	 */
	protected JPanel pnlPersInfo;

	/**
	 * FiscalInfo Panel
	 */
	protected JPanel pnlFiscal;

	/**
	 * CompanyInfo Panel
	 */
	protected JPanel pnlCompanyInfo;

	/**
	 * BirthInfo Panel
	 */
	private JPanel pnlBirthInfo;

	/**
	 * Company
	 */
	protected JCMSLabel lblCompany;

	/**
	 * Company Value
	 */
	protected static JCMSTextField txtCompany;

	/**
	 * InterCoCode
	 */
	protected JCMSLabel lblInterCoCode;

	/**
	 * InterCoCode Value
	 */
	protected JCMSTextField txtInterCoCode;

	/**
	 * Account Number
	 */
	protected JCMSLabel lblAcctNo;

	/**
	 * Account Number value
	 */
	protected JCMSTextField txtAcctNo;

	/**
	 * Barcode
	 */
	protected JCMSLabel lblBarcode;

	/**
	 * Barcode Value
	 */
	protected JCMSTextField txtBarcode;

	/**
	 * Status
	 */
	protected JCMSLabel lblStatus;

	/**
	 * StatusValue
	 */
	protected JCMSLabel lblStatusValue;

	/**
	 * Vat Number
	 */
	protected JCMSLabel lblVatNo;

	/**
	 * Vat Number value
	 */
	protected JCMSTextField txtVatNo;

	/**
	 * Payment Type
	 */
	protected JCMSLabel lblPaymentType;

	/**
	 * Payment Type value
	 */
	protected JCMSComboBox cbxPaymentType;

	/**
	 * FiscalCode
	 */
	protected JCMSLabel lblFiscalCode;

	/**
	 * FiscalCode value
	 */
	protected JCMSTextField txtFiscalCode;

	/**
	 * IDType
	 */
	protected JCMSLabel lblIDType;

	/**
	 * ID value
	 */
	protected JCMSTextField txtIDType;

	/**
	 * Document Number
	 */
	protected JCMSLabel lblDocNo;

	/**
	 * Document Number value
	 */
	protected JCMSTextField txtDocNo;

	/**
	 * Supplier
	 */
	protected JCMSLabel lblSupplier;

	/**
	 * Supplier Payment
	 */
	protected JCMSComboBox cbxSupPayment;

	/**
	 * Bank
	 */
	protected JCMSLabel lblBank;

	/**
	 * Bank value
	 */
	protected JCMSTextField txtBank;

	/**
	 * Place of issue
	 */
	protected JCMSLabel lblPlcOfIssue;

	/**
	 * Place of Issue Value
	 */
	protected JCMSTextField txtPlcOfIssue;

	/**
	 * Date of issue
	 */
	protected JCMSLabel lblDateOfIssue;

	/**
	 * Date of issue value
	 */
	protected JCMSTextField txtDateOfIssue;

	/**
	 * Birthday
	 */
	private JCMSLabel lblBirthDay;

	/**
	 * BirthDay value
	 */
	private JCMSTextField txtBirthDay;

	/**
	 * BirthMonth
	 */
	private JCMSLabel lblBirthMonth;

	/**
	 * BirthMonth value
	 */
	private JCMSTextField txtBirthMonth;

	/**
	 * RealBirth
	 */
	private JCMSLabel lblRealBirth;

	/**
	 * BirthDate value
	 */
	private JCMSTextField txtBirthDate;

	/**
	 * Age
	 */
	private JCMSLabel lblAge;

	/**
	 * Selected age
	 */
	private JCMSComboBox cbxAge;

	/**
	 * Age Range
	 */
	// private final String AGE_RANGE [] = {"Under 18","18-24","25-34","35-44","45-54","55-65", "Above 65", "Not Defined"};
	// private final String AGE_RANGE_LOWER_LIMIT [] = {"0", "18", "25", "35", "45", "55", "65"};
	// private final String AGE_ESTIMATE [] = {"12", "21", "29", "39", "49", "59", "69"};
	/**
	 * Resource
	 */
	protected final ResourceBundle RESOURCE;

	/**
	 * Configuration Manager
	 */
	private static final String CONFIGURATION_FILE = "customer.cfg";
	private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
	private String custDetailPanelClassName = configMgr.getString("CUSTOMER.DETAIL_PANEL");
	protected ArmConfigLoader config;
	IApplicationManager theAppMgr;
	// private ConfigMgr config;
	// for PaymentType
	protected Vector vecKeys;
	protected Vector vecLabels;

	// for supplier payment
	protected Vector vecSupplierKeys;
	protected Vector vecSupplierLabels;
	private static String types[];

	/**
	 * ALPHA_NUMERIC WITH SPECIAL CHARACTER.
	 */
	public static final String ALPHA_NUMERIC_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";

	// private boolean ageRangeFocusFlag = true;
	private boolean birthDateFocusFlag = true;

	private boolean isBirthDateValid = false;
	private boolean errorShowned = false;
	private boolean isError = false;
	//TD
	private FocusTraversalPolicy ftPolicy = null;
	/**
	 * Default Constructor
	 */
	public CustomerDetailPanel() {
		RESOURCE = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		try {
			jbInit();
			if (custDetailPanelClassName == null) {
				//jbInit();
				cbxAge.setModel(new DefaultComboBoxModel(CustomerUtil.AGE_RANGE));
				populatePaymentTypes();
				populateSupplierPaymentTypes();
				setEnabled(false);
				txtBirthDay.addFocusListener(new FocusAdapter() {

					/**
					 * put your documentation comment here
					 * 
					 * @param e
					 */
					public void focusGained(FocusEvent e) {
						if (txtBirthDate.getText().trim().length() > 0) {
							txtBirthMonth.setEnabled(false);
			    	        cbxAge.setEnabled(false);
		   	        		txtBirthDay.transferFocus();
						}
					}

					/**
					 * put your documentation comment here
					 * 
					 * @param evt
					 */
					public void focusLost(FocusEvent evt) {
						// If FocusLost event is result of CANCEL
						// button event.
						if (theAppMgr.getStateObject("ARM_CONSUME_FOCUS_EVT") != null) {
							theAppMgr.removeStateObject("ARM_CONSUME_FOCUS_EVT");
							return;
						}
						//verifyBirthDay();
					}
				});
				txtBirthMonth.addFocusListener(new FocusAdapter() {

					/**
					 * put your documentation comment here
					 * 
					 * @param e
					 */
					public void focusGained(FocusEvent e) {
						if (txtBirthDate.getText().trim().length() > 0) {
							txtBirthDay.setEnabled(false);
							cbxAge.setEnabled(false);
							txtBirthMonth.transferFocus();
						}
					}

					/**
					 * put your documentation comment here
					 * 
					 * @param evt
					 */
					public void focusLost(FocusEvent evt) {
						// If FocusLost event is result of CANCEL
						// button event.
						if (theAppMgr.getStateObject("ARM_CONSUME_FOCUS_EVT") != null) {
							theAppMgr.removeStateObject("ARM_CONSUME_FOCUS_EVT");
							return;
						}
						//verifyBirthMonth();
					}
				});
				txtBirthDate.addFocusListener(new FocusAdapter() {

					/**
					 * put your documentation comment here
					 * 
					 * @param e
					 */
					public void focusGained(FocusEvent e) {
						txtBirthDay.setEnabled(true);
						txtBirthMonth.setEnabled(true);
						cbxAge.setEnabled(true);
					}

					/**
					 * put your documentation comment here
					 * 
					 * @param evt
					 */
					public void focusLost(FocusEvent evt) {
						// If FocusLost event is result of CANCEL
						// button event.
						if (theAppMgr.getStateObject("ARM_CONSUME_FOCUS_EVT") != null) {
							theAppMgr.removeStateObject("ARM_CONSUME_FOCUS_EVT");
							return;
						}
						if (!birthDateFocusFlag) {
							birthDateFocusFlag = true;
							return;
						}
						if (txtBirthDate.getText().trim().length() <= 0) {
							txtBirthDay.setEnabled(true);
							txtBirthMonth.setEnabled(true);
							cbxAge.setEnabled(true);
						}
					}
				});

				cbxAge.addFocusListener(new FocusAdapter() {

					/**
					 * put your documentation comment here
					 * 
					 * @param e
					 */
					public void focusGained(FocusEvent e) {
						if (txtBirthDate.getText().trim().length() > 0) {
				        	  txtBirthDate.transferFocus();
				        	  cbxAge.setEnabled(false);
				        	  txtBirthMonth.setEnabled(false);
				          }
					}
				});
			}
			reset();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//TD
		//Set the focus Policy.
		
		addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event){
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						setFocusPolicy();
					}
				});
			}
			public void ancestorMoved(AncestorEvent event) {
			}
			public void ancestorRemoved(AncestorEvent event){
			}
		});
	}
	//TD
	private void setFocusPolicy() {
		ftPolicy = getFocusCycleRootAncestor().getFocusTraversalPolicy();
	}
	
	/**
	   * put your documentation comment here
	   */
	  private boolean verifyBirthDay() throws Exception {
		  boolean bStatus = true;
		  String sTmp = txtBirthDay.getText().trim();
		  if (sTmp.length() < 1) {
			  //return;
			  bStatus = true;
		  }else {
			  try {
				  String month = "01";
				  if (txtBirthMonth.getText().trim().length() > 0)
					  month = txtBirthMonth.getText().trim();
		//		  year 2000 is choosen because it was a leap year
		
				  sTmp = month + "/" + sTmp + "/2000";
		//		  Format it in MM/dd/yyyy for testing purposes
		//		  as date string constructed is in same format
				  SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				  sdf.setLenient(false);
				  sdf.parse(sTmp);
			  } catch (Exception e) {
				  //theAppMgr.showErrorDlg(CMSApplet.res.getString("Birth day not valid"));
				  //txtBirthDay.requestFocus();
				  bStatus = false;
				  throw new Exception(CMSApplet.res.getString("Birth day not valid"));
			  }
		  }
		  return bStatus;
	  }

	  /**
	   * put your documentation comment here
	   */
	  private boolean verifyBirthMonth() throws Exception {
		  boolean bStatus = true;
		  if (txtBirthMonth.getText().trim().length() < 1) {
			  //return;
			  bStatus = true;
		  } else {
			  try {
				  int iTmp = Integer.parseInt(txtBirthMonth.getText().trim());
				  if (iTmp < 0 || iTmp > 12) {
					  throw new Exception("");
				  //} else {
				  //	  verifyBirthDay();
				  }
			  } catch (Exception e) {
				  //theAppMgr.showErrorDlg(CMSApplet.res.getString("Birth Month not valid"));
				  //txtBirthMonth.requestFocus();
				  bStatus = false;
				  throw new Exception(CMSApplet.res.getString("Birth Month not valid"));
			  }
		  }
		  return bStatus;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   * @exception BusinessRuleException
	   */
	  private boolean verifyBirthDate() throws BusinessRuleException {
		  boolean bStatus = false;
		  try {
			  String sTmp = txtBirthDate.getText().trim();
			  if (sTmp.length() < 1) {
				  bStatus = true;
				  //return true;
			  }else {
				  //Sergio
				  SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.
				  getLocalDateFormat();
				  sdf.setLenient(false);
				  Date dtBirth = sdf.parse(sTmp);
				  if(dtBirth.after(Calendar.getInstance().getTime())) {
					  //theAppMgr.showErrorDlg("Date of Birth can't be a future date.");
					  isBirthDateValid = false;
					  bStatus = false;
					  throw new BusinessRuleException("Date of Birth can't be a future date.");
				  }else{
					  isBirthDateValid = true;
					  bStatus = true;
				  }
			  }
				  //return true;
		  } catch(BusinessRuleException bre) {
			  //txtBirthDate.requestFocus();
			  isBirthDateValid = false;
			  throw bre;
		} catch (Exception e) {
			//theAppMgr.showErrorDlg("Birth Date not valid. Provide in valid 'MM/DD/YYYY' format");
			//txtBirthDate.requestFocus();
			bStatus = false;
			throw new BusinessRuleException("Birth Date not valid. Provide in valid 'MM/DD/YYYY' format");
		}
		return bStatus;
	  }

	/**
	 * To populate the payment types as elements of the relevant combo-box
	 * 
	 * @param <none>
	 */
	protected void populatePaymentTypes() {
		try {
			vecKeys = new Vector();
			vecLabels = new Vector();
			config = new ArmConfigLoader();
			String strSubTypes = config.getString("PAY_TYPE");
			if (strSubTypes == null)
				return;
			StringTokenizer stk = null;
			int i = -1;
			if (strSubTypes != null && strSubTypes.trim().length() > 0) {
				stk = new StringTokenizer(strSubTypes, ",");
			} else
				return;
			if (stk != null) {
				types = new String[stk.countTokens()];
				while(stk.hasMoreTokens()) {
					types[++i] = stk.nextToken();
					String key = config.getString(types[i] + ".CODE");
					vecKeys.add(key);
					String value = config.getString(types[i] + ".LABEL");
					vecLabels.add(value);
				}
			}
			cbxPaymentType.setModel(new DefaultComboBoxModel(vecLabels));
		} catch (Exception e) {
		}
	}

	/**
	 * set the selectedPayment Type
	 * 
	 * @param sValue
	 *            paymentType
	 */
	public void setSelectedPaymentType(String sValue) {
		if (sValue == null || sValue.length() < 1 || vecKeys.indexOf(sValue) == -1)
			return;
		cbxPaymentType.setSelectedIndex(vecKeys.indexOf(sValue));
	}

	/**
	 * Get the selected payment type (key)
	 * 
	 * @return key as String
	 */
	public String getSelectedPaymentType() {
		if (cbxPaymentType.getSelectedIndex() > 0 && cbxPaymentType.getSelectedIndex() < vecKeys.size())
			return (String) vecKeys.elementAt(cbxPaymentType.getSelectedIndex());
		return "";
	}

	/**
	 * To populate the supplier payment types as elements of the relevant combo-box
	 * 
	 * @param <none>
	 */
	protected void populateSupplierPaymentTypes() {
		try {
			String[] types = null;
			vecSupplierKeys = new Vector();
			vecSupplierLabels = new Vector();
			config = new ArmConfigLoader();
			String strSubTypes = config.getString("SUPP_PAY_TYPE");
			StringTokenizer stk = null;
			int i = -1;
			if (strSubTypes != null && strSubTypes.trim().length() > 0) {
				stk = new StringTokenizer(strSubTypes, ",");
			} else
				return;
			if (stk != null) {
				types = new String[stk.countTokens()];
				while(stk.hasMoreTokens()) {
					types[++i] = stk.nextToken();
					String key = config.getString(types[i] + ".CODE");
					vecSupplierKeys.add(key);
					String value = config.getString(types[i] + ".LABEL");
					vecSupplierLabels.add(value);
				}
			}
			cbxSupPayment.setModel(new DefaultComboBoxModel(vecSupplierLabels));
		} catch (Exception e) {
		}
	}

	/**
	 * set the supplier Payment Type
	 * 
	 * @param sValue
	 *            paymentType
	 */
	public void setSupplierPayment(String sValue) {
		if (sValue == null || sValue.trim().length() < 1 || vecSupplierKeys.indexOf(sValue) == -1)
			return;
		cbxSupPayment.setSelectedIndex(vecSupplierKeys.indexOf(sValue));
	}

	/**
	 * Get the selected supplier payment type (key)
	 * 
	 * @return key as String
	 */
	public String getSupplierPayment() {
		if (cbxSupPayment.getSelectedIndex() > 0 && cbxSupPayment.getSelectedIndex() < vecSupplierKeys.size())
			return ((String) vecSupplierKeys.elementAt(cbxSupPayment.getSelectedIndex())).trim();
		return "";
	}

	/**
	 * Set Application Manager
	 * 
	 * @param theAppMgr
	 *            ApplicationManager
	 */
	public void setAppMgr(IApplicationManager theAppMgr) {
		this.theAppMgr = theAppMgr;
		this.setBackground(theAppMgr.getBackgroundColor());
		// Loop through and set Application managers for all
		// JCMS components.
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		pnlPersInfo.setPreferredSize(new Dimension((int) (400 * r), (int) (240 * r)));
		pnlFiscal.setPreferredSize(new Dimension((int) (400 * r), (int) (400 * r)));
		pnlCompanyInfo.setPreferredSize(new Dimension((int) (400 * r), (int) (120 * r)));
		pnlBirthInfo.setPreferredSize(new Dimension((int) (400 * r), (int) (120 * r)));
		Component comps[] = this.getComponents();
		for (int iCtr = 0; iCtr < comps.length; iCtr++) {
			if (comps[iCtr] instanceof JPanel) {
				comps[iCtr].setBackground(theAppMgr.getBackgroundColor());
				getJCMSComponent((JPanel) comps[iCtr], theAppMgr);
			} else if (comps[iCtr].getClass().getName().indexOf("JCMS") != -1) {
				setAppMgrForJCMSBean(comps[iCtr], theAppMgr);
			}
		}
	}

	/**
	 * Company
	 * 
	 * @return Company
	 */
	public String getCompany() {
		return txtCompany.getText();
	}

	/**
	 * Company
	 * 
	 * @param sValue
	 *            Company
	 */
	public void setCompany(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtCompany.setText(sValue.trim());
	}

	/**
	 * InterCoCode
	 * 
	 * @return InterCoCode
	 */
	public String getInterCoCode() {
		return txtInterCoCode.getText().trim();
	}

	/**
	 * InterCoCode
	 * 
	 * @param sValue
	 *            InterCoCode
	 */
	public void setInterCoCode(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtInterCoCode.setText(sValue);
	}

	/**
	 * AcctNo
	 * 
	 * @return AcctNo
	 */
	public String getAcctNo() {
		return txtAcctNo.getText().trim();
	}

	/**
	 * AcctNo
	 * 
	 * @param sValue
	 *            AcctNo
	 */
	public void setAcctNo(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtAcctNo.setText(sValue);
	}

	/**
	 * Barcode
	 * 
	 * @return Barcode
	 */
	public String getBarcode() {
		return txtBarcode.getText().trim();
	}

	/**
	 * Barcode
	 * 
	 * @param sValue
	 *            Barcode
	 */
	public void setBarcode(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtBarcode.setText(sValue);
	}

	/**
	 * BirthDay
	 * 
	 * @return BirthDay
	 */
	public String getBirthDay() {
		return txtBirthDay.getText().trim();
	}

	/**
	 * BirthDay
	 * 
	 * @param sValue
	 *            BirthDay
	 */
	public void setBirthDay(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtBirthDay.setText(sValue);
	}

	/**
	 * BirthMonth
	 * 
	 * @return BirthMonth
	 */
	public String getBirthMonth() {
		return txtBirthMonth.getText().trim();
	}

	/**
	 * BirthMonth
	 * 
	 * @param sValue
	 *            BirthMonth
	 */
	public void setBirthMonth(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtBirthMonth.setText(sValue);
	}

	/**
	 * GetDateoFBirth
	 * 
	 * @return DateoFBirth
	 */

	private Date generateDateOfBirth() throws BusinessRuleException {
		String sBirthDate = "";
		int iBirthDay = 1;
		String sBirthDay = "01";
		int iBirthMonth = 1;
		String sBirthMonth = "01";
		int iBirthYear = 0;
		int iValidBirthDay = 31;
		isBirthDateValid = false;
		if (txtBirthDay.getText().trim().length() > 0) {
			sBirthDay = txtBirthDay.getText().trim();
			iBirthDay = Integer.parseInt(sBirthDay);
		}
		if (txtBirthMonth.getText().trim().length() > 0) {
			sBirthMonth = txtBirthMonth.getText().trim();
			iBirthMonth = Integer.parseInt(sBirthMonth);
		}
		if (cbxAge.getSelectedIndex() != CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX) {
			try {
				iBirthYear = getEstimatedBirthYear();
			} catch (Exception ex) {
				throw new BusinessRuleException("Invalid Age Range Estimate Value encountered");
			}
			// Validate Birth Month
			if (iBirthMonth < 1 || iBirthMonth > 12) {
				requestFocusOnBirthMonth();
				throw new BusinessRuleException("Birth Month not valid");
			}
			// Validate Birth day + month + year combination.
			if (iBirthYear % 4 == 0 && iBirthMonth == 2)
				iValidBirthDay = 29;
			else if (iBirthMonth == 2)
				iValidBirthDay = 28;
			else if (iBirthMonth == 4 || iBirthMonth == 6 || iBirthMonth == 9 || iBirthMonth == 11)
				iValidBirthDay = 30;
			else if (iBirthMonth < 12)
				iValidBirthDay = 31;

			if (iBirthDay > iValidBirthDay) {
				// February -- Check for Leap Year
				// in the range.
				if (iBirthMonth == 2) {
					if (iBirthDay == 29) {
						if (getAgeRangeLeapYear(iBirthYear) == -1) {
							txtBirthDay.requestFocus();
							throw new BusinessRuleException("Birth day is invalid as Birth year range doesn't have leap year");
						} else
							iBirthYear = getAgeRangeLeapYear(iBirthYear);
					} else {
						throw new BusinessRuleException("Birth Day/Month combination not correct, please verify");
					}
				}
				// All other months.
				else {
					txtBirthDay.requestFocus();
					throw new BusinessRuleException("Birth Day/Month combination not correct, please verify");
				}
			}
			// Try formatting the constructed date.
			try {
				SimpleDateFormat defaultSDF = new SimpleDateFormat("MM/dd/yy");
				sBirthDate = sBirthMonth + "/" + sBirthDay + "/" + iBirthYear;
				Date genratedDate = defaultSDF.parse(sBirthDate);
				isBirthDateValid = true;
				return genratedDate;
			} catch (Exception e) {
				txtBirthDay.requestFocus();
				throw new BusinessRuleException("Invalid Date format.");
			}
		} else if (txtBirthDay.getText().trim().length() > 0 || txtBirthMonth.getText().trim().length() > 0) {
			cbxAge.requestFocus();
			throw new BusinessRuleException("Select Age range for provided birth day / month.");
		}
		return null;
	}

	private int getAgeRangeLeapYear(int iAge) {
		for (int iCtr = 0; iCtr < 3; iCtr++, iAge++) {
			if (iAge % 4 == 0)
				return iAge;
		}
		return -1;
	}

	public Date getBirthDate() throws BusinessRuleException {
		// Birth date is not being entered.
		if (txtBirthDate.getText().trim().length() < 1 && cbxAge.getSelectedIndex() == CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX && txtBirthDay.getText().trim().length() < 1
				&& txtBirthMonth.getText().trim().length() < 1) {
			isBirthDateValid = true;
			return null;
		}
		if (txtBirthDate.getText().trim().length() < 1)
			return generateDateOfBirth();

		SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
		try {
			Date birthDate = sdf.parse(txtBirthDate.getText().trim());
			if (birthDate.after(Calendar.getInstance().getTime())) {
				isBirthDateValid = false;
				txtBirthDate.requestFocus();
				throw new BusinessRuleException("Date of Birth can't be a future date.");
			}
			isBirthDateValid = true;
			return birthDate;
		} catch (BusinessRuleException br) {
			throw br;
		} catch (Exception e) {
			isBirthDateValid = false;
		}
		return null;
	}

	/**
	 * Added this to get the JCMTXTField.
	 * 
	 * @param sValue
	 *            String
	 */
	public JCMSTextField getBirthDateTxt() {
		return this.txtBirthDate;
	}

	/**
	 * Set DateOfBirth
	 * 
	 * @param sValue
	 *            DateOfBirth
	 */
	public void setDateOfBirth(String sValue, String ageRange) {
		try {
			SimpleDateFormat localDF = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
			localDF.setLenient(false);
			Date date = localDF.parse(sValue.trim());
			if (ageRange != null && ageRange.trim().length() > 0) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				// System.out.println("Birth Date ====="+date);
				// System.out.println("Birth Day ====="+cal.get(Calendar.DAY_OF_MONTH));
				// System.out.println("Birth Month ====="+cal.get(Calendar.MONTH));
				txtBirthDay.setText("" + cal.get(Calendar.DAY_OF_MONTH));
				txtBirthMonth.setText("" + (cal.get(Calendar.MONTH) + 1));
				setAgeRange(date);
			} else {
				txtBirthDate.setText(sValue);
				setAgeRange(date);
				cbxAge.setEnabled(true);
				txtBirthDay.setText("");
				txtBirthDay.setEnabled(false);
				txtBirthMonth.setText("");
				txtBirthMonth.setEnabled(false);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return;
	}

	/**
	 * VatNo
	 * 
	 * @return VatNo
	 */
	public String getVatNo() {
		return txtVatNo.getText().trim();
	}

	/**
	 * VatNo
	 * 
	 * @param sValue
	 *            VatNo
	 */
	public void setVatNo(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtVatNo.setText(sValue);
	}

	/**
	 * FiscalCode
	 * 
	 * @return FiscalCode
	 */
	public String getFiscalCode() {
		return txtFiscalCode.getText().trim();
	}

	/**
	 * FiscalCode
	 * 
	 * @param sValue
	 *            FiscalCode
	 */
	public void setFiscalCode(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtFiscalCode.setText(sValue);
	}

	/**
	 * IDType
	 * 
	 * @return IDType
	 */
	public String getIDType() {
		return txtIDType.getText().trim();
	}

	/**
	 * IDType
	 * 
	 * @param sValue
	 *            IDType
	 */
	public void setIDType(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtIDType.setText(sValue);
	}

	/**
	 * DocNo
	 * 
	 * @return DocNo
	 */
	public String getDocNo() {
		return txtDocNo.getText().trim();
	}

	/**
	 * DocNo
	 * 
	 * @param sValue
	 *            DocNo
	 */
	public void setDocNo(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtDocNo.setText(sValue);
	}

	/**
	 * Bank
	 * 
	 * @return Bank
	 */
	public String getBank() {
		return txtBank.getText().trim();
	}

	/**
	 * Bank
	 * 
	 * @param sValue
	 *            Bank
	 */
	public void setBank(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtBank.setText(sValue);
	}

	/**
	 * PlcOfIssue
	 * 
	 * @return PlcOfIssue
	 */
	public String getPlcOfIssue() {
		return txtPlcOfIssue.getText().trim();
	}

	/**
	 * PlcOfIssue
	 * 
	 * @param sValue
	 *            PlcOfIssue
	 */
	public void setPlcOfIssue(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtPlcOfIssue.setText(sValue);
	}

	/**
	 * DateOfIssue
	 * 
	 * @return DateOfIssue
	 */
	public String getDateOfIssue() {
		return txtDateOfIssue.getText().trim();
	}

	/**
	 * Added this to get the JCMTXTField.
	 * 
	 * @param sValue
	 *            Object
	 */
	public JCMSTextField getDateOfIssueTxt() {
		return this.txtDateOfIssue;
	}

	/**
	 * DateOfIssue
	 * 
	 * @param sValue
	 *            DateOfIssue
	 */
	public void setDateOfIssue(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		txtDateOfIssue.setText(sValue);
	}

	/**
	 * Status
	 * 
	 * @return Status
	 */
	public String getStatus() {
		return lblStatusValue.getText().trim();
	}

	/**
	 * Status
	 * 
	 * @param sValue
	 *            Status
	 */
	public void setStatus(String sValue) {
		if (sValue == null || sValue.trim().length() < 1)
			return;
		lblStatusValue.setText(sValue);
	}

	/**
	 * Recursively loops through panels to fetch JCMS beans.
	 * 
	 * @param jpnl
	 *            Container
	 * @param theAppMgr
	 *            IApplicationManager
	 */
	protected void getJCMSComponent(JPanel jpnl, IApplicationManager theAppMgr) {
		Component component = null;
		for (int iCtr = 0; iCtr < jpnl.getComponentCount(); iCtr++) {
			component = jpnl.getComponent(iCtr);
			if (component instanceof JPanel) {
				component.setBackground(theAppMgr.getBackgroundColor());
				getJCMSComponent((JPanel) component, theAppMgr);
			}
			if (component.getClass().getName().indexOf("JCMS") != -1) {
				setAppMgrForJCMSBean(component, theAppMgr);
			}
		}
	}

	/**
	 * @param component
	 *            Component
	 * @param theAppMgr
	 *            IApplicationManager
	 */
	protected void setAppMgrForJCMSBean(Component component, IApplicationManager theAppMgr) {
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		if (component instanceof JCMSTextField) {
			((JCMSTextField) component).setAppMgr(theAppMgr);
			((JCMSTextField) component).setFont(theAppMgr.getTheme().getTextFieldFont());
			((JCMSTextField) component).setPreferredSize(new Dimension((int) (55 * r), (int) (25 * r)));
			((JCMSTextField) component).setMaximumSize(new Dimension((int) (55 * r), (int) (25 * r)));
			((JCMSTextField) component).setMinimumSize(new Dimension((int) (55 * r), (int) (25 * r)));
		} else if (component instanceof JCMSTextArea) {
			((JCMSTextArea) component).setAppMgr(theAppMgr);
			((JCMSTextArea) component).setFont(theAppMgr.getTheme().getTextFieldFont());
			((JCMSTextArea) component).setPreferredSize(new Dimension((int) (55 * r), (int) (50 * r)));
		} else if (component instanceof JCMSLabel) {
			((JCMSLabel) component).setAppMgr(theAppMgr);
			((JCMSLabel) component).setFont(theAppMgr.getTheme().getLabelFont());
			((JCMSLabel) component).setPreferredSize(new Dimension((int) (95 * r), (int) (25 * r)));
			((JCMSLabel) component).setMaximumSize(new Dimension((int) (95 * r), (int) (25 * r)));
			((JCMSLabel) component).setMinimumSize(new Dimension((int) (95 * r), (int) (25 * r)));
		} else if (component instanceof JCMSComboBox) {
			((JCMSComboBox) component).setAppMgr(theAppMgr);
			((JCMSComboBox) component).setPreferredSize(new Dimension((int) (55 * r), (int) (25 * r)));
			((JCMSComboBox) component).setMaximumSize(new Dimension((int) (55 * r), (int) (25 * r)));
			((JCMSComboBox) component).setMinimumSize(new Dimension((int) (55 * r), (int) (25 * r)));
		} else if (component instanceof JCMSMaskedTextField) {
			((JCMSMaskedTextField) component).setAppMgr(theAppMgr);
			((JCMSMaskedTextField) component).setFont(theAppMgr.getTheme().getTextFieldFont());
			((JCMSMaskedTextField) component).setPreferredSize(new Dimension((int) (55 * r), (int) (25 * r)));
		} else if (component instanceof JCMSCheckBox) {
			((JCMSCheckBox) component).setAppMgr(theAppMgr);
			((JCMSCheckBox) component).setFont(theAppMgr.getTheme().getTextFieldFont());
			((JCMSCheckBox) component).setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
			((JCMSCheckBox) component).setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		}
		lblSupplier.setPreferredSize(new Dimension((int) (125 * r), (int) (25 * r)));
		txtAcctNo.setPreferredSize(new Dimension((int) (125 * r), (int) (25 * r)));
		txtAcctNo.setMaximumSize(new Dimension((int) (125 * r), (int) (25 * r)));
		txtAcctNo.setMinimumSize(new Dimension((int) (125 * r), (int) (25 * r)));
	}

	/**
	 * put your documentation comment here
	 */
	public void reset() {
		txtCompany.setText("");
		txtInterCoCode.setText("");
		txtAcctNo.setText("");
		txtBarcode.setText("");
		txtBirthDay.setText("");
		txtBirthMonth.setText("");
		txtBirthDate.setText("");
		if (cbxAge.getItemCount() > 0)
			cbxAge.setSelectedIndex(cbxAge.getItemCount() - 1);
		txtVatNo.setText("");
		if (cbxPaymentType.getItemCount() > 0)
			cbxPaymentType.setSelectedIndex(0);
		txtFiscalCode.setText("");
		txtIDType.setText("");
		txtDocNo.setText("");
		if (cbxSupPayment.getItemCount() > 0)
			cbxSupPayment.setSelectedIndex(0);
		txtBank.setText("");
		txtPlcOfIssue.setText("");
		txtDateOfIssue.setText("");
		isBirthDateValid = false;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param bEnabled
	 */
	public void setEnabled(boolean bEnabled) {
		txtBirthDay.setEnabled(bEnabled);
		txtBirthMonth.setEnabled(bEnabled);
		txtBirthDate.setEnabled(bEnabled);
		cbxAge.setEnabled(bEnabled);
		txtVatNo.setEnabled(bEnabled);
		cbxPaymentType.setEnabled(bEnabled);
		txtFiscalCode.setEnabled(bEnabled);
		txtIDType.setEnabled(bEnabled);
		txtDocNo.setEnabled(bEnabled);
		cbxSupPayment.setEnabled(bEnabled);
		txtBank.setEnabled(bEnabled);
		txtPlcOfIssue.setEnabled(bEnabled);
		txtDateOfIssue.setEnabled(bEnabled);
	}

	/**
	 * put your documentation comment here
	 */
	public void makeReadOnly() {
		txtCompany.setEnabled(false);
		txtInterCoCode.setEnabled(false);
		txtInterCoCode.setText("");
		txtAcctNo.setEnabled(false);
		txtBarcode.setEnabled(false);
	}

	public boolean isValidDateOfBirth() {
		return isBirthDateValid;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param dWidth
	 * @param dHeight
	 */
	public void resize(double dWidth, double dHeight) {
		// pnlPersInfo.setPreferredSize(new Dimension((int)dWidth, (int)(dHeight * 0.35)));
		// pnlFiscal.setPreferredSize(new Dimension((int)dWidth, (int)(dHeight * 0.40)));
		// pnlOthPersInfo.setPreferredSize(new Dimension((int)dWidth, (int)(dHeight * 0.25)));
		// pnlPersInfo.doLayout();
		// pnlFiscal.doLayout();
		// pnlOthPersInfo.doLayout();
		// doLayout();
	}

	/**
	 * Initialize components.
	 */
	private void initComponents() {
		pnlPersInfo = new JPanel();
		pnlCompanyInfo = new JPanel();
		pnlBirthInfo = new JPanel();
		pnlFiscal = new JPanel();
		// pnlOthPersInfo = new JPanel();
		lblCompany = new JCMSLabel();
		txtCompany = getJCMSTextField();
		lblInterCoCode = new JCMSLabel();
		txtInterCoCode = getJCMSTextField();
		lblAcctNo = new JCMSLabel();
		txtAcctNo = getJCMSTextField();
		lblBarcode = new JCMSLabel();
		txtBarcode = getJCMSTextField();
		lblStatus = new JCMSLabel();
		lblStatusValue = new JCMSLabel();
		lblBirthDay = new JCMSLabel();
		txtBirthDay = getJCMSTextField();
		lblBirthMonth = new JCMSLabel();
		txtBirthMonth = getJCMSTextField();
		txtBirthMonth.setName("BIRTH_MTH");
		lblRealBirth = new JCMSLabel();
		txtBirthDate = getJCMSTextField();
		txtBirthDate.setName("BIRTH_DATE");
		lblAge = new JCMSLabel();
		cbxAge = new JCMSComboBox();
		lblVatNo = new JCMSLabel();
		txtVatNo = getJCMSTextField();
		lblPaymentType = new JCMSLabel();
		cbxPaymentType = new JCMSComboBox();
		lblFiscalCode = new JCMSLabel();
		txtFiscalCode = getJCMSTextField();
		lblIDType = new JCMSLabel();
		txtIDType = getJCMSTextField();
		lblDocNo = new JCMSLabel();
		txtDocNo = getJCMSTextField();
		lblSupplier = new JCMSLabel();
		cbxSupPayment = new JCMSComboBox();
		lblBank = new JCMSLabel();
		txtBank = getJCMSTextField();
		lblPlcOfIssue = new JCMSLabel();
		txtPlcOfIssue = getJCMSTextField();
		lblDateOfIssue = new JCMSLabel();
		txtDateOfIssue = getJCMSTextField();
		txtDateOfIssue.setName("DateOfIssue");
		makeReadOnly();
		lblCompany.setLabelFor(txtCompany);
		lblCompany.setText(RESOURCE.getString("Company"));
		lblInterCoCode.setLabelFor(txtInterCoCode);
		lblInterCoCode.setText(RESOURCE.getString("Inter Co Code"));
		lblAcctNo.setLabelFor(txtAcctNo);
		lblAcctNo.setText(RESOURCE.getString("Account No"));
		lblBarcode.setLabelFor(txtBarcode);
		lblBarcode.setText(RESOURCE.getString("Barcode"));
		lblStatus.setLabelFor(lblStatusValue);
		lblStatus.setPreferredSize(new Dimension(20, 15));
		lblStatus.setText(RESOURCE.getString("Status"));
		lblStatusValue.setText("");
		lblBirthDay.setText(RESOURCE.getString("Birth day"));
		lblBirthMonth.setText(RESOURCE.getString("Birth month"));
		lblRealBirth.setText(RESOURCE.getString("Real Birth Date"));
		lblAge.setText(RESOURCE.getString("Age"));
		lblVatNo.setLabelFor(txtVatNo);
		lblVatNo.setText(RESOURCE.getString("Vat No"));
		lblPaymentType.setLabelFor(cbxPaymentType);
		lblPaymentType.setText(RESOURCE.getString("Payment Type"));
		lblFiscalCode.setLabelFor(txtFiscalCode);
		lblFiscalCode.setText(RESOURCE.getString("Fiscal Code"));
		lblIDType.setLabelFor(txtIDType);
		lblIDType.setText(RESOURCE.getString("ID Type"));
		lblDocNo.setLabelFor(txtDocNo);
		lblDocNo.setText(RESOURCE.getString("Document Number"));
		lblSupplier.setLabelFor(cbxSupPayment);
		lblSupplier.setText(RESOURCE.getString("Supplier Payment"));
		lblBank.setLabelFor(txtBank);
		lblBank.setText(RESOURCE.getString("Bank"));
		lblPlcOfIssue.setLabelFor(txtPlcOfIssue);
		lblPlcOfIssue.setText(RESOURCE.getString("Place of Issue"));
		lblDateOfIssue.setLabelFor(txtDateOfIssue);
		lblDateOfIssue.setText(RESOURCE.getString("Date of Issue"));
		pnlPersInfo.setLayout(new BorderLayout());
		pnlFiscal.setLayout(new GridBagLayout());
		pnlFiscal.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), RESOURCE.getString("Fiscal Details")));
		pnlCompanyInfo.setLayout(new GridBagLayout());
		pnlBirthInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
		pnlBirthInfo.setLayout(new GridBagLayout());
		pnlCompanyInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
		pnlPersInfo.add(pnlCompanyInfo, java.awt.BorderLayout.NORTH);
		pnlPersInfo.add(pnlBirthInfo, java.awt.BorderLayout.CENTER);
	}

	private JCMSTextField getJCMSTextField(){
		if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
			return new JCMSTextField_JP();
		}
		
		return  new JCMSTextField();
	}
	
	/**
	 * Added RequestFocus on BirthDate
	 */
	public void requestFocusToBirthDay() {
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {

					/**
					 * put your documentation comment here
					 */
					public void run() {
						txtBirthDate.requestFocus();
					}
				});
			}
		});
	}

	/**
	 * Added RequestFocus on IssueDate
	 */
	public void requestFocusToIssueDate() {
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {

					/**
					 * put your documentation comment here
					 */
					public void run() {
						txtDateOfIssue.requestFocus();
					}
				});
			}
		});
	}

	/**
	 * Added RequestFocus on BirtMonth
	 */
	public void requestFocusOnBirthMonth() {
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {

					/**
					 * put your documentation comment here
					 */
					public void run() {
						txtBirthMonth.requestFocus();
					}
				});
			}
		});
	}

	/**
	 * Layout components.
	 * 
	 * @throws Exception
	 */
	protected void jbInit() throws Exception {
		initComponents();
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), RESOURCE.getString("Detail Information")));
		this.setLayout(new BorderLayout());
		this.add(pnlPersInfo, java.awt.BorderLayout.NORTH);
		// this.add(pnlOthPersInfo, java.awt.BorderLayout.SOUTH);
		this.add(pnlFiscal, java.awt.BorderLayout.CENTER);
		pnlCompanyInfo.add(lblCompany,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 75, 0));
		pnlCompanyInfo.add(txtCompany,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 75, 1)); //
		pnlCompanyInfo.add(lblInterCoCode, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 75, 0));
		pnlCompanyInfo.add(txtInterCoCode, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 75, 1));
		pnlCompanyInfo.add(lblAcctNo,      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 75, 0));
		pnlCompanyInfo.add(txtAcctNo,      new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 5), 75, 1));
		pnlCompanyInfo.add(lblBarcode,     new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 75, 0));
		pnlCompanyInfo.add(txtBarcode,     new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 0), 75, 1));
		pnlCompanyInfo.add(lblStatus,      new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 5, 0, 0), 30, 0)); //
		pnlCompanyInfo.add(lblStatusValue, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 1, 0, 5), 35, 0)); //
		pnlBirthInfo.add(lblBirthDay,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 7, 0, 0), 45, 0));
		pnlBirthInfo.add(txtBirthDay,   new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 1), 55, 1));
		pnlBirthInfo.add(lblBirthMonth, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 51, 0));
		pnlBirthInfo.add(txtBirthMonth, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 1), 68, 1));
		pnlBirthInfo.add(lblRealBirth,  new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 84, 0));
		pnlBirthInfo.add(txtBirthDate,  new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 0), 6, 1));
		pnlBirthInfo.add(lblAge,        new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 8, 0, 14), 43, 0));
		pnlBirthInfo.add(cbxAge,        new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 13, 7, 3), 51, 1));
		pnlFiscal.add(lblIDType,     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 4, 0, 78), 46, 0));
		pnlFiscal.add(txtFiscalCode, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 4, 0, -30), 3, 1));
		pnlFiscal.add(txtIDType,     new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 0, -30), 4, 1));
		pnlFiscal.add(lblFiscalCode, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 19, 0));
		pnlFiscal.add(lblVatNo,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(-6, 4, 0, 0), 22, 0));
		pnlFiscal.add(lblDocNo,      new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 4, 0, 68), 69, 0));
		pnlFiscal.add(txtVatNo,      new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 4, 0, -30), 2, 1));
		pnlFiscal.add(txtDocNo,      new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 3, 25), -1, 1));
		pnlFiscal.add(lblPlcOfIssue, new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, -20, 0, 52), 66, 0));
		pnlFiscal.add(lblBank,       new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 45, 0, 0), 77, 0));
		pnlFiscal.add(lblSupplier,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 45, 0, 0), 8, 0));
		pnlFiscal.add(lblPaymentType,new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(-6, 45, 0, 0), 89, 0));
		pnlFiscal.add(cbxSupPayment, new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 45, 0, 0), 44, 1));
		pnlFiscal.add(cbxPaymentType,new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 45, 0, 0), 44, 1));
		pnlFiscal.add(txtPlcOfIssue, new GridBagConstraints(1, 7, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, -20, 3, -20), 2, 1));
		pnlFiscal.add(lblDateOfIssue,new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 25, 0, 4), 94, 0));
		pnlFiscal.add(txtBank,       new GridBagConstraints(2, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 45, 0, 2), 11, 0));
		pnlFiscal.add(txtDateOfIssue,new GridBagConstraints(3, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 3, 2), 26, 1));
		/**
		 * Added TextFilter
		 */	
		txtVatNo.setDocument(new CMSTextFilter(25));
		txtFiscalCode.setDocument(new CMSTextFilter(25));
		txtIDType.setDocument(new CMSTextFilter(20));
		txtDocNo.setDocument(new CMSTextFilter(25));
		txtPlcOfIssue.setDocument(new CMSTextFilter(25));
		txtDateOfIssue.setDocument(new CMSTextFilter(10));
		txtAcctNo.setDocument(new CMSTextFilter(11));
		txtBarcode.setDocument(new CMSTextFilter(11));
		txtBirthDate.setDocument(new CMSTextFilter(10));
		txtBirthDay.setDocument(new TextFilter(TextFilter.NUMERIC, 2));
		txtBirthMonth.setDocument(new TextFilter(TextFilter.NUMERIC, 2));
		txtInterCoCode.setDocument(new CMSTextFilter(11));
		txtCompany.setDocument(new CMSTextFilter(11));
		txtBirthDay.setInputVerifier(new BirthDayVerifier());
	    txtBirthMonth.setInputVerifier(new BirthMonthVerifier());
	    txtBirthDate.setInputVerifier(new BirthDateVerifier());
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusOnFirstField() {
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				txtBirthDay.requestFocus();
			}
		});
	}

	/**
	 * Method to modify all registered listeners that an update is needed.
	 * 
	 * @param e
	 */
	public void fireMessageEventOccured(MessageEvent me) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == MessageListener.class) {
				if (me == null)
					me = new MessageEvent(this, "");
				((MessageListener) listeners[i + 1]).messageOccurred(me);
			}
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param date
	 */
	private void setAgeRange(Date date) {
		int ageRangeIndex = CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX;
		try {
			ageRangeIndex = CustomerUtil.getAgeRangeIndex(date);
			cbxAge.setSelectedIndex(ageRangeIndex);
			cbxAge.setEnabled(false);
		} catch (Exception ex) {
			// ageRangeIndex = CustomerUtil.NOT_DEFINED_INDEX;
			cbxAge.setSelectedIndex(ageRangeIndex);
			cbxAge.setEnabled(true);
		}
	}

	public void setAgeRangeCode(String ageRange) {
		int ageRangeIndex = CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX;
		try {
			ageRangeIndex = Integer.parseInt(ageRange);
			if (ageRangeIndex < 0 || ageRangeIndex > CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX)
				ageRangeIndex = CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		cbxAge.setSelectedIndex(ageRangeIndex);
		cbxAge.setEnabled(true);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getAgeRangeCode() {
		if (txtBirthDate.getText().trim().length() < 1)
			return "" + (cbxAge.getSelectedIndex() + 1);
		else
			// its a calculated value
			return "";
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 * @exception Exception
	 */
	private int getEstimatedBirthYear() throws Exception {
		return Calendar.getInstance().get(Calendar.YEAR) - CustomerUtil.getAgeEstimateForAgeRangeIndex(cbxAge.getSelectedIndex());
	}

	public JCMSTextField getLastFieldOnScreen() {
		return txtDateOfIssue;
	}
	
	class LocalFocusPolicy extends FocusTraversalPolicy {
		FocusTraversalPolicy ftp = null;
		public LocalFocusPolicy( FocusTraversalPolicy fPolicy ) {
			ftp = fPolicy;
		}
		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			if (aComponent.equals(txtBirthDay)  ) {
				if( isError ) {
					return txtBirthDay;
				}else{
					if(errorShowned) {
						errorShowned = false;
					}
	            	return ftp.getComponentAfter(focusCycleRoot, aComponent);
				}
            } else if (aComponent.equals(txtBirthMonth) ) {
				if( isError ) {
					return txtBirthMonth;
				}else{
					if(errorShowned) {
						errorShowned = false;
					}
	            	return ftp.getComponentAfter(focusCycleRoot, aComponent);
				}
            } else if (aComponent.equals(txtBirthDate) ) {
				if( isError ) {
					return txtBirthDate;
				}else{
					if(errorShowned) {
						errorShowned = false;
					}
	            	return ftp.getComponentAfter(focusCycleRoot, aComponent);
				}
            } else {
            	return ftp.getComponentAfter(focusCycleRoot, aComponent);
            }
		}
		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			if (aComponent.equals(txtBirthDay)  ) {
				if( isError ) {
					return txtBirthDay;
				}else{
					if(errorShowned) {
						errorShowned = false;
					}
	            	return ftp.getComponentBefore(focusCycleRoot, aComponent);
				}
            } else if (aComponent.equals(txtBirthMonth) ) {
				if( isError ) {
					return txtBirthMonth;
				}else{
					if(errorShowned) {
						errorShowned = false;
					}
	            	return ftp.getComponentBefore(focusCycleRoot, aComponent);
				}
            } else if (aComponent.equals(txtBirthDate) ) {
				if( isError ) {
					return txtBirthDate;
				}else{
					if(errorShowned) {
						errorShowned = false;
					}
	            	return ftp.getComponentBefore(focusCycleRoot, aComponent);
				}
            } else {
            	return ftp.getComponentBefore(focusCycleRoot, aComponent);
            }
		}
		public Component getDefaultComponent(Container focusCycleRoot) {
            return ftp.getDefaultComponent(focusCycleRoot);
		}
		public Component getLastComponent(Container focusCycleRoot) {
            return ftp.getLastComponent(focusCycleRoot);
		}
		public Component getFirstComponent(Container focusCycleRoot) {
            return ftp.getFirstComponent(focusCycleRoot);
		}
	}

	class BirthDayVerifier extends InputVerifier
	{
		private String strErrMessage = null;
		public BirthDayVerifier() {
		}
		public boolean verify(JComponent input) {
			boolean bRetStatus = true;
			try {
				verifyBirthDay();
				isError=false;
			}catch(Exception e) {
				isError=true;
				bRetStatus = false;
				strErrMessage = e.getMessage();
			}
			return bRetStatus;
		}
		public boolean shouldYieldFocus(JComponent input) {
			boolean valid = super.shouldYieldFocus(input);
			final JTextField tf = (JTextField) input;
			if ( !valid ) {
				if(errorShowned)isError=false;
				if(!errorShowned) {
					theAppMgr.showErrorDlg(strErrMessage);
					errorShowned = true;
				}
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						tf.setFocusable(true);
						tf.requestFocusInWindow(); 
					}
				});
			}else{
				errorShowned = false;
			}
			return valid;
		}
	}
	
	class BirthMonthVerifier extends InputVerifier
	{
		String strErrMessage = null;
		boolean bInvalidBirthDay = false;
		public BirthMonthVerifier() {
		}
		
		public boolean verify(JComponent input) {
			boolean bRetStatus = true;
			try {
				verifyBirthMonth();
				isError=false;
			}catch(Exception e) {
				isError=true;
				bRetStatus = false;
				strErrMessage = e.getMessage();
			}
			if(bRetStatus) {
				try {
					verifyBirthDay();
					isError=false;
				}catch(Exception e) {
					bInvalidBirthDay = true;
					isError=true;
					bRetStatus = false;
					strErrMessage = e.getMessage();
				}
			}
			return bRetStatus;
		}
		
		public boolean shouldYieldFocus(JComponent input) {
			boolean valid = super.shouldYieldFocus(input);
			final JTextField tf = (JTextField) input;
			if ( !valid ) {
				//---------------------------------
				if(bInvalidBirthDay) {
					valid = true;
					//if(!errorShowned) {
					//	theAppMgr.showErrorDlg(strErrMessage);
					//	errorShowned = true;
					//}
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							txtBirthDay.setFocusable(true);
							txtBirthDay.requestFocusInWindow();
						}
					});
				}else{
				//---------------------------------
					if(errorShowned)isError=false;
					if(!errorShowned) {
						theAppMgr.showErrorDlg(strErrMessage);
						errorShowned = true;
					}
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							tf.setFocusable(true);
							tf.requestFocusInWindow(); 
						}
					});
				}
			}else{
				errorShowned = false;
			}
			return valid;
		}
	}
	
	class BirthDateVerifier extends InputVerifier
	{
		String strErrMessage = null;
		public BirthDateVerifier() {
		}
		
		public boolean verify(JComponent input) {
			boolean bRetStatus = true;
			try {
				verifyBirthDate();
				//isError=false;
        		try {
        			//ADD the check for the Blank BirthDate if required.
        			//TDOD - Whether this condition is required or not.
        			if(txtBirthDate.getText().trim().length() > 0 ) {
	        			SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
	        			sdf.setLenient(false);
	        			setAgeRange(sdf.parse(txtBirthDate.getText()));
	        			cbxAge.setEnabled(false);
	        			txtBirthDay.setText("");
	        			txtBirthDay.setEnabled(false);
	        			txtBirthMonth.setText("");
	        			txtBirthMonth.setEnabled(false);
	        			isBirthDateValid = true;
        			}else{
        				//SET the Age Range -- Not Defined
        				int ageRangeIndex = CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX;
        				cbxAge.setSelectedIndex(ageRangeIndex);
        			}
    				isError=false;
        		} catch (Exception e) {
        			birthDateFocusFlag = false;
        			isBirthDateValid = false;
        			strErrMessage="Birth Date not valid. Provide in valid 'MM/DD/YYYY' format";
        			isError=true;
        			bRetStatus = false;
        		}
			}catch(Exception e) {
				isError=true;
				bRetStatus = false;
				strErrMessage = e.getMessage();
			}
			return bRetStatus;
		}
		public boolean shouldYieldFocus(JComponent input) {
			boolean valid = super.shouldYieldFocus(input);
			final JTextField tf = (JTextField) input;
			if ( !valid ) {
				if(errorShowned)isError=false;
				if(!errorShowned) {
					theAppMgr.showErrorDlg(strErrMessage);
					errorShowned = true;
				}
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						tf.setFocusable(true);
						tf.requestFocus(); 
					}
				});
			}else{
				errorShowned = false;
			}
			return valid;
		}
	}
	
}
