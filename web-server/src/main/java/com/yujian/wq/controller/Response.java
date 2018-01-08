package com.yujian.wq.controller;

import java.io.Serializable;

/**
 * springmvc使用的通用返回对象
 *
 * @author wangqing
 * @since 14-4-16 下午1:42
 */
public class Response implements Serializable {
    public static final int SUCCESS = 1;
    public static final int FAILURE = 0;
    public static final int NOLOGIN = 2;
    private int status = SUCCESS; //见上述状态码
    private String msg;
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
