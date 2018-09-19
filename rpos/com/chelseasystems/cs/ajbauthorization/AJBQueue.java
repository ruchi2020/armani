package com.chelseasystems.cs.ajbauthorization;

import org.apache.log4j.Logger;


public class AJBQueue {

	public AJBQueue(int wait, int retries, AJBReadThread rt) {
		maxWait = wait;
		maxRetries = retries;
		readThread = rt;
	}

	public static String getCreditReply(String request) {
		Object result = null;
		for (int i = 0; i < maxRetries; i++) {
			String id = AJBServiceManager.getKey(request);
			result = readThread.getCreditReply(id);
			if (result != null)
				break;
		try {
				Thread.sleep(maxWait);
			} catch (InterruptedException e) {
				log.error(e.toString());
				Thread.yield();
			}
		}

		String str = "";
		if (result != null)
			str = new String(result.toString());
		return str;
	}

	private static int maxRetries;
	private static int maxWait;
	private static AJBReadThread readThread;
	/*Added to log info and errors.*/
	private static Logger log = Logger.getLogger(AJBWriteThread.class.getName());
}
