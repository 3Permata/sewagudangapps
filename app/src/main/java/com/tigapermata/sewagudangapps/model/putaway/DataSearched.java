package com.tigapermata.sewagudangapps.model.putaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataSearched {
    @SerializedName("nama_item")
    @Expose
    private String namaItem;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("batch")
    @Expose
    private String batch;

    @SerializedName("satuan")
    @Expose
    private String satuan;

    @SerializedName("id_inventory_detail")
    @Expose
    private String idInventoryDetail;

    @SerializedName("id_locator")
    @Expose
    private String idLocator;

    @SerializedName("nama_locator")
    @Expose
    private String namaLocator;

    public DataSearched (String namaItem, String qty, String label, String batch, String satuan,
                         String idInventoryDetail, String idLocator, String namaLocator) {
        this.namaItem = namaItem;
        this.qty = qty;
        this.label = label;
        this.batch = batch;
        this.satuan = satuan;
        this.idInventoryDetail = idInventoryDetail;
        this.idLocator = idLocator;
        this.namaLocator = namaLocator;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public String getQty() {
        return qty;
    }

    public String getLabel() {
        return label;
    }

    public String getBatch() {
        return batch;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getIdInventoryDetail() {
        return idInventoryDetail;
    }

    public String getIdLocator() {
        return idLocator;
    }

    public String getNamaLocator() {
        return namaLocator;
    }
}
