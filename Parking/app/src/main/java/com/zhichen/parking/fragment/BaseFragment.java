package com.zhichen.parking.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zhichen.parking.activity.MainActivity;

public class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();

    protected MainActivity activity;
    Activity	mCommonAct ;
    public static final String FRAGMENT_CLASS = "fragment_cls";
    public static final String FRAGMENT_ARGS = "fragment_args";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCommonAct =activity ;
        if(activity instanceof MainActivity){
            this.activity = (MainActivity) activity;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager manager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN){
                    if(getActivity().getCurrentFocus()!=null &&getActivity().getCurrentFocus().getWindowToken()!=null){
                        manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return true;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }


    public static void enterFrament(Context context, String className, Bundle args) {
        Intent intent = new Intent(context, BaseFragment.class);
        intent.putExtra(BaseFragment.FRAGMENT_CLASS, className);
        if(args != null){
            intent.putExtra(FRAGMENT_ARGS, args);
        }
        context.startActivity(intent);
    }

    public void quitCommonAct() {
        mCommonAct.finish();
    }





}
