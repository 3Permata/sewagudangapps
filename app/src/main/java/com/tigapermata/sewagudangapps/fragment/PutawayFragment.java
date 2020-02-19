package com.tigapermata.sewagudangapps.fragment;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.LoginActivity;
import com.tigapermata.sewagudangapps.activity.MainActivity;
import com.tigapermata.sewagudangapps.activity.SearchWarehouseActivity;
import com.tigapermata.sewagudangapps.activity.putaway.ScanPutAwayActivity;
import com.tigapermata.sewagudangapps.adapter.putaway.DataFilterByItemAdapter;
import com.tigapermata.sewagudangapps.adapter.putaway.DataFilterByLabelAdapter;
import com.tigapermata.sewagudangapps.adapter.putaway.DataLocatorAdapter;
import com.tigapermata.sewagudangapps.adapter.putaway.DataPutAwayAdapter;
import com.tigapermata.sewagudangapps.adapter.putaway.DataSearchedAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.putaway.DataFilterByItem;
import com.tigapermata.sewagudangapps.model.putaway.DataFilterByItemList;
import com.tigapermata.sewagudangapps.model.putaway.DataFilterByLabel;
import com.tigapermata.sewagudangapps.model.putaway.DataFilterByLabelList;
import com.tigapermata.sewagudangapps.model.putaway.DataLocator;
import com.tigapermata.sewagudangapps.model.putaway.DataLocatorList;
import com.tigapermata.sewagudangapps.model.putaway.DataPutAway;
import com.tigapermata.sewagudangapps.model.putaway.DataSearched;
import com.tigapermata.sewagudangapps.model.putaway.DataSearchedList;
import com.tigapermata.sewagudangapps.model.putaway.StatusPutAway;
import com.tigapermata.sewagudangapps.utils.CustomButton;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class PutawayFragment extends Fragment implements DataSearchedAdapter.ItemListener {

    private TextView projectName;
    private AutoCompleteTextView acItem, acNewLocator;
    private Button btnNA, btnClearItem, btnClearNewLocator;
    private ImageButton ibScanNewLocator, ibScanItem;
    private RadioButton rbLabel, rbItem;
    private View vSemiTransparent;
    private ProgressBar pbLoading;
    private final String idNA = "71", nameNA = "n/a";

    //locator
    private ArrayList<DataLocator> dataLocatorArrayList;
    private DataLocatorAdapter locatorAdapter;
    Call<DataLocatorList> dataLocatorCall;

    //filter by item
    private ArrayList<DataFilterByItem> dataFilterByItemArrayList;
    private DataFilterByItemAdapter byItemAdapter;
    Call<DataFilterByItemList> dataItemCall;

    //filter by label
    private ArrayList<DataFilterByLabel> dataFilterByLabelArrayList;
    private DataFilterByLabelAdapter byLabelAdapter;
    Call<DataFilterByLabelList> dataLabelCall;

    //searched item
    private ArrayList<DataSearched> dataSearchedArrayList;
    private DataSearchedAdapter searchedAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rvPutaway;
    Call<DataSearchedList> searchedCall;

    String newLocator, idLabel, idItem, qtyAktual, idInventory, filters,
            namaItem, newLocatorName;
    boolean scanOldLocator, scanNewLocator, scanItem, byLabel = false;
    boolean onReqLocator = false, onReqItem = false, onReqLabel = false, onSearchItem = false, onMoveItem = false;

    View view, itemView;
    Call<StatusPutAway> moveCall;

    public static PutawayFragment newInstance() {
        PutawayFragment fragment = new PutawayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_putaway, container, false);
        DBHelper dbHelper = new DBHelper(getContext());

        Toolbar toolbar = rootView.findViewById(R.id.toolbar_putaway);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Put Away");
        setHasOptionsMenu(true);

        //text
        projectName = rootView.findViewById(R.id.textProjectName);
        projectName.setText(dbHelper.getIds().getNamaProject());

        //autocomplete text view for data locator
        acNewLocator = rootView.findViewById(R.id.nama_new_locator_put_away);
        acNewLocator.setThreshold(1);
        acNewLocator.setOnItemClickListener(onNewLocatorClick);
        acNewLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acNewLocator.showDropDown();
            }
        });
        acNewLocator.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                initPutAway();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        acNewLocator.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    findNewLocator(acNewLocator.getText().toString());
                    return true;
                }
                return false;
            }
        });
        acNewLocator.requestFocus();

        //autocomplete for data item/label
        acItem = rootView.findViewById(R.id.nama_item_put_away);
        acItem.setThreshold(1);
        acItem.setOnItemClickListener(onItemClickListener);
        acItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acItem.showDropDown();
            }
        });
        acItem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    findLabelItem(acItem.getText().toString());
                    return true;
                }
                return false;
            }
        });

        //image button
        ibScanNewLocator = rootView.findViewById(R.id.btnLocatorBaru);
        ibScanNewLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(PutawayFragment.this).setCaptureActivity(ScanPutAwayActivity.class).initiateScan();
                scanNewLocator = true;
            }
        });
        ibScanItem = rootView.findViewById(R.id.btnLabel);
        ibScanItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(PutawayFragment.this).setCaptureActivity(ScanPutAwayActivity.class).initiateScan();
                scanItem = true;
            }
        });

        btnNA = rootView.findViewById(R.id.btnNA);
        btnNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acNewLocator.setText(nameNA);
                acItem.setEnabled(true); ibScanItem.setEnabled(true);
                acItem.requestFocus();

                newLocator= idNA;
                newLocatorName = nameNA;
                if (byLabel) filterByLabel();
                else filterByItem();
            }
        });

        btnClearNewLocator = rootView.findViewById(R.id.btnClearNewLocator);
        btnClearNewLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acNewLocator.setText("");
                acNewLocator.requestFocus();
                initPutAway();
            }
        });
        btnClearItem = rootView.findViewById(R.id.btnClearItem);
        btnClearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acItem.setText("");
                acItem.requestFocus();
            }
        });

        rbLabel = rootView.findViewById(R.id.radioButtonLabel);
        rbItem = rootView.findViewById(R.id.radioButtonItem);

        rbLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byLabel = true;
                acItem.setText("");
                acItem.setOnItemClickListener(onLabelClickListener);
                rbItem.setChecked(false);
                filterByLabel();
            }
        });

        rbItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byLabel = false;
                acItem.setText("");
                acItem.setOnItemClickListener(onItemClickListener);
                rbLabel.setChecked(false);
                filterByItem();
            }
        });

        rvPutaway = rootView.findViewById(R.id.rv_putaway);

        vSemiTransparent = rootView.findViewById(R.id.viewSemiTransparent);
        vSemiTransparent.setVisibility(View.INVISIBLE);
        pbLoading = rootView.findViewById(R.id.progressBarLoading);
        pbLoading.setVisibility(View.INVISIBLE);

        requestDataLocator();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                scanItem = false; scanNewLocator = false; scanOldLocator = false;
            } else {
                if (scanNewLocator) {
                    acNewLocator.setText(result.getContents());
                    findNewLocator(result.getContents());
                    scanNewLocator = false;
                }
                else if (scanItem) {
                    acItem.setText(result.getContents());
                    findLabelItem(result.getContents());
                    scanItem = false;
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (onReqLocator) dataLocatorCall.cancel();
        if (onReqLabel) dataLabelCall.cancel();
        if (onReqItem) dataItemCall.cancel();
        if (onSearchItem) searchedCall.cancel();
        if (onMoveItem) moveCall.cancel();
    }


    @Override
    public void onItemClick(final DataSearched item, int position) {
        final int qty = Integer.parseInt(item.getQty());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        itemView = inflater.inflate(R.layout.dialog_putaway_move, null);
        builder.setView(itemView);

        TextView tQty = itemView.findViewById(R.id.qty);
        final EditText etQtyMove = itemView.findViewById(R.id.qty_move);

        tQty.setText(qty + "");
        etQtyMove.setText(qty + "");
        etQtyMove.requestFocus();
        etQtyMove.setSelection(etQtyMove.getText().length());

        builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String qtyMove = etQtyMove.getText().toString();
                int qty_move = Integer.parseInt(qtyMove);
                if (qty_move > qty || qty_move < 0) {
                    AlertSuccess("Data cannot be processed");
                } else {
                    String idLocatorLama = item.getIdLocator();
                    String idInventoryDetail = item.getIdInventoryDetail();
                    moveItem(idLocatorLama, idInventoryDetail, qtyMove);
                    //searchedAdapter.notifyDataSetChanged();
                }
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

    //on auto complete click new locator
    private AdapterView.OnItemClickListener onNewLocatorClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DataLocator data = locatorAdapter.getItem(position);
            newLocator = data.getIdLocator();
            newLocatorName = data.getNamaLocator();
            newLocatorFound();
        }
    };

    //on auto complete click item
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DataFilterByItem data = byItemAdapter.getItem(position);
            idItem = data.getIdItem();
            namaItem = data.getNamaItem();
            qtyAktual = data.getQty();
            idInventory = data.getIdInventoryDetail();
            searchItem();
        }
    };

    //on auto complete click label
    private AdapterView.OnItemClickListener onLabelClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DataFilterByLabel data = byLabelAdapter.getItem(position);
            idLabel = data.getIdItem();
            namaItem = data.getLabel();
            qtyAktual = data.getQty();
            idInventory = data.getIdInventoryDetail();
            searchLabel();
        }
    };

    //locator list request
    private void requestDataLocator() {
        onReqLocator = true;
        onLoading();
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        dataLocatorCall = service.showLocator(idGudang, idProject, idUser, token);
        dataLocatorCall.enqueue(new Callback<DataLocatorList>() {
            @Override
            public void onResponse(Call<DataLocatorList> call, Response<DataLocatorList> response) {
                if (response.isSuccessful()) {
                    dataLocatorArrayList = response.body().getDataLocatorArrayList();
                    locatorAdapter = new DataLocatorAdapter(getContext(), R.layout.detail_autocomplete_layout, dataLocatorArrayList);
                    acNewLocator.setAdapter(locatorAdapter);
                    onReqLocator = false;
                    endLoading();
                    initPutAway();
                }
            }

            @Override
            public void onFailure(Call<DataLocatorList> call, Throwable t) {
                onReqLocator = false;
                t.printStackTrace();
                endLoading();
                if (!call.isCanceled())
                    AlertSuccess("Check your internet connection");
            }
        });
    }

    //request list by item
    private void filterByItem() {
        onLoading();
        onReqItem = true;
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        dataItemCall = service.showFilterByItem(idGudang, idProject, idUser, token);
        dataItemCall.enqueue(new Callback<DataFilterByItemList>() {
            @Override
            public void onResponse(Call<DataFilterByItemList> call, Response<DataFilterByItemList> response) {
                endLoading();
                if (response.isSuccessful()) {
                    dataFilterByItemArrayList = response.body().getDataFilterByItems();
                    if (dataFilterByItemArrayList == null) {
                        AlertSuccess("Sorry, no item here");
                    } else {
                        byItemAdapter = new DataFilterByItemAdapter(getContext(), R.layout.detail_autocomplete_layout, dataFilterByItemArrayList);
                        acItem.setAdapter(byItemAdapter);
                        acItem.requestFocus();
                    }
                    onReqItem = false;
                }
            }

            @Override
            public void onFailure(Call<DataFilterByItemList> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please check your internet connection");
            }
        });
    }

    //request list by label
    private void filterByLabel() {
        onLoading();
        onReqLabel = true;
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        dataLabelCall = service.showFilterByLabel(idGudang, idProject, idUser, token);
        dataLabelCall.enqueue(new Callback<DataFilterByLabelList>() {
            @Override
            public void onResponse(Call<DataFilterByLabelList> call, Response<DataFilterByLabelList> response) {
                endLoading();
                if (response.isSuccessful()) {
                    dataFilterByLabelArrayList = response.body().getDataFilterByLabels();
                    if (dataFilterByLabelArrayList == null) {
                        AlertSuccess("Sorry, no label here");
                    } else {
                        byLabelAdapter = new DataFilterByLabelAdapter(getContext(), R.layout.detail_autocomplete_layout, dataFilterByLabelArrayList);
                        acItem.setAdapter(byLabelAdapter);
                        acItem.requestFocus();
                    }
                    onReqLabel = false;
                }
            }

            @Override
            public void onFailure(Call<DataFilterByLabelList> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                if (!call.isCanceled())
                    AlertSuccess("Please check your internet connection");
            }
        });
    }

    //search by item
    private void searchItem() {
        onLoading();
        onSearchItem = true;
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        searchedCall = service.cariDataItemByItem(idGudang, idProject, idUser, token, idItem);
        searchedCall.enqueue(new Callback<DataSearchedList>() {
            @Override
            public void onResponse(Call<DataSearchedList> call, Response<DataSearchedList> response) {
                endLoading(); onSearchItem = false;
                if (response.isSuccessful()) {
                    dataSearchedArrayList = response.body().getDataSearched();
                    mLayoutManager = new LinearLayoutManager(getContext());
                    rvPutaway.setLayoutManager(mLayoutManager);
                    searchedAdapter = new DataSearchedAdapter(getContext(), dataSearchedArrayList, PutawayFragment.this);
                    rvPutaway.setAdapter(searchedAdapter);
                }
                else {
                    Toast.makeText(getContext(), "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataSearchedList> call, Throwable t) {
                t.printStackTrace();
                endLoading(); onSearchItem = false;
                if (!call.isCanceled())
                    AlertSuccess("Please check your internet connection");
            }
        });
    }

    //search by item
    private void searchLabel() {
        onLoading();
        onSearchItem = true;
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        searchedCall = service.cariDataItemByLabel(idGudang, idProject, idUser, token, namaItem);
        searchedCall.enqueue(new Callback<DataSearchedList>() {
            @Override
            public void onResponse(Call<DataSearchedList> call, Response<DataSearchedList> response) {
                endLoading(); onSearchItem = false;
                if (response.isSuccessful()) {
                    dataSearchedArrayList = response.body().getDataSearched();
                    mLayoutManager = new LinearLayoutManager(getContext());
                    rvPutaway.setLayoutManager(mLayoutManager);
                    searchedAdapter = new DataSearchedAdapter(getContext(), dataSearchedArrayList, PutawayFragment.this);
                    rvPutaway.setAdapter(searchedAdapter);
                }
                else {
                    Toast.makeText(getContext(), "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataSearchedList> call, Throwable t) {
                t.printStackTrace();
                endLoading(); onSearchItem = false;
                if (!call.isCanceled())
                    AlertSuccess("Please check your internet connection");
            }
        });
    }

    private void moveItem(String idLocatorLama, String idInventoryDetail, String qty) {
        onLoading(); onMoveItem = true;
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        moveCall = service.locatorMove(idGudang, idProject, idUser, token, newLocator, idLocatorLama, idInventoryDetail, qty);
        moveCall.enqueue(new Callback<StatusPutAway>() {
            @Override
            public void onResponse(Call<StatusPutAway> call, Response<StatusPutAway> response) {
                endLoading(); onMoveItem = false;
                if (response.isSuccessful()) {
                    if (response.body().getStatus().matches("Item Berhasil Di Move!")) {
                        Toast.makeText(getContext(), "berhasil diproses", Toast.LENGTH_SHORT).show();
                        if (byLabel) searchLabel();
                        else searchItem();
                    }
                    else Toast.makeText(getContext(), "gagal", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusPutAway> call, Throwable t) {
                t.printStackTrace();
                endLoading(); onMoveItem = false;
                if (!call.isCanceled())
                    AlertSuccess("Please check your internet connection");
            }
        });
    }

    /*
    //move locator method
    private void moveItem() {
        onMoveItem = true;
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();
        final DataPutAway data = new DataPutAway(oldLocatorName, filters, namaItem, newLocatorName);

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusPutAway> sendCall = service.putAwayFunc(idGudang, idProject, idUser, token, oldLocator, newLocator, etQty.getText().toString(), idItem, idInventory);
        sendCall.enqueue(new Callback<StatusPutAway>() {
            @Override
            public void onResponse(Call<StatusPutAway> call, Response<StatusPutAway> response) {
                endLoading();
                if (response.isSuccessful()) {
                    initPutAway();
                    oldLocatorFound();
                    DBHelper dbHelper = new DBHelper(getContext());
                    dbHelper.addPutAway(data);
                    AlertSuccess("Item moved successfully");
                    onMoveItem = false;
                } else {
                    Toast.makeText(getContext(), "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusPutAway> call, Throwable t) {
                t.printStackTrace();
                endLoading();
                if (!call.isCanceled())
                    AlertSuccess("Please check your internet connection");
            }
        });
    }
     */

    private void initPutAway() {
        acNewLocator.requestFocus();
        acItem.setEnabled(false);
        acItem.setText("");
        ibScanItem.setEnabled(false);
    }

    private void onLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);

        acItem.setEnabled(false);
        acNewLocator.setEnabled(false);
        btnClearItem.setEnabled(false);
        btnClearNewLocator.setEnabled(false);
        btnNA.setEnabled(false);
        ibScanItem.setEnabled(false);
        ibScanNewLocator.setEnabled(false);
        rbItem.setEnabled(false);
        rbLabel.setEnabled(false);
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);

        acItem.setEnabled(true);
        acNewLocator.setEnabled(true);
        btnClearItem.setEnabled(true);
        btnClearNewLocator.setEnabled(true);
        btnNA.setEnabled(true);
        ibScanItem.setEnabled(true);
        ibScanNewLocator.setEnabled(true);
        rbItem.setEnabled(true);
        rbLabel.setEnabled(true);
    }

    private void findNewLocator(String s) {
        int index = locatorAdapter.findItem(s);
        if (index != -1) {
            DataLocator d = locatorAdapter.getItemFromAll(index);
            newLocator = d.getIdLocator();
            newLocatorName = d.getNamaLocator();
            newLocatorFound();
        }
        else {
            acNewLocator.setSelection(acNewLocator.getText().length());
            Toast.makeText(getContext(), "Locator tidak ditemukan", Toast.LENGTH_LONG).show();
        }
    }

    private void findLabelItem(String s) {
        if (byLabelAdapter != null) {
            int index = byLabelAdapter.findItem(s);
            if (index != -1) {
                DataFilterByLabel d = byLabelAdapter.getItemFromAll(index);
                idLabel = d.getIdItem();
                namaItem = d.getLabel();
                qtyAktual = d.getQty();
                idInventory = d.getIdInventoryDetail();
                searchLabel();
            }
            else {
                acItem.setSelection(acItem.getText().length());
                Toast.makeText(getContext(), "Label item tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            acItem.setSelection(acItem.getText().length());
            Toast.makeText(getContext(), "Item tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

    }

    private void newLocatorFound() {
        ibScanItem.setEnabled(true);
        acItem.setEnabled(true);
        acItem.requestFocus();

        if (byLabel) filterByLabel();
        else filterByItem();
    }

    /*
    //checking the label
    private void checkLabel(final String label) {
        DBHelper dbHelper = new DBHelper(getContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusPutAway> checkLabel = service.checkLabelPutAway(idGudang, idProject, idUser, token, oldLocator, label);
        checkLabel.enqueue(new Callback<StatusPutAway>() {
            @Override
            public void onResponse(Call<StatusPutAway> call, Response<StatusPutAway> response) {
                if (response.isSuccessful()) {
                    itemFound();
                } else if (response.code() == 500) {
                    AlertSuccess("Sorry, Label not found");
                }
            }

            @Override
            public void onFailure(Call<StatusPutAway> call, Throwable t) {
                t.printStackTrace();
                AlertSuccess("Please check your internet connection");
            }
        });
    }


    //alert for moving
    private void putAwayAlert() {
        View itemView;
        CustomTextView tvQtyDokumen;
        final CustomEditText tvQtyAktual;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        itemView = inflater.inflate(R.layout.dialog_put_away, null);
        builder.setView(itemView);

        tvQtyDokumen = itemView.findViewById(R.id.qty_document_put_away);
        tvQtyAktual = itemView.findViewById(R.id.qty_aktual_put_away);
        tvQtyDokumen.setText(qtyAktual);
        tvQtyAktual.setText(qtyAktual);

        builder.setPositiveButton("MOVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int qtyOld = Integer.valueOf(qtyAktual);
                newQty = tvQtyAktual.getText().toString();
                final int qtyNew = Integer.valueOf(newQty);
                if (qtyNew > qtyOld) {
                    AlertSuccess("Data cannot be processed");
                } else if (qtyNew == qtyOld) {
                    moveItem();
                } else {
                    moveItem();
                }

            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }*/

    //alert for everything
    private void AlertSuccess(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                if (message.equals("Check your internet connection")) {
                    onLoading();
                    requestDataLocator();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.history_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            View menuItemView = getActivity().findViewById(R.id.action_home);
            showPopUpMenu(menuItemView);
        }
        else if (id == R.id.action_history) {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity)getActivity()).startHistoryPutAwayAct();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //clear database
    private void clearDataPutaway() {
        DBHelper db = new DBHelper(getContext());
        db.clearPutAway();
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
                        clearDataPutaway();
                        return true;
                    case R.id.action_logout:
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity)getActivity()).logout();
                            ((MainActivity)getActivity()).finishActivity();
                        }
                        clearDataPutaway();
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
