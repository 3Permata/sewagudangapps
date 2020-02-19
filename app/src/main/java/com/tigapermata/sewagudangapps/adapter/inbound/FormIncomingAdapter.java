package com.tigapermata.sewagudangapps.adapter.inbound;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.EditIncoming;
import com.tigapermata.sewagudangapps.model.inbound.FormIncoming;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class FormIncomingAdapter extends RecyclerView.Adapter<FormIncomingAdapter.ViewHolder> {

    ViewGroup mParent;
    Context context;
    public static ArrayList<FormIncoming> formIncomingArrayList;
    public static ArrayList<EditIncoming> editIncomingArrayList;

    public FormIncomingAdapter(Context context, ArrayList<FormIncoming> formIncomings, ArrayList<EditIncoming> editIncomings) {
        this.context = context;
        this.formIncomingArrayList = formIncomings;
        this.editIncomingArrayList = editIncomings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_form_incoming, parent, false);
        FormIncomingAdapter.ViewHolder ivh = new FormIncomingAdapter.ViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.label.setText(formIncomingArrayList.get(position).getLabel());
        holder.body.setHint(formIncomingArrayList.get(position).getLabel());
        if (position + 1 == formIncomingArrayList.size()) holder.body.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    @Override
    public int getItemCount() {
        if (formIncomingArrayList == null) {
            return editIncomingArrayList.size();
        } else {
            return formIncomingArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView label;
        EditText body;

        public ViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            body = itemView.findViewById(R.id.body);

            body.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            body.setRawInputType(InputType.TYPE_CLASS_TEXT);
            body.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    editIncomingArrayList.get(getAdapterPosition()).setEditTextValue(body.getText().toString());
                }
            });
        }
    }
}
