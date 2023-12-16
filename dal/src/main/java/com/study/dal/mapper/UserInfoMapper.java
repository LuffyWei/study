package com.study.dal.mapper;

import com.study.dal.mapper.object.UserInfoDO;
import com.study.dal.mapper.query.UserInfoCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserInfoMapper {

    int insert(UserInfoDO userInfoDO);

    int batchInsert(List<UserInfoDO> list);

    int update(UserInfoDO userInfoDO);

    int batchUpdate(List<UserInfoDO> list);

    int deleteById(Long id);

    List<UserInfoDO> selectByCondition(UserInfoCondition condition);
}
