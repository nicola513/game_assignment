package com.ouhks413f.groupproject.dontforgetit;;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import static com.ouhks413f.groupproject.dontforgetit.R.id.next;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        //, android.view.GestureDetector.OnGestureListener
{
    private Button next_mode, how_btn, levelButton1, levelButton2, levelButton3;
    private int[] menu_image = {R.drawable.classic_mode,R.drawable.order_mode,R.drawable.speed_mode};
    private ImageView modeTitle;
    private TextView textView, how_text;
    private GestureDetector gestureDetector;
    private ViewFlipper viewFlipper;
    private PopupWindow popUpMenu, how_to_play;
    private LinearLayout popUpLayout, how_to_play_layout;
    private ImageView popUpTitle, howTitle;
    private LinearLayout.LayoutParams popUpParams;
    private AppCompatActivity mActivty;
    boolean click = true;
    GestureDetector mGestureDetector;

    UserData userData;

    String classic_high,classic_middle,classic_lower,order_high,order_middle,order_lower,speed_numPair="";

    static final int CLASSIC=0;
    static final int ORDER = 1;
    static final int SPEED = 2;

    int mode=0;
    Context context;
    SoundEffect soundEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.menu_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewFlipper view = (ViewFlipper)findViewById(R.id.vf);
        view.setDisplayedChild(0);

        mActivty = this;
        //gestureDetector = new GestureDetector(this);
        MainGestureDetector mainGestureDetector = new MainGestureDetector();
        mGestureDetector = new GestureDetector(this,mainGestureDetector);

        popUpMenu = new PopupWindow(this);
        popUpLayout = new LinearLayout(this);
        popUpTitle = new ImageView(this);
        levelButton1= new Button(this);
        levelButton2= new Button(this);
        levelButton3= new Button(this);
        popUpTitle.setImageResource(R.drawable.level);
        popUpTitle.setPadding(0,0,0,10);

        levelButton1.setBackgroundResource(R.drawable.high);
        levelButton2.setBackgroundResource(R.drawable.middle);
        levelButton3.setBackgroundResource(R.drawable.low);
        popUpParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popUpLayout.addView(popUpTitle,popUpParams);
        popUpLayout.addView(levelButton1,popUpParams);
        popUpLayout.addView(levelButton2,popUpParams);
        popUpLayout.addView(levelButton3,popUpParams);
        popUpLayout.setOrientation(LinearLayout.VERTICAL);
        popUpMenu.setContentView(popUpLayout);
        //popUpMenu.setOutsideTouchable(true);
        //popUpMenu.setFocusable(true);

        modeTitle = (ImageView) this.findViewById(R.id.mode);
        textView = (TextView) this.findViewById(R.id.text);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.viewflipper);
        viewFlipper.setAutoStart(false);

        for(int i=0; i < menu_image.length;i++){
            ImageView image = new ImageView(getApplicationContext());
            image.setBackgroundResource(menu_image[i]);
            viewFlipper.addView(image);
        }
        modeTitle.setImageResource(R.drawable.classic);
        next_mode = (Button)findViewById(next);
        next_mode.setText("Start");
        next_mode.setOnClickListener(nextModeListener);

        how_btn = (Button) findViewById(R.id.how);
        how_btn.setOnClickListener(howListener);
        how_to_play = new PopupWindow(this);
        how_to_play_layout = new LinearLayout(this);
        howTitle = new ImageView(this);
        how_text= new TextView(this);
        howTitle.setImageResource(R.drawable.how_to_play);
        howTitle.setPadding(50,0,50,0);
        how_text.setText(getResources().getString(R.string.main_how));
        how_text.setTextSize(20);
        how_text.setPadding(50,0,50,50);
        how_to_play_layout.setBackgroundResource(R.drawable.border_how);
        how_to_play_layout.addView(howTitle,popUpParams);
        how_to_play_layout.addView(how_text,popUpParams);
        how_to_play_layout.setOrientation(LinearLayout.VERTICAL);
        how_to_play.setContentView(how_to_play_layout);
        how_to_play.setOutsideTouchable(true);
        how_to_play.setFocusable(true);



        userData = new UserData(this);
        userData.getData();
        context=this;
        setData();
        changeText();


        soundEffect = new SoundEffect(this);
        soundEffect.backgroundSound(R.raw.bike_rides);
        //TODO set background music

        levelButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameFinish.level = 0;
                Intent i = null;
                if(GameFinish.gameMode==CLASSIC){
                    Game.board_y = getResources().getInteger(R.integer.high_level);
                     i= new Intent(getApplicationContext(), ClassicPage.class);
                }else if (GameFinish.gameMode == ORDER){
                    OrderPage.maxMistake = getResources().getInteger(R.integer.order_high_level);;
                    i = new Intent(getApplicationContext(),OrderPage.class);
                }
                if(soundEffect.backgroundPlayer!=null){
                    soundEffect.backgroundPlayer.stop();
                    soundEffect.backgroundPlayer=null;
                }
                startActivity(i);
            }
        });

        levelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameFinish.level = 1;
                Intent i = null;
                if(GameFinish.gameMode==CLASSIC){
                    Game.board_y = getResources().getInteger(R.integer.middle_level);
                    i= new Intent(getApplicationContext(), ClassicPage.class);
                }else if (GameFinish.gameMode == ORDER){
                    OrderPage.maxMistake = getResources().getInteger(R.integer.order_middle_level);;
                    i = new Intent(getApplicationContext(),OrderPage.class);
                }
                if(soundEffect.backgroundPlayer!=null){
                    soundEffect.backgroundPlayer.stop();
                    soundEffect.backgroundPlayer=null;
                }
                startActivity(i);
            }
        });

        levelButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameFinish.level = 2;
                Intent i = null;
                if(GameFinish.gameMode==CLASSIC){
                    Game.board_y = getResources().getInteger(R.integer.low_level);
                    i= new Intent(getApplicationContext(), ClassicPage.class);
                }else if (GameFinish.gameMode == ORDER){
                    OrderPage.maxMistake = getResources().getInteger(R.integer.order_low_level);;
                    i = new Intent(getApplicationContext(),OrderPage.class);
                }
                if(soundEffect.backgroundPlayer!=null){
                    soundEffect.backgroundPlayer.stop();
                    soundEffect.backgroundPlayer=null;
                }
                startActivity(i);
            }
        });

    };

    public void setData(){
        if(userData.classic_min_time_high==0)
            classic_high="--";
        else
            classic_high=String.valueOf(userData.classic_min_time_high/1000);

        if(userData.classic_min_time_middle==0)
            classic_middle="--";
        else
            classic_middle=String.valueOf(userData.classic_min_time_middle/1000);

        if(userData.classic_min_time_lower==0)
            classic_lower="--";
        else
            classic_lower=String.valueOf(userData.classic_min_time_lower/1000);

        if(userData.order_min_time_high==0)
            order_high="--";
        else
            order_high=String.valueOf(userData.order_min_time_high/1000);

        if(userData.order_min_time_middle==0)
            order_middle="--";
        else
            order_middle=String.valueOf(userData.order_min_time_middle/1000);

        if(userData.order_min_time_lower==0)
            order_lower="--";
        else
            order_lower=String.valueOf(userData.order_min_time_lower/1000);
        if(userData.speed_num_pair==0)
            speed_numPair="--";
        else
            speed_numPair=String.valueOf(userData.speed_num_pair);
    }

    private Button.OnClickListener howListener = new Button.OnClickListener() {
        public void onClick(View v) {
            if (click) {
                how_to_play.showAtLocation(how_to_play_layout, Gravity.CENTER, 0, 0);
                click = false;
            } else {
                how_to_play.dismiss();
                click = true;
            }
        }
    };


    private Button.OnClickListener nextModeListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (mode) {
                case 0:
                    if (click) {
                        GameFinish.gameMode= CLASSIC;
                        Game.style = 0;
                        popUpMenu.showAtLocation(popUpLayout, Gravity.CENTER, 0, 0);
                        next_mode.setText("Back");
                        click = false;
                    } else {
                        popUpMenu.dismiss();
                        next_mode.setText("Start");
                        click = true;
                    }

                    break;

                case 1:
                    if (click) {
                        Game.style=1;
                        Game.board_y = getResources().getInteger(R.integer.middle_level);
                        GameFinish.gameMode = ORDER;
                        popUpMenu.showAtLocation(popUpLayout, Gravity.CENTER, 0, 0);
                        next_mode.setText("Back");
                        click = false;
                    } else {
                        popUpMenu.dismiss();
                        next_mode.setText("Start");
                        click = true;
                    }
                    break;

                case 2:
                    Intent i ;
                    Game.style = 2;
                    Game.board_y = getResources().getInteger(R.integer.middle_level);
                    GameFinish.gameMode = SPEED;
                    i = new Intent(getApplicationContext(),SpeedPage.class);
                    if(soundEffect.backgroundPlayer!=null){
                        soundEffect.backgroundPlayer.stop();
                        soundEffect.backgroundPlayer=null;
                    }
                    startActivity(i);
                    break;
            }
        }

    };
   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getX() - e1.getX() > 120) {
            Animation rInAnim = AnimationUtils.loadAnimation(mActivty, R.anim.push_right_in);
            Animation rOutAnim = AnimationUtils.loadAnimation(mActivty, R.anim.push_right_out);

            viewFlipper.setClickable(false);
            viewFlipper.setInAnimation(rInAnim);
            viewFlipper.setOutAnimation(rOutAnim);
            viewFlipper.showPrevious();
            if(mode-1<0){
                mode = 2;
            }else{
                mode--;
            }
            changeText();
            return true;
        } else if (e2.getX() - e1.getX() < -120) {
            Animation lInAnim = AnimationUtils.loadAnimation(mActivty, R.anim.push_left_in);
            Animation lOutAnim = AnimationUtils.loadAnimation(mActivty, R.anim.push_left_out);

            viewFlipper.setClickable(false);
            viewFlipper.setInAnimation(lInAnim);
            viewFlipper.setOutAnimation(lOutAnim);
            viewFlipper.showNext();
            if(mode+1>2){
                mode = 0;
            }else {
                mode++;
            }
            changeText();
            return true;
        }
        return true;

    }*/

class MainGestureDetector extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Swipe left (next)
        if (e1.getX() > e2.getX()) {
            viewFlipper.setInAnimation(context, R.anim.push_left_in);
            viewFlipper.setOutAnimation(context, R.anim.push_left_out);

            viewFlipper.showNext();
            if(mode+1>2){
                mode = 0;
            }else {
                mode++;
            }
        }

        // Swipe right (previous)
        if (e1.getX() < e2.getX()) {
            viewFlipper.setInAnimation(context, R.anim.push_right_in);
            viewFlipper.setOutAnimation(context, R.anim.push_right_out);

            viewFlipper.showPrevious();
            if(mode-1<0){
                mode = 2;
            }else{
                mode--;
            }
        }
        changeText();
        return super.onFling(e1, e2, velocityX, velocityY);
    }
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return super.onDoubleTap(e);
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        return false;
    }
}

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    public void changeText(){
        String text=null;
        switch (mode){
            case 0:
                modeTitle.setImageResource(R.drawable.classic);
                text = "High level: "+classic_high+" s \nMiddle level: "+classic_middle+" s \nLower level: "+classic_lower+" s";
                how_text.setText(getResources().getString(R.string.classic_how));
                break;
            case 1:
                text="High level: "+order_high+" s \nMiddle level: "+order_middle+" s \nLower level: "+order_lower+" s";
                modeTitle.setImageResource(R.drawable.order);
                how_text.setText(getResources().getString(R.string.order_how));
                break;
            case 2:
                text="Max number of pair: "+speed_numPair+" pairs";
                modeTitle.setImageResource(R.drawable.speed);
                how_text.setText(getResources().getString(R.string.speed_how));
                break;
        }
        textView.setText(text);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            if(soundEffect.backgroundPlayer!=null)
            soundEffect.backgroundPlayer.stop();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_us) {
            new AlertDialog.Builder(this)
                    .setTitle("About Us")
                    .setMessage(R.string.about_us)
                    .setNeutralButton(android.R.string.ok, null)
                    .show();

        } else if (id == R.id.nav_how) {
            //TODO how to play this game
            new AlertDialog.Builder(this)
                    .setTitle("About Classic Game")
                    .setMessage(R.string.main_how)
                    .setNeutralButton(android.R.string.ok, null)
                    .show();

        } else if (id == R.id.nav_sound) {
            if(soundEffect.soundOn){
                soundEffect.soundOn = false;
                if(soundEffect.backgroundPlayer!=null){
                    soundEffect.backgroundPlayer.stop();
                }
            }
            else{
                soundEffect.soundOn = true;
                soundEffect.backgroundSound(R.raw.bike_rides);
            }
            //todo set sound in here
        } else if (id == R.id.nav_vibrate) {
            if(soundEffect.vibrateOn)
                soundEffect.vibrateOn = false;
            else
                soundEffect.vibrateOn = true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}