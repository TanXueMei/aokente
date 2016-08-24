package com.zhichen.parking.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zhichen.parking.model.User.Vehicle;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhichen.parking.common.AppConstants;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.model.User;
import com.zhichen.parking.servercontoler.ServerManger;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.MyLogger;

import java.util.List;

/**
 * 个人中心 信息管理类
 */
public class UserManager {
	
	private static final String CFG_NAME = "config";
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_PASSWORD = "password";
	
	private static final UserManager ins = new UserManager();
	
	private User mUser ;
	
	private String 	mUserName ;
	private String 	mPassWord ;
	private String 	mAvatarPath = AppConstants.LOCAL_DIR + "/avatar.jpg";
	
	private BDLocation mLocation ;
	private LatLng mMapTargetPos ;

	public static UserManager instance()
	{
		return ins ;
	}
	
	private UserManager(){
	}
	
	public void clear()
	{
		mUser = null ;
		mLocation = null;
	}
	
	public void loadConfig(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(CFG_NAME, Context.MODE_PRIVATE);
		mUserName = sp.getString(KEY_USER_NAME, null);
		mPassWord = sp.getString(KEY_PASSWORD, null);
	}
	
	public void saveConfig(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(CFG_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(KEY_USER_NAME, mUserName);
		edit.putString(KEY_PASSWORD, mPassWord);
		edit.commit();
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String userName) {
		this.mUserName = userName;
	}

	public String getPassWord() {
		return mPassWord;
	}

	public void setPassWord(String passWord) {
		this.mPassWord = passWord;
	}

	public String getNickName() {
		String name = null ;
		if(mUser != null){
			name = mUser.getNickName() ;
		}
		if(name == null || name.isEmpty()){
			name = getPhoneNumber();
			if(name != null && name.length() == 11){
				name = name.substring(0, 3) + "****" + name.substring(7);
			}
		}
		return name;
	}

	public void setNickName(String nickName) {
		if(mUser == null){
			return ;
		}
		mUser.setNickName(nickName);
	}
	
	public String getPhoneNumber() {
		if(mUser != null){
			return mUser.getPhoneNumber() ;
		}
		return mUserName;
	}
	public String getAvatarPath() {
		return mAvatarPath;
	}
	public void setAvatarPath(String avatarPath) {
		this.mAvatarPath = avatarPath;
	}
	public boolean isLogined() {
		return (mUser != null);
	}
	public boolean isSetPayPassword() {
		if(mUser == null){
			return false;
		}
		return mUser.isSetPayPassword();
	}
	public User getUser() {
		return mUser;
	}
	public void setUser(User user) {
		this.mUser = user;
	}
	public BDLocation getLocation() {
		return mLocation;
	}

	public void setLocation(BDLocation location) {
		this.mLocation = location;
	}
	public LatLng getMapTargetPos() {
		return mMapTargetPos;
	}
	public void setMapTargetPos(LatLng mapTargetPos) {
		this.mMapTargetPos = mapTargetPos;
	}

	public LatLng getPostion()
	{
		if(mLocation != null){
			return new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
		}
		return mMapTargetPos ;
	}

	public double getDistance(LatLng toPos)
	{
		LatLng p1;
		BDLocation location = UserManager.instance().getLocation();
		if(location != null){
			p1 = new LatLng(location.getLatitude(), location.getLongitude());
		}
		else{
			p1 = UserManager.instance().getMapTargetPos();
		}
		double distance = DistanceUtil.getDistance(p1, toPos);
		return distance ;
	}

	public boolean hadCarNumber(String number)
	{
		if(mUser == null){
			return true ;
		}
		if(mUser.getVehicles() != null)
		{
			for(Vehicle vehicle : mUser.getVehicles()){
				if(vehicle.getPlate_number().equals(number)){
					return true ;
				}
			}
		}
		return false ;
	}
	public Vehicle addNewCarNumber(String number) {
		if(mUser == null){
			return null;
		}
		Vehicle vehicle = new Vehicle();
		vehicle.setPlate_number(number);
		mUser.addVehicle(vehicle);
		return vehicle ;
	}
	public void removeCar(Vehicle vehicle) {
		if(mUser == null){
			return;
		}
		mUser.getVehicles().remove(vehicle);
	}
	public List<Vehicle> getVehicleList() {
		if(mUser == null){
			return null ;
		}
		return mUser.getVehicles() ;
	}
	public void updateUser(Context context) {
		final MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
		ServerManger.AsyncResponseHandler asyncResponseHandler = new ServerManger.AsyncResponseHandler() {
			@Override
			public void onSuccess(int statusCode, String response) {
				if(response != null){
					try{
						User user = new Gson().fromJson(response, User.class);
						logger.d("updateUser  user==="+user.toString());
						setUser(user);
					}
					catch(JsonSyntaxException e){
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(int statusCode, String errorResponse, Throwable e) {
				logger.d("updateUser   errorResponse==="+errorResponse);
			}
		};
		UserControler.getUserInfo(context, getUserName(), getPassWord(), asyncResponseHandler);
	}
}
