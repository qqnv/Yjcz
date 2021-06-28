package com.zzlecheng.yjcz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.bean.ReportPictureBean;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.MyImageViewUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: ImageAdapter
 * @描述: 照片adapter
 * @作者: huangchao
 * @时间: 2018/11/25 4:22 PM
 * @版本: 1.0.0
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private List<ReportPictureBean> pictureBeans;
    private ButtonInterface buttonInterface;

    public ImageAdapter(Context context, List<ReportPictureBean> pictureBeans) {
        this.context = context;
        this.pictureBeans = pictureBeans;
    }

    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    public interface ButtonInterface {
        void onClick(View view, String path);

        void onDeleteClick(View view, String path);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (pictureBeans.size()<5){
            ReportPictureBean bean = pictureBeans.get(position);
            holder.ivContent.setImageURL(bean.getLj()+"&app=0");
            holder.ivContent.setOnClickListener(v -> buttonInterface.onClick(v, bean.getLj()+"&app=0"));
            holder.ivDelete.setOnClickListener(v -> buttonInterface.onDeleteClick(v, bean.getLj()+"&app=0"));
        }
    }

    @Override
    public int getItemCount() {
        int size;
        if (pictureBeans.size() < 5){
            size = pictureBeans.size();
        }else {
            size = 4;
        }
        return size;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_content)
        MyImageViewUtil ivContent;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

