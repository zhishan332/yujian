//package com.yujian.wq.spider;
//
//import com.geccocrawler.gecco.annotation.PipelineName;
//import com.geccocrawler.gecco.pipeline.Pipeline;
//import com.geccocrawler.gecco.request.HttpRequest;
//import com.geccocrawler.gecco.scheduler.DeriveSchedulerContext;
//import org.apache.commons.io.FileUtils;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpRequestBase;
//import org.apache.http.client.protocol.HttpClientContext;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//
//import java.io.File;
//import java.util.List;
//
///**
// * 添加描述
// *
// * @author wangqing
// * @since 2018/1/8
// */
//@PipelineName("NvshensImgDownPipeline")
//public class NvshensImgDownPipeline implements Pipeline<NvshensImgSpider> {
//
//    String path = "I:\\doc\\tutu\\img\\";
//
//    private CloseableHttpClient httpClient;
//
//    {
//        RequestConfig clientConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
//        PoolingHttpClientConnectionManager syncConnectionManager = new PoolingHttpClientConnectionManager();
//        syncConnectionManager.setMaxTotal(1000);
//        syncConnectionManager.setDefaultMaxPerRoute(50);
//        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(clientConfig).setConnectionManager(syncConnectionManager).build();
//    }
//
//    @Override
//    public void process(NvshensImgSpider bean) {
//
//        List<String> list = bean.getPics();
//        if (list != null) {
//            System.out.println("listImg:" + list);
//
//            for (String imgUrl : list) {
//                HttpRequestBase request = new HttpGet(imgUrl);
//                try {
//                    HttpClientContext context = HttpClientContext.create();
//                    org.apache.http.HttpResponse response = httpClient.execute(request, context);
//                    int suffixIndex = imgUrl.lastIndexOf(".");
//
//                    String suffix = imgUrl.substring(suffixIndex);
//                    FileUtils.copyInputStreamToFile(response.getEntity().getContent(), new File(path + System.nanoTime() + suffix));
//                    //check train
//                    File checkFile = new File(path);
//                    File[] listAry = checkFile.listFiles();
//                    if (listAry != null && listAry.length > 10) return;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    request.releaseConnection();
//                }
//            }
//            String nextUrl = bean.getNextPageUrl();
//            int nextPage = Integer.parseInt(nextUrl.substring(nextUrl.lastIndexOf(".") - 1, nextUrl.lastIndexOf(".")));
//            if (nextPage > bean.getCurrPage()) {
//                HttpRequest currRequest = bean.getRequest();
//                DeriveSchedulerContext.into(currRequest.subRequest(nextUrl));
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        HttpRequestBase request = new HttpGet("https://t1.onvshen.com:85/gallery/20763/25089/s/0.jpg");
//        try {
//            String path = "I:\\doc\\tutu\\img\\";
//            RequestConfig clientConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
//            PoolingHttpClientConnectionManager syncConnectionManager = new PoolingHttpClientConnectionManager();
//            syncConnectionManager.setMaxTotal(1000);
//            syncConnectionManager.setDefaultMaxPerRoute(50);
//            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(clientConfig).setConnectionManager(syncConnectionManager).build();
//            HttpClientContext context = HttpClientContext.create();
//            org.apache.http.HttpResponse response = httpClient.execute(request, context);
//            FileUtils.copyInputStreamToFile(response.getEntity().getContent(), new File(path + System.nanoTime() + ".jpg"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            request.releaseConnection();
//        }
//    }
//}
