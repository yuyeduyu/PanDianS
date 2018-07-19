package com.ascend.assetcheck_jinhua.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascend.assetcheck_jinhua.R;
import com.ascend.assetcheck_jinhua.base.BaseActivity;
import com.ascend.assetcheck_jinhua.envent.MessageEvent;
import com.ascend.assetcheck_jinhua.result.LoadResultBack;
import com.ascend.assetcheck_jinhua.result.TaskResult;
import com.ascend.assetcheck_jinhua.ui.adapter.CompleteAdapter;
import com.ascend.assetcheck_jinhua.ui.adapter.WaitAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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

}
