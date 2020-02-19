package com.tigapermata.sewagudangapps.model.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ItemOutgoingChecked {
    @SerializedName("data")
    @Expose
    public ArrayList<ItemOutgoing> itemOutgoingArrayList;

    @SerializedName("total_picking")
    @Expose
    private String totalPicking;

    @SerializedName("total_loading")
    @Expose
    private String totalLoading;

    public ItemOutgoingChecked(ArrayList<ItemOutgoing> itemOutgoingArrayList, String totalPicking, String totalLoading) {
        this.itemOutgoingArrayList = itemOutgoingArrayList;
        this.totalPicking = totalPicking;
        this.totalLoading = totalLoading;
    }

    public ArrayList<ItemOutgoing> getItemOutgoingArrayList() {
        return itemOutgoingArrayList;
    }

    public String getTotalPicking() { return totalPicking; }

    public String getTotalLoading() { return totalLoading; }
}
