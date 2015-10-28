package activities;

import java.util.ArrayList;

import com.fantastik4.recipez.R;
import com.fantastik4.recipez.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import objects.Recipe;

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
		TextView tv1 = (TextView)findViewById(R.id.tv_recipeName);
		tv1.setText(selectedRecipe.getName());
		
		TextView tv2 = (TextView)findViewById(R.id.tv_recipeDescription);
		tv2.setText(selectedRecipe.getRecipeDescription());
		
		TextView tv3 = (TextView)findViewById(R.id.tv_recipeIngredients);
		tv3.setText(selectedRecipe.getRecipeIngredients());
		
		TextView tv4 = (TextView)findViewById(R.id.tv_recipeDirections);
		tv4.setText(selectedRecipe.getRecipeDirections());
		
	}
}
