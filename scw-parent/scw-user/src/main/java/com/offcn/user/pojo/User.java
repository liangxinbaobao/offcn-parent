package com.offcn.user.pojo;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 11:38
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("测试实体")
public class User implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "电子邮件")
    private String email;
}
