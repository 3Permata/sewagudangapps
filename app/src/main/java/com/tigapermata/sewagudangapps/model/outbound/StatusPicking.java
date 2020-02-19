package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusPicking {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("total_item_terscan")
    @Expose
    private String totalScanned;

    @SerializedName("total_item_picking")
    @Expose
    private String totalPicking;

    public StatusPicking(String idUser, String token, String info, String totalScanned, String totalPicking) {
        this.idUser = idUser;
        this.token = token;
        this.info = info;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTotalScanned() { return totalScanned; }

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
