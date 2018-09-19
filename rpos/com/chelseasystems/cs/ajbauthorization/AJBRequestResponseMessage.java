package com.chelseasystems.cs.ajbauthorization;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class is responsible for holding the AJB request and response message.
 * It contains an array of length 132(number of fields defined for AJB request
 * and response). This array contains all request or response fields and does
 * tostring(in case of request) or parse(in case of response).
 * 
 * @author Vivek M
 * 
 */
public class AJBRequestResponseMessage implements AJBRequestResponseConstants,
		Serializable {
	private static final long serialVersionUID = 2598423125066253082L;
	// This array represents all AJB request/response.
	protected String[] fields = null;

	// This is a constructor for AJBRequestResponseMessage. It initializes the
	// array with 132 (number of fields defined for AJB request and response).
	// Also fills the array with blank.
	public AJBRequestResponseMessage() {
		int size = 132;
		this.fields = new String[size];
		Arrays.fill(this.fields, 0, size, "");
	}
	
	public AJBRequestResponseMessage(int size) {
		this.fields = new String[size];
		Arrays.fill(this.fields, 0, size, "");
	}

	// This is a constructor for AJBRequestResponseMessage. It takes the
	// response string and then calls the parse method to fill the fields array
	// with response data.
	public AJBRequestResponseMessage(String data) {
		parse(data);
	}

	public String getValue(int index) {
		if (index < this.fields.length) {
			return this.fields[index];
		}

		return null;
	}

	// This method sets the given value in the provided index of the fields
	// array.
	public void setValue(int index, int value) {
		setValue(index, String.valueOf(value));
	}

	// This method sets the given value in the provided index of the fields
	// array.
	public void setValue(int index, String value) {
		if (value == null) {
			throw new NullPointerException("Value is null");
		}
		this.fields[index] = value;
	}

	// This method parses the response string and set it in the fields array.
	public void parse(String data) {
		if (data != null) {
			this.fields = data.split(",");
		} else {
			throw new NullPointerException("data is null");
		}
	}

	// This method takes all the fields of the fileds array and converts them
	// into a comma seperated string.
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (String value : this.fields) {
			builder.append(value).append(",");
		}

		builder.setLength(builder.length() - 1);
		return builder.toString();
	}
}
