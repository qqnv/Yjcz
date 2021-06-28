package com.zzlecheng.yjcz.base;

import java.io.Serializable;

/**
 * @类名: BaseBean
 * @描述: 请求返回基类
 * @作者: huangchao
 * @时间: 2018/8/20 下午3:59
 * @版本: 1.0.0
 */
public class BaseBean<T> implements Serializable{

    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
