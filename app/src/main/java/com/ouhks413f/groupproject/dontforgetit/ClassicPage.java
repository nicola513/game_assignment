package com.ouhks413f.groupproject.dontforgetit;

/**
 * Created by ngnicola on 15/11/2016.
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableLayout.LayoutParams;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ViewFlipper;



public class ClassicPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private TableLayout gameBoard;
    private Game game = new Game();
    private UserData userData = new UserData();

    private CountDownTimer mCountDownTimer;
    PopupWindow popUp;
    LinearLayout layout;
    TextView textView;
    LinearLayout.LayoutParams params;
    SoundEffect soundEffect;

    boolean isChangePage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        ViewFlipper viewFlipper = (ViewFlipper)findViewById(R.id.vf);
        viewFlipper.setDisplayedChild(1);

        gameBoard = (TableLayout) findViewById(R.id.board_table);
        soundEffect = new SoundEffect(this);


        resetGame();
        readyGo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                soundEffect.playSound(R.raw.ready_go);
                mCountDownTimer.start();
                layout.post(new Runnable() {
                    @Override
                    public void run() {
                        popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    }
                });
            }
        }, 0000);

    }

    //set the ready go
    private void readyGo(){
        mCountDownTimer = new CountDownTimer(3000,500){
            @Override
            public void onFinish() {
                popUp.dismiss();
                cardController();
                if(!isChangePage)
                soundEffect.backgroundSound(R.raw.get_outside);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished<2000){
                    textView.setText("Go!");
                }else {
                    textView.setText("Ready ?");

                }
            }
        };

        popUp = new PopupWindow(this);
        layout = new LinearLayout(this);
        textView = new TextView(this);
        textView.setTextColor(Color.BLACK);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(90);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(textView, params);
        popUp.setContentView(layout);
        popUp.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
    }

    private void cardController(){
        for (int i = 0; i < game.gameData.size(); i++) {
            game.gameData.get(i).state = CardData.State.Open;
        }

        for (int x = 0; x < gameBoard.getChildCount(); x++) {
            TableRow row = (TableRow) gameBoard.getChildAt(x);
            for (int j = 0; j < row.getChildCount(); j++) {
                View v = row.getChildAt(j);
                v.invalidate();
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < game.gameData.size(); i++) {
                    game.gameData.get(i).state = CardData.State.Close;
                }

                for (int x = 0; x < gameBoard.getChildCount(); x++) {
                    TableRow row = (TableRow) gameBoard.getChildAt(x);
                    for (int j = 0; j < row.getChildCount(); j++) {
                        View v = row.getChildAt(j);
                        v.invalidate();
                    }
                }
            }
        }, getResources().getInteger(R.integer.card_open_time));

    }

    private void resetGame() {
        isChangePage = false;
        game.generateData(this);
        gameBoard.removeAllViews();
        int index = 0;
        CardView cardView = new CardView(this,game.style);
        for (int i = 0; i < game.board_y; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
            gameBoard.addView(row);
            for (int j = 0; j < game.board_x; j++) {
                CardView view = new CardView(this, game.gameData.get(index++));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickView((CardView) v);
                    }
                });
                row.addView(view);
            }
        }

    }

    private void clickView(CardView cardView) {

        game.openCard(cardView.data);

        cardView.invalidate();

        if (game.hasBadPairOpen()) {
            soundEffect.vibrate();

        } else {
            soundEffect.playSound(R.raw.pop);
        }

        if (game.numberOpenCards() == 2) {
            cardView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    game.handleOpenCards();

                    for (int i=0; i<gameBoard.getChildCount();i++){
                        TableRow row = (TableRow)gameBoard.getChildAt(i);
                        for (int j=0; j<row.getChildCount(); j++) {
                            View v = row.getChildAt(j);
                            v.invalidate();
                        }
                    }

                    if (game.isGameWon()) {
                        long totalTime = game.endTime-game.startTime;
                        GameFinish.totalTime = totalTime;
                        GameFinish.steps = game.numMoves;
                        switch (game.board_y){
                            case 3:
                                if((totalTime<userData.classic_min_time_lower&&userData.classic_min_time_lower!=0)||userData.classic_min_time_lower==0){
                                    userData.classic_min_time_lower=totalTime;
                                }
                                break;
                            case 4:
                                if((totalTime<userData.classic_min_time_middle&&userData.classic_min_time_middle!=0)||userData.classic_min_time_middle==0){
                                    userData.classic_min_time_middle=totalTime;
                                }
                                break;
                            case 5:
                                if((totalTime<userData.classic_min_time_high&&userData.classic_min_time_high!=0)||userData.classic_min_time_high==0){
                                    userData.classic_min_time_high=totalTime;

                                }
                                break;
                        }
                        userData.writeData();
                        Intent i = new Intent(getApplicationContext(),GameFinish.class);
                        if(soundEffect.backgroundPlayer!=null){
                            soundEffect.backgroundPlayer.stop();
                            soundEffect.backgroundPlayer=null;
                        }
                        startActivity(i);
                    }
                }
            }, getResources().getInteger(R.integer.open_view_delay));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            isChangePage = true;
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            if(soundEffect.backgroundPlayer!=null){
                soundEffect.backgroundPlayer.stop();
            }
            soundEffect.backgroundPlayer=null;
            if(soundEffect.player!=null){
                soundEffect.player.stop();
            }
            startActivity(i);

        } else if (id == R.id.nav_newGame) {

            if(soundEffect.backgroundPlayer!=null)
            {
                soundEffect.backgroundPlayer.stop();
            }
            if(popUp!=null&&popUp.isShowing()){
                popUp.dismiss();
            }

            mCountDownTimer.cancel();
            resetGame();
            readyGo();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    soundEffect.playSound(R.raw.ready_go);
                    mCountDownTimer.start();
                    popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);

                }
            }, 0000);

        } else if (id == R.id.nav_how) {
            //TODO how to play this game
            new AlertDialog.Builder(this)
                    .setTitle("About Classic Game")
                    .setMessage(R.string.classic_how)
                    .setNeutralButton(android.R.string.ok, null)
                    .show();

        } else if (id == R.id.nav_sound) {
            if(soundEffect.soundOn){
                soundEffect.soundOn = false;
                if(soundEffect.player!=null){
                    soundEffect.player.stop();}
                if(soundEffect.backgroundPlayer!=null){
                    soundEffect.backgroundPlayer.stop();
                }
            }
            else{
                soundEffect.soundOn = true;
                soundEffect.backgroundSound(R.raw.get_outside);
            }
                //todo set sound in here
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