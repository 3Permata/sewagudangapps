package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataOutgoingList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data_outgoing")
    @Expose
    private ArrayList<DataOutgoing> dataOutgoingArrayList;

    @SerializedName("metode_outbound")
    @Expose
    private String metodeOutbound;

    public DataOutgoingList(String idUser, String token, ArrayList<DataOutgoing> dataOutgoingArrayList, String metodeOutbound) {
        this.idUser = idUser;
        this.token = token;
        this.dataOutgoingArrayList = dataOutgoingArrayList;
        this.metodeOutbound = metodeOutbound;
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

    public ArrayList<DataOutgoing> getDataOutgoingArrayList() {
        return dataOutgoingArrayList;
    }

    public void setDataOutgoingArrayList(ArrayList<DataOutgoing> dataOutgoingArrayList) {
        this.dataOutgoingArrayList = dataOutgoingArrayList;
    }

    public String getMetodeOutbound() {
        return metodeOutbound;
    }

    public void setMetodeOutbound(String metodeOutbound) {
        this.metodeOutbound = metodeOutbound;
    }
}
