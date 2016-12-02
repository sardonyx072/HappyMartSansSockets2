package com.happymart;

import java.text.NumberFormat;

public class ItemType implements Comparable {
	private int id;
	private String name;
	private int price;
	
	public ItemType(int id, String name, int price) {
		this.setID(id);
		this.setName(name);
		this.setPrice(price);
	}

	public int getID() {
		return id;
	}

	private void setID(int id) { //cannot be negative
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) { //cannot be blank or null
		this.name = name;
	}

	public int getPrice() {
		return price;
	}
	
	public String getPriceString() {
		return NumberFormat.getCurrencyInstance().format(this.getPrice()/100.0);
	}

	public void setPrice(int price) { //cannot be negative
		this.price = price;
	}
	
	@Override
	public int compareTo(Object arg0) {
		return this.getName().compareTo(((ItemType)arg0).getName());
	}
	
	@Override
	public boolean equals(Object arg0) {
		return this.getID() == ((ItemType)arg0).getID();
	}
	
	public String toString() {
		return this.getName();
	}
}
