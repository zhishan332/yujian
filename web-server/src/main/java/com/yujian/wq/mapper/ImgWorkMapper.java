package com.yujian.wq.mapper;

import org.springframework.stereotype.Repository;

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
}
