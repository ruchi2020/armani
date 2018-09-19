/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.fiscaldocument;


/**
 * <p>Title:DDT </p>
 * <p>Description: </p>
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
public class DDT extends FiscalDocument {
  /**
   * Carrier Code
   */
  private String sCarrierCode;
  /**
   * Carrier Desc
   */
  private String sCarrierDesc;
  /**
   * Carrier Type
   */
  private String sCarrierType;
  /**
   * Destination
   */
  private String sDestination;
  /**
   * Destination Code
   */
  private String sDestinationCode;
  /**
   * Expedition Code
   */
  private String sExpeditionCode;
  /**
   * Goods Number
   */
  private String sGoodsNumber;
  /**
   * Note
   */
  private String sNote;
  /**
   * Packaget Type
   */
  private String sPackageType;
  /**
   * Sender
   */
  private String sSender;
  /**
   * Sender Code
   */
  private String sSenderCode;
  /**
   * Weight
   */
  private double dWeight;

  /**
   * Default Constructor
   */
  public DDT () {
    sCarrierCode = new String();
    sCarrierDesc = new String();
    sCarrierType = new String();
    sDestination = new String();
    sDestinationCode = new String();
    sExpeditionCode = new String();
    sGoodsNumber = new String();
    sNote = new String();
    sPackageType = new String();
    sSender = new String();
    sSenderCode = new String();
    dWeight = 0.0d;
  }

  /**
   * CarrierCode
   * @return String
   */
  public void setCarrierCode (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetCarrierCode(sValue);
  }

  /**
   * City
   * @param sValue String
   */
  public void doSetCarrierCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sCarrierCode = sValue;
  }

  /**
   * City
   * @return String
   */
  public String getCarrierCode () {
    return  this.sCarrierCode;
  }

  /**
   * CarrierDesc
   * @return String
   */
  public void setCarrierDesc (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetCarrierDesc(sValue);
  }

  /**
   * CarrierDesc
   * @param sValue String
   */
  public void doSetCarrierDesc (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sCarrierDesc = sValue;
  }

  /**
   * CarrierDesc
   * @return String
   */
  public String getCarrierDesc () {
    return  this.sCarrierDesc;
  }

  /**
   * CarrierType
   * @return String
   */
  public void setCarrierType (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetCarrierType(sValue);
  }

  /**
   * CarrierType
   * @param sValue String
   */
  public void doSetCarrierType (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sCarrierType = sValue;
  }

  /**
   * CarrierType
   * @return String
   */
  public String getCarrierType () {
    return  this.sCarrierType;
  }

  /**
   * Destination
   * @return String
   */
  public void setDestination (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetDestination(sValue);
  }

  /**
   * Destination
   * @param sValue String
   */
  public void doSetDestination (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sDestination = sValue;
  }

  /**
   * Destination
   * @return String
   */
  public String getDestination () {
    return  this.sDestination;
  }

  /**
   * DestinationCode
   * @return String
   */
  public void setDestinationCode (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetDestinationCode(sValue);
  }

  /**
   * DestinationCode
   * @param sValue String
   */
  public void doSetDestinationCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sDestinationCode = sValue;
  }

  /**
   * DestinationCode
   * @return String
   */
  public String getDestinationCode () {
    return  this.sDestinationCode;
  }

  /**
   * ExpeditionCode
   * @return String
   */
  public void setExpeditionCode (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetExpeditionCode(sValue);
  }

  /**
   * ExpeditionCode
   * @param sValue String
   */
  public void doSetExpeditionCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sExpeditionCode = sValue;
  }

  /**
   * ExpeditionCode
   * @return String
   */
  public String getExpeditionCode () {
    return  this.sExpeditionCode;
  }

  /**
   * GoodsNumber
   * @return String
   */
  public void setGoodsNumber (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetGoodsNumber(sValue);
  }

  /**
   * GoodsNumber
   * @param sValue String
   */
  public void doSetGoodsNumber (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sGoodsNumber = sValue;
  }

  /**
   * GoodsNumber
   * @return String
   */
  public String getGoodsNumber () {
    return  this.sGoodsNumber;
  }

  /**
   * Note
   * @return String
   */
  public void setNote (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetNote(sValue);
  }

  /**
   * Note
   * @param sValue String
   */
  public void doSetNote (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sNote = sValue;
  }

  /**
   * Note
   * @return String
   */
  public String getNote () {
    return  this.sNote;
  }

  /**
   * PackageType
   * @return String
   */
  public void setPackageType (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetPackageType(sValue);
  }

  /**
   * PackageType
   * @param sValue String
   */
  public void doSetPackageType (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sPackageType = sValue;
  }

  /**
   * PackageType
   * @return String
   */
  public String getPackageType () {
    return  this.sPackageType;
  }

  /**
   * Sender
   * @return String
   */
  public void setSender (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetSender(sValue);
  }

  /**
   * Sender
   * @param sValue String
   */
  public void doSetSender (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sSender = sValue;
  }

  /**
   * PackageType
   * @return String
   */
  public String getSender () {
    return  this.sSender;
  }

  /**
   * SenderCode
   * @return String
   */
  public void setSenderCode (String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetSenderCode(sValue);
  }

  /**
   * SenderCode
   * @param sValue String
   */
  public void doSetSenderCode (String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sSenderCode = sValue;
  }

  /**
   * SenderCode
   * @return String
   */
  public String getSenderCode () {
    return  this.sSenderCode;
  }

  /**
   * Weight
   * @return String
   */
  public void setWeight (double sValue) {
    doSetWeight(sValue);
  }

  /**
   * SenderCode
   * @param sValue String
   */
  public void doSetWeight (double sValue) {
    this.dWeight = sValue;
  }

  /**
   * Weight
   * @return String
   */
  public double getWeight () {
    return  this.dWeight;
  }
}



