package com.webinfrasolutions.ikarti.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.API.DeleteWorkerApi;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.Worker;
import com.webinfrasolutions.ikarti.R;

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

public class SelectWorkersAdapter extends RecyclerView.Adapter<SelectWorkersAdapter.MyViewHolder> {

Context  context;
    List<Worker> records=new ArrayList<Worker>();
    public SelectWorkersAdapter(Context contexts, List<Worker> records) {
this.context=contexts;
       this.records=records;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyTextView name;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.worker_name);

            setIsRecyclable(false);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_worker_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

holder.name.setText(records.get(position).getName());

    }



    @Override
    public int getItemCount() {
        return records.size();
    }

}
