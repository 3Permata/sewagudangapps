package com.tigapermata.sewagudangapps.adapter.outbound;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.model.inbound.EditIncoming;
import com.tigapermata.sewagudangapps.model.outbound.FormOutgoing;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

public class FormOutgoingAdapter extends RecyclerView.Adapter<FormOutgoingAdapter.ViewHolder> {

    ViewGroup mParent;
    Context context;
    public static ArrayList<FormOutgoing> formOutgoingArrayList;
    public static ArrayList<EditIncoming> editOutgoingArrayList;

    public FormOutgoingAdapter(Context context, ArrayList<FormOutgoing> formOutgoing, ArrayList<EditIncoming> editIncomings) {
        this.context = context;
        this.formOutgoingArrayList = formOutgoing;
        this.editOutgoingArrayList = editIncomings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_form_incoming, parent, false);
        FormOutgoingAdapter.ViewHolder ivh = new FormOutgoingAdapter.ViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.label.setText(formOutgoingArrayList.get(position).getLabel());
        holder.body.setHint(formOutgoingArrayList.get(position).getLabel());
        if (position + 1 == formOutgoingArrayList.size()) holder.body.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    @Override
    public int getItemCount() {
        if (formOutgoingArrayList == null) {
            return editOutgoingArrayList.size();
        } else {
            return formOutgoingArrayList.size();
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
                    editOutgoingArrayList.get(getAdapterPosition()).setEditTextValue(body.getText().toString());
                }
            });
        }
    }
}
