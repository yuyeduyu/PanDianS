package com.ascend.assetcheck_jinhua.api;
/**
 * 数据返回基类
 * @author lishanhui
 * created at 2018-06-29 9:46
 */
public class BaseResult {
    private String resultCode="0";//测试用
    private String message;
    private int count;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * error : unauthorized
     */

    private String error;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    //    {"access_token":"8ea41eda-f39b-44aa-b7d9-a4c38aeb046a","token_type":"bearer","expires_in":2525398,"scope":"app"}
    private String access_token,token_type,expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

//    {"error":"unauthorized","error_description":"没有该用户！"}
    private String unauthorized;

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    private String error_description;

    public String getUnauthorized() {
        return unauthorized;
    }

    public void setUnauthorized(String unauthorized) {
        this.unauthorized = unauthorized;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                ", count=" + count +
                ", error='" + error + '\'' +
                ", access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", unauthorized='" + unauthorized + '\'' +
                ", error_description='" + error_description + '\'' +
                '}';
    }
}
