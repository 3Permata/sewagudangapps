package com.tigapermata.sewagudangapps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.model.Gudang;

import java.util.List;

public class GudangAdapter2 extends ArrayAdapter<Gudang> {

    private Context context;

    private List<Gudang> listGudang;

    public GudangAdapter2(Context con, int textViewResourceId, List<Gudang> listGdg) {
        super(con, textViewResourceId, listGdg);
        context = con;
        listGudang = listGdg;
    }

    @Override
    public int getCount() {
        return listGudang.size();
    }

    @Override
    public Gudang getItem(int position) {
        return listGudang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(listGudang.get(position).getNamaGudang());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(listGudang.get(position).getNamaGudang());

        return label;
    }
}
