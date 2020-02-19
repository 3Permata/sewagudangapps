package com.tigapermata.sewagudangapps.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.LoginResponse;
import com.tigapermata.sewagudangapps.model.SavedId;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;
import com.tigapermata.sewagudangapps.utils.TitleTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private CustomEditText mEmail, mPassword;
    private Button btnLogin;
    private ProgressBar pbLoading;
    private View vSemiTransparent;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pbLoading = findViewById(R.id.progressBarLoading);
        vSemiTransparent = findViewById(R.id.viewSemiTransparent);

        mEmail = findViewById(R.id.txtEmail_login);
        mPassword = findViewById(R.id.txtPassword_login);
        btnLogin = findViewById(R.id.btnLogin_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().length() == 0) {
                    mEmail.setError("Email Belum Terisi");
                } else if (mPassword.getText().toString().length() == 0) {
                    mPassword.setError("Password belum terisi");
                } else {
                    String email = mEmail.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        sendData(email, password);
                    }
                }
            }
        });
    }

    private void sendData(String email, String password) {
        onLoading();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> loginCall = service.login(email, password);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                endLoading();
                if (response.code() == 200) {
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    if (dbHelper.getTokenCount() == 0) {
                        dbHelper.addToken(response.body());
                        Intent intent = new Intent(LoginActivity.this, SearchWarehouseActivity.class);
                        startActivity(intent);
                        final SavedId ids = new SavedId("","", "", "");
                        if (dbHelper.getIdsCount() == 0) {
                            dbHelper.addIDS(ids);
                        } else {
                            dbHelper.updateIds(ids);
                        }
                        finish();
                    } else {
                        dbHelper.updateToken(response.body());
                        Intent intent = new Intent(LoginActivity.this, SearchWarehouseActivity.class);
                        startActivity(intent);
                        final SavedId ids = new SavedId("", "", "", "");
                        if (dbHelper.getIdsCount() == 0) {
                            dbHelper.addIDS(ids);
                        } else {
                            dbHelper.updateIds(ids);
                        }
                        finish();
                    }
                } else if (response.code() == 500){
                    Toast.makeText(getApplicationContext(), "Oops! Your email and Password are wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Oops! Something wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                endLoading();
                t.printStackTrace();
                AlertSuccess("Please Check your Internet Connection");
            }
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
