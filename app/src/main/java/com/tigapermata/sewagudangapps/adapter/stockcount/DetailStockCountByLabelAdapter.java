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
import com.tigapermata.sewagudangapps.model.stockcount.DetailStockCountByLabel;

import java.util.ArrayList;

public class DetailStockCountByLabelAdapter extends RecyclerView.Adapter<DetailStockCountByLabelAdapter.ViewHolder> implements Filterable {
    ViewGroup mParent;
    Context mContext;
    private ArrayList<DetailStockCountByLabel> detailSCByLabelArrayList;
    private ArrayList<DetailStockCountByLabel> detailSCByLabelArrayListFiltered;
    private DetailStockCountListener detailSCListener;

    public DetailStockCountByLabelAdapter (Context mContext, ArrayList<DetailStockCountByLabel> detailSCByLabelArrayList, DetailStockCountListener detailSCListener) {
        this.mContext = mContext;
        this.detailSCByLabelArrayList = detailSCByLabelArrayList;
        this.detailSCByLabelArrayListFiltered = detailSCByLabelArrayList;
        this.detailSCListener = detailSCListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_detail_sc_by_label, parent, false);
        DetailStockCountByLabelAdapter.ViewHolder ovh = new DetailStockCountByLabelAdapter.ViewHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        DetailStockCountByLabel detailSC;

        if (detailSCByLabelArrayListFiltered == null) {
            detailSC = detailSCByLabelArrayList.get(position);

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailSCListener.onDetailStockCountClick(detailSCByLabelArrayList.get(position), position);
                }
            });
        }
        else {
            detailSC = detailSCByLabelArrayListFiltered.get(position);

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailSCListener.onDetailStockCountClick(detailSCByLabelArrayListFiltered.get(position), position);
                }
            });
        }

        holder.tStatusDetailSC.setText(detailSC.getStatus());
        holder.tNamaLabel.setText(detailSC.getLabel());
        holder.tLocator.setText(detailSC.getLocator());

        if (detailSC.getStatus().matches("Match"))
            holder.tStatusDetailSC.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.green_complete, null));
        else
            holder.tStatusDetailSC.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.red_unmatch, null));
    }

    @Override
    public int getItemCount() {
        if (detailSCByLabelArrayListFiltered == null) {
            if (detailSCByLabelArrayList == null)
                return 0;
            else
                return detailSCByLabelArrayList.size();
        }
        else
            return  detailSCByLabelArrayListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    detailSCByLabelArrayListFiltered = detailSCByLabelArrayList;
                }
                else {
                    ArrayList<DetailStockCountByLabel> filteredList = new ArrayList<>();
                    for (DetailStockCountByLabel row : detailSCByLabelArrayList) {
                        if ((row.getUser() != null && row.getUser().toLowerCase().contains(charString.toLowerCase())) ||
                                row.getLabel().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getStatus().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    detailSCByLabelArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = detailSCByLabelArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                detailSCByLabelArrayListFiltered = (ArrayList<DetailStockCountByLabel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tTanggalDetailSC, tStatusDetailSC, tUser, tNamaLabel, tLocator, tQtyInput, tQtyAktual, tHasil;

        public ViewHolder(View LabelView) {
            super(LabelView);

            cv = LabelView.findViewById(R.id.cardview_detail_sc);
            tStatusDetailSC = LabelView.findViewById(R.id.status_detail_sc);
            tNamaLabel = LabelView.findViewById(R.id.label);
            tLocator = LabelView.findViewById(R.id.textViewLocator);
        }
    }

    public interface DetailStockCountListener {
        void onDetailStockCountClick(DetailStockCountByLabel detailSC, int position);
    }
}
