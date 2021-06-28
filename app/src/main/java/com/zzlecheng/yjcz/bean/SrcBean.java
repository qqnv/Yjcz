package com.zzlecheng.yjcz.bean;

/**
 * @类名: SrcBean
 * @描述: 照片上传成功后返回数据
 * @作者: huangchao
 * @时间: 2018/11/7 5:12 PM
 * @版本: 1.0.0
 */
public class SrcBean {

    /**
     * uid : 1d4ffd0930b749bea8722948dec1cc6e
     * path : http://192.168.100.120:10086/movies2018\11\07\20181107171115385.jpg
     */

    private String uid;
    private String path;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "SrcBean{" +
                "uid='" + uid + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
