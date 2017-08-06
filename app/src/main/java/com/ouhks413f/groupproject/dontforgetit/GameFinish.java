package com.ouhks413f.groupproject.dontforgetit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Created by ngnicola on 18/11/2016.
 */

public class GameFinish extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    static int gameMode = 0;
    static int level = 0;
    static boolean orderFinish = false;
    static long totalTime = 0;
    static int steps = 0;
    static int mistake = 0;
    static int numPair = 0;

    ImageView resultImage;
    Button home,restart;
    TextView resultView;
    SoundEffect soundEffect;
    boolean isChangePage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
        vf.setDisplayedChild(4);

        home = (Button)findViewById(R.id.homeButton);
        restart = (Button)findViewById(R.id.restartButton);
        resultImage = (ImageView)findViewById(R.id.resultImage);
        resultView = (TextView)findViewById(R.id.result);

        restart.setOnClickListener(restartGame);
        home.setOnClickListener(returnHome);

        soundEffect = new SoundEffect(this);
        isChangePage =false;

        showResult();
    }


    private View.OnClickListener restartGame = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            isChangePage = true;
            if(soundEffect.player!=null){
                soundEffect.player.stop();
            }
            Intent i = null;
            switch (gameMode){
                case 0:
                    i = new Intent(getApplicationContext(),ClassicPage.class);
                    break;
                case 1:
                    i = new Intent(getApplicationContext(),OrderPage.class);
                    break;
                case 2:
                    i = new Intent(getApplicationContext(),SpeedPage.class);
                    break;
            }
            startActivity(i);
        }
    };

    private View.OnClickListener returnHome = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            isChangePage = true;
            if(soundEffect.player!=null){
                soundEffect.player.stop();
            }
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    };

    public void showResult(){
        if(!isChangePage) {
            switch (gameMode) {
                case 0:
                    soundEffect.playSound(R.raw.clap);
                    resultImage.setImageResource(R.drawable.win);
                    String cText = "Congratulations!!\n You solved the puzzle in\n" + steps + " moves and " + totalTime / 1000 + " s.";
                    resultView.setText(cText);
                    break;
                case 1:
                    if (orderFinish) {
                        soundEffect.playSound(R.raw.clap);
                        resultImage.setImageResource(R.drawable.win);
                        String ofText = "Congratulations!!\n You solved the puzzle in\n" + steps + " moves and " + totalTime / 1000 + " s.";
                        resultView.setText(ofText);
                    } else {
                        soundEffect.gameOverPlay();
                        resultImage.setImageResource(R.drawable.cemetery);
                        String ofText = "Sorry.\nYou have " + mistake + " mistakes in this game.";
                        resultView.setText(ofText);
                    }
                    break;
                case 2:
                    soundEffect.playSound(R.raw.time_over);
                    resultImage.setImageResource(R.drawable.timeup);
                    String sText = "Congratulations!!\n You finish " + numPair + " pairs.";
                    resultView.setText(sText);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        isChangePage =true;
        if(soundEffect.player!=null)
            soundEffect.player.stop();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            isChangePage = true;
            if(soundEffect.player!=null){
                soundEffect.player.stop();
            }
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_newGame) {
            isChangePage = true;
            Intent i = null;
            switch (gameMode){
                case 0:
                    i = new Intent(getApplicationContext(),ClassicPage.class);
                    break;
                case 1:
                    i = new Intent(getApplicationContext(),OrderPage.class);
                    break;
                case 2:
                    i = new Intent(getApplicationContext(),SpeedPage.class);
                    break;
            }
            startActivity(i);

        } else if (id == R.id.nav_how) {
            switch (gameMode){
                case 0:
                    new AlertDialog.Builder(this)
                            .setTitle("About Classic Game")
                            .setMessage(R.string.classic_how)
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                    break;
                case 1:
                    new AlertDialog.Builder(this)
                            .setTitle("About Order Game")
                            .setMessage(R.string.order_how)
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                    break;
                case 2:
                    new AlertDialog.Builder(this)
                            .setTitle("About Speed Game")
                            .setMessage(R.string.speed_how)
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                    break;
            }

        } else if (id == R.id.nav_sound) {
            if(soundEffect.soundOn){
                soundEffect.soundOn = false;
                if(soundEffect.player!=null)
                    soundEffect.player.stop();
            }
            else
                soundEffect.soundOn = true;
        } else if (id == R.id.nav_vibrate) {
            if(soundEffect.vibrateOn)
                soundEffect.vibrateOn = false;
            else
                soundEffect.vibrateOn = true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
