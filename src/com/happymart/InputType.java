package com.happymart;

public enum InputType {
	OK,
	OK_CANCEL,
	TEXT,
	TEXT_CANCEL,
	OPTION,
	OPTION_CANCEL,
	ID,
	ID_CANCEL,
	MONEY,
	MONEY_CANCEL;

	private static final String OK_MATCH = "";
	private static final String CANCEL_MATCH = "";
	private static final String EXIT_MATCH = "";
	private static final String QUIT_MATCH = "";

	public static boolean isOK(String input) {
		return input.equals(OK_MATCH);
	}
	
	public static boolean isCancel(String input) {
		return input.equals(CANCEL_MATCH);
	}
	
	public static boolean isExit(String input) {
		return input.equals(EXIT_MATCH);
	}
	
	public static boolean isQuit(String input) {
		return input.equals(QUIT_MATCH);
	}
	
	public static boolean isText(String input) {
		return input.length() > 0;
	}
	
	public static boolean isOption(String input) {
		//return input.matches("^?\\d+$");
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isID(String input) {
		//return input.matches("^?\\d+$");
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isMoney(String input) {
		//return input.matches("?\\d+(\\.\\d+)?"); //wrong
		try {
			Double.parseDouble(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isValid(String input) {
		switch(this) {
		case OK:
			return InputType.isOK(input);
		case OK_CANCEL:
			return InputType.isOK(input) || InputType.isCancel(input) || InputType.isExit(input) || InputType.isQuit(input);
		case TEXT:
			return InputType.isText(input);
		case TEXT_CANCEL:
			return InputType.isText(input) || InputType.isCancel(input) || InputType.isExit(input) || InputType.isQuit(input);
		case OPTION:
			return InputType.isOption(input);
		case OPTION_CANCEL:
			return InputType.isOption(input) || InputType.isCancel(input) || InputType.isExit(input) || InputType.isQuit(input);
		case ID:
			return InputType.isID(input);
		case ID_CANCEL:
			return InputType.isID(input) || InputType.isCancel(input) || InputType.isExit(input) || InputType.isQuit(input);
		case MONEY:
			return InputType.isMoney(input);
		case MONEY_CANCEL:
			return InputType.isMoney(input) || InputType.isCancel(input) || InputType.isExit(input) || InputType.isQuit(input);
		default:
			return false;	
		}
	}
}
