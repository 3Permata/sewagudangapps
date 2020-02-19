package com.tigapermata.sewagudangapps.adapter.inbound;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.DataLain;
import com.tigapermata.sewagudangapps.model.inbound.Incoming;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.util.ArrayList;
import java.util.List;

public class IncomingAdapter extends RecyclerView.Adapter<IncomingAdapter.ViewHolder> implements Filterable {

    Context context;
    private ArrayList<Incoming> incomingList;
    private ArrayList<Incoming> incomingListFiltered;
    IncomingListener listener;

    private final RecyclerView.RecycledViewPool recycledViewPool;
    private ArrayList<DataLain> dataLainArrayList;
    private IncomingDataLainAdapter mAdapter;


    public IncomingAdapter(Context context, ArrayList<Incoming> incomingList, IncomingListener listener) {
        this.context = context;
        this.incomingList = incomingList;
        this.incomingListFiltered = incomingList;
        this.listener = listener;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.incoming_outer_rv, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.rvOuter.setRecycledViewPool(recycledViewPool);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Incoming incoming;
        if (incomingListFiltered == null) {
            incoming = incomingList.get(position);

            holder.incomingCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onIncomingClick(incomingList.get(position), position);
                }
            });
            holder.ibGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onIncomingClick(incomingList.get(position), position);
                }
            });
        }
        else {
            incoming = incomingListFiltered.get(position);
            holder.incomingCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onIncomingClick(incomingListFiltered.get(position), position);
                }
            });
            holder.ibGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onIncomingClick(incomingListFiltered.get(position), position);
                }
            });
        }

        holder.tvNo.setText(incoming.getNoIncoming());
        holder.tvTanggal.setText(incoming.getTglIncoming());

        //second recycler view
        dataLainArrayList = incoming.getDataLainArrayList();
        holder.rvOuter.setHasFixedSize(true);
        holder.rvOuter.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new IncomingDataLainAdapter(dataLainArrayList);
        holder.rvOuter.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        if (incomingListFiltered == null) {
            return incomingList.size();
        } else {
            return incomingListFiltered.size();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    listener = (IncomingListener) incomingList;
                } else {
                    ArrayList<Incoming> filteredList = new ArrayList<>();
                    for (Incoming row: incomingList) {
                        if (row.getNoIncoming().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getTglIncoming().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    incomingListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = incomingListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                incomingListFiltered = (ArrayList<Incoming>) results.values;
                notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTanggal, tvNo;
        ImageButton ibGo;
        CardView incomingCV;
        RecyclerView rvOuter;

        public ViewHolder(View itemView) {
            super(itemView);

            incomingCV = itemView.findViewById(R.id.cv_incoming_outer);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal_incoming);
            tvNo = itemView.findViewById(R.id.tv_no_incoming);
            rvOuter = itemView.findViewById(R.id.rv_outer_incoming);
            ibGo = itemView.findViewById(R.id.imageButtonGo);
        }
    }

    public interface IncomingListener {
        void onIncomingClick(Incoming incoming, int position);
    }

}
