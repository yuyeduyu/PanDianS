package com.ascend.assetcheck_jinhua.result;

import com.ascend.assetcheck_jinhua.api.BaseResult;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：lishanhui on 2018-07-05.
 * 描述：下载盘点任务
 */

public class LoadResultBack extends BaseResult {
    public List<TaskResult> getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(List<TaskResult> jsonObject) {
        this.jsonObject = jsonObject;
    }

    //        {"jsonObject":[{"task_name":"ceshi3","id":3},{"task_name":"乱码测试","id":4}],"message":"成功!","resultCode":200}
    private List<TaskResult> jsonObject;


/*    public class LoadResult implements Serializable{
        private String task_name;
        private String id;

        public String getTask_name() {
            return task_name;
        }

        public void setTask_name(String task_name) {
            this.task_name = task_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }*/
}
