package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.User;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.req.UserRegistVo;
import com.offcn.user.vo.resp.UserResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 11:37
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Api(tags = "第一个Swagger测试")
@Slf4j
public class UserController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("测试方法hello")
    @ApiImplicitParam(name = "name", value = "姓名", required = true)
    @GetMapping("/hello")
    public String hello(String name) {
        return "OK" + name;
    }

    @ApiOperation("保存用户")
    @ApiImplicitParam(name = "user", value = "用户对象", required = true)
    @PostMapping("/saveUser")
    public User saveUser(User user) {
        return user;
    }


    @ApiOperation("注册用户")
    @PostMapping("/registerUser")
    public AppResponse registerUser(UserRegistVo userRegistVo) {
        //1.匹配验证码
        String code = redisTemplate.opsForValue().get(userRegistVo.getLoginacct());
        try {
            if (StringUtils.isNotEmpty(code)) {
                if (userRegistVo.getCode().equalsIgnoreCase(code)) {
                    //2.复制对象
                    TMember tMember = new TMember();
                    BeanUtils.copyProperties(userRegistVo, tMember);
                    log.debug("用户信息注册成功：{}", tMember.getLoginacct());

                    //3.执行保存
                    userService.registerUser(tMember);
                    //4.清空缓存中的验证码
                    redisTemplate.delete(userRegistVo.getLoginacct());

                    return AppResponse.ok("保存成功");
                } else {
                    return AppResponse.fail("验证码过期，请重新获取验证码");
                }
            } else {
                return AppResponse.fail("验证码为空！");
            }

        } catch (BeansException e) {
            e.printStackTrace();
            return AppResponse.fail("保存失败");
        }


    }

    @ApiOperation("根据编号查询用户")
    @GetMapping("/findMemberById/{id}")
    public AppResponse<UserResponseVo> findMemberById(@PathVariable(name = "id") Integer id) {
        TMember member = userService.findMemberById(id);
        UserResponseVo userResponseVo = new UserResponseVo();
        //复制对象
        BeanUtils.copyProperties(member, userResponseVo);
        return AppResponse.ok(userResponseVo);
    }


}
