package com.tigapermata.sewagudangapps.model.putaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataFilterByItemList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private ArrayList<DataFilterByItem> dataFilterByItems;

    public DataFilterByItemList(String idUser, String token, ArrayList<DataFilterByItem> dataFilterByItems) {
        this.idUser = idUser;
        this.token = token;
        this.dataFilterByItems = dataFilterByItems;
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

    public ArrayList<DataFilterByItem> getDataFilterByItems() {
        return dataFilterByItems;
    }

    public void setDataFilterByItems(ArrayList<DataFilterByItem> dataFilterByItems) {
        this.dataFilterByItems = dataFilterByItems;
    }
}
