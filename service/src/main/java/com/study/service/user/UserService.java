package com.study.service.user;

import com.study.dal.mapper.object.UserInfoDO;

public interface UserService {

    int createUser(UserInfoDO userInfoDO);

    int updateUser(UserInfoDO userInfoDO);

    int deleteUser(Long userId);

    UserInfoDO queryById(Long id);
}
