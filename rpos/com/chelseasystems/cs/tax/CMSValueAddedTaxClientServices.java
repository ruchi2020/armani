/*
 * @copyright (c) 2002 Retek
 */

package  com.chelseasystems.cs.tax;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.pos.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cr.tax.*;
import  com.chelseasystems.cs.pos.*;
import  com.chelseasystems.cs.store.*;
import  java.util.*;

/**
 * Client-side object for retrieving and submitting.
 */
public class CMSValueAddedTaxClientServices extends ClientServices
{

    /** Configuration manager **/
//    private ConfigMgr config = null;

    /**
     * Set the current implementation
     */
    public CMSValueAddedTaxClientServices()
    {
        // Set up the configuration manager.
        config = new ConfigMgr("vat.cfg");
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
        LoggingServices.getCurrent().logMsg("On-Line Mode for CMSValueAddedTaxClientServices");
        CMSValueAddedTaxServices serviceImpl = (CMSValueAddedTaxServices) config.getObject("CLIENT_IMPL");
        if(null == serviceImpl)
        {
            LoggingServices.getCurrent().logMsg("CMSValueAddedTaxClientServices", "onLineMode()",
                                                "Cannot instantiate the class that provides the"
                                                + "implementation of CMSValueAddedTaxServices in vat.cfg.",
                                                "Make sure that vat.cfg contains an entry with "
                                                +"a key of CLIENT_IMPL and a value that is the name of a class "
                                                +"that provides a concrete implementation of CMSValueAddedTaxServices.",
                                                LoggingServices.MAJOR);
            setOffLineMode();
            return;
        }
        CMSValueAddedTaxServices.setCurrent(serviceImpl);
    }

    public void offLineMode()
    {
        LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSValueAddedTaxClientServices");
        CMSValueAddedTaxServices serviceImpl = (CMSValueAddedTaxServices) config.getObject("CLIENT_DOWNTIME");
        if(null == serviceImpl)
        {
            LoggingServices.getCurrent().logMsg("CMSValueAddedTaxClientServices", "offLineMode()",
                                                "Cannot instantiate the class that provides the"
                                                + " implementation of CMSValueAddedTaxServices in vat.cfg.",
                                                "Make sure that vat.cfg contains an entry with "
                                                + "a key of CLIENT_DOWNTIME and a value"
                                                + " that is the name of a class that provides a concrete"
                                                + " implementation of CMSValueAddedTaxServices.",
                                                LoggingServices.CRITICAL);
        }
        CMSValueAddedTaxServices.setCurrent(serviceImpl);
    }

    public Object getCurrentService ()
    {
        return(ValueAddedTaxServices.getCurrent());
    }

    public ValueAddedTax getValueAddedTaxForCash (CMSCompositePOSTransaction txn, CMSStore fromStore, CMSStore toStore, Date processDate) throws Exception
    {
        try
        {
            this.fireWorkInProgressEvent(true);
            return(ValueAddedTax) ((CMSValueAddedTaxServices)getCurrentService()).getValueAddedTaxForCash(txn, fromStore, toStore, processDate);
        }
        catch(DowntimeException ex)
        {
            LoggingServices.getCurrent().logMsg(getClass().getName(),
                                                "getValueAddedTax",
                                                "Primary Implementation for CMSValueAddedTaxServices failed, going Off-Line...",
                                                "See Exception",
                                                LoggingServices.MAJOR, ex);
            offLineMode();
            setOffLineMode();
            return(ValueAddedTax) ((CMSValueAddedTaxServices)getCurrentService()).getValueAddedTaxForCash(txn, fromStore, toStore, processDate);
        }
        finally
        {
            this.fireWorkInProgressEvent(false);
        }
    }

    public ValueAddedTax getValueAddedTaxForCredit (CMSCompositePOSTransaction txn, CMSStore fromStore, CMSStore toStore, Date processDate) throws Exception
    {
        try
        {
            this.fireWorkInProgressEvent(true);
            return(ValueAddedTax) ((CMSValueAddedTaxServices)getCurrentService()).getValueAddedTaxForCredit(txn, fromStore, toStore, processDate);
        }
        catch(DowntimeException ex)
        {
            LoggingServices.getCurrent().logMsg(getClass().getName(),
                                                "getValueAddedTax",
                                                "Primary Implementation for CMSValueAddedTaxServices failed, going Off-Line...",
                                                "See Exception",
                                                LoggingServices.MAJOR, ex);
            offLineMode();
            setOffLineMode();
            return(ValueAddedTax) ((CMSValueAddedTaxServices)getCurrentService()).getValueAddedTaxForCredit(txn, fromStore, toStore, processDate);
        }
        finally
        {
            this.fireWorkInProgressEvent(false);
        }
    }

    public ValueAddedTax getOriginalValueAddedTax (CMSCompositePOSTransaction txn, CMSStore fromStore, CMSStore toStore, Date processDate) throws Exception
    {
        try
        {
            this.fireWorkInProgressEvent(true);
            return(ValueAddedTax) ((CMSValueAddedTaxServices)getCurrentService()).getOriginalValueAddedTax(txn, fromStore, toStore, processDate);
        }
        catch(DowntimeException ex)
        {
            LoggingServices.getCurrent().logMsg(getClass().getName(),
                                                "getValueAddedTax",
                                                "Primary Implementation for CMSValueAddedTaxServices failed, going Off-Line...",
                                                "See Exception",
                                                LoggingServices.MAJOR, ex);
            offLineMode();
            setOffLineMode();
            return(ValueAddedTax) ((CMSValueAddedTaxServices)getCurrentService()).getOriginalValueAddedTax(txn, fromStore, toStore, processDate);
        }
        finally
        {
            this.fireWorkInProgressEvent(false);
        }
    }

}
