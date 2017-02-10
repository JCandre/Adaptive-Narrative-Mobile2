package com.adaptivemedia.adaptivenarrativemobile;

import android.app.Activity;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by Joel on 20/01/2017.
 */

public class locationFragment extends Fragment {
    View myView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.ldata_layout, container, false);




        return myView;
    }


}
