//package com.zzlecheng.yjcz.utils;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.res.Resources;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.zzlecheng.yjcz.R;
//
//public class DialogManager
//{
//    private Context mContext;
//    private Dialog mDialog;
//    private ImageView mIcon;
//    private TextView mText;
//    private ImageView mVoice;
//
//    public DialogManager(Context paramContext)
//    {
//        this.mContext = paramContext;
//    }
//
//    public void dismissDialog()
//    {
//        if ((this.mDialog != null) && (this.mDialog.isShowing()))
//        {
//            this.mDialog.dismiss();
//            this.mDialog = null;
//        }
//    }
//
//    public void recording()
//    {
//        Log.e("开始录音", "开始录音");
//        if ((this.mDialog != null) && (this.mDialog.isShowing()))
//        {
//            this.mIcon.setVisibility(View.VISIBLE);
//            this.mVoice.setVisibility(View.VISIBLE);
//            this.mText.setVisibility(View.VISIBLE);
//            this.mIcon.setImageResource(R.mipmap.txs);
//            this.mText.setText("上滑取消，松开发送");
//        }
//    }
//
//    public void showRecordingDialog()
//    {
//        this.mDialog = new Dialog(this.mContext, 2131623942);
//        View localView = LayoutInflater.from(this.mContext).inflate(2131361826, null);
//        this.mDialog.setContentView(localView);
//        this.mIcon = ((ImageView)localView.findViewById(2131230761));
//        this.mVoice = ((ImageView)localView.findViewById(2131230763));
//        this.mText = ((TextView)localView.findViewById(2131230762));
//        this.mDialog.show();
//    }
//
//    public void tooShort()
//    {
//        if ((this.mDialog != null) && (this.mDialog.isShowing()))
//        {
//            this.mIcon.setVisibility(0);
//            this.mVoice.setVisibility(8);
//            this.mText.setVisibility(0);
//            this.mVoice.setImageResource(2131492883);
//            this.mText.setText("������������������");
//        }
//    }
//
//    public void updateVoiceLevel(int paramInt)
//    {
//        if ((this.mDialog != null) && (this.mDialog.isShowing()))
//        {
//            Resources localResources = this.mContext.getResources();
//            StringBuilder localStringBuilder = new StringBuilder();
//            localStringBuilder.append("v");
//            localStringBuilder.append(paramInt);
//            int i = localResources.getIdentifier(localStringBuilder.toString(), "mipmap", this.mContext.getPackageName());
//            this.mVoice.setImageResource(i);
//        }
//    }
//
//    public void wantToCancel()
//    {
//        if ((this.mDialog != null) && (this.mDialog.isShowing()))
//        {
//            this.mIcon.setVisibility(View.VISIBLE);
//            this.mVoice.setVisibility(View.GONE);
//            this.mText.setVisibility(View.VISIBLE;
//            this.mVoice.setImageResource(R.mipmap.txs);
//            this.mText.setText("松开手指，取消发送");
//        }
//    }
//}
