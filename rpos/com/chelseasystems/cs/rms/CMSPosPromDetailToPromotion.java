/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 02-27-2005 | Anand     | N/A       | Created to map POSPromDetail object to the   |
 |      |            |           |           | kind of promotion and build the promotion obj|
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.rms;

import java.util.Calendar;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pricing.IPromotion;
import com.chelseasystems.cr.rms.PosPromDetail;
import com.chelseasystems.cr.rms.PosPromDetailConstants;
import com.chelseasystems.cr.rms.PosPromDetailToPromotion;
import com.chelseasystems.cr.rms.PosPromDetailTranType;
import com.chelseasystems.cs.pricing.BuyXGetYPromotion;
import com.chelseasystems.cs.pricing.CMSPromotion;
import com.chelseasystems.cs.pricing.ItemThresholdPromotion;
import com.chelseasystems.cs.pricing.MultiunitPromotion;
import com.chelseasystems.cs.pricing.PackagePromotion;
import com.chelseasystems.cs.rms.CMSPosPromDetail;


/**
 *
 * <p>Title: CMSPosPromDetailToPromotion</p>
 *
 * <p>Description: Class store the details of promotion</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSPosPromDetailToPromotion extends PosPromDetailToPromotion {

  /**
   *
   * @param rmsPromotion PosPromDetail
   * @return IPromotion
   */
  public IPromotion map(PosPromDetail rmsPromotion) {
    PosPromDetailTranType type = PosPromDetailTranType.getPosPromDetailTranType(rmsPromotion.
        getPromTranType());
    IPromotion retVal = null;
    if (type == PosPromDetailTranType.PROM_TRAN_TYPE_1000) { // Mix and Match
      retVal = this.createMixAndMatch(rmsPromotion);
    } else if (type == PosPromDetailTranType.PROM_TRAN_TYPE_1001) { // Threshold
      retVal = this.createThreshold(rmsPromotion);
    } else if (type == PosPromDetailTranType.PROM_TRAN_TYPE_1002) { // Multi Unit
      retVal = this.createMultiUnit(rmsPromotion);
    }
    return retVal;
  }

  /**
   * Allows the user to set a specified amount of one item that must be
   * purchased to receive another item for free or at a discount.
   * @param rmsPromotion
   * @return
   */
  private IPromotion createMixAndMatch(PosPromDetail rmsPromotion) {
    CMSPromotion mixAndMaxPromotion = null;
    if (PosPromDetailConstants.BUY_TYPE_ALL.equals(rmsPromotion.getBuyType())) {
      mixAndMaxPromotion = this.createPackagePromotion(rmsPromotion);
    } else {
      mixAndMaxPromotion = this.createBuyXGetYPromotion(rmsPromotion);
    }
    mixAndMaxPromotion.doSetId(mixAndMaxPromotion.getId() + "" + rmsPromotion.getMixMatchNo());
    mixAndMaxPromotion.doSetDescription(PosPromDetailTranType.PROM_TRAN_TYPE_1000.getDescription());
    // rpos doesn't currently support Unit Of Measure (all are eaches).
    //    rmsPromotion.getSellingUom();
    return mixAndMaxPromotion;
  }

  /**
   * Allows the user to create promotional discounts that increase as either the
   * total purchase or the number of items the customer purchases increases.
   * @param rmsPromotion
   * @return
   */
  private IPromotion createThreshold(PosPromDetail rmsPromotion) {
    ItemThresholdPromotion itemThresholdPromotion = new ItemThresholdPromotion("");
    this.mapGenericPromotionData(itemThresholdPromotion, rmsPromotion);
    itemThresholdPromotion.doSetId(itemThresholdPromotion.getId());
    itemThresholdPromotion.doSetDescription(PosPromDetailTranType.PROM_TRAN_TYPE_1001.
        getDescription());
    // rpos does not currently support compound promotions
    //    rmsPromotion.getApplyTo();  // compound, exclusive
    if (PosPromDetailConstants.GET_TYPE_AMOUNT_OFF.equals(rmsPromotion.getDiscountType())) {
      itemThresholdPromotion.doSetReductionByUnitPriceOff(new ArmCurrency(rmsPromotion.getDiscountAmt()));
    } else if (PosPromDetailConstants.GET_TYPE_PERCENT_OFF.equals(rmsPromotion.getDiscountType())) {
      double percentagePromo = ((CMSPosPromDetail)rmsPromotion).getDiscountPercent() / 100;
      itemThresholdPromotion.doSetReductionByPercentageOff(percentagePromo);
    } else {
      itemThresholdPromotion.doSetReductionByFixedUnitPrice(new ArmCurrency(rmsPromotion.
          getDiscountAmt()));
    }
    itemThresholdPromotion.doSetTriggerAmount(new ArmCurrency(rmsPromotion.getThresholdAmt()));
    itemThresholdPromotion.doSetTriggerQuantity((int)rmsPromotion.getThresholdAmt());
    return itemThresholdPromotion;
  }

  /**
   * Allows the user to set a special price for purchases of more than one of
   * the same item.
   * @param rmsPromotion
   * @return
   */
  private IPromotion createMultiUnit(PosPromDetail rmsPromotion) {
    MultiunitPromotion multiunitPromotion = new MultiunitPromotion("");
    this.mapGenericPromotionData(multiunitPromotion, rmsPromotion);
    multiunitPromotion.doSetId(multiunitPromotion.getId() + "" + rmsPromotion.getThresholdNo());
    multiunitPromotion.doSetDescription(PosPromDetailTranType.PROM_TRAN_TYPE_1002.getDescription());
    // rpos does not currently support compound promotions
    //    rmsPromotion.getApplyTo();  // compound, exclusive
    multiunitPromotion.doSetQuantityBreak((int)rmsPromotion.getThresholdAmt());
    if (PosPromDetailConstants.GET_TYPE_AMOUNT_OFF.equals(rmsPromotion.getDiscountType())) {
      multiunitPromotion.doSetReductionByUnitPriceOff(new ArmCurrency(rmsPromotion.getDiscountAmt()));
    } else if (PosPromDetailConstants.GET_TYPE_PERCENT_OFF.equals(rmsPromotion.getDiscountType())) {
      multiunitPromotion.doSetReductionByPercentageOff(rmsPromotion.getDiscountAmt());
    } else {
      multiunitPromotion.doSetReductionByFixedUnitPrice(new ArmCurrency(rmsPromotion.getDiscountAmt()));
    }
    return multiunitPromotion;
  }

  /**
   *
   * @param promotion CMSPromotion
   * @param rmsPromotion PosPromDetail
   */
  private void mapGenericPromotionData(CMSPromotion promotion, PosPromDetail rmsPromotion) {
    //promotion.doSetIdStrRt(String.valueOf(rmsPromotion.getStore()));
    promotion.doSetIdStrRt(((CMSPosPromDetail)rmsPromotion).getStoreId());
    // Warning: Expecting default timeZone to be appropriate.
    final Calendar beginTime = Calendar.getInstance();
    beginTime.setTime(rmsPromotion.getStartTime());
    promotion.doSetBeginTime(beginTime);
    // Warning: Expecting default timeZone to be appropriate.
    final Calendar endTime = Calendar.getInstance();
    endTime.setTime(rmsPromotion.getEndTime());
    promotion.doSetEndTime(endTime);
    promotion.doSetId(((CMSPosPromDetail)rmsPromotion).getPromotionNum());
    promotion.doSetPromotionName(((CMSPosPromDetail)rmsPromotion).getPromotionName());
  }

  /**
   * Method is used to create promotion (buy one get another free)
   * @param rmsPromotion PosPromDetail
   * @return BuyXGetYPromotion
   */
  private BuyXGetYPromotion createBuyXGetYPromotion(PosPromDetail rmsPromotion) {
    BuyXGetYPromotion buyXGetYPromotion = new BuyXGetYPromotion("");
    this.mapGenericPromotionData(buyXGetYPromotion, rmsPromotion);
    if (PosPromDetailConstants.GET_TYPE_AMOUNT_OFF.equals(rmsPromotion.getGetType())) {
      buyXGetYPromotion.doSetReductionByUnitPriceOff(new ArmCurrency(rmsPromotion.getGetAmt()));
    } else if (PosPromDetailConstants.GET_TYPE_PERCENT_OFF.equals(rmsPromotion.getGetType())) {
      buyXGetYPromotion.doSetReductionByPercentageOff(rmsPromotion.getGetAmt());
    } else {
      buyXGetYPromotion.doSetReductionByFixedUnitPrice(new ArmCurrency(rmsPromotion.getGetAmt()));
    }
    return buyXGetYPromotion;
  }

  /**
   * Method is used to create promotion package
   * @param rmsPromotion PosPromDetail
   * @return PackagePromotion
   */
  private PackagePromotion createPackagePromotion(PosPromDetail rmsPromotion) {
    PackagePromotion packagePromotion = new PackagePromotion("");
    this.mapGenericPromotionData(packagePromotion, rmsPromotion);
    if (PosPromDetailConstants.GET_TYPE_AMOUNT_OFF.equals(rmsPromotion.getGetType())) {
      ArmCurrency[] reductions = new ArmCurrency[(int)rmsPromotion.getBuyAmt()];
      reductions[(int)rmsPromotion.getBuyAmt() - 1] = new ArmCurrency(rmsPromotion.getGetAmt());
      packagePromotion.doSetReductionByUnitPriceOff(reductions);
    } else if (PosPromDetailConstants.GET_TYPE_PERCENT_OFF.equals(rmsPromotion.getGetType())) {
      double[] percents = new double[(int)rmsPromotion.getBuyAmt()];
      percents[(int)rmsPromotion.getBuyAmt() - 1] = rmsPromotion.getGetAmt();
      packagePromotion.doSetReductionByPercentageOff(percents);
    } else {
      ArmCurrency[] reductions = new ArmCurrency[(int)rmsPromotion.getBuyAmt()];
      reductions[(int)rmsPromotion.getBuyAmt() - 1] = new ArmCurrency(rmsPromotion.getGetAmt());
      packagePromotion.doSetReductionByFixedUnitPrice(reductions);
    }
    return packagePromotion;
  }
}

