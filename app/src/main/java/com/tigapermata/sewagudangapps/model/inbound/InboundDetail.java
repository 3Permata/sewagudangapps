package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboundDetail {
    @SerializedName("no")
    @Expose
    private String no;

    @SerializedName("nama_item")
    @Expose
    private String namaItem;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("qty_aktual")
    @Expose
    private String qtyAktual;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("batch")
    @Expose
    private String batch;

    @SerializedName("expired_date")
    @Expose
    private String expiredDate;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("actual_cbm")
    @Expose
    private String actualCBM;

    @SerializedName("actual_net")
    @Expose
    private String actualNet;

    @SerializedName("item_qty")
    @Expose
    private String itemQty;

    public InboundDetail(String no, String namaItem, String qty, String qtyAktual, String label, String batch, String expiredDate, String status, String actualCBM, String actualNet, String itemQty) {
        this.no = no;
        this.namaItem = namaItem;
        this.qty = qty;
        this.qtyAktual = qtyAktual;
        this.label = label;
        this.batch = batch;
        this.expiredDate = expiredDate;
        this.status = status;
        this.actualCBM = actualCBM;
        this.actualNet = actualNet;
        this.itemQty = itemQty;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQtyAktual() {
        return qtyAktual;
    }

    public void setQtyAktual(String qtyAktual) {
        this.qtyAktual = qtyAktual;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getActualCBM() {
        return actualCBM;
    }

    public void setActualCBM(String actualCBM) {
        this.actualCBM = actualCBM;
    }

    public String getActualNet() {
        return actualNet;
    }

    public void setActualNet(String actualNet) {
        this.actualNet = actualNet;
    }

    public String getItemQty() { return itemQty; }

    public void setItemQty(String itemQty) { this.itemQty = itemQty; }
}
