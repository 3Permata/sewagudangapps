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
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.LoginActivity;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.adapter.outbound.PickingAdapterByItem;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.outbound.DataPickingByItem;
import com.tigapermata.sewagudangapps.model.outbound.DataPickingListByItem;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class PickingListByItemActivity extends AppCompatActivity implements PickingAdapterByItem.PickingListenerByItem {
    private TextView tQtyChecklist, tNamaProject, outboundNumber;
    private ArrayList<DataPickingByItem> dataPickingByItemArrayList;
    private RecyclerView rvPickingByItem;
    private PickingAdapterByItem mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar pbLoading;
    private View vSemiTransparent, view;

    private String idOutbound;

    Call<DataPickingListByItem> pickingCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_list_by_item);

        Toolbar toolbar = findViewById(R.id.toolbar_picking_by_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Picking");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idOutbound = AppController.getInstance().getIdOutbound();

        tQtyChecklist = findViewById(R.id.textViewQtyOutbound);
        tNamaProject = findViewById(R.id.project_name_outbound);
        outboundNumber = findViewById(R.id.no_outbound_picking);

        rvPickingByItem = findViewById(R.id.rv_picking_outbound);

        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);

        outboundNumber.setText(AppController.getInstance().getNoOutbound() + " / " + AppController.getInstance().getReferensi());
        tNamaProject.setText(AppController.getInstance().getNamaProject());

        init();
    }

    private void init() {
        onLoading();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        pickingCall = service.showPickingListByItem(idGudang, idProject, idOutbound, idUser, token);
        pickingCall.enqueue(new Callback<DataPickingListByItem>() {
            @Override
            public void onResponse(Call<DataPickingListByItem> call, Response<DataPickingListByItem> response) {
                endLoading();
                if (response.isSuccessful()) {
                    String qtyScanned = response.body().getTotalScanned();
                    String qtyPicking = response.body().getTotalPicking();
                    if (qtyScanned == null) qtyScanned = "0";

                    if (qtyScanned.matches(qtyPicking))
                        finish();

                    tQtyChecklist.setText(qtyScanned + " / " + qtyPicking);

                    dataPickingByItemArrayList = response.body().getDataPickingArrayList();
                    rvPickingByItem.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rvPickingByItem.setLayoutManager(mLayoutManager);
                    mAdapter = new PickingAdapterByItem(getApplicationContext(), dataPickingByItemArrayList, PickingListByItemActivity.this);
                    rvPickingByItem.setAdapter(mAdapter);

                }
            }

            @Override
            public void onFailure(Call<DataPickingListByItem> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
            }
        });

    }

    @Override
    public void onItemPickingClicked(DataPickingByItem pickingByItem, int position) {
        AppController.getInstance().setIdItem(pickingByItem.getIdItem());
        AppController.getInstance().setNamaItem(pickingByItem.getNamaItem());

        startActivityForResult(new Intent(this, PickingChecklistActivity.class), 10);
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
        finish();
    }

    @Override
    public void finish() {
        setResult(10);
        super.finish();
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PickingListByItemActivity.this);
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
                        startActivity(new Intent(PickingListByItemActivity.this, SearchWarehouseActivity.class));
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
