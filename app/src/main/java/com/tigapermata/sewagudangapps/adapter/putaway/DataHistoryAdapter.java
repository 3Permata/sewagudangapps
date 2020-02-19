package com.tigapermata.sewagudangapps.adapter.putaway;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.putaway.DataHistory;

import java.util.ArrayList;

public class DataHistoryAdapter extends RecyclerView.Adapter<DataHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<DataHistory> dataItemArrayList;
    ItemListener listener;

    public DataHistoryAdapter(Context mContext, ArrayList<DataHistory> dataItem) {
        this.context = mContext;
        this.dataItemArrayList = dataItem;
        //this.listener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_history_putaway, parent, false);
        return new DataHistoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DataHistory dh = dataItemArrayList.get(position);

        holder.tTanggal.setText(dh.getTanggal());
        holder.tLabel.setText(dh.getLabel());
        holder.tName.setText(dh.getItem());
        holder.tQty.setText(dh.getQty());
        holder.tSatuan.setText(dh.getSatuan());
        holder.tLocatorAwal.setText(dh.getLocatorAwal());
        holder.tLocatorTujuan.setText(dh.getLocatorTujuan());
    }

    @Override
    public int getItemCount() {
        return dataItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tTanggal, tLabel, tName, tQty, tSatuan, tLocatorAwal, tLocatorTujuan;

        public ViewHolder(View itemView) {
            super(itemView);

            tTanggal = itemView.findViewById(R.id.textViewHistoryTanggal);
            tLabel = itemView.findViewById(R.id.textViewHistoryLabel);
            tName = itemView.findViewById(R.id.textViewHistoryName);
            tQty = itemView.findViewById(R.id.textViewHistoryQty);
            tSatuan = itemView.findViewById(R.id.textViewHistorySatuan);
            tLocatorAwal = itemView.findViewById(R.id.textViewHistoryLocatorAwal);
            tLocatorTujuan = itemView.findViewById(R.id.textViewHistoryLocatorTujuan);
        }
    }

    public interface ItemListener {
        void onItemClick(DataHistory item, int position);

    }
}
