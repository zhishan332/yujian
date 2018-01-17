package com.yujian.wq.controller;

import com.yujian.wq.mapper.ImgChainEntity;
import com.yujian.wq.mapper.ImgEntity;
import com.yujian.wq.mapper.ImgSpiderEntity;
import com.yujian.wq.mapper.ImgWorkMapper;
import com.yujian.wq.service.ImgWorkService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
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
    @Resource
    private ImgWorkMapper imgWorkMapper;
    @Resource
    private JdbcTemplate jdbcTemplate;

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
//            File[] list = tempFile.listFiles();

            Iterator<File> iter = FileUtils.iterateFiles(tempFile, null, true);
            int i = 0;
            while (iter.hasNext()) {
                File file = iter.next();
                String name = file.getName();
                if (name.endsWith("DS_Store")) {
                    continue;
                }
                if (i != order) {
                    i++;
                    continue;
                }
                ips = new FileInputStream(file);
                response.setContentType("multipart/form-data");
                out = response.getOutputStream();
                //读取文件流
                int len = 0;
                byte[] buffer = new byte[1024 * 10];
                while ((len = ips.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                break;
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

    @RequestMapping(value = "/loadImg", method = RequestMethod.GET)
    @ResponseBody
    public Response findFavList(Integer order) {
        Response resp = new Response();
        try {
            File tempFile = new File(tempPath);
            File[] list = tempFile.listFiles();


            Iterator<File> iter = FileUtils.iterateFiles(tempFile, null, true);
            int i = 0;
            while (iter.hasNext()) {
                File file = iter.next();
                String name = file.getName();
                if (name.endsWith("DS_Store")) {
                    continue;
                }
                if (i != order) {
                    i++;
                    continue;
                }
                ImgSpiderEntity task = readImgSpider(name);
                resp.setStatus(Response.SUCCESS);
                Map<String, Object> data = new HashMap<>();
                data.put("title", task == null ? null : task.getTitle());
                data.put("md5", task == null ? null : task.getMd5());
                data.put("chain", task == null ? null : task.getChain());
                data.put("path", file.getAbsolutePath());
                data.put("total", String.valueOf((list == null ? 0 : list.length)));
                resp.setData(data);
                return resp;
            }
//            if (list != null && list.length > 0 && list.length >= (order + 1)) {
//                Arrays.sort(list, new Comparator<File>() {
//                    public int compare(File f1, File f2) {
//                        long diff = f1.lastModified() - f2.lastModified();
//                        if (diff > 0)
//                            return 1;
//                        else if (diff == 0)
//                            return 0;
//                        else
//                            return -1;
//                    }
//                });
//
//                File workFile = list[order];
//
//
//                resp.setStatus(Response.SUCCESS);
//                Map<String, String> data = new HashMap<>();
//                data.put("path", workFile.getAbsolutePath());
//                data.put("total", String.valueOf((list.length)));
//                resp.setData(data);
//                return resp;
//            }
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

    private Map<String, Integer> tagIdMap = new HashMap<>();

    @RequestMapping(value = "/taglist", method = RequestMethod.GET)
    @ResponseBody
    public Response getAllTag() {
        Response resp = new Response();

        List<String> list = null;
        try {
            list = FileUtils.readLines(new File(tagPath), "utf-8");
            if (list != null) {

                List<String> resList = new ArrayList<>();
                for (String str : list) {
                    if (StringUtils.isNotBlank(str)) {
                        String[] data = str.split(" ");
                        tagIdMap.put(data[0], Integer.valueOf(data[1]));
                        resList.add(data[0]);
                    }
                }
                resp.setStatus(Response.SUCCESS);
                resp.setData(resList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            resp.setStatus(Response.FAILURE);
            resp.setMsg(e.toString());
        }

        return resp;
    }

//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    @ResponseBody
//    public Response save(String img, String tag, String chain) {
//        Response resp = new Response();
//
//        try {
//
//            if (StringUtils.isBlank(img)) {
//                resp.setStatus(Response.FAILURE);
//                resp.setMsg("img is null");
//                return resp;
//            }
//            if (StringUtils.isBlank(tag)) {
//                resp.setStatus(Response.FAILURE);
//                resp.setMsg("tag is null");
//                return resp;
//            }
//
//            File imgFile = new File(img);
//
//            if (!imgFile.exists()) {
//                resp.setStatus(Response.FAILURE);
//                resp.setMsg("imgFile is not exists");
//                return resp;
//            }
//
//            String fileName = getUUID();
//            int folder = getCurrentMonthLastDay() % DeployFolderNum;
//
//            ImgEntity imgEntity = new ImgEntity();
//            imgEntity.setImg(fileName);
//            imgEntity.setTagId(folder);
//            if (StringUtils.isNotBlank(chain)) imgEntity.setChain(chain);
//
//            String[] tagList = tag.split(";");
//
//            List<TagEntity> tagEntityList = new ArrayList<>();
//
//            for (String tagStr : tagList) {
//                if (StringUtils.isBlank(tagStr)) continue;
//                TagEntity tagEntity = new TagEntity();
//                tagEntity.setTag(tagStr);
//                tagEntityList.add(tagEntity);
//            }
//
//            if (tagEntityList.isEmpty()) {
//                resp.setStatus(Response.FAILURE);
//                resp.setMsg("tag is null");
//                return resp;
//            }
//
//            String deployFile = imgWorkService.insert(img, imgEntity, tagEntityList);
//            resp.setStatus(Response.SUCCESS);
//            resp.setData(deployFile);
//            resp.setMsg("folder:" + folder + ";file:" + fileName);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            resp.setStatus(Response.FAILURE);
//            resp.setMsg(e.toString());
//        }
//
//        return resp;
//    }

    @RequestMapping(value = "/saveTrain", method = RequestMethod.POST)
    @ResponseBody
    public Response saveTrain(String img, String tag, String chain, String title, String md5) {
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
            if (StringUtils.isBlank(chain)) {
                resp.setStatus(Response.FAILURE);
                resp.setMsg("chain is null");
                return resp;
            }

            if (StringUtils.isBlank(md5)) {
                resp.setStatus(Response.FAILURE);
                resp.setMsg("md5 is null");
                return resp;
            }


            File imgFile = new File(img);

            if (!imgFile.exists()) {
                resp.setStatus(Response.FAILURE);
                resp.setMsg("imgFile is not exists");
                return resp;
            }

            String fileName = getUUID();

            ImgChainEntity imgEntity = new ImgChainEntity();
            imgEntity.setImg(fileName);
            imgEntity.setChain(chain);
            imgEntity.setTitle(title);
            imgEntity.setMd5(md5);

            if (StringUtils.isBlank(tag)) {
                imgEntity.setTagId(-1);
//                imgEntity.setTag("其他");
            } else {
                String[] tagList = tag.split(";");

                for (String tagStr : tagList) {
                    if (StringUtils.isBlank(tagStr)) continue;
                    Integer type = tagIdMap.get(tagStr);
                    if (type != null && type > 0) {
                        imgEntity.setTagId(type);
//                        imgEntity.setTag(tagStr);
                        break;
                    }
                }
            }
            imgEntity.setNum(1);
//            ImgEntity check = imgWorkMapper.findChain(imgEntity.getChain());
//            if (check != null) {
//                if (!imgEntity.getTagId().equals(check.getTagId())) {
//                    resp.setStatus(Response.FAILURE);
//                    resp.setMsg("同一套图下不能存在两个不同的TAG类型,已有类型:"+(check.getTagId()));
//                    return resp;
//                }
//            }
            String deployFile = imgWorkService.insertTrain(img, imgEntity);
            //更新爬虫数据库
            updateImgSpider(imgFile.getName());
            resp.setStatus(Response.SUCCESS);
            resp.setData(deployFile);
            resp.setMsg("save to train file:" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(Response.FAILURE);
            resp.setMsg(e.toString());
        }

        return resp;
    }


    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Response del(String path,String chain) {
        Response resp = new Response();
        if (StringUtils.isBlank(path)) {
            resp.setStatus(Response.FAILURE);
            resp.setMsg("path is null");
            return resp;
        }
        File imgFile = new File(path);

        if (!imgFile.exists()) {
            resp.setStatus(Response.FAILURE);
            resp.setMsg("imgFile is not exists");
            return resp;
        }

        ImgChainEntity param =new ImgChainEntity();
        param.setChain(chain);

//        imgWorkMapper.updateReduceChain(param);

        imgFile.delete();
        //更新爬虫数据库
        updateImgSpider(imgFile.getName());
        return resp;
    }

    @RequestMapping(value = "/createTrain", method = RequestMethod.GET)
    @ResponseBody
    public Response createTrain() {
        Response resp = new Response();
        long t1 = System.currentTimeMillis();
        imgWorkService.createTrainFile();
        resp.setStatus(1);
        resp.setMsg("成功，cost：" + (System.currentTimeMillis() - t1));
        return resp;
    }

    @RequestMapping(value = "/createTrain2", method = RequestMethod.GET)
    @ResponseBody
    public Response createTrain2() throws IOException {
        Response resp = new Response();
        long t1 = System.currentTimeMillis();
        imgWorkService.createTrainFile2();
        resp.setStatus(1);
        resp.setMsg("成功，cost：" + (System.currentTimeMillis() - t1));
        return resp;
    }

    private ImgSpiderEntity readImgSpider(String name) {

        ImgSpiderEntity data = null;
        try {
            RowMapper<ImgSpiderEntity> rm = ParameterizedBeanPropertyRowMapper.newInstance(ImgSpiderEntity.class);

            data = jdbcTemplate.queryForObject("select img,title,chain,md5,status from img_spider where img = '" + name + "'", rm);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("没有数据,请检查爬虫表");
            return null;
        }

        if (data != null) {
            return data;
        }
        return null;
    }

    private void updateImgSpider(String name) {
        jdbcTemplate.execute("UPDATE img_spider  set status = 2 where img ='" + name + "'");
    }

    //获得当天0点时间
    public static int getCurrentMonthLastDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    private String getUUID() {
        String uuid = UUID.randomUUID().toString(); //获取UUID并转化为String对象
        uuid = uuid.replace("-", "");
        return uuid;
    }

    public static void main(String[] args) {
        int folder = getCurrentMonthLastDay() % DeployFolderNum;
        System.out.println(folder);
        System.out.println(folder);
    }
}
