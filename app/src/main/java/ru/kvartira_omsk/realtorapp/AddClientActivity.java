package ru.kvartira_omsk.realtorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Иван on 14.02.2016.
 */
public class AddClientActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.add_client_activity;

    private Toolbar toolbar;
    private MaterialSpinner SpinnerTypeClient;
    private EditText etNameClient, etPhoneClient, etMailClient;
    private Long mRowId;
    private DBWork mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mDbHelper = new DBWork(this);

        etNameClient = (EditText) findViewById(R.id.client_name);
        etPhoneClient = (EditText) findViewById(R.id.client_phone);
        etMailClient = (EditText) findViewById(R.id.client_mail);


        initToolbar();
        //initNavigationView();
        //initTabs();

        String[] ITEMS = {"Продавец", "Покупатель"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerTypeClient = (MaterialSpinner) findViewById(R.id.spinner_addclient);
        SpinnerTypeClient.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_addclient);
        //toolbar.setTitle(R.string.new_object);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.new_client);
        /*if (toolbar != null) {
            setSupportActionBar(toolbar);
            //toolbar.setTitle();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.new_client);
            //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return false;
                }
            });

            //toolbar.inflateMenu(R.menu.menu);
        }*/

        /*toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu_toolbar);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(AddClientActivity.this, "Press", Toast.LENGTH_LONG).show();
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(AddClientActivity.this, "Back", Toast.LENGTH_LONG).show();
                finish();
                return true;
            case R.id.action_ok:
                Toast.makeText(AddClientActivity.this, "OK", Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(etNameClient.getText().toString())) {
                    Toast.makeText(AddClientActivity.this, "Данные не введены",
                            Toast.LENGTH_LONG).show();
                } else {
                    saveState();
                    Intent intent = new Intent();
                    intent.putExtra("nameclient", etNameClient.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveState() {

        String typeclient = Integer.toString(SpinnerTypeClient.getSelectedItemPosition());
        String nameclient = etNameClient.getText().toString();
        String phoneclient = etPhoneClient.getText().toString();
        String mailclient = etMailClient.getText().toString();

        if (typeclient.length() == 0 && nameclient.length() == 0
                && phoneclient.length() == 0 && mailclient.length() == 0) {
            return;
        }

        if (mRowId == null) {
            long id = mDbHelper.createNewClient(nameclient, typeclient, phoneclient, mailclient);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateClient(mRowId, nameclient, typeclient, phoneclient, mailclient);
        }
    }
}
