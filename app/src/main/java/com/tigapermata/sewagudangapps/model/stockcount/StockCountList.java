package com.tigapermata.sewagudangapps.model.stockcount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StockCountList {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private ArrayList<MasterStockCount> stockCountArrayList;

    public StockCountList (String idUser, String token, ArrayList<MasterStockCount> stockCountArrayList) {
        this.idUser = idUser;
        this.token = token;
        this.stockCountArrayList = stockCountArrayList;
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

    public ArrayList<MasterStockCount> getStockCountArrayList() {
        return stockCountArrayList;
    }

    public void setStockCountArrayList(ArrayList<MasterStockCount> stockCountArrayList) {
        this.stockCountArrayList = stockCountArrayList;
    }
}
