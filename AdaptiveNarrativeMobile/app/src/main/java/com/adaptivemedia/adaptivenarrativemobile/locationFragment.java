package com.adaptivemedia.adaptivenarrativemobile;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by Joel on 20/01/2017.
 */

public class locationFragment extends Fragment {
    View myView;
    TextView mLatitudeText, mLongitudeText;
    String longText = "0.000";
    String latText = "0.000";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        myView = inflater.inflate(R.layout.ldata_layout, container, false);
        mLongitudeText = (TextView) myView.findViewById(R.id.mLongitudeValue);
        mLatitudeText = (TextView) myView.findViewById(R.id.mLatitudeValue);
        mLongitudeText.setText(longText);
        mLatitudeText.setText(latText);

        return myView;
    }


    private void updateLong(String data){
        longText = data;
        mLongitudeText.setText(data);
    }

    private void updateLat(String data){
        latText = data;
        mLatitudeText.setText(data);
    }

    public void update(String longTxt, String latTxt){
        this.updateLong(longTxt);
        this.updateLat(latTxt);
    }

}
