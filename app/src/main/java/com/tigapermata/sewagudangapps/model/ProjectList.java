package com.tigapermata.sewagudangapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProjectList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("projek")
    @Expose
    private List<Project> allProjectList = new ArrayList<Project>();

    public ProjectList(String idUser, String token, List<Project> allProjectList) {
        this.idUser = idUser;
        this.token = token;
        this.allProjectList = allProjectList;
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

    public List<Project> getAllProjectList() {
        return allProjectList;
    }

    public void setAllProjectList(List<Project> allProjectList) {
        this.allProjectList = allProjectList;
    }
}
