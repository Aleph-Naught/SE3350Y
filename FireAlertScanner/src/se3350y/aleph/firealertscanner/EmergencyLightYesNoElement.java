package se3350y.aleph.firealertscanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class EmergencyLightYesNoElement extends inspectionElement {
	
public int yesNo;
	
	//Holds the view for the RadioButtons
		static class yesNoViewHolder {
	        protected RadioGroup VH_radioGroupYesNo;
	      }
		
		
		//Creates the row on the page
		public View XMLInflator(View convertView, ViewGroup parent, Object context){
			
			//View view = null;
			
			if (convertView == null || convertView.getTag() != this.getTag()) {
				LayoutInflater infalInflater = (LayoutInflater) context;
				_view = infalInflater.inflate(R.layout.emergencylight_yesno_item, null);
				
				//For Radio Button state holding
				final yesNoViewHolder viewHolder = new yesNoViewHolder();
				
				//Set ViewHolder's RadioGroup to the one in the row
				viewHolder.VH_radioGroupYesNo = (RadioGroup) _view.findViewById(R.id.emergencyLightRadioGroup);
				
				final RadioButton yes = (RadioButton) _view.findViewById(R.id.radioYes);
				final RadioButton no = (RadioButton) _view.findViewById(R.id.radioNo);
				
				//OnClick Listener
				viewHolder.VH_radioGroupYesNo
		          .setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						
						EmergencyLightYesNoElement element = (EmergencyLightYesNoElement) viewHolder.VH_radioGroupYesNo.getTag();
						
						//Sets Model
						if(checkedId == R.id.radioYes){
							element.setYesNo(1);
							setCompleted(true);
						}
						else if(checkedId == R.id.radioNo){
							element.setYesNo(-1);
							setCompleted(true);
						}
						else if(checkedId == R.id.radioNone)
							element.setYesNo(0);
						
					}
		          });
				
				//Checks to see if changes have been made
				yes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						
						OnElementChangeMade();
						
					}

					});
				
				no.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						OnElementChangeMade();
						makeNotesDialog();
						
					}

					});
				
				//Sets view to ViewHolder or something I don't know really
				_view.setTag(viewHolder);
			    viewHolder.VH_radioGroupYesNo.setTag(this);
			}
			else {
				//If it's already been opened you don't need to rebuild it
			      _view = convertView;
			      ((yesNoViewHolder) _view.getTag()).VH_radioGroupYesNo.setTag(this);
			    }
			
			yesNoViewHolder holder = (yesNoViewHolder) _view.getTag();
			
			//Read the model and set the radio buttons appropriately
			if(this.getYesNo() == 1)
				holder.VH_radioGroupYesNo.check(R.id.radioYes);
			else if(this.getYesNo() == -1)
				holder.VH_radioGroupYesNo.check(R.id.radioNo);
			else if(this.getYesNo() == 0)
				holder.VH_radioGroupYesNo.check(R.id.radioNone);
			
			
			return _view;
			
		}
		
		
		public void setYesNo(int i) {
			// TODO Auto-generated method stub
			yesNo = i;
			
		}

		public int getYesNo() {
			// 1 is Pass, 0 is no answer, -1 is fail
			return yesNo;
		}

}
