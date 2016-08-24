package com.zhichen.parking.fragment.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.DialogHelper;

/**
 * Created by xuemei on 2016-05-30.
 * 建议反馈
 */
public class FeedBackFragment extends Fragment implements View.OnClickListener {

    private EditText editText;
    private Button submit;
    private TextView textNum;
    public FeedBackFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
        editText= (EditText) view.findViewById(R.id.suggest_input_et);
        submit= (Button) view.findViewById(R.id.btn_submit);
        textNum= (TextView) view.findViewById(R.id.textNum);
        submit.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;
            int maxLen = 100;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
//                Log.i("orderuser", "您还能输入" + (maxLen - temp.length()) + "个字");
            }

            @Override
            public void afterTextChanged(Editable s) {
//                int number = num - s.length();
                textNum.setText("" + s.length());
                selectionStart = editText.getSelectionStart();
                selectionEnd = editText.getSelectionEnd();
                if (temp.length() > maxLen) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    editText.setText(s);
                    editText.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                String content=editText.getText().toString();
                if(content == null || content.trim().isEmpty()){
                    Toast.makeText(getContext(), "请输入您的反馈建议！", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(content.trim().length() > 100){
                    Toast.makeText(getContext(), "输入字数超过了100，请减少", Toast.LENGTH_SHORT).show();
                    return ;
                }
                SubmitTask task = new SubmitTask(content);
                task.execute();
                break;
        }
    }

    private class SubmitTask extends AsyncTask<Void, Integer, String> {

        String comment ;
        ProgressDialog mProgressDialog;

        public SubmitTask(String strSugest) {
            this.comment = strSugest ;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = DialogHelper.showProgressDialog(getContext(), this, "提交建议中，请稍后。。。");
        }

        @Override
        protected String doInBackground(Void... params) {
            UserControler.submitSuggest(getContext(), comment);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
//            提交成功后 退出此界面
            if (getContext() instanceof Activity) {
                Activity activity = (Activity) getContext();
                activity.onBackPressed();
            }
        }
    }
}
