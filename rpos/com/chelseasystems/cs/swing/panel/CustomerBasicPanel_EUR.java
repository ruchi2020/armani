/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.CMSTextFilter;
import com.chelseasystems.cs.util.CustomerUtil;

/**
 * <p>Title:CustomerBasicPanel_EUR.java </p>
 *
 * <p>Description: Displays customer's basic information for Europe </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc. </p>
 *
 * @author Sandhya Ajit
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 11-29-2006 | Sandhya   | PCR 9     | Move age range and the birth dates.                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerBasicPanel_EUR extends CustomerBasicPanel {
	
  private static final long serialVersionUID = 1L; 
  
  private CustomerDetailPanel_EUR pnlCustDetail;
 
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
  
  IApplicationManager theAppMgr;
  private boolean isBirthDateValid = false;
  private boolean birthDateFocusFlag = true;
  private boolean errorShowned = false;
  private boolean isError = false;
  /**
   * Default constructor
   */
  public CustomerBasicPanel_EUR() {
	try {
    	jbInit();
    	cbxAge.setModel(new DefaultComboBoxModel(CustomerUtil.AGE_RANGE));
    	setEnabled(false);
    	
    	txtBirthDay.addFocusListener(new FocusAdapter() {

   	        /**
   	         * put your documentation comment here
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
    	     * @param evt
    	     */
   	        public void focusLost(FocusEvent evt) {
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
	         * @param evt
	         */
	        public void focusLost(FocusEvent evt) {
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
	         * @param e
	         */
	        public void focusGained(FocusEvent e) {
	          txtBirthDay.setEnabled(true);
	          txtBirthMonth.setEnabled(true);
	          cbxAge.setEnabled(true);
	        }
	        
	        /**
	         * put your documentation comment here
	         * @param evt
	         */
	        public void focusLost(FocusEvent evt) {
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
		reset();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
    //This has been added to set the Custom Focus Traversal Policy when this panel is visible.
    addAncestorListener(new AncestorListener() {
		public void ancestorAdded(AncestorEvent event){
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					doSetPolicy();
				}
			});
		}
		public void ancestorMoved(AncestorEvent event) {
		}
		public void ancestorRemoved(AncestorEvent event){
			getFocusCycleRootAncestor().setFocusTraversalPolicy(null);
		}
	});
    
	}

	private void doSetPolicy() {
		getFocusCycleRootAncestor().setFocusTraversalPolicy(new LocalFocusPolicy(getFocusCycleRootAncestor().getFocusTraversalPolicy()));
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
   * GetDateoFBirth
   * @return DateoFBirth
   */
  private Date generateDateOfBirth()
  		throws BusinessRuleException {
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
        throw new BusinessRuleException(
            "Invalid Age Range Estimate Value encountered");
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
      else if (iBirthMonth == 4 || iBirthMonth == 6 || iBirthMonth == 9 ||
    		  iBirthMonth == 11)
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
              throw new BusinessRuleException(
                  "Birth day is invalid as Birth year range doesn't have leap year");
            } else
              iBirthYear = getAgeRangeLeapYear(iBirthYear);
          } else {
            throw new BusinessRuleException(
                "Birth Day/Month combination not correct, please verify");
          }
       }
       // All other months.
       else {
         txtBirthDay.requestFocus();
         throw new BusinessRuleException(
             "Birth Day/Month combination not correct, please verify");
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
      } else if (txtBirthDay.getText().trim().length() > 0
          || txtBirthMonth.getText().trim().length() > 0) {
        cbxAge.requestFocus();
        throw new BusinessRuleException(
            "Select Age range for provided birth day / month.");
      }
      return null;
  }
  
  /**
   * put your documentation comment here
   * @param date
   */
  private void setAgeRange(Date date) {
    int ageRangeIndex = CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX;
    try {
        ageRangeIndex = CustomerUtil.getAgeRangeIndex(date);
        cbxAge.setSelectedIndex(ageRangeIndex);
        cbxAge.setEnabled(false);
      } catch (Exception ex) {
        cbxAge.setSelectedIndex(ageRangeIndex);
        cbxAge.setEnabled(true);
    }
  }
  
  /**
   * 
   * @param iAge
   * @return
   */
  private int getAgeRangeLeapYear(int iAge) {
	for (int iCtr = 0; iCtr < 3; iCtr++, iAge++) {
		if (iAge % 4 == 0)return iAge;
	   }
	   return -1;
  }
  
  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  private int getEstimatedBirthYear()
  		throws Exception {
    return Calendar.getInstance().get(Calendar.YEAR)
          - CustomerUtil.getAgeEstimateForAgeRangeIndex(cbxAge.getSelectedIndex());
  }
  
  public boolean isValidDateOfBirth() {
	return isBirthDateValid;
  }
  
  /**
   * BirthDay
   * @param sValue BirthDay
   */
  public void setBirthDay(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtBirthDay.setText(sValue);
  }
   
  /**
   * BirthDay
   * @return BirthDay
   */
  public String getBirthDay() {
    return txtBirthDay.getText().trim();
  }
  
  /**
   * BirthMonth
   * @param sValue BirthMonth
   */
  public void setBirthMonth(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    txtBirthMonth.setText(sValue);
  }  

  /**
   * BirthMonth
   * @return BirthMonth
   */
  public String getBirthMonth() {
    return txtBirthMonth.getText().trim();
  }
  
  /** Set DateOfBirth
   * @param sValue DateOfBirth
   */
  public void setDateOfBirth(String sValue, String ageRange) {
    try {
    	SimpleDateFormat localDF = com.chelseasystems.cs.util.DateFormatUtil.
            getLocalDateFormat();
        localDF.setLenient(false);
        Date date = localDF.parse(sValue.trim());
        if (ageRange != null && ageRange.trim().length() > 0) {
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(date);
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
    } catch (Exception e) {}
      return;
  }
  
  /**
   * DateOfBirth
   * @throws BusinessRuleException
   */
  public Date getBirthDate() throws BusinessRuleException {
	  // Birth date is not being entered.
	  if (txtBirthDate.getText().trim().length() < 1 &&
	      cbxAge.getSelectedIndex() == CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX
	      && txtBirthDay.getText().trim().length() < 1 &&
	      txtBirthMonth.getText().trim().length() < 1) {
	      isBirthDateValid = true;
	      return null;
	  }
	  if (txtBirthDate.getText().trim().length() < 1)
	    return generateDateOfBirth();
	
	  SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
	  try {
	  	Date birthDate = sdf.parse(txtBirthDate.getText().trim());
	      if(birthDate.after(Calendar.getInstance().getTime())) {
	         isBirthDateValid = false;
	         txtBirthDate.requestFocus();
	         throw new BusinessRuleException("Date of Birth can't be a future date.");
	      }
	      isBirthDateValid = true;
	      return birthDate;
	    }
	    catch (BusinessRuleException br) {
	      throw br;
	    }
	    catch (Exception e) {
	      isBirthDateValid = false;
	    }
	    return null;
  }
  
  /**
   * 
   * @param ageRange
   */
  public void setAgeRangeCode(String ageRange) {
	int ageRangeIndex = CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX;
	try {
		ageRangeIndex = Integer.parseInt(ageRange);
	    if(ageRangeIndex < 0 || ageRangeIndex > CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX)
	    	ageRangeIndex = CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX;
	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }
	    cbxAge.setSelectedIndex(ageRangeIndex);
	    cbxAge.setEnabled(true);
  }
  
  /**
   * put your documentation comment here
   * @return
   */
  public String getAgeRangeCode() {
    if (txtBirthDate.getText().trim().length() < 1)
      return "" + (cbxAge.getSelectedIndex() + 1);
    else //its a calculated value
      return "";
  }
  
  /**
   * Added this to get the JCMTXTField.
   * @param sValue String
   */
  public JCMSTextField getBirthDateTxt() {
    return this.txtBirthDate;
  }  
  
  /************* Reference Methods Starts  *************/
  /**
   * Set primary Email.
   * @param sValue PrimaryEmail/SMS
   */
  public void setPrimaryEmail(String sValue) {
    (this.pnlCustDetail).setPrimaryEmail(sValue);
  }

  /**
   * Get PrimaryEmail
   * @return PrimaryEmail/SMS
   */
  public String getPrimaryEmail() {
	return (this.pnlCustDetail.getPrimaryEmail());
  }

  /**
   * Set SecondaryEmail.
   * @param sValue SecondaryEmail/SMS
   */
  public void setSecondaryEmail(String sValue) {
    (this.pnlCustDetail).setSecondaryEmail(sValue);
  }

  /**
   * Set SecondaryEmail
   * @return SecondaryEmail/SMS
   */
  public String getSecondaryEmail() {
	return (this.pnlCustDetail.getSecondaryEmail());
  }

  /**
   * Set ReturnMail
   * @param bReturnMail True/False
   */
  public void setReturnMail(boolean bReturnMail) {
    (this.pnlCustDetail).setReturnMail(bReturnMail);
  }

  /**
   * GetReturnMail
   * @return ReturnMail
   */
  public boolean getReturnMail() {
    return (this.pnlCustDetail.getReturnMail());
  }

  /**
   * Set ReturnMail
   * @param bReturnMail Y/N
   */
  public void setReturnMail(String sValue) {
    (this.pnlCustDetail).setReturnMail(sValue);
  }

  /**
   * Get ReturnMail as String
   * @return Y or N
   */
  public String getReturnMailString() {
    return (this.pnlCustDetail.getReturnMailString());
  }
  
  public void requestFocusOnPrimaryEmail() {
	(this.pnlCustDetail).requestFocusOnPrimaryEmail();
  }
  
  public void requestFocusOnSecondaryEmail() {
	(this.pnlCustDetail).requestFocusOnSecondaryEmail();
  }
  
  /**
   * Sets the detail ref.
   * @param CustomerDetailPanel_EUR pnlCustDetail
   */
  public void setDetailReference(CustomerDetailPanel_EUR pnlCustDetail) {
	this.pnlCustDetail = pnlCustDetail;
  }
  /************* Reference Methods Ends  *************/
  
  /**
   * put your documentation comment here
   */
  public void reset() {
    txtID.setText("");
    txtTitle.setText("");
    txtFirstName.setText("");
    txtMidName.setText("");
    txtLastName.setText("");
    txtSuffix.setText("");
    txtFirstNameJP.setText("");
    txtLastNameJP.setText("");
    txtBirthDay.setText("");
    txtBirthMonth.setText("");
    txtBirthDate.setText("");
    if (cbxAge.getItemCount() > 0)
      cbxAge.setSelectedIndex(cbxAge.getItemCount() - 1);
    isBirthDateValid = false;
  }
  
  /**
   * put your documentation comment here
   * @param bEnabled
   */
  public void setEnabled(boolean bEnabled) {
    txtTitle.setEnabled(bEnabled);
    txtFirstName.setEnabled(bEnabled);
    txtMidName.setEnabled(bEnabled);
    txtLastName.setEnabled(bEnabled);
    txtSuffix.setEnabled(bEnabled);
    txtFirstNameJP.setEnabled(bEnabled);
    txtLastNameJP.setEnabled(bEnabled);
    txtBirthDay.setEnabled(bEnabled);
    txtBirthMonth.setEnabled(bEnabled);
    txtBirthDate.setEnabled(bEnabled);
    cbxAge.setEnabled(bEnabled);
  }
  
  /**
   * Set Application Manager
   * @param theAppMgr ApplicationManager
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
	this.theAppMgr = theAppMgr;
    this.setBackground(theAppMgr.getBackgroundColor());
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    //Loop through and set Application managers for all
    // JCMS components.
    for (int iCtr = 0; iCtr < this.getComponentCount(); iCtr++) {
      Component component = this.getComponent(iCtr);
      Font lblFont = theAppMgr.getTheme().getLabelFont();
      Font txtFont = theAppMgr.getTheme().getTextFieldFont();
      if (component instanceof JCMSTextField) {
        ((JCMSTextField)component).setAppMgr(theAppMgr);
        ((JCMSTextField)component).setFont(txtFont);
        ((JCMSTextField)component).setPreferredSize(new Dimension((int)(55 * r), (int)(25 * r)));
      } else if (component instanceof JCMSTextArea) {
        ((JCMSTextArea)component).setAppMgr(theAppMgr);
        ((JCMSTextArea)component).setFont(txtFont);
        ((JCMSTextArea)component).setPreferredSize(new Dimension((int)(55 * r), (int)(50 * r)));
      } else if (component instanceof JCMSLabel) {
        ((JCMSLabel)component).setAppMgr(theAppMgr);
        ((JCMSLabel)component).setFont(lblFont);
        ((JCMSLabel)component).setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
      } else if (component instanceof JCMSComboBox) {
        ((JCMSComboBox)component).setAppMgr(theAppMgr);
        ((JCMSComboBox)component).setPreferredSize(new Dimension((int)(120 * r), (int)(25 * r)));
        ((JCMSComboBox)component).setMaximumSize(new Dimension((int)(120 * r), (int)(25 * r)));
        ((JCMSComboBox)component).setMinimumSize(new Dimension((int)(120 * r), (int)(25 * r)));
      } else if (component instanceof JCMSMaskedTextField) {
        ((JCMSMaskedTextField)component).setAppMgr(theAppMgr);
        ((JCMSMaskedTextField)component).setFont(txtFont);
        ((JCMSMaskedTextField)component).setPreferredSize(new Dimension((int)(55 * r), (int)(25 * r)));
      } else if (component instanceof JCMSCheckBox) {
        ((JCMSCheckBox)component).setAppMgr(theAppMgr);
        ((JCMSCheckBox)component).setFont(txtFont);
      }
      txtID.setBackground(theAppMgr.getBackgroundColor());
    }
    txtBirthDay.setAppMgr(theAppMgr);
    txtBirthDay.setFont(theAppMgr.getTheme().getTextFieldFont());
    txtBirthDay.setPreferredSize(new Dimension((int)(55 * r), (int)(25 * r)));
  }

  /**
   * Initialize and Layout components
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    java.util.ResourceBundle resBundle = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    lblFirstNameJP = new JCMSLabel();
    txtFirstNameJP = new JCMSTextField();
    lblLastNameJP = new JCMSLabel();
    txtLastNameJP = new JCMSTextField();
    lblID = new JCMSLabel();
    txtID = new JCMSTextField();
    lblTitle = new JCMSLabel();
    lblFirstName = new JCMSLabel();
    lblMiddleName = new JCMSLabel();
    lblLastName = new JCMSLabel();
    lblSuffix = new JCMSLabel();
    lblBirthDay = new JCMSLabel();
    txtBirthDay = new JCMSTextField();
    lblBirthMonth = new JCMSLabel();
    txtBirthMonth = new JCMSTextField();
    txtBirthMonth.setName("BIRTH_MTH");
    lblRealBirth = new JCMSLabel();
    txtBirthDate = new JCMSTextField();
    txtBirthDate.setName("BIRTH_DATE");
    lblAge = new JCMSLabel();
    cbxAge = new JCMSComboBox();
    txtTitle = new JCMSTextField();
    txtTitle.setName("Title");
    txtFirstName = new JCMSTextField();
    txtFirstName.setName("FIRST_NAME");
    txtMidName = new JCMSTextField();
    txtLastName = new JCMSTextField();
    txtLastName.setName("SECOND_NAME");
    txtSuffix = new JCMSTextField();
    txtSuffix.setName("SUFFIX");
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    this.setLayout(gridBagLayout1);
    //this.setPreferredSize(new Dimension(844, 155));
    lblTitle.setText(resBundle.getString("Title"));
    lblFirstName.setText(resBundle.getString("*First Name"));
    lblMiddleName.setText(resBundle.getString("Middle Name"));
    lblLastName.setText(resBundle.getString("*Last Name"));
    lblSuffix.setText(resBundle.getString("Suffix"));
    lblFirstNameJP.setText(resBundle.getString("First Name (JP)"));
    lblLastNameJP.setText(resBundle.getString("Last Name (JP)"));
    lblID.setText(resBundle.getString("ID"));
    lblBirthDay.setText(resBundle.getString("Birth day"));
    lblBirthMonth.setText(resBundle.getString("Birth month"));
    lblRealBirth.setText(resBundle.getString("Real Birth Date"));
    lblAge.setText(resBundle.getString("Age"));
    txtID.setBorder(null);
    txtID.setEnabled(false);
    txtID.setEditable(false);
    this.add(txtBirthDay,      new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0
             ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 5, 5), 165, 0));
    this.add(lblBirthDay,   new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
             ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 1, 5), 22, -1));
    this.add(txtBirthMonth,      new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0
             ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 165, 0));
    this.add(lblBirthMonth,   new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
             ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 1, 5), 22, -1));
    this.add(txtBirthDate,      new GridBagConstraints(2, 6, 1, 1, 1.0, 0.0
             ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 165, 0));
    this.add(lblRealBirth,   new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
             ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 1, 5), 65, -1));
    this.add(cbxAge,    new GridBagConstraints(3, 6, 2, 1, 1.0, 0.0
             ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 5, 5), 131, 0));
    this.add(lblAge,    new GridBagConstraints(3, 5, 2, 1, 0.0, 0.0
             ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 1, 5), 22, -1));
    this.add(txtFirstNameJP,   new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 3, 5), 165, 0));
    this.add(txtLastNameJP,   new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 3, 5), 151, 0));
    this.add(txtSuffix,   new GridBagConstraints(3, 4, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 3, 5), 131, 0));
    this.add(lblFirstNameJP,     new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 2, 3, 5), 90, -1));
    this.add(lblLastNameJP,    new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 0, 3, 5), 76, -1));
    this.add(lblSuffix,    new GridBagConstraints(3, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 38), 79, -1));
    this.add(txtMidName,   new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 151, 0));
    this.add(txtFirstName,   new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 133, 0));
    this.add(txtTitle,   new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 5), 18, 0));
    this.add(lblTitle,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 2, 3, 5), 6, -2));
    this.add(lblFirstName,      new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 0, 3, 5), 78, -2));
    this.add(lblMiddleName,     new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 0, 3, 34), 53, -2));
    this.add(txtLastName,  new GridBagConstraints(3, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 137, 0));
    this.add(lblLastName,     new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 0, 0), 82, -2));
    this.add(lblID, new GridBagConstraints(3, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-5, 115, 0, 0), 7, 0));
    this.add(txtID, new GridBagConstraints(4, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(-5, 0, 0, 0), 15, 5));
    /*
    // Set weights of all columns and rows to 1
    double[][] weights = gridBagLayout1.getLayoutWeights();
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < weights[i].length; j++) {
        weights[i][j] = 1;
      }
    }
    gridBagLayout1.columnWeights = weights[0];
    gridBagLayout1.rowWeights = weights[1];
    */
    /**
     * TextFilters Implemented
     */
    //text lengths are limited by corresponding size in ARM_STG_CUST_OUT
    txtTitle.setDocument(new TextFilter(NAME_SPEC, 10));
    // MSB 02/10/2006
    // To resolve Double byte issues
    // we need to make sure text isn't filtered
    // and only length restriction is imposed on input.
    txtSuffix.setDocument(new CMSTextFilter(12));
    txtMidName.setDocument(new TextFilter(NAME_SPEC, 15));
    txtBirthDate.setDocument(new CMSTextFilter(10));
    txtBirthDay.setDocument(new TextFilter(TextFilter.NUMERIC, 2));
    txtBirthMonth.setDocument(new TextFilter(TextFilter.NUMERIC, 2));
    txtLastName.setDocument(new CMSTextFilter(30));
    txtFirstName.setDocument(new CMSTextFilter(30));
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

