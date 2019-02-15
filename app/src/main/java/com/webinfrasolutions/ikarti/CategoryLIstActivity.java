package com.webinfrasolutions.ikarti;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;

import com.webinfrasolutions.ikarti.API.GetProductsByCat;
import com.webinfrasolutions.ikarti.API.MyProductListApi;
import com.webinfrasolutions.ikarti.Adapters.CustomAdapter;
import com.webinfrasolutions.ikarti.Adapters.RecyclerItemListener;
import com.webinfrasolutions.ikarti.Adapters.Utility;
import com.webinfrasolutions.ikarti.Pojo.MyproductsListPojo;
import com.webinfrasolutions.ikarti.Pojo.Product;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryLIstActivity extends AppCompatActivity{
  UltimateRecyclerView ultimateRecyclerView;
    CustomAdapter customAdapter = null;
    GridLayoutManager gridLayoutManager;
    int moreNum = 2;
    List<Product> myproductlist=new ArrayList<>();

    private ItemTouchHelper mItemTouchHelper;
    List<String> stringList = new ArrayList<>();
    public void goBack(View view) {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_list);
        TextView  title=(TextView)findViewById(R.id.toolbar_title);
        title.setText(getIntent().getStringExtra("cat"));
      //  initList();
        ultimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.ultimate_recycler_view);
        ultimateRecyclerView.setHasFixedSize(false);

       // linearLayoutManager = new LinearLayoutManager(this);
        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());
      gridLayoutManager=new  GridLayoutManager(this, mNoOfColumns);
      //  ultimateRecyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));

      ultimateRecyclerView.setLayoutManager(gridLayoutManager);
fetchList();
       // ultimateRecyclerView.enableLoadmore(fal);

      //  addCustomLoaderView();

        ultimateRecyclerView.setRecylerViewBackgroundColor(Color.parseColor("#f1f2f4"));

    //swipeRefresh();
       // dragList();
       // infinite_Insertlist();

    }

    public void addCustomLoaderView(){
        customAdapter.setCustomLoadMoreView(LayoutInflater.from(this)
                .inflate(R.layout.custom_bottom_progressbar, null));
    }

    public void dragList(){
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(customAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(ultimateRecyclerView.mRecyclerView);
        customAdapter.setOnDragStartListener(new CustomAdapter.OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });
    }

    public  void infinite_Insertlist(){
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //customAdapter.insert("More " + moreNum++, customAdapter.getAdapterItemCount());
                        //customAdapter.insert("More " + moreNum++, customAdapter.getAdapterItemCount());
                       // customAdapter.insert("More " + moreNum++, customAdapter.getAdapterItemCount());
                    }
                }, 1000);
            }
        });
    }

    public void swipeRefresh(){
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       // customAdapter.insert(moreNum++ + "  Refresh things", 0);
                        ultimateRecyclerView.setRefreshing(false);
                        gridLayoutManager.scrollToPosition(0);

                    }
                }, 1000);
            }
        });

    }

    public void goToCart(View view) {
        Intent i=new Intent(CategoryLIstActivity.this,CartActivity.class);
        startActivity(i);
    }

    public void fetchList(){

        RequestBody catId = RequestBody.create(MediaType.parse("text/plain"), getIntent().getExtras().getString("catId"));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        GetProductsByCat service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(GetProductsByCat.class);


        retrofit2. Call<MyproductsListPojo> mService = service.fetch(catId);
        mService.enqueue(new Callback<MyproductsListPojo>() {
            @Override
            public void onResponse(Call<MyproductsListPojo> call, Response<MyproductsListPojo> response) {

                if (response.body().getStatus())
                    // clearFields();
                    myproductlist=response.body().getProduct();
                ultimateRecyclerView.setAdapter(new CustomAdapter(myproductlist,CategoryLIstActivity.this));

                Toast.makeText(CategoryLIstActivity.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<MyproductsListPojo> call, Throwable t) {
                Toast.makeText(CategoryLIstActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}
