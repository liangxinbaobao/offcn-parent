package com.offcn.user.vo.req;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 14:44
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户注册实体")
public class UserRegistVo {
    @ApiModelProperty("手机号")
    private String loginacct;
    @ApiModelProperty("密码")
    private String userpswd;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("验证码")
    private String code;

}
