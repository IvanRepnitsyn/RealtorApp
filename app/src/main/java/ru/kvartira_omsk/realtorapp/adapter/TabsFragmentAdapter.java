package ru.kvartira_omsk.realtorapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import ru.kvartira_omsk.realtorapp.fragment.AbstractTabFragment;
import ru.kvartira_omsk.realtorapp.fragment.ClientsFragment;
import ru.kvartira_omsk.realtorapp.fragment.EventsFragment;
import ru.kvartira_omsk.realtorapp.fragment.ObjectsFragment;

/**
 * Created by Иван on 31.01.2016.
 */
public class TabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;

    public TabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context=context;
        initTabMap(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {

        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    private void initTabMap(Context context) {
        tabs = new HashMap<>();
        tabs.put(0, ObjectsFragment.getInstance(context));
        tabs.put(1, EventsFragment.getInstance(context));
        tabs.put(2, ClientsFragment.getInstance(context));
    }
}
