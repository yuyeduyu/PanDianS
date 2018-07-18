package com.ascend.assetcheck_jinhua.result;

/**
 * 描述：
 *
 * @author lishanhui
 * created at 2018/7/15 0015 22:51
 */
public class upLoadResult {
//    失败返回 {"message":"上传失败!","resultCode":521}
    private String message;
    private String resultCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
