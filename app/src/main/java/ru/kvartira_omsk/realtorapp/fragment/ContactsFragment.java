package ru.kvartira_omsk.realtorapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.kvartira_omsk.realtorapp.R;

/**
 * Created by Иван on 31.01.2016.
 */
public class ContactsFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.fragment_example;

    public static ContactsFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_contacts));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);


        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
