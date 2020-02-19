package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InboundDetailList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data_inbound_detail")
    @Expose
    private ArrayList<InboundDetail> inboundDetails;

    @SerializedName("detail_inbound")
    @Expose
    private ArrayList<DataLain> dataLainArrayList;

    public InboundDetailList(String idUser, String token, ArrayList<InboundDetail> inboundDetails, ArrayList<DataLain> dataLainArrayList) {
        this.idUser = idUser;
        this.token = token;
        this.inboundDetails = inboundDetails;
        this.dataLainArrayList = dataLainArrayList;
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

    public ArrayList<InboundDetail> getInboundDetails() {
        return inboundDetails;
    }

    public void setInboundDetails(ArrayList<InboundDetail> inboundDetails) {
        this.inboundDetails = inboundDetails;
    }

    public ArrayList<DataLain> getDataLainArrayList() {
        return dataLainArrayList;
    }

    public void setDataLainArrayList(ArrayList<DataLain> dataLainArrayList) {
        this.dataLainArrayList = dataLainArrayList;
    }
}
