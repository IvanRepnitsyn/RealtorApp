package ru.kvartira_omsk.realtorapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import ru.kvartira_omsk.realtorapp.AddClientActivity;
import ru.kvartira_omsk.realtorapp.AddObjectActivity;
import ru.kvartira_omsk.realtorapp.DBWork;
import ru.kvartira_omsk.realtorapp.MainActivity;
import ru.kvartira_omsk.realtorapp.R;
import ru.kvartira_omsk.realtorapp.adapter.ClientsListAdapter;
import ru.kvartira_omsk.realtorapp.adapter.ObjectsListAdapter;
import ru.kvartira_omsk.realtorapp.dto.ClientDTO;
import ru.kvartira_omsk.realtorapp.dto.ObjectDTO;

/**
 * Created by Иван on 31.01.2016.
 */
public class ObjectsFragment extends AbstractTabFragment {
    private DBWork dbHelper;

    private static final int LAYOUT = R.layout.fragment_objects;
    //private static final int LAYOUT_MAIN = R.layout.activity_main;
    private static final int OBJECT_ACTIVITY_CREATE = 11;
    private static final int OBJECT_ACTIVITY_EDIT = 15;

    //FloatingActionButton AddObjectButton;


    public static ObjectsFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ObjectsFragment fragment = new ObjectsFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_objects));

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
        //Toast.makeText(this.getActivity(), "Create ObjectFragment", Toast.LENGTH_LONG).show();
        fillData();
        return view;
    }

    private List<ObjectDTO> createObjectsList() {
        DBWork db = new DBWork(context.getApplicationContext());

        //List<String> clientlables = db.getAllClientsName();

        List<ObjectDTO> data = db.getAllObjectsNameDTO();

        return data;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //return super.onContextItemSelected(item);
        //RecyclerContextMenuInfo info = (RecyclerContextMenuInfo) item.getMenuInfo();


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int clickedItemPosition = item.getOrder();
        long longID = (long) clickedItemPosition;

        if (item.getGroupId() == 1){
            switch (item.getItemId()) {
                case 1:
                    //Toast.makeText(this.getActivity(), "ID Object "+clickedItemPosition, Toast.LENGTH_LONG).show();
                    Intent intent_object = new Intent(this.getActivity(), AddObjectActivity.class);
                    intent_object.putExtra("idobject", longID);
                    //Toast.makeText(this.getActivity(), "Вызов из ObjectFragment", Toast.LENGTH_LONG).show();
                    startActivityForResult(intent_object, OBJECT_ACTIVITY_EDIT);
                    //MainActivity.editClient();
                    break;
                case 2:
                    showDeleteObjectDialog(clickedItemPosition);
                    /*dbHelper.deleteObject(clickedItemPosition);
                    Toast.makeText(this.getActivity(), "Delete object"+clickedItemPosition, Toast.LENGTH_LONG).show();
                    fillData();*/
                    //return true;
                    break;


            }
        }


        return super.onContextItemSelected(item);
    }

    public void showDeleteObjectDialog(final long idObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        Cursor cursorObject = dbHelper.getObject(idObject);
        String nameObject = cursorObject.getString(cursorObject
                .getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT));
        String strTitle = "Удалить объект " + nameObject;
        builder.setTitle(strTitle);
        Cursor cursorEvent = dbHelper.getEventforObject(idObject);
        final int countEvent = cursorEvent.getCount();
        String messageAlertDialog = "Объект связан с "+ Integer.toString(countEvent) +" событиями. Удаление объекта приведет к удалению всех связанных с ним событий.";
        builder.setMessage(messageAlertDialog);

        //final boolean b = true;

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        dbHelper.deleteObject(idObject);
                        dbHelper.deleteObjectPhoto(idObject);
                        if (countEvent != 0) {
                            dbHelper.deleteEventbyIdObject(idObject);
                        }
                        fillData();
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

    private void fillData() {
        RecyclerView rvObjects = (RecyclerView) view.findViewById(R.id.recycleView_objects);
        rvObjects.setLayoutManager(new LinearLayoutManager(context));
        rvObjects.setAdapter(new ObjectsListAdapter(createObjectsList()));
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
