package com.ascend.assetcheck_jinhua.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascend.assetcheck_jinhua.R;
import com.ascend.assetcheck_jinhua.api.AppClient;
import com.ascend.assetcheck_jinhua.api.ExceptionHandle;
import com.ascend.assetcheck_jinhua.api.MySubscriber;
import com.ascend.assetcheck_jinhua.base.BaseActivity;
import com.ascend.assetcheck_jinhua.result.LoadResultBack;
import com.ascend.assetcheck_jinhua.result.TaskResult;
import com.ascend.assetcheck_jinhua.utils.SharedPreferencesUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.load)
    TextView load;
    @BindView(R.id.pandian)
    TextView pandian;
    @BindView(R.id.upload)
    TextView upload;

    private List<TaskResult> LoadTaskDatas = new ArrayList<>();
    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @OnClick({R.id.load, R.id.pandian, R.id.upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.load:
                //下载盘点任务
                LoadTaskDatas.clear();
                LoadTask();
                break;
            case R.id.pandian:
                //盘点
                if (LoadTaskDatas.size()<1){
                    Toast.makeText(MainActivity.this,"没有盘点任务,不可盘点",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, PandianActivity.class);
                intent.putExtra("data", (Serializable) LoadTaskDatas);
                startActivity(intent);
                break;
            case R.id.upload:
                //上传盘点结果
                break;
        }
    }

    /**
     * 下载盘点任务
     *
     * @author lishanhui
     * created at 2018-07-05 9:33
     */
    private void LoadTask() {
        mBaseActivity.showDialog(true);
        AppClient.getLockApi(MainActivity.this).getLoadTask().subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new MySubscriber<LoadResultBack>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                        //接下来就可以根据状态码进行处理...
                        int statusCode = responeThrowable.code;
                        Log.e("statusCode:",statusCode+"");
                        switch (statusCode) {
                            case ExceptionHandle.ERROR.SSL_ERROR:
                                Toast.makeText(MainActivity.this,"证书出错",Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.UNKNOWN:
                                Toast.makeText(MainActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.PARSE_ERROR:
                                Toast.makeText(MainActivity.this,"解析错误",Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.NETWORD_ERROR:
                                Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.HTTP_ERROR:
                                Toast.makeText(MainActivity.this,"协议出错",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        mBaseActivity.showDialog(false);
                    }

                    @Override
                    public void onNext(LoadResultBack data) {
                        mBaseActivity.showDialog(false);
                        if (data.getResultCode().equals("200")) {
                            if (data.getJsonObject()!=null&&data.getJsonObject().size()>0){
                                LoadTaskDatas = data.getJsonObject();
                            }else if (data.getResultCode().equals("404")){
                                //登录信息失效
                                Toast.makeText(mBaseActivity,"登录过期，重新登录中",Toast.LENGTH_SHORT).show();
                                AppClient.Login(mBaseActivity,
                                        SharedPreferencesUtil.getString(mBaseActivity,"phone"),
                                        SharedPreferencesUtil.getString(mBaseActivity,"psw"));
                            }else {
                                Toast.makeText(MainActivity.this,"没有可下载的盘点任务",Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(MainActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
