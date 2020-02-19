package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FormPackingList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("form_packinglist")
    @Expose
    private ArrayList<FormPacking> formPackingArrayList;

    public FormPackingList(String idUser, String token, ArrayList<FormPacking> formPackingArrayList) {
        this.idUser = idUser;
        this.token = token;
        this.formPackingArrayList = formPackingArrayList;
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

    public ArrayList<FormPacking> getFormPackingArrayList() {
        return formPackingArrayList;
    }

    public void setFormPackingArrayList(ArrayList<FormPacking> formPackingArrayList) {
        this.formPackingArrayList = formPackingArrayList;
    }
}
