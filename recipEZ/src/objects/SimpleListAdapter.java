package objects;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

@SuppressWarnings("unused")
public class SimpleListAdapter extends ArrayAdapter<String> {
	
	private int resource;
	private Context context;
	private ArrayList<String> ingredients;
	
	public SimpleListAdapter(Context context, int resource, ArrayList<String> ingredients) {
		super(context, resource, ingredients);

		this.context = context;
		this.resource = resource;
		this.ingredients = ingredients;
	}
}
