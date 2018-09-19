/*
  *
  * formatted with JxBeauty (c) johann.langhofer@nextra.at
  *
  *+------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 07-25-2008 | Vivek S.    | N/A       | Armani India Tax Customization                                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
  */


package  com.chelseasystems.cr.register;

/**
  * Title:        <p>
  * Description:  Display information from the sale on a light pole as it is rung up<p>
  * Copyright:    Copyright (c) 2001<p>
  * Company:      Chelsea Market Systems<p>
  * @author       Dan Reading
  * @version
  */
import  com.chelseasystems.cr.pos.*;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.currency.*;
import  com.chelseasystems.cr.payment.*;
import  com.chelseasystems.cr.util.Trace;
import com.chelseasystems.cs.util.Version;

import  java.util.*;
import  java.text.*;
import  jpos.*;
import  jpos.events.*;


/**
  *
  */
public class LightPoleDisplay
{
     private static ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
     static LightPoleDisplay lightPoleDisplay; 
                  // this class
     static boolean pole_on = true;
     static DisplayNotInUseThread timer;
     private LineDisplay lineDisplay; // the class that maps to the JPOS device
     boolean stopped = false;
     String logicalName;
     SimpleDateFormat formatter = new 
     SimpleDateFormat(res.getString("MM/dd/yy   hh:mm a"));
     ThreadSuspendedGate suspendGate;
     String idleMessage;


     /**
      *
      * @return
      */
     public static LightPoleDisplay getInstance ()
     {
         if(lightPoleDisplay == null)
             lightPoleDisplay = new LightPoleDisplay();
         return(lightPoleDisplay);
     }


     /**
      *
      */
     private LightPoleDisplay ()
     {
         suspendGate = new ThreadSuspendedGate();
         ConfigMgr cm = new ConfigMgr("JPOS_peripherals.cfg");
         pole_on = cm.getString("POLE_ON").equals("YES");
         if(!pole_on)
         {
             Trace.out("Customer Pole Display is turned OFF in config.");
             return;
         }
         Trace.out("Customer Pole Display is turned ON in config.");
         logicalName = cm.getString("POLE_LOGICAL_NAME");
         idleMessage = cm.getString("POLE_IDLE_MESSAGE");
         lineDisplay = new LineDisplay();
         try
         {
             Trace.out("opening line display with logical name: " + logicalName);
             lineDisplay.open(logicalName);

             //2007-12-05 Alex start: enabled ONLY when useful
             lineDisplay.claim(1000); //da commentare
             lineDisplay.setDeviceEnabled(true);//da commentare
             //2007-12-05 Alex end: enabled ONLY when useful

             
             timer = new DisplayNotInUseThread();
         }
         catch(JposException je)
         {
             pole_on = false;
             Trace.out("error on open light pole" + je);
             Trace.ex(je);
         }
     }

     /**
      *
      */
     public void startDefaultDisplay ()
     {
         if(pole_on)
         {
             synchronized(suspendGate)
             {
                 suspendGate.isThreadSuspended(false);
                 suspendGate.notify();
             }
             if(!timer.isAlive())
                 timer.start();
             timer.interrupt();  // eg wake up right now if sleeping
         }
     }

     /**
      *
      */
     public void stopDefaultDisplay ()
     {
         if(pole_on)
         {
             synchronized(suspendGate)
             {
                 suspendGate.isThreadSuspended(true);
             }
         }
     }


     /**
      *
      * @param line1
      * @param line2
      */
     public void displayMessage (String line1, String line2)
     {
         if(!pole_on)
         {
             return;
         }
         try
         {
             //2007-12-05 Alex start: enabled ONLY when useful
             //lineDisplay.claim(1000);
             //lineDisplay.setDeviceEnabled(true);
             //2007-12-05 Alex end: enabled ONLY when useful

             lineDisplay.setCurrentWindow(0);
             lineDisplay.clearText();
             //myDisplay.displayText(sizeToSpecification(false, 20, line1) + "\n" + sizeToSpecification(false, 20, line2),LineDisplayConst.DISP_DT_NORMAL);
             lineDisplay.displayTextAt(0, 0,sizeToSpecification(false, 20,line1), LineDisplayConst.DISP_DT_NORMAL);
             lineDisplay.displayTextAt(1, 0,sizeToSpecification(false, 20,line2), LineDisplayConst.DISP_DT_NORMAL);

             //2007-12-05 Alex start: enabled ONLY when useful
             //lineDisplay.setDeviceEnabled(false);
             //2007-12-05 Alex end: enabled ONLY when useful
         }
         catch(JposException je)
         {
             Trace.out("error on display text" + je);
         }
     }

     public void itemReturned (POSLineItem line, String subTotalDue)
     {
         itemDisplay(line,subTotalDue,false);
     }
     public void itemSold (POSLineItem line, String subTotalDue)
     {
         itemDisplay(line,subTotalDue,true);
     }
     /**
      *
      * @param line
      * @param sub
      * @param addItem
      */
     private void itemDisplay (POSLineItem line, String sub, boolean isSale)
     {
         if(!pole_on)
         {
             return;
         }
         //String display = line.getQuantity() + " " + line.getItem().getDescription() + "\n" + line.getExtendedRetailAmount().formattedStringValue();
         String displayLine1 = new String(" ");
                 
         if(isSale)
         {
             displayLine1 = line.getQuantity() + " " + line.getItem().getDescription();
         }
         else
         {
             displayLine1 = line.getQuantity() + " " + line.getItem().getDescription() + " -";
         }
         int len1 = line.getExtendedRetailAmount().formattedStringValue().length();
         int len2 = sub.length();
         int pad = 20 - (len1 + len2);
         String padStr = new String();
         for(int i = 0; i < pad; i++)
         {
             padStr = padStr + " ";
         }
         String displayLine2 = line.getExtendedRetailAmount().formattedStringValue() + padStr + sub;
         //begin code for dist_ind
         if("IND".equalsIgnoreCase(Version.CURRENT_REGION)){
        	 displayLine1 = new String(" ");
        	 displayLine2 = line.getTotalAmountDue().formattedStringValue();
         }
         //end code for dist_ind
         
         displayMessage(displayLine1, displayLine2);
         //myDisplay.displayText(sizeToSpecification(false, 20, displayLine1) + "\n" + sizeToSpecification(false, 20,displayLine2), LineDisplayConst.DISP_DT_NORMAL);
     }

     public void paymentDisplay(Payment payment, ArmCurrency totalNowDue)
     {
         String line1;
         String line2;
         line1 = payment.getGUIPaymentName() + " " + payment.getAmount().formattedStringValue();
         if(totalNowDue.doubleValue() <= 0)
             line2 = "Change: " + totalNowDue.multiply(-1).formattedStringValue();
         else
             line2 = "Due: " + totalNowDue.formattedStringValue();
         displayMessage(line1,line2);
     }


     /**
      *
      * @param isJustRight
      * @param specifiedSize
      * @param stringToSize
      * @return
      */
     private String sizeToSpecification (boolean isJustRight, int specifiedSize, String stringToSize)
     {
         if(isJustRight)
             if(specifiedSize < stringToSize.length())
             {
                 return(stringToSize.substring(stringToSize.length() - specifiedSize));
             }
             else
             {
                 StringBuffer sb = new StringBuffer();
                 for(int i = 0; i < specifiedSize - stringToSize.length(); i++)
                     sb.append(" ");
                 return(sb.toString() + stringToSize);
             }
         else
         {
             if(specifiedSize < stringToSize.length())
             {
                 return(stringToSize.substring(0, specifiedSize));
             }
             else
             {
                 StringBuffer sb = new StringBuffer();
                 for(int i = 0; i < specifiedSize - stringToSize.length(); i++)
                 {
                     sb.append(" ");
                 }
                 return(stringToSize + sb.toString());
             }
         }
     }

     class ThreadSuspendedGate
     {
         boolean isThreadSuspended;
         public ThreadSuspendedGate()
         {
         }
         public boolean isThreadSuspended()
         {
             return(isThreadSuspended);
         }
         public void isThreadSuspended(boolean isThreadSuspended)
         {
             this.isThreadSuspended = isThreadSuspended;
         }
     }

     class DisplayNotInUseThread extends Thread
     {
         /**
          *
          */
         public DisplayNotInUseThread ()
         {
         }
         /**
          *
          */
         public void run ()
         {
             try
             {
                 for(;;)
                 {
                     try {
                    	 synchronized(suspendGate)
                    	 {
                    		 while(suspendGate.isThreadSuspended())
                    			 suspendGate.wait();
                    	 }
                     } catch (InterruptedException e1) {
                    	 Trace.out("LightpoleDisplay Thread Interrupted: " + e1.getMessage());
                    	 e1.printStackTrace();
                     } catch (Throwable tw) {
                    	 tw.printStackTrace();
                     }
                     displayMessage(idleMessage, " " + formatter.format(new Date()));
                     try
                     {
                         Thread.sleep(59000);  // no need to wake up more than once a minute to update time
                     }
                     catch(InterruptedException e)
                     {
                     Trace.out("LightpoleDisplay Thread Interrupted while sleeping: " + e.getMessage());
                     }
                 }
             }
             catch(Exception e)
             {
                 Trace.out("error in LightpoleDisplay Thread: " + e);
             }
         }
     }

    /**
     * 2007-12-05 Alex:
     * Close and open device
     */
    public void reOpen() {
    	if(!pole_on){
    		return;
    	}
        try {
            lineDisplay.setDeviceEnabled(false);
            Thread.sleep(500);
            lineDisplay.claim(1000);
            lineDisplay.setDeviceEnabled(true);
        } catch (JposException e) {
            // TODO Blocco catch generato automaticamente
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Blocco catch generato automaticamente
            e.printStackTrace();
        }
        
    }
    //start for issue #1947 Pole Display by neeti
    public void itemSoldForDiscount (POSLineItem line, String originalAmt, String newAmt)
    {       	 
       itemDisplayForDiscount(line,originalAmt,newAmt, true);	    	 
    }
    public void itemReturnedForDiscount (POSLineItem line, String originalAmt, String newAmt)
    {       	 
       itemDisplayForDiscount(line,originalAmt,newAmt, true);	    	 
    }
    
    private void itemDisplayForDiscount (POSLineItem line, String originalAmt, String newAmt, boolean isSale)
    {    	
       if(!pole_on)
        {
            return;
        }       
        String displayLine1 = new String(" ");
                
        if(isSale)
        {
           displayLine1 = line.getQuantity() + " " + line.getItem().getDescription();         	
        }
        else
        {
           displayLine1 = line.getQuantity() + " " + line.getItem().getDescription() + " -";       	
        }
        int len1 = line.getExtendedRetailAmount().formattedStringValue().length();
        int len2 = originalAmt.length();
        int pad = 20 - (len1 + len2);
        String padStr = new String();
        for(int i = 0; i < pad; i++)
        {
            padStr = padStr + " ";           
        }				
			String displayLine2 = originalAmt+ padStr + newAmt;					
	        displayMessage(displayLine1, displayLine2);		
    
    }
    
    //End for issue #1947 Pole Display by neeti
}


