package com.adaptivemedia.adaptivenarrativemobile;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Joel on 20/01/2017.
 */

public class LibraryFragment extends Fragment{
    View myView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        myView = inflater.inflate(R.layout.library_layout, container, false);
        return myView;

    }
}
