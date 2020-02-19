package com.tigapermata.sewagudangapps.adapter.inbound;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.AutocompleteItem;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class AutocompleteItemAdapter extends ArrayAdapter<AutocompleteItem> {

    private Context mContext;
    private ArrayList<AutocompleteItem> mAuto;
    private ArrayList<AutocompleteItem> mAutoAll;
    private int mLayoutResourceId;

    public AutocompleteItemAdapter(Context context, int resource, ArrayList<AutocompleteItem> autocompleteItems) {
        super(context, resource, autocompleteItems);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mAuto = new ArrayList<>(autocompleteItems);
        this.mAutoAll = new ArrayList<>(autocompleteItems);
    }

    @Override
    public int getCount() {
        return mAuto.size();
    }

    public AutocompleteItem getItem(int position) {
        return mAuto.get(position);
    }

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

            AutocompleteItem autocompleteItem = getItem(position);
            CustomTextView name = (CustomTextView) convertView.findViewById(R.id.autocomplete);
            String namaItem = autocompleteItem.getNamaItem();
            String kodeItem = autocompleteItem.getKodeItem();
            name.setText(kodeItem + " - " + namaItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((AutocompleteItem) resultValue).getKodeItem() + " - " + ((AutocompleteItem) resultValue).getNamaItem() ;
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                ArrayList<AutocompleteItem> autoSuggestions = new ArrayList<>();
                if (constraint != null) {
                    for (AutocompleteItem autoComplete : mAutoAll) {
                        if (autoComplete.getNamaItem().toLowerCase().startsWith(constraint.toString().toLowerCase()) ||
                                autoComplete.getKodeItem().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            autoSuggestions.add(autoComplete);
                        }
                    }

                    filterResults.values = autoSuggestions;
                    filterResults.count = autoSuggestions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mAuto.clear();
                if (results != null && results.count > 0) {
                    for (Object object : (ArrayList<?>) results.values) {
                        if (object instanceof AutocompleteItem) {
                            mAuto.add((AutocompleteItem) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    mAuto.addAll(mAutoAll);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
