package com.yujian.wq.api;

import com.yujian.wq.controller.Response;
import com.yujian.wq.mapper.ImgEntity;
import com.yujian.wq.mapper.ImgWorkMapper;
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
import java.util.*;

/**
 * Created by wangqing on 2018/1/9.
 */
@Controller
@RequestMapping("/api/user")
public class UserApi {

    @Resource
    private ImgWorkMapper imgWorkMapper;
    @Value("#{config['img.deploy.path']}")
    private String deployPath;

    @RequestMapping(value = "/getImage", method = RequestMethod.GET)
    @ResponseBody
    public Response getIndexImage(String openId, Integer start, Integer tag) {
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

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public Response getIndexImage(String openId) {

        int total = imgWorkMapper.findTotal();
        Response response = new Response();
        if (total > 0) {
            //TODO 检查ID有没有被看过
            Random random = new Random();
            Set<Integer> idList = new HashSet<>();

            for (int i = 0; i < 200; i++) {
                idList.add(random.nextInt(total - 1) + 1);
            }
            Map<String, Object> param = new HashMap<>();
            param.put("list", idList);
            param.put("num", 10);
            List<ImageDto> resList = new ArrayList<>();
            List<ImgEntity> dataList = imgWorkMapper.findRandom(param);
            for (ImgEntity entity : dataList) {
                ImageDto dto = new ImageDto();
                dto.setUrl(entity.getTagId() + "/" + entity.getImg());
                dto.setId(entity.getId());
                dto.setTitle(entity.getTitle());
                resList.add(dto);
            }

            response.setStatus(Response.SUCCESS);
            response.setData(resList);
            return response;
        }
        response.setStatus(Response.FAILURE);
        response.setMsg("没有了");
        return response;
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


}
