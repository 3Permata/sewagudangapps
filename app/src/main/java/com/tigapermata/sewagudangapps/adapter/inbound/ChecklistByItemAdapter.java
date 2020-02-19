package com.tigapermata.sewagudangapps.adapter.inbound;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.DataLabelItem;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class ChecklistByItemAdapter extends RecyclerView.Adapter<ChecklistByItemAdapter.ViewHolder> {

    Context context;
    ArrayList<DataLabelItem> dataLabelItemArrayList;
    ItemListener listener;

    public ChecklistByItemAdapter(Context mContext, ArrayList<DataLabelItem> dataLabelItems, ItemListener itemListener) {
        this.context = mContext;
        this.dataLabelItemArrayList = dataLabelItems;
        this.listener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_checklist_by_item, parent, false);
        return new ChecklistByItemAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tNo.setText((position + 1) + "");
        holder.tLabel.setText(dataLabelItemArrayList.get(position).getLabel());

        //listener.onItemClick(dataLabelItemArrayList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataLabelItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tLabel, tNo;

        public ViewHolder(View itemView) {
            super(itemView);

            tLabel = itemView.findViewById(R.id.textViewLabelScanned);
            tNo = itemView.findViewById(R.id.textViewNoLabel);
        }
    }

    public interface ItemListener {
        void onItemClick(DataLabelItem item, int position);

    }
}
