package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutocompleteItem {
    @SerializedName("id_item")
    @Expose
    private String idItem;

    @SerializedName("kode_item")
    @Expose
    private String kodeItem;

    @SerializedName("nama_item")
    @Expose
    private String namaItem;

    @SerializedName("uom")
    @Expose
    private String uom;

    @SerializedName("actual_weight")
    @Expose
    private String actualWeight;

    @SerializedName("actual_cbm")
    @Expose
    private String actualCBM;

    public AutocompleteItem(String idItem, String kodeItem, String namaItem, String uom, String actualWeight, String actualCBM) {
        this.idItem = idItem;
        this.kodeItem = kodeItem;
        this.namaItem = namaItem;
        this.uom = uom;
        this.actualWeight = actualWeight;
        this.actualCBM = actualCBM;
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

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(String actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getActualCBM() {
        return actualCBM;
    }

    public void setActualCBM(String actualCBM) {
        this.actualCBM = actualCBM;
    }
}
