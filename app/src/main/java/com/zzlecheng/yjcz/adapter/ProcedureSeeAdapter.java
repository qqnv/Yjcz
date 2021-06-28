package com.zzlecheng.yjcz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.bean.ProcedureSeeBean;

import java.util.List;

import butterknife.BindView;

/**
 * @类名: ProcedureSeeAdapter
 * @描述: 查看adapter
 * @作者: huangchao
 * @时间: 2018/12/24 6:20 PM
 * @版本: 1.0.0
 */
public class ProcedureSeeAdapter extends RecyclerView.Adapter<ProcedureSeeAdapter.MyViewHolder> {

    private Context context;
    private List<ProcedureSeeBean.PlBean> beans;

    public ProcedureSeeAdapter(Context context, List<ProcedureSeeBean.PlBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_procedure_see, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProcedureSeeBean.PlBean plBean = beans.get(position);
        holder.tvJdDescribe.setText(plBean.getCzms());
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_jdDescribe)
        TextView tvJdDescribe;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
