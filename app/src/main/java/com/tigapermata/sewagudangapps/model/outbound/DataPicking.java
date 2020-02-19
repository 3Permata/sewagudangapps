package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPicking {

    @SerializedName("nama_item")
    @Expose
    private String namaItem;

    @SerializedName("no")
    @Expose
    private Integer no;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("id_item")
    @Expose
    private String idItem;

    @SerializedName("satuan")
    @Expose
    private String satuan;

    @SerializedName("id_inventory_detail")
    @Expose
    private String idInventoryDetail;

    @SerializedName("id_outbound_detail")
    @Expose
    private String idOutboundDetail;

    @SerializedName("nama_locator")
    @Expose
    private String namaLocator;

    @SerializedName("label_inv")
    @Expose
    private String labelInventory;

    public DataPicking(String namaItem, Integer no, String qty, String label, String status,
                       String idItem, String satuan, String idInventoryDetail, String idOutboundDetail,
                       String namaLocator, String labelInventory) {
        this.namaItem = namaItem;
        this.no = no;
        this.qty = qty;
        this.label = label;
        this.status = status;
        this.idItem = idItem;
        this.satuan = satuan;
        this.idInventoryDetail = idInventoryDetail;
        this.idOutboundDetail = idOutboundDetail;
        this.namaLocator = namaLocator;
        this.labelInventory = labelInventory;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getIdInventoryDetail() {
        return idInventoryDetail;
    }

    public void setIdInventoryDetail(String idInventoryDetail) {
        this.idInventoryDetail = idInventoryDetail;
    }

    public String getIdOutboundDetail() {
        return idOutboundDetail;
    }

    public void setIdOutboundDetail(String idOutboundDetail) {
        this.idOutboundDetail = idOutboundDetail;
    }

    public String getNamaLocator() {
        return namaLocator;
    }

    public void setNamaLocator(String namaLocator) {
        this.namaLocator = namaLocator;
    }

    public String getLabelInventory() {
        return labelInventory;
    }

    public void setLabelInventory(String labelInventory) {
        this.labelInventory = labelInventory;
    }
}
