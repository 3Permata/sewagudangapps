package com.tigapermata.sewagudangapps.model.putaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataFilterByItem {
    @SerializedName("id_inventory_detail")
    @Expose
    private String idInventoryDetail;

    @SerializedName("id_item")
    @Expose
    private String idItem;

    @SerializedName("nama_item")
    @Expose
    private String namaItem;

    @SerializedName("kode_item")
    @Expose
    private String kodeItem;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("qty")
    @Expose
    private String qty;

    public DataFilterByItem(String idInventoryDetail, String idItem, String namaItem, String kodeItem, String qty) {
        this.idInventoryDetail = idInventoryDetail;
        this.idItem = idItem;
        this.namaItem = namaItem;
        this.kodeItem = kodeItem;
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

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public String getKodeItem() {
        return kodeItem;
    }

    public void setKodeItem(String kodeItem) {
        this.kodeItem = kodeItem;
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
