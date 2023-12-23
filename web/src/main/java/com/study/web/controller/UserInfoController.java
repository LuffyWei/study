package com.study.web.controller;

import com.alibaba.fastjson.JSON;
import com.study.dal.mapper.object.UserInfoDO;
import com.study.service.user.UserService;
import com.study.web.request.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zelda/user")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @RequestMapping("/create")
    public String createUser(@RequestParam("name") String name, @RequestParam("remark") String remark) {
        try {
            UserInfoDO userInfoDO = new UserInfoDO();
            userInfoDO.setName(name);
            userInfoDO.setRemark(remark);
            userService.createUser(userInfoDO);
            return "用户信息创建成功；信息="+ JSON.toJSONString(userInfoDO);
        } catch (Throwable e) {
            return "用户信息创建失败，"+ e.getMessage();
        }

    }

    @RequestMapping("/create2")
    public String createUser(@RequestBody UserInfo userInfo) {
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setName(userInfo.getName());
        userInfoDO.setRemark(userInfo.getRemark());
        userService.createUser(userInfoDO);
        return "用户信息创建成功；信息="+ JSON.toJSONString(userInfoDO);
    }

    @RequestMapping("/query")
    public String queryUser(@RequestParam("id") Long id) {
        UserInfoDO userInfoDO = userService.queryById(id);
        return "用户信息查询成功；信息="+ JSON.toJSONString(userInfoDO);
    }
}
