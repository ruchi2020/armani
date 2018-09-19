package com.ga.fs.fsbridge.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static String formatDateForXML(Date date){
		
		String sDate = null;
        String datePattern = "yyyy-MM-dd";
        String timePattern = "HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        sDate = format.format(date);
        sDate = sDate+"T";
        format = new SimpleDateFormat(timePattern);
        sDate = sDate + format.format(date);
        
        return sDate;
	}

	
	public static void main(String[] args){
		
			String d = formatDateForXML(new Date());
			System.out.println(d);
		
	}
}
