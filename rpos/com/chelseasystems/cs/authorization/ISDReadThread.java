/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.authorization;

import java.io.*;
import java.net.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;


/**
 * This class is used to continuously obtain credit responces
 * from the ISD Credit authorization Server.
 * After obtaining the responce, it stores them within a hashtable.
 * Then the method getCreditReply() can be used to obtain
 * the credit responce that matches the given id.
 */
public class ISDReadThread extends Thread {

  /* Variable which turns debug information on or off */
  private static boolean debug = false;

  /* Defines buffer size for reads */
  private static final int BUFSIZE = 2191;

  /* Socket used to read from */
  private static Socket readSocket;

  /* Buffered reader used to read data */
  private static BufferedReader dataInput;

  /* Hashtable containing credit replies */
  private static Hashtable creditReply = new Hashtable(1023, (float)0.75);

  /* Character buffer used for reads */
  private static char[] buffer = new char[BUFSIZE];

  /* Maximum number of retries */
  private static int maxRetries;

  /* Maximum number of milliseconds to wait */
  private static int maxWait;

  /*
   * Constuctor to create thread used to read from the
   * ISD socket.
   * @param   psocket    Parent Socket to create stream from
   * @param   retries    Maximum number of retries
   * @param   wait       Maximum milliseconds to wait
   */
  public ISDReadThread(Socket psocket, int retries, int mwait)
      throws Exception {
    super();
    readSocket = psocket;
    maxRetries = retries;
    maxWait = mwait;
    initConnection();
    start();
    //suspend();
  }

  /**
   * This method closes the socket and sets it to null
   */
  public void cleanupSockets() {
    try {
      //suspend();
      dataInput.close();
      dataInput = null;
    } catch (Exception e) {
      if (debug) {
        System.err.println("ISDReadThread.cleanup(): exception = " + e);
      }
      LoggingServices.getCurrent().logMsg(getClass().getName(), "ISDReadThread()"
          , "Problem occurred during socket shutdown, " + "exception: " + e
          , "Verify server is running", LoggingServices.MAJOR);
    }
  }

  /**
   * This method cleans up opened socket connections
   * and sets allocated memory to null.
   */
  public void destroy() {
    // First, clean up the sockets
    cleanupSockets();
    // Now, release memory
    creditReply = null;
  }

  /**
   * This method initializes the input stream
   */
  private void initConnection()
      throws Exception {
    try {
      dataInput = new BufferedReader(new InputStreamReader(readSocket.getInputStream()));
    } catch (Exception e) {
      if (debug) {
        System.err.println("ISDReadThread.initConnection(): exception = " + e);
      }
      LoggingServices.getCurrent().logMsg(getClass().getName(), "ISDReadThread()"
          , "Problem occurred initiliazing input stream:" + e, "Verify ISD Server is running"
          , LoggingServices.CRITICAL);
      // Indicate that a problem has occurred with the socket
      // connection, and notify the ISDServiceManager
      ISDServiceManager.setIsResetConnections(true);
    }
  }

  /**
   * This method returns the matching object associated with this id
   * @param id  Indicates which object to find
   * @return    Object which matches id or null
   */
  public Object getCreditReply(String id) {
    Object obj = creditReply.remove(id);
    return (obj);
  }

  /**
   * This method determines if input is available on the input stream,
   * then it reads it and adds the input to CreditReply
   */
  private void processInput() {
    try {
      if (dataInput == null) {
        System.out.println("ISDReadThread.processInput: Buffer reader is null. @" + new Date());
        ISDServiceManager.setIsResetConnections(true);
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException e) {}
        Thread.yield();
      }
      // Return if a read could be blocked
      if (!dataInput.ready()) {
        return;
      }
      // Now read the data
      int no_read = dataInput.read(buffer, 0, BUFSIZE);
      String key = ISDServiceManager.getKey(buffer);
      // Store the data for later
      String str = new String(buffer, 0, no_read);
      creditReply.put(key, str);
    } catch (IOException e) {
      if (debug) {
        System.err.println("ISDReadThread.processInput(): exception = " + e);
      }
      LoggingServices.getCurrent().logMsg(getClass().getName(), "ISDReadThread()"
          , "Problem occurred during read exception: " + e, "Auto-restart will be attempted"
          , LoggingServices.MAJOR);
      // Indicate that a problem has occurred with the socket
      // connection, and notify the ISDServiceManager
      ISDServiceManager.setIsResetConnections(true);
    }
  }

  /** This methods shuts down the former connection
   *  then it attempts to re-connect to the ISD Server.
   *  @param  psocket Parent Socket to create stream from
   *  @param  retries Maximum number of retries for reads
   *  @param  mwait   Maximum number of milliseconds to wait
   */
  public void reconnect(Socket psocket, int retries, int mwait)
      throws Exception {
    // First, cleanup current socket configuration
    //suspend();
    cleanupSockets();
    // Now reset the connections
    readSocket = psocket;
    maxRetries = retries;
    maxWait = mwait;
    initConnection();
  }

  /**
   * This method performs the main run functionality for this thread.
   * It calls processInput() to process the data input,
   * then it sleeps.
   */
  public void run() {
    while (true) {
      try {
        processInput();
        sleep(maxWait);
      } catch (Exception e) {
        if (debug) {
          System.err.println("ISDReadThread.run(): exception = " + e);
        }
        e.printStackTrace();
        LoggingServices.getCurrent().logMsg(getClass().getName(), "ISDReadThread()"
            , "Exception occured: " + e, "Verify server is running", LoggingServices.INFO);
      }
    }
  }
}
