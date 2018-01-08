package com.yujian.wq.spider;

import com.geccocrawler.gecco.annotation.*;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

import java.util.List;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/8
 */
@Gecco(matchUrl = "https://www.nvshens.com/g/{id}/{currPage}", pipelines = {"NvshensImgDownPipeline"})
public class NvshensImgSpider implements HtmlBean {

    @Request
    private HttpRequest request;

    @RequestParameter
    private int currPage;

    @Image
    @HtmlField(cssPath = "img")
    private List<String> pics;

    @Href
    @HtmlField(cssPath = "#pages > a:last-child ")
    private String nextPageUrl;


    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }
}
