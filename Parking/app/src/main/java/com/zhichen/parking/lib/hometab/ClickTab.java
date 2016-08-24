package com.zhichen.parking.lib.hometab;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

public class ClickTab extends BaseTab
{
	private int mFragmentLayoutId ;
	
	public ClickTab(FragmentManager fm)
	{
		super(fm);
	}
	
	public void setup(List<TabBundle> tabPages, int layoutId)
	{
		setup(tabPages);
		mFragmentLayoutId = layoutId ;
		FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();
		for(int i=0; i<mTabBundles.size(); ++i){
			beginTransaction.add(mFragmentLayoutId, mTabBundles.get(i).tabFragment, String.valueOf(i));
		}
		beginTransaction.commit();
		onClick(mTabBundles.get(3).tabIndicator);
	}

	@SuppressLint("Recycle")
	@Override
	public void onSelectTabItem(int index)
	{
		// TODO Auto-generated method stub
		FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();
		for(int i=0; i<mTabBundles.size(); ++i)
		{
			TabBundle bundle = mTabBundles.get(i);
			if(i == index){
				beginTransaction.show(bundle.tabFragment);
			}
			else{
				beginTransaction.hide(bundle.tabFragment);
			}
		}
		beginTransaction.commit();
	}
}
