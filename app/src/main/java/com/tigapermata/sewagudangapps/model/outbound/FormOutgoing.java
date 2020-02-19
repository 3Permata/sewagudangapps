package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormOutgoing {
    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("jenis")
    @Expose
    private String jenis;

    public FormOutgoing(String label, String jenis) {
        this.label = label;
        this.jenis = jenis;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
