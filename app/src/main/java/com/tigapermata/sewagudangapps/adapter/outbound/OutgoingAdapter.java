package com.tigapermata.sewagudangapps.adapter.outbound;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.adapter.inbound.DataLainAdapter;
import com.tigapermata.sewagudangapps.adapter.inbound.IncomingAdapter;
import com.tigapermata.sewagudangapps.model.inbound.DataLain;
import com.tigapermata.sewagudangapps.model.inbound.Incoming;
import com.tigapermata.sewagudangapps.model.outbound.DataOutgoing;

import java.util.ArrayList;

public class OutgoingAdapter extends RecyclerView.Adapter<OutgoingAdapter.ViewHolder> implements Filterable {

    Context context;
    private ArrayList<DataOutgoing> outgoingArrayList;
    private ArrayList<DataOutgoing> outgoingArrayListFiltered;
    OutgoingListener listener;

    private ArrayList<DataLain> dataLainArrayList;
    private DataLainAdapter mAdapter;

    public OutgoingAdapter(Context context, ArrayList<DataOutgoing> outgoingList, OutgoingListener listener) {
        this.context = context;
        this.outgoingArrayList = outgoingList;
        this.outgoingArrayListFiltered = outgoingList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.outgoing_outer_rv, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DataOutgoing outgoing;
        if (outgoingArrayListFiltered == null) {
            outgoing = outgoingArrayList.get(position);

            holder.outgoingCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOutgoingClick(outgoingArrayList.get(position), position);
                }
            });
            holder.ibGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOutgoingClick(outgoingArrayList.get(position), position);
                }
            });
        }
        else {
            outgoing = outgoingArrayListFiltered.get(position);
            holder.outgoingCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOutgoingClick(outgoingArrayListFiltered.get(position), position);
                }
            });
            holder.ibGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOutgoingClick(outgoingArrayListFiltered.get(position), position);
                }
            });
        }

        holder.tvNo.setText(outgoing.getNoOutgoing());
        holder.tvTanggal.setText(outgoing.getTanggal());

        //second recycler view
        dataLainArrayList = outgoing.getDataLainArrayList();
        holder.rvOuter.setHasFixedSize(true);
        holder.rvOuter.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new DataLainAdapter(context, dataLainArrayList);
        holder.rvOuter.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        if (outgoingArrayListFiltered == null) {
            return outgoingArrayList.size();
        } else {
            return outgoingArrayListFiltered.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    listener = (OutgoingListener) outgoingArrayList;
                } else {
                    ArrayList<DataOutgoing> filteredList = new ArrayList<>();
                    for (DataOutgoing row: outgoingArrayList) {
                        if (row.getNoOutgoing().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getTanggal().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    outgoingArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = outgoingArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                outgoingArrayListFiltered = (ArrayList<DataOutgoing>) results.values;
                notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTanggal, tvNo;
        ImageButton ibGo;
        CardView outgoingCV;
        RecyclerView rvOuter;

        public ViewHolder(View itemView) {
            super(itemView);

            outgoingCV = itemView.findViewById(R.id.cv_outgoing_outer);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal_outgoing);
            tvNo = itemView.findViewById(R.id.tv_no_outgoing);
            rvOuter = itemView.findViewById(R.id.rv_outer_outgoing);
            ibGo = itemView.findViewById(R.id.imageButtonGo);
        }
    }

    public interface OutgoingListener {
        void onOutgoingClick(DataOutgoing dataOutgoing, int position);
    }
}
