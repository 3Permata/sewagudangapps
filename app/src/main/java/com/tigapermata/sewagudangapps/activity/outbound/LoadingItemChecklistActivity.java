package com.tigapermata.sewagudangapps.activity.outbound;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.LoginActivity;
import com.tigapermata.sewagudangapps.activity.ScanActivity;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.adapter.outbound.ItemOutgoingAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.outbound.ItemOutgoing;
import com.tigapermata.sewagudangapps.model.outbound.ItemOutgoingChecked;
import com.tigapermata.sewagudangapps.model.outbound.ItemOutgoingDetail;
import com.tigapermata.sewagudangapps.model.outbound.LoadingChecklistResponse;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class LoadingItemChecklistActivity extends AppCompatActivity implements ItemOutgoingAdapter.ItemOutgoingListener{
    Toolbar toolbar;

    String idOutbound, idOutgoing, idProject, idGudang;
    TextView tvTotalQty, namaProject, outboundNumber, tItemNull;
    EditText etSearchLabel;
    Button btnRefresh;
    ImageButton ibBarcodeScan;
    ProgressBar pbLoading;
    View vSemiTransparent;
    Call<ItemOutgoingChecked> itemCall;
    Boolean onSendData = false, processDone = false, onScan = false, onShowDialog = false;

    private ArrayList<ItemOutgoing> itemOutgoingArrayList;
    private RecyclerView rvItemOutgoing;
    private ItemOutgoingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_item_checklist_bylabel);

        idOutbound = AppController.getInstance().getIdOutbound();
        idOutgoing = AppController.getInstance().getIdOutgoing();

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        idGudang = dbHelper.getIds().getIdGudang();
        idProject = dbHelper.getIds().getIdProject();

        toolbar = findViewById(R.id.toolbar_item_check_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Loading - List Item");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTotalQty = findViewById(R.id.textTotalQty);
        namaProject = findViewById(R.id.namaProject_item_checklist);
        outboundNumber = findViewById(R.id.no_outbound_item_checklist);
        tItemNull = findViewById(R.id.textNoItem);
        etSearchLabel = findViewById(R.id.editTextSearchLabel);
        etSearchLabel.requestFocus();
        ibBarcodeScan = findViewById(R.id.imageButtonBarcodeScan);
        btnRefresh = findViewById(R.id.buttonRefresh);
        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);

        rvItemOutgoing = findViewById(R.id.rv_loading_item_checklist);

        namaProject.setText(AppController.getInstance().getNamaProject());
        outboundNumber.setText(AppController.getInstance().getNoOutbound() + " / " + AppController.getInstance().getReferensi());

        preparedDataChecked();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRefresh();
                preparedDataChecked();
            }
        });

        ibBarcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScan = true;
                AppController.getInstance().setTipeScan("Scan Label");
                IntentIntegrator integrator = new IntentIntegrator(LoadingItemChecklistActivity.this).setCaptureActivity(ScanActivity.class);
                integrator.initiateScan();
            }
        });

        etSearchLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAdapter != null)
                    mAdapter.getFilter().filter(s);
            }
        });

        etSearchLabel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    outgoingChecklistByLabel(etSearchLabel.getText().toString());
                    etSearchLabel.setText("");
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        preparedDataChecked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            View menuItemView = findViewById(R.id.action_home);
            showPopUpMenu(menuItemView);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                onScan = false;
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String label = result.getContents();
                outgoingChecklistByLabel(label);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!onSendData) {
            setResult(10);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        itemCall.cancel();
    }

    @Override
    public void onItemOutgoingClick(ItemOutgoing outgoing, int position) {
        if (idGudang.matches("15") && idProject.matches("140"))
            sendData(outgoing.getIdOutboundDetail(), outgoing.getIdInventoryDetail(), "1");
        else
            alertOutgoingChecklist(outgoing.getIdOutboundDetail(), outgoing.getIdInventoryDetail(), outgoing.getQty(), outgoing.getQtyLoad());
    }

    private void preparedDataChecked() {
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        itemCall = service.dataItemOutgoingChecked(idGudang, idProject, idOutbound, idOutgoing, idUser, token);
        itemCall.enqueue(new Callback<ItemOutgoingChecked>() {
            @Override
            public void onResponse(Call<ItemOutgoingChecked> call, Response<ItemOutgoingChecked> response) {
                endLoading();
                if (response.isSuccessful()) {
                    if (response.body().getItemOutgoingArrayList() == null) {
                        showRefresh();
                    }
                    else if (response.body().getItemOutgoingArrayList().size() == 0) {
                        showRefresh();
                    }
                    else {
                        String totalPicking = response.body().getTotalPicking();
                        String totalLoading = response.body().getTotalLoading();
                        if (totalPicking == null) totalPicking = "0";
                        if (totalLoading == null) totalLoading = "0";

                        tvTotalQty.setText(totalLoading + " / " + totalPicking);

                        itemOutgoingArrayList = response.body().getItemOutgoingArrayList();

                        rvItemOutgoing.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvItemOutgoing.setLayoutManager(mLayoutManager);
                        mAdapter = new ItemOutgoingAdapter(getApplicationContext(), itemOutgoingArrayList, LoadingItemChecklistActivity.this);
                        rvItemOutgoing.setAdapter(mAdapter);

                        if (response.body().getTotalLoading().equals(response.body().getTotalPicking())) {
                            setResult(11);
                            processDone = true; onScan = false;
                            AlertSuccess("Loading sudah selesai");
                        }

                        if (onScan && !onShowDialog) {
                            AppController.getInstance().setTipeScan("Scan Label");
                            IntentIntegrator integrator = new IntentIntegrator(LoadingItemChecklistActivity.this).setCaptureActivity(ScanActivity.class);
                            integrator.initiateScan();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                    showRefresh();
                }
            }

            @Override
            public void onFailure(Call<ItemOutgoingChecked> call, Throwable t) {
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                endLoading(); showRefresh();
            }
        });
    }

    private void alertOutgoingChecklist(final String idOutboundDetail, final String idInventoryDetail, String qty, String qtyLoad) {
        onShowDialog = true;
        final int qtyA, qtyL;
        qtyA = Integer.parseInt(qty);
        if (qtyLoad == null) qtyL = 0;
        else qtyL = Integer.parseInt(qtyLoad);
        final int qtyResult = qtyA - qtyL;

        AlertDialog.Builder builder = new AlertDialog.Builder(LoadingItemChecklistActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View itemView = inflater.inflate(R.layout.dialog_outgoing_checklist, null);
        builder.setView(itemView);

        TextView tQtySisa = itemView.findViewById(R.id.qty);
        final EditText etQtyAktual = itemView.findViewById(R.id.qty_aktual);

        tQtySisa.setText(qtyResult + "");
        etQtyAktual.setText(qtyResult + "");
        etQtyAktual.setSelection(etQtyAktual.getText().length());

        builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onShowDialog = false;
                final String qtyAkt = etQtyAktual.getText().toString();
                final int qty_akt = Integer.parseInt(qtyAkt);
                if (qty_akt > qtyResult || qty_akt < 0) {
                    AlertSuccess("Data cannot be processed");
                } else {
                    sendData(idOutboundDetail, idInventoryDetail, qtyAkt);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onShowDialog = false;
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void sendData(String idOutboundDetail, String idInventoryDetail, String qty) {
        onLoading(); onSendData = true;
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<ItemOutgoingDetail> outgoingDetailCall = service.outgoingChecklist(idGudang, idProject, idOutbound, idOutgoing, idUser, token, idOutboundDetail, idInventoryDetail, qty);
        outgoingDetailCall.enqueue(new Callback<ItemOutgoingDetail>() {
            @Override
            public void onResponse(Call<ItemOutgoingDetail> call, Response<ItemOutgoingDetail> response) {
                endLoading(); onSendData = false;
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();

                    if (status.matches("gagal")) {
                        AlertSuccess("Data sudah di load");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "berhasil diproses", Toast.LENGTH_SHORT).show();
                        preparedDataChecked();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ItemOutgoingDetail> call, Throwable t) {
                endLoading(); onSendData = false;
                AlertSuccess("Please Check your Internet Connection");
            }
        });
    }

    private void searchLabel(String label) {
        ItemOutgoing outgoing = mAdapter.getItem(0);
        if (outgoing == null) AlertSuccess("Label not found");
        else {
            if (outgoing.getLabel().equals(label) || outgoing.getLabelInventory().equals(label)) {
                alertOutgoingChecklist(outgoing.getIdOutboundDetail(), outgoing.getIdInventoryDetail(), outgoing.getQty(), outgoing.getQtyLoad());
            }
            else AlertSuccess("Label not found");
        }
    }

    private void outgoingChecklistByLabel(final String label) {
        onLoading(); onSendData = true;
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        final String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<LoadingChecklistResponse> loadingChecklistResponseCall = service.outgoingChecklistByLabel(idGudang, idProject, idOutbound, idOutgoing, idUser, token, label);
        loadingChecklistResponseCall.enqueue(new Callback<LoadingChecklistResponse>() {
            @Override
            public void onResponse(Call<LoadingChecklistResponse> call, Response<LoadingChecklistResponse> response) {
                endLoading(); onSendData = false;
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();

                    if (status.matches("error")) {
                        onScan = false;
                        AlertSuccess("Data sudah diloading");
                    }
                    else if (status.matches("not found")) {
                        onScan = false;
                        AlertSuccess(label + "\nLabel salah");
                    }
                    else {
                        if (idGudang.matches("15") && idProject.matches("140"))
                            sendData(response.body().getIdOutboundDetail(), response.body().getIdInventoryDetail(), "1");
                        else {
                            onShowDialog = true;
                            alertOutgoingChecklist(response.body().getIdOutboundDetail(), response.body().getIdInventoryDetail(), response.body().getQty(), response.body().getQtyLoad());
                        }
                    }
                }
                else {
                    onScan = false;
                    //Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                    AlertSuccess(label + "\nLabel salah");
                }
            }

            @Override
            public void onFailure(Call<LoadingChecklistResponse> call, Throwable t) {
                endLoading(); onSendData = false;
                onScan = false;
                AlertSuccess("Please Check your Internet Connection");
            }
        });
    }

    private void onLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);
        etSearchLabel.setEnabled(false);
        ibBarcodeScan.setEnabled(false);
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);
        etSearchLabel.setEnabled(true);
        etSearchLabel.requestFocus();
        ibBarcodeScan.setEnabled(true);
    }

    private void showRefresh() {
        tItemNull.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        if (itemOutgoingArrayList != null)
            if (itemOutgoingArrayList.size() > 0) {
                int size = itemOutgoingArrayList.size();
                itemOutgoingArrayList.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
            }
    }

    private void hideRefresh() {
        tItemNull.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoadingItemChecklistActivity.this);
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
                if (processDone) finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logout() {
        DBHelper db = new DBHelper(this);
        db.clearDatabase();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void showPopUpMenu(View view) {
        final PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_warehouse, popup.getMenu());

        DBHelper dbHelper = new DBHelper(this);
        popup.getMenu().getItem(0).setTitle("User : " + dbHelper.getTokenn().getUsername());
        SpannableString s = new SpannableString(popup.getMenu().getItem(0).getTitle());
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.maroon)), 0, s.length(), 0);
        s.setSpan(new StyleSpan(BOLD),0, s.length(), 0);
        popup.getMenu().getItem(0).setTitle(s);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_warehouse:
                        startActivity(new Intent(LoadingItemChecklistActivity.this, SearchWarehouseActivity.class));
                        finish();
                        return true;
                    case R.id.action_logout:
                        logout();
                        finish();
                        return true;
                    default:
                }
                popup.dismiss();
                return false;
            }
        });
        popup.show();
    }
}
