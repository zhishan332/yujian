package com.yujian.wq.mapper;

import java.io.Serializable;

/**
 * Created by wangqing on 2018/1/15.
 */
public class ImgSpiderEntity implements Serializable {

    private String img;
    private String title;
    private String chain;
    private String md5;
    private Integer status;


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
