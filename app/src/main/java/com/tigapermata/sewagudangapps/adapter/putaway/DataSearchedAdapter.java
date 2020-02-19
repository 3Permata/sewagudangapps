package com.tigapermata.sewagudangapps.adapter.putaway;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.putaway.DataSearched;

import java.util.ArrayList;

public class DataSearchedAdapter extends RecyclerView.Adapter<DataSearchedAdapter.ViewHolder> {
    Context context;
    ArrayList<DataSearched> dataItemArrayList;
    ItemListener listener;

    public DataSearchedAdapter(Context mContext, ArrayList<DataSearched> dataItem, ItemListener itemListener) {
        this.context = mContext;
        this.dataItemArrayList = dataItem;
        this.listener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_item_searched, parent, false);
        return new DataSearchedAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DataSearched ds = dataItemArrayList.get(position);

        holder.tLabel.setText(ds.getLabel());
        holder.tName.setText(ds.getNamaItem());
        holder.tQty.setText(ds.getQty());
        holder.tSatuan.setText(ds.getSatuan());
        holder.tLocator.setText(ds.getNamaLocator());

        holder.btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(dataItemArrayList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tLabel, tName, tQty, tSatuan, tLocator;
        Button btnMove;

        public ViewHolder(View itemView) {
            super(itemView);

            tLabel = itemView.findViewById(R.id.textViewSearchedLabel);
            tName = itemView.findViewById(R.id.textViewSearchedName);
            tQty = itemView.findViewById(R.id.textViewSearchedQty);
            tSatuan = itemView.findViewById(R.id.textViewSearchedSatuan);
            tLocator = itemView.findViewById(R.id.textViewSearchedLocator);

            btnMove = itemView.findViewById(R.id.buttonPutawayMove);
        }
    }

    public interface ItemListener {
        void onItemClick(DataSearched item, int position);

    }
}
