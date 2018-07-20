package com.ascend.assetcheck_jinhua.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascend.assetcheck_jinhua.R;
import com.ascend.assetcheck_jinhua.api.AppClient;
import com.ascend.assetcheck_jinhua.api.ExceptionHandle;
import com.ascend.assetcheck_jinhua.api.MySubscriber;
import com.ascend.assetcheck_jinhua.base.BaseActivity;
import com.ascend.assetcheck_jinhua.envent.MessageEvent;
import com.ascend.assetcheck_jinhua.result.LoadResultBack;
import com.ascend.assetcheck_jinhua.result.TaskResult;
import com.ascend.assetcheck_jinhua.result.getTaskRangeResltback;
import com.ascend.assetcheck_jinhua.ui.adapter.CompleteAdapter;
import com.ascend.assetcheck_jinhua.ui.adapter.WaitAdapter;
import com.ascend.assetcheck_jinhua.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PandianActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.wait)
    Button wait;
    @BindView(R.id.complete)
    Button complete;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.recyclerview1)
    RecyclerView recyclerview1;

    private CompleteAdapter completeAdapter;
    private List<TaskResult> completeDatas;

    private WaitAdapter waitAdapter;
    private List<TaskResult> waitDatas;

    private int id;//盘点任务id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_pandian);
    }

    @Override
    protected void initViews() {
        super.initViews();
        waitDatas = (List<TaskResult>) getIntent().getSerializableExtra("data");
        initRecyle();
        getTaskRange();
    }

    private void initRecyle() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        if (waitDatas == null) {
            waitDatas = new ArrayList<>();
        }
        waitAdapter = new WaitAdapter(this, waitDatas);
        recyclerview.setAdapter(waitAdapter);
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerview1.setLayoutManager(new LinearLayoutManager(this));
        if (completeDatas == null) {
            completeDatas = new ArrayList<>();
        }
        completeAdapter = new CompleteAdapter(this, completeDatas);
        recyclerview1.setAdapter(completeAdapter);
        recyclerview1.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    @OnClick({R.id.back, R.id.wait, R.id.complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.wait:
                recyclerview.setVisibility(View.VISIBLE);
                recyclerview1.setVisibility(View.GONE);
                wait.setBackground(ContextCompat.getDrawable(PandianActivity.this, R.drawable.bg_slect));
                complete.setBackground(ContextCompat.getDrawable(PandianActivity.this, R.drawable.bg_unslect));
                break;
            case R.id.complete:
                recyclerview.setVisibility(View.GONE);
                recyclerview1.setVisibility(View.VISIBLE);
                wait.setBackground(ContextCompat.getDrawable(PandianActivity.this, R.drawable.bg_unslect));
                complete.setBackground(ContextCompat.getDrawable(PandianActivity.this, R.drawable.bg_slect));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(TaskResult result) {
        waitDatas.remove(result);
        completeDatas.add(result);
        waitAdapter.notifyDataSetChanged();
        completeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    /**
     * 下载盘点任务
     *
     * @author lishanhui
     * created at 2018-07-05 9:33
     */
    private void getTaskRange() {
        mBaseActivity.showDialog(true);
        AppClient.getLockApi(PandianActivity.this).getTaskRange("2").subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new MySubscriber<getTaskRangeResltback>(this) {
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
                                Toast.makeText(mBaseActivity,"证书出错",Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.UNKNOWN:
                                Toast.makeText(mBaseActivity,"未知错误",Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.PARSE_ERROR:
                                Toast.makeText(mBaseActivity,"解析错误",Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.NETWORD_ERROR:
                                Toast.makeText(mBaseActivity,"网络错误",Toast.LENGTH_SHORT).show();
                                break;
                            case ExceptionHandle.ERROR.HTTP_ERROR:
                                Toast.makeText(mBaseActivity,"协议出错",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        mBaseActivity.showDialog(false);
                    }

                    @Override
                    public void onNext(getTaskRangeResltback data) {
                        mBaseActivity.showDialog(false);
                        if (data.getResultCode().equals("200")) {
                            if (data.getJsonObject()!=null&&data.getJsonObject().size()>0){
//                                LoadTaskDatas = data.getJsonObject();
                                Log.e("data",data.getJsonObject().get(0).getReceivePlace().get(0));
                            }else {
                                Toast.makeText(mBaseActivity,"没有可下载的盘点区域",Toast.LENGTH_SHORT).show();
                            }

                        }else if (data.getResultCode().equals("404")){
                            //登录信息失效
//                            Toast.makeText(mBaseActivity,"登录过期，重新登录中",Toast.LENGTH_SHORT).show();
                            AppClient.Login(mBaseActivity,
                                    SharedPreferencesUtil.getString(mBaseActivity,"phone"),
                                    SharedPreferencesUtil.getString(mBaseActivity,"psw"));
                        }
                    }
                });
    }
}
