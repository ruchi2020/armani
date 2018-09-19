/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.ajbauthorization;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.log4j.Logger;

import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.config.AJBPingThreadBootStrap;

/**
 * This class is used to continuously obtain credit responces from the AJB
 * Credit authorization Server. After obtaining the responce, it stores them
 * within a hashtable. Then the method getCreditReply() can be used to obtain
 * the credit responce that matches the given id.
 * 
 * @author Vivek M
 * 
 */
public class AJBReadThread extends Thread {

	/* Variable which turns debug information on or off */
	private static boolean debug = false;

	/* Defines buffer size for reads */
	private static final int BUFSIZE = 2191;

	/* Socket used to read from */
	private static Socket readSocket;

	/* Buffered reader used to read data */
	private static BufferedReader dataInput;

	/* Hashtable containing credit replies */
	private static Hashtable creditReply = new Hashtable(1023, (float) 0.75);

	/* Character buffer used for reads */
	private static char[] buffer = new char[BUFSIZE];

	/* Maximum number of retries */
	private static int maxRetries;

	/* Maximum number of milliseconds to wait */
	private static int maxWait;
	
	/*Added to log info and errors.*/
	private static Logger log = Logger.getLogger(AJBReadThread.class.getName());

	/*
	 * Constuctor to create thread used to read from the AJB socket.
	 * 
	 * @param psocket Parent Socket to create stream from
	 * 
	 * @param retries Maximum number of retries
	 * 
	 * @param wait Maximum milliseconds to wait
	 */
	public AJBReadThread(Socket psocket, int retries, int mwait)
			throws Exception {
		super();
		readSocket = psocket;
		maxRetries = retries;
		maxWait = mwait;
		initConnection();
		start();
		// suspend();
	}

	/**
	 * This method closes the socket and sets it to null
	 */
	public void cleanupSockets() {
		try {
			// suspend();
			dataInput.close();
			dataInput = null;
		} catch (Exception e) {
			if (debug) {
				log.error("AJBReadThread.cleanup(): exception = " + e);
			}
			LoggingServices.getCurrent().logMsg(
					getClass().getName(),
					"AJBReadThread()",
					"Problem occurred during socket shutdown, " + "exception: "
							+ e, "Verify server is running",
					LoggingServices.MAJOR);
		}
	}

	/**
	 * This method cleans up opened socket connections and sets allocated memory
	 * to null.
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
	private void initConnection() throws Exception {
		try {
			dataInput = new BufferedReader(new InputStreamReader(
					readSocket.getInputStream()));
			log.info("Connection has been initialized");
		} catch (Exception e) {
			if (debug) {
				log.info("AJBReadThread.initConnection(): exception = "
								+ e);
			}
			LoggingServices.getCurrent().logMsg(getClass().getName(),
					"AJBReadThread()",
					"Problem occurred initiliazing input stream:" + e,
					"Verify AJB Server is running", LoggingServices.CRITICAL);
			// Indicate that a problem has occurred with the socket
			// connection, and notify the AJBServiceManager
			log.error(e.toString());
			AJBServiceManager.setIsResetConnections(true);
		}
	}

	/**
	 * This method returns the matching object associated with this id
	 * 
	 * @param id
	 *            Indicates which object to find
	 * @return Object which matches id or null
	 */
	public Object getCreditReply(String id) {
		Object obj = creditReply.remove(id);
		return (obj);
	}

	/**
	 * This method determines if input is available on the input stream, then it
	 * reads it and adds the input to CreditReply
	 */
	private void processInput() {
		try {
			if (dataInput == null) {
				log.info("AJBReadThread.processInput: Buffer reader is null. @"
								+ new Date());
				AJBServiceManager.setIsResetConnections(true);
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					log.error(e.toString());
				Thread.yield();
			}
				
			}
			// Return if a read could be blocked
			if (!dataInput.ready()) {
				return;
			}
			// Now read the data
			/*
			 * int no_read = dataInput.read(buffer, 0, BUFSIZE); // Store the
			 * data for later
			 */
			// String key = AJBServiceManager.getKey(buffer);
			String str = null;
			StringBuffer sb = new StringBuffer();
			char STX = '\002';
			char ETX = '\003';
			int value = 0;
			while ((value = dataInput.read()) != -1) {
				char c = (char) value;
				if (c == STX)
					continue;
				// end? jump out
				if (c == ETX) {
					break;
				}
				sb.append(c);
			}
			log.info("::::::::::::::::::::::AJB Response : "
					+ sb.toString());
			str = sb.toString();
			String key = AJBServiceManager.getKey(str);
			System.out.println("key>>>>>>>>>>>>>>>>>>"+key);
			if(key!=null){
			creditReply.put(key, str);
			}
			
		} catch (IOException e) {
			if (debug) {
				log.info("AJBReadThread.processInput(): exception = "
						+ e);
			}
			LoggingServices.getCurrent().logMsg(getClass().getName(),
					"AJBReadThread()",
					"Problem occurred during read exception: " + e,
					"Auto-restart will be attempted", LoggingServices.MAJOR);
			// Indicate that a problem has occurred with the socket
			// connection, and notify the AJBServiceManager
			log.error(e.toString());
			AJBServiceManager.setIsResetConnections(true);
		}
	}

	/**
	 * This methods shuts down the former connection then it attempts to
	 * re-connect to the AJB Server.
	 * 
	 * @param psocket
	 *            Parent Socket to create stream from
	 * @param retries
	 *            Maximum number of retries for reads
	 * @param mwait
	 *            Maximum number of milliseconds to wait
	 */
	public void reconnect(Socket psocket, int retries, int mwait)
			throws Exception {
		// First, cleanup current socket configuration
		// suspend();
		cleanupSockets();
		// Now reset the connections
		readSocket = null;
		readSocket = psocket;
		maxRetries = retries;
		maxWait = mwait;
		initConnection();
	}

	/**
	 * This method performs the main run functionality for this thread. It calls
	 * processInput() to process the data input, then it sleeps.
	 */
	public void run() {
		while (true) {
			try {
				processInput();
				sleep(100);
			} catch (Exception e) {
				if (debug) {
					log.info("AJBReadThread.run(): exception = " + e);
				}
				e.printStackTrace();
				LoggingServices.getCurrent().logMsg(getClass().getName(),
						"AJBReadThread()", "Exception occured: " + e,
						"Verify server is running", LoggingServices.INFO);
				log.error(e.toString());
			}
		}
	}
}
