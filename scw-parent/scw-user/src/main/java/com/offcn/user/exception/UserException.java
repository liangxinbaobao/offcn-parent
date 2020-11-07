package com.offcn.user.exception;

import com.offcn.user.enums.UserExceptionEnum;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 15:02
 * @Description:
 */
public class UserException extends RuntimeException{

    public UserException(UserExceptionEnum exceptionEnum){
        super(exceptionEnum.getMsg());
    }
}
