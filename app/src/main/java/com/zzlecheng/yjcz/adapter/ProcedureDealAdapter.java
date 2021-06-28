package com.zzlecheng.yjcz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.bean.ProcedureDealBean;
import com.zzlecheng.yjcz.utils.CommonUtil;
import com.zzlecheng.yjcz.utils.MyImageViewUtil;

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
public class ProcedureDealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private View headerView;
    private List<Object> data;
    private int ITEM_HEADER = 1, ITEM_CONTENT = 2, ITEM_FOOTER = 3;
    private ButtonInterface buttonInterface;

    public ProcedureDealAdapter(Context context, List<Object> data) {
        mContext = context;
        this.data = data;
    }

    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    public interface ButtonInterface {
        void onCallClick(View view, String phone);

        void onMessageClick(View view, String phone);

        void onCommentClick(View view, String lclsId, String jdlsId);

        void onImgClick(View view, String path, String lx);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_HEADER) {
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_procedure_deal_header, parent, false);
            return new MyHolderViewHeader(view);
        } else if (viewType == ITEM_CONTENT) {
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_procedure_deal_content, parent, false);
            return new MyHolderViewContent(view);
        } else {
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_procedure_deal_footer, parent, false);
            return new MyHolderViewFooter(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolderViewHeader) {
            ProcedureDealBean.LcxqBean procedureDealBean =
                    (ProcedureDealBean.LcxqBean) data.get(position);
            ((MyHolderViewHeader) holder).tvJd.setText(procedureDealBean.getJdname());
            if ("".equals(procedureDealBean.getCzms())){
                ((MyHolderViewHeader) holder).tvCzms.setText("无操作描述");
            }else {
                ((MyHolderViewHeader) holder).tvCzms.setText(procedureDealBean.getCzms());
            }
            ((MyHolderViewHeader) holder).tvFbr.setText("发布人：" + procedureDealBean.getCzrmc());
            ((MyHolderViewHeader) holder).tvFbsj.setText("操作时间：" + procedureDealBean.getCzsj());
            ((MyHolderViewHeader) holder).ivCall.setOnClickListener(v ->
                    buttonInterface.onCallClick(v, procedureDealBean.getSj()));
            ((MyHolderViewHeader) holder).ivMessage.setOnClickListener(v ->
                    buttonInterface.onMessageClick(v, procedureDealBean.getSj()));
            ((MyHolderViewHeader) holder).ivComment.setOnClickListener(v ->
                    buttonInterface.onCommentClick(v, procedureDealBean.getLclsid(), procedureDealBean.getId()));
        } else if (holder instanceof MyHolderViewContent) {
            ProcedureDealBean.PlBean.PlxqBean plBean =
                    (ProcedureDealBean.PlBean.PlxqBean) data.get(position);
            ((MyHolderViewContent) holder).tvName.setText(plBean.getCzrmc()+"的评论");
            ((MyHolderViewContent) holder).tvCzms.setText("评论："+plBean.getCzms());
            ((MyHolderViewContent) holder).tvCzsj.setText("评论时间："+plBean.getCzsj());
        } else {
            ProcedureDealBean.PlBean.PlfjBean plfjBean =
                    (ProcedureDealBean.PlBean.PlfjBean) data.get(position);
            if ("0".equals(plfjBean.getTypes())) {//照片
                ((MyHolderViewFooter) holder).ivContent.setImageURL(plfjBean.getLj() + "&app=0");
            }else {
                ((MyHolderViewFooter) holder).ivContent.setImageResource(R.drawable.video);
            }
            ((MyHolderViewFooter) holder).ivContent.setOnClickListener(v ->
                    buttonInterface.onImgClick(v, plfjBean.getLj() + "&app=0", plfjBean.getTypes()));
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (data.get(position) instanceof ProcedureDealBean.LcxqBean) {
            return ITEM_HEADER;
        } else if (data.get(position) instanceof ProcedureDealBean.PlBean.PlxqBean) {
            return ITEM_CONTENT;
        } else if (data.get(position) instanceof ProcedureDealBean.PlBean.PlfjBean) {
            return ITEM_FOOTER;
        }
        return ITEM_CONTENT;

    }

    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        if (headerView != null) {
            count++;
        }
        return count;
    }


    class MyHolderViewHeader extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_jd)
        TextView tvJd;
        @BindView(R.id.tv_czms)
        TextView tvCzms;
        @BindView(R.id.iv_comment)
        ImageView ivComment;
        @BindView(R.id.tv_fbr)
        TextView tvFbr;
        @BindView(R.id.iv_call)
        ImageView ivCall;
        @BindView(R.id.tv_fbsj)
        TextView tvFbsj;
        @BindView(R.id.iv_message)
        ImageView ivMessage;

        public MyHolderViewHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MyHolderViewContent extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_czms)
        TextView tvCzms;
        @BindView(R.id.tv_czsj)
        TextView tvCzsj;

        public MyHolderViewContent(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MyHolderViewFooter extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_content)
        MyImageViewUtil ivContent;

        public MyHolderViewFooter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
