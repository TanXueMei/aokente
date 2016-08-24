package com.zhichen.parking.lotmanager;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import com.zhichen.parking.common.AppConstants;
import com.zhichen.parking.lib.messageservice.MessageService;
import com.zhichen.parking.model.ParkingLot;
import com.zhichen.parking.servercontoler.ParkingControler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLotManager {

    private static final int TYPE_ALL = 0;
    private static final int TYPE_DISTANCE = 1;

    private static final ParkingLotManager ins = new ParkingLotManager();

    private List<MiniParkingLot> mMiniParkingLotList;
    private LatLng mLastPos;

    private Object mLock = new Object();

    public static ParkingLotManager instance() {
        return ins;
    }

    private ParkingLotManager() {
        mMiniParkingLotList = new ArrayList<>();
    }

    public void clear() {
        if (mMiniParkingLotList != null) {
            mMiniParkingLotList.clear();
        }
        mLastPos = null;
    }

    public List<MiniParkingLot> getMiniParkingLotList() {
        synchronized (mLock) {
            return mMiniParkingLotList;
        }
    }

    public void updateParkingLotByAll() {
        UpdateThread thread = new UpdateThread(TYPE_ALL, null);
        thread.start();
    }

    public void updateParkingLotByDistance(double latitude, double longitude, long distanceInMeter, long distance4Last) {
        LatLng curPos = new LatLng(latitude, longitude);
        if (mLastPos != null) {
            long dis = (long) DistanceUtil.getDistance(curPos, mLastPos);
            if (dis < distance4Last) {
                return;
            }
        }
        mLastPos = curPos;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("distance", distanceInMeter);
        UpdateThread thread = new UpdateThread(TYPE_DISTANCE, params);
        thread.start();
    }

    private List<ParkingLot> getParkingLots(int type, int startIndex, Map<String, Object> params) {
        if (type == TYPE_ALL) {
            return ParkingControler.getAllParkingLot(startIndex);
        }
        if (type == TYPE_DISTANCE) {
            double latitude = (double) params.get("latitude");
            double longitude = (double) params.get("longitude");
            long distance = (long) params.get("distance");
            return ParkingControler.getParkingLotOfNearby(latitude, longitude, distance, startIndex);
        }
        return null;
    }

    private class UpdateThread extends Thread {
        private int type;
        private Map<String, Object> params;

        public UpdateThread(int type, Map<String, Object> params) {
            this.type = type;
            this.params = params;
        }

        @Override
        public void run() {
            List<MiniParkingLot> lotList = new ArrayList<>();
            List<ParkingLot> fullLotList = new ArrayList<>();
            boolean succeed = false;
            double result=0;
            int mytype=0;
            while (true) {
                List<ParkingLot> parkingLots = getParkingLots(type, lotList.size(), params);
                if (parkingLots != null) {
                    fullLotList.addAll(parkingLots);
                    succeed = true;
                    for (ParkingLot lot : parkingLots) {
//						可用车位数
                        int available = lot.getParkingSpacesAvailable();
                        int totle = lot.getParkingSpaces();
                        NumberFormat numberFormat=NumberFormat.getNumberInstance();
                        result= Double.parseDouble((numberFormat.format((double) available/(double) totle*100)));
//                        Log.e("xue","百分比available"+available);
//                        Log.e("xue","百分比totle"+totle);
//                        Log.e("xue","百分比result"+result);
                        if(result<10||result==10){
                            mytype=AppConstants.TYPE_ICON_RED;
                        }else if(result<50||result==50){
                            mytype=AppConstants.TYPE_ICON_BLUE;
                        }else if(result>50){
                            mytype=AppConstants.TYPE_ICON_GREEN;
                        }else {
                            mytype=AppConstants.TYPE_ICON_GRAY;
                        }

                        lotList.add(new MiniParkingLot(mytype, lot.getLatitude(), lot.getLongitude(), lot.getId()));
                    }
                }
                if (parkingLots == null && !succeed) {
                    try {
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    continue;
                }
                if (parkingLots == null || parkingLots.size() < ParkingControler.MAX_RESULTS) {
                    break;
                }
            }
            if (succeed) {
                MiniParkingLot checkLot = null;
                int index = lotList.size();
                while (--index >= 0) {
                    checkLot = lotList.get(index);
                    for (MiniParkingLot lot : mMiniParkingLotList) {
                        if (lot.getParkingLotId() == checkLot.getParkingLotId()) {
                            lotList.remove(index);
                            break;
                        }
                    }
                }
                if (lotList.size() > 0) {
                    synchronized (mLock) {
                        mMiniParkingLotList.addAll(lotList);
                    }
                    MessageService.instance().notifyMessage(AppConstants.TYPE_PARKINGLOT_LOADED, null);
                }
            }
        }
    }

}
