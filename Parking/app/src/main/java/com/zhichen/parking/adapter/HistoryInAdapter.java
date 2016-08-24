package com.zhichen.parking.adapter;

import android.content.Context;
import android.util.Log;
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
 * 进场适配器
 */
public class HistoryInAdapter extends BaseAdapter {
    private Context context;
    private List<ParkHistoryAll> parkHistoryAllList = new ArrayList<>();
    private LayoutInflater inflater;

    public HistoryInAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<ParkHistoryAll> parkHistoryAllList) {
        this.parkHistoryAllList = parkHistoryAllList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (parkHistoryAllList!=null){
            return parkHistoryAllList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        return parkHistoryAllList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_parkhistory, null);
            viewHodler = new ViewHodler();
            viewHodler.parkLocation = (TextView) convertView.findViewById(R.id.history_location_tv);
            viewHodler.carNumber = (TextView) convertView.findViewById(R.id.history_carnumber_tv);
            viewHodler.inTime = (TextView) convertView.findViewById(R.id.history_time_tv);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.parkLocation.setText(parkHistoryAllList.get(position).getParking_lot());
        Log.d("tanxuemei", "进场适配器中车牌号" + parkHistoryAllList.get(position).getPlate_number());
        viewHodler.carNumber.setText(parkHistoryAllList.get(position).getPlate_number());
        viewHodler.inTime.setText(parkHistoryAllList.get(position).getIn_time());

        return convertView;
    }

    static class ViewHodler {
        TextView parkLocation;
        TextView carNumber;
        TextView inTime;
    }
}
