package ru.kvartira_omsk.realtorapp.fragment;

import android.content.Context;
import androidx.fragment.app.Fragment;
import android.view.View;

/**
 * Created by Иван on 03.02.2016.
 */
public class AbstractTabFragment extends Fragment {

    private String title;
    protected Context context;
    protected View view;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
