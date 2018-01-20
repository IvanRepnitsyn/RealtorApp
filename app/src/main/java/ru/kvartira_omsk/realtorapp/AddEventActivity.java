package ru.kvartira_omsk.realtorapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
//import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
// com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;

import java.util.Calendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Иван on 14.02.2016.
 */
public class AddEventActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.add_event_activity;

    private Toolbar toolbar;
    private MaterialSpinner spinner_objects, spinner_clients, spinner_type, spinner_reminder;
    private EditText etDateEvent, etTimeEvent, etNameEvent, etPlaceEvent, etInfoEvent;
    private Long mRowId, idClient;
    private DBWork mDbHelper;


    int DIALOG_DATE = 1;
    private int myYear;
    private int myMonth;
    private int myDay;

    int DIALOG_TIME = 2;
    private int myHour;
    private int myMinute;
    private int mySecond;

    private MenuItem okMenuItem;
    private MenuItem delMenuItem;
    private MenuItem homeMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mDbHelper = new DBWork(this);

        spinner_objects = (MaterialSpinner) findViewById(R.id.spinner_addevent_objects);
        spinner_clients = (MaterialSpinner) findViewById(R.id.spinner_addevent_clients);
        spinner_type = (MaterialSpinner) findViewById(R.id.spinner_addevent_type);

        etNameEvent = (EditText) findViewById(R.id.event_name);
        etDateEvent = (EditText) findViewById(R.id.event_date);
        etTimeEvent = (EditText) findViewById(R.id.event_time);
        etPlaceEvent = (EditText) findViewById(R.id.event_place);
        etInfoEvent = (EditText) findViewById(R.id.event_info);

        mRowId = null;
        idClient = null;

        Bundle extras = getIntent().getExtras();

        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(DBWork.COLUMN_ID);
        if (extras != null) {
            //Toast.makeText(this, "Get ID OK", Toast.LENGTH_LONG).show();
            mRowId = getIntent().getExtras().getLong("idevent");
            idClient = extras.getLong("idclient");
            //Toast.makeText(this, "ID "+mRowId, Toast.LENGTH_LONG).show();
            //PhoneNmb = extras.getString("callphone");
            //if (PhoneNmb != null) etPhoneClient.setText(PhoneNmb);

        }
        Toast.makeText(this, "ID EditEvent "+mRowId, Toast.LENGTH_LONG).show();

        initToolbar();
        //initNavigationView();
        //initTabs();

        setCurrentDateTime();

        loadSpinnerObjectsData();
        loadSpinnerClientsData();

        String[] ITEMS_TYPE = {"Встреча", "Звонок", "Показ", "Сделка"};
        ArrayAdapter<String> adapter_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS_TYPE);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type = (MaterialSpinner) findViewById(R.id.spinner_addevent_type);
        spinner_type.setAdapter(adapter_type);

        String[] ITEMS_REMINDER = {"Не напоминать", "За 5 минут", "За 15 минут", "За 30 минут", "За 1 час"};
        ArrayAdapter<String> adapter_reminder = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS_REMINDER);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_reminder = (MaterialSpinner) findViewById(R.id.spinner_reminder);
        spinner_reminder.setAdapter(adapter_reminder);



        populateFields();

        etNameEvent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (etNameEvent.getText().toString().length() == 0){
                    String strTypeOut, strObjectOut;
                    if (spinner_type.getSelectedItemPosition() == 0) {
                        //Toast.makeText(AddObjectActivity.this, "Yes", Toast.LENGTH_LONG).show();
                        strTypeOut = "";
                    }
                    else {
                        //Toast.makeText(AddObjectActivity.this, "No", Toast.LENGTH_LONG).show();
                        strTypeOut = spinner_type.getSelectedItem().toString();
                    }
                    if (spinner_objects.getSelectedItemPosition() == 0) {
                        //Toast.makeText(AddObjectActivity.this, "Yes", Toast.LENGTH_LONG).show();
                        strObjectOut = "";
                    }
                    else {
                        //Toast.makeText(AddObjectActivity.this, "No", Toast.LENGTH_LONG).show();
                        strObjectOut = spinner_objects.getSelectedItem().toString();
                    }
                    String strEditText = strTypeOut+' '+strObjectOut;
                    etNameEvent.setText(strEditText);
                }

            }

        });

        /*String[] ITEMS_OBJECTS = {"Object 1", "Object 2", "Object 3", "Object 4", "Object 5", "Object 6"};
        ArrayAdapter<String> adapter_objects = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS_OBJECTS);
        adapter_objects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_objects = (MaterialSpinner) findViewById(R.id.spinner_addevent_objects);
        spinner_objects.setAdapter(adapter_objects);*/


        /*spinner_objects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    CallAddObject();

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });*/

        /*String[] ITEMS_CLIENTS = {"Client 1", "Client 2", "Client 3", "Client 4", "Client 5", "Client 6"};
        ArrayAdapter<String> adapter_clients = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS_CLIENTS);
        adapter_clients.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_clients = (MaterialSpinner) findViewById(R.id.spinner_addevent_clients);
        spinner_clients.setAdapter(adapter_clients);*/


        /*spinner_clients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    CallAddClient();

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });*/



        showDateDialog();
        showTimeDialog();

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

        spinner_objects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    CallAddObject();

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }

    public void showDateDialog() {
        etDateEvent = (EditText) findViewById(R.id.event_date);
        if ((etDateEvent.getText().toString() != null) & (etDateEvent.getText().toString().length() != 0)) {
            String[] partDate = etDateEvent.getText().toString().split("\\.");
            myDay = Integer.parseInt(partDate[0]);
            myMonth = Integer.parseInt(partDate[1])-1;
            myYear = Integer.parseInt(partDate[2]);
        }
        etDateEvent.setOnClickListener(
                new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(DIALOG_DATE);
                }
            }
        );
    }

    public void showTimeDialog() {
        etTimeEvent = (EditText) findViewById(R.id.event_time);
        if ((etTimeEvent.getText().toString() !=null) & (etTimeEvent.getText().toString().length() != 0)) {
            String[] partTime = etTimeEvent.getText().toString().split(":");
            myHour = Integer.parseInt(partTime[0]);
            myMinute = Integer.parseInt(partTime[1]);
        }
        etTimeEvent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_TIME);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog (int id) {
        if (id == DIALOG_DATE)
            return new DatePickerDialog(this, dpickerListener, myYear, myMonth, myDay);
        if (id == DIALOG_TIME)
            return new TimePickerDialog(this, tpickerListener, myHour, myMinute, false);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear+1;
            myDay = dayOfMonth;
            etDateEvent.setText(myDay + "." + myMonth + "." + myYear);
        }
    };

    private TimePickerDialog.OnTimeSetListener tpickerListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myHour = hourOfDay;
            myMinute = minute;
            etTimeEvent.setText(myHour + ":" + myMinute);
        }

    };

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

    public void setCurrentDateTime() {

        //Связываемся с системным календарем и берем из него для нашего
        //приложения значения текущего года, месяца и дня:
        final Calendar calendar = Calendar.getInstance();
        myYear = calendar.get(Calendar.YEAR);
        myMonth = calendar.get(Calendar.MONTH);
        myDay = calendar.get(Calendar.DAY_OF_MONTH);
        myHour = calendar.get(Calendar.HOUR_OF_DAY);
        myMinute = calendar.get(Calendar.MINUTE);

    }

    private void populateFields() {
        if ((mRowId != null) && (mRowId != 0)) {
            //Toast.makeText(this, "ID pF "+mRowId, Toast.LENGTH_LONG).show();
            Cursor event = mDbHelper.getEvent(mRowId);
            if (event != null) Toast.makeText(this, "Получилось event", Toast.LENGTH_LONG).show();
            else Toast.makeText(this, "Не получилось event", Toast.LENGTH_LONG).show();

            startManagingCursor(event);

            String nameEvent = event.getString(event
                    .getColumnIndexOrThrow(DBWork.COLUMN_NAMEEVENT));
            Toast.makeText(this, nameEvent, Toast.LENGTH_LONG).show();

            Long idobject = event.getLong(event.getColumnIndexOrThrow(DBWork.COLUMN_IDOBJECT));
            Cursor objectcursor = mDbHelper.getObject(idobject);
            String objectstr = objectcursor.getString(objectcursor.getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT));
            //Toast.makeText(this, "Объект: " + objectstr, Toast.LENGTH_LONG).show();
            //spinner.setSelection(getIndex(spinner, clientstr));
            //SpinnerAdapter adapterobj = (SpinnerAdapter) spinner_objects.getAdapter();
            //int positionclnt = adapterclnt.getPosition(clientstr);
            //String countObjectStr = Integer.toString(spinner_objects.getCount());
            //Toast.makeText(this, "Count " + countObjectStr, Toast.LENGTH_LONG).show();
            int indexObject = 0;
            for (int i=1;i<spinner_objects.getCount();i++){
                if (spinner_objects.getItemAtPosition(i).toString().equals(objectstr)){
                    //Toast.makeText(this, Integer.toString(i+1), Toast.LENGTH_LONG).show();
                    indexObject = i;
                    break;
                }
            }
            spinner_objects.setSelection(indexObject+1);

            Long idclient = event.getLong(event.getColumnIndexOrThrow(DBWork.COLUMN_IDCLIENT));
            Cursor clientcursor = mDbHelper.getClient(idclient);
            String clientstr = clientcursor.getString(clientcursor.getColumnIndexOrThrow(DBWork.COLUMN_NAMECLIENT));
            //Toast.makeText(this, clientstr, Toast.LENGTH_LONG).show();
            //spinner.setSelection(getIndex(spinner, clientstr));
            //SpinnerAdapter adapterclnt = (SpinnerAdapter) spinner_clients.getAdapter();
            //int positionclnt = adapterclnt.getPosition(clientstr);
            //String countClientStr = Integer.toString(spinner_clients.getCount());
            //Toast.makeText(this, "Count " + countClientStr, Toast.LENGTH_LONG).show();
            int indexClient = 0;
            for (int i=1;i<spinner_clients.getCount();i++){
                if (spinner_clients.getItemAtPosition(i).toString().equals(clientstr)){
                    //Toast.makeText(this, Integer.toString(i+1), Toast.LENGTH_LONG).show();
                    indexClient = i;
                    break;
                }
            }
            spinner_clients.setSelection(indexClient+1);

            int indexType = event.getInt(event.getColumnIndexOrThrow(DBWork.COLUMN_TYPEEVENT));
            //SpinnerAdapter adaptertype = (SpinnerAdapter) spinner_type.getAdapter();
            Toast.makeText(this, "Type " + indexType, Toast.LENGTH_LONG).show();
            spinner_type.setSelection(indexType);

            etNameEvent.setText(nameEvent);
            etDateEvent.setText(event.getString(event
                    .getColumnIndexOrThrow(DBWork.COLUMN_DATEEVENT)));
            /*String[] partDate = event.getString(event.getColumnIndexOrThrow(DBWork.COLUMN_DATEEVENT)).split("\\.");
            myDay = Integer.parseInt(partDate[0]);
            myMonth = Integer.parseInt(partDate[1])-1;
            myYear = Integer.parseInt(partDate[2]);*/
            etTimeEvent.setText(event.getString(event
                    .getColumnIndexOrThrow(DBWork.COLUMN_TIMEEVENT)));
            /*String[] partTime = event.getString(event.getColumnIndexOrThrow(DBWork.COLUMN_TIMEEVENT)).split(":");
            myHour = Integer.parseInt(partTime[0]);
            myMinute = Integer.parseInt(partTime[1]);*/
            etPlaceEvent.setText(event.getString(event
                    .getColumnIndexOrThrow(DBWork.COLUMN_PLACEEVENT)));
            etInfoEvent.setText(event.getString(event
                    .getColumnIndexOrThrow(DBWork.COLUMN_INFOEVENT)));
            spinner_reminder.setSelection(Integer.parseInt(event.getString(event
                    .getColumnIndexOrThrow(DBWork.COLUMN_REMINDEREVENT))));

            stopManagingCursor(event);
            event.close();
            stopManagingCursor(clientcursor);
            clientcursor.close();
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
        toolbar = (Toolbar) findViewById(R.id.toolbar_addevent);
        //toolbar.setTitle(R.string.new_object);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mRowId == null) getSupportActionBar().setTitle(R.string.new_event);
        else getSupportActionBar().setTitle(R.string.edit_event);
    }

    private void setToolbarAttributes() {
        okMenuItem.setVisible(true);
        delMenuItem.setVisible(false);
    }

    private void loadSpinnerClientsData() {
        DBWork dbHelper = new DBWork(getApplicationContext());

        List<String> clientsname = dbHelper.getAllClientsSpinner();
        clientsname.add(0, "Добавить контакт");

        //String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
        String[] CLIENTITEMS = new String[clientsname.size()];
        clientsname.toArray(CLIENTITEMS);
        ArrayAdapter<String> adapter_clients = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CLIENTITEMS);
        adapter_clients.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_clients.setAdapter(adapter_clients);
    }

    private void CallAddClient() {
        Intent intent = new Intent(this, AddClientActivity.class);
        //intent.putExtra(DBWork.COLUMN_ID, id);
        // активити вернет результат если будет вызвано с помощью этого метода
        Toast.makeText(this, "Вызов из AddEvent", Toast.LENGTH_LONG).show();
        startActivityForResult(intent, 23);
    }

    private void loadSpinnerObjectsData() {
        DBWork dbHelper = new DBWork(getApplicationContext());

        List<String> objectsname = dbHelper.getAllObjectsSpinner();
        objectsname.add(0, "Добавить объект");

        //String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
        String[] OBJECTITEMS = new String[objectsname.size()];
        objectsname.toArray(OBJECTITEMS);
        ArrayAdapter<String> adapter_objects = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, OBJECTITEMS);
        adapter_objects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_objects.setAdapter(adapter_objects);
    }

    private void CallAddObject() {
        Intent intent = new Intent(this, AddObjectActivity.class);
        //intent.putExtra(DBWork.COLUMN_ID, id);
        // активити вернет результат если будет вызвано с помощью этого метода
        Toast.makeText(this, "Вызов из AddEvent", Toast.LENGTH_LONG).show();
        startActivityForResult(intent, 21);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}

        switch (requestCode) {
            case 23:
                String nameclient = data.getStringExtra("nameclient");
                Toast.makeText(this, "Get name " + nameclient, Toast.LENGTH_LONG).show();

                loadSpinnerClientsData();

                int indexclient = 0;
                for (int i=1;i<spinner_clients.getCount();i++){
                    if (spinner_clients.getItemAtPosition(i).toString().equals(nameclient)){
                        //Toast.makeText(this, Integer.toString(i+1), Toast.LENGTH_LONG).show();
                        indexclient = i;
                        break;
                    }
                }

                spinner_clients.setSelection(indexclient+1);
                break;
            case 21:
                String nameobject = data.getStringExtra("nameobject");
                Toast.makeText(this, "Get object " + nameobject, Toast.LENGTH_LONG).show();

                loadSpinnerObjectsData();

                int indexobject = 0;
                for (int i=1;i<spinner_objects.getCount();i++){
                    if (spinner_objects.getItemAtPosition(i).toString().equals(nameobject)){
                        //Toast.makeText(this, Integer.toString(i+1), Toast.LENGTH_LONG).show();
                        indexobject = i;
                        break;
                    }
                }

                spinner_objects.setSelection(indexobject+1);
                break;


        }

    }

    private void saveState() {
        String nameobject = spinner_objects.getSelectedItem().toString();
        nameobject = '\''+nameobject+'\'';
        //Log.d(TAG, nameobject);
        Cursor cursorObject = mDbHelper.getObjectforName(nameobject);
        startManagingCursor(cursorObject);
        String idobject = cursorObject.getString(cursorObject.getColumnIndexOrThrow(DBWork.COLUMN_ID));
        //Toast.makeText(this, idobject, Toast.LENGTH_LONG).show();
        cursorObject.close();
        String nameclient = spinner_clients.getSelectedItem().toString();
        nameclient = '\''+nameclient+'\'';
        Cursor cursorClient = mDbHelper.getClientforName(nameclient);
        startManagingCursor(cursorClient);
        String idclient = cursorClient.getString(cursorClient.getColumnIndexOrThrow(DBWork.COLUMN_ID));
        String typeevent = Integer.toString(spinner_type.getSelectedItemPosition());
        Toast.makeText(this, "Save type:" + typeevent, Toast.LENGTH_LONG).show();
        String nameevent = etNameEvent.getText().toString();
        String dateevent = etDateEvent.getText().toString();
        String timeevent = etTimeEvent.getText().toString();
        String placeevent = etPlaceEvent.getText().toString();
        String infoevent = etInfoEvent.getText().toString();
        String reminderevent = Integer.toString(spinner_reminder.getSelectedItemPosition());
        //Notification
        if (spinner_reminder.getSelectedItemPosition() != 1) startNotification();
        //Notification
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
        //if (((mRowId == null) & (idclnt == null)) || ((mRowId == 0) & (idclnt > 0))) {
        if (mRowId == null) {
            //Toast.makeText(this, idclnt.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Create", Toast.LENGTH_LONG).show();
            long id = mDbHelper.createNewEvent(idobject, idclient, nameevent, typeevent, dateevent, timeevent, placeevent, infoevent, reminderevent);
            if (id > 0) {
                mRowId = id;
            }

        } else {
            Toast.makeText(this, "Update typeevent" + typeevent, Toast.LENGTH_LONG).show();
            mDbHelper.updateEvent(mRowId, idobject, idclient ,nameevent, typeevent, dateevent, timeevent, placeevent, infoevent, reminderevent);
        }

        stopManagingCursor(cursorObject);
        stopManagingCursor(cursorClient);
        cursorObject.close();
        cursorClient.close();
        //if ((mRowId != null) & (idclnt != null)) {
        /*if (mRowId != null) {
            //if ((mRowId > 0) & (idclnt == 0)) {
            if (mRowId > 0)  {
                Toast.makeText(this, "Update", Toast.LENGTH_LONG).show();
                mDbHelper.updateEvent(mRowId, idobject, idclient ,nameevent, typeevent, dateevent, timeevent, placeevent, infoevent);
            }
        }*/
    }

    private void startNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent;
        PendingIntent alarmPendingIntent;

        alarmIntent = new Intent(this, AlarmNotification.class);
        alarmPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000, alarmPendingIntent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_ok:
                String checkEventStr = '\'' + etNameEvent.getText().toString() + '\'';
                Cursor cursorEvent = mDbHelper.getEventforName(checkEventStr);
                Toast.makeText(AddEventActivity.this, "Нажали",
                        Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(etNameEvent.getText().toString())) {
                    Toast.makeText(AddEventActivity.this, "Данные не введены",
                            Toast.LENGTH_LONG).show();
                } else if ((cursorEvent != null) && (cursorEvent.getCount()>0) && ((mRowId == null) || (mRowId == 0)))  {
                    String nameeventstr = cursorEvent.getString(cursorEvent.getColumnIndexOrThrow(DBWork.COLUMN_NAMEEVENT));
                    Toast.makeText(AddEventActivity.this, "Имя события не уникально " + nameeventstr, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddEventActivity.this, "Сохраняем", Toast.LENGTH_LONG).show();
                    saveState();
                    Intent intent = new Intent();
                    intent.putExtra("nameevent", etNameEvent.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                stopManagingCursor(cursorEvent);
                cursorEvent.close();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
