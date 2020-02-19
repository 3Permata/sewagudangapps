package com.tigapermata.sewagudangapps.model.stockcount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasterStockCount {

    @SerializedName("no")
    @Expose
    private String noStockCount;

    @SerializedName("id_stock_count")
    @Expose
    private String idStockCount;

    @SerializedName("tgl")
    @Expose
    private String tanggal;

    @SerializedName("kode")
    @Expose
    private String kodeStockCount;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("last_update")
    @Expose
    private String lastUpdate;

    public MasterStockCount (String noStockCount, String idStockCount, String tanggal, String kodeStockCount, String user, String lastUpdate) {
        this.noStockCount = noStockCount;
        this.idStockCount = idStockCount;
        this.tanggal = tanggal;
        this.kodeStockCount = kodeStockCount;
        this.user = user;
        this.lastUpdate = lastUpdate;
    }

    public String getNoStockCount() {
        return noStockCount;
    }

    public void setNoStockCount(String noStockCount) {
        this.noStockCount = noStockCount;
    }

    public String getIdStockCount() {
        return idStockCount;
    }

    public void setIdStockCount(String idStockCount) {
        this.idStockCount = idStockCount;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKodeStockCount() {
        return kodeStockCount;
    }

    public void setKodeStockCount(String kodeStockCount) {
        this.kodeStockCount = kodeStockCount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
