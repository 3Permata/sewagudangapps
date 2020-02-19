package com.tigapermata.sewagudangapps.adapter.inbound;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.Inbound;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;
import java.util.List;

public class InboundAdapter extends RecyclerView.Adapter<InboundAdapter.ViewHolder> implements Filterable {

    ViewGroup mParent;
    private Context mContext;
    private List<Inbound> inboundList;
    private List<Inbound> inboundListFiltered;
    private InboundListener inboundListener;
    private InboundDetailListener inboundDetailListener;

    public InboundAdapter(Context context, List<Inbound> inboundList, InboundListener listener, InboundDetailListener detailListener) {
        this.mContext = context;
        this.inboundList = inboundList;
        this.inboundListFiltered = inboundList;
        this.inboundListener = listener;
        this.inboundDetailListener = detailListener;
    }

    @Override
    public InboundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_inbound, parent, false);
        InboundAdapter.ViewHolder ivh = new InboundAdapter.ViewHolder(v);

        return ivh;
    }

    @Override
    public void onBindViewHolder(InboundAdapter.ViewHolder holder, final int position) {
        Inbound inbound;
        if (inboundListFiltered == null) {
            inbound = inboundList.get(position);

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inboundListener.onInboundCLick(inboundList.get(position), position);
                }
            });
            holder.detailInbound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inboundDetailListener.onDetailInboundClick(inboundList.get(position), position);
                }
            });
        }
        else {
            inbound = inboundListFiltered.get(position);

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inboundListener.onInboundCLick(inboundListFiltered.get(position), position);
                }
            });
            holder.detailInbound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inboundDetailListener.onDetailInboundClick(inboundListFiltered.get(position), position);
                }
            });
        }

        holder.noInbound.setText(inbound.getNoInbound());
        if (inbound.getReferensi().matches(""))
            holder.refInbound.setText("-");
        else holder.refInbound.setText(inbound.getReferensi());

        holder.statusInbound.setText(inbound.getStatusInbound());
        if (inbound.getStatusInbound().matches("incomplete"))
            holder.statusInbound.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.yellow_incomplete, null));
        else holder.statusInbound.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.grey, null));
        holder.qtyActualInbound.setText(inbound.getQtyActual());
        holder.qtyDocumentInbound.setText(inbound.getQtyDocument());
        holder.tglInbound.setText(inbound.getTglInbound());
    }

    @Override
    public int getItemCount() {
        if (inboundListFiltered == null) {
            if (inboundList == null)
                return 0;
            else
                return inboundList.size();
        }
        else
            return  inboundListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    inboundListener = (InboundListener) inboundList;
                } else {
                    List<Inbound> filteredList = new ArrayList<>();
                    for (Inbound row : inboundList) {
                        if (row.getTglInbound().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getNoInbound().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getReferensi().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    inboundListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = inboundListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inboundListFiltered = (ArrayList<Inbound>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView noInbound, refInbound, qtyActualInbound, qtyDocumentInbound, statusInbound, tglInbound;
        ImageView detailInbound;

        public ViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cardview_inbound);

            noInbound = itemView.findViewById(R.id.no_inbound);
            refInbound = itemView.findViewById(R.id.ref_inbound);
            detailInbound = itemView.findViewById(R.id.dtl_inbound);
            qtyActualInbound = itemView.findViewById(R.id.qty_actual_inbound);
            qtyDocumentInbound = itemView.findViewById(R.id.qty_document_inbound);
            statusInbound = itemView.findViewById(R.id.status_inbound);
            tglInbound = itemView.findViewById(R.id.tgl_inbound);
        }
    }

    public interface InboundListener {
        void onInboundCLick(Inbound inbound, int position);
    }

    public interface InboundDetailListener {
        void onDetailInboundClick(Inbound inbound, int position);
    }
}
