package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemOutgoing {
    @SerializedName("nama_item")
    @Expose
    private String namaItem;

    @SerializedName("no")
    @Expose
    private String no;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("qty_load")
    @Expose
    private String qtyLoad;

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

    public ItemOutgoing(String namaItem, String no, String qty, String qtyLoad, String label, String status, String idItem, String satuan,
                        String idInventoryDetail, String idOutboundDetail, String namaLocator, String labelInventory) {
        this.namaItem = namaItem;
        this.no = no;
        this.qty = qty;
        this.qtyLoad = qtyLoad;
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

    public String getNo() {
        return no;
    }

    public String getQty() {
        return qty;
    }

    public String getQtyLoad() {
        if (qtyLoad == null ) return "0";
        else return qtyLoad;
    }

    public String getLabel() {
        return label;
    }

    public String getStatus() {
        return status;
    }

    public String getIdItem() { return idItem; }

    public String getSatuan() { return satuan; }

    public String getIdInventoryDetail() { return idInventoryDetail; }

    public String getIdOutboundDetail() { return idOutboundDetail; }

    public String getNamaLocator() { return namaLocator; }

    public String getLabelInventory() { return labelInventory; }
}
