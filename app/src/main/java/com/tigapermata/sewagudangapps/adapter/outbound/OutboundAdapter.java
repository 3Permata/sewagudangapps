package com.tigapermata.sewagudangapps.adapter.outbound;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.tigapermata.sewagudangapps.model.outbound.Outbound;

import java.util.ArrayList;

public class OutboundAdapter extends RecyclerView.Adapter<OutboundAdapter.ViewHolder> implements Filterable {

    ViewGroup mParent;
    Context mContext;
    private ArrayList<Outbound> outboundArrayList;
    private ArrayList<Outbound> outboundArrayListFiltered;
    private OutboundListener outboundListener;
    private OutboundDoneListener outboundDoneListener;

    public OutboundAdapter(Context context, ArrayList<Outbound> outbounds, OutboundListener outboundListener, OutboundDoneListener outboundDoneListener) {
        this.mContext = context;
        this.outboundArrayList = outbounds;
        this.outboundArrayListFiltered = outbounds;
        this.outboundListener = outboundListener;
        this.outboundDoneListener = outboundDoneListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_outbound, parent, false);
        OutboundAdapter.ViewHolder ovh = new OutboundAdapter.ViewHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Outbound outbound;

        if (outboundArrayListFiltered == null) {
            outbound = outboundArrayList.get(position);

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    outboundListener.onOutboundClick(outboundArrayList.get(position), position);
                }
            });
            holder.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outboundDoneListener.onOutboundDoneClick(outboundArrayList.get(position), position);
                }
            });
        }
        else {
            outbound = outboundArrayListFiltered.get(position);

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    outboundListener.onOutboundClick(outboundArrayListFiltered.get(position), position);
                }
            });
            holder.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outboundDoneListener.onOutboundDoneClick(outboundArrayListFiltered.get(position), position);
                }
            });
        }

        holder.noOutbound.setText(outbound.getNoOutbound());
        holder.tglOutbound.setText(outbound.getTglOutbound());

        String picking, allocated;
        if (outbound.getTotalPicking().matches("")) picking = "0";
        else picking = outbound.getTotalPicking();
        if (outbound.getTotalAllocated().matches("")) allocated = "0";
        else allocated = outbound.getTotalAllocated();
        holder.qtyOutbound.setText(picking + " / " + allocated);

        if (outbound.getRefOutbound().matches(""))
            holder.refOutbound.setText("-");
        else  holder.refOutbound.setText(outbound.getRefOutbound());

        holder.statusOutbound.setText(outbound.getStatusOutbond());
        if (outbound.getStatusOutbond().matches("ready"))
            holder.statusOutbound.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.pink_ready, null));
        else if (outbound.getStatusOutbond().matches("picking"))
            holder.statusOutbound.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.blue_picking, null));
        else if (outbound.getStatusOutbond().matches("loading"))
            holder.statusOutbound.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.yellow_incomplete, null));
        else
            holder.statusOutbound.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.purple_picked, null));

        if (outbound.getStatusOutbond().matches("ready") || outbound.getStatusOutbond().matches("picking"))
            if (outbound.getTotalAllocated().matches(outbound.getTotalPicking()))
                holder.btnDone.setVisibility(View.VISIBLE);
            else holder.btnDone.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        if (outboundArrayListFiltered == null) {
            if (outboundArrayList == null)
                return 0;
            else
                return outboundArrayList.size();
        }
        else
            return  outboundArrayListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    outboundListener = (OutboundListener) outboundArrayList;
                } else {
                    ArrayList<Outbound> filteredList = new ArrayList<>();
                    for (Outbound row : outboundArrayList) {
                        if (row.getTglOutbound().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getNoOutbound().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getRefOutbound().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    outboundArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = outboundArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                outboundArrayListFiltered = (ArrayList<Outbound>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView noOutbound, tglOutbound, qtyOutbound, refOutbound, statusOutbound;
        Button btnDone;

        public ViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cardview_outbound);
            noOutbound = itemView.findViewById(R.id.no_outbound);
            tglOutbound = itemView.findViewById(R.id.tgl_outbound);
            qtyOutbound = itemView.findViewById(R.id.qty_outbound);
            refOutbound = itemView.findViewById(R.id.ref_outbound);
            statusOutbound = itemView.findViewById(R.id.status_outbound);
            btnDone = itemView.findViewById(R.id.buttonDonePicking);
        }
    }

    public interface OutboundListener {
        void onOutboundClick(Outbound outbound, int position);
    }

    public interface OutboundDoneListener {
        void onOutboundDoneClick(Outbound outbound, int position);
    }
}
