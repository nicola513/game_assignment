package com.ouhks413f.groupproject.dontforgetit;

/**
 * Created by ngnicola on 15/11/2016.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class OrderPage  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CountDownTimer mCountDownTimer;
    PopupWindow popUp;
    LinearLayout layout;
    TextView textView;
    LinearLayout.LayoutParams params;

    TableLayout gameBoard,imageBoard;
    ImageView heart;
    ImageData imageData;
    Game game = new Game();
    UserData userData = new UserData();

    int imageIndex = 0;
    Context context = this;

    static int maxMistake = 0;
    static int mistake;
    static int heartStyle =0;
    SoundEffect soundEffect;
    boolean isChangeMusic = false;

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

        ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
        vf.setDisplayedChild(2);

        if(maxMistake==5)
            heartStyle=1;
        else heartStyle=0;

        isChangePage=false;
        context = this;
        CardView cardView = new CardView(this,game.style);

        gameBoard = (TableLayout) findViewById(R.id.order_board_table);
        imageBoard = (TableLayout) findViewById(R.id.image_table);
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

    private void readyGo(){
        mCountDownTimer = new CountDownTimer(3000,500){
            @Override
            public void onFinish() {
                popUp.dismiss();
                if(!isChangePage)
                soundEffect.backgroundSound(R.raw.snack_time);
                cardController();

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
        },  getResources().getInteger(R.integer.card_open_time));

    }

    private void resetGame() {
        game.generateData(this);
        game.setOrderList(this);
        gameBoard.removeAllViews();
        imageBoard.removeAllViews();
        int index = 0;
        imageIndex = 0;
        mistake = 0;
        isChangeMusic =false;
        heart = (ImageView)findViewById(R.id.heart);
        heart.setImageResource(imageData.heartStyle[heartStyle][mistake]);


        TableRow imageRow = new TableRow(this);
        imageRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));



        imageBoard.addView(imageRow);
        for (int i = 0 ; i < 6; i++) {
            ImageListView imageView = new ImageListView(this, game.orderList.get(imageIndex++));
            imageRow.addView(imageView);
        }

        for (int i = 0; i < game.board_y; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
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
                    //TODO if open a real card, the imageRow will move
                    if(game.handleOrderOpenCards()){
                        TableRow imageRow = (TableRow)imageBoard.getChildAt(0);
                        imageRow.removeViewAt(0);
                        ImageListView imageView = new ImageListView(context, game.orderList.get(imageIndex++));
                        imageRow.addView(imageView);
                    }else {
                        mistake++;
                        if(mistake<imageData.heartStyle[heartStyle].length&&maxMistake!=0)
                            heart.setImageResource(imageData.heartStyle[heartStyle][mistake]);
                        switch (maxMistake){
                            case 7:if(mistake>4&&!isChangeMusic){
                                isChangeMusic =true;
                                soundEffect.backgroundPlayer.stop();
                                soundEffect.backgroundSound(R.raw.by_the_pool);
                            }break;
                            case 5:
                                if(mistake>3&&!isChangeMusic){
                                soundEffect.backgroundPlayer.stop();
                                soundEffect.backgroundSound(R.raw.by_the_pool);
                                break;
                                }
                        }
                    }

                    for (int i=0; i<gameBoard.getChildCount();i++){
                        TableRow row = (TableRow)gameBoard.getChildAt(i);
                        for (int j=0; j<row.getChildCount(); j++) {
                            View v = row.getChildAt(j);
                            v.invalidate();
                        }
                    }

                    if (game.isGameWon()) {
                        long totalTime =game.endTime-game.startTime;
                        GameFinish.totalTime = totalTime;
                        GameFinish.steps = game.numMoves;
                        GameFinish.orderFinish = true;
                        switch (maxMistake){
                            case 0:
                                if((totalTime<userData.order_min_time_lower&&userData.order_min_time_lower!=0)||userData.order_min_time_lower==0)
                                    userData.order_min_time_lower=totalTime;
                                break;
                            case 7:
                                if((totalTime<userData.order_min_time_middle&&userData.order_min_time_middle!=0)||userData.order_min_time_middle==0){
                                    userData.order_min_time_middle=totalTime;
                                }
                                break;
                            case 5:
                                if((totalTime<userData.order_min_time_high&&userData.order_min_time_high!=0)||userData.order_min_time_high==0){
                                    userData.order_min_time_high=totalTime;
                                }
                                break;
                        }
                        userData.writeData();
                        if(soundEffect.backgroundPlayer!=null){
                            soundEffect.backgroundPlayer.stop();
                            soundEffect.backgroundPlayer=null;
                        }
                        Intent i = new Intent(getApplicationContext(),GameFinish.class);
                        startActivity(i);
                    }

                    if(isGameOver()){
                        GameFinish.mistake=mistake;
                        GameFinish.orderFinish = false;
                        if(soundEffect.backgroundPlayer!=null){
                            soundEffect.backgroundPlayer.stop();
                            soundEffect.backgroundPlayer=null;
                        }
                        Intent i = new Intent(getApplicationContext(),GameFinish.class);
                        startActivity(i);
                    }
                }
            }, getResources().getInteger(R.integer.open_view_delay));
        }
    }

    public boolean isGameOver(){
        if(maxMistake!=0)
            if(mistake>=maxMistake)
                return true;
        return false;
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
            if(soundEffect.player!=null){
                soundEffect.player.stop();
            }
            if(soundEffect.backgroundPlayer!=null){
                soundEffect.backgroundPlayer.stop();
            }
            startActivity(i);

        } else if (id == R.id.nav_newGame) {
            if(soundEffect.player!=null){
                soundEffect.player.stop();
            }
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
            //TODO how to play order game
            new AlertDialog.Builder(this)
                    .setTitle("About Order Game")
                    .setMessage(R.string.order_how)
                    .setNeutralButton(android.R.string.ok, null)
                    .show();

        } else if (id == R.id.nav_sound) {
            if(soundEffect.soundOn) {
                soundEffect.soundOn = false;
                if (soundEffect.player != null) {
                    soundEffect.player.stop();
                }
                if (soundEffect.backgroundPlayer != null) {
                    soundEffect.backgroundPlayer.stop();
                }
            }
            else
                soundEffect.soundOn = true;
            soundEffect.backgroundSound(R.raw.snack_time);
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
