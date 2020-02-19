package com.tigapermata.sewagudangapps.model.putaway;

public class DataPutAway {
    private String oldLocator;
    private String filter;
    private String item;
    private String newLocator;

    public DataPutAway(String oldLocator, String filter, String item, String newLocator) {
        this.oldLocator = oldLocator;
        this.filter = filter;
        this.item = item;
        this.newLocator = newLocator;
    }

    public String getOldLocator() {
        return oldLocator;
    }

    public void setOldLocator(String oldLocator) {
        this.oldLocator = oldLocator;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getNewLocator() {
        return newLocator;
    }

    public void setNewLocator(String newLocator) {
        this.newLocator = newLocator;
    }
}
