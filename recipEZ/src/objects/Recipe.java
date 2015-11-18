package objects;

import java.io.Serializable;

/**
 * Class for Recipe objects
 * <P>Contains everything inside a recipe: name, id, ingredients, and steps 
 * @author Chris Repanich
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Recipe implements Serializable {

	//member variables
	private String name;
	private String recipeID;
	private String recipeDirections;
	private String recipeDescription;
	private String recipeIngredients;

	/**
	 * Constructor for a Recipe object
	 * @param name String name of recipe
	 * @param id String id of recipe
	 * @param steps ArrayList of Step objects containing the steps of the recipe
	 * @param ingr ArrayList of Ingredient objects holding the ingredients of the recipe
	 */
	public Recipe(String name, String id){
		this.setName(name);
		this.setRecipeID(id);
	}

	public Recipe() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Getter method for name
	 * @return String value of Recipe name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for name
	 * @param name String parameter of the name for the recipe
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for recipe id
	 * @return String value of recipe id
	 */
	public String getRecipeID() {
		return recipeID;
	}

	/**
	 * Setter method for recipe id
	 * @param recipeID String value of recipe id to set
	 */
	public void setRecipeID(String recipeID) {
		this.recipeID = recipeID;
	}

	public String getRecipeDirections() {
		return this.recipeDirections;
	}

	public void setRecipeDirections(String recipeDirections) {
		this.recipeDirections = recipeDirections;
	}

	public String getRecipeIngredients() {
		return this.recipeIngredients;
	}

	public void setRecipeIngredientList(String recipeIngredientList) {
		this.recipeIngredients = recipeIngredientList;
	}

	public String getRecipeDescription() {
		return this.recipeDescription;
	}

	public void setRecipeDescription(String recipeDescription) {
		this.recipeDescription = recipeDescription;
	}
}