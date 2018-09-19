/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   LoggingServices.java

package com.chelseasystems.cr.logging;

// Referenced classes of package com.chelseasystems.cr.logging:
//            LoggingFileServices, LoggingInfo

public abstract class LoggingServices {

	public LoggingServices() {
	}

	public static void setCurrent(LoggingServices aLoggingService) {
		current = aLoggingService;
	}

	public static LoggingServices getCurrent() {
		return current;
	}

	public abstract void setFilterLevel(int i);

	public abstract void logMsg(LoggingInfo logginginfo);

	public abstract void logMsg(String s);

	public abstract void logMsg(String s, Exception exception);

	public abstract void logMsg(String s, String s1, String s2, String s3, int i);

	public abstract void logMsg(String s, String s1, String s2, String s3,
			int i, Exception exception);

	public static final int INFO = 4;

	public static final int MINOR = 3;

	public static final int MAJOR = 2;

	public static final int CRITICAL = 1;

	public static final int DEFAULT = 4;

	private static LoggingServices current = new LoggingFileServices();

}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\clientwindows1.5\US\retek\library\retek_platform.jar
	Total time: 0 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/