package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnum;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.contants.ProjectContants;
import com.offcn.project.pojo.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/10/23 11:33
 * @Description:
 */
@RestController
@RequestMapping("/project")
@Api(tags = "项目基本功能模块（创建、保存、项目信息获取、文件上传等）")
@Slf4j
public class ProjectCreateController {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectCreateService projectCreateService;


    @ApiOperation("项目发起第一步、同意协议，发布项目")
    @PostMapping("/init")
    public AppResponse<Object> initCreateProject(String accessToken) {
        //1.通过登录令牌获得会员编号
        String memberId = redisTemplate.opsForValue().get(accessToken);
        //2.判断编号是否存在
        if (memberId == null) {
            return AppResponse.fail("无此权限，请先登录");
        }
        int id = Integer.parseInt(memberId);
        //3.调用发布项目的方法
        String projectToken = projectCreateService.initCreateProject(Integer.parseInt(memberId));
        //4.返回项目令牌
        return AppResponse.ok(projectToken);
    }




    @ApiOperation("项目发起第二步、收集表单数据")
    @PostMapping("/saveBaseInfo")
    public AppResponse<Object> saveBaseInfo(@RequestBody ProjectBaseInfoVo projectBaseInfoVo) {
        //1.通过项目令牌从缓存中获得临时项目对象
        String projectContext = redisTemplate.opsForValue().get(ProjectContants.TEMP_PROJECT_PREFIX + projectBaseInfoVo.getProjectToken());
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(projectContext, ProjectRedisStorageVo.class);
        //2.复制对象
        BeanUtils.copyProperties(projectBaseInfoVo, projectRedisStorageVo);
        String jsonStr = JSON.toJSONString(projectRedisStorageVo);
        //3.将临时对象重新放回到缓存中
        redisTemplate.opsForValue().set(ProjectContants.TEMP_PROJECT_PREFIX + projectBaseInfoVo.getProjectToken(), jsonStr);
        return AppResponse.ok("保存成功");
    }




    @ApiOperation("项目发起第三步、收集回报增量的信息")
    @PostMapping("/saveReturnInfo")
    public AppResponse<Object> saveReturnInfo(@RequestBody List<ProjectReturnVo> returnVoList) {
        //1.通过项目令牌从缓存中获得临时对象
        String projectToken = returnVoList.get(0).getProjectToken();
        String projectContext = redisTemplate.opsForValue().get(ProjectContants.TEMP_PROJECT_PREFIX + projectToken);
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(projectContext, ProjectRedisStorageVo.class);
        List<TReturn> returnList = new ArrayList();
        //2.遍历回报VO集合
        for (ProjectReturnVo projectReturnVo : returnVoList) {
            TReturn tReturn = new TReturn();
            BeanUtils.copyProperties(projectReturnVo, tReturn);
            returnList.add(tReturn);
        }
        //3.将回报信息设置回临时对象
        projectRedisStorageVo.setProjectReturns(returnList);
        //4.重新保存临时对象
        String jsonStr = JSON.toJSONString(projectRedisStorageVo);
        redisTemplate.opsForValue().set(ProjectContants.TEMP_PROJECT_PREFIX + projectToken, jsonStr);
        return AppResponse.ok("保存回报信息成功");
    }



    @ApiOperation("项目发起第四步、保存项目")
    @ApiImplicitParams({@ApiImplicitParam(name = "accessToken", value = "登录令牌", required = true),
            @ApiImplicitParam(name = "projectToken", value = "项目令牌", required = true),
            @ApiImplicitParam(name = "ops", value = "操作状态 0、草稿 1、提交审核", required = true)})
    @PostMapping("/saveProjectInfo")
    public AppResponse<Object> saveProjectInfo(String accessToken, String projectToken, String ops) {
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (null == memberId) {
            return AppResponse.fail("登录失败，无此操作权限");
        }
        //1.通过项目令牌从缓存中获得临时对象
        String projectContext = redisTemplate.opsForValue().get(ProjectContants.TEMP_PROJECT_PREFIX + projectToken);
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(projectContext, ProjectRedisStorageVo.class);
        //2.判断操作状态
        if (ops.equals("1")) {  //提交审核状态
            ProjectStatusEnum statusEnum = ProjectStatusEnum.SUBMIT_AUTH;
            projectCreateService.saveProjectInfo(statusEnum, projectRedisStorageVo);
            return AppResponse.ok(null);
        } else if (ops.equals("0")) {   //草稿状态
            ProjectStatusEnum statusEnum = ProjectStatusEnum.DRAFT;
            projectCreateService.saveProjectInfo(statusEnum, projectRedisStorageVo);
            return AppResponse.ok(null);
        } else {
            return AppResponse.fail("暂不支持此操作");
        }
    }



}

