package com.yujian.wq.mapper;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by wangqing on 2018/1/17.
 */
public class UserEntity implements Serializable {

    private Integer id;
    private String openId;
    private Integer energy;
    private Timestamp createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getEnergy() {
        return energy;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
