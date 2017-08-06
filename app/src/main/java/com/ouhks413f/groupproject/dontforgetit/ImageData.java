package com.ouhks413f.groupproject.dontforgetit;

/**
 * Created by ngnicola on 15/11/2016.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;


public class ImageData {
    static List<Integer> imageList = new ArrayList<>();

    Drawable drawable;
    Context context;

    ImageData(Context context){
        this.context=context;
        drawable = context.getResources().getDrawable(R.drawable.space);
    }

    ImageData(Context context, int imageNo){
        this.context = context;
        drawable = context.getResources().getDrawable(imageList.get(imageNo));
    }

    static final int[] style1 = {
            R.drawable.classic1,
            R.drawable.classic2,
            R.drawable.classic3,
            R.drawable.classic4,
            R.drawable.classic5,
            R.drawable.classic6,
            R.drawable.classic7,
            R.drawable.classic8,
            R.drawable.classic9,
            R.drawable.classic10,
    };

    static final int[] style2={
            R.drawable.color1,
            R.drawable.color2,
            R.drawable.color3,
            R.drawable.color4,
            R.drawable.color5,
            R.drawable.color6,
            R.drawable.color7,
            R.drawable.color8,
            R.drawable.color9,
            R.drawable.color10,
    };

    static final int[] style3={
            R.drawable.speed1,
            R.drawable.speed2,
            R.drawable.speed3,
            R.drawable.speed4,
            R.drawable.speed5,
            R.drawable.speed6,
            R.drawable.speed7,
            R.drawable.speed8,
            R.drawable.speed9,
            R.drawable.speed10
    };

    static final int[]coveStyle={
            R.drawable.classic_cover,
            R.drawable.order_cover,
            R.drawable.speed_cover
    };

    static final int[] heart1={
            R.drawable.heart,
            R.drawable.heart_1,
            R.drawable.heart_2,
            R.drawable.heart_3,
            R.drawable.heart_4,
            R.drawable.heart_5,
            R.drawable.heart_6
    };

    static final int[] heart2={
            R.drawable.heart,
            R.drawable.heart_1,
            R.drawable.heart_3,
            R.drawable.heart_5,
            R.drawable.heart_7
    };

    static int[][] imageStyle = {style1,style2,style3};
    static int[][] heartStyle = {heart1,heart2};


}