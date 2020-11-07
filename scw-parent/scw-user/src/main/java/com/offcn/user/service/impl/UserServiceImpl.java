package com.offcn.user.service.impl;

import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.pojo.TMemberAddressExample;
import com.offcn.user.pojo.TMemberExample;
import com.offcn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 15:06
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TMemberMapper memberMapper;


    @Autowired
    private TMemberAddressMapper memberAddressMapper;


    /**
     * 注册会员
     *
     * @param tMember
     */
    @Override
    public void registerUser(TMember tMember) {
        //1.判断手机号是否重复
        TMemberExample tMemberExample = new TMemberExample();
        TMemberExample.Criteria criteria = tMemberExample.createCriteria();
        criteria.andLoginacctEqualTo(tMember.getLoginacct());
        long count = memberMapper.countByExample(tMemberExample);
        if (count > 0) {
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        //2.对密码进行加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(tMember.getUserpswd());
        tMember.setUserpswd(password);
        //3.设置属性
        tMember.setAuthstatus("0");   //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
        tMember.setAccttype("2");  //账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
        tMember.setUsertype("0"); // 用户类型: 0 - 个人， 1 - 企业
        tMember.setUsername(tMember.getLoginacct());

        //4.执行保存
        memberMapper.insertSelective(tMember);

    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public TMember login(String username, String password) {
        //1.根据用户名查询记录
        TMemberExample tMemberExample = new TMemberExample();
        TMemberExample.Criteria criteria = tMemberExample.createCriteria();
        criteria.andLoginacctEqualTo(username);
        List<TMember> list = memberMapper.selectByExample(tMemberExample);
        if (!CollectionUtils.isEmpty(list)) {
            //2.匹配密码（加密）
            TMember tMember = list.get(0);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean b = encoder.matches(password,tMember.getUserpswd());
            return b ? tMember : null;
        }
        return null;
    }

    /**
     * 根据主键查询用户信息
     *
     * @param id
     * @return
     */
    @Override
    public TMember findMemberById(Integer id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取用户收货地址
     *
     * @param memberId
     * @return
     */
    @Override
    public List<TMemberAddress> findAddressList(Integer memberId) {
        TMemberAddressExample tMemberAddressExample = new TMemberAddressExample();
        TMemberAddressExample.Criteria criteria = tMemberAddressExample.createCriteria();
        criteria.andMemberidEqualTo(memberId);
        return memberAddressMapper.selectByExample(tMemberAddressExample);
    }
}
