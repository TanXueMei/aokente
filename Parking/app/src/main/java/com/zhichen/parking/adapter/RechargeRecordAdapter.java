package com.zhichen.parking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.model.RechargeRecord;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-08-11.
 */
public class RechargeRecordAdapter extends BaseAdapter {
    private Context context;
    private List<RechargeRecord> rechargeRecords = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private DecimalFormat df = new DecimalFormat("######0.00");

    public RechargeRecordAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<RechargeRecord> rechargeRecords) {
        this.rechargeRecords = rechargeRecords;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rechargeRecords == null ? 0 : rechargeRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return rechargeRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null==convertView){
            convertView=layoutInflater.inflate(R.layout.item_rechage_record,null);
            viewHolder=new ViewHolder();
            viewHolder.payChannel= (TextView) convertView.findViewById(R.id.tv_carnumber);
            viewHolder.amount= (TextView) convertView.findViewById(R.id.rechage_amount);
            viewHolder.payTime= (TextView) convertView.findViewById(R.id.rechage_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        String payment_channel = rechargeRecords.get(position).getPayment_channel();
        if(payment_channel.contains("wxpay")){
            viewHolder.payChannel.setText("微信");
        }else if(payment_channel.contains("alipay")){
            viewHolder.payChannel.setText("支付宝");
        }else {
            viewHolder.payChannel.setText("银联");
        }


        viewHolder.amount.setText( df.format(Double.parseDouble(String.valueOf(rechargeRecords.get(position).getAmount())) / 100) + "元");
        viewHolder.payTime.setText(rechargeRecords.get(position).getPayment_time());

        return convertView;
    }

    static class ViewHolder {
//        TextView carNumber;
        TextView payChannel;
        TextView amount;
        TextView payTime;
    }
}
