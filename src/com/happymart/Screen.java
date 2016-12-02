package com.happymart;

import java.io.IOException;
import java.util.Scanner;

public enum Screen {
	LOGIN_FIRST_USERNAME(
			"Welcome to Happy Mart Store Manager!" 								+ "\n" +
			"" 																	+ "\n" +
			"Please sign in with your employee username and password:" 			+ "\n" +
			"Username: ",InputType.TEXT
			),
	LOGIN_FIRST_PASSWORD(
			"Welcome to Happy Mart Store Manager!" 								+ "\n" +
			"" 																	+ "\n" +
			"Please sign in with your employee username and password:" 			+ "\n" +
			"Username: {{0}}"													+ "\n" +
			"Password: ",InputType.TEXT_CANCEL
			),
	LOGIN_USERNAME(
			"Welcome to Happy Mart Store Manager!" 								+ "\n" +
			"" 																	+ "\n" +
			"Incorrect username and password combination entered."				+ "\n" +
			"Please sign in with your employee username and password:" 			+ "\n" +
			"Username: ",InputType.TEXT
			),
	LOGIN_PASSWORD(
			"Welcome to Happy Mart Store Manager!" 								+ "\n" +
			"" 																	+ "\n" +
			"Incorrect username and password combination entered."				+ "\n" +
			"Please sign in with your employee username and password:" 			+ "\n" +
			"Username: {{0}}"													+ "\n" +
			"Password: ",InputType.TEXT_CANCEL
			),
	HOME_FIRST(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Current Sale: {{1}}"												+ "\n" +
			"{{2}}"																+ "\n" +
			"****************************************************************"	+ "\n" +
			"Options: "															+ "\n" +
			"{{3}}"																+ "\n" +
			""																	+ "\n" +
			"> ",InputType.OPTION_CANCEL
			),
	HOME_UNAVAILABLE(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Current Sale: {{1}}"												+ "\n" +
			"{{2}}"																+ "\n" +
			"****************************************************************"	+ "\n" +
			"Options: "															+ "\n" +
			"{{3}}"																+ "\n" +
			""																	+ "\n" +
			"Option unavailable at this time."									+ "\n" +
			"> ",InputType.OPTION_CANCEL
			),
	HOME(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Current Sale: {{1}}"												+ "\n" +
			"{{2}}"																+ "\n" +
			"****************************************************************"	+ "\n" +
			"Options: "															+ "\n" +
			"{{3}}"																+ "\n" +
			""																	+ "\n" +
			"Invalid input."													+ "\n" +
			"> ",InputType.OPTION_CANCEL
			),
	DRAWER_HOME_FIRST(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Drawer Contains: {{1}}"											+ "\n" +
			"****************************************************************"	+ "\n" +
			"Options: "															+ "\n" +
			"{{2}}"																+ "\n" +
			""																	+ "\n" +
			"> ",InputType.OPTION_CANCEL
			),
	DRAWER_HOME(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Drawer Contains: {{1}}"											+ "\n" +
			"****************************************************************"	+ "\n" +
			"Options: "															+ "\n" +
			"{{2}}"																+ "\n" +
			""																	+ "\n" +
			"Invalid input."													+ "\n" +
			"> ",InputType.OPTION_CANCEL
			),
	DRAWER_ADD(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Drawer Contains: {{1}}"											+ "\n" +
			"****************************************************************"	+ "\n" +
			"Add how much?: ",InputType.MONEY_CANCEL
			),
	DRAWER_TAKE_FIRST(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Drawer Contains: {{1}}"											+ "\n" +
			"****************************************************************"	+ "\n" +
			"Take how much?: ",InputType.MONEY_CANCEL
			),
	DRAWER_TAKE(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Drawer Contains: {{1}}"											+ "\n" +
			"****************************************************************"	+ "\n" +
			"Invalid input."													+ "\n" +
			"Take how much?: ",InputType.MONEY_CANCEL
			),
	DRAWER_SET(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Drawer Contains: {{1}}"											+ "\n" +
			"****************************************************************"	+ "\n" +
			"Set drawer value to how much?: ",InputType.MONEY_CANCEL
			),
	RETURN_TRANSACTION(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Current Sale: {{1}}"												+ "\n" +
			"{{2}}"																+ "\n" +
			"****************************************************************"	+ "\n" +
			"Enter Receipt Number: ",InputType.OPTION_CANCEL
			),
	SCAN_ITEM_FIRST(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Current Sale: {{1}}"												+ "\n" +
			"{{2}}"																+ "\n" +
			"****************************************************************"	+ "\n" +
			"Enter Item ID: ",InputType.OPTION_CANCEL
			),
	SCAN_ITEM(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Current Sale: {{1}}"												+ "\n" +
			"{{2}}"																+ "\n" +
			"****************************************************************"	+ "\n" +
			"Invalid item ID."													+ "\n" +
			"Enter Item ID: ",InputType.OPTION_CANCEL
			),
	VOLUME(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Current Sale: {{1}}"												+ "\n" +
			"{{2}}"																+ "\n" +
			"****************************************************************"	+ "\n" +
			"Enter number of {{3}} items: ",InputType.OPTION_CANCEL
			),
	REMOVE_ITEM_FIRST(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Current Sale: {{1}}"												+ "\n" +
			"{{2}}"																+ "\n" +
			"****************************************************************"	+ "\n" +
			"Enter Item ID: ",InputType.OPTION_CANCEL
			),
	CONFIRM_CANCEL_SALE(
			"Happy Mart Store Manager"											+ "\n" +
			"Currently signed in as: {{0}}"										+ "\n" +
			"****************************************************************"	+ "\n" +
			"Current Sale: {{1}}"												+ "\n" +
			"{{2}}"																+ "\n" +
			"****************************************************************"	+ "\n" +
			"Are you sure you want to cancel sale?: ",InputType.OK_CANCEL
			);
	
	private String prompt;
	private InputType type;
	private static Scanner scan = new Scanner(System.in);
	
	private Screen(String prompt, InputType type) {
		this.prompt = prompt;
		this.type = type;
	}
	
	public static String optionFormat(Object[] list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			builder.append("\n" + i + ". " + list[i]);
		}
		return builder.toString();
	}
	
	private static void cls() {
		try {
			if (System.getProperty("os.name").contains("Windows"))
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			else
				Runtime.getRuntime().exec("clear");
		} catch (InterruptedException | IOException e) {}
	}
	
	public String use(String...strings) {
		String temp = this.prompt;
		int i = 0;
		while (strings.length > 0 && temp.contains("{{"+i+"}}")) {
			temp = temp.replace("{{"+i+"}}", strings[i]);
			i++;
		}
		String raw;
		do {
			cls();
			System.out.println(temp);
			raw = scan.nextLine();
		} while(!this.type.isValid(raw));
		return raw;
	}
}
