package com.ouhks413f.groupproject.dontforgetit;

import android.content.Context;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ngnicola on 16/11/2016.
 */

public class SoundEffect {
    static boolean soundOn = true;
    /** Whether to vibrate the device or not. */
    static boolean vibrateOn = true;
    Context context;


    //private MediaPlayer player = new MediaPlayer();
    AsyncPlayer player = new AsyncPlayer("memory");
    static AsyncPlayer backgroundPlayer;
    private static Vibrator vibrater;
    private long[] pattern = new long[] { 0, 200, 200, 200 };

    SoundEffect(Context context){
        this.context = context;
        this.vibrater= (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    /** Plays a resource sound file if soundOn is set. */
    void playSound(int resId) {
        if(soundOn){
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
            player.play(context, uri, false,
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
            );
        }
    }

    void backgroundSound(int resId) {
        backgroundPlayer = new AsyncPlayer("memory");
        if(soundOn){
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
            backgroundPlayer.play(context,uri,true,
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
            );
        }
    }

    void gameOverPlay(){
        if(soundOn){
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    player.stop();
                }
            };
            playSound(R.raw.game_over);
            timer.schedule(timerTask,3000);
        }
    }


    /** Vibrates if vibrateOn is set. */
    void vibrate() {
        if (vibrateOn)
            vibrater.vibrate(pattern, -1);
    }
}
