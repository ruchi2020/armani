/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.ajbauthorization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.IConfig;
import com.chelseasystems.cr.logging.LoggingServices;

/**
 * This class manages communications to the AJB Credit Authorization Server. It
 * sets up two threads to read and write to the AJB Server. Then it maintains a
 * threadQueue, which is used to obtain credit replies stored within the
 * AJBReadThread instance.
 * 
 * @author Vivek M
 * 
 */
public class AJBServiceManager extends Object implements Runnable, IConfig {

	/* Turns debugging information on or off */
	private static String debug = "FALSE";

	/* Instance of ConfigMgr to obtain current configuration parameters */
	private static ConfigMgr config;

	/* Instance of ConfigMgr to obtain store configuration parameters */
	private static ConfigMgr storeCustomConfig;

	/* Instance of AJBServiceManager */
	private static AJBServiceManager current = null;

	/* Socket connected to the AJB Server */
	private static Socket parentSocket;

	/* Used by the threadQueue to obtain credit replies */
	private static AJBQueue queue;

	/* Thread which reads data from the AJB Server */
	private static AJBReadThread readThread;

	/* Thread which writes data to the AJB Server */
	private static AJBWriteThread writeThread;

	/* Threads which execute AJBQueue methods to obtain credit replies */
	private static Thread[] threadQueue;

	/* Hostname where the AJB server runs */
	private String hostname;

	/* Port number of AJB Server, normally 4243 */
	private int port;

	/* Maximum number to retry */
	private static int max_retries;

	/* Maximum number of milliseconds to wait */
	private static int max_wait;

	/* Number of threads in the threadQueue */
	private static int threadMax = 3;
	
	/* Time out seconds for AJB request to get the response- used in AJBCreditDebitFormatter.java */
	private int ajbResTimeOutSec = -1 ;
	
	/* Time out seconds for AJB request to get the response- used in AJBCreditDebitFormatter.java */
	private int ajbItemResTimeOutSec = -1 ;

	
	/* Indicates whether connections to AJB server need resetting */
	private static boolean isResetConnections = false;

	/* Indicates whether connections to AJB server need resetting */
	//Vivek Mishra : Changed from CMSContainer to AJBPingThreadBootStrap to use AJBPingThread at client side. 
	private AJBPingThread pingThread ;

	/* Current host to which Ping is getting send */
	public String current_host;

	/* Error message */
	public String error_message;
	
	/* Error message */
	public String error_message_prv;

	private String hostnames;

	private String[] hosts;
	
	private Socket itembucketsocket = null;
	
	/*Added to log info and errors.*/
	private static Logger log = Logger.getLogger(AJBServiceManager.class.getName());

	/*
	 * AJBServiceManager is a singleton, it can only be created via the
	 * getCurrent() method.
	 */
	private AJBServiceManager() throws Exception 
	{
		log.info("Initialising the AJBServiceManager ");
		setup_configuration();
		pingThread = new AJBPingThread();
	}
	
	@Deprecated
	/**
	 * This method shuts down all socket connections and sets them to null
	 */
	private void cleanupSockets() {
		try {
			log.info("AJBServiceManager.cleanupSockets() being called.");
			parentSocket.close();
			parentSocket = null;
		} catch (Exception e) {
			LoggingServices.getCurrent().logMsg(getClass().getName(),
					"AJBServiceManager()",
					"Exception occured while closing socket: " + e,
					"Verify server is running", LoggingServices.INFO);
			log.error(e.toString());
		}
	}

	/**
	 * This method cleans up opened socket connections and sets allocated memory
	 * to null.
	 */
	public void finalize() throws Throwable {
		
		/**
		 * 
		 * Commented out - current implementation is not using queue and write thread.
		 */
		
		/*log.info("AJBServiceManager.finalize() being called.   Socket cleanup.");
		// Suspend and destroy the threads
		// suspendThreads();
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
		cleanupSockets();*/
	}

	/**
	 * This method returns the instance of the AJBServiceManager
	 * 
	 * @return AJBServiceManager - Singleton instance of this
	 */
	public static AJBServiceManager getCurrent() throws Exception 
	{
		if (log.isDebugEnabled())
		{
			log.debug("AJBServiceManager.getCurrent() being called.");
		}
		//Vivek Mishra : Changed the condition to create a new AJBServiceManager Object if writethread is null.
		//if (current == null)
		if (current == null ) {
			synchronized (AJBServiceManager.class) {
				//Vivek Mishra : Changed the condition to create a new AJBServiceManager Object if writethread is null.
				//if (current == null) {
				if (current == null ) {
					current = new AJBServiceManager();
				}
			}
		}
		/*else
			current.setup();*/
		return current;
	}

	/*
	 * Returns authorization for the credit request
	 * 
	 * @param request - Credit request String
	 * 
	 * @return - String representing the credit authorization
	 */
	public String getCreditAuth(String request) throws SocketTimeoutException, IOException  
	{
		String result = null;
		try
		{
			
			if (isFipaySimulator()) 
			{
				return readSimulatedResponse();
			}
			// '\002' is <STX> (Hex02) and '\003' is <ETX> (Hex03) as per
			// AJBs standard message format.

			//log.info("Request to AJB : " + request);
			//writeThread.writeCreditRequest('\002' + request + '\003' + "\n", parent_socket);
			//result = AJBQueue.getCreditReply(request);
			
			int timeout = getAjbResTimeOutSec();
			//AJBSocketConnector connector = new AJBSocketConnector(hostname, port, timeout);
			AJBSocketConnector connector = AJBSocketConnector.getInstance();
			if ( log.isDebugEnabled())
			{
				log.debug(" request got in getCreditAuth     ::" + request);
			}
			
			result = connector.sendMessage('\002' + request + '\003' + "\n");

			/*
			 * if (result == null || result.length() == 0) {
			 * setIsResetConnections(true); try { Thread.sleep(1500L); } catch
			 * (InterruptedException e) { log.error(e.toString()); } result =
			 * AJBQueue.getCreditReply(request); }
			 */

			//Mayuri edhara :: 10-MAR-17 - Commented the below as certain flag in fipay config xml are set.
			// Mayuri Edhara :: 12-JAN-17 - added the debit acknowledgement 
		  /* if(result !=null && result.startsWith("101")){
			
				if(result.contains("DEBITACKREQ") || result.contains("DebitAckReq")){
					
					AJBRequestResponseMessage ackRequest = new AJBRequestResponseMessage(result.replace("*AcquirerBackup", ""));
					//Replace the request byte to 901 from 101
					ackRequest.setValue(0, AJBMessageCodes.IX_CMD_ACK_REQUEST.getValue());

					log.info(">>>>>>>>>>>>>>>>>> Sending the ACK request back to Fipay <<<<<<<<<<" + request);
					
					connector.sendAckMessage('\002' + ackRequest.toString() + '\003' + "\n");
					log.info(" >>>>>>>>>>>> sentACKMessage <<<<<<<<<<<<<<<<<<<<<<<");
				}
			} */
			
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
        	log.error("SocketException     "+ e);
        	throw new SocketTimeoutException("Card Response Timeout") ;
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("IOException     "+ e);
			throw e;
		}
		//log.info("Response from AJB : "+result);
		return (result);
	}
	
	/*
	 * Returns authorization for the credit request
	 * 
	 * @param request - Item Bucket request String
	 * 
	 * @return - String representing the credit authorization
	 */
	public String getItemAuth(String request) throws IOException 
	{
		// '\002' is <STX> (Hex02) and '\003' is <ETX> (Hex03) as per
		// AJBs standard message format.
		/*Socket parent_socket = createParentSocket();
		writeThread.writeCreditRequest('\002' + request + '\003' + "\n",parent_socket);
	*/	
		if ( log.isDebugEnabled())
		{
			log.debug(" request got in getItemAuth     ::" + request);
			log.debug(" Getting item Bucket connector" + request);
			
		}
		int timeout = getItemAjbResTimeOutSec();
		//AJBSocketConnector connector = new AJBSocketConnector(hostname, port, timeout);
		//AJBSocketConnector connector = new AJBSocketConnector();
		AJBSocketConnector connector = AJBSocketConnector.getInstance();
		String result = "0";
		Socket itmsrvcsocket = getItemServiceSocket();
		if (itmsrvcsocket != null)
		{
			itmsrvcsocket.setSoTimeout(timeout * 1000);
			result = connector.sendCustomerInteractiveMessage('\002' + request + '\003' + "\n" ,itmsrvcsocket);
		// Sending default Response - Ignoring the response from AJB. 
		 //result = "0"; 
		}
		return (result);
	}

	public void initiateItemSocket() throws IOException
	{
		int timeout = getItemAjbResTimeOutSec();
		try
		{
			if (itembucketsocket != null)
			{
				log.info("Closing the Item service socket");
				itembucketsocket.close();
			}
		}
		catch (IOException e)
		{
			log.error("Error while receiving closing the Item service socket : " + e.getMessage());
		}
		
		itembucketsocket = null;
		try {
		
			log.info("Initiating new socket for Item Serivce");
			InetAddress address = InetAddress.getByName(hostname);
			//itembucketsocket = new Socket(hostname, port);
			itembucketsocket = new Socket(address, port);
			//itembucketsocket.setSoTimeout(( 125 * 1000));
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}	
	
	private Socket getItemServiceSocket() throws IOException
	{
		/*if (itembucketsocket  == null)
		{
			initiateItemSocket();
		}*/
		return itembucketsocket;
	}
	
	/**
	 * This method indicates whether the connection needs to be reset.
	 * 
	 * @return boolean which indicates whether to reset connections
	 */
	public synchronized boolean getIsResetConnections() {
		return isResetConnections;
	}

	/**
	 * This method returns the encoded Key within message
	 * 
	 * @param message
	 *            String containing credit request/reply
	 * @return String containing key for message
	 */
	public static String getKey(String message) {
		String[] fields = message.split(",");
		String command = fields[0];
		//Vivek Mishra : Added due to difference in Void response field in case of success and failure 
	//	String stxnType = fields[9];
	//	String ftxnType = fields[5];
		//Ends
		String key = null;
		//Vivek Mishra : Added condition for checking SAF request(111).  
		if ((command.equals(AJBMessageCodes.IX_CMD_REQUEST.getValue()))
				|| (command.equals(AJBMessageCodes.IX_CMD_RESPONSE.getValue()))
				|| (command.equals(AJBMessageCodes.IX_CMD_PROMPT_RESPONSE
						.getValue()))
				|| (command.equals(AJBMessageCodes.IX_CMD_PROMPT_REQUEST
						.getValue())) || (command.equals(AJBMessageCodes.IX_CMD_SAF_REQUEST
								.getValue()))) {
			//Vivek Mishra : Added due to difference in Void response field in case of success and failure
			//if (txnType.equals("Void"))
			//Vivek Mishra : Changed the key field to field 17 in order to stop using field 2 which is a reserved field for AJB as per Enrique on 31-AUG-2015 
			//if (stxnType=="VoidSale"||stxnType=="VoidRefund"||ftxnType=="Void")//Ends
			if(fields[0].equals("150") || fields[0].equals("151"))
				key = fields[12];
			else	
			key = fields[16];
		/*else
				key = fields[1];*/
		} else {
			key = null;
		}
		return key;
	}


	/**
	 * This method sets the indicator to reset connections
	 * 
	 * @param reset
	 *            Indicates whether to reset connections
	 */
	public static synchronized void setIsResetConnections(boolean reset) {
		log.info("AJBServiceManager.setIsResetConnections() being called.");
		isResetConnections = reset;
	}

	@Deprecated
	/**
	 * This method initializes all the parent socket connections to the AJB
	 * server. Then it creates 
	 * the read and write threads, and the threads which
	 * obtain the credit responses.
	 */
	private void setup() throws Exception 
	{
		if (getErrorMessage() != null) 
		{
			return;
		}
		log.info("AJBServiceManager.setup() being called.  Socket being created at host:port.");
		setup_configuration();
		//Added for simulator
		/*if (isFipaySimulator())
		{
			return;
		}*/
		//Ends
		// Fetching the current host from AJBPingThread
		// added for garbage collection 
		/*
		if (!isFipaySimulator())
		{
			parentSocket = null;
			parentSocket = new Socket(getCurrentHost(), port);
		}
		else
		{
			parentSocket = pingThread.getParent();
			
			
		}
		
		*/
		/*if (isFipaySimulator()) {

			parentSocket = pingThread.getParent();

		} else {

			parentSocket = null;
			parentSocket = new Socket(getCurrentHost(), port);

		}
		*/
		parentSocket.setSoTimeout(1000); // 1 seconds
		log.info("AJBServiceManager.setup() parent socket is successfulyy connected to server."+parentSocket.isConnected());
		readThread = null;
		readThread = new AJBReadThread(parentSocket, max_retries, max_wait);
		log.info("AJBServiceManager.setup() AJBReadThread class on parent socket past creation.");
		writeThread = null;
		writeThread = new AJBWriteThread(parentSocket, max_retries, max_wait);
		log.info("AJBServiceManager.setup() AJBWriteThread class on parent socket past creation.");
		if (debug.equals("TRUE")) {
			log.info("CMS Credit Services AJBServiceManager.setup()Thread: read/write threads created");
		}
		queue = new AJBQueue(max_wait, max_retries, readThread);
		log.info("AJBServiceManager.setup() AJBQueue class on parent socket past creation.");
		threadQueue = new Thread[threadMax];
		for (int i = 0; i < threadMax; i++) {
			threadQueue[i] = new Thread(this);
			threadQueue[i].start();
		}
		if (debug.equals("TRUE")) {
			log.info("CMS Credit Services AJBServiceManager.setup()Thread: queue threads created");
		}
		log.info("Connection has been setup.");
	}

	/**
	 * This method initilizes the configuration setup
	 */
	private void setup_configuration()
	{
		if( log.isDebugEnabled())
		{
			log.debug("Set up config parameter");
		}
		
		String fileName = "credit_auth.cfg";
		String storeSpecificFileName = "store_custom.cfg";

		config = new ConfigMgr(fileName);
		storeCustomConfig = new ConfigMgr(storeSpecificFileName);

		hostname = config.getString("SERVER_HOSTNAME");		
		port = config.getInteger("SERVER_PORT").intValue();
		max_wait = storeCustomConfig.getInteger("MAX_WAIT").intValue();
		max_retries = storeCustomConfig.getInteger("MAX_RETRIES").intValue();
		threadMax = storeCustomConfig.getInteger("MAX_THREAD").intValue();
		//setAjbResTimeOutSec( config.getInteger("AJB_RESPONSE_TIME_OUT").intValue());
		getAjbResTimeOutSec();
		debug = config.getString("VERBOSE_MODE").toUpperCase();
		
		if ( debug.equals("TRUE") || log.isDebugEnabled() ) {
			log.info("CMS Credit Services AJBServiceManager:  configFile = "
							+ fileName);
			log.info("CMS Credit Services AJBServiceManager:  host = "
							+ hostname);
			log.info("CMS Credit Services AJBServiceManager:  port = "
							+ port);
			log.info("CMS Credit Services AJBServiceManager:  max_wait = "
							+ max_wait);
			log.info("CMS Credit Services AJBServiceManager:  max_retries = "
							+ max_retries);
			log.info("CMS Credit Services AJBServiceManager:  ajbResTimeOutSec = " 
							+ ajbResTimeOutSec);
		}
	}

	@Deprecated
	/**
	 * This methods shuts down the former connection then it attempts to
	 * re-connect to the AJB Server.
	 * 
	 * @param host
	 *            Hostname of machine to connect to
	 * @param pport
	 *            Port number to connect to
	 * @param retries
	 *            Maximum number of retries for reads
	 * @param mwait
	 *            Maximum number of milliseconds to wait
	 */
	public void reconnect(String host, int pport, int retries, int mwait)
			throws Exception {
		log.info("AJBServiceManager.reconnect() being called.");
		// suspendThreads();
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
		//added for garbage collector
		parentSocket = null;
		//parentSocket = new Socket(getCurrentHost(), port);
		readThread.reconnect(parentSocket, max_retries, max_wait);
		log.info("AJBServiceManager.setup() AJBReadThread class reconnect method parent socket past creation.");
		writeThread.reconnect(parentSocket, max_retries, max_wait);
		log.info("AJBServiceManager.setup() AJBWriteThread class reconnect method parent socket past creation.");
		log.info("Reconnection has been completed.");
		// resumeThreads();
	}

	/**
	 * This is the run method, it keeps the server running.
	 */
	public void run() {
		log.info("AJBServiceManager.run() being called.");
		while (true) {
			try {
				if (getIsResetConnections()) {
					try_reconnect();
				}
				Thread.sleep(max_wait);
			} catch (Exception e) {
				log.error(e.toString());
			}
		}
	}

	@Deprecated
	// /**
	// * This method resumes the threads in the threadQueue
	// */
	// private void resumeThreads () {
	// System.out.println("AJBServiceManager.resumeThreads() being called.");
	// for (int i = 0; i < threadMax; i++)
	// threadQueue[i].resume();
	// }
	// /**
	// * This method suspends the threads in the threadQueue
	// */
	// private void suspendThreads () {
	// System.out.println("AJBServiceManager.suspendThreads() being called.");
	// for (int i = 0; i < threadMax; i++)
	// threadQueue[i].suspend();
	// }
	/**
	 * Attempt to reconnect to the AJB server
	 */
	private void try_reconnect() {
		log.info("AJBServiceManager.try_reconnect() being called for max_retries times.");
		int i = 0;
		while (i < max_retries) {
			try {
				//reconnect(getCurrentHost(), port, max_retries, max_wait);
				break;
			} catch (Exception e) {
				if (debug.equals("TRUE")) {
					log.info("CMS Credit Service AJBServiceManager.try_reconnect(): reconnect failed exception = "
									+ e);
				}
				LoggingServices.getCurrent().logMsg(getClass().getName(),
						"AJBServiceManager()",
						"Exception occured while reconnecting: " + e,
						"Verify AJB Server is running",
						LoggingServices.CRITICAL);
				log.error(e.toString());
				i++;
			}
			// Sleep before retrying
			try {
				Thread.sleep(max_wait);
			} catch (Exception e) {
				log.error(e.toString());
			}
		}
		setIsResetConnections(false);
	}

	// This method returns the error message from AJBPingThread.
	public String getErrorMessage()
	{
		error_message = pingThread.error_message;
		
		//Vivek Mishra : Added to perform reconnection if ping thread was failed in the last run.
		//if(pingThread.prev_error_message!=null)
		if(pingThread.isResetConnections())
		{
			log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Connection reste is true XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			setIsResetConnections(true);
			try_reconnect();
			log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX IsResetConnections : "+isResetConnections+" XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			pingThread.prev_error_message = null;
		}
		if(error_message != null)
			log.info("AJBPingThread error message : "+error_message);
		
		return error_message;
	}

	public void processConfigEvent(String[] arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static boolean isFipaySimulator()
	{

		try
		{
			System.out.println("isFipaySimulator");
			String isFipaySimulator = config.getString("FIPAY_SIMULATOR");
			if ( isFipaySimulator != null && isFipaySimulator.equalsIgnoreCase("true") && Boolean.valueOf(isFipaySimulator))
			{
				return true;
			}
		}catch(Exception e)
		{
			log.error(e);
		}
		return false;
	
		/*
		String isFipaySimulator = config.getString("FIPAY_SIMULATOR");
		if ( isFipaySimulator != null && isFipaySimulator.equalsIgnoreCase("true"))
		{
			if (Boolean.valueOf(isFipaySimulator))
			{
				System.out.println("It is working.....");
			}
			
			return true;
		}
		return false;*/
	}
	
	private static String readSimulatedResponse()
	{
		BufferedReader br = null;
		String response = null;
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("C:\\response.txt"));

			sCurrentLine = br.readLine();
			System.out.println(sCurrentLine);
			response =sCurrentLine;
			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return response;
	}
	
	
	public boolean isPingSuccess()
	{
		return pingThread.isPingsuccess();
	}

	public int getAjbResTimeOutSec() 
	{
		if (ajbResTimeOutSec == -1)
		{
			try 
			{
			ConfigMgr storeCustomConfig = new ConfigMgr("store_custom.cfg");
			ajbResTimeOutSec = storeCustomConfig.getInteger("AJB_RESPONSE_TIME_OUT").intValue();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e);
				// if exception set to default 
				ajbResTimeOutSec = 120;
			}
		}
		
		return this.ajbResTimeOutSec;
	}
	
	
	public int getItemAjbResTimeOutSec() 
	{
		if (ajbItemResTimeOutSec == -1)
		{
			try 
			{
			ConfigMgr storeCustomConfig = new ConfigMgr("store_custom.cfg");
			ajbItemResTimeOutSec = storeCustomConfig.getInteger("AJB_ITEM_RESPONSE_TIME_OUT").intValue();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e);
				// if exception set to default 
				ajbItemResTimeOutSec = 10;
			}
		}
		
		return this.ajbItemResTimeOutSec;
	}
	
	public void setAjbResTimeOutSec(int ajbResTimeOutSec) 
	{
		this.ajbResTimeOutSec = ajbResTimeOutSec;
	}
	
	@Deprecated
	private Socket createParentSocket()
	{
		
		try {
			parentSocket = null;
			parentSocket = new Socket(hostname, port);
			return parentSocket;

		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			log.error(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
			log.error(e.toString());
		}
		return null;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
