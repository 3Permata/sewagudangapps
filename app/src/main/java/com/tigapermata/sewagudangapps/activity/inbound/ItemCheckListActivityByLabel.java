package com.tigapermata.sewagudangapps.activity.inbound;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.tigapermata.sewagudangapps.activity.MainActivity;
import com.tigapermata.sewagudangapps.activity.ScanActivity;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.adapter.inbound.ItemIncomingAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.inbound.Inbound;
import com.tigapermata.sewagudangapps.model.inbound.ItemIncoming;
import com.tigapermata.sewagudangapps.model.inbound.ItemIncomingChecked;
import com.tigapermata.sewagudangapps.model.inbound.ItemIncomingDetail;
import com.tigapermata.sewagudangapps.model.inbound.Locator;
import com.tigapermata.sewagudangapps.model.inbound.LocatorList;
import com.tigapermata.sewagudangapps.model.inbound.StatusOK;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.SearchToolbar;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class ItemCheckListActivityByLabel extends AppCompatActivity implements
        ItemIncomingAdapter.ItemIncomingListener{

    Toolbar toolbar;

    boolean onScanLocator = false, onScan = false;
    String idInbound, idInboundDetail, idIncoming, idGudang, idProject, namaLocator;
    TextView tvQty, tvTotalQty, namaProject, inboundNumber, tItemNull;
    EditText etSearchLabel;
    Button btnRefresh, btnClearLocator;
    ImageButton ibBarcodeScan, ibBarcodeScanLocator;
    ProgressBar pbLoading;

    private ArrayList<ItemIncoming> itemIncomingArrayList;
    private RecyclerView rvItemIncoming;
    private ItemIncomingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<String> str;
    View itemView, view, vSemiTransparent;
    EditText etQtyAktual;
    AutoCompleteTextView etLocator;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_check_list);

        toolbar = findViewById(R.id.toolbar_item_check_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inbound - List Item");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idInbound = AppController.getInstance().getIdInbound();
        String noInbound = AppController.getInstance().getNoInbound();
        String projectName = AppController.getInstance().getNamaProject();
        String referensi = AppController.getInstance().getReferensi();
        if (referensi.matches("")) referensi = "-";

        tvTotalQty = findViewById(R.id.textTotalQty);
        namaProject = findViewById(R.id.namaProject_item_checklist);
        inboundNumber = findViewById(R.id.no_inbound_item_checklist);
        tItemNull = findViewById(R.id.textNoItem);
        etSearchLabel = findViewById(R.id.editTextSearchLabel);
        etSearchLabel.requestFocus();
        ibBarcodeScan = findViewById(R.id.imageButtonBarcodeScan);
        btnRefresh = findViewById(R.id.buttonRefresh);
        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);

        rvItemIncoming = findViewById(R.id.rv_item_incoming_checklist);

        namaProject.setText(projectName);
        inboundNumber.setText(noInbound + " | " + referensi);


        DBHelper dbHelper = new DBHelper(getApplicationContext());
        idInbound = dbHelper.getIdInbound().getIdInbound();
        idIncoming = dbHelper.getIdInbound().getIdIncoming();
        idGudang = dbHelper.getIds().getIdGudang();
        idProject = dbHelper.getIds().getIdProject();

        requestLocator();

        preparedDataChecked();

        FloatingActionButton fab = findViewById(R.id.fab_item_checklist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ItemCheckListActivityByLabel.this, AddItemIncomingActivity.class), 10);
            }
        });

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
                AppController.getInstance().setTipeScan("Scan Label");
                IntentIntegrator integrator = new IntentIntegrator(ItemCheckListActivityByLabel.this).setCaptureActivity(ScanActivity.class);
                integrator.initiateScan();
                onScan = true;
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
                    incomingChecklist(etSearchLabel.getText().toString());
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(10);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                onScanLocator = false; onScan = false;
            } else {
//                Toast.makeText(getApplicationContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                new IntentIntegrator(FirstActivity.this).setCaptureActivity(MainActivity.class).initiateScan();
                String label = result.getContents();

                if(onScanLocator) {
                    boolean locatorFound = false;
                    for (int i = 0; i < str.size(); i++) {
                        if (str.get(i).matches(label)) locatorFound = true;
                    }
                    if (locatorFound) {
                        onScan = true;
                        if (idGudang.matches("15") && idProject.matches("140")) {
                            //etLocator.setText(label);
                            //namaLocator = label;
                            sendData("1", idInboundDetail, label);
                        }
                        else {
                            onScan = false;
                            etLocator.setText(label);
                            namaLocator = label;
                            //sendData(etQtyAktual.getText().toString(), idInboundDetail, label);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    else {
                        etLocator.setText(label);
                        AlertSuccess("Locator tidak ditemukan");
                        onScanLocator = false; onScan = false;
                    }
                }
                else incomingChecklist(label);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 10) {
                preparedDataChecked();
            }
        }
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

    //request to server for list of item that will be checking
    private void preparedDataChecked() {
        onLoading();

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<ItemIncomingChecked> itemCall = service.dataItemIncomingChecked(idGudang, idProject, idInbound, idIncoming, idUser, token);
        itemCall.enqueue(new Callback<ItemIncomingChecked>() {
            @Override
            public void onResponse(Call<ItemIncomingChecked> call, Response<ItemIncomingChecked> response) {
                endLoading();
                if (response.isSuccessful()) {
                    if (response.body().getItemIncomingArrayList() == null) {
                        showRefresh();
                    }
                    else if (response.body().getItemIncomingArrayList().size() == 0) {
                        showRefresh();
                    }
                    else {
                        String qtyAktual = response.body().getTotalQtyAktual();
                        String qtyDokumen = response.body().getTotalQtyDocument();
                        if (qtyAktual == null) qtyAktual = "0";
                        if (qtyDokumen == null) qtyDokumen = "0";

                        tvTotalQty.setText(qtyAktual + " / " + qtyDokumen);

                        itemIncomingArrayList = response.body().getItemIncomingArrayList();

                        rvItemIncoming.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvItemIncoming.setLayoutManager(mLayoutManager);
                        mAdapter = new ItemIncomingAdapter(getApplicationContext(), itemIncomingArrayList, ItemCheckListActivityByLabel.this);
                        rvItemIncoming.setAdapter(mAdapter);

                        if (qtyAktual.matches(qtyDokumen)) onScan = false;

                        if (onScan) {
                            AppController.getInstance().setTipeScan("Scan Label");
                            IntentIntegrator integrator = new IntentIntegrator(ItemCheckListActivityByLabel.this).setCaptureActivity(ScanActivity.class);
                            integrator.initiateScan();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                    showRefresh();
                }
            }

            @Override
            public void onFailure(Call<ItemIncomingChecked> call, Throwable t) {
                endLoading(); showRefresh();
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            namaLocator = String.valueOf(parent.getItemAtPosition(position));
        }
    };


    //request list of locator, that will be used for autocomplete
    private void requestLocator() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<LocatorList> locatorCAll = service.requestLocator(idGudang, idProject, idInbound, idUser, token);
        locatorCAll.enqueue(new Callback<LocatorList>() {
            @Override
            public void onResponse(Call<LocatorList> call, Response<LocatorList> response) {
                if (response.isSuccessful()) {
                    str = new ArrayList<>();
                    for (Locator locator : response.body().getLocatorArrayList()) {
                        str.add(locator.getNamaLocator());
                    }
//                    locatorArrayList = response.body().getLocatorArrayList();
                }
                else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LocatorList> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //send data after checking
    private void incomingChecklist(final String labels) {
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<ItemIncomingDetail> incomingDetailCall = service.incomingChecklist(idGudang, idProject, idInbound, idIncoming, idUser, token, labels);
        incomingDetailCall.enqueue(new Callback<ItemIncomingDetail>() {
            @Override
            public void onResponse(Call<ItemIncomingDetail> call, Response<ItemIncomingDetail> response) {
                endLoading();
                if (response.isSuccessful()) {
                    String qty = response.body().getQty();
                    String qtyAktual = response.body().getQtyAktual();
                    idInboundDetail = response.body().getIdInboundDetail();
                    String status = response.body().getStatus();

                    Log.e("status", status);

                    String incomplete = "incomplete";
                    String complete = "complete";
                    if (status.equals(complete)) {
                        AlertSuccess("Data sudah di ceklist");
                        onScan = false;
                        clearSearchLabel();
                    } else if (status.equals(incomplete)) {
                        AlertIncomingChecklist(qty, idInboundDetail, qtyAktual);
                    }
                } else {
                    AlertSuccess(labels + "\nLabel salah");
                    onScan = false;
                    clearSearchLabel();
                }
            }

            @Override
            public void onFailure(Call<ItemIncomingDetail> call, Throwable t) {
                endLoading(); onScan = false;
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //alert for input data after checking
    private void AlertIncomingChecklist(final String qty, final String idInboundDetail, final String qtyAktual) {
        final int qtyDoc, qtyAct;
        qtyDoc = Integer.parseInt(qty);
        if (qtyAktual == null) qtyAct = 0;
        else qtyAct = Integer.parseInt(qtyAktual);

        AlertDialog.Builder builder = new AlertDialog.Builder(ItemCheckListActivityByLabel.this);
        LayoutInflater inflater = getLayoutInflater();

        if (idGudang.matches("15") && idProject.matches("140")) {
            itemView = inflater.inflate(R.layout.dialog_incoming_checklist_celanese, null);
            builder.setView(itemView);
        }
        else {
            itemView = inflater.inflate(R.layout.dialog_incoming_checklist, null);
            builder.setView(itemView);

            tvQty = itemView.findViewById(R.id.qty);
            etQtyAktual = itemView.findViewById(R.id.qty_aktual);

            tvQty.setText((qtyDoc - qtyAct) + "");

            if (qtyAktual!= null && !qty.equals(qtyAktual)) {
                int qtyResult = qtyDoc - qtyAct;
                if (qtyAct == 0) {
                    etQtyAktual.setText(qty);
                } else {
                    etQtyAktual.setText(String.valueOf(qtyResult));
                }
            } else {
                etQtyAktual.setText(qty);
            }
            etQtyAktual.requestFocus();
            etQtyAktual.setSelection(etQtyAktual.getText().length());
        }

        etLocator = itemView.findViewById(R.id.id_locator);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                str.toArray(new String[0]));
        etLocator.setAdapter(adapter);
        /*etLocator.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etLocator.showDropDown();
            }
        });*/
        etLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLocator.showDropDown();
            }
        });
        etLocator.setOnItemClickListener(onItemClickListener);

        etLocator.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    boolean locatorFound = false;
                    for (int i = 0; i < str.size(); i++) {
                        if (str.get(i).matches(etLocator.getText().toString())) locatorFound = true;
                    }
                    if (locatorFound) {
                        if (idGudang.matches("15") && idProject.matches("140"))
                            sendData("1", idInboundDetail, namaLocator);
                        else
                            sendData(etQtyAktual.getText().toString(), idInboundDetail, namaLocator);
                        mAdapter.notifyDataSetChanged();
                    }
                    else {
                        AlertSuccess("Locator tidak ditemukan");
                    }
                    etLocator.setText("");
                }
                return false;
            }
        });

        ibBarcodeScanLocator = itemView.findViewById(R.id.btnScanLocator);
        ibBarcodeScanLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanLocator = true;
                AppController.getInstance().setTipeScan("Scan Locator");
                IntentIntegrator integrator = new IntentIntegrator(ItemCheckListActivityByLabel.this).setCaptureActivity(ScanActivity.class);
                integrator.initiateScan();
            }
        });

        btnClearLocator = itemView.findViewById(R.id.btnClearLocator);
        btnClearLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLocator.setText("");
                etLocator.requestFocus();
            }
        });

        builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (idGudang.matches("15") && idProject.matches("140")) {
                    sendData("1", idInboundDetail, namaLocator);
                    mAdapter.notifyDataSetChanged();
                }
                else {
                    final String qtyA = etQtyAktual.getText().toString();
                    final int qty_akt = Integer.parseInt(qtyA);
                    if (qty_akt > qtyDoc - qtyAct || qty_akt < 0) {
                        AlertSuccess("Data tidak dapat diproses");
                    } else {
                        sendData(qtyA, idInboundDetail, namaLocator);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
        if (idGudang.matches("15") && idProject.matches("140")) etLocator.requestFocus();

        if (onScan) {
            onScanLocator = true;
            AppController.getInstance().setTipeScan("Scan Locator");
            IntentIntegrator integrator = new IntentIntegrator(ItemCheckListActivityByLabel.this).setCaptureActivity(ScanActivity.class);
            integrator.initiateScan();
        }
    }

    //send all data that has been checked
    private void sendData(String qtyAktual, String idInboundDetail, String namaLocator) {
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusOK> checklistInputCall = service.incomingChecklistInput(idGudang, idProject,
                idInbound, idIncoming, idUser, token, qtyAktual, idInboundDetail, namaLocator);
        checklistInputCall.enqueue(new Callback<StatusOK>() {
            @Override
            public void onResponse(Call<StatusOK> call, Response<StatusOK> response) {
                endLoading();

                if (response.isSuccessful()) {
                    alertDialog.dismiss();
                    if (onScanLocator) { onScanLocator = false; onScan = true; }
                    Toast.makeText(getApplicationContext(), "berhasil", Toast.LENGTH_SHORT).show();
                    preparedDataChecked();
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusOK> call, Throwable t) {
                endLoading();
                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onItemIncomingClick(ItemIncoming incoming, int position) {
        String labels = incoming.getLabel();
        incomingChecklist(labels);
    }

    private void onLoading() {
        hideRefresh();
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
        if (itemIncomingArrayList != null)
            if (itemIncomingArrayList.size() > 0) {
                int size = itemIncomingArrayList.size();
                itemIncomingArrayList.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
            }
    }

    private void hideRefresh() {
        tItemNull.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
    }

    private void clearSearchLabel() {
        etSearchLabel.setText("");
        etSearchLabel.requestFocus();
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemCheckListActivityByLabel.this);
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_not_found_data, null);
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
                        startActivity(new Intent(ItemCheckListActivityByLabel.this, SearchWarehouseActivity.class));
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
