package com.yujian.wq.service;

import com.yujian.wq.mapper.ImgEntity;
import com.yujian.wq.mapper.ImgWorkMapper;
import com.yujian.wq.mapper.TagEntity;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/7
 */
@Service
public class ImgWorkService {
    @Value("#{config['img.deploy.path']}")
    private String deployPath;

    @Resource
    private ImgWorkMapper imgWorkMapper;


    @Transactional
    public String insert(String path, ImgEntity imgEntity, List<TagEntity> tagEntityList) throws IOException {

        int flag = imgWorkMapper.insertImgAndGetId(imgEntity);

        if (flag > 0) {
            int id = imgEntity.getId();

            for (TagEntity tagEntity : tagEntityList) {
                tagEntity.setImgId(id);
                imgWorkMapper.insertTag(tagEntity);
            }

        }

        File file = new File(path);

        String oldName = file.getName();

        int suffixIndex = oldName.lastIndexOf(".");

        String suffix = oldName.substring(suffixIndex);

        String fileName = imgEntity.getImg() + suffix;


        String deployPathFile = deployPath + imgEntity.getFolder() + File.separator + fileName;

        FileUtils.copyFile(file, new File(deployPathFile));

        File checkFile = new File(deployPathFile);
        if (checkFile.exists()) {
            file.delete();
        }

        return deployPathFile;
    }
}
