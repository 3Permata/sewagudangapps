package com.tigapermata.sewagudangapps.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.model.Gudang;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.List;

public class GudangAdapter extends RecyclerView.Adapter<GudangAdapter.ViewHolder> {

    ViewGroup mParent;
    private List<Gudang> gudangs;
    private GudangListener gudangListener;
    private Context mContext;

    public GudangAdapter(SearchWarehouseActivity context, List<Gudang> gudangList) {
        this.mContext = context;
        this.gudangs = gudangList;
        this.gudangListener = (GudangListener) context;
    }

    @Override
    public GudangAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_gudang, parent, false);
        GudangAdapter.ViewHolder gvh = new GudangAdapter.ViewHolder(v);

        return gvh;
    }

    @Override
    public void onBindViewHolder(GudangAdapter.ViewHolder holder,final int position) {
        holder.namaGudang.setText(gudangs.get(position).getNamaGudang());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaGudang = gudangs.get(position).getNamaGudang();
                Log.e("namaGudang", namaGudang);
                gudangListener.onGudangClick(gudangs.get(position), position);

//                Intent intent = new Intent(view.getContext(), PilihProjectFragment.class);
//                intent.putExtras(bundle);
//                startActivity(view.getContext(), intent, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gudangs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cv;
        CustomTextView namaGudang;

        public ViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.linearlayout);
            namaGudang = itemView.findViewById(R.id.warehouseTitle);
        }
    }

    public interface GudangListener {
        void onGudangClick(Gudang gudang, int position);
    }
}
