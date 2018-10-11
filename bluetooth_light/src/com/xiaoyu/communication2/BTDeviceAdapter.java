package com.xiaoyu.communication2;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaoyu.bluetooth.BTItem;

public class BTDeviceAdapter extends BaseAdapter{

	
	private List<BTItem> mListItem = new ArrayList<BTItem>();;
	private Context mcontext = null;
	private LayoutInflater mInflater = null;
	
	public BTDeviceAdapter(Context context){
		this.mcontext = context;
		this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	void clearData(){
		mListItem.clear();
	}
	
	void addDataModel(List<BTItem> itemList){
		if(itemList == null || itemList.size()==0)
			return;
		
		mListItem.addAll(itemList);
		notifyDataSetChanged();
	}
	void addDataModel(BTItem item){
		mListItem.add(item);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mListItem.size();
	}

	@Override
	public Object getItem(int position) {
		return mListItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.device_item_row, null);
			holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			holder.tv_address = (TextView)convertView.findViewById(R.id.tv_address);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}

		holder.tv_name.setText(mListItem.get(position).getBuletoothName());
		holder.tv_address.setText(mListItem.get(position).getBluetoothAddress());
		
		return convertView;
	}

	 class ViewHolder{
		 TextView tv_name;
		 TextView tv_address;
	 }
}
