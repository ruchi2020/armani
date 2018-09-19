/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import java.util.*;
import java.io.*;
import java.util.zip.*;


/**
 * <p>Title: AlterationLookUpUtil</p>
 * <p>Description: Utility class to zip/unzip files</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Khyati Shah
 * @version 1.0
 */
public class ZipUtil {

  /**
   * This method is only used for testing purpose
   * @param args String[]
   */
  public static void main(String[] args) {
    String zipFileName = "c:\\temp\\file1.zip";
    String[] entries = {"c:\\temp\\file1.txt", "c:\\temp\\file2.txt", "c:\\temp\\file3.txt"
    };
    try {
      //ZipUtil.zip(zipFileName, entries);
      ZipUtil.unzip(zipFileName, "c:\\temp\\dir1");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is used to create zip file
   * @param zipFileName Name of zip file
   * @param entries Files that needs to be zipped
   * @throws Exception
   */
  public static void zip(String zipFileName, String[] entries)
      throws Exception {
    byte[] buffer;
    File zipfile = new File(zipFileName);
    FileOutputStream fos = new FileOutputStream(zipfile);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
    ZipOutputStream zos = new ZipOutputStream(bos);
    File entry;
    FileInputStream fis;
    BufferedInputStream bis;
    ZipEntry ze;
    for (int i = 0; i < entries.length; i++) {
      entry = new File(entries[i]);
      fis = new FileInputStream(entry);
      bis = new BufferedInputStream(fis);
      ze = new ZipEntry(entries[i].toString());
      ze.setMethod(ZipEntry.DEFLATED);
      zos.putNextEntry(ze);
      buffer = new byte[1024];
      int nread;
      while ((nread = bis.read(buffer)) != -1) {
        zos.write(buffer, 0, nread);
      }
      zos.closeEntry();
    }
    zos.close();
  }

  /**
   * This method is used to unzip a file
   * @param zipFileName Name of zip file
   * @param dirName Name of the directory to extract the files.
   * @throws Exception
   */
  public static void unzip(String zipFileName, String dirName)
      throws Exception {
    byte[] buffer;
    ZipEntry ze;
    InputStream is;
    FileOutputStream out;
    File file;
    ZipFile zf = new ZipFile(zipFileName);
    Enumeration en = zf.entries();
    while (en.hasMoreElements()) {
      ze = (ZipEntry)en.nextElement();
      is = zf.getInputStream(ze);
      file = new File(ze.getName());
      out = new FileOutputStream(dirName + File.separator + file.getName());
      buffer = new byte[1024];
      while (true) {
        int nread = is.read(buffer);
        if (nread < 0)
          break;
        out.write(buffer, 0, nread);
      }
      out.flush();
      out.close();
    }
  }
}

