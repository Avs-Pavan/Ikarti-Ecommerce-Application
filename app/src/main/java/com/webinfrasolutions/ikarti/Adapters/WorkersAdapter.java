package com.webinfrasolutions.ikarti.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.API.DeleteWorkerApi;
import com.webinfrasolutions.ikarti.API.FetchWorkerListApi;
import com.webinfrasolutions.ikarti.CategoryLIstActivity;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.Record;
import com.webinfrasolutions.ikarti.Pojo.Storekeeper;
import com.webinfrasolutions.ikarti.Pojo.Worker;
import com.webinfrasolutions.ikarti.Pojo.WorkerListPojo;
import com.webinfrasolutions.ikarti.R;
import com.webinfrasolutions.ikarti.StoreWorkers;
import com.webinfrasolutions.ikarti.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kevin on 12/8/17.
 */

public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.MyViewHolder> {

Context  context;
    List<Worker> records=new ArrayList<Worker>();
    public WorkersAdapter(Context contexts, List<Worker> records) {
this.context=contexts;
       this.records=records;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyTextView name;
        ImageView call,delete;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.name);
            call =  view.findViewById(R.id.call);
            delete =  view.findViewById(R.id.delete);
            setIsRecyclable(false);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.worker_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

holder.name.setText(records.get(position).getName());
       holder.call.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               call(records.get(position));
           }
       });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(records.get(position),position);
            }
        });
    }

    private void call(Worker worker) {
    }

    private void delete(Worker worker, final int position) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        DeleteWorkerApi service = new Retrofit.Builder().baseUrl(context.getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(DeleteWorkerApi.class);
        retrofit2. Call<MyPojo> mService = service.delete(worker.getId());
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_SHORT).show();
if (response.body().getStatus()){
    records.remove(position);notifyDataSetChanged();
}
            }

            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

}
