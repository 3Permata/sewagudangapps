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
import com.tigapermata.sewagudangapps.model.putaway.DataLocator;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class DataLocatorAdapter extends ArrayAdapter<DataLocator> {
    private Context mContext;
    private ArrayList<DataLocator> mData;
    private ArrayList<DataLocator> mDataAll;
    private int mLayoutResourceId;

    public DataLocatorAdapter(Context context, int resource, ArrayList<DataLocator> dataLocators) {
        super(context, resource, dataLocators);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mData = new ArrayList<>(dataLocators);
        this.mDataAll = new ArrayList<>(dataLocators);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public DataLocator getItem(int position) {
        return mData.get(position);
    }

    public DataLocator getItemFromAll(int position) {
        return mDataAll.get(position);
    }

    public int findItem(String s) {
        int index = -1;
        for (int i = 0; i < mDataAll.size(); i++) {
            if (mDataAll.get(i).getNamaLocator().equals(s)) { index = i; break; }
        }
        return index;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }

            DataLocator dataLocator = getItem(position);
            CustomTextView locatorName = convertView.findViewById(R.id.autocomplete);
            String namaLocator = dataLocator.getNamaLocator();
            locatorName.setText(namaLocator);
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
                return ((DataLocator) resultValue).getNamaLocator();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<DataLocator> autoSuggestions = new ArrayList<>();
                if (constraint != null) {
                    for (DataLocator data : mData) {
                        if (data.getNamaLocator().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            autoSuggestions.add(data);
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
                        if (object instanceof DataLocator) {
                            mData.add((DataLocator) object);
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
