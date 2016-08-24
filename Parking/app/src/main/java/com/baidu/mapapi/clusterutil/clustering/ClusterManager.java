/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.baidu.mapapi.clusterutil.clustering;

import android.content.Context;

import com.baidu.mapapi.clusterutil.clustering.algo.Algorithm;
import com.baidu.mapapi.clusterutil.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.baidu.mapapi.clusterutil.clustering.algo.PreCachingAlgorithmDecorator;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Groups many items on a map based on zoom level.
 * <p/>
 * ClusterManager should be added to the map
 * <li>
 */
public class ClusterManager<T extends ClusterItem>{

    private Algorithm<T> mAlgorithm;
    private final ReadWriteLock mAlgorithmLock = new ReentrantReadWriteLock();

    private BaiduMap mMap;
    private MapStatus mPreviousCameraPosition;
    
    private int mLastCount ;
    private Collection<T> mComputeCitems ;
    
    public ClusterManager(Context context, BaiduMap map) {
        mMap = map;
        mAlgorithm = new PreCachingAlgorithmDecorator<T>(new NonHierarchicalDistanceBasedAlgorithm<T>());
    }

    public void setAlgorithm(Algorithm<T> algorithm) {
        mAlgorithmLock.writeLock().lock();
        try {
            if (mAlgorithm != null) {
                algorithm.addItems(mAlgorithm.getItems());
            }
            mAlgorithm = new PreCachingAlgorithmDecorator<T>(algorithm);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
        cluster();
    }
    
    public void updateItems(Collection<T> items)
    {
    	if(items == null){
    		return ;
    	}
    	if(mLastCount == items.size()){
    		return ;
    	}
    	if(mComputeCitems != null){
    		clearItems();
    	}
    	mLastCount = items.size();
    	mComputeCitems = items ;
    	addItems(mComputeCitems);
    }

    public void clearItems() {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.clearItems();
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
    }

    public void addItems(Collection<T> items) {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.addItems(items);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
    }

    public void addItem(T myItem) {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.addItem(myItem);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
    }

    public void removeItem(T item) {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.removeItem(item);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
    }

    /**
     * Force a re-cluster. You may want to call this after adding new item(s).
     */
    public Set<? extends Cluster<T>> cluster() {
    	mAlgorithmLock.readLock().lock();
    	try {
    		return mAlgorithm.getClusters(mMap.getMapStatus().zoom);
    	} finally {
    		mAlgorithmLock.readLock().unlock();
    	}
    }

    public void onMapStatusChange(MapStatus mapStatus) {
        // Don't re-compute clusters if the map has just been panned/tilted/rotated.
        MapStatus position = mMap.getMapStatus();
        if (mPreviousCameraPosition != null && mPreviousCameraPosition.zoom == position.zoom) {
            return;
        }
        mPreviousCameraPosition = mMap.getMapStatus();

        cluster();
    }

}
