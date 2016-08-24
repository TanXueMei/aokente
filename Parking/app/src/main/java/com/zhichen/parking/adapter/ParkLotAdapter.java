package com.zhichen.parking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.fragment.homepage.FindParkingLotFragment;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.ParkingLot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-06-12.
 */
public class ParkLotAdapter extends BaseAdapter {
    private Context context;
    private List<ParkingLot> parkLotLists = new ArrayList<>();
    private LayoutInflater inflater;

    public ParkLotAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<ParkingLot> parkLotLists) {
        this.parkLotLists = parkLotLists;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return parkLotLists.size();
    }

    @Override
    public Object getItem(int position) {
        return parkLotLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_park_lotlist, null);
            viewHodler = new ViewHodler();
            viewHodler.name = (TextView) convertView.findViewById(R.id.title);
            viewHodler.distance = (TextView) convertView.findViewById(R.id.distance);
            viewHodler.totleParkLot = (TextView) convertView.findViewById(R.id.tv_totleParkLot);
            viewHodler.emptyParkLot = (TextView) convertView.findViewById(R.id.tv_emptyPark);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.name.setText(parkLotLists.get(position).getName());

        viewHodler.distance.setText(String.valueOf(FindParkingLotFragment.formatDistance((long) UserManager.instance().getDistance(
                parkLotLists.get(position).getPosition()))));

        viewHodler.totleParkLot.setText(String.valueOf("总车位：" + parkLotLists.get(position).getParkingSpaces()));
        viewHodler.emptyParkLot.setText(String.valueOf("空车位：" + parkLotLists.get(position).getParkingSpacesAvailable()));

        return convertView;
    }

    static class ViewHodler {
        TextView name;
        TextView distance;
        TextView totleParkLot;
        TextView emptyParkLot;
    }

}
