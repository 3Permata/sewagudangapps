package com.tigapermata.sewagudangapps.adapter.outbound;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.outbound.DataPicking;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class PickingAdapter extends RecyclerView.Adapter<PickingAdapter.ViewHolder> implements Filterable {

    Context context;
    ViewGroup mParent;
    private ArrayList<DataPicking> dataPickingArrayList;
    private ArrayList<DataPicking> dataPickingArrayListFiltered;
    PickingListener listener;
    UndoPickingListener undoListener;


    public PickingAdapter(Context context, ArrayList<DataPicking> dataPickings, PickingListener mListener, UndoPickingListener undListener) {
        this.context = context;
        this.dataPickingArrayList = dataPickings;
        this.dataPickingArrayListFiltered = dataPickings;
        this.listener = mListener;
        this.undoListener = undListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_picking, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final DataPicking dataPicking = dataPickingArrayListFiltered.get(position);

        holder.tvNamaItem.setText(dataPicking.getNamaItem());
        holder.tvLabelInv.setText(dataPicking.getLabelInventory());
        holder.tvLabel.setText(dataPicking.getLabel());
        holder.tvLocator.setText(dataPicking.getNamaLocator());
        holder.tvStatus.setText(dataPicking.getStatus());
        holder.tvQty.setText(dataPicking.getQty());
        holder.tvSatuan.setText(dataPicking.getSatuan());

        String picking = "picking";
        String status =  dataPicking.getStatus();
        if (status.equals(picking)) {
            holder.btnPick.setText("Undo");
            holder.tvStatus.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.blue_picking, null));
            holder.btnPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    undoListener.undoOnclick(dataPicking, position);
                }
            });
        } else {
            holder.btnPick.setText("Pick");
            holder.tvStatus.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.pink_ready, null));
            holder.btnPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.pickOnclick(dataPicking, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (dataPickingArrayListFiltered == null) {
            return dataPickingArrayList.size();
        } else {
            return dataPickingArrayListFiltered.size();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    dataPickingArrayListFiltered = dataPickingArrayList;
                } else {
                    ArrayList<DataPicking> filteredList = new ArrayList<>();
                    for (DataPicking row: dataPickingArrayList) {
                        if (row.getLabel().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getLabelInventory().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getNamaItem().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    dataPickingArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataPickingArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataPickingArrayListFiltered = (ArrayList<DataPicking>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaItem, tvLabelInv, tvLabel, tvLocator, tvStatus, tvQty, tvSatuan;
        Button btnPick;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNamaItem = itemView.findViewById(R.id.nama_item_picking);
            tvLabelInv = itemView.findViewById(R.id.label_inventory_picking);
            tvLabel = itemView.findViewById(R.id.label_picking);
            tvLocator = itemView.findViewById(R.id.locator_picking);
            tvStatus = itemView.findViewById(R.id.status_picking);
            tvQty = itemView.findViewById(R.id.qty_picking);
            tvSatuan = itemView.findViewById(R.id.satuan_picking);

            btnPick = itemView.findViewById(R.id.btn_pick);
        }
    }

    public interface PickingListener {
        void pickOnclick(DataPicking picking, int position);
    }

    public interface UndoPickingListener {
        void undoOnclick(DataPicking dataPicking, int position);
    }
}
