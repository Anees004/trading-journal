package com.example.tradingjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.splashscreen.SplashScreenViewProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.tradingjournal.UI.Input.AddFragment;
import com.example.tradingjournal.UI.Input.InputActivity;
import com.example.tradingjournal.UI.allrecords.AllRecordsFragment;
import com.example.tradingjournal.UI.dashboard.DashboardFragment;
import com.example.tradingjournal.UI.dashboard.DashboardViewModel;
import com.example.tradingjournal.UI.dashboard.DashboardViewModelFactory;
import com.example.tradingjournal.UI.home.HomeFragment;
import com.example.tradingjournal.UI.setting.SettingFragment;
import com.example.tradingjournal.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameContainer;
    private DashboardFragment dashboardFragment;
    private AddFragment addFragment;
    private HomeFragment homeFragment;
    private AllRecordsFragment allRecordsFragment;
    private SettingFragment settingFragment;
    ActivityMainBinding binding;
    DashboardViewModel dashboardViewModel;
    private boolean keep = true;
    private final int DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //Keep returning false to Should Keep On Screen until ready to begin.
        splashScreen.setKeepOnScreenCondition(new SplashScreen.KeepOnScreenCondition() {
            @Override
            public boolean shouldKeepOnScreen() {
                return keep;
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(runner, DELAY);

        bottomNavigationView = binding.bottomNavigation;
        frameContainer = binding.frameContainer;
        DashboardViewModelFactory viewModelFactory = new DashboardViewModelFactory(this);
        dashboardViewModel = new ViewModelProvider(this, viewModelFactory).get(DashboardViewModel.class);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (dashboardViewModel.hastableEntriesCount() < 1) {
            Intent intent = new Intent(this, InputActivity.class);
            startActivity(intent);
            this.finish();
        }
        loadFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        {
            Fragment fragment = null;
            if (item.getItemId() == R.id.nav_dashboard) {
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                    fragment = homeFragment;
            } else if (item.getItemId() == R.id.nav_add) {
                if (addFragment == null) {
                    addFragment = new AddFragment();
                }
                fragment = addFragment;
            } else if (item.getItemId() == R.id.nav_charts)
            {
                if (dashboardFragment == null) {
                    dashboardFragment = new DashboardFragment();
                }
                    fragment = dashboardFragment;
            }

            else if (item.getItemId() == R.id.nav_all_records)
                fragment = new AllRecordsFragment();
            else if (item.getItemId() == R.id.nav_account)
                fragment = new SettingFragment();
            if (fragment != null) {
                loadFragment(fragment);
            }
            return true;
        }

    }

    void loadFragment(Fragment fragment) {
        //to attach fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    private final Runnable runner = new Runnable() {
        @Override
        public void run() {
            keep = false;
        }
    };
}
