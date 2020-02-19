package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocatorList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data_locator")
    @Expose
    private ArrayList<Locator> locatorArrayList;

    public LocatorList(String idUser, String token, ArrayList<Locator> locatorArrayList) {
        this.idUser = idUser;
        this.token = token;
        this.locatorArrayList = locatorArrayList;
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

    public ArrayList<Locator> getLocatorArrayList() {
        return locatorArrayList;
    }

    public void setLocatorArrayList(ArrayList<Locator> locatorArrayList) {
        this.locatorArrayList = locatorArrayList;
    }
}
