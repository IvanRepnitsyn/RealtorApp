package ru.kvartira_omsk.realtorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * Created by Иван on 14.02.2016.
 */
public class AddObjectActivity extends AppCompatActivity {

    //private DBWork dbHelper;

    private static final int LAYOUT = R.layout.add_object_activity;

    private int REQUEST_CAMERA = 80, SELECT_FILE = 90;
    private ImageView ivImage;

    private Toolbar toolbar;
    private MaterialSpinner spinner_clients;
    //Spinner begin
    //private Spinner spinner_clients;
    //Spinner end

    private EditText etNameObject, etObjectAddress, etPriceClient;
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
    //For GridView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mDbHelper = new DBWork(this);

        spinner_clients = (MaterialSpinner) findViewById(R.id.spinner_addobject_clients);
        //Spinner begin
        //spinner_clients = (Spinner) findViewById(R.id.spinner_addobject_clients);
        //Spinner end
        etNameObject = (EditText) findViewById(R.id.object_name);
        etObjectAddress = (EditText) findViewById(R.id.object_address);
        etPriceClient = (EditText) findViewById(R.id.object_price);

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
        loadSpinnerClientsData();




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
        return true;
    }

    private void populateFields() {
        if ((mRowId != null) && (mRowId != 0)) {
            //Toast.makeText(this, "ID pF "+mRowId, Toast.LENGTH_LONG).show();
            Cursor object = mDbHelper.getObject(mRowId);
            if (object != null) Toast.makeText(this, "Получилось object", Toast.LENGTH_LONG).show();
            else Toast.makeText(this, "Не получилось object", Toast.LENGTH_LONG).show();
            startManagingCursor(object);

            String nameObj = object.getString(object
                    .getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT));
            Toast.makeText(this, nameObj, Toast.LENGTH_LONG).show();
            Long idclient = object.getLong(object.getColumnIndexOrThrow(DBWork.COLUMN_IDCLIENT));
            Cursor clientcursor = mDbHelper.getClient(idclient);
            String clientstr = clientcursor.getString(clientcursor.getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT));
            Toast.makeText(this, "Клиент: " + clientstr, Toast.LENGTH_LONG).show();
            //spinner.setSelection(getIndex(spinner, clientstr));
            SpinnerAdapter adapterclnt = (SpinnerAdapter) spinner_clients.getAdapter();
            //int positionclnt = adapterclnt.getPosition(clientstr);
            String countStr = Integer.toString(spinner_clients.getCount());
            Toast.makeText(this, "Count " + countStr, Toast.LENGTH_LONG).show();
            int index = 0;
            for (int i=1;i<spinner_clients.getCount();i++){
                if (spinner_clients.getItemAtPosition(i).toString().equals(clientstr)){
                    Toast.makeText(this, Integer.toString(i+1), Toast.LENGTH_LONG).show();
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

            stopManagingCursor(clientcursor);
            stopManagingCursor(object);
            clientcursor.close();
            object.close();

            //Image
            f.clear();
            filePathEdit.clear();
            fileNameEdit.clear();
            List<String> namePhotoObject = mDbHelper.getObjectPhotobyIdObject(Long.toString(mRowId));
            for (int i=0; i< namePhotoObject.size(); i++ ){
                f.add("/sdcard/RealtorAppPhotos/"+namePhotoObject.get(i));
                filePathEdit.add("/sdcard/RealtorAppPhotos/"+namePhotoObject.get(i));
                fileNameEdit.add(namePhotoObject.get(i));
                Toast.makeText(this, "File from: /sdcard/RealtorAppPhotos/"+namePhotoObject.get(i), Toast.LENGTH_LONG).show();
            }

            GridView imagegrid = (GridView) findViewById(R.id.objectGridView);
            imageAdapter = new ImageAdapter();
            imagegrid.setAdapter(imageAdapter);
            //Image
        }

        if ((idClient != null) && (idClient != 0)) {
            Cursor clientcursor = mDbHelper.getClient(idClient);
            String clientstr = clientcursor.getString(clientcursor.getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT));
            String countStr = Integer.toString(spinner_clients.getCount());
            int index = 0;
            for (int i=1;i<spinner_clients.getCount();i++){
                if (spinner_clients.getItemAtPosition(i).toString().equals(clientstr)){
                    Toast.makeText(this, Integer.toString(i+1), Toast.LENGTH_LONG).show();
                    index = i;
                    break;
                }
            }

            spinner_clients.setSelection(index+1);

            stopManagingCursor(clientcursor);
            clientcursor.close();
        }


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

    private void CallAddClient() {
        Intent intent = new Intent(this, AddClientActivity.class);
        //intent.putExtra(DBWork.COLUMN_ID, id);
        // активити вернет результат если будет вызвано с помощью этого метода
        Toast.makeText(this, "Вызов из AddObject", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "Get name " + nameclient, Toast.LENGTH_LONG).show();

                loadSpinnerClientsData();

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
                Toast.makeText(this, "Это была камера", Toast.LENGTH_LONG).show();
                onCaptureImageResult(data);
                //GridView
                getFromSdcard();
                //LinearLayout linLayout = (LinearLayout) findViewById(R.id.linearLayoutObjectPhoto);

                GridView imagegrid = (GridView) findViewById(R.id.objectGridView);
                imageAdapter = new ImageAdapter();
                imagegrid.setAdapter(imageAdapter);
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
            Toast.makeText(this, "Create", Toast.LENGTH_LONG).show();
            long id = mDbHelper.createNewObject(nameobject, idclient, objectaddress, priceclient);
            if (id > 0) {
                mRowId = id;
            }

        } else {
            Toast.makeText(this, "Update", Toast.LENGTH_LONG).show();
            mDbHelper.updateObject(mRowId, nameobject, idclient, objectaddress, priceclient);
        }

        // ObjectPhoto
        mDbHelper.deleteObjectPhoto(mRowId);
        for (int i=0; i< fileName.size(); i++ ){
            long idObjectPhoto = mDbHelper.createNewObjectPhoto(Long.toString(mRowId), fileName.get(i));
            Toast.makeText(this, "Save: " + fileName.get(i), Toast.LENGTH_LONG).show();
        }
        File sourceDir = new File("/sdcard/RealtorAppPhotos/Temp/");
        File destDir = new File("/sdcard/RealtorAppPhotos/");
        try {
            FileUtils.copyDirectory(sourceDir, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(AddObjectActivity.this, "Press", Toast.LENGTH_LONG).show();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_ok:
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

                } else if (items[item].equals("Choose from Library")) {
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
        Toast.makeText(this,"Folder: " + strFolder + " File: " + strFileName, Toast.LENGTH_LONG).show();
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

        f.clear();
        fileName.clear();

        Toast.makeText(AddObjectActivity.this, "mRowId: " + Long.toString(mRowId), Toast.LENGTH_LONG).show();

        if ((mRowId != null) && (mRowId != 0)) {
            for (int i=0; i< filePathEdit.size(); i++ ){
                f.add(filePathEdit.get(i));
                fileName.add(fileNameEdit.get(i));
            }
            Toast.makeText(AddObjectActivity.this, "f size: " + f.size(), Toast.LENGTH_LONG).show();
        }


        if (file.isDirectory())
        {
            listFile = file.listFiles();


            for (int i = 0; i < listFile.length; i++)
            {

                f.add(listFile[i].getAbsolutePath());
                fileName.add(listFile[i].getName());
                Toast.makeText(AddObjectActivity.this, "Path:" + listFile[i].getAbsolutePath() + " Name:" + listFile[i].getName(), Toast.LENGTH_LONG).show();

            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.object_photo_view, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.smallphoto_object);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }


            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
            holder.imageview.setImageBitmap(myBitmap);
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;


    }


}


