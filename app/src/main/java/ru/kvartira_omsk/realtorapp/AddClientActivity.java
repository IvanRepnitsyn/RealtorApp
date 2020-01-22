package ru.kvartira_omsk.realtorapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private Spinner spinnerTypeClient;
    //Spinner end
    private EditText etNameClient, etPhoneClient, etMailClient, etInfoFindObject;
    private Long mRowId;
    private DBWork mDbHelper;

    private MenuItem okMenuItem;
    private MenuItem delMenuItem;
    private MenuItem homeMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mDbHelper = new DBWork(this);

        etNameClient = (EditText) findViewById(R.id.client_name);
        etPhoneClient = (EditText) findViewById(R.id.client_phone);
        etMailClient = (EditText) findViewById(R.id.client_mail);
        //etInfoFindObject = (EditText) findViewById(R.id.findclient_info);

        /*String[] ITEMS = {"Продавец", "Покупатель"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerTypeClient = (MaterialSpinner) findViewById(R.id.spinner_addclient);
        SpinnerTypeClient.setAdapter(adapter);*/

        //Spinner begin
        spinnerTypeClient = (Spinner) findViewById(R.id.spinner_addclient);

        loadSpinnerTypeClient();
        //Spinner end

        mRowId = null;

        Bundle extras = getIntent().getExtras();

        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(DBWork.COLUMN_ID);
        if (extras != null) {
            //Toast.makeText(this, "Get ID OK", Toast.LENGTH_LONG).show();
            mRowId = getIntent().getExtras().getLong("idclient");
            //Toast.makeText(this, "ID "+mRowId, Toast.LENGTH_LONG).show();
            //PhoneNmb = extras.getString("callphone");
            //if (PhoneNmb != null) etPhoneClient.setText(PhoneNmb);

        }

        //populateFields();

        initToolbar();
        //initNavigationView();
        //initTabs();

        spinnerTypeClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout linLayout = (LinearLayout) findViewById(R.id.linearLayoutAddClient);
                if (position == 1) {
                    View viewFindObject = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_find_object, parent, false);
                    //Toast.makeText(AddClientActivity.this, "Выбоан покупатель2", Toast.LENGTH_LONG).show();
                    linLayout.addView(viewFindObject);
                    etInfoFindObject = (EditText) findViewById(R.id.findclient_info);
                    populateFieldsInfoObject();
                }
                if (position != 1) {
                    linLayout.removeAllViews();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        populateFields();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        homeMenuItem = menu.findItem(android.R.id.home);
        okMenuItem = menu.findItem(R.id.action_ok);
        delMenuItem = menu.findItem(R.id.action_delete);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setToolbarAttributes();
        return true;
    }



    private void populateFields() {
        if (mRowId != null) {
            //Toast.makeText(this, "ID pF "+mRowId, Toast.LENGTH_LONG).show();
            Cursor client = mDbHelper.getClient(mRowId);
// Может сообщение о косяке?
            /*if (client != null) Toast.makeText(this, "Получилось", Toast.LENGTH_LONG).show();
            else Toast.makeText(this, "Не получилось", Toast.LENGTH_LONG).show();*/
            startManagingCursor(client);

            String nameCln = client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT));

            //Toast.makeText(this, nameCln, Toast.LENGTH_LONG).show();
            spinnerTypeClient.setSelection(Integer.parseInt(client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_TYPECLIENT))));
            etNameClient.setText(client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT)));
            etPhoneClient.setText(client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_PHONECLIENT)));
            etMailClient.setText(client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_MAILCLIENT)));
            /*Toast.makeText(AddClientActivity.this, "Type " + client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_TYPECLIENT)), Toast.LENGTH_LONG).show();*/
            /*if (Integer.parseInt(client.getString(client
                    .getColumnIndexOrThrow(DBWork.COLUMN_TYPECLIENT))) == 2) {
                //etInfoFindObject = (EditText) findViewById(R.id.findclient_info);
                String stringIdClient = Long.toString(mRowId);
                Cursor cursorInfoObject = mDbHelper.getInfoObjectforId(stringIdClient);
                startManagingCursor(cursorInfoObject);
                String stringInfoObject = cursorInfoObject.getString(cursorInfoObject
                        .getColumnIndexOrThrow(DBWork.COLUMN_INFOFINDOBJECT));
                Toast.makeText(AddClientActivity.this, "Info1 " + stringInfoObject, Toast.LENGTH_LONG).show();
                stopManagingCursor(cursorInfoObject);
                cursorInfoObject.close();
                etInfoFindObject = (EditText) findViewById(R.id.findclient_info);
                etInfoFindObject.setText(stringInfoObject);
            }*/

            stopManagingCursor(client);
            client.close();
        }
    }

    private void populateFieldsInfoObject() {
        if (mRowId != null) {
            String stringIdClient = Long.toString(mRowId);
            Cursor cursorInfoObject = mDbHelper.getInfoObjectforId(stringIdClient);
            startManagingCursor(cursorInfoObject);
            String stringInfoObject = cursorInfoObject.getString(cursorInfoObject
                    .getColumnIndexOrThrow(DBWork.COLUMN_INFOFINDOBJECT));
            //Toast.makeText(AddClientActivity.this, "Info1 " + stringInfoObject, Toast.LENGTH_LONG).show();
            stopManagingCursor(cursorInfoObject);
            cursorInfoObject.close();
            etInfoFindObject = (EditText) findViewById(R.id.findclient_info);
            etInfoFindObject.setText(stringInfoObject);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_addclient);
        //toolbar.setTitle(R.string.new_object);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //delMenuItem.setVisible(false);
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

    private void setToolbarAttributes() {
        okMenuItem.setVisible(true);
        delMenuItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(AddClientActivity.this, "Press", Toast.LENGTH_LONG).show();
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(AddClientActivity.this, "Back", Toast.LENGTH_LONG).show();
                finish();
                return true;
            case R.id.action_ok:
                String checkClientStr = '\'' + etNameClient.getText().toString() + '\'';
                Cursor cursorClient = mDbHelper.getClientforName(checkClientStr);
                //Toast.makeText(AddClientActivity.this, "OK", Toast.LENGTH_LONG).show();
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
                        //Toast.makeText(AddClientActivity.this, "Клиент: "+etNameClient.getText().toString(), Toast.LENGTH_LONG).show();
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

        String typeclient = Integer.toString(spinnerTypeClient.getSelectedItemPosition());
        String nameclient = etNameClient.getText().toString();
        String phoneclient = etPhoneClient.getText().toString();
        String mailclient = etMailClient.getText().toString();
        String infoobject = "";
        if (spinnerTypeClient.getSelectedItemPosition() == 2) {
            infoobject = etInfoFindObject.getText().toString();
        }

        if (typeclient.length() == 0 && nameclient.length() == 0
                && phoneclient.length() == 0 && mailclient.length() == 0) {
            return;
        }

        if (mRowId == null) {
            //Toast.makeText(AddClientActivity.this, "Create " + infoobject, Toast.LENGTH_LONG).show();
            long id = mDbHelper.createNewClient(nameclient, typeclient, phoneclient, mailclient);
            if (infoobject != "") {
                String idclient = Long.toString(id);
                //Toast.makeText(AddClientActivity.this, "IdClient=  " + idclient + "Info= " + infoobject, Toast.LENGTH_LONG).show();
                long idinfoobject = mDbHelper.createNewInfoObject(idclient, infoobject);
            }
            if (id > 0) {
                mRowId = id;
            }
        } else {
            //Toast.makeText(AddClientActivity.this, "Update", Toast.LENGTH_LONG).show();
            mDbHelper.updateClient(mRowId, nameclient, typeclient, phoneclient, mailclient);
            if (infoobject != "") {
                String idclient = Long.toString(mRowId);
                Cursor cursorInfoObject = mDbHelper.getInfoObjectforId(idclient);
                startManagingCursor(cursorInfoObject);
                Long idInfoObject = cursorInfoObject.getLong(cursorInfoObject
                        .getColumnIndexOrThrow(DBWork.COLUMN_ID));
                mDbHelper.updateInfoObject(idInfoObject, idclient, infoobject);
                stopManagingCursor(cursorInfoObject);
                cursorInfoObject.close();
            }
        }
    }

    //Spinner begin
    private void loadSpinnerTypeClient() {
        String[] typedata = {"Продавец", "Покупатель", "Агент"};

        ArrayAdapter<String> typeadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typedata);
        typeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTypeClient.setAdapter(typeadapter);
    }
    //Spinner end



    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean isPhoneValid(CharSequence phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}
