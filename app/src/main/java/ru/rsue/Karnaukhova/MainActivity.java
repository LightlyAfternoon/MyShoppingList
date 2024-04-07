package ru.rsue.Karnaukhova;

import android.view.Gravity;
import android.view.MenuItem;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.google.android.material.navigation.NavigationView;
import org.jetbrains.annotations.NotNull;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    Fragment fragment;
    @Nullable
    String id = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
                id = String.valueOf(menuItem.getItemId());

                if (Integer.parseInt(id) == R.id.shopping_lists_select) {
                    fragment = new ProductsInListHost();
                }
                else if (Integer.parseInt(id) == R.id.items_select) {
                    fragment = new ProductsHost();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                menuItem.setChecked(true);

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (id != null) {
            if (Integer.parseInt(id) == R.id.shopping_lists_select) {
                fragment = new ProductsInListHost();
            }
            else if (Integer.parseInt(id) == R.id.items_select) {
                fragment = new ProductsHost();
            }x

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        }
    }
}