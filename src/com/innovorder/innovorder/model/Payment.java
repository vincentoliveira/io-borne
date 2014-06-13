package com.innovorder.innovorder.model;

import java.util.Date;

public class Payment {

	private Date date = new Date();
	private double amount = 0;
	private String type = "";
	private String status = "";
	private String comments = "";
	private String transactionId = "";
	private boolean alreadyCalled = false;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public void addComment(String comment ) {
		comments = comments + comment + ";";
	}
	public boolean isAlreadyCalled() {
		return alreadyCalled;
	}
	public void setAlreadyCalled() {
		this.alreadyCalled = true;
	}
}
