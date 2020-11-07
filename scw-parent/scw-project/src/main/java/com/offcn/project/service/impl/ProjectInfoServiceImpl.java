package com.offcn.project.service.impl;

import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/10/26 10:15
 * @Description:
 */
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {


    @Autowired
    private TReturnMapper returnMapper;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TTagMapper tTagMapper;


    @Autowired
    private TTypeMapper typeMapper;

    /**
     * 获取项目回报增量列表
     *
     * @param projectId
     * @return
     */
    @Override
    public List<TReturn> getReturnList(Integer projectId) {
        TReturnExample tReturnExample = new TReturnExample();
        TReturnExample.Criteria criteria = tReturnExample.createCriteria();
        criteria.andProjectidEqualTo(projectId);
        return returnMapper.selectByExample(tReturnExample);
    }

    /**
     * 查询所有项目
     *
     * @return
     */
    @Override
    public List<TProject> findProjectAll() {
        return projectMapper.selectByExample(null);
    }

    /**
     * 查询项目图片列表
     *
     * @param projectId
     * @return
     */
    @Override
    public List<TProjectImages> getProjectImages(Integer projectId) {
        TProjectImagesExample projectImagesExample = new TProjectImagesExample();
        TProjectImagesExample.Criteria criteria = projectImagesExample.createCriteria();
        criteria.andProjectidEqualTo(projectId);
        return projectImagesMapper.selectByExample(projectImagesExample);
    }

    /**
     * 获得项目详情信息
     *
     * @param projectId
     * @return
     */
    @Override
    public TProject findProjectById(Integer projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    /**
     * 查询全部标签
     *
     * @return
     */
    @Override
    public List<TTag> findAllTag() {
        return tTagMapper.selectByExample(null);
    }

    /**
     * 查询全部分类
     *
     * @return
     */
    @Override
    public List<TType> findAllType() {
        return typeMapper.selectByExample(null);
    }

    /**
     * 查询回报增量详细信息
     *
     * @param returnId
     * @return
     */
    @Override
    public TReturn findTReturnById(Integer returnId) {
        return returnMapper.selectByPrimaryKey(returnId);
    }
}
