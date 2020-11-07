package com.offcn.user.service;

import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 15:03
 * @Description:        用户模块接口
 */
public interface UserService {

    /**
     * 注册会员
     * @param tMember
     */
    public void registerUser(TMember tMember);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public TMember login(String username,String password);

    /**
     * 根据主键查询用户信息
     * @param id
     * @return
     */
    public TMember findMemberById(Integer id);


    /**
     * 查询收货地址列表
     * @param memberId
     * @return
     */
    public List<TMemberAddress> findAddressList(Integer memberId);
}
