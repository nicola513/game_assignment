package com.ouhks413f.groupproject.dontforgetit;

/**
 * Created by ngnicola on 16/11/2016.
 */

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;


public class UserData extends GameData {
    static long classic_min_time_high,classic_min_time_middle,classic_min_time_lower = 0;
    static long order_min_time_high,order_min_time_middle,order_min_time_lower = 0;
    static int speed_num_pair = 0;

    UserData(){
        super();
    }

    UserData(Context context){
        super(context);
    }

    public void getData(){
        readFile();
        if(data!=null){
            try {
                JSONObject classicData = data.getJSONObject("classic");
                classic_min_time_high = classicData.getLong("min_time_high");
                classic_min_time_middle = classicData.getLong("min_time_middle");
                classic_min_time_lower = classicData.getLong("min_time_lower");

                JSONObject orderData = data.getJSONObject("order");
                order_min_time_high = orderData.getLong("min_time_high");
                order_min_time_middle = orderData.getLong("min_time_middle");
                order_min_time_lower = orderData.getLong("min_time_lower");

                JSONObject speedData = data.getJSONObject("speed");
                speed_num_pair = speedData.getInt("num_pair");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeData(){
        try {
            JSONObject classicData = new JSONObject();
            classicData.put("min_time_high",classic_min_time_high);
            classicData.put("min_time_middle",classic_min_time_middle);
            classicData.put("min_time_lower",classic_min_time_lower);

            JSONObject orderData = new JSONObject();
            orderData.put("min_time_high",order_min_time_high);
            orderData.put("min_time_middle",order_min_time_middle);
            orderData.put("min_time_lower",order_min_time_lower);

            JSONObject speedData = new JSONObject();
            speedData.put("num_pair",speed_num_pair);

            JSONObject userData = new JSONObject();
            userData.put("classic",classicData);
            userData.put("order",orderData);
            userData.put("speed",speedData);

            json = userData.toString();
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        saveFile();
    }


}

