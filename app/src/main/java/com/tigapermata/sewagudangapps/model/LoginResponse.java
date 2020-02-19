package com.tigapermata.sewagudangapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("total_gudang")
    @Expose
    private String totalGudang;

    @SerializedName("total_customer")
    @Expose
    private String totalCustomer;

    @SerializedName("total_project")
    @Expose
    private String totalProject;

    public LoginResponse(String idUser, String username, String token, String totalGudang, String totalCustomer, String totalProject) {
        this.idUser = idUser;
        this.username = username;
        this.token = token;
        this.totalGudang = totalGudang;
        this.totalCustomer = totalCustomer;
        this.totalProject = totalProject;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTotalGudang() {
        return totalGudang;
    }

    public void setTotalGudang(String totalGudang) {
        this.totalGudang = totalGudang;
    }

    public String getTotalCustomer() {
        return totalCustomer;
    }

    public void setTotalCustomer(String totalCustomer) {
        this.totalCustomer = totalCustomer;
    }

    public String getTotalProject() {
        return totalProject;
    }

    public void setTotalProject(String totalProject) {
        this.totalProject = totalProject;
    }
}
