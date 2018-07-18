package com.ascend.assetcheck_jinhua.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascend.assetcheck_jinhua.R;
import com.ascend.assetcheck_jinhua.result.TaskResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：lishanhui on 2018-06-11.
 * 描述：
 */

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.Holder> {

    private Context mContext;
    private List<TaskResult> datas;

    public CompleteAdapter(Context context, List<TaskResult> datas) {
        mContext = context;
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_complete, parent, false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (position == 0) {
            holder.area.setBackgroundResource(R.color.line_gray);
            holder.area.setText("盘点区域");

        }else {
            holder.area.setText(datas.get(position-1).getTask_name());
            holder.area.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.area)
        TextView area;
        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}