package com.tigapermata.sewagudangapps.activity.inbound;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.LoginActivity;
import com.tigapermata.sewagudangapps.activity.MainActivity;
import com.tigapermata.sewagudangapps.activity.ScanActivity;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.adapter.inbound.IncomingAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.SavedIdInbound;
import com.tigapermata.sewagudangapps.model.inbound.Incoming;
import com.tigapermata.sewagudangapps.model.inbound.IncomingList;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class ListIncomingActivity extends AppCompatActivity implements IncomingAdapter.IncomingListener{

    ArrayList<Incoming> incomingArrayList;
    RecyclerView rvIncoming;
    IncomingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String idInbound, noInbound, projectName, referensi;
    private SearchView searchView;
    String idIncoming, metodeInbound;
    TextView namaProject, inboundNumber, tNullIncoming;
    Button btnRefresh;
    ProgressBar pbLoading;
    View vSemiTransparent;
    FloatingActionButton fab;
    boolean onReqIncoming;
    Call<IncomingList> incomingListCall;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_incoming);

        incomingArrayList = new ArrayList<>();

        idInbound = AppController.getInstance().getIdInbound();
        noInbound = AppController.getInstance().getNoInbound();
        projectName = AppController.getInstance().getNamaProject();
        referensi = AppController.getInstance().getReferensi();
        if (referensi.matches("")) referensi = "-";

        Toolbar toolbar = findViewById(R.id.toolbar_list_incoming);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Incoming");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvIncoming = findViewById(R.id.rv_list_incoming);
        namaProject = findViewById(R.id.textProjectName);
        inboundNumber = findViewById(R.id.textIdInbound);
        tNullIncoming = findViewById(R.id.textNoIncoming);
        btnRefresh = findViewById(R.id.buttonRefresh);
        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);

        namaProject.setText(projectName);
        inboundNumber.setText(noInbound + " | " + referensi);

        fab = findViewById(R.id.fab_list_incoming);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ListIncomingActivity.this, FormIncomingActivity.class), 10);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestIncomingData();
            }
        });

        requestIncomingData();
    }

    private void requestIncomingData() {
        onLoading(); onReqIncoming = true;
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        incomingListCall = service.listIncoming(idGudang, idProject, idInbound, idUser, token);
        incomingListCall.enqueue(new Callback<IncomingList>() {
            @Override
            public void onResponse(Call<IncomingList> call, Response<IncomingList> response) {
                endLoading();
                onReqIncoming = false;
                if (response.isSuccessful()) {
                    if (response.body().getIncomingArrayList() == null) {
                        showRefresh();
                    }
                    else if (response.body().getIncomingArrayList().size() == 0) {
                        showRefresh();
                    }
                    else {
                        incomingArrayList = response.body().getIncomingArrayList();
                        metodeInbound = response.body().getMetodeInbound();
                        rvIncoming.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvIncoming.setLayoutManager(mLayoutManager);
                        mAdapter = new IncomingAdapter(getApplicationContext(), incomingArrayList, ListIncomingActivity.this);
                        rvIncoming.setAdapter(mAdapter);
                    }
                } else {
                    showRefresh();
                    Toast.makeText(getApplicationContext(), "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<IncomingList> call, Throwable t) {
                onReqIncoming = false;
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                endLoading();
                showRefresh();
            }
        });

    }

    //saved id inbound and id incoming to database
    private void savedIdInbound() {
        DBHelper helper = new DBHelper(getApplicationContext());
        final SavedIdInbound savedInbound = new SavedIdInbound(idInbound, idIncoming);
        if (helper.getIdInboundCount() == 0) {
            helper.addIdInbound(savedInbound);
        } else {
            helper.updateIdInbound(savedInbound);
        }
    }

    @Override
    public void onIncomingClick(Incoming incoming, int position) {
        idIncoming = incoming.getIdIncoming();

        savedIdInbound();

        String byLabel = "inbound_by_label";
        String byItem = "inbound_by_item"; //gree
        if (metodeInbound != null)
            Log.i("listIncoming", metodeInbound);

        if (metodeInbound != null) {
            if (metodeInbound.equals(byItem)) {
                startActivityForResult(new Intent(this, ItemCheckListActivityByItem.class), 11);
            } else {
                startActivityForResult(new Intent(this, ItemCheckListActivityByLabel.class), 12);
            }
        }
        else startActivityForResult(new Intent(this, ItemCheckListActivityByLabel.class), 12);
        searchView.setQuery("", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == 10) { //form incoming
            requestIncomingData();
        }
        else if (requestCode == 11 & resultCode == 10) { //item checklist by item
            requestIncomingData();
        }
        else if (requestCode == 12 & resultCode == 10) { //item checklist by label
            requestIncomingData();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_info, menu);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ListIncomingActivity.this.getComponentName()));
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
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        setResult(10);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (onReqIncoming) incomingListCall.cancel();
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListIncomingActivity.this);
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

    private void onLoading() {
        tNullIncoming.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);
        fab.setEnabled(false);
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);
        fab.setEnabled(true);
    }

    private void showRefresh() {
        tNullIncoming.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        if (incomingArrayList != null)
            if (incomingArrayList.size() > 0) {
                int size = incomingArrayList.size();
                incomingArrayList.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
            }
    }

    private void logout() {
        DBHelper db = new DBHelper(this);
        db.clearDatabase();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
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
                        startActivity(new Intent(ListIncomingActivity.this, SearchWarehouseActivity.class));
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
