package com.ascend.assetcheck_jinhua.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 作者：lish on 2018-07-23.
 * 描述：
 */
@Entity
public class CompletePlace {
    @Id(autoincrement = true)
    private Long id;
    private int taskId;
    private String receivePlace;

    public String getReceivePlace() {
        return this.receivePlace;
    }

    public void setReceivePlace(String receivePlace) {
        this.receivePlace = receivePlace;
    }

    public int getTaskId() {
        return this.taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CompletePlace){
            CompletePlace place = (CompletePlace) obj;
            return this.taskId==place.getTaskId()&&this.receivePlace.equals(place.getReceivePlace());
        }
        return super.equals(obj);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1052631378)
    public CompletePlace(Long id, int taskId, String receivePlace) {
        this.id = id;
        this.taskId = taskId;
        this.receivePlace = receivePlace;
    }

    @Generated(hash = 111588956)
    public CompletePlace() {
    }
}
