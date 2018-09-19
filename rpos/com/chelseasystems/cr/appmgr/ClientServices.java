/*
 * This unpublished work is protected by trade secret, copyright and other laws.
 * In the event of publication, the following notice shall apply:
 * Copyright &copy; 2004 Retek Inc.  All Rights Reserved.
 */
package com.chelseasystems.cr.appmgr;

import javax.swing.event.EventListenerList;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.event.IWorkInProgressListener;

/**
 * This abstract class provides the base for all ClientServices classes
 * @author John Gray
 * @version 1.0a
 */
public abstract class ClientServices {

  //~ Instance fields ------------------------------------------------------------------------------

  /** Configuration manager **/
  protected ConfigMgr config = null;

  /** list of listeners wishing to hear WorkInProgess events */
  private EventListenerList listenerList = new EventListenerList();

  /** refernce to browser manager */
  private IBrowserManager theBrowserMgr;

  /** reference to the downtime manager */
  private IDownTimeManager theDowntimeMgr;

  /** flag to determine stats of performance monitoring */
  private boolean isPerformance = false;

  //~ Constructors ---------------------------------------------------------------------------------

  /**
   */
  public ClientServices () {
    String performance = System.getProperty("PERFORMANCE");
    if ((null != performance) && (performance.equalsIgnoreCase("TRUE"))) {
      isPerformance = true;
    }
  }

  //~ Methods --------------------------------------------------------------------------------------

  /**
   * Inheriting classes required to implement.  This method will get called to
   * initialize the client server, specifying whether the environment is
   * currently on offline or online mode.
   * @see com.chelseasystems.cr.appmgr.bootstrap.ClientServicesBootStrap
   * @param bOnLine <true> if service is allowed to use <online> implementation
   * @throws Exception if something bad happens
   */
  public abstract void init (boolean bOnLine) throws Exception;

  /**
   * Inheriting classes required to implement.  Any call to this method should
   * be treated as a signal to switch implementation to the specified
   * <b>offline</b> implementation.
   * @see com.chelseasystems.cr.appmgr.IDownTimeManager
   */
  public abstract void offLineMode ();

  /**
   * Inheriting classes required to implement.  Any call to this method should
   * be treated as a signal to switch implementation to the specified
   * <b>online</b> implementation.
   * @see com.chelseasystems.cr.appmgr.IDownTimeManager
   */
  public abstract void onLineMode ();

  /**
   * Reads "CLIENT_IMPL" from config file. Returns the class that defines
   * what object is providing the service to objects using this client service
   * in "on-line" mode, i.e. connected to an app server.  If null, this 
   * clientservice is not considered when determining app online status.
   * @return a class of the online service.
   */
  protected Class getOnlineService () throws ClassNotFoundException {
    String className = config.getString("CLIENT_IMPL");
    Class serviceClass = Class.forName(className);
    return  serviceClass;
  }

  /**
   * sets the browser manager. the call is done by the bootstrap manager
   * @param theMgr reference to the browser manager
   */
  public void setBrowserMgr (IBrowserManager theMgr) {
    this.theBrowserMgr = theMgr;
  }

  /**
   * sets the downtime manager. the call is done by the bootstrap manager
   * @param theMgr reference to the downtime manager
   */
  public void setDownTimeMgr (IDownTimeManager theMgr) {
    this.theDowntimeMgr = theMgr;
  }

  /**
   * Return the current system time in milliseconds.
   * @return long the current system time in milliseconds.
   */
  public long getStartTime () {
    if (isPerformance) {
      return System.currentTimeMillis();
    }
    else
    {
      return 0;
    }
  }

  /**
   * @return <code>true</code> if performance is turned on.
   */
  public boolean isPerformance () {
    return isPerformance;
  }

  /**
   * displays a println showing the delta time for a method
   * @param methodName
   * @param startTime
   */
  public void addPerformance (String methodName, long startTime) {
    if (!isPerformance) {
      return;
    }
    theBrowserMgr.addPerformance(methodName, startTime);
  }

  /**
   * @param l
   */
  public void addWorkInProgressListener (IWorkInProgressListener l) {
    listenerList.add(IWorkInProgressListener.class, l);
  }

  ////////////////////////////////////////
  //
  // Protected methods
  //
  ////////////////////////////////////////

  /**
   * Protected access to the IBrowserManager that is using this client service.
   * @return IBrowserManager
   */
  protected IBrowserManager getBrowserMgr () {
    return (this.theBrowserMgr);
  }

  /**
   * tells the downtime manager that this client service has
   * switched from on-line mode to downtime mode
   */
  protected void setOffLineMode () {
    if (theDowntimeMgr != null)
      theDowntimeMgr.setOnLine(false);
  }

  /**
   * set the work in progress on the GUI.  be sure to call with a
   * false param when the work is done.
   * @param isWorkInProgress true is work in progress, false when work is done
   */
  protected void fireWorkInProgressEvent (boolean isWorkInProgress) {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == IWorkInProgressListener.class) {
        ((IWorkInProgressListener)listeners[i + 1]).workInProgressEvent(isWorkInProgress);
      }
    }
  }
}
