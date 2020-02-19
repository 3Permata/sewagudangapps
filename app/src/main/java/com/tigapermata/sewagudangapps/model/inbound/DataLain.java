package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataLain {
    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("isi")
    @Expose
    private  String isi;

    public DataLain(String label, String isi) {
        this.label = label;
        this.isi = isi;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }
}
