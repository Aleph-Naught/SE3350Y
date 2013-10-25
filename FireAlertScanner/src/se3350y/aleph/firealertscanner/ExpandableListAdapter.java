package se3350y.aleph.firealertscanner;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
	
 
    private Context context;
    private ArrayList<Equipment> groups;

    
 
    public ExpandableListAdapter(Context context, ArrayList<Equipment> groups) {
        this.context = context;
        this.groups = groups;
    }
    
    public void addItem(inspectionElement item, Equipment group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<inspectionElement> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
 
    @Override
    public Object getChild(int groupPosition, int childPosition) {
    	ArrayList<inspectionElement> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    static class passFailViewHolder {
        protected RadioGroup VH_radioGroupPassFail;
        protected int VH_passFail;
      }
 
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
    	
    	//Used to do stuff I dunno it's hard to explain
    	View view = null;
    	
    	//Gets a reference to the current child
		final inspectionElement child = (inspectionElement) getChild(groupPosition, childPosition);
		
		//Inflates XML
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.list_item, null);
			
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
					
					inspectionElement element = (inspectionElement) viewHolder.VH_radioGroupPassFail.getTag();
					
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
		    viewHolder.VH_radioGroupPassFail.setTag(child);


		}
		else {
			//If it's already been opened you don't need to rebuild it
		      view = convertView;
		      ((passFailViewHolder) view.getTag()).VH_radioGroupPassFail.setTag(child);
		    }
		
		passFailViewHolder holder = (passFailViewHolder) view.getTag();
		
		//Read the model and set the radio buttons appropriately
		if(child.getPassFail() == 1)
			holder.VH_radioGroupPassFail.check(R.id.radioPass);
		else if(child.getPassFail() == -1)
			holder.VH_radioGroupPassFail.check(R.id.radioFail);
		else if(child.getPassFail() == 0)
			holder.VH_radioGroupPassFail.check(R.id.radioNone);


		
		//Sets Text
		TextView tv = (TextView) view.findViewById(R.id.inspectionElement);
		tv.setText(child.getName().toString());
		tv.setTag(child.getTag());

		return view;
	}
 
    public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<inspectionElement> chList = groups.get(groupPosition).getItems();

		return chList.size();

	}
 
    @Override
    public Object getGroup(int groupPosition) {
    	return groups.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
    	return groups.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		Equipment group = (Equipment) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.list_group, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.lblListHeader);
		tv.setText(group.getName());
		// TODO Auto-generated method stub
		return view;
	}
 
    @Override
    public boolean hasStableIds() {
        return true;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
