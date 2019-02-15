package com.webinfrasolutions.ikarti;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.webinfrasolutions.ikarti.API.MyProductListApi;
import com.webinfrasolutions.ikarti.Adapters.DashAddsAdapter;
import com.webinfrasolutions.ikarti.Adapters.SlidingImage_Adapter;
import com.webinfrasolutions.ikarti.Adapters.Utility;
import com.webinfrasolutions.ikarti.Pojo.MyproductsListPojo;
import com.webinfrasolutions.ikarti.Pojo.Product;
import com.webinfrasolutions.ikarti.Pojo.UserData;
import com.webinfrasolutions.ikarti.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfferDetailsActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    int POSITION=0;
    int food[]={
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food, R.drawable.food,
            R.drawable.food,
            R.drawable.food, R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food, R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,
            R.drawable.food,

    };RecyclerView rv;
    public void goBack(View view) {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        initSlider();initList();
    }
    private void initSlider() {

        int urls[]={
                R.drawable.shoppy_logo,
                R.drawable.shoppy_logo,
                R.drawable.shoppy_logo,
                R.drawable.shoppy_logo,
                R.drawable.shoppy_logo,
        };

        mPager = (ViewPager) findViewById(R.id.pager);


        //mPager.setAdapter(new SlidingImage_Adapter(OfferDetailsActivity.this,food));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

     //   indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(3 * density);

        NUM_PAGES =food.length;
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
        }, 3000, 1500);

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

    private void initList() {
       rv=(RecyclerView)findViewById(R.id.cat_rv);
        rv.setHasFixedSize(true);

        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());
        rv.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
        fetchList();
    }

    public void goToCart(View view) {
        Intent i=new Intent(OfferDetailsActivity.this,CartActivity.class);
        startActivity(i);
    }

    List<Product> myproductlist=new ArrayList<>();

    public void fetchList(){

        RequestBody StoreID = RequestBody.create(MediaType.parse("text/plain"), "1");
        final UserData userData=new SessionManager().getUserData(OfferDetailsActivity.this);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        MyProductListApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(MyProductListApi.class);


        retrofit2. Call<MyproductsListPojo> mService = service.fetch(StoreID);
        mService.enqueue(new Callback<MyproductsListPojo>() {
            @Override
            public void onResponse(Call<MyproductsListPojo> call, Response<MyproductsListPojo> response) {

                if (response.body().getStatus())
                    // clearFields();
                    myproductlist=response.body().getProduct();
     rv.setAdapter(new DashAddsAdapter(OfferDetailsActivity.this,myproductlist,userData.getUid()));
     //  rv.setAdapter(new CatListAdapter(OfferDetailsActivity.this,OfferDetailsActivity.this,food));

                Toast.makeText(OfferDetailsActivity.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<MyproductsListPojo> call, Throwable t) {
                Toast.makeText(OfferDetailsActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}
