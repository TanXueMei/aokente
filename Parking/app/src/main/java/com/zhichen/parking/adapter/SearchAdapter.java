package com.zhichen.parking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.model.ParkingLot;

import java.util.List;

/**
 * Created by xuemei on 2016-07-05.
 */
public class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<ParkingLot> mDataList;

    public SearchAdapter(Context context, List<ParkingLot> gifts) {
        this.context = context;
        this.mDataList = gifts;
    }

    public void setDatas(List<ParkingLot> datas) {
        mDataList = datas ;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
            holder = new Holder();
            holder.nameTv = (TextView) convertView.findViewById(R.id.parkinglot_name_tv);
            holder.availableTv = (TextView) convertView.findViewById(R.id.parkinglot_available_tv);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        ParkingLot history = mDataList.get(position);
        holder.nameTv.setText(history.getName());
        holder.availableTv.setText(String.valueOf(history.getParkingSpacesAvailable()));
        return convertView;
    }

    private class Holder {
        TextView nameTv;
        TextView availableTv;
    }

}
