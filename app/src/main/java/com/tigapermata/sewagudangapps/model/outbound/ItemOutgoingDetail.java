package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemOutgoingDetail {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    @SerializedName("total_picking")
    @Expose
    private String totalPicking;

    @SerializedName("total_loading")
    @Expose
    private String totalLoading;

    public ItemOutgoingDetail(String status, String keterangan, String totalPicking, String totalLoading) {
        this.status = status;
        this.keterangan = keterangan;
        this.totalPicking = totalPicking;
        this.totalLoading = totalLoading;
    }

    public String getStatus() { return status; }

    public String getKeterangan() { return keterangan; }

    public String getTotalPicking() { return totalPicking; }

    public String getTotalLoading() { return totalLoading; }
}
