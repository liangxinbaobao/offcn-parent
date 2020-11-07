package com.offcn.project.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import com.offcn.utils.OSSTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lhq
 * @Date: 2020/10/23 10:22
 * @Description: 项目模块控制器
 */
@Api(tags = "查询项目信息")
@RestController
@RequestMapping("/project")
public class ProjectInfoController {

    @Autowired
    private OSSTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;

    @PostMapping("/uploadFile")
    public AppResponse<Object> uploadFile(@RequestParam("file") MultipartFile[] multipartFiles) {

        Map map = new HashMap();
        List urls = new ArrayList<>();
        //判断上传控件数据是否为空
        if (null != multipartFiles && multipartFiles.length > 0) {
            for (MultipartFile file : multipartFiles) {
                String fileName = file.getOriginalFilename();     //该文件的名称.扩展名
                try {
                    String uploadUrl = ossTemplate.upload(file.getInputStream(), fileName);
                    urls.add(uploadUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        map.put("urls", urls);
        return AppResponse.ok(map);
    }

    @ApiOperation("获取项目回报列表")
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnList(@PathVariable(name = "projectId") Integer projectId) {
        List<TReturn> returnList = projectInfoService.getReturnList(projectId);
        return AppResponse.ok(returnList);
    }


    @ApiOperation("获取系统所有的项目")
    @GetMapping("/findAllProject")
    public AppResponse<List<ProjectVo>> findAllProject() {
        List<ProjectVo> projectVoList = new ArrayList<>();

        //1.查询全部项目
        List<TProject> projectList = projectInfoService.findProjectAll();
        //2.遍历项目
        for (TProject project : projectList) {
            ProjectVo projectVo = new ProjectVo();
            BeanUtils.copyProperties(project, projectVo);
            // 遍历图片
            List<TProjectImages> projectImagesList = projectInfoService.getProjectImages(project.getId());
            for (TProjectImages projectImages : projectImagesList) {
                //设置头部图片信息
                if (projectImages.getImgtype() == 0) {
                    projectVo.setHeaderImage(projectImages.getImgurl());
                }
            }
            projectVoList.add(projectVo);

        }
        //3.返回项目列表
        return AppResponse.ok(projectVoList);
    }


    @ApiOperation("获取项目信息详情")
    @GetMapping("/findProjectById")
    public AppResponse<ProjectDetailVo> findProjectById(Integer projectId) {
        //1.查询项目信息
        TProject project = projectInfoService.findProjectById(projectId);
        ProjectDetailVo projectDetailVo = new ProjectDetailVo();
        BeanUtils.copyProperties(project, projectDetailVo);
        //2.查询项目图片列表
        List<TProjectImages> projectImagesList = projectInfoService.getProjectImages(projectId);
        List<String> detailImages = projectDetailVo.getDetailsImage();
        if (CollectionUtils.isEmpty(detailImages)) {
            detailImages = new ArrayList<>();
        }
        for (TProjectImages projectImages : projectImagesList) {
            //判断图片类型是否是头部图片(单独设置)还是详情图片（集合）
            if (projectImages.getImgtype() == 0) {
                projectDetailVo.setHeaderImage(projectImages.getImgurl());
            } else {
                detailImages.add(projectImages.getImgurl());
            }
        }
        projectDetailVo.setDetailsImage(detailImages);

        //3.查询项目回报列表并设置
        List<TReturn> returnList = projectInfoService.getReturnList(projectId);
        projectDetailVo.setProjectReturns(returnList);
        return AppResponse.ok(projectDetailVo);

    }

    @ApiOperation("获取系统所有的项目标签")
    @GetMapping("/findAllTag")
    public AppResponse<List<TTag>> findAllTag(){
        List<TTag> tagList = projectInfoService.findAllTag();
        return AppResponse.ok(tagList);
    }


    @ApiOperation("获取系统所有的项目分类")
    @GetMapping("/findAllType")
    public AppResponse<List<TType>> findAllType(){
        List<TType> ttypeList = projectInfoService.findAllType();
        return AppResponse.ok(ttypeList);
    }



    @ApiOperation("获取回报信息")
    @GetMapping("/returns/info/{returnId}")
    public AppResponse<TReturn> findTReturnById(@PathVariable(name="returnId") Integer returnId){
        TReturn tReturn = projectInfoService.findTReturnById(returnId);
        return AppResponse.ok(tReturn);
    }




}
