package com.tigapermata.sewagudangapps.activity.outbound;

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
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.LoginActivity;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.adapter.outbound.OutgoingAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.outbound.DataOutgoing;
import com.tigapermata.sewagudangapps.model.outbound.DataOutgoingList;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class ListOutgoingActivity extends AppCompatActivity implements OutgoingAdapter.OutgoingListener {

    private TextView tNullOutgoing;
    private ProgressBar pbLoading;
    private View vSemiTransparent;
    private Button btnRefresh;
    private RecyclerView rvListOutgoing;
    private FloatingActionButton fab;
    private SearchView searchView;

    private ArrayList<DataOutgoing> dataOutgoingArrayList;
    private OutgoingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String idOutbound;

    Call<DataOutgoingList> dataOutgoingListCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_outgoing);

        dataOutgoingArrayList = new ArrayList<>();

        TextView tProjectName = findViewById(R.id.textProjectName);
        TextView tOutboundId = findViewById(R.id.textIdOutbound);

        tNullOutgoing = findViewById(R.id.textNoOutgoing);
        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);
        btnRefresh = findViewById(R.id.buttonRefresh);
        rvListOutgoing = findViewById(R.id.rv_list_outgoing);

        idOutbound = AppController.getInstance().getIdOutbound();
        String noOutbound = AppController.getInstance().getNoOutbound();
        String referensi = AppController.getInstance().getReferensi();
        if (referensi == null || referensi.matches("")) referensi = "-";
        tProjectName.setText(AppController.getInstance().getNamaProject());
        tOutboundId.setText(noOutbound + " / " + referensi);

        Toolbar toolbar = findViewById(R.id.toolbar_list_outgoing);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Outgoing");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRefresh();
                prepareOutgoingData();
            }
        });

        fab = findViewById(R.id.fab_list_outgoing);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ListOutgoingActivity.this, FormOutgoingActivity.class), 10);
            }
        });

        prepareOutgoingData();
    }

    private void prepareOutgoingData() {
        onLoading();

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        dataOutgoingListCall = service.showOutgoing(idGudang, idProject, idOutbound, idUser, token);
        dataOutgoingListCall.enqueue(new Callback<DataOutgoingList>() {
            @Override
            public void onResponse(Call<DataOutgoingList> call, Response<DataOutgoingList> response) {
                endLoading();
                if (response.isSuccessful()) {
                    if (response.body().getDataOutgoingArrayList() == null) {
                        showRefresh();
                    }
                    else if (response.body().getDataOutgoingArrayList().size() == 0) {
                        showRefresh();
                    }
                    else {
                        dataOutgoingArrayList = response.body().getDataOutgoingArrayList();
                        rvListOutgoing.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvListOutgoing.setLayoutManager(mLayoutManager);
                        mAdapter = new OutgoingAdapter(getApplicationContext(), dataOutgoingArrayList, ListOutgoingActivity.this);
                        rvListOutgoing.setAdapter(mAdapter);
                    }
                }
                else {
                    showRefresh();
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataOutgoingList> call, Throwable t) {
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                endLoading();
                showRefresh();
            }
        });
    }

    @Override
    public void onOutgoingClick(DataOutgoing dataOutgoing, int position) {
        AppController.getInstance().setIdOutgoing(dataOutgoing.getIdOutgoing());

        startActivityForResult(new Intent(this, LoadingItemChecklistActivity.class), 11);
        searchView.setQuery("", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == 10) { //form outgoing
            prepareOutgoingData();
        }
        else if (requestCode == 11 & resultCode == 10) { //loading checklist back pressed
            prepareOutgoingData();
        }
        else if (requestCode == 11 & resultCode == 11) { //loading checklist all done
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_info, menu);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ListOutgoingActivity.this.getComponentName()));
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

        dataOutgoingListCall.cancel();
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

    private void onLoading() {
        tNullOutgoing.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);
    }

    private void showRefresh() {
        tNullOutgoing.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        if (dataOutgoingArrayList != null)
            if (dataOutgoingArrayList.size() > 0) {
                int size = dataOutgoingArrayList.size();
                dataOutgoingArrayList.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
            }
    }

    private void hideRefresh() {
        tNullOutgoing.setVisibility(View.INVISIBLE);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ListOutgoingActivity.this);
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
                        startActivity(new Intent(ListOutgoingActivity.this, SearchWarehouseActivity.class));
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
