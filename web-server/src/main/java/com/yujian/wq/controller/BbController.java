package com.yujian.wq.controller;

import com.yujian.wq.mapper.ImgChainEntity;
import com.yujian.wq.mapper.ImgEntity;
import com.yujian.wq.mapper.ImgSpiderEntity;
import com.yujian.wq.utils.HtmlUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/29
 */
@Controller
@RequestMapping("/bb")
public class BbController {

    @Value("#{config['img.temp.bb.path']}")
    private String tempPath;
    @Value("#{config['img.deploy.bb.path']}")
    private String deployPath;
    @Value("#{config['img.tag.bb.path']}")
    private String tagPath;
    private Map<String, Integer> tagIdMap = new HashMap<>();

    @PostConstruct
    public void init() {
        List<String> list = null;
        try {
            list = FileUtils.readLines(new File(tagPath), "utf-8");
            if (list != null) {

                for (String str : list) {
                    if (StringUtils.isNotBlank(str)) {
                        String[] data = str.split(" ");
                        tagIdMap.put(data[0], Integer.valueOf(data[1]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView showCu(HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("bb");
        mav.getModel().put("pageName", "比一比");
        return mav;
    }

    @RequestMapping(value = "/loadManyImg", method = RequestMethod.GET)
    @ResponseBody
    public Response loadManyImg() {
        Response resp = new Response();
        try {
            File tempFile = new File(tempPath);
            File[] list = tempFile.listFiles();


            Iterator<File> iter = FileUtils.iterateFiles(tempFile, null, true);
            List<Map<String, Object>> resList = new ArrayList<>();
            int i = 0;
            while (iter.hasNext()) {
                File file = iter.next();
                String name = file.getName();
                if (name.endsWith("DS_Store")) {
                    continue;
                }


                resp.setStatus(Response.SUCCESS);
                Map<String, Object> data = new HashMap<>();
                data.put("md5", i);
                data.put("path", file.getAbsolutePath());
                data.put("total", String.valueOf((list == null ? 0 : list.length)));

                resList.add(data);
                i++;

                if (i > 100) break;
            }
            resp.setData(resList);
            resp.setStatus(Response.SUCCESS);

            if (resList.size() <= 0) {
                resp.setStatus(Response.FAILURE);
                resp.setMsg("处理完了");
            }

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(Response.FAILURE);
            resp.setMsg(e.toString());
        }
        return resp;
    }


    @RequestMapping(value = {"/workImg"}, method = RequestMethod.GET)
    public String workImg(String file, HttpServletResponse response) {
        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {

            File viewFile = new File(file);
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


    @RequestMapping(value = "/saveTrain", method = RequestMethod.POST)
    @ResponseBody
    public Response saveTrain(String img, String tag) {
        Response resp = new Response();

        try {

            if (StringUtils.isBlank(img)) {
                resp.setStatus(Response.FAILURE);
                resp.setMsg("img is null");
                return resp;
            }

            if (StringUtils.isBlank(tag)) {
                resp.setStatus(Response.FAILURE);
                resp.setMsg("tag is null");
                return resp;
            }

            if (tag.equals("删除")) {
                FileUtils.forceDelete(new File(img));
                resp.setStatus(Response.SUCCESS);
                return resp;
            }
            ImgChainEntity imgEntity = new ImgChainEntity();
            imgEntity.setImg(img);

            if (StringUtils.isBlank(tag)) {
                imgEntity.setTagId(-1);
//                imgEntity.setTag("其他");
            } else {
                String[] tagList = tag.split(";");

                for (String tagStr : tagList) {
                    if (StringUtils.isBlank(tagStr)) continue;
                    Integer type = tagIdMap.get(tagStr);
                    if (type != null && type >= 0) {
                        imgEntity.setTagId(type);
//                        imgEntity.setTag(tagStr);
                        break;
                    }
                }
            }

            File srFile = new File(img);
            String nnPath = deployPath + imgEntity.getTagId() + File.separator + srFile.getName();
            FileUtils.moveFile(srFile, new File(nnPath));
            resp.setStatus(Response.SUCCESS);
            resp.setMsg("save to train file:" + nnPath);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(Response.FAILURE);
            resp.setMsg(e.toString());
        }

        return resp;
    }


}
