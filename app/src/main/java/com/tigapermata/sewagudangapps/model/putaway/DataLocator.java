package com.tigapermata.sewagudangapps.model.putaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataLocator {
    @SerializedName("id_locator")
    @Expose
    private String idLocator;

    @SerializedName("nama_locator")
    @Expose
    private String namaLocator;

    public DataLocator(String idLocator, String namaLocator) {
        this.idLocator = idLocator;
        this.namaLocator = namaLocator;
    }

    public String getIdLocator() {
        return idLocator;
    }

    public void setIdLocator(String idLocator) {
        this.idLocator = idLocator;
    }

    public String getNamaLocator() {
        return namaLocator;
    }

    public void setNamaLocator(String namaLocator) {
        this.namaLocator = namaLocator;
    }
}
