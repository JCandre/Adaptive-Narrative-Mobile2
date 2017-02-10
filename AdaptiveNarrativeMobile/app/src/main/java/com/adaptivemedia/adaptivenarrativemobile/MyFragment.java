package com.adaptivemedia.adaptivenarrativemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyFragment extends Fragment {

    public final static String AUDIO_FILE_NAME = "track.mp3";


    public static Fragment newInstance(MainActivity context, int pos, float scale) {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);
        return Fragment.instantiate(context, MyFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        LinearLayout l = (LinearLayout)
                inflater.inflate(R.layout.mf, container, false);


        int pos = this.getArguments().getInt("pos");
        final int pos2 = pos + 1;
        TextView tv = (TextView) l.findViewById(R.id.text);
        TextView synopsisTV = (TextView) l.findViewById(R.id.synopsis) ;
        tv.setText("Position = " + pos2);
        //synopsisTV.setText("Synopsis for position = " + pos2);
        ImageButton ib = (ImageButton) l.findViewById(R.id.imageButton);

        switch (pos) {
            case 0:
                tv.setText("Breaking Out");
                synopsisTV.setText(R.string.podcast_breakout);
                ib.setImageResource(R.mipmap.breakoutbg);
                break;

        }


        MyLinearLayout root = (MyLinearLayout) l.findViewById(R.id.root);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);


        FloatingActionButton launch = (FloatingActionButton) l.findViewById(R.id.launchPodcast);
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "play podcast " + pos2, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent playerIntent = new Intent("com.adaptivemedia.adaptivenarrativemobile.mediaplayer");
                playerIntent.putExtra("FileName", AUDIO_FILE_NAME);
                startActivity(playerIntent);
            }
        });

        return l;
    }
}
