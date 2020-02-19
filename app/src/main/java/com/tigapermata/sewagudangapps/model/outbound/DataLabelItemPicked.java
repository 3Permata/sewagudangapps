package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataLabelItemPicked {

    @SerializedName("id_outbound_detail")
    @Expose
    private String idOutboundDetail;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("label_inv")
    @Expose
    private String labelInventory;

    public DataLabelItemPicked(String idOutboundDetail, String label, String labelInventory) {
        this.idOutboundDetail = idOutboundDetail;
        this.label = label;
        this.labelInventory = labelInventory;
    }

    public String getIdOutboundDetail() {
        return idOutboundDetail;
    }

    public String getLabel() {
        return label;
    }

    public String getLabelInventory() {
        return labelInventory;
    }
}
