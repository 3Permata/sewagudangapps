package com.tigapermata.sewagudangapps.model;

public class SavedId {

    private int id;
    private String idGudang, namaGudang;
    private String idProject, namaProject;

    public SavedId(int id, String idGudang, String idProject) {
        this.id = id;
        this.idGudang = idGudang;
        this.idProject = idProject;
    }

    public SavedId(String idGudang, String idProject) {
        this.idGudang = idGudang;
        this.idProject = idProject;
    }

    public SavedId(String idGudang, String namaGudang, String idProject, String namaProject) {
        this.idGudang = idGudang;
        this.namaGudang = namaGudang;
        this.idProject = idProject;
        this.namaProject = namaProject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdGudang() {
        return idGudang;
    }

    public void setIdGudang(String idGudang) {
        this.idGudang = idGudang;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getNamaGudang() { return namaGudang; }
    public String getNamaProject() { return namaProject; }

    public void setNamaGudang(String namaGudang) { this.namaGudang = namaGudang; }
    public void setNamaProject(String namaProject) { this.namaProject = namaProject; }
}
