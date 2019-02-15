package com.webinfrasolutions.ikarti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.API.FetchOrderApi;
import com.webinfrasolutions.ikarti.Adapters.OrdersAdapter;
import com.webinfrasolutions.ikarti.Pojo.OrderListPojo;
import com.webinfrasolutions.ikarti.Pojo.UserData;
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

public class Orders extends AppCompatActivity {


    private RecyclerView orders_rv;
    static LinearLayout error;

    public void goBack(View view) {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
//     ********Listview**************
        orders_rv=findViewById(R.id.orders_rv);
        orders_rv.setLayoutManager(new LinearLayoutManager(this));
        error=findViewById(R.id.emptycart);

        UserData userData=new SessionManager().getUserData(Orders.this);
        fetchList(userData.getUid());
    }
    private void fetchList(String uid) {

        RequestBody Uid = RequestBody.create(MediaType.parse("text/plain"), uid);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        FetchOrderApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchOrderApi.class);
        retrofit2. Call<OrderListPojo> mService = service.fetch(Uid);
        mService.enqueue(new Callback<OrderListPojo>() {
            @Override
            public void onResponse(Call<OrderListPojo> call, Response<OrderListPojo> response) {

                if (response.body().getStatus())
                {
                    if (response.body().getOrderItem().size()>0){
                        orders_rv.setAdapter(new OrdersAdapter(Orders.this,response.body().getOrderItem()));

                        //Toast.makeText(Orders.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();

                    }else {
                        error.setVisibility(View.VISIBLE);
                        orders_rv.setVisibility(View.GONE);
                    }
                }
   }


            @Override
            public void onFailure(Call<OrderListPojo> call, Throwable t) {
                Toast.makeText(Orders.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}
