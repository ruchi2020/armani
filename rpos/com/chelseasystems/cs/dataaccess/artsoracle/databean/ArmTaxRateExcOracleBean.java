package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
*
* This class is an object representation of the Arts database table ARM_TAX_RATE_EXC<BR>
* Followings are the column of the table: <BR>  
* ID_EXC -- NUMBER<BR>
* ID_RATE -- NUMBER<BR>
* AMOUNT_TRN -- NUMBER<BR>
* ZIP_CODE -- NUMBER<BR>
* TAX_JUR -- VARCHAR2(20 BYTE)<BR>
* STATE -- VARCHAR2(10 BYTE)<BR>
* NATION -- VARCHAR2(10 BYTE)<BR>
* TAX_RATE -- NUMBER<BR>
* THRESHOLD_RULE -- VARCHAR2(1 BYTE)<BR>
* EFFECTIVE_DT -- DATE<BR>
* BRAND -- VARCHAR2(10 BYTE)<BR>
* CATEGORY -- VARCHAR2(10 BYTE)<BR>
* PRODUCT -- VARCHAR2(10 BYTE)<BR>
* DATA_INS -- DATE<BR>
* DATA_MOD -- DATE<BR>
*
*/

public class ArmTaxRateExcOracleBean extends BaseOracleBean{
	


	  //public ArmTaxRateOracleBean(){}

	  public static String selectSql = "select ID_EXC, ID_RATE, AMOUNT_TRN, ZIP_CODE, TAX_JUR, STATE, NATION, TAX_RATE, THRESHOLD_RULE, EFFECTIVE_DT, BRAND, CATEGORY, PRODUCT, DATA_INS, DATA_MOD, TAX_TYPE, EXPIRATION_DT from ARM_TAX_RATE_EXC ";
	  public static String insertSql = "insert into ARM_TAX_RATE_EXC (ID_EXC, ID_RATE, AMOUNT_TRN, ZIP_CODE, TAX_JUR, STATE, NATION, TAX_RATE, THRESHOLD_RULE, EFFECTIVE_DT, BRAND, CATEGORY, PRODUCT, DATA_INS, DATA_MOD) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	  public static String updateSql = "update ARM_TAX_RATE_EXC set ID_EXC  = ?, ID_RATE  = ?, AMOUNT_TRN  = ?, ZIP_CODE  = ?, TAX_JUR  = ?, STATE  = ?, NATION  = ?, TAX_RATE  = ?, THRESHOLD_RULE  = ?, EFFECTIVE_DT  = ?, BRAND  = ?, CATEGORY  = ?, PRODUCT  = ?, DATA_INS  = ?, DATA_MOD = ?";
	  public static String deleteSql = "delete from ARM_TAX_RATE_EXC ";

	  public static String TABLE_NAME = "ARM_TAX_RATE_EXC";
	  public static String COL_ID_EXC = "ARM_TAX_RATE_EXC.ID_EXC";
      public static String COL_ID_RATE = "ARM_TAX_RATE_EXC.ID_RATE";
      public static String COL_AMOUNT_TRN = "ARM_TAX_RATE_EXC.AMOUNT_TRN";
      public static String COL_ZIP_CODE = "ARM_TAX_RATE_EXC.ZIP_CODE";
      public static String COL_STATE = "ARM_TAX_RATE_EXC.STATE";
      public static String COL_NATION = "ARM_TAX_RATE_EXC.NATION";
	  public static String COL_TAX_RATE = "ARM_TAX_RATE_EXC.TAX_RATE";
	  public static String COL_THRESHOLD_RULE = "ARM_TAX_RATE_EXC.THRESHOLD_RULE";
	  public static String COL_EFFECTIVE_DT = "ARM_TAX_RATE_EXC.EFFECTIVE_DT";
	  public static String COL_BRAND = "ARM_TAX_RATE_EXC.BRAND";
	  public static String COL_CATEGORY = "ARM_TAX_RATE_EXC.CATEGORY";
	  public static String COL_PRODUCT = "ARM_TAX_RATE_EXC.PRODUCT";
	  public static String COL_DATA_INS = "ARM_TAX_RATE_EXC.DATA_INS";
	  public static String COL_DATA_MOD = "ARM_TAX_RATE_EXC.DATA_MOD";
	  public static String COL_TAX_JUR = "ARM_TAX_RATE_EXC.TAX_JUR";
	  public static String COL_TAX_TYPE = "ARM_TAX_RATE_EXC.TAX_TYPE";
	  
	  //Poonam: Field added for expiration date issue on Nov 15,2016
	  public static String COL_EXPIRATION_DT = "ARM_TAX_RATE_EXC.EXPIRATION_DT";
	  
	  public String getSelectSql() { return selectSql; }
	  public String getInsertSql() { return insertSql; }
	  public String getUpdateSql() { return updateSql; }
	  public String getDeleteSql() { return deleteSql; }

	  private Double idexc;
	  private String state;
	  private Double amountThr;
	  private String zipCode;
	  private String taxJur;
	  private Double taxRate;
	  private String thrRule;
	  private Date effectiveDt;
	  private Double idRate;
	  private String nation;
	  private String brand;
	  private String category;
	  private String product;
	  private Date datains;
	  private Date dataMod;
	  private String taxType;
	  //Poonam: Field added for expiration date issue on Nov 15,2016
	  private Date expiration_dt;
	  

	  public Double getIdExc() { return this.idexc; }
	  public void setIdExc(Double taxRate) { this.idexc = idexc; }
	  
	  public String getState() { return this.state; }
	  public void setState(String state) { this.state = state; }
	  
	  public Double getAmountThr() { return this.amountThr; }
	  public void setAmountThr(Double amountThr) { this.amountThr = amountThr; }
	  
	  public String getZipCode() { return this.zipCode; }
	  public void setZipCode(String zipCode) { this.zipCode = zipCode; }
	  
	  public String getTaxJur() { return this.taxJur; }
	  public void setTaxJur(String taxJur) { this.taxJur = taxJur; }

	  public Double getTaxRate() { return this.taxRate; }
	  public void setTaxRate(Double taxRate) { this.taxRate = taxRate; }
	  
	  public String getThrRule() { return this.thrRule; }
	  public void setThrRule(String thrRule) { this.thrRule = thrRule; }

	  public Date getEffectiveDt() { return this.effectiveDt; }
	  public void setEffectiveDt(Date effectiveDt) { this.effectiveDt = effectiveDt; }
	  
	  public Double getIdRate() { return this.idRate; }
	  public void setIdRate(Double idRate) { this.idRate = idRate; }
	  
	  public String getNation() { return this.nation; }
	  public void setNation(String nation) { this.nation = nation; }
	  
	  public String getBrand() { return this.brand; }
	  public void setBrand(String brand) { this.brand = brand; }
	  
	  public String getCategory() { return this.category; }
	  public void setCategory(String catagory) { this.category = category; }
	  
	  public String getProduct() { return this.product; }
	  public void setProduct(String product) { this.product = product; }
	  
	  public Date getDataIns() { return this.datains; }
	  public void setDataIns(Date datains) { this.datains = datains; }
	  
	  public Date getDataMod() { return this.dataMod; }
	  public void setDataMod(Date dataMod) { this.datains = dataMod; }
	  
	  public String getTaxType() {return taxType;}
	  public void setTaxType(String taxType) {this.taxType = taxType;}
	  
	  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
	    ArrayList list = new ArrayList();
	    while(rs.next()) {
	      ArmTaxRateExcOracleBean bean = new ArmTaxRateExcOracleBean();
	      bean.idexc = getDoubleFromResultSet(rs, "ID_EXC");
	      bean.state = getStringFromResultSet(rs, "STATE");
	      bean.amountThr = getDoubleFromResultSet(rs, "AMOUNT_TRN");
	      bean.zipCode = getStringFromResultSet(rs, "ZIP_CODE");
	      bean.taxJur = getStringFromResultSet(rs, "TAX_JUR");
	      bean.taxRate = getDoubleFromResultSet(rs, "TAX_RATE");
	      bean.thrRule = getStringFromResultSet(rs, "THRESHOLD_RULE");
	      bean.effectiveDt = getDateFromResultSet(rs, "EFFECTIVE_DT");
	      bean.idRate = getDoubleFromResultSet(rs, "ID_RATE");
		  bean.nation = getStringFromResultSet(rs, "NATION");
		  bean.brand = getStringFromResultSet(rs, "BRAND");
		  bean.category = getStringFromResultSet(rs, "CATEGORY");
		  bean.product = getStringFromResultSet(rs, "PRODUCT");
		  bean.datains = getDateFromResultSet(rs, "DATA_INS");
		  bean.dataMod = getDateFromResultSet(rs, "DATA_MOD");
		  bean.taxType = getStringFromResultSet(rs, "TAX_TYPE");
		  
		  //Poonam: Added for expiration date issue on Nov 15,2016
		  bean.expiration_dt=getDateFromResultSet(rs, "EXPIRATION_DT");
		  //ends here
	      list.add(bean);
	    }
	    return (ArmTaxRateExcOracleBean[]) list.toArray(new ArmTaxRateExcOracleBean[0]);
	  }

	  public List toList() {
	    List list = new ArrayList();
	    addToList(list, this.getIdExc(), Types.DECIMAL);
	    addToList(list, this.getState(), Types.VARCHAR);
	    addToList(list, this.getAmountThr(), Types.VARCHAR);
	    addToList(list, this.getZipCode(), Types.VARCHAR);
	    addToList(list, this.getTaxJur(), Types.VARCHAR);
	    addToList(list, this.getTaxRate(), Types.DECIMAL);
	    addToList(list, this.getThrRule(), Types.VARCHAR);
	    addToList(list, this.getEffectiveDt(), Types.TIMESTAMP);
	    addToList(list, this.getIdRate(), Types.DECIMAL);
	    addToList(list, this.getNation(), Types.VARCHAR);
	    addToList(list, this.getBrand(), Types.VARCHAR);
	    addToList(list, this.getCategory(), Types.VARCHAR);
	    addToList(list, this.getProduct(), Types.VARCHAR);
	    addToList(list, this.getDataIns(), Types.TIMESTAMP);
	    addToList(list, this.getDataMod(), Types.TIMESTAMP);
	    addToList(list, this.getTaxType(), Types.VARCHAR);
	    
	    //Poonam: Added for expiration date issue on Nov 15,2016
	    addToList(list, this.getExpiration_dt(), Types.TIMESTAMP);
	    //ends here
	    return list;
	  }
	  
	/**Added for expiration date issue on Nov 15,2016
	 * @return the expiration_dt
	 */
	public Date getExpiration_dt() {
		return this.expiration_dt;
	}
	
	/**Added for expiration date issue on Nov 15,2016
	 * @param expiration_dt the expiration_dt to set
	 */
	public void setExpiration_dt(Date expiration_dt) {
		this.expiration_dt = expiration_dt;
	}
	//ends here


}
