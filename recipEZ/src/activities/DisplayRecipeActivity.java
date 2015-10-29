package activities;

import objects.Recipe;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;
import com.fantastik4.recipez.R;

public class DisplayRecipeActivity extends Activity {

	private Recipe selectedRecipe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		selectedRecipe = (Recipe) getIntent().getSerializableExtra("SelectedRecipe");
		setContentView(R.layout.activity_display_recipe);

		displayRecipe();
	}

	public void displayRecipe() {
		
		String[] tokensForList, tokensForSteps;
		String list = "", steps = "", delim = "\",\"";
		
		//Display Recipe Name
		TextView tv1 = (TextView)findViewById(R.id.tv_recipeName);
		tv1.setText(selectedRecipe.getName() + "\n");

		//Display Recipe Description
		TextView tv2 = (TextView)findViewById(R.id.tv_recipeDescription);
		tv2.setText(selectedRecipe.getRecipeDescription() + "\n");

		//Display Ingredients
		TextView tv3 = (TextView)findViewById(R.id.tv_recipeIngredients);
		
		String ingredients = selectedRecipe.getRecipeIngredients();
		tokensForList = ingredients.substring(2, ingredients.length()-2).split(delim);
		
		for (int i = 0; i < tokensForList.length; i++) {
			if (i == 0) list = "- " + tokensForList[i] + "\n";
			else list = list + "- " + tokensForList[i] + "\n";
		}
		tv3.setText(list);

		//Display Recipe Directions
		TextView tv4 = (TextView)findViewById(R.id.tv_recipeDirections);
		
		String directions = selectedRecipe.getRecipeDirections();
		tokensForSteps = directions.substring(2, directions.length()-2).split(delim);
		
		for (int i = 0; i < tokensForSteps.length; i++) {
			if (i == 0) steps = "- " + tokensForSteps[i] + "\n\n";
			else steps = steps + "- " + tokensForSteps[i] + "\n\n";
		}
		tv4.setText(steps);

	}
}
