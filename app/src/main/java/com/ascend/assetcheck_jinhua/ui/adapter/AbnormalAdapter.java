package com.ascend.assetcheck_jinhua.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascend.assetcheck_jinhua.R;
import com.ascend.assetcheck_jinhua.result.TaskResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：lishanhui on 2018-06-11.
 * 描述：开始盘点 异常
 */

public class AbnormalAdapter extends RecyclerView.Adapter<AbnormalAdapter.Holder> {


    private Context mContext;
    private List<TaskResult> datas;

    public AbnormalAdapter(Context context, List<TaskResult> datas) {
        mContext = context;
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_abnormal, parent, false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        TaskResult result = datas.get(position);
//        private String inventoryResult;//盘点结果(相符   盘亏  盘盈)
        if (result.getInventoryResult().equals("盘亏")){
            holder.image.setBackground(ContextCompat.getDrawable(mContext,R.drawable.text_lost));
            holder.image.setText("少");
            holder.bgBeizhu.setBackgroundResource(R.drawable.btn_click);
            holder.id.setText(position+"."+result.getProductCode());
            holder.name.setText(result.getProductName());
        }else if(result.getInventoryResult().equals("盘盈")){
            holder.image.setBackground(ContextCompat.getDrawable(mContext,R.drawable.text_more));
            holder.image.setText("多");
            holder.bgBeizhu.setBackgroundResource(R.drawable.btn_click_more);
            holder.id.setText(position+"."+result.getProductCode());
            holder.name.setText(result.getProductName());
        }
        holder.bgBeizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        TextView image;
        @BindView(R.id.id)
        TextView id;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.bg_beizhu)
        Button bgBeizhu;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * @param context
     * @param b                     确定按钮的颜色  false 为红色  true为蓝色
     * @param titles                对话框的内容
     * @param commitStr             确定按钮的显示内容
     *                              //     * @param onClickListenerCancle  取消按钮的点击事件
     * @return   带Editetext dialog
     */
    public AlertDialog showDialogsByCause(final Context context, boolean b, String titles, String
            commitStr, final int czzt, final String str) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        View views = LayoutInflater.from(context).inflate(R.layout.dialog_add_order_edite, null);
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout layout = (LinearLayout) views.findViewById(R.id.layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(layout.getLayoutParams());
        params.setMargins(60,0,60,0);
        layout.setLayoutParams(params);

        alertDialog.show();
        TextView title = (TextView) views.findViewById(R.id.title);
        final EditText content_et = (EditText) views.findViewById(R.id.content_et);
        final Button commitBtn = (Button) views.findViewById(R.id.commit_btn);
        commitBtn.setText(commitStr);

        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        final Button cancle = (Button) views.findViewById(R.id.cancle_btn);
        if (b) {
            commitBtn.setBackground(context.getResources().getDrawable(R.drawable.tag_add_order));
        } else {
            commitBtn.setBackground(context.getResources().getDrawable(R.drawable
                    .tag_gradle_commit));
        }
        title.setText(titles);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //执行操作
//                    cause = content_et.getText().toString();
            }
        });
        window.setContentView(views);
        return alertDialog;
    }
}