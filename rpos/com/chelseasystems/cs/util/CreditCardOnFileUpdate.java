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

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.driver.OracleTypes;

import com.isd.jec.IsdEncryptionClient;
import com.isd.jec.crypto.CryptoException;
import com.isd.jec.util.Base64Converter;

public class CreditCardOnFileUpdate {
	private Connection conn;
	private ResultSet rs = null;
	private PreparedStatement psmt = null;
	private EncryptionUtils utils = new EncryptionUtils();
	private  static String key = "8006A68A16DFD792C2842A522D749549A9006F3A52AFBCAEAE49E6AFD9004ADC091D27433193FBEBD540CD14BDC22943";
	private String custNo = null;
	private String oldAccNo = null;
	private String plainAccNo = null;
	private String newEncNo  = null;
	
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
		 
		 String query = "select ID_CT,ID_ACNT_DB_CR_CRD  from TEMP_ARM_CT_CRDB_CRD_DTL";
		 PreparedStatement pstmt = conn2.prepareStatement("UPDATE TEMP_ARM_CT_CRDB_CRD_DTL"+ "   SET STATUS='1', NEW_EN_NO = ? ,PLAIN_AC_NO = ? "+ "   WHERE ID_ACNT_DB_CR_CRD = ? AND ID_CT =? ");

	      psmt = conn.prepareStatement(query); // create a statement
	      rs = psmt.executeQuery();
	      int processBatch =0;
	      while (rs.next()) {
	         custNo = rs.getString(1);
	         oldAccNo = rs.getString(2);
	         plainAccNo = utils.decrypt(oldAccNo);
	        
	        if(!plainAccNo.equals("")){
	        	newEncNo = IsdEncryptionClient.isdencrypttohex(key.getBytes(), plainAccNo.getBytes());
	        	pstmt.setString(1, newEncNo);
	        	pstmt.setString(2, plainAccNo);
	        	pstmt.setString(3, oldAccNo);
	        	pstmt.setString(4, custNo);
	        	pstmt.addBatch();
	        	if(processBatch%100 == 0){
	        		pstmt.executeBatch();
	        		conn2.commit();
	        		pstmt.clearBatch();
	           	}
	        	
	        	
	        }
	        
	        System.out.println(custNo + "\t" + oldAccNo);
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
	
	 public static void  main(String [] args) {
			CreditCardOnFileUpdate fileUpdate = new CreditCardOnFileUpdate();
			fileUpdate.retriveAllCreditCardNo();
						
		}



		 
	 
}
