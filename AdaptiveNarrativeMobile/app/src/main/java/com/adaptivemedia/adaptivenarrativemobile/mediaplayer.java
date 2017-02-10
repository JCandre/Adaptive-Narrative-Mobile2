package com.adaptivemedia.adaptivenarrativemobile;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class mediaplayer extends AppCompatActivity {

    FloatingActionButton play, next, previous;

    private static final String TAG = "PlayTrack";
    public static final String AUDIO_FILE_NAME = "FileName";
    public static final String PLAY_BTN_RES = "quantum_ic_play_circle_filled_white_36";
    public static final String PAUSE_BTN_RES = "quantum_ic_pause_circle_filled_white_36";



    private PerceptivePlayer perceptivePlayer;
    private String audioFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplayer);
        setupActionBar();

        perceptivePlayer = new PerceptivePlayer(this);
        Intent intent = getIntent();
        audioFileName = intent.getStringExtra(AUDIO_FILE_NAME);
        ((TextView) findViewById(R.id.now_playing_text)).setText(audioFileName);

        //Play button onClick listener
        play = (FloatingActionButton) findViewById(R.id.playBTN);
        //play.setImageResource(R.drawable.quantum_ic_play_circle_filled_white_36);
        if (perceptivePlayer.isStopped == false){
            play.setImageResource(R.drawable.quantum_ic_pause_circle_filled_white_36);
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeRes();
            }
        });

        //next button onClick listener
        next = (FloatingActionButton) findViewById(R.id.nextBTN);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        //previous button onClick listener
        previous = (FloatingActionButton) findViewById(R.id.previousBTN);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }

    public void changeRes(){

        if (perceptivePlayer.isStopped == false){
            play.setImageResource(R.drawable.quantum_ic_play_circle_filled_white_36);
            perceptivePlayer.pause();
            perceptivePlayer.isStopped = true;
        } else {
            play.setImageResource(R.drawable.quantum_ic_pause_circle_filled_white_36);
            perceptivePlayer.start();
            perceptivePlayer.isStopped = false;
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        perceptivePlayer.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
        perceptivePlayer.show();
        return false;
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
