package com.tigapermata.sewagudangapps.adapter.outbound;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.outbound.DataPickingByItem;

import java.util.ArrayList;

public class PickingAdapterByItem extends RecyclerView.Adapter<PickingAdapterByItem.ViewHolder> {
    Context context;
    private ArrayList<DataPickingByItem> dataPickingByItemArrayList;
    PickingListenerByItem listenerByItems;

    public PickingAdapterByItem(Context context, ArrayList<DataPickingByItem> dataPickingByItemArrayList, PickingListenerByItem listener) {
        this.context = context;
        this.dataPickingByItemArrayList = dataPickingByItemArrayList;
        this.listenerByItems = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_picking_by_item, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.namaItem.setText(dataPickingByItemArrayList.get(position).getNamaItem());
        String s;
        if (dataPickingByItemArrayList.get(position).getQtyLoading() == null)
            s = "0 / ";
        else
            s = dataPickingByItemArrayList.get(position).getQtyLoading() + " / ";
        s += dataPickingByItemArrayList.get(position).getQty();
        holder.qty.setText(s);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerByItems.onItemPickingClicked(dataPickingByItemArrayList.get(position), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataPickingByItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaItem, qty;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);

            namaItem = itemView.findViewById(R.id.ic_nama_item);
            qty = itemView.findViewById(R.id.ic_qty_by_item);

            cv = itemView.findViewById(R.id.ic_cardView_by_item);
        }
    }

    public interface PickingListenerByItem {
        void onItemPickingClicked(DataPickingByItem PickingByItem, int position);
    }
}
