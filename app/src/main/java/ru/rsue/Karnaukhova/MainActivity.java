package ru.rsue.Karnaukhova;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    Fragment fragment;
    Class fragmentClass;
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
                if (Integer.parseInt(id) == R.id.daily_purchases_select) {
                    fragmentClass = DailyProductsHost.class;
                }
                else if (Integer.parseInt(id) == R.id.items_select) {
                    fragmentClass = ProductsHost.class;
                }
                else if (Integer.parseInt(id) == R.id.lists_select) {
                    fragmentClass = ItemsListsHost.class;
                }
                else if (Integer.parseInt(id) == R.id.lists_purchases_select) {
                    fragmentClass = ListsPurchasesHost.class;
                }
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
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
            if (Integer.parseInt(id) == R.id.daily_purchases_select) {
                fragmentClass = DailyProductsHost.class;
            }
            else if (Integer.parseInt(id) == R.id.items_select) {
                fragmentClass = ProductsHost.class;
            }
            else if (Integer.parseInt(id) == R.id.lists_select) {
                fragmentClass = ItemsListsHost.class;
            }
            else if (Integer.parseInt(id) == R.id.lists_purchases_select) {
                fragmentClass = ListsPurchasesHost.class;
            }
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        }
    }
}