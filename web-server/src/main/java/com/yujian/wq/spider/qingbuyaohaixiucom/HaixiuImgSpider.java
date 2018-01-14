//package com.yujian.wq.spider.qingbuyaohaixiucom;
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
//@Gecco(matchUrl = "https://qingbuyaohaixiu.com/archives/{id}", pipelines = {"HaiXiuImgDownPipeline"})
//public class HaixiuImgSpider implements HtmlBean {
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
//    @HtmlField(cssPath = ".entry-content img")
//    private List<String> pics;
//
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
