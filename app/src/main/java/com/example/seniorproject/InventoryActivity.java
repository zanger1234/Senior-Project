package com.example.seniorproject;

import  android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Objects;

public class InventoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ParseUser currentUser;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    NavController navController;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        DrawerLayout drawerLayout = findViewById(R.id.InventoryDrawerLayout);
        drawerLayout.setScrimColor(getResources().getColor(R.color.transparent));

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        assert navHostFragment != null;
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(mToolbar, navController, drawerLayout);
        navigationView.setNavigationItemSelectedListener(this);

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            System.out.println(currentUser.getObjectId());
            ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
            //for tables other than User: ParseQuery<ParseObject> queryObj = ParseQuery.getQuery("Inventory");
            queryUser.getInBackground(currentUser.getObjectId(), (object, e) -> {
                if (e == null) {
                    String username = object.getUsername();
                    String email = object.getEmail();
                    TextView textViewUser = findViewById(R.id.headerTextUsername);
                    TextView textViewEmail = findViewById(R.id.headerTextEmail);
                    textViewUser.setText(username);
                    textViewEmail.setText(email);
                    navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_loginRegister).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_favorites).setVisible(true);
                    boolean isAdmin = object.getBoolean("isAdmin");
                    if (isAdmin) {
                        navigationView.getMenu().findItem(R.id.nav_admin).setVisible(true);
                        navigationView.getMenu().getItem(0).setChecked(true);
                    }
                } else {
                    Log.i("queryLog", e.getMessage());
                    e.printStackTrace();
                }
            });
        }

        setNavMenuItemColor();
    }


    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        currentUser = ParseUser.getCurrentUser();
        if (menuItem.getItemId() == R.id.nav_logout) {
            if (currentUser != null) {
                ParseUser.logOutInBackground();
                Intent intentLogout = new Intent(InventoryActivity.this, MainActivity.class);
                startActivity(intentLogout);
            }

        } else if (menuItem.getItemId() == R.id.nav_loginRegister) {
            System.out.println("login or register.");
            Intent intentLogin = new Intent(InventoryActivity.this, MainActivity.class);
            this.startActivity(intentLogin);
        } else if (menuItem.getItemId() == R.id.nav_admin) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://saif.admin.back4app.com"));
            startActivity(browserIntent);
        }
        NavigationUI.onNavDestinationSelected(menuItem, navController);
        drawerLayout = findViewById(R.id.InventoryDrawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    private void setNavMenuItemColor() {
        ColorStateList stateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        getResources().getColor(R.color.color3),
                        getResources().getColor(R.color.colorText2)
                }
        );
        navigationView.setItemIconTintList(stateList);
        navigationView.setItemTextColor(stateList);
    }


    public void loginFromInventory(View v) {
        if (currentUser == null) {
            System.out.println(ParseUser.getCurrentUser());
            Intent intentLogin = new Intent(InventoryActivity.this, MainActivity.class);
            startActivity(intentLogin);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.toolbar_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        ImageView searchCloseIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchCloseIcon.setColorFilter(Color.WHITE);
        icon.setColorFilter(Color.WHITE);
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                navController.navigate(R.id.searchFragment, bundle);
                searchView.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

}

