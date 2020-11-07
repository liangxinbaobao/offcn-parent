package com.offcn.order.service;

import com.offcn.order.pojo.TOrder;
import com.offcn.order.vo.req.OrderSubmitInfoVo;

/**
 * @Auther: lhq
 * @Date: 2020/10/26 10:39
 * @Description:
 */
public interface OrderService {

    /**
     * 保存订单
     * @param orderSubmitInfoVo
     * @return
     */
    public TOrder saveOrder(OrderSubmitInfoVo orderSubmitInfoVo);
}
