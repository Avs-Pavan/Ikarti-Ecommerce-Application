package com.webinfrasolutions.ikarti;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.viewpagerindicator.CirclePageIndicator;
import com.webinfrasolutions.ikarti.API.AddToCartApi;
import com.webinfrasolutions.ikarti.API.FetchSimilarAndOtherApi;
import com.webinfrasolutions.ikarti.Adapters.MyAdapter;
import com.webinfrasolutions.ikarti.Adapters.PeopleViewedRecyclerViewAdapter;
import com.webinfrasolutions.ikarti.Adapters.SimilarAndOthersProductsAdapter;
import com.webinfrasolutions.ikarti.Adapters.SlidingImage_Adapter;
import com.webinfrasolutions.ikarti.Models.BeanlistPeopleViewed;
import com.webinfrasolutions.ikarti.Pojo.DashPojo;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.OtherAndSimilarPojo;
import com.webinfrasolutions.ikarti.Pojo.Pic;
import com.webinfrasolutions.ikarti.Pojo.Product;
import com.webinfrasolutions.ikarti.Pojo.Record;
import com.webinfrasolutions.ikarti.Pojo.UserData;
import com.webinfrasolutions.ikarti.util.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

public class ProductDetailsActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    int POSITION=0;
    ImageView plus,minus;
    TextView cartno,cutprice,pricetv,destv;
    RatingBar ratingbar;
    RecyclerView recyclerView;
    MyTextView title;
    Product  pro;
    MyTextView addtocart;
    public void goToCart(View view) {
        Intent i=new Intent(ProductDetailsActivity.this,CartActivity.class);
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
title=findViewById(R.id.title);
destv=findViewById(R.id.des);
        recyclerView    = (RecyclerView) findViewById(R.id.rv2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addtocart=findViewById(R.id.addtocart);
intiView();



        try {
     pro=(Product) getIntent().getSerializableExtra("record");
title.setText(pro.getProductName());
            pricetv.setText(priceToRupees(Double.parseDouble(pro.getProductPrice()+"")));

            destv.setText("- "+pro.getProductDescription());

initSlider(pro.getPics());

          /*  Glide.with(this).
                    load(getResources().getString(R.string.base_url)+"deals/images/"+pic.getPicPath())
                    .thumbnail(Glide.with(this).load(R.drawable.loading))
                    .crossFade()
                    .into(imageView);
                    */
            fetchOtherAndSimilar();


        }catch (NullPointerException ne){
            ne.printStackTrace();
        }



    }

    private void initSlider(List<Pic> pics) {


        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(ProductDetailsActivity.this,pics));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(3 * density);

        NUM_PAGES =pics.size();
        mPager.setCurrentItem(POSITION);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    public static String priceToRupees(Double price) {
        DecimalFormat formatter = new DecimalFormat("##,##,###.00");
        return formatter.format(price);
    }

   public void intiView(){
        pricetv = (TextView) findViewById(R.id.price_tv);

//*******Text thru Line********
        cutprice = (TextView) findViewById(R.id.cutprice);
        cutprice.setPaintFlags(cutprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

//********PRODUCT PLUS MINUS*************
        plus = (ImageView)findViewById(R.id.plus);
        minus = (ImageView)findViewById(R.id.minus);
        cartno = (TextView) findViewById(R.id.cartno);

        final int[] number = {0};
        cartno.setText(""+ number[0]);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number[0] == 0){
                    cartno.setText("" + number[0]);
                }

                if (number[0] > 0){

                    number[0] = number[0] -1;
                    cartno.setText(""+ number[0]);
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number[0] == 9) {
                    cartno.setText("" + number[0]);
                }

                if (number[0] < 9) {

                    number[0] = number[0] + 1;
                    cartno.setText("" + number[0]);

                }
            }
        });

//        ***********ratingBar**********
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);


        //        *********POPUP*************

     //   changeno.setText("000000");
       // changeno.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
/*
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                myDialog1 = new Dialog(ProductDetailsActivity.this);
                myDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                myDialog1.setCancelable(true);
                myDialog1.setContentView(R.layout.productdetails_popup_change);
                myDialog1.show();

                numberpopup = (EditText) myDialog1.findViewById(R.id.numberpopup);

                TextView button = (TextView) myDialog1.findViewById(R.id.button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(numberpopup.getText().toString().equalsIgnoreCase("")||numberpopup.getText().toString().equalsIgnoreCase(null)) {

                            Toast.makeText(ProductDetailsActivity.this,"write a code",Toast.LENGTH_LONG).show();

                        }

                        else{


                            changeno.setText(numberpopup.getText().toString());
                            myDialog1.dismiss();


                        }
                    }
                });
            }
        });
*/

    }

    public void goBack(View view) {
super.onBackPressed();
    }

    public void addToCart(View view) {

        RequestBody Id = RequestBody.create(MediaType.parse("text/plain"), pro.getProductId());
        UserData userData=new SessionManager().getUserData(ProductDetailsActivity.this);
        RequestBody Uid = RequestBody.create(MediaType.parse("text/plain"), userData.getUid());


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        AddToCartApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(AddToCartApi.class);

        retrofit2.Call<MyPojo> mService = service.add(Id,Uid);
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                MyPojo obj = response.body();
                    // clearFields();
                if (response.body().getStatus())
                    addtocart.setVisibility(View.GONE);
                Toast.makeText(ProductDetailsActivity.this, "" + obj.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public  void fetchOtherAndSimilar(){
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), pro.getProductName());
       final UserData userData=new SessionManager().getUserData(ProductDetailsActivity.this);
        RequestBody sid = RequestBody.create(MediaType.parse("text/plain"), pro.getStoreId());


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        FetchSimilarAndOtherApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchSimilarAndOtherApi.class);

        retrofit2.Call<DashPojo> mService = service.fetch(sid,name);
        mService.enqueue(new Callback<DashPojo>() {
            @Override
            public void onResponse(Call<DashPojo> call, Response<DashPojo> response) {

                SimilarAndOthersProductsAdapter adapter = new SimilarAndOthersProductsAdapter(ProductDetailsActivity.this,ProductDetailsActivity.this, response.body().getRecords(),userData.getUid());

                recyclerView.setAdapter(adapter);

                //Toast.makeText(ProductDetailsActivity.this,""+response.body().getRecords(), Toast.LENGTH_SHORT).show();



            }


            @Override
            public void onFailure(Call<DashPojo> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
