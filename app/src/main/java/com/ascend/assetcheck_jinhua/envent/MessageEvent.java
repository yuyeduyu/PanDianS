package com.ascend.assetcheck_jinhua.envent;

/**
 * 作者：lish on 2018-07-17.
 * 描述：
 */

public class MessageEvent {
    private int message;
    public  MessageEvent(int message){
        this.message=message;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
