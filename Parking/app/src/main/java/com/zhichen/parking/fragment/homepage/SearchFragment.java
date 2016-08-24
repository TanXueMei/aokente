package com.zhichen.parking.fragment.homepage;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.adapter.SearchAdapter;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.model.ParkingLot;
import com.zhichen.parking.servercontoler.ParkingControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.tools.FragmentChangeHelper;
import com.zhichen.parking.tools.GsonUtil;
import com.zhichen.parking.util.DensityUtil;
import com.zhichen.parking.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-06-08.
 */
public class SearchFragment extends BaseFragment {

    View mViewRoot;
    private ImageView back;

    private SearchView mSearchView;
    private ListView mHistoryLv;
    private View mHistoryLayout;
    List<ParkingLot> mParkingLotList;

    private ListView mResultLv;
    private SearchAdapter mResultAdapter;

    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_search, container, false);
        mParkingLotList = new ArrayList<>();
        initUI();
        return mViewRoot;
    }

    private void initUI() {
        back = (ImageView) mViewRoot.findViewById(R.id.title_back_iv);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mHistoryLayout = mViewRoot.findViewById(R.id.search_history_layout);
        mHistoryLv = (ListView) mViewRoot.findViewById(R.id.search_history_lv);
        mSearchView = (SearchView) mViewRoot.findViewById(R.id.title_searchview);
        mSearchView.openHistory();

        mResultLv = (ListView) mViewRoot.findViewById(R.id.search_result_lv);
        mResultAdapter = new SearchAdapter(getActivity(), mParkingLotList);
        mResultLv.setAdapter(mResultAdapter);
        mResultLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= mParkingLotList.size()){
                    return ;
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
        });

        final SearchView.SearchViewListener listener = new SearchView.SearchViewListener() {
            @Override
            public void onClear() {
                mHistoryLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRefreshAutoComplete(String text) {
                mHistoryLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearch(String text) {
                mParkingLotList.clear();
                mResultAdapter.notifyDataSetChanged();
                mSearchView.saveHistory(text);
                mHistoryLayout.setVisibility(View.GONE);

                LotTask task = new LotTask(text);
                task.execute();
            }
        };
        mSearchView.setSearchViewListener(listener);

        List<String> histories = mSearchView.getHistory();
        final ArrayAdapter<String> historyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, histories);
        mHistoryLv.setAdapter(historyAdapter);
        TextView clearFooter = new TextView(getActivity());
        clearFooter.setText("清空历史");
        clearFooter.setTextSize(14);
        clearFooter.setTextColor(Color.parseColor("#FFB90F"));
        int p5 = DensityUtil.dip2px(getActivity(), 6);
        clearFooter.setPadding(p5, p5, p5*4, p5);
        clearFooter.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        clearFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.clearHistory();
                historyAdapter.notifyDataSetChanged();
            }
        });
        mHistoryLv.addFooterView(clearFooter);

        mHistoryLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String text = historyAdapter.getItem(i).toString();
                mSearchView.setSearchText(text);
                listener.onSearch(text);
            }
        });
    }

    /**
     * 请求搜索停车场信息
     */
    private class LotTask extends AsyncTask<Void, Integer, String> {
        List<ParkingLot> lotList;
        ProgressDialog pd ;
        String text ;
        public LotTask(String text){
            this.text = text ;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mParkingLotList.isEmpty()){
                pd = DialogHelper.showProgressDialog(getActivity(), "正努力加载，请稍候。。。", false);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            lotList = ParkingControler.searchParkingLot(text);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }
            if(lotList != null){
                mParkingLotList.addAll(lotList);
                mResultAdapter.notifyDataSetChanged();
            }
        }
    }
}
