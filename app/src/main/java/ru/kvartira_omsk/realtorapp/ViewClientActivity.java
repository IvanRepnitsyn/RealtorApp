package ru.kvartira_omsk.realtorapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by ivanr on 19.05.2018.
 */

public class ViewClientActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.view_client_activity;
    private DBWork mDbHelper;
    private Long mRowId;
    private Toolbar toolbar;

    private MenuItem homeMenuItem;
    private MenuItem selectorMenuItem;

    private TextView textViewNameClient;
    private TextView textViewTypeClient;
    private TextView textViewPhoneClient;
    private TextView textViewMailClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mDbHelper = new DBWork(this);

        textViewNameClient = (TextView) findViewById(R.id.tvNameClient);
        textViewTypeClient = (TextView) findViewById(R.id.tvTypeClient);
        textViewPhoneClient = (TextView) findViewById(R.id.tvPhoneClient);
        textViewMailClient = (TextView) findViewById(R.id.tvMailClient);

        Bundle extras = getIntent().getExtras();

        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(DBWork.COLUMN_ID);
        if (extras != null) {
            mRowId = extras.getLong("idviewclient");
        }

        initToolbar();

        Cursor client = mDbHelper.getClient(mRowId);
        startManagingCursor(client);
        textViewNameClient.setText(client.getString(client
                .getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT)));

        int indexType = client.getInt(client.getColumnIndexOrThrow(DBWork.COLUMN_TYPECLIENT));
        String strTypeClient = null;
        switch (indexType) {
            case 1:
                strTypeClient = "Продавец";
                break;
            case 2:
                strTypeClient = "Покупатель";
                break;
        }
        textViewTypeClient.setText(strTypeClient);

        textViewPhoneClient.setText(client.getString(client
                .getColumnIndexOrThrow(DBWork.COLUMN_PHONECLIENT)));
        textViewMailClient.setText(client.getString(client
                .getColumnIndexOrThrow(DBWork.COLUMN_MAILCLIENT)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbarview, menu);
        homeMenuItem = menu.findItem(android.R.id.home);
        selectorMenuItem = menu.findItem(R.id.action_selector);
        //okMenuItem = menu.findItem(R.id.action_ok);
        //delMenuItem = menu.findItem(R.id.action_delete);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setToolbarAttributes();
        selectorMenuItem.setVisible(false);
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_viewclient);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*String nameObj = mDbHelper.getObject(mRowId).getString(mDbHelper.getObject(mRowId)
                .getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT));*/
        getSupportActionBar().setTitle("");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        super.onStop();
    }
}
