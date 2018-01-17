package com.yujian.wq.mapper;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by wangqing on 2018/1/17.
 */
@Repository
public interface UserMapper {

    public UserEntity getUser(String openId);

    public int insertUser(UserEntity userEntity);

    public int reduceEnergy(String openId);

    public int increaseEnergy(Map<String, Object> param);


}
