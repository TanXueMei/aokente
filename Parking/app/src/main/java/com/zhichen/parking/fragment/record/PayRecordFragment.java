package com.zhichen.parking.fragment.record;

import android.app.ProgressDialog;
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
import com.zhichen.parking.adapter.PayRecordAdapter;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.library.refreshlayout.RefreshLayout;
import com.zhichen.parking.model.PayRecord;
import com.zhichen.parking.servercontoler.PayControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.tools.MyLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-07-11.
 * 缴费记录
 */
public class PayRecordFragment extends Fragment {
    private View mViewRoot;
    ListView listView;
    private PayRecordAdapter payRecordAdapter;
    private List<PayRecord> payRecordList;
    RefreshLayout mRefreshLayout;
    View mFooterLayout;
    ProgressBar mMoreProgressBar;

    public PayRecordFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_payrecord, container, false);
        payRecordList = new ArrayList<>();
        mRefreshLayout = (RefreshLayout) mViewRoot.findViewById(R.id.swipe_container);
        listView = (ListView) mViewRoot.findViewById(R.id.pay_record_listview);
        payRecordAdapter = new PayRecordAdapter(getContext());
        listView.setAdapter(payRecordAdapter);
        GetPayRecordTask getPayRecordTask = new GetPayRecordTask(payRecordList.size());
        getPayRecordTask.execute();
        initRefreshLayout(inflater);
        return mViewRoot;
    }
    private ProgressDialog mProgressDialog;
    private class GetPayRecordTask extends AsyncTask<Void, Integer, List<PayRecord>> {
        MyLogger logger=MyLogger.getLogger(Constants.USERNAME);
        private int startindex;
        public GetPayRecordTask(int startindex) {
            this.startindex = startindex;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("cwf","onPreExecute");
//            mProgressDialog = DialogHelper.showProgressDialog(getContext(), GetPayRecordTask.this, "正在加载数据，请稍后...");

        }
        @Override
        protected List<PayRecord> doInBackground(Void... params) {
            List<PayRecord> payRecordList = PayControler.getPayRecord(startindex);
            if (payRecordList != null) {
                return payRecordList;
            }
            return payRecordList;
        }

        @Override
        protected void onPostExecute(List<PayRecord> payRecords) {
            super.onPostExecute(payRecords);
//            if (mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//            }

            mMoreProgressBar.setVisibility(View.GONE);
            mRefreshLayout.setLoading(false);
            logger.i("payRecords==="+payRecords);
            if(payRecords!=null){
                payRecordAdapter.notifyDataSetChanged(payRecords);
            }
        }

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
                GetPayRecordTask task = new GetPayRecordTask(payRecordList.size());
                task.execute();
            }
        });

        mRefreshLayout.setLoading(true);
    }



}
