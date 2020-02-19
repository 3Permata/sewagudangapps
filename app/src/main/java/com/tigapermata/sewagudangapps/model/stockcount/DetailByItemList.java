package com.tigapermata.sewagudangapps.model.stockcount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailByItemList {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private ArrayList<DetailStockCountByItem> detailByItemArrayList;

    public DetailByItemList (String idUser, String token, ArrayList<DetailStockCountByItem> detailByItemArrayList) {
        this.idUser = idUser;
        this.token = token;
        this.detailByItemArrayList = detailByItemArrayList;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getToken() {
        return token;
    }

    public ArrayList<DetailStockCountByItem> getDetailByItemArrayList() {
        return detailByItemArrayList;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDetailByItemArrayList(ArrayList<DetailStockCountByItem> detailByItemArrayList) {
        this.detailByItemArrayList = detailByItemArrayList;
    }
}
