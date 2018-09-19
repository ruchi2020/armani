/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2003, Retek Inc
//


package com.chelseasystems.cs.pricing;

import java.io.*;
import java.util.*;
import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cs.pricing.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.xml.*;


/**
 * put your documentation comment here
 */
public class CMSPromotionFileServices extends CMSPromotionServices {
  private static CMSPromotion[] allPromotions = null;
  private static String promotionFileName = "promotions.xml";
  private static CMSThresholdPromotion[] allThresholdPromotions = null;
  private static String thresholdPromotionFileName = "threshold_promotions.xml";

  /**
   * Method to get a Promotion by id
   * @param id
   * @return a Promotion
   * @throws Exception
   */
  public IPromotion findById(String id)
      throws Exception {
    if (allPromotions == null)
      loadPromotions();
    for (int i = 0; i < allPromotions.length; i++)
      if (allPromotions[i].getRuleDrvId().equals(id))
        return (IPromotion)allPromotions[i].clone();
    return null;
  }

  /**
   * Method to return all deal entries for a store
   * @param store
   * @return an array of all deal entries for a store
   * @throws Exception
   */
  public IPromotion[] findAllForStore(Store store)
      throws Exception {
    if (allPromotions == null)
      loadPromotions();
    IPromotion[] copies = new IPromotion[allPromotions.length];
    for (int i = 0; i < allPromotions.length; i++)
      copies[i] = (IPromotion)allPromotions[i].clone();
    return copies;
  }

  /**
   * Method to get a ThresholdPromotion by id
   * @param id
   * @return a ThresholdPromotion
   * @throws Exception
   */
  public ThresholdPromotion findThresholdPromotionById(String id)
      throws Exception {
    if (allThresholdPromotions == null)
      loadThresholdPromotions();
    for (int i = 0; i < allThresholdPromotions.length; i++)
      if (allThresholdPromotions[i].getId().equals(id))
        return (ThresholdPromotion)allThresholdPromotions[i].clone();
    return null;
  }

  /**
   * Method to return all ThresholdPromotions for a store
   * @param store
   * @return an array of ThresholdPromotions for a store
   * @throws Exception
   */
  public ThresholdPromotion[] findThresholdPromotionsForStore(Store store)
      throws Exception {
    if (allThresholdPromotions == null)
      loadThresholdPromotions();
    ThresholdPromotion[] copies = new ThresholdPromotion[allThresholdPromotions.length];
    for (int i = 0; i < allThresholdPromotions.length; i++)
      copies[i] = (ThresholdPromotion)allThresholdPromotions[i].clone();
    return copies;
  }

  /**
   * put your documentation comment here
   * @param promotion
   * @exception Exception
   */
  public void insert(IPromotion promotion)
      throws Exception {
    //    java.util.Arrays.
    //    ArrayList list =
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void loadPromotions()
      throws Exception {
    long begin = new java.util.Date().getTime();
    String loaclFileName = FileMgr.getLocalFile("xml", promotionFileName);
    if (loaclFileName == null || loaclFileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadPromotions()"
          , "Missing data file name." + loaclFileName, "Make sure the data file is there."
          , LoggingServices.CRITICAL);
      System.exit( -1);
    }
    try {
      allPromotions = (CMSPromotion[])((new PromotionXML()).toObjects(loaclFileName)).toArray(new
          CMSPromotion[0]);
    } catch (Exception exp) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadPromotions()"
          , "Cannot process the promotion info data file." + loaclFileName, "Exception: " + exp
          , LoggingServices.CRITICAL);
      System.out.println("Exception --> " + exp);
      exp.printStackTrace();
    }
    long end = new java.util.Date().getTime();
    System.out.println("Number of Promotion entries loaded: " + allPromotions.length + " ("
        + (end - begin) + "ms)");
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void loadThresholdPromotions()
      throws Exception {
    long begin = new java.util.Date().getTime();
    String loaclFileName = FileMgr.getLocalFile("xml", thresholdPromotionFileName);
    if (loaclFileName == null || loaclFileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadThresholdPromotions()"
          , "Missing data file name." + loaclFileName, "Make sure the data file is there."
          , LoggingServices.CRITICAL);
      System.exit( -1);
    }
    try {
      allThresholdPromotions = (CMSThresholdPromotion[])((new ThresholdPromotionXML()).toObjects(
          loaclFileName)).toArray(new CMSThresholdPromotion[0]);
    } catch (Exception exp) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadThresholdPromotions()"
          , "Cannot process the threshold promotion info data file." + loaclFileName
          , "Exception: " + exp, LoggingServices.CRITICAL);
      System.out.println("Exception --> " + exp);
      exp.printStackTrace();
    }
    long end = new java.util.Date().getTime();
    System.out.println("Number of ThresholdPromotion entries loaded: "
        + allThresholdPromotions.length + " (" + (end - begin) + "ms)");
  }
}

