package ru.kvartira_omsk.realtorapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Иван on 14.02.2016.
 */
public class AddClientActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.add_client_activity;

    private Toolbar toolbar;
    //private MaterialSpinner SpinnerTypeClient;
    //Spinner begin
    private Spinner SpinnerTypeClient;
    //Spinner end
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

        /*String[] ITEMS = {"Продавец", "Покупатель"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerTypeClient = (MaterialSpinner) findViewById(R.id.spinner_addclient);
        SpinnerTypeClient.setAdapter(adapter);*/

        //Spinner begin
        SpinnerTypeClient = (Spinner) findViewById(R.id.spinner_addclient);

        loadSpinnerTypeClient();
        //Spinner end

        mRowId = null;

        Bundle extras = getIntent().getExtras();

        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(DBWork.COLUMN_ID);
        if (extras != null) {
            Toast.makeText(this, "Get ID OK", Toast.LENGTH_LONG).show();
            mRowId = getIntent().getExtras().getLong("idclient");
            Toast.makeText(this, "ID "+mRowId, Toast.LENGTH_LONG).show();
            //PhoneNmb = extras.getString("callphone");
            //if (PhoneNmb != null) etPhoneClient.setText(PhoneNmb);

        }

        populateFields();

        initToolbar();
        //initNavigationView();
        //initTabs();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private void populateFields() {
        if (mRowId != null) {
            //Toast.makeText(this, "ID pF "+mRowId, Toast.LENGTH_LONG).show();
            Cursor client = mDbHelper.getClient(mRowId);
            if (client != null) Toast.makeText(this, "Получилось", Toast.LENGTH_LONG).show();
            else Toast.makeText(this, "Не получилось", Toast.LENGTH_LONG).show();
            startManagingCursor(client);

            String nameCln = client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT));

            Toast.makeText(this, nameCln, Toast.LENGTH_LONG).show();
            SpinnerTypeClient.setSelection(Integer.parseInt(client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_TYPECLIENT))));
            etNameClient.setText(client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT)));
            etPhoneClient.setText(client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_PHONECLIENT)));
            etMailClient.setText(client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_MAILCLIENT)));

            stopManagingCursor(client);
            client.close();
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_addclient);
        //toolbar.setTitle(R.string.new_object);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mRowId == null) getSupportActionBar().setTitle(R.string.new_client);
        else getSupportActionBar().setTitle(R.string.edit_client);
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
                String checkClientStr = '\'' + etNameClient.getText().toString() + '\'';
                Cursor cursorClient = mDbHelper.getClientforName(checkClientStr);
                Toast.makeText(AddClientActivity.this, "OK", Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(etNameClient.getText().toString())) {
                    Toast.makeText(AddClientActivity.this, "Данные не введены", Toast.LENGTH_LONG).show();
                } else if ((cursorClient != null) && (cursorClient.getCount()>0) && ((mRowId == null) || (mRowId == 0)))  {
                    String nameclientstr = cursorClient.getString(cursorClient.getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT));
                    Toast.makeText(AddClientActivity.this, "Имя клиента не уникально " + nameclientstr, Toast.LENGTH_LONG).show();
                } else {

                    if ((isEmailValid(etMailClient.getText())) && (isPhoneValid(etPhoneClient.getText()))){
                        saveState();
                        Intent intent = new Intent();
                        intent.putExtra("nameclient", etNameClient.getText().toString());
                        Toast.makeText(AddClientActivity.this, "Клиент: "+etNameClient.getText().toString(), Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else if (isEmailValid(etMailClient.getText()) == false){
                        Toast.makeText(AddClientActivity.this, "Некоректный Email", Toast.LENGTH_LONG).show();
                    } else if (isPhoneValid(etPhoneClient.getText()) == false){
                        Toast.makeText(AddClientActivity.this, "Некоректный телефон", Toast.LENGTH_LONG).show();
                    }

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
            Toast.makeText(AddClientActivity.this, "Create", Toast.LENGTH_LONG).show();
            long id = mDbHelper.createNewClient(nameclient, typeclient, phoneclient, mailclient);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            Toast.makeText(AddClientActivity.this, "Update", Toast.LENGTH_LONG).show();
            mDbHelper.updateClient(mRowId, nameclient, typeclient, phoneclient, mailclient);
        }
    }

    //Spinner begin
    private void loadSpinnerTypeClient() {
        String[] typedata = {"Продавец", "Покупатель"};

        ArrayAdapter<String> typeadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typedata);
        typeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpinnerTypeClient.setAdapter(typeadapter);
    }
    //Spinner end

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean isPhoneValid(CharSequence phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}
