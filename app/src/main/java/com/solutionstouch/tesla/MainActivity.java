package com.solutionstouch.tesla;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.solutionstouch.tesla.navigation.BaseItem;
import com.solutionstouch.tesla.navigation.CustomDataProvider;

import java.util.List;

import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListAdapter;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.OnItemClickListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private MultiLevelListView multiLevelListView;
    private pl.openrnd.multilevellistview.OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        private void showItemDescription(Object object, ItemInfo itemInfo) {

            if (((BaseItem) object).getName().contains("Model S")) {
                displaySelectedScreen("Model S");
            }
            if (((BaseItem) object).getName().contains("Model 3")) {
                displaySelectedScreen("Model 3");
            }
            if (((BaseItem) object).getName().contains("Model X")) {
                displaySelectedScreen("Model X");
            }
            if (((BaseItem) object).getName().contains("Model Y")) {
                displaySelectedScreen("Model Y");
            }
            if (((BaseItem) object).getName().contains("CyberTruck")) {
                displaySelectedScreen("CyberTruck");
            }
            if (((BaseItem) object).getName().contains("EXIT")) {
                displaySelectedScreen("EXIT");
            }
        }


        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }

        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }

        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null){}
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        confMenu();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen("Model S");
    }

    private void confMenu() {
        multiLevelListView = findViewById(R.id.multi_nav);
        // custom ListAdapter
        ListAdapter listAdapter = new ListAdapter();
        multiLevelListView.setAdapter(listAdapter);
        multiLevelListView.setOnItemClickListener((OnItemClickListener) mOnItemClickListener);

        listAdapter.setDataItems(CustomDataProvider.getInitialItems());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void displaySelectedScreen(String itemName) {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemName) {
            case "Model S":
                fragment = new HomeFragment();
                break;
            case "Model 3":
                fragment = new Assignment1Fragment();
                break;
            case "Model X":
                fragment = new Assignment2Fragment();
                break;
            case "Model Y":
                fragment = new Category1Fragment();
                break;
            case "CyberTruck":
                fragment = new Category2Fragment();
                break;
            case "EXIT":
                dialogExit();

                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void dialogExit() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(String.valueOf(item.getItemId()));
        //make this method blank
        return true;
    }

    private class ListAdapter extends MultiLevelListAdapter {
        @Override
        public List<?> getSubObjects(Object object) {
            // DIEKSEKUSI SAAT KLIK PADA GROUP-ITEM
            return CustomDataProvider.getSubItems((BaseItem) object);
        }

        @Override
        public boolean isExpandable(Object object) {
            return CustomDataProvider.isExpandable((BaseItem) object);
        }

        @Override
        public View getViewForObject(Object object, View convertView, ItemInfo itemInfo) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.data_item, null);
                //viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
                viewHolder.nameView = convertView.findViewById(R.id.dataItemName);
                viewHolder.arrowView = convertView.findViewById(R.id.dataItemArrow);
                viewHolder.icon = convertView.findViewById(R.id.di_image);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.nameView.setText(((BaseItem) object).getName());
            //viewHolder.infoView.setText(getItemInfoDsc(itemInfo));

            if (itemInfo.isExpandable()) {
                viewHolder.arrowView.setVisibility(View.VISIBLE);
                viewHolder.arrowView.setImageResource(itemInfo.isExpanded() ?
                        R.drawable.bottomarrow : R.drawable.rightarrow);
            } else {
                viewHolder.arrowView.setVisibility(View.GONE);
            }
            viewHolder.icon.setImageResource(((BaseItem) object).getIcon());
            return convertView;
        }

        private class ViewHolder {
            TextView nameView;
            ImageView arrowView;
            ImageView icon;

        }
    }
}