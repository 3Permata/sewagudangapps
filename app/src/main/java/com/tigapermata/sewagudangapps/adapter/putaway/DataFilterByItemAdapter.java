package com.tigapermata.sewagudangapps.adapter.putaway;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.putaway.DataFilterByItem;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class DataFilterByItemAdapter extends ArrayAdapter<DataFilterByItem> {

    private Context mContext;
    private ArrayList<DataFilterByItem> mData;
    private ArrayList<DataFilterByItem> mDataAll;
    private int mLayoutResourceId;

    public DataFilterByItemAdapter(Context context, int resource, ArrayList<DataFilterByItem> dataFilterByItems) {
        super(context, resource, dataFilterByItems);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mData = new ArrayList<>(dataFilterByItems);
        this.mDataAll = new ArrayList<>(dataFilterByItems);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public DataFilterByItem getItem(int position) {
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
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }

            DataFilterByItem dataItem = getItem(position);
            CustomTextView itemName = convertView.findViewById(R.id.autocomplete);
            String nama = dataItem.getNamaItem();
            //if (nama.length() > 12) nama = nama.substring(0, 12);
            String s = dataItem.getKodeItem() + " - " + nama;
            itemName.setText(s);
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
                return ((DataFilterByItem) resultValue).getNamaItem();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<DataFilterByItem> autoSuggestions = new ArrayList<>();
                if (constraint != null) {
                    for (DataFilterByItem dataItem : mData) {
                        if (dataItem.getNamaItem().toLowerCase().contains(constraint.toString())) {
                            autoSuggestions.add(dataItem);
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
                        if (object instanceof DataFilterByItem) {
                            mData.add((DataFilterByItem) object);
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
