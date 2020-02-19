package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataPickingList {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data_picking")
    @Expose
    private ArrayList<DataPicking> dataPickingArrayList;

    @SerializedName("totalAllocated")
    @Expose
    private String totalAllocated;

    @SerializedName("totalPicking")
    @Expose
    private String totalPicking;

    public DataPickingList(String idUser, String token, ArrayList<DataPicking> dataPickingArrayList,
                           String totalAllocated, String totalPicking) {
        this.idUser = idUser;
        this.token = token;
        this.dataPickingArrayList = dataPickingArrayList;
        this.totalAllocated = totalAllocated;
        this.totalPicking = totalPicking;
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

    public ArrayList<DataPicking> getDataPickingArrayList() {
        return dataPickingArrayList;
    }

    public void setDataPickingArrayList(ArrayList<DataPicking> dataPickingArrayList) {
        this.dataPickingArrayList = dataPickingArrayList;
    }

    public String getTotalAllocated() {
        return totalAllocated;
    }

    public void setTotalAllocated(String totalAllocated) {
        this.totalAllocated = totalAllocated;
    }

    public String getTotalPicking() {
        return totalPicking;
    }

    public void setTotalPicking(String totalPicking) {
        this.totalPicking = totalPicking;
    }
}
