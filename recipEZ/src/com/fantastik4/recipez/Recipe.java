package com.fantastik4.recipez;
import java.util.ArrayList;

/**
 * Class for Recipe objects
 * <P>Contains everything inside a recipe: name, id, ingredients, and steps 
 * @author Chris Repanich
 * @version 1.0
 */
public class Recipe {
	//member variables
	private String name;
	private String recipeID;
	private ArrayList<Step> recipeSteps;
	private ArrayList<Ingredient> ingredients;
	/**
	 * Constructor for a Recipe object
	 * @param name String name of recipe
	 * @param id String id of recipe
	 * @param steps ArrayList of Step objects containing the steps of the recipe
	 * @param ingr ArrayList of Ingredient objects holding the ingredients of the recipe
	 */
	public Recipe(String name, String id, ArrayList<Step> steps, ArrayList<Ingredient> ingr){
		this.setName(name);
		this.setRecipeID(id);
		this.setRecipeSteps(steps);
		this.setIngredients(ingr);
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
	/**
	 * Getter method for Recipe Steps
	 * @return ArrayList of Steps for the recipe
	 */
	public ArrayList<Step> getRecipeSteps() {
		return recipeSteps;
	}
	/**
	 * Setter method for Recipe Steps
	 * @param recipeSteps ArrayList of Steps to set for the recipe
	 */
	public void setRecipeSteps(ArrayList<Step> recipeSteps) {
		this.recipeSteps = recipeSteps;
	}
	/**
	 * Getter method for Recipe Ingredients
	 * @return ArrayList of Ingredients in the recipe
	 */
	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}
	/**
	 * Setter method for Recipe Ingredients
	 * @param ingredients ArrayList of Ingredients to set in the recipe
	 */
	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
}