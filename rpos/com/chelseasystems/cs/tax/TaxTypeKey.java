/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2002, Chelsea Market Systems
//


package com.chelseasystems.cs.tax;


/**
 *
 * <p>Title: TaxTypeKey</p>
 *
 * <p>Description: Tax constants</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface TaxTypeKey {
  //////////////
  // SALE TAX //
  /////////////
  public static String TAX_ENGINE_NAME_NO_TAX = "NOTAX";
  public static String TAX_ENGINE_NAME_FIXED_RATE = "FIXED_RATE";
  public static String TAX_ENGINE_NAME_STORE_RATE = "STORE_RATE";
  public static String TAX_ENGINE_NAME_CANADIAN = "CANADIAN";
  public static String TAX_ENGINE_NAME_TAXWARE = "TAXWARE";
  //For Fixed Rate ..
  public static String TAX_KEY_FIXED_RATE = "FIXED_RATE";
  //For Store Rate ..
  public static String TAX_KEY_STORE_RATE = "STORE_RATE";
  public static String TAX_KEY_STORE_RATE_REGINOAL = "STORE_RATE_REGIONAL";
  //For Taxware ..
  public static String TAX_KEY_TAXWARE = "TAXWARE";
  //For Canada ..
  public static String TAX_KEY_CANADA_GST = "CANADA_GST";
  public static String TAX_KEY_CANADA_PST = "CANADA_PST";
  public static String TAX_KEY_CANADA_HST = "CANADA_HST";
  //////////////////////
  //  VALUE ADDED TAX //
  /////////////////////
  public static String TAX_ENGINE_NAME_NO_VAT = "NOVAT";
  public static String TAX_ENGINE_NAME_VAT_UK = "VATUK";
  //For VAT UK ..
  public static String TAX_KEY_VAT_UK_STANDARD = "VAT_UK_STANDARD";
  public static String TAX_KEY_VAT_UK_REDUCED = "VAT_UK_REDUCED";
  public static String TAX_KEY_VAT_UK_ZERO = "VAT_UK_ZERO";
}

