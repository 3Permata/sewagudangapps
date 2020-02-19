package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CekItemUpdate {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("total_qty_aktual")
    @Expose
    private String totalQtyAktual;

    @SerializedName("total_qty_document")
    @Expose
    private String totalQtyDokumen;

    public CekItemUpdate(String idUser, String token, String totalQtyAktual, String totalQtyDokumen) {
        this.idUser = idUser;
        this.token = token;
        this.totalQtyAktual = totalQtyAktual;
        this.totalQtyDokumen = totalQtyDokumen;
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

    public String getTotalQtyAktual() {
        return totalQtyAktual;
    }

    public void setTotalQtyAktual(String totalQtyAktual) {
        this.totalQtyAktual = totalQtyAktual;
    }

    public String getTotalQtyDokumen() {
        return totalQtyDokumen;
    }

    public void setTotalQtyDokumen(String totalQtyDokumen) {
        this.totalQtyDokumen = totalQtyDokumen;
    }
}
