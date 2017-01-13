package com.lanxin.romanticboundary.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;


import com.lanxin.romanticboundary.R;
import com.lanxin.romanticboundary.update.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * Created by dongjunkun on 2015/11/24.
 */
public class SystemUtils {

    /**
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


    public static void RequestPermissions(Context context, String permission, int requestCode) {
        // 判断当前版本是否大于等于21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 判断当前Activity是否获得了该权限
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d("zzzzb", "没有权限");
                // 没有授权,判断权限申请是否曾经被拒绝过
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    Log.d("zzzzb", "你曾经拒绝过此权限,需要重新获取");

                    //  Toast.makeText(context, "你曾经拒绝过此权限,需要重新获取", Toast.LENGTH_SHORT).show();
                    // 进行权限请求
                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
                } else {
                    // 进行权限请求
                    Log.d("zzzzb", "弹出权限框申请权限");
                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
                }
            }
        }
    }

    public static void shareImageForMobileQQ(Tencent tencent, String localURL, Activity activity, View V) {
        Log.v("url",localURL);
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "浪漫边界");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, localURL);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);

        tencent.shareToQQ(activity, params, new BaseUiListener(V));
    }

    /****************
     * 发起添加群流程。群号：浪漫边界审核群(556015570) 的 key 为： UWNSRZqYCk_Zqa2dQ_nncKRoU8gWteQN
     * 调用 joinQQGroup(UWNSRZqYCk_Zqa2dQ_nncKRoU8gWteQN) 即可发起手Q客户端申请加群 浪漫边界审核群(556015570)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(String key, Context context) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            ClipData myClip;
            String QQGroupNo = "556015570";
            myClip = ClipData.newPlainText("QQGroupNo", QQGroupNo);

            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static class BaseApiListener implements IRequestListener {

        @Override
        public void onComplete(JSONObject jsonObject) {

        }

        @Override
        public void onIOException(IOException e) {

        }

        @Override
        public void onMalformedURLException(MalformedURLException e) {

        }

        @Override
        public void onJSONException(JSONException e) {

        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException e) {

        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException e) {

        }

        @Override
        public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e) {

        }

        @Override
        public void onHttpStatusException(HttpUtils.HttpStatusException e) {

        }

        @Override
        public void onUnknowException(Exception e) {

        }
    }

    private static class BaseUiListener implements IUiListener {
        View mRootView;


        public BaseUiListener(View mRootView) {
            this.mRootView = mRootView;
        }

        @Override
        public void onComplete(Object o) {
            Snackbar.make(mRootView, "分享成功~", Snackbar.LENGTH_SHORT).show();
            Log.v("onComplete", "onComplete");
        }

        @Override
        public void onError(UiError e) {
            Log.v("onError:", "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
            Log.v("onCancel", "");
        }
    }
}
