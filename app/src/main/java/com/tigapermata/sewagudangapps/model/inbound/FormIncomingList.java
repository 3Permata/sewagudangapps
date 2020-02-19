package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FormIncomingList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("form_incoming")
    @Expose
    private ArrayList<FormIncoming> formIncomings;

    public FormIncomingList(String idUser, String token, ArrayList<FormIncoming> formIncomings) {
        this.idUser = idUser;
        this.token = token;
        this.formIncomings = formIncomings;
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

    public ArrayList<FormIncoming> getFormIncomings() {
        return formIncomings;
    }

    public void setFormIncomings(ArrayList<FormIncoming> formIncomings) {
        this.formIncomings = formIncomings;
    }
}
