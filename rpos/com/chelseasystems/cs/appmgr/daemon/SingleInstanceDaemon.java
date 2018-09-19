/**
 * 
 */
package com.chelseasystems.cs.appmgr.daemon;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IBrowserManager;
import com.chelseasystems.cr.appmgr.daemon.Daemon;
import com.chelseasystems.cr.config.ConfigMgr;

/**
 * @author vikram
 * @version 1.0a
 */
public class SingleInstanceDaemon extends Daemon {

	private final static int DEFAULT_PORT = 47767;
	private static SingleInstanceDaemon instance;

	private ServerSocket serverSocket;
	private int port = DEFAULT_PORT;

	/**
	 * 
	 */
	public SingleInstanceDaemon() {
		this(null);
	}

	/**
	 * 
	 */
	protected SingleInstanceDaemon(IBrowserManager theMgr) {
		super();

		if (instance != null)
			return;
		instance = this;

		//if(theMgr != null)
		this.theMgr = theMgr;

		try {
			ConfigMgr config = new ConfigMgr(System.getProperty("USER_CONFIG"));
			port = config.getInt("EXCLUSIVE_CLIENT_PORT");
		} catch (NumberFormatException e) {
			System.out.println("ERROR: Illegal value for EXCLUSIVE_CLIENT_PORT in " + System.getProperty("USER_CONFIG"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (port <= 0)
			port = DEFAULT_PORT;

		try {
			serverSocket = new ServerSocket(port, 1);
		} catch (BindException e) {
			//e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SingleInstanceDaemon getInstance(IBrowserManager theMgr) {
		if (instance != null)
			return instance;
		else
			return new SingleInstanceDaemon(theMgr);
	}

	/* (non-Javadoc)
	 * @see com.chelseasystems.cr.appmgr.daemon.Daemon#getKey()
	 */
	public Object getKey() {
		return this.getClass().getName();
	}

	/* (non-Javadoc)
	 * @see com.chelseasystems.cr.appmgr.daemon.Daemon#run()
	 */
	public void run() {
		if (this != instance)
			return;

		if (serverSocket != null) {
			while(true) {
				try {
					Socket socket = serverSocket.accept();
					//do nothing...
					socket.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		} else {
			showErrorDialogAndExit();
		}
	}

	private void showErrorDialogAndExit() {
		IApplicationManager theAppMgr = (IApplicationManager) theMgr;
		ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		//String errorStr = "Port " + port + " in use.\r" + res.getString("An instance of RPOS may already be running.\rSystem would exit.");
		String errorStr = res.getString("An instance of RPOS may already be running. System would exit.");
		System.out.println("ERROR: " + errorStr);
		/*try {
		 if(theAppMgr != null) {
		 theAppMgr.showErrorDlg(errorStr);
		 }
		 else {
		 System.out.println("ERROR: " + errorStr);
		 }
		 } catch (Exception e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }*/
		System.out.flush();
		System.exit(-1);
	}

}
