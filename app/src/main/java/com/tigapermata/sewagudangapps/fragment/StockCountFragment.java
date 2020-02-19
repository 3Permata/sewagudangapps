package com.tigapermata.sewagudangapps.fragment;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.MainActivity;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.adapter.stockcount.MasterStockCountAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.stockcount.AddMasterStockCountResponse;
import com.tigapermata.sewagudangapps.model.stockcount.MasterStockCount;
import com.tigapermata.sewagudangapps.model.stockcount.StockCountList;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class StockCountFragment extends Fragment implements MasterStockCountAdapter.StockcountByItemListener, MasterStockCountAdapter.StockcountByLabelListener {

    private MasterStockCountAdapter stockCountAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rvStockcount;
    private ArrayList<MasterStockCount> stockCountArrayList;

    TextView tvName, tStockcountNull, tTanggal;
    ProgressBar pbLoading;
    View vSemiTransparent;
    Button btnRefresh, btnAddMasterStockcount;
    SearchView searchView;

    String namaProject;
    Boolean onInitList = false, onReqAdd = false;
    Call<StockCountList> callStockcountList;
    Call<AddMasterStockCountResponse> callAddStockcount;

    public static StockCountFragment newInstance() {
        StockCountFragment fragment = new StockCountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        namaProject = AppController.getInstance().getNamaProject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stockcount, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar_list_stock_count);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Stock Count");
        setHasOptionsMenu(true);

        rvStockcount = rootView.findViewById(R.id.rv_stockcount);
        tvName = rootView.findViewById(R.id.namaProject_stockcount);
        tvName.setText(namaProject);
       //btnAddMasterStockcount = rootView.findViewById(R.id.fab_list_stockcount);

        pbLoading = rootView.findViewById(R.id.progressBarLoading);
        vSemiTransparent = rootView.findViewById(R.id.viewSemiTransparent);
        tStockcountNull = rootView.findViewById(R.id.textNoStockCount);
        btnRefresh = rootView.findViewById(R.id.buttonRefresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
                hideRefresh();
            }
        });

        FloatingActionButton fab = rootView.findViewById(R.id.fab_list_stockcount);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertAddMasterStockCount();
            }
        });

        initList();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_info , menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (stockCountAdapter != null)
                    stockCountAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (stockCountAdapter != null)
                    stockCountAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_home) {
            View menuItemView = getActivity().findViewById(R.id.action_home);
            showPopUpMenu(menuItemView);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (onInitList) callStockcountList.cancel();
        if (onReqAdd) callAddStockcount.cancel();
    }

    private void initList() {
        onLoading(); onInitList = true;
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        callStockcountList = service.showListStockCount(idGudang, idProject, idUser, token);
        callStockcountList.enqueue(new Callback<StockCountList>() {
            @Override
            public void onResponse(Call<StockCountList> call, Response<StockCountList> response) {
                endLoading(); onInitList = false;
                if (response.isSuccessful()) {
                    if (response.body().getStockCountArrayList() == null) {
                        showRefresh();
                    }
                    else if (response.body().getStockCountArrayList().size() == 0) {
                        showRefresh();
                    }
                    else {
                        stockCountArrayList = response.body().getStockCountArrayList();
                        rvStockcount.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getContext());
                        rvStockcount.setLayoutManager(mLayoutManager);
                        stockCountAdapter = new MasterStockCountAdapter(getContext(), stockCountArrayList, StockCountFragment.this, StockCountFragment.this);
                        rvStockcount.setAdapter(stockCountAdapter);
                    }
                }
                else {
                    showRefresh();
                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StockCountList> call, Throwable t) {
                t.printStackTrace(); onInitList = false;
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                endLoading(); showRefresh();
            }
        });
    }

    private void requestAddMasterStockcount(String tanggal) {
        onLoading(); onReqAdd = true;
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        callAddStockcount = service.addMasterStockCount(idGudang, idProject, idUser, token, tanggal);
        callAddStockcount.enqueue(new Callback<AddMasterStockCountResponse>() {
            @Override
            public void onResponse(Call<AddMasterStockCountResponse> call, Response<AddMasterStockCountResponse> response) {
                endLoading(); onReqAdd = false;
                if (response.isSuccessful()) {
                    if (response.body().getStatus().matches("Data Stock Count Berhasil Di save")) {
                        Toast.makeText(getContext(), "Berhasil", Toast.LENGTH_LONG).show();
                        initList();
                    }
                    else Toast.makeText(getContext(), "Gagal", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getContext(), "Gagal", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<AddMasterStockCountResponse> call, Throwable t) {
                t.printStackTrace(); onReqAdd = false;
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                endLoading();
            }
        });
    }

    @Override
    public void onStockCountDetailByItemClick(MasterStockCount stockcount, int position) {
        AppController.getInstance().setIdStockcount(stockcount.getIdStockCount());

        if (getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).startDetailStockCountByItemActivity();
        }
        searchView.setQuery("", false);
    }

    @Override
    public void onStockCountDetailByLabelClick(MasterStockCount stockcount, int position) {
        AppController.getInstance().setIdStockcount(stockcount.getIdStockCount());

        if (getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).startDetailStockCountByLabelActivity();
        }
        searchView.setQuery("", false);
    }

    private void alertAddMasterStockCount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View itemView = inflater.inflate(R.layout.dialog_add_master_stockcount, null);
        builder.setView(itemView);

        tTanggal = itemView.findViewById(R.id.textViewTanggalStockcount);
        ImageView imgTanggal = itemView.findViewById(R.id.imageViewTanggalStockcount);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = format.format(Calendar.getInstance().getTime());
        tTanggal.setText(dateString);
        imgTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        String dateString = format.format(calendar.getTime());
                        tTanggal.setText(dateString);
                    }
                }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });

        builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestAddMasterStockcount(tTanggal.getText().toString());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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

    private void showRefresh() {
        tStockcountNull.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        if (stockCountArrayList != null)
            if (stockCountArrayList.size() > 0) {
                int size = stockCountArrayList.size();
                stockCountArrayList.clear();
                stockCountAdapter.notifyItemRangeRemoved(0, size);
            }
    }

    private void hideRefresh() {
        tStockcountNull.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
    }

    public void refreshData() {
        initList();
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppController.getInstance().getMainAct());
        LayoutInflater inflater = getLayoutInflater();
        View view  = inflater.inflate(R.layout.dialog_not_found_data, null);
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
        final PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_warehouse, popup.getMenu());

        DBHelper dbHelper = new DBHelper(getContext());
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
                        startActivity(new Intent(getContext(), SearchWarehouseActivity.class));
                        return true;
                    case R.id.action_logout:
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity)getActivity()).logout();
                            ((MainActivity)getActivity()).finishActivity();
                        }
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
