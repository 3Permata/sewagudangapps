package com.tigapermata.sewagudangapps.activity.inbound;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.tigapermata.sewagudangapps.adapter.inbound.IncomingAdapterByItem;
import com.tigapermata.sewagudangapps.adapter.inbound.ItemIncomingAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.inbound.CheckedListbyItem;
import com.tigapermata.sewagudangapps.model.inbound.DataIncomingByItem;
import com.tigapermata.sewagudangapps.model.inbound.ItemIncoming;
import com.tigapermata.sewagudangapps.model.inbound.ItemIncomingDetail;
import com.tigapermata.sewagudangapps.model.inbound.StatusOK;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class ItemCheckListActivityByItem extends AppCompatActivity implements IncomingAdapterByItem.IncomingListenerByItem {

    private TextView tQtyChecklist, namaProject, inboundNumber;
    private ArrayList<DataIncomingByItem> dataIncomingByItemArrayList;
    private RecyclerView rvIncomingByItem;
    private IncomingAdapterByItem mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar pbLoading;
    private View vSemiTransparent;

    String idInbound, idIncoming, projectName, idItem, qtyAktual, qtyDokumen, referensi;

    View view;

    Call<CheckedListbyItem> itemCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_check_list_by_item);

        Toolbar toolbar = findViewById(R.id.toolbar_item_by_label);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inbound - List Item");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referensi = AppController.getInstance().getReferensi();

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        idInbound = dbHelper.getIdInbound().getIdInbound();
        idIncoming = dbHelper.getIdInbound().getIdIncoming();

        tQtyChecklist = findViewById(R.id.textViewQtyChecklist);
        namaProject = findViewById(R.id.namaProject_item_checklist);
        inboundNumber = findViewById(R.id.no_inbound_item_checklist);

        rvIncomingByItem = findViewById(R.id.rv_item_incoming_checklist);

        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);

        init();
    }

    //request list item to server
    private void init() {
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        itemCall = service.itemChecklistByItem(idGudang, idProject, idInbound, idIncoming, idUser, token);
        itemCall.enqueue(new Callback<CheckedListbyItem>() {
            @Override
            public void onResponse(Call<CheckedListbyItem> call, Response<CheckedListbyItem> response) {
                endLoading();
                if (response.isSuccessful()) {
                    String qtyAktual = response.body().getTotalQtyAktual();
                    String qtyDokumen = response.body().getTotalQtyDocument();
                    if (qtyAktual == null) qtyAktual = "0";
                    if (qtyDokumen == null) qtyDokumen = "0";
                    projectName = response.body().getNamaProject();
                    String noInbound = response.body().getNoInbound();

                    tQtyChecklist.setText(qtyAktual + " / " + qtyDokumen);
                    namaProject.setText(projectName);
                    inboundNumber.setText(noInbound + " / " + referensi);
                    inboundNumber.setSelected(true);

                    dataIncomingByItemArrayList = response.body().getDataIncomingByItems();
                    rvIncomingByItem.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rvIncomingByItem.setLayoutManager(mLayoutManager);
                    mAdapter = new IncomingAdapterByItem(getApplicationContext(), dataIncomingByItemArrayList, ItemCheckListActivityByItem.this);
                    rvIncomingByItem.setAdapter(mAdapter);

                }
            }

            @Override
            public void onFailure(Call<CheckedListbyItem> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
            }
        });

    }

    @Override
    public void onItemIncomingClicked(DataIncomingByItem incomingByItem, int position) {
        idItem = incomingByItem.getIdItem();
        qtyAktual = incomingByItem.getQtyAktual();
        qtyDokumen = incomingByItem.getQty();

        if (qtyAktual == null) qtyAktual = "0";
        if (qtyDokumen == null) qtyDokumen = "0";

        AppController.getInstance().setIdItem(idItem);
        AppController.getInstance().setQtyAktual(qtyAktual);
        AppController.getInstance().setQtyDokumen(qtyDokumen);
        AppController.getInstance().setNamaItem(incomingByItem.getNamaItem());

        startActivityForResult(new Intent(this, ChecklistByItem.class), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            init();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);

/*        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ItemCheckListActivityByItem.this.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
*/
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        itemCall.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemCheckListActivityByItem.this);
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
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);
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
                        startActivity(new Intent(ItemCheckListActivityByItem.this, SearchWarehouseActivity.class));
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
