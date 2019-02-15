package com.webinfrasolutions.ikarti.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;
import com.webinfrasolutions.ikarti.API.AddFavouriteApi;
import com.webinfrasolutions.ikarti.API.AddToCartApi;
import com.webinfrasolutions.ikarti.API.DeleteFavouriteApi;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.Pic;
import com.webinfrasolutions.ikarti.Pojo.Product;
import com.webinfrasolutions.ikarti.Pojo.Record;
import com.webinfrasolutions.ikarti.ProductDetailsActivity;
import com.webinfrasolutions.ikarti.R;
import com.webinfrasolutions.ikarti.SmallBang;
import com.webinfrasolutions.ikarti.SmallBangListener;

import java.text.DecimalFormat;
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

public class DashAddsAdapter extends RecyclerView.Adapter<DashAddsAdapter.MyViewHolder> {

Activity context;
    List<Product>  productList;
    String uid;

    public DashAddsAdapter(Activity activity,List<Product> records,String uid) {
this.context=activity;
        this.productList=records;
        this.uid=uid;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener{
        ImageView image;
        TextView title;
        TextView price;
        TextView cutprice;
        TextView discount;
        TextView ratingtex;
        ImageView fav1, fav2;
        RatingBar ratingbar;
        private SmallBang mSmallBang;
        LinearLayout lay;
        public MyViewHolder(View view) {
            super(view);
title=view.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            cutprice = (TextView) itemView.findViewById(R.id.cutprice);
            discount = (TextView) itemView.findViewById(R.id.discount);
            ratingtex = (TextView) itemView.findViewById(R.id.ratingtext);

            lay = (LinearLayout) itemView.findViewById(R.id.lay);


            cutprice.setPaintFlags(cutprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

//        ***********ratingBar**********
            ratingbar = (RatingBar) itemView.findViewById(R.id.ratingbar);
            LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#f8d64e"), PorterDuff.Mode.SRC_ATOP);

            fav1 = (ImageView) itemView.findViewById(R.id.fav1);
            fav2 = (ImageView) itemView.findViewById(R.id.fav2);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("record",productList.get(getLayoutPosition()));
            Intent mainIntent = new Intent(context, ProductDetailsActivity.class);
            //add bundle to intent
            mainIntent.putExtras(bundle);
            //start activity
            context.startActivity(mainIntent);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addview, parent, false);

        return new MyViewHolder(itemView);
    }
    public static String priceToRupees(Double price) {
        DecimalFormat formatter = new DecimalFormat("##,##,###.00");
        return formatter.format(price);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

    holder.title.setText(productList.get(position).getProductName());
        holder.title.setText(productList.get(position).getProductName());
        holder.price.setText(priceToRupees(Double.parseDouble(productList.get(position).getProductPrice()+"")));
/*
        Picasso.with(context)
                .load("http://192.168.1.2/deals/images/151344360373961.jpg")
                //.placeholder(R.drawable.user_placeholder)
                //.error(R.drawable.user_placeholder_error)
                .into(holder.image);
                */

        holder.mSmallBang = SmallBang.attach2Window(context);

        holder.fav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.fav2.setVisibility(View.VISIBLE);
                holder.fav1.setVisibility(View.GONE);
                like(v);
                likeProduct(productList.get(position).getProductId(),uid);

             //   Toast.makeText(context,"like",Toast.LENGTH_SHORT).show();
            }

            public void like(View view){
                holder.fav2.setImageResource(R.drawable.f4);
                holder.mSmallBang.bang(view);
                holder.mSmallBang.setmListener(new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {

                    }


                });
            }

        });

        holder.fav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
unlikeProduct(productList.get(position).getProductId(),uid);
                holder.fav2.setVisibility(View.GONE);
                holder.fav1.setVisibility(View.VISIBLE);
              //  Toast.makeText(context,"unlike",Toast.LENGTH_SHORT).show();


            }
        });

        Pic pic=productList.get(position).getPics().get(0);
        Glide.with(context).
                load(context.getResources().getString(R.string.base_url)+"deals/images/"+pic.getPicPath())
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .crossFade()
                .into(holder.image);
      /*  Glide
                .with(context)
                .load(context.getResources().getString(R.string.base_url)+"deals/images/"+pic.getPicPath())
                .centerCrop()
                .error(R.drawable.cloth)
                .placeholder(R.drawable.loading)
                .into(holder.image);*/

    }

    private void likeProduct(String productId, String s) {
        RequestBody Id = RequestBody.create(MediaType.parse("text/plain"), productId);
        RequestBody Uid = RequestBody.create(MediaType.parse("text/plain"), s);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        AddFavouriteApi service = new Retrofit.Builder().baseUrl(context.getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(AddFavouriteApi.class);

        retrofit2.Call<MyPojo> mService = service.add(Uid,Id);
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                MyPojo obj = response.body();
               if (obj.getStatus())
                    // clearFields();
               {

               }    //Toast.makeText(context, "" + obj.getMessage(), Toast.LENGTH_SHORT).show();
                else                     Toast.makeText(context, "" + obj.getMessage(), Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void unlikeProduct(String productId, String s) {
        RequestBody Id = RequestBody.create(MediaType.parse("text/plain"), productId);
        RequestBody Uid = RequestBody.create(MediaType.parse("text/plain"), s);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        DeleteFavouriteApi service = new Retrofit.Builder().baseUrl(context.getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(DeleteFavouriteApi.class);

        retrofit2.Call<MyPojo> mService = service.add(Id,Uid);
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                MyPojo obj = response.body();
                if (obj.getStatus())
                    // clearFields();
                {}//  Toast.makeText(context, "" + obj.getMessage(), Toast.LENGTH_SHORT).show();
                else    Toast.makeText(context, "" + obj.getMessage(), Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


}
