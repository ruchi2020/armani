/*
 * @copyright (c) 1998-2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.dataaccess;

import com.chelseasystems.cr.database.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.xml.*;
import com.chelseasystems.cs.xml.*;
import com.chelseasystems.cs.xml.tags.*;
import org.w3c.dom.*;


/**
 */
public class DataLoader {
  private static boolean verbose;

  /**
   * @param verbose
   */
  public static void setVerbose(boolean verbose) {
    DataLoader.verbose = verbose;
  }

  /**
   * @param args
   * @exception Exception
   */
  public static void main(String[] args)
      throws Exception {
    long t1 = System.currentTimeMillis();
    System.out.println("Begin time: " + new java.util.Date(t1));
    if (args == null || args.length == 0) {
      System.out.println("Error: need a database type, example: oracle. Exiting ....");
      System.exit( -1);
    }
    if (args[0].equals("oracle"))
      buildOracle();
    else if (args[0].equals("arts_cloudscape"))
      buildCloudscape();
    else {
      System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      System.out.println("!!  DB type not known: " + args[0]);
      System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
    long t2 = System.currentTimeMillis();
    System.out.println("Begin time: " + new java.util.Date(t2));
    System.out.println("Time used: " + (t2 - t1) + "ms");
  }

  /**
   * @exception Exception
   */
  private static void buildCloudscape()
      throws Exception {
    String[] fileNames = getLoadingOrder("loading.txt");
    for (int i = 0; i < fileNames.length; i++) {
      System.out.println("Processing file: " + fileNames[i]);
      if (fileNames[i].equals("create_table.sql")) {
        executeSQL("arts_cloudscape", fileNames[i], START_RECORD_KEY_FOR_CREATE_TABLE);
      } else if (fileNames[i].equals("create_pk.sql")) {
        executeSQL("arts_cloudscape", fileNames[i], START_RECORD_KEY_FOR_ALTER_TABLE);
      } else if (fileNames[i].equals("create_view.sql")) {
        executeSQL("arts_cloudscape", fileNames[i], START_RECORD_KEY_FOR_CREATE_VIEW);
      } else if (fileNames[i].equals("create_chelsea_sequence.sql")) {
        executeSQL("arts_cloudscape", fileNames[i], START_RECORD_KEY_FOR_CREATE_TABLE);
      } else if (fileNames[i].equals("chelsea_system_data.sql")) {
        executeSQL("arts_cloudscape", fileNames[i], START_RECORD_KEY_FOR_INSERT_INTO);
      } else if (fileNames[i].equals("create_fk.sql")) {
        executeSQL("arts_cloudscape", fileNames[i], START_RECORD_KEY_FOR_ALTER_TABLE);
      } else if (fileNames[i].equals("drop_table.sql")) {
        try {
          executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_DROP_TABLE);
        } catch (Exception e) {}
      } else if (fileNames[i].equals("drop_chelsea_sequence.sql")) {
        try {
          executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_DROP_SEQUENCE);
        } catch (Exception e) {}
      } else if (fileNames[i].endsWith(".xml")) {
        Vector objects = getObjectsFromFile(fileNames[i]);
        String currencyCode = getCurrencyCode(fileNames[i]);
        for (int j = 0; j < objects.size(); j++)
          insertIntoDB(objects.get(j), currencyCode);
      } else {
        executeSQL("arts_cloudscape", fileNames[i], START_RECORD_KEY_FOR_INSERT_INTO);
      }
    }
  }

  /**
   * @param fileName
   * @return
   */
  private static String getCurrencyCode(String fileName) {
    if (fileName.indexOf("USD") >= 0)
      return "USD";
    if (fileName.indexOf("DEM") >= 0)
      return "DEM";
    if (fileName.indexOf("FRF") >= 0)
      return "FRF";
    if (fileName.indexOf("GBP") >= 0)
      return "GBP";
    if (fileName.indexOf("CAD") >= 0)
      return "CAD";
    return null;
  }

  /**
   * @exception Exception
   */
  private static void buildOracle()
      throws Exception {
    String[] fileNames = getLoadingOrder("loading.txt");
    for (int i = 0; i < fileNames.length; i++) {
      System.out.println("Processing file: " + fileNames[i]);
      if (fileNames[i].equals("create_table.sql")) {
        executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_CREATE_TABLE);
      } else if (fileNames[i].equals("create_pk.sql")) {
        executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_ALTER_TABLE);
      } else if (fileNames[i].equals("create_view.sql")) {
        executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_CREATE_VIEW);
      } else if (fileNames[i].equals("create_chelsea_sequence.sql")) {
        executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_CREATE_TABLE);
      } else if (fileNames[i].equals("chelsea_system_data.sql")) {
        executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_INSERT_INTO);
      } else if (fileNames[i].equals("create_fk.sql")) {
        executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_ALTER_TABLE);
      } else if (fileNames[i].equals("drop_table.sql")) {
        try {
          executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_DROP_TABLE);
        } catch (Exception e) {}
      } else if (fileNames[i].equals("drop_chelsea_sequence.sql")) {
        try {
          executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_DROP_SEQUENCE);
        } catch (Exception e) {}
      } else if (fileNames[i].endsWith(".sql")) {
        executeSQL("arts_oracle", fileNames[i], null);
      } else if (fileNames[i].endsWith(".xml")) {
        Vector objects = getObjectsFromFile(fileNames[i]);
        String currencyCode = getCurrencyCode(fileNames[i]);
        for (int j = 0; j < objects.size(); j++)
          insertIntoDB(objects.get(j), currencyCode);
      } else {
        executeSQL("arts_oracle", fileNames[i], START_RECORD_KEY_FOR_INSERT_INTO);
      }
    }
  }

  /**
   * @param dir
   * @param fileName
   * @param beginWith
   * @exception IOException, SQLException
   */
  private static void executeSQL(String dir, String fileName, String beginWith)
      throws IOException, SQLException {
    com.chelseasystems.cs.dataaccess.artsoracle.dao.EmployeeOracleDAO dao = new com.chelseasystems.
        cs.dataaccess.artsoracle.dao.EmployeeOracleDAO();
    String[] statements = createRecordList(dir, fileName, beginWith);
    for (int i = 0; i < statements.length; i++) {
      //System.out.println("ex " + statements[i]);
      String sql = statements[i].trim();
      if (sql.endsWith(";"))
        sql = sql.substring(0, sql.length() - 1);
      dao.execute(new ParametricStatement(sql, new ArrayList()));
    }
  }

  /**
   * @param dbType
   * @param fileName
   * @param startRecordKey
   * @return
   * @exception IOException
   */
  private static String[] createRecordList(String dbType, String fileName, String startRecordKey)
      throws IOException {
    List list = new ArrayList();
    File file = new File("../files/prod/database/" + dbType + "/" + fileName);
    FileReader reader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(reader);
    try {
      StringBuffer recordBuffer = null;
      String line = null;
      boolean endOfFile = false;
      while (true) {
        line = bufferedReader.readLine();
        if (line == null) {
          break;
        }
        line = line.trim();
        if (line.length() == 0)
          continue;
        if (line.startsWith("#") || (line.startsWith("--")))
          continue;
        if (line.startsWith("REM "))
          continue;
        if (recordBuffer == null)
          recordBuffer = new StringBuffer();
        recordBuffer.append(line + ' ');
        if (line.endsWith(";") || line.endsWith("/")) {
          String entry = recordBuffer.toString();
          list.add(entry);
          recordBuffer = null;
          if (verbose) {
            System.out.println(entry);
          }
        }
      }
    } finally {
      bufferedReader.close();
    }
    System.out.println("Loaded: " + file + ". Number of records: " + list.size());
    return (String[])list.toArray(new String[0]);
  }

  /**
   * @param object
   * @param currencyCode
   * @exception Exception
   */
  private static void insertIntoDB(Object object, String currencyCode)
      throws Exception {
    if (object instanceof com.chelseasystems.cr.item.Item) {
      System.out.println("Loading Item Data .. " + object);
      itemDAO.insert((com.chelseasystems.cr.item.Item)object);
    } else if (object instanceof com.chelseasystems.cr.pricing.IPromotion) {
      System.out.println("Loading Promotion Data .. " + object);
      promotionDAO.insert((com.chelseasystems.cr.pricing.IPromotion)object, currencyCode);
    } else if (object instanceof com.chelseasystems.cr.pricing.ThresholdPromotion) {
      System.out.println("Loading Threshold Promotion Data .. " + object);
      thresholdPromotionDAO.insert((com.chelseasystems.cr.pricing.ThresholdPromotion)object
          , currencyCode);
    } else if (object instanceof com.chelseasystems.cr.customer.Customer) {
      System.out.println("Loading Customer Data .. " + object);
      customerDAO.insert((com.chelseasystems.cr.customer.Customer)object);
    } else if (object instanceof com.chelseasystems.cr.store.Store) {
      System.out.println("Loading Store Data .. " + object);
      storeDAO.insert((com.chelseasystems.cr.store.Store)object);
    } else if (object instanceof com.chelseasystems.cr.goaling.StoreGoal) {
      System.out.println("Loading StoreGoal Data .. " + object);
      storeGoalDAO.insert((com.chelseasystems.cr.goaling.StoreGoal)object);
    } else if (object instanceof com.chelseasystems.cr.employee.JobCode) {
      System.out.println("Loading JobCode Data .. " + object);
      jobCodeDAO.insert((com.chelseasystems.cr.employee.JobCode)object);
    } else if (object instanceof com.chelseasystems.cr.employee.Employee) {
      System.out.println("Loading Employee Data .. " + object);
      employeeDAO.insert((com.chelseasystems.cr.employee.Employee)object);
    } else if (object instanceof com.chelseasystems.cs.inventory.Lot) {
      System.out.println("Loading Lot Data .. " + object);
      lotDAO.insert((com.chelseasystems.cs.inventory.Lot)object);
    } else {
      System.out.println("Error object " + object + ".  Exiting ...");
      System.exit( -1);
    }
  }

  /**
   * @param fileName
   * @return
   * @exception Exception
   */
  private static Vector getObjectsFromFile(String fileName)
      throws Exception {
    File file = new File("../files/prod/database/demodata/" + fileName);
    if (!file.exists()) {
      System.out.println("File " + file + " does not exist.");
      return null;
    }
    Document document = XMLUtil.getDocumentFromFile(file.getAbsolutePath());
    NodeList list = null;
    list = document.getElementsByTagName(CustomerXMLTags.customerTag);
    if (list != null && list.getLength() > 0)
      return new CustomerXML().toObjects(file.getAbsolutePath());
    list = document.getElementsByTagName(ItemXMLTags.itemTag);
    if (list != null && list.getLength() > 0)
      return new ItemXML().toObjects(file.getAbsolutePath());
    list = document.getElementsByTagName(PromotionXML.promotionTag);
    if (list != null && list.getLength() > 0)
      return new Vector(new PromotionXML().toObjects(file.getAbsolutePath()));
    list = document.getElementsByTagName(ThresholdPromotionXML.thresholdPromotionTag);
    if (list != null && list.getLength() > 0)
      return new Vector(new ThresholdPromotionXML().toObjects(file.getAbsolutePath()));
    list = document.getElementsByTagName(StoreXMLTags.storeTag);
    if (list != null && list.getLength() > 0)
      return new StoreXML().toObjects(file.getAbsolutePath());
    list = document.getElementsByTagName(GoalXMLTags.storeGoalTag);
    if (list != null && list.getLength() > 0)
      return new GoalXML().toObjects(file.getAbsolutePath());
    list = document.getElementsByTagName(EmployeeXMLTags.jobCodeTag);
    if (list != null && list.getLength() > 0)
      return new JobCodeXML().toObjects(file.getAbsolutePath());
    list = document.getElementsByTagName(EmployeeXMLTags.employeeTag);
    if (list != null && list.getLength() > 0)
      return new EmployeeXML().toObjects(file.getAbsolutePath());
    list = document.getElementsByTagName(LotXMLTags.lotTag);
    if (list != null && list.getLength() > 0)
      return new LotXML().toObjects(file.getAbsolutePath()); /*
         list = document.getElementsByTagName("ROLE");
         if (list != null && list.getLength() > 0) {
         Class clss = Class.forName("com.chelseasystems.cs.xml.RoleXML");
         return  ((XMLObject)clss.newInstance()).toObjects(file.getAbsolutePath());
         }
    */

   System.out.println("Fail in processing file: " + fileName + ". Unknown object type. Exit...");
    System.exit( -1);
    return null;
  }

  /**
   * @param fileName
   * @return
   * @exception Exception
   */
  private static String[] getLoadingOrder(String fileName)
      throws Exception {
    List list = new ArrayList();
    File file = new File("../files/prod/database/demodata/" + fileName);
    FileReader reader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(reader);
    try {
      String line = null;
      while (true) {
        line = bufferedReader.readLine();
        if (line == null)
          break;
        if (!line.trim().startsWith("#") && line.length() != 0)
          list.add(line.trim());
      }
    } finally {
      bufferedReader.close();
    }
    String[] array = (String[])list.toArray(new String[0]);
    for (int i = 0; i < array.length; i++)
      System.out.println("" + i + " == " + array[i]);
    return array;
  }

  private static ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
  private static EmployeeDAO employeeDAO = (EmployeeDAO)configMgr.getObject("EMPLOYEE_DAO");

  private static JobCodeDAO jobCodeDAO = (JobCodeDAO)configMgr.getObject("JOBCODE_DAO");

  private static EmployeeAuthInfoDAO employeeAuthInfoDAO = (EmployeeAuthInfoDAO)configMgr.getObject(
      "EMPLOYEEAUTHINFO_DAO");

  ; private static StoreDAO storeDAO = (StoreDAO)configMgr.getObject("STORE_DAO");

  ; private static CustomerDAO customerDAO = (CustomerDAO)configMgr.getObject("CUSTOMER_DAO");

  private static LotDAO lotDAO = (LotDAO)configMgr.getObject("LOT_DAO");

  private static ItemDAO itemDAO = (ItemDAO)configMgr.getObject("ITEM_DAO");

  private static PromotionDAO promotionDAO = (PromotionDAO)configMgr.getObject("PROMOTION_DAO");

  private static ThresholdPromotionDAO thresholdPromotionDAO = (ThresholdPromotionDAO)configMgr.
      getObject("THRESHOLDPROMOTION_DAO");

  private static StoreGoalDAO storeGoalDAO = (StoreGoalDAO)configMgr.getObject("STOREGOAL_DAO");

  private static final String START_RECORD_KEY_FOR_CREATE_TABLE = "CREATE TABLE";
  private static final String START_RECORD_KEY_FOR_CREATE_VIEW = "CREATE VIEW";
  private static final String START_RECORD_KEY_FOR_ALTER_TABLE = "ALTER TABLE";
  private static final String START_RECORD_KEY_FOR_INSERT_INTO = "INSERT INTO";
  private static final String START_RECORD_KEY_FOR_DROP_TABLE = "DROP TABLE";
  private static final String START_RECORD_KEY_FOR_DROP_SEQUENCE = "DROP SEQUENCE";
  private static final String END_RECORD_KEY = ";";
}


