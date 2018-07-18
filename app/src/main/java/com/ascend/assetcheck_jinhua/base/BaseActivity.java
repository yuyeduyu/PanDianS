package com.ascend.assetcheck_jinhua.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import com.ascend.assetcheck_jinhua.MyApplication;
import com.ascend.assetcheck_jinhua.ui.activity.LoginActivity;
import com.ascend.assetcheck_jinhua.ui.dialog.LoadingDialog;
import java.util.List;
import java.util.regex.Pattern;
import butterknife.ButterKnife;

/**
 * Activity基类
 */
//public class BaseActivity extends CheckPermissionsActivity {
public class BaseActivity extends FragmentActivity {
    public BaseActivity mBaseActivity;
    public MyApplication myApplication;
    private Toast mToast;
    //    public LoadingDialog mLoadingDialog;
    public static boolean isForeground = false;

    public LoadingDialog loadingDialog;
    //    private LogWriter mLogWriter;

    private long lastClickTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        initSwipeBackFinish();
        super.onCreate( savedInstanceState );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        //        File logf = new File(Environment.getExternalStorageDirectory()
        //                + File.separator + "DemoLog.txt");
        //
        //        try {
        //            mLogWriter = LogWriter.open(logf.getAbsolutePath());
        //        } catch (IOException e) {
        //            // TODO Auto-generated catch block
        //            Log.d(tag, e.getMessage());
        //        }
         mBaseActivity = this;

        if (null!= savedInstanceState){
            Log.i( "TAG", "onCreate savedInstanceState:   " + (null!= savedInstanceState) );
            Intent intent = new Intent( mBaseActivity, LoginActivity.class );
            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity( intent );
            MyApplication.getInstance().finish();
        }
        requestWindowFeature( Window.FEATURE_NO_TITLE );

        myApplication = MyApplication.getInstance();

        getIntentData();
        findViews( savedInstanceState );

        initButterKnife();
        //修改状态栏
//        initStateBar();
        loadingDialog = new LoadingDialog( this );
        loadingDialog.setCanceledOnTouchOutside( false );
        initViews();
        addListeners();
        requestOnCreate();
        myApplication.addCurrentActivity( mBaseActivity );
    }

    protected void initButterKnife() {
        ButterKnife.bind(this, mBaseActivity);
    }



//    /**
//     * 初始化沉浸式
//     */
//    private void initStateBar() {
//        setColorId();
//        //        if (isNeedLoadStatusBar()) {
//        //            loadStateBar();
//        //        }
//        loadStateBar();
//    }
//
//    private void loadStateBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//        tintManager = new SystemBarTintManager(this);
//        // 激活状态栏设置
//        tintManager.setStatusBarTintEnabled(true);
//        // 激活导航栏设置
//        tintManager.setNavigationBarTintEnabled(true);
//        // 设置一个状态栏颜色
//        tintManager.setStatusBarTintResource(getColorId());
//    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes( winParams );
    }

//    /**
//     * 如果子类使用非默认的StatusBar,就重写此方法,传入布局的id
//     */
//    protected void setColorId() {
//        this.mColorId = Color.TRANSPARENT;//子类重写方式
//    }
//
//    protected int getColorId() {
//        return mColorId;
//    }
//
//    /**
//     * 子类是否需要实现沉浸式,默认需要
//     *
//     * @return
//     */
//    protected boolean isNeedLoadStatusBar() {
//        return true;
//    }


    //*************************************// TODO: 2017/3/9 0009 状态栏修改代码分割线

    @Override
    protected void onStart() {
        super.onStart();
        //        mLoadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeCurrentActivity( mBaseActivity );
        isForeground = false;
        if (loadingDialog != null) {
            if (loadingDialog.isShowing())
                loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        String registrationID = JPushInterface.getRegistrationID(MyApplication.getInstance());
//        Log.i("jpush_c", registrationID);

    }


    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * 获取界面传递数据
     */
    protected void getIntentData() {

    }

    /**
     * 初始化布局中的空间，首先要调用setContentView
     */
    protected void findViews(Bundle savedInstanceState) {
        //        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(true);

    }

    /**
     * 初始化本地数据
     */
    protected void initViews() {
    }

    /**
     * 添加监听器
     */
    protected void addListeners() {

    }


    /**
     * 在onCreate中请求服务
     */
    protected void requestOnCreate() {

    }

    /**
     * 点击外面取消输入法如果外层包裹了scrollview则事件会被处理，不会传出到activity中，也就无效了
     */
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (this.getCurrentFocus() != null) {
                if (this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow( this.getCurrentFocus().getWindowToken(), InputMethodManager
                            .HIDE_NOT_ALWAYS );
                }
            }
        }
        return super.onTouchEvent( event );
    }

    public void goActivity(Class<?> cls) {
        Intent intent = new Intent( mBaseActivity, cls );
        intent.setFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP );
        startActivity( intent );
    }

    /**
     * 设置TextView
     */
    public static void setTextView(TextView tv, String str) {
        if (!TextUtils.isEmpty( str ))
            tv.setText( str );
        else
            tv.setText( str );

    }

    /**
     * 判断list是否为空
     *
     * @param list
     * @return true：list为空；false：list不为空
     */
    public static <T> boolean listNull(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }
    /**
     * make true current connect service is wifi
     *
     * @param mContext
     * @return 判断是不是在WIFI条件下
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断gps是否可用
     *
     * @return
     */
    public boolean isGpsAvailable() {
        LocationManager locationManager = (LocationManager) mBaseActivity.
                getSystemService( Context.LOCATION_SERVICE );
        if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
            return true;
        else {
            return false;
        }
    }


    /**
     * 判断微信是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages( 0 );// 获取所有已安装程序的包信息
        if (pinfo != null) {
            int pinfoSize = pinfo.size();
            for (int i = 0; i < pinfoSize; i++) {
                String pn = pinfo.get( i ).packageName;
                if (pn.equals( "com.tencent.mm" )) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isFixedPhone(String fixedPhone) {
        String reg = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
        return Pattern.matches( reg, fixedPhone );
    }

    public void showDialog(boolean show) {
        if (loadingDialog != null) {
            if (show && !loadingDialog.isShowing())
                loadingDialog.show();
            else if (!show && loadingDialog.isShowing())
                loadingDialog.dismiss();
        }
    }

    /**
     * 关闭键盘
     *
     * @param activity
     */
    public static void closeKeyboard(Activity activity) {
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService( Context
                        .INPUT_METHOD_SERVICE );
                inputMethodManager.hideSoftInputFromWindow( activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS );
            }
        }
    }
}