package activities;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import com.fantastik4.recipez.R;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

public class PortalActivity extends Activity {

	private String username;
	private ImageButton searchRecipes, searchIngredients, editList, favorites;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_portal);
		
		username = (String) getIntent().getSerializableExtra("CurrentUsername");
		
		favorites = (ImageButton)findViewById(R.id.btn_favorites);
		editList = (ImageButton)findViewById(R.id.btn_editListOption);
		searchRecipes = (ImageButton)findViewById(R.id.btn_searchRecipesOption);
		searchIngredients = (ImageButton)findViewById(R.id.btn_searchIngredientsOption);

		/**
		 * Option 1: User chooses to search for recipes based on his current list of ingredients
		 */
		searchRecipes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PortalActivity.this, UserRecipesActivity.class);
				i.putExtra("CurrentUsername", username);
				
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
				i.putExtra("CurrentUsername", username);
				
				startActivity(i);
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
		});
		
		/**
		 * Option 4: User chooses to access their list of favorite recipes
		 */
		favorites.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PortalActivity.this, FavoritesActivity.class);
				i.putExtra("CurrentUsername", username);
				
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
