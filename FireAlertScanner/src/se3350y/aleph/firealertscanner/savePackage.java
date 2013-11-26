package se3350y.aleph.firealertscanner;

import java.util.ArrayList;

import android.app.ProgressDialog;

public class savePackage {
	
	ArrayList<Equipment> equipment;
	String path;
	
	public savePackage(ArrayList<Equipment> _equipment, String _path){
		equipment = _equipment;
		
		path = _path;
	}
	
	public void setEquipment(ArrayList<Equipment> _equipment){
		equipment = _equipment;
	}
	
	public ArrayList<Equipment> getEquipment(){
		return equipment;
	}
	
	
	public void setPath(String _path){
		path = _path;
	}
	
	public String getPath(){
		return path;
	}
	

}
