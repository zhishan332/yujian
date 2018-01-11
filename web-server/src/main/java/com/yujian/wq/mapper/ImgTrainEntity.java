package com.yujian.wq.mapper;

import java.io.Serializable;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/11
 */
public class ImgTrainEntity implements Serializable {

    private int id;
    private String img;
    private int tagId;
    private String tag;

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

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
