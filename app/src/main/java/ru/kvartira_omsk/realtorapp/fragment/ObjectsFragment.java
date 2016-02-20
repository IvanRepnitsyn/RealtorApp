package ru.kvartira_omsk.realtorapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ru.kvartira_omsk.realtorapp.AddObjectActivity;
import ru.kvartira_omsk.realtorapp.MainActivity;
import ru.kvartira_omsk.realtorapp.R;

/**
 * Created by Иван on 31.01.2016.
 */
public class ObjectsFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.fragment_objects;
    private static final int LAYOUT_MAIN = R.layout.activity_main;
    private static final int OBJECT_ACTIVITY_CREATE = 20;
    private static final int OBJECT_ACTIVITY_EDIT = 21;

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
        final View view_main = inflater.inflate(LAYOUT_MAIN, container, false);

        //RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycleView_objects);
        //rv.setLayoutManager(new LinearLayoutManager(context)); //Дописать



        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
