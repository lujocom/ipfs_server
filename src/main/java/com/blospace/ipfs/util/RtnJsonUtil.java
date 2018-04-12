package com.blospace.ipfs.util;

/**
 * <dl>
 * <dt>xcalculas</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 成都微积分科技有限公司</dd>
 * <dd>CreateDate: 2017年04月14日</dd>
 * </dl>
 *
 * @author LuoHui
 */
public class RtnJsonUtil {


    public static RtnJson success() {
        return new RtnJson(ReturnCode.SUCCESS);
    }

    public static RtnJson success(Object data) {
        return new RtnJson(ReturnCode.SUCCESS, data);
    }

    public static RtnJson error() {
        return new RtnJson(ReturnCode.ERROR_ARGU.getCode(), "网络异常");
    }

    public static RtnJson error(String msg) {
        return new RtnJson(ReturnCode.ERROR_ARGU.getCode(), msg);
    }
}
