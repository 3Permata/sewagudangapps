package com.tigapermata.sewagudangapps.model.stockcount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMasterStockCountResponse {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("status")
    @Expose
    private String status;

    public AddMasterStockCountResponse(String idUser, String token, String status) {
        this.idUser = idUser;
        this.token = token;
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getStatus() {
        return status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
