package com.zzlecheng.yjcz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.bean.ReportTodayBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: ReportControlAdapter
 * @描述: 控制adapter
 * @作者: huangchao
 * @时间: 2018/9/3 下午4:15
 * @版本: 1.0.0
 */
public class ReportControlAdapter extends RecyclerView.Adapter<ReportControlAdapter.ViewHolder> {

    private Context context;
    private List<ReportTodayBean> reportTodayBeans;

    public ReportControlAdapter(Context context, List<ReportTodayBean> reportTodayBeans) {
        this.context = context;
        this.reportTodayBeans = reportTodayBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ReportTodayBean bean = reportTodayBeans.get(position);
        viewHolder.tvDepartment.setText(bean.getZzjgmc());
        viewHolder.tvUser.setText(bean.getUsername());
        viewHolder.tvReportTime.setText(bean.getDates());
        viewHolder.tvTypes.setText(bean.getTypes());
    }

    @Override
    public int getItemCount() {
        return reportTodayBeans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_department)
        TextView tvDepartment;
        @BindView(R.id.tv_user)
        TextView tvUser;
        @BindView(R.id.tv_reportTime)
        TextView tvReportTime;
        @BindView(R.id.tv_types)
        TextView tvTypes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
