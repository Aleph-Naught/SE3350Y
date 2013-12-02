package se3350y.aleph.ScanActivity;

import java.util.ArrayList;

import se3350y.aleph.Listeners.OnElementChangedListener;
import se3350y.aleph.Listeners.OnInspectionElementCompletedListener;

import android.graphics.Color;

public class Equipment {

	private String Name;
	private String id;
	private String location;
	private ArrayList<InspectionElement> Items;
	private ArrayList<String> details;

	private int color = Color.BLACK;

	private boolean completed = false;
	private boolean changed = false;
	
	
	OnElementChangedListener onElementChangedListener = null;
	
	public void setOnElementChangedListener(OnElementChangedListener listener) {
		onElementChangedListener = listener;
	}
	
	// This function is called after the check was complete
	public void OnElementChanged(){
	    // Check if the Listener was set, otherwise we'll get an Exception when we try to call it
	    if(onElementChangedListener!=null) {
	    	onElementChangedListener.onElementChanged();
	    }
	}


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
		OnElementChanged();
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
	public ArrayList<InspectionElement> getItems() {
		return Items;
	}
	public void setItems(ArrayList<InspectionElement> Items) {
		this.Items = Items;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int _color) {
		this.color = _color;
	}

	public ArrayList<String> getDetails() {
		return details;
	}

	public void setDetails(ArrayList<String> details) {
		this.details = details;
	}

}
