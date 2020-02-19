package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Outbound {

    @SerializedName("id_outbound")
    @Expose
    private String idOutbound;

    @SerializedName("tgl_outbound")
    @Expose
    private String tglOutbound;

    @SerializedName("no_outbound")
    @Expose
    private String noOutbound;

    @SerializedName("status_outbound")
    @Expose
    private String statusOutbond;

    @SerializedName("refrensi")
    @Expose
    private String refOutbound;

    @SerializedName("total_allocated")
    @Expose
    private String totalAllocated;

    @SerializedName("total_picking")
    @Expose
    private String totalPicking;

    public Outbound(String idOutbound, String tglOutbound, String noOutbound, String statusOutbond, String refOutbound, String totalAllocated, String totalPicking) {
        this.idOutbound = idOutbound;
        this.tglOutbound = tglOutbound;
        this.noOutbound = noOutbound;
        this.statusOutbond = statusOutbond;
        this.refOutbound = refOutbound;
        this.totalAllocated = totalAllocated;
        this.totalPicking = totalPicking;
    }

    public String getIdOutbound() {
        return idOutbound;
    }

    public void setIdOutbound(String idOutbound) {
        this.idOutbound = idOutbound;
    }

    public String getTglOutbound() {
        return tglOutbound;
    }

    public void setTglOutbound(String tglOutbound) {
        this.tglOutbound = tglOutbound;
    }

    public String getNoOutbound() {
        return noOutbound;
    }

    public void setNoOutbound(String noOutbound) {
        this.noOutbound = noOutbound;
    }

    public String getStatusOutbond() {
        return statusOutbond;
    }

    public void setStatusOutbond(String statusOutbond) {
        this.statusOutbond = statusOutbond;
    }

    public String getRefOutbound() {
        return refOutbound;
    }

    public void setRefOutbound(String refOutbound) {
        this.refOutbound = refOutbound;
    }

    public String getTotalAllocated() { return totalAllocated; }

    public void setTotalAllocated(String totalAllocated) { this.totalAllocated = totalAllocated; }

    public String getTotalPicking() { return totalPicking; }

    public void setTotalPicking(String totalPicking) { this.totalPicking = totalPicking; }
}
