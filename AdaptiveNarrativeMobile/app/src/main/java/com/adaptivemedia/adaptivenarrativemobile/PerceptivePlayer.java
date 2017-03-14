package com.adaptivemedia.adaptivenarrativemobile;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.MediaController;

import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by chrsb on 29/01/2017.
 */

public class PerceptivePlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaController.MediaPlayerControl {

    private Activity activity;

    boolean isStopped = false;

    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private String audioFileName;
    private Handler handler = new Handler();
    private TextToSpeech tts;
    boolean isTtsQueued = false;


    public PerceptivePlayer(Activity activity) {
        this.activity = activity;

        // Create new TTS object
        tts=new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.UK);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });

        mediaController = new MediaController(activity);
        mediaPlayer = MediaPlayer.create(activity.getApplicationContext(), activity.getResources().getIdentifier("track", "raw", activity.getPackageName()));
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
        Thread ttsListener = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isStopped) {
                        if (getCurrentPosition() >= 10000 && getCurrentPosition() <= 15000) {
                            mediaPlayer.setVolume(0, 0);
                            if (!isTtsQueued) {
                                isTtsQueued = true;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    tts.speak("Music muted. Conversation detected.",TextToSpeech.QUEUE_FLUSH,null,null);
                                } else {
                                    tts.speak("Music muted. Conversation detected.", TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                        }
                        else {
                            mediaPlayer.setVolume(1, 1);
                            isTtsQueued = false;
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ttsListener.start();
    }

    public void onStop() {
        mediaController.hide();
        mediaPlayer.stop();
        mediaPlayer.release();
        isStopped = true;
    }

    public void show() {
        mediaController.show();
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(activity.findViewById(R.id.activity_mediaplayer));
        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onCompletion");

    }


    //--MediaPlayerControl methods----------------------------------------------------
    public void start() {
        mediaPlayer.start();
        isStopped = false;
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public int getAudioSessionId() {
        return 1;
    }
    //--------------------------------------------------------------------------------


}
