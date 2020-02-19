package com.tigapermata.sewagudangapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gudang {

    @SerializedName("id_gudang")
    @Expose
    private String idGudang;

    @SerializedName("nama_gudang")
    @Expose
    private String namaGudang;

    public Gudang(String idGudang, String namaGudang) {
        this.idGudang = idGudang;
        this.namaGudang = namaGudang;
    }

    public String getIdGudang() {
        return idGudang;
    }

    public void setIdGudang(String idGudang) {
        this.idGudang = idGudang;
    }

    public String getNamaGudang() {
        return namaGudang;
    }

    public void setNamaGudang(String namaGudang) {
        this.namaGudang = namaGudang;
    }
}
