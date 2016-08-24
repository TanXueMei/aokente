package com.zhichen.parking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-05-26.
 * 包含进出场记录
 */
public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<Record> recordList = new ArrayList<>();
    private LayoutInflater inflater;

    public HistoryAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<Record> recordList) {
        this.recordList = recordList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_record,null);
            viewHodler = new ViewHodler();
            viewHodler.title = (TextView) convertView.findViewById(R.id.title);
            viewHodler.enterTime = (TextView) convertView.findViewById(R.id.enter_time);
            viewHodler.OutTime = (TextView) convertView.findViewById(R.id.comeOut_time);
            viewHodler.money = (TextView) convertView.findViewById(R.id.money);
            viewHodler.dataSource = (TextView) convertView.findViewById(R.id.data_source);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        //设置字体
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "font/DroidSansFallback.ttf");

        Record record = recordList.get(position);
        viewHodler.title.setText(record.getTitle());
//        viewHodler.title.setTypeface(tf);

        viewHodler.enterTime.setText("驶入：" + record.getEnterTime());
//        viewHodler.enterTime.setTypeface(tf);

        viewHodler.OutTime.setText("驶出：" + record.getOutTime());
//        viewHodler.OutTime.setTypeface(tf);

        viewHodler.money.setText(record.getMoney());
//        viewHodler.money.setTypeface(tf);

        viewHodler.dataSource.setText(record.getDataSource());
//        viewHodler.dataSource.setTypeface(tf);

        return convertView;
    }

    static class ViewHodler {
        TextView title;
        TextView enterTime;
        TextView OutTime;
        TextView money;
        TextView dataSource;
    }
}
