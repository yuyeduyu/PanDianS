package com.ascend.assetcheck_jinhua.result;

import com.ascend.assetcheck_jinhua.api.BaseResult;

import java.util.List;

/**
 * 作者：lishanhui on 2018-07-09.
 * 描述：
 */

public class getLoadTaskResultBack extends BaseResult {

    private List<TaskResult> jsonObject;

    public List<TaskResult> getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(List<TaskResult> jsonObject) {
        this.jsonObject = jsonObject;
    }
/*    public class getLoadTaskResult {
        private int actualQuantity;//实盘数
        private String assetsType;//资产类型
        private int differenceNum;//差异数
        private int id;  //资产id
        private int inventoryNum;//盘点账面数 (应有数量)
        private String inventoryResult;//盘点结果(相符   盘亏  盘盈)
        private String productCode;//资产编码
        private String productName;//资产品名
        private String receiveDepartment;//使用部门
        private String receivePerson;//使用人
        private String receivePlace;//存放位置
        private String specificationModel;//规格型号
        private int taskId;//任务id

        public int getActualQuantity() {
            return actualQuantity;
        }

        public void setActualQuantity(int actualQuantity) {
            this.actualQuantity = actualQuantity;
        }

        public String getAssetsType() {
            return assetsType;
        }

        public void setAssetsType(String assetsType) {
            this.assetsType = assetsType;
        }

        public int getDifferenceNum() {
            return differenceNum;
        }

        public void setDifferenceNum(int differenceNum) {
            this.differenceNum = differenceNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInventoryNum() {
            return inventoryNum;
        }

        public void setInventoryNum(int inventoryNum) {
            this.inventoryNum = inventoryNum;
        }

        public String getInventoryResult() {
            return inventoryResult;
        }

        public void setInventoryResult(String inventoryResult) {
            this.inventoryResult = inventoryResult;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getReceiveDepartment() {
            return receiveDepartment;
        }

        public void setReceiveDepartment(String receiveDepartment) {
            this.receiveDepartment = receiveDepartment;
        }

        public String getReceivePerson() {
            return receivePerson;
        }

        public void setReceivePerson(String receivePerson) {
            this.receivePerson = receivePerson;
        }

        public String getReceivePlace() {
            return receivePlace;
        }

        public void setReceivePlace(String receivePlace) {
            this.receivePlace = receivePlace;
        }

        public String getSpecificationModel() {
            return specificationModel;
        }

        public void setSpecificationModel(String specificationModel) {
            this.specificationModel = specificationModel;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }
    }*/
}
