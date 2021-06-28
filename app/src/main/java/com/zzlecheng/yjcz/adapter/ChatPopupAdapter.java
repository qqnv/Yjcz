package com.zzlecheng.yjcz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.bean.PopBean;
import com.zzlecheng.yjcz.utils.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: ChatPopupAdapter
 * @描述: 聊天adapter
 * @作者: huangchao
 * @时间: 2018/12/28 2:39 PM
 * @版本: 1.0.0
 */
public class ChatPopupAdapter extends RecyclerView.Adapter<ChatPopupAdapter.MyViewHolder> {

    private Context context;
    private List<PopBean> popBeans;

    public ChatPopupAdapter(Context context, List<PopBean> popBeans) {
        this.context = context;
        this.popBeans = popBeans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_pop, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PopBean popBean = popBeans.get(position);
        holder.tvDepartment.setText(popBean.getDept()+"：");
        holder.tvUser.setText(popBean.getUsers());
    }

    @Override
    public int getItemCount() {
        return popBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_department)
        TextView tvDepartment;
        @BindView(R.id.tv_user)
        TextView tvUser;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
