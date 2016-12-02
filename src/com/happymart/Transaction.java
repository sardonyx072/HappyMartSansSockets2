package com.happymart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;

public class Transaction {
	private int id;
	private Employee employee;
	private ArrayList<Integer> referencedIDs;
	private ArrayList<ItemQuantity> purchased;
	private ArrayList<ItemQuantity> returned;
	private Date timestamp;
	
	private int purchasedSubtotal;
	private int returnedSubtotal;
	private int total;
	
	public Transaction(int id, Employee employee, ArrayList<Integer> refencedIDs, ArrayList<ItemQuantity> purchased, ArrayList<ItemQuantity> returned, Date timestamp) {
		this.setID(id);
		this.setEmployee(employee);
		this.setReferencedIDs(refencedIDs);
		this.setPurchased(purchased);
		this.setReturned(returned);
		this.setTimestamp(timestamp);
		this.setPurchasedSubtotal(0);
		this.setReturnedSubtotal(0);
		this.setTotal(0);
		for (ItemQuantity i : this.getPurchased())
			this.setPurchasedSubtotal(this.getPurchasedSubtotal()+i.getTotalPrice());
		for (ItemQuantity i : this.getReturned())
			this.setReturnedSubtotal(this.getReturnedSubtotal()+i.getTotalPrice());
		this.setTotal(this.getPurchasedSubtotal() - this.getReturnedSubtotal());
	}
	
	public int getID() {
		return id;
	}

	private void setID(int id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	private void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public ArrayList<Integer> getReferencedIDs() {
		return referencedIDs;
	}

	private void setReferencedIDs(ArrayList<Integer> referencedIDs) {
		this.referencedIDs = referencedIDs;
	}

	public ArrayList<ItemQuantity> getPurchased() {
		return purchased;
	}

	private void setPurchased(ArrayList<ItemQuantity> purchased) {
		this.purchased = purchased;
	}

	public ArrayList<ItemQuantity> getReturned() {
		return returned;
	}

	private void setReturned(ArrayList<ItemQuantity> returned) {
		this.returned = returned;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	private void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getPurchasedSubtotal() {
		return this.purchasedSubtotal;
	}
	
	private void setPurchasedSubtotal(int purchasedSubtotal) { //cannot be negative
		this.purchasedSubtotal = purchasedSubtotal;
		this.setTotal(this.getPurchasedSubtotal() - this.getReturnedSubtotal());
	}
	
	public int getReturnedSubtotal() {
		return this.returnedSubtotal;
	}
	
	private void setReturnedSubtotal(int returnedSubtotal) { //cannot be negative
		this.returnedSubtotal = returnedSubtotal;
		this.setTotal(this.getPurchasedSubtotal() - this.getReturnedSubtotal());
	}
	
	public int getTotal() {
		return this.total;
	}
	
	private void setTotal(int total) {
		this.total = total;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Record of Transaction #" + this.getID() + " Completed " + this.getTimestamp());
		builder.append("\nEmployee: " + this.getEmployee().getName());
		if (this.getReturned().size() > 0) {
			builder.append("\n\nReferenced Transaction Numbers:");
			for (Integer i : this.getReferencedIDs())
				builder.append("\n\t" + i);
			builder.append("\n\nPurchased:");
			for (ItemQuantity i : this.getPurchased())
				builder.append("\n\t" + i.getReceiptItemString());
			builder.append("\nSubtotal: " + NumberFormat.getCurrencyInstance().format(this.getPurchasedSubtotal()/100.0));
			builder.append("\n\nReturned:");
			for (ItemQuantity i : this.getReturned())
				builder.append("\n\t" + i.getReceiptItemString());
			builder.append("\nSubtotal: " + NumberFormat.getCurrencyInstance().format(this.getReturnedSubtotal()/100.0));
			
			builder.append("\n\nTotal: " + NumberFormat.getCurrencyInstance().format(this.getTotal()/100.0));
		}
		else {
			builder.append("\n");
			for (ItemQuantity i : this.getPurchased())
				builder.append("\n" + i.getReceiptItemString());
			builder.append("\n\nTotal: " + NumberFormat.getCurrencyInstance().format(this.getTotal()/100.0));
		}
		return builder.toString();
	}
}
