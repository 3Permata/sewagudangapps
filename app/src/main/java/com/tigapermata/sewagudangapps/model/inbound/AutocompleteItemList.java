package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AutocompleteItemList {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data_item")
    @Expose
    private ArrayList<AutocompleteItem> dataItems;

    public AutocompleteItemList(String idUser, String token, ArrayList<AutocompleteItem> dataItems) {
        this.idUser = idUser;
        this.token = token;
        this.dataItems = dataItems;
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

    public ArrayList<AutocompleteItem> getDataItems() {
        return dataItems;
    }

    public void setDataItems(ArrayList<AutocompleteItem> dataItems) {
        this.dataItems = dataItems;
    }
}
