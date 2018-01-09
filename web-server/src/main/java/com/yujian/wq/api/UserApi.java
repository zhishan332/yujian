package com.yujian.wq.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by wangqing on 2018/1/9.
 */
@Controller
@RequestMapping("/api/user")
public class UserApi {


    @RequestMapping(value = "/getImage", method = RequestMethod.GET)
    @ResponseBody
    public List<ImageDto> getIndexImage(String openId) {


        return null;
    }

}
