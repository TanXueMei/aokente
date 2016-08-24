package com.zhichen.parking.lib.hometab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import java.util.List;

public class ViewPagerTab extends BaseTab implements OnPageChangeListener
{
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;

	public ViewPagerTab(FragmentManager fm)
	{
		super(fm);
	}
	
	public void setup(ViewPager vp, List<TabBundle> tabPages, boolean releasePage)
	{
		setup(tabPages);
		mViewPager = vp;
		initAdapter(releasePage);
	}

	private void initAdapter(boolean releasePage)
	{
		mAdapter = new FragmentPagerAdapter(mFragmentManager) {

			@Override
			public int getCount() {
				return mTabBundles.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mTabBundles.get(arg0).tabFragment;
			}
		};
		
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
	}


	@Override
	public void onPageSelected(int arg0) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
		if (positionOffset > 0) {
			ChangeColorIconView left = mTabBundles.get(position).tabIndicator;
			ChangeColorIconView right = mTabBundles.get(position + 1).tabIndicator;

			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onSelectTabItem(int index)
	{
		mViewPager.setCurrentItem(index, false);
	}
}
