package com.gnirt69.customlistviewadapterexam;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class PlaneListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Plane> mPlaneList;

    //Constructor

    public PlaneListAdapter(Context mContext, List<Plane> mPlaneList) {
        this.mContext = mContext;
        this.mPlaneList = mPlaneList;
    }

    public void addListItemToAdapter(List<Plane> list) {
        //Add list to current array list of data
        mPlaneList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mPlaneList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlaneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_plane_list, null);
        TextView tvName = (TextView)v.findViewById(R.id.tv_name);
        TextView tvPrice = (TextView)v.findViewById(R.id.tv_price);
        TextView tvDestination = (TextView)v.findViewById(R.id.tv_destination);
        //Set text for TextView
        tvName.setText(mPlaneList.get(position).getName());
        tvPrice.setText(String.valueOf(mPlaneList.get(position).getPrice()) + " $");
        tvDestination.setText(mPlaneList.get(position).getDestination());

        //Save plane id to tag
        v.setTag(mPlaneList.get(position).getId());

        return v;
    }
}
