package com.happymart;

public class ItemQuantityManaged extends ItemQuantity {
	private int threshold;
	private boolean moreNeeded;
	
	public ItemQuantityManaged (ItemType type, int quantity, int threshold) {
		super(type,quantity);
		this.setThreshold(threshold);
	}
	
	public int getThreshold() {
		return this.threshold;
	}
	
	public void setThreshold(int threshold) { //cannot be negative
		this.threshold = threshold;
	}
	
	public boolean isMoreNeeded() {
		return this.moreNeeded;
	}
	
	private void setMoreNeededAutomated() {
		this.moreNeeded = this.getQuantity() <= this.getThreshold();
	}
	
	@Override
	public void setQuantity(int quantity) { //cannot be negative
		super.setQuantity(quantity);
		this.setMoreNeededAutomated();
	}
	
	@Override
	public void addQuantity(int amount) { //cannot be negative
		super.addQuantity(amount);
		this.setMoreNeededAutomated();
	}
	
	@Override
	public void removeQuantity(int amount) { //cannot be negative
		super.removeQuantity(amount);
		this.setMoreNeededAutomated();
	}
}
