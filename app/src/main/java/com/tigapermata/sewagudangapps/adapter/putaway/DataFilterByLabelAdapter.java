package com.tigapermata.sewagudangapps.adapter.putaway;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.putaway.DataFilterByItem;
import com.tigapermata.sewagudangapps.model.putaway.DataFilterByLabel;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class DataFilterByLabelAdapter extends ArrayAdapter<DataFilterByLabel> {

    private Context context;
    private ArrayList<DataFilterByLabel> mData;
    private ArrayList<DataFilterByLabel> mDataAll;
    private int mLayoutResourceId;

    public DataFilterByLabelAdapter(Context context, int resource, ArrayList<DataFilterByLabel> dataFilterByLabels) {
        super(context, resource, dataFilterByLabels);
        this.context = context;
        this.mLayoutResourceId = resource;
        this.mData = new ArrayList<>(dataFilterByLabels);
        this.mDataAll = new ArrayList<>(dataFilterByLabels);
    }

    public int findItem(String s) {
        int index = -1;
        for (int i = 0; i < mDataAll.size(); i++) {
            if (mDataAll.get(i).getLabel().equals(s)) { index = i; break; }
        }
        return index;
    }

    public DataFilterByLabel getItemFromAll(int position) { return mDataAll.get(position); }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public DataFilterByLabel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }

            DataFilterByLabel dataLabel = getItem(position);
            CustomTextView labelName = convertView.findViewById(R.id.autocomplete);
            String s = dataLabel.getLabel();
            labelName.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((DataFilterByLabel) resultValue).getLabel();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<DataFilterByLabel> autoSuggestions = new ArrayList<>();
                if (constraint != null) {
                    for (DataFilterByLabel dataLabel : mData) {
                        if (dataLabel.getLabel().toLowerCase().startsWith(constraint.toString())) {
                            autoSuggestions.add(dataLabel);
                        }
                    }

                    filterResults.values = autoSuggestions;
                    filterResults.count = autoSuggestions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mData.clear();
                if (results != null && results.count > 0) {
                    for (Object object : (ArrayList<?>) results.values) {
                        if (object instanceof DataFilterByLabel) {
                            mData.add((DataFilterByLabel) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    mData.addAll(mDataAll);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
