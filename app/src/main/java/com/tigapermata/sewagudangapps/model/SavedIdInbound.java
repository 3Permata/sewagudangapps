package com.tigapermata.sewagudangapps.model;

public class SavedIdInbound {

    private String idInbound;
    private String idIncoming;

    public SavedIdInbound(String idInbound, String idIncoming) {
        this.idInbound = idInbound;
        this.idIncoming = idIncoming;
    }

    public String getIdInbound() {
        return idInbound;
    }

    public void setIdInbound(String idInbound) {
        this.idInbound = idInbound;
    }

    public String getIdIncoming() {
        return idIncoming;
    }

    public void setIdIncoming(String idIncoming) {
        this.idIncoming = idIncoming;
    }
}
