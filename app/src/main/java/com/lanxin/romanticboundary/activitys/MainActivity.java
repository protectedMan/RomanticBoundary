package com.lanxin.romanticboundary.activitys;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.bumptech.glide.Glide;
import com.igexin.sdk.PushManager;
import com.lanxin.romanticboundary.App;
import com.lanxin.romanticboundary.R;
import com.lanxin.romanticboundary.base.BaseActivity;
import com.lanxin.romanticboundary.event.SkinChangeEvent;
import com.lanxin.romanticboundary.fragments.GirlsFragment;
import com.lanxin.romanticboundary.theme.ColorUiUtil;
import com.lanxin.romanticboundary.theme.Theme;
import com.lanxin.romanticboundary.update.Constants;
import com.lanxin.romanticboundary.update.DownloadService;
import com.lanxin.romanticboundary.update.UpdateChecker;
import com.lanxin.romanticboundary.utils.BaseUiListener;
import com.lanxin.romanticboundary.utils.FileUtil;
import com.lanxin.romanticboundary.utils.PreUtils;
import com.lanxin.romanticboundary.utils.SystemUtils;
import com.lanxin.romanticboundary.utils.ThemeUtils;
import com.lanxin.romanticboundary.widget.ResideLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.foundation_icons_typeface_library.FoundationIcons;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.tencent.connect.UserInfo;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.xiaopan.android.content.res.DimenUtils;
import me.xiaopan.android.preference.PreferencesUtils;

public class MainActivity extends BaseActivity implements ColorChooserDialog.ColorCallback, ISimpleDialogListener {

    @Bind(R.id.menu)
    RelativeLayout mMenu;
    @Bind(R.id.resideLayout)
    ResideLayout mResideLayout;
    @Bind(R.id.status_bar)
    View mStatusBar;
    @Bind(R.id.all)
    TextView mAll;
    @Bind(R.id.fuli)
    TextView mFuli;
    @Bind(R.id.android)
    TextView mAndroid;
    @Bind(R.id.ios)
    TextView mIos;
    @Bind(R.id.video)
    TextView mVideo;
    @Bind(R.id.front)
    TextView mFront;
    @Bind(R.id.resource)
    TextView mResource;
    @Bind(R.id.about)
    TextView mAbout;
    @Bind(R.id.container)
    FrameLayout mContainer;
    @Bind(R.id.app)
    TextView mApp;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    @Bind(R.id.theme)
    TextView mTheme;
    @Bind(R.id.avatar)
    ImageView mAvatar;
    @Bind(R.id.desc)
    TextView mDesc;
    @Bind(R.id.icon)
    ImageView mIcon;
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.more)
    TextView mMore;
    String access_code;
    private Fragment currentFragment;
    private long exitTime = 0;
    Tencent mTencent;
    private boolean isServerSideLogin;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTencent = App.getmTencent();
        setContentView(R.layout.activity_main);

        PushManager.getInstance().initialize(this.getApplicationContext());
        String Clientid= PushManager.getInstance().getClientid(this);
              UpdateChecker.checkForDialog(this, this.getSupportFragmentManager());//检测更新

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            mStatusBar.setVisibility(View.GONE);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mStatusBar.setVisibility(View.VISIBLE);
//            mStatusBar.getLayoutParams().height = SystemUtils.getStatusHeight(this);
//            mStatusBar.setLayoutParams(mStatusBar.getLayoutParams());
//        } else {
//            mStatusBar.setVisibility(View.GONE);
//        }

        setIconDrawable(mAll, MaterialDesignIconic.Icon.gmi_view_comfy);
        setIconDrawable(mFuli, MaterialDesignIconic.Icon.gmi_mood);
        setIconDrawable(mAndroid, MaterialDesignIconic.Icon.gmi_android);
        setIconDrawable(mIos, MaterialDesignIconic.Icon.gmi_apple);
        setIconDrawable(mVideo, MaterialDesignIconic.Icon.gmi_collection_video);
        setIconDrawable(mFront, MaterialDesignIconic.Icon.gmi_language_javascript);
        setIconDrawable(mResource, FontAwesome.Icon.faw_location_arrow);
        setIconDrawable(mApp, MaterialDesignIconic.Icon.gmi_apps);
        setIconDrawable(mAbout, MaterialDesignIconic.Icon.gmi_account);
//        setIconDrawable(mSetting, MaterialDesignIconic.Icon.gmi_settings);
        setIconDrawable(mTheme, MaterialDesignIconic.Icon.gmi_palette);
        setIconDrawable(mMore, MaterialDesignIconic.Icon.gmi_more);

        Glide.with(MainActivity.this)
                .load(R.drawable.logomain)
                .placeholder(new IconicsDrawable(this)
                        .icon(FoundationIcons.Icon.fou_photo)
                        .color(Color.GRAY)
                        .backgroundColor(Color.WHITE)
                        .roundedCornersDp(40)
                        .paddingDp(15)
                        .sizeDp(75))
                .bitmapTransform(new CropCircleTransformation(this))
                .dontAnimate()
                .into(mAvatar);

        mDesc.setText("浪漫边界");
//

        if (PreferencesUtils.getBoolean(this, "isFirst", true)) {
            mResideLayout.openPane();
            PreferencesUtils.putBoolean(this, "isFirst", false);
        }
        mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_view_comfy).sizeDp(20));
        mTitle.setText("Girls");
        switchFragment(new GirlsFragment());

    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    private void setIconDrawable(TextView view, IIcon icon) {
        view.setCompoundDrawablesWithIntrinsicBounds(new IconicsDrawable(this)
                        .icon(icon)
                        .color(Color.WHITE)
                        .sizeDp(16),
                null, null, null);
        view.setCompoundDrawablePadding(DimenUtils.dp2px(this, 10));
    }

    private void switchFragment(Fragment fragment) {
        if (currentFragment == null || !fragment.getClass().getName().equals(currentFragment.getClass().getName())) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            currentFragment = fragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (mResideLayout.isOpen()) {
            mResideLayout.closePane();
        } else {
            super.onBackPressed();
        }
    }

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);

            System.out.println("token" + token + "-------------" + openId);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_API) {
            if (resultCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, loginlistener);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
        Tencent.onActivityResultData(requestCode, resultCode, data, loginlistener);
        // mTencent.handleLoginData(data, loginlistener);
        //  Tencent.onActivityResultData(requestCode, resultCode, data, loginlistener);
    }

    IUiListener loginlistener = new BaseUiListener() {
        @Override
        public void onComplete(Object response) {
            initOpenidAndToken((JSONObject) response);
        }
    };


    private void onClickServerSideLogin() {
        if (!mTencent.isSessionValid()) {

            mTencent.loginServerSide(this, "all", loginlistener);
            isServerSideLogin = true;
            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            if (!isServerSideLogin) { // SSO模式的登陆，先退出，再进行Server-Side模式登陆
                mTencent.logout(this);
                mTencent.loginServerSide(this, "all", loginlistener);
                isServerSideLogin = true;
                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            mTencent.logout(this);
            isServerSideLogin = false;
//            updateUserInfo();
//            updateLoginButton();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @OnClick({R.id.avatar, R.id.all, R.id.fuli, R.id.android,
            R.id.ios, R.id.video, R.id.front,
            R.id.resource, R.id.about,
            R.id.app, R.id.theme, R.id.icon, R.id.more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:

                onClickServerSideLogin();

                //SystemUtils.joinQQGroup(Constants.TENCENT_JOIN_QQ_GROUP, MainActivity.this);
                break;
//
            case R.id.fuli:
                if (mTencent.isSessionValid()) {

                    new Thread() {
                        @Override
                        public void run() {
                            JSONObject json = null;
                            try {
                                json = mTencent.request(com.tencent.connect.common.Constants.GRAPH_NICK_TIPS, null, com.tencent.connect.common.Constants.HTTP_GET);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (HttpUtils.NetworkUnavailableException e) {
                                e.printStackTrace();
                            } catch (HttpUtils.HttpStatusException e) {
                                e.printStackTrace();
                            }

                            System.out.println(json);
                        }
                    }.start();

                }

                mResideLayout.closePane();
                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_mood).sizeDp(20));
                mTitle.setText(R.string.fuli);
                switchFragment(new GirlsFragment());
                break;
//
            case R.id.about:
                new MaterialDialog.Builder(this)
                        .title(R.string.about)
                        .icon(new IconicsDrawable(this)
                                .color(ThemeUtils.getThemeColor(this, R.attr.colorPrimary))
                                .icon(MaterialDesignIconic.Icon.gmi_account)
                                .sizeDp(20))
                        .content("浪漫边界 © 2016  版本号：" + App.getVersion())
                        .positiveText(R.string.close)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                SystemUtils.joinQQGroup("UWNSRZqYCk_Zqa2dQ_nncKRoU8gWteQN", MainActivity.this);
                            }
                        })
                        .show();
                break;
            case R.id.theme:
                new ColorChooserDialog.Builder(this, R.string.theme)
                        .customColors(R.array.colors, null)
                        .doneButton(R.string.done)
                        .cancelButton(R.string.cancel)
                        .allowUserColorInput(false)
                        .allowUserColorInputAlpha(false)
                        .show();
                break;
            case R.id.icon:
                mResideLayout.openPane();
                break;
        }
    }


    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        if (selectedColor == ThemeUtils.getThemeColor(this, R.attr.colorPrimary))
            return;
        EventBus.getDefault().post(new SkinChangeEvent());

        if (selectedColor == getResources().getColor(R.color.colorBluePrimary)) {
            setTheme(R.style.BlueTheme);
            PreUtils.setCurrentTheme(this, Theme.Blue);

        } else if (selectedColor == getResources().getColor(R.color.colorRedPrimary)) {
            setTheme(R.style.RedTheme);
            PreUtils.setCurrentTheme(this, Theme.Red);

        } else if (selectedColor == getResources().getColor(R.color.colorBrownPrimary)) {
            setTheme(R.style.BrownTheme);
            PreUtils.setCurrentTheme(this, Theme.Brown);

        } else if (selectedColor == getResources().getColor(R.color.colorGreenPrimary)) {
            setTheme(R.style.GreenTheme);
            PreUtils.setCurrentTheme(this, Theme.Green);

        } else if (selectedColor == getResources().getColor(R.color.colorPurplePrimary)) {
            setTheme(R.style.PurpleTheme);
            PreUtils.setCurrentTheme(this, Theme.Purple);

        } else if (selectedColor == getResources().getColor(R.color.colorTealPrimary)) {
            setTheme(R.style.TealTheme);
            PreUtils.setCurrentTheme(this, Theme.Teal);

        } else if (selectedColor == getResources().getColor(R.color.colorPinkPrimary)) {
            setTheme(R.style.PinkTheme);
            PreUtils.setCurrentTheme(this, Theme.Pink);

        } else if (selectedColor == getResources().getColor(R.color.colorDeepPurplePrimary)) {
            setTheme(R.style.DeepPurpleTheme);
            PreUtils.setCurrentTheme(this, Theme.DeepPurple);

        } else if (selectedColor == getResources().getColor(R.color.colorOrangePrimary)) {
            setTheme(R.style.OrangeTheme);
            PreUtils.setCurrentTheme(this, Theme.Orange);

        } else if (selectedColor == getResources().getColor(R.color.colorIndigoPrimary)) {
            setTheme(R.style.IndigoTheme);
            PreUtils.setCurrentTheme(this, Theme.Indigo);

        } else if (selectedColor == getResources().getColor(R.color.colorLightGreenPrimary)) {
            setTheme(R.style.LightGreenTheme);
            PreUtils.setCurrentTheme(this, Theme.LightGreen);

        } else if (selectedColor == getResources().getColor(R.color.colorDeepOrangePrimary)) {
            setTheme(R.style.DeepOrangeTheme);
            PreUtils.setCurrentTheme(this, Theme.DeepOrange);

        } else if (selectedColor == getResources().getColor(R.color.colorLimePrimary)) {
            setTheme(R.style.LimeTheme);
            PreUtils.setCurrentTheme(this, Theme.Lime);

        } else if (selectedColor == getResources().getColor(R.color.colorBlueGreyPrimary)) {
            setTheme(R.style.BlueGreyTheme);
            PreUtils.setCurrentTheme(this, Theme.BlueGrey);

        } else if (selectedColor == getResources().getColor(R.color.colorCyanPrimary)) {
            setTheme(R.style.CyanTheme);
            PreUtils.setCurrentTheme(this, Theme.Cyan);

        }
        final View rootView = getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);

        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        if (null != localBitmap && rootView instanceof ViewGroup) {
            final View tmpView = new View(getApplicationContext());
            tmpView.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) rootView).addView(tmpView, params);
            tmpView.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    ColorUiUtil.changeTheme(rootView, getTheme());
                    System.gc();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) rootView).removeView(tmpView);
                    localBitmap.recycle();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        switch (requestCode) {
            case 0:

                break;
            case 1:
                break;
            default:
                break;
        }
        Log.v("onNegativeButtonClicked", "onNegativeButtonClicked");


    }


    @Override
    public void onNeutralButtonClicked(int requestCode) {
        switch (requestCode) {
            case 0:

                break;
            case 1:
                break;
            default:
                break;
        }
        Log.v("onNeutralButtonClicked", "onNeutralButtonClicked");

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        Log.v("onPositiveButtonClicked", "onPositiveButtonClicked");
        Intent intent;
        switch (requestCode) {
            case 0:
                intent = new Intent(this, DownloadService.class);
                intent.setAction("ACTION_DOWNLOAD_APK");
                intent.putExtra(Constants.APK_DOWNLOAD_URL, PreUtils.getString(this, Constants.UPDATE_URL, ""));
                this.startService(intent);
                break;
            case 1:
//                 intent = new Intent();
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                intent.setData(Uri.fromParts("com.lanxin.romanticboundary", getPackageName(), null));

                startActivity(FileUtil.getAppDetailSettingIntent(this));

                break;
            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Snackbar.make(mResideLayout, "再按一次退出程序哦~", Snackbar.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                App.AppExit(getApplicationContext());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
