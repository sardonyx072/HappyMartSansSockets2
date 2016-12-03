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
	private static final int MAX_TRANSACTION_ID = 9999;
	
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
		this.setCanUpdateQuantityLastScan(false);
		this.setLastScanWasItemToPurchase(false);
		this.setLastScanWasItemToReturn(false);
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
	
	public Employee getEmployeeByID(int id) {
		for (Employee e : this.getEmployees()) {
			if (e.getID() == id) {
				return e;
			}
		}
		return null;
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
	
	public ItemQuantityManaged getItemfromInventoryByID(int id) {
		for (ItemQuantityManaged i : this.getInventory()) {
			if (i.getType().getID() == id) {
				return i;
			}
		}
		return null;
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
	
	public boolean signOut() {
		if (this.getEmployeeSignedIn() != null) {
			this.saveEmployees();
			this.saveInventory();
			this.saveDrawer();
			return true;
		}
		else {
			return false;
		}
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
	
	public void removeItemFromPurchase(int index) {
		this.itemsToPurchase.remove(index);
	}
	
	public void removeItemFromPurchase(int index, int quantity) {
		this.itemsToPurchase.get(index).removeQuantity(quantity);
		if (this.itemsToPurchase.get(index).getQuantity() == 0) {
			this.itemsToPurchase.remove(index);
		}
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
	
	public boolean isValidReturn (ItemQuantity items, int transactionID) {
		File[] files = new File(TRANSACTIONS_DIRECTORY).listFiles();
		for (File f : files) {
			if (f.getName().equals(transactionID + ".txt")) {
				Transaction t = this.loadTransaction(transactionID);
				for (ItemQuantity i : t.getPurchased()) {
					if (i.getType().getID() == items.getType().getID() && i.getQuantity() > items.getQuantity()) {
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}
	
	public void addItemToReturn(ItemQuantity item) {
		this.itemsToReturn.add(item);
		this.setCanUpdateQuantityLastScan(true);
		this.setLastScanWasItemToPurchase(false);
		this.setLastScanWasItemToReturn(true);
	}
	
	public void removeItemFromReturn(int index) {
		this.itemsToReturn.remove(index);
	}
	
	public void removeItemFromReturn(int index, int quantity) {
		this.itemsToReturn.get(index).removeQuantity(quantity);
		if (this.itemsToReturn.get(index).getQuantity() == 0) {
			this.itemsToReturn.remove(index);
		}
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
	
	public boolean lastScanWasItemToReturn() {
		return this.lastScanWasItemToReturn;
	}
	
	private void setLastScanWasItemToReturn(boolean flag) {
		this.lastScanWasItemToReturn = flag;
	}
	
	public ItemQuantity getLastItemScanned() {
		if (this.canUpdateQuantityLastScan() && this.lastScanWasItemToPurchase()) {
			return this.getItemsToPurchase().get(this.getItemsToPurchase().size()-1);
		}
		else if (this.canUpdateQuantityLastScan() && this.lastScanWasItemToReturn()) {
			return this.getItemsToReturn().get(this.getItemsToReturn().size()-1);
		}
		else {
			return null;
		}
	}
	
	public void updateLastItemScanned(int quantity) {
		if (this.canUpdateQuantityLastScan() && this.lastScanWasItemToPurchase()) {
			this.updateQuantityOfItemToPurchase(this.getItemsToPurchase().size()-1, quantity);
		}
		else if (this.canUpdateQuantityLastScan() && this.lastScanWasItemToReturn()) {
			this.updateQuantityOfItemToReturn(this.getItemsToReturn().size()-1, quantity);
		}
	}
	
	public int getCartTotal() {
		return this.getItemsToPurchaseSubtotal() - this.getItemsToReturnSubtotal();
	}
	
	public String getCartTotalAsString() {
		return NumberFormat.getCurrencyInstance().format(this.getCartTotal()/100.0);
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
	
	private int generateNextTransactionID() {
		File[] files = new File(TRANSACTIONS_DIRECTORY).listFiles();
		for (int i = 0; i < MAX_TRANSACTION_ID; i++) {
			boolean alreadyExists = false;
			for (File f : files) {
				if (Integer.parseInt(f.getName().substring(0,f.getName().indexOf('.'))) == i) {
					alreadyExists = true;
					break;
				}
			}
			if(!alreadyExists) {
				return i;
			}
		}
		throw new IllegalStateException();
	}
	
	public void saveTransaction() {
		try {
			Transaction t = new Transaction(this.generateNextTransactionID(),this.getEmployeeSignedIn(),this.getReferencedTransactionIDs(),this.getItemsToPurchase(),this.getItemsToReturn(),new Date());
			BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_DIRECTORY + t.getID() + ".txt"));
			writer.write(t.getID() + "\n" + t.getEmployee().getID() + "\n" + t.getTimestamp().toString());
			writer.write("Referenced IDs:");
			for (Integer i : this.getReferencedTransactionIDs()) {
				writer.write("\n" + i);
			}
			writer.write("\n\nPurchased:");
			for (ItemQuantity i : this.getItemsToPurchase()) {
				writer.write(i.getType().getID() + "\t" + i.getQuantity());
			}
			writer.write("\n\nReturned:");
			for (ItemQuantity i : this.getItemsToReturn()) {
				writer.write(i.getType().getID() + "\t" + i.getQuantity());
			}
			writer.close();
		} catch (IOException e) {
			//what can ya do?
		}
	}
	
	public Transaction loadTransaction(int transactionID) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_DIRECTORY + transactionID + ".txt"));
			int id = Integer.parseInt(reader.readLine());
			int employeeID = Integer.parseInt(reader.readLine());
			Date timestamp = new Date(Date.parse(reader.readLine()));
			ArrayList<Integer> referencedIDs = new ArrayList<Integer>();
			ArrayList<ItemQuantity> purchased = new ArrayList<ItemQuantity>();
			ArrayList<ItemQuantity> returned = new ArrayList<ItemQuantity>();
			if (reader.readLine().equals("Referenced IDs:")) {
				String line;
				while (!(line = reader.readLine()).isEmpty()) {
					referencedIDs.add(Integer.parseInt(line));
				}
			}
			if (reader.readLine().equals("Purchased:")) {
				String line;
				while (!(line = reader.readLine()).isEmpty()) {
					StringTokenizer tokenizer = new StringTokenizer(line,"\t");
					purchased.add(new ItemQuantity(this.getItemfromInventoryByID(Integer.parseInt(tokenizer.nextToken())).getType(),Integer.parseInt(tokenizer.nextToken())));
					if (tokenizer.hasMoreTokens())
						throw new IllegalArgumentException();
				}
			}
			if (reader.readLine().equals("Returned:")) {
				String line;
				while (!(line = reader.readLine()).isEmpty()) {
					StringTokenizer tokenizer = new StringTokenizer(line,"\t");
					purchased.add(new ItemQuantity(this.getItemfromInventoryByID(Integer.parseInt(tokenizer.nextToken())).getType(),Integer.parseInt(tokenizer.nextToken())));
					if (tokenizer.hasMoreTokens())
						throw new IllegalArgumentException();
				}
			}
			reader.close();
			return new Transaction(id,this.getEmployeeByID(employeeID),referencedIDs,purchased,returned,timestamp);
		} catch (IOException | IllegalArgumentException e) {
			return null;
		}
	}
	
	private ArrayList<Transaction> loadAllTransactions() {
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		File[] files = new File(TRANSACTIONS_DIRECTORY).listFiles();
		for (File f : files) {
			transactions.add(this.loadTransaction(Integer.parseInt(f.getName().substring(0,f.getName().indexOf('.')))));
		}
		return transactions;
	}
	
	private void writeGeneratedReport(String report, String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(REPORTS_DIRECTORY + filename + ".txt"));
			writer.write(report);
			writer.close();
		} catch (IOException e) {}
	}
	
	public void generateReport() {
		StringBuilder builder = new StringBuilder();
		ArrayList<Transaction> transactions = this.loadAllTransactions();
		int numberOfSales = 0;
		int totalSalesAmount = 0;
		int totalReturnsAmount = 0;
		int net = 0;
		builder.append("Store Report:");
		for (Transaction t : transactions) {
			numberOfSales++;
			totalSalesAmount += t.getPurchasedSubtotal();
			totalReturnsAmount += t.getReturnedSubtotal();
			builder.append("\n\n**********************************\n" + t);
		}
		net = totalSalesAmount - totalReturnsAmount;
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String totalMadeFormatted = formatter.format(totalSalesAmount/100.0);
		String meanMadeFormatted = formatter.format(1.0*totalSalesAmount/numberOfSales/100.0);
		String totalLostFormatted = formatter.format(totalReturnsAmount/100.0);
		String meanLostFormatted = formatter.format(1.0*totalReturnsAmount/numberOfSales/100.0);
		String totalNetFormatted = formatter.format(net/100.0);
		String meanNetFormatted = formatter.format(1.0*net/numberOfSales/100.0);
		builder.append("\n\n**********************************");
		builder.append("\nStatistics:");
		builder.append("\nNumber of transactions: " + numberOfSales);
		builder.append("\nTotal money made: " + totalMadeFormatted);
		builder.append("\nMean money made per transaction: " + meanMadeFormatted);
		builder.append("\nTotal money lost: " + totalLostFormatted);
		builder.append("\nMean money lost per transaction: " + meanLostFormatted);
		builder.append("\nNet money made: " + totalNetFormatted);
		builder.append("\nNet money made per transaction: " + meanNetFormatted);
		this.writeGeneratedReport(builder.toString(), "Store Report - " + new Date());
	}
	
	public void generateReport(Employee employee) {
		StringBuilder builder = new StringBuilder();
		ArrayList<Transaction> transactions = this.loadAllTransactions();
		int numberOfSales = 0;
		int totalSalesAmount = 0;
		int totalReturnsAmount = 0;
		int net = 0;
		builder.append("Store Report:");
		for (Transaction t : transactions) {
			if (t.getEmployee().equals(employee)) {
				numberOfSales++;
				totalSalesAmount += t.getPurchasedSubtotal();
				totalReturnsAmount += t.getReturnedSubtotal();
				builder.append("\n\n**********************************\n" + t);
			}
		}
		net = totalSalesAmount - totalReturnsAmount;
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String totalMadeFormatted = formatter.format(totalSalesAmount/100.0);
		String meanMadeFormatted = formatter.format(1.0*totalSalesAmount/numberOfSales/100.0);
		String totalLostFormatted = formatter.format(totalReturnsAmount/100.0);
		String meanLostFormatted = formatter.format(1.0*totalReturnsAmount/numberOfSales/100.0);
		String totalNetFormatted = formatter.format(net/100.0);
		String meanNetFormatted = formatter.format(1.0*net/numberOfSales/100.0);
		builder.append("\n\n**********************************");
		builder.append("\nStatistics:");
		builder.append("\nNumber of transactions: " + numberOfSales);
		builder.append("\nTotal money made: " + totalMadeFormatted);
		builder.append("\nMean money made per transaction: " + meanMadeFormatted);
		builder.append("\nTotal money lost: " + totalLostFormatted);
		builder.append("\nMean money lost per transaction: " + meanLostFormatted);
		builder.append("\nNet money made: " + totalNetFormatted);
		builder.append("\nNet money made per transaction: " + meanNetFormatted);
		this.writeGeneratedReport(builder.toString(), "Employee Report - " + employee + " - " + new Date());
	}
}
