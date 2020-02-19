package com.tigapermata.sewagudangapps.model.outbound;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataPickingChecklist {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data_picking")
    @Expose
    private ArrayList<DataLabelItemPicked> dataLabelItemPickedArrayList;

    @SerializedName("total_item_terscan")
    @Expose
    private String totalScanned;

    @SerializedName("total_item_picking")
    @Expose
    private String totalPicking;

    public DataPickingChecklist(String idUser, String token, ArrayList<DataLabelItemPicked> dataLabelItemPickedArrayList,
                                 String totalScanned, String totalPicking) {
        this.idUser = idUser;
        this.token = token;
        this.dataLabelItemPickedArrayList = dataLabelItemPickedArrayList;
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

    public ArrayList<DataLabelItemPicked> getDataLabelItemPickedArrayList() {
        return dataLabelItemPickedArrayList;
    }

    public void setDataLabelItemPickedArrayList(ArrayList<DataLabelItemPicked> dataLabelItemPickedArrayList) {
        this.dataLabelItemPickedArrayList = dataLabelItemPickedArrayList;
    }

    public String getTotalScanned() {
        return totalScanned;
    }

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