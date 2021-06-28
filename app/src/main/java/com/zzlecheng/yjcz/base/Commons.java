package com.zzlecheng.yjcz.base;

/**
 * @类名: Commons
 * @描述: 公共接口
 * @作者: huangchao
 * @时间: 2018/8/20 下午4:05
 * @版本: 1.0.0
 */
public class Commons {

    //    public static final String BASE_URL = "http://192.168.100.184:8080/yjcz/";//天虹接口
//    public static final String BASE_URL = "http://192.168.100.106:7002/yjzzc/";//县委接口
//    public static final String BASE_URL = "http://192.168.100.172:8080/yjcz/";//本辉接口
//    public static final String BASE_URL = "http://192.168.100.113:8080/yjcz/";//陪陪接口
    public static final String BASE_URL = "http://192.168.100.222:8020/yjcz/";//内网接口
    //app更新接口
    //public static String APP_DOWN = "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/json/json.txt";
    public static String APP_DOWN = Commons.BASE_URL + "version/appGetVersion";
    //请求数据成功
    public static String DATA_SUCCESS_CODE = "1";
    //请求超时时间
    public static final long POST_DATA_TIMEOUT = 60;
    //是否需要重新登录
    public static final String HAS_LOGIN = "has_login";
    //推送appKey
    public static final String APP_KEY = "cm8NPHKiHYqjqvp6VLIA8HUx";

    //流程图的几个字段
    //确认
    public static final String NODE_TYPE_SURE = "node_type_sure";
    //查看
    public static final String NODE_TYPE_START = "node_type_start";
    //操作
    public static final String NODE_TYPE_MAKE = "node_type_make";
    //结束
    public static final String NODE_TYPE_END = "node_type_end";

    //集群调度默认设置
    public static final String U_IP = "117.159.21.216";
    public static final int U_DKH = 55555;
    public static final String U_PWD = "123456";
    public static final String U_BITRATE = "1024";
    public static final String U_FPS = "15";
    public static final int U_RESOLUTION = 0;

    //要拨打的账号
    public static final String CALL_NAME = "3002";

}
