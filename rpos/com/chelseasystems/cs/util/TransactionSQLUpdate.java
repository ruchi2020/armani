package com.chelseasystems.cs.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.driver.OracleTypes;

import com.isd.jec.IsdEncryptionClient;
import com.isd.jec.crypto.CryptoException;
import com.isd.jec.util.Base64Converter;

public class TransactionSQLUpdate {
	
	private Connection conn;
	private ResultSet rs = null;
	private PreparedStatement psmt = null;
	private EncryptionUtils utils = new EncryptionUtils();
	private  static String key = "8006A68A16DFD792C2842A522D749549A9006F3A52AFBCAEAE49E6AFD9004ADC091D27433193FBEBD540CD14BDC22943";
	private String transID = null;
	private String oldAccNo = null;
	private String plainAccNo = null;
	private String newEncNo  = null;
	private String presaleID  = null;
	
   public Connection getConnection(){

	     // production connection
	     //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.201.105.130:1521:RPOSTST", "rposprd", "rposprd");
	     //local connection
	    
		try {
			 DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
			 conn = DriverManager.getConnection("jdbc:oracle:thin:@128.100.4.11:1521:armus", "armus_dev", "armus_dev");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	     return conn;
		}  
	 
	
	 public void retriveAllCreditCardNo(){

		 try{
	     ExecutionTimer timer = new ExecutionTimer();
		 timer.start();
		 Connection conn = getConnection(); 
		 Connection conn2 = getConnection(); 
		 
		 String query = "select AI_TRN,ID_ACNT_DB_CR_CRD from TEMP_TR_LTM_CRDB_CRD_TN WHERE  TO_DATE('12/2/2005','MM/DD/YYYY') <= APPROVAL_DATE AND TO_DATE('12/2/2006','MM/DD/YYYY') >= APPROVAL_DATE AND ID_ACNT_DB_CR_CRD NOT LIKE '%XXXX%'";
		 PreparedStatement pstmt = conn2.prepareStatement("UPDATE TEMP_TR_LTM_CRDB_CRD_TN"+ "   SET STATUS='1', NEW_EN_NO = ? ,PLAIN_AC_NO = ? "+ "   WHERE ID_ACNT_DB_CR_CRD = ? AND AI_TRN =?");

	      psmt = conn.prepareStatement(query); // create a statement
	      rs = psmt.executeQuery();
	      int processBatch =0;
	      while (rs.next()) {
	         transID = rs.getString(1);
	         oldAccNo = rs.getString(2);
	         plainAccNo = utils.decrypt(oldAccNo);
	        
	        if(!plainAccNo.equals("")){
	        	newEncNo = IsdEncryptionClient.isdencrypttohex(key.getBytes(), plainAccNo.getBytes());
	        	pstmt.setString(1, newEncNo);
	        	pstmt.setString(2, plainAccNo);
	        	pstmt.setString(3, oldAccNo);
	        	pstmt.setString(4, transID);
	        	
	        	pstmt.addBatch();
	        	if(processBatch%100 ==0){
	        		pstmt.executeBatch();
	        		conn2.commit();
	        		pstmt.clearBatch();
	           	}	
	        }
	        
	        
	        System.out.println(transID + "\t" + oldAccNo);
	        processBatch++;
	      }
	      timer.end();
	      System.out.println("time of execution "+ timer.duration());
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      try {
	        rs.close();
	        psmt.close();
	        conn.close();
	      } catch (SQLException e) {
	        e.printStackTrace();
	      }
	    }
	 	 
	 }
	
	
	
	
	
	
	
	
	public String[] generateDateTimeQuery(){
		String[] dateTimeQuery = null;
		//create the 4 months executtion from 2005 to 2010
		Date oldDate  = new Date();
		Date newDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		oldDate.setDate(01);
		oldDate.setMonth(01);
		oldDate.setYear(2005);
		dateFormat.format(oldDate);
		Date date6MonthsFromNow = new Date(oldDate.getTime() + (182*24*60*60*1000));
		System.out.println(date6MonthsFromNow);
	
		
		
		
		
		
		
		
		return dateTimeQuery ;
	}
	
	
	
	 public static void  main(String [] args) {
		 TransactionSQLUpdate fileUpdate = new TransactionSQLUpdate();
			fileUpdate.generateDateTimeQuery();
						
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	private Connection conn, conn2;
	private ResultSet rs = null;
	private PreparedStatement psmt = null;
	private EncryptionUtils utils = new EncryptionUtils();
	private  static String key = "8006A68A16DFD792C2842A522D749549A9006F3A52AFBCAEAE49E6AFD9004ADC091D27433193FBEBD540CD14BDC22943";
	private String transID = null;
	private String oldAccNo = null;
	private String plainAccNo = null;
	private String newEncNo  = null;
	private CallableStatement stmt = null;
	private int selectFlag = 0;
	private int updateFlag = 0;
	
	
	 public Connection getConnection(){

	     // production connection
	     //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.201.105.130:1521:RPOSTST", "rposprd", "rposprd");
	     //local connection
	    
		try {
			 DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
			 conn = DriverManager.getConnection("jdbc:oracle:thin:@128.100.4.11:1521:armus", "armus_dev", "armus_dev");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	     return conn;
		}  
	 
	 
	 public void retriveAllCreditCardNo(){
		 BufferedWriter bufferedWriter = null;
		 try{
			 	ExecutionTimer t = new ExecutionTimer();
			 	t.start();
		    	conn = getConnection();
		    	conn2 = getConnection();
				stmt = conn.prepareCall("BEGIN  GETTRANSACTIONCC(?); END;");
				stmt.registerOutParameter(1, OracleTypes.CURSOR); //REF CURSOR
  			    PreparedStatement pstmt = conn2.prepareStatement("UPDATE TEMP_TR_LTM_CRDB_TN "+ "   SET STATUS='1', NEW_EN_NO = ? ,PLAIN_AC_NO = ? "+ "   WHERE ID_ACNT_DB_CR_CRD = ? AND AI_TRN =?");

				stmt.execute();
				rs = ((OracleCallableStatement)stmt).getCursor(1);
				int processBatch = 0;
				bufferedWriter = new BufferedWriter(new FileWriter("C:/TR_CRDB_UpdateFinalData.txt"));
				 while (rs.next()) {
			         transID = rs.getString(1);
	 	             oldAccNo = rs.getString(2);
			         plainAccNo = utils.decrypt(oldAccNo);
			        
			        if(!plainAccNo.equals("")){
			        	newEncNo = IsdEncryptionClient.isdencrypttohex(key.getBytes(), plainAccNo.getBytes());
			        	bufferedWriter.write(newEncNo+"$"+plainAccNo+"$"+oldAccNo+"$"+transID);
			        	bufferedWriter.newLine();
			          }
			        System.out.println(transID + "\t" + oldAccNo);
			        processBatch++;
			      	 }
				t.end();
				System.out.println("time duration :" + t.duration());
				   
			} catch (Exception e) {
				selectFlag++;
				 System.out.println("Closing all connection and restarting the Connections");
			     System.out.println("Please Wait...");
			     System.out.println("Attemping for 3 times");
			        if(selectFlag<=3){
			        	 retriveAllCreditCardNo();
			        }
			        else{
			        	System.out.println("Please run the process once again falied...");
			        	System.exit(0);
			        }
	      e.printStackTrace();
	    } finally {
	      try {
	    	bufferedWriter.close();
	        psmt.close();
	        conn.close();
	       
	      } catch (SQLException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	 }
	
	 public void updateTable(){

		 try{
			 	ExecutionTimer t = new ExecutionTimer();
			 	t.start();
		    	conn = getConnection();
				stmt = conn.prepareCall("BEGIN  GETTRANSACTIONCC(?); END;");
				stmt.registerOutParameter(1, OracleTypes.CURSOR); //REF CURSOR
  			    PreparedStatement pstmt = conn.prepareStatement("UPDATE TEMP_TR_LTM_CRDB_TN "+ "   SET STATUS='1', NEW_EN_NO = ? ,PLAIN_AC_NO = ? "+ "   WHERE ID_ACNT_DB_CR_CRD = ? AND AI_TRN =?");
				int processBatch = 0;
				FileInputStream fstream = new FileInputStream("C:/TR_CRDB_UpdateFinalData.txt");
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				 while ((strLine = br.readLine()) != null)   { //104*1*0|ITFo3SWaxmg=|2011-11-10 00:00:00.0|null
				     String temp []  = strLine.split("_");
					 newEncNo = temp[0];
					 plainAccNo = temp[1];
					 oldAccNo = temp[2];
					 transID = temp[3];
				    if(!plainAccNo.equals("")){
			        	pstmt.setString(1, newEncNo);
			        	pstmt.setString(2, plainAccNo);
			        	pstmt.setString(3, oldAccNo);
			        	pstmt.setString(4, transID);
			        	pstmt.addBatch();
			        	if(processBatch%1000 ==0){
			        		pstmt.executeBatch();
			        		conn.commit();
			        		pstmt.clearBatch();
			           	System.out.println(" batch update at ----------------------------------"+processBatch);
			           	System.out.println("DATE TIME "+ new Date());
			        	}
			        	
			        	
			        }
			        
			        System.out.println(transID + "\t" + oldAccNo);
			        processBatch++;
			     
				 }
				 br.close();
				t.end();
				System.out.println("time duration :" + t.duration());
				   
			} catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      try {
	        psmt.close();
	        conn.close();
	        retriveAllCreditCardNo();
	        updateTable();
	      } catch (SQLException e) {
	        e.printStackTrace();
	      }
	    }
	 
	 }
	 
	 
	



		 
	 
*/}
