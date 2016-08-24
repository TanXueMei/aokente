package com.zhichen.parking.servercontoler;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.ParkHistoryIn;
import com.zhichen.parking.model.ParkHistoryOut;
import com.zhichen.parking.model.ParkingLot;
import com.zhichen.parking.model.ParkingLotList;
import com.zhichen.parking.tools.GsonUtil;
import com.zhichen.parking.tools.MyLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by xuemei on 2016-06-22.
 * 有关停车场 的请求类
 */
public class ParkingControler {
    public static final int MAX_RESULTS = 40;

    /**
     * 停车场信息 公共部分
     */
    private static ParkingLotList getParkingLotList(final String url) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final String userName = UserManager.instance().getUserName();
        final String password = UserManager.instance().getPassWord();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        logger.d( "getParkingLotList -- result=" + result);
        ParkingLotList parkinglotlist = null;
        if (result != null) {
            try {
                parkinglotlist = GsonUtil.createGson().fromJson(result, ParkingLotList.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return parkinglotlist;
    }
    /**
     * 获取全部停车场信息
     * @param startIndex  开始下标
     * @param num
     * @return
     */
    public static List<ParkingLot> getAllParkingLot(int startIndex, int num) {
        final String url = Constants.BASE_PARK_URL + "/parking/parking_lots/?"
                + "start_index=" + startIndex + "&max_results=" + num;
        ParkingLotList parkinglotlist = getParkingLotList(url);
        return (parkinglotlist == null) ? null : parkinglotlist.getParking_lots();
    }
    /**
     * 根据下标 获取所有停车信息
     * @param startIndex
     * @return
     */
    public static List<ParkingLot> getAllParkingLot(int startIndex) {
        return getAllParkingLot(startIndex, MAX_RESULTS);
    }
    /**
     * 根据id获取停车场信息
     * @param id
     * @return
     */
    public static ParkingLot getParkingLotById(String id) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final String url = Constants.BASE_PARK_URL + "/parking/parking_lots/?id=" + id;
        ParkingLotList parkinglotlist = getParkingLotList(url);
        logger.d("getParkingLotById==="+parkinglotlist);
        if (parkinglotlist != null && parkinglotlist.getParking_lots().size() > 0) {
            return parkinglotlist.getParking_lots().get(0);
        }
        return null;
    }
    /**
     *
     * @param cityCode
     * @param startIndex
     * @return
     */
    public static List<ParkingLot> getParkingLotOfCity(String cityCode, int startIndex) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final String url = Constants.BASE_PARK_URL+ "/parking/parking_lots/?city_code=" + cityCode
                + "&start_index=" + startIndex + "&max_results=" + MAX_RESULTS;
        ParkingLotList parkinglotlist = getParkingLotList(url);
       logger.d("getParkingLotOfCity=="+parkinglotlist);
        return (parkinglotlist == null) ? null : parkinglotlist.getParking_lots();
    }

    /**
     * 获取附近停车场
     * @param latitude
     * @param longitude
     * @param distance
     * @param startIndex
     * @return
     */
    public static List<ParkingLot> getParkingLotOfNearby(double latitude, double longitude, long distance, int startIndex) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final String url = Constants.BASE_PARK_URL + "/parking/parking_lots/?longitude=" + longitude
                + "&latitude=" + latitude + "&distance=" + distance
                + "&start_index=" + startIndex + "&max_results=" + MAX_RESULTS;
        ParkingLotList parkinglotlist = getParkingLotList(url);
       logger.d("getParkingLotOfNearby 附近停车场==="+parkinglotlist.toString());
        return (parkinglotlist == null) ? null : parkinglotlist.getParking_lots();
    }

    /**
     *根据名字 搜索停车场
     * @param name
     * @return
     */
    public static List<ParkingLot> searchParkingLot(String name) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        logger.d("searchParkingLot名称关键词==="+name);
        final String url =  Constants.BASE_PARK_URL + "/parking/parking_lots/?name=" + name
                + "&start_index=" + 0 + "&max_results=" + MAX_RESULTS;
        ParkingLotList parkinglotlist = getParkingLotList(url);
       logger.d("searchParkingLot==="+parkinglotlist);
        return (parkinglotlist == null) ? null : parkinglotlist.getParking_lots();
    }

    /**
     * 进场请求
     * @param carNumber
     * @param startIndex
     * @return
     */
    public static List<ParkHistoryIn> getParkHistoryInList(final String carNumber, int startIndex) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        logger.d("进场车牌号===" +carNumber);
        String url;
        String result = null;
        List<ParkHistoryIn> parkHistoryInList;
        url = Constants.VEHICLE_IN+ carNumber
                + "&start_index=" + startIndex + "&max_results=" + MAX_RESULTS;
        result = request(url);
        try {
            if(result!=null){
                JSONObject json = new JSONObject(result);
                logger.d("进场json===" +json);
                parkHistoryInList = new Gson().fromJson(json.getJSONArray("records").toString(), new TypeToken<List<ParkHistoryIn>>() {
                }.getType());
                return parkHistoryInList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 出场请求
     * @param carNumber
     * @param startIndex
     * @return
     */
    public static List<ParkHistoryOut> getParkHistoryOutList(final String carNumber, int startIndex) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        logger.d("出场车牌号===" +carNumber);
        String url;
        String result = null;
        List<ParkHistoryOut> parkHistoryOutList = new ArrayList<>();
        url = Constants.VEHICLE_OUT + carNumber
                + "&start_index=" + startIndex + "&max_results=" + MAX_RESULTS;
        result = request(url);
        try {
            if(result!=null){
                JSONObject json = new JSONObject(result);
                logger.d("出场json===" +json);
                parkHistoryOutList = new Gson().fromJson(json.getJSONArray("records").toString(), new TypeToken<List<ParkHistoryOut>>() {
                }.getType());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parkHistoryOutList;
    }

    /**
     * 进出场请求主体部分
     * @param url
     * @return
     */
    public static String request(String url) {
        final String userName = UserManager.instance().getUserName();
        final String password = UserManager.instance().getPassWord();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        return result;
    }

}
