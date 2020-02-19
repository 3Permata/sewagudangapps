package com.tigapermata.sewagudangapps.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.adapter.inbound.DataLainAdapter;
import com.tigapermata.sewagudangapps.adapter.inbound.InboundDetailAdapter;
import com.tigapermata.sewagudangapps.model.inbound.DataLain;
import com.tigapermata.sewagudangapps.model.inbound.InboundDetail;

import java.util.ArrayList;

public class InboundDetailFragment extends DialogFragment {

    ArrayList<InboundDetail> listInboundDetail;
    ArrayList<DataLain> listDataLain;

    public static InboundDetailFragment newInstance(ArrayList<InboundDetail> listInboundDetail, ArrayList<DataLain> listDataLain) {
        InboundDetailFragment frag = new InboundDetailFragment();
        frag.init(listInboundDetail, listDataLain);
        return frag;
    }

    private void init(ArrayList<InboundDetail> listInboundDetail, ArrayList<DataLain> listDataLain) {
        this.listInboundDetail = listInboundDetail;
        this.listDataLain = listDataLain;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_detail_inbound, container, false);

        RecyclerView rvDetailInbound = view.findViewById(R.id.detail_inbound);
        RecyclerView rvDataLainInbound = view.findViewById(R.id.data_lain_inbound);

        //data lain
        rvDataLainInbound.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerDataLain = new LinearLayoutManager(getActivity());
        rvDataLainInbound.setLayoutManager(layoutManagerDataLain);
        DataLainAdapter dataLainAdapter = new DataLainAdapter(getActivity(), listDataLain);
        rvDataLainInbound.setAdapter(dataLainAdapter);

        //detail inbound
        rvDetailInbound.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerDetails = new LinearLayoutManager(getContext());
        rvDetailInbound.setLayoutManager(layoutManagerDetails);
        InboundDetailAdapter detailAdapter = new InboundDetailAdapter(getContext(), listInboundDetail);
        rvDetailInbound.setAdapter(detailAdapter);

        Button btnClose = view.findViewById(R.id.buttonClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels * 9 / 10;

        getDialog().getWindow().setLayout(width, height);
    }
}
