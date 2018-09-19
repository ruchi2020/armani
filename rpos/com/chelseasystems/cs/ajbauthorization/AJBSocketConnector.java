package com.chelseasystems.cs.ajbauthorization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class AJBSocketConnector 
{
	
	private String ipAddress = null;
	
	private int port = 0;
	
	private int timeout = 0;
	
	private boolean reset = false;
	
	private Socket clientSocket = null;
	
	private static  AJBSocketConnector connector = null;
	
	/*Added to log info and errors.*/
	private static Logger logger = Logger.getLogger(AJBSocketConnector.class.getName());

	
	private AJBSocketConnector(String ipAddress, int port, int timeout)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Initializing the Connector Socket for " + ipAddress);
		}
		this.ipAddress = ipAddress;
		this.port = port;
		this.timeout = timeout ;
		
	}
	
	public static AJBSocketConnector getInstance()
	{
		if (connector == null)
		{
			AJBServiceManager srvcmnger;
			try {
			
				srvcmnger = AJBServiceManager.getCurrent();
				String host = srvcmnger.getHostname();
				int port =  srvcmnger.getPort();
				int timeout = srvcmnger.getAjbResTimeOutSec();
				connector = new AJBSocketConnector(host,port,timeout);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(" Error while creating a Socket connector  Object" + e);
			}
			
		}
		
		return connector;
	}
	
	private AJBSocketConnector()
	{
		
	}
	
	public String sendMessage(String request ) throws SocketTimeoutException, IOException
	{
		
        Socket clinetsocket = null;
        String result = null;
        BufferedWriter bw = null;
        BufferedReader dataInput = null;
        try
        {
        	//InetAddress address = InetAddress.getByName(this.ipAddress);
        	//clinetsocket = new Socket(this.ipAddress, port);
        	/*
        	 * Changing the current implementation for single socket
        	 */
        	//clinetsocket = new Socket(address, port);
        	//Setting the time out - socket wait until time out and throw a socket exception if no response in time 
        	clinetsocket = getClientSocket();
        	
        	if (clinetsocket == null)
        	{
        		throw new SocketException("Socket Error") ;
        	}
        	
        	clinetsocket.setSoTimeout(( timeout  * 1000));
            // Send the message to the server
            OutputStream os = clinetsocket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);
            
            bw.write(request, 0, request.length());
            bw.flush();
             
            logger.info("::::::::Request sent to the server  :   " + request);         
            
            
            try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				logger.error(e.toString());
			Thread.yield();
			
			}		
            
            dataInput = new BufferedReader(new InputStreamReader(clinetsocket.getInputStream()));
            
            
            if (!dataInput.ready() || clinetsocket.isInputShutdown()) {
    			System.out.println("date input is not ready");
    		}
            
            
            if ( clinetsocket.isInputShutdown()) {
    			System.out.println("input is shutdown");
    		}
            
            
            StringBuffer sb = null;
            boolean check = true;
            do
            {
            	System.out.println(" looping started.  sendMessage");
            	sb = new StringBuffer();
                char STX = '\002';
                char ETX = '\003';
                int value = 0;
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
                logger.info("AJB Response Received:" + sb);
                logger.info("Processing the response :  " + sb);
				//Vivek Mishra : Added fo 111 SAF issue fix : 20-MAY-2016
                //Anjana added 151
                //Mayuri added 107
                if (sb.toString().startsWith("101")  || sb.toString().startsWith("106") || sb.toString().startsWith("111") || sb.toString().startsWith("151")
                		|| sb.toString().startsWith("107"))
                {
				//Ends here
                	logger.info("Recieved the" +  sb.substring(0,3)+ "Response  ");
                	check = false;
                }else
                {
                	logger.info("not anticipated response.. Looping back to check for more response");
                	logger.info("Recieved the" +  sb + "Response  ");
                }
                
            }while (check);
           
            logger.info("::::::::::::::::::::::AJB Final Response : "+ sb.toString());
            result = sb.toString();
        
        } catch (SocketException e) {
			// TODO Auto-generated catch block
        	/* logger.error("Error while receiving Socket Response : " + timeout);
         	System.out.println("got time out ");
         	*/
        	setReset(true);
        	logger.error(" Error while receiving Socket Response SocketException     "+ e);
        	e.printStackTrace();
        	throw new SocketTimeoutException("Card Response Timeout") ;
			
		} catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			logger.error("SocketException     ", e);
			setReset(true);
			throw new IOException() ; 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error while reading and writing a socket", e);
			//Vivek Mishra : Added to handle no response scenario for 150 Signature Capture request 14-SEP-2016
			String[] requestFields = request.toString().split(",");
			if(requestFields[0].contains("150"))
			{
				result="";
			}
			else{//Ends here 14-SEP-2016
			setReset(true);
        	throw e ; 
			}
		} 
        finally
        {/*
            try
            {
                if (clinetsocket != null)
                {
                	//clinetsocket.close();
                }
                
                if (bw != null)
            	{
            		//bw.close();
            	}
            	
            	if ( dataInput != null )
            	{
            		//dataInput.close();
            	}
            	//clinetsocket.close();
                
            }
            catch (IOException e)
            {
                logger.error("Error while receiving Socket Response : " , e);

            }
            catch (Exception e)
            {
                logger.error("Error while receiving Socket Response : " , e);

            }
        */}

        return result;
	}
	
// Mayuri Edhara :: 12-JAN-17 - added the debit acknowledgement 

	public void sendAckMessage(String Ackrequest) throws SocketTimeoutException, IOException
	{
		
        Socket clinetsocket = null;
        BufferedWriter bw = null;
        try
        {
        	//InetAddress address = InetAddress.getByName(this.ipAddress);
        	//clinetsocket = new Socket(this.ipAddress, port);
        	/*
        	 * Changing the current implementation for single socket
        	 */
        	//clinetsocket = new Socket(address, port);
        	//Setting the time out - socket wait until time out and throw a socket exception if no response in time 
        	clinetsocket = getClientSocket();
        	
        	if (clinetsocket == null)
        	{
        		throw new SocketException("Socket Error") ;
        	}
        	
        	clinetsocket.setSoTimeout(( timeout  * 1000));
            // Send the message to the server
            OutputStream os = clinetsocket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);
            
            bw.write(Ackrequest, 0, Ackrequest.length());
            bw.flush();
             
            logger.info("::::::::Acknowledgement Request 901 sent to the server  :   " + Ackrequest);         
            
        
        } catch (SocketException e) {
			// TODO Auto-generated catch block
        	/* logger.error("Error while receiving Socket Response : " + timeout);
         	System.out.println("got time out ");
         	*/
        	setReset(true);
        	logger.error(" Error while receiving Socket Response SocketException     "+ e);
        	e.printStackTrace();
        	throw new SocketTimeoutException("Card Response Timeout") ;
			
		} catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			logger.error("SocketException     ", e);
			setReset(true);
			throw new IOException() ; 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error while reading and writing a socket", e);
		} 
        finally
        {}
	}
	
	@Deprecated
	public void sendCustomerInteractiveMessage(String request ) throws IOException
	{
        Socket clinetsocket = null;
        BufferedWriter bw = null;
        BufferedReader dataInput = null;
        try
        {
        	clinetsocket = new Socket(this.ipAddress, port);
        	clinetsocket.setSoTimeout(( timeout * 1000));
            // Send the message to the server
            OutputStream os = clinetsocket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);

            bw.write(request, 0, request.length());
            bw.flush();
            logger.info("::::::::::::::::::::::AJB Request Sent : "+ request );
          
            
            try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				logger.error(e.toString());
			Thread.yield();
			
			}		// Return if a read could be blocked
		
            dataInput = new BufferedReader(new InputStreamReader(clinetsocket.getInputStream()));
            
            
            if (!dataInput.ready()) {
    			return;
    		}
            
            StringBuffer sb = null;
            boolean check = true;
            do
            {
            	System.out.println(" looping started.  sendCustomerInteractiveMessage ");
            	sb = new StringBuffer();
                char STX = '\002';
                char ETX = '\003';
                int value = 0;
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
                logger.info("AJB Response Received:" + sb);
                logger.info("Processing the response :  " + sb);
                
                if (sb.toString().startsWith("151")  || sb.toString().startsWith("106") ||  sb.toString().startsWith("101"))
                {
                	logger.info("Recieved the" +  sb.substring(0, 3)+ "Response  ");
                	check = false;
                }else
                {
                	logger.info("not anticipated response.. Looping back to check for more response");
                }
            
            }while (check);
            
        } 
        catch (IOException e) 
        {
			
        	logger.error("Error while reading and writing a socket"+ e);
        	throw e ; 
        	
		} finally
        {
            try
            {
                if (clinetsocket != null)
                {
                	if (logger.isDebugEnabled())
                	{
                		logger.info("Closing the socket");
                	}
                	if (bw != null)
                	{
                		bw.close();
                	}
                	
                	if ( dataInput != null )
                	{
                		dataInput.close();
                	}
                	clinetsocket.close();
                }
                
            }
            catch (IOException e)
            {
                logger.error("Error while receiving Socket Response : " + e.getMessage());

            }
            catch (Exception e)
            {
                logger.error("Error while receiving Socket Response : " + e.getMessage());

            }
        }

	}
	
	public String sendCustomerInteractiveMessage(String request, Socket clinetsocket ) throws IOException
	{
        try
        {
        	/*clinetsocket = new Socket(this.ipAddress, port);
        	clinetsocket.setSoTimeout(( timeout * 1000));*/
            // Send the message to the server
            OutputStream os = clinetsocket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write(request, 0, request.length());
            bw.flush();
            logger.info("::::::::::::::::::::::AJB Request Sent : "+ request );
            BufferedReader dataInput;
            
            dataInput = new BufferedReader(new InputStreamReader(clinetsocket.getInputStream()));
           
            StringBuffer sb = null;
            boolean check = true;
            do
            {
            	System.out.println(" looping started.  sendCustomerInteractiveMessage   - with client socket ");
            	logger.info(" looping started.  sendCustomerInteractiveMessage   - with client socket ");
            	sb = new StringBuffer();
                char STX = '\002';
                char ETX = '\003';
                int value = 0;
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
                logger.info("AJB Response Received:" + sb);
                logger.info("Processing the response :  " + sb);
                
                if (sb.toString().startsWith("151")  || sb.toString().startsWith("106") ||  sb.toString().startsWith("101"))
                {
                	logger.info("Recieved the" +  sb.substring(0, 3)+ "Response  ");
                	check = false;
                }else
                {
                	logger.info("not anticipated response.. Looping back to check for more response");
                	logger.info("Recieved the" +  sb + "Response  ");
                }
            
            }while (check);
            
        }
        catch (SocketTimeoutException timeout )
        {

            logger.error("Error while receiving Socket Response : " , timeout);
        	System.out.println("got time out ");
        	return "-1";
        	//throw timeout;
        	
        }
        catch (IOException e) 
        {
			
        	logger.error("Error while reading and writing a socket", e);
        	logger.error(e);
        	return "-1";
        	//throw e ; 
        	
		} finally
        {
            try
            {
                if (clinetsocket != null)
                {
                	if (logger.isDebugEnabled())
                	{
                		//logger.info("Closing the socket");
                	}
                	//clinetsocket.close();
                }
                
            }
            /*catch (IOException e)
            {
                logger.error("Error while receiving Socket Response : " + e.getMessage());

            }*/
            catch (Exception e)
            {
                logger.error("Error while receiving Socket Response : " + e.getMessage());

            }
        }
        
        return "0";
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		clientSocket = null;
		this.reset = reset;
	}

	
	private Socket getClientSocket()
	{
		
		if (this.clientSocket == null || isReset())
		{
			InetAddress address;
			try {
				address = InetAddress.getByName(this.ipAddress);
				this.clientSocket = new Socket(address, port);
				this.clientSocket.setSoTimeout(( timeout  * 1000));
			
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				logger.error("Error while creating a clientsocket " + e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("Error while creating a clientsocket " + e);
			}
			
			reset = false;
		}
		
		
		return clientSocket; 
	}
	
}
