package com.yujian.wq.mapper;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/7
 */
public class ImgEntity implements Serializable {

    private int id;
    private String img;
    private int folder;
    private String chain;
    private Timestamp createTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }
}
