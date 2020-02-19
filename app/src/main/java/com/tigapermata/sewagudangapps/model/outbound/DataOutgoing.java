package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tigapermata.sewagudangapps.model.inbound.DataLain;

import java.util.ArrayList;

public class DataOutgoing {
    @SerializedName("id_outgoing")
    @Expose
    private String idOutgoing;

    @SerializedName("tgl")
    @Expose
    private String tanggal;

    @SerializedName("no_outgoing")
    @Expose
    private String noOutgoing;

    @SerializedName("data_lain")
    @Expose
    private ArrayList<DataLain> dataLainArrayList;

    public DataOutgoing(String idOutgoing, String tanggal, String noOutgoing, ArrayList<DataLain> dataLainArrayList) {
        this.idOutgoing = idOutgoing;
        this.tanggal = tanggal;
        this.noOutgoing = noOutgoing;
        this.dataLainArrayList = dataLainArrayList;
    }

    public String getIdOutgoing() {
        return idOutgoing;
    }

    public void setIdOutgoing(String idOutgoing) {
        this.idOutgoing = idOutgoing;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNoOutgoing() {
        return noOutgoing;
    }

    public void setNoOutgoing(String noOutgoing) {
        this.noOutgoing = noOutgoing;
    }

    public ArrayList<DataLain> getDataLainArrayList() {
        return dataLainArrayList;
    }

    public void setDataLainArrayList(ArrayList<DataLain> dataLainArrayList) {
        this.dataLainArrayList = dataLainArrayList;
    }
}
