package se3350y.aleph.firealertscanner;

import java.util.ArrayList;

public class Equipment {
	 
		private String Name;
		private ArrayList<inspectionElement> Items;
		
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			this.Name = name;
		}
		public ArrayList<inspectionElement> getItems() {
			return Items;
		}
		public void setItems(ArrayList<inspectionElement> Items) {
			this.Items = Items;
		}

}
