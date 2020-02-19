package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataItem {
    @SerializedName("id_item")
    @Expose
    private String idItem;

    @SerializedName("kode_item")
    @Expose
    private String kodeItem;

    @SerializedName("description_item")
    @Expose
    private String descriptionItem;

    public DataItem(String idItem, String kodeItem, String descriptionItem) {
        this.idItem = idItem;
        this.kodeItem = kodeItem;
        this.descriptionItem = descriptionItem;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getKodeItem() {
        return kodeItem;
    }

    public void setKodeItem(String kodeItem) {
        this.kodeItem = kodeItem;
    }

    public String getDescriptionItem() {
        return descriptionItem;
    }

    public void setDescriptionItem(String descriptionItem) {
        this.descriptionItem = descriptionItem;
    }
}
