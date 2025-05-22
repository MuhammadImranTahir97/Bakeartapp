package com.example.bakeart;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.bakeart.fragments.HomeFragment;
import com.example.bakeart.activities.LoginActivity;
import com.example.bakeart.activities.SearchActivity;
import com.example.bakeart.activities.UploadActivity;
import com.example.bakeart.activities.FavoritesActivity;
import com.example.bakeart.activities.ModeratorDashboardActivity;
import com.example.bakeart.utils.DefaultRecipeSeeder;
import com.example.bakeart.utils.RecipeStatusWorker;
import com.example.bakeart.utils.UserSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // — Require user to be logged in —
        UserSession session = new UserSession(this);
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // — Schedule background check for flagged recipes every 4 hours —
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                RecipeStatusWorker.class,
                4, TimeUnit.HOURS
        ).build();
        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "modCheck",
                        ExistingPeriodicWorkPolicy.KEEP,
                        workRequest
                );

        // — Seed default Verified recipes on first run —
        DefaultRecipeSeeder.seedIfNeeded(this);

        // — Inflate UI & set up bottom navigation —
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Load the default HomeFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        // Handle bottom nav clicks
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment toLoad = null;

            switch (id) {
                case R.id.nav_home:
                    toLoad = new HomeFragment();
                    break;
                case R.id.nav_search:
                    startActivity(new Intent(this, SearchActivity.class));
                    break;
                case R.id.nav_upload:
                    startActivity(new Intent(this, UploadActivity.class));
                    break;
                case R.id.nav_profile:
                    startActivity(new Intent(this, FavoritesActivity.class));
                    break;
                case R.id.nav_moderator:
                    startActivity(new Intent(this, ModeratorDashboardActivity.class));
                    break;
            }

            if (toLoad != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, toLoad)
                        .commit();
            }
            return true;
        });
    }
}
