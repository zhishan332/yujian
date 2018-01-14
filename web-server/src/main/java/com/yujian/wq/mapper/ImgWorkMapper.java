package com.yujian.wq.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/7
 */
@Repository
public interface ImgWorkMapper {

    public int insertImgAndGetId(ImgEntity imgEntity);

    public int insertTag(TagEntity tagEntity);

    public List<ImgEntity> find(Map<String, Object> param);

    public List<ImgEntity> findNotIn(Map<String, Object> param);

}
