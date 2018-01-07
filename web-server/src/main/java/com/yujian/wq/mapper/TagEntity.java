package com.yujian.wq.mapper;

import java.io.Serializable;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/7
 */
public class TagEntity  implements Serializable {

    private int imgId;
    private String tag;


    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
