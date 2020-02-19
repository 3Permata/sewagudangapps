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
import com.tigapermata.sewagudangapps.model.Project;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    ViewGroup mParent;
    private List<Project> projects;
    private ProjectListener projectListener;
    private Context mContext;

    public  ProjectAdapter(SearchWarehouseActivity context, List<Project> projectList) {
        this.mContext = context;
        this.projects = projectList;
        this.projectListener = (ProjectListener) context;
    }

    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_gudang, parent, false);
        ProjectAdapter.ViewHolder gvh = new ProjectAdapter.ViewHolder(v);

        return gvh;
    }

    @Override
    public void onBindViewHolder(ProjectAdapter.ViewHolder holder,final int position) {
        holder.namaProject.setText(projects.get(position).getNamaProject());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaProject = projects.get(position).getNamaProject();
                Log.e("namaProject", namaProject);
                projectListener.onProjectClick(projects.get(position), position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cv;
        CustomTextView namaProject;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.linearlayout);
            namaProject = itemView.findViewById(R.id.warehouseTitle);
        }
    }

    public interface ProjectListener {
        void onProjectClick(Project project, int position);
    }
}
