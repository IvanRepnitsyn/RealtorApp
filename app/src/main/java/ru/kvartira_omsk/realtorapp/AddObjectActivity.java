package ru.kvartira_omsk.realtorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

/**
 * Created by Иван on 14.02.2016.
 */
public class AddObjectActivity extends AppCompatActivity {

    //private DBWork dbHelper;

    private static final int LAYOUT = R.layout.add_object_activity;

    private int REQUEST_CAMERA = 80, SELECT_FILE = 90;
    private ImageView ivImage;

    private Toolbar toolbar;
    private MaterialSpinner spinner_clients, spinner_numberroom, spinner_typeplan, spinner_bathroom, spinner_balcony, spinner_repairs, spinner_windows, spinner_viewfromwindows, spinner_material;
    private RadioGroup radiogroup_newbuild;
    private CheckBox chbIpoteka, chbClearsale, chbChange, chbCornerflat;
    //Spinner begin
    //private Spinner spinner_clients;
    //Spinner end


    ExpandableRelativeLayout expandableLayout1;

    private EditText etNameObject, etObjectAddress, etPriceClient, etAllSquare, etLiveSquare, etKitchenSquare, etFloor, etAllFloor, etYearConstruction, etAddInfo, etPriceSale;
    private Long mRowId, idClient;
    private DBWork mDbHelper;
    private String userChoosenTask;


    //For GridView
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private ImageAdapter imageAdapter;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    ArrayList<String> fileName = new ArrayList<String>(); // list of file name
    ArrayList<String> filePathEdit = new ArrayList<String>();
    ArrayList<String> fileNameEdit = new ArrayList<String>();

    File[] listFile;

    private boolean ifSelectItem;
    //For GridView

    private MenuItem okMenuItem;
    private MenuItem delMenuItem;
    private MenuItem homeMenuItem;

    private Boolean isAddImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        isAddImage = false;

        mDbHelper = new DBWork(this);

        spinner_clients = (MaterialSpinner) findViewById(R.id.spinner_addobject_clients);
        spinner_numberroom = (MaterialSpinner) findViewById(R.id.spinner_addobject_numberroom);
        spinner_typeplan = (MaterialSpinner) findViewById(R.id.spinner_addobject_typeplan);
        spinner_bathroom = (MaterialSpinner) findViewById(R.id.spinner_addobject_bathroom);
        spinner_balcony = (MaterialSpinner) findViewById(R.id.spinner_addobject_balcony);
        spinner_repairs = (MaterialSpinner) findViewById(R.id.spinner_addobject_repairs);
        spinner_windows = (MaterialSpinner) findViewById(R.id.spinner_addobject_windows);
        spinner_viewfromwindows = (MaterialSpinner) findViewById(R.id.spinner_addobject_viewfromwindows);
        spinner_material = (MaterialSpinner) findViewById(R.id.spinner_addobject_material);
        //Spinner begin
        //spinner_clients = (Spinner) findViewById(R.id.spinner_addobject_clients);
        //Spinner end
        etNameObject = (EditText) findViewById(R.id.object_name);
        etObjectAddress = (EditText) findViewById(R.id.object_address);
        etPriceClient = (EditText) findViewById(R.id.object_price);
        etAllSquare = (EditText) findViewById(R.id.object_allsquare);
        etLiveSquare = (EditText) findViewById(R.id.object_livesquare);
        etKitchenSquare = (EditText) findViewById(R.id.object_kitchensquare);
        etFloor = (EditText) findViewById(R.id.object_floor);
        etAllFloor = (EditText) findViewById(R.id.object_allfloor);
        etYearConstruction = (EditText) findViewById(R.id.object_yearconstruction);
        etAddInfo = (EditText) findViewById(R.id.object_addinfo);
        etPriceSale =(EditText) findViewById(R.id.object_pricesale);

        chbIpoteka = (CheckBox) findViewById(R.id.checkBox_ipoteka);
        chbClearsale = (CheckBox) findViewById(R.id.checkBox_clearsale);
        chbChange = (CheckBox) findViewById(R.id.checkBox_change);
        chbCornerflat = (CheckBox) findViewById(R.id.checkBox_cornerflat);


        radiogroup_newbuild = (RadioGroup) findViewById(R.id.radiogroup_newbuild);

        //Menu menuToolbar;

        mRowId = null;
        idClient = null;

        deleteRecursive(new File("/sdcard/RealtorAppPhotos/Temp/"));

        Bundle extras = getIntent().getExtras();

        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(DBWork.COLUMN_ID);
        if (extras != null) {
            //Toast.makeText(this, "Get extras object OK", Toast.LENGTH_LONG).show();
            mRowId = getIntent().getExtras().getLong("idobject");
            idClient = extras.getLong("idclient");
            //Toast.makeText(this, "ID Object "+mRowId, Toast.LENGTH_LONG).show();
            //PhoneNmb = extras.getString("callphone");
            //if (PhoneNmb != null) etPhoneClient.setText(PhoneNmb);

        } else {
            Toast.makeText(this, "Object Null", Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(this, "Variant 35", Toast.LENGTH_LONG).show();

        loadSpinnerClientsData();
        loadSpinnerNumberroom();
        loadSpinnerTypeplan();
        loadSpinnerBathroom();
        loadSpinnerBalcony();
        loadSpinnerRepairs();
        loadSpinnerWindows();
        loadSpinnerViewfromwindows();
        loadSpinnerMaterial();

        ifSelectItem = false;


        initToolbar();
        //initNavigationView();
        //initTabs();



        populateFields();



        etNameObject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Toast.makeText(AddObjectActivity.this, "Работает", Toast.LENGTH_LONG).show();
                if (etNameObject.getText().toString().length() == 0) {
                    String StrAddressIn = etObjectAddress.getText().toString();
                    //Toast.makeText(this, StrAddressIn, Toast.LENGTH_LONG).show();
                    //Toast.makeText(AddObjectActivity.this, StrAddressIn, Toast.LENGTH_LONG).show();
                    if (StrAddressIn.length() != 0) {
                        String strAddressOut = new String();
                        String strClientOut;
                        Integer i = 0;
                        boolean a = true;
                        while (a == true) {
                            //Toast.makeText(AddObjectActivity.this, StrAddressIn.charAt(i), Toast.LENGTH_SHORT).show();
                            if ((StrAddressIn.charAt(i) != ' ') & (StrAddressIn.charAt(i) != ',') & (i <= (StrAddressIn.length()-1))) {
                                strAddressOut = strAddressOut + StrAddressIn.charAt(i);

                            } else a = false;
                            if (i == (StrAddressIn.length()-1)) a = false;
                            i++;
                        }
                        if (spinner_clients.getSelectedItemPosition() == 0) {
                            //Toast.makeText(AddObjectActivity.this, "Yes", Toast.LENGTH_LONG).show();
                            strClientOut = "";
                        }
                        else {
                            //Toast.makeText(AddObjectActivity.this, "No", Toast.LENGTH_LONG).show();
                            strClientOut = spinner_clients.getSelectedItem().toString();
                        }
                        //Toast.makeText(AddObjectActivity.this, "Selected:" + spinner_clients.getSelectedItem().toString() + "Choice:" + strClientOut, Toast.LENGTH_LONG).show();
                        String strEditText = strAddressOut + ' ' + strClientOut;
                        etNameObject.setText(strEditText);
                    }

                }

            }

        });

        spinner_clients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    CallAddClient();

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });





        FloatingActionButton AddPhotoButton = (FloatingActionButton) findViewById(R.id.fabObject);
        AddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });



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
        if ((mRowId != null) && (mRowId != 0)) {
            //Toast.makeText(this, "ID pF "+mRowId, Toast.LENGTH_LONG).show();
            Cursor object = mDbHelper.getObject(mRowId);
            /*if (object != null) Toast.makeText(this, "Получилось object", Toast.LENGTH_LONG).show();
            else Toast.makeText(this, "Не получилось object", Toast.LENGTH_LONG).show();*/
            startManagingCursor(object);

            String nameObj = object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT));
            //Toast.makeText(this, nameObj, Toast.LENGTH_LONG).show();
            Long idclient = object.getLong(object.getColumnIndexOrThrow(DBWork.COLUMN_IDCLIENT));
            int idnumberroom = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_NUMBERROOM));
            int idnewbuild = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_NEWBUILD));
            int idtypeplan = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_TYPYPLAN));
            int idbathroom = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_BATHROOM));
            int idbalcony = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_BALCONY));
            int idrepairs = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_REPAIRS));
            int idwindows = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_WINDOWS));
            int idviewfromwindows = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_VIEWFROMWINDOWS));
            int idmaterial = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_MATERIAL));
            int idconditiondeal = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_CONDITIONDEAL));
            int idcornerflat = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_CORNERFLAT));
            /*float allsquare = object.getFloat(object.getColumnIndexOrThrow(DBWork.COLUMN_ALLSQUARE));
            float livesquare = object.getFloat(object.getColumnIndexOrThrow(DBWork.COLUMN_LIVESQUARE));
            float kitchensquare = object.getFloat(object.getColumnIndexOrThrow(DBWork.COLUMN_KITCHENSQUARE));*/
            //Toast.makeText(this, "Newbuild " + idnewbuild, Toast.LENGTH_LONG).show();
            Cursor clientcursor = mDbHelper.getClient(idclient);
            String clientstr = clientcursor.getString(clientcursor.getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT));
            //Toast.makeText(this, "Клиент: " + clientstr, Toast.LENGTH_LONG).show();
            //spinner.setSelection(getIndex(spinner, clientstr));
            SpinnerAdapter adapterclnt = (SpinnerAdapter) spinner_clients.getAdapter();
            //int positionclnt = adapterclnt.getPosition(clientstr);
            String countStr = Integer.toString(spinner_clients.getCount());
            //Toast.makeText(this, "Count " + countStr, Toast.LENGTH_LONG).show();
            int index = 0;
            for (int i=1;i<spinner_clients.getCount();i++){
                if (spinner_clients.getItemAtPosition(i).toString().equals(clientstr)){
                    //Toast.makeText(this, Integer.toString(i+1), Toast.LENGTH_LONG).show();
                    index = i;
                    break;
                }
            }

            spinner_clients.setSelection(index+1);
            /*spinner.setSelection(Integer.parseInt(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_IDCLIENT))));*/
            etNameObject.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT)));
            etObjectAddress.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_OBJECTADDRESS)));
            etPriceClient.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_PRICECLIENT)));
            spinner_numberroom.setSelection(idnumberroom);
            radiogroup_newbuild.check(radiogroup_newbuild.getChildAt(idnewbuild).getId());
            etAllSquare.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_ALLSQUARE)));
            etLiveSquare.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_LIVESQUARE)));
            etKitchenSquare.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_KITCHENSQUARE)));
            etFloor.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_FLOOR)));
            etAllFloor.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_ALLFLOOR)));
            spinner_typeplan.setSelection(idtypeplan);
            spinner_bathroom.setSelection(idbathroom);
            spinner_balcony.setSelection(idbalcony);
            spinner_repairs.setSelection(idrepairs);
            spinner_windows.setSelection(idwindows);
            spinner_viewfromwindows.setSelection(idviewfromwindows);
            spinner_material.setSelection(idmaterial);
            etYearConstruction.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_YEARCONSTRUCTION)));

            switch (idconditiondeal) {
                case 0:
                    chbIpoteka.setChecked(false);
                    chbClearsale.setChecked(false);
                    chbChange.setChecked(false);
                    break;
                case 1:
                    chbIpoteka.setChecked(true);
                    chbClearsale.setChecked(false);
                    chbChange.setChecked(false);
                    break;
                case 2:
                    chbIpoteka.setChecked(false);
                    chbClearsale.setChecked(true);
                    chbChange.setChecked(false);
                    break;
                case 3:
                    chbIpoteka.setChecked(true);
                    chbClearsale.setChecked(true);
                    chbChange.setChecked(false);
                    break;
                case 4:
                    chbIpoteka.setChecked(false);
                    chbClearsale.setChecked(false);
                    chbChange.setChecked(true);
                    break;
                case 5:
                    chbIpoteka.setChecked(true);
                    chbClearsale.setChecked(false);
                    chbChange.setChecked(true);
                    break;
                case 6:
                    chbIpoteka.setChecked(false);
                    chbClearsale.setChecked(true);
                    chbChange.setChecked(true);
                    break;
                case 7:
                    chbIpoteka.setChecked(true);
                    chbClearsale.setChecked(true);
                    chbChange.setChecked(true);
                    break;
            }
            if (idcornerflat == 0) chbCornerflat.setChecked(false);
            if (idcornerflat == 1) chbCornerflat.setChecked(true);
            etPriceSale.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_PRICESALE)));
            etAddInfo.setText(object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_ADDINFO)));
            stopManagingCursor(clientcursor);
            stopManagingCursor(object);
            clientcursor.close();
            object.close();

            //Image

            //Check table
            Cursor cursorObjectPhotos = mDbHelper.getAllObjectPhotos();
            //Toast.makeText(this, "SIZE OBJECTPHOTOS: " + cursorObjectPhotos.getCount(), Toast.LENGTH_LONG).show();
            //Check Table

            f.clear();
            filePathEdit.clear();
            fileNameEdit.clear();
            List<String> namePhotoObject = mDbHelper.getObjectPhotobyIdObject(Long.toString(mRowId));
            for (int i=0; i< namePhotoObject.size(); i++ ){
                f.add("/sdcard/RealtorAppPhotos/"+namePhotoObject.get(i));
                filePathEdit.add("/sdcard/RealtorAppPhotos/"+namePhotoObject.get(i));
                fileNameEdit.add(namePhotoObject.get(i));
                //Toast.makeText(this, "File from: /sdcard/RealtorAppPhotos/"+namePhotoObject.get(i), Toast.LENGTH_LONG).show();
            }

            /*GridView imagegrid = (GridView) findViewById(R.id.objectGridView);
            imageAdapter = new ImageAdapter();
            imagegrid.setAdapter(imageAdapter);
            //imagegrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

            //Toolbar localToolBar = getTo


            imagegrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Toast.makeText(AddObjectActivity.this, "Short48", Toast.LENGTH_LONG).show();
                    if (imageAdapter.checkSelectedItems()) {
                        ifSelectItem = true;
                        //toolbar.setBackgroundColor(Color.CYAN);
                        //getSupportActionBar().setBackgroundDrawable();
                        imageAdapter.putCheckedItems(position);
                        imageAdapter.notifyDataSetChanged();
                    } else {
                        ifSelectItem = false;
                    }
                    setToolbarAttributes();
                    //imageAdapter.setSelectedPosition(position);
                    //imageAdapter.notifyDataSetChanged();
                }
            });

            imagegrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Toast.makeText(AddObjectActivity.this, "Long48", Toast.LENGTH_LONG).show();
                    //imageAdapter.setSelectedPosition(position);
                    //imageAdapter.selectedPosition = position;
                    imageAdapter.putCheckedItems(position);
                    imageAdapter.notifyDataSetChanged();
                    setToolbarAttributes();
                    return true;
                }
            });*/

            //Image
        }

        if ((idClient != null) && (idClient != 0)) {
            Cursor clientcursor = mDbHelper.getClient(idClient);
            String clientstr = clientcursor.getString(clientcursor.getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT));
            String countStr = Integer.toString(spinner_clients.getCount());
            int index = 0;
            for (int i=1;i<spinner_clients.getCount();i++){
                if (spinner_clients.getItemAtPosition(i).toString().equals(clientstr)){
                    //Toast.makeText(this, Integer.toString(i+1), Toast.LENGTH_LONG).show();
                    index = i;
                    break;
                }
            }

            spinner_clients.setSelection(index+1);

            stopManagingCursor(clientcursor);
            clientcursor.close();
        }

        GridView imagegrid = (GridView) findViewById(R.id.objectGridView);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
        //imagegrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

        //Toolbar localToolBar = getTo


        imagegrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(AddObjectActivity.this, "Short53", Toast.LENGTH_LONG).show();
                if (imageAdapter.checkSelectedItems()) {
                    ifSelectItem = true;
                    //toolbar.setBackgroundColor(Color.CYAN);
                    //getSupportActionBar().setBackgroundDrawable();
                    imageAdapter.putCheckedItems(position);
                    imageAdapter.notifyDataSetChanged();
                } else {
                    ifSelectItem = false;
                }
                setToolbarAttributes();
                //imageAdapter.setSelectedPosition(position);
                //imageAdapter.notifyDataSetChanged();
            }
        });

        imagegrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int position, long id) {
                //Toast.makeText(AddObjectActivity.this, "Long53", Toast.LENGTH_LONG).show();
                //imageAdapter.setSelectedPosition(position);
                //imageAdapter.selectedPosition = position;
                imageAdapter.putCheckedItems(position);
                imageAdapter.notifyDataSetChanged();
                setToolbarAttributes();
                return true;
            }
        });


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_addobject);
        //toolbar.setTitle(R.string.new_object);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mRowId == null) getSupportActionBar().setTitle(R.string.new_object);
        else getSupportActionBar().setTitle(R.string.edit_object);
        /*if (toolbar != null) {
            setSupportActionBar(toolbar);
            //toolbar.setTitle();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.new_object);
            //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return false;
                }
            });

            //toolbar.inflateMenu(R.menu.menu);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);*/
    }

    private void setToolbarAttributes() {
       /* if (mRowId != null) {
            if (imageAdapter.getCheckedItems().size()!=0) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                getSupportActionBar().setTitle("Выделено " + imageAdapter.getCheckedItems().size() + " фото");
                okMenuItem.setVisible(false);
                delMenuItem.setVisible(true);
                //toolbar.inflateMenu(R.menu.menu_toolbar_delete);
                //toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_delete));
                //menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_launcher));
            } else {
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                okMenuItem.setVisible(true);
                delMenuItem.setVisible(false);
                if (mRowId == null) getSupportActionBar().setTitle(R.string.new_object);
                else getSupportActionBar().setTitle(R.string.edit_object);
            }
        } else {*/
            if (imageAdapter.getCheckedItems().size()!=0) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                getSupportActionBar().setTitle("Выделено " + imageAdapter.getCheckedItems().size() + " фото");
                okMenuItem.setVisible(false);
                delMenuItem.setVisible(true);
            } else {
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                okMenuItem.setVisible(true);
                delMenuItem.setVisible(false);
                if (mRowId == null) getSupportActionBar().setTitle(R.string.new_object);
                else getSupportActionBar().setTitle(R.string.edit_object);
            }
       /* } */


    }

    private void loadSpinnerClientsData() {
        DBWork dbHelper = new DBWork(getApplicationContext());

        List<String> clientsname = dbHelper.getAllClientsSpinner();
        clientsname.add(0, "Добавить контакт");

        //String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
        String[] ITEMS = new String[clientsname.size()];
        clientsname.toArray(ITEMS);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_clients.setAdapter(adapter);
    }

    private void loadSpinnerNumberroom() {
        //List<String> stringNumberroom = null;
        String[] ITEMSNUMBERROOM = {"1 комн.", "2 комн.", "3 комн.", "4 комн.", "5 комн."};
        //stringNumberroom.toArray(ITEMSNUMBERROOM);
        ArrayAdapter<String> adapterNumberroom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMSNUMBERROOM);
        adapterNumberroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_numberroom.setAdapter(adapterNumberroom);
    }

    private void loadSpinnerTypeplan() {
        //List<String> stringNumberroom = null;
        String[] ITEMSTYPEPLAN = {"изолированные комнаты", "смежные комнаты", "смежно-изолированные комнаты", "свободная планировка", "студия"};
        //stringNumberroom.toArray(ITEMSNUMBERROOM);
        ArrayAdapter<String> adapterTypeplan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMSTYPEPLAN);
        adapterTypeplan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_typeplan.setAdapter(adapterTypeplan);
    }

    private void loadSpinnerBathroom() {
        String[] ITEMSBATHROOM = {"раздельный", "совмещенный", "нет", "более одного санузла"};
        ArrayAdapter<String> adapterBathroom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMSBATHROOM);
        adapterBathroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bathroom.setAdapter(adapterBathroom);
    }

    private void loadSpinnerBalcony() {
        String[] ITEMSBALCONY = {"балкон", "лоджия", "нет"};
        ArrayAdapter<String> adapterBalcony = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMSBALCONY);
        adapterBalcony.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_balcony.setAdapter(adapterBalcony);
    }

    private void loadSpinnerRepairs() {
        String[] ITEMSREPAIRS = {"евроремонт", "дизайнерский ремонт", "требует ремонта", "ремонт от застройщика", "чистовая отделка", "черновая отделка"};
        ArrayAdapter<String> adapterRepairs = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMSREPAIRS);
        adapterRepairs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_repairs.setAdapter(adapterRepairs);
    }

    private void loadSpinnerWindows() {
        String[] ITEMSWINDOWS = {"деревянные", "пластиковые (ПВХ)"};
        ArrayAdapter<String> adapterWindows = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMSWINDOWS);
        adapterWindows.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_windows.setAdapter(adapterWindows);
    }

    private void loadSpinnerViewfromwindows() {
        String[] ITEMSVIEWFROMWINDOWS = {"во двор", "на улицу", "во двор и на улицу"};
        ArrayAdapter<String> adapterViewfromwindows = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMSVIEWFROMWINDOWS);
        adapterViewfromwindows.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_viewfromwindows.setAdapter(adapterViewfromwindows);
    }

    private void loadSpinnerMaterial() {
        String[] ITEMSMATERIAL = {"кирпичный", "панельный", "монолитный", "монолитно-кирпичный", "рубленный", "брусовой", "каркасно-насыпной", "прочее"};
        ArrayAdapter<String> adapterMaterial = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMSMATERIAL);
        adapterMaterial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_material.setAdapter(adapterMaterial);
    }

    private void CallAddClient() {
        Intent intent = new Intent(this, AddClientActivity.class);
        //intent.putExtra(DBWork.COLUMN_ID, id);
        // активити вернет результат если будет вызвано с помощью этого метода
        //Toast.makeText(this, "Вызов из AddObject", Toast.LENGTH_LONG).show();
        startActivityForResult(intent, 13);
    }

    /*private int getIndex(MaterialSpinner spinnerin, String myString)
    {
        int index = 0;
        String countStr = Integer.toString(spinnerin.getCount());
        Toast.makeText(AddObjectActivity.this, "Count" + countStr, Toast.LENGTH_LONG).show();
        for (int i=0;i<spinnerin.getCount();i++){
            String iStr = Integer.toString(i);


            if (spinnerin.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                //String iStr = Integer.toString(i);
                Toast.makeText(AddObjectActivity.this, iStr + spinnerin.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
                index = i;
                break;
            }
            Toast.makeText(AddObjectActivity.this, iStr, Toast.LENGTH_LONG).show();
        }
        String indexStr = Integer.toString(index);
        Toast.makeText(AddObjectActivity.this, indexStr, Toast.LENGTH_LONG).show();
        return index;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        switch (requestCode) {
            case 13:
                String nameclient = data.getStringExtra("nameclient");
                //Toast.makeText(this, "Get name " + nameclient, Toast.LENGTH_LONG).show();

                loadSpinnerClientsData();
                loadSpinnerNumberroom();
                loadSpinnerTypeplan();
                loadSpinnerBathroom();
                loadSpinnerBalcony();
                loadSpinnerWindows();
                loadSpinnerViewfromwindows();
                loadSpinnerMaterial();

                int index = 0;
                for (int i=1;i<spinner_clients.getCount();i++){
                    if (spinner_clients.getItemAtPosition(i).toString().equals(nameclient)){
                        //Toast.makeText(this, Integer.toString(i+1), Toast.LENGTH_LONG).show();
                        index = i;
                        break;
                    }
                }

                spinner_clients.setSelection(index+1);
                break;
            case 80:
                //Toast.makeText(this, "Это была камера", Toast.LENGTH_LONG).show();
                onCaptureImageResult(data);

                break;
            case 90:
                //Toast.makeText(this, "Это была галерея", Toast.LENGTH_LONG).show();
                onSelectFromGalleryResult(data);

                break;

        }
        if ((requestCode == 80) | (requestCode == 90)) {
            //GridView
            getFromSdcard();
            //LinearLayout linLayout = (LinearLayout) findViewById(R.id.linearLayoutObjectPhoto);

            GridView imagegrid = (GridView) findViewById(R.id.objectGridView);
            imageAdapter = new ImageAdapter();
            imagegrid.setAdapter(imageAdapter);
            imagegrid.requestFocus();
            //GridView
        }

    }

    private void saveState() {
        String nameobject = etNameObject.getText().toString();
        String nameclient = spinner_clients.getSelectedItem().toString();
        nameclient = '\''+nameclient+'\'';
        Cursor cursorClient = mDbHelper.getClientforName(nameclient);
        startManagingCursor(cursorClient);
        String idclient = cursorClient.getString(cursorClient.getColumnIndexOrThrow(DBWork.COLUMN_ID));
        //Toast.makeText(this, idclient, Toast.LENGTH_LONG).show();
        stopManagingCursor(cursorClient);
        cursorClient.close();
        String objectaddress = etObjectAddress.getText().toString();
        String priceclient = etPriceClient.getText().toString();
        String numberroom = Integer.toString(spinner_numberroom.getSelectedItemPosition());
        String checkedRadioBtnNewbuild = Integer.toString(radiogroup_newbuild.indexOfChild(findViewById(radiogroup_newbuild.getCheckedRadioButtonId())));
        String allsquare = etAllSquare.getText().toString();
        String livesquare = etLiveSquare.getText().toString();
        String kitchensquare = etKitchenSquare.getText().toString();
        String floor = etFloor.getText().toString();
        String allfloor = etAllFloor.getText().toString();
        String typeplan = Integer.toString(spinner_typeplan.getSelectedItemPosition());
        String bathroom = Integer.toString(spinner_bathroom.getSelectedItemPosition());
        String balcony = Integer.toString(spinner_balcony.getSelectedItemPosition());
        String repairs = Integer.toString(spinner_repairs.getSelectedItemPosition());
        String windows = Integer.toString(spinner_windows.getSelectedItemPosition());
        String viewfromwindows = Integer.toString(spinner_viewfromwindows.getSelectedItemPosition());
        String material = Integer.toString(spinner_material.getSelectedItemPosition());
        String yearconstruction = etYearConstruction.getText().toString();
        int conditional = 0;
        if (chbIpoteka.isChecked()) conditional = conditional+ 1;
        if (chbClearsale.isChecked()) conditional = conditional + 2;
        if (chbChange.isChecked()) conditional =  conditional + 4;
        String strConditional = Integer.toString(conditional);
        String addinfo = etAddInfo.getText().toString();
        String pricesale = etPriceSale.getText().toString();
        int cornerflat = 0;
        if (chbCornerflat.isChecked()) cornerflat = 1;
        String strCornerflat = Integer.toString(cornerflat);

        //Toast.makeText(this, "Add info" + addinfo, Toast.LENGTH_LONG).show();


        /*if (idclnt != null) {Toast.makeText(this, idclnt.toString(), Toast.LENGTH_LONG).show();}
        else Toast.makeText(this, "Косяк", Toast.LENGTH_LONG).show();*/
        if (nameobject.length() == 0 && nameclient.length() == 0 && objectaddress.length() == 0
                && priceclient.length() == 0) {
            Toast.makeText(this, "Нет данных", Toast.LENGTH_LONG).show();
            return;
        }
        /*String str = "mRowId=";
        if (mRowId == null) {
            str = str + "null, ";
        } else {
            str = str + mRowId.toString() + ", ";
        }
        str = str + "idclnt=";
        if (idclnt == null) {
            str = str + "null.";
        } else {
            str = str + idclnt.toString() + ".";
        }
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();*/
        //if (((mRowId == null) || (idclnt != null)) & (idclnt != 0))  {
        /*if (((mRowId == null) & (idclnt == null)) || ((mRowId == 0) & (idclnt > 0))) {*/
        if (((mRowId == null) & (idClient == null)) || ((mRowId == 0) & (idClient > 0)))  {
            //Toast.makeText(this, idclnt.toString(), Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "Create", Toast.LENGTH_LONG).show();
            long id = mDbHelper.createNewObject(nameobject, idclient,
                    objectaddress, priceclient, numberroom, checkedRadioBtnNewbuild, allsquare, livesquare,
                    kitchensquare, floor, allfloor, typeplan, bathroom, balcony, repairs, windows,
                    viewfromwindows, material, yearconstruction, strConditional, addinfo, pricesale, strCornerflat);
            if (id > 0) {
                mRowId = id;
            }

        } else {
            //Toast.makeText(this, "Update", Toast.LENGTH_LONG).show();
            mDbHelper.updateObject(mRowId, nameobject, idclient,
                    objectaddress, priceclient, numberroom, checkedRadioBtnNewbuild, allsquare, livesquare,
                    kitchensquare, floor, allfloor, typeplan, bathroom, balcony, repairs, windows,
                    viewfromwindows, material, yearconstruction, strConditional, addinfo, pricesale, strCornerflat);
        }

        // ObjectPhoto
        if (isAddImage) {
            mDbHelper.deleteObjectPhoto(mRowId);
            for (int i = 0; i < fileName.size(); i++) {
                long idObjectPhoto = mDbHelper.createNewObjectPhoto(Long.toString(mRowId), fileName.get(i));
                //Toast.makeText(this, "Save: " + fileName.get(i), Toast.LENGTH_LONG).show();
            }
            File sourceDir = new File("/sdcard/RealtorAppPhotos/Temp/");
            File destDir = new File("/sdcard/RealtorAppPhotos/");
            try {
                FileUtils.copyDirectory(sourceDir, destDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(AddObjectActivity.this, "Press", Toast.LENGTH_LONG).show();

        if (item.equals(okMenuItem)) {
            String checkObjectStr = '\'' + etNameObject.getText().toString() + '\'';
            Cursor cursorObject = mDbHelper.getObjectforName(checkObjectStr);
            if (TextUtils.isEmpty(etNameObject.getText().toString())) {
                Toast.makeText(AddObjectActivity.this, "Данные не введены", Toast.LENGTH_LONG).show();
            } else if ((cursorObject != null) && (cursorObject.getCount()>0) && ((mRowId == null) || (mRowId == 0)))  {
                String nameobjectstr = cursorObject.getString(cursorObject.getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT));
                Toast.makeText(AddObjectActivity.this, "Имя объекта не уникально " + nameobjectstr, Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(AddObjectActivity.this, "Сохраняем", Toast.LENGTH_LONG).show();
                saveState();
                Intent intent = new Intent();
                intent.putExtra("nameobject", etNameObject.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
            return super.onOptionsItemSelected(item);
        } else if (item.equals(delMenuItem)) {
            //Toast.makeText(AddObjectActivity.this, "Удаляем", Toast.LENGTH_LONG).show();
            showDeleteObjectDialog();
            return super.onOptionsItemSelected(item);
            }
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (delMenuItem.isVisible()){
                    imageAdapter.uncheckedAllItems();
                    imageAdapter.notifyDataSetChanged();
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    okMenuItem.setVisible(true);
                    delMenuItem.setVisible(false);
                    if (mRowId == null) getSupportActionBar().setTitle(R.string.new_object);
                    else getSupportActionBar().setTitle(R.string.edit_object);
                } else {
                    finish();
                }
                return true;
            /*case R.id.action_ok:
                String checkObjectStr = '\'' + etNameObject.getText().toString() + '\'';
                Cursor cursorObject = mDbHelper.getObjectforName(checkObjectStr);
                if (TextUtils.isEmpty(etNameObject.getText().toString())) {
                    Toast.makeText(AddObjectActivity.this, "Данные не введены",
                            Toast.LENGTH_LONG).show();
                } else if ((cursorObject != null) && (cursorObject.getCount()>0) && ((mRowId == null) || (mRowId == 0)))  {
                    String nameobjectstr = cursorObject.getString(cursorObject.getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT));
                    Toast.makeText(AddObjectActivity.this, "Имя объекта не уникально " + nameobjectstr, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddObjectActivity.this, "Сохраняем", Toast.LENGTH_LONG).show();
                    saveState();
                    Intent intent = new Intent();
                    intent.putExtra("nameobject", etNameObject.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                stopManagingCursor(cursorObject);
                cursorObject.close();
            case R.id.action_delete:

                Toast.makeText(AddObjectActivity.this, "Удаляем", Toast.LENGTH_LONG).show();*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    private void selectImage() {
        final CharSequence[] items = { "Камера", "Галерея", "Закрыть" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddObjectActivity.this);
        builder.setTitle("Добавить фото");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(AddObjectActivity.this);

                if (items[item].equals("Камера")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Галерея")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Закрыть")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void showDeleteObjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String messageAlertDialog = "Вы действительно хотите удалить выделенные фотографии";
        builder.setMessage(messageAlertDialog);

        //final boolean b = true;

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        ArrayList<String> itemForDelete = new ArrayList<String>();
                        ArrayList<String> itemForDeleteName = new ArrayList<String>();
                        //Toast.makeText(AddObjectActivity.this, "Size: " +  f.size(), Toast.LENGTH_LONG).show();
                        for (int i=0; i< f.size(); i++ ) {
                            if (imageAdapter.checkItemStatus(i) == true){
                                //Toast.makeText(AddObjectActivity.this, "Prosto!!!", Toast.LENGTH_LONG).show();
                                //Toast.makeText(AddObjectActivity.this, "Item: " + i + " Status TRUE " + f.get(i) + " NameFile: " + fileNameEdit.get(i), Toast.LENGTH_LONG).show();
                                itemForDelete.add(f.get(i));
                                itemForDeleteName.add(fileNameEdit.get(i));
                            } else {
                                //Toast.makeText(AddObjectActivity.this, "Item: " + i + "Status FALSE", Toast.LENGTH_LONG).show();
                            }
                            //Toast.makeText(AddObjectActivity.this, "Item for delete:  " + itemForDelete.size(), Toast.LENGTH_LONG).show();

                        }

                        for (int i=0; i< itemForDelete.size(); i++ ) {
                            File fileForDelete = new File(itemForDelete.get(i));
                            boolean deleted = fileForDelete.delete();
                            mDbHelper.deleteObjectPhotoByName(itemForDeleteName.get(i));
                            for (int j=0; j< f.size(); j++ ) {
                                if (f.get(j) == itemForDelete.get(i)){
                                    f.remove(j);
                                    break;
                                }
                            }
                        }
                        if (delMenuItem.isVisible()){
                            imageAdapter.uncheckedAllItems();
                            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            okMenuItem.setVisible(true);
                            delMenuItem.setVisible(false);
                            if (mRowId == null) getSupportActionBar().setTitle(R.string.new_object);
                            else getSupportActionBar().setTitle(R.string.edit_object);
                        }
                        imageAdapter.notifyDataSetChanged();
                        //File fileForDelete = new File(selectedFilePath);
                        //boolean deleted = file.delete();
                        dialog.cancel();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.cancel();
                    }
                });


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
        /*if (b == true) {
            Toast.makeText(this.getActivity(), "Set B true", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(this.getActivity(), "Set B false", Toast.LENGTH_LONG).show();
            return false;
        }*/
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        Time time = new Time();
        time.setToNow();
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        //String strFolder = Environment.getExternalStorageDirectory().toString() + R.string.photofolder_name;
        String strFolder = Environment.getExternalStorageDirectory().toString() + "/ReRealtorAppPhotos/Temp";
        File folder =  new  File(strFolder);
        if (!folder.exists()) {
            File wallpaperDirectory = new File("/sdcard/RealtorAppPhotos/Temp/");
            wallpaperDirectory.mkdirs();
        }

        String strFileName = null;
        strFileName = Integer.toString(time.year) + Integer.toString(time.month+1) + Integer.toString(time.monthDay) + Integer.toString(time.hour) + Integer.toString(time.minute) + Integer.toString(time.second) +".jpg";
            /*File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");*/
        //Toast.makeText(this,"Folder: " + strFolder + " File: " + strFileName, Toast.LENGTH_LONG).show();
        File destination = new File(new File("/sdcard/RealtorAppPhotos/Temp/"), strFileName);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "FileNotFound", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IOException1", Toast.LENGTH_LONG).show();
        }

        //ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Time time = new Time();
        time.setToNow();
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        String strFolder = Environment.getExternalStorageDirectory().toString() + "/ReRealtorAppPhotos/Temp";
        File folder =  new  File(strFolder);
        if (!folder.exists()) {
            File wallpaperDirectory = new File("/sdcard/RealtorAppPhotos/Temp/");
            wallpaperDirectory.mkdirs();
        }

        String strFileName = null;
        strFileName = Integer.toString(time.year) + Integer.toString(time.month+1) + Integer.toString(time.monthDay) + Integer.toString(time.hour) + Integer.toString(time.minute) + Integer.toString(time.second) +".jpg";
            /*File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");*/
        //Toast.makeText(this,"Folder: " + strFolder + " File: " + strFileName, Toast.LENGTH_LONG).show();
        File destination = new File(new File("/sdcard/RealtorAppPhotos/Temp/"), strFileName);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "FileNotFound", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IOException1", Toast.LENGTH_LONG).show();
        }
        //ivImage.setImageBitmap(bm);
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }




    /*@Override
    protected void onResume() {

        super.onResume();
        populateFields();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }*/
    public void getFromSdcard()
    {
        File file= new File("/sdcard/RealtorAppPhotos/Temp/");

        isAddImage = true;

        f.clear();
        fileName.clear();

        //Toast.makeText(AddObjectActivity.this, "mRowId: " + Long.toString(mRowId), Toast.LENGTH_LONG).show();

        if ((mRowId != null) && (mRowId != 0)) {
            for (int i=0; i< filePathEdit.size(); i++ ){
                f.add(filePathEdit.get(i));
                fileName.add(fileNameEdit.get(i));
            }
            //Toast.makeText(AddObjectActivity.this, "f size: " + f.size(), Toast.LENGTH_LONG).show();
        }


        if (file.isDirectory())
        {
            listFile = file.listFiles();


            for (int i = 0; i < listFile.length; i++) {
                f.add(listFile[i].getAbsolutePath());
                fileName.add(listFile[i].getName());
                fileNameEdit.add(listFile[i].getName());
                //Toast.makeText(AddObjectActivity.this, "Path:" + listFile[i].getAbsolutePath() + " Name:" + listFile[i].getName(), Toast.LENGTH_LONG).show();
            }
        }
        //Toast.makeText(AddObjectActivity.this, "f size after: " + f.size(), Toast.LENGTH_LONG).show();
    }


   /* public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }*/

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;
        //private int selectedPosition = -1;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mSparseBooleanArray = new SparseBooleanArray();
        }

        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArray = new ArrayList<String>();

            for (int i = 0; i < f.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArray.add(f.get(i));
                }
            }

            return mTempArray;
        }

        public void putCheckedItems(int position){
            if(mSparseBooleanArray.get(position))
                mSparseBooleanArray.put(position, false);
            else
                mSparseBooleanArray.put(position, true);
        }

        public void uncheckedAllItems(){
            for (int i=0; i< f.size(); i++ ) {
                mSparseBooleanArray.put(i, false);
            }
        }

        public boolean checkSelectedItems() {
            boolean b = false;
            for (int i = 0; i < f.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    b = true;
                }
            }
            return b;
        }

        public boolean checkItemStatus(int position){
            boolean b = false;
            if (mSparseBooleanArray.get(position)) {
                b = true;
            }
            return b;
        }

        public int getCount() {
            return f.size();
        }

        public String getItem(int position) {
            return f.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        /*public void setSelectedPosition(int position) {
            selectedPosition = position;
        }*/

        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.object_photo_view, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.smallphoto_object);
                holder.viewBackground =  (LinearLayout) convertView.findViewById(R.id.photoLinearLayout);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            //multyselect


            /*if (position == selectedPosition) {
                holder.imageview.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                holder.imageview.setBackgroundColor(Color.TRANSPARENT);
            }

            holder.imageview.setId(position);*/
            //holder.imageview.setLongClickable(true);
            /*holder.imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Toast.makeText(AddObjectActivity.this, "Short", Toast.LENGTH_LONG).show();


                }
            });
            holder.imageview.setOnLongClickListener(new View.OnLongClickListener() {

                public boolean onLongClick(View v) {
                    Toast.makeText(AddObjectActivity.this, "Long", Toast.LENGTH_LONG).show();

                    return true;
                }
            });*/

            //multyselect

            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
            holder.imageview.setImageBitmap(myBitmap);
            if(mSparseBooleanArray.get(position))
                holder.viewBackground.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            else
                holder.viewBackground.setBackgroundColor(Color.TRANSPARENT);
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;
        LinearLayout viewBackground;

    }


}


