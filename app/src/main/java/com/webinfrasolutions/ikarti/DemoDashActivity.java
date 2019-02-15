package com.webinfrasolutions.ikarti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.API.FetchDashApi;
import com.webinfrasolutions.ikarti.API.MyProductListApi;
import com.webinfrasolutions.ikarti.Adapters.MyAdapter;
import com.webinfrasolutions.ikarti.Pojo.DashPojo;
import com.webinfrasolutions.ikarti.Pojo.MyproductsListPojo;
import com.webinfrasolutions.ikarti.Pojo.Record;
import com.webinfrasolutions.ikarti.Pojo.UserData;
import com.webinfrasolutions.ikarti.util.SessionManager;

import java.util.List;

import customfonts.MyTextView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemoDashActivity extends AppCompatActivity {
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_dash);
        MyTextView title=(MyTextView)findViewById(R.id.toolbar_title);
        title.setText("Demo Dash");
        title.setTextColor(getResources().getColor(R.color.light_blue));
        fetchList();
        recyclerView    = (RecyclerView) findViewById(R.id.dash_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }



    public void fetchList(){
        final UserData userData=new SessionManager().getUserData(DemoDashActivity.this);
        RequestBody StoreID = RequestBody.create(MediaType.parse("text/plain"), "1");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        FetchDashApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchDashApi.class);


        retrofit2. Call<DashPojo> mService = service.fetch("demo");
        mService.enqueue(new Callback<DashPojo>() {
            @Override
            public void onResponse(Call<DashPojo> call, Response<DashPojo> response) {

                if (response.body().getStatus())
                    // clearFields();
                {
                    MyAdapter adapter = new MyAdapter(DemoDashActivity.this,DemoDashActivity.this, response.body().getRecords(),userData.getUid());

               recyclerView.setAdapter(adapter);
                }
                //Toast.makeText(DemoDashActivity.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<DashPojo> call, Throwable t) {
                Toast.makeText(DemoDashActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void goBack(View view) {
        super.onBackPressed();
    }
}
