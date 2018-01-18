package com.yujian.wq.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yujian.wq.controller.Response;
import com.yujian.wq.mapper.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqing on 2018/1/9.
 */
@Controller
@RequestMapping("/api/user")
public class UserApi {
    private static final Logger logger = LoggerFactory.getLogger(UserApi.class);


    @Resource
    private ImgWorkMapper imgWorkMapper;
    @Value("#{config['img.deploy.path']}")
    private String deployPath;

    @Value("#{config['wechat.login.url']}")
    private String wechatLoginUrl;

    @Value("#{config['wechat.app.id']}")
    private String wechatAppID;

    @Value("#{config['wechat.app.secret']}")
    private String wechatAppSecret;

    @Value("#{config['init.energy']}")
    private Integer initEnergy;

    @Resource
    private UserMapper userMapper;
    @Resource
    private StoryMapper storyMapper;

    @RequestMapping(value = "/loadIndexData", method = RequestMethod.GET)
    @ResponseBody
    public Response loadIndexData(String openId, Integer start, Integer tag) {
        Map<String, Object> param = new HashMap<>();
        param.put("tagId", tag == null ? 2 : tag);
        param.put("start", start == null ? 0 : start * 20);
        param.put("num", 20);
        List<ImgEntity> dataList = imgWorkMapper.find(param);
        List<ImageDto> resList = new ArrayList<>();
        for (ImgEntity entity : dataList) {
            ImageDto dto = new ImageDto();
            dto.setUrl(entity.getTagId() + "/" + entity.getImg());
            dto.setId(entity.getId());
            dto.setTitle(entity.getTitle());
            resList.add(dto);
        }
        Response response = new Response();
        response.setStatus(Response.SUCCESS);
        response.setData(resList);
        return response;
    }

    @RequestMapping(value = "/loadChainData", method = RequestMethod.GET)
    @ResponseBody
    public Response loadChainData(String openId, Integer start) {

        Response response = new Response();
        Map<String, Object> param = new HashMap<>();

//        param.put("tagId", 2);
        param.put("start", start == null ? 0 : start * 20);
        param.put("num", 20);

        List<ImgChainEntity> dataList = imgWorkMapper.findChainByTime(param);


        if (dataList != null && !dataList.isEmpty()) {
            Map<String, ImgChainDto> map = new HashMap<>();
            for (ImgChainEntity entity : dataList) {
                if (entity.getChain().equals("1516203406586")) {
                    System.out.println(123);
                }
                entity.setImg(entity.getTagId() + "/" + entity.getImg());

                if (map.containsKey(entity.getChain())) {
                    map.get(entity.getChain()).getChainList().add(convert(entity, false));
                } else {
                    ImgChainDto ndata = convert(entity, true);
                    ArrayList<ImgChainDto> list = new ArrayList<>();
                    list.add(convert(entity, false));
                    ndata.setChainList(list);
                    map.put(entity.getChain(), ndata);
                }
            }
            List<ImgChainDto> resList = new ArrayList<>();
            for (Map.Entry<String, ImgChainDto> entry : map.entrySet()) {
                resList.add(entry.getValue());
            }


            response.setStatus(Response.SUCCESS);
            response.setData(resList);
            return response;
        }
        response.setStatus(Response.FAILURE);
        response.setMsg("没有了");
        return response;
    }

    private ImgChainDto convert(ImgChainEntity imgChainEntity, boolean first) {

        ImgChainDto dto = new ImgChainDto();

        dto.setImg(imgChainEntity.getImg());
        if (first) {
            dto.setChain(imgChainEntity.getChain());
            dto.setTitle(imgChainEntity.getTitle());
            dto.setNum(imgChainEntity.getNum());
        }

        return dto;
    }

    @RequestMapping(value = "/img/{tag}/{name}/{token}", method = RequestMethod.GET)
    public String showImg(@PathVariable("tag") String tag, @PathVariable("name") String name, @PathVariable("token") String token,
                          HttpServletResponse response) {
        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {
            File viewFile = new File(deployPath + tag + File.separator + name);
            if (!viewFile.exists()) return null;
            ips = new FileInputStream(viewFile);
            response.setContentType("multipart/form-data");
            out = response.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = ips.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ips != null) {
                try {
                    ips.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    /** 涉及登录操作 开始 **/

    /**
     * 微信会先调用小程序的登录接口小程序再调用微信服务端的接口获取登录信息
     *
     * @param code 用户登录凭证（有效期五分钟）。开发者需要在开发者服务器后台调用 api，使用 code 换取 openid 和 session_key 等信息
     * @return
     */
    CloseableHttpClient httpclient = HttpClients.createDefault();

    @RequestMapping(value = "/onLogin", method = RequestMethod.POST)
    @ResponseBody
    public Response onLogin(String code) {

        Response response = new Response();

        if (StringUtils.isBlank(code)) {
            response.setStatus(Response.FAILURE);
            response.setMsg("login code must be set");
            return response;
        }

        String body = null;
        try {
            // Get请求
            HttpGet httpget = new HttpGet(wechatLoginUrl);
            // 设置参数
            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("appid", wechatAppID));
            params.add(new BasicNameValuePair("secret", wechatAppSecret));
            params.add(new BasicNameValuePair("js_code", code));
            params.add(new BasicNameValuePair("grant_type", "authorization_code"));
            String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
            httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
            // 发送请求
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);

            if (StringUtils.isNotBlank(responseBody)) {

                JSONObject json = JSON.parseObject(responseBody);
                if (json == null || StringUtils.isBlank(json.getString("openid"))) {
                    response.setStatus(Response.FAILURE);
                    response.setMsg("登录失败:" + responseBody);
                } else {
                    logger.info("user login ,openid:" + json.getString("openid"));
                    String openId = json.getString("openid");
                    UserEntity user = userMapper.getUser(openId);
                    if (null == user) {
                        UserEntity userInfo = new UserEntity();
                        userInfo.setOpenId(openId);
                        userInfo.setEnergy(initEnergy);
                        userMapper.insertUser(userInfo);
                    }
                    response.setStatus(Response.SUCCESS);
                    response.setData(json);
                }

            } else {
                response.setStatus(Response.FAILURE);
                response.setMsg("没有获取到服务器的响应信息");
            }
        } catch (Exception e) {
            logger.error("请求微信服务器异常", e);
            response.setStatus(Response.FAILURE);
            response.setMsg("请求微信服务器异常,e:" + e.getMessage());
            return response;
        }

        return response;
    }


    @RequestMapping(value = "/findStory", method = RequestMethod.GET)
    @ResponseBody
    public Response findStory(String openId, Integer start) {
        Response response = new Response();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("start", start == null ? 0 : start * 20);
            param.put("num", 10);
            List<StoryEntity> list = storyMapper.find(param);

            response.setStatus(Response.SUCCESS);
            response.setData(list);

        } catch (Exception e) {
            logger.error("findStory error", e);
            response.setStatus(Response.FAILURE);
        }
        return response;
    }

    @RequestMapping(value = "/getStory", method = RequestMethod.GET)
    @ResponseBody
    public Response getStory(String openId, Integer id) {
        Response response = new Response();
        try {
            StoryEntity data = storyMapper.get(id);

            response.setStatus(Response.SUCCESS);
            response.setData(data);

        } catch (Exception e) {
            logger.error("getStory error", e);
            response.setStatus(Response.FAILURE);
        }

        return response;
    }

//    @RequestMapping(value = "/increase", method = RequestMethod.POST)
//    @ResponseBody
//    public Response reduce(String openId, Integer num) {
//
//        Response response = new Response();
//        try {
//            Map<String, Object> param = new HashMap<>();
//            param.put("openId", openId);
//            if (num == 1) {
//                param.put("energy", 30);
//            } else if (num == 2) {
//                param.put("energy", 365);
//            } else if (num == 3) {
//                param.put("energy", 365 * 500);//500年
//            }
//            userMapper.increaseEnergy(param);
//        } catch (Exception e) {
//            logger.error("increaseEnergy error", e);
//        }
//        response.setStatus(Response.SUCCESS);
//        return response;
//    }
}
