//package com.yujian.wq.spider;
//
//import com.geccocrawler.gecco.annotation.PipelineName;
//import com.geccocrawler.gecco.pipeline.Pipeline;
//import com.geccocrawler.gecco.request.HttpRequest;
//import com.geccocrawler.gecco.scheduler.DeriveSchedulerContext;
//import com.geccocrawler.gecco.scheduler.SchedulerContext;
//
//import java.util.List;
//
///**
// * 添加描述
// *
// * @author wangqing
// * @since 2018/1/8
// */
//@PipelineName("NvshensPipeline")
//public class NvshensPipeline implements Pipeline<NvshensAlbumSpider> {
//
//
//    @Override
//    public void process(NvshensAlbumSpider bean) {
//
//        List<String> list = bean.getUrls();
//
//        if (list != null) {
//            System.out.println("list:" + list);
//            for(String url :list){
//                HttpRequest currRequest = bean.getRequest();
//                DeriveSchedulerContext.into(currRequest.subRequest(url));
//            }
//        }
//
//    }
//}
