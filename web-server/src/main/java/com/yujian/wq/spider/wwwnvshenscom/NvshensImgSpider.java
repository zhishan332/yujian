//package com.yujian.wq.spider.wwwnvshenscom;
//
//import com.geccocrawler.gecco.annotation.*;
//import com.geccocrawler.gecco.request.HttpRequest;
//import com.geccocrawler.gecco.spider.HtmlBean;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
//* 添加描述
//*
//* @author wangqing
//* @since 2018/1/8
//*/
//@Gecco(matchUrl = "https://www.nvshens.com/g/{id}/{currPage}", pipelines = {"NvshensImgDownPipeline"})
//public class NvshensImgSpider implements HtmlBean {
//    @RequestParameter("id")
//    private String id;
//
//    @Request
//    private HttpRequest request;
//
//    @RequestParameter
//    private int currPage;
//
//    @Image
//    @HtmlField(cssPath = "img")
//    private List<String> pics;
//
//    @Href
//    @HtmlField(cssPath = "#pages > a:last-child ")
//    private String nextPageUrl;
//
//   private Map<String, Integer> numberMap = new HashMap<>();
//
//    public List<String> getPics() {
//        return pics;
//    }
//
//    public void setPics(List<String> pics) {
//        this.pics = pics;
//    }
//
//    public HttpRequest getRequest() {
//        return request;
//    }
//
//    public void setRequest(HttpRequest request) {
//        this.request = request;
//    }
//
//    public String getNextPageUrl() {
//        return nextPageUrl;
//    }
//
//    public void setNextPageUrl(String nextPageUrl) {
//        this.nextPageUrl = nextPageUrl;
//    }
//
//    public int getCurrPage() {
//        return currPage;
//    }
//
//    public void setCurrPage(int currPage) {
//        this.currPage = currPage;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public Map<String, Integer> getNumberMap() {
//        return numberMap;
//    }
//
//    public void setNumberMap(Map<String, Integer> numberMap) {
//        this.numberMap = numberMap;
//    }
//}
