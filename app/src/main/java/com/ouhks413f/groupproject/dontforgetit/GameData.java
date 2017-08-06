package com.ouhks413f.groupproject.dontforgetit;

/**
 * Created by ngnicola on 15/11/2016.
 */

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class GameData {
    //TODO data address
    static File dataAddress;
    static File file;

    //TODO
    static FileOutputStream outputStream;
    static FileInputStream inputStream;

    static JSONObject data;
    String json = null;

    GameData(){}

    GameData(Context context){
        dataAddress = new File(context.getFilesDir()+"/gameData");
        file = new File(dataAddress.getAbsolutePath()+"/gameData.json");
    }



    public void saveFile(){
        if(!dataAddress.exists()){
            dataAddress.mkdir();
        }
        try{
            if (!file.exists()){
                file.createNewFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(json.getBytes());
            outputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readFile(){
        if(!dataAddress.exists()){
            dataAddress.mkdir();
        }
        try{
            if (!file.exists()){
                file.createNewFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        String jsonFile = null;
        try{
            inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            char[] inputBuffer = new char[inputStream.available()];
            inputStreamReader.read(inputBuffer);
            jsonFile = new String(inputBuffer);
            inputStreamReader.close();
            inputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            data = new JSONObject(jsonFile);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
