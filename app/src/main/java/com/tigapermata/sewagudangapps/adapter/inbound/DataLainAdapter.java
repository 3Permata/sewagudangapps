package com.tigapermata.sewagudangapps.adapter.inbound;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.DataLain;

import java.util.ArrayList;

public class DataLainAdapter extends RecyclerView.Adapter<DataLainAdapter.ViewHolder> {

    ViewGroup mParent;
    Context mContext;
    private ArrayList<DataLain> dataLainArrayList;

    public DataLainAdapter(Context context, ArrayList<DataLain> dataLains) {
        this.mContext = context;
        this.dataLainArrayList = dataLains;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_data_lain, parent, false);
        DataLainAdapter.ViewHolder dvh = new DataLainAdapter.ViewHolder(v);

        return dvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvLabel.setText(dataLainArrayList.get(position).getLabel());
        holder.tvIsi.setText(dataLainArrayList.get(position).getIsi());
    }

    @Override
    public int getItemCount() {
        return dataLainArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvLabel, tvIsi;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.label_data_lain);
            tvIsi = itemView.findViewById(R.id.isi_data_lain);
        }
    }
}
