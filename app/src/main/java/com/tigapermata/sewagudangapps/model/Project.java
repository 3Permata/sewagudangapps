package com.tigapermata.sewagudangapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Project {
    @SerializedName("id_project")
    @Expose
    private String idProject;

    @SerializedName("nama_project")
    @Expose
    private String namaProject;

    public Project(String idProject, String namaProject) {
        this.idProject = idProject;
        this.namaProject = namaProject;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getNamaProject() {
        return namaProject;
    }

    public void setNamaProject(String namaProject) {
        this.namaProject = namaProject;
    }
}
