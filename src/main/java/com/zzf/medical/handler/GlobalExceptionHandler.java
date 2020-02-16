package com.zzf.medical.handler;

import com.zzf.medical.common.CommonError;
import com.zzf.medical.common.CommonRes;
import com.zzf.medical.enums.EmCommonError;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.util.CommonUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public CommonRes doError(HttpServletRequest servletRequest,
                             HttpServletResponse servletResponse,
                             Exception ex){
        if(ex instanceof BusinessException){
            return CommonRes.create(((BusinessException) ex).getCommonErr(),"fail");
        }else if(ex instanceof NoHandlerFoundException){
            //找不到执行的路径操作
            CommonError commonError = new CommonError(EmCommonError.NO_HANDLER_FOUND);
            return CommonRes.create(commonError,"fail");
        }else if(ex instanceof MissingServletRequestParameterException){
            //请求参数错误
            CommonError commonError = new CommonError(EmCommonError.BIND_EXCEPTION_ERROR);
            return CommonRes.create(commonError,"fail");
        }else if(ex instanceof MethodArgumentTypeMismatchException){
            //数据库格式不正确MYSQL_FORMAT_ERROR
            CommonError commonError = new CommonError(EmCommonError.MYSQL_FORMAT_ERROR);
            return CommonRes.create(commonError,"fail");
        }else if(ex instanceof MyBatisSystemException){
            //很有可能是没有token
            CommonError commonError = new CommonError(EmCommonError.TOKEN_ERROR);
            return CommonRes.create(commonError,"fail");
        }else if(ex instanceof HttpRequestMethodNotSupportedException){
            //请求方法不正确
            CommonError commonError = new CommonError(EmCommonError.REQUEST_METHOD_NOT_SUPPORTED);
            return CommonRes.create(commonError);
        }else if(ex instanceof MethodArgumentNotValidException){
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            Objects.requireNonNull(bindingResult.getFieldError());
            CommonError commonError = new CommonError(EmCommonError.PARAMETER_VALIDATION_ERROR);
            commonError.setErrMsg(CommonUtils.processErrorString(bindingResult));
            return CommonRes.create(commonError);
        }
        else{
            CommonError commonError = new CommonError(EmCommonError.UNKNOWN_ERROR);
            return CommonRes.create(commonError,"fail");
        }
    }
}
