package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;


/** 
 * This class has been added for PCR 1864
 * VAT Exemption Transactions.
 * The application must force the operator to associate a fiscal document on it.
 * After the ticket printout all the buttons  pressed by the operator except “Fiscal Document” will produce a warning window  with “Mandatory add a Fiscal Document on this transaction”.
 * 
 * 
 */

public class MandatoryAddFiscalDocument extends Rule{
	
	

	private static final long serialVersionUID = 1L;
	java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
	//Default Constructor
	public MandatoryAddFiscalDocument(){
		
	}
	
	/**
	   * Execute the rule.
	   *
	   * @param object theParent - instance of CMSMenuOption
	   * @param args - instance of CMSCompositeTransaction
	   * @param args - instance of Store
	   * @return RulesInfo
	   */
	  public RulesInfo execute(Object theParent,Object[] args) {
	    return execute((CMSCompositePOSTransaction)theParent);
	  }

	  /**
	   * Execute the Rule
	   * @param parent Object
	   * @return RulesInfo
	   */
	  private RulesInfo execute(Object parent) {
		   try {
	    	 if(parent instanceof CMSCompositePOSTransaction) {
				  CMSCompositePOSTransaction compTxn = (CMSCompositePOSTransaction)parent;
				   if(compTxn.getCurrFiscalDocument()== null){
					   return new RulesInfo(res.getString("Mandatory Add a Fiscal Document on this Transaction"));
				   }	
			}	      
		   	} catch (Exception ex) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
	          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
	    }
	    return new RulesInfo();
	  }

	  /**
	   * Returns the description of the business rule.
	   * @returns description of the business rule.
	   */
	  public String getDesc() {
	    return ("Add fiscal document.");
	  }

	  /**
	   * Return the name of the rule.
	   * @return name of the rule.
	   */
	  public String getName() {
	    return "MandatoryAddFiscalDocument";
	  }

}
