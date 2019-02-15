package com.webinfrasolutions.ikarti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.API.FetchMyCartListApi;
import com.webinfrasolutions.ikarti.API.FetchWorkerOrderList;
import com.webinfrasolutions.ikarti.Adapters.RecyclerItemListener;
import com.webinfrasolutions.ikarti.Adapters.WorkerOrderListAdapter;
import com.webinfrasolutions.ikarti.Pojo.CartListPojo;
import com.webinfrasolutions.ikarti.Pojo.Worker;
import com.webinfrasolutions.ikarti.Pojo.WorkerOrder;
import com.webinfrasolutions.ikarti.Pojo.WorkerOrderListPojo;
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

public class WorkerDispathOrderList extends AppCompatActivity {
RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_dispath_order_list);
        rv=findViewById(R.id.rv);
        fetchList("f");
    }


    private void fetchList(String uid) {

        Worker worker=new SessionManager().getWorker(WorkerDispathOrderList.this);
        RequestBody Wid = RequestBody.create(MediaType.parse("text/plain"), worker.getId());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        FetchWorkerOrderList service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchWorkerOrderList.class);


        retrofit2. Call<WorkerOrderListPojo> mService = service.fetch(Wid);
        mService.enqueue(new Callback<WorkerOrderListPojo>() {
            @Override
            public void onResponse(Call<WorkerOrderListPojo> call, final Response<WorkerOrderListPojo> response) {

                if (response.body().getStatus()){

                    if (!response.body().getWorkerorders().isEmpty()){
                      //  Toast.makeText(WorkerDispathOrderList.this,response.body().getWorkerorders().size()+" sixe",Toast.LENGTH_SHORT).show();

                        rv.setLayoutManager(new LinearLayoutManager(WorkerDispathOrderList.this));
                        rv.setAdapter(new WorkerOrderListAdapter(WorkerDispathOrderList.this,response.body().getWorkerorders()));
                        rv.addOnItemTouchListener(new RecyclerItemListener(WorkerDispathOrderList.this,rv,new
                                RecyclerItemListener.RecyclerTouchListener(){
                                    @Override
                                    public void onClickItem(View v, int position) {

                                        Intent i=new Intent(WorkerDispathOrderList.this,DispatchOrder.class);
                                        WorkerOrder order=response.body().getWorkerorders().get(position);
                                        i.putExtra("record",order);
                                        startActivity(i);
                                    }

                                    @Override
                                    public void onLongClickItem(View v, int position) {

                                    }
                                }));
                    }else {
                        Toast.makeText(WorkerDispathOrderList.this,"ggg",Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<WorkerOrderListPojo> call, Throwable t) {

            }
        });

    }


    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
fetchList("1");

    }

}
