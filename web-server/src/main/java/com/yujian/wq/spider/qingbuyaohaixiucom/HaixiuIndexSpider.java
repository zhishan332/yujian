//package com.yujian.wq.spider.qingbuyaohaixiucom;
//
//import com.geccocrawler.gecco.GeccoEngine;
//import com.geccocrawler.gecco.annotation.*;
//import com.geccocrawler.gecco.pipeline.Pipeline;
//import com.geccocrawler.gecco.request.HttpRequest;
//import com.geccocrawler.gecco.scheduler.DeriveSchedulerContext;
//import com.geccocrawler.gecco.spider.HtmlBean;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 添加描述
// *
// * @author wangqing
// * @since 2018/1/8
// */
//@Gecco(matchUrl = "https://qingbuyaohaixiu.com/archives/category/{id}", pipelines = {"consolePipeline"})
//public class HaixiuIndexSpider implements HtmlBean, Pipeline<HaixiuIndexSpider> {
//
//    @RequestParameter("id")
//    private String id;
//
//    @Request
//    private HttpRequest request;
//
//    @Href
//    @HtmlField(cssPath = "div.featured-image > a")
//    private List<String> urls;
//
//    public List<String> getUrls() {
//        return urls;
//    }
//
//    public void setUrls(List<String> urls) {
//        this.urls = urls;
//    }
//
//    private Map<String, Integer> numberMap = new HashMap<>();
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
//    public Map<String, Integer> getNumberMap() {
//        return numberMap;
//    }
//
//    public void setNumberMap(Map<String, Integer> numberMap) {
//        this.numberMap = numberMap;
//    }
//    @Override
//    public void process(HaixiuIndexSpider bean) {
//        List<String> list = bean.getUrls();
//
//        if (list != null) {
//            System.out.println("list:" + list);
//            for(String url :list){
//                HttpRequest currRequest = bean.getRequest();
//                DeriveSchedulerContext.into(currRequest.subRequest(url));
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        GeccoEngine.create()
//                .classpath("com.yujian.wq.spider.qingbuyaohaixiucom")
//                .start("https://qingbuyaohaixiu.com/archives/category/美腿")
//                .thread(1)
//                .interval(300)
//                .loop(false)
//                .mobile(false)
//                .start();
//        System.out.println("start fetching.....");
//    }
//
//
//}
