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
import com.tigapermata.sewagudangapps.adapter.stockcount.DetailStockCountByLabelAdapter;
import com.tigapermata.sewagudangapps.adapter.stockcount.DetailStockCountByLabelAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.putaway.DataLocator;
import com.tigapermata.sewagudangapps.model.putaway.DataLocatorList;
import com.tigapermata.sewagudangapps.model.stockcount.AddDetailStockCountResponse;
import com.tigapermata.sewagudangapps.model.stockcount.DetailByLabelList;
import com.tigapermata.sewagudangapps.model.stockcount.DetailStockCountByLabel;
import com.tigapermata.sewagudangapps.model.stockcount.DetailStockCountByLabel;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class DetailStockCountByLabelActivity extends AppCompatActivity implements DetailStockCountByLabelAdapter.DetailStockCountListener {
    private TextView tNullDetailSC;
    private EditText etLabel, etQty;
    private ProgressBar pbLoading;
    private View vSemiTransparent;
    private Button btnRefresh, btnClearLocator, btnClearLabel, btnClearQty, btnSave;
    private ImageButton ibScanLocator, ibScanLabel;
    private RecyclerView rvDetailSCByLabel;
    private SearchView searchView;
    private AutoCompleteTextView acLocator;

    private ArrayList<DetailStockCountByLabel> dataDetailSCByLabelArrayList;
    private DetailStockCountByLabelAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<DataLocator> dataLocatorArrayList;
    private DataLocatorAdapter locatorAdapter;
    Call<DataLocatorList> dataLocatorCall;

    private String idStockCount, idLocator;
    private boolean locatorFound = false, onReqLocator = false, onReqInitList = false, onScanLocator = false, onScanLabel = false, onSaveData = false;

    Call<DetailByLabelList> dataDetailByLabelListCall;
    Call<AddDetailStockCountResponse> addDetailByLabelCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sc_by_label);

        dataDetailSCByLabelArrayList = new ArrayList<>();

        TextView tProjectName = findViewById(R.id.namaProject);

        tNullDetailSC = findViewById(R.id.textNoDetailSC);
        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);
        btnRefresh = findViewById(R.id.buttonRefresh);
        rvDetailSCByLabel = findViewById(R.id.rv_detail_sc_by_label);

        acLocator = findViewById(R.id.nama_locator);
        btnClearLocator = findViewById(R.id.buttonClearLocator);
        ibScanLocator = findViewById(R.id.imageButtonScanLocator);
        etLabel = findViewById(R.id.editTextLabel);
        btnClearLabel = findViewById(R.id.buttonClearLabel);
        ibScanLabel = findViewById(R.id.imageButtonScanLabel);
        etQty = findViewById(R.id.editTextQty);
        btnClearQty = findViewById(R.id.buttonClearQty);
        btnSave = findViewById(R.id.buttonSave);

        idStockCount = AppController.getInstance().getIdStockcount();
        tProjectName.setText(AppController.getInstance().getNamaProject());

        Toolbar toolbar = findViewById(R.id.toolbar_detail_sc_by_label);
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

        btnClearLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLabel.setText("");
                etLabel.requestFocus();
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
                IntentIntegrator integrator = new IntentIntegrator(DetailStockCountByLabelActivity.this).setCaptureActivity(ScanActivity.class);
                integrator.initiateScan();
            }
        });

        ibScanLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanLabel = true;
                AppController.getInstance().setTipeScan("Scan Label");
                IntentIntegrator integrator = new IntentIntegrator(DetailStockCountByLabelActivity.this).setCaptureActivity(ScanActivity.class);
                integrator.initiateScan();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!locatorFound) acLocator.setError("Locator salah");
                else if (etLabel.getText().toString().length() == 0) {
                    etLabel.setError("Data belum terisi");
                    acLocator.setError(null);
                }
                else if (etQty.getText().toString().length() == 0) {
                    etQty.setError("Data belum terisi");
                    etLabel.setError(null);
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
                onScanLabel = false; onScanLocator = false;
            } else {
                if (onScanLocator) {
                    acLocator.setText(result.getContents());
                    findLocator(result.getContents());
                    onScanLocator = false;
                }
                else if (onScanLabel) {
                    etLabel.setText(result.getContents());
                    etQty.requestFocus();
                    etQty.setSelection(etQty.getText().length());
                    onScanLabel = false;
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
        dataDetailByLabelListCall = service.showDetailStockCountByLabel(idGudang, idProject, idStockCount, idUser, token);
        dataDetailByLabelListCall.enqueue(new Callback<DetailByLabelList>() {
            @Override
            public void onResponse(Call<DetailByLabelList> call, Response<DetailByLabelList> response) {
                if (!onReqLocator) endLoading();
                onReqInitList = false;
                if (response.isSuccessful()) {
                    if (response.body().getDetailByLabelArrayList() == null) {
                        showRefresh();
                    }
                    else if (response.body().getDetailByLabelArrayList().size() == 0) {
                        showRefresh();
                    }
                    else {
                        dataDetailSCByLabelArrayList = response.body().getDetailByLabelArrayList();

                        rvDetailSCByLabel.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvDetailSCByLabel.setLayoutManager(mLayoutManager);
                        mAdapter = new DetailStockCountByLabelAdapter(getApplicationContext(), dataDetailSCByLabelArrayList, DetailStockCountByLabelActivity.this);
                        rvDetailSCByLabel.setAdapter(mAdapter);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                    showRefresh();
                }
            }

            @Override
            public void onFailure(Call<DetailByLabelList> call, Throwable t) {
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
                    locatorAdapter = new DataLocatorAdapter(DetailStockCountByLabelActivity.this, R.layout.detail_autocomplete_layout, dataLocatorArrayList);
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
        addDetailByLabelCall = service.addDetailStockCountByLabel(idGudang, idProject, idStockCount, idUser, token, idLocator, etLabel.getText().toString(), etQty.getText().toString());
        addDetailByLabelCall.enqueue(new Callback<AddDetailStockCountResponse>() {
            @Override
            public void onResponse(Call<AddDetailStockCountResponse> call, Response<AddDetailStockCountResponse> response) {
                endLoading(); onSaveData = false;
                if (response.isSuccessful()) {
                    if (response.body().getStatus().matches("Add Stock Count berhasil")) {
                        Toast.makeText(DetailStockCountByLabelActivity.this, "berhasil", Toast.LENGTH_LONG).show();
                        initList();
                        etQty.setText(""); etQty.setError(null); etLabel.setText(""); etLabel.setError(null); acLocator.setError(null);
                        etLabel.requestFocus();
                    }
                    else Toast.makeText(DetailStockCountByLabelActivity.this, "gagal", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DetailStockCountByLabelActivity.this, "gagal", Toast.LENGTH_LONG).show();
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
    public void onDetailStockCountClick(DetailStockCountByLabel detailSC, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View LabelView = inflater.inflate(R.layout.dialog_detail_sc_by_label, null);
        builder.setView(LabelView);

        TextView tTanggalDetailSC, tStatusDetailSC, tUser, tNamaLabel, tLocator, tQtyInput, tQtyAktual, tHasil;

        tTanggalDetailSC = LabelView.findViewById(R.id.tanggal_detail_sc);
        tStatusDetailSC = LabelView.findViewById(R.id.status_detail_sc);
        tUser = LabelView.findViewById(R.id.nama_user);
        tNamaLabel = LabelView.findViewById(R.id.label);
        tLocator = LabelView.findViewById(R.id.textViewLocator);
        tQtyInput = LabelView.findViewById(R.id.textViewQtyInput);
        tQtyAktual = LabelView.findViewById(R.id.textViewQtyAktual);
        tHasil = LabelView.findViewById(R.id.textViewHasil);

        tTanggalDetailSC.setText(detailSC.getTanggal());
        tStatusDetailSC.setText(detailSC.getStatus());
        tNamaLabel.setText(detailSC.getLabel());
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
        searchView.setSearchableInfo(searchManager.getSearchableInfo(DetailStockCountByLabelActivity.this.getComponentName()));
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
    public boolean onOptionsItemSelected(MenuItem Label) {
        switch (Label.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_home:
                View menuLabelView = findViewById(R.id.action_home);
                showPopUpMenu(menuLabelView);
        }
        return super.onOptionsItemSelected(Label);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (onReqInitList) dataDetailByLabelListCall.cancel();
        if (onReqLocator) dataLocatorCall.cancel();
        if (onSaveData) addDetailByLabelCall.cancel();
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
            etLabel.post(new Runnable() {
                public void run() {
                    etLabel.requestFocus();
                }
            });
        }
        else {
            acLocator.requestFocus();
            acLocator.setSelection(acLocator.getText().length());
            Toast.makeText(this, "Locator tidak dLabelukan", Toast.LENGTH_LONG).show();
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
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);
    }

    private void showRefresh() {
        tNullDetailSC.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        if (dataDetailSCByLabelArrayList != null)
            if (dataDetailSCByLabelArrayList.size() > 0) {
                int size = dataDetailSCByLabelArrayList.size();
                dataDetailSCByLabelArrayList.clear();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailStockCountByLabelActivity.this);
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
            public boolean onMenuItemClick(MenuItem Label) {
                switch (Label.getItemId()) {
                    case R.id.action_warehouse:
                        startActivity(new Intent(DetailStockCountByLabelActivity.this, SearchWarehouseActivity.class));
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
