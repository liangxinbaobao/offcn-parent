package com.offcn.project.service;

import com.offcn.dycommon.enums.ProjectStatusEnum;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

/**
 * @Auther: lhq
 * @Date: 2020/10/23 11:23
 * @Description:
 */
public interface ProjectCreateService {


    /**
     * 同意协议，发布项目
     * @param memberId   会员编号
     * @return
     */
    public String initCreateProject(Integer memberId);

    /**
     * 保存项目到数据库
     * @param statusEnum
     * @param projectRedisStorageVo
     */
    public void  saveProjectInfo(ProjectStatusEnum statusEnum, ProjectRedisStorageVo projectRedisStorageVo);


}
