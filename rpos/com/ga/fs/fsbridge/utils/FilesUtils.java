/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ga.fs.fsbridge.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ga.fs.fsbridge.object.ARMFSBridgeObject;

/**
 * 
 * @author Tagbeyves
 */
public class FilesUtils {

	public static byte[] fileToByteArray(File file) {

		byte[] b = new byte[(int) file.length()];

		return b;
	}

	public static boolean stringToFile(String filePath, String content) {

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filePath));
			writer.write(content);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (writer != null)
					writer.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

	}

	public static String getNotPostedFileContent(String name) {
		 String folder = new ConfigurationManager().getConfig(
				 ARMFSBridgeObject.fsBridgeCfgFilePath, "FS_BRIDGE_NOT_POSTED_FOLDER");
		 
		 	StringBuilder contentBuilder = new StringBuilder();
		 	BufferedReader br = null;
	        try 
	        {
	        	br = new BufferedReader(new FileReader(folder+name));
	            String sCurrentLine;
	            while ((sCurrentLine = br.readLine()) != null)
	            {
	                contentBuilder.append(sCurrentLine).append("\n");
	            }
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        } finally {
	        	
	        	try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	        return contentBuilder.toString();
	}
	
	

}
