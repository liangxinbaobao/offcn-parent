package com.offcn.project.service;

import com.offcn.project.pojo.*;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/10/26 10:13
 * @Description:  项目信息接口
 */
public interface ProjectInfoService {


    /**
     * 获取项目回报增量列表
     * @param projectId
     * @return
     */
    public List<TReturn> getReturnList(Integer projectId);


    /**
     * 查询所有项目
     * @return
     */
    public List<TProject>  findProjectAll();


    /**
     * 查询项目图片列表
     * @param projectId
     * @return
     */
    public List<TProjectImages> getProjectImages(Integer projectId);

    /**
     * 获得项目详情信息
     * @param projectId
     * @return
     */
    public TProject findProjectById(Integer projectId);


    /**
     * 查询全部标签
     * @return
     */
    public List<TTag> findAllTag();

    /**
     * 查询全部分类
     * @return
     */
    public List<TType> findAllType();


    /**
     * 查询回报增量详细信息
     * @param returnId
     * @return
     */
    public TReturn findTReturnById(Integer returnId);
}
