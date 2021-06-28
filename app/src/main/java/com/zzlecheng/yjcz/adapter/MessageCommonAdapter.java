package com.zzlecheng.yjcz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.bean.MessageBean;
import com.zzlecheng.yjcz.utils.LogUtils;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: MessageCommonAdapter
 * @描述: 消息
 * @作者: huangchao
 * @时间: 2018/12/27 5:20 PM
 * @版本: 1.0.0
 */
public class MessageCommonAdapter extends RecyclerView.Adapter<MessageCommonAdapter.ViewHolder> {

    private Context mContext;
    private List<MessageBean> beans;
    private ButtonInterface buttonInterface;

    public MessageCommonAdapter(Context context, List<MessageBean> messageBeans) {
        mContext = context;
        beans = messageBeans;
    }

    public void buttonOnClick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    public interface ButtonInterface {
        void onItemClick(View view, String id, String lcid, String lclsid, String lcmc);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_common, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageBean messageBean = beans.get(position);
        holder.tvTitle.setText(messageBean.getTitle());
        holder.tvContent.setText("内容：" + messageBean.getMessage());
        holder.tvTime.setText("时间：" + messageBean.getDates());
        String extraStr = messageBean.getExtraStr();
//        String lcid = "";
//        String lclsid = "";
//        String lcmc = "";
        String lcid = JSONObject.parseObject(extraStr).get("lcid").toString();
        String lclsid = JSONObject.parseObject(extraStr).getString("lclsid").toString();
        String lcmc = JSONObject.parseObject(extraStr).getString("lcmc").toString();
        holder.llMessage.setOnClickListener(v -> buttonInterface.onItemClick(v, messageBean.getId(),
                lcid, lclsid, lcmc));
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.ll_message)
        LinearLayout llMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
