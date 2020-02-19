package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoadingChecklistResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    @SerializedName("id_inventory_detail")
    @Expose
    private String idInventoryDetail;

    @SerializedName("id_outbound_detail")
    @Expose
    private String idOutboundDetail;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("qty_load")
    @Expose
    private String qtyLoad;

    public LoadingChecklistResponse (String status, String keterangan, String idInvetoryDetail, String idOutboundDetail, String qty, String qtyLoad) {
        this.status = status;
        this.keterangan = keterangan;
        this.idInventoryDetail = idInventoryDetail;
        this.idOutboundDetail = idOutboundDetail;
        this.qty = qty;
        this.qtyLoad = qtyLoad;
    }

    public String getIdOutboundDetail() {
        return idOutboundDetail;
    }

    public String getQtyLoad() {
        return qtyLoad;
    }

    public String getIdInventoryDetail() {
        return idInventoryDetail;
    }

    public String getQty() {
        return qty;
    }

    public String getStatus() {
        return status;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
