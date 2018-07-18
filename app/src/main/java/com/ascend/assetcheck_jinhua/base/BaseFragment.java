package com.ascend.assetcheck_jinhua.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Fragment基类
 * Created by zhaohh on 2016/5/3.
 */
public abstract class BaseFragment extends Fragment {
    protected BaseActivity mBaseActivity;
    public SharedPreferences my_save;
    private long lastClickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // FIXME: 2017/4/5
//        ToastUtil.showToast(getClass().getSimpleName());
        return createView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBaseActivity = (BaseActivity) getActivity();
        my_save=getActivity().getApplicationContext().getSharedPreferences("ADDRESS", Context.MODE_PRIVATE);
        getIntentData();
        initViews(view);

        addListeners();
        requestOnViewCreated();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 传值
     */
    protected void getIntentData() {
    }

    /**
     * 初始化本地数据
     */
    protected void initViews(View view) {
    }

    /**
     * 添加监听器
     */
    protected void addListeners() {
    }


    /**
     * 在onCreate中请求服务
     */
    protected void requestOnViewCreated() {
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
     * 防止重复点击
     * @return
     */
    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
