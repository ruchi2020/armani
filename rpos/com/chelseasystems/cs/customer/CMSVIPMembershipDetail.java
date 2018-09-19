package com.chelseasystems.cs.customer;

import java.util.Date;

import com.chelseasystems.cr.business.BusinessObject;

public class CMSVIPMembershipDetail extends BusinessObject{

	
	private String membership_id;
	private String customer_id;
	private Date expiry_dt;
	private String brand;
	private double discount_pct;
	
	public CMSVIPMembershipDetail(){
		
	}

	public String getCustomer_id() {
		return this.customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public Date getExpiry_dt() {
		return this.expiry_dt;
	}

	public void setExpiry_dt(Date expiry_dt) {
		this.expiry_dt = expiry_dt;
	}

	public String getMembership_id() {
		return this.membership_id;
	}

	public void setMembership_id(String membership_id) {
		this.membership_id = membership_id;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getDiscount_pct() {
		return this.discount_pct;
	}

	public void setDiscount_pct(double discount_pct) {
		this.discount_pct = discount_pct;
	}
}
