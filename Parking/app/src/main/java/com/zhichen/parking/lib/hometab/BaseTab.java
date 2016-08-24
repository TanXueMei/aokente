package com.zhichen.parking.lib.hometab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.List;

public abstract class BaseTab implements OnClickListener 
{
	protected FragmentManager mFragmentManager ;
	protected List<TabBundle> mTabBundles ;

	public BaseTab(FragmentManager fm)
	{
		mFragmentManager = fm ;
	}

	protected void setup(List<TabBundle> tabPages)
	{
		mTabBundles = tabPages ;
		initTabIndicator();
	}

	private void initTabIndicator()
	{
		for(TabBundle tab : mTabBundles)
		{
			tab.tabIndicator.setOnClickListener(this);
		}
		mTabBundles.get(0).tabIndicator.setIconAlpha(1.0f);
	}

	@Override
	public void onClick(View v)
	{
		resetOtherTabs();
		
		final int size = mTabBundles.size();
		for(int i=0; i<size; ++i)
		{
			TabBundle tab = mTabBundles.get(i);
			if(v.getId() == tab.tabIndicator.getId())
			{
				tab.tabIndicator.setIconAlpha(1.0f);
				onSelectTabItem(i);
				break;
			}
		}
	}

	/**
	 * 重置其他的Tab
	 */
	private void resetOtherTabs()
	{
		for(TabBundle tab : mTabBundles){
			tab.tabIndicator.setIconAlpha(0);
		}
	}
	
	public abstract void onSelectTabItem(int index);
	
	public static class TabBundle
	{
		protected ChangeColorIconView tabIndicator ;
		protected Fragment tabFragment ;

		public TabBundle(ChangeColorIconView tabIndicator, Fragment tabFragment)
		{
			this.tabIndicator = tabIndicator ;
			this.tabFragment = tabFragment ;
		}
	}
}
