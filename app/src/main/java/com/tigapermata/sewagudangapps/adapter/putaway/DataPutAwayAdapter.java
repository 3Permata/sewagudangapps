package com.tigapermata.sewagudangapps.adapter.putaway;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.putaway.DataPutAway;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class DataPutAwayAdapter extends RecyclerView.Adapter<DataPutAwayAdapter.ViewHolder> {

    private ArrayList<DataPutAway> dataPutAwayArrayList;
    Context context;
    ViewGroup mParent;


    public DataPutAwayAdapter(ArrayList<DataPutAway> list, Context context) {
        this.dataPutAwayArrayList = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        mParent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_put_away, parent, false);
        DataPutAwayAdapter.ViewHolder dpv = new DataPutAwayAdapter.ViewHolder(v);
        return dpv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvOldLocator.setText(dataPutAwayArrayList.get(position).getOldLocator());
        holder.tvNewLocator.setText(dataPutAwayArrayList.get(position).getNewLocator());
        holder.tvFilter.setText(dataPutAwayArrayList.get(position).getFilter());
        holder.tvItem.setText(dataPutAwayArrayList.get(position).getItem());
    }

    @Override
    public int getItemCount() {
        return dataPutAwayArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CustomTextView tvNewLocator, tvOldLocator, tvFilter, tvItem;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNewLocator = itemView.findViewById(R.id.pa_new_locator);
            tvOldLocator = itemView.findViewById(R.id.pa_old_locator);
            tvFilter = itemView.findViewById(R.id.pa_filter);
            tvItem = itemView.findViewById(R.id.pa_item);

        }
    }
}
