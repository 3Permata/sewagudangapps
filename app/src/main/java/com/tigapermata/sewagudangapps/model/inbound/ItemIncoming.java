package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemIncoming {
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

    @SerializedName("last_update")
    @Expose
    private String lastUpdate;

    @SerializedName("status_item")
    @Expose
    private String statusItem;

    @SerializedName("data_locator")
    @Expose
    private String dataLocator;

    public ItemIncoming(String no, String namaItem, String qty, String qtyAktual, String label, String lastUpdate, String statusItem, String dataLocator) {
        this.no = no;
        this.namaItem = namaItem;
        this.qty = qty;
        this.qtyAktual = qtyAktual;
        this.label = label;
        this.lastUpdate = lastUpdate;
        this.statusItem = statusItem;
        this.dataLocator = dataLocator;
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

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getStatusItem() {
        return statusItem;
    }

    public void setStatusItem(String statusItem) {
        this.statusItem = statusItem;
    }

    public String getDataLocator() {
        return dataLocator;
    }

    public void setDataLocator(String dataLocator) {
        this.dataLocator = dataLocator;
    }
}
