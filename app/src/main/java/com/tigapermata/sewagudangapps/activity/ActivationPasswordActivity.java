package com.tigapermata.sewagudangapps.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.api.ApiClient;
import com.tigapermata.sewagudangapps.api.ApiService;
import com.tigapermata.sewagudangapps.helper.DBHelper;
import com.tigapermata.sewagudangapps.model.LoginResponse;
import com.tigapermata.sewagudangapps.utils.CustomButton;
import com.tigapermata.sewagudangapps.utils.CustomEditText;
import com.tigapermata.sewagudangapps.utils.CustomTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationPasswordActivity extends AppCompatActivity {

    private static final String TAG = "PasswordActivation";
    CustomButton btnSubmit;
    CustomEditText mPassword, mConfirmPassword;
    String lastPath;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_password_actvity);

        onNewIntent(getIntent());

        btnSubmit = findViewById(R.id.btnSubmit_password);
        mPassword = findViewById(R.id.txtPassword);
        mConfirmPassword = findViewById(R.id.txtPasswordConfirm);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getText().toString().length() == 0) {
                    mPassword.setError("Entry Your Password");
                } else if (mConfirmPassword.getText().toString().length() == 0) {
                    mConfirmPassword.setError("Enter Your Confirm Password");
                } else {
                    String password = mPassword.getText().toString().trim();
                    String confirm = mConfirmPassword.getText().toString().trim();

                    if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm)) {
                        checkPassword(password, confirm);
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivationPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data!= null) {
            String tokens = data.substring(data.lastIndexOf("/"));

        }
    }

    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            String recipeId = appLinkData.getLastPathSegment();
            Uri appData = Uri.parse("https://apps.sewagudang.id/site/useraktif/token_email/").buildUpon()
                    .appendPath(recipeId).build();
            lastPath = appLinkData.getLastPathSegment();
        }
    }

    private boolean checkPassword(String pass, String conf) {
        boolean status = false;

        if (conf != null && pass != null) {
            if (pass.equals(conf)) {
                status = true;
                String token = lastPath;
                sendData(conf, token);
            } else {
                Toast.makeText(this, "Confirm the password you entered is incorrect", Toast.LENGTH_SHORT).show();
            }
        }

        return status;

    }

    private void sendData(String token, String password) {
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> loginCall = service.confirmPassword(token, password);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    if (dbHelper.getTokenCount() == 0) {
                        dbHelper.addToken(response.body());
                    } else {
                        dbHelper.updateToken(response.body());
                    }
                } else {
                    Toast.makeText(ActivationPasswordActivity.this, "Oops! Something wrong", Toast.LENGTH_SHORT).show();
                }


                Intent intent = new Intent(ActivationPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                AlertSuccess("Please Check your Internet Connection");
            }
        });

    }

    private void AlertSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivationPasswordActivity.this);
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
