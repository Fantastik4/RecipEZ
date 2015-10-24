package activities;

import android.os.Bundle;

import com.fantastik4.recipez.R;
import com.fantastik4.recipez.R.layout;

import android.app.Activity;

public class EditListActivity extends Activity {

	private String currentUsername;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_list);
		currentUsername = (String) getIntent().getSerializableExtra("CurrentUsername");

	}
}
