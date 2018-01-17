package com.yujian.wq.api;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/7
 */
public class ImgChainDto implements Serializable {

    private String img;
    private String chain;
    private String title;
    private Integer num;

    private List<ImgChainDto> chainList;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public List<ImgChainDto> getChainList() {
        return chainList;
    }

    public void setChainList(List<ImgChainDto> chainList) {
        this.chainList = chainList;
    }
}
