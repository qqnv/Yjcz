

package com.zzlecheng.yjcz.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.service.DownloadService;
import com.vector.update_app.utils.AppUpdateUtils;
import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.activity.AboutUsActivity;
import com.zzlecheng.yjcz.activity.ChangePassWordActivity;
import com.zzlecheng.yjcz.activity.MessageActivity;
import com.zzlecheng.yjcz.activity.PersonalActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.base.Commons;
import com.zzlecheng.yjcz.bean.VersionUpdateBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.CProgressDialogUtils;
import com.zzlecheng.yjcz.utils.HProgressDialogUtils;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.OkGoUpdateHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @类名: FirstFragment
 * @描述:
 * @作者: huangchao
 * @时间: 2018/8/21 上午9:17
 * @版本: 1.0.0
 */
public class SecondFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.iv_portrait)
    ImageView ivPortrait;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.ll_first)
    RelativeLayout llFirst;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.rl_message)
    RelativeLayout rlMessage;
    @BindView(R.id.iv_changePwd)
    ImageView ivChangePwd;
    @BindView(R.id.tv_changePwd)
    TextView tvChangePwd;
    @BindView(R.id.rl_changePwd)
    RelativeLayout rlChangePwd;
    @BindView(R.id.iv_versionUpdate)
    ImageView ivVersionUpdate;
    @BindView(R.id.tv_versionUpdate)
    TextView tvVersionUpdate;
    @BindView(R.id.rl_versionUpdate)
    RelativeLayout rlVersionUpdate;
    @BindView(R.id.iv_aboutUs)
    ImageView ivAboutUs;
    @BindView(R.id.tv_aboutUs)
    TextView tvAboutUs;
    @BindView(R.id.rl_aboutUs)
    RelativeLayout rlAboutUs;
    @BindView(R.id.ll_second)
    LinearLayout llSecond;
    Unbinder unbinder;

    private String numbers;//当前版本号
    private String types;//APP类型（Android/IOS）

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    /**
     * 初始化
     */
    private void initView() {

        numbers = AppUpdateUtils.getVersionName(getContext());
        types = "Android";

        llFirst.setOnClickListener(this);
        rlMessage.setOnClickListener(this);
        rlChangePwd.setOnClickListener(this);
        rlVersionUpdate.setOnClickListener(this);
        rlAboutUs.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_first:
                startActivity(new Intent(getContext(), PersonalActivity.class));
                return;
            case R.id.rl_message:
                startActivity(new Intent(getContext(), MessageActivity.class));
                return;
            case R.id.rl_changePwd:
                startActivity(new Intent(getContext(), ChangePassWordActivity.class));
                return;
            case R.id.rl_versionUpdate:
                versionUpdate();
                return;
            case R.id.rl_aboutUs:
                startActivity(new Intent(getContext(), AboutUsActivity.class));
                return;
        }
    }

    private void versionUpdate() {
        HttpMethods.getHttpMethods().versionUpdate(getContext(), numbers, types,"0", new BaseObserver<VersionUpdateBean>(getContext(), true) {
            @Override
            protected void onHandleSuccess(VersionUpdateBean versionUpdateBean) {
                LogUtils.e("更新信息" + versionUpdateBean.toString());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LogUtils.e(e.toString());
            }
        });
    }

    /**
     * 检查版本更新，自定义接口协议+自定义对话框+显示进度对话框
     */
    private void versionUpdate1() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Map<String, String> params = new HashMap<String, String>();

//        params.put("appKey", "ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f");
//        params.put("appVersion", AppUpdateUtils.getVersionName(getContext()));
        params.put("numbers", AppUpdateUtils.getVersionName(getContext()));
        params.put("types", "Android");
        LogUtils.e(Commons.APP_DOWN + params);
        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(getActivity())
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new OkGoUpdateHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(Commons.APP_DOWN)
                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(true)
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
//                .setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")
                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
//                            updateAppBean
//                                    //（必须）是否更新Yes,No
//                                    .setUpdate(jsonObject.optString("update"))
//                                    //（必须）新版本号，
//                                    .setNewVersion(jsonObject.optString("new_version"))
//                                    //（必须）下载地址
//                                    .setApkFileUrl(jsonObject.optString("apk_file_url"))
//                                    //（必须）更新内容
//                                    .setUpdateLog(jsonObject.optString("update_log"))
//                                    //大小，不设置不显示大小，可以不设置
//                                    .setTargetSize(jsonObject.optString("target_size"))
//                                    //是否强制更新，可以不设置
//                                    .setConstraint(false)
//                                    //设置md5，可以不设置
//                                    .setNewMd5(jsonObject.optString("new_md5"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    /**
                     * 有新版本
                     *
                     * @param updateApp        新版本信息
                     * @param updateAppManager app更新管理器
                     */
                    @Override
                    public void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        //自定义对话框
                        showDiyDialog(updateApp, updateAppManager);
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                        CProgressDialogUtils.showProgressDialog(getActivity());
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                        CProgressDialogUtils.cancelProgressDialog(getActivity());
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp(String error) {
                        Toast.makeText(getContext(), "没有新版本", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 自定义对话框
     *
     * @param updateApp
     * @param updateAppManager
     */
    private void showDiyDialog(final UpdateAppBean updateApp, final UpdateAppManager updateAppManager) {
        String targetSize = updateApp.getTargetSize();
        String updateLog = updateApp.getUpdateLog();

        String msg = "";

        if (!TextUtils.isEmpty(targetSize)) {
            msg = "新版本大小：" + targetSize + "\n\n";
        }

        if (!TextUtils.isEmpty(updateLog)) {
            msg += updateLog;
        }

        new AlertDialog.Builder(getContext())
                .setTitle(String.format("是否升级到%s版本？", updateApp.getNewVersion()))
                .setMessage(msg)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateAppManager.download(new DownloadService.DownloadCallback() {
                            @Override
                            public void onStart() {
                                HProgressDialogUtils.showHorizontalProgressDialog(getActivity(), "下载进度", false);
                            }

                            /**
                             * 进度
                             *
                             * @param progress  进度 0.00 -1.00 ，总大小
                             * @param totalSize 总大小 单位B
                             */
                            @Override
                            public void onProgress(float progress, long totalSize) {
                                HProgressDialogUtils.setProgress(Math.round(progress * 100));
                            }

                            /**
                             *
                             * @param total 总大小 单位B
                             */
                            @Override
                            public void setMax(long total) {

                            }


                            @Override
                            public boolean onFinish(File file) {
                                HProgressDialogUtils.cancel();
                                return true;
                            }

                            @Override
                            public void onError(String msg) {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                HProgressDialogUtils.cancel();

                            }

                            @Override
                            public boolean onInstallAppAndAppOnForeground(File file) {
                                return false;
                            }
                        });

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

}

