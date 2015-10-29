package activities;

import objects.Model;
import android.os.Bundle;
import objects.Ingredient;
import java.util.ArrayList;
import android.app.Activity;
import android.widget.Button;
import android.widget.ListView;
import com.fantastik4.recipez.R;
import android.widget.SearchView;
import resources.IngredientProvider;
import objects.CheckedListViewAdapter;

public class EditListActivity extends Activity {

	
	String userID;
	ListView listView;
	SearchView searchField;
	Button findRecipes, addToFridge;
	ArrayList<Ingredient> ingredients;
	IngredientProvider ingredientProvider;
	CheckedListViewAdapter ingredientListAdapter;
	ArrayList<Model> displayedIngredients, ingredientNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_list);
		
		userID = (String) getIntent().getSerializableExtra("Currentusername");
		ingredientProvider = new IngredientProvider();
		PopulateIngredientList();
	}
	
	@SuppressWarnings("unchecked")
	private void PopulateIngredientList() {
		listView = (ListView)findViewById(R.id.lv_userList);		

		ingredients = ingredientProvider.FetchIngredientsByUserID(userID);
		ingredientNames = ConvertIngredientsToModelArray(ingredients);
		displayedIngredients = (ArrayList<Model>) ingredientNames.clone();
		ingredientListAdapter = new CheckedListViewAdapter(this, displayedIngredients);

		listView.setAdapter(ingredientListAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	private ArrayList<Model> ConvertIngredientsToModelArray(ArrayList<Ingredient> ingredients) {
		ArrayList<Model> ingredientNames = new ArrayList<Model>();
		Model temp;
		for(Ingredient i: ingredients) {
			temp = new Model(i.getName());
			temp.setSelected(false);
			ingredientNames.add(temp);
		}

		return ingredientNames;
	}
}
