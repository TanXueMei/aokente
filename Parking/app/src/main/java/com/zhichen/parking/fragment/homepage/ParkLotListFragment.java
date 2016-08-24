package com.zhichen.parking.fragment.homepage;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.zhichen.parking.R;
import com.zhichen.parking.adapter.ParkLotAdapter;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.library.refreshlayout.RefreshLayout;
import com.zhichen.parking.model.ParkingLot;
import com.zhichen.parking.servercontoler.ParkingControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.tools.FragmentChangeHelper;
import com.zhichen.parking.tools.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-06-08.
 * 停车场列表
 */
public class ParkLotListFragment extends BaseFragment {

    View mViewRoot;
    private ListView listView;
    private ParkLotAdapter parkLotAdapter;
    private View mFooterLayout;
    private ProgressBar mMoreProgress;
    RefreshLayout mRefreshLayout;

    List<ParkingLot> mParkingLotList;

    public ParkLotListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_parklotlist, container, false);
        mRefreshLayout = (RefreshLayout) mViewRoot.findViewById(R.id.swipe_container);
        mParkingLotList = new ArrayList<>();
        initUI();
        initRefreshLayout(inflater);
        return mViewRoot;
    }

    private void initUI() {
        listView = (ListView) mViewRoot.findViewById(R.id.history_lv);
        parkLotAdapter = new ParkLotAdapter(getActivity());
        listView.setAdapter(parkLotAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view == mFooterLayout) {
                    if (!mRefreshLayout.isLoading()) {
                        mRefreshLayout.setLoading(true);
                    }
                } else {
                    if (position >= mParkingLotList.size()) {
                        return;
                    }
                    ParkingLot parkingLot = mParkingLotList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString(ParkDetailFragment.KEY_PARKINGLOT_DATA, GsonUtil.createGson().toJson(parkingLot));
                    Fragment fragment = new ParkDetailFragment();
                    if (fragment != null) {
                        FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
                        helper.addToBackStack(fragment.getClass().getSimpleName());
                        helper.setArguments(bundle);
                        activity.changeFragment(helper);
                    }
                }
            }
        });
    }


    private void initRefreshLayout(LayoutInflater inflater) {
        mFooterLayout = inflater.inflate(R.layout.listview_footer, null);
        mMoreProgress = (ProgressBar) mFooterLayout.findViewById(R.id.load_progress_bar);
        mRefreshLayout.setChildView(listView);
//        使用SwipeRefreshLayout的下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
            }
        });
        //        使用SwipeRefreshLayout的上拉加载
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                ParkingLotTask parkingLotTask = new ParkingLotTask(mParkingLotList.size());
                parkingLotTask.execute();
            }
        });
        mRefreshLayout.setLoading(true);
    }

    /**
     * 请求停车场列表
     */
    private class ParkingLotTask extends AsyncTask<Void, Integer, String> {

        private List<ParkingLot> lotList;
        private int startIndex;
        ProgressDialog progressDialog;

        public ParkingLotTask(int startIndex) {
            this.startIndex = startIndex;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMoreProgress.setVisibility(View.VISIBLE);
            if (mParkingLotList.isEmpty()) {
                progressDialog = DialogHelper.showProgressDialog(getContext(), "正努力加载，请稍候。。。", false);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            lotList = ParkingControler.getAllParkingLot(startIndex, 10);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mMoreProgress.setVisibility(View.GONE);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            mRefreshLayout.setLoading(false);
            if (lotList != null) {
                mParkingLotList.addAll(lotList);
                if (mFooterLayout.getParent() == null) {
                    listView.addFooterView(mFooterLayout);
                }
                parkLotAdapter.notifyDataSetChanged(mParkingLotList);
            }
        }
    }


}
