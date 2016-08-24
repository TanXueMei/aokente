package com.zhichen.parking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.model.ParkHistoryAll;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-06-16.
 * 出场适配器
 */
public class HistoryOutAdapter extends BaseAdapter {
    private Context context;
    private List<ParkHistoryAll> parkHistoryOutList=new ArrayList<>();
    private LayoutInflater inflater;

    public HistoryOutAdapter(Context context) {
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<ParkHistoryAll> parkHistoryInList) {
        this.parkHistoryOutList=parkHistoryInList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return parkHistoryOutList.size();
    }

    @Override
    public Object getItem(int position) {
        return parkHistoryOutList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler=null;
        if(null==convertView){
            convertView=inflater.inflate(R.layout.item_parkhistory,null);
            viewHodler=new ViewHodler();
            viewHodler.parkLocation= (TextView) convertView.findViewById(R.id.history_location_tv);
            viewHodler.carNumber= (TextView) convertView.findViewById(R.id.history_carnumber_tv);
            viewHodler.outTime= (TextView) convertView.findViewById(R.id.history_time_tv);
            viewHodler.style= (TextView) convertView.findViewById(R.id.history_time_des_tv);
            convertView.setTag(viewHodler);
        }else {
            viewHodler= (ViewHodler) convertView.getTag();
        }
        viewHodler.style.setText("离场：");
        viewHodler.parkLocation.setText(parkHistoryOutList.get(position).getParking_lot());
        viewHodler.carNumber.setText(parkHistoryOutList.get(position).getPlate_number());
        viewHodler.outTime.setText(parkHistoryOutList.get(position).getOut_time());
        return convertView;
    }
    static class ViewHodler{
        TextView parkLocation;
        TextView carNumber;
        TextView outTime;
        TextView style;
    }
}
