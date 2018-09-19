/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	3/14/05	        KS	Pricing	                        Discount
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.discount;

import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.item.Item;


/**
 * <p>Title: LineItemDiscountDetail</p>
 * <p>Description: This class stores the discount details of line item </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Khyati Shah
 * @version 1.0
 */
public class LineItemDiscountDetail implements com.chelseasystems.cr.rules.IRuleEngine {
  static final long serialVersionUID = 8350808060163710291L;
  private String txnId = null;
  private int itemId;
  private int seqNum;
  private int dscSeqNum;

  /**
   * Constructor
   * @param txnId String
   * @param itemId int
   * @param aSequenceNumber int
   * @param dscSeqNum int
   */
  public LineItemDiscountDetail(String txnId, int itemId, int aSequenceNumber, int dscSeqNum) {
    txnId = txnId;
    itemId = itemId;
    seqNum = aSequenceNumber;
    dscSeqNum = dscSeqNum;
  }

  /**
   * Default Constructor
   */
  public LineItemDiscountDetail() {
  }

  /**
   * This method is used to get transaction id
   * @return String
   */
  public String getTxnId() {
    return this.txnId;
  }

  /**
   * This method is used to set transaction id
   * @param txnId String
   */
  public void setTxnId(String txnId) {
    doSetTxnId(txnId);
  }

  /**
   * This method is used to set transaction id
   * @param txnId String
   */
  public void doSetTxnId(String txnId) {
    this.txnId = txnId;
  }

  /**
   * This method is used to get item id
   * @return int
   */
  public int getItemId() {
    return this.itemId;
  }

  /**
   * This method is used to get item id
   * @return Long
   */
  public Long getLongItemId() {
    return new Long(this.itemId);
  }

  /**
   * This method is used to set item id
   * @param itemId int
   */
  public void setItemId(int itemId) {
    doSetItemId(itemId);
  }

  /**
   * This method is used to set item id
   * @param itemId int
   */
  public void doSetItemId(int itemId) {
    this.itemId = itemId;
  }

  /**
   * This method is used to get sequence number
   * @return int
   */
  public int getSeqNum() {
    return this.seqNum;
  }

  /**
   * This method is used to set sequence number
   * @param seqNum int
   */
  public void setSeqNum(int seqNum) {
    doSetSeqNum(seqNum);
  }

  /**
   * This method is used to set sequence number
   * @param seqNum int
   */
  public void doSetSeqNum(int seqNum) {
    this.seqNum = seqNum;
  }

  /**
   *
   * @return int
   */
  public int getDScSeqNum() {
    return this.dscSeqNum;
  }

  /**
   *
   * @param dscSeqNum int
   */
  public void setDscSeqNum(int dscSeqNum) {
    doSetDscSeqNum(dscSeqNum);
  }

  /**
   *
   * @param dscSeqNum int
   */
  public void doSetDscSeqNum(int dscSeqNum) {
    this.dscSeqNum = dscSeqNum;
  }
}

