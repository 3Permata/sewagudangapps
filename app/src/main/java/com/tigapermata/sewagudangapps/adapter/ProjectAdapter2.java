package com.tigapermata.sewagudangapps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.model.Project;

import java.util.List;

public class ProjectAdapter2 extends ArrayAdapter<Project> {

    private Context context;

    private List<Project> listProject;

    public ProjectAdapter2(Context con, int textViewResourceId, List<Project> listPrj) {
        super(con, textViewResourceId, listPrj);
        context = con;
        listProject = listPrj;
    }

    @Override
    public int getCount() {
        return listProject.size();
    }

    @Override
    public Project getItem(int position) {
        return listProject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(listProject.get(position).getNamaProject());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(listProject.get(position).getNamaProject());

        return label;
    }
}
