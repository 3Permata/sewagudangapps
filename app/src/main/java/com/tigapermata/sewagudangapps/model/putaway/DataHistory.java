package com.tigapermata.sewagudangapps.model.putaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataHistory {
    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("item")
    @Expose
    private String item;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("satuan")
    @Expose
    private String satuan;

    @SerializedName("locator_awal")
    @Expose
    private String locatorAwal;

    @SerializedName("locator_tujuan")
    @Expose
    private String locatorTujuan;

    public DataHistory (String tanggal, String label, String item, String qty, String satuan, String locatorAwal, String locatorTujuan) {
        this.tanggal = tanggal;
        this.label = label;
        this.item = item;
        this.qty = qty;
        this.satuan = satuan;
        this.locatorAwal = locatorAwal;
        this.locatorTujuan = locatorTujuan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getLabel() {
        return label;
    }

    public String getItem() {
        return item;
    }

    public String getQty() {
        return qty;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getLocatorAwal() {
        return locatorAwal;
    }

    public String getLocatorTujuan() {
        return locatorTujuan;
    }
}
