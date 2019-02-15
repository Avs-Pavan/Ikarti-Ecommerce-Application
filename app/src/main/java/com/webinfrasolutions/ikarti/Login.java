package com.webinfrasolutions.ikarti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.webinfrasolutions.ikarti.API.GetOtpApi;
import com.webinfrasolutions.ikarti.Pojo.Category;
import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;
import com.webinfrasolutions.ikarti.util.Config;
import com.webinfrasolutions.ikarti.util.NotificationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import customfonts.MyEditText;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
TextInputLayout numlay;
    MyEditText numet;
    private static final String TAG = Login.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        numlay=findViewById(R.id.number_lay);
        numet=findViewById(R.id.phone_number_edit_text);
        numet.addTextChangedListener(new MyTextWatcher(numet));
initFb();
    }

    private void initFb() {



        txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        txtMessage = (TextView) findViewById(R.id.txt_push_message);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    //displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }
            }
        };

       displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    public void goHome(View view) {
        Intent i=new Intent(Login.this,WorkerDispathOrderList.class);
        startActivity(i);
    }

    public void register(View view) {

        Intent i=new Intent(Login.this,RegisterShop.class);

        startActivity(i);

    }

    public void getOtp(View view) {
        showMsg("get otp");
        Log.i("login butootn ","clicked ");
        if(validateNumber())
           loginCall();


    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.phone_number_edit_text:
                {
                    validateNumber();
                    break;}


            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateNumber() {//||numet.getText().toString().trim().length()!=10
        if (numet.getText().toString().trim().isEmpty()) {
            numlay.setError("Please Enter A Valid 10 Digit Mobile Number");
            requestFocus(numet);
            return false;
        } else {
            numlay.setErrorEnabled(false);
        }

        return true;
    }
    public void showMsg(String msg){


     //   Toast.makeText(Login.this,msg,Toast.LENGTH_SHORT).show();
    }

public  void loginCall(){
    String number=numet.getText().toString();
    RequestBody Number = RequestBody.create(MediaType.parse("text/plain"), number);

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    // Change base URL to your upload server URL.
    GetOtpApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(GetOtpApi.class);


    retrofit2.Call<GetOtpPojo> mService = service.login(Number);
    mService.enqueue(new Callback<GetOtpPojo>() {
        @Override
        public void onResponse(Call<GetOtpPojo> call, Response<GetOtpPojo> response) {

            if (response.body().getStatus()) {
    Intent i=new Intent(Login.this,OtpScreen.class);
    i.putExtra("obj",response.body());
                i.putExtra("number",""+numet.getText().toString());
    startActivity(i);finish();
            }
            else
            Toast.makeText(Login.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onFailure(Call<GetOtpPojo> call, Throwable t) {
            Toast.makeText(Login.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

        }
    });

}

   /* public void storeCategories(Context context, List favorites) {
// used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("category",Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString("list", jsonFavorites);
        editor.commit();

    }*/

  /*  public  void saveStoreKeeperData(Context context,Object obj){

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(obj);
        editor.putString("user", jsonFavorites);
        editor.commit();

    }*/

    public ArrayList loadCategories(Context context) {
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

    public  void print(){

       ArrayList arr= loadCategories(Login.this);
        for (int x=0;x<arr.size();x++){

            Log.i("last",""+arr.get(x));
        }

    }
}
/**
 *    if (response.body().getUtype().equals("shopkeeper")){

 new SessionManager().saveStoreKeeperData(Login.this,response.body().getStorekeeper(),"userdata","user");
 new SessionManager().storeCategories(Login.this,response.body().getStorekeeper().getCategorylist(),"category","list");
 Intent i=new Intent(Login.this,Shome.class);
 startActivity(i);
 }else {

 new SessionManager().saveStoreKeeperData(Login.this,response.body().getUserData(),"userdata","user");

 Intent i=new Intent(Login.this,Uhome.class);
 startActivity(i);
 }*/