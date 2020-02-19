package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FormOutgoingList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("form_outgoing")
    @Expose
    private ArrayList<FormOutgoing> formOutgoings;

    public FormOutgoingList(String idUser, String token, ArrayList<FormOutgoing> formOutgoings) {
        this.idUser = idUser;
        this.token = token;
        this.formOutgoings = formOutgoings;
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

    public ArrayList<FormOutgoing> getFormIncomings() {
        return formOutgoings;
    }

    public void setFormIncomings(ArrayList<FormOutgoing> formIncomings) {
        this.formOutgoings = formIncomings;
    }
}
