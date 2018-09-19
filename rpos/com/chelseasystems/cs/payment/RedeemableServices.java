
 package com.chelseasystems.cs.payment;
 
//import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.payment.StoreValueCard;
import com.chelseasystems.cr.payment.GiftCert;
import com.chelseasystems.cr.payment.DueBill;
// added 4 import statements above.
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
 
 
 public abstract class RedeemableServices
 {
   private static RedeemableServices current = null;
 
   public static RedeemableServices getCurrent()
   {
     return current;
   }
 
   public static void setCurrent(RedeemableServices svcs)
   {
     current = svcs;
   }
 
   public abstract void addRedeemable(Redeemable paramRedeemable)
     throws Exception;
 
   public abstract Redeemable findRedeemable(String paramString)
     throws Exception;
 
   public abstract StoreValueCard findStoreValueCard(String paramString)
     throws Exception;
 
   // Added by Satin
   // Added for coupon management PCR.
   // Finds Coupon based on Barcode and StoreId.
   public abstract CMSCoupon findByBarcodeAndStoreId(String barcode, String StoreId)
     throws Exception;
   
   public abstract GiftCert findGiftCert(String paramString)
     throws Exception;
 
   public abstract void addGiftCert(GiftCert paramGiftCert)
     throws Exception;
 
   public abstract void updateGiftCert(CompositePOSTransaction paramCompositePOSTransaction)
     throws Exception;
 
   public abstract DueBill findDueBill(String paramString)
     throws Exception;
 
   public abstract void addDueBill(DueBill paramDueBill)
     throws Exception;
 
   public abstract void updateDueBill(CompositePOSTransaction paramCompositePOSTransaction)
     throws Exception;
 
   public abstract boolean isNewGiftCertControlNumberValid(String paramString)
     throws Exception;

 }

/* Location:           C:\armani\armani\retek\library\retek_pos.jar
 * Qualified Name:     com.chelseasystems.cr.payment.RedeemableServices
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.5.3
 */