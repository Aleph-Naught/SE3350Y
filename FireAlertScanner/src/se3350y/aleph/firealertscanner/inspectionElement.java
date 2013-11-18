package se3350y.aleph.firealertscanner;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class inspectionElement {
	

		String Name;
		private String Tag;
		private String notes = "";
		View _view;
	
		public String getName() {
			return Name;
		}
		public void setName(String Name) {
			this.Name = Name;
		}
		public String getTag() {
			return Tag;
		}
		public void setTag(String Tag) {
			this.Tag = Tag;
		}
		public View getView(){ return _view; }
		public void setNotes(String n){ this.notes = n; }
		public String getNotes(){ return notes; }

}
