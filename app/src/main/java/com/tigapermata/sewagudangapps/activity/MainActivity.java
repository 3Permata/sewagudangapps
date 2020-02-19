package com.tigapermata.sewagudangapps.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.tigapermata.sewagudangapps.AppController;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.inbound.ListIncomingActivity;
import com.tigapermata.sewagudangapps.activity.outbound.ListOutgoingActivity;
import com.tigapermata.sewagudangapps.activity.outbound.PickingListActivity;
import com.tigapermata.sewagudangapps.activity.outbound.PickingListByItemActivity;
import com.tigapermata.sewagudangapps.activity.putaway.HistoryPutAwayActivity;
import com.tigapermata.sewagudangapps.activity.stockcount.DetailStockCountByItemActivity;
import com.tigapermata.sewagudangapps.activity.stockcount.DetailStockCountByLabelActivity;
import com.tigapermata.sewagudangapps.fragment.InboundFragment;
import com.tigapermata.sewagudangapps.fragment.LoadingFragment;
import com.tigapermata.sewagudangapps.fragment.PickingFragment;
import com.tigapermata.sewagudangapps.fragment.PutawayFragment;
import com.tigapermata.sewagudangapps.fragment.StockCountFragment;
import com.tigapermata.sewagudangapps.helper.DBHelper;

public class MainActivity extends AppCompatActivity {

    int press = 0;
    private Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelected);

        selectedFragment = InboundFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_main, selectedFragment);
        transaction.commit();

        AppController.getInstance().setMainAct(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelected =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navigation_inbound:
                            selectedFragment = InboundFragment.newInstance();
                            break;

                        case R.id.navigation_putaway:
                            selectedFragment = PutawayFragment.newInstance();
                            break;

                        case R.id.navigation_loading:
                            selectedFragment = LoadingFragment.newInstance();
                            break;

                        case R.id.navigation_picking:
                            selectedFragment = PickingFragment.newInstance();
                            break;

                        case R.id.navigation_stockon:
                            selectedFragment = StockCountFragment.newInstance();
                            break;

                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container_main,selectedFragment);
                    transaction.commit();

                    return true;

                }
            };

    @Override
    public void onBackPressed() {
        if (press == 0){
            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
            press = 1;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    press = 0;
                }
            }, 3000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            press = 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) { //picking list
            ((PickingFragment)selectedFragment).refreshData();
        }
        else if (requestCode == 11 & resultCode == 10) { //list outgoing
            ((LoadingFragment)selectedFragment).refreshData();
        }
        else if (requestCode == 12 && resultCode == 10) { //list incoming
            ((InboundFragment)selectedFragment).refreshData();
        }
        else if (requestCode == 14 && resultCode == 10) {//detail stock count
            ((StockCountFragment)selectedFragment).refreshData();
        }
    }

    public void logout() {
        DBHelper db = new DBHelper(this);
        db.clearDatabase();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void finishActivity() {
        finish();
    }

    public void startPickingListAct(String metode) {
        if (metode.matches("by label"))
            startActivityForResult(new Intent(this, PickingListActivity.class), 10);
        else
            startActivityForResult(new Intent(this, PickingListByItemActivity.class), 10);
    }

    public void startListOutgoingAct() {
        startActivityForResult(new Intent(this, ListOutgoingActivity.class), 11);
    }

    public void startListIncomingAct() {
        startActivityForResult(new Intent(this, ListIncomingActivity.class), 12);
    }

    public void startHistoryPutAwayAct() {
        startActivityForResult(new Intent(this, HistoryPutAwayActivity.class), 13);
    }

    public void startDetailStockCountByItemActivity() {
        startActivityForResult(new Intent(this, DetailStockCountByItemActivity.class), 14);
    }

    public void startDetailStockCountByLabelActivity() {
        startActivityForResult(new Intent(this, DetailStockCountByLabelActivity.class), 14);
    }
}
