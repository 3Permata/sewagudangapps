package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PickingResponse {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalAllocated")
    @Expose
    private String totalAllocated;

    @SerializedName("totalPicking")
    @Expose
    private String totalPicking;

    public PickingResponse(String idUser, String token, String status, String totalAllocated, String totalPicking) {
        this.idUser = idUser;
        this.token = token;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
