package com.blospace.ipfs.util;

/**
 *
 *
 *
 * @date		2015.10.15
 * @version		1.0
 */
public enum ReturnCode {
	
	/**
     * 操作成功
	 */
	SUCCESS("000","操作成功"),
    ERROR_SERVER("100", "无法连接ipfs服务器"),
	ERROR_BUSINESS("113", "业务异常"),
	ERROR_ARGU("101","参数解析失败");
	private String code;
	private String message;

	private ReturnCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "returnCode=" + code +", message=" + message+"";
	}
}
