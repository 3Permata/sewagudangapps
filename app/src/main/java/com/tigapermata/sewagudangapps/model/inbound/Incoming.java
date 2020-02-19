package com.tigapermata.sewagudangapps.model.inbound;

import android.media.MediaDrm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Incoming {
    @SerializedName("id_incoming")
    @Expose
    private String idIncoming;

    @SerializedName("tgl")
    @Expose
    private String tglIncoming;

    @SerializedName("no_incoming")
    @Expose
    private String noIncoming;

    @SerializedName("data_lain")
    @Expose
    private ArrayList<DataLain> dataLainArrayList;

    public Incoming(String idIncoming, String tglIncoming, String noIncoming, ArrayList<DataLain> dataLainArrayList) {
        this.idIncoming = idIncoming;
        this.tglIncoming = tglIncoming;
        this.noIncoming = noIncoming;
        this.dataLainArrayList = dataLainArrayList;
    }

    public String getIdIncoming() {
        return idIncoming;
    }

    public void setIdIncoming(String idIncoming) {
        this.idIncoming = idIncoming;
    }

    public String getTglIncoming() {
        return tglIncoming;
    }

    public void setTglIncoming(String tglIncoming) {
        this.tglIncoming = tglIncoming;
    }

    public String getNoIncoming() {
        return noIncoming;
    }

    public void setNoIncoming(String noIncoming) {
        this.noIncoming = noIncoming;
    }

    public ArrayList<DataLain> getDataLainArrayList() {
        return dataLainArrayList;
    }

    public void setDataLainArrayList(ArrayList<DataLain> dataLainArrayList) {
        this.dataLainArrayList = dataLainArrayList;
    }
}
