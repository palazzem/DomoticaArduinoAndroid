package org.perugiagnulug.arduino;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceListActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get selected type from previous Activity
		final String selectedType = getIntent().getStringExtra("selectedType");

		String[] deviceList = Arduino.getInstance().getDevicesFromType(
				selectedType);
		setListAdapter(new ArrayAdapter<String>(this,
				R.layout.list_item_device, deviceList));

		ListView lvTypes = getListView();
		lvTypes.setTextFilterEnabled(true);

		lvTypes.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Get device status
				int status = Arduino.getInstance().apiGetStatusDevice(
						selectedType, ((TextView) view).getText().toString());
				
				// Switch the device status
				if (status >= 0) {
					if (status == 0) {
						status = 1;
					} else {
						status = 0;
					}

					// Set the new device status
					boolean reply = Arduino.getInstance().apiSetStatusDevice(
							selectedType,
							((TextView) view).getText().toString(), status);
					if (!reply) {
						// There is a communication error with Arduino
						AlertDialog.Builder builder = new AlertDialog.Builder(DeviceListActivity.this);
						builder.setMessage(R.string.discoveryFail);
						AlertDialog alert = builder.create();
						alert.show();
					}
				} else {
					// There is a communication error with Arduino because no status is returned
					AlertDialog.Builder builder = new AlertDialog.Builder(DeviceListActivity.this);
					builder.setMessage(R.string.discoveryFail);
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});
	}
}
