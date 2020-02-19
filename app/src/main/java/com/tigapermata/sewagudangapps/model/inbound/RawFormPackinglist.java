package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RawFormPackinglist {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("id_item")
    @Expose
    private String idItem;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("package")
    @Expose
    private String packages;

    @SerializedName("qty_package")
    @Expose
    private String qtyPackage;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("batch")
    @Expose
    private String batch;

    @SerializedName("expired_date")
    @Expose
    private String expiredDate;

    @SerializedName("actual_net")
    @Expose
    private String actualNet;

    @SerializedName("actual_cbm")
    @Expose
    private String actualCBM;

    @SerializedName("data_lain")
    @Expose
    private ArrayList<DataLain> dataLainArrayList;

    public RawFormPackinglist(String idUser, String token, String idItem, String qty, String packages,
                              String qtyPackage, String label, String batch, String expiredDate, String actualNet, String actualCBM, ArrayList<DataLain> dataLainArrayList) {
        this.idUser = idUser;
        this.token = token;
        this.idItem = idItem;
        this.qty = qty;
        this.packages = packages;
        this.qtyPackage = qtyPackage;
        this.label = label;
        this.batch = batch;
        this.expiredDate = expiredDate;
        this.actualNet = actualNet;
        this.actualCBM = actualCBM;
        this.dataLainArrayList = dataLainArrayList;
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

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getQtyPackage() {
        return qtyPackage;
    }

    public void setQtyPackage(String qtyPackage) {
        this.qtyPackage = qtyPackage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getActualNet() {
        return actualNet;
    }

    public void setActualNet(String actualNet) {
        this.actualNet = actualNet;
    }

    public String getActualCBM() {
        return actualCBM;
    }

    public void setActualCBM(String actualCBM) {
        this.actualCBM = actualCBM;
    }

    public ArrayList<DataLain> getDataLainArrayList() {
        return dataLainArrayList;
    }

    public void setDataLainArrayList(ArrayList<DataLain> dataLainArrayList) {
        this.dataLainArrayList = dataLainArrayList;
    }
}
