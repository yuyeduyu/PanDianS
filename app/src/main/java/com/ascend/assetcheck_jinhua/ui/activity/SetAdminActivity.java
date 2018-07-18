/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ascend.assetcheck_jinhua.ui.activity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ascend.assetcheck_jinhua.R;
import com.ascend.assetcheck_jinhua.api.AppClient;
import com.ascend.assetcheck_jinhua.ui.myview.IpEditer;
import com.ascend.assetcheck_jinhua.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetAdminActivity extends Activity {
    @BindView(R.id.ip_device)
    IpEditer ipDevice;
    @BindView(R.id.port_device)
    EditText portDevice;
    @BindView(R.id.button_ok)
    Button buttonOk;
    // Debugging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ip_admin);
        ButterKnife.bind(this);
        //窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        win.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        initView();
    }

    private void initView() {
        ipDevice.setText(SharedPreferencesUtil.getString(this, "ip", "183.146.254.204"));
        portDevice.setText(SharedPreferencesUtil.getString(this, "port", "7810"));
    }

    @OnClick(R.id.button_ok)
    public void onViewClicked() {
        if (TextUtils.isEmpty(ipDevice.getText().toString())) {
            Toast.makeText(SetAdminActivity.this, "请输入IP地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(portDevice.getText().toString())) {
            Toast.makeText(SetAdminActivity.this, "请输入端口", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ipCheck(ipDevice.getText().toString())){
            Toast.makeText(SetAdminActivity.this, "请输入正确的IP", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferencesUtil.putString(this, "ip", ipDevice.getText().toString());
        SharedPreferencesUtil.putString(this, "port", portDevice.getText().toString());
        AppClient.resetLocalApi(SetAdminActivity.this);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (text.matches(regex)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }
}
