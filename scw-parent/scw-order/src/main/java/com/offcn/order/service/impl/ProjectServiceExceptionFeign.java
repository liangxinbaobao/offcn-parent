package com.offcn.order.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/10/26 10:33
 * @Description:
 */
@Component
public class ProjectServiceExceptionFeign implements ProjectServiceFeign {
    @Override
    public AppResponse<List<TReturn>> getReturnList(Integer projectId) {
        //降级处理
        AppResponse response = AppResponse.fail(null);
        //response.setMsg("调用项目模块（查询回报列表）发生异常，触发熔断");
        response.setMsg("调用远程服务器失败【订单】");

        return response;
    }
}
