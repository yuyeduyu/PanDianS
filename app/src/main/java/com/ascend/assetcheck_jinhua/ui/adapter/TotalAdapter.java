package com.ascend.assetcheck_jinhua.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascend.assetcheck_jinhua.R;
import com.ascend.assetcheck_jinhua.result.TaskResult;
import com.ascend.assetcheck_jinhua.ui.activity.StartPandianActivity;
import com.uhf.scanlable.UHfData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：lishanhui on 2018-06-11.
 * 描述：开始盘点 全部
 */

public class TotalAdapter extends RecyclerView.Adapter<TotalAdapter.Holder> {
    private Context mContext;
    private List<TaskResult> datas;

    public TotalAdapter(Context context, List<TaskResult> datas) {
        mContext = context;
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_total, parent, false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        TaskResult result = datas.get(position);
        holder.id.setText(position+1 + "." + result.getProductCode());
        holder.name.setText(result.getProductName());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.id)
        TextView id;
        @BindView(R.id.name)
        TextView name;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}