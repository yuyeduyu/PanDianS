package com.ascend.assetcheck_jinhua.result;

import com.ascend.assetcheck_jinhua.api.BaseResult;

import java.util.List;

/**
 * 作者：lish on 2018-07-23.
 * 描述：
 */

public class TaskBack extends BaseResult {

    private List<Task> jsonObject;

    public List<Task> getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(List<Task> jsonObject) {
        this.jsonObject = jsonObject;
    }

    public static class Task {
        private int taskId;
        private String task_name;

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public String getTask_name() {
            return task_name;
        }

        public void setTask_name(String task_name) {
            this.task_name = task_name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Task){
                Task task = (Task) obj;
                return this.getTaskId() == task.getTaskId();
            }
            return super.equals(obj);
        }
    }

}
