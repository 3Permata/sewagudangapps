package com.tigapermata.sewagudangapps.model.putaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataFilterByLabelList {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private ArrayList<DataFilterByLabel> dataFilterByLabels;

    public DataFilterByLabelList(String idUser, String token, ArrayList<DataFilterByLabel> dataFilterByLabels) {
        this.idUser = idUser;
        this.token = token;
        this.dataFilterByLabels = dataFilterByLabels;
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

    public ArrayList<DataFilterByLabel> getDataFilterByLabels() {
        return dataFilterByLabels;
    }

    public void setDataFilterByLabels(ArrayList<DataFilterByLabel> dataFilterByLabels) {
        this.dataFilterByLabels = dataFilterByLabels;
    }
}
