package com.tigapermata.sewagudangapps.adapter.inbound;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.ItemIncoming;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class ItemIncomingAdapter extends RecyclerView.Adapter<ItemIncomingAdapter.ViewHolder> implements Filterable {

    Context context;
    ViewGroup mParent;
    private ArrayList<ItemIncoming> itemIncomingArrayList;
    private ArrayList<ItemIncoming> itemIncomingFiltered;
    ItemIncomingListener listener;

    public ItemIncomingAdapter(Context context, ArrayList<ItemIncoming> itemIncomingArrayList, ItemIncomingListener incomingListener) {
        this.context = context;
        this.itemIncomingArrayList = itemIncomingArrayList;
        this.itemIncomingFiltered = itemIncomingArrayList;
        this.listener = incomingListener;
    }

    @Override
    public int getItemCount() {
        if (itemIncomingFiltered == null) {
            return itemIncomingArrayList.size();
        } else {
            return itemIncomingFiltered.size();
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_item_incoming, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemIncomingAdapter.ViewHolder holder, final int position) {

        final ItemIncoming incoming = itemIncomingFiltered.get(position);

        holder.tvNamaItem.setText(incoming.getNamaItem());
        holder.tvQty.setText(incoming.getQty());
        holder.tvLabel.setText(incoming.getLabel());
        holder.tvDataLocator.setText(incoming.getDataLocator());
        if (holder.tvDataLocator.getText().toString().matches(""))
            holder.tvDataLocator.setText("-");
        holder.tvQtyAktual.setText(incoming.getQtyAktual());
        if (holder.tvQtyAktual.getText().toString().matches(""))
            holder.tvQtyAktual.setText("0");
        holder.status.setText(incoming.getStatusItem());
        if (incoming.getStatusItem().matches("ceklist")) {
            holder.status.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.blue_done, null));
        }
        else {
            holder.status.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.green_complete, null));
        }

        String qtyActual = incoming.getQtyAktual();
        if (qtyActual == null) qtyActual = "0";
        String qtyDocument = incoming.getQty();

        if (qtyActual.equals(qtyDocument)) {
            holder.btnCheck.setVisibility(View.INVISIBLE);
        }
        else {
            holder.btnCheck.setVisibility(View.VISIBLE);
        }

        holder.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemIncomingClick(itemIncomingArrayList.get(position), position);
            }
        });
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    itemIncomingFiltered = itemIncomingArrayList;
                } else {
                    ArrayList<ItemIncoming> filteredList = new ArrayList<>();
                    for (ItemIncoming row: itemIncomingArrayList) {
                        if (row.getLabel().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getDataLocator().toLowerCase().contains(charString.toLowerCase()) ||
                        row.getNamaItem().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    itemIncomingFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemIncomingFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemIncomingFiltered = (ArrayList<ItemIncoming>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaItem, tvQty, tvLabel, tvDataLocator, tvQtyAktual, status;
        CardView cardView;
        Button btnCheck;

        public ViewHolder(View convertView) {
            super(convertView);

            tvNamaItem = convertView.findViewById(R.id.ic_item);
            tvLabel = convertView.findViewById(R.id.ic_label);
            tvQty = convertView.findViewById(R.id.ic_qty);
            tvDataLocator = convertView.findViewById(R.id.ic_locator);
            tvQtyAktual = convertView.findViewById(R.id.ic_qty_aktual);
            status = convertView.findViewById(R.id.textStatus);
            btnCheck = convertView.findViewById(R.id.buttonCheck);
            cardView = convertView.findViewById(R.id.ic_cardView);
        }
    }

    public interface ItemIncomingListener {
        void onItemIncomingClick(ItemIncoming incoming, int position);
    }

}