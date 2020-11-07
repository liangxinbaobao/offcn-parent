package com.offcn.order.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.vo.req.OrderSubmitInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lhq
 * @Date: 2020/10/26 10:58
 * @Description:
 */
@Api(tags = "(保存订单)")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    @ApiOperation("保存订单")
    @PostMapping("/createOrder")
    public AppResponse<TOrder> createOrder(@RequestBody OrderSubmitInfoVo orderSubmitInfoVo) {
        //登录判断
        String memberId = redisTemplate.opsForValue().get(orderSubmitInfoVo.getAccessToken());
        if (memberId == null) {
            AppResponse<TOrder> response = AppResponse.fail(null);
            response.setMsg("登录失败，无此操作权限");
            return response;
        }

        TOrder order = null;
        try {
            order = orderService.saveOrder(orderSubmitInfoVo);
            return AppResponse.ok(order);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail(null);
        }
    }
}
