package com.zzlecheng.yjcz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.bean.ProcedureDealBean;

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
public class ProcedureDealSunAdapter extends RecyclerView.Adapter<ProcedureDealSunAdapter.ViewHolder> {

    private Context mContext;
    private List<ProcedureDealBean.PlBean> mPlBeans;

    public ProcedureDealSunAdapter(Context context, List<ProcedureDealBean.PlBean> plBeans) {
        mContext = context;
        mPlBeans = plBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_procedure_deal_content, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProcedureDealBean.PlBean emergencyBean = mPlBeans.get(position);
        holder.tvName.setText(emergencyBean.getPlxq().getCzrmc());
        holder.tvCzms.setText(emergencyBean.getPlxq().getCzms());
        holder.tvCzsj.setText(emergencyBean.getPlxq().getCzsj());
    }

    @Override
    public int getItemCount() {
        return mPlBeans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_czms)
        TextView tvCzms;
        @BindView(R.id.tv_czsj)
        TextView tvCzsj;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
