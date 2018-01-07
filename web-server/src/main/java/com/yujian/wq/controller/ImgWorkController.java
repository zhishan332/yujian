package com.yujian.wq.controller;

import com.yujian.wq.mapper.ImgEntity;
import com.yujian.wq.mapper.TagEntity;
import com.yujian.wq.service.ImgWorkService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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
 * @since 2018/1/7
 */
@Controller
@RequestMapping("")
public class ImgWorkController {

    @Value("#{config['img.temp.path']}")
    private String tempPath;
    @Value("#{config['img.deploy.path']}")
    private String deployPath;
    @Value("#{config['img.tag.path']}")
    private String tagPath;
    @Resource
    private ImgWorkService imgWorkService;

    private static final int DeployFolderNum = 10;

    @RequestMapping(value = {"", "index", "/index",}, method = RequestMethod.GET)
    public ModelAndView showIndex(HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("fav");
        mav.getModel().put("pageName", "首页");

        return mav;
    }


    @RequestMapping(value = {"/tempimg"}, method = RequestMethod.GET)
    public ModelAndView showImg(Integer order, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("fav");
        mav.getModel().put("pageName", "首页");
        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {
            File tempFile = new File(tempPath);
            File[] list = tempFile.listFiles();


            if (list != null && list.length > 0 && list.length >= (order + 1)) {
                Arrays.sort(list, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        long diff = f1.lastModified() - f2.lastModified();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    }
                });

                File workFile = list[order];
                mav.getModel().put("data", workFile.getAbsolutePath());
                ips = new FileInputStream(workFile);
                response.setContentType("multipart/form-data");
                out = response.getOutputStream();
                //读取文件流
                int len = 0;
                byte[] buffer = new byte[1024 * 10];
                while ((len = ips.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            } else {
                mav = new ModelAndView("error");
                mav.getModel().put("info", "全部处理完了");
            }
            return null;
        } catch (Exception e) {
            mav = new ModelAndView("error");
            mav.getModel().put("info", "系统错误,请重试");
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
        return mav;
    }

    @RequestMapping(value = {"/viewimg"}, method = RequestMethod.GET)
    public String showImg(String deployFile, HttpServletResponse response) {
        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {
            File viewFile = new File(deployFile);
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

    @RequestMapping(value = "/tempimg/name", method = RequestMethod.GET)
    @ResponseBody
    public Response findFavList(Integer order) {
        Response resp = new Response();
        try {
            File tempFile = new File(tempPath);
            File[] list = tempFile.listFiles();


            if (list != null && list.length > 0 && list.length >= (order + 1)) {
                Arrays.sort(list, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        long diff = f1.lastModified() - f2.lastModified();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    }
                });

                File workFile = list[order];


                resp.setStatus(Response.SUCCESS);
                resp.setData(workFile.getAbsolutePath());
                return resp;
            }
            resp.setStatus(Response.FAILURE);
            resp.setMsg("处理完了");
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(Response.FAILURE);
            resp.setMsg(e.toString());
        }
        return resp;
    }

    @RequestMapping(value = "/taglist", method = RequestMethod.GET)
    @ResponseBody
    public Response getAllTag() {
        Response resp = new Response();

        List<String> list = null;
        try {
            list = FileUtils.readLines(new File(tagPath), "utf-8");
            if (list != null) {

                resp.setStatus(Response.SUCCESS);
                resp.setData(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
            resp.setStatus(Response.FAILURE);
            resp.setMsg(e.toString());
        }

        return resp;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Response save(String img, String tag, String chain) {
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

            File imgFile = new File(img);

            if (!imgFile.exists()) {
                resp.setStatus(Response.FAILURE);
                resp.setMsg("imgFile is not exists");
                return resp;
            }

            String fileName = UUID.randomUUID().toString();
            int folder = getTimesmorning() % DeployFolderNum;

            ImgEntity imgEntity = new ImgEntity();
            imgEntity.setImg(fileName);
            imgEntity.setFolder(folder);
            if (StringUtils.isNotBlank(chain)) imgEntity.setChain(chain);

            String[] tagList = tag.split(";");

            List<TagEntity> tagEntityList = new ArrayList<>();

            for (String tagStr : tagList) {
                if (StringUtils.isBlank(tagStr)) continue;
                TagEntity tagEntity = new TagEntity();
                tagEntity.setTag(tagStr);
                tagEntityList.add(tagEntity);
            }

            if (tagEntityList.isEmpty()) {
                resp.setStatus(Response.FAILURE);
                resp.setMsg("tag is null");
                return resp;
            }

            String deployFile = imgWorkService.insert(img, imgEntity, tagEntityList);
            resp.setStatus(Response.SUCCESS);
            resp.setData(deployFile);
            resp.setMsg("folder:" + folder + ";file:" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(Response.FAILURE);
            resp.setMsg(e.toString());
        }

        return resp;
    }

    //获得当天0点时间
    public static int getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    public static void main(String[] args) {
        int folder = getTimesmorning() % DeployFolderNum;
        System.out.println(folder);
        System.out.println(folder);
    }
}
