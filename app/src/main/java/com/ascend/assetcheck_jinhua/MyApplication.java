package com.ascend.assetcheck_jinhua;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.ascend.assetcheck_jinhua.base.Settings;
import com.ascend.assetcheck_jinhua.utils.DensityUtils;
import com.ascend.assetcheck_jinhua.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
//import android.support.multidex.MultiDex;
//import android.support.multidex.MultiDex;


public class MyApplication extends MultiDexApplication {
    public static MyApplication mApplication;
    /**
     * 存放activity的集合
     */
    public static List<Activity> mActivityList;

    public static int time = 30;
    private static String version;
    /**
     * 程序启动时的处理
     *
     * @see Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mActivityList = new ArrayList<Activity>();
        initData();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext

        // 出现应用级异常时的处理
       /* Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            String errMsg = "";

            @Override
            public void uncaughtException(Thread thread, final Throwable throwable) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                errMsg = sw.toString();
                writeLogToFile(errMsg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        MobclickAgent.reportError(getApplicationContext(),throwable);
                        Looper.prepare();
                        if (mActivityList.size() > 0) {
                            new AlertDialog.Builder(getCurrentActivity()).setTitle(R.string
                                    .app_name).setMessage(Log.getStackTraceString(throwable))
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 强制退出程序
                                    finish();
                                }
                            }).setCancelable(false).show();

                        } else {
                            if (Settings.DEBUG) {
                                Log.e(Settings.TAG, errMsg);
                            }
                            finish();
                        }
                        Looper.loop();
                    }
                }).start();
                if (Settings.DEBUG) {
                    // 错误LOG
                    Log.e(Settings.TAG, throwable.getMessage(), throwable);
                }
            }
        });*/
    }


    /**
     * 打印错误日志到文件
     *
     * @param errMsg
     */
    private void writeLogToFile(String errMsg) {
        File file = new File(Settings.TEMP_PATH, "log.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write("\n" + errMsg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 本身实例
     */
    public static MyApplication getInstance() {
        return mApplication;
    }

    /**
     * 初始化信息
     */
    private void initData() {
        // 获得屏幕高度（像素）
        Settings.DISPLAY_HEIGHT = getResources().getDisplayMetrics().heightPixels;
        // 获得屏幕宽度（像素）
        Settings.DISPLAY_WIDTH = getResources().getDisplayMetrics().widthPixels;
        // 获得系统状态栏高度（像素）
        Settings.STATUS_BAR_HEIGHT = DensityUtils.getStatusBarHeight(mApplication);
        // 文件路径设置
        String parentPath = null;
        // SD卡正常挂载（可读写）
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            parentPath = Environment.getExternalStorageDirectory().getPath() + File.separator +
                    getPackageName();
        } else {
            parentPath = Environment.getDataDirectory().getPath() + "/data/" + getPackageName();
        }
        // 临时文件路径设置
        Settings.TEMP_PATH = parentPath + "/tmp";
        // 录音文件路径设置
        Settings.VIDEO_PATH = parentPath + "/video";
        // 图片缓存路径设置
        Settings.PIC_PATH = parentPath + "/pic";
        // 更新APK路径设置
        Settings.APK_PATH = parentPath + "/apk";
        // 创建各目录
        new File(Settings.TEMP_PATH).mkdirs();
        new File(Settings.VIDEO_PATH).mkdirs();
        new File(Settings.PIC_PATH).mkdirs();
        new File(Settings.APK_PATH).mkdirs();
    }
    /**
     * 清空Activity列表
     */
    public static void clearActivityList() {
        int size = mActivityList.size();
        for (int i = 0; i < size; i++) {
            Activity activity = mActivityList.get(i);
            activity.finish();
        }
        mActivityList.clear();
    }

    public static void clearActivityListOnlyOne() {
        int size = mActivityList.size() - 1;
        for (int i = 0; i < size; i++) {
            Activity activity = mActivityList.get(i);
            activity.finish();
        }
    }

    /**
     * 获得当前最顶层的activity
     *
     * @return 当前最顶层的activity
     */
    public Activity getCurrentActivity() {
        if (mActivityList != null && mActivityList.size() >= 1) {
            return mActivityList.get(mActivityList.size() - 1);
        }
        return null;
    }

    public Activity getBelowCurrentActivity() {
        if (mActivityList.size() > 1) {
            return mActivityList.get(mActivityList.size() - 2);
        }
        return null;
    }

    /**
     * 生成Activity存入列表
     *
     * @param activity
     */
    public void addCurrentActivity(Activity activity) {
        if (activity != null && mActivityList != null) mActivityList.add(activity);
    }

    /**
     * 从列表移除activity
     *
     * @param activity
     */
    public void removeCurrentActivity(Activity activity) {
        if (activity != null && mActivityList != null) mActivityList.remove(activity);
    }

    /**
     * 清除所有的activity
     */
    public void removeAllActivity() {
        int size = mActivityList.size();
        for (int i = 0; i < size; i++) {
            Activity activity = mActivityList.get(i);
            if (activity != null) activity.finish();
        }
        mActivityList.clear();
    }


    public void finish() {
        removeAllActivity();
//        AccessTokenKeeper.clear(this);
        // 清理临时文件
        try {
            FileUtils.deleteFile(Settings.TEMP_PATH);
        } catch (Exception e) {
        }
//        MobclickAgent.onKillProcess(getApplicationContext());
        System.exit(0);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
        MultiDex.install(base);

    }
}
