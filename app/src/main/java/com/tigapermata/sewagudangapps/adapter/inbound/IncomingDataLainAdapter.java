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

public class IncomingDataLainAdapter extends RecyclerView.Adapter<IncomingDataLainAdapter.ViewHolder> {

    private Context context;
    private static ArrayList<DataLain> dataLainArrayList;

    public IncomingDataLainAdapter(ArrayList<DataLain> dataLains) {
        this.dataLainArrayList = dataLains;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.incoming_inner_rv, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvLabel.setText(dataLainArrayList.get(position).getLabel());
        holder.tvIsi.setText(dataLainArrayList.get(position).getIsi());
    }

    @Override
    public int getItemCount() {
        if (dataLainArrayList == null) {
            return 0;
        } else {
            return dataLainArrayList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel, tvIsi;

        public ViewHolder(View itemView) {
            super(itemView);

            tvLabel = itemView.findViewById(R.id.tv_label_inner_incoming);
            tvIsi = itemView.findViewById(R.id.tv_isi_inner_incoming);
        }
    }
}
