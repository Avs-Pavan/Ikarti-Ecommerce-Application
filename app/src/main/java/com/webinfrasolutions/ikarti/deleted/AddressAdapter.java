package com.webinfrasolutions.ikarti.deleted;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.API.DeleteAddressApi;
import com.webinfrasolutions.ikarti.Pojo.Address;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.R;
import com.webinfrasolutions.ikarti.ThankUActivity;

import java.util.ArrayList;
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

/**
 * Created by kevin on 12/8/17.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

Context  context;
    List<Address> records=new ArrayList<>();
    public AddressAdapter(Context contexts, List<Address> records) {
this.context=contexts;
       this.records=records;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyTextView fname,lname,addd,city,state,zip,country,phone;
ImageView deleteAddressBtn;LinearLayout lay;
        public MyViewHolder(View view) {
            super(view);
            fname =  view.findViewById(R.id.name3);
            lname =  view.findViewById(R.id.name4);
            addd =  view.findViewById(R.id.addL2);
            city =  view.findViewById(R.id.city2);
            state =  view.findViewById(R.id.state2);
            zip =  view.findViewById(R.id.zip2);
            country =  view.findViewById(R.id.country2);
            phone =  view.findViewById(R.id.phn);
            deleteAddressBtn=view.findViewById(R.id.deleteAddressBtn);
lay=view.findViewById(R.id.linear1);
            setIsRecyclable(false);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
final Address address=records.get(position);

holder.fname.setText(address.getFirstName());
holder.lname.setText(address.getLastName());
holder.addd.setText(address.getAddressLine());
holder.city.setText(address.getCity());
holder.state.setText(address.getState());
holder.zip.setText(address.getZipcode());
holder.country.setText("India");
holder.phone.setText(address.getMobileNumber());
        holder.deleteAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
delete(address.getId(),position);            }
        });
holder.lay.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("address_id",records.get(position).getId());
        bundle.putString("dtype","address");
        Intent mainIntent = new Intent(context, ThankUActivity.class);
        //add bundle to intent
        mainIntent.putExtras(bundle);
        //start activity
        context.startActivity(mainIntent);
    }
});

    }


    @Override
    public int getItemCount() {
        return records.size();
    }
    public void delete(String aid, final int position){

        RequestBody AddressId = RequestBody.create(MediaType.parse("text/plain"), aid);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        DeleteAddressApi service = new Retrofit.Builder().baseUrl(context.getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(DeleteAddressApi.class);
        retrofit2.Call<MyPojo> mService = service.delete(AddressId);
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                if (response.body().getStatus()) {
                    records.remove(position);
                    notifyDataSetChanged();
                }
                // clearFields();
                Toast.makeText(context,  response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(context,t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


}
