package com.tigapermata.sewagudangapps.adapter.inbound;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.DataIncomingByItem;
import com.tigapermata.sewagudangapps.model.inbound.ItemIncoming;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class IncomingAdapterByItem extends RecyclerView.Adapter<IncomingAdapterByItem.ViewHolder> {

    Context context;
    private ArrayList<DataIncomingByItem> dataIncomingByItemArrayList;
    private ArrayList<DataIncomingByItem> dataIncomingByItemArrayListFiltered;
    IncomingListenerByItem listenerByItems;

    public IncomingAdapterByItem(Context context, ArrayList<DataIncomingByItem> dataIncomingByItems, IncomingListenerByItem listener) {
        this.context = context;
        this.dataIncomingByItemArrayList = dataIncomingByItems;
        this.dataIncomingByItemArrayListFiltered = dataIncomingByItems;
        this.listenerByItems = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_incoming_by_item, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.namaItem.setText(dataIncomingByItemArrayList.get(position).getNamaItem());
        String s;
        if (dataIncomingByItemArrayList.get(position).getQtyAktual() == null)
            s = "0 / ";
        else
            s = dataIncomingByItemArrayList.get(position).getQtyAktual() + " / ";
        s += dataIncomingByItemArrayList.get(position).getQty();
        holder.qty.setText(s);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerByItems.onItemIncomingClicked(dataIncomingByItemArrayList.get(position), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dataIncomingByItemArrayListFiltered == null) {
            return dataIncomingByItemArrayList.size();
        } else {
            return dataIncomingByItemArrayListFiltered.size();
        }
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

    public interface IncomingListenerByItem {
        void onItemIncomingClicked(DataIncomingByItem incomingByItem, int position);
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    listenerByItems = (IncomingAdapterByItem.IncomingListenerByItem) dataIncomingByItemArrayList;
                } else {
                    ArrayList<DataIncomingByItem> filteredList = new ArrayList<>();
                    for (DataIncomingByItem row: dataIncomingByItemArrayList) {
                        if (row.getNamaItem().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    dataIncomingByItemArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataIncomingByItemArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataIncomingByItemArrayListFiltered = (ArrayList<DataIncomingByItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
