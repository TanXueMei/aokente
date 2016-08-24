package com.zhichen.parking.tools;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Fragment管理类
 */
public class FragmentChangeHelper {
    private Fragment targetFragment;
    private Bundle args;
    private String targetFragmentTag;
    private String[] removeFragmentTag;
    private boolean clearAllBackStack;
    private String backToFragmentTag;


    public FragmentChangeHelper(){}
    /**
     * 构造方法，传入需要跳转的Fragment
     * @param targetFragment
     */
    public FragmentChangeHelper(Fragment targetFragment) {
        this.targetFragment = targetFragment;
    }

    /**
     * 构造方法，需要传入会退栈的目标Fragment tag
     * @param backToFragmentTag
     */
    public FragmentChangeHelper(String backToFragmentTag) {
        this.backToFragmentTag = backToFragmentTag;
    }

    /**
     * 设置需要传给目标Fragment 的数据
     * @param args
     */
    public void setArguments(Bundle args){
        this.args = args;
    }

    /**
     * 将目标Fragment 添加到会退栈，同时为Fragment添加tag
     * @param targetFragmentTag
     */
    public void addToBackStack(String targetFragmentTag){
        this.targetFragmentTag = targetFragmentTag;
    }

    /**
     * 删除回退栈中指定Tag的Fragment，可同时删除多个
     * @param removeFragmentTag
     */
    public void removeFragmentFromBackStack(String... removeFragmentTag){
        this.removeFragmentTag = removeFragmentTag;
    }


    /**
     * 设置是否在跳转前清空回退栈
     * @param clearAllBackStack
     */
    public void clearAllBackStack(boolean clearAllBackStack) {
        this.clearAllBackStack = clearAllBackStack;
    }


    ////////////////////////////////////////////////////////
    // 获取内容的getter方法
    public Fragment getTargetFragment() {
        return targetFragment;
    }

    public Bundle getArgs() {
        return args;
    }

    public String getTargetFragmentTag() {
        return targetFragmentTag;
    }

    public String[] getRemoveFragmentTag() {
        return removeFragmentTag;
    }

    public boolean isClearAllBackStack() {
        return clearAllBackStack;
    }

    public String getBackToFragmentTag() {
        return backToFragmentTag;
    }
}
