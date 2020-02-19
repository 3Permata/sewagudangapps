package com.tigapermata.sewagudangapps.model.putaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataHistoryList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private ArrayList<DataHistory> dataHistories;

    public DataHistoryList(String idUser, String token, ArrayList<DataHistory> dataHistories) {
        this.idUser = idUser;
        this.token = token;
        this.dataHistories = dataHistories;
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

    public ArrayList<DataHistory> getDataHistories() {
        return dataHistories;
    }
}
