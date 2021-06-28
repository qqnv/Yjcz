package com.zzlecheng.yjcz.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.bean.ProcedureChatBean;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.MyImageViewUtil;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: ProcedureChatAdapter
 * @描述: 应急指挥室
 * @作者: huangchao
 * @时间: 2018/12/28 4:25 PM
 * @版本: 1.0.0
 */
public class ProcedureChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    type 0照片 1视频 3语音 4文字

    private Context context;
    private List<ProcedureChatBean> beans;
    public static final int LEFT_CONTENT_ITEM = 1;//内容在左侧
    public static final int RIGHT_CONTENT_ITEM = 2;//内容在右侧
    public static final int LEFT_IMG_ITEM = 3;//图片在左侧
    public static final int RIGHT_IMG_ITEM = 4;//图片在右侧
    public static final int LEFT_VOICE_ITEM = 5;//语音在左侧
    public static final int RIGHT_VOICE_ITEM = 6;//语音在右侧
    private String userid = SharedPreferenceUtils.getInstance().getString("userid");
    private ButtonInterface buttonInterface;

    public ProcedureChatAdapter(Context context, List<ProcedureChatBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    public interface ButtonInterface {
        void onImgClick(View view, String path, String types);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (LEFT_CONTENT_ITEM == viewType) {//左侧文字
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_left_content, parent, false);
            return new MyViewHolderLeftContent(view);
        } else if (RIGHT_CONTENT_ITEM == viewType) {//右侧文字
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_right_content, parent, false);
            return new MyViewHolderRightContent(view);
        } else if (LEFT_IMG_ITEM == viewType) {//左侧图片
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_left_img, parent, false);
            return new MyViewHolderLeftImg(view);
        } else if (RIGHT_IMG_ITEM == viewType) {//右侧图片
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_right_img, parent, false);
            return new MyViewHolderRightImg(view);
        } else if (LEFT_VOICE_ITEM == viewType) {//左侧语音
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_left_voice, parent, false);
            return new MyViewHolderLeftVoice(view);
        } else if(RIGHT_VOICE_ITEM == viewType){//右侧语音
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_right_voice, parent, false);
            return new MyViewHolderRightVoice(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String title = beans.get(position).getUsername();
        String content = beans.get(position).getContent();
        //0-图片 1-视频 3-音频 4-文字
        String types = beans.get(position).getTypes();
        if (holder instanceof MyViewHolderLeftContent) {//左侧文字(或音频)
            ((MyViewHolderLeftContent) holder).tvLeftContentTitle.setText(title);
            if ("4".equals(types)) {
                ((MyViewHolderLeftContent) holder).leftContentContent.setText(content);
            }
        } else if (holder instanceof MyViewHolderRightContent) {//右侧文字（或音频）
            ((MyViewHolderRightContent) holder).tvRightContentTitle.setText(title);
            if ("4".equals(types)) {
                ((MyViewHolderRightContent) holder).rightContentContent.setText(content);
            }
        } else if (holder instanceof MyViewHolderLeftImg) {//左侧图片（或视频）
            ((MyViewHolderLeftImg) holder).tvLeftSrcTitle.setText(title);
            if ("0".equals(types)) {//图片
                ((MyViewHolderLeftImg) holder).leftSrcContent.setImageURL(content + "&app=0");
            } else {//视频
//                ((MyViewHolderLeftImg) holder).leftSrcContent.setImageBitmap(
//                        CommonUtil.createVideoThumbnail(content + "&app=0", 200, 200));
            }
            ((MyViewHolderLeftImg) holder).leftSrcContent.setOnClickListener(v ->
                    buttonInterface.onImgClick(v, content + "&app=0", types));
        } else if (holder instanceof MyViewHolderRightImg) {//右侧图片（或视频）
            ((MyViewHolderRightImg) holder).tvRightSrcTitle.setText(title);
            if ("0".equals(types)) {//图片
                ((MyViewHolderRightImg) holder).rightSrcContent.setImageURL(content + "&app=0");
            } else {//视频
            }
            ((MyViewHolderRightImg) holder).rightSrcContent.setOnClickListener(v ->
                    buttonInterface.onImgClick(v, content + "&app=0", types));
        } else if (holder instanceof MyViewHolderLeftVoice) {//左侧语音
            ((MyViewHolderLeftVoice) holder).tvLeftVoiceTitle.setText(title);
            ((MyViewHolderLeftVoice) holder).leftVoiceContent.setOnClickListener(v ->
            {
                if ("3".equals(types)) {
                    String url = content + "&app=0";
                    Uri uri = Uri.parse(url);
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(context, uri);
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnPreparedListener(mp -> {
                        mediaPlayer.start();
                        ((MyViewHolderLeftVoice) holder).leftVoiceContent.setText("正在播放···");
                    });
                    mediaPlayer.setOnCompletionListener(mp -> {
                        ((MyViewHolderLeftVoice) holder).leftVoiceContent.setText("点击播放");
                    });
                }
            });
        } else if (holder instanceof MyViewHolderRightVoice){//右侧语音
            ((MyViewHolderRightVoice)holder).tvRightVoiceTitle.setText(title);
            ((MyViewHolderRightVoice) holder).rightVoiceContent.setOnClickListener(v ->
                    {
                        if ("3".equals(types)) {
                            String url = content + "&app=0";
                            Uri uri = Uri.parse(url);
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource(context, uri);
                                mediaPlayer.prepareAsync();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mediaPlayer.setOnPreparedListener(mp -> {
                                mediaPlayer.start();
                                ((MyViewHolderRightVoice) holder).rightVoiceContent.setText("正在播放···");
                            });
                            mediaPlayer.setOnCompletionListener(mp -> {
                                ((MyViewHolderRightVoice) holder).rightVoiceContent.setText("点击播放");
                            });
                        }
                    }

            );
        }

    }

    @Override
    public int getItemViewType(int position) {
        String types = beans.get(position).getTypes();
        if (userid.equals(beans.get(position).getUserid())) {//右侧
            if ("4".equals(types)) {//右侧文字
                return RIGHT_CONTENT_ITEM;
            } else if ("3".equals(types)) {//右侧语音
                return RIGHT_VOICE_ITEM;
            } else {//右侧图片
                return RIGHT_IMG_ITEM;
            }
        } else {//左侧
            if ("4".equals(types)) {//左侧文字
                return LEFT_CONTENT_ITEM;
            } else if ("3".equals(types)) {//左侧语音
                return LEFT_VOICE_ITEM;
            } else {//左侧图片
                return LEFT_IMG_ITEM;
            }
        }
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    class MyViewHolderLeftContent extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_leftContentTitle)
        TextView tvLeftContentTitle;
        @BindView(R.id.leftContentContent)
        TextView leftContentContent;

        public MyViewHolderLeftContent(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MyViewHolderRightContent extends RecyclerView.ViewHolder {

        @BindView(R.id.rightContentContent)
        TextView rightContentContent;
        @BindView(R.id.tv_rightContentTitle)
        TextView tvRightContentTitle;

        public MyViewHolderRightContent(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MyViewHolderLeftImg extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_leftSrcTitle)
        TextView tvLeftSrcTitle;
        @BindView(R.id.leftSrcContent)
        MyImageViewUtil leftSrcContent;


        public MyViewHolderLeftImg(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MyViewHolderRightImg extends RecyclerView.ViewHolder {

        @BindView(R.id.rightSrcContent)
        MyImageViewUtil rightSrcContent;
        @BindView(R.id.tv_rightSrcTitle)
        TextView tvRightSrcTitle;

        public MyViewHolderRightImg(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MyViewHolderLeftVoice extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_leftVoiceTitle)
        TextView tvLeftVoiceTitle;
        @BindView(R.id.leftVoiceContent)
        TextView leftVoiceContent;

        public MyViewHolderLeftVoice(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MyViewHolderRightVoice extends RecyclerView.ViewHolder {

        @BindView(R.id.rightVoiceContent)
        TextView rightVoiceContent;
        @BindView(R.id.tv_rightVoiceTitle)
        TextView tvRightVoiceTitle;

        public MyViewHolderRightVoice(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
