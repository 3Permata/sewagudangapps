package com.tigapermata.sewagudangapps.adapter.outbound;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.outbound.DataLabelItemPicked;

import java.util.ArrayList;

public class PickingChecklistAdapter extends RecyclerView.Adapter<PickingChecklistAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DataLabelItemPicked> dataLabelItemPickedArrayList;
    private PickingChecklistListener listener;

    public PickingChecklistAdapter(Context context, ArrayList<DataLabelItemPicked> dataLabelItemPickedArrayList, PickingChecklistListener listener) {
        this.context = context;
        this.dataLabelItemPickedArrayList = dataLabelItemPickedArrayList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_picking_checklist, parent, false);
        return new PickingChecklistAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tLabel.setText(dataLabelItemPickedArrayList.get(position).getLabel());
        holder.tLabelInventory.setText(dataLabelItemPickedArrayList.get(position).getLabelInventory());
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemPickingChecklistClicked(dataLabelItemPickedArrayList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataLabelItemPickedArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tLabel, tLabelInventory;
        Button btnCancel;

        public ViewHolder(View itemView) {
            super(itemView);

            tLabel = itemView.findViewById(R.id.textViewLabel);
            tLabelInventory = itemView.findViewById(R.id.textViewLabelInventory);
            btnCancel = itemView.findViewById(R.id.buttonCancelPicking);
        }
    }

    public interface PickingChecklistListener {
        void onItemPickingChecklistClicked(DataLabelItemPicked itemPicked, int position);
    }
}
