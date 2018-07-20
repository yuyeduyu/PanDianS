package com.ascend.assetcheck_jinhua.result;

import com.ascend.assetcheck_jinhua.api.BaseResult;

import java.util.List;

/**
 * 作者：lish on 2018-07-20.
 * 描述：
 */

public class getTaskRangeResltback extends BaseResult {
    private List <TaskRange> jsonObject;

    public List<TaskRange> getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(List<TaskRange> jsonObject) {
        this.jsonObject = jsonObject;
    }

    public class TaskRange{
        private int id;
        private List<String> receivePlace;

        public List<String> getReceivePlace() {
            return receivePlace;
        }

        public void setReceivePlace(List<String> receivePlace) {
            this.receivePlace = receivePlace;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
