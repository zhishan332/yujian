//package com.yujian.wq.spider.wwwnvshenscom;
//
//import com.geccocrawler.gecco.GeccoEngine;
//import com.geccocrawler.gecco.annotation.*;
//import com.geccocrawler.gecco.request.HttpRequest;
//import com.geccocrawler.gecco.spider.HtmlBean;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
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
//@Gecco(matchUrl="https://www.nvshens.com/girl/{id}/album/", pipelines={"consolePipeline","NvshensPipeline"})
//public class NvshensAlbumSpider implements HtmlBean {
//
//    @RequestParameter("id")
//    private String id;
//
//    @Request
//    private HttpRequest request;
//
//
//    @Href
//    @HtmlField(cssPath = "div.igalleryli_div > a")
//    private List<String> urls;
//
//
//    public List<String> getUrls() {
//        return urls;
//    }
//
//    public void setUrls(List<String> urls) {
//        this.urls = urls;
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
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public static void main(String[] args) {
//        GeccoEngine.create()
//                .classpath("com.yujian.wq.spider.wwwnvshenscom")
//                .start("https://www.nvshens.com/girl/22878/album/")
//                .thread(2)
//                .interval(300)
//                .loop(false)
//                .mobile(false)
//                .start();
//        System.out.println("start fetching.....");
//    }
//}
