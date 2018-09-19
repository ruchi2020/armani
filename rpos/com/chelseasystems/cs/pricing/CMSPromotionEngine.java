//
// Copyright 2003, Retek Inc
//
package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.pos.CMSSaleLineItem;

import java.util.*;

public abstract class CMSPromotionEngine extends Object implements IPromotionEngine, java.io.Serializable {

  private static final long serialVersionUID = 152722854576962539L;
  private static ConfigMgr configMgr = new ConfigMgr("promotion.cfg");
  
  protected boolean isOnPromotionTimeRange(CMSPromotion promotion) {
    Calendar currentTime = Calendar.getInstance();
    Calendar beginTime = promotion.getBeginTime();
    Calendar endTime = promotion.getEndTime();
    if (beginTime.before(currentTime) && endTime.after(currentTime))
      return true;
    else
      return false;
  }

  protected boolean isWithinPresaleStartDays(CMSPromotion promotion) {
      String sDays = configMgr.getString("ACTIVE_PROMOTION_BEGIN_DAYS");
      int days = 0;
      try {
        days = Integer.parseInt(sDays);
      }catch (NumberFormatException ex){
        ex.printStackTrace();
      }
      Calendar currentTime = Calendar.getInstance();
      Calendar validTime = Calendar.getInstance();
      validTime.add(Calendar.DATE,days);
      Calendar beginTime = promotion.getBeginTime();
      Calendar endTime = promotion.getEndTime();
      return beginTime.before(validTime) && endTime.after(currentTime);
  }
  
  //
  //utility methods begin here
  //

  /*public static POSLineItemDetail[] removeNotAvailableForPromtion(POSLineItemDetail[] details) {
    List list = new ArrayList();
    for (int i = 0; i < details.length; i++)
      if (details[i].isAvailableForDeal()) list.add(details[i]);
    return (POSLineItemDetail[])list.toArray(new POSLineItemDetail[0]);
  }
*/
  	//Poonam:Added code to handle multiple promotions on an item
  	public static POSLineItemDetail[] removeNotAvailableForPromtion(POSLineItemDetail[] details) {
	    List list = new ArrayList();
	    POSLineItem lineItem; 
	    
	    // Added only those line items that are applicable for best deal Promotion Id itemwise - Himani
	    List<String> s = CMSPromotionBasedPriceEngine.getLineItem(); 
	    POSLineItemDetail[] lineDetails = CMSPromotionBasedPriceEngine.getLineItemArray();
	    // Null check added to bypass when single promotion is applied to item.
	    if(s!=null && !s.isEmpty())
	    {
	    	
	    	for (int i = 0; i < lineDetails.length; i++){
	    		lineItem=lineDetails[i].getLineItem();
	    		if (lineItem.isApplicableForPromotion()) list.add(lineDetails[i]);
	    	}
	    }
	    else
	    {
	    	for (int i = 0; i < details.length; i++)
	    	      if (details[i].isAvailableForDeal()) list.add(details[i]);
	    }
		
	    return (POSLineItemDetail[])list.toArray(new POSLineItemDetail[0]);
	  }
  
  private static DetailAmountComparator detailAmountComparator = new DetailAmountComparator();
  public static POSLineItemDetail[] sortByAmount(POSLineItemDetail[] details) {
    List list = Arrays.asList(details);
    Collections.sort(list, detailAmountComparator);
    return (POSLineItemDetail[])list.toArray(new POSLineItemDetail[0]);
  }

  private static class DetailAmountComparator implements Comparator {
    public int compare(Object obj1, Object obj2) {
      double amount1 = ((POSLineItemDetail)obj1).getLineItem().getItem().getRetailPrice().doubleValue();
      double amount2 = ((POSLineItemDetail)obj2).getLineItem().getItem().getRetailPrice().doubleValue();
      if (amount1 > amount2) return -1;
      if (amount1 < amount2) return 1;
      return 0;
    }
  }

  public static ArmCurrency[] modifyLastEntryToMakeTotalUp(ArmCurrency[] amounts, ArmCurrency shouldBeTotal) throws CurrencyException {
    if (amounts == null || amounts.length == 0) return new ArmCurrency[0];
    ArmCurrency totalWithoutLastEntry = new ArmCurrency(0.0);
    for (int i = 0; i < amounts.length - 1; i++) totalWithoutLastEntry = totalWithoutLastEntry.add(amounts[i]);
    amounts[amounts.length - 1] = shouldBeTotal.subtract(totalWithoutLastEntry);
    return amounts;
  }

}
