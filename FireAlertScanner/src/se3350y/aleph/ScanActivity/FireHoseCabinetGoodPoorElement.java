package se3350y.aleph.ScanActivity;

import se3350y.aleph.firealertscanner.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class FireHoseCabinetGoodPoorElement extends InspectionElement {
	
	private int goodPoor;
	private boolean falseTrigger;
	
	public boolean isFalseTrigger() {
		return falseTrigger;
	}

	public void setFalseTrigger(boolean falseTrigger) {
		this.falseTrigger = falseTrigger;
	}

	public FireHoseCabinetGoodPoorElement(Context context){
		goodPoor = 2;
		falseTrigger = true;
		
	}
	
	public void setGoodPoor(int i) {
		goodPoor = i;
		
	}

	public int getGoodPoor() {
		return goodPoor;
	}
	
	//Holds the view for the RadioButtons
		static class goodPoorViewHolder {
	        protected Spinner VH_spinner;
	      }
	
	//Creates the row on the page
	public View XMLInflator(View convertView, ViewGroup parent, Object context){
		
		falseTrigger = true;
		
		View view = null;
		
		if (convertView == null || convertView.getTag() != this.getTag()) {
			LayoutInflater infalInflater = (LayoutInflater) context;
			view = infalInflater.inflate(R.layout.firehosecabinet_goodpoor_item, null);
			
			
			final goodPoorViewHolder viewHolder = new goodPoorViewHolder();
			
			
			viewHolder.VH_spinner = (Spinner) view.findViewById(R.id.goodPoorSpinner);
			
			
			viewHolder.VH_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {


				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int posSelected, long arg3) {
					
					FireHoseCabinetGoodPoorElement element = (FireHoseCabinetGoodPoorElement) viewHolder.VH_spinner.getTag();
					
					if(!falseTrigger){
						element.setGoodPoor(posSelected);
						setCompleted(true);
						setChanged(true);
					}
					
					falseTrigger = false;
					
				}
				

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
            });
			
			view.setTag(viewHolder);
		    viewHolder.VH_spinner.setTag(this);
			
		}
		else{
			view = convertView;
			((goodPoorViewHolder) view.getTag()).VH_spinner.setTag(this);
		}
		
		goodPoorViewHolder holder = (goodPoorViewHolder) view.getTag();
		
		holder.VH_spinner.setSelection(this.getGoodPoor());
		
		
		return view;
			
	}

}
