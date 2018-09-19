package com.chelseasystems.cs.item;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.item.*;


public abstract class ItemServices {

//}
//
//
// package com.chelseasystems.cr.item;
// 
// import com.chelseasystems.cr.currency.ArmCurrency;
// import com.chelseasystems.cr.store.Store;
// 
// public abstract class ItemServices
// {
   private static ItemServices current;
 
   public static void setCurrent(ItemServices aItemService)
   {
     current = aItemService;
   }
 
   public static ItemServices getCurrent()
   {
     return current;
   }
 
   public abstract Item findById(String paramString)
     throws Exception;
 
   public abstract String[] findIDListByDescription(String paramString)
     throws Exception;
 
   public abstract Item[] findAllItems()
     throws Exception;
 
   public abstract Item[] findAllItemsForStore(Store paramStore)
     throws Exception;
 
   public abstract boolean updateItemPrice(String paramString, ArmCurrency paramArmCurrency)
     throws Exception;
   
   //Added by Satin to search an itemId corresponding to entered barcode for exception item.
   public abstract String selectItemIdFromAsItm(String barcode)
   	 throws Exception;
   
   //Added by Satin to select an item corresponding to entered itemId for exception item.
   public abstract String selectItemFromAsItm(String itemId)
		   	 throws Exception;

   // Added by Satin to insert a record into AS_ITM_RTL_STR for exception item.
   public abstract void insertIntoAsItmRtlStr(String storeId, String itemId, ArmCurrency retailPrice, String currencyCode, String itemDesc, Double vatRate, String taxable)
			throws Exception;


}

/* Location:           C:\armani\armani\retek\library\retek_common.jar
 * Qualified Name:     com.chelseasystems.cr.item.ItemServices
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.5.3
 */