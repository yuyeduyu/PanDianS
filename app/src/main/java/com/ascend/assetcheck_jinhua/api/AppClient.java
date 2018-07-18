package com.ascend.assetcheck_jinhua.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ascend.assetcheck_jinhua.Config;
import com.ascend.assetcheck_jinhua.base.BaseActivity;
import com.ascend.assetcheck_jinhua.ui.activity.LoginActivity;
import com.ascend.assetcheck_jinhua.ui.activity.MainActivity;
import com.ascend.assetcheck_jinhua.utils.SharedPreferencesUtil;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：lishanhui on 2018-05-21.
 * 描述：联网 请求
 */

public class AppClient {
    ///assets/appHandheldMachine/getTask.do
    private static final String url = Config.BASEURL + "/assets/appHandheldMachine/";
    private static Retrofit retrofit;
    private static LockApi lockApi;

    public static LockApi getLockApi(Context context) {
        if (lockApi == null) {
            synchronized (AppClient.class) {
                //session
                ClearableCookieJar cookieJar =
                        new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .cookieJar(cookieJar);
                OkHttpClient client = builder.build();

                retrofit = new Retrofit.Builder()
                        .client(client)
                        .baseUrl(url)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                lockApi = retrofit.create(LockApi.class);
                Log.e("url:", url);
            }
        }
        return lockApi;
    }

    public static LockApi getLockApi(String urlPath) {
        if (lockApi == null) {
            synchronized (AppClient.class) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS);
                OkHttpClient client = builder.build();
                retrofit = new Retrofit.Builder()
                        .client(client)
                        .baseUrl(urlPath)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                lockApi = retrofit.create(LockApi.class);
            }
        }
        return lockApi;
    }
    public  static void  Login(final BaseActivity mBaseActivity, final String phone, final String psw) {
        mBaseActivity.showDialog(true);
        AppClient.getLockApi(mBaseActivity).Login(phone, psw).subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new MySubscriber<BaseResult>(mBaseActivity) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                        //接下来就可以根据状态码进行处理...
                        int statusCode = responeThrowable.code;
                        Log.e("statusCode:", statusCode + "");
                        switch (statusCode) {
                            case ExceptionHandle.ERROR.SSL_ERROR:
                                Toast.makeText(mBaseActivity, "证书出错", Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.UNKNOWN:
                                Toast.makeText(mBaseActivity, "未知错误", Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.PARSE_ERROR:
                                Toast.makeText(mBaseActivity, "解析错误", Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.NETWORD_ERROR:
                                Toast.makeText(mBaseActivity, "网络错误", Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.HTTP_ERROR:
                                Toast.makeText(mBaseActivity, "协议出错", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        mBaseActivity.showDialog(false);
                    }

                    @Override
                    public void onNext(BaseResult data) {
                        mBaseActivity.showDialog(false);
                        if (data.getResultCode().equals("200")) {
                            Toast.makeText(mBaseActivity, "登录成功", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(mBaseActivity, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
