package com.fantastik4.recipez;

/**
 * Recipe Step Object
 * <P>Attributes of the individual steps in a recipe
 * @author Chris Repanich
 * @version 1.0
 * 
 */
public class Step {
	private String stepID;
	private String description;
	/**
	 * Constructor method for individual 'Step's of a recipe
	 * @param id
	 * @param desc
	 */
	public Step(String id, String desc){
		this.stepID = id;
		this.description = desc;
	}

	public Step(String desc)
	{
		this.description = desc;
	}
	//getters and setters
	/**
	 * Setter method for Step ID
	 * @param id String value of the Step's ID
	 */
	public void setStepID(String id){
		this.stepID = id;
	}
	/**
	 * Getter method for Step ID
	 * @return String value of the Step's ID
	 */
	public String getStepID(){
		return this.stepID;
	}
	/**
	 * Setter method for Step Description
	 * @param desc String value of the description for the step
	 */
	public void setDescription(String desc){
		this.description = desc;
	}
	/**
	 * Getter method for Step Description
	 * @return String value of the Step's description
	 */
	public String getDescription(){
		return this.description;
	}
}