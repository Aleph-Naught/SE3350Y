package se3350y.aleph.firealertscanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ExtinguisherPassFailElement extends inspectionElement {
	
	private int passFail;
	
	//Holds the view for the RadioButtons
	static class passFailViewHolder {
        protected RadioGroup VH_radioGroupPassFail;
      }
	
	//Creates the row on the page
	public View XMLInflator(View convertView, ViewGroup parent, Object context){
		
		View view = null;
		
		if (convertView == null || convertView.getTag() != this.getTag()) {
			LayoutInflater infalInflater = (LayoutInflater) context;
			view = infalInflater.inflate(R.layout.extinguisher_item, null);
			
			//For Radio Button state holding
			final passFailViewHolder viewHolder = new passFailViewHolder();
			
			//Set ViewHolder's RadioGroup to the one in the row
			viewHolder.VH_radioGroupPassFail = (RadioGroup) view.findViewById(R.id.extinguisherRadioGroup);
			
			//OnClick Listener
			viewHolder.VH_radioGroupPassFail
	          .setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					
					ExtinguisherPassFailElement element = (ExtinguisherPassFailElement) viewHolder.VH_radioGroupPassFail.getTag();
					
					//Sets Model
					if(checkedId == R.id.radioPass)
						element.setPassFail(1);
					else if(checkedId == R.id.radioFail)
						element.setPassFail(-1);
					else if(checkedId == R.id.radioNone)
						element.setPassFail(0);
					
				}
	          });
			
			//Sets view to ViewHolder or something I don't know really
			view.setTag(viewHolder);
		    viewHolder.VH_radioGroupPassFail.setTag(this);
		}
		else {
			//If it's already been opened you don't need to rebuild it
		      view = convertView;
		      ((passFailViewHolder) view.getTag()).VH_radioGroupPassFail.setTag(this);
		    }
		
		passFailViewHolder holder = (passFailViewHolder) view.getTag();
		
		//Read the model and set the radio buttons appropriately
		if(this.getPassFail() == 1)
			holder.VH_radioGroupPassFail.check(R.id.radioPass);
		else if(this.getPassFail() == -1)
			holder.VH_radioGroupPassFail.check(R.id.radioFail);
		else if(this.getPassFail() == 0)
			holder.VH_radioGroupPassFail.check(R.id.radioNone);
		
		
		return view;
		
	}

	public void setPassFail(int i) {
		// TODO Auto-generated method stub
		passFail = i;
		
	}

	public int getPassFail() {
		// 1 is Pass, 0 is no answer, -1 is fail
		return passFail;
	}



}
