/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.authorization;

import java.net.Socket;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.IConfig;
import com.chelseasystems.cr.logging.LoggingServices;


/**
 * This class manages communications to the ISD Credit Authorization Server.
 * It sets up two threads to read and write to the ISD Server.
 * Then it maintains a threadQueue, which is used to obtain
 * credit replies stored within the ISDReadThread instance.
 */
public class ISDServiceManager extends Object implements Runnable, IConfig {

  /* Turns debugging information on or off */
  private static String debug = "FALSE";

  /* Instance of ConfigMgr to obtain current configuration parameters */
  private static ConfigMgr config;

  /* Instance of ISDServiceManager */
  private static ISDServiceManager current = null;

  /* Socket connected to the ISD Server */
  private static Socket parentSocket;

  /* Used by the threadQueue to obtain credit replies */
  private static ISDQueue queue;

  /* Thread which reads data from the ISD Server */
  private static ISDReadThread readThread;

  /* Thread which writes data to the ISD Server */
  private static ISDWriteThread writeThread;

  /* Threads which execute ISDQueue methods to obtain credit replies*/
  private static Thread[] threadQueue;

  /* Hostname where the ISD server runs */
  private static String hostname;

  /* Port number of ISD Server, normally 4243 */
  private static int port;

  /* Maximum number to retry */
  private static int max_retries;

  /* Maximum number of milliseconds to wait */
  private static int max_wait;

  /* Number of threads in the threadQueue */
  private static int threadMax = 3;

  /* Indicates whether connections to ISD server need resetting */
  private static boolean isResetConnections = false;

  /*
   * ISDServiceManager is a singleton,
   * it can only be created via the getCurrent() method.
   */
  private ISDServiceManager()
      throws Exception {
    System.out.println("ISDServiceManager.constructor() being called.");
    setup();
  }

  /**
   * This method shuts down all socket connections and
   * sets them to null
   */
  private void cleanupSockets() {
    try {
      System.out.println("ISDServiceManager.cleanupSockets() being called.");
      parentSocket.close();
      parentSocket = null;
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "ISDServiceManager()"
          , "Exception occured while closing socket: " + e, "Verify server is running"
          , LoggingServices.INFO);
    }
  }

  /**
   * This method cleans up opened socket connections
   * and sets allocated memory to null.
   */
  public void finalize()
      throws Throwable {
    System.out.println("ISDServiceManager.finalize() being called.   Socket cleanup.");
    // Suspend and destroy the threads
    //suspendThreads();
    for (int i = 0; i < threadMax; i++) {
      threadQueue[i].destroy();
      threadQueue[i] = null;
    }
    // Now, release memory
    writeThread.destroy();
    writeThread = null;
    readThread.destroy();
    readThread = null;
    // Finally, cleanup the sockets
    cleanupSockets();
  }

  /**
   * This method returns the instance of the ISDServiceManager
   * @return ISDServiceManager  - Singleton instance of this
   */
  public static ISDServiceManager getCurrent()
      throws Exception {
    System.out.println("ISDServiceManager.getCurrent() being called.");
    if (current == null) {
      synchronized (ISDServiceManager.class) {
        if (current == null) {
          current = new ISDServiceManager();
        }
      }
    }
    return current;
  }

  /*
   * Returns authorization for the credit request
   * @param request - Credit request String
   * @return        - String representing the credit authorization
   */
  public static String getCreditAuth(String request) {
    writeThread.writeCreditRequest(request);
    // Make sure the read/write threads are resumed
    //writeThread.resume();
    //readThread.resume();
    String result = ISDQueue.getCreditReply(request);
    if (result == null || result.length() == 0) {
      setIsResetConnections(true);
      try {
        Thread.sleep(1500L);
      } catch (InterruptedException e) {}
      result = ISDQueue.getCreditReply(request);
    }
    return (result);
  }

  /**
   * This method indicates whether the connection needs
   * to be reset.
   * @return  boolean which indicates whether to reset connections
   */
  public synchronized boolean getIsResetConnections() {
    return isResetConnections;
  }

  /**
   * This method returns the encoded Key within message
   * @param message String containing credit request/reply
   * @return        String containing key for message
   */
  public static String getKey(String message) {
    String str = "";
    if (message.length() > 88) {
      str = message.substring(67, 87);
    } else {
      str = null;
    }
    return str;
  }

  /**
   * This method returns the encoded Key within message
   * @param message Character array containing credit request/reply
   * @return        String containing key for message
   */
  public static String getKey(char message[]) {
    String str = "";
    if (message.length > 88) {
      str = new String(message, 67, 20);
    } else {
      str = null;
    }
    return str;
  }

  /**
   * This method allows the configuration parameters
   * to be changed.
   * @param aKey  An array of strings containing keys
   */
  public void processConfigEvent(String[] aKey) {
    boolean valueChanged = false;
    for (int x = 0; x < aKey.length; x++) {
      if (aKey[x].equalsIgnoreCase("SERVER_HOSTNAME")) {
        hostname = config.getString("SERVER_HOSTNAME");
        valueChanged = true;
      }
      if (aKey[x].equalsIgnoreCase("SERVER_PORT")) {
        port = (config.getInteger("SERVER_PORT")).intValue();
        valueChanged = true;
      }
      if (aKey[x].equalsIgnoreCase("MAX_WAIT")) {
        max_wait = (config.getInteger("MAX_WAIT")).intValue();
        valueChanged = true;
      }
      if (aKey[x].equalsIgnoreCase("MAX_RETRIES")) {
        max_retries = (config.getInteger("MAX_RETRIES")).intValue();
        valueChanged = true;
      }
      if (debug.equals("TRUE")) {
        System.out.println("Resetting CMS Credit Services ISDServiceManager:, Key: " + aKey[x]);
      }
    }
    if (valueChanged) {
      setIsResetConnections(true);
    }
    if (debug.equals("TRUE")) {
      System.out.println("RESET CMS Credit Services ISDServiceManager:  host = " + hostname);
      System.out.println("RESET CMS Credit Services ISDServiceManager:  port = " + port);
      System.out.println("RESET CMS Credit Services ISDServiceManager:  max_wait = " + max_wait);
      System.out.println("RESET CMS Credit Services ISDServiceManager:  max_retries = "
          + max_retries);
    }
  }

  /**
   * This method sets the indicator to reset connections
   * @param  reset  Indicates whether to reset connections
   */
  public static synchronized void setIsResetConnections(boolean reset) {
    System.out.println("ISDServiceManager.setIsResetConnections() being called.");
    isResetConnections = reset;
  }

  /**
   * This method initializes all the parent socket connections
   * to the ISD server.
   * Then it creates the read and write threads,
   * and the threads which obtain the credit responses.
   */
  private void setup()
      throws Exception {
    System.out.println(
        "ISDServiceManager.setup() being called.  Socket being created at host:port.");
    setup_configuration();
    parentSocket = new Socket(hostname, port);
    parentSocket.setSoTimeout(30000); // 30 seconds
    System.out.println("ISDServiceManager.setup() parent socket hostname:port past creation.");
    readThread = new ISDReadThread(parentSocket, max_retries, max_wait);
    System.out.println(
        "ISDServiceManager.setup() ISDReadThread class on parent socket past creation.");
    writeThread = new ISDWriteThread(parentSocket, max_retries, max_wait);
    System.out.println(
        "ISDServiceManager.setup() ISDWriteThread class on parent socket past creation.");
    if (debug.equals("TRUE")) {
      System.out.println(
          "CMS Credit Services ISDServiceManager.setup()Thread: read/write threads created");
    }
    queue = new ISDQueue(max_wait, max_retries, readThread);
    System.out.println("ISDServiceManager.setup() ISDQueue class on parent socket past creation.");
    threadQueue = new Thread[threadMax];
    for (int i = 0; i < threadMax; i++) {
      threadQueue[i] = new Thread(this);
      threadQueue[i].start();
    }
    if (debug.equals("TRUE")) {
      System.out.println(
          "CMS Credit Services ISDServiceManager.setup()Thread: queue threads created");
    }
  }

  /**
   * This method initilizes the configuration setup
   */
  private void setup_configuration() {
    String fileName = "credit_auth.cfg";
    if (fileName == null) {
      System.out.println("System property CONFIG_FILE missing in bat file.");
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setup_configuration()"
          , "System property CONFIG_FILE missing in bat file for ISDServiceManager."
          , "Make sure that the .bat file contains a -DCONFIG_FILE parameter." + " for the "
          + fileName + " file.", LoggingServices.MAJOR);
    }
    config = new ConfigMgr(fileName);
    hostname = config.getString("SERVER_HOSTNAME");
    port = config.getInteger("SERVER_PORT").intValue();
    max_wait = config.getInteger("MAX_WAIT").intValue();
    max_retries = config.getInteger("MAX_RETRIES").intValue();
    threadMax = config.getInteger("MAX_THREAD").intValue();
    debug = config.getString("VERBOSE_MODE").toUpperCase();
    if (debug.equals("TRUE")) {
      System.out.println("CMS Credit Services ISDServiceManager:  configFile = " + fileName);
      System.out.println("CMS Credit Services ISDServiceManager:  host = " + hostname);
      System.out.println("CMS Credit Services ISDServiceManager:  port = " + port);
      System.out.println("CMS Credit Services ISDServiceManager:  max_wait = " + max_wait);
      System.out.println("CMS Credit Services ISDServiceManager:  max_retries = " + max_retries);
    }
  }

  /** This methods shuts down the former connection
   *  then it attempts to re-connect to the ISD Server.
   *  @param  host    Hostname of machine to connect to
   *  @param  pport   Port number to connect to
   *  @param  retries Maximum number of retries for reads
   *  @param  mwait   Maximum number of milliseconds to wait
   */
  public void reconnect(String host, int pport, int retries, int mwait)
      throws Exception {
    System.out.println("ISDServiceManager.reconnect() being called.");
    //suspendThreads();
    // Cleanup the current socket configuration
    readThread.cleanupSockets();
    writeThread.cleanupSockets();
    cleanupSockets();
    // Now reset the connections
    hostname = host;
    port = pport;
    max_retries = retries;
    max_wait = mwait;
    // Then attempt to restart the connections
    parentSocket = new Socket(hostname, port);
    readThread.reconnect(parentSocket, max_retries, max_wait);
    writeThread.reconnect(parentSocket, max_retries, max_wait);
    //resumeThreads();
  }

  /**
   * This is the run method, it keeps the server running.
   */
  public void run() {
    System.out.println("ISDServiceManager.run() being called.");
    while (true) {
      try {
        if (getIsResetConnections()) {
          try_reconnect();
        }
        Thread.sleep(max_wait);
      } catch (Exception e) {}
    }
  }

  //   /**
   //    * This method resumes the threads in the threadQueue
   //    */
  //   private void resumeThreads () {
  //      System.out.println("ISDServiceManager.resumeThreads() being called.");
  //      for (int i = 0; i < threadMax; i++)
  //         threadQueue[i].resume();
  //   }
  //   /**
   //    * This method suspends the threads in the threadQueue
   //    */
  //   private void suspendThreads () {
  //      System.out.println("ISDServiceManager.suspendThreads() being called.");
  //      for (int i = 0; i < threadMax; i++)
  //         threadQueue[i].suspend();
  //   }
  /**
   * Attempt to reconnect to the ISD server
   */
  private void try_reconnect() {
    System.out.println("ISDServiceManager.try_reconnect() being called for max_retries times.");
    int i = 0;
    while (i < max_retries) {
      try {
        reconnect(hostname, port, max_retries, max_wait);
        break;
      } catch (Exception e) {
        if (debug.equals("TRUE")) {
          System.out.println(
              "CMS Credit Service ISDServiceManager.try_reconnect(): reconnect failed exception = "
              + e);
        }
        LoggingServices.getCurrent().logMsg(getClass().getName(), "ISDServiceManager()"
            , "Exception occured while reconnecting: " + e, "Verify ISD Server is running"
            , LoggingServices.CRITICAL);
        i++;
      }
      // Sleep before retrying
      try {
        Thread.sleep(max_wait);
      } catch (Exception e) {}
    }
    setIsResetConnections(false);
  }
}
