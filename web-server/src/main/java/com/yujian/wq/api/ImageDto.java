package com.yujian.wq.api;

import java.io.Serializable;

/**
 * Created by wangqing on 2018/1/9.
 */
public class ImageDto  implements Serializable{

    private String url;
    private String title;
    private String chain;
    private Integer id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
