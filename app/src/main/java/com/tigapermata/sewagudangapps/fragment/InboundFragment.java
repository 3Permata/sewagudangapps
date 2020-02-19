package com.tigapermata.sewagudangapps.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
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
import com.tigapermata.sewagudangapps.activity.inbound.ListIncomingActivity;
import com.tigapermata.sewagudangapps.activity.MainActivity;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.adapter.inbound.InboundAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.inbound.DataLain;
import com.tigapermata.sewagudangapps.model.inbound.Inbound;
import com.tigapermata.sewagudangapps.model.inbound.InboundDetail;
import com.tigapermata.sewagudangapps.model.inbound.InboundDetailList;
import com.tigapermata.sewagudangapps.model.inbound.InboundList;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;


public class InboundFragment extends Fragment implements InboundAdapter.InboundListener, InboundAdapter.InboundDetailListener {

    private InboundAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rvInbound;
    private List<Inbound> inboundList;
    private SearchView searchView;
    private TextView textProjectName, textNoInbound;
    private Button btnRefresh;
    private ProgressBar pbLoading;
    private View vSemiTransparent;

    //inbound detail
    ArrayList<InboundDetail> inboundDetailList;
    ArrayList<DataLain> dataLainArrayList;
    Call<InboundList> inboundCall;
    Call<InboundDetailList> inboundDetailCall;

    private boolean onInitList = false, onReqData = false;

    public static InboundFragment newInstance() {
        InboundFragment fragment = new InboundFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inboundList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbound, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar_list_inbound);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Inbound");
        setHasOptionsMenu(true);

        rvInbound = rootView.findViewById(R.id.rvInbound);
        textProjectName = rootView.findViewById(R.id.textProjectName);
        textNoInbound = rootView.findViewById(R.id.textNoInbound);
        btnRefresh = rootView.findViewById(R.id.buttonRefresh);
        pbLoading = rootView.findViewById(R.id.progressBarLoading);
        vSemiTransparent = rootView.findViewById(R.id.viewSemiTransparent);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRefresh();
                initList();
            }
        });

        initList();

        return rootView;
    }

    @Override
    public void onInboundCLick(Inbound inbound, int position) {
        AppController.getInstance().setIdInbound(inbound.getIdInbound());
        AppController.getInstance().setNoInbound(inbound.getNoInbound());
        AppController.getInstance().setReferensi(inbound.getReferensi());

        if (getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).startListIncomingAct();
        }
        searchView.setQuery("", false);
    }

    private void initList() {
        onLoading(); onInitList = true;
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        inboundCall = service.listInbound(idGudang, idProject, idUser, token);
        inboundCall.enqueue(new Callback<InboundList>() {
            @Override
            public void onResponse(Call<InboundList> call, Response<InboundList> response) {
                endLoading(); onInitList = false;
                if (response.isSuccessful()) {
                    if (response.body().getInboundList() == null) {
                        showRefresh();
                    } else if (response.body().getInboundList().size() == 0) {
                        showRefresh();
                    }
                    else {
                        inboundList = response.body().getInboundList();
                        //Log.e("inbound", String.valueOf(response.body().getInboundList().size()));
                        rvInbound.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getContext());
                        rvInbound.setLayoutManager(mLayoutManager);
                        mAdapter = new InboundAdapter(getContext(), inboundList, InboundFragment.this, InboundFragment.this);
                        rvInbound.setAdapter(mAdapter);
                    }

                } else {
                    showRefresh();
                    Toast.makeText(getContext(), "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InboundList> call, Throwable t) {
                endLoading(); showRefresh();
                t.printStackTrace(); onInitList = false;
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
            }
        });

        textProjectName.setText(dbHelper.getIds().getNamaProject());
        AppController.getInstance().setNamaProject(textProjectName.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (onInitList) inboundCall.cancel();
        if (onReqData) inboundDetailCall.cancel();
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
    public void onDetailInboundClick(Inbound inbound, int position) {
        requestDetailInbound(inbound.getIdInbound());
    }

    private void requestDetailInbound(final String idInbound) {
        onLoading(); onReqData = true;
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        inboundDetailCall = service.listDetailInbound(idGudang, idProject, idInbound, idUser, token);
        inboundDetailCall.enqueue(new Callback<InboundDetailList>() {
            @Override
            public void onResponse(Call<InboundDetailList> call, Response<InboundDetailList> response) {
                endLoading(); onReqData = false;
                if (response.isSuccessful()) {
                    inboundDetailList = response.body().getInboundDetails();
                    dataLainArrayList = response.body().getDataLainArrayList();
                    showDetailInbound();
                } else {
                    Toast.makeText(getContext(), "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InboundDetailList> call, Throwable t) {
                endLoading(); onReqData = false;
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please Check your Internet Connection");
            }
        });
    }

    private void showDetailInbound() {
        DialogFragment frag = InboundDetailFragment.newInstance(inboundDetailList, dataLainArrayList);
        pbLoading.setVisibility(View.INVISIBLE); vSemiTransparent.setVisibility(View.INVISIBLE);
        frag.show(getFragmentManager(), "detailInbound");
    }

    public void refreshData() {
        initList();
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
        textNoInbound.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        if (inboundList != null)
            if (inboundList.size() > 0) {
                int size = inboundList.size();
                inboundList.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
            }
    }

    private void hideRefresh() {
        textNoInbound.setVisibility(View.INVISIBLE);
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
