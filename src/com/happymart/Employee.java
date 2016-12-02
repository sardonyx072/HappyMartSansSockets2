package com.happymart;

public class Employee implements Comparable {
	private int id;
	private String name;
	private String user;
	private String pass;
	
	public Employee(int id, String name, String user, String pass) {
		this.setID(id);
		this.setName(name);
		this.setUsername(user);
		this.setPassword(pass);
	}
	
	public int getID() {
		return this.id;
	}
	
	private void setID(int id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return this.user;
	}
	
	public void setUsername(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return this.pass;
	}
	
	public void setPassword(String pass) {
		this.pass = pass;
	}

	@Override
	public int compareTo(Object arg0) {
		return this.getName().compareTo(((Employee)arg0).getName());
	}
	
	@Override
	public boolean equals(Object arg0) {
		return this.getID() == ((Employee)arg0).getID();
	}
}
