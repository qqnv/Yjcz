package com.zzlecheng.yjcz.net;

import com.zzlecheng.yjcz.base.BaseBean;
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

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @类名: APIService
 * @描述: 所有接口
 * @作者: huangchao
 * @时间: 2018/8/20 下午4:31
 * @版本: 1.0.0
 */
public interface APIService {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @return
     */
    @FormUrlEncoded
    @POST("loginAndroid")
    Observable<BaseBean<LoginBean>> login(@Field("username") String username,
                                          @Field("password") String password,
                                          @Field("channelid") String channelId,
                                          @Field("app") String app);

    /**
     * 修改用户密码
     *
     * @param id
     * @param mm
     * @return
     */
    @FormUrlEncoded
    @POST("user/updatepassword")
    Observable<BaseBean<CommonBean>> changeWord(@Field("id") String id,
                                                @Field("mm") String mm,
                                                @Field("app") String app);

    /**
     * 检查版本更新
     *
     * @param numbers 版本号
     * @param types   手机系统（Android/IOS）
     * @return
     */
    @FormUrlEncoded
    @POST("version/appGetVersion")
    Observable<BaseBean<VersionUpdateBean>> versionUpdate(@Field("numbers") String numbers,
                                                          @Field("types") String types,
                                                          @Field("app") String app);

    /**
     * 获取消息
     *
     * @param userid
     * @param sfyd
     * @return
     */
    @FormUrlEncoded
    @POST("xxzx/getList")
    Observable<BaseBean<List<MessageBean>>> getMessage(@Field("userid") String userid,
                                                       @Field("sfyd") String sfyd,
                                                       @Field("app") String app);

    /**
     * 将未读消息改为已读
     *
     * @param id
     * @param sfyd
     * @return
     */
    @FormUrlEncoded
    @POST("xxzx/updateMsg")
    Observable<BaseBean<CommonBean>> commitMessage(@Field("id") String id,
                                                   @Field("sfyd") String sfyd,
                                                   @Field("app") String app);

    /**
     * 报班
     *
     * @param departmentid 报班人员部门ID
     * @param types        报班类型
     * @param userid       报班人员ID
     * @return
     */
    @FormUrlEncoded
    @POST("work/insertWork")
    Observable<BaseBean<CommonBean>> report(@Field("departmentid") String departmentid,
                                            @Field("types") String types,
                                            @Field("userid") String userid,
                                            @Field("app") String app);

    /**
     * 退班
     *
     * @param departmentid 退班人员部门ID
     * @param userid       退班人员ID
     * @return
     */
    @FormUrlEncoded
    @POST("work/deleteWork")
    Observable<BaseBean<UnReportBean>> unReport(@Field("departmentid") String departmentid,
                                                @Field("userid") String userid,
                                                @Field("app") String app);

    /**
     * 今日值班列表（摸个人的值班情况）
     *
     * @param userid
     * @return
     */
    @FormUrlEncoded
    @POST("work/getTodayWork")
    Observable<BaseBean<List<ReportTodayBean>>> reportToday(@Field("userid") String userid,
                                                            @Field("app") String app);

    /**
     * 某时间段内值班列表
     *
     * @param begindate 开始时间
     * @param enddate   结束时间
     * @return
     */
    @FormUrlEncoded
    @POST("work/getWorkPage")
    Observable<BaseBean<ReportSomeDayBean>> reportSomeDay(@Field("begindate") String begindate,
                                                          @Field("enddate") String enddate,
                                                          @Field("app") String app);

    /**
     * 报班类型查询
     *
     * @return
     */
    @FormUrlEncoded
    @POST("work/getWorkZZJG")
    Observable<BaseBean<ReportTypesBean>> reportTypes(@Field("app") String app);

    /**
     * 事件上报位置查询
     *
     * @param sjdm
     * @return
     */
    @FormUrlEncoded
    @POST("zd/getZdList")
    Observable<BaseBean<List<LocationBean>>> location(@Field("sjdm") String sjdm,
                                                      @Field("app") String app);

    /**
     * 事件上报位置查询
     *
     * @param zddm
     * @return
     */
    @FormUrlEncoded
    @POST("zd/getZdYaqyTreeList")
    Observable<BaseBean<List<LocationTreeBean>>> locationTree(@Field("zddm") String zddm,
                                                              @Field("app") String app);

    /**
     * 事件上报
     *
     * @param id           该事件的唯一ID，由app端生成
     * @param reportdepart 上报人员部门
     * @param reportorid   上报人员ID
     * @param occurtime    发生时间
     * @param reporttime   上报时间
     * @param eventarea    上报位置
     * @param position     具体位置
     * @param description  描述
     * @return
     */
    @FormUrlEncoded
    @POST("event_api/insertEvent")
    Observable<BaseBean<CommonBean>> reportEvent(@Field("id") String id,
                                                 @Field("reportdepart") String reportdepart,
                                                 @Field("reportorid") String reportorid,
                                                 @Field("occurtime") String occurtime,
                                                 @Field("reporttime") String reporttime,
                                                 @Field("eventarea") String eventarea,
                                                 @Field("position") String position,
                                                 @Field("description") String description,
                                                 @Field("app") String app);

    /**
     * 事件上报照片
     *
     * @param
     * @return
     */
    @Multipart
    @POST("upload/upload")
    Observable<BaseBean<CommonBean>> reportPicture(@Part("pid") String pid,
                                                   @Part("types") String types,
                                                   @Part("app") String app,
                                                   @Part MultipartBody.Part file);

    /**
     * 事件上报照片成功后加载照片
     *
     * @param pid
     * @return
     */
    @FormUrlEncoded
    @POST("upload/getFj")
    Observable<BaseBean<List<ReportPictureBean>>> postReportPicture(@Field("pid") String pid,
                                                                    @Field("types") String types,
                                                                    @Field("app") String app);

    /**
     * 事件上报照片成功后加载视频
     *
     * @param pid
     * @return
     */
    @FormUrlEncoded
    @POST("upload/getFj")
    Observable<BaseBean<List<ReportVideoBean>>> postReportVideo(@Field("pid") String pid,
                                                                @Field("types") String types,
                                                                @Field("app") String app);

    /**
     * 事件删除照片
     *
     * @param lj 图片路径
     * @return
     */
    @FormUrlEncoded
    @POST("upload/deleteupload")
    Observable<BaseBean<CommonBean>> deletePicture(@Field("lj") String lj,
                                                   @Field("app") String app);

    /**
     * 流程图数据
     *
     * @param lcid
     * @return
     */
    @FormUrlEncoded
    @POST("nodes/getLcxqList")
    Observable<BaseBean<List<ProcedureBean>>> getProcedure(@Field("lcid") String lcid,
                                                           @Field("lclsid") String lclsid,
                                                           @Field("zzjgbm") String zzjgbm,
                                                           @Field("app") String app);

    /**
     * 需要操作的流程图数据
     *
     * @param lcid
     * @param lclsid
     * @param zzjgbm
     * @param app
     * @return
     */
    @FormUrlEncoded
    @POST("nodes/getMyNodeList")
    Observable<BaseBean<List<ProcedureBean>>> getMyProcedure(@Field("lcid") String lcid,
                                                             @Field("lclsid") String lclsid,
                                                             @Field("zzjgbm") String zzjgbm,
                                                             @Field("app") String app);

    /**
     * 应急处置列表
     *
     * @param lczt
     * @param app
     * @return
     */
    @FormUrlEncoded
    @POST("lcls/getList")
    Observable<BaseBean<List<EmergencyBean>>> getEmergency(@Field("lczt") String lczt,
                                                           @Field("app") String app);

    /**
     * 处理流程展示
     *
     * @param app
     * @return
     */
    @FormUrlEncoded
    @POST("lcjdls/getLcjdls")
    Observable<BaseBean<List<ProcedureDealBean>>> getProdureDeal(@Field("lclsid") String lclsid,
                                                                 @Field("app") String app);


    /**
     * 评论提交
     *
     * @param app
     * @return
     */
    @FormUrlEncoded
    @POST("lclspl/insert")
    Observable<BaseBean<CommonBean>> commitComment(@Field("lcid") String lcid,
                                                   @Field("id") String id,
                                                   @Field("lclsid") String lclsid,
                                                   @Field("jdlsid") String jdlsid,
                                                   @Field("czms") String czms,
                                                   @Field("czr") String czr,
                                                   @Field("czsj") String czsj,
                                                   @Field("app") String app);

    /**
     * 流程确认或结束或操作
     *
     * @param lclsid
     * @param jdid
     * @param czjg
     * @param jdname
     * @return
     */
    @FormUrlEncoded
    @POST("lcjdls/insert")
    Observable<BaseBean<CommonBean>> commitProcedure(@Field("lcid") String lcid,
                                                     @Field("lclsid") String lclsid,
                                                     @Field("jdid") String jdid,
                                                     @Field("czjg") String czjg,
                                                     @Field("jdname") String jdname,
                                                     @Field("czr") String czr,
                                                     @Field("id") String id,
                                                     @Field("czlx") String czlx,
                                                     @Field("lcmc") String lcmc,
                                                     @Field("czms") String czms,
                                                     @Field("app") String app);


    /**
     * 获取流程节点的操作内容（流程查看）
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("nodes/getNode")
    Observable<BaseBean<ProcedureSeeBean>> getJd(@Field("id") String id,
                                                 @Field("app") String app);

    /**
     * 关键点内容
     *
     * @param lcid
     * @return
     */
    @FormUrlEncoded
    @POST("lc/getKeyPoint")
    Observable<BaseBean<List<GjdBean>>> getGjd(@Field("lcid") String lcid,
                                               @Field("app") String app);

    /**
     * 关键点附件
     *
     * @param pid
     * @param types
     * @return
     */
    @FormUrlEncoded
    @POST("lc/getFjList")
    Observable<BaseBean<GjdfjBean>> getGjdfj(@Field("pid") String pid,
                                             @Field("types") String types,
                                             @Field("app") String app);

    /**
     * 应急聊天室列表
     *
     * @param lclsid
     * @return
     */
    @FormUrlEncoded
    @POST("hy/getList")
    Observable<BaseBean<List<ProcedureChatBean>>> getChatList(@Field("lclsid") String lclsid,
                                                              @Field("app") String app);

    /**
     * 应急聊天室发送文字信息
     *
     * @param lclsid   流程历史ID
     * @param content  文字信息
     * @param soures   app
     * @param types    4
     * @param userid   用户ID
     * @param username 用户姓名
     * @return
     */
    @FormUrlEncoded
    @POST("hy/insertHy")
    Observable<BaseBean<CommonBean>> commitContent(@Field("lclsid") String lclsid,
                                                   @Field("content") String content,
                                                   @Field("soures") String soures,
                                                   @Field("types") String types,
                                                   @Field("userid") String userid,
                                                   @Field("username") String username,
                                                   @Field("app") String app);

    /**
     * 应急聊天发送附件
     *
     * @param lclsid
     * @param soures
     * @param types
     * @param userid
     * @param username
     * @param app
     * @param file
     * @return
     */
    @Multipart
    @POST("hy/HYupload")
    Observable<BaseBean<CommonBean>> commitChatFj(@Part("lclsid") String lclsid,
                                                  @Part("soures") String soures,
                                                  @Part("types") String types,
                                                  @Part("userid") String userid,
                                                  @Part("username") String username,
                                                  @Part("app") String app,
                                                  @Part MultipartBody.Part file);

    /**
     * 应急聊天参与人员
     *
     * @param lcid 流程ID
     * @param app
     * @return
     */
    @FormUrlEncoded
    @POST("user/getLcAllUserForApp")
    Observable<BaseBean<List<PopBean>>> getPop(@Field("lcid") String lcid,
                                               @Field("app") String app);

    /**
     * 集群调度通讯录
     *
     * @param app
     * @return
     */
    @FormUrlEncoded
    @POST("user/getZzjgForTreeList")
    Observable<BaseBean<List<ClusterMailListBean>>> getClusterMainList(@Field("app") String app);

}
