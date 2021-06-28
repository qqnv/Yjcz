package com.zzlecheng.yjcz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.eventbus.ClusterNameBean;
import com.zzlecheng.yjcz.eventbus.EventBusBean;
import com.zzlecheng.yjcz.utils.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.media.CamcorderProfile.get;
import static com.zzlecheng.yjcz.fragment.ClusterTalkBackFragment.currentGroupIds;

/**
 * @类名: ChatPopupAdapter
 * @描述: 聊天adapter
 * @作者: huangchao
 * @时间: 2018/12/28 2:39 PM
 * @版本: 1.0.0
 */
public class ClusterPopupAdapter extends RecyclerView.Adapter<ClusterPopupAdapter.MyViewHolder> {

    private Context context;
    private List<ClusterNameBean> clusterNameBeans;

    public ClusterPopupAdapter(Context context, List<ClusterNameBean> clusterNameBeans) {
        this.context = context;
        this.clusterNameBeans = clusterNameBeans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cluster_popup, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ClusterNameBean clusterNameBean = clusterNameBeans.get(position);
            holder.tvUser.setText(clusterNameBean.getName());
    }

    @Override
    public int getItemCount() {
        return clusterNameBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_user)
        TextView tvUser;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
