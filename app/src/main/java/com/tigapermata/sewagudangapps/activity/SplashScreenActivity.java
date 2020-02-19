package com.tigapermata.sewagudangapps.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.inbound.AddItemIncomingActivity;
import com.tigapermata.sewagudangapps.activity.inbound.AddMasterItemActivity;
import com.tigapermata.sewagudangapps.helper.DBHelper;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        }, 2000);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    //this method is checking have user login on the apps?
    private void checkUser() {
        DBHelper db = new DBHelper(SplashScreenActivity.this);
        if(db.getTokenCount() == 0) {
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (!db.getIds().getIdGudang().matches("") && !db.getIds().getIdProject().matches("")) {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(SplashScreenActivity.this, SearchWarehouseActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }
}
