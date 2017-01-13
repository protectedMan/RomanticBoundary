package com.lanxin.romanticboundary.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.lanxin.romanticboundary.App;
import com.lanxin.romanticboundary.R;

import com.lanxin.romanticboundary.base.BaseFragment;
import com.lanxin.romanticboundary.fragments.GirlFragment;
import com.lanxin.romanticboundary.utils.ColorUtil;
import com.lanxin.romanticboundary.utils.FileUtil;
import com.lanxin.romanticboundary.utils.SystemUtils;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by oracleen on 2016/7/4.
 */
public class GirlActivity extends AppCompatActivity implements GirlFragment.OnGirlChange, ISimpleDialogListener {


    //获取Intent
    protected void handleIntent(Intent intent) {

    }

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.LL_rootview)
    LinearLayout LL_rootview;
    Tencent mTencent;
    GirlFragment mGirlFragment;
    ArrayList<Parcelable> girls;
    int current;

    protected int getContentViewId() {
        return R.layout.activity_girl;
    }


    protected int getFragmentContentId() {
        return R.id.girl_fragment;
    }


//    protected BaseFragment getFirstFragment() {
//        girls = getIntent().getParcelableArrayListExtra("girls");
//        current = getIntent().getIntExtra("current", 0);
//        mGirlFragment =
//        return mGirlFragment;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemUtils.RequestPermissions(this, "android.permission.WRITE_EXTERNAL_STORAGE", 1);
        mTencent=App.getmTencent();
//        RxPermissions.getInstance(this)
//                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe(granted -> {
//                    if (granted) { // Always true pre-M
//                        // I can control the camera now
//                        startActivity(new Intent(this, CaptureActivity.class));
//                    } else {
//                        // Oups permission denied
//                        Toast.makeText(this, "相机打开失败", Toast.LENGTH_LONG).show();
//                    }
//                });
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            girls = savedInstanceState.getParcelableArrayList("girls");
            current = savedInstanceState.getInt("current");
        } else {
            girls = getIntent().getParcelableArrayListExtra("girls");
            current = getIntent().getIntExtra("current", 0);
        }

        setContentView(getContentViewId());

        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        //避免重复添加Fragment
        if (null == getSupportFragmentManager().getFragments()) {
            mGirlFragment = GirlFragment.newInstance(girls, current);
            if (null != mGirlFragment) {
                addFragment(mGirlFragment);
            }
        }
        ButterKnife.bind(this);
        initView();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelableArrayList("girls", girls);
        outState.putInt("current", current);
        Log.v("onSaveInstanceState", "onSaveInstanceState");
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);

    }

    private void initView() {
        mToolbar.setTitle(R.string.meizhi);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_girl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            new SaveImgAsyncTask(2).execute();
            //Log.v("URL",PreUtils.getString(App.getContext(), Constants.CURRENTGRIL, "http://img3.duitang.com/uploads/item/201606/24/20160624124307_QwfVs.jpeg"));

            //   mGirlFragment.shareGirl();
            return true;
        } else if (id == R.id.action_save) {
            new SaveImgAsyncTask(1).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void change(int color) {
        mToolbar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(ColorUtil.colorBurn(color));
            window.setNavigationBarColor(ColorUtil.colorBurn(color));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishActivity();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    //添加fragment
    protected void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    //移除fragment
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {
    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        startActivity(FileUtil.getAppDetailSettingIntent(this));
    }

    class SaveImgAsyncTask extends AsyncTask<String, String, String> {
        int type;

        public SaveImgAsyncTask() {
        }

        public SaveImgAsyncTask(int type) {
            this.type = type;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... params) {
            return mGirlFragment.saveGirl(GirlActivity.this);
        }

        @Override
        protected void onPostExecute(final String s) {
            String text = "";
            if (type == 1) {
                text = "保存";
            } else {
                text = "分享";
            }
            if (s.equals("-1")) {

                SimpleDialogFragment.createBuilder(App.getContext(), getSupportFragmentManager())
                        .setTitle("权限不足")
                        .setRequestCode(1)
                        .setMessage(Html.fromHtml(text + "图片需要存储设备权限呢~"))
                        .setPositiveButtonText("去设置")
                        .show();
            } else if (s.equals("0")) {
                Snackbar.make(mGirlFragment.mRootView, text + "出错了哦~", Snackbar.LENGTH_LONG).show();
            } else {

                if (type == 1) {
                    Snackbar sb = Snackbar.make(mGirlFragment.mRootView, text + "到图库了呢~", Snackbar.LENGTH_LONG);

                    sb.setAction("打开", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //使用Intent
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse("file://" + s), "image/*");
                            startActivity(intent);
                        }
                    });
                    sb.show();
                } else {

                    SystemUtils.shareImageForMobileQQ(mTencent,
                            s
                            //   PreUtils.getString(App.getContext(), Constants.CURRENTGRIL, "http://img3.duitang.com/uploads/item/201606/24/20160624124307_QwfVs.jpeg")
                            , GirlActivity.this, LL_rootview);
                }

            }
            super.onPostExecute(s);
        }
    }
}
