/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.fiscaldocument;

import  java.io.Serializable;


/**
 * <p>Title:FiscalDocumentNumber </p>
 * <p>Description: Holds Document Number</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 06-15-2005 | Manpreet  | N/A       | POS_104665_TS_FiscalDocuments_Rev0                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class FiscalDocumentNumber
    implements Serializable {
  /**
   * RegisterID
   */
  private String sRegisterId;
  /**
   * StoreID
   */
  private String sStoreId;
  /**
   * DDT number
   */
  private long lDDTNo;
  /**
   * Vat number
   */
  private long lVATNo;
  /**
   * CreditNote Number
   */
  private long lCreditNoteNo;

  /**
   * Default Constructor
   */
  public FiscalDocumentNumber () {
    sRegisterId = new String();
    sStoreId = new String();
    lDDTNo = 1;
    lVATNo = 1;
    lCreditNoteNo = 1;
  }

  /**
   * RegisterId
   * @param sValue String
   */
  public void setRegisterId (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    this.sRegisterId = sValue;
  }

  /**
   * RegisterId
   * @return String
   */
  public String getRegisterId () {
    return  this.sRegisterId;
  }

  /**
   * StoreId
   * @param sValue String
   */
  public void setStoreId (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    this.sStoreId = sValue;
  }

  /**
   * StoreId
   * @return String
   */
  public String getStoreId () {
    return  this.sStoreId;
  }

  /**
   * DDT no.
   * @param iValue long
   */
  public void setNextDDTNo (long lValue) {
    this.lDDTNo = lValue;
  }

  /**
   * DDT no.
   * @return long
   */
  public long getNextDDTNo () {
    return  lDDTNo;
  }

  /**
   * VATNo
   * @param iValue long
   */
  public void setNextVATNo (long Value) {
    this.lVATNo = Value;
  }

  /**
   * VATNo
   * @return long
   */
  public long getNextVATNo () {
    return  lVATNo;
  }

  /**
   * CreditNoteNo
   * @param iValue long
   */
  public void setNextCreditNoteNo (long iValue) {
    this.lCreditNoteNo = iValue;
  }

  /**
   * CreditNoteNo
   * @return long
   */
  public long getNextCreditNoteNo () {
    return  lCreditNoteNo;
  }
}



