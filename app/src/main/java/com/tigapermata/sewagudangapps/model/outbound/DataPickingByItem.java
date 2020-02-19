package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPickingByItem {

    @SerializedName("no")
    @Expose
    private Integer no;

    @SerializedName("id_item")
    @Expose
    private String idItem;

    @SerializedName("item")
    @Expose
    private String namaItem;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("qty_loading")
    @Expose
    private String qtyLoading;

    public DataPickingByItem(Integer no, String idItem, String namaItem, String status, String qty, String qtyLoading) {
        this.no = no;
        this.idItem = idItem;
        this.namaItem = namaItem;
        this.status = status;
        this.qty = qty;
        this.qtyLoading = qtyLoading;
    }

    public Integer getNo() { return no; }

    public String getIdItem() { return idItem; }

    public String getNamaItem() { return namaItem; }

    public  String getStatus() { return status; }

    public String getQty() { return qty; }

    public String getQtyLoading() { return qtyLoading; }

}
