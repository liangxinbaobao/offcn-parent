package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnum;
import com.offcn.project.contants.ProjectContants;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: lhq
 * @Date: 2020/10/23 11:24
 * @Description:
 */
@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;

    @Autowired
    private TReturnMapper returnMapper;

    /**
     * 同意协议，发布项目
     *
     * @param memberId 会员编号
     * @return
     */
    @Override
    public String initCreateProject(Integer memberId) {
        //1.向临时对象中设置memberId
        ProjectRedisStorageVo projectRedisStorageVo = new ProjectRedisStorageVo();
        projectRedisStorageVo.setMemberid(memberId);
        //2.生成项目的临时令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        //3.现将对象转换成JSON结构字符串
        String jsonStr = JSON.toJSONString(projectRedisStorageVo);
        //4.将临时项目对象存储到缓存中
        redisTemplate.opsForValue().set(ProjectContants.TEMP_PROJECT_PREFIX + token, jsonStr);
        return token;
    }









    /**
     * 保存项目到数据库
     *
     * @param statusEnum
     * @param projectRedisStorageVo
     */
    @Override
    public void saveProjectInfo(ProjectStatusEnum statusEnum, ProjectRedisStorageVo projectRedisStorageVo) {
        //1.保存项目对象
        /**
         * 保存项目的基本信息，获取到数据库的id
         * 页面考过来的：name,remark,money,day,memberid
         * 我们自己后来操作：status,deploydate,createdate
         * 自动化修改：supportmoney（已支持的金额），supporter（支持人数） ,completion（完成度）,follower（关注者人数）
         */
        TProject tProject = new TProject();
        BeanUtils.copyProperties(projectRedisStorageVo, tProject);
        tProject.setMoney(projectRedisStorageVo.getMoney().longValue());
        //完成状态
        tProject.setStatus(statusEnum.getCode()+"");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = sdf.format(new Date());
        //发布日期
        tProject.setDeploydate(time);
        //创建时间
        tProject.setCreatedate(time);
        projectMapper.insertSelective(tProject);
        //获得项目对象的ID
        Integer projectId = tProject.getId();
        //2.保存图片（头图，详情图）
        TProjectImages headImage = new TProjectImages(null, projectId, projectRedisStorageVo.getHeaderImage(), ProjectImageTypeEnume.HEADER.getCode().byteValue());
        projectImagesMapper.insertSelective(headImage);
        if (!CollectionUtils.isEmpty(projectRedisStorageVo.getDetailsImage())) {
            for (String detailImageUrl : projectRedisStorageVo.getDetailsImage()) {
                TProjectImages detailImage = new TProjectImages(null, projectId, detailImageUrl, ProjectImageTypeEnume.DETAILS.getCode().byteValue());
                projectImagesMapper.insertSelective(detailImage);
            }
        }
        //3.保存分类关联
        if (!CollectionUtils.isEmpty(projectRedisStorageVo.getTypeids())) {
            for (Integer typeId : projectRedisStorageVo.getTypeids()) {
                TProjectType tProjectType = new TProjectType(null, projectId, typeId);
                projectTypeMapper.insertSelective(tProjectType);
            }
        }
        //4.保存标签关联
        if (!CollectionUtils.isEmpty(projectRedisStorageVo.getTagids())) {
            for (Integer tagId : projectRedisStorageVo.getTagids()) {
                TProjectTag tProjectTag = new TProjectTag(null, projectId, tagId);
                projectTagMapper.insertSelective(tProjectTag);
            }
        }
        //5.保存回报增量信息
        if (!CollectionUtils.isEmpty(projectRedisStorageVo.getProjectReturns())) {
            for (TReturn tReturn : projectRedisStorageVo.getProjectReturns()) {
                //设置项目ID
                tReturn.setProjectid(projectId);
                returnMapper.insertSelective(tReturn);
            }
        }

        //6.清空缓存
        redisTemplate.delete(ProjectContants.TEMP_PROJECT_PREFIX + projectRedisStorageVo.getProjectToken());

    }
}
