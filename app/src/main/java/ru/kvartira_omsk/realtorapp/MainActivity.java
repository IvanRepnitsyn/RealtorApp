package ru.kvartira_omsk.realtorapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import ru.kvartira_omsk.realtorapp.adapter.TabsFragmentAdapter;


public class MainActivity extends AppCompatActivity  {

    //private DBWork dbHelper;
    private static final int LAYOUT = R.layout.activity_main;
    private static final int OBJECT_ACTIVITY_CREATE = 11;
    private static final int OBJECT_ACTIVITY_EDIT = 15;
    private static final int EVENT_ACTIVITY_CREATE = 22;
    private static final int EVENT_ACTIVITY_EDIT = 25;
    private static final int CLIENT_ACTIVITY_CREATE = 33;
    private static final int CLIENT_ACTIVITY_EDIT = 35;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    //private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initToolbar();
        initNavigationView();
        initTabs();

        FloatingActionButton AddObjectButton = (FloatingActionButton) findViewById(R.id.fab);
        AddObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(viewPager.getCurrentItem()) {
                    case 0:
                        //Toast.makeText(O.this.getActivity(), "Работает", Toast.LENGTH_LONG).show();
                        Intent intent_object = new Intent(MainActivity.this, AddObjectActivity.class);
                        startActivityForResult(intent_object, OBJECT_ACTIVITY_CREATE);
                        break;
                    case 1:
                        //Toast.makeText(O.this.getActivity(), "Работает", Toast.LENGTH_LONG).show();
                        Intent intent_event = new Intent(MainActivity.this, AddEventActivity.class);
                        startActivityForResult(intent_event, EVENT_ACTIVITY_CREATE);
                        break;
                    case 2:
                        //Toast.makeText(O.this.getActivity(), "Работает", Toast.LENGTH_LONG).show();
                        Intent intent_client = new Intent(MainActivity.this, AddClientActivity.class);
                        startActivityForResult(intent_client, CLIENT_ACTIVITY_CREATE);
                        break;
                }
            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsFragmentAdapter adapter = new TabsFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuNavigationItem) {
                drawerLayout.closeDrawers();
                switch (menuNavigationItem.getItemId()) {
                    case R.id.navigation_addobject:
                        viewPager.setCurrentItem(0);
                        Intent intent_object = new Intent(MainActivity.this, AddObjectActivity.class);
                        startActivityForResult(intent_object, OBJECT_ACTIVITY_CREATE);
                        break;
                    case R.id.navigation_addevent:
                        viewPager.setCurrentItem(1);
                        Intent intent_event = new Intent(MainActivity.this, AddEventActivity.class);
                        startActivityForResult(intent_event, EVENT_ACTIVITY_CREATE);
                        break;
                    case R.id.navigation_addclient:
                        viewPager.setCurrentItem(2);
                        Intent intent_client = new Intent(MainActivity.this, AddClientActivity.class);
                        startActivityForResult(intent_client, OBJECT_ACTIVITY_CREATE);
                        break;
                }
                return true;
            }
        });

    }

   /* public void editClient() {

    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (dbHelper != null) {
            dbHelper.close();
        }*/
    }
}