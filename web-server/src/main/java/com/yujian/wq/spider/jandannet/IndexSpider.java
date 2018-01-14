package com.yujian.wq.spider.jandannet;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.*;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/8
 */
@Gecco(matchUrl = "http://jandan.net/ooxx/page-{id}", pipelines = {"consolePipeline", "ImgDownPipeline"})
public class IndexSpider implements HtmlBean {

    @RequestParameter("id")
    private String id;

    @Request
    private HttpRequest request;

    @Image
    @HtmlField(cssPath = ".commentlist  li img")
    private List<String> pics;

    private Map<String, Integer> numberMap = new HashMap<>();

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public Map<String, Integer> getNumberMap() {
        return numberMap;
    }

    public void setNumberMap(Map<String, Integer> numberMap) {
        this.numberMap = numberMap;
    }

    public static void main(String[] args) {
        GeccoEngine.create()
                .classpath("com.yujian.wq.spider.jandannet")
                .start("http://jandan.net/ooxx/page-100")
                .thread(1)
                .interval(300)
                .loop(false)
                .mobile(false)
                .start();
        System.out.println("start fetching.....");
    }
}
