package com.tigapermata.sewagudangapps.activity.outbound;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.tigapermata.sewagudangapps.activity.inbound.ItemCheckListActivityByLabel;
import com.tigapermata.sewagudangapps.activity.inbound.ListIncomingActivity;
import com.tigapermata.sewagudangapps.adapter.outbound.PickingAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.outbound.DataPicking;
import com.tigapermata.sewagudangapps.model.outbound.DataPickingList;
import com.tigapermata.sewagudangapps.model.outbound.PickingResponse;
import com.tigapermata.sewagudangapps.model.putaway.StatusPutAway;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.SearchToolbar;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class PickingListActivity extends AppCompatActivity implements PickingAdapter.PickingListener,
PickingAdapter.UndoPickingListener {

    String idOutbound, namaProject, referensi, noOutbound;
    private TextView tvProjectName, tvNoOutbound, tQtyOutbound, tItemNull;

    Toolbar toolbar;
    ProgressBar pbLoading;
    Button btnRefresh;
    ImageButton ibBarcodeScan;
    EditText etSearchLabel;
    View vSemiTransparent;
    boolean onSendData = false, onScan = false;

    private RecyclerView rvPicking;
    private PickingAdapter pickAdapter;
    private ArrayList<DataPicking> dataPickingArrayList;
    private RecyclerView.LayoutManager mLayoutManager;

    Call<DataPickingList> pickingCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_list);

        toolbar = findViewById(R.id.toolbar_list_picking);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Picking");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idOutbound = AppController.getInstance().getIdOutbound();
        noOutbound = AppController.getInstance().getNoOutbound();
        referensi = AppController.getInstance().getReferensi();
        namaProject = AppController.getInstance().getNamaProject();

        tvNoOutbound = findViewById(R.id.no_outbound_picking);
        tvProjectName = findViewById(R.id.project_name_outbound);
        tQtyOutbound = findViewById(R.id.textViewQtyOutbound);

        tvNoOutbound.setText(noOutbound + " / " + referensi);
        tvProjectName.setText(namaProject);

        rvPicking = findViewById(R.id.rv_picking_outbound);

        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);
        btnRefresh = findViewById(R.id.buttonRefresh);
        tItemNull = findViewById(R.id.textNoItem);
        etSearchLabel = findViewById(R.id.editTextSearchLabel);
        etSearchLabel.requestFocus();
        ibBarcodeScan = findViewById(R.id.imageButtonBarcodeScan);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRefresh();
                init();
            }
        });

        ibBarcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScan = true;
                AppController.getInstance().setTipeScan("Scan Label");
                IntentIntegrator integrator = new IntentIntegrator(PickingListActivity.this).setCaptureActivity(ScanActivity.class);
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
                if (pickAdapter != null)
                    pickAdapter.getFilter().filter(s);
            }
        });

        etSearchLabel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    scanPicking(etSearchLabel.getText().toString());
                    etSearchLabel.setText("");
                }
                return false;
            }
        });

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
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

    private void init() {
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        pickingCall = service.showPickingList(idGudang, idProject, idOutbound, idUser, token);
        pickingCall.enqueue(new Callback<DataPickingList>() {
            @Override
            public void onResponse(Call<DataPickingList> call, Response<DataPickingList> response) {
                endLoading();
                if (response.isSuccessful()) {
                    if (response.body().getDataPickingArrayList() == null) {
                        showRefresh();
                    }
                    else if (response.body().getDataPickingArrayList().size() == 0) {
                        showRefresh();
                    }
                    else {
                        dataPickingArrayList = response.body().getDataPickingArrayList();
                        rvPicking.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvPicking.setLayoutManager(mLayoutManager);
                        pickAdapter = new PickingAdapter(getApplicationContext(), dataPickingArrayList,
                                PickingListActivity.this, PickingListActivity.this);
                        rvPicking.setAdapter(pickAdapter);

                        String allocated = response.body().getTotalAllocated();
                        String picking = response.body().getTotalPicking();

                        //if (allocated.matches(picking)) {
                        //  finish();
                        //}
                        //else {
                        tQtyOutbound.setText(picking + " / " + allocated);
                        //}

                        if (onScan) {
                            AppController.getInstance().setTipeScan("Scan Label");
                            IntentIntegrator integrator = new IntentIntegrator(PickingListActivity.this).setCaptureActivity(ScanActivity.class);
                            integrator.initiateScan();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                    showRefresh();
                }
            }

            @Override
            public void onFailure(Call<DataPickingList> call, Throwable t) {
                endLoading(); showRefresh();
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please check your internet connection");
            }
        });
    }

    private void pickingProcess(String idOutboundDetail, String idInventory) {
        onSendData = true;
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<PickingResponse> pickingCall = service.pickingProcessbyClick(idGudang, idProject, idOutbound, idUser, token, idInventory, idOutboundDetail);
        pickingCall.enqueue(new Callback<PickingResponse>() {
            @Override
            public void onResponse(Call<PickingResponse> call, Response<PickingResponse> response) {
                endLoading(); onSendData = false;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Picking berhasil", Toast.LENGTH_SHORT).show();
                    init();
                } else {
                    AlertSuccess("Item cannot be picked");
                }
            }

            @Override
            public void onFailure(Call<PickingResponse> call, Throwable t) {
                endLoading(); onSendData = false;
                t.printStackTrace();
                AlertSuccess("Please check your internet connection");
            }
        });
    }

    private void undoPicking(String idOutboundDetail, String idInventory) {
        onLoading();
        onSendData = true;
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusPutAway> undoCall = service.undoPickingProcess(idGudang, idProject, idOutbound, idUser, token, idInventory, idOutboundDetail);
        undoCall.enqueue(new Callback<StatusPutAway>() {
            @Override
            public void onResponse(Call<StatusPutAway> call, Response<StatusPutAway> response) {
                endLoading(); onSendData = false;
                if (response.isSuccessful()) {
                    AlertSuccess("Undo berhasil");
                    init();
                } else {
                    AlertSuccess("Item cannot be undo");
                }
            }

            @Override
            public void onFailure(Call<StatusPutAway> call, Throwable t) {
                endLoading(); onSendData = false;
                t.printStackTrace();
                AlertSuccess("Please check your internet connection");
            }
        });
    }

    private void scanPicking(final String label) {
        onSendData = true;
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusPutAway> scanCall = service.pickingScan(idGudang, idProject, idOutbound, idUser, token, label);
        scanCall.enqueue(new Callback<StatusPutAway>() {
            @Override
            public void onResponse(Call<StatusPutAway> call, Response<StatusPutAway> response) {
                endLoading(); onSendData = false;
                if (response.isSuccessful()) {
                    if (response.body().getStatus().matches("berhasil"))
                        Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                    else if (response.body().getStatus().matches("sudah"))
                        Toast.makeText(getApplicationContext(), "Sudah dipicking", Toast.LENGTH_LONG).show();
                    else {
                        AlertSuccess(label + "\nData tidak ditemukan");
                        onScan = false;
                    }
                    init();
                } else {
                    AlertSuccess("Error");
                }
            }

            @Override
            public void onFailure(Call<StatusPutAway> call, Throwable t) {
                endLoading(); onSendData = false;
                t.printStackTrace();
                AlertSuccess("Please check your internet connection");

            }
        });
    }

    @Override
    public void pickOnclick(DataPicking picking, int position) {
        String idOutboundDetail = picking.getIdOutboundDetail();
        String idInventory = picking.getIdInventoryDetail();
        pickingProcess(idOutboundDetail, idInventory);
    }

    @Override
    public void undoOnclick(DataPicking dataPicking, int position) {
        String idOutboundDetail = dataPicking.getIdOutboundDetail();
        String idInventory = dataPicking.getIdInventoryDetail();
        undoPicking(idOutboundDetail, idInventory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                onScan = false;
            } else {
                String label = result.getContents();
                scanPicking(label);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        pickingCall.cancel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!onSendData) finish();
    }

    @Override
    public void finish() {
        setResult(10);
        super.finish();
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
        if (dataPickingArrayList != null)
            if (dataPickingArrayList.size() > 0) {
                int size = dataPickingArrayList.size();
                dataPickingArrayList.clear();
                pickAdapter.notifyItemRangeRemoved(0, size);
            }
    }

    private void hideRefresh() {
        tItemNull.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PickingListActivity.this);
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
                        startActivity(new Intent(PickingListActivity.this, SearchWarehouseActivity.class));
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
