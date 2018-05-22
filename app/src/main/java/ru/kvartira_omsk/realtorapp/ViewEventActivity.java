package ru.kvartira_omsk.realtorapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



/**
 * Created by ivanr on 13.05.2018.
 */

public class ViewEventActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.view_event_activity;
    private DBWork mDbHelper;
    private Long mRowId;
    private Toolbar toolbar;

    private MenuItem homeMenuItem;
    private MenuItem selectorMenuItem;

    private TextView textViewNameEvent;
    private TextView textViewTypeEvent;
    private TextView textViewDateEvent;
    private TextView textViewTimeEvent;
    private TextView textViewClientEvent;
    private TextView textViewPlaceEvent;
    private TextView textViewInfoEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mDbHelper = new DBWork(this);

        textViewNameEvent = (TextView) findViewById(R.id.tvNameEvent);
        textViewTypeEvent = (TextView) findViewById(R.id.tvTypeEvent);
        textViewDateEvent = (TextView) findViewById(R.id.tvDateEvent);
        textViewTimeEvent = (TextView) findViewById(R.id.tvTimeEvent);
        textViewClientEvent= (TextView) findViewById(R.id.tvClientEvent);
        textViewPlaceEvent= (TextView) findViewById(R.id.tvPlaceEvent);
        textViewInfoEvent= (TextView) findViewById(R.id.tvInfoEvent);


        Bundle extras = getIntent().getExtras();

        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(DBWork.COLUMN_ID);
        if (extras != null) {
            mRowId = extras.getLong("idviewevent");
        }

        initToolbar();

        Cursor event = mDbHelper.getEvent(mRowId);
        startManagingCursor(event);
        textViewNameEvent.setText(event.getString(event
                .getColumnIndexOrThrow(DBWork.COLUMN_NAMEEVENT)));

        int indexType = event.getInt(event.getColumnIndexOrThrow(DBWork.COLUMN_TYPEEVENT));
        String strTypeEvent = null;
        switch (indexType) {
            case 1:
                strTypeEvent = "Встреча";
                break;
            case 2:
                strTypeEvent = "Звонок";
                break;
            case 3:
                strTypeEvent = "Показ";
                break;
            case 4:
                strTypeEvent = "Сделка";
                break;
        }
        textViewTypeEvent.setText(strTypeEvent);

        textViewDateEvent.setText(event.getString(event
                .getColumnIndexOrThrow(DBWork.COLUMN_DATEEVENT)));
        textViewTimeEvent.setText(event.getString(event
                .getColumnIndexOrThrow(DBWork.COLUMN_TIMEEVENT)));

        Long idClient = event.getLong(event.getColumnIndexOrThrow(DBWork.COLUMN_IDCLIENT));
        Cursor clientcursor = mDbHelper.getClient(idClient);
        String strClient = clientcursor.getString(clientcursor.getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT));
        textViewClientEvent.setText(strClient);

        textViewPlaceEvent.setText(event.getString(event
                .getColumnIndexOrThrow(DBWork.COLUMN_PLACEEVENT)));

        textViewInfoEvent.setText(event.getString(event
                .getColumnIndexOrThrow(DBWork.COLUMN_INFOEVENT)));

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
        toolbar = (Toolbar) findViewById(R.id.toolbar_viewevent);

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
