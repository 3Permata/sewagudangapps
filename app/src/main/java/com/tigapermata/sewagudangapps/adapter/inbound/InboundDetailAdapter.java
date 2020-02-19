package com.tigapermata.sewagudangapps.adapter.inbound;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.InboundDetail;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class InboundDetailAdapter extends RecyclerView.Adapter<InboundDetailAdapter.ViewHolder> {

    ViewGroup mParent;
    private Context mContext;
    private ArrayList<InboundDetail> inboundDetails;

    public InboundDetailAdapter(Context mContext, ArrayList<InboundDetail> inboundDetails) {
        this.mContext = mContext;
        this.inboundDetails = inboundDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mParent = viewGroup;

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_list_inbound_detail, viewGroup, false);
        ViewHolder ivh = new InboundDetailAdapter.ViewHolder(v);

        return ivh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.labels.setText(inboundDetails.get(position).getLabel());
        viewHolder.namaItem.setText(inboundDetails.get(position).getNamaItem());
        viewHolder.qty.setText(inboundDetails.get(position).getQtyAktual() + " / " + inboundDetails.get(position).getQty());
        viewHolder.status.setText(inboundDetails.get(position).getStatus());
        if (inboundDetails.get(position).getStatus().matches("complete"))
            viewHolder.status.setBackground(mContext.getDrawable(R.drawable.oval_rectangle_green));
        else viewHolder.status.setBackground(mContext.getDrawable(R.drawable.oval_rectangle_blue));
    }

    @Override
    public int getItemCount() {
        return inboundDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView namaItem, labels, qty, status;

        public ViewHolder(View itemView) {
            super(itemView);

            namaItem = itemView.findViewById(R.id.nama_item);
            labels = itemView.findViewById(R.id.label);
            qty = itemView.findViewById(R.id.textQty);
            status = itemView.findViewById(R.id.textStatus);
        }
    }
}
