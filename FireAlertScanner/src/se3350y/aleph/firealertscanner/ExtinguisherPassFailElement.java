package se3350y.aleph.firealertscanner;

import se3350y.aleph.firealertscanner.ExpandableListAdapter.passFailViewHolder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioButton;

public class ExtinguisherPassFailElement extends inspectionElement {
	
	private int passFail;
	
	//Holds the view for the RadioButtons
	static class passFailViewHolder {
        protected RadioGroup VH_radioGroupPassFail;
    }
	
	//Creates the row on the page
	public View XMLInflator(View convertView, ViewGroup parent, Object context){
		
		_view = null;
		
		if (convertView == null || convertView.getTag() != this.getTag()) {
			LayoutInflater infalInflater = (LayoutInflater) context;
			_view = infalInflater.inflate(R.layout.extinguisher_item, null);
			
			
			
			//For Radio Button state holding
			final passFailViewHolder viewHolder = new passFailViewHolder();
			
			//Set ViewHolder's RadioGroup to the one in the row
			viewHolder.VH_radioGroupPassFail = (RadioGroup) _view.findViewById(R.id.extinguisherRadioGroup);
			
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
			_view.setTag(viewHolder);
		    viewHolder.VH_radioGroupPassFail.setTag(this);
		}
		else {
			//If it's already been opened you don't need to rebuild it
		      _view = convertView;
		      ((passFailViewHolder) _view.getTag()).VH_radioGroupPassFail.setTag(this);
		}
		
		// A note must be entered if Fail is selected
		RadioButton rb = (RadioButton) _view.findViewById(R.id.radioFail);
		rb.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				makeNotesDialog();
			}
		});
		
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
		// TODO Auto-generated method stub
		passFail = i;
		
	}

	public int getPassFail() {
		// 1 is Pass, 0 is no answer, -1 is fail
		return passFail;
	}



}
