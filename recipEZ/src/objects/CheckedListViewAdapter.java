package objects;

import java.util.List;
import android.view.View;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.fantastik4.recipez.R;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

public class CheckedListViewAdapter extends ArrayAdapter<Model> {

	private final List<Model> list;
	private final Activity context;
	boolean checkAll_flag = false, checkItem_flag = false;
	
	public CheckedListViewAdapter(Activity context,List<Model> list) {
		super(context, R.layout.row, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			convertView = inflator.inflate(R.layout.row, null);
			viewHolder = new ViewHolder();
			viewHolder.text = (TextView) convertView.findViewById(R.id.label);
			viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.check);
			viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
					list.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
				}
			});
			convertView.setTag(viewHolder);
			convertView.setTag(R.id.label, viewHolder.text);
			convertView.setTag(R.id.check, viewHolder.checkbox);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.checkbox.setTag(position); // This line is important.

		viewHolder.text.setText(list.get(position).getName());
		viewHolder.checkbox.setChecked(list.get(position).isSelected());

		return convertView;
	}
}