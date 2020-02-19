package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemIncomingDetail {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("no")
    @Expose
    private String no;

    @SerializedName("kode_item")
    @Expose
    private String kodeItem;

    @SerializedName("nama_item")
    @Expose
    private String namaItem;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("qty_aktual")
    @Expose
    private String qtyAktual;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("id_inbound_detail")
    @Expose
    private String idInboundDetail;

    @SerializedName("locator")
    @Expose
    private String locator;

    @SerializedName("id_locator")
    @Expose
    private String idLocator;

    public ItemIncomingDetail(String idUser, String token, String no, String kodeItem, String namaItem, String qty, String qtyAktual, String status, String label, String idInboundDetail, String locator, String idLocator) {
        this.idUser = idUser;
        this.token = token;
        this.no = no;
        this.kodeItem = kodeItem;
        this.namaItem = namaItem;
        this.qty = qty;
        this.qtyAktual = qtyAktual;
        this.status = status;
        this.label = label;
        this.idInboundDetail = idInboundDetail;
        this.locator = locator;
        this.idLocator = idLocator;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getKodeItem() {
        return kodeItem;
    }

    public void setKodeItem(String kodeItem) {
        this.kodeItem = kodeItem;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQtyAktual() {
        return qtyAktual;
    }

    public void setQtyAktual(String qtyAktual) {
        this.qtyAktual = qtyAktual;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIdInboundDetail() {
        return idInboundDetail;
    }

    public void setIdInboundDetail(String idInboundDetail) {
        this.idInboundDetail = idInboundDetail;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public String getIdLocator() {
        return idLocator;
    }

    public void setIdLocator(String idLocator) {
        this.idLocator = idLocator;
    }
}
