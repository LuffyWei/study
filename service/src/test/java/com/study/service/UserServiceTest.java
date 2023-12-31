package com.study.service;


import com.alibaba.fastjson.JSON;
import com.study.config.H2Config;
import com.study.dal.mapper.UserInfoMapper;
import com.study.dal.mapper.object.UserInfoDO;
import com.study.service.user.UserService;
import com.study.service.user.UserServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(
        classes = {
                H2Config.class,
                UserServiceImpl.class
        }
)
@RunWith(SpringRunner.class)
public class UserServiceTest {

        @Autowired
        private UserInfoMapper userInfoMapper;

        @Autowired
        private UserService userService;

        //针对需要模拟输出的bean，可以先mock该bean，然后利用mokito工具模拟调用该的方法
//        @MockBean
//        private UserInfoMapper userInfoMapper;

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        private static final String NAME = "weixun";

        @Before
        public void setup() {
                UserInfoDO userInfoDO = new UserInfoDO();
                userInfoDO.setName(NAME);
                userInfoDO.setRemark("我炸死你啊啊啊");
                userService.createUser(userInfoDO);
                System.out.println("测试用户创建成功："+JSON.toJSONString(userInfoDO));
        }

        @Test
        public void create_success() {
                UserInfoDO createInfo = userService.queryByName(NAME);
                System.out.println("用户创建成功，创建结果："+JSON.toJSONString(createInfo));
        }

        @Test
        public void mokito_test() {
                Mockito.doReturn(null).when(userInfoMapper).selectByCondition(null);
                UserInfoDO createInfo = userService.queryByName(NAME);
                System.out.println("用户创建成功，创建结果："+JSON.toJSONString(createInfo));
        }

}
