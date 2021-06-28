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
import com.zzlecheng.yjcz.base.Commons;
import com.zzlecheng.yjcz.bean.EmergencyBean;
import com.zzlecheng.yjcz.bean.ProcedureBean;
import com.zzlecheng.yjcz.utils.CommonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: ProcedureChartRightAdapter
 * @描述: 处理adapter
 * @作者: huangchao
 * @时间: 2018/12/11 3:53 PM
 * @版本: 1.0.0
 */
public class ProcedureChartRightAdapter extends RecyclerView.Adapter<ProcedureChartRightAdapter.ViewHolder> {

    private Context mContext;
    private List<ProcedureBean> beans;
    private ButtonInterface buttonInterface;

    public ProcedureChartRightAdapter(Context context, List<ProcedureBean> procedureBeans) {
        mContext = context;
        beans = procedureBeans;
    }

    public void buttonOnClick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    public interface ButtonInterface {
        void onBtnClick(View view,String zt, String state,String content,String jdid,String jdname);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_procedure_chart_right, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProcedureBean procedureBean = beans.get(position);
        if ("0".equals(procedureBean.getJdzt())){//未操作
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.wwc));
            holder.tvState.setTextColor(mContext.getResources().getColor(R.color.wwc));
            holder.tvContent.setTextColor(mContext.getResources().getColor(R.color.wwc));
            holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.wwc));
            holder.tvList.setBackgroundColor(mContext.getResources().getColor(R.color.wwc));
            if (Commons.NODE_TYPE_SURE.equals(procedureBean.getNodetype())){//确认
                holder.tvState.setText("待确认");
            }else if (Commons.NODE_TYPE_MAKE.equals(procedureBean.getNodetype())){//操作
                holder.tvState.setText("待操作");
            }else if (Commons.NODE_TYPE_END.equals(procedureBean.getNodetype())){//结束
                holder.tvState.setText("待结束");
            }else {
                holder.tvState.setText("");
            }
        }else {//已操作
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.ywc));
            holder.tvState.setTextColor(mContext.getResources().getColor(R.color.ywc));
            holder.tvContent.setTextColor(mContext.getResources().getColor(R.color.ywc));
            holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.ywc));
            holder.tvList.setBackgroundColor(mContext.getResources().getColor(R.color.ywc));
            holder.tvState.setText("已完成");
        }
        holder.tvTitle.setText(procedureBean.getNodename());
        holder.tvContent.setText("内容：" + procedureBean.getNodedescribe());
        if ("".equals(procedureBean.getCzsj())){
            holder.tvTime.setText("时间：无" );
        }else {
            holder.tvTime.setText("时间：" + procedureBean.getCzsj());
        }
        holder.llContent.setOnClickListener(v -> {
            buttonInterface.onBtnClick(v,procedureBean.getJdzt(),procedureBean.getNodetype(),procedureBean.getNodedescribe(),
                    procedureBean.getId(),procedureBean.getNodename());
        });
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_list)
        TextView tvList;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.ll_content)
        LinearLayout llContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
