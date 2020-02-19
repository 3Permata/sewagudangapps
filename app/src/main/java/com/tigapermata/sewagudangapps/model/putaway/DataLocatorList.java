package com.tigapermata.sewagudangapps.model.putaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataLocatorList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private ArrayList<DataLocator> dataLocatorArrayList;

    public DataLocatorList(String idUser, String token, ArrayList<DataLocator> dataLocatorArrayList) {
        this.idUser = idUser;
        this.token = token;
        this.dataLocatorArrayList = dataLocatorArrayList;
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

    public ArrayList<DataLocator> getDataLocatorArrayList() {
        return dataLocatorArrayList;
    }

    public void setDataLocatorArrayList(ArrayList<DataLocator> dataLocatorArrayList) {
        this.dataLocatorArrayList = dataLocatorArrayList;
    }
}
