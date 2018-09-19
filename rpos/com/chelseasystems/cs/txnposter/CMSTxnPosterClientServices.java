/*
 * @copyright (c) 2002 Chelsea Market Systems
 */

package  com.chelseasystems.cs.txnposter;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.transaction.ITransaction;
import  com.chelseasystems.cr.transaction.CommonTransaction;
import  com.chelseasystems.cr.txnposter.*;

import  com.chelseasystems.cs.register.CMSRegister;

/**
 * Client-side object for retrieving and submitting.
 */
public class CMSTxnPosterClientServices extends ClientServices
{

    /** Configuration manager **/
 //   private ConfigMgr config = null;

    /**
     * Set the current implementation
     */
    public CMSTxnPosterClientServices()
    {
        // Set up the configuration manager.
        config = new ConfigMgr("txnposter.cfg");
    }

    /**
     * initialize primary implementation
     */
    public void init(boolean online) throws Exception {
        // Set up the proper implementation of the service.
        if(online)
            onLineMode();
        else
            offLineMode();
    }

    public void onLineMode()
    {
        LoggingServices.getCurrent().logMsg("On-Line Mode for CMSTxnPosterClientServices");
        TxnPosterServices serviceImpl = (TxnPosterServices) config.getObject("CLIENT_IMPL");
        if(null == serviceImpl)
        {
            LoggingServices.getCurrent().logMsg("CMSTxnPosterClientServices", "onLineMode()",
                                                "Cannot instantiate the class that provides the"
                                                + "implementation of TxnPosterServices in txnposter.cfg.",
                                                "Make sure that txnposter.cfg contains an entry with "
                                                +"a key of CLIENT_IMPL and a value that is the name of a class "
                                                +"that provides a concrete implementation of TxnPosterServices.",
                                                LoggingServices.MAJOR);
            setOffLineMode();
            return;
        }
        TxnPosterServices.setCurrent(serviceImpl);
    }

    public void offLineMode()
    {
        LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSTxnPosterClientServices");
        TxnPosterServices serviceImpl = (TxnPosterServices) config.getObject("CLIENT_DOWNTIME");
        if(null == serviceImpl)
        {
            LoggingServices.getCurrent().logMsg("CMSTxnPosterClientServices", "offLineMode()",
                                                "Cannot instantiate the class that provides the"
                                                + " implementation of TxnPosterServices in txnposter.cfg.",
                                                "Make sure that txnposter.cfg contains an entry with "
                                                + "a key of CLIENT_DOWNTIME and a value"
                                                + " that is the name of a class that provides a concrete"
                                                + " implementation of TxnPosterServices.",
                                                LoggingServices.CRITICAL);
        }
        TxnPosterServices.setCurrent(serviceImpl);
    }

   public Object getCurrentService () {
      return  TxnPosterServices.getCurrent();
   }

    /**
     * post a transaction
     * @param aTxn aTxn
     */
    public boolean post (ITransaction aTxn) throws Exception {
        try
        {
            this.fireWorkInProgressEvent(true);

            if(aTxn instanceof CommonTransaction)
            {
                if(((CommonTransaction)aTxn).getRegisterId() != null ||
                   ((CommonTransaction)aTxn).getRegisterId().length() == 0)
                {
                    Object regId = getBrowserMgr().getGlobalObject("REGISTER");

                    if(regId != null)
                    {
                        ((CommonTransaction)aTxn).setRegisterId(((CMSRegister)regId).getId());
                    }
                }
            }
            
            return(boolean) TxnPosterServices.getCurrent().post(aTxn);
         }
        catch(DowntimeException ex)
        {
            LoggingServices.getCurrent().logMsg(getClass().getName(),"post",
                                                "Primary Implementation for TxnPosterServices failed, going Off-Line...",
                                                "See Exception", LoggingServices.MAJOR, ex);
            System.out.println("Just before starting the posting agent thread  :"+System.currentTimeMillis());
            offLineMode();
            setOffLineMode();
            return(boolean) TxnPosterServices.getCurrent().post(aTxn);
        }
        
        finally
        {
            TxnPosterServices.fireTxnPostedEvent(new TxnPostedEvent(this, aTxn));
            System.out.println("Just before starting the posting agent thread  :"+System.currentTimeMillis());
            this.fireWorkInProgressEvent(false);
        }
       
    }

    /**    */
    public boolean repostBrokenTransactions () throws Exception {
        try
        {
            this.fireWorkInProgressEvent(true);
            return(boolean) TxnPosterServices.getCurrent().repostBrokenTransactions();
        }
        catch(DowntimeException ex)
        {
            LoggingServices.getCurrent().logMsg(getClass().getName(),"repostBrokenTransactions",
                                                "Primary Implementation for TxnPosterServices failed, going Off-Line...",
                                                "See Exception", LoggingServices.MAJOR, ex);
            offLineMode();
            setOffLineMode();
            return(boolean) TxnPosterServices.getCurrent().repostBrokenTransactions();
        }
        finally
        {
            this.fireWorkInProgressEvent(false);
        }
    }

}
