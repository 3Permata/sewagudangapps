package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RawFormIncoming {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    @SerializedName("data_lain")
    @Expose
    private ArrayList<DataLain> dataLains;

    public RawFormIncoming(String idUser, String token, String tanggal, ArrayList<DataLain> dataLains) {
        this.idUser = idUser;
        this.token = token;
        this.tanggal = tanggal;
        this.dataLains = dataLains;
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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public ArrayList<DataLain> getDataLains() {
        return dataLains;
    }

    public void setDataLains(ArrayList<DataLain> dataLains) {
        this.dataLains = dataLains;
    }
}
