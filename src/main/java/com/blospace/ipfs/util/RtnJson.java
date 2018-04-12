package com.blospace.ipfs.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author 陆华
 * @描述   返回到页面的JSON类
 * @date 16-6-14 上午11:52
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class RtnJson {
    private String returnCode;
    private String returnMsg;
    private Object data;

    public RtnJson(ReturnCode returnCode, Object data) {
        this.returnCode = returnCode.getCode();
        this.returnMsg = returnCode.getMessage();
        this.data = data;
    }

    public RtnJson(ReturnCode returnCode) {
        this.returnCode = returnCode.getCode();
        this.returnMsg = returnCode.getMessage();
    }

    public RtnJson(String code, String msg, Object data) {
        this.returnCode = code;
        this.returnMsg = msg;
        this.data = data;
    }

    public RtnJson(String code, String msg) {
        this.returnCode = code;
        this.returnMsg = msg;
    }

    public RtnJson(ReturnCode returnCode, String msg) {
        this.returnCode = returnCode.getCode();
        this.returnMsg = msg;
    }

    public RtnJson() {

    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public Object getData() {
        return data;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
