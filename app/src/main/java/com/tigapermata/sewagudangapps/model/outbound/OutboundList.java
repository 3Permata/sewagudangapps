package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OutboundList {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("outbound")
    @Expose
    private ArrayList<Outbound> outboundArrayList;

    @SerializedName("metode_picking")
    @Expose
    private String metode;

    public OutboundList(String idUser, String token, ArrayList<Outbound> outboundArrayList, String metode) {
        this.idUser = idUser;
        this.token = token;
        this.outboundArrayList = outboundArrayList;
        this.metode = metode;
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

    public ArrayList<Outbound> getOutboundArrayList() {
        return outboundArrayList;
    }

    public void setOutboundArrayList(ArrayList<Outbound> outboundArrayList) {
        this.outboundArrayList = outboundArrayList;
    }

    public String getMetode() { return metode; }

    public void setMetode(String metode) { this.metode = metode; }
}
