package com.tigapermata.sewagudangapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GudangList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("gudang")
    @Expose
    private List<Gudang> addAllGudangList = new ArrayList<Gudang>();

    public GudangList(String idUser, String token, List<Gudang> addAllGudangList) {
        this.idUser = idUser;
        this.token = token;
        this.addAllGudangList = addAllGudangList;
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

    public List<Gudang> getAddAllGudangList() {
        return addAllGudangList;
    }

    public void setAddAllGudangList(List<Gudang> addAllGudangList) {
        this.addAllGudangList = addAllGudangList;
    }
}
