package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inbound {

    @SerializedName("id_inbound")
    @Expose
    private String idInbound;

    @SerializedName("tgl_inbound")
    @Expose
    private String tglInbound;

    @SerializedName("no_inbound")
    @Expose
    private String noInbound;

    @SerializedName("refrensi")
    @Expose
    private String referensi;

    @SerializedName("cbm")
    @Expose
    private String cbm;

    @SerializedName("coly")
    @Expose
    private String colly;

    @SerializedName("status_inbound")
    @Expose
    private String statusInbound;

    @SerializedName("qty_actual")
    @Expose
    private String qtyActual;

    @SerializedName("qty_dokument")
    @Expose
    private String qtyDocument;

    public Inbound(String idInbound, String tglInbound, String noInbound, String referensi, String cbm, String colly, String statusInbound, String qtyActual, String qtyDocument) {
        this.idInbound = idInbound;
        this.tglInbound = tglInbound;
        this.noInbound = noInbound;
        this.referensi = referensi;
        this.cbm = cbm;
        this.colly = colly;
        this.statusInbound = statusInbound;
        this.qtyActual = qtyActual;
        this.qtyDocument = qtyDocument;
    }

    public String getIdInbound() {
        return idInbound;
    }

    public void setIdInbound(String idInbound) {
        this.idInbound = idInbound;
    }

    public String getTglInbound() {
        return tglInbound;
    }

    public void setTglInbound(String tglInbound) {
        this.tglInbound = tglInbound;
    }

    public String getNoInbound() {
        return noInbound;
    }

    public void setNoInbound(String noInbound) {
        this.noInbound = noInbound;
    }

    public String getReferensi() {
        return referensi;
    }

    public void setReferensi(String referensi) {
        this.referensi = referensi;
    }

    public String getCbm() {
        return cbm;
    }

    public void setCbm(String cbm) {
        this.cbm = cbm;
    }

    public String getColly() {
        return colly;
    }

    public void setColly(String colly) {
        this.colly = colly;
    }

    public String getStatusInbound() {
        return statusInbound;
    }

    public void setStatusInbound(String statusInbound) {
        this.statusInbound = statusInbound;
    }

    public String getQtyActual() {
        return qtyActual;
    }

    public void setQtyActual(String qtyActual) {
        this.qtyActual = qtyActual;
    }

    public String getQtyDocument() {
        return qtyDocument;
    }

    public void setQtyDocument(String qtyDocument) {
        this.qtyDocument = qtyDocument;
    }
}
