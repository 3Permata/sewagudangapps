package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataIncomingByItem {
    @SerializedName("no")
    @Expose
    private String no;

    @SerializedName("nama_item")
    @Expose
    private String namaItem;

    @SerializedName("id_item")
    @Expose
    private String idItem;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("qty_aktual")
    @Expose
    private String qtyAktual;

    public DataIncomingByItem(String no, String namaItem, String idItem, String qty, String qtyAktual) {
        this.no = no;
        this.namaItem = namaItem;
        this.idItem = idItem;
        this.qty = qty;
        this.qtyAktual = qtyAktual;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQtyAktual() {
        return qtyAktual;
    }

    public void setQtyAktual(String qtyAktual) {
        this.qtyAktual = qtyAktual;
    }
}
