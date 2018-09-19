package com.chelseasystems.cs.v12basket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;

public class CMSV12Basket extends BusinessObject {
	private static final long serialVersionUID = 1L;
	private String mobileRegisterId;
	private String storeId;
	private String customerId;
	private String barcode;
	private String itemPrice;
	private String employeeId;
	private Double totalBasketAmount;
	private String transactionType;
	private Date trnTimestamp;
	private String trnComment;
	private String idVirtualSale;
	private String trnStatus;
	private String rowNumberId;
	private String itemQty;
	public static String open = "OPEN";
	public static String close = "CLOSED";
	public static String inProcess = "IN PROCESS";
	private ArrayList<String> itemList = new ArrayList<String>();
	private HashMap<String, Double> itemPriceMap = new HashMap<String, Double>();

	public CMSV12Basket() {
		mobileRegisterId = new String();
		storeId = new String();
		customerId = new String();
		barcode = new String();
		employeeId = new String();
		totalBasketAmount = new Double(0.0);
		transactionType = new String();
		trnTimestamp = new Date();
		trnComment = new String();
		idVirtualSale = new String();
		trnStatus = new String();
		rowNumberId = new String();
		itemPrice = new String();
		itemQty = new String();
	}

	public String getItemQty() {
		return itemQty;
	}

	public String getTotalItemNo() {
		return String.valueOf(itemList.size());
	}

	public String getItemTotalPrice() {
		Double itemPrice = Double.valueOf(this.itemPrice);
		int quantity = Integer.valueOf(this.itemQty);
		Double totalItemPrice = itemPrice * quantity;
		return String.valueOf(totalItemPrice);
	}

	public void setItemQty(String itemId, List<String> itemList) {
		int quantity = 0;
		for (String item : itemList) {
			if (item.equals(itemId))
				quantity++;
		}
		this.itemQty = String.valueOf(quantity);
	}

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getMobileRegisterId() {
		return mobileRegisterId;
	}

	public void setMobileRegisterId(String mobileRegisterId) {
		this.mobileRegisterId = mobileRegisterId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getTotalBasketAmount() {
		ArmCurrency total = null;
		totalBasketAmount = 0.0d;
		for (String itemId : itemList) {	
			Double price = itemPriceMap.get(itemId);
			totalBasketAmount += price;
		}
		total = new ArmCurrency(totalBasketAmount);
		return total.formattedStringValue();
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTrnTimestamp() {
		
		SimpleDateFormat simDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
		String date = simDate.format(trnTimestamp);
		return date;
	}

	public void setTrnTimestamp(Date trnTimestamp) {
		this.trnTimestamp = trnTimestamp;
	}

	public String getTrnComment() {
		return trnComment;
	}

	public void setTrnComment(String trnComment) {
		this.trnComment = trnComment;
	}

	public String getIdVirtualSale() {
		return idVirtualSale;
	}

	public void setIdVirtualSale(String idVirtualSale) {
		this.idVirtualSale = idVirtualSale;
	}

	public String getTrnStatus() {
		return trnStatus;
	}

	public void setTrnStatus(String trnStatus) {
		this.trnStatus = trnStatus;
	}

	public String getRowNumberId() {
		return rowNumberId;
	}

	public void setRowNumberId(String rowNumberId) {
		this.rowNumberId = rowNumberId;
	}

	public ArrayList<String> getItemList() {
		return itemList;
	}

	public void addItem(String itemId) {
		this.itemList.add(itemId);
	}

	public HashMap<String, Double> getItemPriceMap() {
		return itemPriceMap;
	}

	public void addItemPrice(String itemId, double price) {
		this.itemPriceMap.put(itemId, price);
	}

}
