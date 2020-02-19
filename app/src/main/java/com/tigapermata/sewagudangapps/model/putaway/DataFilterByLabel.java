package com.tigapermata.sewagudangapps.model.putaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataFilterByLabel {
    @SerializedName("id_inventory_detail")
    @Expose
    private String idInventoryDetail;

    @SerializedName("id_item")
    @Expose
    private String idItem;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("qty")
    @Expose
    private String qty;

    public DataFilterByLabel(String idInventoryDetail, String idItem, String label, String qty) {
        this.idInventoryDetail = idInventoryDetail;
        this.idItem = idItem;
        this.label = label;
        this.qty = qty;
    }

    public String getIdInventoryDetail() {
        return idInventoryDetail;
    }

    public void setIdInventoryDetail(String idInventoryDetail) {
        this.idInventoryDetail = idInventoryDetail;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
