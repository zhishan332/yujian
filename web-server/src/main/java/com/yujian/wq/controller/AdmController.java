package com.yujian.wq.controller;

import com.yujian.wq.api.ImgChainDto;
import com.yujian.wq.mapper.ImgChainEntity;
import com.yujian.wq.mapper.ImgEntity;
import com.yujian.wq.mapper.ImgWorkMapper;
import com.yujian.wq.service.ImgWorkService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqing on 2018/1/18.
 */
@Controller
@RequestMapping("/adm")
public class AdmController {

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

    @RequestMapping(value = {"",}, method = RequestMethod.GET)
    public ModelAndView showAdm(HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("adm");
        mav.getModel().put("pageName", "发布数据操作");
        return mav;
    }


    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public Response find(String chain, String title, Integer start) {

        Response response = new Response();
        Map<String, Object> param = new HashMap<>();
        param.put("chain", StringUtils.isBlank(chain) ? null : chain);
        param.put("title", StringUtils.isBlank(title) ? null : title);
        param.put("start", start == null ? 0 : start * 20);
        param.put("num", 20);

        List<ImgChainEntity> dataList = imgWorkMapper.findChainCommon(param);


        if (dataList != null && !dataList.isEmpty()) {
            List<ImgChainDto> resList = new ArrayList<>();

            for (ImgChainEntity imgChainEntity : dataList) {

                ImgChainDto dto = convert(imgChainEntity, true);
                String chainDD = imgChainEntity.getChain();
                if (dto.getChain().equals("1516198402032")) {
                    System.out.println(123);
                }
                List<ImgEntity> imgList = imgWorkMapper.findByChain(chainDD);

                for (ImgEntity imgEntity : imgList) {
                    dto.getChainList().add(convert(imgEntity));
                }
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

    @RequestMapping(value = "/deleteChain", method = RequestMethod.POST)
    @ResponseBody
    public Response deleteChain(String chain) {
        Response response = new Response();
        if (StringUtils.isBlank(chain)) {
            response.setStatus(Response.FAILURE);
            return response;
        }
        imgWorkMapper.deleteChain(chain);
        imgWorkMapper.deleteImgChain(chain);
        response.setStatus(Response.SUCCESS);
        return response;
    }

    @RequestMapping(value = "/deleteImg", method = RequestMethod.POST)
    @ResponseBody
    public Response deleteImg(String img, String chain) {
        Response response = new Response();
        if (StringUtils.isBlank(img)) {
            response.setStatus(Response.FAILURE);
            return response;
        }

        imgWorkMapper.deleteImg(img);
        ImgChainEntity param = new ImgChainEntity();
        param.setChain(chain);

        imgWorkMapper.updateReduceChain(param);
        response.setStatus(Response.SUCCESS);
        return response;
    }


    private ImgChainDto convert(ImgEntity imgChainEntity) {

        ImgChainDto dto = new ImgChainDto();
        dto.setImg(imgChainEntity.getImg());
        return dto;
    }


    private ImgChainDto convert(ImgChainEntity imgChainEntity, boolean first) {

        ImgChainDto dto = new ImgChainDto();

        dto.setImg(imgChainEntity.getImg());
        if (first) {
            dto.setChain(imgChainEntity.getChain());
            dto.setTitle(imgChainEntity.getTitle());
            dto.setNum(imgChainEntity.getNum());
            dto.setChainList(new ArrayList<ImgChainDto>());
        }

        return dto;
    }

}
