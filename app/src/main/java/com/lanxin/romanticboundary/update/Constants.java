package com.lanxin.romanticboundary.update;

import com.lanxin.romanticboundary.utils.FileUtil;
import com.lanxin.romanticboundary.App;

public class Constants {


    // json {"url":"http://192.168.205.33:8080/Hello/app_v3.0.1_Other_20150116.apk","versionCode":2,"updateMessage":"版本更新信息"}

    public static final String APK_DOWNLOAD_URL = "url";
    static final String APK_CHANGE_LOG = "changelog";
    static final String APK_VER_NAME = "versionShort";
    static final String APK_VER_CODE = "build";
    static final String APK_INSTALL_URL = "installUrl";

    static final String FIR_API_TOKEN = "490013915810eff7fdf2c6df0417a794";
    public static final String TENCENT_APP_ID="1105610740";
    public static final String TENCENT_JOIN_QQ_GROUP="UWNSRZqYCk_Zqa2dQ_nncKRoU8gWteQN";
    static final String UPDATE_JSON="UPDATE_JSON";
    public static String UPDATE_URL="UPDATE_URL";
    public static final String GANHUO_API = "http://gank.io/";
    static final int TYPE_NOTIFICATION = 2;

    static final int TYPE_DIALOG = 1;
    public  static final String CURRENTGRIL="CURRENTGRIL";
    static final String TAG = "UpdateChecker";
    public static final String THEMECOLOR = "THEMECOLOR";
    public static final String dir = FileUtil.getDiskCacheDir(App.getIntstance()) + "/浪漫边界";
   // static final String UPDATE_URL = "https://raw.githubusercontent.com/feicien/android-auto-update/develop/extras/update.json";
}
