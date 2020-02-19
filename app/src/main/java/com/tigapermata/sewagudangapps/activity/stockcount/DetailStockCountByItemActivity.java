package com.tigapermata.sewagudangapps.activity.stockcount;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.tigapermata.sewagudangapps.activity.ScanActivity;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.adapter.putaway.DataLocatorAdapter;
import com.tigapermata.sewagudangapps.adapter.stockcount.DetailStockCountByItemAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.putaway.DataLocator;
import com.tigapermata.sewagudangapps.model.putaway.DataLocatorList;
import com.tigapermata.sewagudangapps.model.stockcount.AddDetailStockCountResponse;
import com.tigapermata.sewagudangapps.model.stockcount.DetailByItemList;
import com.tigapermata.sewagudangapps.model.stockcount.DetailStockCountByItem;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class DetailStockCountByItemActivity extends AppCompatActivity implements DetailStockCountByItemAdapter.DetailStockCountListener {
    private TextView tNullDetailSC;
    private EditText etItem, etQty;
    private ProgressBar pbLoading;
    private View vSemiTransparent;
    private Button btnRefresh, btnClearLocator, btnClearItem, btnClearQty, btnSave;
    private ImageButton ibScanLocator, ibScanItem;
    private RecyclerView rvDetailSCByItem;
    private SearchView searchView;
    private AutoCompleteTextView acLocator;

    private ArrayList<DetailStockCountByItem> dataDetailSCByItemArrayList;
    private DetailStockCountByItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<DataLocator> dataLocatorArrayList;
    private DataLocatorAdapter locatorAdapter;
    Call<DataLocatorList> dataLocatorCall;

    private String idStockCount, idLocator;
    private boolean locatorFound = false, onReqLocator = false, onReqInitList = false, onScanLocator = false, onScanItem = false, onSaveData = false;

    Call<DetailByItemList> dataDetailByItemListCall;
    Call<AddDetailStockCountResponse> addDetailByItemCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sc_by_item);

        dataDetailSCByItemArrayList = new ArrayList<>();

        TextView tProjectName = findViewById(R.id.namaProject);

        tNullDetailSC = findViewById(R.id.textNoDetailSC);
        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);
        btnRefresh = findViewById(R.id.buttonRefresh);
        rvDetailSCByItem = findViewById(R.id.rv_detail_sc_by_item);

        acLocator = findViewById(R.id.nama_locator);
        btnClearLocator = findViewById(R.id.buttonClearLocator);
        ibScanLocator = findViewById(R.id.imageButtonScanLocator);
        etItem = findViewById(R.id.editTextItem);
        btnClearItem = findViewById(R.id.buttonClearItem);
        ibScanItem = findViewById(R.id.imageButtonScanItem);
        etQty = findViewById(R.id.editTextQty);
        btnClearQty = findViewById(R.id.buttonClearQty);
        btnSave = findViewById(R.id.buttonSave);

        idStockCount = AppController.getInstance().getIdStockcount();
        tProjectName.setText(AppController.getInstance().getNamaProject());

        Toolbar toolbar = findViewById(R.id.toolbar_detail_sc_by_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Stock Count");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRefresh();
                initList();
            }
        });

        acLocator.setThreshold(1);
        acLocator.setOnItemClickListener(onLocatorClick);
        acLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acLocator.showDropDown();
            }
        });
        acLocator.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locatorFound = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        acLocator.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    findLocator(acLocator.getText().toString());
                    return true;
                }
                return false;
            }
        });
        acLocator.requestFocus();

        btnClearLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acLocator.setText("");
                acLocator.requestFocus();
            }
        });

        btnClearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etItem.setText("");
                etItem.requestFocus();
            }
        });

        btnClearQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etQty.setText("");
                etQty.requestFocus();
            }
        });

        ibScanLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanLocator = true;
                AppController.getInstance().setTipeScan("Scan Locator");
                IntentIntegrator integrator = new IntentIntegrator(DetailStockCountByItemActivity.this).setCaptureActivity(ScanActivity.class);
                integrator.initiateScan();
            }
        });

        ibScanItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanItem = true;
                AppController.getInstance().setTipeScan("Scan Label");
                IntentIntegrator integrator = new IntentIntegrator(DetailStockCountByItemActivity.this).setCaptureActivity(ScanActivity.class);
                integrator.initiateScan();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!locatorFound) acLocator.setError("Locator salah");
                else if (etItem.getText().toString().length() == 0) {
                    etItem.setError("Data belum terisi");
                    acLocator.setError(null);
                }
                else if (etQty.getText().toString().length() == 0) {
                    etQty.setError("Data belum terisi");
                    etItem.setError(null);
                }
                else saveData();
            }
        });

        initList(); requestDataLocator();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                onScanItem = false; onScanLocator = false;
            } else {
                if (onScanLocator) {
                    acLocator.setText(result.getContents());
                    findLocator(result.getContents());
                    onScanLocator = false;
                }
                else if (onScanItem) {
                    etItem.setText(result.getContents());
                    etQty.requestFocus();
                    etQty.setSelection(etQty.getText().length());
                    onScanItem = false;
                }
            }
        }
    }

    private void initList() {
        onLoading(); onReqInitList = true;
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        dataDetailByItemListCall = service.showDetailStockCountByItem(idGudang, idProject, idStockCount, idUser, token);
        dataDetailByItemListCall.enqueue(new Callback<DetailByItemList>() {
            @Override
            public void onResponse(Call<DetailByItemList> call, Response<DetailByItemList> response) {
                if (!onReqLocator) endLoading();
                onReqInitList = false;
                if (response.isSuccessful()) {
                    if (response.body().getDetailByItemArrayList() == null) {
                        showRefresh();
                    }
                    else if (response.body().getDetailByItemArrayList().size() == 0) {
                        showRefresh();
                    }
                    else {
                        dataDetailSCByItemArrayList = response.body().getDetailByItemArrayList();

                        rvDetailSCByItem.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvDetailSCByItem.setLayoutManager(mLayoutManager);
                        mAdapter = new DetailStockCountByItemAdapter(getApplicationContext(), dataDetailSCByItemArrayList, DetailStockCountByItemActivity.this);
                        rvDetailSCByItem.setAdapter(mAdapter);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                    showRefresh();
                }
            }

            @Override
            public void onFailure(Call<DetailByItemList> call, Throwable t) {
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                if (!onReqLocator) endLoading();
                showRefresh(); onReqInitList = false;
            }
        });
    }

    private void requestDataLocator() {
        onReqLocator = true;
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        dataLocatorCall = service.showLocator(idGudang, idProject, idUser, token);
        dataLocatorCall.enqueue(new Callback<DataLocatorList>() {
            @Override
            public void onResponse(Call<DataLocatorList> call, Response<DataLocatorList> response) {
                if (!onReqInitList) endLoading();
                onReqLocator = false;
                if (response.isSuccessful()) {
                    dataLocatorArrayList = response.body().getDataLocatorArrayList();
                    locatorAdapter = new DataLocatorAdapter(DetailStockCountByItemActivity.this, R.layout.detail_autocomplete_layout, dataLocatorArrayList);
                    acLocator.setAdapter(locatorAdapter);
                }
            }

            @Override
            public void onFailure(Call<DataLocatorList> call, Throwable t) {
                onReqLocator = false;
                t.printStackTrace();
                if (!onReqInitList) endLoading();
                if (!call.isCanceled())
                    AlertSuccess("Check your internet connection");
            }
        });
    }

    private void saveData() {
        onSaveData = true;
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        addDetailByItemCall = service.addDetailStockCountByItem(idGudang, idProject, idStockCount, idUser, token, idLocator, etItem.getText().toString(), etQty.getText().toString());
        addDetailByItemCall.enqueue(new Callback<AddDetailStockCountResponse>() {
            @Override
            public void onResponse(Call<AddDetailStockCountResponse> call, Response<AddDetailStockCountResponse> response) {
                endLoading(); onSaveData = false;
                if (response.isSuccessful()) {
                    if (response.body().getStatus().matches("Add Stock Count berhasil")) {
                        Toast.makeText(DetailStockCountByItemActivity.this, "berhasil", Toast.LENGTH_LONG).show();
                        initList();
                        etQty.setText(""); etQty.setError(null); etItem.setText(""); etItem.setError(null); acLocator.setError(null);
                        etItem.requestFocus();
                    }
                    else Toast.makeText(DetailStockCountByItemActivity.this, "gagal", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DetailStockCountByItemActivity.this, "gagal", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddDetailStockCountResponse> call, Throwable t) {
                onSaveData = false;
                t.printStackTrace();
                endLoading();
                if (!call.isCanceled())
                    AlertSuccess("Check your internet connection");
            }
        });
    }

    @Override
    public void onDetailStockCountClick(DetailStockCountByItem detailSC, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View itemView = inflater.inflate(R.layout.dialog_detail_sc_by_item, null);
        builder.setView(itemView);

        TextView tTanggalDetailSC, tStatusDetailSC, tUser, tNamaItem, tLocator, tQtyInput, tQtyAktual, tHasil;

        tTanggalDetailSC = itemView.findViewById(R.id.tanggal_detail_sc);
        tStatusDetailSC = itemView.findViewById(R.id.status_detail_sc);
        tUser = itemView.findViewById(R.id.nama_user);
        tNamaItem = itemView.findViewById(R.id.nama_item);
        tLocator = itemView.findViewById(R.id.textViewLocator);
        tQtyInput = itemView.findViewById(R.id.textViewQtyInput);
        tQtyAktual = itemView.findViewById(R.id.textViewQtyAktual);
        tHasil = itemView.findViewById(R.id.textViewHasil);

        tTanggalDetailSC.setText(detailSC.getTanggal());
        tStatusDetailSC.setText(detailSC.getStatus());
        tNamaItem.setText(detailSC.getItem());
        tLocator.setText(detailSC.getLocator());
        tQtyInput.setText(detailSC.getQty());
        tHasil.setText(detailSC.getCheckQty());

        if (detailSC.getUser() == null || detailSC.getUser().matches("")) tUser.setText("-");
        else tUser.setText(detailSC.getUser());
        if (detailSC.getQtyActual() == null || detailSC.getQtyActual().matches("")) tQtyAktual.setText("-");
        else tQtyAktual.setText(detailSC.getQtyActual());

        if (detailSC.getStatus().matches("Match"))
            tStatusDetailSC.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green_complete, null));
        else
            tStatusDetailSC.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red_unmatch, null));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_info, menu);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(DetailStockCountByItemActivity.this.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mAdapter != null)
                    mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter != null)
                    mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_home:
                View menuItemView = findViewById(R.id.action_home);
                showPopUpMenu(menuItemView);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (onReqInitList) dataDetailByItemListCall.cancel();
        if (onReqLocator) dataLocatorCall.cancel();
        if (onSaveData) addDetailByItemCall.cancel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        setResult(10);
        super.finish();
    }

    private void findLocator(String s) {
        int index = locatorAdapter.findItem(s);
        if (index != -1) {
            DataLocator d = locatorAdapter.getItemFromAll(index);
            idLocator = d.getIdLocator();
            locatorFound = true;
            etItem.post(new Runnable() {
                public void run() {
                    etItem.requestFocus();
                }
            });
        }
        else {
            acLocator.requestFocus();
            acLocator.setSelection(acLocator.getText().length());
            Toast.makeText(this, "Locator tidak ditemukan", Toast.LENGTH_LONG).show();
        }
    }

    private AdapterView.OnItemClickListener onLocatorClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DataLocator data = locatorAdapter.getItem(position);
            idLocator = data.getIdLocator();
            locatorFound = true;
        }
    };

    private void onLoading() {
        tNullDetailSC.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);

        btnClearLocator.setEnabled(false);
        btnClearItem.setEnabled(false);
        btnClearQty.setEnabled(false);
        ibScanLocator.setEnabled(false);
        ibScanItem.setEnabled(false);
        btnSave.setEnabled(false);
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);

        btnClearLocator.setEnabled(true);
        btnClearItem.setEnabled(true);
        btnClearQty.setEnabled(true);
        ibScanLocator.setEnabled(true);
        ibScanItem.setEnabled(true);
        btnSave.setEnabled(true);
    }

    private void showRefresh() {
        tNullDetailSC.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        if (dataDetailSCByItemArrayList != null)
            if (dataDetailSCByItemArrayList.size() > 0) {
                int size = dataDetailSCByItemArrayList.size();
                dataDetailSCByItemArrayList.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
            }
    }

    private void hideRefresh() {
        tNullDetailSC.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
    }

    private void logout() {
        DBHelper db = new DBHelper(this);
        db.clearDatabase();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailStockCountByItemActivity.this);
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
                        startActivity(new Intent(DetailStockCountByItemActivity.this, SearchWarehouseActivity.class));
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
