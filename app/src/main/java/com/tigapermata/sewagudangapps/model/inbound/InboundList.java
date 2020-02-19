package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tigapermata.sewagudangapps.model.inbound.Inbound;

import java.util.ArrayList;
import java.util.List;

public class InboundList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("inbound")
    @Expose
    private List<Inbound> inboundList = new ArrayList<Inbound>();

    public InboundList(String idUser, String token, ArrayList<Inbound> inboundList) {
        this.idUser = idUser;
        this.token = token;
        this.inboundList = inboundList;
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

    public List<Inbound> getInboundList() {
        return inboundList;
    }

    public void setInboundList(List<Inbound> inboundList) {
        this.inboundList = inboundList;
    }
}
