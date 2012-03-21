package org.perugiagnulug.arduino;

import android.view.View;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TypeListActivity extends ListActivity {
	final TypeListActivity typeListActivity = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] typeList = Arduino.getInstance().getTypes();
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item_type,
				typeList));

		ListView lvTypes = getListView();
		lvTypes.setTextFilterEnabled(true);

		// Create new activity when a type is selected
		lvTypes.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selectedType = ((TextView) view).getText().toString();

				// Run next activity and append a selected type variable
				Intent intent = new Intent();
				intent.setClass(typeListActivity, DeviceListActivity.class);
				intent.putExtra("selectedType", selectedType);
				startActivity(intent);
			}
		});
	}
}