package com.ascend.assetcheck_jinhua.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ascend.assetcheck_jinhua.result.TaskBack;
import com.ascend.assetcheck_jinhua.result.TaskResult;
import com.ascend.assetcheck_jinhua.ui.adapter.CompleteAdapter;
import com.ascend.assetcheck_jinhua.ui.adapter.TaskAdapter;
import com.ascend.assetcheck_jinhua.ui.adapter.WaitAdapter;
import com.ascend.assetcheck_jinhua.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<TaskBack.Task> LoadTaskDatas = new ArrayList<>();
    private TaskAdapter waitAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

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
                if (LoadTaskDatas.size() < 1) {
                    Toast.makeText(MainActivity.this, "没有盘点任务,不可盘点", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void initViews() {
        super.initViews();
        initRecyle();
        LoadTask();
    }
    private void initRecyle() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        if (LoadTaskDatas == null) {
            LoadTaskDatas = new ArrayList<>();
        }
        waitAdapter = new TaskAdapter(this, LoadTaskDatas);
        recyclerview.setAdapter(waitAdapter);
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

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
                .subscribe(new MySubscriber<TaskBack>(this) {
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
                                Toast.makeText(MainActivity.this, "证书出错", Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.UNKNOWN:
                                Toast.makeText(MainActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.PARSE_ERROR:
                                Toast.makeText(MainActivity.this, "解析错误", Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.NETWORD_ERROR:
                                Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.HTTP_ERROR:
                                Toast.makeText(MainActivity.this, "协议出错", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        mBaseActivity.showDialog(false);
                    }

                    @Override
                    public void onNext(TaskBack data) {
                        mBaseActivity.showDialog(false);
                        if (data.getResultCode().equals("200")) {
                            if (data.getJsonObject() != null && data.getJsonObject().size() > 0) {
                                LoadTaskDatas.addAll(data.getJsonObject());
                                waitAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MainActivity.this, "没有可下载的盘点任务", Toast.LENGTH_SHORT).show();
                            }

                        } else if (data.getResultCode().equals("404")) {
                            //登录信息失效
//                            Toast.makeText(mBaseActivity,"登录过期，重新登录中",Toast.LENGTH_SHORT).show();
                            AppClient.Login(mBaseActivity,
                                    SharedPreferencesUtil.getString(mBaseActivity, "phone"),
                                    SharedPreferencesUtil.getString(mBaseActivity, "psw"));
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(TaskBack.Task result) {
        LoadTaskDatas.remove(result);
        waitAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
