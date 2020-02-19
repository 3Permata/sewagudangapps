package com.tigapermata.sewagudangapps.model.stockcount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailStockCountByLabel {
    @SerializedName("no")
    @Expose
    private String noStockCount;

    @SerializedName("tgl")
    @Expose
    private String tanggal;

    @SerializedName("locator")
    @Expose
    private String locator;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("qty_actual")
    @Expose
    private String qtyActual;

    @SerializedName("check_qty")
    @Expose
    private String checkQty;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("status")
    @Expose
    private String status;

    public DetailStockCountByLabel (String noStockCount, String tanggal, String locator, String label, String qty, String qtyActual, String checkQty, String user, String status) {
        this.noStockCount = noStockCount;
        this.tanggal = tanggal;
        this.locator = locator;
        this.label = label;
        this.qty = qty;
        this.qtyActual = qtyActual;
        this.checkQty = checkQty;
        this.user = user;
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getNoStockCount() {
        return noStockCount;
    }

    public String getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public String getQty() {
        return qty;
    }

    public String getLabel() {
        return label;
    }

    public String getCheckQty() {
        return checkQty;
    }

    public String getLocator() {
        return locator;
    }

    public String getQtyActual() {
        return qtyActual;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setNoStockCount(String noStockCount) {
        this.noStockCount = noStockCount;
    }

    public void setCheckQty(String checkQty) {
        this.checkQty = checkQty;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setQtyActual(String qtyActual) {
        this.qtyActual = qtyActual;
    }
}
