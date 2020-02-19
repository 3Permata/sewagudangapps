package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddItemResponse {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data_item")
    @Expose
    private DataItem dataItem;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("notif")
    @Expose
    private String notif;

    public AddItemResponse(String idUser, String token, DataItem dataItem, String status, String notif) {
        this.idUser = idUser;
        this.token = token;
        this.dataItem = dataItem;
        this.status = status;
        this.notif = notif;
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

    public DataItem getDataItem() {
        return dataItem;
    }

    public void setDataItem(DataItem dataItem) {
        this.dataItem = dataItem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }
}
