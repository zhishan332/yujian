//package com.yujian.wq.spider.qingbuyaohaixiucom;
//
//import com.geccocrawler.gecco.annotation.PipelineName;
//import com.geccocrawler.gecco.pipeline.Pipeline;
//import com.geccocrawler.gecco.request.HttpRequest;
//import com.geccocrawler.gecco.scheduler.DeriveSchedulerContext;
//import com.yujian.wq.spider.jandannet.IndexSpider;
//import org.apache.commons.io.FileUtils;
//import org.apache.http.client.ClientProtocolException;
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
//@PipelineName("HaiXiuImgDownPipeline")
//public class HaiXiuImgDownPipeline implements Pipeline<HaixiuImgSpider> {
//
//    String path = "I:\\doc\\tutu\\img\\";
//
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
//    public void process(HaixiuImgSpider bean) {
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
//                    //check train todo delete
//                    String pageId = bean.getId();
//                    Integer num = bean.getNumberMap().get(pageId);
//                    System.out.println("page:" + pageId + ";num:" + num);
//                    if (num != null && num > 0) {
//                        num++;
//                        bean.getNumberMap().put(pageId, num);
//                    } else {
//                        bean.getNumberMap().put(pageId, 1);
//                    }
//                    if (num != null && num >= 8) return;
//
//                } catch (ClientProtocolException e1) {
//                    System.out.println("ClientProtocolException is ok");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    request.releaseConnection();
//                }
//            }
//        }
//    }
//
//}
