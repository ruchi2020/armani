package com.chelseasystems.cs.swing.builder;



import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.payment.Amex;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.DebitCard;
import com.chelseasystems.cr.payment.IPaymentConstants;
import com.chelseasystems.cr.payment.MasterCard;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.payment.Visa;
import com.chelseasystems.cs.payment.CMSIPaymentConstants;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.payment.JCBCreditCard;
import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.config.ConfigMgr;

public class ValidateCardType extends CreditCardBldrUtil{

	/**
	   *added by vishal to get Redeemable obj for payment type
	   * @param accountNum
	   * @return
	   */
	public static Payment allocGiftCardPayment(String gfAccNo,IApplicationManager theAppMgr){
		try{
		if(gfAccNo==null && gfAccNo.length()!=16){
			return null;
		}
		Redeemable received = CMSRedeemableHelper.findRedeemable(theAppMgr, gfAccNo);

		return received;
		}catch(Exception e){
			  e.printStackTrace();
			  return null;
		}
	}
	  public static Payment allocCreditCardPayment(String firstSixOrEight, String lastFour , String payDesc) {
		   String accountNum = firstSixOrEight + lastFour;
		  if (accountNum == null || accountNum.length() < 5) {
	      return null;
		  }
	  
	    /*
	     if (this.creditOrDebit != null && this.creditOrDebit.equals("D")) {
	     return new DebitCard();
	     }
	     */
	    /*if (!CreditCardBldrUtil.validateCheckDigit(accountNum))
	      return null;*/
	      // bug # 29959
	      /*
	      disable the internal bin check utility so
			that, if cashier chooses AMEX button it will automatically use AMEX as the card
			type even if the bin entered manually was a Visa bin range
	      */
	    /*int code = new Integer(accountNum.substring(0, 2)).intValue();
	    int code4 = new Integer(accountNum.substring(0, 4)).intValue();
	    // american express
	    if ((code == 34) || (code == 37)) {
	    	accountNum = firstSixOrEight + "*****" + lastFour;
	      Amex amx = new Amex(IPaymentConstants.AMERICAN_EXPRESS);
	      amx.setAccountNumber(accountNum);
	      amx.setMaskCardNum(accountNum);
	      return amx;
	    }*/
	    //changes ends for bug #29959
	    
	    /* Bug # 92 Diners Card:  Payment/Credit Card: Diners Card account numbers are not accepted
	     */
	     // bug # 29959
	    /* if (((code >= 51) && (code <= 55)) || (code == 36)) {
	      // code 36 with account number length 16 represents Master Cards and
	      // code 36 with account number length 14 represents Diners Master Card.
	      // If the code is 36 check to see if the account number length is either
	      // 16 or 14. If not, return null.

	      //  code 36 with account number length 14 represents JCB
	      /*if(code == 36){
	    	  JCBCreditCard jcb = new JCBCreditCard(CMSIPaymentConstants.JCB);
	    	  accountNum = firstSixOrEight + "****" + lastFour;
	    	  jcb.setAccountNumber(accountNum);
	    	  jcb.setMaskCardNum(accountNum);
	    	  return jcb;
	      }
	      
	      else{
	      MasterCard mc = new MasterCard(IPaymentConstants.BANK_CARD);
	      accountNum = firstSixOrEight + "******" + lastFour;
	      mc.setAccountNumber(accountNum);
	      mc.setMaskCardNum(accountNum);
	      return mc;
	      }
	    }
	    if ((code >= 40) && (code <= 49)) {
	      Visa visa = new Visa(IPaymentConstants.BANK_CARD);
	      accountNum = firstSixOrEight + "******" + lastFour;
	      visa.setAccountNumber(accountNum);
	      visa.setMaskCardNum(accountNum);
	      return visa;
	    }
	    // For Bug #93
	    // Discover card not supported.( Payment/Credit Card: Discover account not supported)
	    //Issue # 1849 US wants to get support for Discover card.
	       /*if (code4 == 6011)  {
	             if (accountNum.length() != 16) {
	                return  null;
	             }
	             Discover dis = new Discover(IPaymentConstants.DISCOVER);
	             return  dis;
	         }
	          */
	          //changes ends for bug #29959
	    // For JCB Credit Card.
	    //Issue # 1849 add the Discover bin ranges into JCB.  So if a Discover card is used, it will be the same as if a JCB card was used.
	    // changes for bug #29959
	    /*int codeJCB = new Integer(accountNum.substring(0, 4)).intValue();
	    int codeCUP = new Integer(accountNum.substring(0, 8)).intValue();
	    boolean IsJCBObj = false;
	    // for bug#91 : JCB credit-card account numbers are not accepted changed code to codeJCB
	    // 1849: updated bin ranges.
	    //added new BIN Ranges to support discover card
	    if (((codeJCB >= 3000) && (codeJCB <= 3059))
	    		||(codeJCB == 3095)
	    		||((codeJCB >= 3528) && (codeJCB <= 3589))
	    		||((codeJCB >= 3600) && (codeJCB <= 3699))
	    		||((codeJCB >= 3800) && (codeJCB <= 3999))
	    		||(codeJCB == 6011)
//	    		||((codeJCB == 6500)&& (codeJCB ==6509))
	    		||((codeCUP >= 62212600)&& (codeCUP <= 62292599))
	    		||((codeJCB >= 6240) && (codeJCB <= 6269))
	    		||((codeJCB >= 6282) && (codeJCB <= 6288))
	    		||((codeJCB >= 6440) && (codeJCB <= 6599))
	    		||((codeCUP >= 65000000)&& (codeCUP <= 65099999))) {
	      IsJCBObj = true;
	      if ((IsJCBObj == true)) {
	        JCBCreditCard jcb = new JCBCreditCard(CMSIPaymentConstants.JCB);
	        //Vivek Mishra : Added for CUP card.
	        if(firstSixOrEight.length()==8)
	        	accountNum = firstSixOrEight + "****" + lastFour;
	        else
	        accountNum = firstSixOrEight + "******" + lastFour;
	        jcb.setAccountNumber(accountNum);
	        jcb.setMaskCardNum(accountNum);
	        return jcb;
	      }
	    }*/
	    //changes ends for bug #29959
	    // For North American Diners Club CreditCard
	    // i.	To extend MasterCard bin range to include International Diners Club
	    //1.	Identifier - 36.
	    //ii.	To add bin range calculations for North American Diners Club credit card.
	    //1.	Identifier  300  305
	    //2.	 380  388.
	    // for internal bug : Diners credit-card account numbers are not accepted changed code to codeJCBDiners
	    //int codeJCBDiners = new Integer(accountNum.substring(0, 3)).intValue();
	    //boolean IsDinersObj = false;
	    
	    
	    
	    //Comment following code as the new BIN RANGES added for JCB and DINERS CREDIT CARD.
		    /*if ((codeJCBDiners >= 300) && (codeJCBDiners <= 305)
		        || ((codeJCBDiners >= 380) && (codeJCBDiners <= 389))) {
		            IsDinersObj = true;
		            if ((IsDinersObj == true) && (accountNum.length() == 14)) {
		            	Diners diner = new Diners(CMSIPaymentConstants.diner);
		            	return diner;
	                }
	  
		        
		    */
	    	/*int jcbNewCode = new Integer(accountNum.substring(0,3)).intValue();
	       	if((jcbNewCode>=360)&&(jcbNewCode<=369))
	       	 //((jcbNewCode>=300) && (jcbNewCode<=305)
	    		//||((jcbNewCode>=309)&&(jcbNewCode<=309))	
	    		//||((jcbNewCode>=360)&&(jcbNewCode<=369))
	    		//||((jcbNewCode>=380)&&(jcbNewCode<=399))    		
	    	{
	    		IsDinersObj = true; 
	            if ((IsDinersObj == true)) {
	    		 JCBCreditCard jcb = new JCBCreditCard(CMSIPaymentConstants.JCB);
	    		 accountNum = firstSixOrEight + "****" + lastFour;
	 	        jcb.setAccountNumber(accountNum);
	 	        jcb.setMaskCardNum(accountNum);
	    	    return jcb;
	    	}
	    
	       	}else*/
	       		//If none of the bin ranges are satisfied the card is added as CREDIT CARD .. 
	     // return null;
	    //     if (code == 50) {
	    //        if (accountNum.length() != 11) {
	    //           theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
	    //           return null;
	    //        }
	    //        TMW tmw = new TMW();
	    //        return tmw;
	    //     }
	       		// For international cards and Canadian Cards no bin look up is needed or if above bin range is not met, all cards are added based on card type 
	       		//cashier selects from mobile terminal pop up
	       		
	       	if(payDesc.equalsIgnoreCase("VISA")){
	       		Visa visa = new Visa(IPaymentConstants.BANK_CARD);
	   	      accountNum = firstSixOrEight + "******" + lastFour;
	   	      //Issue #29959
	   	      Pattern sixteendigitpattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\*\\*\\*\\*\\*\\*\\d\\d\\d\\d");	   
			  if(!sixteendigitpattern.matcher(accountNum).matches()){
			    	return null;	
			     }else{
			     visa.setAccountNumber(accountNum);
	   	      	 visa.setMaskCardNum(accountNum);
	   	      	 ((Payment)visa).setPaymentCode("149");
	   	      return visa;
		    }
	       	}
	       	
	       	if(payDesc.equalsIgnoreCase("MASTER_CARD")){
	       	  MasterCard mc = new MasterCard(IPaymentConstants.BANK_CARD);
	       	  accountNum = firstSixOrEight + "******" + lastFour;
	       	  //Issue #29959
	       	  Pattern sixteendigitpattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\*\\*\\*\\*\\*\\*\\d\\d\\d\\d");
		      if(!sixteendigitpattern.matcher(accountNum).matches()){
			    	return null;	
			     }else{
		      mc.setAccountNumber(accountNum);
		      mc.setMaskCardNum(accountNum);
		      return mc;
	       		}
	       	}
	       	if(payDesc.equalsIgnoreCase("AMEX")){
	       		Amex amx = new Amex(IPaymentConstants.AMERICAN_EXPRESS);	
	       	  accountNum = firstSixOrEight + "*****" + lastFour;
	       	  //Issue #29959
	       	Pattern sixteendigitpattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\*\\*\\*\\*\\*\\d\\d\\d\\d");
	  	    if(!sixteendigitpattern.matcher(accountNum).matches()){
		    	return null;	
		     }else{
	  	      amx.setAccountNumber(accountNum);
	  	      amx.setMaskCardNum(accountNum);
	  	      return amx;
		       }
	       	}
	       if(payDesc.equalsIgnoreCase("JCB") || payDesc.equalsIgnoreCase("DINERS") ||payDesc.equalsIgnoreCase("DISCOVER") ||payDesc.equalsIgnoreCase("CUP")){
	       	JCBCreditCard jcb = new JCBCreditCard(CMSIPaymentConstants.JCB);
    		accountNum = firstSixOrEight + "****" + lastFour;
    		 //Issue #29959
    		Pattern sixteendigitpattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\*\\*\\*\\*\\d\\d\\d\\d");
    		Pattern eigteendigitpattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d\\*\\*\\*\\*\\d\\d\\d\\d");
    		if((accountNum.length()>= 16)&&(!eigteendigitpattern.matcher(accountNum).matches())){
    			return null;
    		}else if ((accountNum.length()<16)&&(!sixteendigitpattern.matcher(accountNum).matches())){
    			return null;
    		}else{
     	        jcb.setAccountNumber(accountNum);
     	        jcb.setMaskCardNum(accountNum);
        	    return jcb;
    		}
	       }
	       	
	    	if(payDesc.equalsIgnoreCase("DEBIT")){
	    		DebitCard debit = new DebitCard(IPaymentConstants.DEBIT_CARD);
	    		accountNum = firstSixOrEight + "*****" + lastFour;
	    		 //Issue #29959
	    		Pattern sixteendigitpattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\*\\*\\*\\*\\*\\d\\d\\d\\d");
	    		if(!sixteendigitpattern.matcher(accountNum).matches()){
			    	return null;	
			     }else{
	    		debit.setAccountNumber(accountNum);
	    		debit.setMaskCardNum(accountNum);
	    		((CreditCard)debit).setTenderType("Debit");
	    		((CreditCard)debit).setPaymentCode("145");
	    		 return debit;
	    	}
	    	}	
	       	    return null;
	  }
}
