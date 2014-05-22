package com.innovorder.innovorder.model;

import java.util.ArrayList;

public class CarteItem {
	private long id;
	private String type;
	private String name;
	private String description;
	private double price;
	private double vat;
	private String mediaUrl;
	private long parentId = -1;
	private ArrayList<CarteItem> children = new ArrayList<CarteItem>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double d) {
		this.price = d;
	}
	public double getVat() {
		return vat;
	}
	public void setVat(double vat) {
		this.vat = vat;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public ArrayList<CarteItem> getChildren() {
		return children;
	}
	public void addChild(CarteItem child) {
		this.children.add(child);
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	
}

