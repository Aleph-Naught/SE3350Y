package se3350y.aleph.firealertscanner;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class inspectionElement {
	

		private String Name;
		private int passFail;
		private String Tag;
		
		public void setPassFail(int i){
			passFail = i;
		}
		public int getPassFail(){
			return passFail;
		}
	
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

}
