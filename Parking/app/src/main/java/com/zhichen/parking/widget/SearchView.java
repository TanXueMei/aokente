package com.zhichen.parking.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yetwish on 2015-05-11
 */
public class SearchView extends LinearLayout implements View.OnClickListener {

	private static final String HISTORY = "history";
	
    private EditText etInput;
    private ImageView ivDelete;
    private Button btnSearch;
    private Context mContext;

    private List<String>	mHistoryList ;
    private SearchViewListener mListener;
    private SharedPreferences mSharedPreferences ;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(HISTORY, Context.MODE_PRIVATE);
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);
        initViews();
    }

    private void initViews() {
        etInput = (EditText) findViewById(R.id.search_et_input);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        btnSearch = (Button) findViewById(R.id.go_search_btn);

        ivDelete.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnClickListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    notifyStartSearching(etInput.getText().toString());
                }
                return true;
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     * @param text
     */
    public void notifyStartSearching(String text){
        if (mListener != null) {
            mListener.onSearch(etInput.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(VISIBLE);
                if (mListener != null) {
                    mListener.onRefreshAutoComplete(charSequence + "");
                }
            } else {
                ivDelete.setVisibility(GONE);
                if (mListener != null) {
                    mListener.onClear();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_et_input:
                break;
            case R.id.search_iv_delete:{
            	etInput.setText("");
            	ivDelete.setVisibility(GONE);
            	if (mListener != null) {
            		mListener.onClear();
            	}
            }
            break;
            case R.id.go_search_btn:
                notifyStartSearching(etInput.getText().toString());
                break;
        }
    }
    
    public void setSearchText(String text)
    {
		etInput.setText(text);
		etInput.setSelection(text.length());
    }
    
    private static final String CDATA_PRE = "<![CDATA[";
    private static final String CDATA_SUF = "]]>";
    
    public void openHistory()
    {
    	String his = mSharedPreferences.getString(HISTORY, null);
    	if(his != null){
    		his = his.substring(CDATA_PRE.length(), his.length()-CDATA_SUF.length());
    		Gson gson = new Gson();
    		Type type = new TypeToken<List<String>>(){}.getType();
    		mHistoryList = gson.fromJson(his, type);
    	}
    	if(mHistoryList == null){
    		mHistoryList = new ArrayList<String>();
    	}
    }

    public void saveHistory(String text)
    {
    	if(text == null || text.trim().isEmpty()){
    		return ;
    	}
        if( ! mHistoryList.contains(text)){
        	mHistoryList.add(0, text);
        	Gson gson = new Gson();
        	Type type = new TypeToken<List<String>>(){}.getType();
        	String his = gson.toJson(mHistoryList, type);
        	Editor edit = mSharedPreferences.edit();
        	edit.putString(HISTORY, CDATA_PRE + his + CDATA_SUF);
        	edit.commit();
        }
    }
    
    public List<String> getHistory()
    {
    	return mHistoryList ;
    }
    
    public void clearHistory()
    {
    	Editor edit = mSharedPreferences.edit();
    	edit.remove(HISTORY);
    	edit.commit();
		mHistoryList.clear() ;
    }
    
    public void setSearchViewListener(SearchViewListener listener)
    {
        mListener = listener;
    }

    /**
     * search view回调方法
     */
    public interface SearchViewListener {

    	/**
    	 * 清空输入输入框内容 
    	 */
    	void onClear();
    	
        /**
         * 更新自动补全内容
         * @param text 传入补全后的文本
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         * @param text 传入输入框的文本
         */
        void onSearch(String text);
    }
}

