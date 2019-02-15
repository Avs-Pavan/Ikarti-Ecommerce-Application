package com.webinfrasolutions.ikarti;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.webinfrasolutions.ikarti.API.GetOtpApi;
import com.webinfrasolutions.ikarti.API.SaveTokenApi;
import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.util.Config;
import com.webinfrasolutions.ikarti.util.SessionManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtpScreen extends AppCompatActivity {
    Pinview pinview;
    GetOtpPojo otpPojo;
String number;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        try{
            otpPojo=(GetOtpPojo)getIntent().getSerializableExtra("obj");
            number=getIntent().getStringExtra("number");
        }
        catch ( NullPointerException n){
            n.printStackTrace();
        }
        pinview=(Pinview)findViewById(R.id.pinview);
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                hideKeyboard(OtpScreen.this);
              // Toast.makeText(OtpScreen.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
                if (pinview.getValue().equals(otpPojo.getOtp())) {
                    if (otpPojo.getUtype().equals("shopkeeper")){
/////////user is shop keeper
                        type=0;
                        try{

                            saveToken(otpPojo.getStorekeeper().getContactNumber1(),getToken(),"shopkeeper");

                        }catch (NullPointerException n){
                            n.printStackTrace();
                        }

                    } else  if (otpPojo.getUtype().equals("worker"))
                    {
                        type=1;
                        try{

                            saveToken(otpPojo.getWorker().getNumber(),getToken(),"worker");

                        }catch (NullPointerException n){
                            n.printStackTrace();
                        }
                    }  else {

                        //////user is user
                        if (otpPojo.getNewattempt())
                        {
                            //user is new
                            Intent i=new Intent(OtpScreen.this,UserProfile.class);
                            i.putExtra("number",number);
                            startActivity(i);finish();
                        }else {

                            //user is old
type=2;
                            try{

                                saveToken(otpPojo.getUserData().getMobileNumber(),getToken(),"user");

                            }catch (NullPointerException n){
                                n.printStackTrace();
                            }

                        }

                    }
                }
                else {
                    showMsg("Wrong Otp");

                    //pinview.setValue("1234");

                }

            }
        });

    }
    public void goBack(View view) {
        super.onBackPressed();
    }

    private String  getToken() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.i("token fck",regId+"gg");
        return regId;

    }



    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
public void showMsg(String msg){

    Toast.makeText(OtpScreen.this,msg,Toast.LENGTH_SHORT).show();
}

    public void resentOtp(View view) {
     showMsg("resend otp");
    }
    public void saveToken(String mobileNumber,String token,String utype){

       // RequestBody Number = RequestBody.create(MediaType.parse("text/plain"), mobileNumber);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        SaveTokenApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(SaveTokenApi.class);


        retrofit2.Call<MyPojo> mService = service.save(mobileNumber,token,utype);
mService.enqueue(new Callback<MyPojo>() {
    @Override
    public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

      Toast.makeText(OtpScreen.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
        if (response.body().getStatus()){
            new SessionManager().saveUserType(OtpScreen.this,type);
            new SessionManager().saveLogin(OtpScreen.this);

            if (type==0){

                new SessionManager().saveStoreKeeperData(OtpScreen.this,otpPojo.getStorekeeper());
                new SessionManager().storeCategories(OtpScreen.this,otpPojo.getStorekeeper().getCategorylist(),"category","list");
                Intent i=new Intent(OtpScreen.this,Shome.class);
                startActivity(i);finish();
            }else if (type==1){
                new SessionManager().saveWorker(OtpScreen.this,otpPojo.getWorker());

                Intent i=new Intent(OtpScreen.this,WHome.class);
                startActivity(i);finish();

            }else
                {
                new SessionManager().saveUserData(OtpScreen.this,otpPojo.getUserData());

                Intent i=new Intent(OtpScreen.this,Uhome.class);
                startActivity(i);finish();
            }



        }
    }

    @Override
    public void onFailure(Call<MyPojo> call, Throwable t) {
        Toast.makeText(OtpScreen.this,t+"",Toast.LENGTH_SHORT).show();

    }
});
    }
}
