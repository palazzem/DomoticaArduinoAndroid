package org.perugiagnulug.arduino;

/* This class is used to store each deice managed by Arduino */
public class Device {
	private int id;
	private int status;

	public Device(int name, int status) {
		this.id = name;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	
	public int getStatus() {
		return status;
	}
}
