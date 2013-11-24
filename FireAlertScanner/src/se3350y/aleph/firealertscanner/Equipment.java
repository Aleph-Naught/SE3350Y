package se3350y.aleph.firealertscanner;

import java.util.ArrayList;

public class Equipment {
	 
		private String Name;
		private String id;
		private String location;
		private ArrayList<inspectionElement> Items;
		
		
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

}
