package objects;

/**
 * Recipe Ingredient Class
 * <P>Denotes the attributes for individual ingredients within a recipe
 * @author Chris Repanich
 * @version 1.0
 */
public class Ingredient {
	//Member variables
	private String ingredientID;
	private String name;
	private String amount;

	/**
	 * Constructor for an Ingredient object
	 * @param id String value of the ingredient's ID
	 * @param n String value of the ingredients's name
	 * @param a String value denoting the amount of the ingredient used in the recipe
	 */
	public Ingredient(String id, String n, String a){
		this.setIngredientID(id);
		this.setName(n);
		this.setAmount(a);
	}
	public Ingredient() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Getter method for Ingredient ID
	 * @return String value of the ingredient ID
	 */
	public String getIngredientID() {
		return ingredientID;
	}
	/**
	 * Setter method for IngredientID
	 * @param ingredientID String value to set the ingredient's ID to
	 */
	public void setIngredientID(String ingredientID) {
		this.ingredientID = ingredientID;
	}
	/**
	 * Getter method for ingredient name
	 * @return String value of the ingredient's name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Setter method for ingredient name
	 * @param name String value to set the ingredient's name to
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Getter method for ingredient amount
	 * @return String value of denoting the amount of the ingredient
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * Setter method for ingredient amount
	 * @param amount String value to set the amount to
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
}