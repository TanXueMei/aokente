package com.zhichen.parking.fragment.record;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.adapter.HistoryInAdapter;
import com.zhichen.parking.adapter.HistoryOutAdapter;
import com.zhichen.parking.library.dialogplus.DialogPlus;
import com.zhichen.parking.library.dialogplus.ViewHolder;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.ParkHistoryAll;
import com.zhichen.parking.model.ParkHistoryIn;
import com.zhichen.parking.model.ParkHistoryOut;
import com.zhichen.parking.model.User;
import com.zhichen.parking.servercontoler.ParkingControler;
import com.zhichen.parking.tools.DialogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-05-25.
 * 停车记录
 */
public class ParkRecordFragment extends Fragment {
    private ListView listView;
    private LinearLayout record_empty;
    private View mViewRoot;
    //	进场适配器
    private HistoryInAdapter historyInAdapter;
    //	出场适配器
    private HistoryOutAdapter historyOutAdapter;
    private RadioGroup radioGroup;
    private RadioButton btInRecord;
    private Button modifBt;
    private TextView mCarNumberTv;


    public ParkRecordFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_record_parking, container, false);
        initUI();
        firstRequest();
        return mViewRoot;
    }
    /**
     * 第一次默认显示第一辆车的停车记录
     */
    private void firstRequest() {
        List<User.Vehicle> vehicleList = UserManager.instance().getVehicleList();
        if (vehicleList != null && !vehicleList.isEmpty()) {
            mCarNumberTv.setText(vehicleList.get(0).getPlate_number());
        }
        listView.setAdapter(historyInAdapter);
        //            刷新停车记录
        new HistoryOneInTask(mCarNumberTv.getText().toString()).execute();

    }
    private void transitionInAndOutRecord(int checkID) {
        String carNumber = mCarNumberTv.getText().toString();
        switch (checkID) {
            case R.id.in_record:
                listView.setAdapter(historyInAdapter);
                new HistoryOneInTask(carNumber).execute();
                break;
            case R.id.out_ecord:
                listView.setAdapter(historyOutAdapter);
                new HistoryOneOutTask(carNumber).execute();
                break;
        }
    }

    private void initUI() {
        listView = (ListView) mViewRoot.findViewById(R.id.listview);
        record_empty = (LinearLayout) mViewRoot.findViewById(R.id.record_empty);
        modifBt = (Button) mViewRoot.findViewById(R.id.pay_car_change_btn);
        mCarNumberTv = (TextView) mViewRoot.findViewById(R.id.pay_car_no_tv);
        radioGroup = (RadioGroup) mViewRoot.findViewById(R.id.radioGroup);
        btInRecord = (RadioButton) mViewRoot.findViewById(R.id.in_record);
        historyInAdapter = new HistoryInAdapter(getContext());
        historyOutAdapter = new HistoryOutAdapter(getContext());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getId() == R.id.radioGroup) {
                    transitionInAndOutRecord(checkedId);
                }
            }
        });
        modifBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager um = UserManager.instance();
                final int size = um.getVehicleList() == null ? 0 : um.getVehicleList().size();
                final String[] mItems = new String[size];
                for (int i = 0; i < size; i++) {
                    mItems[i] = um.getVehicleList().get(i).getPlate_number();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, mItems);
                ListView lv = new ListView(getContext());
                lv.setAdapter(adapter);
                ViewHolder holder = new ViewHolder(lv);
                final DialogPlus dialog = DialogPlus.newDialog(getContext())
                        .setContentHolder(holder)
                        .setGravity(Gravity.CENTER)
                        .setExpanded(false)
                        .setCancelable(true)
                        .setBackgroundColorResourceId(R.color.white)
                        .create();
                dialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        mCarNumberTv.setText(mItems[position]);
//                        刷新停车记录
                        if (btInRecord.isChecked()) {
                            new HistoryOneInTask(mCarNumberTv.getText().toString()).execute();
                        } else {
                            new HistoryOneOutTask(mCarNumberTv.getText().toString()).execute();
                        }

                    }
                });

            }
        });

    }
    /**
     * 进场异步任务---单个车辆
     */
    private ProgressDialog mProgressDialog;
    private class HistoryOneInTask extends AsyncTask<Void, Integer, List<ParkHistoryAll>> {
        List<ParkHistoryAll> parkHistoryInList;
        private String plate_numbers;

        private HistoryOneInTask(String plate_numbers) {
            this.plate_numbers = plate_numbers;
            parkHistoryInList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = DialogHelper.showProgressDialog(getContext(), this, "正在加载数据，请稍后。。。");
        }

        @Override
        protected List<ParkHistoryAll> doInBackground(Void... params) {
            List<ParkHistoryAll> parkHistoryAllList = new ArrayList<>();
            List<ParkHistoryIn> parkHistoryInList = ParkingControler.getParkHistoryInList(plate_numbers, 0);
            if (parkHistoryInList != null) {
                for (int j = 0; j < parkHistoryInList.size(); j++) {
                    ParkHistoryAll parkHistoryAll = new ParkHistoryAll();
                    parkHistoryAll.setParking_lot(parkHistoryInList.get(j).getParking_lot());
                    parkHistoryAll.setPlate_number(parkHistoryInList.get(j).getPlate_number());
                    parkHistoryAll.setIn_time(parkHistoryInList.get(j).getIn_time());
                    parkHistoryAllList.add(parkHistoryAll);
                }
                return parkHistoryAllList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ParkHistoryAll> parkHistoryInList) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            historyInAdapter.notifyDataSetChanged(parkHistoryInList);
        }

    }

    /**
     * 出场异步任务---单个车辆
     */
    private class HistoryOneOutTask extends AsyncTask<Void, Integer, List<ParkHistoryAll>> {
        List<ParkHistoryAll> parkHistoryInList;
        private String plate_numbers;

        private HistoryOneOutTask(String plate_numbers) {
            this.plate_numbers = plate_numbers;
            parkHistoryInList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = DialogHelper.showProgressDialog(getContext(), this, "正在加载数据，请稍后。。。");
        }

        @Override
        protected List<ParkHistoryAll> doInBackground(Void... params) {
            List<ParkHistoryAll> parkHistoryAllList = new ArrayList<>();
            List<ParkHistoryOut> parkHistoryOutList = ParkingControler.getParkHistoryOutList(plate_numbers, 0);
            if (parkHistoryOutList != null) {
                for (int j = 0; j < parkHistoryOutList.size(); j++) {
                    ParkHistoryAll parkHistoryAll = new ParkHistoryAll();
                    parkHistoryAll.setParking_lot(parkHistoryOutList.get(j).getParking_lot());
                    parkHistoryAll.setPlate_number(parkHistoryOutList.get(j).getPlate_number());
                    parkHistoryAll.setOut_time(parkHistoryOutList.get(j).getOut_time());
                    parkHistoryAllList.add(parkHistoryAll);
                }
                Log.d("cwf", "出场parkHistoryAllList==" + parkHistoryAllList);
            }
            return parkHistoryAllList;
        }

        @Override
        protected void onPostExecute(List<ParkHistoryAll> parkHistoryInList) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            historyOutAdapter.notifyDataSetChanged(parkHistoryInList);
        }

    }

}
