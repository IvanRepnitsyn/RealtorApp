package ru.kvartira_omsk.realtorapp;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

//import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
//import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
// com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Иван on 14.02.2016.
 */
public class AddEventActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.add_event_activity;

    private Toolbar toolbar;
    private MaterialSpinner spinner_objects, spinner_clients, spinner_type;
    private EditText etDateEvent, etTimeEvent;

    int DIALOG_DATE = 1;
    private int myYear;
    private int myMonth;
    private int myDay;

    int DIALOG_TIME = 2;
    private int myHour;
    private int myMinute;
    private int mySecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);




        initToolbar();
        //initNavigationView();
        //initTabs();

        String[] ITEMS_OBJECTS = {"Object 1", "Object 2", "Object 3", "Object 4", "Object 5", "Object 6"};
        ArrayAdapter<String> adapter_objects = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS_OBJECTS);
        adapter_objects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_objects = (MaterialSpinner) findViewById(R.id.spinner_addevent_objects);
        spinner_objects.setAdapter(adapter_objects);

        String[] ITEMS_CLIENTS = {"Client 1", "Client 2", "Client 3", "Client 4", "Client 5", "Client 6"};
        ArrayAdapter<String> adapter_clients = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS_CLIENTS);
        adapter_clients.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_clients = (MaterialSpinner) findViewById(R.id.spinner_addevent_clients);
        spinner_clients.setAdapter(adapter_clients);

        String[] ITEMS_TYPE = {"Встреча", "Звонок", "Показ", "Сделка"};
        ArrayAdapter<String> adapter_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS_TYPE);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type = (MaterialSpinner) findViewById(R.id.spinner_addevent_type);
        spinner_type.setAdapter(adapter_type);

        showDateDialog();
        showTimeDialog();
    }

    public void showDateDialog() {
        etDateEvent = (EditText) findViewById(R.id.event_date);
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
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_addobject);
        //toolbar.setTitle(R.string.new_object);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //toolbar.setTitle();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.new_event);
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

        toolbar.inflateMenu(R.menu.menu);
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
}
