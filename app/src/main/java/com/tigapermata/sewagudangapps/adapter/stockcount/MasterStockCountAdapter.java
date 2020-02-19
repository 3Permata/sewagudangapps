package com.tigapermata.sewagudangapps.adapter.stockcount;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.tigapermata.sewagudangapps.model.stockcount.MasterStockCount;

import java.util.ArrayList;

public class MasterStockCountAdapter extends RecyclerView.Adapter<MasterStockCountAdapter.ViewHolder> implements Filterable {
    ViewGroup mParent;
    Context mContext;
    private ArrayList<MasterStockCount> stockcountArrayList;
    private ArrayList<MasterStockCount> stockcountArrayListFiltered;
    private StockcountByItemListener stockcountByItemListener;
    private StockcountByLabelListener stockcountByLabelListener;

    public MasterStockCountAdapter (Context context, ArrayList<MasterStockCount> stockcountArrayList, StockcountByItemListener stockcountByItemListener, StockcountByLabelListener stockcountByLabelListener) {
        this.mContext = context;
        this.stockcountArrayList = stockcountArrayList;
        this.stockcountArrayListFiltered = stockcountArrayList;
        this.stockcountByItemListener = stockcountByItemListener;
        this.stockcountByLabelListener = stockcountByLabelListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_stockcount, parent, false);
        MasterStockCountAdapter.ViewHolder ovh = new MasterStockCountAdapter.ViewHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        MasterStockCount stockCount;

        if (stockcountArrayListFiltered == null) {
            stockCount = stockcountArrayList.get(position);

            holder.btnDetailByItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stockcountByItemListener.onStockCountDetailByItemClick(stockcountArrayList.get(position), position);
                }
            });
            holder.btnDetailByLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stockcountByLabelListener.onStockCountDetailByLabelClick(stockcountArrayList.get(position), position);
                }
            });
        }
        else {
            stockCount = stockcountArrayListFiltered.get(position);

            holder.btnDetailByItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stockcountByItemListener.onStockCountDetailByItemClick(stockcountArrayListFiltered.get(position), position);
                }
            });
            holder.btnDetailByLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stockcountByLabelListener.onStockCountDetailByLabelClick(stockcountArrayListFiltered.get(position), position);
                }
            });
        }

        holder.kodeStockCount.setText(stockCount.getKodeStockCount());
        holder.tglStockcount.setText(stockCount.getTanggal());
        holder.lastUpdate.setText(stockCount.getLastUpdate());

        if (stockCount.getUser() == null || stockCount.getUser().matches("")) holder.user.setText("-");
        else holder.user.setText(stockCount.getUser());
    }

    @Override
    public int getItemCount() {
        if (stockcountArrayListFiltered == null) {
            if (stockcountArrayList == null)
                return 0;
            else
                return stockcountArrayList.size();
        }
        else
            return  stockcountArrayListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    stockcountByItemListener = (StockcountByItemListener) stockcountArrayList;
                } else {
                    ArrayList<MasterStockCount> filteredList = new ArrayList<>();
                    for (MasterStockCount row : stockcountArrayList) {
                        if (row.getKodeStockCount().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getTanggal().toLowerCase().contains(charString.toLowerCase()) ||
                                (row.getUser() != null && row.getUser().toLowerCase().contains(charString.toLowerCase()))) {
                            filteredList.add(row);
                        }
                    }
                    stockcountArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = stockcountArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                stockcountArrayListFiltered = (ArrayList<MasterStockCount>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView kodeStockCount, tglStockcount, user, lastUpdate;
        Button btnDetailByItem, btnDetailByLabel;

        public ViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cardview_stockcount);
            kodeStockCount = itemView.findViewById(R.id.kode_stockcount);
            tglStockcount = itemView.findViewById(R.id.tanggal_stockcount);
            user = itemView.findViewById(R.id.nama_user);
            lastUpdate = itemView.findViewById(R.id.last_update_stockcount);
            btnDetailByItem = itemView.findViewById(R.id.buttonDetailByItem);
            btnDetailByLabel = itemView.findViewById(R.id.buttonDetailByLabel);
        }
    }

    public interface StockcountByItemListener {
        void onStockCountDetailByItemClick(MasterStockCount stockcount, int position);
    }

    public interface StockcountByLabelListener {
        void onStockCountDetailByLabelClick(MasterStockCount stockcount, int position);
    }
}
