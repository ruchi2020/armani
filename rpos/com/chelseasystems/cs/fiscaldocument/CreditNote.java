/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.fiscaldocument;


/**
 * <p>Title:CreditNote </p>
 * <p>Description: CreditNote object </p>
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
 | 1    | 05-26-2005 | Manpreet  | N/A       | POS_104665_TS_FiscalDocuments_Rev0                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CreditNote extends FiscalDocument {
  /**
   * Bank
   */
  private String sBank;
  /**
   * FiscalCode
   */
  private String sFiscalCode;
  /**
   * Supplier Payment Type
   */
  private String sSupplierPayType;

  /**
   * Default Constructor
   */
  public CreditNote () {
    sBank = new String();
    sFiscalCode = new String();
    sSupplierPayType = new String();
  }

  /**
   * Bank
   * @return String
   */
  public void setBank (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetBank(sValue);
  }

  /**
   * Bank
   * @param sValue String
   */
  public void doSetBank (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }

    this.sBank = sValue;
  }

  /**
   * Bank
   * @return String
   */
  public String getBank () {
    return  this.sBank;
  }

  /**
   * FiscalCode
   * @return String
   */
  public void setFiscalCode (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetFiscalCode(sValue);
  }

  /**
   * FiscalCode
   * @param sValue String
   */
  public void doSetFiscalCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sFiscalCode = sValue;
  }

  /**
   * FiscalCode
   * @return String
   */
  public String getFiscalCode () {
    return  this.sFiscalCode;
  }

  /**
   * SupplierPayType
   * @return String
   */
  public void setSupplierPayType (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetSupplierPayType(sValue);
  }

  /**
   * SupplierPayType
   * @param sValue String
   */
  public void doSetSupplierPayType (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sSupplierPayType = sValue;
  }

  /**
   * SupplierPayType
   * @return String
   */
  public String getSupplierPayType () {
    return  this.sSupplierPayType;
  }
}



