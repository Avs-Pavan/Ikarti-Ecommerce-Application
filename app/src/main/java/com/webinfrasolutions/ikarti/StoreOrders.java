package com.webinfrasolutions.ikarti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.API.FetchStoreOrdersApi;
import com.webinfrasolutions.ikarti.API.FetchWorkerListApi;
import com.webinfrasolutions.ikarti.Adapters.StoreOrdersAdapter;
import com.webinfrasolutions.ikarti.Adapters.WorkersAdapter;
import com.webinfrasolutions.ikarti.Pojo.OrderListPojo;
import com.webinfrasolutions.ikarti.Pojo.Storekeeper;
import com.webinfrasolutions.ikarti.Pojo.WorkerListPojo;
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

public class StoreOrders extends AppCompatActivity {
    Storekeeper storekeeper;
    RecyclerView rv;
    LinearLayout error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_orders);
rv=findViewById(R.id.orders_rv);
        error=findViewById(R.id.emptycart);
       storekeeper=new SessionManager().getStoreKeeperData(this);
        fetchList(storekeeper.getStoreId());
        fetchWorkersList();

    }
    public  void goBack(View view){
        super.onBackPressed();
    }

    private void fetchList(String sid) {

        RequestBody Sid = RequestBody.create(MediaType.parse("text/plain"), sid);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        FetchStoreOrdersApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchStoreOrdersApi.class);
        retrofit2. Call<OrderListPojo> mService = service.fetch(Sid);
        mService.enqueue(new Callback<OrderListPojo>() {
            @Override
            public void onResponse(Call<OrderListPojo> call, Response<OrderListPojo> response) {

                if (response.body().getStatus()){
                    if (response.body().getOrderItem().size()>0) {
                        rv.setLayoutManager(new LinearLayoutManager(StoreOrders.this));
                        rv.setAdapter(new StoreOrdersAdapter(StoreOrders.this, response.body().getOrderItem()));

                    }else {
                        rv.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }
                // Toast.makeText(StoreOrders.this,""+response.body().getOrderItem(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<OrderListPojo> call, Throwable t) {
                Toast.makeText(StoreOrders.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fetchWorkersList() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        Storekeeper store=new SessionManager().getStoreKeeperData(StoreOrders.this);

        FetchWorkerListApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchWorkerListApi.class);
        retrofit2. Call<WorkerListPojo> mService = service.fetch(store.getStoreId());
        mService.enqueue(new Callback<WorkerListPojo>() {
            @Override
            public void onResponse(Call<WorkerListPojo> call, Response<WorkerListPojo> response) {

                if (response.body().getStatus()){

                    if (response.body().getWorker().size()>0)
                    {
new SessionManager().storeWorkers(StoreOrders.this,response.body().getWorker());

                    }
                }

            }

            @Override
            public void onFailure(Call<WorkerListPojo> call, Throwable t) {

            }
        });
    }

}
