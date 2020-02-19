package com.tigapermata.sewagudangapps.model.stockcount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailByLabelList {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private ArrayList<DetailStockCountByLabel> detailByLabelArrayList;

    public DetailByLabelList (String idUser, String token, ArrayList<DetailStockCountByLabel> detailByLabelArrayList) {
        this.idUser = idUser;
        this.token = token;
        this.detailByLabelArrayList = detailByLabelArrayList;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getToken() {
        return token;
    }

    public ArrayList<DetailStockCountByLabel> getDetailByLabelArrayList() {
        return detailByLabelArrayList;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDetailByLabelArrayList(ArrayList<DetailStockCountByLabel> detailByLabelArrayList) {
        this.detailByLabelArrayList = detailByLabelArrayList;
    }
}
