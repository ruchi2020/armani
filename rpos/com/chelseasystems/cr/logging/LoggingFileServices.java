/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   LoggingFileServices.java

package com.chelseasystems.cr.logging;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.util.Trace;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.chelseasystems.cr.logging:
//            LoggingServices, MsgRecThread, LoggingInfo

public class LoggingFileServices extends LoggingServices {

	public void setLogFile(String LogFile) {
		FileName = FileMgr.getAbsoluteFile(LogFile);
	}

	public LoggingFileServices() {
		infoList = new Vector(100, 10);
		FileName = null;
		pause = 5000L;
		loggingFile = null;
		filterLevel = 4;
		writeThread = null;
		ConfigMgr config = new ConfigMgr("logging.cfg");
		setLogFile("..\\log\\unknown.log");
		filterLevel = 4;
		writeThread = new MsgRecThread(pause, this);
		writeThread.start();
	}

	private String convertFileSeperators(String aString) {
		String sTemp = aString.replace('/', File.separatorChar);
		return sTemp.replace('\\', File.separatorChar);
	}

	public void setFilterLevel(int filterLevel) {
		if (filterLevel >= 1 && filterLevel <= 4)
			this.filterLevel = filterLevel;
	}

	public void setPause(long pause) {
		this.pause = pause;
		writeThread.setDelay(pause);
	}

	private void addInfoItem(LoggingInfo infoItem) {
		outputToConsole(infoItem);
		infoList.addElement(infoItem);
	}

	public void logMsg(LoggingInfo infoItem) {
		if (infoItem.getSeverity() > filterLevel) {
			return;
		} else {
			addInfoItem(infoItem);
			return;
		}
	}

	public void logMsg(String message) {
		if (4 > filterLevel) {
			return;
		} else {
			LoggingInfo infoItem = new LoggingInfo(message);
			addInfoItem(infoItem);
			return;
		}
	}

	public void logMsg(String message, Exception e) {
		if (4 > filterLevel) {
			return;
		} else {
			LoggingInfo infoItem = new LoggingInfo(message, e);
			addInfoItem(infoItem);
			return;
		}
	}

	public void logMsg(String className, String method, String message,
			String fix, int severity) {
		if (severity > filterLevel) {
			return;
		} else {
			LoggingInfo infoItem = new LoggingInfo(className, method, message,
					fix, severity);
			addInfoItem(infoItem);
			return;
		}
	}

	public void logMsg(String className, String method, String message,
			String fix, int severity, Exception e) {
		if (severity > filterLevel) {
			return;
		} else {
			LoggingInfo infoItem = new LoggingInfo(className, method, message,
					fix, severity, e);
			addInfoItem(infoItem);
			return;
		}
	}

	private void outputToConsole(LoggingInfo info) {
		String name = Thread.currentThread().getName();
		StringBuffer buf = new StringBuffer("[");
		buf.append(name);
		buf.append("]");
		buf.append(info.getMessage());
		if (info.isException())
			buf.append((new StringBuilder()).append(" ").append(
					info.getException()).toString());
		if (info.getSeverity() == 4) {
			Trace.out(buf.toString());
		} else {
			Trace.out(buf.toString());
			Trace.err(buf.toString());
		}
	}

	public boolean recordMsg() {
		//logMsg("@@@@inside recordMsg ");
		if (infoList.isEmpty())
			return true;
		Vector tempList = null;
		synchronized (infoList) {
			tempList = (Vector) infoList.clone();
			infoList.removeAllElements();
		}
		try {
			File dir = (new File(FileName)).getParentFile();
			if (!dir.exists())
				dir.mkdirs();
			RandomAccessFile outputFile = new RandomAccessFile(FileName, "rw");
			//logMsg("outputFile   :");
			outputFile.seek(outputFile.length());
			synchronized (tempList) {
				for (Enumeration theList = tempList.elements(); theList
						.hasMoreElements(); outputFile
						.writeBytes(currentMsgItem.getFileString()))
					currentMsgItem = (LoggingInfo) theList.nextElement();

				tempList.removeAllElements();
				currentMsgItem = null;
			}
			outputFile.close();
			return true;
		} catch (Exception e) {
			Trace.ex(e);
		}
		return false;
	}

	private Vector infoList;

	private String FileName;

	private long pause;

	private File loggingFile;

	private int filterLevel;

	private LoggingInfo currentMsgItem;

	private MsgRecThread writeThread;
}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\clientwindows1.5\US\retek\library\retek_platform.jar
	Total time: 78 ms
	Jad reported messages/errors:
Overlapped try statements detected. Not all exception handlers will be resolved in the method recordMsg
	Exit status: 0
	Caught exceptions:
*/