package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.component.SmsTemplate;
import com.offcn.user.pojo.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.resp.UserResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 14:17
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户登录模块")
public class UserLoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private UserService userService;


    @ApiOperation("发送短信验证码")
    @ApiImplicitParam(name = "mobile", value = "手机号", required = true)
    @GetMapping("/sendSms")
    public AppResponse sendSms(String mobile) {
        //1.生成随机验证码
        String code = UUID.randomUUID().toString().substring(0, 4);
        //2.保存到缓存中
        stringRedisTemplate.opsForValue().set(mobile, code, 60 * 5, TimeUnit.SECONDS);
        //3.发送验证码
        String sendCode = smsTemplate.sendSms(mobile, code);
        if (StringUtils.isEmpty(sendCode)) {
            return AppResponse.fail("发送短信失败");
        }
        return AppResponse.ok(sendCode);

    }

    @ApiOperation("用户登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)})
    @GetMapping("/login")
    public AppResponse<UserResponseVo> login(String username, String password) {
        //1、判断用户名和密码是否匹配
        TMember tMember = userService.login(username, password);
        if (tMember == null) {
            AppResponse response = new AppResponse();
            response.setMsg("登录失败");
            return response;
        }
        //2.生成令牌
        String accessToken = UUID.randomUUID().toString().replace("-", "");
        UserResponseVo userResponseVo = new UserResponseVo();
        BeanUtils.copyProperties(tMember, userResponseVo);
        userResponseVo.setAccessToken(accessToken);

        //3.将令牌放入到缓存
        stringRedisTemplate.opsForValue().set(accessToken, tMember.getId() + "", 2, TimeUnit.HOURS);

        return AppResponse.ok(userResponseVo);
    }
}
