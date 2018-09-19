/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2003, Retek Inc
//


package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.dataaccess.*;


/**
 *
 * <p>Title: CMSPromotionJDBCServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSPromotionJDBCServices extends CMSPromotionServices {
  private PromotionDAO promotionDAO;
  private ThresholdPromotionDAO thresholdPromotionDAO;

  /**
   * Default constructor
   */
  public CMSPromotionJDBCServices() {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    promotionDAO = (PromotionDAO)configMgr.getObject("PROMOTION_DAO");
    thresholdPromotionDAO = (ThresholdPromotionDAO)configMgr.getObject("THRESHOLDPROMOTION_DAO");
  }

  /**
   * This method is used to get a Promotion by id
   * @param id
   * @return a Promotion
   * @throws Exception
   */
  public IPromotion findById(String id)
      throws Exception {
    return null; //should not call this method from remote
  }

  /**
   * This method is used to return all promotion for a store
   * @param store
   * @return an array of all promotion for a store
   * @throws Exception
   */
  public IPromotion[] findAllForStore(Store store)
      throws Exception {
    try {
      return promotionDAO.selectByStore(store);
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
      //throw new Exception();
    }
  }

  /**
   * This method is used to get a ThresholdPromotion by id
   * @param id
   * @return a ThresholdPromotion
   * @throws Exception
   */
  public ThresholdPromotion findThresholdPromotionById(String id)
      throws Exception {
    return null; //should not call this method from remote
  }

  /**
   * This method is used to return all ThresholdPromotions for a store
   * @param store
   * @return an array of ThresholdPromotions for a store
   * @throws Exception
   */
  public ThresholdPromotion[] findThresholdPromotionsForStore(Store store)
      throws Exception {
    return thresholdPromotionDAO.selectByStore(store);
  }

  /**
   * This method is used to permantly persist specified CMSPromotion into storage.
   * @param promotion
   * @throws Exception
   */
  public void insert(IPromotion promotion)
      throws Exception {
    promotionDAO.insert(promotion);
  }
}

