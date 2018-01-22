package com.yujian.wq.controller;

import com.yujian.wq.mapper.StoryEntity;
import com.yujian.wq.mapper.StoryMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

/**
 * Created by wangqing on 2018/1/19.
 */
@Controller
@RequestMapping("/story")
public class StoryController {

    @Resource
    private StoryMapper storyMapper;

    @RequestMapping(value = {"",}, method = RequestMethod.GET)
    public ModelAndView showAdm(HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("story");
        mav.getModel().put("pageName", "短文");
        return mav;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Response deleteChain(String title, String summary, String content, String author, String tags, Integer hot) {
        Response response = new Response();
        if (StringUtils.isBlank(content)) {
            response.setStatus(Response.FAILURE);
            return response;
        }
        if (StringUtils.isBlank(summary)) {
            response.setStatus(Response.FAILURE);
            return response;
        }
        StoryEntity param = new StoryEntity();
        param.setTitle(title);
        param.setContent(content);
        param.setAuthor(author);
        if (StringUtils.isNotBlank(tags)) {
            String[] arry = tags.split(" ");
            String tagStr = "";
            for (String tag : arry) {
                tagStr += tag;
                tagStr += ",";
            }
            tagStr = tagStr.substring(0, tagStr.length() - 1);
            param.setTag(tagStr);
        }
        if (hot == null || hot <= 0) {
            hot = new Random().nextInt(46) + 7;
        }
        param.setHot(hot);
        param.setSummary(summary);
        storyMapper.insert(param);
        response.setStatus(Response.SUCCESS);
        return response;
    }

}
