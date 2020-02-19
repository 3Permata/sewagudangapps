package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataPickingListByItem {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data_picking")
    @Expose
    private ArrayList<DataPickingByItem> dataPickingArrayList;

    @SerializedName("total_item_terscan")
    @Expose
    private String totalScanned;

    @SerializedName("total_item_picking")
    @Expose
    private String totalPicking;

    public DataPickingListByItem(String idUser, String token, ArrayList<DataPickingByItem> dataPickingArrayList,
                           String totalScanned, String totalPicking) {
        this.idUser = idUser;
        this.token = token;
        this.dataPickingArrayList = dataPickingArrayList;
        this.totalScanned = totalScanned;
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

    public ArrayList<DataPickingByItem> getDataPickingArrayList() {
        return dataPickingArrayList;
    }

    public void setDataPickingArrayList(ArrayList<DataPickingByItem> dataPickingArrayList) {
        this.dataPickingArrayList = dataPickingArrayList;
    }

    public String getTotalScanned() {
        return totalScanned;
    }

    public void setTotalScanned(String totalScanned) {
        this.totalScanned = totalScanned;
    }

    public String getTotalPicking() {
        return totalPicking;
    }

    public void setTotalPicking(String totalPicking) {
        this.totalPicking = totalPicking;
    }
}
