package com.tigapermata.sewagudangapps.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.MainActivity;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.activity.outbound.PickingListActivity;
import com.tigapermata.sewagudangapps.adapter.outbound.OutboundAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.outbound.Outbound;
import com.tigapermata.sewagudangapps.model.outbound.OutboundList;
import com.tigapermata.sewagudangapps.model.putaway.StatusPutAway;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class PickingFragment extends Fragment implements OutboundAdapter.OutboundListener, OutboundAdapter.OutboundDoneListener {

    private OutboundAdapter outAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rvOutbound;
    private ArrayList<Outbound> outboundArrayList;

    TextView tvName, tOutboundNull;
    Button btnRefresh;
    ProgressBar pbLoading;
    View vSemiTransparent;
    SearchView searchView;

    String namaProject, metodePicking;

    Call<OutboundList> callOutbound;
    Call<StatusPutAway> callChangeStatusPicking;

    public static PickingFragment newInstance() {
        PickingFragment fragment = new PickingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        namaProject = AppController.getInstance().getNamaProject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_picking, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar_picking);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Picking");
        setHasOptionsMenu(true);

        rvOutbound = rootView.findViewById(R.id.rv_picking);
        tvName = rootView.findViewById(R.id.textProjectName);
        tvName.setText(namaProject);

        tOutboundNull = rootView.findViewById(R.id.textNoOutbound);
        btnRefresh = rootView.findViewById(R.id.buttonRefresh);
        pbLoading = rootView.findViewById(R.id.progressBarLoading);
        vSemiTransparent = rootView.findViewById(R.id.viewSemiTransparent);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showbyPicking();
                hideRefresh();
            }
        });

        showbyPicking();

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
                if (outAdapter != null)
                    outAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (outAdapter != null)
                    outAdapter.getFilter().filter(newText);
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
    public void onOutboundClick(Outbound outbound, int position) {
        AppController.getInstance().setIdOutbound(outbound.getIdOutbound());
        AppController.getInstance().setNoOutbound(outbound.getNoOutbound());
        AppController.getInstance().setReferensi(outbound.getRefOutbound());

        if (getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).startPickingListAct(metodePicking);
        }
        searchView.setQuery("", false);
    }

    @Override
    public void onOutboundDoneClick(Outbound outbound, int position) {
        onLoading();
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();
        String idOutbound = outbound.getIdOutbound();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        callChangeStatusPicking = service.pickingToPicked(idGudang, idProject, idOutbound, idUser, token);
        callChangeStatusPicking.enqueue(new Callback<StatusPutAway>() {
            @Override
            public void onResponse(Call<StatusPutAway> call, Response<StatusPutAway> response) {
                endLoading();
                if (response.isSuccessful()) {
                    showbyPicking();
                    searchView.setQuery("", false);
                    Toast.makeText(getContext(), "Berhasil diproses", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<StatusPutAway> call, Throwable t) {
                endLoading(); t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
            }
        });
    }

    public void refreshData() {
        showbyPicking();
    }

    //show outbound list
    private void showbyPicking() {
        onLoading();
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        callOutbound = service.showbyPicking(idGudang, idProject, idUser, token);
        callOutbound.enqueue(new Callback<OutboundList>() {
            @Override
            public void onResponse(Call<OutboundList> call, Response<OutboundList> response) {
                endLoading();
                if (response.isSuccessful()) {
                    if (response.body().getOutboundArrayList() == null) {
                        showRefresh();
                    }
                    else if (response.body().getOutboundArrayList().size() == 0) {
                        showRefresh();
                    }
                    else {
                        outboundArrayList = response.body().getOutboundArrayList();
                        rvOutbound.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getContext());
                        rvOutbound.setLayoutManager(mLayoutManager);
                        outAdapter = new OutboundAdapter(getContext(), outboundArrayList, PickingFragment.this, PickingFragment.this);
                        rvOutbound.setAdapter(outAdapter);
                        metodePicking = response.body().getMetode();
                    }
                }
                else {
                    showRefresh();
                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OutboundList> call, Throwable t) {
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
                endLoading(); showRefresh();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        callOutbound.cancel();
        if (callChangeStatusPicking != null)
            callChangeStatusPicking.cancel();
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
        tOutboundNull.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        if (outboundArrayList != null)
            if (outboundArrayList.size() > 0) {
                int size = outboundArrayList.size();
                outboundArrayList.clear();
                outAdapter.notifyItemRangeRemoved(0, size);
            }
    }

    private void hideRefresh() {
        tOutboundNull.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
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


