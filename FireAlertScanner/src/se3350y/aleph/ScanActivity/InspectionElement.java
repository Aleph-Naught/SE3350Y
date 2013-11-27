package se3350y.aleph.ScanActivity;

import se3350y.aleph.Listeners.OnElementChangedListener;
import se3350y.aleph.Listeners.OnInspectionElementCompletedListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class InspectionElement {
	

		String Name;
		private String Tag;
		private String notes = "";
		View _view;
		boolean completed = false;
		boolean changed = false;
		
		
		public void setCompleted(boolean _completed){
			completed = _completed;
			OnInspectionElementCompleted();
		}
		
		public boolean getCompleted(){
			return completed;
		}
		
		public void setChanged (boolean _changed) {
			changed = _changed;
		}
		
		public boolean getChanged() {
			return changed;
		}
		
		OnInspectionElementCompletedListener onInspectionElementCompletedListener = null;
		
		public void setOnInspectionElementCompletedListener(OnInspectionElementCompletedListener listener) {
			onInspectionElementCompletedListener = listener;
		}
		
		// This function is called after the check was complete
		public void OnInspectionElementCompleted(){
		    // Check if the Listener was set, otherwise we'll get an Exception when we try to call it
		    if(onInspectionElementCompletedListener!=null) {
		    	onInspectionElementCompletedListener.onInspectionElementComplete();
		    }
		}
		
		
		OnElementChangedListener onElementChangedListener = null;
		
		public void setOnElementChangedListener(OnElementChangedListener listener) {
			onElementChangedListener = listener;
		}
		
		// This function is called after the check was complete
		public void OnElementChangeMade(){
		    // Check if the Listener was set, otherwise we'll get an Exception when we try to call it
		    if(onElementChangedListener!=null) {
		        onElementChangedListener.onElementChanged();
		    }
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
		
		public View getView(){ return _view; }
		public void setNotes(String n){ this.notes = n; }
		public String getNotes(){ return notes; }
		
		public void makeNotesDialog(){
			AlertDialog.Builder builder = new AlertDialog.Builder(_view.getContext());
			builder.setTitle("Enter note:");

			// Set up the input
			final EditText input = new EditText(_view.getContext());
			input.setHint("Reason for fail");
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			input.setText(getNotes());
			builder.setView(input);

			// Set up the buttons
			Resources res = _view.getResources();
			builder.setPositiveButton(res.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Log.i("inspectionElement","OK button clicked.");
					setNotes(input.getText().toString());
				}
			});
			builder.setNegativeButton(res.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			builder.show();
		}



		

}
