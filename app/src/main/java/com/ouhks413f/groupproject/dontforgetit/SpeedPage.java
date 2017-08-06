package com.ouhks413f.groupproject.dontforgetit;

/**
 * Created by ngnicola on 15/11/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableLayout.LayoutParams;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class SpeedPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private TableLayout gameBoard;
    Game game = new Game();
    UserData userData = new UserData();

    private ProgressBar progressBar;
    private ImageView imageView;
    static CountDownTimer timer = null;
    int addTime=0;
    long countdownPeriod;
    Animation animShake;

    private CountDownTimer mCountDownTimer = null;
    PopupWindow popUp = null;
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
        viewFlipper.setDisplayedChild(3);
        isChangePage=false;


        //set timer bar and clock image data
        progressBar = (ProgressBar) findViewById(R.id.bar);
        imageView = (ImageView)findViewById(R.id.clock);
        imageView.setImageResource(R.drawable.clock);
        animShake = AnimationUtils.loadAnimation(this, R.anim.clock_shake);
        progressBar.setProgress(100);
        countdownPeriod = 30000;


        soundEffect = new SoundEffect(this);
        soundEffect.soundOn = true;
        soundEffect.vibrateOn = true;

        gameBoard = (TableLayout)findViewById(R.id.speed_board_table);
        game.numPair =0;

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

    //show ready go before game start
    private void readyGo(){
        mCountDownTimer = new CountDownTimer(3000,500){
            @Override
            public void onFinish() {
                popUp.dismiss();

                cardController();
                if(!isChangePage)
                    soundEffect.backgroundSound(R.raw.space_adventure);

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

    //control the card open and close when reset game
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

                gameTimer();
            }
        },  getResources().getInteger(R.integer.card_open_time));

    }

    private void resetGame() {
        game.generateData(this);
        gameBoard.removeAllViews();
        CardView cardView = new CardView(this,game.style);
        int index = 0;
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


    private void gameTimer(){
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(countdownPeriod + 10000 * addTime ,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                addTime=0;
                progressBar.setProgress((int)(millisUntilFinished/500*2));
                if(millisUntilFinished/1000>=5){
                    imageView.startAnimation(animShake);
                }
                countdownPeriod=millisUntilFinished;
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                imageView.clearAnimation();
                timer.cancel();
                timer = null;
                if(game.numPair>userData.speed_num_pair){
                    userData.speed_num_pair=game.numPair;
                    userData.writeData();
                }

                GameFinish.numPair=game.numPair;
                if(soundEffect.backgroundPlayer!=null){
                    soundEffect.backgroundPlayer.stop();
                    soundEffect.backgroundPlayer=null;
                }
                Intent i = new Intent(getApplicationContext(),GameFinish.class);
                startActivity(i);
            }
        }.start();


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

                    //TODO reset the timer and reset the game
                    /*
                    * when user finish one set of card, addTime +1 and reset the timer.
                    * The time will increase ten second
                    * reset the game
                    * */
                    if (game.isGameWon()) {
                        if(countdownPeriod+10000>30000)
                            countdownPeriod = 30000;
                        else addTime++;
                        timer.cancel();
                        resetGame();
                        cardController();
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
            isChangePage=true;
            if(timer != null) {
                timer.cancel();
                timer = null;
            }
            if(soundEffect.backgroundPlayer!=null){
                soundEffect.backgroundPlayer.stop();
            }
            if(soundEffect.player!=null){
                soundEffect.player.stop();
            }
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_newGame) {
            if(popUp!=null&&popUp.isShowing()){
                popUp.dismiss();
            }
            if(mCountDownTimer!=null){
                mCountDownTimer.cancel();
            }
            if(soundEffect.backgroundPlayer!=null){
                soundEffect.backgroundPlayer.stop();
            }
            if(timer != null) {
                timer.cancel();
                timer = null;
            }
            mCountDownTimer.cancel();
            countdownPeriod = 30000;
            progressBar.setProgress(100);
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
                    .setTitle("About Speed Game")
                    .setMessage(R.string.speed_how)
                    .setNeutralButton(android.R.string.ok, null)
                    .show();

        } else if (id == R.id.nav_sound) {
            if(soundEffect.soundOn){
                soundEffect.soundOn = false;
                if(soundEffect.player!=null){
                    soundEffect.player.stop();
                }
                if(soundEffect.backgroundPlayer!=null){
                    soundEffect.backgroundPlayer.stop();

                }

            }
            else
            {
                soundEffect.soundOn = true;
                soundEffect.backgroundSound(R.raw.get_outside);
            }

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