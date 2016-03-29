package ru.kvartira_omsk.realtorapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.kvartira_omsk.realtorapp.DBWork;
import ru.kvartira_omsk.realtorapp.MainActivity;
import ru.kvartira_omsk.realtorapp.R;
import ru.kvartira_omsk.realtorapp.adapter.ClientsListAdapter;
import ru.kvartira_omsk.realtorapp.dto.ClientDTO;

/**
 * Created by Иван on 31.01.2016.
 */
public class ClientsFragment extends AbstractTabFragment {
    private DBWork mDbHelper;

    private static final int LAYOUT = R.layout.fragment_clients;

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

        RecyclerView rvClients = (RecyclerView) view.findViewById(R.id.recycleViewClients);
        rvClients.setLayoutManager(new LinearLayoutManager(context));
        rvClients.setAdapter(new ClientsListAdapter(createClientsList()));

        registerForContextMenu(rvClients);
        Toast.makeText(this.getActivity(), "List", Toast.LENGTH_LONG).show();

        return view;
    }

    private List<ClientDTO> createClientsList() {
        DBWork db = new DBWork(context.getApplicationContext());

        //List<String> clientlables = db.getAllClientsName();

        List<ClientDTO> data = db.getAllClientsNameDTO();

        return data;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Toast.makeText(this.getActivity(), "Menu", Toast.LENGTH_LONG).show();


        //menu.add(0, ADDOBJECT_ID, 0, "Добавить объект");
        //menu.add(0, ADDEVENT_ID, 0, "Добавить событие");
        menu.add(0, 1, 0, R.string.menu_delete);
        //menu.add(0, CALLCLIENT_ID, 0, "Позвонить");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                //Toast.makeText(MainActivity.this, "Delete", Toast.LENGTH_LONG).show();
                //fillData();
                return true;


        }
        return super.onContextItemSelected(item);
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
