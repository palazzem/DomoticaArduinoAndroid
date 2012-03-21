package org.perugiagnulug.arduino;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	final MainActivity mainActivity = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void connect(View view) {
		TextView txtIP = (TextView) findViewById(R.id.txtIPArduino);
		String arduinoIP = txtIP.getText().toString();
		if (validateIP(arduinoIP)) {
			// The ip is saved in a Singleton Class
			Arduino.getInstance().setArduinoIP(arduinoIP);

			// Create an AsyncTask to use the discovery method
			new DiscoveryTask(MainActivity.this).execute();
		} else {
			// Error in input validation
			AlertDialog alertDialog;
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getResources().getText(R.string.warningTitle));
			alertDialog.setMessage(getResources().getText(
					R.string.warningIPArduino));
			alertDialog.show();
		}
	}

	/* TODO make an IP validation */
	private boolean validateIP(String ip) {
		return true;
	}

	/* AsyncTask is used to avoid UI blocks*/
	private class DiscoveryTask extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog dialog = null;
		private Context context = null;

		public DiscoveryTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(context,
					getResources().getText(R.string.progressDiscoveryTitle),
					getResources().getText(R.string.progressDiscovery), true);
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			return Arduino.getInstance().apiDiscovery();
		}

		@Override
		protected void onPostExecute(Boolean reply) {
			dialog.cancel();
			if (reply) {
				// Close this activity and show the device list
				finish();
				Intent intent = new Intent();
				intent.setClass(mainActivity, TypeListActivity.class);
				startActivity(intent);
			} else {
				// There is a communication error with Arduino
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(R.string.discoveryFail);
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}
}