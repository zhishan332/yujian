package com.yujian.wq.service;

import com.yujian.wq.mapper.ImgEntity;
import com.yujian.wq.mapper.ImgWorkMapper;
import com.yujian.wq.mapper.TagEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Value("#{config['img.tag.path']}")
    private String tagPath;

    @Value("#{config['train.tag.path']}")
    private String trainTagPath;

    @Value("#{config['train.test.tag.path']}")
    private String trainTagTestPath;

    @Value("#{config['train.data.path']}")
    private String trainDataPath;
    @Resource
    private ImgWorkMapper imgWorkMapper;


    @Transactional
    public String insert(String path, ImgEntity imgEntity, List<TagEntity> tagEntityList) throws IOException {

        File file = new File(path);

        String oldName = file.getName();

        int suffixIndex = oldName.lastIndexOf(".");

        String suffix = oldName.substring(suffixIndex);

        imgEntity.setImg(imgEntity.getImg() + suffix);
        int flag = imgWorkMapper.insertImgAndGetId(imgEntity);

        if (flag > 0) {
            int id = imgEntity.getId();

            for (TagEntity tagEntity : tagEntityList) {
                tagEntity.setImgId(id);
                imgWorkMapper.insertTag(tagEntity);
            }

        }


        String fileName = imgEntity.getImg() + suffix;


        String deployPathFile = deployPath + imgEntity.getTagId() + File.separator + fileName;

        FileUtils.copyFile(file, new File(deployPathFile));

        File checkFile = new File(deployPathFile);
        if (checkFile.exists()) {
            file.delete();
        }

        return deployPathFile;
    }


    @Transactional
    public String insertTrain(String path, ImgEntity imgEntity) throws IOException {
        File file = new File(path);


        if (!file.exists()) {
            return null;
        }


        String oldName = file.getName();

        int suffixIndex = oldName.lastIndexOf(".");

        String suffix = oldName.substring(suffixIndex);

        imgEntity.setImg(imgEntity.getImg() + suffix);

        int flag = imgWorkMapper.insertImgAndGetId(imgEntity);

        if (flag > 0) {
//            int id = imgEntity.getId();
            ImgEntity checkData = imgWorkMapper.findChain(imgEntity.getChain());
            if (checkData == null) {
//                ImgEntity chainEntity = new ImgEntity();
//                chainEntity.setTitle(imgEntity.getTitle());
//                chainEntity.setImg(imgEntity.getImg());
//                chainEntity.setMd5(imgEntity.getMd5());
//                chainEntity.setTagId(imgEntity.getTagId());
//                chainEntity.setChain(imgEntity.getChain());
                imgWorkMapper.insertChain(imgEntity);
            }
        }

        String fileName = imgEntity.getImg();

        String deployPathFile = deployPath + imgEntity.getTagId() + File.separator + fileName;

        FileUtils.copyFile(file, new File(deployPathFile));

        File checkFile = new File(deployPathFile);
        if (checkFile.exists()) {
            file.delete();
        }

        return deployPathFile;
    }


    public void createTrainFile() {
        List<String> list = null;
        try {
            list = FileUtils.readLines(new File(tagPath), "utf-8");
            if (list != null) {

                List<String> resList = new ArrayList<>();
                for (String str : list) {
                    if (StringUtils.isNotBlank(str)) {
                        String[] data = str.split(" ");
                        resList.add(data[1]);
                    }
                }

                String xunlianPath = trainDataPath + "train";
                String testPath = trainDataPath + "test";
                for (String str : resList) {
                    String path = deployPath + str;


                    Map<String, Object> param = new HashMap<>();
                    param.put("tagId", Integer.parseInt(str));
                    param.put("start", 0);
                    param.put("num", 80);
                    List<ImgEntity> trainList = imgWorkMapper.find(param);

                    for (ImgEntity entity : trainList) {

                        String name = entity.getImg();

                        String srcPath = path + File.separator + name;
                        String destPath = xunlianPath + File.separator + name;

                        FileUtils.copyFile(new File(srcPath), new File(destPath));
                        FileUtils.write(new File(trainTagPath), name + " " + str + "\r\n", true);
                    }


                    Map<String, Object> param2 = new HashMap<>();
                    param2.put("tagId", Integer.parseInt(str));
                    param2.put("start", 81);
                    param2.put("num", 40);
                    List<ImgEntity> trainList2 = imgWorkMapper.find(param2);

                    for (ImgEntity entity : trainList2) {

                        String name = entity.getImg();

                        String srcPath = path + File.separator + name;
                        String destPath = testPath + File.separator + name;

                        FileUtils.copyFile(new File(srcPath), new File(destPath));
                        FileUtils.write(new File(trainTagTestPath), name + " " + str + "\r\n", true);
                    }


                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void createTrainFile2() throws IOException {
        Map<String, Object> param = new HashMap<>();
        param.put("tagId", 2);
        param.put("start", 0);
        param.put("num", 400);
        List<ImgEntity> trainList = imgWorkMapper.find(param);

        String xunlianPath = trainDataPath + "train";
        String testPath = trainDataPath + "test";


        for (ImgEntity entity : trainList) {

            String name = entity.getImg();
            String path = deployPath + "2";
            String srcPath = path + File.separator + name;
            String destPath = xunlianPath + File.separator + name;

            FileUtils.copyFile(new File(srcPath), new File(destPath));
            FileUtils.write(new File(trainTagPath), name + " " + 2 + "\r\n", true);
        }


        Map<String, Object> param2 = new HashMap<>();
        param2.put("tagId", 2);
        param2.put("start", 0);
        param2.put("num", 400);
        List<ImgEntity> trainList2 = imgWorkMapper.findNotIn(param2);

        for (ImgEntity entity : trainList2) {

            String name = entity.getImg();
            String path2 = deployPath + entity.getTagId();

            String srcPath = path2 + File.separator + name;
            String destPath = xunlianPath + File.separator + name;

            FileUtils.copyFile(new File(srcPath), new File(destPath));
            FileUtils.write(new File(trainTagPath), name + " " + 1 + "\r\n", true);
        }


        Map<String, Object> paramTest1 = new HashMap<>();
        paramTest1.put("tagId", 2);
        paramTest1.put("start", 401);
        paramTest1.put("num", 120);
        List<ImgEntity> testList1 = imgWorkMapper.find(paramTest1);

        for (ImgEntity entity : testList1) {

            String name = entity.getImg();
            String path = deployPath + "2";
            String srcPath = path + File.separator + name;
            String destPath = testPath + File.separator + name;

            FileUtils.copyFile(new File(srcPath), new File(destPath));
            FileUtils.write(new File(trainTagTestPath), name + " " + 2 + "\r\n", true);
        }


        Map<String, Object> paramTest2 = new HashMap<>();
        paramTest2.put("tagId", 2);
        paramTest2.put("start", 401);
        paramTest2.put("num", 120);
        List<ImgEntity> testList2 = imgWorkMapper.findNotIn(paramTest2);

        for (ImgEntity entity : testList2) {

            String name = entity.getImg();
            String path2 = deployPath + entity.getTagId();

            String srcPath = path2 + File.separator + name;
            String destPath = testPath + File.separator + name;

            FileUtils.copyFile(new File(srcPath), new File(destPath));
            FileUtils.write(new File(trainTagTestPath), name + " " + 1 + "\r\n", true);
        }

    }

    public static void main(String[] args) {
        String path = "I:\\doc\\tutu\\deploy\\5";
        File ff = new File(path);
        File[] list = ff.listFiles();
        for (File fe : list) {

            String name = fe.getName();

            String xinName = name.substring(0, name.length() - 4);
            fe.renameTo(new File(path + File.separator + xinName));
        }

//        String str= "00a6dc4a85bf4f84bfdf2ccc6d2e5101.jpg.jpg";
//        System.out.println(str.substring(0,str.length()-4));
    }
}
