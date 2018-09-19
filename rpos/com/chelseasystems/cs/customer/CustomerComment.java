/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import java.util.Date;
import java.io.Serializable;


/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-10-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
/**
 *
 * <p>Title: CustomerComment</p>
 *
 * <p>Description: Stores information about customer comment</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CustomerComment implements Comparable, Serializable {
  /**
   * Store ID
   */
  private String sStoreID;
  /**
   * Brand Id
   */
  private String sBrandID;
  /**
   * Associate Id
   */
  private String sAssociateID;
  /**
   * Date Commented
   */
  private Date dtCommented;
  /**
   * Comments
   */
  private String sComment;
  /**
   * CustomerCommentId
   */
  private String sCustomerCommentId;
  private boolean isModified;

  /**
   * Default Constructor
   */
  public CustomerComment() {
  }

  /**
   * This method is used to Add Comment
   * @param sStoreId StoreId
   * @param sBrandId BrandId
   * @param sAssocId AssociateId
   * @param dtCommented DateCommented
   * @param sComment Comment
   */
  public void addComment(String sStoreId, String sBrandId, String sAssocId, Date dtCommented
      , String sComment) {
    if (sStoreId == null || sStoreId.length() < 1 || sBrandId == null || sBrandId.length() < 1
        || sAssocId == null || sAssocId.length() < 1 || sComment == null || sComment.length() < 1
        || dtCommented == null) {
      return;
    }
  }

  /**
   * This method is used to Set StoreId
   * @param sValue StoreId
   */
  public void setStoreId(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sStoreID = sValue;
  }

  /**
   * This method is used to Get StoreId
   * @return StoreId
   */
  public String getStoreId() {
    return this.sStoreID;
  }

  /**
   * This method is used to set BrandId
   * @param sValue BrandId
   */
  public void setBrandId(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sBrandID = sValue;
  }

  /**
   * This method is used to get BrandId
   * @return BrandId
   */
  public String getBrandId() {
    return this.sBrandID;
  }

  /**
   * This method is used to set AssociateId
   * @param sValue AssociateId
   */
  public void setAssociateId(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sAssociateID = sValue;
  }

  /**
   * This method is used to get AssociateId
   * @return AssociateId
   */
  public String getAssociateId() {
    return this.sAssociateID;
  }

  /**
   * This method is used to get Set Comment
   * @param sValue Comment
   */
  public void setComment(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sComment = sValue;
  }

  /**
   * This method is used to get comment
   * @return StoreId
   */
  public String getComment() {
    return this.sComment;
  }

  /**
   * This method is used to set Date Commented
   * @param dtComment DateCommented
   */
  public void setDateCommented(Date dtComment) {
    if (dtComment == null) {
      return;
    }
    this.dtCommented = dtComment;
  }

  /**
   * This method is used to get Date Commented
   * @return DateCommented
   */
  public Date getDateCommented() {
    return dtCommented;
  }

  /**
   * This method is used for sorting Comments on current store to currend date
   * @param obj CommentToBeCompared
   * @return  int
   *   1 - If StoreId is more than StoreId of CommentToBeCompared
   *  -1 - If StoreId is less than StoreId of CommentToBeCompared
   *   0 - If StoreId & DateCommented is same as StoreId & DateCommented of CommentToBeCompared
   *   1 - If StoreId is same but DateCommented is After
   *  -1 - If StoreId is same but Datecommented is Before
   */
  public int compareTo(Object obj) {
    CustomerComment commentObj = (CustomerComment)obj;
    if (Integer.parseInt(sStoreID) > Integer.parseInt(commentObj.getStoreId())) {
      return 1;
    }
    if (Integer.parseInt(sStoreID) == Integer.parseInt(commentObj.getStoreId())) {
      if (dtCommented.after(commentObj.getDateCommented())) {
        return 1;
      } else if (dtCommented.before(commentObj.getDateCommented())) {
        return -1;
      } else {
        return 0;
      }
    }
    return -1;
  }

  /**
   * This method is used to set CustomerComment Id
   * @param sValue CustomerCommentId
   */
  public void setCustomerCommentId(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sCustomerCommentId = sValue;
  }

  /**
   * This method is used to get CustomerComment ID
   * @return CustomerComment Id
   */
  public String getCustomerCommentId() {
    return this.sCustomerCommentId;
  }

  /**
   * This method is used to set that customer is modified
   * @param val boolean
   */
  public void setIsModified(boolean val) {
    this.isModified = val;
  }

  /**
   * This method is used to check whether customer is modified or not
   * @return boolean
   */
  public boolean isModified() {
    return this.isModified;
  }
}

