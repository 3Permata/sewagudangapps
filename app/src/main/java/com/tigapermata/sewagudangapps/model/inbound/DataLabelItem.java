package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataLabelItem {
    @SerializedName("no")
    @Expose
    private String no;

    @SerializedName("id_item")
    @Expose
    private String idItem;

    @SerializedName("item")
    @Expose
    private String item;

    @SerializedName("batch")
    @Expose
    private String batch;

    @SerializedName("expired_date")
    @Expose
    private String expiredDate;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("id_inbound_detail")
    @Expose
    private String idInboundDetail;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("last_update")
    @Expose
    private String lastUpdate;

    public DataLabelItem(String no, String idItem, String item, String batch, String expiredDate,
                         String status, String qty, String idInboundDetail, String label, String user,
                         String lastUpdate) {
        this.no = no;
        this.idItem = idItem;
        this.item = item;
        this.batch = batch;
        this.expiredDate = expiredDate;
        this.status = status;
        this.qty = qty;
        this.idInboundDetail = idInboundDetail;
        this.label = label;
        this.user = user;
        this.lastUpdate = lastUpdate;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getIdInboundDetail() {
        return idInboundDetail;
    }

    public void setIdInboundDetail(String idInboundDetail) {
        this.idInboundDetail = idInboundDetail;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
