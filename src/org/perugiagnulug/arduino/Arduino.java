package org.perugiagnulug.arduino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Arduino {
	private static Arduino INSTANCE = null;
	private String arduinoIP = "";
	private HashMap<String, Integer> types = null;
	private ArrayList<HashMap<String, Device>> devices = null;

	/** Arduino API list */
	
	/* Get all devices connected to Arduino */
	public boolean apiDiscovery() {
		boolean value = false;
		JSONObject response = null;
		String apiURL = "http://" + arduinoIP + "/?discovery";

		try {
			response = new JSONObject(HttpRequest.requestGET(apiURL));
			setDevices(response);
			value = true;
		} catch (Exception e) {
			value = false;
		}

		return value;
	}

	/* Get the status of selected device */
	public int apiGetStatusDevice(String selectedType, String deviceName) {
		int value = -1;
		JSONObject response = null;
		int type = types.get(selectedType);
		HashMap<String, Device> deviceList = devices.get(type);
		Device device = deviceList.get(deviceName);
		String apiURL = "http://" + arduinoIP + "/?status&device="
				+ device.getId();

		try {
			response = new JSONObject(HttpRequest.requestGET(apiURL));
			value = Integer.valueOf(response.getString("status"));
		} catch (Exception e) {
			value = -1;
		}

		return value;
	}

	/* Set the status of selected device */
	public boolean apiSetStatusDevice(String selectedType, String deviceName,
			int newStatus) {
		boolean value = false;
		int type = types.get(selectedType);
		JSONObject response = null;
		HashMap<String, Device> deviceList = devices.get(type);
		Device device = deviceList.get(deviceName);

		String apiURL = "http://" + arduinoIP + "/?device=" + device.getId()
				+ "&status=" + newStatus;

		try {
			response = new JSONObject(HttpRequest.requestGET(apiURL));
			value = (response.getString("response").equals("ACK"));
		} catch (Exception e) {
			value = false;
		}

		return value;
	}

	/* These values should be the same written in DomoticaArduino program */
	private String typeMapping(int type) {
		String value = "";
		switch (type) {
		case 0:
			value = "Light";
			break;
		case 1:
			value = "Object";
			break;
		}
		return value;
	}

	/* Set Arduino IP in the singleton Class */
	public void setArduinoIP(String arduinoIP) {
		this.arduinoIP = arduinoIP;
	}

	/* Convert Arduino response in Hashmap variables */
	private void setDevices(JSONObject response) throws JSONException {
		devices = new ArrayList<HashMap<String, Device>>();
		types = new HashMap<String, Integer>();

		JSONArray devicesJSONList = response.getJSONArray("devices");
		for (int i = 0; i < devicesJSONList.length(); i++) {
			int type = Integer.valueOf(devicesJSONList.getJSONObject(i)
					.getString("type"));
			String typeName = typeMapping(type);
			
			// The type is register if it is not already done
			if (!types.containsKey(typeName)) {
				types.put(typeName, type);
				devices.add(type, new HashMap<String, Device>());
			}
			HashMap<String, Device> deviceList = devices.get(type);

			// Load the device into the right HashMap list
			int deviceID = Integer.valueOf(devicesJSONList.getJSONObject(i)
					.getString("id"));
			String deviceName = devicesJSONList.getJSONObject(i).getString(
					"name");
			int deviceStatus = Integer.valueOf(devicesJSONList.getJSONObject(i)
					.getString("status"));
			deviceList.put(deviceName, new Device(deviceID, deviceStatus));
		}
	}

	/* Get all registered types of device */
	public String[] getTypes() {
		String[] values = new String[types.size()];
		Iterator<Entry<String, Integer>> iterator = types.entrySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			values[i] = iterator.next().getKey().toString();
			i++;
		}

		return values;
	}

	/* Get all devices name (Hashmap key) from a chosen type */
	public String[] getDevicesFromType(String typeName) {
		int type = types.get(typeName);
		HashMap<String, Device> deviceList = devices.get(type);
		String[] values = new String[deviceList.size()];

		Iterator<Entry<String, Device>> iterator = deviceList.entrySet()
				.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			values[i] = iterator.next().getKey();
			i++;
		}

		return values;
	}

	/* Get instance of the singleton class */
	public static synchronized Arduino getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Arduino();
		}

		return INSTANCE;
	}

	private Arduino() {
	}
}