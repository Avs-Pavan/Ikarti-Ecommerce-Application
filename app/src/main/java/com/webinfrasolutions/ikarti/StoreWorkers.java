package com.webinfrasolutions.ikarti;

import android.app.Dialog;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.API.AddAddressApi;
import com.webinfrasolutions.ikarti.API.AddWorkerApi;
import com.webinfrasolutions.ikarti.API.FetchWorkerListApi;
import com.webinfrasolutions.ikarti.Adapters.WorkersAdapter;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.Pic;
import com.webinfrasolutions.ikarti.Pojo.Storekeeper;
import com.webinfrasolutions.ikarti.Pojo.WorkerListPojo;
import com.webinfrasolutions.ikarti.util.SessionManager;

import customfonts.MyEditText;
import customfonts.MyTextView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreWorkers extends AppCompatActivity {
RecyclerView rv;Storekeeper store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_store_workers);
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
         store=new SessionManager().getStoreKeeperData(StoreWorkers.this);
        fetchWorkersList();
    }

    public void goBack(View view){
        super.onBackPressed();
    }
    private void fetchWorkersList() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        Storekeeper store=new SessionManager().getStoreKeeperData(StoreWorkers.this);

        FetchWorkerListApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchWorkerListApi.class);
        retrofit2. Call<WorkerListPojo> mService = service.fetch(store.getStoreId());
        mService.enqueue(new Callback<WorkerListPojo>() {
            @Override
            public void onResponse(Call<WorkerListPojo> call, Response<WorkerListPojo> response) {

if (response.body().getStatus()){

    if (response.body().getWorker().size()>0)
        rv.setAdapter(new WorkersAdapter(StoreWorkers.this,response.body().getWorker()));

}

            }

            @Override
            public void onFailure(Call<WorkerListPojo> call, Throwable t) {

            }
        });
    }

    public void addWorker(View view) {
showDialog();
    }

    public void showDialog(){
        MyTextView ok,cancel;
        final MyEditText number,name;
        final Dialog catDialog = new BottomSheetDialog(StoreWorkers.this);
        catDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        catDialog.setCancelable(true);
        catDialog.setContentView(R.layout.addworker_lay);
        catDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        catDialog.show();
        ok =  catDialog.findViewById(R.id.button_ok);
        cancel =  catDialog.findViewById(R.id.button_cancel);

        number =  catDialog.findViewById(R.id.number);
        name =  catDialog.findViewById(R.id.name);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateNumber()&&validateName())
                {

                    addWorkerCall(number.getText().toString(),name.getText().toString());
                    catDialog.dismiss();
                }else{

                }

            }

            private boolean validateName() {

                if (name.getText().toString().equals("")||name.getText().toString().length()<4)
                {
                    number.setError("Name Should  Have 4 Letters");
                    return false;
                }

                return true;
            }

            private boolean validateNumber() {

              if (number.getText().toString().trim().equals("")||number.getText().toString().trim().length()<10)
              {
                  number.setError("Please Enter A Valid 10 digit MObile Number");
                  return false;
              }

                return true;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catDialog.dismiss();
            }
        });

    }

    private void addWorkerCall(String number, String name) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.

        AddWorkerApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(AddWorkerApi.class);
        retrofit2. Call<MyPojo> mService = service.add(store.getStoreId(),number,name);
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                if (response.body().getStatus())
                    fetchWorkersList();
                Toast.makeText(StoreWorkers.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {

            }
        });
    }

}
