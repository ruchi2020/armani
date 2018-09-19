package com.chelseasystems.cs.ajbauthorization;

/**
 * @author Vivek M
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.Logger;

import com.armani.sync.customerLoaderManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;

/**
 * 
 * As per AJB's requirement POS should send 106 Ping massage to AJBServer after
 * a particular time interval. This class is the Ping Thread which gets started
 * with server startup from CMSContainer. It initiates the normal Socket
 * connection with AJB server then, send the 106 Ping message and waits for the
 * response. If no response received within give time limit then sets error
 * message otherwise sets the host name(as there is a provision of having
 * multiple AJB severs) of the AJBsever from where it received the response. The
 * same host gets used for further communication.
 */

public class AJBPingThread{

	/* Turns debugging information on or off */
	private static String debug = "FALSE";

	/* Instance of ConfigMgr to obtain current configuration parameters */
	private static ConfigMgr config;

	/* Instance of ConfigMgr to obtain store configuration parameters */
	private static ConfigMgr storeCustomConfig;

	/* SSLSocket connected to the AJB Server */
	private Socket parentSocket;

	/* SSLSocketfactory */
	SSLSocketFactory sslsocketfactory;

	/* Hosts where the AJB server runs */
	String[] hosts;

	/* Hostnames where the AJB server runs */
	private String hostnames;

	/* Port number of ISD Server, normally 4243 */
	private int port;

	/* Location of the SSL certificate */
	private String sslCerLoc;

	/* Password of the SSL certificate */
	private String sslCerPass;

	/* Maximum number to retry */
	private int max_retries;

	/* Maximum number of milliseconds to wait */
	private int max_wait;

	/* Ping interval in milliseconds */
	private int ping_interval;

	/* Indicates whether connections to AJB server is already established or not */
	private static boolean isConnected = false;

	/* Indicates whether connections to ISD server need resetting */
	private static boolean isResetConnections = false;

	private static AJBPingThread pingThread;

	/* Ping response max wait in milliseconds */
	private int ping_wait;

	/* Current host to which Ping is getting send */
	public String current_host;

	/* Error message */
	public String error_message;
	
	/* Error message */
	public String prev_error_message;
	
	/* Store Id */
	private String storeId;
	
	/* Register Id */
	private String registerId;
	
	private boolean pingsuccess = false;

	private int ajbResTimeOutSec;
	
	/*Added to log info and errors.*/
	private static Logger log = Logger.getLogger(AJBPingThread.class.getName());
	
	public static boolean  success106 = false;
	
	// Zero parameterize Constructor for AJBPingThread which calls the initConfig method in turn.
	public AJBPingThread() {
		initConfig();
	}

	// Two parameterize Constructor for AJBPingThread which calls the initConfig method in turn.
	public AJBPingThread(String storeId, String registerId) 
	{
		this.storeId = storeId;
		this.registerId = registerId;
		initConfig();
		
		//checksimulator();

	}
	// This method initializes all the properties required to make a Socket
	// connection with AJB Server. It reads those properties from
	// credit_auth.cfg and store_custom.cfg
	public void initConfig()
	{
		
		log.info("Initialising config parmater");
		
		ServerSocket serverSocket = null;
		System.out.println("inside the initConfig");
		config = new ConfigMgr("credit_auth.cfg");
		storeCustomConfig = new ConfigMgr("store_custom.cfg");

		hostnames = config.getString("SERVER_HOSTNAME");
		hosts = hostnames.split(",");
		port = config.getInteger("SERVER_PORT").intValue();
		ping_interval = storeCustomConfig.getInteger("PING_INTERVAL").intValue();
		ping_wait = storeCustomConfig.getInteger("PING_RESPOSE_MAX_WAIT").intValue();
		ajbResTimeOutSec = getAjbResTimeOutSec();
		//Vivek Mishra : Needs to be uncommented if there is no 106 ping required before sending 150 item bucket message.
		current_host = hosts[0];

		if(isFipaySimulator())
		{
			System.out.println("inside the isFipaySimulator condition - true");
			serverSocket = runSimulatedServerSocket();
		}
		//connectCurrenthost();
		
		if(isFipaySimulator())
		{
			try {
				Socket clientSocket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
				e.printStackTrace();
				// if exception set to default 
				ajbResTimeOutSec = 120;
			}
		}
		
		return this.ajbResTimeOutSec;
	}
	// This method is responsible for sending 106 Ping request to the AJB
	// Server.
	public boolean sendMessage()
	{
		//Vivek Mishra : Added to keep track of previous ping thread status if it got failed in last run. 
		
		//sendMessage("106,,,0,"+ping_wait+",,,"+storeId+","+registerId+",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
		String request = '\002'+"106,,,0,"+ping_wait+",,,"+storeId+","+registerId+",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"+'\003' ;
		log.info("106 Ping has been sent::"+ '\002'+"106,,,0,"+ping_wait+",,,"+storeId+","+registerId+",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"+'\003');
	
		if(isFipaySimulator())
		{
			return true;
		}
		
		try {
			if (parentSocket != null && parentSocket.isConnected())
			{
				parentSocket.getOutputStream().write(request.getBytes());
				return true;
			}
			else 
				return false;
			
		} catch (IOException e) {
			log.error(e.toString());
		}
		return false;
	}

	// This method is responsible for receiving 106 ping response from the AJB
	// Server
	public boolean recieveMessage()
	{

		try {
			
			if(isFipaySimulator())
			{
				success106 = true;
				return success106;
			}
			
			parentSocket.setSoTimeout(15*1000);
			BufferedReader dataInput = new BufferedReader(
					new InputStreamReader(parentSocket.getInputStream()));			
			StringBuffer sb = new StringBuffer();
			char STX = '\002';
			char ETX = '\003';
			int value = 0;
		    //According to CAFipay guide we are making the change
			
			/*if(dataInput.toString().length()>0){
				success106 = true;
			}*/
			
			
			if(dataInput.toString().length()>0)
			{
				//Vivek Mishra : Commented to fix Ping thread hang issue.
				while ((value = dataInput.read()) != -1) {
					char c = (char) value;
					System.out.print(c);
					if (c == STX)
						continue;
					// end? jump out
					if (c == ETX) {
						break;
					}
					sb.append(c);
					
				}
				log.info("::::::::::::::::::::::::::::106 Response received : "+sb.toString()+":::::::::::::::::::::::::::::::::::");
				
				String[] response = sb.toString().split(",");
				
				if (response != null && response.length > 0)
				{
					if ( response[0].equalsIgnoreCase("106"))
					{
						//Ends
						success106 = true;
						return success106;
					}
					else
					{
						//Ends
						success106 = false;
						return success106;
					}
				}
				
			}
			
			
			//Vivek Mishra : Commented to fix Ping thread hang issue.
			/*while ((value = dataInput.read()) != -1) {
				char c = (char) value;
				
				if (c == STX)
					continue;
				// end? jump out
				if (c == ETX) {
					break;
				}
				sb.append(c);
			}*/
		} catch (IOException e)
		{
			log.error(e.toString());
			success106 = false;
			return success106;
		}
		return false;}
	
	private boolean iscurrentParenthostconnnected()
	{
		log.info("DEBUG-PROD-ISSUE::::parentSocket is : "+parentSocket);
		if (this.parentSocket  != null )
		{
			return parentSocket.isConnected();
			
		}
				
		return false;
	}
	
	// This method does the connection with one of the ALB server which is up at
	// the moment. If no server is up the it sets the isConnected flag to false.
	public Socket connect()
	{
		for (int i = 0; i < hosts.length; i++)
		{
			try {
				//Vivek Mishra : Added in order to avoid null current host.
				log.info("DEBUG-PROD-ISSUE::::parentSocket initialized");
				current_host = hosts[i];
				parentSocket = new Socket(hosts[i], port);
				isConnected = true;
				//current_host = hosts[i];
				return parentSocket;
			} catch (UnknownHostException e) {
				System.out.println(e.toString());
				log.error(e.toString());
				continue;
			} catch (IOException e) {
				System.out.println(e.toString());
				log.error(e.toString());
				continue;
			}
		}
		//isConnected = false;
		return null;
	}
	
	private Socket connectCurrenthost() {
		
		try {
			log.info("DEBUG-PROD-ISSUE::::parentSocket initialized");
			parentSocket = null;
			parentSocket = new Socket(current_host, port);
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
	
	private boolean try_reconnect() 
	{	
		boolean reconnect = false;
		log.info("Trying to reconnect to AJB");
		int i = 0;
		while (i < max_retries) 
		{
			try {
					if ( connectCurrenthost()!= null)
					{
						reconnect = true;
						break;
					}
					
			} catch (Exception e) 
			{
				if (debug.equals("TRUE")) {
					log.info("CMS Credit Service AJBServiceManager.try_reconnect(): reconnect failed exception = "
									+ e);
				}
				LoggingServices.getCurrent().logMsg(getClass().getName(),
						"AJBPingThread()",
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
		return reconnect;
	}
	// Method which returns the value of isConnected flag.
	public boolean isConnected() {
		if (this.parentSocket != null) {
			//return isConnected;
			return parentSocket.isConnected();
		}
		return false;
	}

	// AJBPing Therad's run method which calls the connect method in turn. It
	// checks the isConnected flag, if it is true then it sends the ping message
	// to the AJBServer and receive the response. If the flag is false then is
	// sets the error message.
	//Vivek Mishra : Changed the implementation of Ping Thread as it is no longer required to run as a thread 29-OCT-2015 status call.
	public boolean ping() 
	{
		log.info("DEBUG-PROD-ISSUE::::Ping method called");
			if(error_message!=null)
				prev_error_message=error_message;
			
			error_message = null;
			setPingsuccess(false);
			
			Boolean.valueOf(config.getString("FIPAY_SIMULATOR"));
			String isfipaysimulator = config.getString("FIPAY_SIMULATOR");
			
			if (isfipaysimulator != null && isfipaysimulator.equalsIgnoreCase("true")  && Boolean.valueOf(isfipaysimulator) )
			{
				setPingsuccess(true);
				//return true;
			}
			
			//connect();
			
			if (iscurrentParenthostconnnected())
			{
				
				if ( sendMessage())
				{
					if(!recieveMessage()){
						setPingsuccess(false);
						error_message = "All the Ajb Servers are down at the moment.";	
					} 
					else{
						setPingsuccess(true);
						return true;
					}
				}
				else 
				{
					
					setPingsuccess(false);
					error_message = "All the Ajb Servers are down at the moment.";	
			
				}
					
				
			} else {
				
				if (try_reconnect())
				{
					
					if (iscurrentParenthostconnnected())
					{
						sendMessage();
						recieveMessage();
						setResetConnections(true);
						if(!recieveMessage()){
							setPingsuccess(false);
							error_message = "All the Ajb Servers are down at the moment.";	
						}
						else{
							setPingsuccess(true);
							return true;
						}
					}
				}
				else{
					error_message = "All the Ajb Servers are down at the moment.";
					setPingsuccess(false);
					return false;
				}
					
				
				
				/*if (connect()!=null)
				{
					setResetConnections(true);
					
					if (iscurrentParenthostconnnected())
					{
						sendMessage();
						recieveMessage();
						if(!recieveMessage()){
							error_message = "All the Ajb Servers are down at the moment.";	
						}
						else
							return true;
					}
					
				} else
				{
					//error_message = "All the Ajb Servers are down at the moment.";
					return false;
				}*/
			}
			return false;
	}
	
	public boolean echoping()
	{
		/*Boolean.valueOf(config.getString("FIPAY_SIMULATOR"));
		String isfipaysimulator = config.getString("FIPAY_SIMULATOR");
		
		if (isfipaysimulator != null && isfipaysimulator.equalsIgnoreCase("true")  && Boolean.valueOf(isfipaysimulator) )
		{
			setPingsuccess(true);
		}*/
		
		//AJBSocketConnector connector = new AJBSocketConnector(current_host, port, ping_wait );
		AJBSocketConnector connector = AJBSocketConnector.getInstance();
		String request = '\002'+"106,,,0,"+ping_wait+",,,"+storeId+","+registerId+",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"+'\003' ;
		//log.info("106 Ping has been sent::"+ '\002'+"106,,,0,"+ping_wait+",,,"+storeId+","+registerId+",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"+'\003');
		
		try 
		{
			String response = connector.sendMessage(request);
			
			if (response != null && response.length() >0 )
			{	
				String[] response_array = response.toString().split(",");
				if (response != null && response_array.length > 0)
				{
					if ( response_array[0].equalsIgnoreCase("106"))
					{
						success106 = true;
						return success106;
					}
					else
					{
						success106 = false;
						return success106;
					}
				}
			} 
		
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		return false ;
		
	}
	
	public static boolean isResetConnections() {
		return isResetConnections;
	}

	public static void setResetConnections(boolean isResetConnections) {
		AJBPingThread.isResetConnections = isResetConnections;
	}

	public boolean isPingsuccess() {
		return pingsuccess;
	}

	public void setPingsuccess(boolean pingsuccess) {
		this.pingsuccess = pingsuccess;
	}
	
	
	
	private ServerSocket runSimulatedServerSocket()
	{
		ServerSocket MyService = null;
	    try {
	    	MyService = new ServerSocket(port);
	    	
	        }
	        catch (IOException e) {
	           System.out.println(e);
	        }
	    return MyService;
	}
	

	public static boolean isFipaySimulator()
	{
		try
		{
			//System.out.println("isFipaySimulator");
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
	}
	
	
	public Socket getParent()
	{
		
		return parentSocket;
	}
}
