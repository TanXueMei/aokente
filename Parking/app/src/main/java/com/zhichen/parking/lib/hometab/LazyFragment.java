package com.zhichen.parking.lib.hometab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class LazyFragment extends Fragment
{  
    protected boolean mHasLoaded = false;  
    protected boolean mIsPrepareToLoad = false;  
	
    @Override  
    public void setUserVisibleHint(boolean isVisibleToUser) 
    {  
        super.setUserVisibleHint(isVisibleToUser);  
        if(getUserVisibleHint() && !isLazyLoaded() && isPrepareToLoad())
        {
        	mHasLoaded = true;  
            doLazyLoad();
        } 
    }  
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		mHasLoaded = false ;
		mIsPrepareToLoad = true ;

		View view = doCreateView(inflater, container, savedInstanceState);
		if(getUserVisibleHint())
		{
			mHasLoaded = true ;
			doLazyLoad();
		}
		
		return view;
	}

	@Override
	public void onDestroyView() 
	{
		super.onDestroyView();
		
		mHasLoaded = false ;
		mIsPrepareToLoad = false;  
		
		doDestroyView();
	}
	
    /**
     * 用于判断是否需要可以执行 onLazyLoad
     * setUserVisibleHint 先于 onCreateView 执行
     */
	protected boolean isPrepareToLoad()
	{
		return mIsPrepareToLoad ;
	}
	
    /**
     * 用于判断是否需要重新执行 onLazyLoad
     */
	protected boolean isLazyLoaded()
	{
		return mHasLoaded;
	}
	
	/**
	 *  onCreateView执行时必须加载的部分
	 */
	protected abstract View doCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);
    
    /**  
     * 延迟加载部分，当此 Fragment 处于可见状态会回调
     */  
    protected abstract void doLazyLoad();  
    
	
	/**
	 *  onDestroyView执行时回调
	 */
    protected abstract void doDestroyView();  
}
