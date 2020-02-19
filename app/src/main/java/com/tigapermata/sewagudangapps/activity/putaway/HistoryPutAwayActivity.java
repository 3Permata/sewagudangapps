package com.tigapermata.sewagudangapps.activity.putaway;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.adapter.putaway.DataHistoryAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.putaway.DataHistory;
import com.tigapermata.sewagudangapps.model.putaway.DataHistoryList;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryPutAwayActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tProjectName, tNoHistory;
    Button btnRefresh;
    ProgressBar pbLoading;
    View vSemiTransparent;
    Call<DataHistoryList> historyCall;

    private ArrayList<DataHistory> dataHistoryArrayList;
    private RecyclerView rvHistoryPutaway;
    private DataHistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_putaway);

        toolbar = findViewById(R.id.toolbar_putaway);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Report Put Away");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tProjectName = findViewById(R.id.textProjectName);
        tNoHistory = findViewById(R.id.textNoHistory);
        btnRefresh = findViewById(R.id.buttonRefresh);
        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);
        rvHistoryPutaway = findViewById(R.id.rv_history_putaway);

        tProjectName.setText(AppController.getInstance().getNamaProject());

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRefresh();
                preparedDataHistory();
            }
        });

        preparedDataHistory();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        historyCall.cancel();
    }

    private void preparedDataHistory() {
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        historyCall = service.putawayHistory(idGudang, idProject, idUser, token);
        historyCall.enqueue(new Callback<DataHistoryList>() {
            @Override
            public void onResponse(Call<DataHistoryList> call, Response<DataHistoryList> response) {
                endLoading();
                if (response.isSuccessful()) {
                    if (response.body().getDataHistories() == null) {
                        showRefresh();
                    }
                    else if (response.body().getDataHistories().size() == 0) {
                        showRefresh();
                    }
                    else {
                        dataHistoryArrayList = response.body().getDataHistories();

                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvHistoryPutaway.setLayoutManager(mLayoutManager);
                        mAdapter = new DataHistoryAdapter(getApplicationContext(), dataHistoryArrayList);
                        rvHistoryPutaway.setAdapter(mAdapter);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                    showRefresh();
                }
            }

            @Override
            public void onFailure(Call<DataHistoryList> call, Throwable t) {
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                endLoading(); showRefresh();
            }
        });
    }

    private void onLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);
    }

    private void showRefresh() {
        tNoHistory.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        /*if (dataHistoryArrayList != null)
            if (dataHistoryArrayList.size() > 0) {
                int size = dataHistoryArrayList.size();
                dataHistoryArrayList.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
            }*/
    }

    private void hideRefresh() {
        tNoHistory.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryPutAwayActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_not_found_data, null);
        CustomTextView textDialog;
        textDialog = view.findViewById(R.id.text_dialog);
        textDialog.setText(message);
        textDialog.setTextSize(18);

        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
