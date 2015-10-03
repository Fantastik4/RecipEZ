package com.fantastik4.recipez;

/**
 * User object for the application
 * @author Chris Repanich
 * @version 1.0
 */
public class User {
	private String userID;
	private String name;
	/**
	 * Constructor for the User object
	 * @param id String value of the user ID
	 * @param name String value of the user name
	 */
	public User(String id, String name){
		this.setUserID(id);
		this.setName(name);
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Getter method for user ID
	 * @return String value of the user id
	 */
	public String getUserID() {
		return userID;
	}
	/**
	 * Setter method for user id
	 * @param userID String value to set the user id to
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	/**
	 * Getter method for user name
	 * @return String value of the user's name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Setter method for the user name
	 * @param name String value to set the user's name to
	 */
	public void setName(String name) {
		this.name = name;
	}
}
