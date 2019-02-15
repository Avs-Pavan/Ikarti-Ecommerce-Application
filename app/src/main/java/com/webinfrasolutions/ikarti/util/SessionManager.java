package com.webinfrasolutions.ikarti.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.webinfrasolutions.ikarti.Pojo.Category;
import com.webinfrasolutions.ikarti.Pojo.Storekeeper;
import com.webinfrasolutions.ikarti.Pojo.UserData;
import com.webinfrasolutions.ikarti.Pojo.Worker;
import com.webinfrasolutions.ikarti.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import customfonts.MyTextView;

/**
 * Created by kevin on 18/12/17.
 */

public class SessionManager {

    public  void saveStoreKeeperData(Context context, Storekeeper obj){

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("storekeeper",Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(obj);
        editor.putString("data", jsonFavorites);
        editor.commit();

    }
    public  void saveUserType(Context context, int n){
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("user_type",Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt("type",n);
        editor.commit();

    }
    public  int getUserType(Context context){
        SharedPreferences settings;
        UserData object=new UserData();
        int type=10;
        settings = context.getSharedPreferences("user_type",Context.MODE_PRIVATE);
        if (settings.contains("type")) {
          type = settings.getInt("type",0);

        }
        return type;

    }
    public void storeCategories(Context context, List favorites,String ParentName,String chilname) {
// used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(ParentName,Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(chilname, jsonFavorites);
        editor.commit();

    }
    public  Storekeeper getStoreKeeperData(Context context){
        SharedPreferences settings;
        Storekeeper object=new Storekeeper();
        settings = context.getSharedPreferences("storekeeper",Context.MODE_PRIVATE);
        if (settings.contains("data")) {
            String jsonFavorites = settings.getString("data", null);
            Gson gson = new Gson();
            object = gson.fromJson(jsonFavorites,Storekeeper.class);

        }
        return object;

    }
    public ArrayList getList(Context context) {
// used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        List favorites;
        settings = context.getSharedPreferences("category",Context.MODE_PRIVATE);
        if (settings.contains("list")) {
            String jsonFavorites = settings.getString("list", null);
            Gson gson = new Gson();
            Category[] favoriteItems = gson.fromJson(jsonFavorites,Category[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList(favorites);
        } else
            return null;
        return (ArrayList) favorites;
    }
    public  void saveUserData(Context context, UserData obj){

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(obj);
        editor.putString("data", jsonFavorites);
        editor.commit();

    }
    public  UserData getUserData(Context context){
        SharedPreferences settings;
        UserData object=new UserData();
        settings = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        if (settings.contains("data")) {
            String jsonFavorites = settings.getString("data", null);
            Gson gson = new Gson();
            object = gson.fromJson(jsonFavorites,UserData.class);

        }
        return object;

    }
    public void storeWorkers(Context context, List workers) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("workers",Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(workers);
        editor.putString("list", jsonFavorites);
        editor.commit();

    }
    public ArrayList getWorkerList(Context context) {
// used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        List favorites;
        settings = context.getSharedPreferences("workers",Context.MODE_PRIVATE);
        if (settings.contains("list")) {
            String jsonFavorites = settings.getString("list", null);
            Gson gson = new Gson();
            Worker[] favoriteItems = gson.fromJson(jsonFavorites,Worker[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList(favorites);
        } else
            return null;
        return (ArrayList) favorites;
    }
    public  void saveWorker(Context context, Worker obj){

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(obj);
        editor.putString("data", jsonFavorites);
        editor.commit();

    }
    public  Worker getWorker(Context context){
        SharedPreferences settings;
        Worker object=new Worker();
        settings = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        if (settings.contains("data")) {
            String jsonFavorites = settings.getString("data", null);
            Gson gson = new Gson();
            object = gson.fromJson(jsonFavorites,Worker.class);

        }
        return object;

    }

    public  void saveLogin(Context context){
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean("login",true);
        editor.commit();

    }
    public  void logout(Context context){
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean("login",false);
        editor.commit();

    }
    public  boolean isLoggedIn(Context context){
        SharedPreferences settings;
        settings = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        Log.e("my fucking login",settings.getBoolean("login",false)+"");
        return  settings.getBoolean("login",false);
    }

}
