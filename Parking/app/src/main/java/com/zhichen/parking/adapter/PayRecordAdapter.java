package com.zhichen.parking.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.model.PayRecord;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-06-16.
 */
public class PayRecordAdapter extends BaseAdapter {
    private Context context;
    private List<PayRecord> payRecordList = new ArrayList<>();
    private LayoutInflater inflater;
    private DecimalFormat df = new DecimalFormat("######0.00");

    public PayRecordAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<PayRecord> payRecordList) {
        this.payRecordList = payRecordList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(payRecordList.size()!=0){
            return payRecordList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return payRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_pay_record, null);
            viewHolder = new ViewHolder();
            viewHolder.payparkLot = (TextView) convertView.findViewById(R.id.history_location_tv);
            viewHolder.carNumber = (TextView) convertView.findViewById(R.id.history_carnumber_tv);
            viewHolder.payMoney = (TextView) convertView.findViewById(R.id.tv_payMoney);
            viewHolder.payTime = (TextView) convertView.findViewById(R.id.history_time_tv);
            viewHolder.payType = (TextView) convertView.findViewById(R.id.tv_paytype);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.payparkLot.setText(payRecordList.get(position).getParking_lot());
        viewHolder.carNumber.setText(payRecordList.get(position).getPlate_number());
        viewHolder.payMoney.setText(df.format(Double.parseDouble(String.valueOf(payRecordList.get(position).getAmount())) / 100) + "元");
        viewHolder.payTime.setText(payRecordList.get(position).getPayment_time());
        viewHolder.payType.setText(payRecordList.get(position).getPayment_type());
        String payment_type = payRecordList.get(position).getPayment_type();
        if(payment_type.contains("offline")){
            viewHolder.payType.setText("线下支付");
        }
        if(payment_type.contains("monthly")){
            viewHolder.payType.setText("月卡支付");
        }
        if(payment_type.contains("online")){
            viewHolder.payType.setText("线上支付");
        }
        return convertView;
    }

    static class ViewHolder {
        TextView carNumber;
        TextView payTime;
        TextView payMoney;
        TextView payparkLot;
        TextView payType;

    }
}
