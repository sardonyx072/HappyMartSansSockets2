package com.happymart;

public class Application implements Runnable {
	
	private Register reg;
	
	public Application() {
		this.reg = new Register();
	}

	@Override
	public void run() {
		while (this.login()) {
			while(this.home()) {
				
			}
		}
	}
	
	private boolean login () {
		String input = "";
		boolean first = true;
		while (!InputType.isExit(input) && !InputType.isQuit(input)) {
			if (first) {
				input = Screen.LOGIN_FIRST_USERNAME.use();
				if (InputType.isUniversalExit(input)) {
					continue;
				}
				else {
					String username = input;
					input = Screen.LOGIN_FIRST_PASSWORD.use(username);
					if (InputType.isUniversalExit(input)) {
						continue;
					}
					else {
						String password = input;
						if (this.reg.signInWithEmployeeCredentials(username, password)) {
							return true;
						}
						else {
							first = false;
							continue;
						}
					}
				}
			}
			else {
				input = Screen.LOGIN_USERNAME.use();
				if (InputType.isUniversalExit(input)) {
					continue;
				}
				else {
					String username = input;
					input = Screen.LOGIN_PASSWORD.use(username);
					if (InputType.isUniversalExit(input)) {
						continue;
					}
					else {
						String password = input;
						if (this.reg.signInWithEmployeeCredentials(username, password)) {
							return true;
						}
						else {
							continue;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean home() {
		String[] opts = {"open drawer","return item","scan item","enter quantity of last item","remove item","check inventory","complete sale","clear sale","log out"};
		String input = "";
		boolean first = true;
		boolean unavailable = false;
		while (!InputType.isUniversalExit(input)) {
			if (first) {
				input = Screen.HOME_FIRST.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getCartTotalAsString(),this.reg.getCartAsString(),Screen.optionFormat(opts));
				unavailable = false;
			}
			else {
				if (unavailable) {
					input = Screen.HOME_UNAVAILABLE.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getCartTotalAsString(),this.reg.getCartAsString(),Screen.optionFormat(opts));
				}
				else {
					input = Screen.HOME.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getCartTotalAsString(),this.reg.getCartAsString(),Screen.optionFormat(opts));
					unavailable = false;
				}
			}
			switch (Integer.parseInt(input)) {
			case 0: //open drawer
				while(this.drawer());
				break;
			case 1: //return item
				while(this.scanReturn());
				break;
			case 2: //scan item
				while(this.scan());
				break;
			case 3: //set number of item
				if (this.reg.canUpdateQuantityLastScan())
					while(this.volume());
				else
					unavailable = true;
				break;
			case 4: //remove item
				if (this.reg.getItemsToPurchase().size() > 0 || this.reg.getItemsToReturn().size() > 0) {
					unavailable = true; //TODO
				}
				else {
					unavailable = true;
				}
				break;
			case 5: //check inventory
				break;
			case 6: //complete sale
				if (this.reg.getItemsToPurchase().size() > 0 || this.reg.getItemsToReturn().size() > 0) {
					unavailable = true; //TODO
				}
				else {
					unavailable = true;
				}
				break;
			case 7: //clear sale
				this.reg.clearCart();
				break;
			case 8: //log out
				this.reg.signOut();
				return false;
			default:
				first = false;
				break;
			}
		}
		return false;
	}
	
	private boolean drawer() {
		String[] opts = {"add money","take money","set money","close drawer"};
		String input = "";
		boolean first = true;
		while (!InputType.isUniversalExit(input)) {
			if (first) {
				input = Screen.DRAWER_HOME_FIRST.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getMoneyInDrawerAsString(),Screen.optionFormat(opts));
			}
			else {
				input = Screen.DRAWER_HOME.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getMoneyInDrawerAsString(),Screen.optionFormat(opts));
			}
			switch (Integer.parseInt(input)) {
			case 0: //add money
				input = Screen.DRAWER_ADD.use(this.reg.getEmployeeSignedIn().getName(),reg.getMoneyInDrawerAsString());
				if (!InputType.isCancel(input) && !InputType.isExit(input) && !InputType.isQuit(input)) {
					reg.addMoneyToDrawer((int)(Double.parseDouble(input)*100));
				}
				break;
			case 1: //take money
				boolean first_1 = true;
				while (!InputType.isUniversalExit(input)) {
					if (first_1) {
						input = Screen.DRAWER_TAKE_FIRST.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getMoneyInDrawerAsString());
					}
					else {
						input = Screen.DRAWER_TAKE.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getMoneyInDrawerAsString());
					}
					if (!InputType.isUniversalExit(input)) {
						if (this.reg.getMoneyInDrawer() >= ((int)(Double.parseDouble(input)*100))) {
							this.reg.removeMoneyFromDrawer((int)(Double.parseDouble(input)*100));
							break;
						}
						else {
							first_1 = false;
						}
					}
				}
				break;
			case 2: //set money
				input = Screen.DRAWER_SET.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getMoneyInDrawerAsString());
				if (!InputType.isUniversalExit(input)) {
					reg.setMoneyInDrawer((int)(Double.parseDouble(input)*100));
				}
				break;
			case 3: //close drawer
				return false;
			default:
				first = false;
				continue;
			}
		}
		return false; //temp
	}
	
	private boolean scanReturn() {
		String input = "";
		boolean first_receipt = true;
		while (!InputType.isUniversalExit(input)) {
			if (first_receipt) {
				input = Screen.RETURN_TRANSACTION_FIRST.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getCartTotalAsString(),this.reg.getCartAsString());
			}
			else {
				input = Screen.RETURN_TRANSACTION.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getCartTotalAsString(),this.reg.getCartAsString());
			}
			if (!InputType.isUniversalExit(input)) {
				int transactionID = Integer.parseInt(input);
				boolean first_scan = true;
				while (!InputType.isUniversalExit(input)) {
					if (first_scan) {
						input = Screen.SCAN_ITEM_FIRST.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getCartTotalAsString(),this.reg.getCartAsString());
					}
					else {
						input = Screen.SCAN_ITEM.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getCartTotalAsString(),this.reg.getCartAsString());
					}
					ItemQuantityManaged item = this.reg.getItemfromInventoryByID(Integer.parseInt(input));
					if (item!=null && this.reg.isValidReturn(new ItemQuantity(item.getType(),1), transactionID)) {
						this.reg.addItemToReturn(new ItemQuantity(item.getType(),1));
						return false;
					}
					else {
						first_scan = false;
					}
				}
			}
		}
		return false;
	}
	
	private boolean scan() {
		String input = "";
		boolean first = true;
		while (!InputType.isUniversalExit(input)) {
			if (first) {
				input = Screen.SCAN_ITEM_FIRST.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getCartTotalAsString(),this.reg.getCartAsString());
			}
			else {
				input = Screen.SCAN_ITEM.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getCartTotalAsString(),this.reg.getCartAsString());
			}
			ItemQuantityManaged item = this.reg.getItemfromInventoryByID(Integer.parseInt(input));
			if (item != null && item.getQuantity() >= 1) {
				this.reg.addItemToPurchase(new ItemQuantity(this.reg.getItemfromInventoryByID(Integer.parseInt(input)).getType(), 1));
				return false;
			}
			else {
				first = false;
			}
		}
		return false;
	}
	
	private boolean volume() {
		String input = "";
		while (!InputType.isUniversalExit(input)) {
			input = Screen.VOLUME.use(this.reg.getEmployeeSignedIn().getName(),this.reg.getCartTotalAsString(),this.reg.getCartAsString(),this.reg.getLastItemScanned().getType().getName());
			if (!InputType.isUniversalExit(input)) {
				this.reg.updateLastItemScanned(Integer.parseInt(input));
				break;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		new Application().run();
	}
}
