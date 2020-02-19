package com.tigapermata.sewagudangapps.adapter.outbound;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.outbound.ItemOutgoing;

import java.util.ArrayList;

public class ItemOutgoingAdapter extends RecyclerView.Adapter<ItemOutgoingAdapter.ViewHolder> implements Filterable {
    Context context;
    ViewGroup mParent;
    private ArrayList<ItemOutgoing> itemOutgoingArrayList;
    private ArrayList<ItemOutgoing> itemOutgoingFiltered;
    ItemOutgoingListener listener;

    public interface ItemOutgoingListener {
        void onItemOutgoingClick(ItemOutgoing outgoing, int position);
    }

    public ItemOutgoingAdapter(Context context, ArrayList<ItemOutgoing> itemOutgoingArrayList, ItemOutgoingListener outgoingListener) {
        this.context = context;
        this.itemOutgoingArrayList = itemOutgoingArrayList;
        this.itemOutgoingFiltered = itemOutgoingArrayList;
        this.listener = outgoingListener;
    }

    @Override
    public int getItemCount() {
        if (itemOutgoingFiltered == null) {
            return itemOutgoingArrayList.size();
        } else {
            return itemOutgoingFiltered.size();
        }
//        return itemIncomingFiltered.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_item_outgoing, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemOutgoingAdapter.ViewHolder holder, final int position) {

        final ItemOutgoing outgoing = itemOutgoingFiltered.get(position);

        holder.tvNamaItem.setText(outgoing.getNamaItem());
        holder.tvQty.setText(outgoing.getQtyLoad() + " / " + outgoing.getQty());
        holder.tvLabel.setText(outgoing.getLabel());
        holder.tvSatuan.setText(outgoing.getSatuan());
        holder.tvLabelInv.setText(outgoing.getLabelInventory());

        holder.tvDataLocator.setText(outgoing.getNamaLocator());
        if (holder.tvDataLocator.getText().toString().matches(""))
            holder.tvDataLocator.setText("-");
        holder.status.setText(outgoing.getStatus());
        if (outgoing.getStatus().matches("picking")) {
            holder.status.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.purple_picked, null));
        }
        else if (outgoing.getStatus().matches("partial loading")) {
            holder.status.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.blue_picking, null));
        }
        else {
            holder.status.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.yellow_incomplete, null));
        }

        holder.btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemOutgoingClick(itemOutgoingArrayList.get(position), position);
            }
        });

        if (outgoing.getQty().equals(outgoing.getQtyLoad())) {
            holder.btnLoad.setVisibility(View.GONE);
        }
        else {
            holder.btnLoad.setVisibility(View.VISIBLE);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    itemOutgoingFiltered = itemOutgoingArrayList;
                } else {
                    ArrayList<ItemOutgoing> filteredList = new ArrayList<>();
                    for (ItemOutgoing row: itemOutgoingArrayList) {
                        if (row.getLabel().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getLabelInventory().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getNamaItem().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    itemOutgoingFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemOutgoingFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemOutgoingFiltered = (ArrayList<ItemOutgoing>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public ItemOutgoing getItem(int position) {
        if (itemOutgoingFiltered == null) {
            return itemOutgoingArrayList.get(position);
        } else {
            if (itemOutgoingFiltered.size() == 0) return null;
            else return itemOutgoingFiltered.get(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaItem, tvQty, tvLabel, tvLabelInv, tvDataLocator, tvSatuan, status;
        CardView cardView;
        Button btnLoad;

        public ViewHolder(View convertView) {
            super(convertView);

            tvNamaItem = convertView.findViewById(R.id.ic_item);
            tvLabel = convertView.findViewById(R.id.ic_label);
            tvLabelInv = convertView.findViewById(R.id.textLabelInventory);
            tvQty = convertView.findViewById(R.id.ic_qty);
            tvDataLocator = convertView.findViewById(R.id.ic_locator);
            tvSatuan = convertView.findViewById(R.id.textViewSatuan);
            status = convertView.findViewById(R.id.textStatus);
            btnLoad = convertView.findViewById(R.id.buttonLoad);
            cardView = convertView.findViewById(R.id.ic_cardView);
        }
    }
}
