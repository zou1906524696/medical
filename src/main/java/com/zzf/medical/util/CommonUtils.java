package com.zzf.medical.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class CommonUtils {
    /**
     * 错误拼接成字符串返回给前端页面
     * **/
    public static String processErrorString(BindingResult bindingResult){
        if(!bindingResult.hasErrors()){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(FieldError fieldError:bindingResult.getFieldErrors()){
            stringBuilder.append(fieldError.getDefaultMessage()+",");
        }
        return stringBuilder.substring(0,stringBuilder.length()-1);
    }
}
