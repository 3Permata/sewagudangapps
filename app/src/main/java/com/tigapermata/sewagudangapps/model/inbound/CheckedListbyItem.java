package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CheckedListbyItem {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data_incoming")
    @Expose
    public ArrayList<DataIncomingByItem> dataIncomingByItems;

    @SerializedName("total_qty_aktual")
    @Expose
    private String totalQtyAktual;

    @SerializedName("total_qty_document")
    @Expose
    private String totalQtyDocument;

    @SerializedName("no_inbound")
    @Expose
    private String noInbound;

    @SerializedName("nama_project")
    @Expose
    private String namaProject;

    public CheckedListbyItem(String idUser, String token, ArrayList<DataIncomingByItem> dataIncomingByItems, String totalQtyAktual, String totalQtyDocument, String noInbound, String namaProject) {
        this.idUser = idUser;
        this.token = token;
        this.dataIncomingByItems = dataIncomingByItems;
        this.totalQtyAktual = totalQtyAktual;
        this.totalQtyDocument = totalQtyDocument;
        this.noInbound = noInbound;
        this.namaProject = namaProject;
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

    public ArrayList<DataIncomingByItem> getDataIncomingByItems() {
        return dataIncomingByItems;
    }

    public void setDataIncomingByItems(ArrayList<DataIncomingByItem> dataIncomingByItems) {
        this.dataIncomingByItems = dataIncomingByItems;
    }

    public String getTotalQtyAktual() {
        return totalQtyAktual;
    }

    public void setTotalQtyAktual(String totalQtyAktual) {
        this.totalQtyAktual = totalQtyAktual;
    }

    public String getTotalQtyDocument() {
        return totalQtyDocument;
    }

    public void setTotalQtyDocument(String totalQtyDocument) {
        this.totalQtyDocument = totalQtyDocument;
    }

    public String getNoInbound() {
        return noInbound;
    }

    public void setNoInbound(String noInbound) {
        this.noInbound = noInbound;
    }

    public String getNamaProject() {
        return namaProject;
    }

    public void setNamaProject(String namaProject) {
        this.namaProject = namaProject;
    }
}
