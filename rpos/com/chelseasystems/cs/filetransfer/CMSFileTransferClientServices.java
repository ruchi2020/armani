/*
 * @copyright (c) 2002 Chelsea Market Systems
 */

package  com.chelseasystems.cs.filetransfer;

import com.chelseasystems.cr.filetransfer.Transport;
import com.chelseasystems.cr.filetransfer.FileTransferServices;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.appmgr.*;


/**
 * Client-side object for retrieving and submitting.
 */
public class CMSFileTransferClientServices extends ClientServices
{
    /** Configuration manager **/
//    private ConfigMgr config = null;

    /**
     * Set the current implementation
     */
    public CMSFileTransferClientServices()
    {
        // Set up the configuration manager.
        config = new ConfigMgr("filetransfer.cfg");
    }

    /**
     * initialize primary implementation
     */
    public void init(boolean online) throws Exception
    {
        // Set up the proper implementation of the service.
        FileTransferServices.setClient((FileTransferServices)config.getObject("CLIENT_LOCAL_IMPL"));
        if(online)
        {
            onLineMode();
        }
        else
        {
            offLineMode();
        }
    }

    /**
     * Reads "CLIENT_IMPL" from config file. Returns the class that defines
     * what object is providing the service to objects using this client service
     * in "on-line" mode, i.e. connected to an app server.  If null, this 
     * clientservice is not considered when determining app online status.
     * @return a class of the online service.
     */
    protected Class getOnlineService () throws ClassNotFoundException {
      String className = config.getString("CLIENT_REMOTE_IMPL");
      Class serviceClass = Class.forName(className);
      return  serviceClass;
    }

    
    public void onLineMode()
    {
        LoggingServices.getCurrent().logMsg("On-Line Mode for CMSFileTransferClientServices");
        FileTransferServices remoteImpl = (FileTransferServices)config.getObject("CLIENT_REMOTE_IMPL");

        if(remoteImpl == null)
        {
            LoggingServices.getCurrent().logMsg("CMSFileTransferClientServices", "onLineMode()",
                                                "Cannot instantiate the class that provides the"
                                                + "implementation of FileTransferServices in filetransfer.cfg.",
                                                "Make sure that filetransfer.cfg contains an entry with "
                                                +"a key of CLIENT_IMPL and a value that is the name of a class "
                                                +"that provides a concrete implementation of FileTransferServices.",
                                                LoggingServices.MAJOR);
            setOffLineMode();
            return;
        }

        FileTransferServices.setServer(remoteImpl);
    }

    public void offLineMode()
    {
        LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSFileTransferClientServices");
        FileTransferServices remoteImpl = (FileTransferServices) config.getObject("CLIENT_DOWNTIME");

        if(remoteImpl == null)
        {
            LoggingServices.getCurrent().logMsg("CMSFileTransferClientServices", "offLineMode()",
                                                "Cannot instantiate the class that provides the"
                                                + " implementation of FileTransferServices in filetransfer.cfg.",
                                                "Make sure that filetransfer.cfg contains an entry with "
                                                + "a key of CLIENT_DOWNTIME and a value"
                                                + " that is the name of a class that provides a concrete"
                                                + " implementation of FileTransferServices.",
                                                LoggingServices.CRITICAL);
        }

        FileTransferServices.setServer(remoteImpl);
    }

   public Object getCurrentService () {
      return  FileTransferServices.getClient();
   }

    /**
     * Save a transport.
     * @param transport Transport
     */
    public void saveUploadTransport(Transport transport) throws Exception
    {
        try
        {
            this.fireWorkInProgressEvent(true);
            FileTransferServices.getServer().saveTransport(transport);
        }
        catch(DowntimeException ex)
        {
            LoggingServices.getCurrent().logMsg(getClass().getName(),"saveTransport",
                                                "Primary Implementation for FileTransferServices failed, going Off-Line...",
                                                "See Exception", LoggingServices.MAJOR, ex);
            offLineMode();
            setOffLineMode();

            FileTransferServices.getServer().saveTransport(transport);
        }
        finally
        {
            this.fireWorkInProgressEvent(false);
        }
    }


    public void saveDownloadTransport(Transport transport) throws Exception
    {
        try
        {
            this.fireWorkInProgressEvent(true);
            FileTransferServices.getClient().saveTransport(transport);
        }
        catch(DowntimeException ex)
        {
            LoggingServices.getCurrent().logMsg(getClass().getName(),"saveTransport",
                                                "Primary Implementation for FileTransferServices failed, going Off-Line...",
                                                "See Exception", LoggingServices.MAJOR, ex);
            offLineMode();
            setOffLineMode();

            FileTransferServices.getClient().saveTransport(transport);
        }
        finally
        {
            this.fireWorkInProgressEvent(false);
        }
    }


    /**
     * Prepare a transport.
     * @param transportName Name of transport to download.
     */
    public Transport[] prepareUploadTransport(String transportName) throws Exception
    {
        try
        {
            this.fireWorkInProgressEvent(true);
            return(Transport[])FileTransferServices.getClient().prepareTransport(transportName);
        }
        catch(DowntimeException ex)
        {
            LoggingServices.getCurrent().logMsg(getClass().getName(),"prepareTransport",
                                                "Primary Implementation for FileTransferServices failed, going Off-Line...",
                                                "See Exception", LoggingServices.MAJOR, ex);
            offLineMode();
            setOffLineMode();
            return(Transport[])FileTransferServices.getClient().prepareTransport(transportName);
        }
        finally
        {
            this.fireWorkInProgressEvent(false);
        }
    }


    public Transport[] prepareDownloadTransport(String transportName) throws Exception
    {
        try
        {
            this.fireWorkInProgressEvent(true);
            return(Transport[])FileTransferServices.getServer().prepareTransport(transportName);
        }
        catch(DowntimeException ex)
        {
            LoggingServices.getCurrent().logMsg(getClass().getName(),"prepareTransport",
                                                "Primary Implementation for FileTransferServices failed, going Off-Line...",
                                                "See Exception", LoggingServices.MAJOR, ex);
            offLineMode();
            setOffLineMode();
            return(Transport[])FileTransferServices.getServer().prepareTransport(transportName);
        }
        finally
        {
            this.fireWorkInProgressEvent(false);
        }
    }
}
