package ru.kvartira_omsk.realtorapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.kvartira_omsk.realtorapp.AddClientActivity;
import ru.kvartira_omsk.realtorapp.AddEventActivity;
import ru.kvartira_omsk.realtorapp.AddObjectActivity;
import ru.kvartira_omsk.realtorapp.ContextMenuRecyclerView;
import ru.kvartira_omsk.realtorapp.ContextMenuRecyclerView.RecyclerContextMenuInfo;
import ru.kvartira_omsk.realtorapp.DBWork;
import ru.kvartira_omsk.realtorapp.MainActivity;
import ru.kvartira_omsk.realtorapp.R;
import ru.kvartira_omsk.realtorapp.adapter.ClientsListAdapter;
import ru.kvartira_omsk.realtorapp.dto.ClientDTO;

/**
 * Created by Иван on 31.01.2016.
 */
public class ClientsFragment extends AbstractTabFragment {
    private DBWork dbHelper;

    private static final int LAYOUT = R.layout.fragment_clients;
    private static final int CLIENT_ACTIVITY_CREATE = 33;
    private static final int CLIENT_ACTIVITY_EDIT = 35;
    private static final int OBJECT_ACTIVITY_CREATE = 31;
    private static final int EVENT_ACTIVITY_CREATE = 32;

    private long id;

    public RecyclerView rvClients;

    public static ClientsFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ClientsFragment fragment = new ClientsFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_contacts));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        /*RecyclerView rvClients = (RecyclerView) view.findViewById(R.id.recycleViewClients);
        rvClients.setLayoutManager(new LinearLayoutManager(context));
        rvClients.setAdapter(new ClientsListAdapter(createClientsList()));

        registerForContextMenu(rvClients);*/
        //Toast.makeText(this.getActivity(), "List", Toast.LENGTH_LONG).show();
        dbHelper = new DBWork(this.getActivity());
        Toast.makeText(this.getActivity(), "Create ClientFragment", Toast.LENGTH_LONG).show();
        fillData();
        return view;
    }

    private List<ClientDTO> createClientsList() {
        DBWork db = new DBWork(context.getApplicationContext());

        //List<String> clientlables = db.getAllClientsName();

        List<ClientDTO> data = db.getAllClientsNameDTO();

        return data;
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Toast.makeText(this.getActivity(), "Menu", Toast.LENGTH_LONG).show();


        //menu.add(0, ADDOBJECT_ID, 0, "Добавить объект");
        //menu.add(0, ADDEVENT_ID, 0, "Добавить событие");
        menu.add(0, 1, 0, R.string.menu_delete);
        //menu.add(0, CALLCLIENT_ID, 0, "Позвонить");
    }*/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //return super.onContextItemSelected(item);
        //RecyclerContextMenuInfo info = (RecyclerContextMenuInfo) item.getMenuInfo();


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int clickedItemPosition = item.getOrder();
        long longID = (long) clickedItemPosition;

        if (item.getGroupId() == 3){
            switch (item.getItemId()) {
                case 1:
                    Toast.makeText(this.getActivity(), "ID Client "+clickedItemPosition, Toast.LENGTH_LONG).show();
                    Intent intent_client = new Intent(this.getActivity(), AddClientActivity.class);
                    intent_client.putExtra("idclient", longID);
                    Toast.makeText(this.getActivity(), "Вызов из ClientFragment", Toast.LENGTH_LONG).show();
                    startActivityForResult(intent_client, CLIENT_ACTIVITY_EDIT);
                    //MainActivity.editClient();
                    break;
                case 2:
                    dbHelper.deleteClient(clickedItemPosition);
                    Toast.makeText(this.getActivity(), "Delete client "+clickedItemPosition, Toast.LENGTH_LONG).show();
                    fillData();
                    //return true;
                    break;
                case 3:
                    Intent intent_object = new Intent(this.getActivity(), AddObjectActivity.class);
                    intent_object.putExtra("idclient", longID);
                    startActivityForResult(intent_object, OBJECT_ACTIVITY_CREATE);
                    break;
                case 4:
                    Intent intent_event = new Intent(this.getActivity(), AddEventActivity.class);
                    intent_event.putExtra("idclient", longID);
                    startActivityForResult(intent_event, EVENT_ACTIVITY_CREATE);
                    break;
            }
        }


        return super.onContextItemSelected(item);
    }

    private void fillData() {
        RecyclerView rvClients = (RecyclerView) view.findViewById(R.id.recycleViewClients);
        rvClients.setLayoutManager(new LinearLayoutManager(context));
        rvClients.setAdapter(new ClientsListAdapter(createClientsList()));
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
