package com.tigapermata.sewagudangapps.model.inbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormIncoming {
    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("jenis")
    @Expose
    private String jenis;

    public FormIncoming(String label, String jenis) {
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
