/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ga.fs.fsbridge.local;

/**
 *
 * @author Yves Agbessi
 */
public class ARMFSBTransactionItem {
    
    public String ItemID;
    public String Description;
    public String UnitDescription;
    public double RegularUnitPrice;
    public double ActualUnitPrice;
    public int Quantity;
    public double TotalLineAmount;
    public String TaxGroupCode;
    public double TaxPercent;
    public double TaxableAmount;
    public double TaxAmount;
    public String DiscountID;
    public String DiscountName;
    public double DiscountPercentage;
    public double DiscountUnitAmount;
    public double DiscountAmount;
    
    
    
    /**
     * @return the ItemID
     */
    public String getItemID() {
        return ItemID;
    }

    /**
     * @param ItemID the ItemID to set
     */
    public void setItemID(String ItemID) {
        this.ItemID = ItemID;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    public String getDescription(ARMFSBTransactionItem item) {
        return item.getDescription();
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     * @return the UnitDescription
     */
    public String getUnitDescription() {
        return UnitDescription;
    }

    public String getUnitDescription(ARMFSBTransactionItem item) {
        return item.getUnitDescription();
    }

    /**
     * @param UnitDescription the UnitDescription to set
     */
    public void setUnitDescription(String UnitDescription) {
        this.UnitDescription = UnitDescription;
    }

    /**
     * @return the RegularUnitPrice
     */
    public double getRegularUnitPrice() {
        return RegularUnitPrice;
    }

    public double getRegularUnitPrice(ARMFSBTransactionItem item) {
        return item.getRegularUnitPrice();
    }

    /**
     * @param RegularUnitPrice the RegularUnitPrice to set
     */
    public void setRegularUnitPrice(double RegularUnitPrice) {
        this.RegularUnitPrice = RegularUnitPrice;
    }

    /**
     * @return the ActualUnitPrice
     */
    public double getActualUnitPrice() {
        return ActualUnitPrice;
    }

    public double getActualUnitPrice(ARMFSBTransactionItem item) {
        return item.getActualUnitPrice();
    }

    /**
     * @param ActualUnitPrice the ActualUnitPrice to set
     */
    public void setActualUnitPrice(double ActualUnitPrice) {
        this.ActualUnitPrice = ActualUnitPrice;
    }

    /**
     * @return the Quantity
     */
    public int getQuantity() {
        return Quantity;
    }

    public int getQuantity(ARMFSBTransactionItem item) {
        return item.getQuantity();
    }

    /**
     * @param Quantity the Quantity to set
     */
    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    /**
     * @return the TotalLineAmount
     */
    public double getTotalLineAmount() {
        return TotalLineAmount;
    }

    public double getTotalLineAmount(ARMFSBTransactionItem item) {
        return item.getTotalLineAmount();
    }

    /**
     * @param TotalLineAmount the TotalLineAmount to set
     */
    public void setTotalLineAmount(double TotalLineAmount) {
        this.TotalLineAmount = TotalLineAmount;
    }

    /**
     * @return the TaxGroupCode
     */
    public String getTaxGroupCode() {
        return TaxGroupCode;
    }

    public String getTaxGroupCode(ARMFSBTransactionItem item) {
        return item.getTaxGroupCode();
    }

    /**
     * @param TaxGroupCode the TaxGroupCode to set
     */
    public void setTaxGroupCode(String TaxGroupCode) {
        this.TaxGroupCode = TaxGroupCode;
    }

    /**
     * @return the TaxPercent
     */
    public double getTaxPercent() {
        return TaxPercent;
    }

    public double getTaxPercent(ARMFSBTransactionItem item) {
        return item.getTaxPercent();
    }

    /**
     * @param TaxPercent the TaxPercent to set
     */
    public void setTaxPercent(double TaxPercent) {
        this.TaxPercent = TaxPercent;
    }

    /**
     * @return the TaxableAmount
     */
    public double getTaxableAmount() {
        return TaxableAmount;
    }

    public double getTaxableAmount(ARMFSBTransactionItem item) {
        return item.getTaxableAmount();
    }

    /**
     * @param TaxableAmount the TaxableAmount to set
     */
    public void setTaxableAmount(double TaxableAmount) {
        this.TaxableAmount = TaxableAmount;
    }

    /**
     * @return the TaxAmount
     */
    public double getTaxAmount() {
        return TaxAmount;
    }

    public double getTaxAmount(ARMFSBTransactionItem item) {
        return item.getTaxAmount();
    }

    /**
     * @param TaxAmount the TaxAmount to set
     */
    public void setTaxAmount(double TaxAmount) {
        this.TaxAmount = TaxAmount;
    }

    /**
     * @return the DiscountID
     */
    public String getDiscountID() {
        return DiscountID;
    }

    public String getDiscountID(ARMFSBTransactionItem item) {
        return item.getDiscountID();
    }

    /**
     * @param DiscountID the DiscountID to set
     */
    public void setDiscountID(String DiscountID) {
        this.DiscountID = DiscountID;
    }

    /**
     * @return the DiscountName
     */
    public String getDiscountName() {
        return DiscountName;
    }

    public String getDiscountName(ARMFSBTransactionItem item) {
        return item.getDiscountName();
    }

    /**
     * @param DiscountName the DiscountName to set
     */
    public void setDiscountName(String DiscountName) {
        this.DiscountName = DiscountName;
    }

    /**
     * @return the DiscountPercentage
     */
    public double getDiscountPercentage() {
        return DiscountPercentage;
    }

    public double getDiscountPercentage(ARMFSBTransactionItem item) {
        return item.getDiscountPercentage();
    }

    /**
     * @param DiscountPercentage the DiscountPercentage to set
     */
    public void setDiscountPercentage(double DiscountPercentage) {
        this.DiscountPercentage = DiscountPercentage;
    }

    /**
     * @return the DiscountUnitAmount
     */
    public double getDiscountUnitAmount() {
        return DiscountUnitAmount;
    }

    public double getDiscountUnitAmount(ARMFSBTransactionItem item) {
        return item.getDiscountUnitAmount();
    }

    /**
     * @param DiscountUnitAmount the DiscountUnitAmount to set
     */
    public void setDiscountUnitAmount(double DiscountUnitAmount) {
        this.DiscountUnitAmount = DiscountUnitAmount;
    }

    /**
     * @return the DiscountAmount
     */
    public double getDiscountAmount() {
        return DiscountAmount;
    }

    public double getDiscountAmount(ARMFSBTransactionItem item) {
        return item.getDiscountAmount();
    }

    /**
     * @param DiscountAmount the DiscountAmount to set
     */
    public void setDiscountAmount(double DiscountAmount) {
        this.DiscountAmount = DiscountAmount;
    }



    public ARMFSBTransactionItem(String ItemID, String Description, String UnitDescription,
            double RegularUnitPrice, double ActualUnitPrice, int Quantity, double TotalLineAmount,
            String TaxGroupCode, double TaxPercent, double TaxableAmount, double TaxAmount,
            String DiscountID, String DiscountName, double DiscountPercentage,
            double DiscountUnitAmount, double DiscountAmount) {

        this.ItemID = ItemID;
        this.Description = Description;
        this.UnitDescription = UnitDescription;
        this.RegularUnitPrice = RegularUnitPrice;
        this.ActualUnitPrice = ActualUnitPrice;
        this.Quantity = Quantity;
        this.TotalLineAmount = TotalLineAmount;
        this.TaxGroupCode = TaxGroupCode;
        this.TaxPercent = TaxPercent;
        this.TaxableAmount = TaxableAmount;
        this.TaxAmount = TaxAmount;
        this.DiscountID = DiscountID;
        this.DiscountName = DiscountName;
        this.DiscountPercentage = DiscountPercentage;
        this.DiscountUnitAmount = DiscountUnitAmount;
        this.DiscountAmount = DiscountAmount;
    }

}
