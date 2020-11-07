package com.offcn.user.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 15:41
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("返回用户信息实体")
public class UserResponseVo implements Serializable {


    private String loginacct;
    private String username;

    private String email;

    private String authstatus;

    private String usertype;

    private String realname;

    private String cardnum;

    private String accttype;

    @ApiModelProperty(value = "登录令牌，以后所有请求都需要匹配")
    private String accessToken;
}
