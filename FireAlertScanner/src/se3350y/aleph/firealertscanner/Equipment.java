package se3350y.aleph.firealertscanner;

import java.util.ArrayList;

import android.graphics.Color;

public class Equipment {

	private String Name;
	private String id;
	private String location;
	private ArrayList<inspectionElement> Items;
	private ArrayList<String> details;

	private int color = Color.BLACK;

	private boolean completed = false;
	private boolean changed = false;

	public void setCompleted(boolean _completed){
		this.completed = _completed;
	}

	public boolean getCompleted()
	{
		return completed;
	}
	
	public boolean getChanged() {
		return changed;
	}
	
	public void setChanged(boolean _changed) {
		this.changed = _changed;
	}


	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String _id) {
		this.id = _id;
	}
	public ArrayList<inspectionElement> getItems() {
		return Items;
	}
	public void setItems(ArrayList<inspectionElement> Items) {
		this.Items = Items;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public int getColor() {
		// TODO Auto-generated method stub
		return color;
	}

	public void setColor(int _color) {
		// TODO Auto-generated method stub
		this.color = _color;
	}

	public ArrayList<String> getDetails() {
		return details;
	}

	public void setDetails(ArrayList<String> details) {
		this.details = details;
	}

}
