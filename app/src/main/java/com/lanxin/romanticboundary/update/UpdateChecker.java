package com.lanxin.romanticboundary.update;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;


import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

public class UpdateChecker {


    public static void checkForDialog(final Context context, final FragmentManager fragmentManager) {
        if (context != null) {

            //获取远程的更新信息的JSON
            FIR.checkForUpdateInFIR(Constants.FIR_API_TOKEN, new VersionCheckCallback() {
                @Override
                public void onSuccess(String versionJson) {
                    Log.i("fir", "check from fir.im success! " + "\n" + versionJson);
                    parseJson(versionJson, context, fragmentManager);
                }

                @Override
                public void onFail(Exception exception) {
                    Log.i("fir", "check fir.im fail! " + "\n" + exception.getMessage());
                }

                @Override
                public void onStart() {
              //      Toast.makeText(context, "正在获取", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
               //     Toast.makeText(context, "获取完成", Toast.LENGTH_SHORT).show();
                }
            });
            //   new CheckUpdateTask(context, fragmentManager, Constants.TYPE_DIALOG, true).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }


    private static void parseJson(String result, Context context, FragmentManager fragmentManager) {//提取字符串的关键信息
        try {

            JSONObject obj = new JSONObject(result);
            String updateMessage = obj.getString(Constants.APK_CHANGE_LOG);//更新日志
            String apkUrl = obj.getString(Constants.APK_INSTALL_URL);//下载路径
            int apkCode = obj.getInt(Constants.APK_VER_CODE);//服务器版本代码

            int versionCode = AppUtils.getVersionCode(context);//当前app版本代码

            if (apkCode > versionCode) {
                UpdateDialog.show(context, fragmentManager, updateMessage, apkUrl);//弹出更新提示!!
            }

        } catch (JSONException e) {
            Log.e(Constants.TAG, "parse json error");
        }
    }

    public static void checkForNotification(Context context, FragmentManager fragmentManager) {
        if (context != null) {
            new CheckUpdateTask(context, fragmentManager, Constants.TYPE_NOTIFICATION, false).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }

    }


}
