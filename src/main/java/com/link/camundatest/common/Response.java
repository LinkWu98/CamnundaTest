package com.link.camundatest.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "统一响应对象")
@Data
@Accessors(chain = true)
public class Response<T> {
    
    @Schema(description = "响应码", example = "200")
    private Integer code;
    
    @Schema(description = "响应消息", example = "操作成功")
    private String message;
    
    @Schema(description = "响应数据")
    private T data;
    
    public static <T> Response<T> success(T data) {
        return new Response<T>()
                .setCode(200)
                .setMessage("操作成功")
                .setData(data);
    }

    public static <T> Response<T> success() {
        return new Response<T>()
                .setCode(200)
                .setMessage("操作成功");
    }
    
    public static <T> Response<T> error(Integer code, String message) {
        return new Response<T>()
                .setCode(code)
                .setMessage(message);
    }
}