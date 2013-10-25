package se3350y.aleph.firealertscanner;

import se3350y.aleph.firealertscanner.ExtinguisherPassFailElement.passFailViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FireHoseCabinetGoodPoorElement extends inspectionElement {
	
	private int goodPoor;
	
	public void setGoodPoor(int i) {
		// TODO Auto-generated method stub
		goodPoor = i;
		
	}

	public int getGoodPoor() {
		// TODO Auto-generated method stub
		return goodPoor;
	}
	
	//Holds the view for the RadioButtons
		static class textViewViewHolder {
	        protected TextView VH_textView;
	      }
	
	//Creates the row on the page
	public View XMLInflator(View convertView, ViewGroup parent, Object context){
		
		View view = null;
		
		if (convertView == null || convertView.getTag() != this.getTag()) {
			LayoutInflater infalInflater = (LayoutInflater) context;
			view = infalInflater.inflate(R.layout.firehosecabinet_goodpoor_item, null);
			
			
			final textViewViewHolder viewHolder = new textViewViewHolder();
			
			//Set ViewHolder's textview to the one in the row
			viewHolder.VH_textView = (TextView) view.findViewById(R.id.textView1);
			
			view.setTag(viewHolder);
		    viewHolder.VH_textView.setTag(this);
			
		}
		else{
			view = convertView;
			((textViewViewHolder) view.getTag()).VH_textView.setTag(this);
		}
		
		return view;
			
	}

}
