package com.zzlecheng.yjcz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.bean.EmergencyBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: EmergencyHandAdapter
 * @描述: 处理adapter
 * @作者: huangchao
 * @时间: 2018/12/11 3:53 PM
 * @版本: 1.0.0
 */
public class EmergencyHandAdapter extends RecyclerView.Adapter<EmergencyHandAdapter.ViewHolder> {

    private Context mContext;
    private List<EmergencyBean> beans;
    private ButtonInterface buttonInterface;

    public EmergencyHandAdapter(Context context, List<EmergencyBean> emergencyBeans) {
        mContext = context;
        beans = emergencyBeans;
    }

    public void buttonOnClick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    public interface ButtonInterface {
        void onBtnClick(View view, String lclsId, String lcId,String lcmc);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_emergency_hand, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyBean emergencyBean = beans.get(position);
        holder.tvTitle.setText(emergencyBean.getLcmc());
        holder.tvContent.setText("内容："+emergencyBean.getRemarks());
        holder.tvTime.setText("时间："+emergencyBean.getCzsj());
        holder.tvName.setText(emergencyBean.getCzrmc());
        holder.llContent.setOnClickListener(v -> {
            buttonInterface.onBtnClick(v, emergencyBean.getId(), emergencyBean.getLcid(),emergencyBean.getLcmc());
        });
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_content)
        LinearLayout llContent;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
