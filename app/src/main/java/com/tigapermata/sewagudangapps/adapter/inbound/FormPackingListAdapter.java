package com.tigapermata.sewagudangapps.adapter.inbound;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.EditPacking;
import com.tigapermata.sewagudangapps.model.inbound.FormPacking;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class FormPackingListAdapter extends RecyclerView.Adapter<FormPackingListAdapter.ViewHolder> {

    ViewGroup mParent;
    Context mContext;
    public static ArrayList<FormPacking> formPackingArrayList;
    public static ArrayList<EditPacking> editPackingArrayList;

    public FormPackingListAdapter(Context context, ArrayList<FormPacking> formPackings, ArrayList<EditPacking> editPackings) {
        this.mContext = context;
        this.formPackingArrayList = formPackings;
        this.editPackingArrayList = editPackings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_form_packing_list, parent, false);
        FormPackingListAdapter.ViewHolder fpv = new FormPackingListAdapter.ViewHolder(v);
        return fpv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.label.setText(formPackingArrayList.get(position).getLabel());
    }

    @Override
    public int getItemCount() {
        if (formPackingArrayList == null) {
            return editPackingArrayList.size();
        } else {
            return formPackingArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView label;
        CustomEditText body;

        public ViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.packing_label);
            body = itemView.findViewById(R.id.packing_body);

            body.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    editPackingArrayList.get(getAdapterPosition()).setEditTextValue(body.getText().toString());
                }
            });
        }
    }
}
