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

    public List<ImgEntity> findRandom(Map<String, Object> param);

    public int findTotal();

    public ImgChainEntity findChain(String chain);

    public int insertChain(ImgChainEntity imgChainEntity);

    public int updateIncreaseChain(ImgChainEntity imgChainEntity);

    public int updateReduceChain(ImgChainEntity imgChainEntity);

    public ImgEntity findChainByTag(Map<String, Object> param);

    public List<ImgChainEntity> findChainByTime(Map<String, Object> param);


    public List<ImgEntity> findByChain(String chain);

    public List<ImgChainEntity> findChainCommon(Map<String, Object> param);

    public int deleteChain(String chain);

    public int deleteImgChain(String chain);

    public int deleteImg(String img);

    public int updateTag(Map<String, Object> param);

    public ImgEntity getImgByMd5(Map<String, Object> param);

    public List<ImgChainEntity> report();

}
