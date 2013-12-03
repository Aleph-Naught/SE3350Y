package se3350y.aleph.ScanActivity;

import se3350y.aleph.firealertscanner.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioButton;

public class ExtinguisherPassFailElement extends InspectionElement {
	
	private int passFail;
	
	//Holds the view for the RadioButtons
	static class passFailViewHolder {
		
        protected RadioGroup VH_radioGroupPassFail;
        
        
    }
	
	//Creates the row on the page
	public View XMLInflator(View convertView, ViewGroup parent, Object context){
		
		//_view = null;
		
		if (convertView == null || convertView.getTag() != this.getTag()) {
			LayoutInflater infalInflater = (LayoutInflater) context;
			_view = infalInflater.inflate(R.layout.extinguisher_item, null);
			
			
			
			//For Radio Button state holding
			final passFailViewHolder viewHolder = new passFailViewHolder();
			
			final RadioButton pass = (RadioButton) _view.findViewById(R.id.radioPass);
			final RadioButton fail = (RadioButton) _view.findViewById(R.id.radioFail);
			
			//Set ViewHolder's RadioGroup to the one in the row
			viewHolder.VH_radioGroupPassFail = (RadioGroup) _view.findViewById(R.id.extinguisherRadioGroup);
			
			//OnClick Listener
			viewHolder.VH_radioGroupPassFail
	          .setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					
					ExtinguisherPassFailElement element = (ExtinguisherPassFailElement) viewHolder.VH_radioGroupPassFail.getTag();
					
					//Sets Model
					if(checkedId == R.id.radioPass){
						element.setPassFail(1);
						setCompleted(true);
					}
					else if(checkedId == R.id.radioFail){
						element.setPassFail(-1);
						setCompleted(true);
					}
					else if(checkedId == R.id.radioNone)
						element.setPassFail(0);
					
				}
	          });
			
			
			//Checks to see if changes have been made
			pass.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					
					setChanged(true);
					
				}

				});
			
			fail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					setChanged(true);
					makeNotesDialog();
					
				}

				});
		

			
			
			
			//Sets view to ViewHolder or something I don't know really
			_view.setTag(viewHolder);
		    viewHolder.VH_radioGroupPassFail.setTag(this);
		}
		else {
			//If it's already been opened you don't need to rebuild it
		      _view = convertView;
		      ((passFailViewHolder) _view.getTag()).VH_radioGroupPassFail.setTag(this);
		}
		
		
		
		passFailViewHolder holder = (passFailViewHolder) _view.getTag();
		
		//Read the model and set the radio buttons appropriately
		if(this.getPassFail() == 1)
			holder.VH_radioGroupPassFail.check(R.id.radioPass);
		else if(this.getPassFail() == -1)
			holder.VH_radioGroupPassFail.check(R.id.radioFail);
		else if(this.getPassFail() == 0)
			holder.VH_radioGroupPassFail.check(R.id.radioNone);
		
		
		return _view;
		
	}
	
	public void setPassFail(int i) {
		passFail = i;
		
	}

	public int getPassFail() {
		// 1 is Pass, 0 is no answer, -1 is fail
		return passFail;
	}



}
