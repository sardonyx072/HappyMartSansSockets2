package com.happymart;

import java.text.NumberFormat;

public class ItemQuantity {
	private ItemType type;
	private int quantity;
	
	public ItemQuantity(ItemType type, int quantity) {
		this.setType(type);
		this.setQuantity(quantity);
	}
	
	public ItemType getType() {
		return this.type;
	}
	
	public void setType(ItemType type) {
		this.type = type;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(int quantity) { //cannot be negative
		this.quantity = quantity;
	}
	
	public void addQuantity(int amount) { //cannot be negative
		this.setQuantity(this.getQuantity()+amount);
	}
	
	public void removeQuantity(int amount) { //cannot be negative
		this.setQuantity(this.getQuantity()-amount);
	}
	
	public int getTotalPrice() {
		return this.getType().getPrice()*this.getQuantity();
	}
	
	public String getTotalPriceAsString() {
		return NumberFormat.getCurrencyInstance().format(this.getTotalPrice()/100.0);
	}
	
	public String getReceiptItemString() {
		return "" + this.getTotalPriceAsString() + " = (" + this.getQuantity() + " x " + this.getType().getPriceString() + ") " + this.getType().getName();
	}
}
