package ru.kvartira_omsk.realtorapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

import ru.kvartira_omsk.realtorapp.AddEventActivity;
import ru.kvartira_omsk.realtorapp.DBWork;
import ru.kvartira_omsk.realtorapp.R;
import ru.kvartira_omsk.realtorapp.adapter.EventsListAdapter;
import ru.kvartira_omsk.realtorapp.dto.EventDTO;

/**
 * Created by Иван on 31.01.2016.
 */
public class EventsFragment extends AbstractTabFragment {
    private DBWork dbHelper;

    private static final int LAYOUT = R.layout.fragment_events;
    //private static final int LAYOUT_MAIN = R.layout.activity_main;
    private static final int EVENT_ACTIVITY_CREATE = 22;
    private static final int EVENT_ACTIVITY_EDIT = 25;

    //FloatingActionButton AddObjectButton;


    public static EventsFragment getInstance(Context context) {
        Bundle args = new Bundle();
        EventsFragment fragment = new EventsFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_events));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        //final View view_main = inflater.inflate(LAYOUT_MAIN, container, false);

        //RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycleView_objects);
        //rv.setLayoutManager(new LinearLayoutManager(context)); //Дописать

        dbHelper = new DBWork(this.getActivity());
        Toast.makeText(this.getActivity(), "Create EventFragment", Toast.LENGTH_LONG).show();
        fillData();
        return view;
    }

    private List<EventDTO> createEventsList() {
        DBWork db = new DBWork(context.getApplicationContext());

        //List<String> clientlables = db.getAllClientsName();

        List<EventDTO> data = db.getAllEventsNameDTO();

        return data;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //return super.onContextItemSelected(item);
        //RecyclerContextMenuInfo info = (RecyclerContextMenuInfo) item.getMenuInfo();


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int clickedItemPosition = item.getOrder();
        long longID = (long) clickedItemPosition;

        if (item.getGroupId() == 2){
            switch (item.getItemId()) {
                case 1:
                    Toast.makeText(this.getActivity(), "ID Event "+clickedItemPosition, Toast.LENGTH_LONG).show();
                    Intent intent_event = new Intent(this.getActivity(), AddEventActivity.class);
                    intent_event.putExtra("idevent", longID);
                    Toast.makeText(this.getActivity(), "Вызов из EventFragment", Toast.LENGTH_LONG).show();
                    startActivityForResult(intent_event, EVENT_ACTIVITY_EDIT);
                    //MainActivity.editClient();
                    break;
                case 2:
                    dbHelper.deleteEvent(clickedItemPosition);
                    Toast.makeText(this.getActivity(), "Delete event "+clickedItemPosition, Toast.LENGTH_LONG).show();
                    fillData();
                    //return true;
                    break;


            }
        }


        return super.onContextItemSelected(item);
    }

    private void fillData() {
        RecyclerView rvEvents = (RecyclerView) view.findViewById(R.id.recycleView_events);
        rvEvents.setLayoutManager(new LinearLayoutManager(context));
        rvEvents.setAdapter(new EventsListAdapter(createEventsList()));
        //rvClients.setHasFixedSize(true);

        //registerForContextMenu(rvClients);
    }

    public void setContext(Context context) {

        this.context = context;
    }

    @Override
    public void onResume() {
        fillData();
        super.onResume();
    }
}
