package com.zhichen.parking.fragment.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.common.AppConstants;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.lib.messageservice.MessageService;
import com.zhichen.parking.library.dialogplus.DialogPlus;
import com.zhichen.parking.library.dialogplus.ViewHolder;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.tools.DialogHelper.OnModifyCallback;
import com.zhichen.parking.tools.MyLogger;
import com.zhichen.parking.util.BitmapUtil;
import com.zhichen.parking.util.FileUtil;
import com.zhichen.parking.widget.CircularImage;

import java.io.File;
import java.io.IOException;

/**
 * Created by xuemei on 2016-06-04.
 */
public class UserInfoFragment extends BaseFragment implements View.OnClickListener {
    View mViewRoot;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private static final int CROP_WIDTH = 360; // 截图保存图片的宽度
    private static final int CROP_HEIGHT = 360; // 截图保存图片的高度

    private static final String CAMERA_PHOTO_PATH = AppConstants.TEMP_DIR + "/camera.jpg";
    private static final String CROP_IMG_PATH = AppConstants.TEMP_DIR + "/crop.jpg";

    private CircularImage mAvatarIv;
    private RelativeLayout headLayout, nickNameLayout, phonelayout;
    private TextView nickNameTV, phoneTV;

    private MyLogger logger=MyLogger.getLogger(Constants.USERNAME);

    public UserInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_userinfo, container, false);
        initUI();
        return mViewRoot;
    }

    private void initUI() {
        headLayout = (RelativeLayout) mViewRoot.findViewById(R.id.person_head_layout);
        nickNameLayout = (RelativeLayout) mViewRoot.findViewById(R.id.person_nickname_layout);
        phonelayout = (RelativeLayout) mViewRoot.findViewById(R.id.person_phone_layout);

        mAvatarIv = (CircularImage) mViewRoot.findViewById(R.id.user_photo);
        updateAvatar(UserManager.instance().getAvatarPath());

        nickNameTV = (TextView) mViewRoot.findViewById(R.id.nick_name);
        phoneTV = (TextView) mViewRoot.findViewById(R.id.user_phone);
        phoneTV.setText(UserManager.instance().getPhoneNumber());
        String nickName = UserManager.instance().getNickName();
        if (nickName != null) {
            nickNameTV.setText(nickName);
        }

        headLayout.setOnClickListener(this);
        nickNameLayout.setOnClickListener(this);
        phonelayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_head_layout:
                showActionView();
                break;
            case R.id.person_nickname_layout:
                modifyNickName();
                break;
            case R.id.person_phone_layout:

                break;
        }
    }
    /**
     * 修改昵称
     */
    private void modifyNickName() {
        final OnModifyCallback callback = new OnModifyCallback() {
            @Override
            public void onResult(String result) {
                nickNameTV.setText(result);
                UserManager.instance().setNickName(result);
                MessageService.instance().notifyMessage(AppConstants.TYPE_NICKNAME_CHANGE, null);
            }
        };
        DialogHelper.showModifyDialog(getContext(), "昵称修改", nickNameTV.getText().toString(), "取消", "确定", callback);
    }
    /**
     * 更换头像
     */
    private void showActionView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_capture, null);
        ViewHolder holder = new ViewHolder(contentView);
        final DialogPlus dialog = DialogPlus.newDialog(getContext())
                .setContentHolder(holder)
                .setGravity(Gravity.BOTTOM)
                .setExpanded(false)
                .setCancelable(true)
                .setBackgroundColorResourceId(R.color.transparent)
                .create();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                switch(v.getId())
                {
                    case R.id.capture_camera_btn:
                        camera();
                        break;
                    case R.id.capture_gallery_btn:
                        gallery();
                        break;
                }
                dialog.dismiss();
            }
        };
        View camera = contentView.findViewById(R.id.capture_camera_btn);
        camera.setOnClickListener(listener);
        View gallery = contentView.findViewById(R.id.capture_gallery_btn);
        gallery.setOnClickListener(listener);
        View cancel = contentView.findViewById(R.id.capture_cancel_btn);
        cancel.setOnClickListener(listener);

        dialog.show();
    }


    /**
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片/* * 从相册获取 */
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 从相机获取
     */
    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(CAMERA_PHOTO_PATH)));
        }
        getActivity().startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 裁剪图片
     * @param uri
     * @param file
     */
    private void cropimg(Uri uri, File file) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", CROP_WIDTH);
        intent.putExtra("outputY", CROP_HEIGHT);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        // intent.putExtra("scale", true);// 去黑边
        // intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        intent.putExtra("output", Uri.fromFile(file)); // 保存路径
        intent.putExtra("noFaceDetection", true); // no face detection
        // intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        getActivity().startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    /**
     * 回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logger.e("requestCode==="+requestCode);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                File file = new File(CROP_IMG_PATH);
                cropimg(uri, file);
            }
        } else if (requestCode == PHOTO_REQUEST_CAMERA && resultCode == getActivity().RESULT_OK) {
            File tempPath = new File(CAMERA_PHOTO_PATH);
            File imgFile = new File(CROP_IMG_PATH);
            try {
                imgFile.createNewFile();
                cropimg(Uri.fromFile(tempPath), imgFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (resultCode == Activity.RESULT_OK) {
                final ProgressDialog pd = DialogHelper.showProgressDialog(getContext(), "上传中，请稍等", false);
                 AsyncResponseHandler asyncResponseHandler = new AsyncResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        logger.d("uploadFile onSuccess==="+"statusCode==="+statusCode+"response==="+response);
                        FileUtil.copyFile(CROP_IMG_PATH, UserManager.instance().getAvatarPath());
                        MessageService.instance().notifyMessage(AppConstants.TYPE_AVATAR_CHANGE, null);
                        updateAvatar(CAMERA_PHOTO_PATH);
                        pd.dismiss();
                    }
                    @Override
                    public void onFailure(int statusCode, String errorResponse, Throwable e) {
                        logger.e("uploadFile onFailure==="+"statusCode==="+statusCode+"response==="+errorResponse);
                        Toast.makeText(getContext(), "上传失败，请重试", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                };
                UserControler.uploadFile(getContext(), CROP_IMG_PATH, asyncResponseHandler);
            }
        }

    }
    private void updateAvatar(String path) {
        Bitmap avatar = BitmapUtil.decodeImage(path, 100);
        if (avatar != null) {
            mAvatarIv.setImageBitmap(avatar);
        }
    }
}
