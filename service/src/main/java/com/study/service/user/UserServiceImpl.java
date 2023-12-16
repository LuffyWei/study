package com.study.service.user;

import com.alibaba.fastjson.JSON;
import com.study.dal.mapper.UserInfoMapper;
import com.study.dal.mapper.object.UserInfoDO;
import com.study.dal.mapper.query.UserInfoCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public int createUser(UserInfoDO userInfoDO) {
        log.info("用户信息创建成功，userInfo={}", JSON.toJSONString(userInfoDO));
        return userInfoMapper.insert(userInfoDO);
    }

    @Override
    public int updateUser(UserInfoDO userInfoDO) {
        log.info("用户信息更新成功，userInfo={}", JSON.toJSONString(userInfoDO));
        return userInfoMapper.update(userInfoDO);
    }

    @Override
    public int deleteUser(Long userId) {
        log.info("用户信息删除成功，userId={}", userId);
        return userInfoMapper.deleteById(userId);
    }

    @Override
    public UserInfoDO queryById(Long id) {
        UserInfoCondition userInfoCondition = new UserInfoCondition();
        userInfoCondition.setId(id);
        List<UserInfoDO> userInfoDOList = userInfoMapper.selectByCondition(userInfoCondition);
        return userInfoDOList.stream().findAny().orElse(null);
    }
}
