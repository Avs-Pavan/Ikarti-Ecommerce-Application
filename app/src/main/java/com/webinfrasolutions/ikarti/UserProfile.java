package com.webinfrasolutions.ikarti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.webinfrasolutions.ikarti.API.CreateUserProfileApi;
import com.webinfrasolutions.ikarti.Pojo.CreateProfilePojo;
import com.webinfrasolutions.ikarti.util.Config;
import com.webinfrasolutions.ikarti.util.SessionManager;

import org.jetbrains.annotations.NotNull;
import customfonts.MyEditText;
import io.ghyeok.stickyswitch.widget.StickySwitch;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfile extends AppCompatActivity {
    TextInputLayout namelay;
    MyEditText namet;
    String gender="male";
    StickySwitch stickySwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        namelay=findViewById(R.id.name_lay);

        namet=findViewById(R.id.name_et);
        namet.addTextChangedListener(new MyTextWatcher(namet));
stickySwitch=findViewById(R.id.sticky_switch);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String s) {
               gender=s;
            }
        });
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
    public void register(View view) {
if (validateName()) {

    //UserData data = (UserData) getIntent().getSerializableExtra("data");
    try {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        CreateUserProfileApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(CreateUserProfileApi.class);

        retrofit2.Call<CreateProfilePojo> mService = service.create(getIntent().getStringExtra("number"), gender, namet.getText().toString(),"token");


        mService.enqueue(new Callback<CreateProfilePojo>() {
            @Override
            public void onResponse(Call<CreateProfilePojo> call, Response<CreateProfilePojo> response) {
                CreateProfilePojo obj = response.body();
                if (obj.getStatus())
                {            new SessionManager().saveLogin(UserProfile.this);

                    new SessionManager().saveUserType(UserProfile.this,2);
                    new SessionManager().saveUserData(UserProfile.this,response.body().getUserData());
                    Toast.makeText(UserProfile.this, obj.getMessage(), Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(UserProfile.this,Uhome.class);
                    startActivity(i);finish();
                }
                Toast.makeText(UserProfile.this, obj.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CreateProfilePojo> call, Throwable t) {
                Toast.makeText(UserProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }catch (NullPointerException n){
        n.printStackTrace();
    }
}
    }
    public void showMsg(String msg){
        Toast.makeText(UserProfile.this,msg,Toast.LENGTH_SHORT).show();
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

                case R.id.name_et:
                {
                    validateName();
                    break;}


            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }




    private boolean validateName() {
        if (namet.getText().toString().trim().isEmpty()) {
            namelay.setError("Name Should Not Be Empty");
            requestFocus(namet);
            return false;
        } else {
            namelay.setErrorEnabled(false);
        }

        return true;
    }

    private String  getToken() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        return regId;

    }

}
