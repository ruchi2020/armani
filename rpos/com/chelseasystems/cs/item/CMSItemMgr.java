/**
 * This class is used for creating dynamic buttons for Dolci Candy.
 * @author vivek.sawant
*/
package com.chelseasystems.cs.item;


import java.util.HashMap;
import java.util.StringTokenizer;
import com.chelseasystems.cr.config.ConfigMgr;
/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+
| 1    | 05-02-2012 | Deepika   | Bug 8443  | Modified for adding New Dolci POS Button           |               |
+------+------------+-----------+-----------+----------------------------------------------------+
*/
public class CMSItemMgr {
	
	  private static String candyKeys[] = null;
	  private static String pralinesKeys[] , drageesKeys[] , caraqueKeys[], hotChocoKeys[], fruitJellyKeys[],
	  JarsKeys[],TeaKeys[],MarronGlaceeKeys[],ShortbrdBiscKeys[],OrangePeelsKeys[],HolidayKeys[],CorpGiftKeys[]= null;
	  private static String CombinedKeys[] = null;	
	  private static HashMap itemValues  = null;
	  private static ConfigMgr config = new ConfigMgr("item.cfg");


	  /**
	   * This static block read the item.cfg file and populate the array of
	   * available Dolcy candies
	  */
	  static {
		try {
	      String dolciCandy = config.getString("DOLCI_CANDY_KEYS");
	      StringTokenizer stk = new StringTokenizer(dolciCandy, ",");
	      candyKeys = new String[stk.countTokens()];
	       while(stk.hasMoreElements()){
		       for(int i=0; i <candyKeys.length;i++){
		    	   candyKeys[i] =(String)stk.nextElement();
			    }
	        }
          } catch (Exception e) {
	      System.out.println("\t\t*** Exception in CMSItemMgr static initializer: " + e);
	    }
	  }
	  
	  //this is used to get numbers of candy names in Dolci
	  public static String[] getCandyNames() {
			  return candyKeys;
		  }
	  
	
	  /**@
	   * This will return number of box Keys for PARALINES 
	   * @return String []
	   * */
	  public static String[] getPralineBoxKeys(){
		  String boxKey = config.getString("PRALINES_BOXES_KEYS");
	      StringTokenizer stks = new StringTokenizer(boxKey, ",");
	      pralinesKeys = new String[stks.countTokens()];
	      while(stks.hasMoreElements()){
			   for(int i=0; i <pralinesKeys.length;i++){
				   pralinesKeys[i] =(String)stks.nextElement();				  
			   }
		   }  
	    
		  return pralinesKeys;
	  }
	
	  public static String[] getCaraqueKeys(){
		 String boxCaraqueKey = config.getString("CARAQUE_BOXES_KEYS");
	      StringTokenizer tokens = new StringTokenizer(boxCaraqueKey, ",");
	      caraqueKeys = new String[tokens.countTokens()];
	      while(tokens.hasMoreElements()){
			   for(int i=0; i <caraqueKeys.length;i++){
				   caraqueKeys[i] =(String)tokens.nextElement();
				}
		   } 
	      return caraqueKeys;
		  }
	
	 public static String[] getDrageesBoxKeys(){
		 String boxDrageesKey = config.getString("DRAGEES_BOXES_KEYS");
	      StringTokenizer token = new StringTokenizer(boxDrageesKey, ",");
	      drageesKeys = new String[token.countTokens()];
	      while(token.hasMoreElements()){
			   for(int i=0; i <drageesKeys.length;i++){
				   drageesKeys[i] =(String)token.nextElement();
			   }
		   } 
	    
		  return drageesKeys;
	 }
	 //Added by Satin for HOT_CHOCO Dolci buttons(OCT-18-2012)
	 public static String[] getHotChocoBoxKeys(){
		 String boxHotChocoKey = config.getString("HOT_CHOCO_BOXES_KEYS");
	      StringTokenizer token = new StringTokenizer(boxHotChocoKey, ",");
	      hotChocoKeys = new String[token.countTokens()];
	      while(token.hasMoreElements()){
			   for(int i=0; i <hotChocoKeys.length;i++){
				   hotChocoKeys[i] =(String)token.nextElement();
			   }
		   } 
	    
		  return hotChocoKeys;
	 }
	 public static String[] getFruitJellyBoxKeys(){
		 String boxFruitJellyKey = config.getString("FRUIT_JELLY_BOXES_KEYS");
	      StringTokenizer token = new StringTokenizer(boxFruitJellyKey, ",");
	      fruitJellyKeys = new String[token.countTokens()];
	      while(token.hasMoreElements()){
			   for(int i=0; i <fruitJellyKeys.length;i++){
				   fruitJellyKeys[i] =(String)token.nextElement();
			   }
		   } 
	    
		  return fruitJellyKeys;
	 }
	 
	 //start code for adding new dolci items :deepika
	 
	 public static String[] getJarsBoxKeys(){
		 String boxJarsKey = config.getString("JARS_BOXES_KEYS");
	      StringTokenizer token = new StringTokenizer(boxJarsKey, ",");
	   JarsKeys = new String[token.countTokens()];
	      while(token.hasMoreElements()){
			   for(int i=0; i <JarsKeys.length;i++){
				   JarsKeys[i] =(String)token.nextElement();
			   }
		   } 
	    
		  return JarsKeys;
	 }
	 public static String[] getTeaBoxKeys(){
		 String boxTeaKey = config.getString("TEA_BOXES_KEYS");
	      StringTokenizer token = new StringTokenizer(boxTeaKey, ",");
	      TeaKeys = new String[token.countTokens()];
	      while(token.hasMoreElements()){
			   for(int i=0; i <TeaKeys.length;i++){
				   TeaKeys[i] =(String)token.nextElement();
			   }
		   } 
	    
		  return TeaKeys;
	 }
	 public static String[] getMarronGlaceeBoxKeys(){
		 String boxMarronGlaceeKey = config.getString("MARRON_GLACEE_BOXES_KEYS");
	      StringTokenizer token = new StringTokenizer(boxMarronGlaceeKey, ",");
	      MarronGlaceeKeys = new String[token.countTokens()];
	      while(token.hasMoreElements()){
			   for(int i=0; i <MarronGlaceeKeys.length;i++){
				   MarronGlaceeKeys[i] =(String)token.nextElement();
			   }
		   } 
	    
		  return MarronGlaceeKeys;
	 }
	 public static String[] getShortbrdBiscBoxKeys(){
		 String boxShortbrdBiscKey = config.getString("SHORTBRD_BISC_BOXES_KEYS");
	      StringTokenizer token = new StringTokenizer(boxShortbrdBiscKey, ",");
	      ShortbrdBiscKeys = new String[token.countTokens()];
	      while(token.hasMoreElements()){
			   for(int i=0; i <ShortbrdBiscKeys.length;i++){
				   ShortbrdBiscKeys[i] =(String)token.nextElement();
			   }
		   } 
	    
		  return ShortbrdBiscKeys;
	 }
	 public static String[] getOrangePeelsBoxKeys(){
		 String boxOrangePeelsKey = config.getString("ORANGE_PEELS_BOXES_KEYS");
	      StringTokenizer token = new StringTokenizer(boxOrangePeelsKey, ",");
	      OrangePeelsKeys = new String[token.countTokens()];
	      while(token.hasMoreElements()){
			   for(int i=0; i <OrangePeelsKeys.length;i++){
				   OrangePeelsKeys[i] =(String)token.nextElement();
			   }
		   } 
	    
		  return OrangePeelsKeys;
	 }
	 public static String[] getHolidayBoxKeys(){
		 String boxHolidayKey = config.getString("HOLIDAY_BOXES_KEYS");
	      StringTokenizer token = new StringTokenizer(boxHolidayKey, ",");
	      HolidayKeys = new String[token.countTokens()];
	      while(token.hasMoreElements()){
			   for(int i=0; i <HolidayKeys.length;i++){
				   HolidayKeys[i] =(String)token.nextElement();
			   }
		   } 
	    
		  return HolidayKeys;
	 }
	 
	//Code added by vivek mishra on 26-SEP-2012
	 
	 public static String[] getCorpGiftBoxKeys(){
		 String boxHolidayKey = config.getString("CORP_GIFT_BOXES_KEYS");
	      StringTokenizer token = new StringTokenizer(boxHolidayKey, ",");
	      CorpGiftKeys = new String[token.countTokens()];
	      while(token.hasMoreElements()){
			   for(int i=0; i <CorpGiftKeys.length;i++){
				   CorpGiftKeys[i] =(String)token.nextElement();
			   }
		   } 
	    
		  return CorpGiftKeys;
	 }
	 
	//Code ended vivek mishra
	 
	 //end code for adding new dolci items :deepika
}
