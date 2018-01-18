package com.yujian.wq.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by wangqing on 2018/1/18.
 */
@Repository
public interface StoryMapper {

    public List<StoryEntity> find(Map<String, Object> param);

    public StoryEntity get(Integer id);
}
