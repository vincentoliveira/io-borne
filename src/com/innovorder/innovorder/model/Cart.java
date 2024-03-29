package com.innovorder.innovorder.model;

import java.util.ArrayList;
import java.util.Date;

public class Cart {
	private long serverId = 0;
	private double serverPrice;
	private String orderName;
	private Date startOrderDate;
	private ArrayList<CarteItem> items = new ArrayList<CarteItem>();
	private static Cart instance = null;
	private Payment payment;
	
	private Cart()
	{
	}
	
	public static Cart getInstance()
	{
		if (instance == null) {
			instance = new Cart();
		}
		
		return instance;
	}
	
	public void reinit() {
		this.serverId = 0;
		this.serverPrice = 0.0;
		this.startOrderDate = new Date();
		this.setOrderName("");
		this.items.clear();
		this.payment = null;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Date getStartOrderDate() {
		if (startOrderDate == null) {
			startOrderDate = new Date();
		}
		return startOrderDate;
	}

	public void setStartOrderDate(Date startOrderDate) {
		this.startOrderDate = startOrderDate;
	}

	public ArrayList<CarteItem> getItems() {
		return items;
	}

	public void removeItem(CarteItem toRemoveItem) {
		for (CarteItem item : items) {
			if (item.getId() == toRemoveItem.getId()) {
				items.remove(item);
				break;
			}
		}
	}

	public void addItem(CarteItem item) {
		this.items.add(0, item);
	}

	public int nbItem() {
		return this.items.size();
	}

	public double getTotal() {
		double total = 0;
		for (CarteItem item : items) {
			total += item.getPrice();
		}
		return total;
	}

	public double getServerPrice() {
		return serverPrice;
	}

	public void setServerPrice(double serverPrice) {
		this.serverPrice = serverPrice;
	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

}
