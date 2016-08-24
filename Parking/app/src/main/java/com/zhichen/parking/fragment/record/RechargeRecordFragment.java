package com.zhichen.parking.fragment.record;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.zhichen.parking.R;
import com.zhichen.parking.adapter.RechargeRecordAdapter;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.library.refreshlayout.RefreshLayout;
import com.zhichen.parking.model.PayRecord;
import com.zhichen.parking.model.RechargeRecord;
import com.zhichen.parking.servercontoler.PayControler;
import com.zhichen.parking.tools.MyLogger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xuemei on 2016-08-11.
 * 充值记录
 */
public class RechargeRecordFragment extends Fragment {
    private View mViewRoot;
    private ListView listView;
    private RefreshLayout mRefreshLayout;
    private View mFooterLayout;
    private ProgressBar mMoreProgressBar;
    private RechargeRecordAdapter adapter;
    private List<RechargeRecord> rechargeRecords;

    public RechargeRecordFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_rechargerecord, container, false);
        mRefreshLayout = (RefreshLayout) mViewRoot.findViewById(R.id.swipe_container);
        listView = (ListView) mViewRoot.findViewById(R.id.recharge_record_listview);
        rechargeRecords=new ArrayList<>();
        adapter=new RechargeRecordAdapter(getContext());
        listView.setAdapter(adapter);
        GetRechargeRecordTask getRechargeRecordTask = new GetRechargeRecordTask(rechargeRecords.size());
        getRechargeRecordTask.execute();

        initRefreshLayout(inflater);
        return mViewRoot;
    }
    private void initRefreshLayout(LayoutInflater inflater) {
        // 这里可以替换为自定义的footer布局
        mFooterLayout = inflater.inflate(R.layout.listview_footer, null);
        mMoreProgressBar = (ProgressBar) mFooterLayout.findViewById(R.id.load_progress_bar);
        mRefreshLayout.setChildView(listView);

        mRefreshLayout.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red,
                R.color.google_yellow);

        // 使用SwipeRefreshLayout的下拉刷新监听
        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
            }
        });
        // 使用自定义的RefreshLayout加载更多监听
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                GetRechargeRecordTask getRechargeRecordTask = new GetRechargeRecordTask(rechargeRecords.size());
                getRechargeRecordTask.execute();
            }
        });

        mRefreshLayout.setLoading(true);
    }

    /**
     * 请求 充值记录
     */
    private class GetRechargeRecordTask extends AsyncTask<Void, Integer,  List<RechargeRecord>> {
        MyLogger logger=MyLogger.getLogger(Constants.USERNAME);
        private int startindex;
        public GetRechargeRecordTask(int startindex) {
            this.startindex = startindex;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected  List<RechargeRecord> doInBackground(Void... params) {
            List<RechargeRecord>  rechargeRecords = PayControler.getRechargeRecord(startindex);
            if (rechargeRecords != null) {
                return rechargeRecords;
            }
            return rechargeRecords;
        }

        @Override
        protected void onPostExecute( List<RechargeRecord> rechargeRecords) {
            super.onPostExecute(rechargeRecords);
            mMoreProgressBar.setVisibility(View.GONE);
            mRefreshLayout.setLoading(false);
            logger.i("rechargeRecords==="+rechargeRecords);
            if(rechargeRecords!=null){
                adapter.notifyDataSetChanged(rechargeRecords);
            }
        }

    }

}
