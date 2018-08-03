package com.ascend.assetcheck_jinhua.ui.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ascend.assetcheck_jinhua.R;
import com.ascend.assetcheck_jinhua.api.AppClient;
import com.ascend.assetcheck_jinhua.api.BaseResult;
import com.ascend.assetcheck_jinhua.api.ExceptionHandle;
import com.ascend.assetcheck_jinhua.api.MySubscriber;
import com.ascend.assetcheck_jinhua.base.BaseActivity;
import com.ascend.assetcheck_jinhua.utils.SharedPreferencesUtil;
import com.ascend.assetcheck_jinhua.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.psw)
    EditText psw;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.ll_set)
    LinearLayout llSet;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initViews() {
        super.initViews();
        verifyStoragePermissions(this);
        phone.setText(SharedPreferencesUtil.getString(mBaseActivity, "phone", ""));
        psw.setText(SharedPreferencesUtil.getString(mBaseActivity, "psw", ""));

        llSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置服务器地址
                startActivity(new Intent(LoginActivity.this,SetAdminActivity.class));
            }
        });
    }

    /**
     * 登录
     */
    private void Login(final String phone, final String psw) {
        mBaseActivity.showDialog(true);
        AppClient.getLockApi(LoginActivity.this).Login(phone, psw).subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new MySubscriber<BaseResult>(this) {
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
                                Toast.makeText(mBaseActivity, "连接服务器失败", Toast.LENGTH_SHORT).show();
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
                            //保存登录信息
                            SharedPreferencesUtil.putString(mBaseActivity, "phone", phone);
                            SharedPreferencesUtil.putString(mBaseActivity, "psw", psw);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(mBaseActivity, "登录成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(mBaseActivity, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    @OnClick(R.id.login)
    public void onViewClicked() {

        if (phone.getText().toString().trim().equals("")) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (psw.getText().toString().trim().length() < 6) {
            Toast.makeText(LoginActivity.this, "密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        Login(phone.getText().toString().trim(), psw.getText().toString().trim());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

