package com.tigapermata.sewagudangapps.model.putaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataSearchedList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private ArrayList<DataSearched> dataSearched;

    public DataSearchedList(String idUser, String token, ArrayList<DataSearched> dataSearched) {
        this.idUser = idUser;
        this.token = token;
        this.dataSearched = dataSearched;
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

    public ArrayList<DataSearched> getDataSearched() {
        return dataSearched;
    }
}
