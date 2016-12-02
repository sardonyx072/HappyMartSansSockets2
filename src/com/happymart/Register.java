package com.happymart;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

public class Register {
	private static final String DRAWER_FILE = "..\\res\\drawer.txt";
	private static final String EMPLOYEES_FILE = "..\\res\\employees.txt";
	private static final String INVENTORY_FILE = "..\\res\\inventory.txt";
	private static final String TRANSACTIONS_DIRECTORY = "..\\res\\transactions\\";
	private static final String REPORTS_DIRECTORY = "..\\res\\reports\\";
	private static final int MAX_EMPLOYEE_ID = 9999;
	private static final int MAX_ITEMTYPE_ID = 9999;
	
	private HashSet<Employee> employees;
	private HashSet<ItemQuantityManaged> inventory;
	private Employee employeeSignedIn;
	private int moneyInDrawer;
	private ArrayList<Integer> referencedTransactionIDs;
	private ArrayList<ItemQuantity> itemsToPurchase;
	private ArrayList<ItemQuantity> itemsToReturn;
	private boolean canUpdateQuantityLastScan;
	private boolean lastScanWasItemToPurchase;
	private boolean lastScanWasItemToReturn;

	public Register() {
		this.initEmployees();
		this.setEmployeeSignedIn(null);
		this.initInventory();
		this.initDrawer();
		this.setReferencedTransactionIDs(new ArrayList<Integer>());
		this.setItemsToPurchase(new ArrayList<ItemQuantity>());
		this.setItemsToReturn(new ArrayList<ItemQuantity>());
	}
	
	private void initEmployees() {
		HashSet<Employee> employees = new HashSet<Employee>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEES_FILE));
			while (reader.ready()) {
				StringTokenizer tokenizer = new StringTokenizer(reader.readLine(),"\t");
				int id = Integer.parseInt(tokenizer.nextToken());
				String name = tokenizer.nextToken();
				String user = tokenizer.nextToken();
				String pass = tokenizer.nextToken();
				if (tokenizer.hasMoreTokens())
					throw new IllegalArgumentException();
				employees.add(new Employee(id,name,user,pass));
			}
			reader.close();
		} catch (IOException | IllegalArgumentException e) {
			Employee defaultAdmin = new Employee(0,"admin","admin","password");
			if (!employees.contains(defaultAdmin))
				employees.add(defaultAdmin);
		}
		this.setEmployees(employees);
	}
	
	private void saveEmployees() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(EMPLOYEES_FILE));
			boolean first = true;
			for (Employee e : this.getEmployees()) {
				if (!first) {
					writer.write("\n");
				}
				writer.write(e.getID() + "\t" + e.getName() + "\t" + e.getUsername() + "\t" + e.getPassword());
				first = false;
			}
			writer.close();
		} catch (IOException e) {
			//what can ya do?
		}
	}

	public HashSet<Employee> getEmployees() {
		return employees;
	}

	private void setEmployees(HashSet<Employee> employees) {
		this.employees = employees;
		this.saveEmployees();
	}
	
	public void addEmployee(Employee employee) {
		this.employees.add(employee);
		this.saveEmployees();
		this.setCanUpdateQuantityLastScan(false);
	}
	
	public int generateNextEmployeeID() {
		for (int i = 0; i <= MAX_EMPLOYEE_ID; i++) {
			boolean alreadyExists = false;
			for (Employee e : this.getEmployees()) {
				if (e.getID() == i) {
					alreadyExists = true;
					break;
				}
			}
			if (!alreadyExists) {
				return i;
			}
		}
		throw new IllegalStateException();
	}
	
	private void initInventory() {
		HashSet<ItemQuantityManaged> inventory = new HashSet<ItemQuantityManaged>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE));
			while (reader.ready()) {
				StringTokenizer tokenizer = new StringTokenizer(reader.readLine(),"\t");
				int id = Integer.parseInt(tokenizer.nextToken());
				String name = tokenizer.nextToken();
				int price = Integer.parseInt(tokenizer.nextToken());
				int quantity = Integer.parseInt(tokenizer.nextToken());
				int threshold = Integer.parseInt(tokenizer.nextToken());
				if (tokenizer.hasMoreTokens())
					throw new IllegalArgumentException();
				inventory.add(new ItemQuantityManaged(new ItemType(id,name,price),quantity,threshold));
			}
			reader.close();
		} catch (IOException | IllegalArgumentException e) {
			//what can ya do?
		}
		this.setInventory(inventory);
	}
	
	private void saveInventory() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTORY_FILE));
			boolean first = true;
			for (ItemQuantityManaged i : this.getInventory()) {
				if (!first) {
					writer.write("\n");
				}
				writer.write(i.getType().getID() + "\t" + i.getType().getName() + "\t" + i.getType().getPrice() + "\t" + i.getQuantity() + "\t" + i.getThreshold());
				first = false;
			}
			writer.close();
		} catch (IOException e) {
			//what can ya do?
		}
	}
	
	public HashSet<ItemQuantityManaged> getInventory() {
		return this.inventory;
	}
	
	private void setInventory(HashSet<ItemQuantityManaged> inventory) {
		this.inventory = inventory;
		this.saveInventory();
	}
	
	public void addInventory(ItemQuantityManaged item) {
		this.inventory.add(item);
		this.saveInventory();
		this.setCanUpdateQuantityLastScan(false);
	}
	
	public int generateNextItemTypeID() {
		for (int i = 0; i <= MAX_ITEMTYPE_ID; i++) {
			boolean alreadyExists = false;
			for (ItemQuantityManaged j : this.getInventory()) {
				if (j.getType().getID() == i) {
					alreadyExists = true;
					break;
				}
			}
			if (!alreadyExists) {
				return i;
			}
		}
		throw new IllegalStateException();
	}

	public Employee getEmployeeSignedIn() {
		return employeeSignedIn;
	}

	public void setEmployeeSignedIn(Employee employeeSignedIn) {
		this.employeeSignedIn = employeeSignedIn;
		this.setCanUpdateQuantityLastScan(false);
	}
	
	public boolean signInWithEmployeeCredentials(String user, String pass) {
		for (Employee e : this.getEmployees()) {
			if (e.getUsername().equals(user) && e.getPassword().equals(pass)) {
				this.setEmployeeSignedIn(e);
				return true;
			}
		}
		return false;
	}
	
	public boolean credentialsMatchEmployeeSignedIn(String user, String pass) {
		return this.getEmployeeSignedIn().getUsername().equals(user) && this.getEmployeeSignedIn().getPassword().equals(pass);
	}
	
	private void initDrawer() {
		int moneyInDrawer;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(DRAWER_FILE));
			moneyInDrawer = Integer.parseInt(reader.readLine());
			reader.close();
		} catch (NumberFormatException | IOException e) {
			moneyInDrawer = 0;
		}
		this.setMoneyInDrawer(moneyInDrawer);
	}
	
	private void saveDrawer() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(DRAWER_FILE));
			writer.write(this.getMoneyInDrawer());
			writer.close();
		} catch (IOException e) {
			//what can ya do?
		}
	}

	public int getMoneyInDrawer() {
		return moneyInDrawer;
	}
	
	public String getMoneyInDrawerAsString() {
		return NumberFormat.getCurrencyInstance().format(this.getMoneyInDrawer()/100.0);
	}

	public void setMoneyInDrawer(int moneyInDrawer) { //cannot be negative
		this.moneyInDrawer = moneyInDrawer;
		this.saveDrawer();
	}
	
	public void addMoneyToDrawer(int amount) { //cannot be negative
		this.setMoneyInDrawer(this.getMoneyInDrawer()+amount);
	}
	
	public void removeMoneyFromDrawer(int amount) { //cannot be negative
		this.setMoneyInDrawer(this.getMoneyInDrawer()-amount);
	}

	public ArrayList<Integer> getReferencedTransactionIDs() {
		return referencedTransactionIDs;
	}

	private void setReferencedTransactionIDs(ArrayList<Integer> referencedTransactionIDs) {
		this.referencedTransactionIDs = referencedTransactionIDs;
	}

	public ArrayList<ItemQuantity> getItemsToPurchase() {
		return itemsToPurchase;
	}
	
	public ArrayList<ItemQuantity> getFlattenedItemsToPurchase() {
		ArrayList<ItemQuantity> flatList = new ArrayList<ItemQuantity>();
		for (int i = 0; i < this.getItemsToPurchase().size(); i++) {
			boolean exists = false;
			for (int j = 0; j < flatList.size(); j++) {
				if (flatList.get(j).equals(this.getItemsToPurchase().get(i))) {
					flatList.get(j).addQuantity(this.getItemsToPurchase().get(i).getQuantity());
					exists = true;
				}
			}
			if (!exists) {
				flatList.add(this.getItemsToPurchase().get(i));
			}
		}
		return flatList;
	}
	
	public int getItemsToPurchaseSubtotal() {
		int subtotal = 0;
		for (ItemQuantity i : this.getItemsToPurchase())
			subtotal += i.getType().getPrice()*i.getQuantity();
		return subtotal;
	}

	private void setItemsToPurchase(ArrayList<ItemQuantity> itemsToPurchase) {
		this.itemsToPurchase = itemsToPurchase;
	}
	
	public void addItemToPurchase(ItemQuantity item) {
		this.itemsToPurchase.add(item);
		this.setCanUpdateQuantityLastScan(true);
		this.setLastScanWasItemToPurchase(true);
		this.setLastScanWasItemToReturn(false);
	}
	
	public void updateQuantityOfItemToPurchase(int index, int quantity) {
		this.itemsToPurchase.get(index).setQuantity(quantity);
	}

	public ArrayList<ItemQuantity> getItemsToReturn() {
		return itemsToReturn;
	}
	public ArrayList<ItemQuantity> getFlattenedItemsToReturn() {
		ArrayList<ItemQuantity> flatList = new ArrayList<ItemQuantity>();
		for (int i = 0; i < this.getItemsToReturn().size(); i++) {
			boolean exists = false;
			for (int j = 0; j < flatList.size(); j++) {
				if (flatList.get(j).equals(this.getItemsToReturn().get(i))) {
					flatList.get(j).addQuantity(this.getItemsToReturn().get(i).getQuantity());
					exists = true;
				}
			}
			if (!exists) {
				flatList.add(this.getItemsToReturn().get(i));
			}
		}
		return flatList;
	}
	
	public int getItemsToReturnSubtotal() {
		int subtotal = 0;
		for (ItemQuantity i : this.getItemsToReturn())
			subtotal += i.getType().getPrice()*i.getQuantity();
		return subtotal;
	}

	private void setItemsToReturn(ArrayList<ItemQuantity> itemsToReturn) {
		this.itemsToReturn = itemsToReturn;
	}
	
	public void addItemToReturn(ItemQuantity item) {
		this.itemsToReturn.add(item);
		this.setCanUpdateQuantityLastScan(true);
		this.setLastScanWasItemToPurchase(false);
		this.setLastScanWasItemToReturn(true);
	}
	
	public void updateQuantityOfItemToReturn(int index, int quantity) {
		this.itemsToReturn.get(index).setQuantity(quantity);
	}
	
	public void clearCart() {
		this.setReferencedTransactionIDs(new ArrayList<Integer>());
		this.setItemsToPurchase(new ArrayList<ItemQuantity>());
		this.setItemsToReturn(new ArrayList<ItemQuantity>());
		this.setCanUpdateQuantityLastScan(false);
	}

	public boolean canUpdateQuantityLastScan() {
		return canUpdateQuantityLastScan;
	}

	private void setCanUpdateQuantityLastScan(boolean canUpdateQuantityLastScan) {
		this.canUpdateQuantityLastScan = canUpdateQuantityLastScan;
	}
	
	private boolean lastScanWasItemToPurchase() {
		return this.lastScanWasItemToPurchase;
	}
	
	private void setLastScanWasItemToPurchase(boolean flag) {
		this.lastScanWasItemToPurchase = flag;
	}
	
	public boolean lastScanWasItemtoReturn() {
		return this.lastScanWasItemToReturn;
	}
	
	private void setLastScanWasItemToReturn(boolean flag) {
		this.lastScanWasItemToReturn = flag;
	}
	
	public int getCartTotal() {
		return this.getItemsToPurchaseSubtotal() - this.getItemsToReturnSubtotal();
	}
	
	public String getCartAsString() {
		StringBuilder builder = new StringBuilder();
		if (this.getItemsToReturn().size() > 0) {
			int i = 0;
			builder.append("Referenced Transaction Numbers:");
			for (int j = 0; j < this.getReferencedTransactionIDs().size(); j++) {
				builder.append("\n\t" + this.getReferencedTransactionIDs().get(j));
			}
			builder.append("\n\nPurchased:");
			for (int j = 0; j < this.getItemsToPurchase().size(); j++) {
				builder.append("\n\t" + (++i) + ". " + this.getItemsToPurchase().get(j).getReceiptItemString());
			}
			builder.append("\nSubtotal: " + NumberFormat.getCurrencyInstance().format(this.getItemsToPurchaseSubtotal()/100.0));
			builder.append("\n\nReturned:");
			for (int j = 0; j < this.getItemsToReturn().size(); j++) {
				builder.append("\n\t" + (++i) + ". " + this.getItemsToReturn().get(j).getReceiptItemString());
			}
			builder.append("\nSubtotal: " + NumberFormat.getCurrencyInstance().format(this.getItemsToReturnSubtotal()/100.0));
		}
		else {
			boolean first = true;
			for (int i = 0; i < this.getItemsToPurchase().size(); i++) {
				if (!first) {
					builder.append("\n");
				}
				builder.append(i + ". " + this.getItemsToPurchase().get(i).getReceiptItemString());
				first = false;
			}
		}
		return builder.toString();
	}
}
