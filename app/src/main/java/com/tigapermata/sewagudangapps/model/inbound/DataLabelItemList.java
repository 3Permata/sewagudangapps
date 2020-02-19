package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataLabelItemList {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private ArrayList<DataLabelItem> dataLabelItemArrayList;

    public DataLabelItemList(String idUser, String token, ArrayList<DataLabelItem> dataLabelItemArrayList) {
        this.idUser = idUser;
        this.token = token;
        this.dataLabelItemArrayList = dataLabelItemArrayList;
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

    public ArrayList<DataLabelItem> getDataLabelItemArrayList() {
        return dataLabelItemArrayList;
    }

    public void setDataLabelItemArrayList(ArrayList<DataLabelItem> dataLabelItemArrayList) {
        this.dataLabelItemArrayList = dataLabelItemArrayList;
    }
}
