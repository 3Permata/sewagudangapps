package com.tigapermata.sewagudangapps.activity.inbound;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.adapter.inbound.AutocompleteItemAdapter;
import com.tigapermata.sewagudangapps.adapter.inbound.FormPackingListAdapter;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.inbound.AutocompleteItem;
import com.tigapermata.sewagudangapps.model.inbound.AutocompleteItemList;
import com.tigapermata.sewagudangapps.model.inbound.DataLain;
import com.tigapermata.sewagudangapps.model.inbound.EditPacking;
import com.tigapermata.sewagudangapps.model.inbound.FormPacking;
import com.tigapermata.sewagudangapps.model.inbound.FormPackingList;
import com.tigapermata.sewagudangapps.model.inbound.RawFormPackinglist;
import com.tigapermata.sewagudangapps.model.inbound.StatusOK;
import com.tigapermata.sewagudangapps.utils.CustomButton;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemIncomingActivity extends AppCompatActivity {

    private ImageButton addMasterItem;
    private ImageView imgCalendar;
    private CheckBox cbGenerated;
    private TextView labelsGenerated, tvUom, etExpiredDate, tvQtyPerPck;
    private EditText etActualWeight, etQty, etQtyPck, etActualCBM, etLabel, etBatch;
    private Button submitBtn;
    String idInbound;

    //autocomplete
    AutoCompleteTextView atItemName;
    AutocompleteItemAdapter itemAdapter;
    private ArrayList<AutocompleteItem> autocompleteItemList;

    //packinglist
    RecyclerView rvPackinglist;
    private ArrayList<FormPacking> formPackingArrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    private FormPackingListAdapter packingAdapter;

    String mIdItem, mKodeItem, mNamaItem, mUOM, mActualWeight, mActualCBM, labels, textAlert;
    boolean itemSelected, initReqFailed, onSendData, onInitLoading;
    int numberOfRequest;

    View view, vSemiTransparent;
    ProgressBar pbLoading;

    Call<AutocompleteItemList> itemListCall;
    Call<FormPackingList> packingListCall;

    private DatePickerDialog datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_incoming);

        Toolbar toolbar = findViewById(R.id.toolbar_add_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Item");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //call database
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        idInbound = dbHelper.getIdInbound().getIdInbound();

        onInitLoading = true;
        requestItem();

        //checkbox
        cbGenerated = findViewById(R.id.cb_create_package);

        //image button
        addMasterItem = findViewById(R.id.add_master_item);

        //image view
        imgCalendar = findViewById(R.id.imageViewCalendar);

        //textview
        labelsGenerated = findViewById(R.id.label_generated);
        tvUom = findViewById(R.id.uom);
        etExpiredDate = findViewById(R.id.expired_date);
        tvQtyPerPck = findViewById(R.id.textQtyPerPck);

        //button
        submitBtn = findViewById(R.id.submit);

        //edittext
        etActualWeight = findViewById(R.id.actual_weight);
        etQty = findViewById(R.id.qty_add_item);
        etQtyPck = findViewById(R.id.qty_pkg);
        etActualCBM = findViewById(R.id.actual_cbm);
        etLabel = findViewById(R.id.label_add_item);
        etBatch = findViewById(R.id.batch);

        //autocomplete text view
        atItemName = findViewById(R.id.id_item);
        atItemName.setThreshold(1);
        atItemName.setOnItemClickListener(onItemClickListener);
        atItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atItemName.showDropDown();
            }
        });
        atItemName.requestFocus();

        //recycler view
        rvPackinglist = findViewById(R.id.rv_packing_list);

        vSemiTransparent = findViewById(R.id.viewSemiTransparent);
        pbLoading = findViewById(R.id.progressBarLoading);

        init();

        atItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemSelected = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbGenerated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etQtyPck.setEnabled(true);
                    tvQtyPerPck.setVisibility(View.VISIBLE);
                    labelsGenerated.setVisibility(View.VISIBLE);
                    etLabel.setVisibility(View.INVISIBLE);
                } else {
                    etQtyPck.setEnabled(false);
                    tvQtyPerPck.setVisibility(View.INVISIBLE);
                    labelsGenerated.setVisibility(View.INVISIBLE);
                    etLabel.setVisibility(View.VISIBLE);
                }
            }
        });

        addMasterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddItemIncomingActivity.this, AddMasterItemActivity.class), 10);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AddItemIncomingActivity.this, ListIncomingActivity.class));
                checkingField();
            }
        });

        //text view expired date for calendar
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = format.format(Calendar.getInstance().getTime());
        etExpiredDate.setText(dateString);
        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePicker = new DatePickerDialog(AddItemIncomingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth){
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                String dateString = format.format(calendar.getTime());
                                etExpiredDate.setText(dateString);
                            }
                        }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });

        itemSelected = false; initReqFailed = false; onSendData = false;
        numberOfRequest = 2;
        onLoading();
    }

    //request for checking another data on form packing list
    private void init() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        packingListCall = service.packinglistCheck(idGudang, idProject, idInbound, idUser, token);
        packingListCall.enqueue(new Callback<FormPackingList>() {
            @Override
            public void onResponse(Call<FormPackingList> call, Response<FormPackingList> response) {
                numberOfRequest--;
                if (numberOfRequest == 0) {
                    endLoading(); onInitLoading = false;
                }
                if (response.isSuccessful()) {
                    formPackingArrayList =  response.body().getFormPackingArrayList();
                    rvPackinglist.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rvPackinglist.setLayoutManager(mLayoutManager);
                    packingAdapter = new FormPackingListAdapter(AddItemIncomingActivity.this, formPackingArrayList, populateList());
                    rvPackinglist.setAdapter(packingAdapter);
                }
                else initReqFailed = true;
            }

            @Override
            public void onFailure(Call<FormPackingList> call, Throwable t) {
                numberOfRequest--;
                initReqFailed = true;
                if (numberOfRequest == 0) {
                    endLoading(); onInitLoading = false;
                    textAlert = "Refresh";
                    if (!call.isCanceled())
                        AlertSuccess("Please Check your Internet Connection");
                }
                t.printStackTrace();
            }
        });
    }

    //request item for autocomplete text view
    private void requestItem() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        final String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        itemListCall = service.autoCompleteItemIncoming(idGudang, idProject, idInbound, idUser, token);
        itemListCall.enqueue(new Callback<AutocompleteItemList>() {
            @Override
            public void onResponse(Call<AutocompleteItemList> call, Response<AutocompleteItemList> response) {
                numberOfRequest--;
                if (numberOfRequest == 0) {
                    endLoading(); onInitLoading = false;
                }
                if (response.isSuccessful()) {
                    autocompleteItemList = response.body().getDataItems();
                    itemAdapter = new AutocompleteItemAdapter(AddItemIncomingActivity.this, R.layout.detail_autocomplete_layout, autocompleteItemList);
                    atItemName.setAdapter(itemAdapter);
                }
                else initReqFailed = true;
            }

            @Override
            public void onFailure(Call<AutocompleteItemList> call, Throwable t) {
                numberOfRequest--;
                initReqFailed = true;
                if (numberOfRequest == 0) {
                    endLoading(); onInitLoading = false;
                    textAlert = "Refresh";
                    if (!call.isCanceled())
                        AlertSuccess("Please Check your Internet Connection");
                }
                t.printStackTrace();
            }
        });
    }

    //send data form packing list
    private void sendDataPackinglist() {
        onSendData = true;
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String packages, qtyPackages;

        //path
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();
        String idInbound = dbHelper.getIdInbound().getIdInbound();

        //input data
        String idUser = dbHelper.getTokenn().getIdUser();
        String token = dbHelper.getTokenn().getToken();
        String qty = etQty.getText().toString();
        if (cbGenerated.isChecked()) {
            packages = "1";
            qtyPackages = etQtyPck.getText().toString();
            labels = "";
        } else {
            packages = "0";
            qtyPackages = "0";
            labels = etLabel.getText().toString();
        }

        String batch = etBatch.getText().toString();
        String expiredDate = etExpiredDate.getText().toString();

        RawFormPackinglist rawFormPackinglist = new RawFormPackinglist(idUser,token, mIdItem, qty, packages,
                qtyPackages, labels, batch, expiredDate, mActualWeight, mActualCBM, postList());
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<StatusOK> addPackinglist = service.addPackinglist(idGudang, idProject, idInbound, rawFormPackinglist);
        addPackinglist.enqueue(new Callback<StatusOK>() {
            @Override
            public void onResponse(Call<StatusOK> call, Response<StatusOK> response) {
                endLoading();
                onSendData = false;
                if (response.isSuccessful()) {
                    setResult(10);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusOK> call, Throwable t) {
                endLoading();
                onSendData = false;
                t.printStackTrace();
                textAlert = "OK";
                AlertSuccess("Please Check your Internet Connection");
            }
        });

    }

    //show list of custom list label
    private ArrayList<EditPacking> populateList() {
        ArrayList<EditPacking> list = new ArrayList<>();
        if (formPackingArrayList == null) {

        } else {
            for (int i = 0; i<formPackingArrayList.size(); i++) {
                EditPacking editPacking = new EditPacking();
                editPacking.setEditTextValue(String.valueOf(i));
                list.add(editPacking);
            }
        }
        return list;
    }

    //get data from custom list label
    private ArrayList<DataLain> postList() {
        ArrayList<DataLain> list = new ArrayList<>();
        if (formPackingArrayList == null) {
            DataLain dataModel = new DataLain(" ", " ");
            list.add(dataModel);

        } else {
            for (int i = 0; i< FormPackingListAdapter.formPackingArrayList.size(); i++) {
                DataLain dataModel = new DataLain(FormPackingListAdapter.formPackingArrayList.get(i).getLabel(),
                        FormPackingListAdapter.editPackingArrayList.get(i).getEditTextValue());
                list.add(dataModel);
            }
        }
        return list;
    }

    //onitemclick for autocomplete text view
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AutocompleteItem autocompleteItem = itemAdapter.getItem(position);
            mIdItem = autocompleteItem.getIdItem();
            mKodeItem = autocompleteItem.getKodeItem();
            mNamaItem = autocompleteItem.getNamaItem();
            mUOM = autocompleteItem.getUom();
            mActualWeight = autocompleteItem.getActualWeight();
            mActualCBM = autocompleteItem.getActualCBM();

            etActualWeight.setText(mActualCBM);
            etActualCBM.setText(mActualCBM);
            tvUom.setText(mUOM);

            itemSelected = true;
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            requestItem(); init();
            numberOfRequest = 2;
        }
    }

    @Override
    public void onBackPressed() {
        if (!onSendData) {
            setResult(10);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (onInitLoading) {
            packingListCall.cancel();
            itemListCall.cancel();
        }
    }

    //checking if field is empty
    private void checkingField() {
        offError();
        if (atItemName.getText().toString().length() == 0 &&
                etActualWeight.getText().toString().length() == 0 &&
                etQty.getText().toString().length() == 0 &&
                etActualCBM.getText().toString().length() == 0) {
            atItemName.setError("Data belum terisi");
            etActualWeight.setError("Data belum terisi");
            etQty.setError("Data belum terisi");
            etActualCBM.setError("Data belum terisi");
        } else if (etActualWeight.getText().toString().length() == 0) {
            etActualWeight.setError("Data belum terisi");
        } else if (etQty.getText().toString().length() == 0) {
            etQty.setError("Data belum terisi");
        } else if (etActualCBM.getText().toString().length() == 0) {
            etActualCBM.setError("Data belum terisi");
        } else if (cbGenerated.isChecked() && etQtyPck.getText().toString().length() == 0) {
            etQtyPck.setError("Data belum terisi");
        } else if (!itemSelected) {
            atItemName.setError("Item tidak ditemukan");
        } else {
            onLoading();
            sendDataPackinglist();
        }
    }

    private void offError() {
        atItemName.setError(null);
        etActualWeight.setError(null);
        etQty.setError(null);
        etActualCBM.setError(null);
        etQtyPck.setError(null);
    }

    private void onLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemIncomingActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_not_found_data, null);
        CustomTextView textDialog;
        textDialog = view.findViewById(R.id.text_dialog);
        textDialog.setText(message);
        textDialog.setTextSize(18);


        builder.setView(view);
        builder.setPositiveButton(textAlert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (initReqFailed) {
                    initReqFailed = false; onInitLoading = true;
                    init();
                    requestItem();
                    numberOfRequest = 2;
                    onLoading();
                }
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

