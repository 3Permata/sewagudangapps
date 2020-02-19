package com.tigapermata.sewagudangapps.activity.inbound;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.inbound.AddItemResponse;
import com.tigapermata.sewagudangapps.utils.CustomButton;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMasterItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGenerateCode, btnCountCBM, btnUpload, btnReupload, btnDelete, btnSubmit;
    private EditText etKodeItem, etPanjang, etLebar, etTinggi, etCbmResult, etUom, etNamaItem, etBeratBersih, etBeratKotor;
    private TextView tPanjang, tLebar, tTinggi, tCBM, tCodeGenerated;
    private RadioGroup rgHitungCBM;
    Boolean generated = false, photoUploaded = false, onSendData = false;
    float length, width, height;

    String jenisGenerated, kodeItem, hitungCBM;

    String idUser, token, totalCBM, panjang, lebar, tinggi, namaItem, uom, beratBersih, satuanTonase,
            beratKotor;

    Integer htgCBM = 1;
    Integer jnsGnrt = 0;

    //upload photo
    private ImageView masterImage;
    private static final String IMAGE_DIRECTORY = "/image";

    View view, vSemiTransparent;
    ProgressBar pbLoading;
    File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_master_item);

        Toolbar toolbar = findViewById(R.id.toolbar_master_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Master Item");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //button
        btnGenerateCode = findViewById(R.id.generate_code_master_item);
        btnGenerateCode.setOnClickListener(this);
        btnCountCBM = findViewById(R.id.count_cbm);
        btnCountCBM.setOnClickListener(this);
        btnUpload = findViewById(R.id.upload_photo);
        btnUpload.setOnClickListener(this);
        btnReupload = findViewById(R.id.buttonReuploadPhoto);
        btnReupload.setOnClickListener(this);
        btnDelete = findViewById(R.id.buttonDeletePhoto);
        btnDelete.setOnClickListener(this);
        btnSubmit = findViewById(R.id.master_item_submit);
        btnSubmit.setOnClickListener(this);

        //edittext
        etKodeItem = findViewById(R.id.kode_master_item);
        etPanjang = findViewById(R.id.panjang_master_item);
        etLebar = findViewById(R.id.lebar_master_item);
        etTinggi = findViewById(R.id.tinggi_master_item);
        etCbmResult = findViewById(R.id.cbm_result);
        etUom = findViewById(R.id.uom_master_item);
        etNamaItem = findViewById(R.id.nama_master_item);
        etBeratBersih = findViewById(R.id.bersih_master_item);
        etBeratKotor = findViewById(R.id.kotor_master_item);

        //textview
        tPanjang = findViewById(R.id.textViewPanjang);
        tLebar = findViewById(R.id.textViewLebar);
        tTinggi = findViewById(R.id.textViewTinggi);
        tCBM = findViewById(R.id.textViewCBM);
        tCodeGenerated = findViewById(R.id.textViewCodeGenerated);

        //radio group
        rgHitungCBM = findViewById(R.id.hitung_cbm_master_item);

        //image view
        masterImage = findViewById(R.id.master_image);

        vSemiTransparent = findViewById(R.id.viewSemiTransparent);
        pbLoading = findViewById(R.id.progressBarLoading);

        rgHitungCBM.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case 0: // first button (langsung)
                        toLangsungCBM();
                        htgCBM = 1;
                        break;

                    case 1: // secondbutton (manual)
                        toManualCBM();
                        htgCBM = 0;
                        break;
                }
            }
        });

        checkPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.count_cbm:
                toLangsungCBM();
                rgHitungCBM.check(R.id.radioButton);
                countingCBM();
                break;

            case R.id.generate_code_master_item:
                if (!generated) {
                    etKodeItem.setVisibility(View.INVISIBLE);
                    tCodeGenerated.setVisibility(View.VISIBLE);
                    btnGenerateCode.setText("Manual");
                    jnsGnrt = 1;
                    generated = true;
                } else {
                    etKodeItem.setVisibility(View.VISIBLE);
                    tCodeGenerated.setVisibility(View.INVISIBLE);
                    btnGenerateCode.setText("Generate");
                    jnsGnrt = 0;
                    generated = false;
                }
                break;

            case R.id.upload_photo:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
                break;

            case R.id.buttonReuploadPhoto:
                Intent cameraIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent2, 1);
                break;

            case R.id.buttonDeletePhoto:
                masterImage.setVisibility(View.GONE);
                btnReupload.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnUpload.setVisibility(View.VISIBLE);
                photoUploaded = false;
                break;

            case R.id.master_item_submit:
                checkField();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!onSendData) {
            setResult(10);
            finish();
        }
    }

    private void toManualCBM() {
        tCBM.setVisibility(View.INVISIBLE);
        etCbmResult.setVisibility(View.INVISIBLE);

        btnCountCBM.setVisibility(View.VISIBLE);
        tPanjang.setVisibility(View.VISIBLE);
        tLebar.setVisibility(View.VISIBLE);
        tTinggi.setVisibility(View.VISIBLE);
        etPanjang.setVisibility(View.VISIBLE);
        etLebar.setVisibility(View.VISIBLE);
        etTinggi.setVisibility(View.VISIBLE);
    }

    private void toLangsungCBM() {
        tCBM.setVisibility(View.VISIBLE);
        etCbmResult.setVisibility(View.VISIBLE);

        btnCountCBM.setVisibility(View.INVISIBLE);
        tPanjang.setVisibility(View.INVISIBLE);
        tLebar.setVisibility(View.INVISIBLE);
        tTinggi.setVisibility(View.INVISIBLE);
        etPanjang.setVisibility(View.INVISIBLE);
        etLebar.setVisibility(View.INVISIBLE);
        etTinggi.setVisibility(View.INVISIBLE);
    }

    private void onLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        vSemiTransparent.setVisibility(View.VISIBLE);
    }

    private void endLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        vSemiTransparent.setVisibility(View.INVISIBLE);
    }

    //function for counting CBM
    private void countingCBM(){
        if (etPanjang.getText().toString().length() == 0) {
            etPanjang.setError("Data belum terisi");
        }
        else if (etLebar.getText().toString().length() == 0) {
            etLebar.setError("Data belum terisi");
        }
        else if (etTinggi.getText().toString().length() == 0) {
            etTinggi.setError("Data belum terisi");
        }
        else {
            length = (Float.parseFloat(etPanjang.getText().toString()))/100;
            width = (Float.parseFloat(etLebar.getText().toString()))/100;
            height = (Float.parseFloat(etTinggi.getText().toString()))/100;

            float cbm;
            cbm = length*width*height;

            etCbmResult.setText(String.valueOf(cbm));
            etCbmResult.setSelection(etCbmResult.getText().toString().length());
            etCbmResult.requestFocus();
        }
    }

    //this method for checking permission
    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 123);
        } else if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA}, 124);
        }
    }

    //result from permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    checkPermission();
                }
                break;
        }
    }

    //result from taking picture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
          return;
        }

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            saveImage(bitmap);
            photoUploaded = true;
        }
    }

    //method for saving image
    public String saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() +IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            mFile = new File(file, Calendar.getInstance()
                    .getTimeInMillis() + ".png");
            Log.e("mFile", mFile.getPath().toString());
            mFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(mFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();

            //serverUploaded(mFile);
            Toast.makeText(getApplicationContext(), "image saved", Toast.LENGTH_SHORT).show();
            btnUpload.setVisibility(View.INVISIBLE);

            btnDelete.setVisibility(View.VISIBLE);
            btnReupload.setVisibility(View.VISIBLE);
            masterImage.setVisibility(View.VISIBLE);
            masterImage.setImageBitmap(bitmap);
            photoUploaded = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "File Error", Toast.LENGTH_SHORT).show();
            Log.e("file", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "IO Error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exception error", Toast.LENGTH_SHORT).show();
            Log.e("exception", e.toString());
        }
        return "";
    }

    //upload data
    private void sendData() {
        onLoading(); onSendData = true;
        DBHelper dbHelper = new DBHelper(getApplicationContext());

        //path
        String idGudang = dbHelper.getIds().getIdGudang();
        String idProject = dbHelper.getIds().getIdProject();
        String idInbound = dbHelper.getIdInbound().getIdInbound();

        //input data
        idUser = dbHelper.getTokenn().getIdUser();
        token = dbHelper.getTokenn().getToken();

        if (htgCBM == 1) {
            hitungCBM = "langsung";
            panjang = "0";
            lebar = "0";
            tinggi = "0";
        } else if (htgCBM == 0) {
            hitungCBM = "manual";
            tinggi = etTinggi.getText().toString();
            lebar = etLebar.getText().toString();
            panjang = etPanjang.getText().toString();
        }

        if (jnsGnrt == 0) {
            jenisGenerated = "manual";
            kodeItem = etKodeItem.getText().toString();
        } else {
            jenisGenerated = "generated";
            kodeItem = " ";
        }

        totalCBM = etCbmResult.getText().toString();
        namaItem = etNamaItem.getText().toString();
        uom = etUom.getText().toString();
        beratBersih = etBeratBersih.getText().toString();
        beratKotor = etBeratKotor.getText().toString();
        satuanTonase = "kg";

        //string data upload
        RequestBody reqIdUser = RequestBody.create(MediaType.parse("multipart/form-data"), idUser);
        RequestBody reqToken = RequestBody.create(MediaType.parse("multipart/form-data"), token);
        RequestBody reqJenisGenerated = RequestBody.create(MediaType.parse("multipart/form-data"), jenisGenerated);
        RequestBody reqKodeItem = RequestBody.create(MediaType.parse("multipart/form-data"), kodeItem);
        RequestBody reqHitungCBM = RequestBody.create(MediaType.parse("multipart/form-data"), hitungCBM);
        RequestBody reqPanjang = RequestBody.create(MediaType.parse("multipart/form-data"), panjang);
        RequestBody reqLebar = RequestBody.create(MediaType.parse("multipart/form-data"), lebar);
        RequestBody reqTinggi = RequestBody.create(MediaType.parse("multipart/form-data"), tinggi);
        RequestBody reqTotalCBM = RequestBody.create(MediaType.parse("multipart/form-data"), totalCBM);
        RequestBody reqNamaItem = RequestBody.create(MediaType.parse("multipart/form-data"), namaItem);
        RequestBody reqUOM = RequestBody.create(MediaType.parse("multipart/form-data"), uom);
        RequestBody reqTonase = RequestBody.create(MediaType.parse("multipart/form-data"), beratBersih);
        RequestBody reqSatuanTonase = RequestBody.create(MediaType.parse("multipart/form-data"), satuanTonase);
        RequestBody reqBeratKotor = RequestBody.create(MediaType.parse("multipart/form-data"), beratKotor);

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<AddItemResponse> addItemCall;

        if (photoUploaded) {
            //image upload
            RequestBody request = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
            MultipartBody.Part reqFoto = MultipartBody.Part.createFormData("foto", mFile.getName(), request);

            addItemCall = service.addMasterItem(idGudang, idProject, idInbound, reqIdUser, reqToken,
                    reqJenisGenerated, reqKodeItem, reqFoto, reqHitungCBM, reqPanjang, reqLebar, reqTinggi, reqTotalCBM, reqNamaItem, reqUOM,
                    reqTonase, reqSatuanTonase, reqBeratKotor);
        }
        else {
            addItemCall = service.addMasterItemWithoutPhoto(idGudang, idProject, idInbound, reqIdUser, reqToken,
                    reqJenisGenerated, reqKodeItem, reqHitungCBM, reqPanjang, reqLebar, reqTinggi, reqTotalCBM, reqNamaItem, reqUOM,
                    reqTonase, reqSatuanTonase, reqBeratKotor);
        }

        addItemCall.enqueue(new Callback<AddItemResponse>() {
            @Override
            public void onResponse(Call<AddItemResponse> call, Response<AddItemResponse> response) {
                endLoading(); onSendData = false;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Upload Data Successful", Toast.LENGTH_SHORT).show();
                    setResult(10);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddItemResponse> call, Throwable t) {
                endLoading(); onSendData = false;
                t.printStackTrace();
                AlertSuccess("Please Check your Internet Connection");
            }
        });
    }

    //check field if empty
    private void checkField() {
        if (etCbmResult.getText().toString().length() == 0) {
            etCbmResult.setError("Data belum terisi");
        } else if (etNamaItem.getText().toString().length() == 0 ) {
            etNamaItem.setError("Data belum terisi");
        } else if (etUom.getText().toString().length() == 0) {
            etUom.setError("Data belum terisi");
        } else if (etBeratBersih.getText().toString().length() == 0) {
            etBeratBersih.setError("Data belum terisi");
        } else if (etBeratKotor.getText().toString().length() == 0) {
            etBeratKotor.setError("Data belum terisi");
        } else if (jnsGnrt == 0 && etKodeItem.getText().toString().length() == 0) {
            etKodeItem.setError("Data belum terisi");
        }
        else sendData();
    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMasterItemActivity.this);
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

}
