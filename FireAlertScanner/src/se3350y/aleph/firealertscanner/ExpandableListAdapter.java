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
    	
    	
    	//Used to do stuff, I dunno it's hard to explain
    	View view = null;
    	
    	Object child = null;
    	
    	final Equipment groupParent = (Equipment) getGroup(groupPosition);
    	
    	
    	//Gets a reference to the current child
		
    	if(groupParent.getName().equals("Extinguisher")){
    		child = (ExtinguisherPassFailElement) getChild(groupPosition, childPosition);
    		view = ((ExtinguisherPassFailElement) child).XMLInflator(convertView, parent, context.getSystemService(context.LAYOUT_INFLATER_SERVICE));
    	}
    	else if(groupParent.getName().equals("FireHoseCabinet")){
    		child = (FireHoseCabinetGoodPoorElement) getChild(groupPosition, childPosition);
    		view = ((FireHoseCabinetGoodPoorElement) child).XMLInflator(convertView, parent, context.getSystemService(context.LAYOUT_INFLATER_SERVICE));
    	}
		
		//Sets Text
		TextView tv = (TextView) view.findViewById(R.id.inspectionElement);
		tv.setText(((inspectionElement) child).getName().toString());
		tv.setTag(((inspectionElement) child).getTag());

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
