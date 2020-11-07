package com.offcn.order.service.impl;

import com.offcn.dycommon.enums.OrderStatusEnum;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderSubmitInfoVo;
import com.offcn.order.vo.resp.TReturn;
import com.offcn.utils.AppDateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @Auther: lhq
 * @Date: 2020/10/26 10:40
 * @Description:
 */
@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;


    @Autowired
    private TOrderMapper orderMapper;

    /**
     * 保存订单
     *
     * @param orderSubmitInfoVo
     * @return
     */
    @Override
    public TOrder saveOrder(OrderSubmitInfoVo orderSubmitInfoVo) {
        TOrder order = new TOrder();

        //1.根据登录令牌得到当前登录用户的ID
        String memberId = redisTemplate.opsForValue().get(orderSubmitInfoVo.getAccessToken());
        //会员ID
        order.setMemberid(Integer.parseInt(memberId));
        //2.设置订单信息
        BeanUtils.copyProperties(orderSubmitInfoVo, order);
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        //订单编号
        order.setOrdernum(orderNum);
        //支付状态
        order.setStatus(OrderStatusEnum.UNPAY.getCode() + "");
        //创建时间
        order.setCreatedate(AppDateUtil.getFormatTime());
        //发票类型
        order.setInvoice(orderSubmitInfoVo.getInvoice().toString());
        //3.支持金额  查询汇报  微服务和微服务之间的调用
        AppResponse<List<TReturn>> response = projectServiceFeign.getReturnList(orderSubmitInfoVo.getProjectid());
        List<TReturn> returnList = response.getData();
        //默认取得第一个回报增量的信息
        TReturn tReturn = returnList.get(0);
        //回报数量*回报金额+运费（远程调用回报增量列表）
        Integer money = orderSubmitInfoVo.getRtncount()*tReturn.getSupportmoney()+tReturn.getFreight();
        order.setMoney(money);
        //4.执行保存
        orderMapper.insertSelective(order);

        return order;
    }
}
