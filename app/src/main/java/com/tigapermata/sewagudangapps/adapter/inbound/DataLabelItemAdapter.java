package com.tigapermata.sewagudangapps.adapter.inbound;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tigapermata.sewagudangapps.model.inbound.DataLabelItem;

import java.util.ArrayList;

public class DataLabelItemAdapter extends RecyclerView.Adapter<DataLabelItemAdapter.ViewHolder> {

    Context context;
    private ArrayList<DataLabelItem> dataLabelItemArrayList;

    public DataLabelItemAdapter(Context context, ArrayList<DataLabelItem> dataLabelItems) {
        this.context = context;
        this.dataLabelItemArrayList = dataLabelItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
