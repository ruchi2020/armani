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
 * This class continously writes credit requests to the ISD server.
 * writeCreditRequest() stores the credit requests.
 * Then processOutput() takes each stored request and writes
 * it out the ISD Server.
 */
public class ISDWriteThread extends Thread {

  /* Turns debugging information on or off */
  private static boolean debug = false;

  /* Credit requests are queued up into this vector */
  private static Vector creditRequest = new Vector(1025, 1025);

  /* Socket to write credit requests to */
  private static Socket writeSocket;

  /* Buffered writer for writing out credit requests to ISD */
  private static BufferedWriter dataOutput;

  /* Maximum number of retries */
  private static int maxRetries;

  /* Maximum number of milliseconds to wait */
  private static int maxWait;

  /* Buffer size used in writes */
  private static final int BUFSIZE = 2191;

  /**
   * Constuctor to create thread which writes
   * to the ISD socket.
   * @param   psocket    Parent Socket to create stream from
   * @param   retries    Maximum number of retries
   * @param   wait       Maximum milliseconds to wait
   */
  public ISDWriteThread(Socket psocket, int retries, int mwait)
      throws Exception {
    super();
    writeSocket = psocket;
    maxRetries = retries;
    maxWait = mwait;
    initConnection();
    start();
    //suspend();
  }

  /**
   * This method closes and destroys the socket connection.
   */
  public void cleanupSockets() {
    try {
      dataOutput.close();
    } catch (Exception e) {
      if (debug) {
        System.err.println("ISDWriteThread.cleanup(): exception = " + e);
      }
      LoggingServices.getCurrent().logMsg(getClass().getName(), "ISDWriteThread()"
          , "Exception occured while closing sockets: " + e, "Verify CreditAuth Server is running"
          , LoggingServices.MAJOR);
    }
    dataOutput = null;
  }

  /**
   * This method ensures that the socket connections are cleaned up,
   * and that memory is freed
   */
  public void destroy() {
    // First, clean up the sockets
    cleanupSockets();
    // Now, release memory
    creditRequest = null;
  }

  /**
   * This method initializes the output stream connection
   */
  private void initConnection()
      throws Exception {
    try {
      dataOutput = new BufferedWriter(new OutputStreamWriter(writeSocket.getOutputStream()));
    } catch (Exception e) {
      if (debug) {
        System.err.println("ISDWriteThread.initConnection(): exception = " + e);
      }
      LoggingServices.getCurrent().logMsg(getClass().getName(), "ISDWriteThread()"
          , "Exception creating buffered writer: " + e, "Verify ISD Server is running"
          , LoggingServices.CRITICAL);
      // Indicate that a problem has occurred with the socket
      // connection, and notify the ISDServiceManager
      ISDServiceManager.setIsResetConnections(true);
    }
  }

  /**
   * This method takes each element out of creditRequest,
   * and writes it out to the ISD server.
   */
  private void processOutput() {
    while (!creditRequest.isEmpty()) {
      Object obj = creditRequest.elementAt(0);
      String request = obj.toString();
      try {
        dataOutput.write(request, 0, request.length());
        dataOutput.flush();
      } catch (Exception ex) {
        if (debug) {
          System.out.println("ISDWriteThread.processOutput: exception =" + ex);
        }
        LoggingServices.getCurrent().logMsg(getClass().getName(), "ISDWriteThread()"
            , "Exception occurred during write: " + ex, "Verify ISD Server is running"
            , LoggingServices.CRITICAL);
        // Indicate that a problem has occurred with the socket
        // connection, and notify the ISDServiceManager
        ISDServiceManager.setIsResetConnections(true);
      }
      creditRequest.removeElementAt(0);
    }
    //suspend();
  }

  /**
   * This method closes down the former socket connection
   * and attempts to reconnect to the ISD server.
   * @param   psocket   Parent Socket to create stream from
   * @param   retries   Maximum number of retries for reads
   * @param   mwait     Maximum number of milliseconds to wait
   */
  public void reconnect(Socket psocket, int retries, int mwait)
      throws Exception {
    // First, cleanup current socket configuration
    //suspend();
    cleanupSockets();
    // Now reset the connections
    writeSocket = psocket;
    maxRetries = retries;
    maxWait = mwait;
    initConnection();
  }

  /**
   * This method performs the main run functionality for this thread.
   * It calls processOutput() to write the data,
   * then it sleeps.
   */
  public void run() {
    while (true) {
      try {
        processOutput();
        sleep(maxWait);
      } catch (Exception e) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "ISDWriteThread()"
            , "Exception: " + e, "", LoggingServices.INFO);
      }
    }
  }

  /**
   * This method adds the given string to creditRequest,
   * then it resumes the thread.
   * @param request   Credit request to write to the ISD server
   */
  public void writeCreditRequest(String request) {
    if (request.length() < 1) {
      return;
    }
    if (debug) {
      System.out.println("writethread.writeCreditRequest: id =" + ISDServiceManager.getKey(request)
          + " request length = " + request.length());
    }
    creditRequest.addElement(request);
    //resume();
  }
}
