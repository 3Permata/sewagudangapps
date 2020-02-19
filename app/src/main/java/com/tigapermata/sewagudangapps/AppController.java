package com.tigapermata.sewagudangapps;

import android.app.Application;

import com.tigapermata.sewagudangapps.activity.MainActivity;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;
    private String namaProject, idInbound, noInbound, referensi, idItem, qtyAktual, qtyDokumen, idOutbound, noOutbound, idOutgoing, namaItem, idStockcount;
    private String tipeScan;
    private MainActivity mainAct;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public String getNamaProject() {
        return namaProject;
    }

    public String getIdInbound() {
        return idInbound;
    }

    public String getNoInbound() {
        return noInbound;
    }

    public String getReferensi() {
        return referensi;
    }

    public String getIdItem() {
        return idItem;
    }

    public String getQtyAktual() {
        return qtyAktual;
    }

    public String getQtyDokumen() {
        return qtyDokumen;
    }

    public String getIdOutbound() { return idOutbound; }

    public String getNoOutbound() { return noOutbound; }

    public String getIdOutgoing() { return idOutgoing; }

    public String getNamaItem() { return namaItem; }

    public String getIdStockcount() { return idStockcount; }

    public String getTipeScan() { return tipeScan; }

    public MainActivity getMainAct() { return mainAct; }

    public void setNamaProject(String namaProject) {
        this.namaProject = namaProject;
    }

    public void setIdInbound(String idInbound) {
        this.idInbound = idInbound;
    }

    public void setNoInbound(String noInbound) {
        this.noInbound = noInbound;
    }

    public void setReferensi(String referensi) {
        this.referensi = referensi;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public void setQtyAktual(String qtyAktual) {
        this.qtyAktual = qtyAktual;
    }

    public void setQtyDokumen(String qtyDokumen) {
        this.qtyDokumen = qtyDokumen;
    }

    public void setIdOutbound(String idOutbound) { this.idOutbound = idOutbound; }

    public void setNoOutbound(String noOutbound) { this.noOutbound = noOutbound; }

    public void setIdOutgoing(String idOutgoing) { this.idOutgoing = idOutgoing; }

    public void setNamaItem(String namaItem) { this.namaItem = namaItem; }

    public void setIdStockcount(String idStockcount) { this.idStockcount = idStockcount; }

    public void setTipeScan(String tipeScan) { this.tipeScan = tipeScan; }

    public void setMainAct(MainActivity mainAct) { this.mainAct = mainAct; }
}
