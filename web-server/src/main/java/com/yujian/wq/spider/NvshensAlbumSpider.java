package com.yujian.wq.spider;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.Href;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

import java.util.List;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/8
 */
@Gecco(matchUrl="https://www.nvshens.com/girl/{id}/album/", pipelines={"consolePipeline","NvshensPipeline"})
public class NvshensAlbumSpider implements HtmlBean {

    @Request
    private HttpRequest request;

    @Href
    @HtmlField(cssPath = "div.igalleryli_div > a")
    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public static void main(String[] args) {
        GeccoEngine.create()
                .classpath("com.yujian.wq.spider")
                .start("https://www.nvshens.com/girl/20763/album/")
                .thread(8)
                .interval(2000)
                .loop(false)
                .mobile(false)
                .start();
    }
}
