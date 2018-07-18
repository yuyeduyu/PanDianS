package com.ascend.assetcheck_jinhua.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
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
import com.ascend.assetcheck_jinhua.result.getLoadTaskResultBack;
import com.ascend.assetcheck_jinhua.result.upLoadResult;
import com.ascend.assetcheck_jinhua.ui.adapter.AbnormalAdapter;
import com.ascend.assetcheck_jinhua.ui.adapter.TotalAdapter;
import com.ascend.assetcheck_jinhua.utils.SharedPreferencesUtil;
import com.uhf.scanlable.UHfData;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StartPandianActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.bg_finish)
    Button bgFinish;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.abnormal)
    TextView abnormal;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.recyclerview1)
    RecyclerView recyclerview1;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private TotalAdapter totalAdapter;
    private List<TaskResult> totalDatas;

    private AbnormalAdapter abnormalAdapter;
    private List<TaskResult> abnormalDatas;

    private List<TaskResult> allDatas = new ArrayList<>();//服务器待盘点所有数据
    private TaskResult data;

    //扫描初始化需要对象
    private int MESSAGE_SUCCESS = 0;
    private int MESSAGE_FAIL = 1;
    private String devport = "/dev/ttyMT1";

    //扫描需要参数对象
    private Timer timer;
    private Handler dataHandler;
    private boolean isCanceled = true;
    private static final int SCAN_INTERVAL = 5;

    private static final int MSG_UPDATE_LISTVIEW = 0;
    private static final int MODE_18000 = 1;
    private static boolean Scanflag = false;
    int selectedEd = 0;
    int TidFlag = 0;
    int AntIndex = 0;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_start_pandian);
    }

    @Override
    protected void initViews() {
        super.initViews();
        data = (TaskResult) getIntent().getSerializableExtra("data");
        title.setText(data.getTask_name());
        bgFinish.setVisibility(View.VISIBLE);
        initRecyle();

        getLoadTask(String.valueOf(data.getId()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new UHfData(this);
        try {
            dataHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (isCanceled)
                        return;
                    switch (msg.what) {
                        case MSG_UPDATE_LISTVIEW:
                            totalDatas.clear();
                            abnormalDatas.clear();
                            List<UHfData.InventoryTagMap> back = UHfData.lsTagList;
                            for (int i = 0; i < allDatas.size(); i++) {
                                allDatas.get(i).setActualQuantity(1);
                                allDatas.get(i).setDifferenceNum(allDatas.get(i).getInventoryNum()-allDatas.get(i).getActualQuantity());
                                for (int j = 0; j < back.size(); j++) {
                                    if (allDatas.get(i).getProductCode().equals(back.get(j).strEPC)) {
                                        if (allDatas.get(i).getInventoryNum() == allDatas.get(i).getActualQuantity()){
                                            allDatas.get(i).setInventoryResult("相符");
                                            totalDatas.add(allDatas.get(i));
                                        }
                                        else if (allDatas.get(i).getInventoryNum()>allDatas.get(i).getActualQuantity()){
                                            allDatas.get(i).setInventoryResult("盘亏");
                                            abnormalDatas.add(allDatas.get(i));
                                        }
                                        else if (allDatas.get(i).getInventoryNum()<allDatas.get(i).getActualQuantity()){
                                            allDatas.get(i).setInventoryResult("盘盈");
                                            abnormalDatas.add(allDatas.get(i));
                                        }
                                    }
                                }
                            }
                            abnormalAdapter.notifyDataSetChanged();
                            totalAdapter.notifyDataSetChanged();

                            break;
                        default:
                            break;
                    }
                    super.handleMessage(msg);
                }

            };
        } catch (Exception e) {

        }
    }

    /**
     * 获取盘点数据
     *
     * @author lishanhui
     * created at 2018-07-09 11:46
     */
    private void getLoadTask(String id) {
        mBaseActivity.showDialog(true);
        AppClient.getLockApi(StartPandianActivity.this).downloadTask(id).subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new MySubscriber<getLoadTaskResultBack>(this) {
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
                    public void onNext(getLoadTaskResultBack back) {
                        mBaseActivity.showDialog(false);
                        allDatas.clear();
                        if (back.getResultCode().equals("200")) {
                            if (back.getJsonObject() != null && back.getJsonObject().size() > 0) {
                                allDatas.addAll(back.getJsonObject().get(0));
                            } else {
                                Toast.makeText(mBaseActivity, "未获取盘点信息", Toast.LENGTH_SHORT).show();
                            }

                        } else if (back.getResultCode().equals("404")) {
                            //登录信息失效
                            Toast.makeText(mBaseActivity, "登录过期，重新登录中", Toast.LENGTH_SHORT).show();
                            AppClient.Login(mBaseActivity,
                                    SharedPreferencesUtil.getString(mBaseActivity, "phone"),
                                    SharedPreferencesUtil.getString(mBaseActivity, "psw"));
                        } else {
                            Toast.makeText(mBaseActivity, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initRecyle() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        totalDatas = new ArrayList<>();

        totalAdapter = new TotalAdapter(this, totalDatas);
        recyclerview.setAdapter(totalAdapter);
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerview1.setLayoutManager(new LinearLayoutManager(this));
        abnormalDatas = new ArrayList<>();

        abnormalAdapter = new AbnormalAdapter(this, abnormalDatas);
        recyclerview1.setAdapter(abnormalAdapter);
        recyclerview1.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    @OnClick({R.id.back, R.id.bg_finish, R.id.total, R.id.abnormal, R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.bg_finish:
                //完成盘点
                for (TaskResult result:allDatas){
                    if (result.getInventoryResult()==null){
                        showNormalDialog();
                        return;
                    }
                }
                commitData();
                break;
            case R.id.total:
                recyclerview.setVisibility(View.VISIBLE);
                recyclerview1.setVisibility(View.GONE);
                total.setBackground(ContextCompat.getDrawable(StartPandianActivity.this, R.drawable.bg_slect));
                abnormal.setBackground(ContextCompat.getDrawable(StartPandianActivity.this, R.drawable.bg_unslect));
                break;
            case R.id.abnormal:
                recyclerview.setVisibility(View.GONE);
                recyclerview1.setVisibility(View.VISIBLE);
                total.setBackground(ContextCompat.getDrawable(StartPandianActivity.this, R.drawable.bg_unslect));
                abnormal.setBackground(ContextCompat.getDrawable(StartPandianActivity.this, R.drawable.bg_slect));
                break;
            case R.id.fab:
                //扫码获取设备信息

                fabClick();

                break;
        }
    }

    /*
     * @Author:lishanhui
     * @Description: 提交数据
     * @Date: 2018/7/15 0015 22:35
     */
    private void commitData() {
        mBaseActivity.showDialog(true);
        AppClient.getLockApi(StartPandianActivity.this).upLoadTask(formaJson()).subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new MySubscriber<upLoadResult>(this) {
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
                    public void onNext(upLoadResult back) {
                        mBaseActivity.showDialog(false);
//                        上传成功返回{"message":"成功!","resultCode":200}
//                        失败返回 {"message":"上传失败!","resultCode":521}
//                        {"message":"上传结果为空!","resultCode":522}

                        if (!back.getResultCode().equals("404")) {

                            Toast.makeText(mBaseActivity, back.getResultCode() + ":" + back.getMessage(), Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new MessageEvent(data.getTaskId()));
                            finish();
                        } else if (back.getResultCode().equals("404")) {
                            //登录信息失效
                            Toast.makeText(mBaseActivity, "登录过期，重新登录中", Toast.LENGTH_SHORT).show();
                            AppClient.Login(mBaseActivity,
                                    SharedPreferencesUtil.getString(mBaseActivity, "phone"),
                                    SharedPreferencesUtil.getString(mBaseActivity, "psw"));
                        } else {
                            Toast.makeText(mBaseActivity, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /*
     * @Author:lishanhui
     * @Description: 对象转换上传json
     * @Date: 2018/7/15 0015 22:40
     */
    private String formaJson() {
        StringBuffer str = new StringBuffer("{\"jsonObject\":\n" +
                "[[");
        for (int i = 0; i < allDatas.size(); i++) {
            if (i == 0) {
                str.append("{actualQuantity:" + "\"" + allDatas.get(i).getActualQuantity() + "\""
                        + ",assetsType:" + "\"" + allDatas.get(i).getAssetsType() + "\""
                        + ",differenceNum:" + "\"" + allDatas.get(i).getDifferenceNum() + "\""
                        + ",id:" + "\"" + allDatas.get(i).getId() + "\""
                        + ",inventoryNum:" + "\"" + allDatas.get(i).getInventoryNum() + "\""
                        + ",inventoryResult:" + "\"" + allDatas.get(i).getInventoryResult() + "\""
                        + ",productCode:" + "\"" + allDatas.get(i).getProductCode() + "\""
                        + ",productName:" + "\"" + allDatas.get(i).getProductName() + "\""
                        + ",receiveDepartment:" + "\"" + allDatas.get(i).getReceiveDepartment() + "\""
                        + ",receivePerson:" + "\"" + allDatas.get(i).getReceivePerson() + "\""
                        + ",receivePlace:" + "\"" + allDatas.get(i).getReceivePlace() + "\""
                        + ",specificationModel:" + "\"" + allDatas.get(i).getSpecificationModel() + "\""
                        + ",remark:" + "\"" + allDatas.get(i).getRemark() + "\""
                        + ",taskId:" + "\"" + allDatas.get(i).getTaskId() + "\"}"
                );
            } else {
                str.append(",{actualQuantity:" + "\"" + allDatas.get(i).getActualQuantity() + "\""
                        + ",assetsType:" + "\"" + allDatas.get(i).getAssetsType() + "\""
                        + ",differenceNum:" + "\"" + allDatas.get(i).getDifferenceNum() + "\""
                        + ",id:" + "\"" + allDatas.get(i).getId() + "\""
                        + ",inventoryNum:" + "\"" + allDatas.get(i).getInventoryNum() + "\""
                        + ",inventoryResult:" + "\"" + allDatas.get(i).getInventoryResult() + "\""
                        + ",productCode:" + "\"" + allDatas.get(i).getProductCode() + "\""
                        + ",productName:" + "\"" + allDatas.get(i).getProductName() + "\""
                        + ",receiveDepartment:" + "\"" + allDatas.get(i).getReceiveDepartment() + "\""
                        + ",receivePerson:" + "\"" + allDatas.get(i).getReceivePerson() + "\""
                        + ",receivePlace:" + "\"" + allDatas.get(i).getReceivePlace() + "\""
                        + ",specificationModel:" + "\"" + allDatas.get(i).getSpecificationModel() + "\""
                        + ",remark:" + "\"" + allDatas.get(i).getRemark() + "\""
                        + ",taskId:" + "\"" + allDatas.get(i).getTaskId() + "\"}"
                );
            }
        }
        str.append("]],\"message\":\"成功!\",\"resultCode\":200}");
        Log.e("str", str.toString());
        return str.toString();
    }


    //初始化扫描集合代码
    @Override
    protected void onStart() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    int result = UHfData.UHfGetData.OpenUHf(devport, 57600);
                    if (result == 0) {
                        mHandler.sendEmptyMessage(MESSAGE_SUCCESS);
                    } else {
                        mHandler.sendEmptyMessage(MESSAGE_FAIL);
                    }
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(MESSAGE_FAIL);
                }
            }
        }).start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        UHfData.UHfGetData.CloseUHf();
        super.onStop();
    }

    private ConnectHandler mHandler = new ConnectHandler(this);

    private static class ConnectHandler extends Handler {
        private WeakReference<StartPandianActivity> mReference;
        private StartPandianActivity mActivity;

        ConnectHandler(StartPandianActivity activity) {
            mReference = new WeakReference<StartPandianActivity>(activity);
            mActivity = mReference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == mActivity.MESSAGE_SUCCESS) {
                Toast.makeText(mActivity.getApplicationContext(), "设备连接成功",
                        Toast.LENGTH_SHORT).show();
            } else if (msg.what == mActivity.MESSAGE_FAIL) {
                Toast.makeText(mActivity.getApplicationContext(), "设备连接失败",
                        Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 扫描
     *
     * @author lish
     * created at 2018-07-16 15:38
     */
    private void fabClick() {
        try {
            if (timer == null) {
                if (totalAdapter != null) {
                    UHfData.lsTagList.clear();
                    UHfData.dtIndexMap.clear();
                    dataHandler.removeMessages(MSG_UPDATE_LISTVIEW);
                    dataHandler.sendEmptyMessage(MSG_UPDATE_LISTVIEW);
                }
                //自动模式
                selectedEd = 255;
                AntIndex = 0;
                isCanceled = false;
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (Scanflag)
                            return;
                        Scanflag = true;
                        UHfData.Inventory_6c(selectedEd, TidFlag);
//						UHfData.Inventory_6c_Mask((byte)0, 16, 0, UHfGetData.hexStringToBytes("E200"));
                        dataHandler.removeMessages(MSG_UPDATE_LISTVIEW);
                        dataHandler.sendEmptyMessage(MSG_UPDATE_LISTVIEW);
                        Scanflag = false;
                    }
                }, 0, SCAN_INTERVAL);
            } else {
                isCanceled = true;
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        } catch (Exception e) {
        }
    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(StartPandianActivity.this);
        normalDialog.setTitle("异常提醒");
        normalDialog.setMessage("存在未盘点的设备，是否设置为异常");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        for (int i=0;i>allDatas.size();i++){
                            if (allDatas.get(i).getInventoryResult()==null){
                                allDatas.get(i).setInventoryResult("盘亏");
                                abnormalDatas.add(allDatas.get(i));
                            }
                            abnormalAdapter.notifyDataSetChanged();
                        }
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }
}

