package com.tigapermata.sewagudangapps.adapter.stockcount;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.stockcount.DetailStockCountByItem;

import java.util.ArrayList;

public class DetailStockCountByItemAdapter extends RecyclerView.Adapter<DetailStockCountByItemAdapter.ViewHolder> implements Filterable {
    ViewGroup mParent;
    Context mContext;
    private ArrayList<DetailStockCountByItem> detailSCByItemArrayList;
    private ArrayList<DetailStockCountByItem> detailSCByItemArrayListFiltered;
    private DetailStockCountListener detailSCListener;

    public DetailStockCountByItemAdapter (Context mContext, ArrayList<DetailStockCountByItem> detailSCByItemArrayList, DetailStockCountListener detailSCListener) {
        this.mContext = mContext;
        this.detailSCByItemArrayList = detailSCByItemArrayList;
        this.detailSCByItemArrayListFiltered = detailSCByItemArrayList;
        this.detailSCListener = detailSCListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_detail_sc_by_item, parent, false);
        DetailStockCountByItemAdapter.ViewHolder ovh = new DetailStockCountByItemAdapter.ViewHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        DetailStockCountByItem detailSC;

        if (detailSCByItemArrayListFiltered == null) {
            detailSC = detailSCByItemArrayList.get(position);

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailSCListener.onDetailStockCountClick(detailSCByItemArrayList.get(position), position);
                }
            });
        }
        else {
            detailSC = detailSCByItemArrayListFiltered.get(position);

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailSCListener.onDetailStockCountClick(detailSCByItemArrayListFiltered.get(position), position);
                }
            });
        }

        holder.tStatusDetailSC.setText(detailSC.getStatus());
        holder.tNamaItem.setText(detailSC.getItem());
        holder.tLocator.setText(detailSC.getLocator());

        if (detailSC.getStatus().matches("Match"))
            holder.tStatusDetailSC.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.green_complete, null));
        else
            holder.tStatusDetailSC.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.red_unmatch, null));
    }

    @Override
    public int getItemCount() {
        if (detailSCByItemArrayListFiltered == null) {
            if (detailSCByItemArrayList == null)
                return 0;
            else
                return detailSCByItemArrayList.size();
        }
        else
            return  detailSCByItemArrayListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    detailSCByItemArrayListFiltered = detailSCByItemArrayList;
                }
                else {
                    ArrayList<DetailStockCountByItem> filteredList = new ArrayList<>();
                    for (DetailStockCountByItem row : detailSCByItemArrayList) {
                        if ((row.getUser() != null && row.getUser().toLowerCase().contains(charString.toLowerCase())) ||
                                row.getItem().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getStatus().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    detailSCByItemArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = detailSCByItemArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                detailSCByItemArrayListFiltered = (ArrayList<DetailStockCountByItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tTanggalDetailSC, tStatusDetailSC, tUser, tNamaItem, tLocator, tQtyInput, tQtyAktual, tHasil;

        public ViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cardview_detail_sc);
            tStatusDetailSC = itemView.findViewById(R.id.status_detail_sc);
            tNamaItem = itemView.findViewById(R.id.nama_item);
            tLocator = itemView.findViewById(R.id.textViewLocator);
        }
    }

    public interface DetailStockCountListener {
        void onDetailStockCountClick(DetailStockCountByItem detailSC, int position);
    }
}
