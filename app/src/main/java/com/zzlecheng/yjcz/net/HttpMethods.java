package com.zzlecheng.yjcz.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzlecheng.yjcz.MyApplication;
import com.zzlecheng.yjcz.base.BaseBean;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.base.RxSchedulers;
import com.zzlecheng.yjcz.bean.ClusterMailListBean;
import com.zzlecheng.yjcz.bean.CommonBean;
import com.zzlecheng.yjcz.bean.EmergencyBean;
import com.zzlecheng.yjcz.bean.GjdBean;
import com.zzlecheng.yjcz.bean.GjdfjBean;
import com.zzlecheng.yjcz.bean.LocationTreeBean;
import com.zzlecheng.yjcz.bean.MessageBean;
import com.zzlecheng.yjcz.bean.PopBean;
import com.zzlecheng.yjcz.bean.ProcedureChatBean;
import com.zzlecheng.yjcz.bean.ProcedureSeeBean;
import com.zzlecheng.yjcz.bean.LocationBean;
import com.zzlecheng.yjcz.bean.LoginBean;
import com.zzlecheng.yjcz.bean.ProcedureBean;
import com.zzlecheng.yjcz.bean.ProcedureDealBean;
import com.zzlecheng.yjcz.bean.ReportPictureBean;
import com.zzlecheng.yjcz.bean.ReportSomeDayBean;
import com.zzlecheng.yjcz.bean.ReportTodayBean;
import com.zzlecheng.yjcz.bean.ReportTypesBean;
import com.zzlecheng.yjcz.bean.ReportVideoBean;
import com.zzlecheng.yjcz.bean.UnReportBean;
import com.zzlecheng.yjcz.bean.VersionUpdateBean;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.OkHttpClientUtil;
import com.zzlecheng.yjcz.utils.ResultDeserializerUtil;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @类名: HttpMethods
 * @描述:
 * @作者: huangchao
 * @时间: 2018/8/20 下午4:37
 * @版本: 1.0.0
 */
public class HttpMethods {

    private static HttpMethods httpMethods;

    private static APIService apiService;

    private HttpMethods() {

        //自定义Gson解析模式
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(BaseBean.class, new ResultDeserializerUtil())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .client(OkHttpClientUtil.getUnsafeOkHttpClient(MyApplication.getInstance()))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(SharedPreferenceUtils.getInstance().getString("url"))
                .build();
        apiService = retrofit.create(APIService.class);
    }

    public static HttpMethods getHttpMethods() {
        if (SharedPreferenceUtils.getInstance().getBoolean("changed")) {
            httpMethods = new HttpMethods();
        } else if (httpMethods == null && !SharedPreferenceUtils.getInstance().getBoolean("changed")) {
            httpMethods = new HttpMethods();
        }
        return httpMethods;
    }

    /**
     * 登陆
     *
     * @param context
     * @param username
     * @param password
     * @param baseObserver
     */
    public void login(Context context, String username, String password, String channelId, String app,
                      BaseObserver<LoginBean> baseObserver) {
        apiService.login(username, password, channelId, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 修改用户密码
     *
     * @param context
     * @param id
     * @param mm
     * @param baseObserver
     */
    public void changeWord(Context context, String id, String mm, String app,
                           BaseObserver<CommonBean> baseObserver) {
        apiService.changeWord(id, mm, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 版本升级
     *
     * @param context
     * @param numbers
     * @param types
     * @param baseObserver
     */
    public void versionUpdate(Context context, String numbers, String types, String app,
                              BaseObserver<VersionUpdateBean> baseObserver) {
        apiService.versionUpdate(numbers, types, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 获取消息
     *
     * @param context
     * @param userid
     * @param sfyd
     * @param baseObserver
     */
    public void getMessage(Context context, String userid, String sfyd, String app,
                           BaseObserver<List<MessageBean>> baseObserver) {
        apiService.getMessage(userid, sfyd, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 将未读消息改为已读
     *
     * @param context
     * @param id
     * @param sfyd
     * @param baseObserver
     */
    public void commitMessage(Context context, String id, String sfyd, String app,
                              BaseObserver<CommonBean> baseObserver) {
        apiService.commitMessage(id, sfyd, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 报班
     *
     * @param context
     * @param departmentid 报班人员部门ID
     * @param types        报班类型
     * @param userid       报班人员ID
     * @param baseObserver
     */
    public void report(Context context, String departmentid, String types, String userid, String app,
                       BaseObserver<CommonBean> baseObserver) {
        apiService.report(departmentid, types, userid, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 退班
     *
     * @param context
     * @param departmentid 退班人员部门ID
     * @param userid       退班人员ID
     * @param baseObserver
     */
    public void unReport(Context context, String departmentid, String userid, String app,
                         BaseObserver<UnReportBean> baseObserver) {
        apiService.unReport(departmentid, userid, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 今日值班列表
     *
     * @param context
     * @param baseObserver
     */
    public void reportToday(Context context, String userid, String app,
                            BaseObserver<List<ReportTodayBean>> baseObserver) {
        apiService.reportToday(userid, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 某时间段内值班列表
     *
     * @param context
     * @param begindate    开始时间
     * @param enddate      结束时间
     * @param baseObserver
     */
    public void reportSomeDay(Context context, String begindate, String enddate, String app,
                              BaseObserver<ReportSomeDayBean> baseObserver) {
        apiService.reportSomeDay(begindate, enddate, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 报班类型查询
     *
     * @param context
     * @param baseObserver
     */
    public void reportTypes(Context context, String app, BaseObserver<ReportTypesBean> baseObserver) {
        apiService.reportTypes(app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 上报位置查询
     *
     * @param context
     * @param sjdm
     * @param baseObserver
     */
    public void location(Context context, String sjdm, String app,
                         BaseObserver<List<LocationBean>> baseObserver) {
        apiService.location(sjdm, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 上报位置树查询
     *
     * @param context
     * @param zddm
     * @param baseObserver
     */
    public void locationTree(Context context, String zddm, String app,
                             BaseObserver<List<LocationTreeBean>> baseObserver) {
        apiService.locationTree(zddm, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 事件上报
     *
     * @param context
     * @param id
     * @param reportdepart
     * @param reportorid
     * @param occurtime
     * @param reporttime
     * @param eventarea
     * @param position
     * @param description
     * @param baseObserver
     */
    public void reportEvent(Context context, String id, String reportdepart,
                            String reportorid, String occurtime, String reporttime,
                            String eventarea, String position, String description, String app,
                            BaseObserver<CommonBean> baseObserver) {
        apiService.reportEvent(id, reportdepart, reportorid, occurtime,
                reporttime, eventarea, position, description, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 上传照片
     *
     * @param context
     * @param pid
     * @param file
     * @param baseObserver
     */
    public void reportPicture(Context context, String pid, String types, String app, File file,
                              BaseObserver<CommonBean> baseObserver) {
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
        apiService.reportPicture(pid, types, app, part)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 加载照片
     *
     * @param context
     * @param pid
     * @param baseObserver
     */
    public void postReportPicture(Context context, String pid, String types, String app,
                                  BaseObserver<List<ReportPictureBean>> baseObserver) {
        apiService.postReportPicture(pid, types, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 加载视频
     *
     * @param context
     * @param pid
     * @param types
     * @param app
     * @param baseObserver
     */
    public void postReportVideo(Context context, String pid, String types, String app,
                                BaseObserver<List<ReportVideoBean>> baseObserver) {
        apiService.postReportVideo(pid, types, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 事件删除照片
     *
     * @param context
     * @param lj           图片路径
     * @param baseObserver
     */
    public void deletePicture(Context context, String lj, String app,
                              BaseObserver<CommonBean> baseObserver) {
        apiService.deletePicture(lj, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 加载流程图数据
     *
     * @param context
     * @param lcid
     * @param baseObserver
     */
    public void getProcedure(Context context, String lcid, String lclsid, String zzjgbm, String app,
                             BaseObserver<List<ProcedureBean>> baseObserver) {
        apiService.getProcedure(lcid, lclsid, zzjgbm, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 需要操作的流程图数据
     *
     * @param context
     * @param lcid
     * @param lclsid
     * @param zzjgbm
     * @param app
     * @param baseObserver
     */
    public void getMyProcedure(Context context, String lcid, String lclsid, String zzjgbm, String app,
                               BaseObserver<List<ProcedureBean>> baseObserver) {
        apiService.getMyProcedure(lcid, lclsid, zzjgbm, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 应急处置数据
     *
     * @param context
     * @param lczt
     * @param app
     * @param baseObserver
     */
    public void getEmergency(Context context, String lczt, String app,
                             BaseObserver<List<EmergencyBean>> baseObserver) {
        apiService.getEmergency(lczt, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 加载流程详情数据
     *
     * @param context
     * @param app
     * @param baseObserver
     */
    public void getProcedureDeal(Context context, String lclsid, String app,
                                 BaseObserver<List<ProcedureDealBean>> baseObserver) {
        apiService.getProdureDeal(lclsid, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 评论提交
     *
     * @param context
     * @param id
     * @param lclsid
     * @param jdlsid
     * @param czms
     * @param czr
     * @param czsj
     * @param app
     * @param baseObserver
     */
    public void commitComment(Context context, String lcid, String id, String lclsid, String jdlsid, String czms,
                              String czr, String czsj, String app,
                              BaseObserver<CommonBean> baseObserver) {
        apiService.commitComment(lcid, id, lclsid, jdlsid, czms, czr, czsj, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 流程确认或结束或操作
     *
     * @param context
     * @param lclsid
     * @param jdid
     * @param czjg
     * @param jdname
     * @param baseObserver
     */
    public void commitProcedure(Context context, String lcid, String lclsid, String jdid, String czjg,
                                String jdname, String czr, String id, String czlx, String lcmc,
                                String czms, String app, BaseObserver<CommonBean> baseObserver) {
        apiService.commitProcedure(lcid, lclsid, jdid, czjg, jdname, czr, id, czlx, lcmc, czms, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }


    /**
     * 获取流程节点的操作内容（流程查看）
     *
     * @param context
     * @param id
     * @param baseObserver
     */
    public void getJd(Context context, String id, String app,
                      BaseObserver<ProcedureSeeBean> baseObserver) {
        apiService.getJd(id, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 关键点内容
     *
     * @param context
     * @param lcid
     * @param baseObserver
     */
    public void getGjd(Context context, String lcid, String app, BaseObserver<List<GjdBean>> baseObserver) {
        apiService.getGjd(lcid, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 关键点附件
     *
     * @param context
     * @param pid
     * @param types
     * @param baseObserver
     */
    public void getGjdfj(Context context, String pid, String types, String app,
                         BaseObserver<GjdfjBean> baseObserver) {
        apiService.getGjdfj(pid, types, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 应急聊天室列表
     *
     * @param context
     * @param lclsid
     * @param app
     * @param baseObserver
     */
    public void getChatList(Context context, String lclsid, String app,
                            BaseObserver<List<ProcedureChatBean>> baseObserver) {
        apiService.getChatList(lclsid, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 应急聊天室发送文字信息
     *
     * @param lclsid   流程历史ID
     * @param content  文字信息
     * @param soures   app
     * @param types    4
     * @param userid   用户ID
     * @param username 用户姓名
     */
    public void commitContent(Context context, String lclsid, String content, String soures,
                              String types, String userid, String username, String app,
                              BaseObserver<CommonBean> baseObserver) {
        apiService.commitContent(lclsid, content, soures, types, userid, username, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 应急聊天发送附件
     *
     * @param context
     * @param lclsid
     * @param soures
     * @param types
     * @param userid
     * @param username
     * @param app
     * @param file
     * @param baseObserver
     */
    public void commitChatFj(Context context, String lclsid, String soures, String types,
                             String userid, String username, String app, File file,
                             BaseObserver<CommonBean> baseObserver) {
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
        apiService.commitChatFj(lclsid, soures, types, userid, username, app, part)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);


    }

    /**
     * 应急聊天参与人员
     *
     * @param context
     * @param lcid
     * @param app
     * @param baseObserver
     */
    public void getPop(Context context, String lcid, String app,
                       BaseObserver<List<PopBean>> baseObserver) {
        apiService.getPop(lcid, app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

    /**
     * 集群调度通讯录
     *
     * @param context
     * @param app
     * @param baseObserver
     */
    public void getClusterMainList(Context context, String app,
                                   BaseObserver<List<ClusterMailListBean>> baseObserver) {
        apiService.getClusterMainList(app)
                .compose(RxSchedulers.compose(context))
                .subscribe(baseObserver);
    }

}
