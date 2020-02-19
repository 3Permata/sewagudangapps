package com.tigapermata.sewagudangapps.activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.inbound.FormIncomingActivity;
import com.tigapermata.sewagudangapps.activity.inbound.ListIncomingActivity;
import com.tigapermata.sewagudangapps.adapter.GudangAdapter;
import com.tigapermata.sewagudangapps.adapter.GudangAdapter2;
import com.tigapermata.sewagudangapps.adapter.ProjectAdapter;
import com.tigapermata.sewagudangapps.adapter.ProjectAdapter2;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.Gudang;
import com.tigapermata.sewagudangapps.model.GudangList;
import com.tigapermata.sewagudangapps.model.Project;
import com.tigapermata.sewagudangapps.model.ProjectList;
import com.tigapermata.sewagudangapps.model.SavedId;
import com.tigapermata.sewagudangapps.utils.CustomButton;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class SearchWarehouseActivity extends AppCompatActivity {

    private ImageView imgSetting;
    private Spinner spGudang, spProject;
    private GudangAdapter2 adapterGudang;
    private ProjectAdapter2 adapterProject;
    private Button btnCari;
    private boolean onLoadingGudang, onLoadingProject, gudangFailed, projectFailed;
    private String idGudang, idProject, namaGudang, namaProject;
    private List<Gudang> gudangList;
    private Call<GudangList> gudangCall;
    private List<Project> projectList;
    private Call<ProjectList> projectCall;
    private DBHelper dbHelper;

    View view, vSemiTransparent;
    ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_warehouse);

        dbHelper = new DBHelper(getApplicationContext());
        gudangList = new ArrayList<>();
        projectList = new ArrayList<>();

        imgSetting = findViewById(R.id.imgSetting);
        btnCari = findViewById(R.id.btnCari);
        spGudang = findViewById(R.id.spinnerGudang);
        spProject = findViewById(R.id.spinnerProject);
        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);

        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpMenu(imgSetting);
            }
        });

        spGudang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idGudang = adapterGudang.getItem(position).getIdGudang();
                namaGudang = adapterGudang.getItem(position).getNamaGudang();
                searchProject();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idProject = adapterProject.getItem(position).getIdProject();
                namaProject = adapterProject.getItem(position).getNamaProject();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onLoadingGudang && !onLoadingProject) {
                    saveIdToDB();
                    Intent intent = new Intent(SearchWarehouseActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Loading, please wait", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchGudang();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (onLoadingGudang) gudangCall.cancel();
        if (onLoadingProject) projectCall.cancel();
    }

    private void onLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);

        btnCari.setEnabled(false);
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);

        btnCari.setEnabled(true);
    }

    private void showPopUpMenu(View view) {
        final PopupMenu popup = new PopupMenu(SearchWarehouseActivity.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_logout, popup.getMenu());

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
                    case R.id.action_logout:
                        logOut();
                        return true;
                    default:
                }
                popup.dismiss();
                return false;
            }
        });
        popup.show();
    }

    //saving id gudang and id project to sqlite
    private void saveIdToDB() {
        final SavedId ids = new SavedId(idGudang, namaGudang, idProject, namaProject);
        if (dbHelper.getIdsCount() == 0) {
            dbHelper.addIDS(ids);
        } else {
            dbHelper.updateIds(ids);
        }

    }

    //request for list gudang to server
    private void searchGudang() {
        onLoadingGudang = true; gudangFailed = false;
        onLoading();
        final String idUser = dbHelper.getTokenn().getIdUser();
        final String token = dbHelper.getTokenn().getToken();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        gudangCall = service.searchGudang(idUser, token);
        gudangCall.enqueue(new Callback<GudangList>() {
            @Override
            public void onResponse(Call<GudangList> call, Response<GudangList> response) {
                onLoadingGudang = false;
                if (response.isSuccessful()) {
                    gudangList = response.body().getAddAllGudangList();
                    idGudang = gudangList.get(0).getIdGudang();
                    namaGudang = gudangList.get(0).getNamaGudang();

                    adapterGudang = new GudangAdapter2(SearchWarehouseActivity.this, android.R.layout.simple_spinner_dropdown_item, gudangList);
                    spGudang.setAdapter(adapterGudang);

                    searchProject();
                }
            }

            @Override
            public void onFailure(Call<GudangList> call, Throwable t) {
                t.printStackTrace();
                gudangFailed = true;
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                onLoadingGudang = false;
            }
        });
    }

    //request list project to server based on the gudang's info
    private void searchProject() {
        onLoading();
        onLoadingProject = true; projectFailed = false;
        final String idUser = dbHelper.getTokenn().getIdUser();
        final String token = dbHelper.getTokenn().getToken();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        projectCall = service.searchProject(idUser, token, idGudang);
        projectCall.enqueue(new Callback<ProjectList>() {
            @Override
            public void onResponse(Call<ProjectList> call, Response<ProjectList> response) {
                onLoadingProject = false;
                endLoading();
                if (response.isSuccessful()) {
                    projectList = response.body().getAllProjectList();
                    if (projectList.size() > 0) {
                        idProject = projectList.get(0).getIdProject();
                        namaProject = projectList.get(0).getNamaProject();

                        adapterProject = new ProjectAdapter2(SearchWarehouseActivity.this, android.R.layout.simple_spinner_dropdown_item, projectList);
                        spProject.setAdapter(adapterProject);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProjectList> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                projectFailed = true;
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                onLoadingProject = false;
            }
        });
    }

    //logout method
    private void logOut() {
        DBHelper db = new DBHelper(this);
        db.clearDatabase();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchWarehouseActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_not_found_data, null);
        CustomTextView textDialog;
        textDialog = view.findViewById(R.id.text_dialog);
        textDialog.setText(message);
        textDialog.setTextSize(18);


        builder.setView(view);
        builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (gudangFailed) searchGudang();
                else if (projectFailed) searchProject();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
