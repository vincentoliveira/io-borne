package com.innovorder.innovorder.model;

import java.util.ArrayList;
import java.util.Date;

public class Cart {
	private long id;
	private String orderName;
	private Date startOrderDate;
	private Date endOrderDate;
	private ArrayList<CarteItem> items = new ArrayList<CarteItem>();
	private static Cart instance = null;
	
	
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Date getStartOrderDate() {
		return startOrderDate;
	}

	public void setStartOrderDate(Date startOrderDate) {
		this.startOrderDate = startOrderDate;
	}

	public Date getEndOrderDate() {
		return endOrderDate;
	}

	public void setEndOrderDate(Date endOrderDate) {
		this.endOrderDate = endOrderDate;
	}

	public ArrayList<CarteItem> getItems() {
		return items;
	}

	public void addItem(CarteItem item) {
		this.items.add(item);
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

}
