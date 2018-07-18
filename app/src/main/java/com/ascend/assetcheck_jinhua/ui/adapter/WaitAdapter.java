package com.ascend.assetcheck_jinhua.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascend.assetcheck_jinhua.R;
import com.ascend.assetcheck_jinhua.result.LoadResultBack;
import com.ascend.assetcheck_jinhua.result.TaskResult;
import com.ascend.assetcheck_jinhua.ui.activity.StartPandianActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：lishanhui on 2018-06-11.
 * 描述：
 */

public class WaitAdapter extends RecyclerView.Adapter<WaitAdapter.Holder> {

    private Context mContext;
    private List<TaskResult> datas;

    public WaitAdapter(Context context, List<TaskResult> datas) {
        mContext = context;
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_wait, parent, false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (position == 0) {
            holder.area.setBackgroundResource(R.color.line_gray);
            holder.pandian.setBackgroundResource(R.color.line_gray);
            holder.pandian.setTextColor(ContextCompat.getColor(mContext,R.color.black33));
            holder.area.setText("盘点区域");
            holder.pandian.setText("操作");
        }else {
            holder.area.setText(datas.get(position-1).getTask_name());
            holder.area.setBackgroundResource(R.color.white);
            holder.pandian.setBackgroundResource(R.color.white);
            holder.pandian.setTextColor(ContextCompat.getColor(mContext,R.color.button_blue));
            holder.pandian.setText("开始盘点");
            holder.pandian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //开始盘点
                    Intent intent = new Intent(mContext, StartPandianActivity.class);
                    intent.putExtra("data",datas.get(position-1));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.area)
        TextView area;
        @BindView(R.id.pandian)
        TextView pandian;
        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}