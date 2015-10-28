package activities;

import com.fantastik4.recipez.R;
import com.fantastik4.recipez.R.anim;
import com.fantastik4.recipez.R.id;
import com.fantastik4.recipez.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class PortalActivity extends Activity {

	ImageButton searchRecipes, searchIngredients, editList;
	private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_portal);
		username = (String) getIntent().getSerializableExtra("CurrentUsername");
		editList = (ImageButton)findViewById(R.id.btn_editListOption);
		searchRecipes = (ImageButton)findViewById(R.id.btn_searchRecipesOption);
		searchIngredients = (ImageButton)findViewById(R.id.btn_searchIngredientsOption);

		/**
		 * Option 1: User chooses to search for recipes based on his current list of ingredients
		 */
		searchRecipes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PortalActivity.this, RecipeResultsActivity.class);

				startActivity(i);
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}

		});

		/**
		 * Option 2: User chooses to search for ingredients
		 */
		searchIngredients.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PortalActivity.this, IngredientSearchActivity.class);
				i.putExtra("CurrentUsername", username);
				startActivity(i);
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
		});

		/**
		 * Option 3: User chooses to edit his list of ingredients
		 */
		editList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PortalActivity.this, EditListActivity.class);
				i.putExtra("Currentusername", username);
				System.out.println(username);
				startActivity(i);
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
		});
	}

	/**
	 * Disables onBackPressd
	 */
	public void onBackPressed() {
		// Do nothing
	}
}
