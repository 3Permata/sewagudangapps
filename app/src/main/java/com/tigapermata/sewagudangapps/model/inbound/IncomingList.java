package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tigapermata.sewagudangapps.model.inbound.Incoming;

import java.util.ArrayList;

public class IncomingList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data_incoming")
    @Expose
    private ArrayList<Incoming> incomingArrayList;

    @SerializedName("metode_inbound")
    @Expose
    private String metodeInbound;

    public IncomingList(String idUser, String token, ArrayList<Incoming> incomingArrayList, String metodeInbound) {
        this.idUser = idUser;
        this.token = token;
        this.incomingArrayList = incomingArrayList;
        this.metodeInbound = metodeInbound;
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

    public ArrayList<Incoming> getIncomingArrayList() {
        return incomingArrayList;
    }

    public void setIncomingArrayList(ArrayList<Incoming> incomingArrayList) {
        this.incomingArrayList = incomingArrayList;
    }

    public String getMetodeInbound() {
        return metodeInbound;
    }

    public void setMetodeInbound(String metodeInbound) {
        this.metodeInbound = metodeInbound;
    }
}
